/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework.eclipse;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IBundle;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.IPlatform;
import org.eclipse.birt.core.framework.IPlatformPath;
import org.eclipse.birt.core.internal.util.EclipseUtil;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 *
 */
public class EclipsePlatform implements IPlatform {
	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger(IPlatform.class.getName());

	BundleContext context;
	ClassLoader contextClassLoader;

	public EclipsePlatform(BundleContext context, ClassLoader contextClassLoader) {
		this.context = context;
		this.contextClassLoader = contextClassLoader;
	}

	@Override
	public IExtensionRegistry getExtensionRegistry() {
		return new EclipseExtensionRegistry(Platform.getExtensionRegistry());
	}

	@Override
	public IAdapterManager getAdapterManager() {
		return Platform.getAdapterManager();
	}

	@Override
	public IBundle getBundle(String symbolicName) {
		Bundle bundle = EclipseUtil.getBundle(symbolicName);
		if (bundle != null) {
			return new EclipseBundle(bundle);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.core.framework.IPlatform#find(org.eclipse.birt.core.
	 * framework.IBundle, org.eclipse.core.runtime.IPath)
	 */
	@Override
	public URL find(IBundle bundle, IPlatformPath path) {
		if ((bundle instanceof EclipseBundle) && (path instanceof EclipsePath)) {
			return FileLocator.find(((EclipseBundle) bundle).bundle, ((EclipsePath) path).path, null);
		}

		return null;
	}

	@Override
	public URL asLocalURL(URL url) throws IOException {
		return FileLocator.toFileURL(url);
	}

	static IConfigurationElement wrap(org.eclipse.core.runtime.IConfigurationElement object) {
		if (object != null) {
			return new EclipseConfigurationElement(object);
		}
		return null;
	}

	static IConfigurationElement[] wrap(org.eclipse.core.runtime.IConfigurationElement[] objects) {
		if (objects == null) {
			return new IConfigurationElement[0];
		}
		IConfigurationElement[] wraps = new IConfigurationElement[objects.length];
		for (int i = 0; i < objects.length; i++) {
			wraps[i] = new EclipseConfigurationElement(objects[i]);
		}
		return wraps;
	}

	static IExtensionPoint wrap(org.eclipse.core.runtime.IExtensionPoint object) {
		if (object != null) {
			return new EclipseExtensionPoint(object);
		}
		return null;
	}

	static IExtensionPoint[] wrap(org.eclipse.core.runtime.IExtensionPoint[] objects) {
		if (objects == null) {
			return new IExtensionPoint[0];
		}
		IExtensionPoint[] wraps = new IExtensionPoint[objects.length];
		for (int i = 0; i < objects.length; i++) {
			wraps[i] = new EclipseExtensionPoint(objects[i]);
		}
		return wraps;
	}

	static IExtension wrap(org.eclipse.core.runtime.IExtension object) {
		if (object != null) {
			return new EclipseExtension(object);
		}
		return null;
	}

	static IExtension[] wrap(org.eclipse.core.runtime.IExtension[] objects) {
		if (objects == null) {
			return new IExtension[0];
		}
		IExtension[] wraps = new IExtension[objects.length];
		for (int i = 0; i < objects.length; i++) {
			wraps[i] = new EclipseExtension(objects[i]);
		}
		return wraps;
	}

	static Object wrap(Object object) {
		if (object instanceof org.eclipse.core.runtime.IConfigurationElement) {
			return EclipsePlatform.wrap((org.eclipse.core.runtime.IConfigurationElement) object);
		} else if (object instanceof org.eclipse.core.runtime.IExtension) {
			return EclipsePlatform.wrap((org.eclipse.core.runtime.IExtension) object);
		} else if (object instanceof org.eclipse.core.runtime.IExtensionPoint) {
			return EclipsePlatform.wrap((org.eclipse.core.runtime.IExtensionPoint) object);
		}
		return object;
	}

	/**
	 * get debug options.
	 *
	 * call Eclipse's getDebugeOption directly.
	 *
	 * @param option option name
	 * @return option value
	 */
	@Override
	public String getDebugOption(String option) {
		return Platform.getDebugOption(option);
	}

	/**
	 * setup logger used for tracing.
	 *
	 * It reads ".options" in the plugin folder to get all the tracing items, call
	 * the .getDebugOptions() to get the option values and setup the logger use the
	 * values.
	 *
	 * @param pluginId plugin id
	 */
	@Override
	public void initializeTracing(String pluginId) {

		Bundle bundle = EclipseUtil.getBundle(pluginId);
		String debugFlag = pluginId + "/debug";
		if (bundle != null) {

			try {
				URL optionUrl = bundle.getEntry(".options");
				InputStream in = optionUrl.openStream();
				if (in != null) {
					Properties options = new Properties();
					options.load(in);
					Iterator entryIter = options.entrySet().iterator();
					while (entryIter.hasNext()) {
						Map.Entry entry = (Map.Entry) entryIter.next();
						String option = (String) entry.getKey();
						if (!debugFlag.equals(option)) {
							String value = org.eclipse.core.runtime.Platform.getDebugOption(option);
							setupLogger(option, value);
						}
					}
				}
			} catch (Exception ex) {
				logger.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

	/**
	 * setup logger
	 *
	 * @param option
	 * @param value
	 */
	static void setupLogger(String option, String value) {
		// get the plugin name
		if ("true".equals(value)) {
			Level level = getLoggerLevel(option);
			String loggerName = getLoggerName(option);
			Logger logger = Logger.getLogger(loggerName);
			logger.addHandler(getTracingHandler());
			logger.setLevel(level);
		}
	}

	/**
	 * get the logger level from the option.
	 *
	 * It checks the option name, to see if it matches the rules:
	 *
	 * .fine Logger.FINE .finer Logger.FINER .finest Logger.FINEST
	 *
	 * others are Logger.FINE
	 *
	 * @param option option name
	 * @return logger level
	 */
	static protected Level getLoggerLevel(String option) {
		assert option != null;
		if (option.endsWith(".finer")) {
			return Level.FINER;
		}
		if (option.endsWith(".finest")) {
			return Level.FINEST;
		}
		return Level.FINE;
	}

	/**
	 * get the logger name from the option.
	 *
	 * It get the logger name from the options: 1) remove any post fix from the
	 * option (.fine, .finest, .finer) 2) replace all '/' with '.' 3) trim spaces
	 *
	 * @param option option name
	 * @return the logger used to output the trace of that option
	 *
	 */
	static protected String getLoggerName(String option) {
		assert option != null;
		if (option.endsWith(".fine")) {
			option = option.substring(0, option.length() - 5);
		} else if (option.endsWith("finer")) {
			option = option.substring(0, option.length() - 6);
		} else if (option.endsWith(".finest")) {
			option = option.substring(0, option.length() - 7);
		}
		return option.replace('/', '.').trim();
	}

	/**
	 * logger handler use to output the trace information.
	 */
	// use initialization on demand holder idiom to avoid double-checking problem.
	private static class TracingHandlerHolder {
		public static StreamHandler tracingHandler = new StreamHandler(System.out, new SimpleFormatter());
		static {
			tracingHandler.setLevel(Level.ALL);
		}
	}

	/**
	 * get the trace logger handle.
	 *
	 * Trace logger handle output all the logging information to System.out
	 *
	 * @return
	 */
	static StreamHandler getTracingHandler() {
		return TracingHandlerHolder.tracingHandler;
	}

	/**
	 * Create factory object for an extension by picking the highest priority
	 * factory extension
	 *
	 * @throws FrameworkException
	 */
	public static Object createFactoryObjectForExtension(IExtension[] extensions, String extensionId)
			throws FrameworkException {
		if (extensions == null || extensionId == null) {
			return null;
		}

		// Find extension with highest priority
		int hiPriority = Integer.MIN_VALUE;
		IConfigurationElement hiConfig = null;
		for (IExtension ext : extensions) {
			if (extensionId.equals(ext.getUniqueIdentifier())) {
				IConfigurationElement[] ces = ext.getConfigurationElements();
				if (ces != null && ces.length > 0) {
					// We only expect one config element "factory"
					String priStr = ces[0].getAttribute("priority");
					int priority = 0;
					try {
						if (priStr != null && !priStr.isEmpty()) {
							priority = Integer.parseInt(priStr);
						}
					} catch (NumberFormatException e) {
					}

					if (priority > hiPriority) {
						hiPriority = priority;
						hiConfig = ces[0];
					}
				}
			}
		}

		if (hiConfig != null) {
			Object factory = hiConfig.createExecutableExtension("class");
			return factory;
		}
		return null;
	}

	@Override
	public Object createFactoryObject(String extensionId) {
		try {
			IExtensionRegistry registry = getExtensionRegistry();
			// We allow multiple extensions with the same extensionId. In such a case
			// the priority attribute on the factory element is used to pick the one we use
			IExtensionPoint extPoint = registry.getExtensionPoint("org.eclipse.birt.core",
					IPlatform.EXTENSION_POINT_FACTORY_SERVICE);
			if (extPoint != null) {
				return createFactoryObjectForExtension(extPoint.getExtensions(), extensionId);
			}
		} catch (Exception ex) {
			logger.log(Level.WARNING, ex.getMessage(), ex);
		}
		return null;

	}

	@Override
	public Object enterPlatformContext() {
		Thread thread = Thread.currentThread();
		ClassLoader loader = thread.getContextClassLoader();
		thread.setContextClassLoader(contextClassLoader);
		return loader;
	}

	@Override
	public void exitPlatformContext(Object context) {
		if (!(context instanceof ClassLoader)) {
			throw new IllegalArgumentException("The context must be returned by teh enterPlatformContext");
		}
		Thread.currentThread().setContextClassLoader((ClassLoader) context);
	}

	@Override
	public String getOS() {
		return Platform.getOS();
	}
}
