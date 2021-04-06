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

package org.eclipse.birt.report.model.api.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import org.eclipse.birt.report.model.util.SecurityUtil;
import org.eclipse.birt.report.model.util.URIUtilImpl;

/**
 * Utility class to handle URI.
 */

public class URIUtil {

	/**
	 * File schema.
	 */

	public static final String FILE_SCHEMA = "file"; //$NON-NLS-1$

	/**
	 * The defautl separator for url schema.
	 */

	private static final String URL_SEPARATOR = "/"; //$NON-NLS-1$

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
		return URIUtilImpl.getLocalPath(uri);
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
		return URIUtilImpl.getLocalPath(filePath) != null;
	}

	/**
	 * Converts a filename to a valid URL string. The filename can include directory
	 * information, either relative or absolute directory.
	 * 
	 * @param filePath the file name
	 * @return a valid URL String
	 */

	public static String convertFileNameToURLString(String filePath) {
		StringBuffer buffer = new StringBuffer();
		String path = filePath;

		// copy, converting URL special characters as we go

		for (int i = 0; i < path.length(); i++) {
			char c = path.charAt(i);
			if (c < 0x1F || c == 0x7f)
				buffer.append("%" + Character.toString(c)); //$NON-NLS-1$
			else if (c == '#')
				buffer.append("%23"); //$NON-NLS-1$
			else if (c == '%')
				buffer.append("%25"); //$NON-NLS-1$
			else if (c == '<')
				buffer.append("%3C"); //$NON-NLS-1$
			else if (c == '>')
				buffer.append("%3E"); //$NON-NLS-1$
			else if (c == '"')
				buffer.append("%22"); //$NON-NLS-1$
			else if (c == ' ')
				buffer.append("%20"); //$NON-NLS-1$
			else if (c == '%')
				buffer.append("%25"); //$NON-NLS-1$
			else if (c == '^')
				buffer.append("%5E"); //$NON-NLS-1$
			else if (c == '`')
				buffer.append("%60"); //$NON-NLS-1$
			else if (c == '[')
				buffer.append("%5B"); //$NON-NLS-1$
			else if (c == ']')
				buffer.append("%5D"); //$NON-NLS-1$
			else if (c == '{')
				buffer.append("%7B"); //$NON-NLS-1$
			else if (c == '}')
				buffer.append("%7D"); //$NON-NLS-1$

			// change the '\' to '/' if applicable

			else if (c == '\\')
				buffer.append("/"); //$NON-NLS-1$
			else
				buffer.append(c);
		}

		// return URL

		return buffer.toString();
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
	 * 
	 * @deprecated not supported
	 */

	public static URL getDirectory(String filePath) {
		return URIUtilImpl.getDirectory(filePath);
	}

	/**
	 * Returns the directory of the given file name in a valid URL.The filename can
	 * include directory information, either relative or absolute directory. And the
	 * file should be on the local disk. The url has been encoded.
	 * 
	 * @param url the url of the file.
	 * @return a valid URL
	 * 
	 * @deprecated not supported
	 */

	public static URL getDirectory(URL url) {
		return URIUtilImpl.getDirectory(url);
	}

	/**
	 * Return the relative path for the given <code>resource</code> according to
	 * <code>base</code>. Only handle file system and valid url syntax.
	 * <p>
	 * The <code>base</code> value should be directory ONLY and does NOT contain
	 * file name and the format can be:
	 * <ul>
	 * <li>./../hello/
	 * <li>C:\\hello\..\
	 * <li>/C:/../hello/
	 * </ul>
	 * The spearator in the return path is platform-indepedent "/". Please note that
	 * the <code>/</code> in the end of directory will be striped in the return
	 * value.
	 * 
	 * @param base     the base directory
	 * @param resource the full path
	 * @return the relative path.
	 */

	public static String getRelativePath(String base, String resource) {
		if (base == null || resource == null)
			return resource;

		if (isFileProtocol(resource) && isFileProtocol(base))
			return createRelativePathFromFilePath(base, resource);

		return createRelativePathFromString(base, resource, URL_SEPARATOR);
	}

	/**
	 * Return the relative path for the given file path <code>resource</code>
	 * according to <code>base</code>. Only handle file system.
	 * 
	 * @param base     the base directory
	 * @param resource the full path
	 * @return the relative path.
	 */

	private static String createRelativePathFromFilePath(String base, String resource) {
		String baseDir = getLocalPath(base);
		String resourceDir = getLocalPath(resource);

		if (baseDir == null || resourceDir == null)
			return resource;

		File baseFile = new File(baseDir);
		File resourceFile = new File(resourceDir);

		// get platform-depedent file path by using Java File class

		baseDir = SecurityUtil.getFileAbsolutePath(baseFile);
		resourceDir = SecurityUtil.getFileAbsolutePath(resourceFile);

		return createRelativePathFromString(baseDir, resourceDir, File.separator);
	}

	/**
	 * Return the relative path for the given string <code>resource</code> according
	 * to <code>base</code>. This method purely works on character level.
	 * 
	 * @param baseDir     the base directory
	 * @param resourceDir the full path
	 * @return the relative path.
	 */

	private static String createRelativePathFromString(String baseDir, String resourceDir, String separator) {
		String newBaseDir = baseDir;

		if (newBaseDir.endsWith("/") || newBaseDir.endsWith(separator)) //$NON-NLS-1$
			newBaseDir = newBaseDir.substring(0, newBaseDir.length() - 1);

		// do the string match to get the location of same prefix

		int matchedPos = 0;
		for (matchedPos = 0; matchedPos < newBaseDir.length() && matchedPos < resourceDir.length(); matchedPos++) {
			if (newBaseDir.charAt(matchedPos) != resourceDir.charAt(matchedPos))
				break;
		}

		// adjust the same prefix by the path separator

		// for the special case like:
		// baseDir: c:/hello/test
		// resourceDir: c:/hello/test/library.xml
		// then the matched position is the length of baseDir insteadof
		// the slash before "test".

		if (isLastDirectoryMatched(newBaseDir, resourceDir, matchedPos)
				|| isLastDirectoryMatched(resourceDir, newBaseDir, matchedPos))
			;
		else {
			int oldMatchedPos = matchedPos;
			matchedPos = newBaseDir.lastIndexOf(separator, oldMatchedPos - 1);
		}

		// saves the matched position

		int samePrefixPos = matchedPos;

		int upDirs = 0;

		// calcualtes out the number of up directory should have.

		while (matchedPos < newBaseDir.length() && matchedPos >= 0) {
			matchedPos = newBaseDir.indexOf(separator, matchedPos + 1);
			upDirs++;
		}

		// appends up directories information.

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < upDirs; i++) {
			sb.append("../"); //$NON-NLS-1$
		}

		// appends the relative path.

		if (samePrefixPos < resourceDir.length()) {
			String remainPath = resourceDir.substring(samePrefixPos + 1);
			remainPath = remainPath.replace('\\', '/');
			sb.append(remainPath);
		}

		// remove the tail file.separatorChar

		int len = sb.length();
		if (len > 0) {
			char lastChar = sb.charAt(len - 1);
			if (lastChar == '/')
				sb.deleteCharAt(len - 1);
		}

		return sb.toString();
	}

	/**
	 * Gets the absolute path for the given <code>base</code> and <code>
	 * relativePath</code> .
	 * <p>
	 * The <code>base</code> value should be directory ONLY and does NOT contain
	 * file name and the format can be:
	 * <ul>
	 * <li>./../hello/
	 * <li>C:\\hello\..\
	 * <li>/C:/../hello/
	 * </ul>
	 * The spearator in the return path is platform-depedent.
	 * 
	 * @param base         the base directory
	 * @param relativePath the relative path
	 * @return the absolute path
	 */

	public static String resolveAbsolutePath(String base, String relativePath) {
		if (base == null || relativePath == null)
			return relativePath;

		if (isFileProtocol(base) && isFileProtocol(relativePath))
			return resolveAbsolutePathFromFilePath(base, relativePath);

		return resolveAbsolutePathFromString(base, relativePath);
	}

	/**
	 * Gets the absolute path for the given <code>base</code> and <code>
	 * relativePath</code> .
	 * <p>
	 * The <code>base</code> value should be directory ONLY and does NOT contain
	 * file name and the format can be:
	 * <ul>
	 * <li>./../hello/
	 * <li>C:\\hello\..\
	 * <li>/C:/../hello/
	 * </ul>
	 * The spearator in the return path is platform-depedent.
	 * 
	 * @param base         the base directory
	 * @param relativePath the relative path
	 * @return the absolute path
	 */

	private static String resolveAbsolutePathFromFilePath(String base, String relativePath) {

		File file = new File(relativePath);
		if (file.isAbsolute())
			return relativePath;

		String baseDir = getLocalPath(base);
		String relativeDir = getLocalPath(relativePath);

		if (baseDir == null || relativeDir == null)
			return relativePath;

		File baseFile = new File(baseDir);
		File resourceFile = new File(baseFile, relativeDir);

		try {
			return resourceFile.getCanonicalPath();
		} catch (IOException e) {
			return resourceFile.getPath();

		}
	}

	/**
	 * Gets the absolute path for the given <code>base</code> and <code>
	 * relativePath</code> .
	 * <p>
	 * The <code>base</code> value should be directory ONLY and does NOT contain
	 * file name and the format can be:
	 * <ul>
	 * <li>./../hello/
	 * <li>C:\\hello\..\
	 * <li>/C:/../hello/
	 * </ul>
	 * The spearator in the return path is platform-depedent.
	 * 
	 * @param base         the base directory
	 * @param relativePath the relative path
	 * @return the absolute path
	 */

	private static String resolveAbsolutePathFromString(String base, String relativePath) {
		if (base == null || relativePath == null)
			return relativePath;

		URI uri = URIUtilImpl.resolveAbsolutePath(relativePath);
		if (uri != null && uri.isAbsolute()) {
			return relativePath;
		}

		boolean appendDirectorySeparator = false;
		if (base.length() > 0 && relativePath.length() > 0) {
			char lastBaseChar = base.charAt(base.length() - 1);
			char firstRelativeChar = relativePath.charAt(0);

			if (lastBaseChar != '/' && lastBaseChar != File.separatorChar && firstRelativeChar != '/'
					&& firstRelativeChar != File.separatorChar)
				appendDirectorySeparator = true;
		}

		String path = null;
		if (appendDirectorySeparator) {
			path = base + '/' + relativePath;
		} else {
			path = base + relativePath;
		}

		uri = URIUtilImpl.resolveAbsolutePath(path);
		if (uri != null) {
			return uri.normalize().toString();
		}
		return path;
	}

	/**
	 * Tests whether the string before <code>matchedPos</code> is a directory.
	 * 
	 * @param baseDir     the base directory
	 * @param resourceDir the resource directory
	 * @param matchedPos  the 0-based position
	 * @return <code>true</code> if the string before <code>matchedPos</code> is a
	 *         directory
	 */

	private static boolean isLastDirectoryMatched(String baseDir, String resourceDir, int matchedPos) {
		if (matchedPos == baseDir.length() && ((matchedPos < resourceDir.length()
				&& (resourceDir.charAt(matchedPos) == File.separatorChar || resourceDir.charAt(matchedPos) == '/')
				|| matchedPos == resourceDir.length())))
			return true;

		return false;
	}

	/**
	 * Tests whether the input string is a valid resource directory.
	 * 
	 * @param resourceDir the resource directory
	 * @return <code>true</code> if the input string is a valid resource directory,
	 *         <code>false</code> otherwise.
	 * @throws MalformedURLException
	 */

	public static boolean isValidResourcePath(final String resourceDir) {
		if (resourceDir == null)
			return false;

		File f = new File(resourceDir);
		if (f.isAbsolute() && SecurityUtil.isFile(f) && SecurityUtil.isDirectory(f))
			return true;

		return false;
	}
}
