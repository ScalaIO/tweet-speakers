import java.net.URL

import com.typesafe.config.{Config, ConfigFactory}
import image.{ImageDetails, ImageGenerator}
import submission.{Avatar, Papercall, Submission, TwitterAccount}
import twitter.Twitter
import zio.console._
import zio.stream._
import zio.{App, ZIO, _}

import scala.reflect.io.File

object ScalaIOTweets extends App {

  private val conf: Config = ConfigFactory.load
  private val speakerPhotosDir = conf.getString("files.speakerPhotosDir")
  val acceptedTalks = Stream
    .fromEffect(ZIO.fromEither(Papercall.acceptedTalks()))
    .flatMap(submissions => Stream.fromIterable(submissions))

  private val kitten = zio.Task.succeed(this.getClass.getClassLoader.getResource("kitten.jpg"))
  val imageDetails = acceptedTalks
    .flatMap(submission => Stream.fromEffect(imageDetailsFromSubmission(submission)))
  val savedImages = imageDetails.foreach(details => ImageGenerator.of(details).unit)

  private def speakerImageFromTwitterProfile(twitterAccount: TwitterAccount) =
    ZIO.fromOption(twitterAccount).flatMap(Twitter.profilePicture)

  private def imageDetailsFromSubmission(submission: Submission): IO[Nothing, ImageDetails] =
    ZIO
      .succeed(submission)
      .tap(submission => putStrLn(submission.toString))
      .andThen(speakerImageFromLocalDirectory(submission.profile.name))
      .orElse(speakerImageFromPaperCall(submission.profile.avatar))
      .orElse(speakerImageFromTwitterProfile(submission.profile.twitter))
      .orElse(speakerImageFromGravatar(submission.profile.avatar))
      .orElse(kitten)
      .map(url => ImageDetails(submission.talk.title, submission.profile.name, submission.talk.talk_format, url))
      .tap(details => putStrLn(details.toString))
      .asInstanceOf[IO[Nothing, ImageDetails]]

  private def speakerImageFromGravatar(avatar: Avatar) =
    ZIO.fromOption(avatar.filter(_.contains("gravatar"))).map(link => new URL(link.replace("?s=500", "?s=801")))

  private def speakerImageFromPaperCall(avatar: Avatar) =
    ZIO.fromOption(avatar.filter(_.contains("papercall"))).map(link => new URL(link))

  private def speakerImageFromLocalDirectory(name: String) = {
    ZIO.fromOption(Option(File(s"$speakerPhotosDir/$name.jpg")).filter(_.exists).map(_.toURL))
  }

  override def run(args: List[String]): ZIO[ScalaIOTweets.Environment, Nothing, Int] = savedImages.fold(_ => -1, _ => 0)

}
