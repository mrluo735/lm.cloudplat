package lm.com.framework.sqlmedium;

import java.util.HashMap;

/**
 * hibernate查询 ： Query
 * 
 * @author Administrator
 *
 */
public class HibernateQuery extends Query {
	private HashMap<String, Object> values = new HashMap<>();

	public HashMap<String, Object> getValues() {
		return values;
	}

	public void setValues(HashMap<String, Object> values) {
		this.values = values;
	}
}
