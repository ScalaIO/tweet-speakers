package image

import java.awt._
import java.awt.image.BufferedImage
import java.io.File

import com.typesafe.config.{Config, ConfigFactory}
import javax.imageio.ImageIO
import org.apache.commons.text.WordUtils
import zio.ZIO

import scala.util.Try

object ImageGenerator {
  private val scalaIORed = new Color(0xcc1424)
  private val transparent = new Color(0, 0, 0, 0)
  private val backgroundColor: Color = Color.black

  private val montserrat40Plain = new Font("Montserrat", Font.PLAIN, 40)
  private val ratio = 3
  private val h = 267 * ratio
  private val w = 507 * ratio
  val conf: Config = ConfigFactory.load

  def of(imageDetails: ImageDetails) = {
    val img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    val g = img.getGraphics.asInstanceOf[Graphics2D]

    initializeCanvas(g)
    drawProfilePicture(g, imageDetails)
    drawScalaIOLogo(g)
    drawTalkTitle(g, imageDetails)
    drawSpeakerName(g, imageDetails)

    ZIO.fromTry(
      Try(ImageIO.write(img, "png", new File(s"${conf.getString("output.imageDir")}/${imageDetails.speakerName}.png")))
    )
  }

  private def initializeCanvas(g: Graphics2D) = {
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setBackground(backgroundColor)
  }

  private def drawProfilePicture(g: Graphics2D, imageDetails: ImageDetails): Unit = {
    val profilePicture: BufferedImage = ImageIO.read(imageDetails.speakerPicture)
    g.drawImage(profilePicture, w - h, 0, h, h, backgroundColor, null)
    val speakerPictureGradientWidth = 100
    g.setPaint(new GradientPaint(w - h, 0, backgroundColor, w - h + speakerPictureGradientWidth, 0, transparent))
    g.fillRect(0, 0, w - h + speakerPictureGradientWidth, h)
  }

  private def drawScalaIOLogo(g: Graphics2D) = {
    val scalaIOPicture: BufferedImage =
      ImageIO.read(new File(this.getClass.getResource("scalaio_black.png").getFile))
    val logoWidth = 600
    g.drawImage(
      scalaIOPicture,
      50,
      h - 300,
      logoWidth,
      logoWidth * scalaIOPicture.getHeight / scalaIOPicture.getWidth,
      backgroundColor,
      null
    )
  }

  private def drawTalkTitle(g: Graphics2D, imageDetails: ImageDetails): Unit = {
    g.setColor(scalaIORed)
    g.setFont(montserrat40Plain)
    drawMultilineString(g, imageDetails.talkTitle, 31, 50, 100)
  }

  private def drawSpeakerName(g: Graphics2D, imageDetails: ImageDetails): Unit = {
    val speakerNameGradientWidth = w - h - 100
    g.setPaint(new GradientPaint(speakerNameGradientWidth / 2, 0, scalaIORed, speakerNameGradientWidth, 0, transparent))
    val lineHeight = g.getFontMetrics().getHeight
    g.fillRect(0, h / 2 - lineHeight, speakerNameGradientWidth, (lineHeight * 1.5).intValue())

    g.setColor(Color.white)
    g.setFont(montserrat40Plain)
    drawMultilineString(g, imageDetails.speakerName, 31, 50, h / 2)
  }

  private def drawMultilineString(g: Graphics2D, text: String, wrapLength: Int, x: Int, y: Int) = {
    val lineHeight = g.getFontMetrics().getHeight
    WordUtils
      .wrap(text, wrapLength)
      .split("\n")
      .zipWithIndex
      .map { case (line, number) => (line, number * lineHeight) }
      .foreach { case (line, offset) => g.drawString(line, x, y + offset) }
  }
}
