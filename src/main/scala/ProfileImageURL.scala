import java.net.URL

import com.typesafe.config.{Config, ConfigFactory}
import image.SpeakerDetails
import submission.{Avatar, Profile, Submission, TwitterAccount}
import twitter.Twitter
import zio.stream.ZStream
import zio.{IO, ZIO}

import scala.reflect.io.File

object ProfileImageURL {
  private val conf: Config = ConfigFactory.load
  private val speakerPhotosDir = conf.getString("files.speakerPhotosDir")
  private val kitten = zio.Task.succeed(this.getClass.getClassLoader.getResource("kitten.jpg"))

  def speakerProfileUrls(submission: Submission) =
    ZStream
      .fromIterable(Seq(submission.profile) ++ submission.co_presenter_profiles)
      .mapM(
        profile =>
          profileUrl(profile).map(
            url =>
              SpeakerDetails(profile.formattedName, url, profile.twitter, profile.bio, profile.company, profile.url)
        )
      )

  private def profileUrl(profile: Profile): IO[Unit, URL] =
    speakerImageFromLocalDirectory(profile.name) <>
      speakerImageFromPaperCall(profile.avatar) <>
      speakerImageFromTwitterProfile(profile.twitter) <>
      speakerImageFromGravatar(profile.avatar) <>
      kitten

  private def speakerImageFromLocalDirectory(name: String) =
    ZIO.fromOption(Option(File(s"$speakerPhotosDir/$name.jpg")).filter(_.exists).map(_.toURL))

  private def speakerImageFromPaperCall(avatar: Avatar) =
    ZIO.fromOption(avatar.filter(_.contains("papercall"))).map(link => new URL(link))

  private def speakerImageFromTwitterProfile(twitterAccount: TwitterAccount) =
    ZIO.fromOption(twitterAccount).flatMap(Twitter.profilePicture)

  private def speakerImageFromGravatar(avatar: Avatar) =
    ZIO.fromOption(avatar.filter(_.contains("gravatar"))).map(link => new URL(link.replace("?s=500", "?s=801")))
}
