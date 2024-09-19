package com.github.skozlov.messenger.config.secrets

import com.github.skozlov.messenger.commons.lang.file.{
  createDirectories,
  createFile,
}
import com.github.skozlov.messenger.core.DefaultCharset
import org.apache.commons.io.FileUtils.{readFileToString, writeStringToFile}
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.StringUtils.capitalize

import java.nio.file.Paths
import java.util.UUID.randomUUID
import java.util.{Locale, UUID}
import scala.annotation.tailrec

object SecretProfileGenerator {
  private val sourceCodeBasePath = "config/src/main/scala"
  private val profilePackage =
    "com.github.skozlov.messenger.config.secrets.profiles"
  private val directoryPosixPermissions = "rwx------"
  private val filePosixPermissions = "rw-------"
  private val templatePackage = "com.github.skozlov.messenger.config.secrets"
  private val templateObjectName = "SecretProfileTemplate"

  /**
   * Replaces each occurrence of `"???"` placeholder with `"""Secret("<UUIDv4 without hyphen>")"""`.
   *
   * This method is intended to be used only for short templates with small amount of placeholders,
   * otherwise it may be slow.
   */
  @tailrec
  private def substituteSecretValues(template: String): String = {
    val secretValue = randomUUID().toString.replace("-", "")
    val result = template.replaceFirst(
      """\Q???\E""",
      s"""Secret("$secretValue")""",
    )
    if (result == template) {
      result
    } else {
      substituteSecretValues(result)
    }
  }

  @main
  def generate(repositoryRoot: String, profileName: String): Unit = {
    val sourceCodeBaseDir = Paths.get(repositoryRoot, sourceCodeBasePath)
    val profilesDir =
      sourceCodeBaseDir.resolve(profilePackage.replace('.', '/'))
    val profileObjectName = capitalize(profileName.toLowerCase(Locale.ROOT))
    val profileFile = profilesDir.resolve(profileObjectName + ".scala")

    def prepareFileContent: String = {
      val templateFile =
        sourceCodeBaseDir
          .resolve(templatePackage.replace('.', '/'))
          .resolve(templateObjectName + ".scala")

      substituteSecretValues(
        readFileToString(templateFile.toFile, DefaultCharset)
          .replace("package " + templatePackage, "package " + profilePackage)
          .replace(
            "object " + templateObjectName,
            "object " + profileObjectName,
          )
      )
    }

    if (profileFile.toFile.exists()) {
      //todo
    } else {
      if (!profilesDir.toFile.exists()) {
        createDirectories(
          profilesDir,
          posixPermissions = directoryPosixPermissions,
        )
      }

      val fileContent = prepareFileContent
      createFile(profileFile, filePosixPermissions)
      writeStringToFile(
        profileFile.toFile,
        fileContent,
        DefaultCharset,
        true,
      )
    }
  }
}
