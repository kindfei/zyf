DROP TABLE IF EXISTS `TEST`;

CREATE TABLE `TEST` (
  `ID` varchar(10) NOT NULL,
  `CONTENT` varchar(60) DEFAULT NULL,
  `VERSION` int(11) NOT NULL,
  `INPUT_STAFF_ID` varchar(10),
  `UPDATE_STAFF_ID` varchar(10),
  `INPUT_DATE` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `UPDATE_DATE` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=cp932;

INSERT TEST values ('001', 'c01', 0, null, null, current_timestamp, current_timestamp);


