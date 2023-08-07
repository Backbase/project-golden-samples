# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0]
### Changed
- [XXX-188](https://backbase.atlassian.net/browse/XXX-188): Added hard token config
> mandatory property added
```yaml
identity-server:
  backbaseidentity:
    env:
      XXX:
        status: ADDED
        value: "http://confirmation:8080"
```
