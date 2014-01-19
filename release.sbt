import sbtrelease._
import ReleaseStateTransformations._
import sbt._
import Keys._
import sbtrelease._
import ReleaseStateTransformations._
import ReleasePlugin._
import ReleaseKeys._
import Utilities._
import com.typesafe.sbt.SbtPgp.PgpKeys._

releaseSettings

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,              
  inquireVersions,                        
  runTest,                                
  setReleaseVersion,                      
  commitReleaseVersion,                   
  tagRelease,                             
  publishArtifacts.copy(action = publishSignedAction),
  setNextVersion,                         
  commitNextVersion,                      
  pushChanges                             
)


lazy val publishSignedAction = { st: State =>
  val extracted = st.extract
  val ref = extracted.get(thisProjectRef)
  extracted.runAggregated(publishSigned in Global in ref, st)
}


