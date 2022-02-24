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

package org.eclipse.birt.report.model.util;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;

/**
 * Test cases for XPathUtil.
 * 
 */

public class XPathUtilTest extends BaseTestCase {

	private static final String INPUT_FILE = "XPathUtilTest.xml"; //$NON-NLS-1$

	/**
	 * Test cases to get XPath string from element, slot and structure.
	 * 
	 * @throws Exception
	 */

	public void testGetXPath() throws Exception {
		openDesign(INPUT_FILE);

		SlotHandle tmpSlot = designHandle.getBody();
		assertEquals("/report/body", XPathUtil.getXPath(tmpSlot)); //$NON-NLS-1$

		tmpSlot = designHandle.findElement("My table1 nested label1") //$NON-NLS-1$
				.getContainerSlotHandle();

		assertEquals("/report/body/table[@id=\"10\"]/detail/row[@id=\"15\"]/cell[@id=\"17\"][@slotName=\"content\"]", //$NON-NLS-1$
				XPathUtil.getXPath(tmpSlot));

		ListHandle list = (ListHandle) designHandle.findElement("My list1"); //$NON-NLS-1$
		assertEquals("/report/body/list[@id=\"21\"]/header", //$NON-NLS-1$
				XPathUtil.getXPath(list.getHeader()));

		PropertyHandle propHandle = designHandle.getPropertyHandle(IModuleModel.IMAGES_PROP);
		assertEquals("/report/list-property[@name=\"images\"]", //$NON-NLS-1$
				XPathUtil.getXPath(propHandle));

		propHandle = designHandle.findParameter("Param 1").getPropertyHandle( //$NON-NLS-1$
				DesignElement.NAME_PROP);
		assertEquals("/report/parameters/scalar-parameter[@id=\"2\"]/@name", //$NON-NLS-1$
				XPathUtil.getXPath(propHandle));

		propHandle = designHandle.findElement("My text1").getPropertyHandle( //$NON-NLS-1$
				ITextItemModel.CONTENT_PROP);
		assertEquals("/report/body/text[@id=\"19\"]/text-property[@name=\"content\"]", //$NON-NLS-1$
				XPathUtil.getXPath(propHandle));

		propHandle = designHandle.findElement("My text1").getPropertyHandle( //$NON-NLS-1$
				ITextItemModel.CONTENT_RESOURCE_KEY_PROP);
		assertEquals("/report/body/text[@id=\"19\"]/text-property[@name=\"content\"]/@key", //$NON-NLS-1$
				XPathUtil.getXPath(propHandle));

		EmbeddedImageHandle image = (EmbeddedImageHandle) designHandle.imagesIterator().next();

		assertEquals("/report/list-property[@name=\"images\"]/structure[1]", //$NON-NLS-1$
				XPathUtil.getXPath(image));

		OdaDataSetHandle tmpDataSet = (OdaDataSetHandle) designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		assertEquals("/report/data-sets/oda-data-set[@id=\"7\"]", XPathUtil.getXPath(tmpDataSet)); //$NON-NLS-1$

		ExtendedItemHandle tmpMatrix = (ExtendedItemHandle) designHandle.findElement("matrix1"); //$NON-NLS-1$
		assertEquals("/report/body/extended-item[@id=\"20\"]", XPathUtil //$NON-NLS-1$
				.getXPath(tmpMatrix));
		// test extension model property handle
		propHandle = tmpMatrix.getPropertyHandle("type"); //$NON-NLS-1$
		assertNotNull(propHandle);
		assertEquals("/report/body/extended-item[@id=\"20\"]/property[@name=\"type\"]", XPathUtil.getXPath(propHandle)); //$NON-NLS-1$
		assertTrue(propHandle.equals(XPathUtil.getInstance(designHandle, XPathUtil.getXPath(propHandle))));

		OdaDataSetHandle dataSet = (OdaDataSetHandle) designHandle.findDataSet("firstDataSet"); //$NON-NLS-1$
		Iterator iter1 = dataSet.getCachedMetaDataHandle().getResultSet().iterator();
		ResultSetColumnHandle setColumn = (ResultSetColumnHandle) iter1.next();
		assertEquals(
				"/report/data-sets/oda-data-set[@id=\"7\"]/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]/structure[1]", //$NON-NLS-1$
				XPathUtil.getXPath(setColumn));

		setColumn = (ResultSetColumnHandle) iter1.next();
		assertEquals(
				"/report/data-sets/oda-data-set[@id=\"7\"]/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]/structure[2]", //$NON-NLS-1$
				XPathUtil.getXPath(setColumn));

		// cases on scripts

		LabelHandle label1 = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		propHandle = label1.getPropertyHandle(LabelHandle.ON_PREPARE_METHOD);
		String path = XPathUtil.getXPath(propHandle);
		assertEquals("/report/body/label[@id=\"24\"]/method[@name=\"onPrepare\"]", //$NON-NLS-1$
				path);

		// cases on extension elements that has content elements.

		ExtendedItemHandle tmpTable = (ExtendedItemHandle) designHandle.findElement("extensionTable"); //$NON-NLS-1$
		propHandle = tmpTable.getPropertyHandle("filter"); //$NON-NLS-1$
		FilterConditionElementHandle tmpFilter = (FilterConditionElementHandle) propHandle.get(0);
		assertEquals("/report/body/extended-item[@id=\"26\"]/property[@name=\"filter\"]/filter-condition-element", //$NON-NLS-1$
				XPathUtil.getXPath(tmpFilter));

		propHandle = tmpFilter.getPropertyHandle(StyleRule.VALUE1_MEMBER);
		path = XPathUtil.getXPath(propHandle);

		assertEquals(
				"/report/body/extended-item[@id=\"26\"]/property[@name=\"filter\"]/filter-condition-element/simple-property-list[@name=\"value1\"]", //$NON-NLS-1$
				path);

		path = XPathUtil.getXPath(propHandle, 0);
		assertEquals(
				"/report/body/extended-item[@id=\"26\"]/property[@name=\"filter\"]/filter-condition-element/simple-property-list[@name=\"value1\"]/value[1]", //$NON-NLS-1$
				path);
	}

	/**
	 * @throws Exception
	 */

	public void testGetInstance() throws Exception {
		openDesign(INPUT_FILE);

		Object retValue = XPathUtil.getInstance(designHandle, "/report"); //$NON-NLS-1$
		assertTrue(retValue instanceof ReportDesignHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/parameters/scalar-parameter"); //$NON-NLS-1$
		assertTrue(retValue instanceof ScalarParameterHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/body/image[@id=\"9\"]"); //$NON-NLS-1$
		assertTrue(retValue instanceof ImageHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/body/table/group/header/row"); //$NON-NLS-1$
		assertTrue(retValue instanceof RowHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/body/table/group/header/row/"); //$NON-NLS-1$
		assertTrue(retValue instanceof RowHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/body/table/group/header/row/*"); //$NON-NLS-1$
		assertTrue(retValue instanceof RowHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/body/table/group/header/row[@slotName=\"cells\"]"); //$NON-NLS-1$
		assertTrue(retValue instanceof SlotHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/body/list[@id=\"21\"]/header]"); //$NON-NLS-1$
		assertTrue(retValue instanceof SlotHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/list-property[@name=\"images\"]"); //$NON-NLS-1$
		assertTrue(retValue instanceof PropertyHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/parameters/scalar-parameter[@id=\"2\"]/@name"); //$NON-NLS-1$
		assertTrue(retValue instanceof PropertyHandle);
		assertEquals(DesignElement.NAME_PROP, ((PropertyHandle) retValue).getDefn().getName());

		retValue = XPathUtil.getInstance(designHandle,
				"/report/body/text[@id=\"19\"]/text-property[@name=\"content\"]"); //$NON-NLS-1$
		assertTrue(retValue instanceof PropertyHandle);
		assertEquals(ITextItemModel.CONTENT_PROP, ((PropertyHandle) retValue).getDefn().getName());

		retValue = XPathUtil.getInstance(designHandle,
				"/report/body/text[@id=\"19\"]/text-property[@name=\"content\"]/@key"); //$NON-NLS-1$
		assertTrue(retValue instanceof PropertyHandle);
		assertEquals(ITextItemModel.CONTENT_RESOURCE_KEY_PROP, ((PropertyHandle) retValue).getDefn().getName());

		retValue = XPathUtil.getInstance(designHandle, "/report/list-property[@name=\"images\"]/structure"); //$NON-NLS-1$
		assertTrue(retValue instanceof EmbeddedImageHandle);

		retValue = XPathUtil.getInstance(designHandle, "/report/list-property[@name=\"images\"]/structure[1]"); //$NON-NLS-1$
		assertTrue(retValue instanceof EmbeddedImageHandle);

		retValue = XPathUtil.getInstance(designHandle,
				"/report/data-sets/oda-data-set/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]/structure[1]"); //$NON-NLS-1$
		assertTrue(retValue instanceof ResultSetColumnHandle);
		ResultSetColumnHandle column = (ResultSetColumnHandle) retValue;
		assertEquals("name1", column.getColumnName()); //$NON-NLS-1$
		assertTrue(1 == column.getPosition().intValue());

		retValue = XPathUtil.getInstance(designHandle,
				"/report/data-sets/oda-data-set/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]/structure[2]"); //$NON-NLS-1$
		assertTrue(retValue instanceof ResultSetColumnHandle);
		column = (ResultSetColumnHandle) retValue;
		assertEquals("date1", column.getColumnName()); //$NON-NLS-1$
		assertTrue(2 == column.getPosition().intValue());

		retValue = XPathUtil.getInstance(designHandle,
				"/report/data-sets/oda-data-set/structure[@name=\"cachedMetaData\"]/list-property[@name=\"resultSet\"]"); //$NON-NLS-1$
		assertNull(retValue);

		// the instance for the data set.

		retValue = XPathUtil.getInstance(designHandle, "/report/data-sets/oda-data-set"); //$NON-NLS-1$
		assertTrue(retValue instanceof OdaDataSetHandle);

		// invalid test cases. Must avoid NPE.

		assertNull(XPathUtil.getInstance(designHandle, "*/")); //$NON-NLS-1$
		assertNull(XPathUtil.getInstance(designHandle, "/library")); //$NON-NLS-1$
		assertNull(XPathUtil.getInstance(designHandle, "/report/*/body")); //$NON-NLS-1$

		assertNull(XPathUtil.getInstance(designHandle, "/report/list-property[@name=\"images\"]/structure[3]")); //$NON-NLS-1$

		// cases on scripts

		retValue = XPathUtil.getInstance(designHandle, "/report/body/label[@id=\"24\"]/method[@name=\"onPrepare\"]"); //$NON-NLS-1$

		assertTrue(retValue instanceof PropertyHandle);
		assertEquals("\"prepare\"", ((PropertyHandle) retValue).getValue());//$NON-NLS-1$

		// make sure for invalid string, no exception

		retValue = XPathUtil.getInstance(designHandle, "<"); //$NON-NLS-1$
		assertNull(retValue);

		// cases on extension elements that has content elements.

		retValue = XPathUtil.getInstance(designHandle,
				"/report/body/extended-item[@id=\"26\"]/property[@name=\"filter\"]/filter-condition-element"); //$NON-NLS-1$
		assertTrue(retValue instanceof FilterConditionElementHandle);

		retValue = XPathUtil.getInstance(designHandle,
				"/report/body/extended-item[@id=\"26\"]/property[@name=\"filter\"]/filter-condition-element/simple-property-list[@name=\"value1\"]"); //$NON-NLS-1$
		assertTrue(retValue instanceof PropertyHandle);

		retValue = XPathUtil.getInstance(designHandle,
				"/report/body/extended-item[@id=\"26\"]/property[@name=\"filter\"]/filter-condition-element/simple-property-list[@name=\"value1\"]/value[1]"); //$NON-NLS-1$
		assertTrue(retValue instanceof Expression);
		assertEquals("value1 expression", retValue.toString()); //$NON-NLS-1$
	}
}
