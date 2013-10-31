package models
import anorm._
import anorm.SqlParser._
import play.api.db._
import com.basho.riak.client.RiakFactory
import play.api.Play.current
import play.api.libs.json._
import com.basho.riak.client.bucket.Bucket
import java.util.UUID
import com.basho.riak.client.http.RiakClient



object BucketAddon {

    val name = "programme_de_test"
    val uuid = "bucketId_"+UUID.randomUUID
    val url: String = "http://localhost:5432"
    val riakClient = RiakFactory.pbcClient()
    
    def createBucketAddon() ={
      DB.withConnection { implicit c =>
        SQL("insert into addon (label, uuid) values ({label},{uuid})").on(
          'label -> name,
          'uuid -> uuid
        ).executeUpdate()
      }
      val myBucket = riakClient.createBucket(uuid).execute()
      myBucket.store("create", "creation_du_bucket").execute()  
    }

   def createJson(): JsValue ={
      JsObject(Seq("id" -> JsString(uuid),"config" -> JsObject(Seq("CLEVERADDON_URL"->JsString(url)))))
   }

   def deleteBucketAddon(id: String) {
     import scala.collection.JavaConversions._
     
     val myBucket = riakClient.fetchBucket(id).execute()
     val listkeys = myBucket.keys().getAll()
     listkeys.toList.foreach( k => myBucket.delete(k).execute())
     DB.withConnection { implicit c =>
       SQL("delete from addon where uuid = {uuid}").on(
         'uuid -> id
       ).executeUpdate()
     }
   }
    
   def changeBucketAddon(id: String) {
     val myBucket = riakClient.fetchBucket(id).execute()
     
     //TODO
   }
}
