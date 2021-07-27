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

public final class RuleBasedScorecardData implements Serializable {

    private final Collection<CriteriaScoreData> criteriaScores;
    private final BigDecimal scorecardScore;
    private final String scorecardColor;

    private RuleBasedScorecardData(final Collection<CriteriaScoreData> criteriaScores, final BigDecimal scorecardScore,
            final String scorecardColor) {
        this.criteriaScores = criteriaScores;
        this.scorecardScore = scorecardScore;
        this.scorecardColor = scorecardColor;
    }

    public static RuleBasedScorecardData instance(final Collection<CriteriaScoreData> criteriaScores, final BigDecimal scorecardScore,
            final String scorecardColor) {
        return new RuleBasedScorecardData(criteriaScores, scorecardScore, scorecardColor);
    }

    public static CriteriaScoreData criteriaScoreInstance(final String feature, final String value, final BigDecimal score,
            final String color) {
        return CriteriaScoreData.instance(feature, value, score, color);
    }

    public static class CriteriaScoreData {

        private final String feature;
        private final String value;
        private final BigDecimal score;
        private final String color;

        private CriteriaScoreData(String feature, String value, BigDecimal score, String color) {
            this.feature = feature;
            this.value = value;
            this.score = score;
            this.color = color;
        }

        private static CriteriaScoreData instance(final String feature, final String value, final BigDecimal score, final String color) {
            return new CriteriaScoreData(feature, value, score, color);
        }

        public BigDecimal getScore() {
            return score;
        }

        public String getColor() {
            return color;
        }
    }
}
