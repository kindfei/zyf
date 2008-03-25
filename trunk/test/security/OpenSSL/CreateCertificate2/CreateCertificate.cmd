@echo off

set openssl=E:\Service\Apache2.2\bin\openssl.exe
set home=E:/Service/Apache2.2/conf/CreateCertificate2
set gendir=result
set genpath=%home%/%gendir%
set server_name=server
set user_name=user
set root_ca_conf=%genpath%\root-ca.conf
set server_cert_conf=%genpath%\server-cert.conf
set user_cert_conf=%genpath%\user-cert.conf
set ca_conf=%genpath%\ca.conf

mkdir %gendir%

if %1.==new-root-ca. goto new-root-ca
if %1.==new-server-cert. goto new-server-cert
if %1.==new-user-cert. goto new-user-cert
if %1.==sign-server-cert. goto sign-server-cert
if %1.==sign-user-cert. goto sign-user-cert

goto exit




:new-root-ca
if not exist %genpath%/ca.key (
echo No %genpath%/ca.key. generating one.
%openssl% genrsa -des3 -out %genpath%/ca.key 1024 -rand random-bits
) else (
echo You have %genpath%/ca.key already.
)

echo Create %root_ca_conf%.
echo [ req ]>%root_ca_conf%
echo default_bits                      = 1024>>%root_ca_conf%
echo default_keyfile                   = ca.key>>%root_ca_conf%
echo distinguished_name                = req_distinguished_name>>%root_ca_conf%
echo x509_extensions                   = v3_ca>>%root_ca_conf%
echo string_mask                       = nombstr>>%root_ca_conf%
echo req_extensions                    = v3_req>>%root_ca_conf%
echo [ req_distinguished_name ]>>%root_ca_conf%
echo countryName                       = Country Name (2 letter code)>>%root_ca_conf%
echo countryName_default               = MY>>%root_ca_conf%
echo countryName_min                   = 2>>%root_ca_conf%
echo countryName_max                   = 2>>%root_ca_conf%
echo stateOrProvinceName               = State or Province Name (full name)>>%root_ca_conf%
echo stateOrProvinceName_default       = Perak>>%root_ca_conf%
echo localityName                      = Locality Name (eg, city)>>%root_ca_conf%
echo localityName_default              = Sitiawan>>%root_ca_conf%
echo 0.organizationName                = Organization Name (eg, company)>>%root_ca_conf%
echo 0.organizationName_default        = My Directory Sdn Bhd>>%root_ca_conf%
echo organizationalUnitName            = Organizational Unit Name (eg, section)>>%root_ca_conf%
echo organizationalUnitName_default    = Certification Services Division>>%root_ca_conf%
echo commonName                        = Common Name (eg, MD Root CA)>>%root_ca_conf%
echo commonName_max                    = 64>>%root_ca_conf%
echo emailAddress                      = Email Address>>%root_ca_conf%
echo emailAddress_max                  = 40>>%root_ca_conf%
echo [ v3_ca ]>>%root_ca_conf%
echo basicConstraints                  = critical,CA:true>>%root_ca_conf%
echo subjectKeyIdentifier              = hash>>%root_ca_conf%
echo [ v3_req ]>>%root_ca_conf%
echo nsCertType                        = objsign,email,server>>%root_ca_conf%

echo Self-sign the root CA...
%openssl% req -new -x509 -days 3650 -config %root_ca_conf% -key %genpath%/ca.key -out %genpath%/ca.crt

echo Delete %root_ca_conf%
del "%root_ca_conf%"

goto exit




:new-server-cert
if not exist %genpath%/%server_name%.key (
echo No root %genpath%/%server_name%.key generating one.
%openssl% genrsa -out %genpath%/%server_name%.key 1024
) else (
echo You have %genpath%/%server_name%.key already.
)

echo Create %server_cert_conf%.
echo [ req ]>%server_cert_conf%
echo default_bits                      = 1024>>%server_cert_conf%
echo default_keyfile                   = %server_name%.key>>%server_cert_conf%
echo distinguished_name                = req_distinguished_name>>%server_cert_conf%
echo string_mask                       = nombstr>>%server_cert_conf%
echo req_extensions                    = v3_req>>%server_cert_conf%
echo [ req_distinguished_name ]>>%server_cert_conf%
echo countryName                       = Country Name (2 letter code)>>%server_cert_conf%
echo countryName_default               = MY>>%server_cert_conf%
echo countryName_min                   = 2>>%server_cert_conf%
echo countryName_max                   = 2>>%server_cert_conf%
echo stateOrProvinceName               = State or Province Name (full name)>>%server_cert_conf%
echo stateOrProvinceName_default       = Perak>>%server_cert_conf%
echo localityName                      = Locality Name (eg, city)>>%server_cert_conf%
echo localityName_default              = Sitiawan>>%server_cert_conf%
echo 0.organizationName                = Organization Name (eg, company)>>%server_cert_conf%
echo 0.organizationName_default        = My Directory Sdn Bhd>>%server_cert_conf%
echo organizationalUnitName            = Organizational Unit Name (eg, section)>>%server_cert_conf%
echo organizationalUnitName_default    = Secure Web Server>>%server_cert_conf%
echo commonName                        = Common Name (eg, www.domain.com)>>%server_cert_conf%
echo commonName_max                    = 64>>%server_cert_conf%
echo emailAddress                      = Email Address>>%server_cert_conf%
echo emailAddress_max                  = 40>>%server_cert_conf%
echo [ v3_req ]>>%server_cert_conf%
echo nsCertType                        = server>>%server_cert_conf%
echo basicConstraints                  = critical,CA:false>>%server_cert_conf%

echo "Fill in certificate data"
%openssl% req -new -config %server_cert_conf% -key %genpath%/%server_name%.key -out %genpath%/%server_name%.csr

echo Delete %server_cert_conf%
del "%server_cert_conf%"

goto exit




:new-user-cert
if not exist %genpath%/%user_name%.key (
echo No root %genpath%/%user_name%.key generating one.
%openssl% genrsa -out %genpath%/%user_name%.key 1024
) else (
echo You have %genpath%/%user_name%.key already.
)

echo Create %user_cert_conf%.
echo [ req ]>%user_cert_conf%
echo default_bits              = 1024>>%user_cert_conf%
echo default_keyfile           = user.key>>%user_cert_conf%
echo distinguished_name        = req_distinguished_name>>%user_cert_conf%
echo string_mask               = nombstr>>%user_cert_conf%
echo req_extensions            = v3_req>>%user_cert_conf%
echo [ req_distinguished_name ]>>%user_cert_conf%
echo commonName                = Common Name (eg, John Doe)>>%user_cert_conf%
echo commonName_max            = 64>>%user_cert_conf%
echo emailAddress              = Email Address>>%user_cert_conf%
echo emailAddress_max          = 40>>%user_cert_conf%
echo [ v3_req ]>>%user_cert_conf%
echo nsCertType                = client,email>>%user_cert_conf%
echo basicConstraints          = critical,CA:false>>%user_cert_conf%

echo Fill in certificate data
%openssl% req -new -config %user_cert_conf% -key %genpath%/%user_name%.key -out %genpath%/%user_name%.csr

echo Delete %user_cert_conf%
del "%user_cert_conf%"

goto exit




:sign-server-cert
if not exist %genpath%/db.serial echo 01>%genpath%/db.serial
if not exist %genpath%/db.index echo off>%genpath%/db.index

echo Create %ca_conf%.
echo [ ca ]>%ca_conf%
echo default_ca              = default_CA>>%ca_conf%
echo [ default_CA ]>>%ca_conf%
echo dir                     = %genpath%>>%ca_conf%
echo certs                   = $dir>>%ca_conf%
echo new_certs_dir           = $dir>>%ca_conf%
echo database                = $dir/db.index>>%ca_conf%
echo serial                  = $dir/db.serial>>%ca_conf%
echo RANDFILE                = $dir/random-bits>>%ca_conf%
echo certificate             = $dir/ca.crt>>%ca_conf%
echo private_key             = $dir/ca.key>>%ca_conf%
echo default_days            = 3650>>%ca_conf%
echo default_crl_days        = 30>>%ca_conf%
echo default_md              = md5>>%ca_conf%
echo preserve                = no>>%ca_conf%
echo x509_extensions         = server_cert>>%ca_conf%
echo policy                  = policy_anything>>%ca_conf%
echo [ policy_anything ]>>%ca_conf%
echo countryName             = optional>>%ca_conf%
echo stateOrProvinceName     = optional>>%ca_conf%
echo localityName            = optional>>%ca_conf%
echo organizationName        = optional>>%ca_conf%
echo organizationalUnitName  = optional>>%ca_conf%
echo commonName              = supplied>>%ca_conf%
echo emailAddress            = optional>>%ca_conf%
echo [ server_cert ]>>%ca_conf%
echo #subjectKeyIdentifier   = hash>>%ca_conf%
echo authorityKeyIdentifier  = keyid:always>>%ca_conf%
echo extendedKeyUsage        = serverAuth,clientAuth,msSGC,nsSGC>>%ca_conf%
echo basicConstraints        = critical,CA:false>>%ca_conf%

echo "CA signing: %genpath%/%server_name%.csr -> %genpath%/%server_name%.crt:"
%openssl% ca -config %ca_conf% -out %genpath%/%server_name%.crt -infiles %genpath%/%server_name%.csr
echo "CA verifying: %genpath%/%server_name%.crt <-> CA cert"
%openssl% verify -CAfile  %genpath%/ca.crt %genpath%/%server_name%.crt

echo cleanup after SSLeay
del "%ca_conf%"
del "%genpath%\db.serial.old"
del "%genpath%\db.index.old"
del "%genpath%\db.index.attr"

goto exit




:sign-user-cert
if not exist %genpath%/db.serial echo 01>%genpath%db.serial
if not exist %genpath%/db.index echo off>%genpath%db.index

echo Create %ca_conf%.
echo [ ca ]>%ca_conf%
echo default_ca              = default_CA>>%ca_conf%
echo [ default_CA ]>>%ca_conf%
echo dir                     = %genpath%>>%ca_conf%
echo certs                   = $dir>>%ca_conf%
echo new_certs_dir           = $dir>>%ca_conf%
echo database                = $dir/db.index>>%ca_conf%
echo serial                  = $dir/db.serial>>%ca_conf%
echo RANDFILE                = $dir/random-bits>>%ca_conf%
echo certificate             = $dir/ca.crt>>%ca_conf%
echo private_key             = $dir/ca.key>>%ca_conf%
echo default_days            = 3650>>%ca_conf%
echo default_crl_days        = 30>>%ca_conf%
echo default_md              = md5>>%ca_conf%
echo preserve                = yes>>%ca_conf%
echo x509_extensions         = user_cert>>%ca_conf%
echo policy                  = policy_anything>>%ca_conf%
echo [ policy_anything ]>>%ca_conf%
echo commonName              = supplied>>%ca_conf%
echo emailAddress            = supplied>>%ca_conf%
echo [ user_cert ]>>%ca_conf%
echo #SXNetID                = 3:yeak>>%ca_conf%
echo subjectAltName          = email:copy>>%ca_conf%
echo basicConstraints        = critical,CA:false>>%ca_conf%
echo authorityKeyIdentifier  = keyid:always>>%ca_conf%
echo extendedKeyUsage        = clientAuth,emailProtection>>%ca_conf%

echo "CA signing: %genpath%/%user_name%.csr -> %genpath%/%user_name%.crt:"
%openssl% ca -config %ca_conf% -out %genpath%/%user_name%.crt -infiles %genpath%/%user_name%.csr
echo "CA verifying: %genpath%/%user_name%.crt <-> CA cert"
%openssl% verify -CAfile %genpath%/ca.crt %genpath%/%user_name%.crt

echo cleanup after SSLeay 
del "%ca_conf%"
del "%genpath%\db.serial.old"
del "%genpath%\db.index.old"
del "%genpath%\db.index.attr"

goto exit




:exit
echo Press any key to exit.
pause > nul