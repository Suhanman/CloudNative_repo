
/* chat_message 테이블 생성*/
CREATE TABLE `chat_message` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`sender` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
	`receiver` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
	`message` TEXT NOT NULL COLLATE 'utf8mb4_general_ci',
	`timestamp` DATETIME NULL DEFAULT (CURRENT_TIMESTAMP),
	`read_status` TINYINT NULL DEFAULT '0',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=43
;

/* idealprofile 테이블 생성*/
CREATE TABLE `idealprofile` (
	`id` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`ideal_mbti` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`ideal_smoke` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`ideal_body` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`ideal_style` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`id`) USING BTREE,
	CONSTRAINT `idealprofile_ibfk_1` FOREIGN KEY (`id`) REFERENCES `member` (`id`) ON UPDATE RESTRICT ON DELETE CASCADE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

/* member 테이블 생성*/
CREATE TABLE `member` (
	`id` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`pw` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
	`username` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`mobile` VARCHAR(20) NOT NULL COLLATE 'utf8mb4_general_ci',
	`gender` ENUM('남성','여성') NOT NULL COLLATE 'utf8mb4_general_ci',
	`age` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`imageUrl` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`home` VARCHAR(100) NOT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

/* myprofile 테이블 생성*/
CREATE TABLE `myprofile` (
	`id` VARCHAR(50) NOT NULL COLLATE 'utf8mb4_general_ci',
	`mbti` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`smoke` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`body` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`style` VARCHAR(10) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`id`) USING BTREE,
	CONSTRAINT `myprofile_ibfk_1` FOREIGN KEY (`id`) REFERENCES `member` (`id`) ON UPDATE RESTRICT ON DELETE CASCADE
)
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;
