/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.integration.wtp.ui.internal.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.integration.wtp.ui.internal.webapplication.ContextParamBean;
import org.eclipse.jst.j2ee.common.Listener;
import org.eclipse.jst.j2ee.jsp.JSPConfig;
import org.eclipse.jst.j2ee.jsp.TagLibRefType;
import org.eclipse.jst.j2ee.webapplication.FilterMapping;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.j2ee.webapplication.TagLibRef;
import org.eclipse.jst.j2ee.webapplication.WebApp;

/**
 * Birt WebArtifact Utility
 * 
 */
public class WebArtifactUtil {

	/**
	 * get filter-mapping from list by key String
	 * 
	 * @param list
	 * @param name
	 * @return
	 */
	public static Object getFilterMappingByKey(List list, String key) {
		if (list == null || key == null)
			return null;

		Iterator it = list.iterator();
		while (it.hasNext()) {
			// get filter-mapping object
			FilterMapping filterMapping = (FilterMapping) it.next();
			if (filterMapping != null) {
				String name = filterMapping.getFilter().getName();
				String servletName = filterMapping.getServletName();
				String uri = filterMapping.getUrlPattern();
				String curKey = getFilterMappingString(name, servletName, uri);
				if (key.equals(curKey))
					return filterMapping;
			}
		}

		return null;
	}

	/**
	 * get listener from list by class name
	 * 
	 * @param list
	 * @param name
	 * @return
	 */
	public static Object getListenerByClassName(List list, String className) {
		if (list == null || className == null)
			return null;

		Iterator it = list.iterator();
		while (it.hasNext()) {
			// get listener object
			Listener listener = (Listener) it.next();
			if (listener != null && className.equals(listener.getListenerClassName())) {
				return listener;
			}
		}

		return null;
	}

	/**
	 * get servlet mapping from list by uri
	 * 
	 * @param list
	 * @param name
	 * @return
	 */
	public static Object getServletMappingByUri(List list, String uri) {
		if (list == null || uri == null)
			return null;

		Iterator it = list.iterator();
		while (it.hasNext()) {
			// get servlet-mapping object
			ServletMapping servletMapping = (ServletMapping) it.next();
			if (servletMapping != null && uri.equals(servletMapping.getUrlPattern())) {
				return servletMapping;
			}
		}

		return null;
	}

	/**
	 * get servlet mapping from webapp by uri
	 * 
	 * @param webapp
	 * @param name
	 * @return
	 */
	public static Object getTagLibByUri(WebApp webapp, String uri) {
		if (webapp == null || uri == null)
			return null;

		List list = null;

		JSPConfig config = webapp.getJspConfig();
		if (config != null) {
			// for servlet 2.4
			list = config.getTagLibs();
		} else {
			list = webapp.getTagLibs();
		}

		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object obj = it.next();

			// for servlet 2.3
			if (obj instanceof TagLibRef) {
				TagLibRef ref = (TagLibRef) obj;
				if (uri.equals(ref.getTaglibURI()))
					return ref;
			}

			// for servlet 2.4
			if (obj instanceof TagLibRefType) {
				TagLibRefType ref = (TagLibRefType) obj;
				if (uri.equals(ref.getTaglibURI()))
					return ref;
			}
		}

		return null;
	}

	/**
	 * returns context-param value
	 * 
	 * @param map
	 * @param name
	 * @return
	 */
	public static String getContextParamValue(Map map, String name) {
		if (map == null || name == null)
			return null;

		ContextParamBean bean = (ContextParamBean) map.get(name);
		if (bean == null)
			return null;

		return bean.getValue();
	}

	/**
	 * set param value
	 * 
	 * @param map
	 * @param name
	 * @param value
	 */
	public static void setContextParamValue(Map map, String name, String value) {
		if (name == null)
			return;

		if (map == null)
			map = new HashMap();

		// get context-param bean
		ContextParamBean bean = (ContextParamBean) map.get(name);
		if (bean == null) {
			bean = new ContextParamBean(name, value);
			map.put(name, bean);
			return;
		}

		bean.setValue(value);
	}

	/**
	 * Returns the filter-mapping string
	 * 
	 * @param name
	 * @param servletName
	 * @param uri
	 * @return
	 */
	public static String getFilterMappingString(String name, String servletName, String uri) {
		return (name != null ? name : "") //$NON-NLS-1$
				+ (servletName != null ? servletName : "") //$NON-NLS-1$
				+ (uri != null ? uri : ""); //$NON-NLS-1$
	}
}
