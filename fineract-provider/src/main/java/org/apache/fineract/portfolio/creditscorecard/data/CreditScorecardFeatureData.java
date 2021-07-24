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
package org.apache.fineract.portfolio.creditscorecard.data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.portfolio.creditscorecard.domain.CreditScorecardFeature;
import org.apache.fineract.portfolio.creditscorecard.domain.ScorecardFeatureCriteria;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductScorecardFeature;
import org.jetbrains.annotations.NotNull;

public final class CreditScorecardFeatureData implements Comparable<CreditScorecardFeatureData>, Serializable {

    private final Long id;
    private final String name;
    private final EnumOptionData valueType;
    private final EnumOptionData dataType;
    private final EnumOptionData category;
    private final Boolean active;

    private final String featureValue;

    private final BigDecimal weightage;
    private final Integer greenMin;
    private final Integer greenMax;
    private final Integer amberMin;
    private final Integer amberMax;
    private final Integer redMin;
    private final Integer redMax;
    private final Collection<ScorecardFeatureCriteriaData> criteria;

    private final Collection<EnumOptionData> valueTypeOptions;
    private final Collection<EnumOptionData> dataTypeOptions;
    private final Collection<EnumOptionData> categoryOptions;

    private CreditScorecardFeatureData(Long id, String name, EnumOptionData valueType, EnumOptionData dataType, EnumOptionData category,
            Boolean active, Collection<EnumOptionData> valueTypeOptions, Collection<EnumOptionData> dataTypeOptions,
            Collection<EnumOptionData> categoryOptions) {
        this.id = id;
        this.name = name;
        this.valueType = valueType;
        this.dataType = dataType;
        this.category = category;
        this.active = active;

        this.featureValue = null;

        this.weightage = null;
        this.greenMin = null;
        this.greenMax = null;
        this.amberMin = null;
        this.amberMax = null;
        this.redMin = null;
        this.redMax = null;

        this.criteria = null;

        this.valueTypeOptions = valueTypeOptions;
        this.dataTypeOptions = dataTypeOptions;
        this.categoryOptions = categoryOptions;
    }

    private CreditScorecardFeatureData(Long id, String name, EnumOptionData valueType, EnumOptionData dataType, EnumOptionData category,
            Boolean active, final BigDecimal weightage, final Integer greenMin, final Integer greenMax, final Integer amberMin,
            final Integer amberMax, final Integer redMin, final Integer redMax) {
        this.id = id;
        this.name = name;
        this.valueType = valueType;
        this.dataType = dataType;
        this.category = category;
        this.active = active;

        this.featureValue = null;

        this.weightage = weightage;
        this.greenMin = greenMin;
        this.greenMax = greenMax;
        this.amberMin = amberMin;
        this.amberMax = amberMax;
        this.redMin = redMin;
        this.redMax = redMax;

        this.criteria = null;

        this.valueTypeOptions = null;
        this.dataTypeOptions = null;
        this.categoryOptions = null;
    }

    private CreditScorecardFeatureData(final Long id, final String name, final EnumOptionData valueType, final EnumOptionData dataType,
            final EnumOptionData category, final Boolean active, final BigDecimal weightage, final Integer greenMin, final Integer greenMax,
            final Integer amberMin, final Integer amberMax, final Integer redMin, final Integer redMax,
            final Collection<ScorecardFeatureCriteriaData> criteria) {
        this.id = id;
        this.name = name;
        this.valueType = valueType;
        this.dataType = dataType;
        this.category = category;
        this.active = active;

        this.featureValue = null;

        this.weightage = weightage;
        this.greenMin = greenMin;
        this.greenMax = greenMax;
        this.amberMin = amberMin;
        this.amberMax = amberMax;
        this.redMin = redMin;
        this.redMax = redMax;

        this.criteria = criteria;

        this.valueTypeOptions = null;
        this.dataTypeOptions = null;
        this.categoryOptions = null;
    }

    private CreditScorecardFeatureData(final Long id, final String name, final EnumOptionData valueType, final EnumOptionData dataType,
            final EnumOptionData category, final Boolean active, final BigDecimal weightage, final Integer greenMin, final Integer greenMax,
            final Integer amberMin, final Integer amberMax, final Integer redMin, final Integer redMax,
            final Collection<ScorecardFeatureCriteriaData> criteria, final String featureValue) {
        this.id = id;
        this.name = name;
        this.valueType = valueType;
        this.dataType = dataType;
        this.category = category;
        this.active = active;

        this.featureValue = featureValue;

        this.weightage = weightage;
        this.greenMin = greenMin;
        this.greenMax = greenMax;
        this.amberMin = amberMin;
        this.amberMax = amberMax;
        this.redMin = redMin;
        this.redMax = redMax;

        this.criteria = criteria;

        this.valueTypeOptions = null;
        this.dataTypeOptions = null;
        this.categoryOptions = null;
    }

    public static CreditScorecardFeatureData instance(Long id, String name, EnumOptionData valueType, EnumOptionData dataType,
            EnumOptionData category, Boolean active) {
        return new CreditScorecardFeatureData(id, name, valueType, dataType, category, active, null, null, null);
    }

    public static CreditScorecardFeatureData template(Collection<EnumOptionData> valueTypeOptions,
            Collection<EnumOptionData> dataTypeOptions, Collection<EnumOptionData> categoryOptions) {
        return new CreditScorecardFeatureData(null, null, null, null, null, null, valueTypeOptions, dataTypeOptions, categoryOptions);
    }

    public static CreditScorecardFeatureData withTemplate(CreditScorecardFeatureData scorecardFeature,
            CreditScorecardFeatureData template) {
        return new CreditScorecardFeatureData(scorecardFeature.id, scorecardFeature.name, scorecardFeature.valueType,
                scorecardFeature.dataType, scorecardFeature.category, scorecardFeature.active, template.valueTypeOptions,
                template.dataTypeOptions, template.categoryOptions);
    }

    public static CreditScorecardFeatureData loanProductFeatureInstance(final CreditScorecardFeature scf, final BigDecimal weightage,
            final Integer greenMin, final Integer greenMax, final Integer amberMin, final Integer amberMax, final Integer redMin,
            final Integer redMax, final List<ScorecardFeatureCriteria> scorecardFeatureCriteria) {

        final List<ScorecardFeatureCriteriaData> criteriaData = scorecardFeatureCriteria.stream().map(ScorecardFeatureCriteria::toData)
                .collect(Collectors.toList());
        return new CreditScorecardFeatureData(scf.getId(), scf.getName(), scf.getValueType(), scf.getDataType(), scf.getCategory(),
                scf.isActive(), weightage, greenMin, greenMax, amberMin, amberMax, redMin, redMax, criteriaData);
    }

    public static CreditScorecardFeatureData loanFeatureInstance(final LoanProductScorecardFeature lpscf, final String featureValue) {

        final CreditScorecardFeature scf = lpscf.getScorecardFeature();

        final Collection<ScorecardFeatureCriteriaData> criteriaData = lpscf.getFeatureCriteria().stream()
                .map(ScorecardFeatureCriteria::toData).collect(Collectors.toList());
        return new CreditScorecardFeatureData(scf.getId(), scf.getName(), scf.getValueType(), scf.getDataType(), scf.getCategory(),
                scf.isActive(), lpscf.getWeightage(), lpscf.getGreenMin(), lpscf.getGreenMax(), lpscf.getAmberMin(), lpscf.getAmberMax(),
                lpscf.getRedMin(), lpscf.getRedMax(), criteriaData, featureValue);
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof CreditScorecardFeatureData)) {
            return false;
        }
        final CreditScorecardFeatureData creditScorecardFeatureData = (CreditScorecardFeatureData) obj;
        return this.id.equals(creditScorecardFeatureData.id);
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public int compareTo(@NotNull CreditScorecardFeatureData obj) {
        return obj.id.compareTo(this.id);
    }
}
