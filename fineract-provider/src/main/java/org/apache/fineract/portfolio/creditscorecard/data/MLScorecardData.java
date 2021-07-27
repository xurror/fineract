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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.apache.fineract.infrastructure.codes.data.CodeValueData;
import org.apache.fineract.portfolio.creditscorecard.domain.MLScorecard;
import org.apache.fineract.portfolio.creditscorecard.domain.MLScorecardFields;

public class MLScorecardData implements Serializable {

    private final Long id;
    private final Integer age;
    private final String sex;
    private final String job;
    private final String housing;
    private final BigDecimal creditAmount;
    private final Integer duration;
    private final String purpose;

    private final String risk;
    private final BigDecimal accuracy;

    private final Collection<Map<String, Object>> jobOptions;
    private final Collection<Map<String, Object>> genderOptions;
    private final Collection<Map<String, Object>> purposeOptions;
    private final Collection<Map<String, Object>> housingOptions;

    public MLScorecardData(Long id, Integer age, String sex, String job, String housing, BigDecimal creditAmount, Integer duration,
            String purpose, String risk, BigDecimal accuracy, Collection<Map<String, Object>> jobOptions,
            Collection<Map<String, Object>> genderOptions, Collection<Map<String, Object>> purposeOptions,
            Collection<Map<String, Object>> housingOptions) {
        this.id = id;
        this.age = age;
        this.sex = sex;
        this.job = job;
        this.housing = housing;
        this.creditAmount = creditAmount;
        this.duration = duration;
        this.purpose = purpose;

        this.risk = risk;
        this.accuracy = accuracy;

        this.jobOptions = jobOptions;
        this.genderOptions = genderOptions;
        this.purposeOptions = purposeOptions;
        this.housingOptions = housingOptions;
    }

    public static MLScorecardData instance(final Integer age, final String sex, final BigDecimal creditAmount, final Integer duration,
            final String purpose) {

        final Long id = null;
        final String job = null;
        final String housing = null;

        final String risk = null;
        final BigDecimal accuracy = null;

        final Collection<Map<String, Object>> jobOptions = new ArrayList<>(
                Arrays.asList(Map.of("code", 2, "value", "unemployed"), Map.of("code", 3, "value", "self-employed")));

        final Collection<Map<String, Object>> purposeOptions = new ArrayList<>(
                Arrays.asList(Map.of("code", "radio/TV", "value", "Radio/TV"), Map.of("code", "repairs", "value", "Repairs"),
                        Map.of("code", "business", "value", "Business"), Map.of("code", "education", "value", "Education")));

        final Collection<Map<String, Object>> housingOptions = new ArrayList<>(Arrays.asList(Map.of("code", "own", "value", "Own"),
                Map.of("code", "free", "value", "Free"), Map.of("code", "rent", "value", "Rent")));

        final Collection<Map<String, Object>> genderOptions = new ArrayList<>(
                Arrays.asList(Map.of("code", "male", "value", "Male"), Map.of("code", "female", "value", "Female")));

        return new MLScorecardData(id, age, sex, job, housing, creditAmount, duration, purpose, risk, accuracy, jobOptions, genderOptions,
                purposeOptions, housingOptions);
    }

    public static MLScorecardData template(Integer age, String sex, Collection<CodeValueData> genderCodeValue) {

        final Long id = null;

        final String job = null;
        final String housing = null;

        final BigDecimal creditAmount = null;
        final Integer duration = null;
        final String purpose = null;

        final String risk = null;
        final BigDecimal accuracy = null;

        final Collection<Map<String, Object>> jobOptions = new ArrayList<>(
                Arrays.asList(Map.of("code", 2, "value", "Unemployed"), Map.of("code", 3, "value", "Self-employed")));

        final Collection<Map<String, Object>> purposeOptions = new ArrayList<>(
                Arrays.asList(Map.of("code", "radio/TV", "value", "Radio/TV"), Map.of("code", "repairs", "value", "Repairs"),
                        Map.of("code", "business", "value", "Business"), Map.of("code", "education", "value", "Education")));

        final Collection<Map<String, Object>> housingOptions = new ArrayList<>(Arrays.asList(Map.of("code", "own", "value", "Own"),
                Map.of("code", "free", "value", "Free"), Map.of("code", "rent", "value", "Rent")));

        final Collection<Map<String, Object>> genderOptions = new ArrayList<>(
                Arrays.asList(Map.of("code", "male", "value", "Male"), Map.of("code", "female", "value", "Female")));

        return new MLScorecardData(id, age, sex, job, housing, creditAmount, duration, purpose, risk, accuracy, jobOptions, genderOptions,
                purposeOptions, housingOptions);
    }

    public static MLScorecardData instanceFromPrediction(MLScorecard sc, final String risk, final BigDecimal accuracy) {
        final MLScorecardFields scf = sc.getScorecardFields();
        return new MLScorecardData(sc.getId(), scf.getAge(), scf.getSex(), scf.getJob(), scf.getHousing(), scf.getCreditAmount(),
                scf.getDuration(), scf.getPurpose(), risk, accuracy, null, null, null, null);
    }
}
