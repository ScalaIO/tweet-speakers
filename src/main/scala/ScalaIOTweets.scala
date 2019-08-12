import image.{ImageDetails, ImageGenerator}
import submission.{Papercall, Submission}
import twitter.Twitter
import zio.console._
import zio.{App, ZIO}

object ScalaIOTweets extends App {
  val acceptedTalks = ZIO.fromEither(Papercall.acceptedTalks())

  val imageDetails = acceptedTalks.flatMap(
    submissions =>
      ZIO.collectAll(
        submissions
          .map(generateImage)
          //TODO remove when complete
          .take(1)
    )
  )

  val savedImages = imageDetails.flatMap(details => ZIO.collectAll(details.map(ImageGenerator.of)))

  override def run(args: List[String]): ZIO[ScalaIOTweets.Environment, Nothing, Int] = savedImages.fold(_ => -1, _ => 0)

  private def generateImage(submission: Submission) = {
    val imageDetails = for {
      profilePictureURL <- Twitter.profilePicture(submission.profile.twitter)
    } yield ImageDetails(submission.talk.title, submission.profile.name, profilePictureURL)
    imageDetails.tap(details => putStrLn(details.toString))
  }
}
