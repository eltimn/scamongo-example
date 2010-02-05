import sbt._
import Process._

class ScamongoExampleProject(info: ProjectInfo) extends DefaultProject(info) {

	val mongo = "org.mongodb" % "mongo-java-driver" % "1.2.1" % "compile->default"
	val liftjson = "net.liftweb" % "lift-json" % "2.0-M1" % "compile->default"
	
	// required because Ivy doesn't pull repositories from poms
	//val smackRepo = "m2-repository-smack" at "http://maven.reucon.com/public"
}
