enablePlugins(SbtOsgi)

OsgiKeys.exportPackage := Seq(s"${organization.value}.*;version=${version.value}")

OsgiKeys.importPackage := Seq("""scala*;version="${range;[==,=+)}"""", "*")

