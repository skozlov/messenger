package com.github.skozlov.messenger.config

import org.apache.commons.lang3.StringUtils.capitalize

import java.util.Locale

package object secrets {
  val SecretProfilePackage: String =
    "com.github.skozlov.messenger.config.secrets.profiles"

  def profileNameToObjectName(profileName: String): String = {
    capitalize(profileName.toLowerCase(Locale.ROOT))
  }
}
