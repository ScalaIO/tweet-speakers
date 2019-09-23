package submission

object LateCoSpeakers {
  // Co-speakers who have been added after the talk was accepted do not show on PaperCall. We have to deal with them manually
  def of(submission: Submission): Submission = submission.profile.formattedName match {
    case "Florent Pellet" =>
      submission.copy(co_presenter_profiles = Seq(Profile("Clément Bouillier", Some("clem_bouillier"), None)))
    case "Caroline Gaudreau" =>
      submission.copy(co_presenter_profiles = Seq(Profile("Gaël Deest", Some("gael_deest"), None)))
    case "Aggelos Biboudis" => submission.copy(co_presenter_profiles = Seq(Profile("Olivier Blanvillain", None, None)))
    case "Vincent Brule" =>
      submission.copy(
        co_presenter_profiles = Seq(
          Profile(
            "Xavier Tordoir",
            Some("xtordoir"),
            Some("https://secure.gravatar.com/avatar/f45f19c580c97062b3282d8cfec24863?s=500")
          )
        )
      )
    case _ => submission
  }

}
