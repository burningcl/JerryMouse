package com.skyline.db.jerrymouse.core;

import android.util.Log;

import com.skyline.db.jerrymouse.core.annotation.DaoConf;
import com.skyline.db.jerrymouse.core.annotation.Sql;
import com.skyline.db.jerrymouse.core.exception.DataSourceException;
import com.skyline.db.jerrymouse.core.exception.MethodParseException;
import com.skyline.db.jerrymouse.core.proxy.AbsMethodProxy;
import com.skyline.db.jerrymouse.core.proxy.DeleteMethodProxy;
import com.skyline.db.jerrymouse.core.proxy.InsertMethodProxy;
import com.skyline.db.jerrymouse.core.proxy.SelectMethodProxy;
import com.skyline.db.jerrymouse.core.proxy.UpdateMethodProxy;
import com.skyline.db.jerrymouse.core.type.SqlType;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by jairus on 15-12-10.
 */
public class DaoProxy implements InvocationHandler {

	/**
	 * LOG_TAG
	 */
	private static final String LOG_TAG = DaoProxy.class.getSimpleName();

	/**
	 * DAO_CACHE
	 */
	private static LRUCache<Class<? extends Dao>, Object> DAO_CACHE = new LRUCache<>(32);

	/**
	 * METHOD_PROXY_CACHE
	 */
	private static LRUCache<Class<? extends Dao>, LRUCache<Method, AbsMethodProxy>> METHOD_PROXY_CACHE = new LRUCache<>(32);

	/**
	 *
	 */
	private Class<? extends Dao> clazz;

	/**
	 *
	 */
	protected boolean enableCache = true;

	/**
	 * @param clazz
	 */
	private DaoProxy(Class<? extends Dao> clazz) {
		this.clazz = clazz;
		DaoConf daoConf = clazz.getAnnotation(DaoConf.class);
		if (daoConf != null) {
			enableCache = daoConf.enableParseCache();
			Log.d(LOG_TAG, "DaoProxy, clazz: " + clazz + ", enableCache: " + enableCache);
		} else {
			enableCache = true;
		}
	}

	/**
	 * @param clazz
	 * @param <D>
	 * @return
	 * @throws Exception
	 */
	public synchronized static <D extends Dao> D getDao(Class<D> clazz) throws Exception {
		if (clazz == null) {
			throw new DataSourceException(DataSourceException.Reason.DAO_CLASS_REQUIRED);
		}
		try {
			Object cachedProxy = DAO_CACHE.get(clazz);
			if (cachedProxy != null) {
				return (D) cachedProxy;
			}
		} catch (Exception e) {
			Log.w(LOG_TAG, "fail to get dao from cache", e);
		}
		D proxy = (D) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new DaoProxy(clazz));
		DAO_CACHE.put(clazz, proxy);
		return proxy;
	}


	/**
	 * @param proxy
	 * @param method
	 * @param args
	 * @return
	 * @throws Exception
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Exception {

		Sql sql = method.getAnnotation(Sql.class);

		AbsMethodProxy methodProxy = getMethodProxy(method, sql);

		if (methodProxy == null) {
			throw new MethodParseException(MethodParseException.Reason.UNABLE_TO_PARSE_SQL_ANNOTATION);
		}
		methodProxy.parseClassAnnotations();
		methodProxy.parseMethodAnnotations();
		return methodProxy.invoke(args);
	}

	/**
	 * @param method
	 * @param sql
	 * @return
	 * @throws MethodParseException
	 */
	private AbsMethodProxy getMethodProxy(Method method, Sql sql) throws MethodParseException {
		if (sql == null) {
			throw new MethodParseException(MethodParseException.Reason.SQL_METHOD_ANNOTATION_REQUIRED);
		}

		SqlType sqlType = sql.type();

		AbsMethodProxy methodProxy = getMethodProxyFromCache(clazz, method);
		if (methodProxy != null) {
			return methodProxy;
		}

		if (sqlType.equals(SqlType.SELECT)) {
			methodProxy = new SelectMethodProxy(clazz, method, sql.value(), sql.mapper());
		} else if (sqlType.equals(SqlType.INSERT)) {
			methodProxy = new InsertMethodProxy(clazz, method);
		} else if (sqlType.equals(SqlType.UPDATE)) {
			methodProxy = new UpdateMethodProxy(clazz, method, sql.value());
		} else if (sqlType.equals(SqlType.DELETE)) {
			methodProxy = new DeleteMethodProxy(clazz, method, sql.value());
		}

		if (methodProxy != null)
			putMethodProxyIntoCache(clazz, method, methodProxy);
		return methodProxy;
	}

	/**
	 * @param clazz
	 * @param method
	 * @return
	 */
	private synchronized AbsMethodProxy getMethodProxyFromCache(Class<? extends Dao> clazz, Method method) {
		if (!enableCache || clazz == null || method == null) {
			return null;
		}
		LRUCache<Method, AbsMethodProxy> subMethodProxyCache = METHOD_PROXY_CACHE.get(clazz);
		if (subMethodProxyCache == null) {
			subMethodProxyCache = new LRUCache<>(8);
			METHOD_PROXY_CACHE.put(clazz, subMethodProxyCache);
		}
		AbsMethodProxy methodProxy = subMethodProxyCache.get(method);
		return methodProxy;
	}

	/**
	 * @param clazz
	 * @param method
	 * @param methodProxy
	 */
	private synchronized void putMethodProxyIntoCache(Class<? extends Dao> clazz, Method method, AbsMethodProxy methodProxy) {
		if (!enableCache || clazz == null || method == null || methodProxy == null) {
			return;
		}
		LRUCache<Method, AbsMethodProxy> subMethodProxyCache = METHOD_PROXY_CACHE.get(clazz);
		if (subMethodProxyCache == null) {
			subMethodProxyCache = new LRUCache<>(8);
			METHOD_PROXY_CACHE.put(clazz, subMethodProxyCache);
		}
		subMethodProxyCache.put(method, methodProxy);
	}

}
