package image

import java.net.URL

import submission.TalkFormat

case class SpeakerDetails(name: String, picture: URL) {
  val formattedName = name.toLowerCase
    .split(' ')
    .map(_.capitalize)
    .mkString(" ")
}

case class ImageDetails(
  talkTitle: String,
  speaker: SpeakerDetails,
  coSpeaker: Option[SpeakerDetails],
  talkFormat: TalkFormat
)
