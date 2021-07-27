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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;

@Entity
@Table(name = "m_credit_scorecard")
public class CreditScorecard extends AbstractPersistableCustom {

    @Column(name = "scorecard_scoring_method")
    private String scoringMethod;

    @Column(name = "scorecard_scoring_model")
    private String scoringModel;

    public CreditScorecard(String scoringMethod, String scoringModel) {
        this.scoringMethod = scoringMethod;
        this.scoringModel = scoringModel;
    }

    public CreditScorecard() {
        //
    }

    public String getScoringMethod() {
        return scoringMethod;
    }

    public String getScoringModel() {
        return scoringModel;
    }
}
