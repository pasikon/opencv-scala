package tests

import java.awt.image.BufferedImage
import javax.swing.{ImageIcon, JFrame, JLabel}

import org.bytedeco.javacpp.opencv_core.{Mat, Size}
import org.bytedeco.javacpp.{opencv_core, opencv_imgcodecs, opencv_imgproc}

object TestOpenCVMatApi extends App {

  /**
    * 1. Load image from file\
    * 2. OpenCV resize image
    * 3. Get byte array from resized image
    * 4. create another OpenCV Mat from 3. byte array
    * 5. Get byte array from 4. Mat
    * 6. Using 5. byte array create BufferedImage & show
    *
    * This just proves correct OpenCV & Java API usage
    *
    * @param args
    */
  override def main(args: Array[String]): Unit = {
    val _width = 150
    val _height = 150

    val matSrc: Mat = opencv_imgcodecs.imread("/Users/mzurek/PycharmProjects/cnn_finetune/seedlings_data/test/0c27cf05f.png")
    val matDst = new Mat()

    val size = new Size(_width, _height)
    opencv_imgproc.resize(matSrc, matDst, size)

    println(matDst.isContinuous)
    println(matDst.total() * matDst.channels())

    val darr: Array[Byte] = new Array[Byte]((matDst.total() * matDst.channels()).toInt)
    matDst.data().get(darr)

    // form obtained byte array create another Mat to prove correct params etc.
    val myPic = new Mat(size, opencv_core.CV_8UC3)
    myPic.data().put(darr, 0, darr.length)

    val darrMyPic: Array[Byte] = new Array[Byte]((myPic.total() * myPic.channels()).toInt)
    myPic.data().get(darrMyPic)
    println(myPic.total() * myPic.channels())

    try {
      val bufImage = new BufferedImage(_width, _height, BufferedImage.TYPE_3BYTE_BGR)
      bufImage.getRaster.setDataElements(0, 0, _width, _height, darrMyPic)

      val frame = new JFrame
      val icon = new ImageIcon(bufImage)
      frame.getContentPane.add(new JLabel(icon))
      frame.pack()
      frame.setVisible(true)
    } catch {
      case e: Exception =>
        e.printStackTrace()
    }

    println("Done")
  }

}
