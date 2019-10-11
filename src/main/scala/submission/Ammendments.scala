package submission

object Ammendments {
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
    case "Maude Cahuet" =>
      submission.copy(
        profile = Profile(
          "Vincent Brule",
          Some("BruleVincent"),
          Some("https://papercallio-production.s3.amazonaws.com/uploads/user/avatar/51055/Vincent_B.jpg")
        )
      )
    case "Harrison Cheng" => submission.copy(talk = submission.talk.copy(audience_level = Beginner))
    case "Jonathan Winandy" =>
      submission.copy(talk = submission.talk.copy(title = "L'incroyable efficacité de l'unification des logs !"))
    case "Francois Armand" =>
      submission.copy(
        talk = submission.talk
          .copy(title = "On a porté Rudder sur ZIO - Gestion systématique des erreurs dans vos applications")
      )
    case "Tankoua Stéphane" =>
      submission.copy(
        profile = submission.profile.copy(name = "Stéphane Tankoua"),
        co_presenter_profiles =
          submission.co_presenter_profiles.map(profile => profile.copy(name = profile.name.replace("Calves", "Calvès")))
      )
    case _ => submission
  }

}
