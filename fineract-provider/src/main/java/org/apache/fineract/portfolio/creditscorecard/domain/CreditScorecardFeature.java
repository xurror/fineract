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
package org.apache.fineract.portfolio.creditscorecard.domain;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.portfolio.creditscorecard.data.CreditScorecardFeatureData;
import org.apache.fineract.portfolio.creditscorecard.service.CreditScorecardEnumerations;

@Entity
@Table(name = "m_credit_scorecard_feature")
public class CreditScorecardFeature extends AbstractPersistableCustom implements Serializable {

    @Column(name = "name", length = 100, unique = true, nullable = false)
    private String name;

    @Column(name = "value_type_enum", nullable = false)
    private Integer valueType;

    @Column(name = "data_type_enum", nullable = false)
    private Integer dataType;

    @Column(name = "category_enum", nullable = false)
    private Integer category;

    @Column(name = "is_active", nullable = false)
    private Boolean active = false;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    public CreditScorecardFeature() {
        //
    }

    public CreditScorecardFeature(String name, FeatureValueType valueType, FeatureDataType dataType, FeatureCategory category,
            Boolean active) {
        this.name = name;
        this.valueType = valueType.getValue();
        this.dataType = dataType.getValue();
        this.category = category.getValue();
        this.active = active;
    }

    public static CreditScorecardFeature fromJson(JsonCommand command) {
        final String name = command.stringValueOfParameterNamed("name");
        final FeatureValueType valueType = FeatureValueType.fromInt(command.integerValueOfParameterNamed("valueType"));
        final FeatureDataType dataType = FeatureDataType.fromInt(command.integerValueOfParameterNamed("dataType"));
        final FeatureCategory category = FeatureCategory.fromInt(command.integerValueOfParameterNamed("category"));
        final boolean active = command.booleanPrimitiveValueOfParameterNamed("active");

        return new CreditScorecardFeature(name, valueType, dataType, category, active);
    }

    public String getName() {
        return this.name;
    }

    public EnumOptionData getValueType() {
        return CreditScorecardEnumerations.featureValueType(valueType);
    }

    public EnumOptionData getDataType() {
        return CreditScorecardEnumerations.featureDataType(dataType);
    }

    public EnumOptionData getCategory() {
        return CreditScorecardEnumerations.featureCategory(category);
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    /**
     * Delete is a <i>soft delete</i>. Updates flag on feature so it wont appear in query/report results.
     *
     * Any fields with unique constraints and prepended with id of record.
     */
    public void delete() {
        this.deleted = true;
        this.name = getId() + "_" + this.name;
    }

    public CreditScorecardFeatureData toData() {
        final EnumOptionData valueType = CreditScorecardEnumerations.featureValueType(this.valueType);
        final EnumOptionData dataType = CreditScorecardEnumerations.featureDataType(this.dataType);
        final EnumOptionData category = CreditScorecardEnumerations.featureCategory(this.category);

        return CreditScorecardFeatureData.instance(null, getId(), this.name, valueType, dataType, category, this.active, null, null, null,
                null, null, null, null);
    }

    public Map<String, Object> update(final JsonCommand command) {
        final Map<String, Object> actualChanges = new LinkedHashMap<>(6);

        final String nameParamName = "name";
        if (command.isChangeInStringParameterNamed(nameParamName, this.name)) {
            final String newValue = command.stringValueOfParameterNamed(nameParamName);
            actualChanges.put(nameParamName, newValue);
            this.name = newValue;
        }

        final String valueTypeParamName = "valueType";
        if (command.isChangeInIntegerParameterNamed(valueTypeParamName, this.valueType)) {
            final Integer newValue = command.integerValueOfParameterNamed(valueTypeParamName);
            actualChanges.put(valueTypeParamName, newValue);
            this.valueType = newValue;
        }

        final String dataTypeParamName = "dataType";
        if (command.isChangeInIntegerParameterNamed(dataTypeParamName, this.dataType)) {
            final Integer newValue = command.integerValueOfParameterNamed(dataTypeParamName);
            actualChanges.put(dataTypeParamName, newValue);
            this.dataType = newValue;
        }

        final String categoryParamName = "category";
        if (command.isChangeInIntegerParameterNamed(categoryParamName, this.category)) {
            final Integer newValue = command.integerValueOfParameterNamed(categoryParamName);
            actualChanges.put(categoryParamName, newValue);
            this.category = newValue;
        }

        final String activeParamName = "active";
        if (command.isChangeInBooleanParameterNamed(activeParamName, this.active)) {
            final boolean newValue = command.booleanPrimitiveValueOfParameterNamed(activeParamName);
            actualChanges.put(activeParamName, newValue);
            this.active = newValue;
        }

        return actualChanges;
    }

}
