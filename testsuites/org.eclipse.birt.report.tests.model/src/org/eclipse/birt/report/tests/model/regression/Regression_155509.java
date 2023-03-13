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

package org.eclipse.birt.report.tests.model.regression;

import java.net.URL;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.util.ResourceLocatorImpl;
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
public class Regression_155509 extends BaseTestCase {

	private String filename = "Regression_155509.xml"; //$NON-NLS-1$
	private String propfile = "Regression_155509.properties"; //$NON-NLS-1$
	private String jarfile = "Regression_155509.jar"; //$NON-NLS-1$
	private ResourceLocatorImpl rl;

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( filename , filename );
		// copyResource_INPUT( propfile , propfile );
		// copyResource_INPUT( jarfile , jarfile );

		copyInputToFile(INPUT_FOLDER + "/" + filename);

		copyInputToFile(INPUT_FOLDER + "/" + propfile);

		copyInputToFile(INPUT_FOLDER + "/" + jarfile);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 */
	public void test_regression_155509() throws DesignFileException {
		openDesign(filename);
		rl = new ResourceLocatorImpl();
		// sessionHandle.setResourceFolder( getClassFolder( ) );
		sessionHandle.setResourceFolder(getTempFolder() + "/" + INPUT_FOLDER);

		URL jarrsc = rl.findResource(designHandle, jarfile, IResourceLocator.JAR_FILE);
		assertNotNull(jarrsc);

		// sessionHandle.setResourceFolder( this.getFullQualifiedClassName( ) + "/" +
		// INPUT_FOLDER + "/" );
		sessionHandle.setResourceFolder(getTempFolder() + "/" + INPUT_FOLDER);

		URL messagersc = rl.findResource(designHandle, "Regression_155509", IResourceLocator.MESSAGE_FILE);
		assertNotNull(messagersc);

	}

}
