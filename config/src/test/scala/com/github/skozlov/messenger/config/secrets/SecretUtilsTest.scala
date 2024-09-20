package com.github.skozlov.messenger.config.secrets

import com.github.skozlov.messenger.commons.test.Test
import com.github.skozlov.messenger.config.secrets.Secrets.Secret

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

  test("templatePath") {
    templatePath(Paths.get("moduleSourceBaseDir")) shouldBe Paths.get(
      "moduleSourceBaseDir",
      "com",
      "github",
      "skozlov",
      "messenger",
      "config",
      "secrets",
      "SecretProfileTemplate.scala",
    )
  }

  test("insertSecrets") {
    insertSecrets(
      template = """
                   |override def A: Secret = ???
                   |override def B: Secret = ???
                   |""".stripMargin,
      secrets = Iterator(Secret("a"), Secret("b")),
    ) shouldBe
      """
        |override def A: Secret = Secret("a")
        |override def B: Secret = Secret("b")
        |""".stripMargin
  }
}
