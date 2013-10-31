package controllers

import play.api._
import play.api.db
import play.api.mvc._
import models._
import play.api.libs.json._
import play.api.mvc.BodyParsers._
import play.api.mvc.Result
import play.api.mvc.Security.Authenticated

object Application extends Controller with Authentication {


  def index = Action { implicit request =>
    Ok("Hello World")
  }

  def provisioning = Authenticated {implicit request =>
    BucketAddon.createBucketAddon()
    Ok(BucketAddon.createJson)
  }
  
  def deprovisioning(id: String) = Authenticated(parse.tolerantText) {implicit request =>
    BucketAddon.deleteBucketAddon(id)
    Ok
  }
  
  def planchange(id: String) = Authenticated {implicit request =>
    BucketAddon.changeBucketAddon(id)
    Ok
  }
    
    
    /* val reg:JsValue = js \ "region"
    val regStr = reg match {
      case JsString(str) => str
      case _ => ""
    }*/

}

