package com.github.skozlov.messenger.config.secrets

import com.github.skozlov.messenger.commons.test.Test
import com.github.skozlov.messenger.config.secrets.Secrets.Secret
import com.github.skozlov.messenger.core.DefaultCharset
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FileUtils.{
  deleteDirectory,
  readFileToString,
  writeStringToFile,
}
import org.scalatest.BeforeAndAfterEach

import java.nio.file.Files.{createDirectories, createTempDirectory, exists}
import java.nio.file.{Files, Path}

class SecretProfileGenerationTest extends Test with BeforeAndAfterEach {
  private val profileName = "test"
  private var baseDir: Path = scala.compiletime.uninitialized
  private var profilePath: Path = scala.compiletime.uninitialized
  private val expectedProfileContent =
    """package com.github.skozlov.messenger.config.secrets.profiles
      |
      |import com.github.skozlov.messenger.config.secrets.Secrets.Secret
      |
      |object Test
      |    extends com.github.skozlov.messenger.config.secrets.Secrets {
      |  override def DbPasswordAdmin: Secret = Secret("1")
      |  override def DbPasswordRest: Secret = Secret("2")
      |}
      |
      |""".stripMargin

  override protected def beforeEach(): Unit = {
    baseDir = createTempDirectory("Messenger_SecretProfileGenerationTest_")
    val templatePath = baseDir.resolve(
      "com/github/skozlov/messenger/config/secrets/SecretProfileTemplate.scala"
        .replace("/", baseDir.getFileSystem.getSeparator)
    )
    createDirectories(templatePath.getParent)
    val templateContent =
      """package com.github.skozlov.messenger.config.secrets
        |
        |import com.github.skozlov.messenger.config.secrets.Secrets.Secret
        |
        |object SecretProfileTemplate
        |    extends com.github.skozlov.messenger.config.secrets.Secrets {
        |  override def DbPasswordAdmin: Secret = ???
        |  override def DbPasswordRest: Secret = ???
        |}
        |
        |""".stripMargin
    writeStringToFile(
      templatePath.toFile,
      templateContent,
      DefaultCharset,
      true,
    )
    profilePath = baseDir.resolve(
      "com/github/skozlov/messenger/config/secrets/profiles/Test.scala"
        .replace("/", baseDir.getFileSystem.getSeparator)
    )
  }

  override protected def afterEach(): Unit = {
    if (exists(baseDir)) {
      println(
        "Temp directory was not deleted: " + baseDir.toAbsolutePath
      )
    }
  }

  test("from scratch") {
    prepareProfileFile(
      profileName,
      baseDir,
      secrets = Iterator(Secret("1"), Secret("2")),
    )
    readFileToString(
      profilePath.toFile,
      DefaultCharset,
    ) shouldBe expectedProfileContent

    deleteDirectory(baseDir.toFile)
  }

  test("from existing directory") {
    createDirectories(profilePath.getParent)

    prepareProfileFile(
      profileName,
      baseDir,
      secrets = Iterator(Secret("1"), Secret("2")),
    )
    readFileToString(
      profilePath.toFile,
      DefaultCharset,
    ) shouldBe expectedProfileContent

    deleteDirectory(baseDir.toFile)
  }

  test("from incomplete file") {
    createDirectories(profilePath.getParent)
    writeStringToFile(
      profilePath.toFile,
      """package com.github.skozlov.messenger.config.secrets.profiles
        |
        |import com.github.skozlov.messenger.config.secrets.Secrets.Secret
        |
        |object Test
        |    extends com.github.skozlov.messenger.config.secrets.Secrets {
        |  override def DbPasswordAdmin: Secret = Secret("1")
        |  override def DbPasswordRest: Secret = ???
        |}
        |
        |""".stripMargin,
      DefaultCharset,
      true,
    )

    prepareProfileFile(profileName, baseDir, secrets = Iterator(Secret("2")))
    readFileToString(
      profilePath.toFile,
      DefaultCharset,
    ) shouldBe expectedProfileContent

    deleteDirectory(baseDir.toFile)
  }

  test("from complete file") {
    createDirectories(profilePath.getParent)
    writeStringToFile(
      profilePath.toFile,
      expectedProfileContent,
      DefaultCharset,
      true,
    )

    prepareProfileFile(profileName, baseDir, secrets = Iterator.empty)
    readFileToString(
      profilePath.toFile,
      DefaultCharset,
    ) shouldBe expectedProfileContent

    deleteDirectory(baseDir.toFile)
  }
}
