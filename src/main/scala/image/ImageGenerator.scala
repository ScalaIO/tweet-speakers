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

  private val montserratBigPlain = new Font("Montserrat", Font.PLAIN, 40)
  private val montserratSmallPlain = new Font("Montserrat", Font.PLAIN, 32)
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
    drawSpeakerName(g, imageDetails.speaker.formattedName, imageDetails.coSpeaker.map(_.formattedName))
    drawTalkFormat(g, imageDetails.talkFormat.name)

    val speakersName =
      imageDetails.coSpeaker.fold(imageDetails.speaker.formattedName)(
        co => s"${imageDetails.speaker.formattedName} - ${co.formattedName}"
      )

    ZIO.fromTry(
      Try(
        ImageIO
          .write(img, "png", new File(s"${conf.getString("files.outputImagesDir")}/${speakersName}.png"))
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
    val logoHeight = logoWidth * scalaIOPicture.getHeight / scalaIOPicture.getWidth
    g.drawImage(scalaIOPicture, 50, h - 400, logoWidth, logoHeight, backgroundColor, null)
    g.setColor(Color.white)
    g.setFont(montserratSmallPlain)
    val dateY = h - g.getFontMetrics.getHeight * 2 - 15
    g.drawString("29-31 October 2019, Lyon, France", 50, dateY)
    g.drawString("#scalaIO2019", 50, dateY + g.getFontMetrics.getHeight)
  }

  private def drawTalkTitle(g: Graphics2D, talkTitle: String): Unit = {
    g.setColor(Color.white)
    g.setFont(montserratBigPlain)
    drawMultilineString(g, talkTitle, 31, 50, 100)
  }

  private def drawSpeakerName(g: Graphics2D, speakerName: String, coSpeakerName: Option[String]): Unit = {
    val speakerNameGradientWidth = w - h - 100
    g.setPaint(new GradientPaint(speakerNameGradientWidth / 2, 0, scalaIORed, speakerNameGradientWidth, 0, transparent))
    val lineHeight = (g.getFontMetrics().getHeight * 1.5).intValue()
    val rectangleHeight = lineHeight * coSpeakerName.fold(1)(_ => 2)
    g.fillRect(0, h / 2 - rectangleHeight, speakerNameGradientWidth, rectangleHeight)

    g.setColor(Color.white)
    g.setFont(montserratBigPlain)
    val base = h / 2 - rectangleHeight + lineHeight * 3 / 4
    coSpeakerName.fold(drawMultilineString(g, speakerName, 31, 50, base))(co => {
      drawMultilineString(g, speakerName, 31, 50, base)
      drawMultilineString(g, co, 31, 50, base + lineHeight)
    })
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
