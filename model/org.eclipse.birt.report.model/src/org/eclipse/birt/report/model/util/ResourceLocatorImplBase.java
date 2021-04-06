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
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.BundleFactory;
import org.eclipse.birt.report.model.api.IBundleFactory;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.core.BundleHelper;

import com.ibm.icu.util.ULocale;

public class ResourceLocatorImplBase implements IResourceLocator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.IResourceLocator#findResource(org.eclipse
	 * .birt.report.model.api.ModuleHandle, java.lang.String, int)
	 */

	public URL findResource(ModuleHandle moduleHandle, String fileName, int type) {
		return findResource(moduleHandle, fileName, type, Collections.EMPTY_MAP);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.IResourceLocator#findResource(org.eclipse
	 * .birt.report.model.api.ModuleHandle, java.lang.String, int, java.util.Map)
	 */
	public URL findResource(ModuleHandle moduleHandle, String fileName, int type, Map appContext) {
		URL u = null;

		if (fileName == null)
			return u;

		int location = RESOURCE_BUNDLE | RESOURCE_FOLDER | RESOURCE_FILEPATH | RESOURCE_DESIGN;

		if (appContext != null) {
			Object loc = appContext.get(BIRT_RESOURCELOCATOR_SEARCH_LOCATION);
			if (loc instanceof Integer)
				location = ((Integer) loc).intValue();
		}

		switch (type) {
		case IResourceLocator.MESSAGE_FILE:
			u = getMessageFile(moduleHandle, fileName, location);
			break;
		default:
			u = getResource(moduleHandle, fileName, location);
			break;
		}
		return u;
	}

	/**
	 * Gets message file URL.
	 * 
	 * @param moduleHandle module handle
	 * @param fileName     file name
	 * @param location     the location to search
	 * @return message file URL.
	 */

	private URL getMessageFile(ModuleHandle moduleHandle, String fileName, int location) {
		if (moduleHandle == null)
			return null;

		ULocale locale = moduleHandle.getModule().getSession().getLocale();

		List<String> possibleFiles = BundleHelper.getHelper(moduleHandle.getModule(), fileName)
				.getMessageFilenames(locale);

		for (int i = 0; i < possibleFiles.size(); i++) {
			String filename = possibleFiles.get(i);
			URL url = getResource(moduleHandle, filename, location);
			if (url != null)
				return url;
		}
		return null;
	}

	/**
	 * Gets resource url. Now support <code>IMAGE</code>,<code>LIBRARY</code> ,
	 * <code>CASCADING_STYLE_SHEET</code>
	 * 
	 * @param moduleHandle module handle
	 * @param fileName     file name
	 * @param location     the location to search
	 * @return resource url
	 */

	private URL getResource(ModuleHandle moduleHandle, String fileName, int location) {
		// try absolute path search
		if ((location & RESOURCE_FILEPATH) != 0) {
			URL retURL = tryDiskFileSearch(null, fileName);
			if (retURL != null)
				return retURL;

			// try url search

			try {
				retURL = tryURLSearch(new URL(fileName));
				if (retURL != null)
					return retURL;
			} catch (MalformedURLException e) {
				// ignore the error
			}
		}
		// if module is null, then can not search the resource path or systemId
		if (moduleHandle == null && (location & RESOURCE_BUNDLE) != 0)
			return tryFragmentSearch(fileName);

		// try file search based on resource path, value set on the session
		// takes the higher priority than that in the module itself

		String resourcePath = moduleHandle.getModule().getSession().getResourceFolder();

		if (StringUtil.isBlank(resourcePath))
			resourcePath = moduleHandle.getResourceFolder();

		if (resourcePath != null && (location & RESOURCE_FOLDER) != 0) {
			URL retURL = tryDiskFileSearch(resourcePath, fileName);
			if (retURL != null)
				return retURL;

			try {
				URL baseURL = new URL(resourcePath);

				retURL = tryURLSearch(baseURL, fileName);
			} catch (MalformedURLException e) {
			}

			if (retURL != null)
				return retURL;

		}

		// try fragment search
		if ((location & RESOURCE_BUNDLE) != 0) {
			URL retURL = tryFragmentSearch(fileName);
			if (retURL != null)
				return retURL;
		}

		// try file search based on path of the input module
		URL systemId = moduleHandle.getModule().getSystemId();
		if (systemId != null && (location & RESOURCE_DESIGN) != 0)
			return tryURLSearch(systemId, fileName);

		return null;
	}

	/**
	 * Tests if the url indicates a global resource.
	 * 
	 * @param url the url to test
	 * @return true if the url indicates to a global resource, false otherwise.
	 */

	private boolean isGlobalResource(URL url) {
		if (URIUtilImpl.FTP_SCHEMA.equalsIgnoreCase(url.getProtocol())
				|| URIUtilImpl.HTTP_SCHEMA.equalsIgnoreCase(url.getProtocol())
				|| URIUtilImpl.HTTPS_SCHEMA.equalsIgnoreCase(url.getProtocol())
				|| URIUtilImpl.MAIL_SCHEMA.equalsIgnoreCase(url.getProtocol()))
			return true;

		if (url.getFile().toLowerCase().startsWith(URIUtilImpl.FTP_SCHEMA)
				|| url.getFile().toLowerCase().startsWith(URIUtilImpl.HTTP_SCHEMA))
			return true;

		return false;
	}

	/**
	 * Search the URL resource.
	 * 
	 * @param baseURL  the base URL.
	 * @param fileName the file name.
	 * @return url of the resource if found, null otherwise.
	 */
	private URL tryURLSearch(URL baseURL, String fileName) {
		assert baseURL != null;

		URL retURL = null;
		try {
			// if the url is bundleentry or bundleresource protocol, the file
			// name will not be converted a valid URL string, otherwise the file
			// name will be converted a valid URL string.
			// will not be
			if (URIUtilImpl.isBundleProtocol(baseURL.toString())) {
				retURL = tryURLSearch(new URL(baseURL, fileName));
			} else {
				retURL = tryURLSearch(new URL(baseURL, URIUtil.convertFileNameToURLString(fileName)));
			}
		} catch (MalformedURLException e) {

		}

		return retURL;
	}

	/**
	 * Search the URL resource.
	 * 
	 * @param url the url of the resources.
	 * @return url of the resource if found, null otherwise.
	 */

	private URL tryURLSearch(URL url) {
		boolean networkProtocol = isGlobalResource(url);
		if (networkProtocol)
			return url;

		InputStream in = null;
		try {
			in = url.openStream();
		} catch (IOException e1) {
			return null;
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
				}
		}

		return url;
	}

	/**
	 * Returns the url of resource which is in corresponding bundle.
	 * 
	 * @param moduleHandle module in which the bundle symbolic name is cached
	 * @param fileName     the relative file name
	 * @return the url of resource if found
	 */

	private URL tryFragmentSearch(String fileName) {
		IBundleFactory bundleFactory = BundleFactory.getBundleFactory();
		if (bundleFactory != null) {
			return bundleFactory.getBundleResource(FRAGMENT_RESOURCE_HOST, fileName);
		}
		// search in the class path
		return this.getClass().getClassLoader().getResource(fileName);
	}

	/**
	 * Search the file on the local disk resources.
	 * 
	 * @param fileDir  the file directory
	 * @param filePath the file path. May contain the relative directory.
	 * 
	 * @return url of the resource if found, null otherwise.
	 */

	private URL tryDiskFileSearch(String fileDir, String filePath) {

		File f = null;

		String tmpFilePath = URIUtilImpl.toUniversalFileFormat(filePath);
		if (StringUtil.isBlank(fileDir))
			f = new File(tmpFilePath);
		else
			f = new File(fileDir, tmpFilePath);

		try {
			if (SecurityUtil.exists(f) && SecurityUtil.isFile(f))
				return SecurityUtil.fileToURI(SecurityUtil.getCanonicalFile(f)).toURL();
		} catch (MalformedURLException e) {
			assert false;
		} catch (IllegalArgumentException e) {
			assert false;
		}

		return null;

	}
}
