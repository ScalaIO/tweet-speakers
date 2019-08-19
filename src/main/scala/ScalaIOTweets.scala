import image.{ImageDetails, ImageGenerator}
import submission.{Papercall, Submission, TwitterAccount}
import twitter.Twitter
import zio.console._
import zio.{App, ZIO}

object ScalaIOTweets extends App {

  private val kitten = zio.Task.succeed(this.getClass.getClassLoader.getResource("kitten.jpg"))

  private def imageDetailsFromSubmission(submission: Submission) =
    ZIO
      .succeed(submission)
      .tap(submission => putStrLn(submission.toString))
      .andThen(speakerImageFromLocalDirectory(submission.profile.name))
      .orElse(speakerImageFromTwitterProfile(submission.profile.twitter))
      .orElse(kitten)
      .map(url => ImageDetails(submission.talk.title, submission.profile.name, submission.talk.talk_format, url))
      .tap(details => putStrLn(details.toString))

  private def speakerImageFromLocalDirectory(name: String) =
    ZIO.fromOption(Option(this.getClass.getClassLoader.getResource(s"image/speakers/$name.jpg")))

  private def speakerImageFromTwitterProfile(twitterAccount: TwitterAccount) =
    ZIO.fromOption(twitterAccount).flatMap(Twitter.profilePicture)

  val acceptedTalks = ZIO.fromEither(Papercall.acceptedTalks())

  val imageDetails = acceptedTalks.flatMap(
    submissions =>
      ZIO.collectAll(
        submissions
          .map(imageDetailsFromSubmission)
        //uncomment to test only first submission
        //.take(1)
    )
  )

  val savedImages = imageDetails.flatMap(details => ZIO.collectAll(details.map(ImageGenerator.of)))

  override def run(args: List[String]): ZIO[ScalaIOTweets.Environment, Nothing, Int] = savedImages.fold(_ => -1, _ => 0)

}
