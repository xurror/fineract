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
package org.apache.fineract.portfolio.creditscorecard.domain;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.fineract.portfolio.charge.exception.ChargeIsNotActiveException;
import org.apache.fineract.portfolio.creditscorecard.exception.FeatureNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Wrapper for {@link CreditScorecardFeatureRepository} that adds NULL checking and Error handling capabilities
 * </p>
 */
@Service
public class CreditScorecardFeatureRepositoryWrapper {

    private final CreditScorecardFeatureRepository repository;

    @Autowired
    public CreditScorecardFeatureRepositoryWrapper(final CreditScorecardFeatureRepository repository) {
        this.repository = repository;
    }

    public CreditScorecardFeature findOneWithNotFoundDetection(final Long id) {

        final CreditScorecardFeature scorecardFeature = this.repository.findById(id).orElseThrow(() -> new FeatureNotFoundException(id));
        if (scorecardFeature.isDeleted()) {
            throw new FeatureNotFoundException(id);
        }
        if (!scorecardFeature.isActive()) {
            throw new ChargeIsNotActiveException(id, scorecardFeature.getName());
        }

        return scorecardFeature;
    }

    public List<CreditScorecardFeature> findAllWithNotFoundDetection() {
        return this.repository.findAll().stream().filter(CreditScorecardFeature::isActive).filter(feature -> !feature.isDeleted())
                .collect(Collectors.toList());
    }
}
