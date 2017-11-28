/**
 * @title ImageUtil.java
 * @description TODO
 * @package lm.com.framework.image
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月9日下午6:28:53
 * @version v1.0
 */
package lm.com.framework.image;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lm.com.framework.StreamUtil;
import lm.com.framework.StringUtil;

/**
 * 图片工具类
 * 
 * @author mrluo735
 *
 */
public class ImageUtil {
	private static final Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	/**
	 * 图片后缀名数组
	 */
	private final static String[] IMAGES_EXT = { "bmp", "dib", "gif", "jfif", "jpe", "jpeg", "jpg", "png", "tif",
			"tiff", "ico", "bw", "cel", "cut", "dib", "icb", "pcc", "pcd", "pdd", "pcx", "pic", "pbm", "pgm", "ppm",
			"psd", "rgb", "rgba", "rla", "rle", "rpf", "scr", "sgi", "tga", "vda", "vst", "win", "emf", "wmf" };

	public static final MediaTracker tracker = new MediaTracker(new Component() {
		private static final long serialVersionUID = 1234162663955668507L;
	});

	/**
	 * 检查图片是否有效
	 * 
	 * @param image
	 */
	public static boolean checkImage(Image image) {
		waitForImage(image);
		int imageWidth = image.getWidth(null);
		if (imageWidth < 1) {
			logger.info("image width " + imageWidth + " is out of range");
			return false;
		}
		int imageHeight = image.getHeight(null);
		if (imageHeight < 1) {
			logger.info("image height " + imageHeight + " is out of range");
			return false;
		}
		return true;
	}

	/**
	 * Waits for given image to load. Use before querying image
	 * height/width/colors.
	 */
	public static void waitForImage(Image image) {
		try {
			tracker.addImage(image, 0);
			tracker.waitForID(0);
			tracker.removeImage(image, 0);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param formatName
	 *            包含格式非正式名称的 String：如JPG、JPEG、GIF等
	 * @param destImageFile
	 *            目标图像地址
	 */
	public final static boolean convert(String srcImageFile, String formatName, String destImageFile) {
		try {
			File file = new File(srcImageFile);
			file.canRead();
			file.canWrite();
			BufferedImage src = ImageIO.read(file);
			return ImageIO.write(src, formatName, new File(destImageFile));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 转换成byte[]
	 * 
	 * @param filePath
	 * @return
	 */
	public static byte[] toBytes(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.exists())
				return null;
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
			int readLength = -1;
			int bufferSize = 1024;
			byte bytes[] = new byte[bufferSize];
			while ((readLength = fis.read(bytes, 0, bufferSize)) != -1) {
				byteStream.write(bytes, 0, readLength);
			}
			byte[] byteImage = byteStream.toByteArray();
			fis.close();
			byteStream.close();

			return byteImage;
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 转换Image数据为byte数组
	 * 
	 * @param image
	 *            Image对象
	 * @param format
	 *            image格式字符串.如"gif","png"
	 * @return byte数组
	 */
	public static byte[] toBytes(Image image, String format) {
		BufferedImage bImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics bg = bImage.getGraphics();
		bg.drawImage(image, 0, 0, null);
		bg.dispose();

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			ImageIO.write(bImage, format, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}

	/**
	 * 转换byte数组为Image
	 * 
	 * @param bytes
	 * @return Image
	 */
	public static Image bytesToImage(byte[] bytes) {
		Image image = Toolkit.getDefaultToolkit().createImage(bytes);
		try {
			MediaTracker mt = new MediaTracker(new Label());
			mt.addImage(image, 0);
			mt.waitForAll();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return image;
	}

	/**
	 * 将流转换成图片
	 * 
	 * @param outputStream
	 * @param outputImage
	 * @param format
	 * @throws java.io.IOException
	 */
	public static void toImage(OutputStream outputStream, Image outputImage, String format) throws java.io.IOException {
		int outputWidth = outputImage.getWidth(null);
		if (outputWidth < 1)
			throw new IllegalArgumentException("output image width " + outputWidth + " is out of range");
		int outputHeight = outputImage.getHeight(null);
		if (outputHeight < 1)
			throw new IllegalArgumentException("output image height " + outputHeight + " is out of range");

		// Get a buffered image from the image.
		BufferedImage bi = new BufferedImage(outputWidth, outputHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D biContext = bi.createGraphics();
		biContext.drawImage(outputImage, 0, 0, null);
		ImageIO.write(bi, format, outputStream);
		outputStream.flush();
		outputStream.close();
	}

	/**
	 * 重载+1 获取图片文件实际类型
	 * <p>
	 * 若不是图片则返回""
	 * </p>
	 * 
	 * @param bytes
	 * @return fileType
	 * 
	 */
	public final static String getImageType(byte[] bytes) {
		if (isImage(bytes) == null)
			return "";

		InputStream inputStream = null;
		ImageInputStream iis = null;
		try {
			inputStream = StreamUtil.toInputStream(bytes);
			iis = ImageIO.createImageInputStream(inputStream);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				return "";
			}
			ImageReader reader = iter.next();
			return reader.getFormatName();
		} catch (IOException e) {
			return "";
		} catch (Exception e) {
			return "";
		} finally {
			try {
				inputStream.close();
				iis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 重载+2 获取图片文件实际类型
	 * <p>
	 * 若不是图片则返回""
	 * </p>
	 * 
	 * @param File
	 * @return fileType
	 * 
	 */
	public final static String getImageType(File file) {
		if (isImage(file) == null)
			return "";
		ImageInputStream iis = null;
		try {
			iis = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
			if (!iter.hasNext()) {
				return "";
			}
			ImageReader reader = iter.next();
			return reader.getFormatName();
		} catch (IOException e) {
			return "";
		} catch (Exception e) {
			return "";
		} finally {
			try {
				iis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 重载+1 判断文件是否为图片
	 * 
	 * @param file
	 * @return Image 是 | null 否
	 * 
	 */
	public static final Image isImage(byte[] bytes) {
		Image img = null;
		InputStream inputStream = null;
		try {
			inputStream = StreamUtil.toInputStream(bytes);
			img = ImageIO.read(inputStream);
			if (null == img || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
				return null;
			}
			return img;
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 重载+2 判断文件是否为图片
	 * 
	 * @param file
	 * @return Image 是 | null 否
	 * 
	 */
	public static final Image isImage(File imageFile) {
		if (!imageFile.exists()) {
			return null;
		}
		Image img = null;
		try {
			img = ImageIO.read(imageFile);
			if (null == img || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
				return null;
			}
			return img;
		} catch (IOException e) {
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 根据后缀判断是否为图片
	 * 
	 * @param extension
	 * @return
	 */
	public static final boolean isImageByExtension(String extension) {
		if (StringUtil.isNullOrWhiteSpace(extension)) {
			return false;
		}
		for (int index = 0; index < IMAGES_EXT.length; index++) {
			if (IMAGES_EXT[index].equalsIgnoreCase(extension)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 彩色转为黑白
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param destImageFile
	 *            目标图像地址
	 */
	public final static void gray(String srcImageFile, String destImageFile, String formatName) {
		try {
			BufferedImage src = ImageIO.read(new File(srcImageFile));
			ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
			ColorConvertOp op = new ColorConvertOp(cs, null);
			src = op.filter(src, null);
			ImageIO.write(src, formatName, new File(destImageFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 是否为jpg
	 * 
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public static boolean isJPEG(InputStream source) throws IOException {
		InputStream iis = source;

		if (!source.markSupported()) {
			throw new IllegalArgumentException("Input stream must support mark");
		}

		iis.mark(30);
		// If the first two bytes are a JPEG SOI marker, it's probably
		// a JPEG file. If they aren't, it definitely isn't a JPEG file.
		try {
			int byte1 = iis.read();
			int byte2 = iis.read();
			if ((byte1 == 0xFF) && (byte2 == 0xD8)) {
				return true;
			}
		} finally {
			iis.reset();
		}

		return false;
	}

	/**
	 * 是否为bmp
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static boolean isBMP(InputStream in) throws IOException {
		if (!in.markSupported()) {
			throw new IllegalArgumentException("Input stream must support mark");
		}

		byte[] b = new byte[2];
		try {
			in.mark(30);
			in.read(b);
		} finally {
			in.reset();
		}

		return (b[0] == 0x42) && (b[1] == 0x4d);
	}

	/**
	 * 是否为gif
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static boolean isGIF(InputStream in) throws IOException {
		if (!in.markSupported()) {
			throw new IllegalArgumentException("Input stream must support mark");
		}

		byte[] b = new byte[6];

		try {
			in.mark(30);
			in.read(b);
		} finally {
			in.reset();
		}

		return b[0] == 'G' && b[1] == 'I' && b[2] == 'F' && b[3] == '8' && (b[4] == '7' || b[4] == '9') && b[5] == 'a';
	}

	/**
	 * 是否为bmp
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static boolean isPNG(InputStream in) throws IOException {
		if (!in.markSupported()) {
			throw new IllegalArgumentException("Input stream must support mark");
		}

		byte[] b = new byte[8];
		try {
			in.mark(30);
			in.read(b);
		} finally {
			in.reset();
		}

		return (b[0] == (byte) 137 && b[1] == (byte) 80 && b[2] == (byte) 78 && b[3] == (byte) 71 && b[4] == (byte) 13
				&& b[5] == (byte) 10 && b[6] == (byte) 26 && b[7] == (byte) 10);
	}

	/**
	 * 是否为tiff
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static boolean isTIFF(InputStream in) throws IOException {
		if (!in.markSupported()) {
			throw new IllegalArgumentException("Input stream must support mark");
		}
		byte[] b = new byte[4];
		try {
			in.mark(30);
			in.read(b);
		} finally {
			in.reset();
		}

		return ((b[0] == (byte) 0x49 && b[1] == (byte) 0x49 && b[2] == (byte) 0x2a && b[3] == (byte) 0x00)
				|| (b[0] == (byte) 0x4d && b[1] == (byte) 0x4d && b[2] == (byte) 0x00 && b[3] == (byte) 0x2a));
	}
}
