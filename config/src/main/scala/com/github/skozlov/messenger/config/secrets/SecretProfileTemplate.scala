package com.github.skozlov.messenger.config.secrets

import com.github.skozlov.messenger.config.secrets.Secrets.Secret

object SecretProfileTemplate
    extends com.github.skozlov.messenger.config.secrets.Secrets {
  override val DbPasswordAdmin: Secret = ???
  override val DbPasswordRest: Secret = ???
}
