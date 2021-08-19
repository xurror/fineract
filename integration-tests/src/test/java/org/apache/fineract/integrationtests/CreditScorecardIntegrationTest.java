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
package org.apache.fineract.integrationtests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.fineract.integrationtests.common.ClientHelper;
import org.apache.fineract.integrationtests.common.Utils;
import org.apache.fineract.integrationtests.common.creditscorecard.CreditScorecardHelper;
import org.apache.fineract.integrationtests.common.creditscorecard.ScorecardFeatureTestBuilder;
import org.apache.fineract.integrationtests.common.loans.LoanApplicationTestBuilder;
import org.apache.fineract.integrationtests.common.loans.LoanProductTestBuilder;
import org.apache.fineract.integrationtests.common.loans.LoanStatusChecker;
import org.apache.fineract.integrationtests.common.loans.LoanTransactionHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class CreditScorecardIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(CreditScorecardIntegrationTest.class);
    private ResponseSpecification responseSpec;
    private ResponseSpecification responseSpecForStatusCode403;
    private ResponseSpecification responseSpecForStatusCode400;
    private RequestSpecification requestSpec;
    private LoanTransactionHelper loanTransactionHelper;

    @BeforeEach
    public void setup() {
        Utils.initializeRESTAssured();
        this.requestSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).build();
        this.requestSpec.header("Authorization", "Basic " + Utils.loginIntoServerAndGetBase64EncodedAuthenticationKey());
        this.responseSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
        this.responseSpecForStatusCode403 = new ResponseSpecBuilder().expectStatusCode(403).build();
        this.responseSpecForStatusCode400 = new ResponseSpecBuilder().expectStatusCode(400).build();
        this.loanTransactionHelper = new LoanTransactionHelper(this.requestSpec, this.responseSpec);
    }

    @Test
    public void testCreditScorecardFeatures() {
        final String genderFeatureString = new Gson().toJson(CreditScorecardHelper.genderScorecardFeature());
        final Integer genderFeatureId = CreditScorecardHelper.createScorecardFeature(this.requestSpec, this.responseSpec,
                genderFeatureString);
        Assertions.assertNotNull(genderFeatureId);

        final HashMap changes = CreditScorecardHelper.updateScorecardFeature(this.requestSpec, this.responseSpec, genderFeatureId,
                CreditScorecardHelper.getModifyFeatureJSON());

        final HashMap featureDataAfterChanges = CreditScorecardHelper.getScorecardFeatureById(this.requestSpec, this.responseSpec,
                genderFeatureId);

        final HashMap valueTypeAfterChanges = (HashMap) featureDataAfterChanges.get("valueType");
        Assertions.assertEquals(valueTypeAfterChanges.get("id"), changes.get("valueType"), "Verifying Feature after Modification");

        Integer featureIdAfterDeletion = CreditScorecardHelper.deleteScorecardFeature(this.responseSpec, this.requestSpec, genderFeatureId);
        Assertions.assertEquals(genderFeatureId, featureIdAfterDeletion, "Verifying Charge ID after deletion");
    }

    @Test
    public void testRuleBasedCreditScorecard() {
        final String genderFeatureString = new Gson().toJson(CreditScorecardHelper.genderScorecardFeature());
        final Integer genderFeatureId = CreditScorecardHelper.createScorecardFeature(this.requestSpec, this.responseSpec,
                genderFeatureString);
        Assertions.assertNotNull(genderFeatureId);

        final String ageFeatureString = new Gson().toJson(CreditScorecardHelper.ageScorecardFeature());
        final Integer ageFeatureId = CreditScorecardHelper.createScorecardFeature(this.requestSpec, this.responseSpec, ageFeatureString);
        Assertions.assertNotNull(ageFeatureId);

        final String purposeFeatureString = new Gson().toJson(CreditScorecardHelper.purposeScorecardFeature());
        final Integer purposeFeatureId = CreditScorecardHelper.createScorecardFeature(this.requestSpec, this.responseSpec,
                purposeFeatureString);
        Assertions.assertNotNull(purposeFeatureId);

        final HashMap<String, Object> genderLpFeature = new ScorecardFeatureTestBuilder().withGenderCriteriaScores().build(genderFeatureId);

        final HashMap<String, Object> ageLpFeature = new ScorecardFeatureTestBuilder().withAgeCriteriaScores().build(ageFeatureId);

        final HashMap<String, Object> purposeLpFeature = new ScorecardFeatureTestBuilder().withPurposeCriteriaScores()
                .build(purposeFeatureId);

        final ArrayList<HashMap<String, Object>> lpScorecardFeatures = new ArrayList<>(
                Arrays.asList(genderLpFeature, ageLpFeature, purposeLpFeature));

        final Integer loanProductId = this.loanTransactionHelper
                .getLoanProductId(new LoanProductTestBuilder().withScorecardFeatures(lpScorecardFeatures).build(null));
        Assertions.assertNotNull(loanProductId);

        final String loanProductDetailsString = this.loanTransactionHelper.getLoanProductDetailsWithScorecard(this.requestSpec,
                this.responseSpec, loanProductId);

        final JsonObject loanProductObject = new Gson().fromJson(loanProductDetailsString, JsonObject.class);

        final ArrayList<HashMap<String, Object>> criteriaScores = new ArrayList<>();

        final JsonArray scorecardFeatures = loanProductObject.get("scorecardFeatures").getAsJsonArray();

        scorecardFeatures.forEach(feature -> {
            final JsonObject featureObj = feature.getAsJsonObject();

            final String id = featureObj.get("id").getAsString();
            String value = null;

            final int featureId = featureObj.get("featureId").getAsInt();
            boolean validValues = false;
            if (featureId == genderFeatureId) {
                validValues = true;
                value = "Male";
            }

            if (featureId == ageFeatureId) {
                validValues = true;
                value = "27";
            }

            if (featureId == purposeFeatureId) {
                validValues = true;
                value = "Housing";
            }

            if (validValues) {
                final HashMap<String, Object> map = new HashMap<>();
                map.put("featureId", id);
                map.put("value", value);
                criteriaScores.add(map);
            }
        });

        final HashMap<String, Object> scorecardMap = new HashMap<>();

        final String scoringMethod = "ruleBased";
        final String scoringModel = "ruleBased";

        scorecardMap.put("scoringMethod", "ruleBased");
        scorecardMap.put("scoringModel", "ruleBased");
        scorecardMap.put("ruleBasedScorecard", Map.of("criteriaScores", criteriaScores));

        final Integer clientId = ClientHelper.createClient(this.requestSpec, this.responseSpec, "01 January 2012");
        Assertions.assertNotNull(clientId);

        final String proposedAmount = "10000";

        final Integer loanId = applyForLoanWithRuleBasedScorecardApplication(clientId, loanProductId, proposedAmount, scorecardMap);

        HashMap loanStatusHashMap = LoanStatusChecker.getStatusOfLoan(this.requestSpec, this.responseSpec, loanId);
        LoanStatusChecker.verifyLoanIsPending(loanStatusHashMap);

        final String loanDetailsString = this.loanTransactionHelper.getLoanDetails(this.requestSpec, this.responseSpec, loanId);

        final JsonObject loanObject = new Gson().fromJson(loanDetailsString, JsonObject.class);

        final JsonObject scorecard = loanObject.get("scorecard").getAsJsonObject();

        final JsonObject ruleBasedScorecard = scorecard.get("ruleBasedScorecard").getAsJsonObject();

        assertEquals(scoringMethod, scorecard.get("scoringMethod").getAsString());
        assertEquals(scoringModel, scorecard.get("scoringModel").getAsString());

        final String expectedOverallColor = "green";
        final String expectedOverallScore = "8.00000";
        assertEquals(expectedOverallColor, ruleBasedScorecard.get("overallColor").getAsString());
        assertEquals(expectedOverallScore, ruleBasedScorecard.get("overallScore").getAsString());

    }

    private Integer applyForLoanWithRuleBasedScorecardApplication(Integer clientId, Integer loanProductId, String proposedAmount,
            HashMap<String, Object> scorecard) {
        final String loanApplication = new LoanApplicationTestBuilder().withPrincipal(proposedAmount).withLoanTermFrequency("5")
                .withLoanTermFrequencyAsMonths().withNumberOfRepayments("5").withRepaymentEveryAfter("1")
                .withRepaymentFrequencyTypeAsMonths().withInterestRatePerPeriod("2").withExpectedDisbursementDate("04 April 2012")
                .withSubmittedOnDate("02 April 2012").withScorecard(scorecard).build(clientId.toString(), loanProductId.toString(), null);
        return this.loanTransactionHelper.getLoanId(loanApplication);
    }

}
