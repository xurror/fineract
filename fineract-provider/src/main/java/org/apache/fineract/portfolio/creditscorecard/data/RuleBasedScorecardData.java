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
import org.apache.fineract.portfolio.creditscorecard.domain.FeatureCriteriaScore;
import org.apache.fineract.portfolio.creditscorecard.domain.RuleBasedScorecard;

public final class RuleBasedScorecardData implements Serializable {

    private final Long id;
    private final Collection<FeatureCriteriaScoreData> criteriaScores;
    private final BigDecimal overallScore;
    private final String overallColor;

    private RuleBasedScorecardData(final Long id, final Collection<FeatureCriteriaScoreData> criteriaScores, final BigDecimal overallScore,
            final String overallColor) {
        this.id = id;
        this.criteriaScores = criteriaScores;
        this.overallScore = overallScore;
        this.overallColor = overallColor;
    }

    public static RuleBasedScorecardData instance(RuleBasedScorecard rbs) {
        final List<FeatureCriteriaScore> ctScores = rbs.getCriteriaScores();
        final List<FeatureCriteriaScoreData> ctScoresData = ctScores.stream().map(FeatureCriteriaScoreData::instance)
                .collect(Collectors.toList());

        return new RuleBasedScorecardData(rbs.getId(), ctScoresData, rbs.getOverallScore(), rbs.getOverallColor());
    }
}
