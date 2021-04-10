/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Collection of file utility.
 * 
 */
public class FileUtil {

	/**
	 * The default prefix for creating temporarily file.
	 */
	private static String DEFAULT_PREFIX = "Res"; //$NON-NLS-1$

	/**
	 * The Log object that <code>FileUtil</code> uses to log the error, debug,
	 * information messages.
	 */
	protected static Logger logger = Logger.getLogger(FileUtil.class.getName());

	/**
	 * The <code>HashMap</code> object that stores image type/file extension
	 * mapping.
	 */
	private static HashMap fileExtension = new HashMap();
	private static HashMap mimeType = new HashMap();

	static {
		// initialize fileExtension
		fileExtension.put("image/bmp", ".bmp"); //$NON-NLS-1$ //$NON-NLS-2$
		fileExtension.put("image/gif", ".gif"); //$NON-NLS-1$//$NON-NLS-2$
		fileExtension.put("image/jpeg", ".jpg"); //$NON-NLS-1$//$NON-NLS-2$
		fileExtension.put("image/pcx", ".pcx"); //$NON-NLS-1$//$NON-NLS-2$
		fileExtension.put("image/png", ".png"); //$NON-NLS-1$//$NON-NLS-2$
		fileExtension.put("image/x-png", ".png"); //$NON-NLS-1$//$NON-NLS-2$
		fileExtension.put("image/tif", ".tif"); //$NON-NLS-1$//$NON-NLS-2$
		fileExtension.put("image/svg+xml", ".svg"); //$NON-NLS-1$//$NON-NLS-2$
		fileExtension.put("image/x-icon", ".ico");//$NON-NLS-1$//$NON-NLS-2$

		// initialize mimeType
		mimeType.put(".bmp", "image/bmp"); //$NON-NLS-1$ //$NON-NLS-2$
		mimeType.put(".gif", "image/gif"); //$NON-NLS-1$//$NON-NLS-2$
		mimeType.put(".jpg", "image/jpeg"); //$NON-NLS-1$//$NON-NLS-2$
		mimeType.put(".pcx", "image/pcx"); //$NON-NLS-1$//$NON-NLS-2$
		mimeType.put(".png", "image/png"); //$NON-NLS-1$//$NON-NLS-2$
		mimeType.put(".png", "image/x-png"); //$NON-NLS-1$//$NON-NLS-2$
		mimeType.put(".tif", "image/tif"); //$NON-NLS-1$//$NON-NLS-2$
		mimeType.put(".svg", "image/svg+xml"); //$NON-NLS-1$//$NON-NLS-2$
		mimeType.put(".ico", "image/x-icon"); //$NON-NLS-1$//$NON-NLS-2$
	}

	/**
	 * Creates a new empty file in the specified directory, using the given prefix
	 * and suffix strings to generate its name.
	 * 
	 * @param prefix The prefix string to be used in generating the file's name; if
	 *               it's less than three characters, we will use DEFAULT_PREFIX
	 *               instead
	 * @param suffix The suffix string to be used in generating the file's name; may
	 *               be null, in which case the suffix ".tmp" will be used
	 * @param path   The directory in which the file is to be created
	 * @return the corresponding File object
	 */
	public static File createTempFile(String prefix, String suffix, String path) {
		assert path != null;

		if (prefix == null || prefix.length() < 3)
			prefix = DEFAULT_PREFIX;

		File dir = new File(path);
		if (!dir.exists()) {
			if (dir.mkdirs() == false) {
				logger.log(Level.SEVERE, "[FileUtil] Cannot create directory."); //$NON-NLS-1$
				return null;
			}
		}

		try {
			File newFile = File.createTempFile(prefix, suffix, dir);
			return newFile;
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Copies the content from a resource specified by URI string to a target file.
	 * 
	 * @param srcUri  The source URI string.
	 * @param tgtFile The target File object.
	 * @return A <code>boolean</code> value indicating if copyFile succeeded or not.
	 */
	public static boolean copyFile(String srcUri, File tgtFile) {
		assert srcUri != null && tgtFile != null;

		FileOutputStream fos = null;
		InputStream is = null;
		try {
			fos = new FileOutputStream(tgtFile);

			URL srcUrl = new URL(srcUri);
			is = srcUrl.openStream();

			byte[] buffer = new byte[1024];
			while (is.read(buffer) > 0) {
				fos.write(buffer);
			}

			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return false;
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, ex.getMessage(), ex);
				}
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, ex.getMessage(), ex);
				}
			}
		}
	}

	/**
	 * Extract the file extension string from the given file name.
	 * 
	 * @param fileName The file name.
	 * @return The file extension string containing the '.' character.
	 */
	public static String getExtFromFileName(String fileName) {
		if (fileName != null) {
			int lastIndex = fileName.lastIndexOf('.');
			if (lastIndex != -1) {
				String extName = fileName.substring(lastIndex);
				if (extName.indexOf('/') == -1 && extName.indexOf('\\') == -1) {
					return extName;
				}
			}
		}
		return null;
	}

	/**
	 * Checks if a given URI string refers to a local resource. If a URI string
	 * starts with "http", we assume it's a global resource.
	 * 
	 * @param uri The URI string value to check.
	 * @return A <code>boolean</code> value indicating if it's a local or global
	 *         resource.
	 */
	public static boolean isLocalResource(String uri) {
		return uri != null && uri.length() > 0 && uri.toLowerCase().startsWith("http") == false; //$NON-NLS-1$
	}

	/**
	 * Generates an absolute file name from the give path and file name.
	 * 
	 * @param path     The path name.
	 * @param fileName The file name.
	 * @return The absolute file path.
	 */
	public static String getAbsolutePath(String path, String fileName) {
		File file = new File(fileName);
		if (!file.exists()) {
			file = new File(path, fileName);
		}

		if (file.exists()) {
			return file.getAbsolutePath();
		}
		return null;
	}

	/**
	 * Verifies whether a file exists and then return its corresponding URI string.
	 * 
	 * @param fileName The file name to verify.
	 * @return The file's URI string, returns <code>null</code> if the file does not
	 *         exist.
	 */
	public static String getURI(String fileName) {
		if (fileName == null || fileName.length() == 0) {
			return null;
		}

		File file = new File(fileName);
		if (file.exists() && file.length() > 0l) {
			return file.toURI().toString();
		}

		return null;
	}

	/**
	 * Saves a byte array to a specified file.
	 * 
	 * @param targetFile The target File object.
	 * @param data       The output byte array.
	 * @return A <code>boolean</code> value indicating if the function succeeded or
	 *         not.
	 */
	public static boolean saveFile(File targetFile, byte[] data) {
		if (targetFile == null) {
			return false;
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(targetFile);
			out.write(data);
			return true;
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			return false;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ex) {
					logger.log(Level.SEVERE, ex.getMessage(), ex);
				}
			}
		}
	}

	/**
	 * Gets the file extension according to the given Image file type.
	 * 
	 * @param fileType The image file type say, "image/jpg".
	 * @return File extension string say, ".jpg".
	 */
	public static String getExtFromType(String fileType) {
		return (String) fileExtension.get(fileType);
	}

	/**
	 * Gets the Image file mime type according to the given file extension.
	 * 
	 * @param fileType The image file type say, ".jpg".
	 * @return File extension string say, "image/jpg".
	 */
	public static String getTypeFromExt(String imgExt) {
		return (String) mimeType.get(imgExt);
	}

	/**
	 * Checks if a given file name contains relative path.
	 * 
	 * @param fileName The file name.
	 * @return A <code>boolean</code> value indicating if the file name contains
	 *         relative path or not.
	 */
	public static boolean isRelativePath(String fileName) {
		if (fileName == null || fileName.indexOf(':') > 0 || fileName.startsWith("\\\\")) //$NON-NLS-1$
		{
			return false;
		}

		if (File.separatorChar == '/') {
			// Linux
			return !fileName.startsWith(File.separator);
		} else if (File.separatorChar == '\\') {
			// Windows
			File file = new File(fileName);
			return !file.isAbsolute();
		}

		return false;
	}

	/**
	 * Gets the file content in the format of byte array
	 * 
	 * @param fileName the file to be read
	 * @return the file content if no error happens, otherwise reutrn <tt>null</tt>
	 */
	public static byte[] getFileContent(String fileName) {
		if (fileName == null) {
			return null;
		}

		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			// Uses the buffered stream to improve the performance.
			in = new BufferedInputStream(new FileInputStream(fileName));
			out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) != -1) {
				out.write(buf, 0, len);
			}
			return out.toByteArray();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Cannot get the content of the file " + fileName, e); //$NON-NLS-1$
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage(), e);
				}
			}
		}
		return null;
	}

	public static void deleteDir(File file) {
		if (file.isDirectory()) {
			String[] list = file.list();
			for (int i = 0; i < list.length; i++) {
				File f = new File(file, list[i]);
				deleteDir(f);
			}
			file.delete();
		}
		file.delete();
	}

	public static String getJavaTmpDir() {
		return AccessController.doPrivileged(new PrivilegedAction<String>() {

			public String run() {
				return System.getProperty("java.io.tmpdir");
			}
		});
	}
}