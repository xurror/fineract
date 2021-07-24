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
package org.apache.fineract.portfolio.creditscorecard.serialization;

import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.apache.fineract.infrastructure.core.data.ApiParameterError;
import org.apache.fineract.infrastructure.core.data.DataValidatorBuilder;
import org.apache.fineract.infrastructure.core.exception.InvalidJsonException;
import org.apache.fineract.infrastructure.core.serialization.FromJsonHelper;
import org.apache.fineract.portfolio.creditscorecard.domain.FeatureCategory;
import org.apache.fineract.portfolio.creditscorecard.domain.FeatureDataType;
import org.apache.fineract.portfolio.creditscorecard.domain.FeatureValueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreditScorecardApiJsonDeserializer {

    private final Set<String> supportedParameters = new HashSet<>(
            Arrays.asList("name", "valueType", "dataType", "category", "active", "locale"));

    private final FromJsonHelper fromJsonHelper;

    @Autowired
    public CreditScorecardApiJsonDeserializer(final FromJsonHelper fromJsonHelper) {
        this.fromJsonHelper = fromJsonHelper;
    }

    public void validateFeatureForCreate(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromJsonHelper.checkForUnsupportedParameters(typeOfMap, json, this.supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("credit_scorecard_feature");

        final JsonElement element = this.fromJsonHelper.parse(json);

        final String name = this.fromJsonHelper.extractStringNamed("name", element);
        baseDataValidator.reset().parameter("name").value(name).notBlank().notExceedingLengthOf(100);

        final Integer valueType = this.fromJsonHelper.extractIntegerSansLocaleNamed("valueType", element);
        baseDataValidator.reset().parameter("valueType").value(valueType).notNull();
        if (valueType != null) {
            baseDataValidator.reset().parameter("valueType").value(valueType).isOneOfTheseValues(FeatureValueType.validValues());
        }

        final Integer dataType = this.fromJsonHelper.extractIntegerSansLocaleNamed("dataType", element);
        baseDataValidator.reset().parameter("dataType").value(dataType).notNull();
        if (valueType != null) {
            baseDataValidator.reset().parameter("dataType").value(dataType).isOneOfTheseValues(FeatureDataType.validValues());
        }

        final Integer category = this.fromJsonHelper.extractIntegerSansLocaleNamed("category", element);
        baseDataValidator.reset().parameter("category").value(category).notNull();
        if (valueType != null) {
            baseDataValidator.reset().parameter("category").value(category).isOneOfTheseValues(FeatureCategory.validValues());
        }

        if (this.fromJsonHelper.parameterExists("active", element)) {
            final Boolean active = this.fromJsonHelper.extractBooleanNamed("active", element);
            baseDataValidator.reset().parameter("active").value(active).notNull();
        }

    }

    public void validateFeatureForUpdate(final String json) {
        if (StringUtils.isBlank(json)) {
            throw new InvalidJsonException();
        }

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();
        this.fromJsonHelper.checkForUnsupportedParameters(typeOfMap, json, this.supportedParameters);

        final List<ApiParameterError> dataValidationErrors = new ArrayList<>();
        final DataValidatorBuilder baseDataValidator = new DataValidatorBuilder(dataValidationErrors).resource("credit_scorecard_feature");

        final JsonElement element = this.fromJsonHelper.parse(json);

        final String name = this.fromJsonHelper.extractStringNamed("name", element);
        baseDataValidator.reset().parameter("name").value(name).notBlank().notExceedingLengthOf(100);

        final Integer valueType = this.fromJsonHelper.extractIntegerSansLocaleNamed("valueType", element);
        baseDataValidator.reset().parameter("valueType").value(valueType).notNull();
        if (valueType != null) {
            baseDataValidator.reset().parameter("valueType").value(valueType).isOneOfTheseValues(FeatureValueType.validValues());
        }

        final Integer dataType = this.fromJsonHelper.extractIntegerSansLocaleNamed("dataType", element);
        baseDataValidator.reset().parameter("dataType").value(dataType).notNull();
        if (valueType != null) {
            baseDataValidator.reset().parameter("dataType").value(dataType).isOneOfTheseValues(FeatureDataType.validValues());
        }

        final Integer category = this.fromJsonHelper.extractIntegerSansLocaleNamed("category", element);
        baseDataValidator.reset().parameter("category").value(category).notNull();
        if (valueType != null) {
            baseDataValidator.reset().parameter("category").value(category).isOneOfTheseValues(FeatureCategory.validValues());
        }

        if (this.fromJsonHelper.parameterExists("active", element)) {
            final Boolean active = this.fromJsonHelper.extractBooleanNamed("active", element);
            baseDataValidator.reset().parameter("active").value(active).notNull();
        }

    }
}
