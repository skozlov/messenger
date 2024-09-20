package com.github.skozlov.messenger.commons.lang.file

import com.github.skozlov.messenger.commons.test.Test

import java.nio.file.Paths

class FileUtilsTest extends Test {
  test("javaPackagePath") {
    javaPackagePath(
      packageName = "com.github",
      moduleSourceBaseDir = Paths.get("a", "b"),
    ) shouldBe Paths.get("a", "b", "com", "github")
  }
}
