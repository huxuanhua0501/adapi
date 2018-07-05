package com.racetime.xsad.util;

import java.util.Map;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.internal.Primitives;

public class BeanUtil {

	/**
	 * map转object
	 * @param map
	 * @param beanClass
	 * @return
	 * @throws Exception
	 */
	public static <T> T mapToObject(Map<String, Object> map, Class<T> classOfT)
			throws Exception {
		if (map == null)
			return null;

		Object obj = classOfT.newInstance();

		BeanUtils.populate(obj, map);

		return Primitives.wrap(classOfT).cast(obj);
	}

	/**
	 * object转map
	 * @param obj
	 * @return
	 */
	public static Map<?, ?> objectToMap(Object obj) {
		if (obj == null)
			return null;

		return new BeanMap(obj);
	}
}
