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

package org.eclipse.birt.chart.integration.wtp.ui.internal.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.integration.wtp.ui.internal.webapp.ContextParamBean;
import org.eclipse.birt.chart.integration.wtp.ui.internal.webapp.ListenerBean;
import org.eclipse.birt.chart.integration.wtp.ui.internal.webapp.ServletBean;
import org.eclipse.birt.chart.integration.wtp.ui.internal.webapp.ServletMappingBean;
import org.eclipse.birt.chart.integration.wtp.ui.internal.webapp.TagLibBean;
import org.eclipse.birt.chart.integration.wtp.ui.internal.webapp.WebAppBean;
import org.eclipse.birt.chart.integration.wtp.ui.internal.wizards.IBirtWizardConstants;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jst.j2ee.common.CommonFactory;
import org.eclipse.jst.j2ee.common.Description;
import org.eclipse.jst.j2ee.common.Listener;
import org.eclipse.jst.j2ee.common.ParamValue;
import org.eclipse.jst.j2ee.jsp.JSPConfig;
import org.eclipse.jst.j2ee.jsp.JspFactory;
import org.eclipse.jst.j2ee.jsp.TagLibRefType;
import org.eclipse.jst.j2ee.web.componentcore.util.WebArtifactEdit;
import org.eclipse.jst.j2ee.webapplication.ContextParam;
import org.eclipse.jst.j2ee.webapplication.Servlet;
import org.eclipse.jst.j2ee.webapplication.ServletMapping;
import org.eclipse.jst.j2ee.webapplication.ServletType;
import org.eclipse.jst.j2ee.webapplication.TagLibRef;
import org.eclipse.jst.j2ee.webapplication.WebApp;
import org.eclipse.jst.j2ee.webapplication.WebapplicationFactory;
import org.eclipse.ui.dialogs.IOverwriteQuery;

/**
 * Birt WebArtifact Utility
 *
 */
public class WebArtifactUtil implements IBirtWizardConstants {

	/**
	 * Configure the web application general descriptions
	 *
	 * @param webApp
	 * @param project
	 * @param query
	 * @param monitor
	 * @throws CoreException
	 */
	public static void configureWebApp(WebAppBean webAppBean, IProject project, IOverwriteQuery query,
			IProgressMonitor monitor) throws CoreException {
		// cancel progress
		if (monitor.isCanceled() || webAppBean == null || project == null) {
			return;
		}

		// create WebArtifact
		WebArtifactEdit webEdit = WebArtifactEdit.getWebArtifactEditForWrite(project);
		if (webEdit == null) {
			return;
		}

		try {
			WebApp webapp = (WebApp) webEdit.getDeploymentDescriptorRoot();
			webapp.setDescription(webAppBean.getDescription());
			webEdit.saveIfNecessary(monitor);
		} finally {
			webEdit.dispose();
		}
	}

	/**
	 * Configure the context param settings
	 *
	 * @param map
	 * @param project
	 * @param query
	 * @param monitor
	 * @throws CoreException
	 */
	public static void configureContextParam(Map map, IProject project, IOverwriteQuery query, IProgressMonitor monitor)
			throws CoreException {
		// cancel progress
		if (monitor.isCanceled() || map == null || project == null) {
			return;
		}

		// create WebArtifact
		WebArtifactEdit webEdit = WebArtifactEdit.getWebArtifactEditForWrite(project);
		if (webEdit == null) {
			return;
		}

		try {
			WebApp webapp = (WebApp) webEdit.getDeploymentDescriptorRoot();

			// handle context-param settings
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String name = DataUtil.getString(it.next(), false);
				ContextParamBean bean = (ContextParamBean) map.get(name);
				if (bean == null) {
					continue;
				}

				// if contained this param
				List list = null;
				if (webapp.getVersionID() == 23) {
					// for servlet 2.3
					list = webapp.getContexts();
				} else {
					// for servlet 2.4
					list = webapp.getContextParams();
				}

				Object obj = getContextParamByName(list, name);
				if (obj != null) {
					String ret = query.queryOverwrite("Context-param '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$

					// check overwrite query result
					if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
						continue;
					}
					if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
						monitor.setCanceled(true);
						return;
					}

					// remove old item
					webapp.getContextParams().remove(obj);
				}

				String value = bean.getValue();
				String description = bean.getDescription();

				if (webapp.getVersionID() == 23) {
					// create context-param object
					ContextParam param = WebapplicationFactory.eINSTANCE.createContextParam();
					param.setParamName(name);
					param.setParamValue(value);
					if (description != null) {
						param.setDescription(description);
					}

					param.setWebApp(webapp);
				} else {
					// create ParamValue object for servlet 2.4
					ParamValue param = CommonFactory.eINSTANCE.createParamValue();
					param.setName(name);
					param.setValue(value);
					if (description != null) {
						Description descriptionObj = CommonFactory.eINSTANCE.createDescription();
						descriptionObj.setValue(description);
						param.getDescriptions().add(descriptionObj);
						param.setDescription(description);
					}

					// add into list
					webapp.getContextParams().add(param);
				}
			}

			webEdit.saveIfNecessary(monitor);
		} finally {
			webEdit.dispose();
		}
	}

	/**
	 * get context-param from list by name
	 *
	 * @param list
	 * @param name
	 * @return
	 */
	public static Object getContextParamByName(List list, String name) {
		if (list == null || name == null) {
			return null;
		}

		Iterator it = list.iterator();
		while (it.hasNext()) {
			// get param object
			Object paramObj = it.next();

			// for servlet 2.3
			if (paramObj instanceof ContextParam) {
				ContextParam param = (ContextParam) paramObj;
				if (name.equals(param.getParamName())) {
					return param;
				}
			}

			// for servlet 2.4
			if (paramObj instanceof ParamValue) {
				ParamValue param = (ParamValue) paramObj;
				if (name.equals(param.getName())) {
					return param;
				}
			}
		}

		return null;
	}

	/**
	 * Configure the listener settings
	 *
	 * @param map
	 * @param project
	 * @param query
	 * @param monitor
	 * @throws CoreException
	 */
	public static void configureListener(Map map, IProject project, IOverwriteQuery query, IProgressMonitor monitor)
			throws CoreException {
		// cancel progress
		if (monitor.isCanceled() || map == null || project == null) {
			return;
		}

		// create WebArtifact
		WebArtifactEdit webEdit = WebArtifactEdit.getWebArtifactEditForWrite(project);
		if (webEdit == null) {
			return;
		}

		try {
			WebApp webapp = (WebApp) webEdit.getDeploymentDescriptorRoot();

			// handle listeners settings
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String name = DataUtil.getString(it.next(), false);
				ListenerBean bean = (ListenerBean) map.get(name);
				if (bean == null) {
					continue;
				}

				String className = bean.getClassName();
				String description = bean.getDescription();

				// if listener existed in web.xml, skip it
				Object obj = getListenerByClassName(webapp.getListeners(), className);
				if (obj != null) {
					continue;
				}

				// create Listener object
				Listener listener = CommonFactory.eINSTANCE.createListener();
				listener.setListenerClassName(className);
				if (description != null) {
					listener.setDescription(description);
				}

				webapp.getListeners().remove(listener);
				webapp.getListeners().add(listener);
			}

			webEdit.saveIfNecessary(monitor);
		} finally {
			webEdit.dispose();
		}
	}

	/**
	 * get listener from list by class name
	 *
	 * @param list
	 * @param name
	 * @return
	 */
	public static Object getListenerByClassName(List list, String className) {
		if (list == null || className == null) {
			return null;
		}

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
	 * Configure the servlet settings
	 *
	 * @param map
	 * @param project
	 * @param query
	 * @param monitor
	 * @throws CoreException
	 */
	public static void configureServlet(Map map, IProject project, IOverwriteQuery query, IProgressMonitor monitor)
			throws CoreException {
		// cancel progress
		if (monitor.isCanceled() || map == null || project == null) {
			return;
		}

		// create WebArtifact
		WebArtifactEdit webEdit = WebArtifactEdit.getWebArtifactEditForWrite(project);
		if (webEdit == null) {
			return;
		}

		try {
			WebApp webapp = (WebApp) webEdit.getDeploymentDescriptorRoot();

			// handle servlet settings
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String name = DataUtil.getString(it.next(), false);
				ServletBean bean = (ServletBean) map.get(name);

				if (bean == null) {
					continue;
				}

				// if contained this servlet
				Object obj = getServletByName(webapp.getServlets(), name);
				if (obj != null) {
					String ret = query.queryOverwrite("Servlet '" + name + "'"); //$NON-NLS-1$ //$NON-NLS-2$

					// check overwrite query result
					if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
						continue;
					}
					if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
						monitor.setCanceled(true);
						return;
					}

					// remove old item
					webapp.getServlets().remove(obj);
				}

				String className = bean.getClassName();
				String description = bean.getDescription();

				// create Servlet Type object
				ServletType servletType = WebapplicationFactory.eINSTANCE.createServletType();
				servletType.setClassName(className);

				// create Servlet object
				Servlet servlet = WebapplicationFactory.eINSTANCE.createServlet();
				servlet.setServletName(name);
				if (description != null) {
					servlet.setDescription(description);
				}

				servlet.setWebType(servletType);

				servlet.setWebApp(webapp);
			}

			webEdit.saveIfNecessary(monitor);
		} finally {
			webEdit.dispose();
		}
	}

	/**
	 * get servlet from list by name
	 *
	 * @param list
	 * @param name
	 * @return
	 */
	public static Object getServletByName(List list, String name) {
		if (list == null || name == null) {
			return null;
		}

		Iterator it = list.iterator();
		while (it.hasNext()) {
			// get servlet object
			Servlet servlet = (Servlet) it.next();
			if (servlet != null && name.equals(servlet.getServletName())) {
				return servlet;
			}
		}

		return null;
	}

	/**
	 * Configure the servlet-mapping settings
	 *
	 * @param map
	 * @param project
	 * @param query
	 * @param monitor
	 * @throws CoreException
	 */
	public static void configureServletMapping(Map map, IProject project, IOverwriteQuery query,
			IProgressMonitor monitor) throws CoreException {
		// cancel progress
		if (monitor.isCanceled() || map == null || project == null) {
			return;
		}

		// create WebArtifact
		WebArtifactEdit webEdit = WebArtifactEdit.getWebArtifactEditForWrite(project);
		if (webEdit == null) {
			return;
		}

		try {
			WebApp webapp = (WebApp) webEdit.getDeploymentDescriptorRoot();

			// handle servlet-mapping settings
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String uri = DataUtil.getString(it.next(), false);
				ServletMappingBean bean = (ServletMappingBean) map.get(uri);

				if (bean == null) {
					continue;
				}

				// if contained this servlet-mapping
				Object obj = getServletMappingByUri(webapp.getServletMappings(), uri);
				if (obj != null) {
					String ret = query.queryOverwrite("Servlet-mapping '" + uri + "'"); //$NON-NLS-1$ //$NON-NLS-2$

					// check overwrite query result
					if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
						continue;
					}
					if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
						monitor.setCanceled(true);
						return;
					}

					// remove old item
					webapp.getServletMappings().remove(obj);
				}

				// servlet name
				String name = bean.getName();

				// create ServletMapping object
				ServletMapping mapping = WebapplicationFactory.eINSTANCE.createServletMapping();

				// get servlet by name
				Servlet servlet = webapp.getServletNamed(name);
				if (servlet != null) {
					mapping.setServlet(servlet);
					mapping.setUrlPattern(uri);
					mapping.setWebApp(webapp);
				}
			}

			webEdit.saveIfNecessary(monitor);
		} finally {
			webEdit.dispose();
		}
	}

	/**
	 * get servlet mapping from list by uri
	 *
	 * @param list
	 * @param name
	 * @return
	 */
	public static Object getServletMappingByUri(List list, String uri) {
		if (list == null || uri == null) {
			return null;
		}

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
	 * Configure the taglib settings
	 *
	 * @param map
	 * @param project
	 * @param query
	 * @param monitor
	 * @throws CoreException
	 */
	public static void configureTaglib(Map map, IProject project, IOverwriteQuery query, IProgressMonitor monitor)
			throws CoreException {
		// cancel progress
		if (monitor.isCanceled() || map == null || project == null) {
			return;
		}

		// create WebArtifact
		WebArtifactEdit webEdit = WebArtifactEdit.getWebArtifactEditForWrite(project);
		if (webEdit == null) {
			return;
		}

		try {
			WebApp webapp = (WebApp) webEdit.getDeploymentDescriptorRoot();

			// handle taglib settings
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String uri = DataUtil.getString(it.next(), false);
				TagLibBean bean = (TagLibBean) map.get(uri);

				if (bean == null) {
					continue;
				}

				// if contained this taglib
				Object obj = getTagLibByUri(webapp, uri);
				if (obj != null) {
					String ret = query.queryOverwrite("Taglib '" + uri + "'"); //$NON-NLS-1$ //$NON-NLS-2$

					// check overwrite query result
					if (IOverwriteQuery.NO.equalsIgnoreCase(ret)) {
						continue;
					}
					if (IOverwriteQuery.CANCEL.equalsIgnoreCase(ret)) {
						monitor.setCanceled(true);
						return;
					}

					// remove old item
					if (obj instanceof TagLibRefType && webapp.getJspConfig() != null) {
						webapp.getJspConfig().getTagLibs().remove(obj);
					} else {
						webapp.getTagLibs().remove(obj);
					}

				}

				String location = bean.getLocation();

				if (webapp.getVersionID() == 23) {
					// create TaglibRef object for servlet 2.3
					TagLibRef taglib = WebapplicationFactory.eINSTANCE.createTagLibRef();
					taglib.setTaglibURI(uri);
					taglib.setTaglibLocation(location);
					webapp.getTagLibs().add(taglib);
				} else {
					// for servlet 2.4
					JSPConfig jspConfig = JspFactory.eINSTANCE.createJSPConfig();
					TagLibRefType ref = JspFactory.eINSTANCE.createTagLibRefType();
					ref.setTaglibURI(uri);
					ref.setTaglibLocation(location);
					jspConfig.getTagLibs().add(ref);
					webapp.setJspConfig(jspConfig);
				}
			}

			webEdit.saveIfNecessary(monitor);

		} finally {
			webEdit.dispose();
		}
	}

	/**
	 * get servlet mapping from webapp by uri
	 *
	 * @param webapp
	 * @param name
	 * @return
	 */
	public static Object getTagLibByUri(WebApp webapp, String uri) {
		if (webapp == null || uri == null) {
			return null;
		}

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
				if (uri.equals(ref.getTaglibURI())) {
					return ref;
				}
			}

			// for servlet 2.4
			if (obj instanceof TagLibRefType) {
				TagLibRefType ref = (TagLibRefType) obj;
				if (uri.equals(ref.getTaglibURI())) {
					return ref;
				}
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
		if (map == null || name == null) {
			return null;
		}

		ContextParamBean bean = (ContextParamBean) map.get(name);
		if (bean == null) {
			return null;
		}

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
		if (name == null) {
			return;
		}

		if (map == null) {
			map = new HashMap();
		}

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
	 * Initialize web settings from existed web.xml file
	 *
	 * @param map
	 * @param project
	 */
	public static void initializeWebapp(Map map, IProject project) {
		if (project == null) {
			return;
		}

		if (map == null) {
			map = new HashMap();
		}

		// create WebArtifact
		WebArtifactEdit webEdit = WebArtifactEdit.getWebArtifactEditForWrite(project);
		if (webEdit == null) {
			return;
		}

		try {
			// get webapp
			WebApp webapp = (WebApp) webEdit.getDeploymentDescriptorRoot();

			// context-param
			initializeContextParam(map, webapp);
		} finally {
			webEdit.dispose();
		}
	}

	/**
	 * Initialize context-param
	 *
	 * @param map
	 * @param webapp
	 */
	protected static void initializeContextParam(Map map, WebApp webapp) {
		if (webapp == null) {
			return;
		}

		// get context-param map
		Map son = (Map) map.get(EXT_CONTEXT_PARAM);
		if (son == null) {
			return;
		}

		// get param list
		List list = null;
		if (webapp.getVersionID() == 23) {
			// for servlet 2.3
			list = webapp.getContexts();
		} else {
			// for servlet 2.4
			list = webapp.getContextParams();
		}

		// initialzie context-param from web.xml
		Iterator it = son.keySet().iterator();
		while (it.hasNext()) {
			String name = (String) it.next();
			Object obj = getContextParamByName(list, name);
			if (obj == null) {
				continue;
			}

			String value = null;
			String description = null;

			if (obj instanceof ContextParam) {
				// for servlet 2.3
				ContextParam param = (ContextParam) obj;
				name = param.getParamName();
				value = param.getParamValue();
				description = param.getDescription();
			} else if (obj instanceof ParamValue) {
				// for servlet 2.4
				ParamValue param = (ParamValue) obj;
				name = param.getName();
				value = param.getValue();
				description = param.getDescription();
			}

			// push into map
			if (value != null) {
				ContextParamBean bean = new ContextParamBean(name, value);
				bean.setDescription(description);

				son.put(name, bean);
			}
		}
	}
}
