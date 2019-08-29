import image.{ImageDetails, ImageGenerator, SpeakerDetails}
import submission._
import twitter.Tweet
import zio.console._
import zio.stream._
import zio.{App, _}

object ScalaIOTweets extends App {
  override def run(args: List[String]): UIO[Int] = generatedImages.runDrain.fold(_ => 1, _ => 0).asInstanceOf[UIO[Int]]

  val generatedImages: ZStream[Console, Any, String] = Papercall
    .acceptedTalks()
    .map(LateCoSpeakers.of)
    .mapM(imageDetailsFromSubmission)
    .mapM(ImageGenerator.of)

  private def imageDetailsFromSubmission(submission: Submission) =
    for {
      _ <- putStrLn(submission.toString)
      _ <- putStrLn(
        Tweet(submission.talk.title, submission.profile, submission.co_presenter_profiles.headOption).message
      )
      profiles <- ProfileImageURL.speakerProfileUrls(submission)
    } yield
      ImageDetails(
        submission.talk.title,
        SpeakerDetails(submission.profile.formattedName, profiles._1),
        profiles._2.map(url => SpeakerDetails(submission.co_presenter_profiles.head.name, url)),
        submission.talk.talk_format
      )
}
