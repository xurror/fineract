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

import java.util.Collection;
import org.apache.fineract.infrastructure.core.data.EnumOptionData;
import org.apache.fineract.infrastructure.core.service.RoutingDataSource;
import org.apache.fineract.portfolio.creditscorecard.data.CreditScorecardFeatureData;
import org.apache.fineract.portfolio.creditscorecard.domain.CreditScorecardFeatureRepository;
import org.apache.fineract.portfolio.creditscorecard.exception.FeatureNotFoundException;
import org.apache.fineract.portfolio.loanaccount.domain.LoanScorecardFeature;
import org.apache.fineract.portfolio.loanaccount.domain.LoanScorecardFeatureRepository;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductScorecardFeatureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditScorecardReadPlatformServiceImpl implements CreditScorecardReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final CreditScorecardFeatureRepository creditScorecardFeatureRepository;
    private final LoanScorecardFeatureRepository loanScorecardFeatureRepository;
    private final LoanProductScorecardFeatureRepository loanProductScorecardFeatureRepository;
    private final CreditScorecardFeatureDropdownReadPlatformService creditScorecardFeatureDropdownReadPlatformService;

    @Autowired
    public CreditScorecardReadPlatformServiceImpl(final RoutingDataSource dataSource,
            final CreditScorecardFeatureRepository creditScorecardFeatureRepository,
            final CreditScorecardFeatureDropdownReadPlatformService creditScorecardFeatureDropdownReadPlatformService,
            final LoanScorecardFeatureRepository loanScorecardFeatureRepository,
            final LoanProductScorecardFeatureRepository loanProductScorecardFeatureRepository) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.loanScorecardFeatureRepository = loanScorecardFeatureRepository;
        this.loanProductScorecardFeatureRepository = loanProductScorecardFeatureRepository;
        this.creditScorecardFeatureRepository = creditScorecardFeatureRepository;
        this.creditScorecardFeatureDropdownReadPlatformService = creditScorecardFeatureDropdownReadPlatformService;
    }

    @Transactional(readOnly = true)
    public LoanScorecardFeature retrieveOneLoanFeatureWithNotFoundDetection(final Long id) {
        return this.loanScorecardFeatureRepository.findById(id).orElseThrow(() -> new FeatureNotFoundException(id));
    }

    public Collection<LoanScorecardFeature> retrieveLoanFeaturesByLoanId(Long loanId) {
        return this.loanScorecardFeatureRepository.findByLoanId(loanId);
    }

    @Override
    public CreditScorecardFeatureData retrieveNewScorecardFeatureDetails() {

        final Collection<EnumOptionData> valueTypeOptions = this.creditScorecardFeatureDropdownReadPlatformService.retrieveValueTypes();
        final Collection<EnumOptionData> dataTypeOptions = this.creditScorecardFeatureDropdownReadPlatformService.retrieveDataTypes();
        final Collection<EnumOptionData> categoryOptions = this.creditScorecardFeatureDropdownReadPlatformService.retrieveCategoryTypes();

        return CreditScorecardFeatureData.template(valueTypeOptions, dataTypeOptions, categoryOptions);
    }

}
