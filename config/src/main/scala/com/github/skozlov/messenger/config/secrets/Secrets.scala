package com.github.skozlov.messenger.config.secrets

import Secrets._

trait Secrets {
  val DbPasswordAdmin: Secret
  val DbPasswordRest: Secret
}

object Secrets {
  opaque type Secret = String

  object Secret {
    def apply(value: String): Secret = value
  }
}
