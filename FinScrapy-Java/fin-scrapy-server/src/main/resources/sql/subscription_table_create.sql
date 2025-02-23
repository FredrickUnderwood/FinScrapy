DROP TABLE if exists `subscription`;
CREATE TABLE `subscription` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` bigint NOT NULL,
    `keywords` varchar(255) NOT NULL,
    `push_notify` int NOT NULL,
    `email_notify` int NOT NULL,
    `status` int NOT NULL default 1,
    `created_time` timestamp NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;