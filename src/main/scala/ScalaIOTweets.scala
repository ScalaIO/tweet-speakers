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
                .foreach(ImageGenerator.of)
        )
    )
}
