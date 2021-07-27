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
package org.apache.fineract.portfolio.loanaccount.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.portfolio.creditscorecard.domain.ScorecardFeatureCriteria;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductScorecardFeature;

@Entity
@Table(name = "m_loan_scorecard_feature")
public class LoanScorecardFeature extends AbstractPersistableCustom implements Serializable {

    @OneToOne
    @JoinColumn(name = "loan_product_scorecard_feature_id", referencedColumnName = "id", nullable = false)
    private LoanProductScorecardFeature scorecardFeature;

    @Column(name = "feature_value", nullable = false)
    private String featureValue;

    @Column(name = "score")
    private BigDecimal score;

    @Column(name = "color")
    private String color;

    public LoanScorecardFeature() {
        //
    }

    public LoanScorecardFeature(LoanProductScorecardFeature scorecardFeature, String featureValue) {
        this.scorecardFeature = scorecardFeature;
        this.featureValue = featureValue;
    }

    public LoanProductScorecardFeature getScorecardFeature() {
        return scorecardFeature;
    }

    public String getFeatureValue() {
        return featureValue;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public EnumOptionData getFeatureValueType() {
        return this.scorecardFeature.getScorecardFeature().getValueType();
    }

    public List<ScorecardFeatureCriteria> getFeatureCriteria() {
        return this.scorecardFeature.getFeatureCriteria();
    }

    public String getFeatureName() {
        return this.scorecardFeature.getScorecardFeature().getName();
    }

    public String getColorFromScore(final BigDecimal score) {
        String color;
        if (score.longValue() >= this.scorecardFeature.getGreenMin().longValue()
                && score.longValue() <= this.scorecardFeature.getGreenMax().longValue()) {
            color = "green";
        } else if (score.longValue() >= this.scorecardFeature.getAmberMin().longValue()
                && score.longValue() <= this.scorecardFeature.getAmberMax().longValue()) {
            color = "amber";
        } else if (score.longValue() >= this.scorecardFeature.getRedMin().longValue()
                && score.longValue() <= this.scorecardFeature.getRedMax().longValue()) {
            color = "red";
        } else {
            color = "orange";
        }

        return color;
    }

    public BigDecimal getWeightage() {
        return this.scorecardFeature.getWeightage();
    }
}
