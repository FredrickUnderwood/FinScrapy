DROP TABLE if exists `user`;
CREATE TABLE `user` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `username` varchar(50) NOT NULL,
    `password` varchar(255) NOT NULL,
    `level` int NOT NULL default 0,
    `email` varchar(50) NOT NULL,
    `phone` varchar(50) NOT NULL,
    `created_time` timestamp NOT NULL,
    `status` int NOT NULL default 1,
    PRIMARY KEY (`id`),
    UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB;