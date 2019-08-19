package twitter

import java.net.URL

import com.danielasfregola.twitter4s.TwitterRestClient
import zio.ZIO

object Twitter {
  val restClient = TwitterRestClient()

  def sendMediaTweetWithImage(message: String, imagePath: String) = {
    import scala.concurrent.ExecutionContext.Implicits.global
    for {
      upload <- restClient.uploadMediaFromPath(imagePath)
      tweet <- restClient.createTweet(status = message, media_ids = Seq(upload.media_id))
    } yield tweet
  }

  def profilePicture(name: String) = {
    ZIO.fromFuture(
      executionContext =>
        restClient
          .user(name)
          .map(rUser => rUser.data.profile_image_url.default)(executionContext)
          .map(new URL(_))(executionContext)
    )
  }

}
