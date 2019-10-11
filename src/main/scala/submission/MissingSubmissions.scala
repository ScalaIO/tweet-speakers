package submission

import zio.stream.ZStream

object MissingSubmissions {
  val frenchFlagEmoji = "\uD83C\uDDEB\uD83C\uDDF7"
  val ukFlagEmoji = "\uD83C\uDDEC\uD83C\uDDE7"
  val submissions = ZStream.fromIterable(
    Seq(
      Submission(
        1,
        Talk(
          "FP: The Good, the Bad and the Ugly",
          "You are about to fall in love with Functional Programming, if not already. You are going to learn the good parts that are going to make your day-to-day life easier. But since nobody is perfect - not even FP -, you are also going to see its bad and ugly parts, and you'll discover how to deal with them: from learning challenges to performance issues on the JVM. ",
          Keynote,
          All
        ),
        Profile("Daniela Sfregola", Some("DanielaSfregola")),
        Seq.empty,
        Seq(ukFlagEmoji)
      ),
      Submission(
        2,
        Talk(
          "The art of asking questions",
          """There are specific skills that are crucial for people to utilize in every profession, and without a doubt, one of the most essential skills in a workplace is the ability to ask questions.
						|
						|It doesn’t matter what your job title is. Whether you’re a junior developer, a team lead, or an architect; you need to master the art of asking questions. Why is this skill so important? One study shows that curious children ask, on average, 73 questions every day, a practice that helps them learn faster. This practice is also true of students, interns, and new hires. Asking questions is the simplest and most productive way of learning.
						|
						|However, as we become more senior in our fields, we tend to assume we know it all and stop asking as many questions. But do we ever stop wondering, challenging ourselves, doubting things, or searching for answers?
						|
						|In this keynote, Oli will share insights she's gained while conducting podcast interviews with individuals from a variety of sectors in the Scala community. She'll also discuss how she's incorporated these principles into asking the right questions, a skill that helps her be an effective technical consultant.""".stripMargin,
          Keynote,
          All
        ),
        Profile("Oli M", Some("Oli_kitty")),
        Seq.empty,
        Seq(ukFlagEmoji)
      ),
      Submission(
        3,
        Talk(
          "Comment faire des tests de charge avec Gatling ?",
          "Découvrez la problématique des tests de charge, et apprenez à la résoudre en Scala avec l’outil Open-Source Gatling",
          Workshop,
          All
        ),
        Profile("Cédric Cousseran"),
        Seq(Profile("Thomas Petillot")),
        Seq(ukFlagEmoji, frenchFlagEmoji)
      ),
      Submission(
        4,
        Talk(
          "Dans s'cas là",
          """Lors de cet atelier, nous vous proposons de revenir sur des fondamentaux de Scala en nous intéressant tout particulièrement aux case class, au Pattern Matching, et pour finir aux Collections. L’atelier sera organisé autour d’exercices, mettant en jeu ces différentes notions et leurs subtilités.
						|
						|Pas besoin de connaître les monades, monoïde ou autres, des connaissances en Java vous suffiront !""".stripMargin,
          Workshop,
          Beginner
        ),
        Profile("Jean Helou", Some("jeanhelou")),
        Seq(Profile("Daniel Lovera", Some("dan_lovera"))),
        Seq(ukFlagEmoji, frenchFlagEmoji)
      ),
      Submission(
        5,
        Talk(
          "ZIO Chunk: A Fast, Pure Alternative to Arrays",
          "Arrays are fast and don’t box primitive values, but due to class tag requirements and mutability, they are painful to use and don’t integrate well into functional code. In this  presentation, Aleksandra A Holubitska introduces the audience to Chunk, a data type in ZIO that’s designed to be friendly to use, immutable, and high-performance. Learn more about how you can replace usage of arrays in your application with this new high-performance data type that powers ZIO Streams and other libraries in the ZIO ecosystem.",
          Talk,
          All
        ),
        Profile("Aleksandra A Holubitska", Some("oleksandra_a")),
        Seq.empty,
        Seq(ukFlagEmoji)
      )
    )
  )
}
