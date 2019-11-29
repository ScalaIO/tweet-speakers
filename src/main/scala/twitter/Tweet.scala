package twitter

import image.SpeakerDetails

case class Tweet(title: String, speakers: Seq[SpeakerDetails]) {
  lazy val message = '"' + title + '"' + s" by ${speakers.map(speaker => speaker.name).mkString(" and ")} will be presented at #ScalaIO19"
}
