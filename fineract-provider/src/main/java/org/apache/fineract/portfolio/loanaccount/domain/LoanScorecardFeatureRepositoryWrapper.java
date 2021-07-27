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
package org.apache.fineract.portfolio.loanaccount.domain;

import java.util.List;
import org.apache.fineract.portfolio.creditscorecard.exception.FeatureNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Wrapper for {@link LoanScorecardFeatureRepository} that adds NULL checking and Error handling capabilities
 * </p>
 */
@Service
public class LoanScorecardFeatureRepositoryWrapper {

    private final LoanScorecardFeatureRepository repository;

    @Autowired
    public LoanScorecardFeatureRepositoryWrapper(final LoanScorecardFeatureRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public LoanScorecardFeature findOneWithNotFoundDetection(final Long id) {
        return this.repository.findById(id).orElseThrow(() -> new FeatureNotFoundException(id));
    }

    public LoanScorecardFeature saveAndFlush(final LoanScorecardFeature scorecardFeature) {
        return this.repository.saveAndFlush(scorecardFeature);
    }

    @Transactional
    public LoanScorecardFeature save(final LoanScorecardFeature scorecardFeature) {
        return this.repository.save(scorecardFeature);
    }

    public List<LoanScorecardFeature> save(List<LoanScorecardFeature> scorecardFeatures) {
        return this.repository.saveAll(scorecardFeatures);
    }

    public void flush() {
        this.repository.flush();
    }

    public void delete(final Long featureId) {
        this.repository.deleteById(featureId);
    }

}
