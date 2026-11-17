/*
 Navicat Premium Dump SQL

 Source Server         : 作业
 Source Server Type    : MySQL
 Source Server Version : 80407 (8.4.7)
 Source Host           : localhost:3306
 Source Schema         : sp500

 Target Server Type    : MySQL
 Target Server Version : 80407 (8.4.7)
 File Encoding         : 65001

 Date: 17/11/2026 20:45:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gspc
-- ----------------------------
DROP TABLE IF EXISTS `gspc`;
CREATE TABLE `gspc`  (
  `date` date NOT NULL,
  `open` double NULL DEFAULT NULL,
  `high` double NULL DEFAULT NULL,
  `low` double NULL DEFAULT NULL,
  `close` double NULL DEFAULT NULL,
  `adj_close` double NULL DEFAULT NULL,
  `volume` double NULL DEFAULT NULL,
  PRIMARY KEY (`date`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
