package com.github.skozlov.messenger.config

import com.github.skozlov.messenger.commons.lang.file.javaPackagePath
import com.github.skozlov.messenger.config.secrets.Secrets.Secret
import org.apache.commons.lang3.StringUtils.capitalize

import java.nio.file.Path
import java.util.Locale
import java.util.regex.{Matcher, Pattern}

package object secrets {
  val SecretProfilePackage: String =
    "com.github.skozlov.messenger.config.secrets.profiles"

  lazy val TemplateObjectName: String =
    SecretProfileTemplate.getClass.getSimpleName.stripSuffix("$")

  def profileNameToObjectName(profileName: String): String = {
    capitalize(profileName.toLowerCase(Locale.ROOT))
  }

  def profilePath(profileName: String, moduleSourceBaseDir: Path): Path = {
    val fileName = profileNameToObjectName(profileName) + ".scala"
    javaPackagePath(SecretProfilePackage, moduleSourceBaseDir).resolve(fileName)
  }

  def templatePath(moduleSourceBaseDir: Path): Path = {
    val fileName = TemplateObjectName + ".scala"
    javaPackagePath(
      SecretProfileTemplate.getClass.getPackageName,
      moduleSourceBaseDir,
    ).resolve(fileName)
  }

  def insertSecrets(template: String, secrets: Iterator[Secret]): String = {
    Pattern
      .compile("""\Q???\E""")
      .matcher(template)
      .replaceAll(_ =>
        s"""Secret("${Matcher.quoteReplacement(secrets.next().value)}")"""
      )
  }
}
