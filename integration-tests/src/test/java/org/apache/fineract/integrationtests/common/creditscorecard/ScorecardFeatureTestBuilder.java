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
package org.apache.fineract.integrationtests.common.creditscorecard;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ScorecardFeatureTestBuilder {

    private String weightage = "0.5";
    private String greenMin = "0";
    private String greenMax = "3";
    private String amberMin = "4";
    private String amberMax = "6";
    private String redMin = "7";
    private String redMax = "10";

    private Collection<Map<String, Object>> criteriaScores = null;

    public HashMap<String, Object> build(final Integer featureId) {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("featureId", featureId);
        map.put("weightage", weightage);
        map.put("greenMin", greenMin);
        map.put("greenMax", greenMax);
        map.put("amberMin", amberMin);
        map.put("amberMax", amberMax);
        map.put("redMin", redMin);
        map.put("redMax", redMax);
        map.put("criteriaScores", criteriaScores);
        return map;
    }

    public String toJson(final HashMap<String, Object> map) {
        return new Gson().toJson(map);
    }

    public ScorecardFeatureTestBuilder withGenderCriteriaScores() {
        this.criteriaScores = new ArrayList<>(
                Arrays.asList(Map.of("criteria", "Male", "score", "5"), Map.of("criteria", "Female", "score", "3")));
        return this;
    }

    public ScorecardFeatureTestBuilder withAgeCriteriaScores() {
        this.criteriaScores = new ArrayList<>(Arrays.asList(Map.of("criteria", "15-30", "score", "6"),
                Map.of("criteria", "31-60", "score", "4"), Map.of("criteria", "61-100", "score", "8")));
        return this;
    }

    public ScorecardFeatureTestBuilder withPurposeCriteriaScores() {
        this.criteriaScores = new ArrayList<>(
                Arrays.asList(Map.of("criteria", "Housing", "score", "5"), Map.of("criteria", "Repairs", "score", "3")));
        return this;
    }
}
