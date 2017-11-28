/**
 * @title PdfUtil.java
 * @description TODO(这里用一句话描述这个文件的作用)
 * @package lm.com.framework.itext
 * @author mrluo735
 * @since JDK1.7
 * @date 2017年5月18日上午8:56:43
 * @version v1.0.0
 */
package lm.com.framework.itext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lm.com.framework.StringUtil;

/**
 * @ClassName: PdfUtil
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author mrluo735
 * @date 2017年5月18日 上午8:56:43
 * 
 */
public class PdfUtil {
	/**
	 * 写入pdf
	 * 
	 * @param jsonArray
	 * @param ttfPath
	 * @return
	 */
	public static byte[] write(JSONArray jsonArray, String ttfPath) {
		Rectangle rectPageSize = new Rectangle(PageSize.A4); // A4纸张
		Document document = new Document(rectPageSize, 40, 40, 40, 40); // 上、下、左、右间距
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		PdfWriter pdfWriter = null;
		try {
			pdfWriter = PdfWriter.getInstance(document, outputStream);
			if (!document.isOpen())
				document.open();
			// 中文简体
			BaseFont baseFont = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			if (!StringUtil.isNullOrWhiteSpace(ttfPath))
				baseFont = BaseFont.createFont(ttfPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
			// 自定义字体属性
			Font font = new Font(baseFont, 12, Font.NORMAL);

			PdfPTable pdfTable = new PdfPTable(jsonArray.toList().size());
			List<String> headerList = new ArrayList<String>();
			Iterator<Object> iterator = jsonArray.iterator();
			while (iterator.hasNext()) {
				JSONObject item = (JSONObject) iterator.next();

				if (headerList.size() < 1) {
					Iterator<String> headerIterator = item.keys();
					while (headerIterator.hasNext()) {
						String text = headerIterator.next();
						headerList.add(text);
						pdfTable.addCell(new Phrase(text, font));
					}
				}
				for (String header : headerList) {
					Object value = item.get(header);
					pdfTable.addCell(new Phrase(null == value ? "" : value.toString(), font));
				}
			}
			document.add(pdfTable);
			document.close();
			outputStream.flush();
			return outputStream.toByteArray();
		} catch (DocumentException | IOException e) {
			return new byte[0];
		} finally {
			pdfWriter.close();
			try {
				outputStream.close();
			} catch (IOException e) {
			}
		}
	}
}
