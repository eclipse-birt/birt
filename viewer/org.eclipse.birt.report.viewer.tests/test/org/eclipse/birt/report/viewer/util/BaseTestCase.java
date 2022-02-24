/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.util;

import java.io.File;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.eclipse.birt.report.viewer.mock.HttpServletRequestSimulator;
import org.eclipse.birt.report.viewer.mock.HttpServletResponseSimulator;
import org.eclipse.birt.report.viewer.mock.HttpSessionSimulator;
import org.eclipse.birt.report.viewer.mock.ServletContextSimulator;

import junit.framework.TestCase;

/**
 * Abstract class extends junit framework TestCase.
 * <P>
 * Set up the required UnitTest environment.
 * <ol>
 * <li>Mock a base ServletContext object</li>
 * <li>Mock a base HttpServletRequest object</li>
 * </ol>
 */
public abstract class BaseTestCase extends TestCase {

	protected HttpServletRequestSimulator request;
	protected HttpServletResponseSimulator response;
	protected HttpSessionSimulator session;
	protected ServletContextSimulator context;
	protected File root;

	protected static final String TEST_FOLDER = "test/"; //$NON-NLS-1$
	protected static final String ROOT_FOLDER = "test" + File.separator //$NON-NLS-1$
			+ "root"; //$NON-NLS-1$
	protected static final String DEFAULT_LOCALE = "en_US"; //$NON-NLS-1$

	protected static final String ENCODING_UTF8 = "UTF-8"; //$NON-NLS-1$
	protected static final String ENCODING_ISO = "ISO-8859-1"; //$NON-NLS-1$
	protected static final String ENCODING_GBK = "GBK"; //$NON-NLS-1$

	/**
	 * Init test environment
	 */
	protected void setUp() throws Exception {
		super.setUp();

		root = new File(ROOT_FOLDER);

		// Initialize ServletContext Mocked Object
		this.context = new ServletContextSimulator();
		context.setContextDir(root);
		context.setInitParameter(ParameterAccessor.INIT_PARAM_LOCALE, DEFAULT_LOCALE);
		context.setInitParameter(ParameterAccessor.INIT_PARAM_WORKING_FOLDER_ACCESS_ONLY, "false"); //$NON-NLS-1$
		context.setInitParameter(ParameterAccessor.INIT_PARAM_OVERWRITE_DOCUMENT, "true"); //$NON-NLS-1$
		context.setInitParameter(ParameterAccessor.INIT_PARAM_LOG_LEVEL, "OFF"); //$NON-NLS-1$
		context.setInitParameter(ParameterAccessor.INIT_PARAM_BIRT_RESOURCE_PATH, root.getAbsolutePath());
		context.setInitParameter(ParameterAccessor.INIT_PARAM_CONFIG_FILE, (new File(
				"../org.eclipse.birt.report.viewer/birt" + File.separator + IBirtConstants.DEFAULT_VIEWER_CONFIG_FILE))
						.getAbsolutePath());

		// Initialize HttpServletRequest Mocked Object
		this.request = new HttpServletRequestSimulator(context);
		this.response = new HttpServletResponseSimulator();
		this.session = new HttpSessionSimulator(context);
		request.setSession(session);

		// Initialize ParameterAccessor
		ParameterAccessor.initParameters(context);
	}

	/**
	 * Handler after done test
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/**
	 * Locates the folder where the unit test java source file is saved.
	 * 
	 * @return the path name where the test java source file locates.
	 */
	protected String getClassFolder() {
		String pathBase = null;

		ProtectionDomain domain = this.getClass().getProtectionDomain();
		if (domain != null) {
			CodeSource source = domain.getCodeSource();
			if (source != null) {
				URL url = source.getLocation();
				pathBase = url.getPath();

				if (pathBase.endsWith("bin/")) //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 4);
				if (pathBase.endsWith("bin")) //$NON-NLS-1$
					pathBase = pathBase.substring(0, pathBase.length() - 3);
			}
		}

		pathBase = pathBase + TEST_FOLDER;
		String className = this.getClass().getName();
		int lastDotIndex = className.lastIndexOf("."); //$NON-NLS-1$
		className = className.substring(0, lastDotIndex);
		className = pathBase + className.replace('.', '/');

		return className;
	}
}
