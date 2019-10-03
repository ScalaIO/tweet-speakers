import image._
import submission.{LateCoSpeakers, MissingSubmissions, Papercall, Submission}
import twitter.Tweet
import zio.console.{Console, putStrLn}
import zio.stream.ZStream

object ScalaIOSubmissionDetails {

  val details: ZStream[Console, Any, SubmissionDetails] =
    (Papercall.acceptedTalks() ++ MissingSubmissions.submissions)
      .map(LateCoSpeakers.of)
      .mapM(detailsFromSubmission)

  private def detailsFromSubmission(submission: Submission) =
    ProfileImageURL
      .speakerProfileUrls(submission)
      .tap(
        _ =>
          putStrLn(
            Tweet(submission.talk.title, submission.profile, submission.co_presenter_profiles.headOption).message
        )
      )
      .runCollect
      .map(
        speakerDetails =>
          SubmissionDetails(
            submission.id,
            TalkDetails(
              submission.talk.title,
              submission.talk.description,
              submission.talk.talk_format,
              submission.tags.flatMap(tagToLanguage),
              submission.talk.audience_level
            ),
            speakerDetails
        )
      )

  private def tagToLanguage(x: String) = x match {
    case "\uD83C\uDDEC\uD83C\uDDE7" => Some(English)
    case "\uD83C\uDDEB\uD83C\uDDF7" => Some(French)
    case _                          => None
  }

}
