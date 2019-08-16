package submission

import com.softwaremill.sttp._

object Papercall {

  import com.typesafe.config.{Config, ConfigFactory}

  private val conf: Config = ConfigFactory.load
  private val token = conf.getString("papercall.token")

  def acceptedTalks() = {
    val request = sttp.get(uri"https://www.papercall.io/api/v1/submissions?_token=$token&per_page=100&state=accepted")
    implicit val backend = HttpURLConnectionBackend()
    val response = request.send()

    import io.circe.Decoder
    import io.circe.generic.semiauto.deriveDecoder
    import io.circe.parser.decode

    implicit val decodeTwitterAccount: Decoder[TwitterAccount] =
      Decoder.decodeString.map(name => if (name.isEmpty) None else Some(name))
    implicit val decodeProfile: Decoder[Profile] = deriveDecoder[Profile]
    implicit val decodeFormat: Decoder[TalkFormat] = Decoder.decodeString.map {
      case "Workshop (3 hours)"      => Workshop
      case "Talk (45 minutes)"       => Talk
      case "Short Talk (20 minutes)" => Talk
    }
    implicit val decodeTalk: Decoder[Talk] = deriveDecoder[Talk]
    implicit val decodeSubmission: Decoder[Submission] = deriveDecoder[Submission]

    decode[Seq[Submission]](response.unsafeBody)
  }

}
