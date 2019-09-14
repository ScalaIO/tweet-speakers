package submission

import zio.stream.ZStream

object Keynotes {
  val submissions = ZStream.fromIterable(
    Seq(
      Submission(
        Talk("FP: The Good, the Bad and the Ugly", Keynote),
        Profile("Daniela Sfregola", Some("DanielaSfregola"), None),
        Seq.empty
      ),
      Submission(Talk("The art of asking questions", Keynote), Profile("Oli M", Some("Oli_kitty"), None), Seq.empty),
    )
  )
}
