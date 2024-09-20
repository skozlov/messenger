package com.github.skozlov.messenger.commons.lang

import java.nio.file.attribute.PosixFilePermissions
import java.nio.file.{FileSystem, Files, Path}

//noinspection ScalaWeakerAccess
package object file {
  extension (fs: FileSystem) {
    def isPosixCompliant: Boolean =
      fs.supportedFileAttributeViews().contains("posix")
  }

  def createDirectories(path: Path, posixPermissions: String): Unit = {
    if (path.getFileSystem.isPosixCompliant) {
      Files.createDirectories(
        path,
        PosixFilePermissions.asFileAttribute(
          PosixFilePermissions.fromString(posixPermissions)
        ),
      )
    } else {
      Files.createDirectories(path)
    }
  }

  def createFile(path: Path, posixPermissions: String): Unit = {
    if (path.getFileSystem.isPosixCompliant) {
      Files.createFile(
        path,
        PosixFilePermissions.asFileAttribute(
          PosixFilePermissions.fromString(posixPermissions)
        ),
      )
    } else {
      Files.createFile(path)
    }
  }
  
  def javaPackagePath(packageName: String, moduleSourceBaseDir: Path): Path = {
    moduleSourceBaseDir.resolve(
      packageName.replace(".", moduleSourceBaseDir.getFileSystem.getSeparator)
    )
  }
}
