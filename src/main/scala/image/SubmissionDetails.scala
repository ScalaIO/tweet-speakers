package image

import java.net.URL

import submission.TalkFormat

case class SpeakerDetails(name: String, picture: URL)

case class TalkDetails(title: String, description: String, format: TalkFormat)

case class SubmissionDetails(id: Long, talk: TalkDetails, speaker: SpeakerDetails, coSpeaker: Option[SpeakerDetails])
