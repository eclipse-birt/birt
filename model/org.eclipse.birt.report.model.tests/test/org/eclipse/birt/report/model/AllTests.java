/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.activity.AllActivityTests;
import org.eclipse.birt.report.model.api.AllApiTests;
import org.eclipse.birt.report.model.command.AllCommandTests;
import org.eclipse.birt.report.model.core.AllCoreTests;
import org.eclipse.birt.report.model.css.AllCssTests;
import org.eclipse.birt.report.model.elements.AllElementsTests;
import org.eclipse.birt.report.model.extension.AllExtensionTests;
import org.eclipse.birt.report.model.i18n.AllI18nTests;
import org.eclipse.birt.report.model.library.AllLibraryTests;
import org.eclipse.birt.report.model.metadata.AllMetadataTests;
import org.eclipse.birt.report.model.parser.AllParserTests;
import org.eclipse.birt.report.model.simpleapi.AllSimpleApiTests;
import org.eclipse.birt.report.model.util.AllUtilTests;
import org.eclipse.birt.report.model.validators.AllValidatorTests;
import org.eclipse.birt.report.model.writer.AllWriterTests;

/**
 * Tests cases run in the build script.
 */

public class AllTests {

	/**
	 * @return test run in build script
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all package tests here
		test.addTest(AllActivityTests.suite());
		test.addTest(AllApiTests.suite());
		test.addTest(AllCommandTests.suite());
		test.addTest(AllCoreTests.suite());
		test.addTest(AllCssTests.suite());
		test.addTest(AllElementsTests.suite());
		test.addTest(AllExtensionTests.suite());
		test.addTest(AllI18nTests.suite());
		test.addTest(AllLibraryTests.suite());
		test.addTest(AllMetadataTests.suite());
		test.addTest(AllParserTests.suite());
		test.addTest(AllUtilTests.suite());
		test.addTest(AllValidatorTests.suite());
		test.addTest(AllWriterTests.suite());
		test.addTest(AllSimpleApiTests.suite());

		return test;
	}

}
