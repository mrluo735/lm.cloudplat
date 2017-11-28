/**
 * @title AsciiArtUtil.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月19日下午4:10:41
 * @version v1.0.0
 */
package lm.com.framework;

import java.io.PrintStream;

import org.slf4j.Logger;

import com.github.lalyos.jfiglet.FigletFont;

/**
 * @ClassName: AsciiArtUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月19日 下午4:10:41
 * 
 */
public final class AsciiArtUtil {
	private static final String ANSI_RESET = "\u001B[0m";
	private static final String ANSI_CYAN = "\u001B[36m";

	private AsciiArtUtil() {
	}

	/**
	 * Print ascii art.
	 *
	 * @param out
	 *            the out
	 * @param asciiArt
	 *            the ascii art
	 */
	public static void printAsciiArt(final PrintStream out, final String asciiArt) {
		printAsciiArt(out, asciiArt, null);
	}

	/**
	 * Print ascii art.
	 *
	 * @param out
	 *            the out
	 * @param asciiArt
	 *            the ascii art
	 * @param additional
	 *            the additional
	 */
	public static void printAsciiArt(final Logger out, final String asciiArt, final String additional) {
		try {
			out.warn(ANSI_CYAN);
			out.warn("\n\n".concat(FigletFont.convertOneLine(asciiArt)).concat(additional));
			out.warn(ANSI_RESET);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Print ascii art.
	 *
	 * @param out
	 *            the out
	 * @param asciiArt
	 *            the ascii art
	 * @param additional
	 *            the additional
	 */
	public static void printAsciiArt(final PrintStream out, final String asciiArt, final String additional) {
		try {
			out.println();
			out.println(ANSI_CYAN);
			out.println(FigletFont.convertOneLine(asciiArt));
			if (!StringUtil.isNullOrWhiteSpace(additional)) {
				out.println(additional);
			}
			out.println(ANSI_RESET);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
