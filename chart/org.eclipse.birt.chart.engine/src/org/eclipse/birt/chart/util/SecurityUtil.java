/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
	 * @return format message
	 */
	public static String formatMessage(final String pattern, final Object... args) {
		String piTmp0;
		piTmp0 = MessageFormat.format(pattern, args);

		return piTmp0;
	}

	/**
	 * Instantiate a new FileInputStream with a file
	 *
	 * @param file
	 * @return Return the new file input stream
	 * @throws FileNotFoundException
	 */
	public static FileInputStream newFileInputStream(final File file) throws FileNotFoundException {
		FileInputStream piTmp0 = null;
		try {
			piTmp0 = new FileInputStream(file);
		} catch (Exception typedException) {
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
	 * @return Return the new file input stream
	 * @throws FileNotFoundException
	 */
	public static FileInputStream newFileInputStream(final String filename) throws FileNotFoundException {
		FileInputStream piTmp0 = null;
		try {
			piTmp0 = new FileInputStream(filename);
		} catch (Exception typedException) {
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
	 * @return Return the new file output stream
	 * @throws FileNotFoundException
	 */
	public static FileOutputStream newFileOutputStream(final String filename) throws FileNotFoundException {
		FileOutputStream piTmp0 = null;
		try {
			piTmp0 = new FileOutputStream(filename);
		} catch (Exception typedException) {
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
	 * @return Return the new input stream reader
	 * @throws UnsupportedEncodingException
	 */
	public static InputStreamReader newInputStreamReader(final InputStream in, final String charsetName)
			throws UnsupportedEncodingException {
		InputStreamReader piTmp0 = null;
		try {
			piTmp0 = new InputStreamReader(in, charsetName);
		} catch (Exception typedException) {
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
	 * @return Return the new file reader
	 * @throws FileNotFoundException
	 */
	public static FileReader newFileReader(final String filename) throws FileNotFoundException {
		FileReader piTmp0 = null;
		try {
			piTmp0 = new FileReader(filename);
		} catch (Exception typedException) {
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
	 * @return Return the new file reader
	 * @throws FileNotFoundException
	 */
	public static FileReader newFileReader(final File file) throws FileNotFoundException {
		FileReader piTmp0 = null;
		try {
			piTmp0 = new FileReader(file);
		} catch (Exception typedException) {
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
	 * @return Return a new file writer
	 * @throws IOException
	 */
	public static FileWriter newFileWriter(final String filename) throws IOException {
		FileWriter piTmp0 = null;
		try {
			piTmp0 = new FileWriter(filename);
		} catch (Exception typedException) {
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new FileWriter with a file
	 *
	 * @param file of the file writer
	 * @return Return the new file writer
	 * @throws IOException
	 */
	public static FileWriter newFileWriter(final File file) throws IOException {
		FileWriter piTmp0 = null;
		try {
			piTmp0 = new FileWriter(file);
		} catch (Exception typedException) {
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new OutputStreamWriter
	 *
	 * @param out
	 * @param charsetName
	 * @return Return the new output stream writer
	 * @throws UnsupportedEncodingException
	 */
	public static OutputStreamWriter newOutputStreamWriter(final OutputStream out, final String charsetName)
			throws UnsupportedEncodingException {
		OutputStreamWriter piTmp0 = null;
		try {
			piTmp0 = new OutputStreamWriter(out, charsetName);
		} catch (Exception typedException) {
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
	 * @return Return an object from an object input stream
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object readObject(final ObjectInputStream ois) throws IOException, ClassNotFoundException {
		Object piTmp0 = null;
		try {
			piTmp0 = ois.readObject();
		} catch (Exception typedException) {
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
	 * @return Return a new object from object output stream
	 * @throws IOException
	 */
	public static ObjectOutputStream newObjectOutputStream(final OutputStream out) throws IOException {
		ObjectOutputStream piTmp0 = null;
		try {
			piTmp0 = new ObjectOutputStream(out);
		} catch (Exception typedException) {
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
	 * @return Return an object input stream
	 * @throws IOException
	 */
	public static ObjectInputStream newObjectInputStream(final InputStream is) throws IOException {
		ObjectInputStream piTmp0 = null;
		try {
			piTmp0 = new ObjectInputStream(is);
		} catch (Exception typedException) {
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
	 * @return Return the new image output stream
	 * @throws IOException
	 */
	public static ImageOutputStream newImageOutputStream(final Object output) throws IOException {
		ImageOutputStream piTmp0 = null;
		try {
			piTmp0 = ImageIO.createImageOutputStream(output);
		} catch (Exception typedException) {
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
	 * @return Return a Hashtable
	 */
	public static <K, V> Hashtable<K, V> newHashtable() {
		Hashtable<K, V> piTmp0;
		piTmp0 = new Hashtable<>();

		return piTmp0;
	}

	/**
	 * Returns as ClassLoader of a class
	 *
	 * @param cls
	 * @return Return a class loader
	 */
	public static ClassLoader getClassLoader(final Class<?> cls) {
		ClassLoader piTmp0;
		piTmp0 = cls.getClassLoader();

		return piTmp0;
	}

	/**
	 * Instantiate a new URLClassLoader.
	 *
	 * @param urls
	 * @param parent
	 * @return Return the URL class loader
	 */
	public static URLClassLoader newURLClassLoader(final URL[] urls, final ClassLoader parent) {
		URLClassLoader piTmp0;
		piTmp0 = new URLClassLoader(urls, parent);// $NON-SEC-2

		return piTmp0;
	}

	/**
	 * Instantiate a class
	 *
	 * @param <T>
	 * @param cls
	 * @return Return a new class instance
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T newClassInstance(Class<T> cls) throws InstantiationException, IllegalAccessException {
		return cls.newInstance();
	}

	/**
	 * Load a class.
	 *
	 * @param loader
	 * @param name
	 * @return Return the loaded class
	 * @throws ClassNotFoundException
	 */
	public static Class<?> loadClass(final ClassLoader loader, final String name) throws ClassNotFoundException {
		Class<?> piTmp0 = null;
		try {
			piTmp0 = loader.loadClass(name);
		} catch (Exception typedException) {
			if (typedException instanceof ClassNotFoundException) {
				throw (ClassNotFoundException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Get methods
	 *
	 * @param cls class
	 * @return Return the methods
	 * @throws SecurityException
	 */
	public static Method[] getMethods(final Class<?> cls) throws SecurityException {
		Method[] piTmp0 = null;
		try {
			piTmp0 = cls.getMethods();
		} catch (Exception typedException) {
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
	 * @return Return the object
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Object invokeMethod(final Method method, final Object caller, final Object... args)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Object piTmp0 = null;
		try {
			piTmp0 = method.invoke(caller, args);
		} catch (Exception typedException) {
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
	 * @return Return the constructor
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static <T> Constructor<T> getConstructor(final Class<T> cls, final Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		Constructor<T> piTmp0 = null;
		try {
			piTmp0 = cls.getConstructor(parameterTypes);
		} catch (Exception typedException) {
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
	 * @return Return the property keys
	 */
	public static String getSysProp(final String key) {
		String piTmp0;
		piTmp0 = System.getProperty(key);

		return piTmp0;
	}

	/**
	 * Set a system property
	 *
	 * @param key
	 * @param value
	 * @return Return the set of system properties
	 */
	public static String setSysProp(final String key, final String value) {
		String piTmp0;
		piTmp0 = System.setProperty(key, value);

		return piTmp0;
	}

	/**
	 * Calls System.exit
	 *
	 * @param status
	 */
	public static void sysExit(final int status) {
		System.exit(status);
	}

	/**
	 * Instantiate a new URL
	 *
	 * @param spec
	 * @return Return a new URL
	 * @throws MalformedURLException
	 */
	public static URL newURL(final String spec) throws MalformedURLException {
		URL piTmp0 = null;
		try {
			piTmp0 = new URL(spec);
		} catch (Exception typedException) {
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
	 * @return Return the process of the executed command
	 * @throws IOException
	 */
	public static Process execRuntimeCommand(final Runtime runtime, final String command) throws IOException {
		Process piTmp0 = null;
		try {
			piTmp0 = runtime.exec(command);
		} catch (Exception typedException) {
			if (typedException instanceof IOException) {
				throw (IOException) typedException;
			}
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new TransformerFactory.
	 *
	 * @return Return a new transformer factory
	 * @throws Exception
	 */
	public static TransformerFactory newTransformerFactory() throws Exception {
		TransformerFactory piTmp0 = null;

		try {
			piTmp0 = TransformerFactory.newInstance();
		} catch (TransformerFactoryConfigurationError error) {
			throw error.getException();
		}

		return piTmp0;
	}

	/**
	 * Instantiate a new DocumentBuilderFactory.
	 *
	 * @return Return the new document builder factory
	 */
	public static DocumentBuilderFactory newDocumentBuilderFactory() {

		DocumentBuilderFactory piTmp0;
		piTmp0 = DocumentBuilderFactory.newInstance();

		return piTmp0;
	}

	/**
	 * Constructs a URL from an URI.
	 *
	 * @param uri
	 * @return Return a new URL
	 * @throws MalformedURLException
	 */
	public static URL toURL(final URI uri) throws MalformedURLException {
		URL piTmp0 = null;
		try {
			piTmp0 = uri.toURL();
		} catch (Exception typedException) {
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
	 * @return Return the file URI based
	 * @throws NullPointerException
	 * @throws IllegalArgumentException
	 */
	public static File newFile(final URI uri) throws NullPointerException, IllegalArgumentException {
		File piTmp0 = null;
		try {
			piTmp0 = new File(uri);
		} catch (Exception typedException) {
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
	 * @return Return the encoded URL
	 * @throws UnsupportedEncodingException
	 */
	public static String urlEncode(final String s, final String enc) throws UnsupportedEncodingException {
		String piTmp0 = null;
		try {
			piTmp0 = URLEncoder.encode(s, enc);
		} catch (Exception typedException) {
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
	 * @return Return the print writter
	 */
	public static PrintWriter newPrintWriter(final Writer out, final boolean autoFlush) {
		PrintWriter piTmp0;
		piTmp0 = new PrintWriter(out, autoFlush);

		return piTmp0;
	}

	/**
	 * Instantiate a new ImageOutputStream.
	 *
	 * @param output
	 * @return Return the image output stream
	 * @throws IOException
	 */
	public static ImageOutputStream createImageOutputStream(final Object output) throws IOException {
		ImageOutputStream piTmp0 = null;
		try {
			piTmp0 = ImageIO.createImageOutputStream(output);
		} catch (Exception typedException) {
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
		String piTmp0;
		piTmp0 = System.getenv(name);

		return piTmp0;
	}

}
