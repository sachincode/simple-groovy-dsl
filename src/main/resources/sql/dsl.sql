
CREATE TABLE `T_dsl_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建的物理时间戳',
  `gmt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改的物理时间戳',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'dsl名称编码',
  `describe` varchar(64) NOT NULL DEFAULT '' COMMENT 'dsl描述',
  `content` LONGTEXT NOT NULL COMMENT 'dsl代码内容',
  `import_list` TEXT NOT NULL COMMENT 'import列表',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态，0禁用1启用2删除',
  `schedule_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '定时任务状态，0禁用1启用',
  `operator` varchar(64) NOT NULL DEFAULT '' COMMENT '操作人',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_name` (`name`),
  KEY `idx_gmt_modify` (`gmt_modify`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPRESSED COMMENT='dsl配置表';


CREATE TABLE `T_dsl_config_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'PK',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建的物理时间戳',
  `gmt_modify` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录修改的物理时间戳',
  `name` varchar(64) NOT NULL DEFAULT '' COMMENT 'dsl名称编码',
  `config` LONGTEXT NOT NULL COMMENT 'dsl配置内容',
  `operator` varchar(64) NOT NULL DEFAULT '' COMMENT '操作人',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号',
  `op_type` varchar(12) NOT NULL DEFAULT '' COMMENT '操作类型',
  `describe` varchar(200) NOT NULL DEFAULT '' COMMENT '操作描述',
  PRIMARY KEY (`id`),
  KEY `uq_name` (`name`),
  KEY `idx_gmt_create` (`gmt_create`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=COMPRESSED COMMENT='dsl配置历史记录表';
