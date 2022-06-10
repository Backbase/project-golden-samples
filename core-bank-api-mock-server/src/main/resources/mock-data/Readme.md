### Mock Data

This folder contains the mock data used by the [Mock Services](../../java/com/backbase/openbanking/mockserver/service).

Mock Services expect their mock data to be placed in a specific folder, files be named on a specific way
and have a specific format, which is a JSON object with two main fields:
- `status-code`: Status code to use for the mock service response
- `response`: Body for the mock service response. It's the actual response from the backend, excluding headers.


#### How does it work

- A request, let's say, to list accounts is received.
- `AccountsController` invokes `AccountsMockService`.
- The `AccountsMockService`:
  - Gets the customer number from the request
  - Goes to `accounts` folder
  - Reads `accounts-<customer number>.json` file
  - "status-code" from the file is used to set the status code in the response
  - "response" object from the file is used to set the body of the response
 
Both `accounts` folder and filename prefix are known by the service.

#### Settings

In [application.yaml](./../application.yaml) file you can configure:

- `cookbook.mockdata-path`: Location of mock data folder
- `cookbook.file-extension`: File extension for the files. Currently "json" is used.
- `cookbook.filename-separator`: File name separator. Currently "-" is used.

