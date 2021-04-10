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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Unit test for JoinConditionHandle. The test cases are:
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="black">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected Result</th>
 * 
 * <tr>
 * <td>testUnsupportedMethods</td>
 * <td><code>The mehtods inherited from data set but not supported by joint data
 * set don't work.</td>
 * <td>Call to these methods will get exception.</td>
 * </tr>
 * 
 * <tr>
 * <td>testGetDataSetNames</td>
 * <td><code>Data set names can be gotten from joint data set handle.</td>
 * <td>Data set names can be correctly read out and added in.</td>
 * </tr>
 * 
 * <tr>
 * <td>testAddDataSetNames</td>
 * <td><code>Data set can be added into joint data set by name.</td>
 * <td></td>
 * </tr>
 * 
 * <tr>
 * <td>testRemoveDataSetNames</td>
 * <td><code>Data set can be removed from joint data set by name.</td>
 * <td></td>
 * </tr>
 * 
 * <tr>
 * <td>testSematicCheck</td>
 * <td><code>Test sematic check.</td>
 * <td></td>
 * </tr>
 * 
 * </table>
 * 
 * @see org.eclipse.birt.report.model.elements.JointDataSet
 */

public class JointDataSetHandleTest extends BaseTestCase {

	private String fileName = "JointDataSetHandleTest.xml"; //$NON-NLS-1$
	private String semanticfileName = "JointDataSetHandleTest_Semantic.xml"; //$NON-NLS-1$
	private String fileNameForRelation = "JointDataSetHandleTest_1.xml";//$NON-NLS-1$
	private JointDataSetHandle dataSet;

	/**
	 * Creates a joint data set.
	 */

	public void setUp() throws DesignFileException {
		openDesign(fileName);
		dataSet = designHandle.findJointDataSet("JointDataSet"); //$NON-NLS-1$

	}

	/**
	 * Tests the methods <code>columnHintsIterator</code>,
	 * <code>computedColumnsIterator</code>, <code>filtersIterator</code>,
	 * <code>getCachedMetaDataHandle</code>, <code>paramBindingsIterator</code>
	 * <code>parametersIterator</code>, <code>resultSetIterator</code>,
	 * <code>getCachedRowCount</code>, <code>getAfterClose</code>,
	 * <code>getAfterOpen</code>, <code>getBeforeClose</code>,
	 * <code>getBeforeOpen</code>, <code>getDataSource</code>,
	 * <code>getDataSourceName</code>, <code>getOnFetch</code>,
	 * <code>getOnFetch</code> are uncallable.
	 * 
	 */

	public void testUnsupportedMethods() {
		assertFalse(dataSet.filtersIterator().hasNext());
		assertFalse(dataSet.resultSetIterator().hasNext());
		assertFalse(dataSet.computedColumnsIterator().hasNext());
		assertFalse(dataSet.columnHintsIterator().hasNext());
		assertFalse(dataSet.parametersIterator().hasNext());

		assertFalse(dataSet.paramBindingsIterator().hasNext());
		assertNotNull(dataSet.getCachedMetaDataHandle());
		assertEquals(0, dataSet.getCachedRowCount());
		assertEquals(0, dataSet.getDataSetRowLimit());
		assertNull(dataSet.getAfterClose());
		assertNull(dataSet.getAfterOpen());
		assertNull(dataSet.getBeforeClose());
		assertNull(dataSet.getBeforeOpen());
		assertNull(dataSet.getDataSource());
		assertNull(dataSet.getDataSourceName());
		assertNull(dataSet.getOnFetch());
	}

	/**
	 * Test getting data set names.
	 */

	public void testDataSetNames() {
		List dataSets = dataSet.getElement().getListProperty(design, JointDataSet.DATA_SETS_PROP);
		assertEquals(2, dataSets.size());
		assertSame(design.findDataSet("DataSet1"), //$NON-NLS-1$
				((ElementRefValue) dataSets.get(0)).getElement());
		assertSame(design.findDataSet("DataSet2"), //$NON-NLS-1$
				((ElementRefValue) dataSets.get(1)).getElement());
	}

	/**
	 * Test removing data set.
	 * 
	 * @throws SemanticException
	 */

	public void testRemoveDataSet() throws SemanticException {
		dataSet.removeDataSet("DataSet2"); //$NON-NLS-1$
		List names = dataSet.getDataSetNames();
		assertEquals(1, names.size());
		assertEquals("DataSet1", names.get(0)); //$NON-NLS-1$
	}

	/**
	 * Test adding data set.
	 * 
	 * @throws SemanticException
	 */

	public void testAddDataSet() throws SemanticException {
		dataSet.addDataSet("DataSet3"); //$NON-NLS-1$
		List dataSets = dataSet.getElement().getListProperty(design, JointDataSet.DATA_SETS_PROP);
		assertEquals(3, dataSets.size());
		assertSame(design.findDataSet("DataSet1"), //$NON-NLS-1$
				((ElementRefValue) dataSets.get(0)).getElement());
		assertSame(design.findDataSet("DataSet2"), //$NON-NLS-1$
				((ElementRefValue) dataSets.get(1)).getElement());
		assertSame(design.findDataSet("DataSet3"), //$NON-NLS-1$
				((ElementRefValue) dataSets.get(2)).getElement());

	}

	/**
	 * Test sematic check.
	 * 
	 * @throws DesignFileException
	 */

	public void testSematicCheck() throws DesignFileException {
		openDesign(semanticfileName);
		dataSet = designHandle.findJointDataSet("JointDataSet"); //$NON-NLS-1$

		List errors = design.getErrorList();

		assertEquals(2, errors.size());

		ErrorDetail detail = (ErrorDetail) errors.get(0);
		assertSame(dataSet.getElement(), detail.getElement());
		assertEquals(SemanticError.DESIGN_EXCEPTION_DATA_SET_MISSED_IN_JOINT_DATA_SET, detail.getErrorCode());

		detail = (ErrorDetail) errors.get(1);
		assertSame(dataSet.getElement(), detail.getElement());
		assertEquals(SemanticError.DESIGN_EXCEPTION_DATA_SET_MISSED_IN_JOINT_DATA_SET, detail.getErrorCode());
	}

	/**
	 * test if count of clients of <code>DataSet</code> is right or not
	 * 
	 * @throws Exception
	 */
	public void testReference() throws Exception {
		// test parser
		openDesign(fileNameForRelation);
		JointDataSetHandle jdsHandle = designHandle.findJointDataSet("Data Set"); //$NON-NLS-1$
		assertEquals(0, countNum(jdsHandle));

		DataSetHandle dsHandle = (DataSetHandle) design.findDataSet("Data Set1").getHandle(design.getRoot()); //$NON-NLS-1$
		assertEquals(1, countNum(dsHandle));

		dsHandle = (DataSetHandle) design.findDataSet("Data Set2").getHandle(design.getRoot()); //$NON-NLS-1$
		assertEquals(1, countNum(dsHandle));

		dsHandle = (DataSetHandle) design.findDataSet("Data Set3").getHandle(design.getRoot()); //$NON-NLS-1$
		assertEquals(0, countNum(dsHandle));

		// add a data set exist, undo/redo
		jdsHandle.addDataSet("Data Set3"); //$NON-NLS-1$
		assertEquals(0, countNum(jdsHandle));
		dsHandle = (DataSetHandle) design.findDataSet("Data Set3").getHandle(design.getRoot()); //$NON-NLS-1$
		assertEquals(1, countNum(dsHandle));

		designHandle.getCommandStack().undo();
		assertEquals(0, countNum(dsHandle));

		designHandle.getCommandStack().redo();
		assertEquals(1, countNum(dsHandle));

		designHandle.getCommandStack().undo();
		assertEquals(0, countNum(dsHandle));

		// delete a data set, undo/redo
		jdsHandle.removeDataSet("Data Set1"); //$NON-NLS-1$
		dsHandle = (DataSetHandle) design.findDataSet("Data Set1").getHandle(design.getRoot()); //$NON-NLS-1$
		assertEquals(0, countNum(dsHandle));

		designHandle.getCommandStack().undo();
		assertEquals(1, countNum(dsHandle));

		designHandle.getCommandStack().redo();
		assertEquals(0, countNum(dsHandle));

		designHandle.getCommandStack().undo();
		assertEquals(1, countNum(dsHandle));

		// drop the joint data set, undo/redo
		jdsHandle.drop();
		dsHandle = (DataSetHandle) design.findDataSet("Data Set1").getHandle(design.getRoot()); //$NON-NLS-1$
		assertEquals(0, countNum(dsHandle));
		dsHandle = (DataSetHandle) design.findDataSet("Data Set2").getHandle(design.getRoot()); //$NON-NLS-1$
		assertEquals(0, countNum(dsHandle));

		designHandle.getCommandStack().undo();
		dsHandle = (DataSetHandle) design.findDataSet("Data Set2").getHandle(design.getRoot()); //$NON-NLS-1$
		assertEquals(1, countNum(dsHandle));

		// drop the simple data set from the tree, and test exception
		dsHandle = (DataSetHandle) design.findDataSet("Data Set1").getHandle(design.getRoot()); //$NON-NLS-1$
		dsHandle.drop();
		assertNull(design.findDataSet("Data Set1")); //$NON-NLS-1$

		designHandle.getCommandStack().undo();
		assertNotNull(design.findDataSet("Data Set1")); //$NON-NLS-1$
	}

	/**
	 * get count of clients of one <code>DataSetHandle</code>
	 * 
	 * @param dsHandle
	 * @return the size of the clients
	 */
	private int countNum(DataSetHandle dsHandle) {
		DataSet ds = (DataSet) dsHandle.getElement();
		return ds.getClientList().size();
	}

	/**
	 * Test cases for the bug 210341. It was caused by the bugs in ContentCommand.
	 * When ds1 is deleted, datasets property value of the joint data set should not
	 * be cleared. Only corresponding element reference should be removed.
	 * 
	 * @throws SemanticException
	 */

	public void testReferenceAfterDropDataSet() throws SemanticException {
		DataSetHandle ds1 = designHandle.findDataSet("DataSet1"); //$NON-NLS-1$
		DataSetHandle ds2 = designHandle.findDataSet("DataSet2"); //$NON-NLS-1$

		ds1.dropAndClear();

		// after delete the ds1, make sure only one corresponding element
		// reference value was removed.

		List datasets = dataSet.getListProperty(JointDataSetHandle.DATA_SETS_PROP);
		assertEquals(1, datasets.size());

		Iterator clients = ds2.clientsIterator();
		assertTrue(clients.hasNext());

		// reference to the ds2 is removed, so as to the ds2 back reference.

		dataSet.drop();
		clients = ds2.clientsIterator();
		assertFalse(clients.hasNext());

	}
}
