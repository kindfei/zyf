drop table if exists JMS_DESTINATION;
CREATE TABLE JMS_DESTINATION (
  DESTINATION_NAME varchar(20) PRIMARY KEY NOT NULL,
  PROVIDER_ID varchar(20) NOT NULL
);

INSERT INTO JMS_DESTINATION (DESTINATION_NAME,PROVIDER_ID) VALUES ('queue/testQueue','provider1');
INSERT INTO JMS_DESTINATION (DESTINATION_NAME,PROVIDER_ID) VALUES ('topic/testTopic','provider3');

drop table if exists JMS_PROVIDER;
CREATE TABLE JMS_PROVIDER (
  PROVIDER_ID varchar(20) PRIMARY KEY NOT NULL,
  PROVIDER_TYPE varchar(20) NOT NULL,
  MANUAL_HA decimal(1,0) NOT NULL,
  URL varchar(200) NOT NULL,
  USER varchar(20),
  PASSWORD varchar(20)
);

INSERT INTO JMS_PROVIDER VALUES ('provider1','JBossMQ',1,'jnp://localhost:1099,jnp://10.4.6.218:1099',null,null);
INSERT INTO JMS_PROVIDER VALUES ('provider2','JBossMQ',1,'jnp://localhost:1099,jnp://10.4.6.218:1099',null,null);
INSERT INTO JMS_PROVIDER VALUES ('provider3','ActiveMQ',0,'failover://(tcp://localhost:61616,tcp://10.4.6.218:61616)',null,null);
INSERT INTO JMS_PROVIDER VALUES ('provider4','ActiveMQ',0,'failover://(tcp://localhost:61616,tcp://10.4.6.218:61616)',null,null);
INSERT INTO JMS_PROVIDER VALUES ('provider5','JBossMessaging',0,'localhost:1100,10.4.6.218:1100',null,null);
INSERT INTO JMS_PROVIDER VALUES ('provider6','JBossMessaging',0,'localhost:1100,10.4.6.218:1100',null,null);
