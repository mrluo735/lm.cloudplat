package lm.com.framework.captcha;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.imageio.ImageIO;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

/**
 * 图形验证码
 * 
 * @author mrluo735
 *
 */
public class KaptchaHelper {
	private static final KaptchaHelper instance = new KaptchaHelper();
	private static DefaultKaptcha kaptcha = new DefaultKaptcha();

	/**
	 * 单例
	 * 
	 * @return
	 */
	public static KaptchaHelper getInstance() {
		kaptcha.setConfig(instance.initProperties());
		return instance;
	}

	// region 私有属性
	/**
	 * 是否有边框
	 * <p>
	 * true
	 * </p>
	 */
	private boolean border = true;
	/**
	 * 边框颜色 默认为Color.BLACK
	 */
	private Color borderColor = new Color(105, 179, 90);
	/**
	 * 边框粗细度 默认为1
	 */
	private int borderThickness = 1;
	/**
	 * 验证码字符集
	 */
	private String codes = "0123456789abcdefghjkmnpqrstuvwxyz";
	/**
	 * 验证码长度
	 */
	private int length = 4;
	/**
	 * 字体名称
	 * <p>
	 * 可多个
	 * </p>
	 */
	private String fontName = "宋体,楷体,微软雅黑";
	/**
	 * 字体大小
	 */
	private int fontSize = 35;
	/**
	 * 字体颜色
	 */
	private Color fontColor = new Color(30, 170, 37);
	/**
	 * 文本字符间距
	 */
	private int charSpace = 4;
	/**
	 * 噪点颜色
	 */
	private Color noiseColor = new Color(129, 132, 159);
	/**
	 * 背景渐变开始颜色
	 */
	private Color clearColorFrom = new Color(220, 213, 200);
	/**
	 * 背景渐变结束颜色
	 */
	private Color clearColorTo = new Color(220, 199, 223);
	/**
	 * 图片宽度
	 */
	private int width = 200;
	/**
	 * 图片高度
	 */
	private int height = 50;
	// endregion

	/**
	 * 获取验证码
	 * 
	 * @return
	 */
	public String getVerifyCode() {
		return kaptcha.createText();
	}

	/**
	 * 重载+1 生成随机验证码文件,并返回验证码值
	 * 
	 * @param outputFile
	 * @return
	 * @throws IOException
	 */
	public String outputVerifyImage(File outputFile) throws IOException {
		String code = this.getVerifyCode();
		this.outputImage(code, outputFile);
		return code;
	}

	/**
	 * 重载+2 生成指定验证码图像文件
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
			this.outputImage(code, fos);
			fos.close();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * 重载+3 输出图片流
	 * 
	 * @param code
	 * @param os
	 * @throws IOException
	 */
	public String outputImage(OutputStream os) throws IOException {
		String code = this.getVerifyCode();
		BufferedImage image = kaptcha.createImage(code);
		ImageIO.write(image, "jpg", os);
		return code;
	}

	/**
	 * 重载+4 输出图片流
	 * 
	 * @param code
	 * @param os
	 * @throws IOException
	 */
	public void outputImage(String code, OutputStream os) throws IOException {
		BufferedImage image = kaptcha.createImage(code);
		ImageIO.write(image, "jpg", os);
	}

	/**
	 * 初始化配置
	 * 
	 * @return
	 */
	private Config initProperties() {
		Properties properties = new Properties();
		// 验证码样式引擎(水纹:WaterRipple;鱼眼:FishEyeGimpy;阴影:ShadowGimpy) 默认为WaterRipple
		properties.setProperty(Constants.KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
		// 是否有边框 默认为true 我们可以自己设置yes，no
		properties.setProperty(Constants.KAPTCHA_BORDER, this.border == true ? "yes" : "no");
		// 边框颜色 默认为Color.BLACK
		properties.setProperty(Constants.KAPTCHA_BORDER_COLOR, String.format("%s,%s,%s", this.borderColor.getRed(),
				this.borderColor.getGreen(), this.borderColor.getBlue()));
		// 边框粗细度 默认为1
		properties.setProperty(Constants.KAPTCHA_BORDER_THICKNESS, String.valueOf(this.borderThickness));
		// 验证码文本字符内容范围 默认为abcde2345678gfynmnpwx
		properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_STRING, this.codes);
		// kaptcha.textproducer.char.length 验证码文本字符长度 默认为5
		properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, String.valueOf(this.length));
		// kaptcha.textproducer.font.names 验证码文本字体样式 默认为new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
		properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_NAMES, this.fontName);
		// 验证码文本字符大小 默认为40
		properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_SIZE, String.valueOf(this.fontSize));
		// 验证码文本字符颜色 默认为Color.BLACK
		properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, String.format("%s,%s,%s",
				this.fontColor.getRed(), this.fontColor.getGreen(), this.fontColor.getBlue()));
		// 验证码文本字符间距 默认为2
		properties.setProperty(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, String.valueOf(this.charSpace));
		// 验证码噪点生成对象 默认为DefaultNoise
		// properties.setProperty(Constants.KAPTCHA_NOISE_IMPL, "DefaultNoise");
		// 验证码噪点颜色 默认为Color.BLACK
		properties.setProperty(Constants.KAPTCHA_NOISE_COLOR, String.format("%s,%s,%s", this.noiseColor.getRed(),
				this.noiseColor.getGreen(), this.noiseColor.getBlue()));
		// 验证码文本字符渲染   默认为DefaultWordRenderer
		// properties.setProperty(Constants.KAPTCHA_WORDRENDERER_IMPL, "DefaultWordRenderer");
		// 验证码背景生成器   默认为DefaultBackground
		// properties.setProperty(Constants.KAPTCHA_BACKGROUND_IMPL, "DefaultBackground");
		// 验证码背景颜色渐进   默认为Color.LIGHT_GRAY
		properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_FROM, String.format("%s,%s,%s",
				this.clearColorFrom.getRed(), this.clearColorFrom.getGreen(), this.clearColorFrom.getBlue()));
		// 验证码背景颜色渐进   默认为Color.WHITE
		properties.setProperty(Constants.KAPTCHA_BACKGROUND_CLR_TO, String.format("%s,%s,%s",
				this.clearColorTo.getRed(), this.clearColorTo.getGreen(), this.clearColorTo.getBlue()));
		// 验证码图片宽度  默认为200
		properties.setProperty(Constants.KAPTCHA_IMAGE_WIDTH, String.valueOf(this.width));
		// 验证码图片高度  默认为50
		properties.setProperty(Constants.KAPTCHA_IMAGE_HEIGHT, String.valueOf(this.height));
		// Session键
		properties.setProperty(Constants.KAPTCHA_SESSION_CONFIG_KEY, Constants.KAPTCHA_SESSION_KEY);

		Config config = new Config(properties);
		return config;
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
			String verifyCode = KaptchaHelper.getInstance().getVerifyCode();
			File file = new File(dir, verifyCode + ".jpg");
			KaptchaHelper.getInstance().outputImage(verifyCode, file);
		}
	}
}
