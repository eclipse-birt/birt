/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.framework;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

/**
 * A URL class loader with close API.
 * 
 * Java's URL class loader locks the JAR file it loaded until the JVM exit. It
 * may cause some problem when we need remove the JARs if the class loader are
 * not used any more.
 * 
 * BIRT's URL class loader add a new close() method to close the JAR files
 * explicitly. Once the close() is called, the user can't use the class and the
 * loaded classes any more.
 * 
 */
public class URLClassLoader extends java.net.URLClassLoader {

	private static Logger logger = Logger.getLogger(URLClassLoader.class.getName());

	private List<URL> urls = new LinkedList<URL>();
	private ArrayList<Loader> loaders;
	private AccessControlContext acc;

	public URLClassLoader(URL[] urls) {
		super(new URL[] {});

		initURLs(urls);

		loaders = new ArrayList<Loader>(urls.length);
		for (int i = 0; i < urls.length; i++) {
			Loader loader = createLoader(urls[i]);
			if (loader != null) {
				loaders.add(loader);
			}
		}
		acc = AccessController.getContext();
	}

	private void initURLs(URL[] urls) {
		for (URL url : urls) {
			this.urls.add(url);
		}
	}

	public URLClassLoader(URL[] urls, ClassLoader parent) {
		super(new URL[] {}, parent);

		initURLs(urls);

		loaders = new ArrayList<Loader>(urls.length);
		for (int i = 0; i < urls.length; i++) {
			Loader loader = createLoader(urls[i]);
			if (loader != null) {
				loaders.add(loader);
			}
		}
		acc = AccessController.getContext();
	}

	public void close() {
		if (loaders != null) {
			for (Loader loader : loaders) {
				try {
					loader.close();
				} catch (IOException ex) {
				}
			}
			loaders = null;
		}
	}

	public void addURL(URL url) {
		if (url == null || this.urls.contains(url))
			return;
		this.urls.add(url);
		Loader loader = createLoader(url);
		if (loader != null) {
			loaders.add(loader);
		}
	}

	public URL[] getURLs() {
		return this.urls.toArray(new URL[0]);
	}

	protected Class<?> findClass(final String name) throws ClassNotFoundException {
		try {
			return (Class<?>) AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {

				public Class<?> run() throws ClassNotFoundException {
					return findClass1(name);
				}
			}, acc);
		} catch (java.security.PrivilegedActionException pae) {
			throw (ClassNotFoundException) pae.getException();
		}
	}

	protected Class<?> findClass1(String name) throws ClassNotFoundException {
		if (loaders == null) {
			throw new ClassNotFoundException(name);
		}
		String path = name.replace('.', '/').concat(".class");
		try {
			Resource res = loadResource(path);
			if (res != null) {
				CodeSource codeSource = res.getCodeSource();
				definePackage(name, res);
				byte[] b = res.getBytes();
				return defineClass(name, b, 0, b.length, codeSource);
			}
		} catch (IOException e) {
			throw new ClassNotFoundException(name, e);
		}
		throw new ClassNotFoundException(name);
	}

	protected void definePackage(String className, Resource resource) {
		int pos = className.lastIndexOf('.');
		if (pos == -1) {
			// no package name
			return;
		}
		String packageName = className.substring(0, pos);
		Package pkg = getPackage(packageName);
		if (pkg != null) {
			// package has been defined
			return;
		}
		try {
			Manifest manifest = resource.getManifest();
			if (manifest == null) {
				definePackage(packageName, null, null, null, null, null, null, null);
			} else {
				CodeSource codeSource = resource.getCodeSource();
				URL codeBase = codeSource == null ? null : codeSource.getLocation();
				definePackage(packageName, manifest, codeBase);
			}
		} catch (IllegalArgumentException e) {
		}
	}

	public URL findResource(final String name) {
		return AccessController.doPrivileged(new PrivilegedAction<URL>() {

			public URL run() {
				return findResource1(name);
			}
		}, acc);
	}

	protected URL findResource1(String name) {
		if (loaders != null) {
			for (Loader loader : loaders) {
				try {
					URL url = loader.findResource(name);
					if (url != null) {
						return url;
					}
				} catch (IOException ex) {
				}
			}
		}
		return null;
	}

	public Enumeration<URL> findResources(final String name) {
		return AccessController.doPrivileged(new PrivilegedAction<Enumeration<URL>>() {

			public Enumeration<URL> run() {
				return findResources1(name);
			}
		}, acc);
	}

	protected Enumeration<URL> findResources1(String name) {
		Vector<URL> urls = new Vector<URL>();
		if (loaders != null) {
			for (Loader loader : loaders) {
				try {
					URL url = loader.findResource(name);
					if (url != null) {
						urls.add(url);
					}
				} catch (IOException ex) {

				}
			}
		}
		return urls.elements();
	}

	private Resource loadResource(final String name) throws IOException {
		for (Loader loader : loaders) {
			Resource resource = loader.loadResource(name);
			if (resource != null) {
				return resource;
			}
		}
		return null;
	}

	abstract static class Resource {

		abstract CodeSource getCodeSource();

		abstract Manifest getManifest();

		abstract byte[] getBytes() throws IOException;
	}

	static abstract class Loader {

		abstract URL findResource(String name) throws IOException;

		abstract Resource loadResource(String name) throws IOException;

		abstract void close() throws IOException;
	}

	static class UrlLoader extends Loader {

		CodeSource codeSource;
		URL baseUrl;

		UrlLoader(URL url) {
			baseUrl = url;
			codeSource = new CodeSource(url, (CodeSigner[]) null);
		}

		void close() throws IOException {
		}

		URL findResource(String name) throws IOException {
			URL url = new URL(baseUrl, name);
			URLConnection conn = url.openConnection();
			if (conn instanceof HttpURLConnection) {
				HttpURLConnection hconn = (HttpURLConnection) conn;
				hconn.setRequestMethod("HEAD");
				if (hconn.getResponseCode() >= HttpURLConnection.HTTP_BAD_REQUEST) {
					return null;
				}
			} else {
				// our best guess for the other cases
				InputStream is = url.openStream();
				is.close();
			}
			return url;
		}

		Resource loadResource(String name) throws IOException {
			URL url = new URL(baseUrl, name);
			InputStream in = url.openStream();
			try {
				final byte[] bytes = loadStream(in);
				return new Resource() {

					byte[] getBytes() {
						return bytes;
					};

					CodeSource getCodeSource() {
						return codeSource;
					}

					Manifest getManifest() {
						return null;
					}
				};
			} finally {
				in.close();
			}
		}
	}

	static class JarLoader extends Loader {

		URL baseUrl;
		URL jarUrl;
		JarFile jarFile;
		Manifest jarManifest;

		JarLoader(URL url) throws IOException {
			baseUrl = url;
			jarUrl = new URL("jar", "", -1, baseUrl + "!/");
			if (baseUrl.getProtocol().equalsIgnoreCase("file")) {
				String filePath = getFilePath(baseUrl);
				jarFile = new JarFile(filePath);
			} else {
				JarURLConnection jarConn = (JarURLConnection) jarUrl.openConnection();
				jarFile = jarConn.getJarFile();
			}
			jarManifest = jarFile.getManifest();
		}

		public void close() throws IOException {
			if (jarFile != null) {
				jarFile.close();
				jarFile = null;
			}
		}

		URL findResource(String name) throws IOException {
			if (jarFile != null) {
				ZipEntry entry = jarFile.getEntry(name);
				if (entry != null) {
					return new URL(jarUrl, name, new JarEntryHandler(entry));
				}
			}
			return null;
		}

		Resource loadResource(String name) throws IOException {
			// first test if the jar file exist
			if (jarFile != null) {
				final JarEntry entry = jarFile.getJarEntry(name);
				if (entry != null) {
					InputStream in = jarFile.getInputStream(entry);
					try {
						final byte[] bytes = loadStream(in);
						return new Resource() {

							byte[] getBytes() {
								return bytes;
							};

							CodeSource getCodeSource() {
								return new CodeSource(baseUrl, entry.getCodeSigners());
							}

							Manifest getManifest() {
								return jarManifest;
							}
						};
					} finally {
						in.close();
					}
				}
			}
			return null;
		}

		private class JarEntryHandler extends URLStreamHandler {

			private ZipEntry entry;

			JarEntryHandler(ZipEntry entry) {
				this.entry = entry;
			}

			protected URLConnection openConnection(URL u) throws IOException {
				return new URLConnection(u) {

					public void connect() throws IOException {
					}

					public int getContentLength() {
						return (int) entry.getSize();
					}

					public InputStream getInputStream() throws IOException {
						if (jarFile != null) {
							return jarFile.getInputStream(entry);
						}
						throw new IOException("ClassLoader has been closed");
					}
				};
			}
		}
	}

	static class FileLoader extends Loader {

		URL baseUrl;
		File baseDir;
		CodeSource codeSource;

		FileLoader(URL url) {
			baseUrl = url;
			baseDir = new File(getFilePath(url));
			codeSource = new CodeSource(baseUrl, (CodeSigner[]) null);
		}

		void close() throws IOException {

		}

		URL findResource(String name) throws IOException {
			File file = new File(baseDir, name.replace('/', File.separatorChar));
			if (file.exists() && file.isFile()) {
				return file.toURI().toURL();
			}
			return null;
		}

		Resource loadResource(String name) throws IOException {
			File file = new File(baseDir, name.replace('/', File.separatorChar));
			if (file.exists()) {
				FileInputStream in = new FileInputStream(file);
				try {
					final byte[] bytes = loadStream(in);
					return new Resource() {

						public byte[] getBytes() {
							return bytes;
						};

						CodeSource getCodeSource() {
							return codeSource;
						}

						Manifest getManifest() {
							return null;
						}
					};
				} finally {
					in.close();
				}
			}
			return null;
		}
	}

	static Loader createLoader(URL url) {
		try {

			String file = url.getFile();
			if (file != null && file.endsWith("/")) {
				if ("file".equals(url.getProtocol())) {
					return new FileLoader(url);
				}
				return new UrlLoader(url);
			}
			return new JarLoader(url);
		} catch (IOException ex) {
			logger.log(Level.WARNING, "can not load the class from " + url, ex);
			return null;
		}
	}

	static byte[] loadStream(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream(in.available());
		byte[] bytes = new byte[1024];
		int readSize = in.read(bytes);
		while (readSize != -1) {
			out.write(bytes, 0, readSize);
			readSize = in.read(bytes);
		}
		return out.toByteArray();
	}

	private static String getFilePath(URL url) {
		String path = url.getFile();
		return decode(path);
	}

	public static String decode(String s) {

		boolean changed = false;
		int length = s.length();
		StringBuffer buffer = new StringBuffer();

		int i = 0;
		char c;
		byte[] bytes = null;
		while (i < length) {
			c = s.charAt(i);
			if (c == '%') {
				try {

					if (bytes == null)
						bytes = new byte[(length - i) / 3];
					int pos = 0;

					while (((i + 2) < length) && (c == '%')) {
						bytes[pos++] = (byte) Integer.parseInt(s.substring(i + 1, i + 3), 16);
						i += 3;
						if (i < length)
							c = s.charAt(i);
					}

					if ((i < length) && (c == '%'))
						throw new IllegalArgumentException("Incorrect escape pattern.");
					buffer.append(new String(bytes, 0, pos, "utf-8"));
				} catch (NumberFormatException e) {
					throw new IllegalArgumentException("Illegal hex numbers in escape pattern." + e.getMessage());
				} catch (UnsupportedEncodingException e) {
					// Unachievable
				}
				changed = true;
			} else {
				buffer.append(c);
				i++;
			}
		}
		return (changed ? buffer.toString() : s);
	}

}