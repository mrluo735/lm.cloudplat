package lm.com.framework.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultLoadEventListener;
import org.hibernate.event.spi.LoadEvent;
import org.hibernate.event.spi.LoadEventListener;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostDeleteEventListener;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.hibernate.event.spi.PreUpdateEvent;
import org.hibernate.event.spi.PreUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.hibernate.event.spi.SaveOrUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lm.com.framework.DateTimeUtil;

/**
 * hibernate日志监听器
 * 
 * @author mrluo735
 *
 */
@SuppressWarnings("all")
public class HibernateLogEventListener extends DefaultLoadEventListener
		implements PreInsertEventListener, PostInsertEventListener, PreUpdateEventListener, PostUpdateEventListener,
		PreDeleteEventListener, PostDeleteEventListener, SaveOrUpdateEventListener {
	private final Logger logger = LoggerFactory.getLogger(HibernateLogEventListener.class);
	private Long startOn = 0L, endOn = 0L;

	@Override
	public void onLoad(LoadEvent event, LoadEventListener.LoadType loadType) throws HibernateException {
		logger.info("加载");
		super.onLoad(event, loadType);
	}

	@Override
	public boolean onPreInsert(PreInsertEvent event) {
		startOn = System.currentTimeMillis();
		logger.info("新增开始前：{}", DateTimeUtil.getNow("yyyy-MM-dd HH:mm:ss"));
		return false;
	}

	@Override
	public void onPostInsert(PostInsertEvent event) {
		endOn = System.currentTimeMillis();
		for (int i = 0; i < event.getState().length; i++) {
			// 新增的属性名
			String propertyName = event.getPersister().getPropertyNames()[i];
			// 新增的值
			if (null != event.getState() && event.getState().length >= i) {
				Object value = event.getState()[i];
			}
		}
		Long minus = endOn - startOn;
		logger.info(String.format("新增结束，实体类[%s]，耗时：%sms", event.getEntity().getClass().getName(), minus.toString()));
	}

	@Override
	public boolean onPreUpdate(PreUpdateEvent event) {
		logger.info("修改开始前：" + DateTimeUtil.getNow("yyyy-MM-dd HH:mm:ss"));
		return false;
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
		for (int i = 0; i < event.getState().length; i++) {
			// 修改的属性名
			String propertyName = event.getPersister().getPropertyNames()[i];
			// 修改前的值
			if (null != event.getOldState() && event.getOldState().length >= i) {
				Object oldValue = event.getOldState()[i];
			}
			// 修改后的新值
			if (null != event.getState() && event.getState().length >= i) {
				Object newValue = event.getState()[i];
			}
		}
		logger.info(event.getEntity().getClass().getName() + ":修改结束");
	}

	@Override
	public boolean onPreDelete(PreDeleteEvent event) {
		logger.info("删除开始前：" + DateTimeUtil.getNow("yyyy-MM-dd HH:mm:ss"));
		return false;
	}

	@Override
	public void onPostDelete(PostDeleteEvent event) {
		for (int i = 0; i < event.getDeletedState().length; i++) {
			// 删除的属性名
			String propertyName = event.getPersister().getPropertyNames()[i];
			// 删除的值
			if (null != event.getDeletedState() && event.getDeletedState().length >= i) {
				Object value = event.getDeletedState()[i];
			}
		}
		logger.info(event.getEntity().getClass().getName() + ":删除结束");
	}

	@Override
	public void onSaveOrUpdate(SaveOrUpdateEvent event) throws HibernateException {
		logger.info(event.getEntity().getClass().getName() + ":新增或修改结束");
	}

	@Override
	public boolean requiresPostCommitHanding(EntityPersister persister) {
		logger.info("requiresPostCommitHanding");
		return true;
	}
}
