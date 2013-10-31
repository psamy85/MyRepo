package models

import play.api.mvc._
import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.libs.json._

case class AuthData(username: String, password: String)

object AuthData {

  val reference: AuthData = AuthData("cleveraddon", "6596df0dc3485f306fe8e5c311e0f5d4")

  def extract(header: String) : Option [AuthData] = {
    def extractb64(b64: String) : Option [AuthData] = {
      val plaintext = new String(org.apache.commons.codec.binary.Base64.decodeBase64(b64.getBytes))
      plaintext.split(":").toList match {
        case username :: password :: Nil => Some(AuthData(username, password)) 
        case _ => None 
      }
    }
    val parts = header.split(" ").toList
    parts match {
      case "Basic" :: b64 :: Nil => extractb64(b64)
      case _ => None
    }
  }
  
  def isValid(ado: Option[AuthData]): Boolean = ado.map((ad:AuthData) => (ad == reference)).getOrElse(false)
  
}

trait Authentication {
  import AuthData._
  import scala.concurrent.Future
  import play.api.mvc.Results._

  object Authenticated extends ActionBuilder[Request] {
    def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[SimpleResult]) = {
      val header: Option[String] = request.headers.get("Authorization")
      val authData: Option[AuthData] = header.flatMap(h => extract(h))
      if(isValid(authData)){
        block(request)
      }else{
        Future.successful(Unauthorized.withHeaders("WWW-Authenticate" -> """Basic realm="Secured""""))
      }
    }
  }
}
