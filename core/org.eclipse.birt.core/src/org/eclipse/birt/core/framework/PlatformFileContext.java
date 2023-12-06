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

/**
 * An platform context that is based on file operations. Since in web
 * environment WAR deployment, absolute file path is not available. In this
 * case, user should NOT use this class. In this case, user should use
 * PlatformServletContext or develop his own PlatformContext to make sure
 * reousce operation are used.
 */
public class PlatformFileContext implements IPlatformContext {

	protected String root;

	protected String[] arguments;

	/**
	 * PlatformFileContext Constructor
	 *
	 */
	public PlatformFileContext() {
		root = getSystemBirtHome();
		arguments = null;
	}

	/**
	 * PlatformFileContext Constructor( String , IPlatformConfig )
	 *
	 * @param root
	 * @param platformConfig
	 */
	public PlatformFileContext(PlatformConfig config) {
		assert config != null;
		root = config.getBIRTHome();
		if (root == null || "".equals(root)) {
			root = getSystemBirtHome();
		}
		arguments = config.getOSGiArguments();
	}

	@Override
	public String getPlatform() {
		return root;
	}

	public String[] getLaunchArguments() {
		return arguments;
	}

	private String getSystemBirtHome() {
		String home = System.getProperty(IPlatformConfig.BIRT_HOME);
		if (home == null || "".equals(home)) {
			return null;
		}
		return home;
	}
}
