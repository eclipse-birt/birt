/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.net.URL;

import org.eclipse.birt.report.model.api.DefaultResourceLocator;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Description:</b>
 * <p>
 * There is no fileType for .properties files in IResourceLocator.
 * <p>
 * *.jar is also a type of report resource, so, must support *.jar too.
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Find .properties file and .jar file in resource folder
 */
public class Regression_155509 extends BaseTestCase
{

	private String filename = "Regression_155509.xml"; //$NON-NLS-1$
	private String propfile = "Regression_155509"; //$NON-NLS-1$
	private String jarfile = "input/Regression_155509.jar"; //$NON-NLS-1$
	private DefaultResourceLocator rl;

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_155509( ) throws DesignFileException
	{
		openDesign( filename );
		rl = new DefaultResourceLocator( );
		sessionHandle.setResourceFolder( getClassFolder( ) );

		URL jarrsc = rl.findResource(
				designHandle,
				jarfile,
				IResourceLocator.JAR_FILE );
		assertNotNull( jarrsc );

		sessionHandle.setResourceFolder( getClassFolder( ) + INPUT_FOLDER );

		URL messagersc = rl.findResource(
				designHandle,
				propfile,
				IResourceLocator.MESSAGE_FILE );
		assertNotNull( messagersc );

	}

}
