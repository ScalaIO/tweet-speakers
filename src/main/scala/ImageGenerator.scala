import java.awt.{Color, Font}
import java.awt.image.BufferedImage
import java.io.File

import javax.imageio.ImageIO

object ImageGenerator {
	val scalaIORed = 0xbc1321

	def of(imageDetails: ImageDetails) = {
		val h,w = 500
		val timesRoman = "TimesRoman"

		val img = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB)

		val g = img.getGraphics
		g.setColor(Color.WHITE)
		g.fillRect(0,0,h-1,w-1)

		g.setColor(new Color(scalaIORed))
		for(offset <- 1 until 3){
			g.drawLine(50, 250 + offset, 200, 250 + offset)
		}

		val profilePicture: BufferedImage = ImageIO.read(imageDetails.speakerPicture)
		g.drawImage(profilePicture, 270, 100, 200, 200, Color.WHITE, null)

		val scalaIOPicture: BufferedImage = ImageIO.read(new File(this.getClass.getResource("scalaio_small.png").getFile))
		g.drawImage(scalaIOPicture, 270, 400, 100, 42, Color.WHITE, null)

		g.setFont(new Font(timesRoman, Font.PLAIN, 40))
		g.drawString(imageDetails.talkTitle, 50, 50)
		g.setFont(new Font(timesRoman, Font.PLAIN, 24))
		g.drawString(imageDetails.speakerName, 50, 150)

		ImageIO.write(img, "png", new File(s"/Users/jeandetoeuf/Desktop/accepted/${imageDetails.speakerName}.png"))
	}

}
