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
package org.apache.fineract.infrastructure.core.boot;

import org.apache.fineract.credit.scorecard.ApiClient;
import org.apache.fineract.credit.scorecard.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class CreditScorecardApiClientConfiguration {

    @Value("${fineract.credit.scorecard.uid}")
    String username;

    @Value("${fineract.credit.scorecard.password}")
    String password;

    @Value("${fineract.credit.scorecard.baseUrl}")
    String baseUrl;

    @Bean
    public ApiClient creditScorecardClient() {
        final ApiClient client = Configuration.getDefaultApiClient();
        client.setBasePath(baseUrl);
        return client;
    }

}
