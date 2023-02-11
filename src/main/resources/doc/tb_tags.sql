DROP TABLE IF EXISTS `tbl_tag`;
CREATE TABLE `tbl_tag`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `tagName`        varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' ,
    `parentId`        int(11) ,
    `tagIcon`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `createTime` int(11) NOT NULL NOT NULL DEFAULT 0,
    `updateTime` int(11) NOT NULL NOT NULL DEFAULT 0,
    `deleted`          int(2) NOT NULL DEFAULT 0,
    `version`          int(5) NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;


DROP TABLE IF EXISTS `tbl_geng`;
CREATE TABLE `tbl_geng`
(
    `id`          int(11) NOT NULL AUTO_INCREMENT,
    `resume`        varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '' ,
    `tagIds`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `src`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `srcType`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '',
    `createTime` int(11) NOT NULL NOT NULL DEFAULT 0,
    `updateTime` int(11) NOT NULL NOT NULL DEFAULT 0,
    `deleted`          int(2) NOT NULL DEFAULT 0,
    `version`          int(5) NOT NULL DEFAULT 1,
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;