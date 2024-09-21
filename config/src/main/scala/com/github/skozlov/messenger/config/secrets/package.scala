package com.github.skozlov.messenger.config

import com.github.skozlov.messenger.commons.lang.file.{
  createDirectories,
  createFile,
  javaPackagePath,
}
import com.github.skozlov.messenger.config.secrets.Secrets.Secret
import com.github.skozlov.messenger.core.DefaultCharset
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FileUtils.{readFileToString, writeStringToFile}
import org.apache.commons.lang3.StringUtils.capitalize

import java.nio.file.Files.exists
import java.nio.file.{Files, Path}
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

  def templateToProfile(
      template: String,
      profileName: String,
      secrets: Iterator[Secret],
  ): String = {
    insertSecrets(template, secrets)
      .replace(
        "package " + SecretProfileTemplate.getClass.getPackageName,
        "package " + SecretProfilePackage,
      )
      .replace(
        "object " + TemplateObjectName,
        "object " + profileNameToObjectName(profileName),
      )
  }

  def updateProfile(path: Path, secrets: Iterator[Secret]): Unit = {
    val file = path.toFile
    val template = readFileToString(file, DefaultCharset)
    val finalContent = insertSecrets(template, secrets)
    if (finalContent != template) {
      writeStringToFile(file, finalContent, DefaultCharset)
    }
  }

  def createProfile(
      profileName: String,
      profilePath: Path,
      templatePath: Path,
      secrets: Iterator[Secret],
  ): Unit = {
    val template = readFileToString(templatePath.toFile, DefaultCharset)
    val profileContent = templateToProfile(template, profileName, secrets)
    val profileDirectory = profilePath.getParent
    if (!exists(profileDirectory)) {
      createDirectories(profileDirectory, "rwx------")
    }
    createFile(profilePath, "rw-------")
    writeStringToFile(profilePath.toFile, profileContent, DefaultCharset, true)
  }

  def prepareProfileFile(
      profileName: String,
      moduleSourceBaseDir: Path,
      secrets: Iterator[Secret],
  ): Unit = {
    val _profilePath = profilePath(profileName, moduleSourceBaseDir)
    if (exists(_profilePath)) {
      updateProfile(_profilePath, secrets)
    } else {
      createProfile(
        profileName,
        _profilePath,
        templatePath(moduleSourceBaseDir),
        secrets,
      )
    }
  }
}
