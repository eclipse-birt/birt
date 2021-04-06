/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Bug Description:</b>
 * <p>
 * Need support page-break-inside property on report items. Also need support
 * page-break-inside on rows, refer to 156471,156471
 * <p>
 * <b>Test Description:</b>
 * <ol>
 * <li>Set/Get page-break-inside on report item
 * <li>Set/Get page-break-inside on listing group
 * <li>Set/Get page-break-inside on table row
 * <li>Set/Get page-break-inside on table column
 * <li>Set page-break-inside to style
 * <li>Set page-break-inside on selector
 * <li>Set/Get page-break-inside on table row in library
 * <li>Set page-break-inside to style in library
 * </ol>
 */
public class Regression_161383 extends BaseTestCase {

	private final static String REPORT = "regression_161383.xml"; //$NON-NLS-1$
	private final static String REPORT1 = "regression_161383_1.xml"; //$NON-NLS-1$
	private final static String REPORT2 = "regression_161383_2.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( REPORT , REPORT );
		// copyResource_INPUT( REPORT1 , REPORT1 );
		// copyResource_INPUT( REPORT2 , REPORT2 );
		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
		copyInputToFile(INPUT_FOLDER + "/" + REPORT1);
		copyInputToFile(INPUT_FOLDER + "/" + REPORT2);

	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * Set/Get page-break-inside on report item, listing group, table row, table
	 * column
	 * 
	 * @throws SemanticException
	 */
	public void test_regression_161383() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();

		// Set/Get page-break-inside on Label
		LabelHandle label = factory.newLabel("label");
		designHandle.getBody().add(label);
		label.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				label.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on text
		TextItemHandle text = factory.newTextItem("text");
		designHandle.getBody().add(text);
		text.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				text.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on Image
		ImageHandle image = factory.newImage("image");
		designHandle.getBody().add(image);
		image.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				image.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on Data
		DataItemHandle data = factory.newDataItem("data");
		designHandle.getBody().add(data);
		data.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				data.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on Dynamic text
		TextDataHandle dtext = factory.newTextData("dtext");
		designHandle.getBody().add(dtext);
		dtext.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				dtext.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on grid
		GridHandle grid = factory.newGridItem("grid");
		designHandle.getBody().add(grid);
		grid.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				grid.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on table
		TableHandle table = factory.newTableItem("table");
		designHandle.getBody().add(table);
		table.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				table.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on list
		DesignElementHandle list = factory.newList("list");
		designHandle.getBody().add(list);
		list.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				list.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on chart
		// TODO: add it after resolve problem with extension
		// ExtendedItemHandle chart = factory.newExtendedItem( "chart", "Chart"
		// );
		// assertNotNull(chart);
		// designHandle.getBody( ).add( chart );
		// chart.setStringProperty(
		// Style.PAGE_BREAK_INSIDE_PROP,
		// DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID );
		//
		// assertEquals( DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID, chart
		// .getStringProperty( Style.PAGE_BREAK_INSIDE_PROP ) );

		// Set/Get page-break-inside on list group
		ListHandle list1 = factory.newList("list");
		designHandle.getBody().add(list1);
		ListGroupHandle listgroup = factory.newListGroup();
		list1.getGroups().add(listgroup);
		ListGroupHandle group = (ListGroupHandle) list1.getGroups().get(0);
		group.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		group.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				group.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on table group
		TableHandle table3 = factory.newTableItem("table");
		designHandle.getBody().add(table3);
		TableGroupHandle tablegroup = factory.newTableGroup();
		table3.getGroups().add(tablegroup);
		TableGroupHandle group1 = (TableGroupHandle) table3.getGroups().get(0);
		group1.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		group1.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				group1.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on table row
		TableHandle table1 = factory.newTableItem("table1", 3, 1, 1, 1);//$NON-NLS-1$
		designHandle.getBody().add(table1);

		RowHandle row = (RowHandle) table1.getDetail().get(0);
		row.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				row.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set/Get page-break-inside on table column
		TableHandle table2 = factory.newTableItem("table2", 3, 1, 1, 1);//$NON-NLS-1$
		designHandle.getBody().add(table2);

		ColumnHandle column = (ColumnHandle) table2.getColumns().get(0);
		assertNotNull(column);
		try {
			column.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyNameException.DESIGN_EXCEPTION_PROPERTY_NAME_INVALID, e.getErrorCode());
		}

	}

	/**
	 * Set page-break-inside to style and selector
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_161383_1() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		RowHandle row1 = (RowHandle) table.getDetail().get(0);
		RowHandle row2 = (RowHandle) table.getHeader().get(0);

		// Set page-break-inside to style
		StyleHandle style1 = (StyleHandle) designHandle.findStyle("style1");
		style1.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);
		row1.setStyleName("style1");

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				row1.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

		// Set page-break-inside on selector
		StyleHandle style2 = (StyleHandle) designHandle.findStyle("table-header");
		style2.setStringProperty(Style.PAGE_BREAK_INSIDE_PROP, DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID);
		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				row2.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

	}

	/**
	 * Set/Get page-break-inside on table row in library and to style in library
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_161383_2() throws DesignFileException, SemanticException {
		openDesign(REPORT1);

		libraryHandle = designHandle.getLibrary("LIB"); //$NON-NLS-1$
		assertNotNull(libraryHandle);
		TableHandle table = (TableHandle) libraryHandle.findElement("table"); //$NON-NLS-1$
		assertNotNull(table);

		TableHandle newtable = (TableHandle) designHandle.getElementFactory().newElementFrom(table, "newTable"); //$NON-NLS-1$
		designHandle.getBody().add(newtable);
		RowHandle row = (RowHandle) newtable.getDetail().get(0);

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				row.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

	}

	/**
	 * Set/Get page-break-inside to style in library
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_161383_3() throws DesignFileException, SemanticException {
		openDesign(REPORT2);

		// Set page-break-inside to style in library
		TableHandle table = (TableHandle) designHandle.findElement("table");
		assertNotNull(table);

		designHandle.setThemeName("LIB2.theme1"); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.PAGE_BREAK_INSIDE_AVOID,
				table.getStringProperty(Style.PAGE_BREAK_INSIDE_PROP));

	}
}
