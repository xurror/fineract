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
import java.util.Map;
import javax.persistence.PersistenceException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.fineract.infrastructure.core.api.JsonCommand;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResult;
import org.apache.fineract.infrastructure.core.data.CommandProcessingResultBuilder;
import org.apache.fineract.infrastructure.core.exception.PlatformDataIntegrityException;
import org.apache.fineract.infrastructure.security.service.PlatformSecurityContext;
import org.apache.fineract.portfolio.charge.service.ChargeWritePlatformServiceJpaRepositoryImpl;
import org.apache.fineract.portfolio.creditscorecard.domain.CreditScorecardFeature;
import org.apache.fineract.portfolio.creditscorecard.domain.CreditScorecardFeatureRepository;
import org.apache.fineract.portfolio.creditscorecard.exception.FeatureCannotBeDeletedException;
import org.apache.fineract.portfolio.creditscorecard.exception.FeatureNotFoundException;
import org.apache.fineract.portfolio.creditscorecard.serialization.CreditScorecardApiJsonDeserializer;
import org.apache.fineract.portfolio.loanaccount.domain.Loan;
import org.apache.fineract.portfolio.loanaccount.domain.LoanRepository;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProduct;
import org.apache.fineract.portfolio.loanproduct.domain.LoanProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditScorecardWritePlatformServiceImpl implements CreditScorecardWritePlatformService {

    private static final Logger LOG = LoggerFactory.getLogger(ChargeWritePlatformServiceJpaRepositoryImpl.class);
    private final PlatformSecurityContext context;

    private final LoanRepository loanRepository;
    private final LoanProductRepository loanProductRepository;
    private final CreditScorecardApiJsonDeserializer fromApiJsonDeserializer;
    private final CreditScorecardFeatureRepository featureRepository;

    @Autowired
    public CreditScorecardWritePlatformServiceImpl(final PlatformSecurityContext context,
            final CreditScorecardApiJsonDeserializer fromApiJsonDeserializer, final LoanRepository loanRepository,
            final LoanProductRepository loanProductRepository, final CreditScorecardFeatureRepository featureRepository) {
        this.context = context;
        this.fromApiJsonDeserializer = fromApiJsonDeserializer;
        this.loanRepository = loanRepository;
        this.loanProductRepository = loanProductRepository;
        this.featureRepository = featureRepository;
    }

    @Transactional
    @Override
    @CacheEvict(value = "scoring_features", key = "T(org.apache.fineract.infrastructure.core.service.ThreadLocalContextUtil).getTenant().getTenantIdentifier().concat('scf')")
    public CommandProcessingResult createScoringFeature(JsonCommand command) {

        try {
            this.context.authenticatedUser();
            this.fromApiJsonDeserializer.validateFeatureForCreate(command.json());

            final CreditScorecardFeature scorecardFeature = CreditScorecardFeature.fromJson(command);
            this.featureRepository.save(scorecardFeature);

            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(scorecardFeature.getId()).build();
        } catch (final JpaSystemException | DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(command, dve.getMostSpecificCause(), dve);
            return CommandProcessingResult.empty();
        } catch (final PersistenceException dve) {
            Throwable throwable = ExceptionUtils.getRootCause(dve.getCause());
            handleDataIntegrityIssues(command, throwable, dve);
            return CommandProcessingResult.empty();
        }
    }

    @Transactional
    @Override
    @CacheEvict(value = "scoring_features", key = "T(org.apache.fineract.infrastructure.core.service.ThreadLocalContextUtil).getTenant().getTenantIdentifier().concat('scf')")
    public CommandProcessingResult updateScoringFeature(Long featureId, JsonCommand command) {

        try {
            this.context.authenticatedUser();
            this.fromApiJsonDeserializer.validateFeatureForUpdate(command.json());

            final CreditScorecardFeature featureForUpdate = this.featureRepository.findById(featureId)
                    .orElseThrow(() -> new FeatureNotFoundException(featureId));

            final Map<String, Object> changes = featureForUpdate.update(command);

            if (!changes.isEmpty()) {
                this.featureRepository.save(featureForUpdate);
            }

            return new CommandProcessingResultBuilder().withCommandId(command.commandId()).withEntityId(featureId).with(changes).build();
        } catch (final JpaSystemException | DataIntegrityViolationException dve) {
            handleDataIntegrityIssues(command, dve.getMostSpecificCause(), dve);
            return CommandProcessingResult.empty();
        } catch (final PersistenceException dve) {
            Throwable throwable = ExceptionUtils.getRootCause(dve.getCause());
            handleDataIntegrityIssues(command, throwable, dve);
            return CommandProcessingResult.empty();
        }
    }

    @Transactional
    @Override
    @CacheEvict(value = "scoring_features", key = "T(org.apache.fineract.infrastructure.core.service.ThreadLocalContextUtil).getTenant().getTenantIdentifier().concat('scf')")
    public CommandProcessingResult deleteScoringFeature(Long entityId) {

        this.context.authenticatedUser();

        final CreditScorecardFeature featureForDelete = this.featureRepository.findById(entityId)
                .orElseThrow(() -> new FeatureNotFoundException(entityId));
        if (featureForDelete.isDeleted()) {
            throw new FeatureNotFoundException(entityId);
        }

        final Collection<LoanProduct> loanProducts = this.loanProductRepository.retrieveLoanProductsByScorecardFeatureId(entityId);
        final Collection<Loan> loans = this.loanRepository.retrieveLoansByScorecardFeatureId(entityId);

        if (!loanProducts.isEmpty() || !loans.isEmpty()) {
            throw new FeatureCannotBeDeletedException("error.msg.scorecard.feature.cannot.be.deleted.it.is.already.used.in.loan",
                    "This Scoring Feature cannot be deleted, it is already used in loan");
        }

        featureForDelete.delete();

        this.featureRepository.save(featureForDelete);

        return new CommandProcessingResultBuilder().withEntityId(featureForDelete.getId()).build();
    }

    /*
     * Guaranteed to throw an exception no matter what the data integrity issue is.
     */
    private void handleDataIntegrityIssues(final JsonCommand command, final Throwable realCause, final Exception dve) {

        if (realCause.getMessage().contains("name")) {
            final String name = command.stringValueOfParameterNamed("name");
            throw new PlatformDataIntegrityException("error.msg.scorecard.feature.duplicate.name",
                    "Scorecard Feature with name `" + name + "` already exists", "name", name);
        }

        LOG.error("Error occured.", dve);
        throw new PlatformDataIntegrityException("error.msg.scorecard.feature.unknown.data.integrity.issue",
                "Unknown data integrity issue with resource: " + realCause.getMessage());
    }

}
