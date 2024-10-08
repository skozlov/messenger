package com.github.skozlov.messenger.commons.lang

import java.nio.file.attribute.PosixFilePermissions
import java.nio.file.{FileSystem, FileSystems, Files, Path}

//noinspection ScalaWeakerAccess
package object file {
  extension (fs: FileSystem) {
    def isPosixCompliant: Boolean =
      fs.supportedFileAttributeViews().contains("posix")
  }

  lazy val isCurrentFileSystemPosixCompliant: Boolean =
    FileSystems.getDefault.isPosixCompliant

  def createDirectories(path: Path, posixPermissions: String): Unit = {
    if (isCurrentFileSystemPosixCompliant) {
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
    Files.createFile(
      path,
      PosixFilePermissions.asFileAttribute(
        PosixFilePermissions.fromString(posixPermissions)
      ),
    )
  }
}
