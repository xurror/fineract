/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.portfolio.loanaccount.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.creditscorecard.domain.CreditScorecard;
import org.apache.fineract.portfolio.creditscorecard.domain.MLScorecard;
import org.apache.fineract.portfolio.creditscorecard.domain.MLScorecardFields;
import org.apache.fineract.portfolio.creditscorecard.exception.FeatureNotFoundException;
import org.apache.fineract.portfolio.loanaccount.domain.LoanScorecardFeature;
import org.apache.fineract.portfolio.loanaccount.domain.LoanScorecardFeatureRepository;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductRepository;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductScorecardFeature;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductScorecardFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanScorecardAssembler {

    private final FromJsonHelper fromApiJsonHelper;
    private final LoanProductRepository loanProductRepository;
    private final LoanScorecardFeatureRepository loanScorecardFeatureRepository;
    private final LoanProductScorecardFeatureRepository loanProductScorecardFeatureRepository;

    @Autowired
    public LoanScorecardAssembler(final FromJsonHelper fromApiJsonHelper, final LoanProductRepository loanProductRepository,
            final LoanScorecardFeatureRepository loanScorecardFeatureRepository,
            final LoanProductScorecardFeatureRepository loanProductScorecardFeatureRepository) {
        this.fromApiJsonHelper = fromApiJsonHelper;
        this.loanProductRepository = loanProductRepository;
        this.loanScorecardFeatureRepository = loanScorecardFeatureRepository;
        this.loanProductScorecardFeatureRepository = loanProductScorecardFeatureRepository;
    }

    public Set<LoanScorecardFeature> loanScorecardFeatureFromParsedJson(final JsonElement element) {

        final Set<LoanScorecardFeature> loanScorecardFeatures = new HashSet<>();
        // final Long productId = this.fromApiJsonHelper.extractLongNamed("productId", element);
        // final LoanProduct loanProduct = this.loanProductRepository.findById(productId)
        // .orElseThrow(() -> new LoanProductNotFoundException(productId));

        if (element.isJsonObject()) {

            final JsonObject topLevelJsonElement = element.getAsJsonObject();
            final String dateFormat = this.fromApiJsonHelper.extractDateFormatParameter(topLevelJsonElement);
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);

            if (topLevelJsonElement.has("scoringData") && topLevelJsonElement.get("scoringData").isJsonArray()) {
                final JsonArray array = topLevelJsonElement.get("scoringData").getAsJsonArray();
                for (int i = 0; i < array.size(); i++) {

                    final JsonObject scorecardDataElement = array.get(i).getAsJsonObject();

                    final Long id = this.fromApiJsonHelper.extractLongNamed("id", scorecardDataElement);
                    final String value = this.fromApiJsonHelper.extractStringNamed("value", scorecardDataElement);

                    LoanProductScorecardFeature loanProductScorecardFeature = this.loanProductScorecardFeatureRepository.findById(id)
                            .orElseThrow(() -> new FeatureNotFoundException(id));

                    loanScorecardFeatures.add(new LoanScorecardFeature(loanProductScorecardFeature, value));

                }
            }
        }

        return loanScorecardFeatures;
    }

    public MLScorecard mlScorecardFieldsFromParsedJson(final JsonElement element) {

        MLScorecard mlScorecard = null;
        // final Long productId = this.fromApiJsonHelper.extractLongNamed("productId", element);
        // final LoanProduct loanProduct = this.loanProductRepository.findById(productId)
        // .orElseThrow(() -> new LoanProductNotFoundException(productId));

        if (element.isJsonObject()) {

            final JsonObject topLevelJsonElement = element.getAsJsonObject();
            final String dateFormat = this.fromApiJsonHelper.extractDateFormatParameter(topLevelJsonElement);
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);

            if (topLevelJsonElement.has("scoringData") && topLevelJsonElement.get("scoringData").isJsonObject()) {
                final JsonObject scorecardDataElement = topLevelJsonElement.getAsJsonObject("scoringData");

                mlScorecard = new MLScorecard(
                        new MLScorecardFields(this.fromApiJsonHelper.extractIntegerWithLocaleNamed("age", scorecardDataElement),
                                this.fromApiJsonHelper.extractStringNamed("sex", scorecardDataElement),
                                this.fromApiJsonHelper.extractStringNamed("job", scorecardDataElement),
                                this.fromApiJsonHelper.extractStringNamed("housing", scorecardDataElement),
                                this.fromApiJsonHelper.extractBigDecimalWithLocaleNamed("creditAmount", scorecardDataElement),
                                this.fromApiJsonHelper.extractIntegerWithLocaleNamed("duration", scorecardDataElement),
                                this.fromApiJsonHelper.extractStringNamed("purpose", scorecardDataElement)));
            }
        }
        return mlScorecard;
    }

    public CreditScorecard scoringMethod(final JsonElement element) {
        CreditScorecard creditScorecard = null;

        if (element.isJsonObject()) {

            final JsonObject topLevelJsonElement = element.getAsJsonObject();

            if (topLevelJsonElement.has("scoringMethod") && topLevelJsonElement.get("scoringMethod").isJsonObject()) {
                final JsonObject scoringMethodElement = topLevelJsonElement.getAsJsonObject("scoringMethod");

                final String method = this.fromApiJsonHelper.extractStringNamed("method", scoringMethodElement);
                final String model = this.fromApiJsonHelper.extractStringNamed("model", scoringMethodElement);

                creditScorecard = new CreditScorecard(method, model);
            }
        }

        return creditScorecard;
    }

}
