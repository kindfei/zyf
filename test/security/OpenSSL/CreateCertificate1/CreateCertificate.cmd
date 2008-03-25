@echo off

set openssl=E:\Service\Apache2.2\bin\openssl.exe
set home=E:/Service/Apache2.2/conf/CreateCertificate1
set gendir=result
set conf=%home%/openssl.conf

mkdir %gendir%

if %1.==create-root-ca. goto create-root-ca
if %1.==create-server-cert. goto create-server-cert

goto exit




:create-root-ca
"%openssl%" genrsa -des3 -out %gendir%/ca.key 1024
"%openssl%" req -config %conf% -new -key %gendir%/ca.key -out %gendir%/ca.csr
"%openssl%" x509 -days 3650 -req -signkey %gendir%/ca.key -in %gendir%/ca.csr -out %gendir%/ca.crt

goto exit




:create-server-cert
if not exist %gendir%/db.serial echo 01>%gendir%/db.serial
if not exist %gendir%/db.index echo off>%gendir%/db.index

"%openssl%" genrsa -out %gendir%/server.key 1024
"%openssl%" req -config %conf% -new -key %gendir%/server.key -out %gendir%/server.csr
"%openssl%" ca -config %conf% -days 3650 -cert %gendir%/ca.crt -keyfile %gendir%/ca.key -in %gendir%/server.csr -out %gendir%/server.crt

goto exit




:exit
echo Press any key to exit.
pause > nul
