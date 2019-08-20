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

case class Talk(title: String, talk_format: TalkFormat)

case class Profile(name: String, twitter: TwitterAccount, avatar: Avatar)

case class Submission(talk: Talk, profile: Profile, co_presenter_profiles: Seq[Profile])
