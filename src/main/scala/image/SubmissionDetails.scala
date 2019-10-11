package image

import java.net.URL

import submission._

case class SpeakerDetails(
  name: String,
  picture: URL,
  twitter: TwitterAccount,
  bio: Biography,
  company: Company,
  url: Url
)

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
