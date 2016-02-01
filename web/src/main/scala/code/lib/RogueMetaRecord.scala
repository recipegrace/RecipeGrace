package code
package lib

import code.config.MongoConfig
import com.foursquare.rogue._
import net.liftweb._
import net.liftweb.mongodb.record._

/**
  * A custom MongoMetaRecord that adds Rogue and an injectable MongoIdentifier.
  */
trait RogueMetaRecord[A <: MongoRecord[A]] extends MongoMetaRecord[A] with LiftRogue {
  self: A =>

  override def mongoIdentifier = MongoConfig.defaultId.vend
}
 