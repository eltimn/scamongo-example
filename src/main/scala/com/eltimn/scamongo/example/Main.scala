package com.eltimn.scamongo.example

import java.util.Date

import com.eltimn.scamongo._
import com.mongodb._

case class Address(street: String, city: String)
case class Child(name: String, age: Int, birthdate: Option[Date])
case class Person(_id: String, name: String, age: Int, address: Address, children: List[Child])
	 extends MongoDocument[Person] {
	 def meta = Person
}
object Person extends MongoDocumentMeta[Person] {
 override def collectionName = "mypersons"
}

object Main {

	def main(args: Array[String]) {
		MongoDB.defineDb(DefaultMongoIdentifier, MongoAddress(MongoHost("localhost", 27017), "test"))	

		def date(s: String) = Person.formats.dateFormat.parse(s).get

		val p = Person(
		 ObjectId.get.toString,
		 "joe",
		 27,
		 Address("Bulevard", "Helsinki"),
		 List(Child("Mary", 5, Some(date("2004-09-04T18:06:22.000Z"))), Child("Mazy", 3, None))
		)

		p.save

		//val pFromDb = Person.find(p._id)
		import net.liftweb.json._
		import net.liftweb.json.JsonAST.JObject
		import net.liftweb.json.JsonDSL._
		implicit val formats = DefaultFormats.lossless
		val json = JsonParser.parse("""
		{ "name": "joe",
			"children": [
				{
					"name": "Mary",
					"age": 5
				},
				{
					"name": "Mazy",
					"age": 3
				}
			]
		}
		""").asInstanceOf[JObject]
		MongoDB.useCollection("myCollection")( coll => {
			coll.save(JObjectParser.parse(json))
		})
	}
}