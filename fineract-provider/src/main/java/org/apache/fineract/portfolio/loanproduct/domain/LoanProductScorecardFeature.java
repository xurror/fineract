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
package org.apache.fineract.portfolio.loanproduct.domain;

import com.google.gson.JsonObject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.portfolio.creditscorecard.domain.CreditScorecardFeature;
import org.apache.fineract.portfolio.creditscorecard.domain.ScorecardFeatureCriteria;

@Entity
@Table(name = "m_loan_product_scorecard_feature")
public class LoanProductScorecardFeature extends AbstractPersistableCustom {

    @Column(name = "weightage", scale = 6, precision = 5, nullable = false)
    private BigDecimal weightage;

    @Column(name = "green_min", nullable = false)
    private Integer greenMin;

    @Column(name = "green_max", nullable = false)
    private Integer greenMax;

    @Column(name = "amber_min", nullable = false)
    private Integer amberMin;

    @Column(name = "amber_max", nullable = false)
    private Integer amberMax;

    @Column(name = "red_min", nullable = false)
    private Integer redMin;

    @Column(name = "red_max", nullable = false)
    private Integer redMax;

    @ManyToOne(optional = false)
    @JoinColumn(name = "scorecard_feature_id", referencedColumnName = "id", nullable = false)
    private CreditScorecardFeature scorecardFeature;

    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH }, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "loan_product_scorecard_feature_id", referencedColumnName = "id")
    private List<ScorecardFeatureCriteria> featureCriteria;

    public LoanProductScorecardFeature() {
        //
    }

    public LoanProductScorecardFeature(CreditScorecardFeature scorecardFeature, BigDecimal weightage, Integer greenMin, Integer greenMax,
            Integer amberMin, Integer amberMax, Integer redMin, Integer redMax) {
        this.scorecardFeature = scorecardFeature;
        this.weightage = weightage;
        this.greenMin = greenMin;
        this.greenMax = greenMax;
        this.amberMin = amberMin;
        this.amberMax = amberMax;
        this.redMin = redMin;
        this.redMax = redMax;
    }

    public LoanProductScorecardFeature(CreditScorecardFeature scorecardFeature, BigDecimal weightage, Integer greenMin, Integer greenMax,
            Integer amberMin, Integer amberMax, Integer redMin, Integer redMax, List<ScorecardFeatureCriteria> featureCriteria) {
        this.scorecardFeature = scorecardFeature;
        this.weightage = weightage;
        this.greenMin = greenMin;
        this.greenMax = greenMax;
        this.amberMin = amberMin;
        this.amberMax = amberMax;
        this.redMin = redMin;
        this.redMax = redMax;
        this.featureCriteria = featureCriteria;
    }

    public BigDecimal getWeightage() {
        return weightage;
    }

    public Integer getGreenMin() {
        return greenMin;
    }

    public Integer getGreenMax() {
        return greenMax;
    }

    public Integer getAmberMin() {
        return amberMin;
    }

    public Integer getAmberMax() {
        return amberMax;
    }

    public Integer getRedMin() {
        return redMin;
    }

    public Integer getRedMax() {
        return redMax;
    }

    public CreditScorecardFeature getScorecardFeature() {
        return scorecardFeature;
    }

    public List<ScorecardFeatureCriteria> getFeatureCriteria() {
        return featureCriteria;
    }

    public void setFeatureCriteria(List<ScorecardFeatureCriteria> featureCriteria) {
        this.featureCriteria = featureCriteria;
    }

    public LoanProductScorecardFeature update(final JsonObject jsonObject) {
        this.weightage = jsonObject.get("weightage").getAsBigDecimal();
        this.greenMin = jsonObject.get("greenMin").getAsInt();
        this.greenMax = jsonObject.get("greenMax").getAsInt();
        this.amberMin = jsonObject.get("amberMin").getAsInt();
        this.amberMax = jsonObject.get("amberMax").getAsInt();
        this.redMin = jsonObject.get("redMin").getAsInt();
        this.redMax = jsonObject.get("redMax").getAsInt();
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoanProductScorecardFeature)) return false;
        LoanProductScorecardFeature that = (LoanProductScorecardFeature) o;
        return Objects.equals(weightage, that.weightage) && Objects.equals(greenMin, that.greenMin)
                && Objects.equals(greenMax, that.greenMax) && Objects.equals(amberMin, that.amberMin)
                && Objects.equals(amberMax, that.amberMax) && Objects.equals(redMin, that.redMin) && Objects.equals(redMax, that.redMax)
                && Objects.equals(scorecardFeature, that.scorecardFeature) && Objects.equals(featureCriteria, that.featureCriteria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weightage, greenMin, greenMax, amberMin, amberMax, redMin, redMax, scorecardFeature, featureCriteria);
    }

    @Override
    public String toString() {
        return "LoanProductScorecardFeature{" + "weightage=" + weightage + ", greenMin=" + greenMin + ", greenMax=" + greenMax
                + ", amberMin=" + amberMin + ", amberMax=" + amberMax + ", redMin=" + redMin + ", redMax=" + redMax + ", scorecardFeature="
                + scorecardFeature + ", featureCriteria=" + featureCriteria + '}';
    }

}
