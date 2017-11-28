/**
 * @title SqlParserUtil.java
 * @description TODO
 * @package lm.com.framework
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月27日上午9:20:23
 * @version v1.0
 */
package lm.com.framework;

import java.io.StringReader;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * @author mrluo735
 *
 */
public class SqlParserUtil {
	public static String escapeColumn(String driver, String sql) throws JSQLParserException {
		CCJSqlParserManager spm = new CCJSqlParserManager();
		Statement statement = spm.parse(new StringReader(sql));
		if (statement instanceof Insert) {
			Insert insertStatement = (Insert) statement;
			List<Column> columns = insertStatement.getColumns();
			for (Column c : columns) {
				c.setColumnName(RMDBUtil.escapeColumn(c.getColumnName(), driver));
			}
			Select select = insertStatement.getSelect();
			if (null != select) {
				PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
				List<SelectItem> selectItems = plainSelect.getSelectItems();
				selectItems.get(0).toString();
			}
		}
		return "";
	}
}
