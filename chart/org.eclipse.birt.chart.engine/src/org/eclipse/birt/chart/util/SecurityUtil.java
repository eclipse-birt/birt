/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLEncoder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.text.MessageFormat;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;

/**
 * Utility class to support application level security policy.
 */

public class SecurityUtil {

	private SecurityUtil() {
		// disable instantiation
	}

	/**
	 * Format a message using MessageFormat.format.
	 * 
	 * @param pattern
	 * @param args
	 * @return
	 */
	public static String formatMessage(final String pattern, final Object... args) {
		String piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return MessageFormat.format(pattern, args);
			}
		});

		return piTmp0;
	}

	/**
	 * Instantiate a new FileInputStream with a file
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileInputStream newFileInputStream(final File file) throws FileNotFoundException {
		FileInputStream piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>() {

				public FileInputStream run() throws FileNotFoundException {
					return new FileInputStream(file);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new FileInputStream
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileInputStream newFileInputStream(final String filename) throws FileNotFoundException {
		FileInputStream piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>() {

				public FileInputStream run() throws FileNotFoundException {
					return new FileInputStream(filename);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new FileOutputStream
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileOutputStream newFileOutputStream(final String filename) throws FileNotFoundException {
		FileOutputStream piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<FileOutputStream>() {

				public FileOutputStream run() throws FileNotFoundException {
					return new FileOutputStream(filename);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new InputStreamReader.
	 * 
	 * @param in
	 * @param charsetName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static InputStreamReader newInputStreamReader(final InputStream in, final String charsetName)
			throws UnsupportedEncodingException {
		InputStreamReader piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<InputStreamReader>() {

				public InputStreamReader run() throws UnsupportedEncodingException {
					return new InputStreamReader(in, charsetName);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof UnsupportedEncodingException) {
				throw (UnsupportedEncodingException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new FileReader with filename.
	 * 
	 * @param filename
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileReader newFileReader(final String filename) throws FileNotFoundException {
		FileReader piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<FileReader>() {

				public FileReader run() throws FileNotFoundException {
					return new FileReader(filename);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new FileReader with a file.
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static FileReader newFileReader(final File file) throws FileNotFoundException {
		FileReader piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<FileReader>() {

				public FileReader run() throws FileNotFoundException {
					return new FileReader(file);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof FileNotFoundException) {
				throw (FileNotFoundException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new FileWriter with filename
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static FileWriter newFileWriter(final String filename) throws IOException {
		FileWriter piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<FileWriter>() {

				public FileWriter run() throws IOException {
					return new FileWriter(filename);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new FileWriter with a file
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static FileWriter newFileWriter(final File file) throws IOException {
		FileWriter piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<FileWriter>() {

				public FileWriter run() throws IOException {
					return new FileWriter(file);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * 
	 * @param out
	 * @param charsetName
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static OutputStreamWriter newOutputStreamWriter(final OutputStream out, final String charsetName)
			throws UnsupportedEncodingException {
		OutputStreamWriter piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<OutputStreamWriter>() {

				public OutputStreamWriter run() throws UnsupportedEncodingException {
					return new OutputStreamWriter(out, charsetName);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof UnsupportedEncodingException) {
				throw (UnsupportedEncodingException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Read an object from an ObjectInputStream.
	 * 
	 * @param ois
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
		Object piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {

				public Object run() throws IOException, ClassNotFoundException {
					return ois.readObject();
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
			if (typedException instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new ObjectOutputStream
	 * 
	 * @param out
	 * @return
	 * @throws IOException
	 */
	public static ObjectOutputStream newObjectOutputStream(final OutputStream out) throws IOException {
		ObjectOutputStream piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectOutputStream>() {

				public ObjectOutputStream run() throws IOException {
					return new ObjectOutputStream(out);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new ObjectInputStream.
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static ObjectInputStream newObjectInputStream(final InputStream is) throws IOException {
		ObjectInputStream piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<ObjectInputStream>() {

				public ObjectInputStream run() throws IOException {
					return new ObjectInputStream(is);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new ImageOutputStream.
	 * 
	 * @param output
	 * @return
	 * @throws IOException
	 */
	public static ImageOutputStream newImageOutputStream(final Object output) throws IOException {
		ImageOutputStream piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<ImageOutputStream>() {

				public ImageOutputStream run() throws IOException {
					return ImageIO.createImageOutputStream(output);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new Hashtable
	 * 
	 * @param <K>
	 * @param <V>
	 * @return
	 */
	public static <K, V> Hashtable<K, V> newHashtable() {
		Hashtable<K, V> piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<Hashtable<K, V>>() {

			public Hashtable<K, V> run() {
				return new Hashtable<K, V>();
			}
		});

		return piTmp0;
	}

	/**
	 * Returns as ClassLoader of a class
	 * 
	 * @param cls
	 * @return
	 */
	public static ClassLoader getClassLoader(final Class<?> cls) {
		ClassLoader piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() {

			public ClassLoader run() {
				return cls.getClassLoader();
			}
		});

		return piTmp0;
	}

	/**
	 * Instantiate a new URLClassLoader.
	 * 
	 * @param urls
	 * @param parent
	 * @return
	 */
	public static URLClassLoader newURLClassLoader(final URL[] urls, final ClassLoader parent) {
		URLClassLoader piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<URLClassLoader>() {

			public URLClassLoader run() {
				return new URLClassLoader(urls, parent);// $NON-SEC-2
			}
		});

		return piTmp0;
	}

	/**
	 * Instantiate a class
	 * 
	 * @param <T>
	 * @param cls
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T newClassInstance(Class<T> cls) throws InstantiationException, IllegalAccessException {
		return cls.newInstance();
	}

	/**
	 * Load a class.
	 * 
	 * @param name
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(final ClassLoader loader, final String name) throws ClassNotFoundException {
		Class<?> piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {

				public Class<?> run() throws ClassNotFoundException {
					return loader.loadClass(name);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * 
	 * @return
	 * @throws SecurityException
	 */
	public static Method[] getMethods(final Class<?> cls) throws SecurityException {
		Method[] piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<Method[]>() {

				public Method[] run() throws SecurityException {
					return cls.getMethods();
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof SecurityException) {
				throw (SecurityException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Invoke a method
	 * 
	 * @param method
	 * @param caller
	 * @param args
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(final Method method, final Object caller, final Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() {

				public Object run() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
					return method.invoke(caller, args);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IllegalAccessException) {
				throw (IllegalAccessException) typedException;
			}
			if (typedException instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) typedException;
			}
			if (typedException instanceof InvocationTargetException) {
				throw (InvocationTargetException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Retrieve constructor of a class with the list of parameter types
	 * 
	 * @param <T>
	 * @param cls
	 * @param parameterTypes
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static <T> Constructor<T> getConstructor(final Class<T> cls, final Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		Constructor<T> piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<Constructor<T>>() {

				public Constructor<T> run() throws NoSuchMethodException, SecurityException {
					return cls.getConstructor(parameterTypes);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof NoSuchMethodException) {
				throw (NoSuchMethodException) typedException;
			}
			if (typedException instanceof SecurityException) {
				throw (SecurityException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Retrieve a system property
	 * 
	 * @param key
	 * @return
	 */
	public static String getSysProp(final String key) {
		String piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return System.getProperty(key);
			}
		});

		return piTmp0;
	}

	/**
	 * Set a system property
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String setSysProp(final String key, final String value) {
		String piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return System.setProperty(key, value);
			}
		});

		return piTmp0;
	}

	/**
	 * Calls System.exit
	 * 
	 * @param status
	 */
	public static void sysExit(final int status) {
		AccessController.doPrivileged(new PrivilegedAction<Object>() {

			public Object run() {
				System.exit(status);
				return null;
			}
		});

	}

	/**
	 * Instantiate a new URL
	 * 
	 * @param spec
	 * @return
	 * @throws MalformedURLException
	 */
	public static URL newURL(final String spec) throws MalformedURLException {
		URL piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<URL>() {

				public URL run() throws MalformedURLException {
					return new URL(spec);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof MalformedURLException) {
				throw (MalformedURLException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Executes the specified string command in a separate process.
	 * 
	 * @param runtime
	 * @param command
	 * @return
	 * @throws IOException
	 */
	public static Process execRuntimeCommand(final Runtime runtime, final String command) throws IOException {
		Process piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<Process>() {

				public Process run() throws IOException {
					return runtime.exec(command);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new TransformerFactory.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static TransformerFactory newTransformerFactory() throws Exception {
		return AccessController.doPrivileged(new PrivilegedExceptionAction<TransformerFactory>() {

			public TransformerFactory run() throws Exception {
				try {
					return TransformerFactory.newInstance();
				} catch (TransformerFactoryConfigurationError error) {
					throw error.getException();
				}
			}
		});
	}

	/**
	 * Instantiate a new DocumentBuilderFactory.
	 * 
	 * @return
	 */
	public static DocumentBuilderFactory newDocumentBuilderFactory() {

		DocumentBuilderFactory piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<DocumentBuilderFactory>() {

			public DocumentBuilderFactory run() {
				return DocumentBuilderFactory.newInstance();
			}
		});

		return piTmp0;
	}

	/**
	 * Constructs a URL from an URI.
	 * 
	 * @return
	 */
	public static URL toURL(final URI uri) throws MalformedURLException {
		URL piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<URL>() {

				public URL run() throws MalformedURLException {
					return uri.toURL();
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof MalformedURLException) {
				throw (MalformedURLException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new File with uri.
	 * 
	 * @param uri
	 * @return
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static File newFile(final URI uri) throws NullPointerException, IllegalArgumentException {
		File piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<File>() {

				public File run() throws NullPointerException, IllegalArgumentException {
					return new File(uri);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof NullPointerException) {
				throw (NullPointerException) typedException;
			} else if (typedException instanceof IllegalArgumentException) {
				throw (IllegalArgumentException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Wrapper of URLEncoder.encode.
	 * 
	 * @param s
	 * @param enc
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String urlEncode(final String s, final String enc) throws UnsupportedEncodingException {
		String piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<String>() {

				public String run() throws UnsupportedEncodingException {
					return URLEncoder.encode(s, enc);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof UnsupportedEncodingException) {
				throw (UnsupportedEncodingException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new PrintWriter.
	 * 
	 * @param out
	 * @param autoFlush
	 * @return
	 */
	public static PrintWriter newPrintWriter(final Writer out, final boolean autoFlush) {
		PrintWriter piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<PrintWriter>() {

			public PrintWriter run() {
				return new PrintWriter(out, autoFlush);
			}
		});

		return piTmp0;
	}

	/**
	 * Instantiate a new ImageOutputStream.
	 * 
	 * @param output
	 * @return
	 * @throws IOException
	 */
	public static ImageOutputStream createImageOutputStream(final Object output) throws IOException {
		ImageOutputStream piTmp0 = null;
		try {
			piTmp0 = AccessController.doPrivileged(new PrivilegedExceptionAction<ImageOutputStream>() {

				public ImageOutputStream run() throws IOException {
					return ImageIO.createImageOutputStream(output);
				}
			});
		} catch (PrivilegedActionException e) {
			Exception typedException = e.getException();
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Get the value of a system environment variable.
	 * 
	 * @param name
	 * @return the value
	 */
	public static String getSystemEnv(final String name) {
		String piTmp0 = null;
		piTmp0 = AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return System.getenv(name);
			}
		});

		return piTmp0;
	}

}
