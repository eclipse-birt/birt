/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test <code>CachedMetaDataHandle</code>.
 * <p>
 * 1. Parse a design file that has contains cached meta-data for a data set.
 * <p>
 * 2. Set a new CachedMetaData on a data set, it contains a list of Result set
 * column, a list of input parameters and a list of output parameters.
 */

public class CachedMetaDataParserTest extends BaseTestCase {

	private final static String INPUT_FILE = "CachedMetaDataParserTest.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE = "CachedMetaDataParserTest_golden.xml"; //$NON-NLS-1$

	/**
	 * Data set handle.
	 */

	private DataSetHandle dataSetHandle = null;

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * This test reads the design file, and checks the properties and style
	 * properties of line.
	 *
	 * @throws Exception
	 */

	public void testParserDesignFile() throws Exception {
		openDesign(INPUT_FILE);

		dataSetHandle = designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		assert dataSetHandle != null;

		parser();
	}

	/**
	 * This test sets properties, writes the design file and compares it with golden
	 * file.
	 *
	 * @throws Exception
	 */

	public void testWriterDesignFile() throws Exception {
		openDesign(INPUT_FILE);

		dataSetHandle = designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		assert dataSetHandle != null;
		writer();
	}

	/**
	 * test parser.
	 * <p>
	 * Parse a design file that has contains cached meta-data for a data set.
	 */

	private void parser() {

		CachedMetaDataHandle metadataHandle = dataSetHandle.getCachedMetaDataHandle();
		assertNotNull(metadataHandle);

		MemberHandle inputParameterHandle = metadataHandle.getParameters();
		assertEquals(4, inputParameterHandle.getListValue().size());

		MemberHandle resultSetHandle = metadataHandle.getResultSet();
		assertEquals(2, resultSetHandle.getListValue().size());
	}

	/**
	 * test writer.
	 * <p>
	 * Set a new CachedMetaData on a data set, it contains a list of Result set
	 * column, a list of input parameters and a list of output parameters.
	 *
	 * @throws Exception
	 */

	private void writer() throws Exception {
		// clear the cache.

		dataSetHandle.setCachedMetaData(null);

		CachedMetaDataHandle metadataHandle = dataSetHandle.getCachedMetaDataHandle();
		assert metadataHandle == null;

		// set a new cached meta-data on the data set.
		CachedMetaData metadata = StructureFactory.createCachedMetaData();
		metadataHandle = dataSetHandle.setCachedMetaData(metadata);
		assert metadataHandle != null;

		MemberHandle paramHandle = metadataHandle.getParameters();
		DataSetParameter inputParam1 = new DataSetParameter();
		inputParam1.setName("inputParam1"); //$NON-NLS-1$
		inputParam1.setDataType(DesignChoiceConstants.PARAM_TYPE_FLOAT);
		inputParam1.setPosition(new Integer(5));
		inputParam1.setAllowNull(true);
		paramHandle.addItem(inputParam1);

		DataSetParameter outputParam1 = new DataSetParameter();
		outputParam1.setName("outputParam1"); //$NON-NLS-1$
		outputParam1.setDataType("integer"); //$NON-NLS-1$
		outputParam1.setPosition(new Integer(4));
		paramHandle.addItem(outputParam1);

		MemberHandle resultSetHandle = metadataHandle.getResultSet();
		ResultSetColumn resultSet1 = new ResultSetColumn();
		resultSet1.setColumnName("student"); //$NON-NLS-1$
		resultSet1.setDataType("string"); //$NON-NLS-1$
		resultSet1.setPosition(new Integer(6));
		resultSetHandle.addItem(resultSet1);

		save();
		compareFile(GOLDEN_FILE);
	}

}
