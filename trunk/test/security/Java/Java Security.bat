
@rem Generating key pair.
keytool -genkey -dname "cn=Yifei Zhang, ou=FX Development Divsion, o=EdgeSoft, c=CH" -alias test -keypass 123456 -keystore TestKeyStore -storepass 888888 -validity 180

@rem Requesting a signed certificate.
keytool -certreq -alias test -keystore TestKeyStore -file Test.csr -storepass 888888 -keypass 123456

@rem Create RootCA.cer with OpenSSL.

@rem Importing a certificate for the CA.
keytool -import -alias rootca -file RootCA.cer -keystore TestKeyStore -storepass 888888

@rem Sign the Test.csr using RootCA private key with OpenSSL.

@rem Importing the certificate reply from the CA.
keytool -import -trustcacerts -alias test -file Test.cer -keystore TestKeyStore -storepass 888888 -keypass 123456

@rem Exporting a certificate authenticating public key.
keytool -export -alias test -file PublicKey.cer -keystore TestKeyStore -storepass 888888 -keypass 123456




@rem Show the default trusted certificates in cacerts file within JRE.
keytool -list -keystore cacerts -v -storepass changeit

@rem Create a self-signed certificate.
keytool -selfcert -alias globalsignca -dname "CN=GlobalSign Root CA, OU=Root CA, O=GlobalSign nv-sa, C=BE" -keypass 123456 -keystore TestKeyStore -storepass 888888




