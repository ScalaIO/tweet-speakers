package image

import java.awt.image.BufferedImage
import java.awt._
import java.io.File

import javax.imageio.ImageIO
import org.apache.commons.text.WordUtils
import zio.ZIO

import scala.util.Try

object ImageGenerator {
  val scalaIORed = new Color(0xbc1321)

  def of(imageDetails: ImageDetails) = {
    val h = 267 * 3
    val w = 507 * 3
    val timesRoman = "Arial Black"

    val img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)

    val g = img.getGraphics.asInstanceOf[Graphics2D]
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g.setBackground(Color.black)
    g.setColor(Color.black)
    g.fillRect(0, 0, w - 1, h - 1)

    g.setColor(scalaIORed)
    for (offset <- 1 until 3) {
      g.drawLine(50, 250 + offset, 200, 250 + offset)
    }

    val profilePicture: BufferedImage = ImageIO.read(imageDetails.speakerPicture)
    g.drawImage(profilePicture, w - h, 0, h, h, Color.black, null)
    val transparent = new Color(255, 255, 255, 0)
    val gradientWidth = 100
    g.setPaint(new GradientPaint(w - h, 0, Color.black, w - h + gradientWidth, 0, transparent))
    g.fillRect(w - h, 0, gradientWidth, h)

    val scalaIOPicture: BufferedImage =
      ImageIO.read(new File(this.getClass.getResource("scalaio_black.png").getFile))
    g.drawImage(scalaIOPicture, 50, h - 300, 600, 300, Color.black, null)

    g.setColor(scalaIORed)
    g.setClip(0, 0, w / 2, h)
    g.setFont(new Font(timesRoman, Font.PLAIN, 40))
    drawMultilineString(g, imageDetails.talkTitle, 31, 50, 50)

    g.setColor(scalaIORed)
    g.setFont(new Font(timesRoman, Font.PLAIN, 24))
    drawMultilineString(g, imageDetails.speakerName, 31, 50, h / 2)

    ZIO.fromTry(
      Try(ImageIO.write(img, "png", new File(s"/Users/jeandetoeuf/Desktop/accepted/${imageDetails.speakerName}.png")))
    )
  }

  private def drawMultilineString(g: Graphics2D, text: String, wrapLength: Int, x: Int, y: Int) = {
    val lineHeight = g.getFontMetrics().getHeight()
    WordUtils
      .wrap(text, wrapLength)
      .split("\n")
      .zipWithIndex
      .map { case (line, number) => (line, number * lineHeight) }
      .foreach { case (line, offset) => g.drawString(line, x, y + offset) }
  }
}
