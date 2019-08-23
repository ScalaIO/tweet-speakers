package twitter

import submission.Profile

case class Tweet(title: String, speaker: Profile, coSpeaker: Option[Profile]) {
  lazy val message = {
    val andCo = coSpeaker.fold("")(co => s" and ${co.label}")
    '"' + title + '"' + s" by ${speaker.label}$andCo will be presented at #ScalaIO19"
  }
}
