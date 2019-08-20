package image

import java.awt._
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL

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
    drawProfilePicture(g, imageDetails.speaker.picture, imageDetails.coSpeaker.map(_.picture))
    drawScalaIOLogo(g)
    drawTalkTitle(g, imageDetails.talkTitle)
    drawSpeakerName(g, imageDetails.speaker.name)
    drawTalkFormat(g, imageDetails.talkFormat.name)

    val fileName =
      imageDetails.coSpeaker.fold(imageDetails.speaker.name)(co => s"${imageDetails.speaker.name}-${co.name}")

    ZIO.fromTry(
      Try(
        ImageIO
          .write(img, "png", new File(s"${conf.getString("files.outputImagesDir")}/${fileName}.png"))
      )
    )
  }

  private def initializeCanvas(g: Graphics2D) = {
    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g.setBackground(backgroundColor)
  }

  private def drawProfilePicture(g: Graphics2D, speakerPicture: URL, coSpeakerPicture: Option[URL]): Unit = {
    val (maxWidth, maxHeight) = (h, h)
    val speakerImage: BufferedImage = ImageIO.read(speakerPicture)
    coSpeakerPicture.fold {
      drawImage(g, speakerImage, maxWidth, maxHeight, w - h, 0)
    } { coSpeakerPic =>
      drawImage(g, speakerImage, maxWidth / 2, maxHeight, w - h, 0)
      val coSpeakerImage: BufferedImage = ImageIO.read(coSpeakerPic)
      drawImage(g, coSpeakerImage, maxWidth / 2, maxHeight, w - h + maxWidth / 2, 0)
    }
    val speakerPictureGradientWidth = 100
    g.setPaint(new GradientPaint(w - h, 0, backgroundColor, w - h + speakerPictureGradientWidth, 0, transparent))
    g.fillRect(0, 0, w - h + speakerPictureGradientWidth, h)
  }

  private def drawImage(g: Graphics2D, image: BufferedImage, maxWidth: Int, maxHeight: Int, x: Int, y: Int) = {
    val savedClip = g.getClip
    g.setClip(x, y, maxWidth, maxHeight)
    val resizedWidth = maxHeight * image.getWidth / image.getHeight
    val resizedHeight = maxWidth * image.getHeight / image.getWidth
    val width = Math.max(resizedWidth, maxWidth)
    val height = Math.max(maxHeight, resizedHeight)
    g.drawImage(
      image,
      x + (maxWidth - width) / 2,
      y + (maxHeight - Math.max(resizedHeight, maxHeight)) / 2,
      width,
      height,
      backgroundColor,
      null
    )
    g.setClip(savedClip)
  }

  private def drawScalaIOLogo(g: Graphics2D) = {
    val scalaIOPicture: BufferedImage = ImageIO.read(this.getClass.getResource("scalaio_black.png"))
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

  private def drawTalkTitle(g: Graphics2D, talkTitle: String): Unit = {
    g.setColor(scalaIORed)
    g.setFont(montserrat40Plain)
    drawMultilineString(g, talkTitle, 31, 50, 100)
  }

  private def drawSpeakerName(g: Graphics2D, speakerName: String): Unit = {
    val speakerNameGradientWidth = w - h - 100
    g.setPaint(new GradientPaint(speakerNameGradientWidth / 2, 0, scalaIORed, speakerNameGradientWidth, 0, transparent))
    val lineHeight = g.getFontMetrics().getHeight
    g.fillRect(0, h / 2 - lineHeight, speakerNameGradientWidth, (lineHeight * 1.5).intValue())

    g.setColor(Color.white)
    g.setFont(montserrat40Plain)
    drawMultilineString(g, speakerName, 31, 50, h / 2)
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

  def drawTalkFormat(g: Graphics2D, talkFormat: String) = {
    g.setColor(scalaIORed)
    val offsetT = 50
    val (marginH, marginT) = (50, 30)
    val rectangle = g.getFontMetrics.getStringBounds(talkFormat, g)
    val (width, height) = (rectangle.getWidth.intValue() + marginH, rectangle.getHeight.intValue() + marginT)
    g.fillRect(w - width, offsetT, width, height)

    g.setColor(Color.white)
    g.drawString(talkFormat, w - width + marginH / 2, offsetT + height - rectangle.getHeight.intValue() / 2)
  }
}
