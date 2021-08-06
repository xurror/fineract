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
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.fineract.integrationtests.common.CommonConstants;
import org.apache.fineract.integrationtests.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public final class CreditScorecardHelper {

    private CreditScorecardHelper() {

    }

    private static final Logger LOG = LoggerFactory.getLogger(CreditScorecardHelper.class);
    private static final String CREDIT_SCORECARD_FEATURES_URL = "/fineract-provider/api/v1/creditScorecard/features";
    private static final String CREATE_CREDIT_SCORECARD_FEATURE_URL = CREDIT_SCORECARD_FEATURES_URL + "?" + Utils.TENANT_IDENTIFIER;

    private static final Integer FEATURE_VALUE_TYPE_BINARY = 0;
    private static final Integer FEATURE_VALUE_TYPE_NOMINAL = 1;
    private static final Integer FEATURE_VALUE_TYPE_INTERVAL = 2;
    private static final Integer FEATURE_VALUE_TYPE_RATIO = 3;

    private static final Integer FEATURE_DATA_TYPE_NUMERIC = 0;
    private static final Integer FEATURE_DATA_TYPE_STRING = 1;
    private static final Integer FEATURE_DATA_TYPE_DATE = 2;

    private static final Integer FEATURE_CATEGORY_INDIVIDUAL = 0;
    private static final Integer FEATURE_CATEGORY_ORGANISATION = 1;

    private static final boolean ACTIVE = true;

    public static HashMap<String, Object> genderScorecardFeature() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("name", Utils.randomNameGenerator("Gender_", 4));
        map.put("valueType", FEATURE_VALUE_TYPE_BINARY);
        map.put("dataType", FEATURE_DATA_TYPE_STRING);
        map.put("category", FEATURE_CATEGORY_INDIVIDUAL);
        map.put("active", ACTIVE);
        map.put("locale", CommonConstants.LOCALE);
        return map;
    }

    public static HashMap<String, Object> ageScorecardFeature() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("name", Utils.randomNameGenerator("Age_", 4));
        map.put("valueType", FEATURE_VALUE_TYPE_INTERVAL);
        map.put("dataType", FEATURE_DATA_TYPE_NUMERIC);
        map.put("category", FEATURE_CATEGORY_INDIVIDUAL);
        map.put("active", ACTIVE);
        map.put("locale", CommonConstants.LOCALE);
        return map;
    }

    public static HashMap<String, Object> purposeScorecardFeature() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("name", Utils.randomNameGenerator("Purpose_", 4));
        map.put("valueType", FEATURE_VALUE_TYPE_NOMINAL);
        map.put("dataType", FEATURE_DATA_TYPE_STRING);
        map.put("category", FEATURE_CATEGORY_INDIVIDUAL);
        map.put("active", ACTIVE);
        map.put("locale", CommonConstants.LOCALE);
        return map;
    }

    public static String getModifyFeatureJSON() {
        final HashMap<String, Object> map = new HashMap<>();
        map.put("valueType", FEATURE_VALUE_TYPE_NOMINAL);
        map.put("locale", CommonConstants.LOCALE);
        String json = new Gson().toJson(map);
        LOG.info("{}", json);
        return json;
    }

    public static Integer createScorecardFeature(final RequestSpecification requestSpec, final ResponseSpecification responseSpec,
            final String request) {
        return Utils.performServerPost(requestSpec, responseSpec, CREATE_CREDIT_SCORECARD_FEATURE_URL, request, "resourceId");
    }

    public static ArrayList<HashMap> getScorecardFeatures(final RequestSpecification requestSpec,
            final ResponseSpecification responseSpec) {
        return (ArrayList) Utils.performServerGet(requestSpec, responseSpec, CREDIT_SCORECARD_FEATURES_URL + "?" + Utils.TENANT_IDENTIFIER,
                "");
    }

    public static HashMap getScorecardFeatureById(final RequestSpecification requestSpec, final ResponseSpecification responseSpec,
            final Integer chargeId) {
        return Utils.performServerGet(requestSpec, responseSpec,
                CREDIT_SCORECARD_FEATURES_URL + "/" + chargeId + "?" + Utils.TENANT_IDENTIFIER, "");
    }

    public static HashMap updateScorecardFeature(final RequestSpecification requestSpec, final ResponseSpecification responseSpec,
            final Integer chargeId, final String request) {
        return Utils.performServerPut(requestSpec, responseSpec,
                CREDIT_SCORECARD_FEATURES_URL + "/" + chargeId + "?" + Utils.TENANT_IDENTIFIER, request, CommonConstants.RESPONSE_CHANGES);
    }

    public static Integer deleteScorecardFeature(final ResponseSpecification responseSpec, final RequestSpecification requestSpec,
            final Integer chargeId) {
        return Utils.performServerDelete(requestSpec, responseSpec,
                CREDIT_SCORECARD_FEATURES_URL + "/" + chargeId + "?" + Utils.TENANT_IDENTIFIER, CommonConstants.RESPONSE_RESOURCE_ID);
    }

}
