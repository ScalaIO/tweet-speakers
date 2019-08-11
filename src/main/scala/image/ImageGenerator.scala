package image

import java.awt.image.BufferedImage
import java.awt.{Color, Font, Graphics2D, RenderingHints}
import java.io.File

import javax.imageio.ImageIO
import zio.ZIO

import scala.util.Try

object ImageGenerator {
  val scalaIORed = 0xbc1321

  def of(imageDetails: ImageDetails) = {
    val h = 267 * 3
    val w = 507 * 3
    val timesRoman = "Arial Black"

    val img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

    val g = img.getGraphics.asInstanceOf[Graphics2D]
    g.setColor(Color.WHITE)
    g.fillRect(0, 0, w - 1, h - 1)
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g.setColor(new Color(scalaIORed))
    for (offset <- 1 until 3) {
      g.drawLine(50, 250 + offset, 200, 250 + offset)
    }

    val profilePicture: BufferedImage = ImageIO.read(imageDetails.speakerPicture)
    g.drawImage(profilePicture, 270, 100, 200, 200, Color.WHITE, null)

    val scalaIOPicture: BufferedImage =
      ImageIO.read(new File(this.getClass.getResource("scalaio_small.png").getFile))
    g.drawImage(scalaIOPicture, 270, 400, 100, 42, Color.WHITE, null)

    g.setFont(new Font(timesRoman, Font.PLAIN, 40))
    g.drawString(imageDetails.talkTitle, 50, 50)
    g.setFont(new Font(timesRoman, Font.PLAIN, 24))
    g.drawString(imageDetails.speakerName, 50, 150)

    ZIO.fromTry(
      Try(ImageIO.write(img, "png", new File(s"/Users/jeandetoeuf/Desktop/accepted/${imageDetails.speakerName}.png")))
    )
  }

}
