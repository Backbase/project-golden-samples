# WireMock KeyStores

This will generate the WireMock self-signed [key-pair](wiremock/ca-cert.jks) and add its CA to a custom JVM [truststore](wiremock/cacerts).
```shell
cd wiremock
./generate-keystores.sh
```
> Scripts are based on the ones provided by WireMock [here](https://github.com/wiremock/wiremock/tree/master/scripts).
