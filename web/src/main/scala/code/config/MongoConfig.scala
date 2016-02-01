package code
package config

import java.util.Arrays

import com.mongodb.{DBAddress, MongoClient, MongoCredential}
import net.liftweb._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.mongodb._
import net.liftweb.util.{ConnectionIdentifier, DefaultConnectionIdentifier, Props}

object MongoConfig extends Factory with Loggable {

  // configure your MongoMetaRecords to use this. See lib/RogueMetaRecord.scala.
  val defaultId = new FactoryMaker[ConnectionIdentifier](DefaultConnectionIdentifier) {}

  def init() {
    /**
      * First checks for existence of mongo.default.url. If not found, then
      * checks for mongo.default.host, port, and name. Uses defaults if those
      * are not found.
      */
    val defaultDbAddress = Props.get("mongo.default.url")
      .map(url => new DBAddress(url))
      .openOr(new DBAddress(
        Props.get("mongo.default.host", "127.0.0.1"),
        Props.getInt("mongo.default.port", 27017),
        Props.get("mongo.default.name", "liftmongotest")))

    /*
						 * If mongo.default.user, and pwd are defined, configure Mongo using authentication.
						 */
    (Props.get("mongo.default.user"), Props.get("mongo.default.pwd")) match {
      case (Full(user), Full(pwd)) =>

        val credential = MongoCredential.createMongoCRCredential(user, defaultDbAddress.getDBName,
          pwd.toCharArray());

        MongoDB.defineDb(
          DefaultConnectionIdentifier,
          new MongoClient(defaultDbAddress, Arrays.asList(credential)),
          defaultDbAddress.getDBName)
        logger.info("MongoDB inited using authentication: %s".format(defaultDbAddress.toString))
      case _ =>
        MongoDB.defineDb(
          DefaultConnectionIdentifier,
          new MongoClient(defaultDbAddress),
          defaultDbAddress.getDBName)
        logger.info("MongoDB inited: %s".format(defaultDbAddress.toString))
    }
  }

  val admin = "feroshjacob@gmail.com"
}

