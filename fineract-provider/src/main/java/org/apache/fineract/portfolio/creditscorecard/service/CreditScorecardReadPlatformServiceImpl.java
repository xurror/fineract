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
package org.apache.fineract.portfolio.creditscorecard.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import org.apache.fineract.infrastructure.codes.data.CodeValueData;
import org.apache.fineract.infrastructure.codes.service.CodeValueReadPlatformService;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.portfolio.client.domain.ClientRepositoryWrapper;
import org.apache.fineract.portfolio.creditscorecard.data.CreditScorecardFeatureData;
import org.apache.fineract.portfolio.creditscorecard.data.MLScorecardData;
import org.apache.fineract.portfolio.creditscorecard.data.ScorecardFeatureCriteriaData;
import org.apache.fineract.portfolio.creditscorecard.domain.CreditScorecardFeatureRepository;
import org.apache.fineract.portfolio.loanaccount.domain.LoanScorecardFeatureRepository;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductScorecardFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class CreditScorecardReadPlatformServiceImpl implements CreditScorecardReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final ClientRepositoryWrapper clientRepository;
    private final CodeValueReadPlatformService codeValueReadPlatformService;
    private final CreditScorecardFeatureRepository creditScorecardFeatureRepository;
    private final LoanScorecardFeatureRepository loanScorecardFeatureRepository;
    private final LoanProductScorecardFeatureRepository loanProductScorecardFeatureRepository;
    private final CreditScorecardFeatureDropdownReadPlatformService creditScorecardFeatureDropdownReadPlatformService;

    @Autowired
    public CreditScorecardReadPlatformServiceImpl(final RoutingDataSource dataSource,
            final CreditScorecardFeatureRepository creditScorecardFeatureRepository,
            final CreditScorecardFeatureDropdownReadPlatformService creditScorecardFeatureDropdownReadPlatformService,
            final LoanScorecardFeatureRepository loanScorecardFeatureRepository,
            final LoanProductScorecardFeatureRepository loanProductScorecardFeatureRepository,
            final ClientRepositoryWrapper clientRepository, final CodeValueReadPlatformService codeValueReadPlatformService) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.clientRepository = clientRepository;
        this.codeValueReadPlatformService = codeValueReadPlatformService;
        this.loanScorecardFeatureRepository = loanScorecardFeatureRepository;
        this.loanProductScorecardFeatureRepository = loanProductScorecardFeatureRepository;
        this.creditScorecardFeatureRepository = creditScorecardFeatureRepository;
        this.creditScorecardFeatureDropdownReadPlatformService = creditScorecardFeatureDropdownReadPlatformService;
    }

    @Override
    public CreditScorecardFeatureData retrieveNewScorecardFeatureDetails() {

        final Collection<EnumOptionData> valueTypeOptions = this.creditScorecardFeatureDropdownReadPlatformService.retrieveValueTypes();
        final Collection<EnumOptionData> dataTypeOptions = this.creditScorecardFeatureDropdownReadPlatformService.retrieveDataTypes();
        final Collection<EnumOptionData> categoryOptions = this.creditScorecardFeatureDropdownReadPlatformService.retrieveCategoryTypes();

        return CreditScorecardFeatureData.template(valueTypeOptions, dataTypeOptions, categoryOptions);
    }

    @Override
    public Collection<CreditScorecardFeatureData> retrieveLoanProductFeatures(Long productId) {
        final ScorecardFeatureMapper rm = new ScorecardFeatureMapper();

        final String sql = "SELECT " + rm.featureSchema()
                + " WHERE scf.is_deleted=false AND scf.is_active=true AND lpscf.loan_product_id=? ";

        final Collection<CreditScorecardFeatureData> scorecardFeatures = this.jdbcTemplate.query(sql, rm, new Object[] { productId });

        for (CreditScorecardFeatureData scorecardFeature : scorecardFeatures) {
            Collection<ScorecardFeatureCriteriaData> criteriaData = this.retrieveFeatureCriteria(scorecardFeature.getId());
            scorecardFeature.getCriteria().addAll(criteriaData);
        }
        return scorecardFeatures;
    }

    @Override
    public MLScorecardData retrieveMLScorecardTemplate(Long clientId) {
        // final Client client = this.clientRepository.findOneWithNotFoundDetection(clientId);
        //
        // final LocalDate dateOfBirth = LocalDate.ofInstant(client.dateOfBirth().toInstant(),
        // DateUtils.getDateTimeZoneOfTenant());
        //
        // final int age = Period.between(dateOfBirth, LocalDate.now(DateUtils.getDateTimeZoneOfTenant())).getYears();
        //
        // String gender = null;
        // if (client.gender() != null) {
        // gender = client.gender().label();
        // }

        // final LocalDate expectedDisbursementDate = loan.getExpectedDisbursementDate().withDayOfMonth(1);
        // final LocalDate expectedMaturityDate = loan.getExpectedMaturityDate().withDayOfMonth(1);
        //
        // final Period durationPeriod = Period.between(expectedDisbursementDate, expectedMaturityDate);
        //
        // final int duration = (durationPeriod.getYears() * 12) + durationPeriod.getMonths();
        //
        // String purpose = null;
        // if (loan.getLoanPurpose() != null) {
        // purpose = loan.getLoanPurpose().label();
        // }

        final Collection<CodeValueData> genderOptions = new ArrayList<>(
                this.codeValueReadPlatformService.retrieveCodeValuesByCode("Gender"));

        return MLScorecardData.template(null, null, genderOptions);
    }

    private Collection<ScorecardFeatureCriteriaData> retrieveFeatureCriteria(Long featureId) {
        final FeatureCriteriaMapper rm = new FeatureCriteriaMapper();

        final String sql = "SELECT " + rm.featureCriteriaSchema() + " WHERE crit.loan_product_scorecard_feature_id=?";

        return this.jdbcTemplate.query(sql, rm, new Object[] { featureId });
    }

    private static final class FeatureCriteriaMapper implements RowMapper<ScorecardFeatureCriteriaData> {

        public String featureCriteriaSchema() {
            return "crit.id as id, crit.criteria as criteria, crit.score as score " + "FROM m_credit_scorecard_feature_criteria crit";
        }

        @Override
        public ScorecardFeatureCriteriaData mapRow(ResultSet rs, int rowNum) throws SQLException {
            final Long id = rs.getLong("id");
            final String criteria = rs.getString("criteria");
            final int score = rs.getInt("score");
            return ScorecardFeatureCriteriaData.instance(id, criteria, BigDecimal.valueOf(score));
        }
    }

    private static final class ScorecardFeatureMapper implements RowMapper<CreditScorecardFeatureData> {

        public String featureSchema() {
            return "scf.id as featureId, scf.name as name, scf.value_type_enum as valueType, "
                    + "scf.data_type_enum as dataType, scf.category_enum as category, scf.is_active as active, " +

                    "lpscf.id as id, lpscf.weightage as weightage, lpscf.green_min as greenMin, "
                    + "lpscf.green_max as greenMax, lpscf.amber_min as amberMin, lpscf.amber_max as amberMax, "
                    + "lpscf.red_min as redMin, lpscf.red_max as redMax " +

                    "FROM m_loan_product_scorecard_feature lpscf "
                    + "JOIN m_credit_scorecard_feature scf ON scf.id = lpscf.scorecard_feature_id ";
        }

        @Override
        public CreditScorecardFeatureData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
            final Long id = rs.getLong("id");

            final Long featureId = rs.getLong("featureId");
            final String name = rs.getString("name");

            final int valueType = rs.getInt("valueType");
            final EnumOptionData valueTypeEnum = CreditScorecardEnumerations.featureValueType(valueType);

            final int dataType = rs.getInt("dataType");
            final EnumOptionData dataTypeEnum = CreditScorecardEnumerations.featureDataType(dataType);

            final int category = rs.getInt("category");
            final EnumOptionData categoryEnum = CreditScorecardEnumerations.featureCategory(category);

            final boolean active = rs.getBoolean("active");

            final BigDecimal weightage = rs.getBigDecimal("weightage");

            final int greenMin = rs.getInt("greenMin");
            final int greenMax = rs.getInt("greenMax");

            final int amberMin = rs.getInt("amberMin");
            final int amberMax = rs.getInt("amberMax");

            final int redMin = rs.getInt("redMin");
            final int redMax = rs.getInt("redMax");

            return CreditScorecardFeatureData.instance(id, featureId, name, valueTypeEnum, dataTypeEnum, categoryEnum, active, weightage,
                    greenMin, greenMax, amberMin, amberMax, redMin, redMax);
        }
    }

}
