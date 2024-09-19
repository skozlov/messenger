# Messenger

A simple messenger created to prove my ideas on build & deployment automation and so on.

## Technologies

|                      |                |
|----------------------|----------------|
| Programming language | Scala 3        |
| Runtime environment  | JVM 21+        |
| Build tool           | SBT            |
| CI environment       | Github Actions |

## Configuration

### Secrets

Secrets are configuration parameters that should be kept in secret: passwords, private keys, etc.
Secret value is a UUIDv4 without hyphens, e.g. `bd125bece0ed485cae94ee1d7b646bf0`.

`com.github.skozlov.messenger.config.secrets.Secrets` trait defines which secrets are used in configuration.
In Scala code, secrets are represented as instances of
`com.github.skozlov.messenger.config.secrets.Secrets.Secret` opaque type.

Every deployment environment has its own secret profile that defines secret values.
Secret profile with name `abc` is represented
as a Scala object `com.github.skozlov.messenger.config.secrets.profiles.Abc`
that extends `com.github.skozlov.messenger.config.secrets.Secrets`.
It should not be added to Git.
