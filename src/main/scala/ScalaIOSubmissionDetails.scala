import image._
import submission.{Keynotes, LateCoSpeakers, Papercall, Submission}
import zio.console.Console
import zio.stream.ZStream

object ScalaIOSubmissionDetails {

  val details: ZStream[Console, Any, SubmissionDetails] =
    (Papercall.acceptedTalks() ++ Keynotes.submissions)
      .map(LateCoSpeakers.of)
      .mapM(detailsFromSubmission)

  private def detailsFromSubmission(submission: Submission) =
    for {
      /*_ <- putStrLn(submission.toString)
      _ <- putStrLn(
        Tweet(submission.talk.title, submission.profile, submission.co_presenter_profiles.headOption).message
      )*/
      profiles <- ProfileImageURL.speakerProfileUrls(submission)
    } yield
      SubmissionDetails(
        submission.id,
        TalkDetails(
          submission.talk.title,
          submission.talk.description,
          submission.talk.talk_format,
          submission.tags.flatMap(tagToLanguage)
        ),
        SpeakerDetails(submission.profile.formattedName, profiles._1),
        profiles._2.map(url => SpeakerDetails(submission.co_presenter_profiles.head.name, url))
      )

  private def tagToLanguage(x: String) = x match {
    case "\uD83C\uDDEC\uD83C\uDDE7" => Some(English)
    case "\uD83C\uDDEB\uD83C\uDDF7" => Some(French)
    case _                          => None
  }

}
