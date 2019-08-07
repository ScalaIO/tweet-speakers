package twitter

import java.net.URL

import com.danielasfregola.twitter4s.TwitterRestClient

import scala.concurrent.ExecutionContext.Implicits.global

object Twitter {
  val restClient = TwitterRestClient()

  def sendMediaTweetWithImage(message: String, imagePath: String): Any = {
    for {
      upload <- restClient.uploadMediaFromPath(imagePath)
      tweet <- restClient.createTweet(status = message, media_ids = Seq(upload.media_id))
    } yield tweet
  }

  def profilePicture(name: String) = {
    restClient.user(name).map(rUser => rUser.data.profile_image_url.default).map(new URL(_))
  }

}
