package image

import java.net.URL

import submission.TalkFormat

case class ImageDetails(talkTitle: String, speakerName: String, speakerPicture: URL, talkFormat: TalkFormat)
