package image

import java.net.URL

import submission.{AudienceLevel, TalkFormat}

case class SpeakerDetails(name: String, picture: URL)

sealed trait Language

object English extends Language

object French extends Language

case class TalkDetails(
  title: String,
  description: String,
  format: TalkFormat,
  languages: Seq[Language],
  level: AudienceLevel
)

case class SubmissionDetails(id: Long, talk: TalkDetails, speakers: Seq[SpeakerDetails])
