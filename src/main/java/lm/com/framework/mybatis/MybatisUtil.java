/**
 * @title MybatisUtil.java
 * @description TODO
 * @package lm.com.framework.mybatis
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年11月23日下午6:06:37
 * @version v1.0
 */
package lm.com.framework.mybatis;

import java.util.Map;

import org.apache.ibatis.parsing.XNode;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;

/**
 * mybatis工具类
 * 
 * @author mrluo735
 *
 */
public class MybatisUtil {
	/**
	 * 获取mapper.xml中定义的TableName值
	 * 
	 * @param sqlSessionFactory
	 * @param mapperClassName
	 * @return
	 */
	public static String getTableName(final DefaultSqlSessionFactory sqlSessionFactory, String mapperClassName) {
		if(null == mapperClassName)
			return "";
		Map<String, XNode> sqlFragments = sqlSessionFactory.getConfiguration().getSqlFragments();
		if (null == sqlFragments)
			return "";
		XNode xNode = sqlFragments.get(mapperClassName.concat(".TableName"));
		if (null == xNode)
			return "";
		return null == xNode.getStringBody() ? "" : xNode.getStringBody().trim();
	}
}
