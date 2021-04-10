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

package org.eclipse.birt.report.model.metadata;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in extension package.
 */

public class AllMetadataTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(BooleanPropertyTypeTest.class);
		test.addTestSuite(ChoicePropertyTypeTest.class);
		test.addTestSuite(ChoiceSetTest.class);
		test.addTestSuite(ChoiceTest.class);
		test.addTestSuite(ClassDefnTest.class);
		test.addTestSuite(ColorPropertyTypeTest.class);
		test.addTestSuite(DateTimePropertyTypeTest.class);
		test.addTestSuite(DimensionPropertyTypeTest.class);
		test.addTestSuite(DimensionValueTest.class);
		test.addTestSuite(ElementDefnTest.class);
		test.addTestSuite(ElementRefPropertyTypeTest.class);
		test.addTestSuite(ElementRefValueTest.class);
		test.addTestSuite(ExpressionPropertyTypeTest.class);
		test.addTestSuite(ExtendsPropertyTypeTest.class);
		test.addTestSuite(FloatPropertyTypeTest.class);
		test.addTestSuite(HTMLPropertyTypeTest.class);
		test.addTestSuite(IntegerPropertyTypeTest.class);
		test.addTestSuite(MetaDataDictionaryTest.class);
		test.addTestSuite(MetaDataExceptionTest.class);
		test.addTestSuite(MetaDataReaderTest.class);
		test.addTestSuite(MetaLoggerTest.class);
		test.addTestSuite(MetaLogManagerTest.class);
		test.addTestSuite(NamePropertyTypeTest.class);
		test.addTestSuite(NumberPropertyTypeTest.class);
		test.addTestSuite(ObjectDefnTest.class);
		test.addTestSuite(PropertyDefnTest.class);
		test.addTestSuite(PropertyValueExceptionTest.class);
		test.addTestSuite(ResourceKeyPropertyTypeTest.class);
		test.addTestSuite(SlotDefnTest.class);
		test.addTestSuite(StandardStyleTest.class);
		test.addTestSuite(StringPropertyTypeTest.class);
		test.addTestSuite(StructListPropertyTypeTest.class);
		test.addTestSuite(StructPropertyDefnTest.class);
		test.addTestSuite(SystemPropertyDefnTest.class);
		test.addTestSuite(URIPropertyTypeTest.class);
		test.addTestSuite(ValidatorTriggerTest.class);
		test.addTestSuite(XMLPropertyTypeTest.class);
		test.addTestSuite(MetaDataStringTrimTest.class);

		// add all test classes here

		return test;
	}
}
