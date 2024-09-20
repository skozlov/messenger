package com.github.skozlov.messenger.config.secrets

import com.github.skozlov.messenger.commons.test.Test

class SecretUtilsTest extends Test {
  test("profileNameToObjectName") {
    profileNameToObjectName("dev") shouldBe "Dev"
  }
}
