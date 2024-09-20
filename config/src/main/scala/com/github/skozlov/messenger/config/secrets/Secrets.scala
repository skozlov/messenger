package com.github.skozlov.messenger.config.secrets

import Secrets._

trait Secrets {
  def DbPasswordAdmin: Secret
  def DbPasswordRest: Secret
}

object Secrets {
  opaque type Secret = String

  object Secret {
    def apply(value: String): Secret = value
  }

  extension (secret: Secret) {
    def value: String = secret
  }
}
