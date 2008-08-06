#!/bin/sh

# dso-env -q
TC_JAVA_OPTS=
-Xbootclasspath/p:"E:\Service\terracotta-2.6.2\bin\..\lib\dso-boot\dso-boot-hotspot_win32_150_14.jar" 
-Dtc.install-root="E:\Service\terracotta-2.6.2\bin\.."

# server
C:\Java\JDK1.6.0_04\bin\javaw.exe 
-Dtc.install-root=E:\Eclipse\plugin-common\terracotta\eclipse\plugins\org.terracotta.dso_2.6.2.r8952_v20080626 
-Dtc.config=E:\Eclipse\workspace-zyf\test-terracotta\tc-config.xml 
-Dtc.server.name=localhost 
-Dcom.sun.management.jmxremote 
-Dcom.sun.management.jmxremote.authenticate=false 
-Xms256m -Xmx256m 
-classpath E:\Eclipse\plugin-common\terracotta\eclipse\plugins\org.terracotta.dso_2.6.2.r8952_v20080626\lib\tc.jar 
-agentlib:jdwp=transport=dt_socket,suspend=y,address=localhost:2735 
com.tc.server.TCServerMain

# client
C:\Java\JDK1.6.0_04\bin\javaw.exe 
-Dtc.install-root=E:\Eclipse\plugin-common\terracotta\eclipse\plugins\org.terracotta.dso_2.6.2.r8952_v20080626 
-Dtc.config=E:\Eclipse\workspace-zyf\test-terracotta\src\test\terracota\demo\rmi\tc-config.xml 
-Xbootclasspath/p:E:\Eclipse\workspace-zyf\test-terracotta\dso-boot-hotspot_win32_160_04.jar 
-Dproject.name=test-terracotta 
-Dtc.node-name=node1 
-classpath E:\Eclipse\workspace-zyf\test-terracotta\bin test.terracota.demo.rmi.Main


# The specified Terracotta server will push its configuration to the Terracotta client.
# If <server_host1> is unavailable, <server_host2> is used.
-Dtc.config=<server_host1>:<dso-port>,<server_host2>:<dso-port>




# tc.properties set in server running

# default=false
-Dcom.tc.l2.l1reconnect.enabled=true

# default=5000
-Dcom.tc.l2.l1reconnect.timeout.millis=5000








