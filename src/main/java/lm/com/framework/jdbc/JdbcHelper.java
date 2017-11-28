/**
 * @title JdbcHelper.java
 * @description TODO
 * @package lm.com.framework.jdbc
 * @author mrluo735
 * @since JDK1.7
 * @date 2016年12月16日下午3:52:06
 * @version v1.0
 */
package lm.com.framework.jdbc;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sql.DataSource;

import org.springframework.beans.factory.InitializingBean;

import lm.com.framework.ResultSetUtil;
import lm.com.framework.StringUtil;
import lm.com.framework.map.KeyValuePair;

/**
 * jdbc帮助类
 * 
 * @author mrluo735
 * @version jdk 1.6
 */
public class JdbcHelper implements InitializingBean {
	private DataSource dataSource = null;

	/**
	 * Set the JDBC DataSource to obtain connections from.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * 构造函数
	 * 
	 * @param dataSource
	 * @throws Exception
	 */
	public JdbcHelper(DataSource dataSource) {
		this.setDataSource(dataSource);
		this.afterPropertiesSet();
	}

	/**
	 * 获取连接
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		Connection connection = this.dataSource.getConnection();
		return connection;
	}

	/**
	 * 关闭连接
	 * 
	 * @param connection
	 * @throws SQLException
	 */
	public void closeConnection(Connection connection) throws SQLException {
		if (null != connection && !connection.isClosed())
			connection.close();
	}

	/**
	 * 获取驱动名称
	 * 
	 * @return
	 * @throws SQLException
	 */
	public String getDriverName() throws SQLException {
		Connection connection = null;
		try {
			connection = this.getConnection();
			DatabaseMetaData databaseMetaData = connection.getMetaData();
			String driverName = databaseMetaData.getDriverName();
			return driverName;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			this.closeConnection(connection);
		}
	}

	// region 执行executeUpdate，不返回结果集
	/**
	 * 重载+1 执行executeUpdate，不返回结果集
	 * 
	 * @param sqlText
	 * @return 受影响的行数
	 * @throws SQLException
	 */
	public int executeNonQuery(String sqlText) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText);
			int rowCount = preparedStatement.executeUpdate();
			return rowCount;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			preparedStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+2 执行executeUpdate，不返回结果集
	 * 
	 * @param sqlText
	 * @param parameters
	 * @return 受影响的行数
	 * @throws SQLException
	 */
	public int executeNonQuery(String sqlText, Object... parameters) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, parameters);
			int rowCount = preparedStatement.executeUpdate();
			return rowCount;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			preparedStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+3 执行executeUpdate，不返回结果集
	 * 
	 * @param sqlText
	 * @param 是否有自增值
	 * @return 受影响的行数, 自增值
	 * @throws SQLException
	 */
	public KeyValuePair<Integer, Object> executeNonQuery(String sqlText, boolean identityKey) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Object key = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, identityKey);
			int rowCount = preparedStatement.executeUpdate();
			if (identityKey) {
				ResultSet rs = preparedStatement.getGeneratedKeys();
				while (rs.next()) {
					key = rs.getObject(1);
				}
				rs.close();
			}
			return new KeyValuePair<Integer, Object>(rowCount, key);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			preparedStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+4 执行executeUpdate，不返回结果集
	 * 
	 * @param sqlText
	 * @param 是否有自增值
	 * @param parameters
	 * @return 受影响的行数, 自增值
	 * @throws SQLException
	 */
	public KeyValuePair<Integer, Object> executeNonQuery(String sqlText, boolean identityKey, Object... parameters)
			throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Object key = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, identityKey, parameters);
			int rowCount = preparedStatement.executeUpdate();
			if (identityKey) {
				ResultSet rs = preparedStatement.getGeneratedKeys();
				while (rs.next()) {
					key = rs.getObject(1);
				}
				rs.close();
			}
			return new KeyValuePair<>(rowCount, key);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			preparedStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 批量执行
	 * 
	 * @param sqlText
	 * @param parameters
	 * @param batchSize
	 * @return
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	public int[] executeBatch(String sqlText, List<Object[]> parameters, Integer batchSize)
			throws NumberFormatException, SQLException {
		List<Integer> list = new ArrayList<Integer>();
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText);
			int i = 0;
			for (Object[] objects : parameters) {
				this.setParameter(preparedStatement, objects);
				preparedStatement.addBatch();

				if (batchSize > 0 && i % batchSize == 0) {
					int[] rowCounts = preparedStatement.executeBatch();
					for (int item : rowCounts)
						list.add(item);
				}
				i++;
			}
			int[] rowCounts = preparedStatement.executeBatch();
			for (int item : rowCounts)
				list.add(item);

			return rowCounts;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			preparedStatement.close();
			this.closeConnection(connection);
		}
	}
	// endregion

	// region 返回结果集
	/**
	 * 执行executeQuery, 返回结果集
	 * <p>
	 * 未关闭连接和结果集，使用完后请自行关闭
	 * </p>
	 * 
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sqlText) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText);
			preparedStatement.closeOnCompletion();
			ResultSet rs = preparedStatement.executeQuery();
			return rs;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	/**
	 * 执行executeQuery, 返回结果集
	 * <p>
	 * 未关闭连接和结果集，使用完后请自行关闭
	 * </p>
	 * 
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(String sqlText, Object... parameters) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, parameters);
			preparedStatement.closeOnCompletion();
			ResultSet rs = preparedStatement.executeQuery();
			return rs;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	/**
	 * 执行executeQuery, 返回单个结果
	 * 
	 * @param clazz
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("all")
	public <T extends Object> T executeQueryScalar(Class<T> clazz, String sqlText)
			throws SQLException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText);
			rs = preparedStatement.executeQuery();
			T bean = ResultSetUtil.toBean(rs, clazz);
			return bean;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 执行executeQuery, 返回单个结果
	 * 
	 * @param clazz
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("all")
	public <T extends Object> T executeQueryScalar(Class<T> clazz, String sqlText, Object... parameters)
			throws SQLException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, parameters);
			rs = preparedStatement.executeQuery();
			T bean = ResultSetUtil.toBean(rs, clazz);
			return bean;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 执行executeQuery, 返回结果集
	 * 
	 * @param clazz
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("all")
	public <T extends Object> List<T> executeQuery(Class<T> clazz, String sqlText)
			throws SQLException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText);
			rs = preparedStatement.executeQuery();
			List<T> list = ResultSetUtil.toBeans(rs, clazz);
			return list;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 执行executeQuery, 返回结果集
	 * 
	 * @param clazz
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("all")
	public <T extends Object> List<T> executeQuery(Class<T> clazz, String sqlText, Object... parameters)
			throws SQLException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, parameters);
			rs = preparedStatement.executeQuery();
			List<T> list = ResultSetUtil.toBeans(rs, clazz);
			return list;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 执行executeQuery, 返回第一条记录的第一列的值
	 * 
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	public Object executeScalar(String sqlText) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Object key = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				key = rs.getObject(1);
				break;
			}
			return key;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 执行executeQuery, 返回第一条记录的第一列的值
	 * 
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public Object executeScalar(String sqlText, Object... parameters) throws SQLException {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		Object key = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, parameters);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				key = rs.getObject(1);
				break;
			}
			return key;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 重载+1 执行executeQuery, 返回Map<K, V>
	 * 
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> executeMapScalar(String sqlText) throws SQLException {
		Map<String, Object> map = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				map = new HashMap<>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String key = rsmd.getColumnLabel(i);
					if (StringUtil.isNullOrWhiteSpace(key))
						key = rsmd.getColumnName(i);
					map.put(key, rs.getObject(i));
				}
				break;
			}
			return map;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 重载+2 执行executeQuery, 返回Map<K, V>
	 * 
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> executeMapScalar(String sqlText, Object... parameters) throws SQLException {
		Map<String, Object> map = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, parameters);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				map = new HashMap<>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String key = rsmd.getColumnLabel(i);
					if (StringUtil.isNullOrWhiteSpace(key))
						key = rsmd.getColumnName(i);
					map.put(key, rs.getObject(i));
				}
				break;
			}
			return map;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 重载+1 执行executeQuery, 返回List<Map<K, V>>
	 * 
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> executeMap(String sqlText) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String key = rsmd.getColumnLabel(i);
					if (StringUtil.isNullOrWhiteSpace(key))
						key = rsmd.getColumnName(i);
					map.put(key, rs.getObject(i));
				}
				list.add(map);
			}
			return list;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}

	/**
	 * 重载+2 执行executeQuery, 返回List<Map<K, V>>
	 * 
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> executeMap(String sqlText, Object... parameters) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			preparedStatement = this.getPreparedStatement(connection, sqlText, parameters);
			rs = preparedStatement.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String key = rsmd.getColumnLabel(i);
					if (StringUtil.isNullOrWhiteSpace(key))
						key = rsmd.getColumnName(i);
					map.put(key, rs.getObject(i));
				}
				list.add(map);
			}
			return list;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			preparedStatement.close();
			connection.close();
		}
	}
	// endregion

	/**
	 * 重载+1 执行存储过程
	 * 
	 * @param sqlText
	 * @return 受影响行数
	 * @throws SQLException
	 */
	public int callableNonQuery(String sqlText) throws SQLException {
		Connection connection = null;
		CallableStatement callableStatement = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText);
			int rowCount = callableStatement.executeUpdate();
			return rowCount;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			callableStatement.close();
			connection.close();
		}
	}

	/**
	 * 重载+2 执行存储过程
	 * 
	 * @param sqlText
	 * @param parameters
	 *            参数, Integer为java.sql.Types
	 * @return 受影响行数, 输出参数
	 * @throws SQLException
	 */
	public KeyValuePair<Integer, List<Object>> callableNonQuery(String sqlText, Map<Object, Integer> parameters)
			throws SQLException {
		int rowCount = 0;
		List<Object> outList = new ArrayList<>();
		// 没有参数视为无参存储过程
		if (null == parameters || parameters.size() < 1) {
			rowCount = this.callableNonQuery(sqlText);
			return new KeyValuePair<Integer, List<Object>>(rowCount, outList);
		}

		Connection connection = null;
		CallableStatement callableStatement = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText, parameters);
			rowCount = callableStatement.executeUpdate();
			outList = this.getParameter(callableStatement, parameters);
			return new KeyValuePair<Integer, List<Object>>(rowCount, outList);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			callableStatement.close();
			connection.close();
		}
	}

	/**
	 * 重载+1 执行存储过程
	 * <p>
	 * 未关闭连接和结果集，请自行关闭
	 * </p>
	 * 
	 * @param sqlText
	 * @return 结果集
	 * @throws SQLException
	 */
	public ResultSet callableQuery(String sqlText) throws SQLException {
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText);
			callableStatement.closeOnCompletion();
			rs = callableStatement.executeQuery();
			return rs;
		} catch (SQLException ex) {
			throw ex;
		}
	}

	/**
	 * 重载+2 执行存储过程
	 * <p>
	 * 未关闭连接和结果集，请自行关闭
	 * </p>
	 * 
	 * @param sqlText
	 * @return 结果集
	 * @throws SQLException
	 */
	public KeyValuePair<ResultSet, List<Object>> callableQuery(String sqlText, Map<Object, Integer> parameters)
			throws SQLException {
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		List<Object> outList = new ArrayList<>();
		// 没有参数视为无参存储过程
		if (null == parameters || parameters.size() < 1) {
			rs = this.callableQuery(sqlText);
			return new KeyValuePair<ResultSet, List<Object>>(rs, outList);
		}

		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText, parameters);
			callableStatement.closeOnCompletion();
			rs = callableStatement.executeQuery();
			outList = this.getParameter(callableStatement, parameters);
			return new KeyValuePair<ResultSet, List<Object>>(rs, outList);
		} catch (SQLException ex) {
			throw ex;
		}
	}

	/**
	 * 重载+3 执行存储过程
	 * 
	 * @param sqlText
	 * @return 结果集
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T extends Object> List<T> callableQuery(Class<T> clazz, String sqlText)
			throws SQLException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText);
			rs = callableStatement.executeQuery();
			List<T> list = ResultSetUtil.toBeans(rs, clazz);
			return list;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			callableStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+4 执行存储过程
	 * 
	 * @param clazz
	 * @param sqlText
	 * @param parameters
	 * @return 结果集
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T extends Object> KeyValuePair<List<T>, List<Object>> callableQuery(Class<T> clazz, String sqlText,
			Map<Object, Integer> parameters) throws SQLException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText, parameters);
			rs = callableStatement.executeQuery();
			List<T> list = ResultSetUtil.toBeans(rs, clazz);
			List<Object> outList = this.getParameter(callableStatement, parameters);
			return new KeyValuePair<List<T>, List<Object>>(list, outList);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			callableStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+1 执行存储过程
	 * 
	 * @param sqlText
	 * @return 结果
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public <T extends Object> T callableQueryScalar(Class<T> clazz, String sqlText)
			throws SQLException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText);
			rs = callableStatement.executeQuery();
			T bean = ResultSetUtil.toBean(rs, clazz);
			return bean;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			callableStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+2 执行存储过程
	 * 
	 * @param clazz
	 * @param sqlText
	 * @param parameters
	 * @return 结果
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@SuppressWarnings("all")
	public <T extends Object> KeyValuePair<T, List<Object>> callableQueryScalar(Class<T> clazz, String sqlText,
			Map<Object, Integer> parameters) throws SQLException, InstantiationException, IllegalAccessException {
		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText, parameters);
			rs = callableStatement.executeQuery();
			T bean = ResultSetUtil.toBean(rs, clazz);
			List<Object> outList = this.getParameter(callableStatement, parameters);
			return new KeyValuePair<T, List<Object>>(bean, outList);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			callableStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+1 执行executeQuery, 返回Map<K, V>
	 * 
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> callableMapScalar(String sqlText) throws SQLException {
		Map<String, Object> map = null;

		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText);
			rs = callableStatement.executeQuery();
			while (rs.next()) {
				map = new HashMap<>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String key = rsmd.getColumnLabel(i);
					if (StringUtil.isNullOrWhiteSpace(key))
						key = rsmd.getColumnName(i);
					map.put(key, rs.getObject(i));
				}
				break;
			}
			return map;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			callableStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+2 执行executeQuery, 返回KeyValuePair<Map<K, V>>
	 * 
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public KeyValuePair<Map<String, Object>, List<Object>> callableMapScalar(String sqlText,
			Map<Object, Integer> parameters) throws SQLException {
		Map<String, Object> map = null;

		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText, parameters);
			rs = callableStatement.executeQuery();
			while (rs.next()) {
				map = new HashMap<>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String key = rsmd.getColumnLabel(i);
					if (StringUtil.isNullOrWhiteSpace(key))
						key = rsmd.getColumnName(i);
					map.put(key, rs.getObject(i));
				}
				break;
			}
			List<Object> outList = this.getParameter(callableStatement, parameters);
			return new KeyValuePair<Map<String, Object>, List<Object>>(map, outList);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			callableStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+1 执行executeQuery, 返回List<Map<String, Object>>
	 * 
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> callableMap(String sqlText) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();

		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText);
			rs = callableStatement.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String key = rsmd.getColumnLabel(i);
					if (StringUtil.isNullOrWhiteSpace(key))
						key = rsmd.getColumnName(i);
					map.put(key, rs.getObject(i));
				}
				list.add(map);
			}
			return list;
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			callableStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 重载+2 执行executeQuery, 返回List<Map<String, Object>>
	 * 
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	public KeyValuePair<List<Map<String, Object>>, List<Object>> callableMap(String sqlText,
			Map<Object, Integer> parameters) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();

		Connection connection = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try {
			connection = this.getConnection();
			callableStatement = this.getCallableStatement(connection, sqlText, parameters);
			rs = callableStatement.executeQuery();
			while (rs.next()) {
				Map<String, Object> map = new HashMap<>();
				ResultSetMetaData rsmd = rs.getMetaData();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					String key = rsmd.getColumnLabel(i);
					if (StringUtil.isNullOrWhiteSpace(key))
						key = rsmd.getColumnName(i);
					map.put(key, rs.getObject(i));
				}
				list.add(map);
			}
			List<Object> outList = this.getParameter(callableStatement, parameters);
			return new KeyValuePair<List<Map<String, Object>>, List<Object>>(list, outList);
		} catch (SQLException ex) {
			throw ex;
		} finally {
			rs.close();
			callableStatement.close();
			this.closeConnection(connection);
		}
	}

	/**
	 * 开始事务
	 * 
	 * @throws SQLException
	 */
	public void beginTransaction(Connection connection) throws SQLException {
		connection.setAutoCommit(false);
	}

	/**
	 * 提交事务
	 * 
	 * @throws SQLException
	 */
	public void commitTransaction(Connection connection) throws SQLException {
		connection.commit();
	}

	/**
	 * 回滚事务
	 * 
	 * @throws SQLException
	 */
	public void rollbackTransaction(Connection connection) throws SQLException {
		connection.rollback();
	}

	/**
	 * 
	 */
	@Override
	public void afterPropertiesSet() {
		this.init();
	}

	/**
	 * 初始化
	 * 
	 * @throws Exception
	 */
	private void init() {
		if (null == this.dataSource)
			throw new IllegalArgumentException("数据源没有设置！");
	}

	/**
	 * 重载+1 获取PreparedStatement
	 * 
	 * @param connection
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(Connection connection, String sqlText) throws SQLException {
		return connection.prepareStatement(sqlText);
	}

	/**
	 * 重载+2 获取PreparedStatement
	 * 
	 * @param connection
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(Connection connection, String sqlText, Object... parameters)
			throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement(sqlText);
		this.setParameter(preparedStatement, parameters);
		return preparedStatement;
	}

	/**
	 * 重载+3 获取PreparedStatement
	 * 
	 * @param connection
	 * @param sqlText
	 * @param identityKey
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(Connection connection, String sqlText, boolean identityKey)
			throws SQLException {
		if (!identityKey)
			return connection.prepareStatement(sqlText);
		else {
			int generatedKeys = Statement.RETURN_GENERATED_KEYS;
			return connection.prepareStatement(sqlText, generatedKeys);
		}
	}

	/**
	 * 重载+4 获取PreparedStatement
	 * 
	 * @param connection
	 * @param sqlText
	 * @param identityKey
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPreparedStatement(Connection connection, String sqlText, boolean identityKey,
			Object... parameters) throws SQLException {
		if (!identityKey) {
			PreparedStatement preparedStatement = connection.prepareStatement(sqlText);
			this.setParameter(preparedStatement, parameters);
			return preparedStatement;
		} else {
			int generatedKeys = Statement.RETURN_GENERATED_KEYS;
			PreparedStatement preparedStatement = connection.prepareStatement(sqlText, generatedKeys);
			this.setParameter(preparedStatement, parameters);
			return preparedStatement;
		}
	}

	/**
	 * 重载+1 获取CallableStatement
	 * 
	 * @param connection
	 * @param sqlText
	 * @return
	 * @throws SQLException
	 */
	private CallableStatement getCallableStatement(Connection connection, String sqlText) throws SQLException {
		return connection.prepareCall(sqlText);
	}

	/**
	 * 重载+2 获取CallableStatement
	 * 
	 * @param connection
	 * @param sqlText
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	private CallableStatement getCallableStatement(Connection connection, String sqlText,
			Map<Object, Integer> parameters) throws SQLException {
		CallableStatement callableStatement = connection.prepareCall(sqlText);
		this.setParameter(callableStatement, parameters);
		return callableStatement;
	}

	// region 设置参数
	/**
	 * 重载+1 设置参数
	 * 
	 * @param preparedStatement
	 * @param parameters
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	private void setParameter(PreparedStatement preparedStatement, Object... parameters)
			throws NumberFormatException, SQLException {
		if (null == parameters || parameters.length < 1)
			return;

		int i = 1;
		for (Object parameter : parameters) {
			this.setParameter(preparedStatement, i, parameter);
			i++;
		}
	}

	/**
	 * 重载+2 设置参数
	 * 
	 * @param preparedStatement
	 * @param parameters
	 * @throws SQLException
	 * @throws NumberFormatException
	 */
	private void setParameter(CallableStatement callableStatement, Map<Object, Integer> parameters)
			throws NumberFormatException, SQLException {
		if (null == parameters || parameters.size() < 1)
			return;

		int i = 1;
		for (Entry<Object, Integer> entry : parameters.entrySet()) {
			if (null == entry.getValue())
				this.setParameter(callableStatement, i, entry.getKey());
			else
				callableStatement.registerOutParameter(i, entry.getValue());

			i++;
		}
	}

	/**
	 * 重载+3 设置参数
	 * 
	 * @param statement
	 * @param i
	 * @param parameter
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	private void setParameter(Statement statement, int i, Object parameter) throws NumberFormatException, SQLException {
		PreparedStatement preparedStatement = null;
		CallableStatement callableStatement = null;
		if (statement instanceof CallableStatement)
			callableStatement = (CallableStatement) statement;
		else if (statement instanceof PreparedStatement)
			preparedStatement = (PreparedStatement) statement;

		if (null == parameter) {
			if (null != preparedStatement)
				preparedStatement.setObject(i, null);
			if (null != callableStatement)
				callableStatement.setObject(i, null);
		} else if (parameter instanceof String) {
			if (null != preparedStatement)
				preparedStatement.setString(i, parameter.toString());
			if (null != callableStatement)
				callableStatement.setString(i, parameter.toString());
		} else if (parameter instanceof Boolean) {
			if (null != preparedStatement)
				preparedStatement.setBoolean(i, Boolean.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setBoolean(i, Boolean.valueOf(parameter.toString()));
		}
		// 数字型
		else if (parameter instanceof Byte) {
			if (null != preparedStatement)
				preparedStatement.setByte(i, Byte.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setByte(i, Byte.valueOf(parameter.toString()));
		} else if (parameter instanceof Short) {
			if (null != preparedStatement)
				preparedStatement.setShort(i, Short.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setShort(i, Short.valueOf(parameter.toString()));
		} else if (parameter instanceof Integer) {
			if (null != preparedStatement)
				preparedStatement.setInt(i, Integer.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setInt(i, Integer.valueOf(parameter.toString()));
		} else if (parameter instanceof Long) {
			if (null != preparedStatement)
				preparedStatement.setLong(i, Long.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setLong(i, Long.valueOf(parameter.toString()));
		} else if (parameter instanceof BigDecimal) {
			if (null != preparedStatement)
				preparedStatement.setBigDecimal(i, BigDecimal.valueOf(Double.valueOf(parameter.toString())));
			if (null != callableStatement)
				callableStatement.setBigDecimal(i, BigDecimal.valueOf(Double.valueOf(parameter.toString())));
		} else if (parameter instanceof Float) {
			if (null != preparedStatement)
				preparedStatement.setFloat(i, Float.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setFloat(i, Float.valueOf(parameter.toString()));
		} else if (parameter instanceof Double) {
			if (null != preparedStatement)
				preparedStatement.setDouble(i, Double.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setDouble(i, Double.valueOf(parameter.toString()));
		}
		// 日期时间型
		else if (parameter instanceof Time) {
			if (null != preparedStatement)
				preparedStatement.setTime(i, Time.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setTime(i, Time.valueOf(parameter.toString()));
		} else if (parameter instanceof Date) {
			if (null != preparedStatement)
				preparedStatement.setDate(i, Date.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setDate(i, Date.valueOf(parameter.toString()));
		} else if (parameter instanceof Timestamp) {
			if (null != preparedStatement)
				preparedStatement.setTimestamp(i, Timestamp.valueOf(parameter.toString()));
			if (null != callableStatement)
				callableStatement.setTimestamp(i, Timestamp.valueOf(parameter.toString()));
		}
		// 二进制类型
		else if (parameter instanceof byte[]) {
			if (null != preparedStatement)
				preparedStatement.setBytes(i, (byte[]) parameter);
			if (null != callableStatement)
				callableStatement.setBytes(i, (byte[]) parameter);
		} else if (parameter instanceof Blob) {
			if (null != preparedStatement)
				preparedStatement.setBlob(i, (Blob) parameter);
			if (null != callableStatement)
				callableStatement.setBlob(i, (Blob) parameter);
		} else if (parameter instanceof Clob) {
			if (null != preparedStatement)
				preparedStatement.setClob(i, (Clob) parameter);
			if (null != callableStatement)
				callableStatement.setClob(i, (Clob) parameter);
		} else if (parameter instanceof NClob) {
			if (null != preparedStatement)
				preparedStatement.setNClob(i, (NClob) parameter);
			if (null != callableStatement)
				callableStatement.setNClob(i, (NClob) parameter);
		} else if (parameter instanceof Array) {
			if (null != preparedStatement)
				preparedStatement.setArray(i, (Array) parameter);
			if (null != callableStatement)
				callableStatement.setArray(i, (Array) parameter);
		} else if (parameter instanceof InputStream) {
			if (null != preparedStatement)
				preparedStatement.setBinaryStream(i, (InputStream) parameter);
			if (null != callableStatement)
				callableStatement.setBinaryStream(i, (InputStream) parameter);
		} else if (parameter instanceof Reader) {
			if (null != preparedStatement)
				preparedStatement.setCharacterStream(i, (Reader) parameter);
			if (null != callableStatement)
				callableStatement.setCharacterStream(i, (Reader) parameter);
		} else if (parameter instanceof URL) {
			if (null != preparedStatement)
				preparedStatement.setURL(i, (URL) parameter);
			if (null != callableStatement)
				callableStatement.setURL(i, (URL) parameter);
		} else {
			if (null != preparedStatement)
				preparedStatement.setObject(i, parameter);
			if (null != callableStatement)
				callableStatement.setObject(i, parameter);
		}
		// else if (parameter instanceof Date)
		// preparedStatement.setDate(parameterIndex, x, cal);
	}
	// endregion

	// region 获取参数
	/**
	 * 获取参数
	 * 
	 * @param callableStatement
	 * @param parameters
	 * @return
	 * @throws SQLException
	 */
	private List<Object> getParameter(CallableStatement callableStatement, Map<Object, Integer> parameters)
			throws SQLException {
		List<Object> outObjects = new ArrayList<>();

		Object value = null;
		int i = 0;
		for (Entry<Object, Integer> entry : parameters.entrySet()) {
			i++;
			if (null == entry.getValue())
				continue;
			switch (entry.getValue()) {
			case Types.BOOLEAN:
			case Types.BIT:
				value = callableStatement.getBoolean(i);
				break;
			case Types.TINYINT:
				value = callableStatement.getByte(i);
				break;
			case Types.SMALLINT:
				value = callableStatement.getShort(i);
				break;
			case Types.INTEGER:
				value = callableStatement.getInt(i);
				break;
			case Types.BIGINT:
				value = callableStatement.getLong(i);
				break;
			case Types.DECIMAL:
				value = callableStatement.getBigDecimal(i);
				break;
			case Types.FLOAT:
			case Types.REAL:
				value = callableStatement.getFloat(i);
				break;
			case Types.DOUBLE:
			case Types.NUMERIC:
				value = callableStatement.getDouble(i);
				break;
			case Types.TIME:
				value = callableStatement.getTime(i);
				break;
			case Types.TIMESTAMP:
				value = callableStatement.getTimestamp(i);
				break;
			case Types.DATE:
				value = callableStatement.getDate(i);
				break;
			case Types.BINARY:
			case Types.VARBINARY:
			case Types.LONGVARBINARY:
				value = callableStatement.getBytes(i);
				break;
			case Types.BLOB:
				value = callableStatement.getBlob(i);
				break;
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				value = callableStatement.getString(i);
				break;
			case Types.NCHAR:
			case Types.NVARCHAR:
			case Types.LONGNVARCHAR:
				value = callableStatement.getNString(i);
				break;
			case Types.CLOB:
				value = callableStatement.getClob(i);
				break;
			case Types.NCLOB:
				value = callableStatement.getNClob(i);
				break;
			case Types.ARRAY:
				value = callableStatement.getArray(i);
				break;
			case Types.REF:
				value = callableStatement.getRef(i);
				break;
			case Types.ROWID:
				value = callableStatement.getRowId(i);
				break;
			case Types.SQLXML:
				value = callableStatement.getSQLXML(i);
				break;
			case Types.NULL:
			case Types.DATALINK:
			case Types.DISTINCT:
			case Types.JAVA_OBJECT:
			case Types.OTHER:
			case Types.STRUCT:
				value = callableStatement.getObject(i);
				break;
			default:
				value = callableStatement.getObject(i);
				break;
			}
			outObjects.add(value);
		}
		return outObjects;
	}
	// endregion
}
