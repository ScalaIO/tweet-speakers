import java.net.URL

import image._
import io.circe.Printer
import submission.{Keynote, Talk, TalkFormat, Workshop}
import zio.console.putStrLn
import zio.{App, _}

object ScalaIOSubmissions extends App {
  import io.circe.generic.semiauto._
  import io.circe.syntax._
  import io.circe.{Encoder, Json}

  implicit val encodeUrl: Encoder[URL] = (a: URL) => {
    if (a.getProtocol.contains("http")) Json.fromString(a.toString)
    else Json.fromString(s"/assets/speakers/${a.getPath.split("/").last}")
  }
  implicit val encodeSpeakerDetails: Encoder[SpeakerDetails] = deriveEncoder

  implicit val encodeTalkFormat: Encoder[TalkFormat] = Encoder.instance {
    case Talk     => Json.fromString("Talk")
    case Workshop => Json.fromString("Workshop")
    case Keynote  => Json.fromString("Keynote")
  }
  implicit val encodeLanguage: Encoder[Language] = Encoder.instance {
    case English => Json.fromString("\uD83C\uDDEC\uD83C\uDDE7")
    case French  => Json.fromString("\uD83C\uDDEB\uD83C\uDDF7")
  }
  implicit val encodeTalkDetails: Encoder[TalkDetails] = deriveEncoder
  implicit val encodeSubmissionDetails: Encoder[SubmissionDetails] = deriveEncoder
  val printer = Printer.noSpaces.copy(dropNullValues = true)

  override def run(args: List[String]): UIO[Int] =
    ScalaIOSubmissionDetails.details.runCollect
      .map(details => details.asJson.pretty(printer))
      .tap(x => putStrLn(x))
      .fold(_ => 1, _ => 0)
      .asInstanceOf[UIO[Int]]
}
