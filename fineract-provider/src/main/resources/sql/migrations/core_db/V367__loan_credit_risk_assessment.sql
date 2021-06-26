--
-- Licensed to the Apache Software Foundation (ASF) under one
-- or more contributor license agreements. See the NOTICE file
-- distributed with this work for additional information
-- regarding copyright ownership. The ASF licenses this file
-- to you under the Apache License, Version 2.0 (the
-- "License"); you may not use this file except in compliance
-- with the License. You may obtain a copy of the License at
--
-- http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing,
-- software distributed under the License is distributed on an
-- "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
-- KIND, either express or implied. See the License for the
-- specific language governing permissions and limitations
-- under the License.
--

-- INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'ASSESS_CREDIT_RISK', 'CREDIT_RISK', 'ASSESS', 0);

CREATE TABLE `m_loan_scorecard`
(
    `id`                    BIGINT         NOT NULL AUTO_INCREMENT,
    `age`                   int                     DEFAULT NULL,
    `sex`                   varchar(50)             DEFAULT NULL,
    `job`                   varchar(50)             DEFAULT NULL,
    `housing`               varchar(50)             DEFAULT NULL,
    `credit_amount`         decimal(19, 6) NOT NULL DEFAULT '0.000000',
    `duration`              int                     DEFAULT NULL,
    `purpose`               varchar(50)             DEFAULT NULL,
    `predicted_risk`        varchar(50)             DEFAULT NULL,
    `actual_risk`           varchar(50)             DEFAULT NULL,
    `prediction_request_id` int                     DEFAULT NULL,
        PRIMARY KEY (`id`)
);

ALTER TABLE `m_loan` ADD `loan_scorecard_id` BIGINT DEFAULT NULL;
ALTER TABLE `m_loan` ADD FOREIGN KEY (`loan_scorecard_id`) REFERENCES `m_loan_scorecard`(`id`);
