package com.github.skozlov.messenger.config

import com.github.skozlov.messenger.commons.lang.file.javaPackagePath
import org.apache.commons.lang3.StringUtils.capitalize

import java.nio.file.Path
import java.util.Locale

package object secrets {
  val SecretProfilePackage: String =
    "com.github.skozlov.messenger.config.secrets.profiles"

  def profileNameToObjectName(profileName: String): String = {
    capitalize(profileName.toLowerCase(Locale.ROOT))
  }

  def profilePath(profileName: String, moduleSourceBaseDir: Path): Path = {
    val fileName = profileNameToObjectName(profileName) + ".scala"
    javaPackagePath(SecretProfilePackage, moduleSourceBaseDir).resolve(fileName)
  }
}
