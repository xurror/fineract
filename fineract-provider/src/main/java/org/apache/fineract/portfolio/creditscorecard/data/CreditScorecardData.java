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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public final class CreditScorecardData implements Serializable {

    private final Long id;
    private final String scoringMethod;
    private final String scoringModel;

    private final MLScorecardData mlScorecard;
    private final StatScorecardData statScorecard;
    private final RuleBasedScorecardData ruleBasedScorecard;

    // Options
    private final Collection<Map<String, Object>> scoringMethods;
    private final Collection<Map<String, Object>> mlScoringModels;
    private final Collection<Map<String, Object>> statScoringModels;
    private final Collection<Map<String, Object>> ruleBasedScoringModels;

    private CreditScorecardData(final Long id, final String scoringMethod, final String scoringModel, final MLScorecardData mlScorecard,
            final StatScorecardData statScorecard, final RuleBasedScorecardData ruleBasedScorecard,
            final Collection<Map<String, Object>> scoringMethods, final Collection<Map<String, Object>> mlScoringModels,
            final Collection<Map<String, Object>> statScoringModels, final Collection<Map<String, Object>> ruleBasedScoringModels) {
        this.id = id;
        this.scoringMethod = scoringMethod;
        this.scoringModel = scoringModel;

        this.mlScorecard = mlScorecard;
        this.statScorecard = statScorecard;
        this.ruleBasedScorecard = ruleBasedScorecard;

        this.scoringMethods = scoringMethods;
        this.mlScoringModels = mlScoringModels;
        this.statScoringModels = statScoringModels;
        this.ruleBasedScoringModels = ruleBasedScoringModels;
    }

    public static CreditScorecardData instance(final Long id, final String scoringMethod, final String scoringModel) {
        final MLScorecardData mlScorecardData = null;
        final StatScorecardData statScorecardData = null;
        final RuleBasedScorecardData ruleBasedScorecardData = null;

        final Collection<Map<String, Object>> scoringMethods = null;
        final Collection<Map<String, Object>> mlScoringModels = null;
        final Collection<Map<String, Object>> statScoringModels = null;
        final Collection<Map<String, Object>> ruleBasedScoringModels = null;

        return new CreditScorecardData(id, scoringMethod, scoringModel, mlScorecardData, statScorecardData, ruleBasedScorecardData,
                scoringMethods, mlScoringModels, statScoringModels, ruleBasedScoringModels);
    }

    public static CreditScorecardData ruleBasedInstance(final Long id, final String scoringMethod, final String scoringModel,
            final RuleBasedScorecardData ruleBasedScorecard) {
        final MLScorecardData mlScorecardData = null;
        final StatScorecardData statScorecardData = null;

        final Collection<Map<String, Object>> scoringMethods = null;
        final Collection<Map<String, Object>> mlScoringModels = null;
        final Collection<Map<String, Object>> statScoringModels = null;
        final Collection<Map<String, Object>> ruleBasedScoringModels = null;

        return new CreditScorecardData(id, scoringMethod, scoringModel, mlScorecardData, statScorecardData, ruleBasedScorecard,
                scoringMethods, mlScoringModels, statScoringModels, ruleBasedScoringModels);
    }

    public static CreditScorecardData mlInstance(final Long id, final String scoringMethod, final String scoringModel,
            final MLScorecardData mlScorecard) {
        final StatScorecardData statScorecardData = null;
        final RuleBasedScorecardData ruleBasedScorecardData = null;

        final Collection<Map<String, Object>> scoringMethods = null;
        final Collection<Map<String, Object>> mlScoringModels = null;
        final Collection<Map<String, Object>> statScoringModels = null;
        final Collection<Map<String, Object>> ruleBasedScoringModels = null;

        return new CreditScorecardData(id, scoringMethod, scoringModel, mlScorecard, statScorecardData, ruleBasedScorecardData,
                scoringMethods, mlScoringModels, statScoringModels, ruleBasedScoringModels);
    }

    public static CreditScorecardData loanTemplate() {
        final Long id = null;
        final String scoringMethod = null;
        final String scoringModel = null;

        final MLScorecardData mlScorecardData = MLScorecardData.template(null, null);
        final StatScorecardData statScorecardData = null;
        final RuleBasedScorecardData ruleBasedScorecardData = null;

        final Collection<Map<String, Object>> scoringMethods = new ArrayList<>(
                Arrays.asList(Map.of("code", "ruleBased", "value", "Rule Based"), Map.of("code", "stat", "value", "Statistical"),
                        Map.of("code", "ml", "value", "Machine Learning")));

        final Collection<Map<String, Object>> mlScoringModels = new ArrayList<>(
                Arrays.asList(Map.of("code", "randomForest", "value", "Random Forest")));

        final Collection<Map<String, Object>> statScoringModels = new ArrayList<>(Arrays.asList(
                Map.of("code", "linearRegression", "value", "Linear Regression"),
                Map.of("code", "polynomialRegression", "value", "Polynomial Regression"), Map.of("code", "manova", "value", "Manova")));

        final Collection<Map<String, Object>> ruleBasedScoringModels = new ArrayList<>(
                Arrays.asList(Map.of("code", "ruleBased", "value", "Rule Based")));

        return new CreditScorecardData(id, scoringMethod, scoringModel, mlScorecardData, statScorecardData, ruleBasedScorecardData,
                scoringMethods, mlScoringModels, statScoringModels, ruleBasedScoringModels);
    }

    public static CreditScorecardData loanScorecardWithTemplate(CreditScorecardData sc) {

        final Collection<Map<String, Object>> scoringMethods = new ArrayList<>(
                Arrays.asList(Map.of("code", "ruleBased", "value", "Rule Based"), Map.of("code", "stat", "value", "Statistical"),
                        Map.of("code", "ml", "value", "Machine Learning")));

        final Collection<Map<String, Object>> mlScoringModels = new ArrayList<>(
                Arrays.asList(Map.of("code", "randomForest", "value", "Random Forest")));

        final Collection<Map<String, Object>> statScoringModels = new ArrayList<>(Arrays.asList(
                Map.of("code", "linearRegression", "value", "Linear Regression"),
                Map.of("code", "polynomialRegression", "value", "Polynomial Regression"), Map.of("code", "manova", "value", "Manova")));

        final Collection<Map<String, Object>> ruleBasedScoringModels = new ArrayList<>(
                Arrays.asList(Map.of("code", "ruleBased", "value", "Rule Based")));

        return new CreditScorecardData(sc.id, sc.scoringMethod, sc.scoringModel, sc.mlScorecard, sc.statScorecard, sc.ruleBasedScorecard,
                scoringMethods, mlScoringModels, statScoringModels, ruleBasedScoringModels);
    }

    public Long getId() {
        return id;
    }
}
