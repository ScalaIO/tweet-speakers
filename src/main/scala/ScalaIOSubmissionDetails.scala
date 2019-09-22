import image.{SpeakerDetails, SubmissionDetails, TalkDetails}
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
        TalkDetails(submission.talk.title, submission.talk.description, submission.talk.talk_format),
        SpeakerDetails(submission.profile.formattedName, profiles._1),
        profiles._2.map(url => SpeakerDetails(submission.co_presenter_profiles.head.name, url))
      )

}
