/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Utility class to handle URL.
 * 
 */

public class URIUtilImpl {

	/**
	 * File schema.
	 */

	public static final String FILE_SCHEMA = "file"; //$NON-NLS-1$

	/**
	 * URL with JAR protocol.
	 */

	public static final String JAR_SCHEMA = "jar"; //$NON-NLS-1$

	/**
	 * URL with http protocol.
	 */

	public static final String HTTP_SCHEMA = "http"; //$NON-NLS-1$

	/**
	 * URL with https protocol.
	 */

	public static final String HTTPS_SCHEMA = "https"; //$NON-NLS-1$

	/**
	 * URL with FTP protocol.
	 */

	public static final String FTP_SCHEMA = "ftp"; //$NON-NLS-1$

	/**
	 * URL with mailto protocol
	 */
	public static final String MAIL_SCHEMA = "mailto"; //$NON-NLS-1$

	/**
	 * File with jar extension name.
	 */

	public static final String JAR_EXTENTION = ".jar"; //$NON-NLS-1$

	/**
	 * The default separator for url schema.
	 */

	private static final String URL_SIGNATURE = "://"; //$NON-NLS-1$

	/**
	 * The default separator for uri schema. jndi URL can use this schema.
	 */

	private static final String URI_SIGNATURE = ":/"; //$NON-NLS-1$

	/**
	 * Bundle resource schema.
	 */
	private static final String BUNDLE_RESOURCE_SCHEMA = "bundleresource"; //$NON-NLS-1$

	/**
	 * Bundle entry schema.
	 */
	private static final String BUNDLE_ENTRY_SCHEMA = "bundleentry"; //$NON-NLS-1$

	/**
	 * Returns the URL object of the given string. If the input value is in URL
	 * format, return it. Otherwise, create the corresponding file object then
	 * return the url of the file object.
	 * 
	 * @param filePath the file path
	 * @return the URL object or <code>null</code> if the <code>filePath</code>
	 *         cannot be parsed to the URL.
	 */

	public static URL getURLPresentation(String filePath) {
		// the filePath must be decoded.

		if (filePath == null)
			return null;

		URL url = null;
		int sigPos = filePath.indexOf(URL_SIGNATURE);
		if (sigPos != -1) {
			// if the URL can be created, don't need encoding

			try {
				url = new URL(filePath);
			} catch (MalformedURLException e) {
				return getDiskFileDirectory(filePath, false);
			}

			return url;
		}

		if (filePath.startsWith(FILE_SCHEMA) || filePath.startsWith(JAR_SCHEMA)) {
			URI uri = null;
			try {
				uri = new URI(toUniversalFileFormat(filePath));
				if (uri != null)
					return uri.toURL();
			} catch (URISyntaxException e) {
				// try file protocol
			} catch (MalformedURLException e) {

			}
		}

		return getDiskFileDirectory(filePath, false);
	}

	/**
	 * Formats the file path into the format of unix. Unix file path is compatible
	 * on the windows platforms. If the <code>filePath</code> contains '\'
	 * characters, these characters are replaced by '/'.
	 * 
	 * @param filePath the file path
	 * @return the file path only containing '/'
	 */

	public static String toUniversalFileFormat(String filePath) {
		if (StringUtil.isBlank(filePath))
			return filePath;

		if (filePath.indexOf('\\') == -1)
			return filePath;

		return filePath.replace('\\', '/');
	}

	/**
	 * Checks <code>uri</code> is file path. If <code>uri</code> is an absolute uri
	 * and refers to a file, removes "file://" and returns the file path. If
	 * <code>uri</code> is relative uri and refers to a file, returns the
	 * <code>uri</code>. For other cases, returns null.
	 * <p>
	 * For examples, following uri are supported:
	 * <ul>
	 * <li>file://C:/disk/test/data.file
	 * <li>/C:/disk/test/data.file
	 * <li>/usr/local/disk/test/data.file
	 * <li>C:\\disk\\test/data.file
	 * <li>C:/disk/test/data.file
	 * <li>./test/data.file
	 * </ul>
	 * 
	 * @param uri the input uri
	 * @return the file path if <code>uri</code> refers to a file. Otherwise null.
	 */

	public static String getLocalPath(String uri) {
		if (uri == null)
			return null;

		URI objURI = null;

		try {
			objURI = new URI(uri);
		} catch (URISyntaxException e) {
			return getLocalFileOfFailedURI(uri);
		}

		if (objURI.getScheme() == null) {
			if (isFileProtocol(uri))
				return uri;
		} else if (objURI.getScheme().equalsIgnoreCase(FILE_SCHEMA)) {
			return objURI.getSchemeSpecificPart();
		} else {
			// this is for files on the windows platforms.

			if (objURI.getScheme().length() == 1 || objURI.getScheme().equalsIgnoreCase(JAR_SCHEMA)) {
				return uri;
			}

		}

		return null;
	}

	/**
	 * Checks whether <code>filePath</code> is a valid file on the disk.
	 * <code>filePath</code> can follow these scheme.
	 * <ul>
	 * <li>./../hello/
	 * <li>C:\\hello\..\
	 * <li>/C:/../hello/.
	 * </ul>
	 * 
	 * @param filePath the input filePath
	 * @return true if filePath exists on the disk. Otherwise false.
	 */

	private static boolean isFileProtocol(String filePath) {
		try {
			URL fileUrl = new URL(filePath);
			if (FILE_SCHEMA.equalsIgnoreCase(fileUrl.getProtocol()))
				return true;

			return false;
		} catch (MalformedURLException e) {
			// ignore the error since this string is not in URL format
		}
		File file = new File(filePath);
		String schema = SecurityUtil.getFiletoURISchemaPart(file);
		if (schema == null)
			return false;

		if (schema.equalsIgnoreCase(FILE_SCHEMA)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks whether <code>filePath</code> is a file protocol if it is not a
	 * invalid URI.
	 * <p>
	 * A invalid URI contains excluded US-ASCII characters:
	 * <ul>
	 * <li>contro = <US-ASCII coded characters 00-1F and 7F hexadecimal>
	 * <li>space = <US-ASCII coded character 20 hexadecimal>
	 * <li>delims="<" | ">" | "#" | "%" | <">
	 * <li>unwise="{" | "}" | "|" | "\" | "^" | "[" | "]" | "`"
	 * </ul>
	 * Details are described at the hyperlink: http://www.ietf.org/rfc/rfc2396.txt.
	 * 
	 * @param uri the input uri
	 * @return the file path if <code>uri</code> refers to a file. Otherwise null.
	 */

	private static String getLocalFileOfFailedURI(String uri) {
		if (!isLocalFilePath(uri)) {
			URL objURI = null;
			try {
				objURI = new URL(uri);

				if (objURI.getProtocol().equalsIgnoreCase(FILE_SCHEMA)) {
					return objURI.getAuthority() == null ? objURI.getPath() : objURI.getAuthority() + objURI.getPath();
				} else if (objURI.getProtocol().equalsIgnoreCase(JAR_SCHEMA))
					return uri;
				else
					return null;
			} catch (MalformedURLException e) {
				// Do nothing
			}
		}
		URL url = getFileDirectory(uri, false);

		if (uri.startsWith(JAR_SCHEMA))
			return JAR_SCHEMA + ":" + FILE_SCHEMA + ":" + url.getPath(); //$NON-NLS-1$ //$NON-NLS-2$

		try {
			if (uri.startsWith(FILE_SCHEMA))
				return url.toURI().getSchemeSpecificPart();
		} catch (URISyntaxException e) {
		}

		return uri;
	}

	/**
	 * Checks if the given URI is a local file path. The URI is considered as a
	 * local file path if the protocol's length is 1 and it is a character.
	 * 
	 * @param uri the given URI
	 * 
	 * @return true if the protocol's length is 1 and it is a characte.Otherwise
	 *         false
	 */
	private static boolean isLocalFilePath(String uri) {
		uri = uri.trim();
		int index = uri.indexOf(':');
		if (index == 1 || (index == 2 && uri.startsWith("/"))) {
			return Character.isLetter(uri.charAt(index - 1));
		}
		return false;
	}

	/**
	 * Returns the directory of the given file name in a valid URL. The filename can
	 * include directory information, either relative or absolute directory. And the
	 * file should be on the local disk. The parameter filePath should be decoded.
	 * If the filePath is encoded, it should be converted to URL and call
	 * getDirectory as the parameter.
	 * 
	 * @param filePath the file name
	 * @return a valid URL
	 */

	public static URL getDirectory(String filePath) {
		// the filePath must be decoded.
		if (filePath == null)
			return null;

		URL url = null;

		// jndi is a special case, even the string like jndi:/tests/..., in
		// Tomcat, this string can be constructed with a valid URL

		int sigPos = filePath.indexOf(URI_SIGNATURE);
		if (sigPos != -1) {
			// if the URL can be created, don't need encoding

			try {
				url = new URL(filePath);
			} catch (MalformedURLException e) {
				return getDiskFileDirectory(filePath, true);
			}

			String tmpProtocol = url.getProtocol();

			// to avoid case like C:/abc/test/...

			if (url != null && tmpProtocol != null && tmpProtocol.length() > 1)
				return getDirectoryByURL(url);

			return getDiskFileDirectory(filePath, true);
		}

		String lowerFilePath = filePath.toLowerCase();
		if (lowerFilePath.startsWith(FILE_SCHEMA)) {
			URI uri = null;
			try {
				uri = new URI(toUniversalFileFormat(filePath));
			} catch (URISyntaxException e) {
				// try file protocol
			}

			if (uri != null)
				return getFileDirectory(uri.getSchemeSpecificPart(), true);
		}

		String prefix = null;

		if (lowerFilePath.startsWith(JAR_SCHEMA))
			prefix = JAR_SCHEMA;

		if (prefix != null) {
			URI uri = null;
			try {
				uri = new URI(toUniversalFileFormat(filePath));
				if (uri != null)
					return new URL(prefix + ":" //$NON-NLS-1$
							+ getDirectory(uri.getSchemeSpecificPart()).toExternalForm() + "/"); //$NON-NLS-1$
			} catch (URISyntaxException e) {
				// try file protocol
			} catch (MalformedURLException e) {
				// try file protocol
			}

		}

		return getDiskFileDirectory(filePath, true);
	}

	/**
	 * Returns the directory of the given file name in a valid URL.The filename can
	 * include directory information, either relative or absolute directory. And the
	 * file should be on the local disk. The url has been encoded.
	 * 
	 * @param url the url of the file.
	 * @return a valid URL
	 */

	public static URL getDirectory(URL url) {
		if (url == null)
			return null;

		return getDirectoryByURL(url);
	}

	/**
	 * Gets the directory according to the given uri. The uri contains the encoded
	 * file path.When the methods getFileDirectory and getJarDirectory are called,
	 * encode file path should be decoded. To get the decoded file path, the <URL>
	 * should be converted to <URI> and method getSchemeSpecificPart( ) will be
	 * called.
	 * 
	 * @param uri the uri of the file
	 * @return the validate url
	 */

	private static URL getDirectoryByURL(URL url) {
		if (FILE_SCHEMA.equalsIgnoreCase(url.getProtocol()) || JAR_SCHEMA.equalsIgnoreCase(url.getProtocol())) {
			URI uri = null;
			try {
				uri = url.toURI();
			} catch (URISyntaxException e) {
				return url;
			}

			if (FILE_SCHEMA.equalsIgnoreCase(uri.getScheme()))
				return getFileDirectory(uri.getSchemeSpecificPart(), true);
			else if (JAR_SCHEMA.equalsIgnoreCase(uri.getScheme())
					&& !uri.getSchemeSpecificPart().toLowerCase().startsWith(HTTP_SCHEMA))
				return getJarDirectory(uri.getSchemeSpecificPart(), true);
		}

		// rather then the file protocol

		return getNetDirectory(url);
	}

	/**
	 * Returns the directory of a file path that is a url with network protocols.
	 * Note that <code>filePath</code> should include the file name and file
	 * extension.
	 * 
	 * @param filePath the file url
	 * @return a url for the directory of the file
	 */

	private static URL getNetDirectory(URL uri) {
		URL filePath = uri;

		String path = filePath.getFile();

		// remove the file name

		int index = path.lastIndexOf('/');
		if (index != -1 && index != path.length() - 1)
			path = path.substring(0, index + 1);

		try {
			return new URL(filePath.getProtocol(), filePath.getHost(), filePath.getPort(), path);
		} catch (MalformedURLException e) {
		}

		assert false;
		return null;
	}

	/**
	 * Returns the valid URL for the disk file. The <code>filePath</code> should be
	 * a valid disk file.
	 * 
	 * @param filePath the file path
	 * @return the URL
	 */

	private static URL getDiskFileDirectory(String filePath, boolean returnDirectory) {
		URL url = null;

		if (filePath.indexOf(JAR_EXTENTION) > -1)
			// try jar format
			url = getJarDirectory(filePath, returnDirectory);
		else
			// follows the file protocol
			url = getFileDirectory(filePath, returnDirectory);

		return url;
	}

	/**
	 * Returns the directory of a file path that is a filepath or a url with the
	 * file protocol.
	 * 
	 * @param filePath the file url
	 * @return a url for the directory of the file
	 */

	private static URL getFileDirectory(String filePath, boolean returnDirectory) {

		File file = new File(filePath);

		// get the absolute file in case of filePath is just
		// "newReport.rptdesign".

		file = SecurityUtil.getAbsoluteFile(file);

		// get the parent file when the absolute file is
		// ready.

		if (returnDirectory)
			file = file.getParentFile();

		if (file == null)
			return null;

		try {
			return SecurityUtil.fileToURI(SecurityUtil.getCanonicalFile(file)).toURL();
		} catch (MalformedURLException e) {
			assert false;
		}

		return null;

	}

	/**
	 * Returns the directory of a file path that is a filepath or a url with the
	 * file protocol.
	 * 
	 * @param filePath the file url
	 * @return a url for the directory of the file
	 */

	private static URL getJarDirectory(String filePath, boolean returnDirectory) {
		if (filePath.startsWith(JAR_SCHEMA))
			filePath = filePath.substring(4);

		if (filePath.startsWith(FILE_SCHEMA))
			filePath = filePath.substring(5);

		URL url = getFileDirectory(filePath, returnDirectory);
		if (url != null)
			try {
				return new URL(JAR_SCHEMA + ":" + FILE_SCHEMA + ":" //$NON-NLS-1$ //$NON-NLS-2$
						+ url.getPath() + '/');
			} catch (MalformedURLException e) {
				assert false;
			}

		return null;
	}

	/**
	 * Resolves the absolute path according to the input path string.
	 * 
	 * @param path the path.
	 * @return the <code>URI</code> if the path can be resolved as URI type,
	 *         otherwise return null.
	 */
	public static URI resolveAbsolutePath(String path) {
		try {
			URI uri = new URI(path);
			return uri;
		} catch (URISyntaxException e) {
			return null;
		}
	}

	/**
	 * Checks if the file path is bundleresource or bundleentry protocol.
	 * 
	 * @param filePath the file path.
	 * @return <true> if the the file path is bundleresource or bundleentry
	 *         protocol, else return <false>.
	 */
	public static boolean isBundleProtocol(String filePath) {
		if (filePath == null)
			return false;
		int sigPos = filePath.indexOf(URIUtilImpl.URL_SIGNATURE);
		if (sigPos != -1 && (filePath.startsWith(BUNDLE_RESOURCE_SCHEMA) || filePath.startsWith(BUNDLE_ENTRY_SCHEMA)))
			return true;
		return false;
	}
}
