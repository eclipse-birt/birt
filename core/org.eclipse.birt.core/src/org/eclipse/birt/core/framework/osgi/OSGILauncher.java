/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.core.framework.osgi;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.Policy;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.core.framework.PlatformFileContext;
import org.eclipse.birt.core.framework.PlatformLauncher;
import org.eclipse.birt.core.framework.URLClassLoader;

public class OSGILauncher extends PlatformLauncher {

	private static Logger logger = Logger.getLogger(OSGILauncher.class.getName());

	/** the class used to start the elipse framework */
	private static final String ECLIPSE_STARTER = "org.eclipse.core.runtime.adaptor.EclipseStarter";

	private static String PluginId = "org.eclipse.birt.core";
	private PlatformConfig platformConfig;
	private File platformDirectory;
	private URL osgiFramework;
	private ChildFirstURLClassLoader frameworkClassLoader;
	private ClassLoader frameworkContextClassLoader;
	private HashMap properties;
	private Object bundleContext;

	// OSGi properties
	private static final String PROP_OSGI_INSTALL_AREA = "osgi.install.area";//$NON-NLS-1$
	private static final String PROP_OSGI_INSTANCE_AREA = "osgi.instance.area";//$NON-NLS-1$
	private static final String PROP_OSGI_CONFIGURATION_AREA = "osgi.configuration.area";//$NON-NLS-1$
	private static final String PROP_ECLIPSE_IGNOREAPP = "eclipse.ignoreApp";//$NON-NLS-1$
	private static final String PROP_OSGI_NOSHUTDOWN = "osgi.noShutdown";//$NON-NLS-1$
	private static final String PROP_OSGI_CLEAN = "osgi.clean";//$NON-NLS-1$
	private static final String PROP_ECLIPSE_SECURITY = "eclipse.security"; //$NON-NLS-1$

	private static final String CONFIG_FILE = "config.ini";//$NON-NLS-1$
	private static final String CONFIG_FOLDER = "configuration";//$NON-NLS-1$
	private static final String INSTANCE_FOLDER = "workspace";//$NON-NLS-1$

	public void startup(final PlatformConfig config) throws FrameworkException {
		try {
			java.security.AccessController.doPrivileged(new java.security.PrivilegedExceptionAction<Object>() {

				public Object run() throws Exception {
					doStartup(config);
					return null;
				}
			});
		} catch (Exception ex) {
			if (ex instanceof FrameworkException) {
				throw (FrameworkException) ex;
			}
			throw new FrameworkException(ex.getMessage(), ex);
		}
	}

	private void doStartup(PlatformConfig config) throws FrameworkException {
		if (frameworkClassLoader != null) {
			logger.log(Level.WARNING, "Framework is already started"); //$NON-NLS-1$
			return;
		}

		platformConfig = config;
		IPlatformContext context = config.getPlatformContext();
		if (context == null) {
			throw new FrameworkException("PlatformContext is not setted - {0}", new Object[] { "PlatformConfig" }); //$NON-NLS-1$
		}

		// process install.area
		String root = context.getPlatform();
		platformDirectory = new File(root);
		if (!platformDirectory.exists() || !platformDirectory.isDirectory()) {
			throw new FrameworkException("Framework {0} doesn't exist or is not a directory", new Object[] { root }); //$NON-NLS-1$
		}

		String searchPath = new File(platformDirectory, "plugins").toString(); //$NON-NLS-1$
		String path = searchFor("org.eclipse.osgi", searchPath); //$NON-NLS-1$
		if (path == null) {
			throw new FrameworkException("Could not find the Framework - {0} in path {1}", //$NON-NLS-1$
					new Object[] { "org.eclipse.osgi", searchPath });
		}
		try {
			osgiFramework = new File(path).toURI().toURL();
		} catch (MalformedURLException ex) {
			// cannot be here
		}

		ClassLoader original = Thread.currentThread().getContextClassLoader();
		try {
			ClassLoader loader = this.getClass().getClassLoader();
			frameworkClassLoader = new ChildFirstURLClassLoader(new URL[] { osgiFramework }, loader);
			// frameworkClassLoader = new OSGIClassLoader(
			// new URL[]{frameworkUrl}, loader );

			// Weblogic 8.1SP6 contains old version JS.JAR, we need
			// set pref-web-inf to true, if we set it to true, the
			// URL classloader still loads the JS in weblogic, so
			// load the class explicitly.
			try {
				loader.loadClass("org.mozilla.javascript.Context");
				loader.loadClass("org.mozilla.javascript.Scriptable");
				loader.loadClass("org.mozilla.javascript.ScriptableObject");
				// frameworkClassLoader.loadClass(
				// "org.mozilla.javascript.Context"
			} catch (Exception ex) {
			}

			Class clazz = frameworkClassLoader.loadClass(ECLIPSE_STARTER);

			setupOSGiProperties();
			setupSecurityPolicy();

			Method initPropertiesMethod = clazz.getMethod("setInitialProperties", new Class[] { Map.class }); //$NON-NLS-1$

			if (initPropertiesMethod != null) {
				System.setProperty("osgi.framework.useSystemProperties", "false"); //$NON-NLS-1$ //$NON-NLS-2$
				properties.put("osgi.framework.useSystemProperties", "false");
				initPropertiesMethod.invoke(null, new Object[] { properties });
			} else {
				Iterator iter = properties.entrySet().iterator();
				while (iter.hasNext()) {
					Map.Entry entry = (Map.Entry) iter.next();
					String key = (String) entry.getKey();
					String value = (String) entry.getValue();
					System.setProperty(key, value);
				}
				System.setProperty("osgi.framework.useSystemProperties", "true"); //$NON-NLS-1$ //$NON-NLS-2$
			}

			Method runMethod = clazz.getMethod("startup", new Class[] { String[].class, Runnable.class }); //$NON-NLS-1$
			bundleContext = runMethod.invoke(null, new Object[] { new String[] {}, null });
			frameworkContextClassLoader = Thread.currentThread().getContextClassLoader();
		} catch (FrameworkException be) {
			throw be;
		} catch (Exception e) {
			throw new FrameworkException("Can not start up OSGI - {0}", new Object[] { e.getMessage() }, e);
		} finally {
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	private void setupOSGiProperties() {
		properties = new HashMap();
		// copy all system properties to the property.
		Properties systemProperties = System.getProperties();
		if (systemProperties != null) {
			for (Iterator it = systemProperties.entrySet().iterator(); it.hasNext();) {
				Map.Entry entry = (Map.Entry) it.next();
				String key = (String) entry.getKey();
				Object value = entry.getValue();
				// Tomcat 6 setting some of property to Object instead of String
				if (value == null || value instanceof String) {
					if (!key.startsWith("osgi.") && !key.startsWith("eclipse.") && !key.startsWith("org.osgi.")) {
						properties.put(key, value);
					} else {
						properties.put(key, null);
					}
				}
			}
		}

		// load the config.ini distribued with BIRT system.
		File configFolder = new File(platformDirectory, CONFIG_FOLDER);
		try {
			HashMap configProps = loadConfiguration(configFolder.toURL());
			properties.putAll(configProps);
		} catch (Exception ex) {
			/* do nothing */}

		// copy properties defined by the caller
		Map osgiConfig = platformConfig.getOSGiConfig();
		if (osgiConfig != null) {
			Iterator iter = osgiConfig.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key instanceof String) {
					if (value == null || value instanceof String) {
						properties.put(key, value);
					}
				}
			}
		}
		// set up the properied defined by BIRT
		/* osgi.install.area, always using the one defined by BIRT_HOME. */
		properties.put(PROP_OSGI_INSTALL_AREA, platformDirectory.getAbsolutePath()); // $NON-NLS-1$

		/* setup the osgi framework, it is the osgi.jar in the platform/plugins */
		properties.put("osgi.framework", osgiFramework.toExternalForm()); //$NON-NLS-1$ //$NON-NLS-2$

		/* setup the config area, it is the /configuration under platform */
		String configArea = getProperty(properties, PROP_OSGI_CONFIGURATION_AREA);
		if (configArea == null) {
			File configDirectory = new File(platformDirectory, CONFIG_FOLDER);
			properties.put(PROP_OSGI_CONFIGURATION_AREA, configDirectory.getAbsolutePath());
		}

		// instance.area, it is the workspace under platform
		String instanceArea = getProperty(properties, PROP_OSGI_INSTANCE_AREA);
		if (instanceArea == null) {
			File workspaceDirectory = new File(platformDirectory, INSTANCE_FOLDER); // $NON-NLS-1$
			if (!workspaceDirectory.exists()) {
				workspaceDirectory.mkdirs();
			}
			properties.put(PROP_OSGI_INSTANCE_AREA, workspaceDirectory.getAbsolutePath()); // $NON-NLS-1$
		}

		properties.put(PROP_ECLIPSE_IGNOREAPP, "true");//$NON-NLS-1$
		properties.put(PROP_OSGI_NOSHUTDOWN, "true"); //$NON-NLS-1$

		// set -clean if the user doens't define it.
		String clean = getProperty(properties, PROP_OSGI_CLEAN);
		if (clean == null) {
			properties.put(PROP_OSGI_CLEAN, "true");
		}
	}

	private HashMap loadConfiguration(URL url) {
		HashMap result = null;
		if (url == null)
			return result;
		try {
			url = new URL(url, CONFIG_FILE);
		} catch (MalformedURLException e) {
			return result;
		}

		Properties tempProp = new Properties();
		;

		try {
			result = new HashMap();
			InputStream is = null;
			try {
				is = url.openStream();
				tempProp.load(is);
			} finally {
				if (is != null)
					try {
						is.close();
					} catch (IOException e) {
						// ignore failure to close
					}
			}
		} catch (IOException e) {
			// do nothing so far
		}

		// copy all the properties
		Iterator ti = tempProp.entrySet().iterator();
		while (ti.hasNext()) {
			Map.Entry entry = (Map.Entry) ti.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			result.put(key, value);
		}
		return result;
	}

	/**
	 * return the property value.
	 * 
	 * @param properties
	 * @param name
	 * @return value, must be none empty string or NULL.
	 */
	private String getProperty(Map properties, String name) {
		Object value = properties.get(name);
		if (value instanceof String) {
			String strValue = (String) value;
			strValue = strValue.trim();
			if (strValue.length() > 0) {
				return strValue;
			}
		}
		return null;
	}

	public ClassLoader getFrameworkContextClassLoader() {
		return frameworkContextClassLoader;
	}

	public void shutdown() {
		java.security.AccessController.doPrivileged(new java.security.PrivilegedAction<Object>() {

			public Object run() {
				doShutdown();
				return null;
			}
		});

	}

	private void doShutdown() {
		if (platformDirectory == null) {
			logger.log(Level.WARNING, "Shutdown unnecessary. (not deployed)"); //$NON-NLS-1$
			return;
		}

		if (frameworkClassLoader == null) {
			logger.log(Level.WARNING, "Framework is already shutdown"); //$NON-NLS-1$
			return;
		}

		ClassLoader original = Thread.currentThread().getContextClassLoader();
		try {
			Class clazz = frameworkClassLoader.loadClass(ECLIPSE_STARTER);
			Method method = clazz.getDeclaredMethod("shutdown", (Class[]) null); //$NON-NLS-1$
			Thread.currentThread().setContextClassLoader(frameworkContextClassLoader);
			method.invoke(clazz, (Object[]) null);

		} catch (Exception e) {
			logger.log(Level.WARNING, "Error while stopping Framework", e); //$NON-NLS-1$
			return;
		} finally {
			frameworkClassLoader.close();
			frameworkClassLoader = null;
			frameworkContextClassLoader = null;
			Thread.currentThread().setContextClassLoader(original);
		}
	}

	/***************************************************************************
	 * See org.eclipse.core.launcher [copy of searchFor, findMax, compareVersion,
	 * getVersionElements] TODO: If these methods were made public and static we
	 * could use them directly
	 **************************************************************************/

	/**
	 * Searches for the given target directory starting in the "plugins"
	 * subdirectory of the given location. If one is found then this location is
	 * returned; otherwise an exception is thrown.
	 * 
	 * @param target
	 * 
	 * @return the location where target directory was found
	 * @param start the location to begin searching
	 */
	protected String searchFor(final String target, String start) {
		FileFilter filter = new FileFilter() {

			public boolean accept(File candidate) {
				return candidate.getName().equals(target) || candidate.getName().startsWith(target + "_"); //$NON-NLS-1$
			}
		};
		File[] candidates = new File(start).listFiles(filter); // $NON-NLS-1$
		if (candidates == null)
			return null;
		String[] arrays = new String[candidates.length];
		for (int i = 0; i < arrays.length; i++) {
			arrays[i] = candidates[i].getName();
		}
		int result = findMax(arrays);
		if (result == -1)
			return null;
		return candidates[result].getAbsolutePath().replace(File.separatorChar, '/')
				+ (candidates[result].isDirectory() ? "/" : ""); //$NON-NLS-1$//$NON-NLS-2$
	}

	protected int findMax(String[] candidates) {
		int result = -1;
		Object maxVersion = null;
		for (int i = 0; i < candidates.length; i++) {
			String name = candidates[i];
			String version = ""; //$NON-NLS-1$ // Note: directory with version suffix is always > than directory
									// without version suffix
			int index = name.indexOf('_');
			if (index != -1)
				version = name.substring(index + 1);
			Object currentVersion = getVersionElements(version);
			if (maxVersion == null) {
				result = i;
				maxVersion = currentVersion;
			} else {
				if (compareVersion((Object[]) maxVersion, (Object[]) currentVersion) < 0) {
					result = i;
					maxVersion = currentVersion;
				}
			}
		}
		return result;
	}

	/**
	 * Compares version strings.
	 * 
	 * @param left
	 * @param right
	 * @return result of comparison, as integer; <code><0</code> if left < right;
	 *         <code>0</code> if left == right; <code>>0</code> if left > right;
	 */
	private int compareVersion(Object[] left, Object[] right) {

		int result = ((Integer) left[0]).compareTo((Integer) right[0]); // compare
		// major
		if (result != 0)
			return result;

		result = ((Integer) left[1]).compareTo((Integer) right[1]); // compare
		// minor
		if (result != 0)
			return result;

		result = ((Integer) left[2]).compareTo((Integer) right[2]); // compare
		// service
		if (result != 0)
			return result;

		return ((String) left[3]).compareTo((String) right[3]); // compare
		// qualifier
	}

	/**
	 * Do a quick parse of version identifier so its elements can be correctly
	 * compared. If we are unable to parse the full version, remaining elements are
	 * initialized with suitable defaults.
	 * 
	 * @param version
	 * @return an array of size 4; first three elements are of type Integer
	 *         (representing major, minor and service) and the fourth element is of
	 *         type String (representing qualifier). Note, that returning anything
	 *         else will cause exceptions in the caller.
	 */
	private Object[] getVersionElements(String version) {
		if (version.endsWith(".jar")) //$NON-NLS-1$
			version = version.substring(0, version.length() - 4);
		Object[] result = { 0, 0, 0, "" }; //$NON-NLS-1$
		StringTokenizer t = new StringTokenizer(version, "."); //$NON-NLS-1$
		String token;
		int i = 0;
		while (t.hasMoreTokens() && i < 4) {
			token = t.nextToken();
			if (i < 3) {
				// major, minor or service ... numeric values
				try {
					result[i++] = new Integer(token);
				} catch (Exception e) {
					// invalid number format - use default numbers (0) for the
					// rest
					break;
				}
			} else {
				// qualifier ... string value
				result[i++] = token;
			}
		}
		return result;
	}

	/**
	 * return the bundle named by symbolic name
	 * 
	 * @param symbolicName the bundle name
	 * @return bundle object
	 */
	Object getBundle(String symbolicName) {
		if (bundleContext == null) {
			return null;
		}
		try {
			Method methodLoadBundle = bundleContext.getClass().getMethod("getBundles", new Class[] {});
			Object objects = methodLoadBundle.invoke(bundleContext, new Object[] {});
			if (objects instanceof Object[]) {
				Object[] bundles = (Object[]) objects;
				for (int i = 0; i < bundles.length; i++) {
					Object bundle = bundles[i];
					Method methodGetSymbolicName = bundle.getClass().getMethod("getSymbolicName", new Class[] {});
					Object name = methodGetSymbolicName.invoke(bundle, new Object[] {});
					if (symbolicName.equals(name)) {
						return bundle;
					}
				}
			}
		} catch (Exception ex) {
			logger.log(Level.WARNING, ex.getMessage());
		}
		return null;
	}

	/**
	 * The ChildFirstURLClassLoader alters regular ClassLoader delegation and will
	 * check the URLs used in its initialization for matching classes before
	 * delegating to it's parent. Sometimes also referred to as a
	 * ParentLastClassLoader
	 */
	static protected class ChildFirstURLClassLoader extends URLClassLoader {

		public ChildFirstURLClassLoader(URL[] urls) {
			super(urls);
		}

		public ChildFirstURLClassLoader(URL[] urls, ClassLoader parent) {
			super(urls, parent);
		}

		public URL getResource(String name) {
			URL resource = findResource(name);
			if (resource == null) {
				ClassLoader parent = getParent();
				if (parent != null)
					resource = parent.getResource(name);
			}
			return resource;
		}

		protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			Class<?> clazz = null;
			synchronized (ChildFirstURLClassLoader.class) {
				clazz = findLoadedClass(name);
				if (clazz == null) {
					try {
						clazz = findClass(name);
					} catch (ClassNotFoundException e) {
					}
				}
			}
			if (clazz == null) {
				ClassLoader parent = getParent();
				if (parent != null)
					clazz = parent.loadClass(name);
				else
					clazz = getSystemClassLoader().loadClass(name);
			}
			if (resolve) {
				resolveClass(clazz);
			}
			return clazz;
		}
	}

	protected void setupSecurityPolicy() throws FrameworkException {
		String eclipseSecurity = (String) properties.get(PROP_ECLIPSE_SECURITY);
		if (eclipseSecurity != null) {
			// setup a policy that grants the launcher and path for the
			// framework AllPermissions.
			// Do not set the security manager, this will be done by the
			// framework itself.
			ProtectionDomain domain = OSGILauncher.class.getProtectionDomain();
			CodeSource source = null;
			if (domain != null)
				source = OSGILauncher.class.getProtectionDomain().getCodeSource();
			if (domain == null || source == null) {
				throw new FrameworkException(
						"Can not automatically set the security manager. Please use a policy file.");
			}
			// get the list of codesource URLs to grant AllPermission to
			URL[] rootURLs = new URL[] { source.getLocation(), osgiFramework };
			// replace the security policy
			Policy eclipsePolicy = new OSGIPolicy(Policy.getPolicy(), rootURLs);
			Policy.setPolicy(eclipsePolicy);
		}
	}

	static public boolean isValidPlatform(PlatformFileContext context) {
		String root = context.getPlatform();
		if (root != null) {
			File plugin = new File(new File(root), "plugins");
			if (plugin.exists() && plugin.isDirectory()) {
				return true;
			}
		}
		return false;
	}
}
