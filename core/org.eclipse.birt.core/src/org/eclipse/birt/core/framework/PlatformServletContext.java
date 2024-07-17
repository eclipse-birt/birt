/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.core.framework;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.servlet.ServletContext;

/**
 * An platform context that is based on resource operations instead of file
 * operations. Since in web environment WAR deployment, absolute file path is
 * not available, user must use resource operations instead of file operations.
 * In this case, user should use this PlatformContext or develop his own
 * PlatformContext to make sure no file operations are used.
 */
public class PlatformServletContext implements IPlatformContext {

	static protected Logger log = Logger.getLogger(PlatformServletContext.class.getName());

	private static final String RESOURCE_BASE = "/WEB-INF/platform/"; //$NON-NLS-1$
	private ServletContext context = null; // the ServletContext.
	private String platform;
	protected PlatformConfig platformConfig;

	/**
	 * @param context
	 * @param urlLeadingString
	 * @deprecated since 2.1
	 */
	@Deprecated
	public PlatformServletContext(ServletContext context, String urlLeadingString) {
		this.context = context;
	}

	public PlatformServletContext(ServletContext context) {
		this.context = context;
	}

	@Override
	public String getPlatform() {
		if (platform == null) {
			synchronized (this) {
				if (platform == null) {
					deploy();
				}
			}
		}
		return platform;
	}

	/**
	 * deploy the platform resources to file based platform.
	 *
	 */
	private void deploy() {
		assert platform == null;
		if (!hasPlatform()) {
			return;
		}
		platform = context.getRealPath(RESOURCE_BASE);
		if (platform == null || !new File(platform).exists()) {
			File contextTemp = (File) context.getAttribute("jakarta.servlet.context.tempdir"); //$NON-NLS-1$
			File platformFolder = new File(contextTemp, "platform");
			// Weblogic try to remove the platform but it fails,
			// so try to copy the platform each time.
			// if ( !platformFolder.exists( ) )
			{
				platformFolder.mkdir();
				copyResources(RESOURCE_BASE, platformFolder.getAbsolutePath());
			}
			platform = platformFolder.getAbsolutePath();
		}
	}

	private boolean hasPlatform() {
		Set resources = context.getResourcePaths(RESOURCE_BASE);
		if (resources == null || resources.isEmpty()) {
			return false;
		}
		return true;
	}

	/**
	 * copy resource to the platform. If the resources is a folder, make the same
	 * folder into the platform and copy all resources into the dest folder.
	 *
	 * @param resourcePath resource path.
	 * @param platform     platform folder
	 */
	private void copyResources(String resourcePath, String platform) {
		Set paths = context.getResourcePaths(resourcePath);
		if (paths != null) {
			for (Iterator it = paths.iterator(); it.hasNext();) {
				String path = (String) it.next();
				File newFile = new File(platform, path.substring(RESOURCE_BASE.length()));
				if (path.endsWith("/")) { //$NON-NLS-1$
					newFile.mkdir();
					copyResources(path, platform);
				} else {
					InputStream is = null;
					OutputStream os = null;
					try {
						if (newFile.createNewFile()) {
							is = context.getResourceAsStream(path);
							os = new FileOutputStream(newFile);
							byte[] buffer = new byte[8192];
							int bytesRead = is.read(buffer);
							while (bytesRead != -1) {
								os.write(buffer, 0, bytesRead);
								bytesRead = is.read(buffer);
							}
						}
					} catch (IOException e) {
						log.log(Level.WARNING, "Error copying resources {0} to platform.", e); //$NON-NLS-1$
					} finally {
						if (is != null) {
							try {
								is.close();
							} catch (Exception exin) {
								log.log(Level.WARNING, "Error closing resource stream.", exin);//$NON-NLS-1$
							}
							is = null;
						}

						if (os != null) {
							try {
								os.close();
							} catch (Exception exout) {
								log.log(Level.WARNING, "Error closing file output stream.", exout);//$NON-NLS-1$
							}
							os = null;
						}
					}
				}
			}
		}
	}
}
