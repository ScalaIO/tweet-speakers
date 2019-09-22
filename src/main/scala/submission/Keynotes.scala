package submission

import zio.stream.ZStream

object Keynotes {
  val submissions = ZStream.fromIterable(
    Seq(
      Submission(
        1,
        Talk("FP: The Good, the Bad and the Ugly", "TODO", Keynote),
        Profile("Daniela Sfregola", Some("DanielaSfregola"), None),
        Seq.empty,
        Seq("\uD83C\uDDEC\uD83C\uDDE7")
      ),
      Submission(
        2,
        Talk("The art of asking questions", "TODO", Keynote),
        Profile("Oli M", Some("Oli_kitty"), None),
        Seq.empty,
        Seq("\uD83C\uDDEC\uD83C\uDDE7")
      ),
    )
  )
}
