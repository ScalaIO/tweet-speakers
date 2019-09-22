package submission

sealed trait TalkFormat {
  val name: String
}

object Talk extends TalkFormat {
  override val name: String = "Talk"
}

object Workshop extends TalkFormat {
  override val name: String = "Workshop"
}

object Keynote extends TalkFormat {
  override val name: String = "Keynote"
}

case class Talk(title: String, description: String, talk_format: TalkFormat)

case class Profile(name: String, twitter: TwitterAccount, avatar: Avatar) {
  lazy val formattedName = name.toLowerCase
    .split(' ')
    .map(_.capitalize)
    .mkString(" ")
  lazy val label = formattedName + twitter.fold("")(t => s" (@$t)")
}

case class Submission(id: Long, talk: Talk, profile: Profile, co_presenter_profiles: Seq[Profile], tags: Seq[String])
