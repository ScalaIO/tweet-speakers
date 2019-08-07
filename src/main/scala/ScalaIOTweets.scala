import image.{ImageDetails, ImageGenerator}
import submission.Papercall
import twitter.Twitter

import scala.concurrent.ExecutionContext.Implicits.global

object ScalaIOTweets extends App {
  Papercall
    .acceptedTalks()
    .map(
      submissions =>
        submissions
          .map(
            submission =>
              Twitter
                .profilePicture(submission.profile.twitter)
                .map(
                  profilePictureURL => ImageDetails(submission.talk.title, submission.profile.name, profilePictureURL)
                )
                .map(details => {
                  println(details)
                  details
                })
                .foreach(ImageGenerator.of)
        )
    )
}
