/**
 * @title ImageWaterMarkUtil.java
 * @description TODO
 * @package lm.com.framework.image
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月9日下午6:55:23
 * @version v1.0
 */
package lm.com.framework.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import lm.com.framework.StringUtil;
import lm.com.framework.io.FileUtil;

/**
 * 图片水印工具类
 * 
 * @author mrluo735
 *
 */
public class ImageWaterMarkUtil {
	/**
	 * 重载+1 图片添加文字水印
	 * 
	 * @param srcImageFile
	 *            源图片地址
	 * @param destImageFile
	 *            目标图片地址
	 * @param text
	 *            水印文字
	 * @param fontName
	 *            水印的字体名称
	 * @param fontStyle
	 *            水印的字体样式 (<code>Font.PLAIN</code>, <code>Font.BOLD</code>,
	 *            <code>Font.ITALIC</code>)
	 * @param fontSize
	 *            水印的字体大小
	 * @param fontColor
	 *            水印的字体颜色
	 * @param position
	 *            水印位置
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @param degree
	 *            水印旋转角度
	 */
	public final static void addWaterMark(String srcImgPath, String destImgPath, String text, String fontName,
			int fontStyle, int fontSize, Color fontColor, EnumWaterMarkPosition position, float alpha, Integer degree) {
		try {
			// 加载源图片
			File srcFile = new File(srcImgPath);
			Image srcImage = ImageIO.read(srcFile);

			int srcImgWidth = srcImage.getWidth(null);
			int srcImgHeight = srcImage.getHeight(null);

			int waterMarkWidth = StringUtil.getLength(text) * fontSize;
			Map<String, Integer> map = calcPosition(srcImgWidth, srcImgHeight, waterMarkWidth, fontSize, position);
			addWaterMark(srcImgPath, destImgPath, text, fontName, fontStyle, fontSize, fontColor, map.get("x"),
					map.get("y"), alpha, degree);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 重载+2 图片添加文字水印
	 * 
	 * @param srcImageFile
	 *            源图片地址
	 * @param destImageFile
	 *            目标图片地址
	 * @param text
	 *            水印文字
	 * @param fontName
	 *            水印的字体名称
	 * @param fontStyle
	 *            水印的字体样式 (<code>Font.PLAIN</code>, <code>Font.BOLD</code>,
	 *            <code>Font.ITALIC</code>)
	 * @param fontSize
	 *            水印的字体大小
	 * @param fontColor
	 *            水印的字体颜色
	 * @param x
	 *            水印文字距离目标图片左侧的偏移量，如果x<0, 则在正中间
	 * @param y
	 *            水印文字距离目标图片左侧的偏移量，如果y<0, 则在正中间
	 * @param alpha
	 *            透明度：alpha 必须是范围 [0.0, 1.0] 之内（包含边界值）的一个浮点数字
	 * @param degree
	 *            水印旋转角度
	 */
	public final static void addWaterMark(String srcImgPath, String destImgPath, String text, String fontName,
			int fontStyle, int fontSize, Color fontColor, int x, int y, float alpha, Integer degree) {
		try {
			// 加载源图片
			File srcFile = new File(srcImgPath);
			Image srcImage = ImageIO.read(srcFile);

			int width = srcImage.getWidth(null);
			int height = srcImage.getHeight(null);
			String extension = FileUtil.getExtension(srcImgPath);

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = image.createGraphics();
			g.drawImage(srcImage, 0, 0, width, height, null);
			g.setColor(fontColor);
			g.setFont(new Font(fontName, fontStyle, fontSize));
			// 设置水印图片的透明度。
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			// 计算文字水印宽，高
			int waterTextWidth = StringUtil.getLength(text) * fontSize;
			int waterTextHeight = fontSize;
			// 设置水印图片的位置。
			int widthDiff = width - waterTextWidth;
			int heightDiff = height - waterTextHeight;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}

			// y坐标的起点=字体大小-2
			y = y + (fontSize - 2);

			if (degree != null) {
				// 设置水印自身旋转
				double wx = waterTextWidth / 2 + x;
				double wy = waterTextHeight / 2 + y;
				g.rotate(Math.toRadians(degree), wx, wy);
			}

			// 在指定坐标绘制水印文字
			g.drawString(text, x, y);
			g.dispose();

			// 输出到文件流
			ImageIO.write((BufferedImage) image, extension, new File(destImgPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重载+3 给图片添加水印、可设置水印图片旋转角度
	 * 
	 * @param srcImgPath
	 *            源图片路径
	 * @param waterImgPath
	 *            水印图片路径
	 * @param destImgPath
	 *            目标图片路径
	 * @param position
	 *            水印位置
	 * @param alpha
	 *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
	 * @param degree
	 *            水印图片旋转角度
	 */
	public static void addWaterMark(String srcImgPath, String waterImgPath, String destImgPath,
			EnumWaterMarkPosition position, float alpha, Integer degree) {
		try {
			// 加载源图片
			File srcFile = new File(srcImgPath);
			Image srcImage = ImageIO.read(srcFile);
			int srcImgWidth = srcImage.getWidth(null);
			int srcImgHeight = srcImage.getHeight(null);

			File waterFile = new File(waterImgPath);
			Image waterImage = ImageIO.read(waterFile);
			int waterMarkWidth = waterImage.getWidth(null);
			int waterMarkHeight = waterImage.getHeight(null);

			Map<String, Integer> map = calcPosition(srcImgWidth, srcImgHeight, waterMarkWidth, waterMarkHeight,
					position);
			addWaterMark(srcImgPath, waterImgPath, destImgPath, map.get("x"), map.get("y"), alpha, degree);
		} catch (IllegalArgumentException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 重载+4 给图片添加水印、可设置水印图片旋转角度
	 * 
	 * @param srcImgPath
	 *            源图片路径
	 * @param waterImgPath
	 *            水印图片路径
	 * @param destImgPath
	 *            目标图片路径
	 * @param x
	 *            水印图片距离目标图片左侧的偏移量，如果x<0, 则在正中间
	 * @param y
	 *            水印图片距离目标图片上侧的偏移量，如果y<0, 则在正中间
	 * @param alpha
	 *            透明度(0.0 -- 1.0, 0.0为完全透明，1.0为完全不透明)
	 * @param degree
	 *            水印图片旋转角度
	 */
	public static void addWaterMark(String srcImgPath, String waterImgPath, String destImgPath, int x, int y,
			float alpha, Integer degree) {
		OutputStream os = null;
		try {
			// 加载源图片
			Image srcImg = ImageIO.read(new File(srcImgPath));
			// 获取源图片的扩展名，宽，高
			String extension = FileUtil.getExtension(srcImgPath);
			int width = srcImg.getWidth(null);
			int height = srcImg.getHeight(null);
			// 将源图片加载到内存。
			BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
					BufferedImage.TYPE_INT_RGB);

			// 得到画笔对象
			// Graphics g= buffImg.getGraphics();
			Graphics2D g = buffImg.createGraphics();
			// 设置对线段的锯齿状边缘处理
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null), srcImg.getHeight(null), Image.SCALE_SMOOTH), 0,
					0, null);

			// 加载水印图片(水印一般为gif或者png的，这样可设置透明度)
			ImageIcon waterImgIcon = new ImageIcon(waterImgPath);
			// 得到Image对象。
			Image waterImg = waterImgIcon.getImage();
			int waterImgWidth = waterImg.getWidth(null);
			int waterImgHeight = waterImg.getHeight(null);
			// 设置水印图片的透明度。
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

			// 设置水印图片的位置。
			int widthDiff = width - waterImgWidth;
			int heightDiff = height - waterImgHeight;
			if (x < 0) {
				x = widthDiff / 2;
			} else if (x > widthDiff) {
				x = widthDiff;
			}
			if (y < 0) {
				y = heightDiff / 2;
			} else if (y > heightDiff) {
				y = heightDiff;
			}
			// 设置水印旋转
			if (null != degree) {
				// 设置水印绕原图中心点旋转
				// g.rotate(Math.toRadians(degree), (double) buffImg.getWidth()
				// / 2, (double) buffImg.getHeight() / 2);
				// 设置水印自身旋转
				double wx = waterImgWidth / 2 + x;
				double wy = waterImgHeight / 2 + y;
				g.rotate(Math.toRadians(degree), wx, wy);
			}
			// 将水印图片"画"在原有的图片的制定位置。
			g.drawImage(waterImg, x, y, waterImgWidth, waterImgHeight, null);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
			// 关闭画笔。
			g.dispose();

			// 生成图片
			os = new FileOutputStream(destImgPath);
			ImageIO.write(buffImg, extension, os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != os)
					os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// region 计算水印位置枚举的x,y坐标
	/**
	 * 计算水印位置枚举的x,y坐标
	 * 
	 * @param srcImgWidth
	 *            原图的宽
	 * @param srcImgHeight
	 *            原图的高
	 * @param waterMarkWidth
	 *            水印的宽
	 * @param waterMarkHeight
	 *            水印的高
	 * @param position
	 *            水印位置枚举
	 * @return Map {x:10, y:10}
	 */
	private static Map<String, Integer> calcPosition(int srcImgWidth, int srcImgHeight, int waterMarkWidth,
			int waterMarkHeight, EnumWaterMarkPosition position) {
		Map<String, Integer> map = new HashMap<>();
		int x = 0, y = 0, initialX = 10, initialY = 10;
		switch (position) {
			case LeftTop:
				map.put("x", initialX);
				map.put("y", initialY);
				break;
			case LeftMiddle:
				map.put("x", initialX);
				y = (srcImgHeight - waterMarkHeight) / 2;
				map.put("y", y);
				break;
			case LeftBottom:
				map.put("x", initialX);
				y = srcImgHeight - waterMarkHeight - initialY;
				break;
			case CenterTop:
				x = (srcImgWidth - waterMarkWidth) / 2;
				map.put("x", x);
				map.put("y", initialY);
				break;
			case CenterMiddle:
				x = (srcImgWidth - waterMarkWidth) / 2;
				y = (srcImgHeight - waterMarkHeight) / 2;
				map.put("x", x);
				map.put("y", y);
				break;
			case CenterBottom:
				x = (srcImgWidth - waterMarkWidth) / 2;
				y = srcImgHeight - waterMarkHeight - initialY;
				map.put("x", x);
				map.put("y", y);
				break;
			case RightTop:
				x = srcImgWidth - waterMarkWidth - initialX;
				map.put("x", x);
				map.put("y", initialY);
				break;
			case RightMiddle:
				x = srcImgWidth - waterMarkWidth - initialX;
				y = (srcImgHeight - waterMarkHeight) / 2;
				map.put("x", x);
				map.put("y", y);
				break;
			case RightBottom:
				x = srcImgWidth - waterMarkWidth - initialX;
				y = srcImgHeight - waterMarkHeight - initialY;
				map.put("x", x);
				map.put("y", y);
				break;
			default:
				break;
		}
		return map;
	}
	// endregion
}
