import image._
import submission.{Keynotes, LateCoSpeakers, Papercall, Submission}
import twitter.Tweet
import zio.console.{Console, putStrLn}
import zio.stream.ZStream

object ScalaIOSubmissionDetails {

  val details: ZStream[Console, Any, SubmissionDetails] =
    (Papercall.acceptedTalks() ++ Keynotes.submissions)
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
              submission.tags.flatMap(tagToLanguage)
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
