package image
import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import zio.ZIO

import scala.sys.process._
import scala.util.Try

object ImageCopy {
  private val conf: Config = ConfigFactory.load

  def of(imageDetails: SubmissionDetails) = {
    ZIO.fromTry(Try {
      imageDetails.speakers.foreach(
        speaker => speaker.picture #> new File(s"${conf.getString("files.outputPhotosDir")}/${speaker.name}.jpg") !!
      )
      imageDetails
    })
  }
}
