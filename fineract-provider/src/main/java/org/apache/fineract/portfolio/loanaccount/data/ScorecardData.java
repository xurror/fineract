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
package org.apache.fineract.portfolio.loanaccount.data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import org.apache.fineract.infrastructure.codes.data.CodeValueData;

public final class ScorecardData {

    private final Integer age;
    private final String sex;
    private final String job;
    private final String housing;
    private final BigDecimal creditAmount;
    private final Integer duration;
    private final String purpose;
    private final String risk;

    private final Collection<Map<String, Object>> genderOptions = new ArrayList<>();
    private final Collection<Map<String, Object>> housingOptions = new ArrayList<>(
            Arrays.asList(Map.of("name", "own"), Map.of("name", "free"), Map.of("name", "rent")));

    private ScorecardData(final Integer age, final String sex, final BigDecimal creditAmount, final Integer duration, final String purpose,
            final Collection<CodeValueData> genderOptions) {
        this.age = age;
        this.sex = sex;
        this.job = null;
        this.housing = null;
        this.creditAmount = creditAmount;
        this.duration = duration;
        this.purpose = purpose;
        this.risk = null;
        for (CodeValueData gender : genderOptions) {
            this.genderOptions.add(Map.of("name", gender.getName()));
        }
    }

    public static ScorecardData instance(final Integer age, final String sex, final BigDecimal creditAmount, final Integer duration,
            final String purpose, final Collection<CodeValueData> genderOptions) {
        return new ScorecardData(age, sex, creditAmount, duration, purpose, genderOptions);
    }
}
