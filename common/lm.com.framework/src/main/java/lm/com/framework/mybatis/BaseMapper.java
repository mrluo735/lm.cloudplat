package lm.com.framework.mybatis;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 基础Mapper
 * 
 * @author mrluo735
 *
 * @param <E>
 *            实体类型
 * @param <R>
 *            返回值类型
 * @param <P>
 *            主键类型
 */
@Mapper
public interface BaseMapper<E, R, P> {
	/**
	 * 新增信息
	 * 
	 * @param obj
	 * @return
	 */
	R insert(E obj);

	/**
	 * 修改信息
	 * 
	 * @param obj
	 * @return
	 */
	int update(E obj);

	/**
	 * 逻辑删除信息
	 * 
	 * @param id
	 * @return
	 */
	int rubbish(P id);

	/**
	 * 批量逻辑删除信息
	 * 
	 * @param idList
	 * @return
	 */
	int rubbishBatch(List<P> idList);

	/**
	 * 删除信息
	 * 
	 * @param id
	 * @return
	 */
	int delete(P id);

	/**
	 * 批量删除信息
	 * 
	 * @param id
	 * @return
	 */
	int deleteBatch(List<P> idList);

	/**
	 * 根据主键id查找信息
	 * 
	 * @param id
	 * @return
	 */
	E selectById(P id);

	/**
	 * 根据条件查找信息
	 * 
	 * @param whereMap
	 * @return
	 */
	List<E> selectList(Map<String, Object> whereMap);

	/**
	 * 分页查找信息
	 * 
	 * @param pagerMap
	 * @return
	 */
	List<E> selectPaged(Map<String, Object> pagerMap);

	// ↓↓↓↓↓↓↓↓↓↓ 扩展方法 ↓↓↓↓↓↓↓↓↓↓
	// ↑↑↑↑↑↑↑↑↑↑ 扩展方法 ↑↑↑↑↑↑↑↑↑↑
}
