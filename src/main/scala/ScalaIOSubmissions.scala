import java.io.{File, PrintWriter}
import java.net.URL

import com.typesafe.config.{Config, ConfigFactory}
import image._
import io.circe.Printer
import submission._
import zio.{App, ZIO, _}
import zio._

import scala.sys.process._
import scala.util.Try
import zio.ZIO

import scala.sys.process._
import scala.util.Try

object ScalaIOSubmissions extends App {
  import io.circe.generic.semiauto._
  import io.circe.syntax._
  import io.circe.{Encoder, Json}

  implicit val encodeUrl: Encoder[URL] = (a: URL) => {
    if (a.getProtocol.contains("http")) Json.fromString(a.toString.replace("http:", "https:"))
    else Json.fromString(a.getPath.split("/").last)
  }
  implicit val encodeSpeakerDetails: Encoder[SpeakerDetails] = deriveEncoder

  implicit val encodeTalkFormat: Encoder[TalkFormat] = Encoder.instance {
    case Talk     => Json.fromString("Talk")
    case Workshop => Json.fromString("Workshop")
    case Keynote  => Json.fromString("Keynote")
  }
  implicit val encodeTalkLevel: Encoder[AudienceLevel] = Encoder.instance {
    case Beginner     => Json.fromString("Beginner")
    case Intermediate => Json.fromString("Intermediate")
    case All          => Json.fromString("All")
  }
  implicit val encodeLanguage: Encoder[Language] = Encoder.instance {
    case English => Json.fromString("\uD83C\uDDEC\uD83C\uDDE7")
    case French  => Json.fromString("\uD83C\uDDEB\uD83C\uDDF7")
  }
  implicit val encodeTalkDetails: Encoder[TalkDetails] = deriveEncoder
  implicit val encodeSubmissionDetails: Encoder[SubmissionDetails] = deriveEncoder
  val printer = Printer.noSpaces.copy(dropNullValues = true)

  private val conf: Config = ConfigFactory.load

  private def openFile() = Task(new PrintWriter(new File(conf.getString("files.outputSubmissions"))))
  private def closeFile(printWriter: PrintWriter) = UIO(printWriter.close())
  private def writeContent(writer: PrintWriter, json: String) = Task.effect(writer.print(json))

  override def run(args: List[String]): UIO[Int] =
    ScalaIOSubmissionDetails.details
      .mapM(ImageCopy.of)
      .runCollect
      .map(details => details.asJson.pretty(printer))
      .tap(
        json =>
          openFile().bracket(closeFile) { writer =>
            writeContent(writer, json)
        }
      )
      .fold(_ => 1, _ => 0)
      .asInstanceOf[UIO[Int]]
}
