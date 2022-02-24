/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.tests.model.api;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * TestCases for EmbeddedImageHandle class.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * 
 * <tr>
 * <td>{@link #testDrop()}</td>
 * <td>Drop embedded image.</td>
 * <td>Dropped images are deleted.</td>
 * </tr>
 * </table>
 * 
 */
public class EmbeddedImageHandleTest extends BaseTestCase {
	String filename = "Improved_test6.xml";

	public EmbeddedImageHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {

		return new TestSuite(EmbeddedImageHandleTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyInputToFile(INPUT_FOLDER + "/" + filename);

	}

	/**
	 * Drop embedded image.
	 * 
	 * @throws Exception
	 */
	public void testDrop() throws Exception {

		SessionHandle sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		ReportDesignHandle designHandle = sessionHandle
				.openDesign(getTempFolder() + "/" + INPUT_FOLDER + "/" + filename);

		SimpleValueHandle propHandle = (SimpleValueHandle) designHandle.getPropertyHandle(ReportDesign.IMAGES_PROP);

		EmbeddedImageHandle image1handle = (EmbeddedImageHandle) designHandle.findImage("group confirmation logo.jpg")
				.getHandle(propHandle);
		EmbeddedImageHandle image2handle = (EmbeddedImageHandle) designHandle.findImage("circles.png")
				.getHandle(propHandle);

		image1handle.drop();
		List value = propHandle.getListValue();
		assertEquals(1, value.size());
		assertEquals(image2handle.getStructure(), value.get(0));
		assertNull(image1handle.getStructure());

		image2handle.drop();
		List value1 = propHandle.getListValue();
		assertNull(value1);
		assertNull(image2handle.getStructure());
	}

}
