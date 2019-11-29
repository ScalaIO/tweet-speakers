import image.ImageGenerator
import twitter.Tweet
import zio.console.putStrLn
import zio.{App, _}

object ScalaIOTweets extends App {
  override def run(args: List[String]): UIO[Int] =
    ScalaIOSubmissionDetails.details
      .tap(submissionDetails => putStrLn(Tweet(submissionDetails.talk.title, submissionDetails.speakers).message))
      .mapM(ImageGenerator.of)
      .runDrain
      .fold(_ => 1, _ => 0)
      .asInstanceOf[UIO[Int]]
}
