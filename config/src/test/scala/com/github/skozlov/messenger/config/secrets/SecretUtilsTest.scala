package com.github.skozlov.messenger.config.secrets

import com.github.skozlov.messenger.commons.test.Test

import java.nio.file.Paths

class SecretUtilsTest extends Test {
  test("TemplateObjectName") {
    TemplateObjectName shouldBe "SecretProfileTemplate"
  }

  test("profileNameToObjectName") {
    profileNameToObjectName("test") shouldBe "Test"
  }

  test("profilePath") {
    profilePath(
      profileName = "test",
      moduleSourceBaseDir = Paths.get("moduleSourceBaseDir"),
    ) shouldBe Paths.get(
      "moduleSourceBaseDir",
      "com",
      "github",
      "skozlov",
      "messenger",
      "config",
      "secrets",
      "profiles",
      "Test.scala",
    )
  }
}
