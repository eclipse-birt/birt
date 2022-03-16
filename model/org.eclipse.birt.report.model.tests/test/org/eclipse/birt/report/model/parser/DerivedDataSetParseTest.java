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

package org.eclipse.birt.report.model.parser;

import java.util.List;

import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DerivedDataSetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the parser and all APIs for derived data set.
 *
 */
public class DerivedDataSetParseTest extends BaseTestCase {

	private static final String fileName = "DerivedDataSetParseTest.xml"; //$NON-NLS-1$

	/**
	 * Tests properties. Added for TED 14963.
	 *
	 * @throws Exception
	 */
	public void testProperties() throws Exception {
		createDesign();
		DerivedDataSetHandle setHandle = designHandle.getElementFactory().newDerivedDataSet(null, "test");
		designHandle.getDataSets().add(setHandle);
		assertNotNull(setHandle.paramBindingsIterator());
		assertNotNull(setHandle.parametersIterator());
		assertNull(setHandle.getDataSourceName());
		try {
			setHandle.setDataSource("Test");
			fail();
		} catch (PropertyNameException e) {
			assertEquals(PropertyNameException.DESIGN_EXCEPTION_PROPERTY_NAME_INVALID, e.getErrorCode());
		}

	}

	/**
	 * Tests the parser and get APIs for derived data set.
	 *
	 * @throws Exception
	 */
	public void testParser() throws Exception {
		openDesign(fileName);
		DerivedDataSetHandle derivedDataSetHandle = (DerivedDataSetHandle) designHandle.findDataSet("derivedDataSet"); //$NON-NLS-1$

		assertEquals("derived.extension", derivedDataSetHandle.getExtensionID()); //$NON-NLS-1$
		assertEquals("query text for the derived data set", derivedDataSetHandle.getQueryText()); //$NON-NLS-1$

		List<DataSetHandle> dataSets = derivedDataSetHandle.getInputDataSets();
		assertEquals(designHandle.findDataSet("DataSet1"), dataSets.get(0)); //$NON-NLS-1$
		assertEquals(designHandle.findDataSet("DataSet2"), dataSets.get(1)); //$NON-NLS-1$
	}

	/**
	 * Tests the writer and write APIs for derived data set. Also test the factory
	 * method for derived data set.
	 *
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		openDesign(fileName);

		DerivedDataSetHandle derivedDataSetHandle = (DerivedDataSetHandle) designHandle.findDataSet("derivedDataSet"); //$NON-NLS-1$

		// set query-text
		derivedDataSetHandle.setQueryText("updated " + derivedDataSetHandle.getQueryText()); //$NON-NLS-1$
		derivedDataSetHandle.removeInputDataSet("DataSet2"); //$NON-NLS-1$

		// create another derived-data set
		derivedDataSetHandle = designHandle.getElementFactory().newDerivedDataSet(null, "derived.extensionID.new"); //$NON-NLS-1$
		derivedDataSetHandle.addInputDataSets("DataSet3"); //$NON-NLS-1$
		derivedDataSetHandle.addInputDataSets("DataSet1"); //$NON-NLS-1$
		designHandle.getDataSets().add(derivedDataSetHandle);

		save();
		assertTrue(compareFile("DerivedDataSetParseTest_golden.xml")); //$NON-NLS-1$
	}

	public void testCommand() throws Exception {
		openDesign(fileName);

		DerivedDataSetHandle derivedDataSetHandle = (DerivedDataSetHandle) designHandle.findDataSet("derivedDataSet"); //$NON-NLS-1$

		DerivedDataSetHandle newDerivedHandle = (DerivedDataSetHandle) derivedDataSetHandle.copy().getHandle(design);
		designHandle.rename(newDerivedHandle);
		designHandle.getDataSets().add(newDerivedHandle);
		newDerivedHandle.addInputDataSets(derivedDataSetHandle.getName());

		// circular reference
		try {
			derivedDataSetHandle.addInputDataSets(newDerivedHandle.getName());
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}
	}
}
