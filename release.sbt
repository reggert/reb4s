releaseCrossBuild := true

import ReleaseTransformations._

val groupId = "io.github.reggert"
val stagingName = "reb4s-staging"
val openStaging = releaseStepCommand(s"""sonatypeOpen "$groupId" "$stagingName""")
val releaseStaging = releaseStepCommand("sonatypeRelease")

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,              // : ReleaseStep
  inquireVersions,                        // : ReleaseStep
  runClean,                               // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,                      // : ReleaseStep
  commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
  tagRelease,                             // : ReleaseStep
  openStaging,
  publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion,                         // : ReleaseStep
  commitNextVersion,                      // : ReleaseStep
  pushChanges,                             // : ReleaseStep, also checks that an upstream branch is properly configured
  releaseStaging
)