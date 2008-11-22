drop table if exists DESTINATION_GROUP;
CREATE TABLE DESTINATION_GROUP (
  DESTINATION_NAME varchar(20) PRIMARY KEY NOT NULL,
  GROUP_ID varchar(20) NOT NULL
);

INSERT INTO destination_group (DESTINATION_NAME,GROUP_ID) VALUES ('queue/testQueue','group1');
INSERT INTO destination_group (DESTINATION_NAME,GROUP_ID) VALUES ('topic/testTopic','group2');

drop table if exists JMS_PROVIDER;
CREATE TABLE JMS_PROVIDER (
  PROVIDER_TYPE varchar(20) NOT NULL,
  GROUP_ID varchar(20) NOT NULL,
  PROVIDER_URL varchar(200) NOT NULL,
  MANUAL_HA decimal(1,0) NOT NULL,
  USER varchar(20),
  PASSWORD varchar(20),
  PRIMARY KEY (PROVIDER_TYPE, GROUP_ID)
);

INSERT INTO jms_provider VALUES ('jbossmq','group1','jnp://localhost:1099,jnp://10.4.6.218:1099',1,null,null);
INSERT INTO jms_provider VALUES ('jbossmq','group2','jnp://localhost:1099,jnp://10.4.6.218:1099',1,null,null);
INSERT INTO jms_provider VALUES ('activemq','group1','failover://(tcp://localhost:61616,tcp://10.4.6.218:61616)',0,null,null);
INSERT INTO jms_provider VALUES ('activemq','group2','failover://(tcp://localhost:61616,tcp://10.4.6.218:61616)',0,null,null);
INSERT INTO jms_provider VALUES ('jbossmessaging','group1','localhost:1100,10.4.6.218:1100',0,null,null);
INSERT INTO jms_provider VALUES ('jbossmessaging','group2','localhost:1100,10.4.6.218:1100',0,null,null);
