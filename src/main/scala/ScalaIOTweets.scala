import image.{ImageDetails, ImageGenerator}
import io.circe
import submission.{Papercall, Submission}
import twitter.Twitter
import zio.{App, IO, ZIO}
import zio.console._

object ScalaIOTweets extends App {
  val acceptedTalks = ZIO.fromEither(Papercall.acceptedTalks())

  val imageDetails =
    acceptedTalks.flatMap(
      submissions =>
        ZIO.collectAll(
          submissions
            .map(generateImage)
            // remove when complete to cleanup
            .take(2)
      )
    )

  val saveImages = imageDetails.flatMap(details => ZIO.collectAll(details.map(ImageGenerator.of)))

  override def run(args: List[String]): ZIO[ScalaIOTweets.Environment, Nothing, Int] =
    saveImages.fold(_ => -1, _ => 0)

  private def generateImage(submission: Submission) = {
    val imageDetails = for {
      profilePictureURL <- Twitter.profilePicture(submission.profile.twitter)
    } yield ImageDetails(submission.talk.title, submission.profile.name, profilePictureURL)
    imageDetails.tap(details => putStrLn(details.toString))
  }
}
