import image.ImageGenerator
import zio.{App, _}

object ScalaIOTweets extends App {
  override def run(args: List[String]): UIO[Int] =
    ScalaIOSubmissionDetails.details
      .mapM(ImageGenerator.of)
      .runDrain
      .fold(_ => 1, _ => 0)
      .asInstanceOf[UIO[Int]]
}
