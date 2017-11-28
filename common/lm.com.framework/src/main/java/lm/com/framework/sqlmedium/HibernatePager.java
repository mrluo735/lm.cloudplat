package lm.com.framework.sqlmedium;

import java.util.HashMap;

/**
 * hibernate分页 : Pager
 * 
 * @author mrluo735
 *
 */
public class HibernatePager extends Pager {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7432209396918724584L;
	private HashMap<String, Object> values = new HashMap<>();

	/**
	 * 获取值
	 * 
	 * @return
	 */
	public HashMap<String, Object> getValues() {
		return values;
	}

	/**
	 * 设置值
	 * 
	 * @param values
	 */
	public void setValues(HashMap<String, Object> values) {
		this.values = values;
	}
}
