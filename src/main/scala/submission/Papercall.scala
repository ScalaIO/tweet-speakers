package submission

import com.softwaremill.sttp._

case class Talk(title: String)
case class Profile(name: String, twitter: String)
case class Submission(talk: Talk, profile: Profile)

object Papercall {

  import com.typesafe.config.{Config, ConfigFactory}

  val conf: Config = ConfigFactory.load
  val token = conf.getString("papercall.token")

  def acceptedTalks() = {
    val request = sttp.get(uri"https://www.papercall.io/api/v1/submissions?_token=$token&per_page=50&state=accepted")
    implicit val backend = HttpURLConnectionBackend()
    val response = request.send()

    import io.circe.Decoder
    import io.circe.generic.semiauto.deriveDecoder
    import io.circe.parser.decode

    implicit val decodeTalk: Decoder[Talk] = deriveDecoder[Talk]
    implicit val decodeProfile: Decoder[Profile] = deriveDecoder[Profile]
    implicit val decodeSubmission: Decoder[Submission] = deriveDecoder[Submission]

    decode[Seq[Submission]](response.unsafeBody)
  }

}
