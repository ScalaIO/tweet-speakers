import image.{ImageDetails, ImageGenerator}
import submission.{Papercall, Submission}
import twitter.Twitter
import zio.console._
import zio.{App, ZIO}

object ScalaIOTweets extends App {
  val kitten = this.getClass.getClassLoader.getResource("kitten.jpg")

  val acceptedTalks = ZIO.fromEither(Papercall.acceptedTalks())

  val imageDetails = acceptedTalks.flatMap(
    submissions =>
      ZIO.collectAll(
        submissions
          .map(imageDetailsFromSubmission)
        //TODO remove when complete
        //.take(1)
    )
  )

  val savedImages = imageDetails.flatMap(details => ZIO.collectAll(details.map(ImageGenerator.of)))

  override def run(args: List[String]): ZIO[ScalaIOTweets.Environment, Nothing, Int] = savedImages.fold(_ => -1, _ => 0)

  private def imageDetailsFromSubmission(submission: Submission) = {
    println(submission)
    val profilePicture: zio.ZIO[Any, Throwable, java.net.URL] = submission.profile.twitter
      .fold(zio.Task.succeed(kitten).asInstanceOf[zio.ZIO[Any, Throwable, java.net.URL]])(Twitter.profilePicture)
    val imageDetails = for {
      profilePictureURL <- profilePicture
    } yield ImageDetails(submission.talk.title, submission.profile.name, profilePictureURL, submission.talk.talk_format)
    imageDetails.tap(details => putStrLn(details.toString))
  }
}
