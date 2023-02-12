/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : geng_db

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2023-02-12 21:19:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tbl_geng
-- ----------------------------
DROP TABLE IF EXISTS `tbl_geng`;
CREATE TABLE `tbl_geng` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `resume` varchar(50) NOT NULL DEFAULT '',
  `tagIds` varchar(255) NOT NULL DEFAULT '',
  `src` varchar(255) NOT NULL DEFAULT '',
  `srcType` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `createTime` int(11) NOT NULL DEFAULT '0',
  `updateTime` int(11) NOT NULL DEFAULT '0',
  `deleted` int(2) NOT NULL DEFAULT '0',
  `version` int(5) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbl_geng
-- ----------------------------
INSERT INTO `tbl_geng` VALUES ('16', '23', '', '', '', '', '0', '0', '0', '1');
INSERT INTO `tbl_geng` VALUES ('17', '3342', '', '', '', '', '0', '0', '0', '1');

-- ----------------------------
-- Table structure for tbl_tag
-- ----------------------------
DROP TABLE IF EXISTS `tbl_tag`;
CREATE TABLE `tbl_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `tagName` varchar(50) NOT NULL DEFAULT '',
  `parentId` int(11) DEFAULT NULL,
  `tagIcon` varchar(255) NOT NULL DEFAULT '',
  `description` varchar(255) NOT NULL DEFAULT '',
  `createTime` int(11) NOT NULL DEFAULT '0',
  `updateTime` int(11) NOT NULL DEFAULT '0',
  `deleted` int(2) NOT NULL DEFAULT '0',
  `version` int(5) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbl_tag
-- ----------------------------
INSERT INTO `tbl_tag` VALUES ('13', 'tag1', null, '', '', '0', '0', '0', '1');
INSERT INTO `tbl_tag` VALUES ('14', 'tag2', '13', '', '', '0', '0', '0', '1');
INSERT INTO `tbl_tag` VALUES ('15', 'tag3', '13', '', '', '0', '0', '0', '1');
INSERT INTO `tbl_tag` VALUES ('16', 'tag4', '14', '', '', '0', '0', '0', '1');
INSERT INTO `tbl_tag` VALUES ('17', 'tag5', '14', '', '', '0', '0', '0', '1');
INSERT INTO `tbl_tag` VALUES ('18', 'tag6', '15', '', '', '0', '0', '0', '1');
INSERT INTO `tbl_tag` VALUES ('19', 'tag7', '15', '', '', '0', '0', '0', '1');
