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

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.fineract.cn.sekhmet.models.PredictionResponse;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "m_loan_scorecard")
public class LoanScorecard extends AbstractPersistableCustom {

    private static final Logger LOG = LoggerFactory.getLogger(Loan.class);

    @Embedded
    private LoanScorecardFields loanScorecardFields;

    @Column(name = "predicted_risk")
    private String predictedRisk;

    @Column(name = "actual_risk")
    private String actualRisk;

    @Column(name = "prediction_request_id")
    private Integer predictionRequestId;

    @OneToOne(mappedBy = "loanScorecard")
    private Loan loan;

    public LoanScorecard() {

    }

    public LoanScorecard(final LoanScorecardFields loanScorecardFields, final String predictedRisk, final String actualRisk,
            final Integer predictionRequestId) {
        this.loanScorecardFields = loanScorecardFields;
        this.predictedRisk = predictedRisk;
        this.actualRisk = actualRisk;
        this.predictionRequestId = predictionRequestId;
    }

    public LoanScorecard scorecardFields(final LoanScorecardFields loanScorecardFields) {
        this.loanScorecardFields = loanScorecardFields;
        return this;
    }

    public LoanScorecardFields getScorecardFields() {
        return loanScorecardFields;
    }

    public void setScorecardFields(final LoanScorecardFields loanScorecardFields) {
        this.loanScorecardFields = loanScorecardFields;
    }

    public String getPredictedRisk() {
        return predictedRisk;
    }

    public void setPredictedRisk(final String predictedRisk) {
        this.predictedRisk = predictedRisk;
    }

    public String getActualRisk() {
        return actualRisk;
    }

    public void setActualRisk(final String actualRisk) {
        this.actualRisk = actualRisk;
    }

    public Integer getPredictionRequestId() {
        return predictionRequestId;
    }

    public void setPredictionRequestId(final Integer predictionRequestId) {
        this.predictionRequestId = predictionRequestId;
    }

    public LoanScorecard setPredictionResponse(final PredictionResponse response) {
        this.predictedRisk = response.getLabel();
        this.predictionRequestId = response.getRequestId();
        return this;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(final Loan loan) {
        this.loan = loan;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoanScorecard)) return false;
        LoanScorecard loanScorecard = (LoanScorecard) o;
        return Objects.equals(loanScorecardFields, loanScorecard.loanScorecardFields)
                && Objects.equals(predictedRisk, loanScorecard.predictedRisk) && Objects.equals(actualRisk, loanScorecard.actualRisk)
                && Objects.equals(predictionRequestId, loanScorecard.predictionRequestId) && Objects.equals(loan, loanScorecard.loan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanScorecardFields, predictedRisk, actualRisk, predictionRequestId, loan);
    }
}
