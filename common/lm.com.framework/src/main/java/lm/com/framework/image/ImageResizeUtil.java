/**
 * @title ImageResizeUtil.java
 * @description TODO
 * @package lm.com.framework.image
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月9日下午6:38:57
 * @version v1.0
 */
package lm.com.framework.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.Kernel;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import lm.com.framework.StreamUtil;
import net.coobird.thumbnailator.Thumbnailator;

/**
 * 图片缩放工具类
 * 
 * @author mrluo735
 *
 */
@SuppressWarnings("restriction")
public class ImageResizeUtil {
	/**
	 * 重载+1 缩放图片
	 * <p>
	 * gif缩放后会转成jpg
	 * </p>
	 * 
	 * @param originalFile
	 *            原图片
	 * @param resizedFile
	 *            缩放后的图片
	 * @param width
	 *            图片宽
	 * @param extension
	 *            图片格式 jpg, png, gif(缩放后会转成jpg)
	 * @throws IOException
	 */
	public static void resize(File originalFile, File resizedFile, int width, int height) throws IOException {
		byte[] bytes = StreamUtil.file2Bytes(originalFile);
		FileOutputStream outputStream = new FileOutputStream(resizedFile);
		resize(bytes, outputStream, width, height);
		outputStream.close();
	}

	/**
	 * 重载+2 缩放图片
	 * <p>
	 * gif缩放后会转成jpg
	 * </p>
	 * 
	 * @param originalFile
	 *            原图片
	 * @param resizedFile
	 *            缩放后的图片
	 * @param width
	 *            缩放后的图片宽
	 * @param height
	 *            缩放后的图片高
	 * @throws IOException
	 */
	public static void resize(byte[] originalSource, OutputStream outputStream, int width, int height)
			throws IOException {
		String format = ImageUtil.getImageType(originalSource);
		if ("gif".equals(format.toLowerCase())) {
			InputStream is = StreamUtil.toInputStream(originalSource);
			Thumbnailator.createThumbnail(is, outputStream, width, height);
			return;
		}

		Image inputImage = Toolkit.getDefaultToolkit().createImage(originalSource);
		ImageUtil.waitForImage(inputImage);
		int imageWidth = inputImage.getWidth(null);
		if (imageWidth < 1)
			throw new IllegalArgumentException("image width " + imageWidth + " is out of range");
		int imageHeight = inputImage.getHeight(null);
		if (imageHeight < 1)
			throw new IllegalArgumentException("image height " + imageHeight + " is out of range");

		// Create output image.
		double scaleW = (double) imageWidth / (double) width;
		double scaleY = (double) imageHeight / (double) height;
		if (scaleW >= 0 && scaleY >= 0) {
			if (scaleW > scaleY) {
				height = -1;
			} else {
				width = -1;
			}
		}
		Image outputImage = inputImage.getScaledInstance(width, height, java.awt.Image.SCALE_DEFAULT);
		if (!ImageUtil.checkImage(outputImage))
			throw new RuntimeException("image invalid");
		ImageUtil.toImage(outputStream, outputImage, format);
	}

	/**
	 * 重载+1 缩放图片
	 * <p>
	 * 缩放后为jpg格式
	 * <p>
	 * 
	 * @param originalFile
	 *            原图片
	 * @param resizedFile
	 *            缩放后的图片
	 * @param newWidth
	 *            宽度
	 * @param quality
	 *            缩放比例 (等比例,0 - 1之间)
	 * @throws IOException
	 */
	public static void resize2Jpg(File originalFile, File resizedFile, int newWidth, float quality) throws IOException {
		ImageIcon imageIcon = new ImageIcon(originalFile.getCanonicalPath());
		FileOutputStream outputStream = new FileOutputStream(resizedFile);

		int width = 0, height = 0;
		Image image = imageIcon.getImage();
		int rawWidth = image.getWidth(null);
		int rawHeight = image.getHeight(null);
		if (rawWidth > rawHeight) {
			width = newWidth;
			height = (newWidth * rawHeight) / rawWidth;
		} else {
			width = (newWidth * rawWidth) / rawHeight;
			height = newWidth;
		}
		resize2Jpg(imageIcon, outputStream, width, height, quality);
		outputStream.close();
	}

	/**
	 * 重载+2 缩放图片
	 * <p>
	 * 缩放后为jpg格式
	 * <p>
	 * 
	 * @param originalSource
	 *            原图片
	 * @param outputStream
	 *            缩放后的图片Stream
	 * @param width
	 *            缩放后的图片宽度
	 * @param height
	 *            缩放后的图片高度
	 * @param quality
	 *            缩放比例 (等比例,0 - 1之间)
	 * @throws IOException
	 */
	public static void resize2Jpg(byte[] originalSource, OutputStream outputStream, int width, int height,
			float quality) throws IOException {
		ImageIcon imageIcon = new ImageIcon(originalSource);
		resize2Jpg(imageIcon, outputStream, width, height, quality);
	}

	/**
	 * 重载+3 缩放图片
	 * <p>
	 * 缩放后为jpg格式
	 * <p>
	 * 
	 * @param imageIcon
	 *            原图片
	 * @param outputStream
	 *            缩放后的图片Stream
	 * @param width
	 *            缩放后的图片宽度
	 * @param height
	 *            缩放后的图片高度
	 * @param quality
	 *            缩放比例 (等比例,0 - 1之间)
	 * @throws IOException
	 */
	private static void resize2Jpg(ImageIcon imageIcon, OutputStream outputStream, int width, int height, float quality)
			throws IOException {
		if (quality < 0 || quality > 1)
			throw new IllegalArgumentException("Quality has to be between 0 and 1");

		// 获取原图片大小，计算缩放比例
		Image image = imageIcon.getImage();
		int rawWidth = image.getWidth(null);
		int rawHeight = image.getHeight(null);
		double scaleW = (double) rawWidth / (double) width;
		double scaleY = (double) rawHeight / (double) height;
		if (scaleW >= 0 && scaleY >= 0) {
			if (scaleW > scaleY) {
				height = -1;
			} else {
				width = -1;
			}
		}
		// 创建此图像的缩放版本, 将返回一个新的图像对象
		// 该对象将在默认情况下使图像达到指定的宽度和高度。即使原始源映像已经完全加载，新的图像对象也可以异步加载。
		// 如果宽度或高度是负数，则用一个值来保持原始图像尺寸的长宽比。如果宽度和高度都是负数，则使用原始图像尺寸
		Image resizedImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		// 确保图像中的所有像素都被加载
		Image tempImg = new ImageIcon(resizedImage).getImage();
		// 创建缓冲图片
		BufferedImage bufferedImage = new BufferedImage(tempImg.getWidth(null), tempImg.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		// Copy image to buffered image.
		Graphics g = bufferedImage.createGraphics();
		// Clear background and paint the image.
		g.setColor(Color.white);
		g.fillRect(0, 0, tempImg.getWidth(null), tempImg.getHeight(null));
		g.drawImage(tempImg, 0, 0, null);
		g.dispose();
		// Soften.
		float softenFactor = 0.05f;
		float[] softenArray = { 0, softenFactor, 0, softenFactor, 1 - (softenFactor * 4), softenFactor, 0, softenFactor,
				0 };
		Kernel kernel = new Kernel(3, 3, softenArray);
		ConvolveOp cOp = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		bufferedImage = cOp.filter(bufferedImage, null);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(outputStream);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufferedImage);
		param.setQuality(quality, true);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(bufferedImage);
	}

	/**
	 * 图像切割(按指定起点坐标和宽高切割)
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param result
	 *            切片后的图像地址
	 * @param x
	 *            目标切片起点坐标X
	 * @param y
	 *            目标切片起点坐标Y
	 * @param width
	 *            目标切片宽度
	 * @param height
	 *            目标切片高度
	 */
	public final static void cut(String srcImageFile, String result, int x, int y, int width, int height) {
		try {
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				// 四个参数分别为图像起点坐标和宽高
				// 即: CropImageFilter(int x,int y,int width,int height)
				ImageFilter cropFilter = new CropImageFilter(x, y, width, height);
				Image img = Toolkit.getDefaultToolkit()
						.createImage(new FilteredImageSource(image.getSource(), cropFilter));
				BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(img, 0, 0, width, height, null); // 绘制切割后的图
				g.dispose();
				// 输出为文件
				ImageIO.write(tag, "JPEG", new File(result));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图像切割（指定切片的行数和列数）
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param rows
	 *            目标切片行数。默认2，必须是范围 [1, 20] 之内
	 * @param cols
	 *            目标切片列数。默认2，必须是范围 [1, 20] 之内
	 */
	public final static void cut2(String srcImageFile, String descDir, int rows, int cols) {
		try {
			if (rows <= 0 || rows > 20)
				rows = 2; // 切片行数
			if (cols <= 0 || cols > 20)
				cols = 2; // 切片列数
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > 0 && srcHeight > 0) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int destWidth = srcWidth; // 每张切片的宽度
				int destHeight = srcHeight; // 每张切片的高度
				// 计算切片的宽度和高度
				if (srcWidth % cols == 0) {
					destWidth = srcWidth / cols;
				} else {
					destWidth = (int) Math.floor(srcWidth / cols) + 1;
				}
				if (srcHeight % rows == 0) {
					destHeight = srcHeight / rows;
				} else {
					destHeight = (int) Math.floor(srcWidth / rows) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
						img = Toolkit.getDefaultToolkit()
								.createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 图像切割（指定切片的宽度和高度）
	 * 
	 * @param srcImageFile
	 *            源图像地址
	 * @param descDir
	 *            切片目标文件夹
	 * @param destWidth
	 *            目标切片宽度。默认200
	 * @param destHeight
	 *            目标切片高度。默认150
	 */
	public final static void cut3(String srcImageFile, String descDir, int destWidth, int destHeight) {
		try {
			if (destWidth <= 0)
				destWidth = 200; // 切片宽度
			if (destHeight <= 0)
				destHeight = 150; // 切片高度
			// 读取源图像
			BufferedImage bi = ImageIO.read(new File(srcImageFile));
			int srcWidth = bi.getHeight(); // 源图宽度
			int srcHeight = bi.getWidth(); // 源图高度
			if (srcWidth > destWidth && srcHeight > destHeight) {
				Image img;
				ImageFilter cropFilter;
				Image image = bi.getScaledInstance(srcWidth, srcHeight, Image.SCALE_DEFAULT);
				int cols = 0; // 切片横向数量
				int rows = 0; // 切片纵向数量
				// 计算切片的横向和纵向数量
				if (srcWidth % destWidth == 0) {
					cols = srcWidth / destWidth;
				} else {
					cols = (int) Math.floor(srcWidth / destWidth) + 1;
				}
				if (srcHeight % destHeight == 0) {
					rows = srcHeight / destHeight;
				} else {
					rows = (int) Math.floor(srcHeight / destHeight) + 1;
				}
				// 循环建立切片
				// 改进的想法:是否可用多线程加快切割速度
				for (int i = 0; i < rows; i++) {
					for (int j = 0; j < cols; j++) {
						// 四个参数分别为图像起点坐标和宽高
						// 即: CropImageFilter(int x,int y,int width,int height)
						cropFilter = new CropImageFilter(j * destWidth, i * destHeight, destWidth, destHeight);
						img = Toolkit.getDefaultToolkit()
								.createImage(new FilteredImageSource(image.getSource(), cropFilter));
						BufferedImage tag = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
						Graphics g = tag.getGraphics();
						g.drawImage(img, 0, 0, null); // 绘制缩小后的图
						g.dispose();
						// 输出为文件
						ImageIO.write(tag, "JPEG", new File(descDir + "_r" + i + "_c" + j + ".jpg"));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
