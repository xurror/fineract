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
package org.apache.fineract.portfolio.creditscorecard.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.creditscorecard.domain.CreditScorecard;
import org.apache.fineract.portfolio.creditscorecard.domain.FeatureCriteriaScore;
import org.apache.fineract.portfolio.creditscorecard.domain.MLScorecard;
import org.apache.fineract.portfolio.creditscorecard.domain.MLScorecardFields;
import org.apache.fineract.portfolio.creditscorecard.domain.RuleBasedScorecard;
import org.apache.fineract.portfolio.creditscorecard.domain.StatScorecard;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductScorecardFeature;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductScorecardFeatureRepositoryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreditScorecardAssembler {

    private final FromJsonHelper fromApiJsonHelper;
    private final LoanProductScorecardFeatureRepositoryWrapper productFeatureRepository;

    @Autowired
    public CreditScorecardAssembler(final FromJsonHelper fromApiJsonHelper,
            final LoanProductScorecardFeatureRepositoryWrapper productFeatureRepository) {
        this.fromApiJsonHelper = fromApiJsonHelper;
        this.productFeatureRepository = productFeatureRepository;
    }

    public CreditScorecard assembleFrom(final JsonElement element) {
        CreditScorecard creditScorecard = null;

        if (element.isJsonObject()) {

            final String scoringMethod = this.fromApiJsonHelper.extractStringNamed("scoringMethod", element);
            final String scoringModel = this.fromApiJsonHelper.extractStringNamed("scoringModel", element);

            RuleBasedScorecard ruleBasedScorecard = null;
            StatScorecard statScorecard = null;
            MLScorecard mlScorecard = null;

            if (scoringMethod.equalsIgnoreCase("ruleBased")) {
                ruleBasedScorecard = this.assembleRuleBasedScorecard(element);
            }

            if (scoringMethod.equalsIgnoreCase("ml")) {
                mlScorecard = this.assembleMLScorecard(element);
            }

            if (scoringMethod.equalsIgnoreCase("statistical")) {
                statScorecard = null;
            }

            creditScorecard = new CreditScorecard(scoringMethod, scoringModel, ruleBasedScorecard, statScorecard, mlScorecard);
        }

        return creditScorecard;
    }

    public RuleBasedScorecard assembleRuleBasedScorecard(final JsonElement element) {

        final RuleBasedScorecard ruleBasedScorecard = new RuleBasedScorecard();

        final List<FeatureCriteriaScore> criteriaScores = new ArrayList<>();

        if (element.isJsonObject()) {
            final JsonObject topLevelJsonElement = element.getAsJsonObject();

            final String dateFormat = this.fromApiJsonHelper.extractDateFormatParameter(topLevelJsonElement);
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);

            final String rbScorecardParameterName = "ruleBasedScorecard";
            if (topLevelJsonElement.has(rbScorecardParameterName) && topLevelJsonElement.get(rbScorecardParameterName).isJsonObject()) {
                final JsonObject rbScorecardElement = topLevelJsonElement.getAsJsonObject(rbScorecardParameterName);

                final String criteriaScoresParameterName = "criteriaScores";
                if (rbScorecardElement.has(criteriaScoresParameterName)
                        && rbScorecardElement.get(criteriaScoresParameterName).isJsonArray()) {
                    final JsonArray array = rbScorecardElement.get(criteriaScoresParameterName).getAsJsonArray();
                    for (int i = 0; i < array.size(); i++) {

                        final JsonObject criteriaScoreElement = array.get(i).getAsJsonObject();

                        final Long featureId = this.fromApiJsonHelper.extractLongNamed("featureId", criteriaScoreElement);
                        final String value = this.fromApiJsonHelper.extractStringNamed("value", criteriaScoreElement);

                        final LoanProductScorecardFeature lpScorecardFeature = this.productFeatureRepository.findOneWithNotFoundDetection(featureId);

                        criteriaScores.add(new FeatureCriteriaScore(lpScorecardFeature, value));

                    }

                } else {
                    return null;

                }
            }
        }

        ruleBasedScorecard.setCriteriaScores(criteriaScores);

        return ruleBasedScorecard;
    }

    public MLScorecard assembleMLScorecard(final JsonElement element) {

        MLScorecard mlScorecard = null;

        if (element.isJsonObject()) {

            final JsonObject topLevelJsonElement = element.getAsJsonObject();

            final String dateFormat = this.fromApiJsonHelper.extractDateFormatParameter(topLevelJsonElement);
            final Locale locale = this.fromApiJsonHelper.extractLocaleParameter(topLevelJsonElement);

            final String mlScorecardParameterName = "mlScorecard";
            if (topLevelJsonElement.has(mlScorecardParameterName) && topLevelJsonElement.get(mlScorecardParameterName).isJsonObject()) {
                final JsonObject scorecardDataElement = topLevelJsonElement.getAsJsonObject(mlScorecardParameterName);

                mlScorecard = new MLScorecard(
                        new MLScorecardFields(this.fromApiJsonHelper.extractIntegerWithLocaleNamed("age", scorecardDataElement),
                                this.fromApiJsonHelper.extractStringNamed("sex", scorecardDataElement),
                                this.fromApiJsonHelper.extractStringNamed("job", scorecardDataElement),
                                this.fromApiJsonHelper.extractStringNamed("housing", scorecardDataElement),
                                this.fromApiJsonHelper.extractBigDecimalNamed("creditAmount", scorecardDataElement, locale),
                                this.fromApiJsonHelper.extractIntegerNamed("duration", scorecardDataElement, locale),
                                this.fromApiJsonHelper.extractStringNamed("purpose", scorecardDataElement)));
            }
        }
        return mlScorecard;
    }

}
