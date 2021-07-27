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

CREATE TABLE `m_credit_scorecard_feature` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `value_type_enum` SMALLINT NOT NULL,
    `data_type_enum` SMALLINT NOT NULL,
    `category_enum` SMALLINT NOT NULL,
    `is_active` tinyint NOT NULL,
    `is_deleted` tinyint NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;


INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'READ_CREDIT_SCORECARD_FEATURE', 'CREDIT_SCORECARD_FEATURE', 'READ', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'CREATE_CREDIT_SCORECARD_FEATURE', 'CREDIT_SCORECARD_FEATURE', 'CREATE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'DELETE_CREDIT_SCORECARD_FEATURE', 'CREDIT_SCORECARD_FEATURE', 'DELETE', 0);
INSERT INTO `m_permission` (`grouping`, `code`, `entity_name`, `action_name`, `can_maker_checker`) VALUES ('portfolio', 'UPDATE_CREDIT_SCORECARD_FEATURE', 'CREDIT_SCORECARD_FEATURE', 'UPDATE', 0);


CREATE TABLE `m_loan_product_scorecard_feature` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `loan_product_id` BIGINT NOT NULL,
    `scorecard_feature_id` BIGINT NOT NULL,

    `weightage` DECIMAL(6,5) NOT NULL DEFAULT '0.000000',
    `green_min` INT NOT NULL,
    `green_max` INT NOT NULL,
    `amber_min` INT NOT NULL,
    `amber_max` INT NOT NULL,
    `red_min` INT NOT NULL,
    `red_max` INT NOT NULL,

    PRIMARY KEY (`id`),
    KEY `scorecard_feature_id` (`scorecard_feature_id`),
    KEY `m_loan_product_scorecard_feature_ibfk_2` (`loan_product_id`),
    CONSTRAINT `m_loan_product_scorecard_feature_ibfk_1` FOREIGN KEY (`scorecard_feature_id`) REFERENCES `m_credit_scorecard_feature` (`id`),
    CONSTRAINT `m_loan_product_scorecard_feature_ibfk_2` FOREIGN KEY (`loan_product_id`) REFERENCES `m_product_loan` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;


CREATE TABLE IF NOT EXISTS `m_credit_scorecard_feature_criteria` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `criteria` VARCHAR(20) NOT NULL,
    `score` DECIMAL(6,5) NOT NULL DEFAULT '0.000000',
    `loan_product_scorecard_feature_id` BIGINT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `m_credit_scorecard_feature_criteria_ibfk_1` FOREIGN KEY (`loan_product_scorecard_feature_id`) REFERENCES `m_loan_product_scorecard_feature`(`id`)
);


CREATE TABLE `m_loan_scorecard_feature` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `loan_scorecard_feature_id` BIGINT NOT NULL,
    `loan_product_scorecard_feature_id` BIGINT NOT NULL,
    `feature_value` varchar(100) DEFAULT NULL,
    `score` DECIMAL(6,5) DEFAULT '0.000000',
    `color` varchar(50) DEFAULT NULL,


    PRIMARY KEY (`id`),
    KEY `loan_scorecard_feature_id` (`loan_scorecard_feature_id`),
    KEY `loan_product_scorecard_feature_id` (`loan_product_scorecard_feature_id`),
    CONSTRAINT `m_loan_scorecard_feature_ibfk_1` FOREIGN KEY (`loan_scorecard_feature_id`) REFERENCES `m_loan` (`id`),
    CONSTRAINT `m_loan_scorecard_feature_ibfk_2` FOREIGN KEY (`loan_product_scorecard_feature_id`) REFERENCES `m_loan_product_scorecard_feature` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

ALTER TABLE `m_loan`
    ADD COLUMN `loan_scorecard_feature_id` BIGINT NULL,
    ADD CONSTRAINT `FK_loan_scorecard_feature_m_loan_m_loan_scorecard_feature` FOREIGN KEY (`loan_scorecard_feature_id`) REFERENCES `m_loan_scorecard_feature` (`id`);


CREATE TABLE `m_credit_scorecard` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `scorecard_scoring_method` VARCHAR(100) NOT NULL,
    `scorecard_scoring_model` VARCHAR(100) NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

ALTER TABLE `m_loan`
    ADD COLUMN `loan_credit_scorecard_id` BIGINT NULL DEFAULT NULL,
    ADD CONSTRAINT `FK_credit_scorecard_m_loan_m_credit_scorecard` FOREIGN KEY (`loan_credit_scorecard_id`) REFERENCES `m_credit_scorecard` (`id`);


CREATE TABLE `m_ml_scorecard` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `age` INT DEFAULT NULL,
    `sex` VARCHAR(50) DEFAULT NULL,
    `job` VARCHAR(50) DEFAULT NULL,
    `housing` VARCHAR(50) DEFAULT NULL,
    `credit_amount` DECIMAL(19, 6) NOT NULL DEFAULT '0.000000',
    `duration` INT DEFAULT NULL,
    `purpose` VARCHAR(50) DEFAULT NULL,
    `predicted_risk` VARCHAR(50) DEFAULT NULL,
    `actual_risk` VARCHAR(50) DEFAULT NULL,
    `prediction_request_id` INT DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=UTF8MB4;

ALTER TABLE `m_loan`
    ADD COLUMN `loan_ml_scorecard_id` BIGINT NULL DEFAULT NULL,
    ADD CONSTRAINT `FK_credit_ml_scorecard_m_loan_m_ml_scorecard` FOREIGN KEY (`loan_ml_scorecard_id`) REFERENCES `m_ml_scorecard` (`id`);
