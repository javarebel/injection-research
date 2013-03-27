injection-research
==================

For Learning and Practising JEE6 Features.

For building project
---------------------------------
Goto root folder and run mvn clean package

For deploying the ear file to GlassFish using asadmin
----------------------------------------------------------------
In Windows
D:\DEV\Softwares\glassfish-3.1.2.2\bin>asadmin deploy d:\IBM\JDK7Projects\Sample1\injection-research\injection-research-ear\target\injection-research-ear-1.0.ear

In Mac
/Users/naveensisupalan/Library/GlassFish_Server/bin/asadmin deploy injection-research-ear-1.0.ear

To undeploy application from GlassFish
------------------------------------------

In Windows
D:\DEV\Softwares\glassfish-3.1.2.2\bin>asadmin undeploy d:\IBM\JDK7Projects\Sample1\injection-research\injection-research-ear\target\injection-research-ear-1.0.ear

In Mac
/Users/naveensisupalan/Library/GlassFish_Server/bin/asadmin undeploy injection-research-ear-1.0