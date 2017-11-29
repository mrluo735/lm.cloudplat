package lm.com.framework.captcha;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * 验证码Helper类
 * <p>
 * 使用Algerian字体
 * </p>
 * 
 * @author mrluo735
 *
 */
public class CaptchaHelper {
	private static final CaptchaHelper instance = new CaptchaHelper();
	private static Random random = new Random();

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static CaptchaHelper getInstance() {
		return instance;
	}

	// region 私有字段
	/**
	 * 字符集
	 * <p>
	 * 去掉了1,0,i,o几个容易混淆的字符
	 * </p>
	 */
	private String codes = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

	/**
	 * 验证码长度
	 */
	private int length = 4;

	/**
	 * 图片宽度
	 */
	private int width = 200;

	/**
	 * 图片高度
	 */
	private int height = 80;

	/**
	 * 边框颜色
	 */
	private Color borderColor = Color.GRAY;

	/**
	 * 噪点率
	 * <p>
	 * 0.05f
	 * </p>
	 */
	private float noiseRate = 0.05f;

	/**
	 * 字体名称
	 * <p>
	 * Algerian
	 * </p>
	 */
	private String fontName = "Algerian";

	/**
	 * 字体风格
	 * <p>
	 * Font.ITALIC
	 * </p>
	 */
	private int fontStyle = Font.ITALIC;
	// endregion

	// region 属性
	/**
	 * 获取字符集
	 * 
	 * @return
	 */
	public String getCodes() {
		return codes;
	}

	/**
	 * 设置字符集
	 * 
	 * @param codes
	 */
	public void setCodes(String codes) {
		this.codes = codes;
	}

	/**
	 * 获取验证码长度
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/**
	 * 设置验证码长度
	 * 
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/**
	 * 获取宽度
	 * 
	 * @return
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 设置宽度
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 获取高度
	 * 
	 * @return
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 设置高度
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 获取边框颜色
	 * 
	 * @return
	 */
	public Color getBorderColor() {
		return borderColor;
	}

	/**
	 * 设置边框颜色
	 * 
	 * @param borderColor
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * 获取噪点率
	 * 
	 * @return
	 */
	public float getNoiseRate() {
		return noiseRate;
	}

	/**
	 * 设置噪点率
	 * 
	 * @param noiseRate
	 */
	public void setNoiseRate(float noiseRate) {
		this.noiseRate = noiseRate;
	}

	/**
	 * 获取字体名称
	 * 
	 * @return
	 */
	public String getFontName() {
		return fontName;
	}

	/**
	 * 设置字体名称
	 * 
	 * @param fontName
	 */
	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	/**
	 * 获取字体风格
	 * 
	 * @return
	 */
	public int getFontStyle() {
		return fontStyle;
	}

	/**
	 * 设置字体风格
	 * 
	 * @param fontStyle
	 */
	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}
	// endregion

	/**
	 * 重载+1 获取验证码
	 * 
	 * @return
	 */
	public String getVerifyCode() {
		return getVerifyCode(this.length, this.codes);
	}

	/**
	 * 重载+2 获取验证码
	 * 
	 * @param length
	 *            验证码长度
	 * @return
	 */
	public String getVerifyCode(int length) {
		return getVerifyCode(length, this.codes);
	}

	/**
	 * 重载+3 获取验证码
	 * 
	 * @param length
	 *            验证码长度
	 * @param codes
	 *            验证码字符源
	 * @return
	 */
	public String getVerifyCode(int length, String codes) {
		if (codes == null || codes.length() == 0) {
			codes = this.codes;
		}
		int codesLen = codes.length();
		Random rand = new Random(System.currentTimeMillis());
		StringBuilder verifyCode = new StringBuilder(length);
		for (int i = 0; i < length; i++) {
			verifyCode.append(codes.charAt(rand.nextInt(codesLen - 1)));
		}
		return verifyCode.toString();
	}

	/**
	 * 重载+1 生成随机验证码文件,并返回验证码值
	 * 
	 * @param outputFile
	 * @return
	 * @throws IOException
	 */
	public String outputVerifyImage(File outputFile) throws IOException {
		String verifyCode = this.getVerifyCode(this.length);
		this.outputImage(verifyCode, outputFile);
		return verifyCode;
	}

	/**
	 * 重载+2 输出随机验证码图片流,并返回验证码值
	 * 
	 * @param os
	 * @return
	 * @throws IOException
	 */
	public String outputVerifyImage(OutputStream os) throws IOException {
		String verifyCode = this.getVerifyCode(this.length);
		this.outputImage(this.width, this.height, verifyCode, os);
		return verifyCode;
	}

	/**
	 * 重载+3 生成指定验证码图像文件
	 * 
	 * @param code
	 * @param outputFile
	 * @throws IOException
	 */
	public void outputImage(String code, File outputFile) throws IOException {
		if (outputFile == null) {
			return;
		}
		File dir = outputFile.getParentFile();
		if (!dir.exists()) {
			dir.mkdirs();
		}
		try {
			outputFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(outputFile);
			this.outputImage(this.width, this.height, code, fos);
			fos.close();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 重载+4 输出指定验证码图片流
	 * 
	 * @param w
	 * @param h
	 * @param code
	 * @param os
	 * @throws IOException
	 */
	public void outputImage(int w, int h, String code, OutputStream os) throws IOException {
		int verifySize = code.length();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Random rand = new Random();
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Color[] colors = new Color[5];
		Color[] colorSpaces = new Color[] { Color.WHITE, Color.CYAN, Color.GRAY, Color.LIGHT_GRAY, Color.MAGENTA,
				Color.ORANGE, Color.PINK, Color.YELLOW };
		float[] fractions = new float[colors.length];
		for (int i = 0; i < colors.length; i++) {
			colors[i] = colorSpaces[rand.nextInt(colorSpaces.length)];
			fractions[i] = rand.nextFloat();
		}
		Arrays.sort(fractions);

		g2.setColor(borderColor);// 设置边框色
		g2.fillRect(0, 0, w, h);

		Color c = getRandColor(200, 250);
		g2.setColor(c);// 设置背景色  
		g2.fillRect(0, 2, w, h - 4);

		//绘制干扰线  
		Random random = new Random();
		g2.setColor(getRandColor(160, 200));// 设置线条的颜色  
		for (int i = 0; i < 20; i++) {
			int x = random.nextInt(w - 1);
			int y = random.nextInt(h - 1);
			int xl = random.nextInt(6) + 1;
			int yl = random.nextInt(12) + 1;
			g2.drawLine(x, y, x + xl + 40, y + yl + 20);
		}

		// 添加噪点  
		float yawpRate = noiseRate;// 噪声率  
		int area = (int) (yawpRate * w * h);
		for (int i = 0; i < area; i++) {
			int x = random.nextInt(w);
			int y = random.nextInt(h);
			int rgb = getRandomIntColor();
			image.setRGB(x, y, rgb);
		}

		shear(g2, w, h, c);// 使图片扭曲  

		g2.setColor(getRandColor(100, 160));
		int fontSize = h - 4;
		Font font = new Font(this.fontName, this.fontStyle, fontSize);
		g2.setFont(font);
		char[] chars = code.toCharArray();
		for (int i = 0; i < verifySize; i++) {
			AffineTransform affine = new AffineTransform();
			affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1),
					(w / verifySize) * i + fontSize / 2, h / 2);
			g2.setTransform(affine);
			g2.drawChars(chars, i, 1, ((w - 10) / verifySize) * i + 5, h / 2 + fontSize / 2 - 10);
		}

		g2.dispose();
		ImageIO.write(image, "jpg", os);
	}

	/**
	 * 获取随机颜色
	 * 
	 * @param fc
	 * @param bc
	 * @return
	 */
	private Color getRandColor(int fc, int bc) {
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	/**
	 * 获取随机int值的颜色
	 * 
	 * @return
	 */
	private int getRandomIntColor() {
		int[] rgb = getRandomRgb();
		int color = 0;
		for (int c : rgb) {
			color = color << 8;
			color = color | c;
		}
		return color;
	}

	/**
	 * 获取随机rgb颜色
	 * 
	 * @return
	 */
	private int[] getRandomRgb() {
		int[] rgb = new int[3];
		for (int i = 0; i < 3; i++) {
			rgb[i] = random.nextInt(255);
		}
		return rgb;
	}

	/**
	 * 扭曲图片
	 * 
	 * @param g
	 * @param w1
	 * @param h1
	 * @param color
	 */
	private void shear(Graphics g, int w1, int h1, Color color) {
		shearX(g, w1, h1, color);
		shearY(g, w1, h1, color);
	}

	/**
	 * 横向扭曲
	 * 
	 * @param g
	 * @param w1
	 * @param h1
	 * @param color
	 */
	private static void shearX(Graphics g, int w1, int h1, Color color) {
		int period = random.nextInt(2);
		boolean borderGap = true;
		int frames = 1;
		int phase = random.nextInt(2);

		for (int i = 0; i < h1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(0, i, w1, 1, (int) d, 0);
			if (borderGap) {
				g.setColor(color);
				g.drawLine((int) d, i, 0, i);
				g.drawLine((int) d + w1, i, w1, i);
			}
		}

	}

	/**
	 * 纵向扭曲
	 * 
	 * @param g
	 * @param w1
	 * @param h1
	 * @param color
	 */
	private void shearY(Graphics g, int w1, int h1, Color color) {
		int period = random.nextInt(40) + 10; // 50;
		boolean borderGap = true;
		int frames = 20;
		int phase = 7;
		for (int i = 0; i < w1; i++) {
			double d = (double) (period >> 1)
					* Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
			g.copyArea(i, 0, 1, h1, 0, (int) d);
			if (borderGap) {
				g.setColor(color);
				g.drawLine(i, (int) d, i, 0);
				g.drawLine(i, (int) d + h1, i, h1);
			}

		}
	}

	/**
	 * 测试main
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		File dir = new File("d:/verifies");
		// int w = 200, h = 80;
		for (int i = 0; i < 50; i++) {
			String verifyCode = CaptchaHelper.getInstance().getVerifyCode(4);
			File file = new File(dir, verifyCode + ".jpg");
			CaptchaHelper.getInstance().outputImage(verifyCode, file);
		}
	}
}
