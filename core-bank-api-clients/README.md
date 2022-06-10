# Open Banking Clients
Open Banking API Documentation: https://openbankinguk.github.io/read-write-api-site3/v3.1.10/profiles/account-and-transaction-api-profile.html

## Overview
This project is used to generate the Open Banking Client Libraries used by the DBS integration services.

### How to generate the clients
If you have updated an existing specification or added a new, run the following command to generate the new client(s):

`mvn clean install -Pgenerate-clients`

Note: As the generated clients are stored in Git you are not required to run this command if you've made no changes.

### How to install the clients
To install the generated clients into you local Maven repository run:

`mvn clean install -Pinclude-clients`

> If you're facing error to generate java doc use this argument to skip api doc generation while installing artifacts `-Dmaven.javadoc.skip=true`

### Exercise
Generate the Payment Initiation API client from Open Banking API Spec [Open Banking API](https://github.com/OpenBankingUK/read-write-api-specs) and add it the this project.