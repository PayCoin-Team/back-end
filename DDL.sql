CREATE DATABASE IF NOT EXISTS paycoin; -- 혹은 test 등 본인의 DB 이름
USE paycoin;

-- 데이터베이스 초기화 스크립트
-- 사용법: MySQL Workbench나 터미널에서 실행

-- 1. exchange_rate
CREATE TABLE `exchange_rate` (
  `target_currency` varchar(255) NOT NULL,
  `rate` decimal(38,2) NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  PRIMARY KEY (`target_currency`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 2. polling
CREATE TABLE `polling` (
  `id` bigint NOT NULL,
  `updated_at` datetime(6) NOT NULL,
  `last_timestamp` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 3. user
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ROLE_ADMIN','ROLE_BANNED','ROLE_USER','WITH_DRAW') DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 4. verification
CREATE TABLE `verification` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `difference` enum('DIFFER','EQUAL') NOT NULL,
  `server_balance` decimal(18,6) NOT NULL,
  `total_fee` decimal(18,6) NOT NULL,
  `user_balance` decimal(18,6) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `verification_chk_1` CHECK ((`difference` between 0 and 1))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 5. external_wallet
CREATE TABLE `external_wallet` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK3mmeyptj5wq1kru21u8u8qbvy` (`user_id`),
  CONSTRAINT `FK3mmeyptj5wq1kru21u8u8qbvy` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 6. nonce
CREATE TABLE `nonce` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `expired_at` datetime(6) NOT NULL,
  `nonce` varchar(255) NOT NULL,
  `wallet_address` varchar(255) NOT NULL,
  `user_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKreo8og2sirhs2b22kmqvhuc57` (`nonce`),
  KEY `FKm3a49vvegcafhiqnr773rnhxw` (`user_id`),
  CONSTRAINT `FKm3a49vvegcafhiqnr773rnhxw` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 7. transaction
CREATE TABLE `transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `amount` decimal(18,6) NOT NULL,
  `fee` decimal(18,6) NOT NULL,
  `from_address` varchar(64) DEFAULT NULL,
  `status` enum('COMPLETED','FAILED','PENDING','PROCESSING') NOT NULL,
  `to_address` varchar(64) NOT NULL,
  `txid` varchar(100) DEFAULT NULL,
  `type` enum('DEPOSIT','WITHDRAW') NOT NULL,
  `externalwallet` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnu8juh2rhmko37oydi5vtc2p6` (`externalwallet`),
  CONSTRAINT `FKnu8juh2rhmko37oydi5vtc2p6` FOREIGN KEY (`externalwallet`) REFERENCES `external_wallet` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 8. user_wallet
CREATE TABLE `user_wallet` (
  `user_id` bigint NOT NULL,
  `balance` decimal(18,6) DEFAULT NULL,
  `public_address` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FK5wbfrr7pvvooqb01ko72t6ewg` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 9. history
CREATE TABLE `history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `amount` decimal(18,6) DEFAULT NULL,
  `receive_wallet_id` bigint DEFAULT NULL,
  `send_wallet_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrjiq50narfg6m98e16cv3bmkp` (`receive_wallet_id`),
  KEY `FKhhde2ee8dq5qxvjv2480hp6ew` (`send_wallet_id`),
  CONSTRAINT `FKhhde2ee8dq5qxvjv2480hp6ew` FOREIGN KEY (`send_wallet_id`) REFERENCES `user_wallet` (`user_id`),
  CONSTRAINT `FKrjiq50narfg6m98e16cv3bmkp` FOREIGN KEY (`receive_wallet_id`) REFERENCES `user_wallet` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;