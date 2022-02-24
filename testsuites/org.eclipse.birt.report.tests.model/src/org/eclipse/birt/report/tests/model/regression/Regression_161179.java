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

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.StyleException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Bug Description:</b>
 * <p>
 * According to bug 156450, we need to support border style for table rows. And
 * this enhancement needs Engine team, Designer team and Model team work
 * together to implement it.
 * <p>
 * <b>Test Description:</b>
 * <ol>
 * <li>Set/Get border to table row
 * <li>Set border to style
 * <li>Set border to container
 * <li>Set/Get border to table row in library
 * <li>Set border to style in library
 * </ol>
 */
public class Regression_161179 extends BaseTestCase {

	private final static String REPORT = "regression_161179.xml"; //$NON-NLS-1$
	private final static String REPORT1 = "regression_161179_1.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		// copyResource_INPUT( REPORT , REPORT );
		// copyResource_INPUT( REPORT1 , REPORT1 );
		copyInputToFile(INPUT_FOLDER + "/" + REPORT);
		copyGoldenToFile(INPUT_FOLDER + "/" + REPORT1);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * test set/get border properties on table row
	 * 
	 * @throws SemanticException
	 */
	public void test_regression_161179() throws SemanticException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table", 3, 1, 1, 1); //$NON-NLS-1$
		designHandle.getBody().add(table);
		RowHandle row = (RowHandle) table.getHeader().get(0);

		row.setStringProperty(Style.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row.setStringProperty(Style.BORDER_TOP_COLOR_PROP, "red"); //$NON-NLS-1$
		row.setStringProperty(Style.BORDER_TOP_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_MEDIUM);

		assertEquals(DesignChoiceConstants.LINE_STYLE_SOLID, row.getStringProperty(Style.BORDER_TOP_STYLE_PROP));
		assertEquals("red", row //$NON-NLS-1$
				.getStringProperty(Style.BORDER_TOP_COLOR_PROP));
		assertEquals(DesignChoiceConstants.LINE_WIDTH_MEDIUM, row.getStringProperty(Style.BORDER_TOP_WIDTH_PROP));

	}

	/**
	 * Set border to style Set border to container
	 * 
	 * @throws DesignFileException
	 * @throws StyleException
	 */
	public void test_regression_161179_1() throws DesignFileException, StyleException {
		openDesign(REPORT);
		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		RowHandle row = (RowHandle) table.getDetail().get(0);

		// set border to style
		row.setStyleName("style"); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.LINE_STYLE_DOTTED, row.getStringProperty(Style.BORDER_RIGHT_STYLE_PROP));
		assertEquals(DesignChoiceConstants.LINE_WIDTH_THIN, row.getStringProperty(Style.BORDER_RIGHT_WIDTH_PROP));
		assertEquals("black", row //$NON-NLS-1$
				.getStringProperty(Style.BORDER_RIGHT_COLOR_PROP));

		// Border is not a cascading style property, only get default value for
		// border properties on cell
		RowHandle headerrow = (RowHandle) table.getHeader().get(0);
		CellHandle cell = (CellHandle) headerrow.getCells().get(0);

		assertEquals("none", cell //$NON-NLS-1$
				.getStringProperty(Style.BORDER_BOTTOM_STYLE_PROP));
		assertEquals(DesignChoiceConstants.LINE_WIDTH_MEDIUM, cell.getStringProperty(Style.BORDER_BOTTOM_WIDTH_PROP));
		assertEquals("black", cell //$NON-NLS-1$
				.getStringProperty(Style.BORDER_BOTTOM_COLOR_PROP));
	}

	/**
	 * Set/Get border to table row in library Set border to style in library
	 * 
	 * @throws DesignFileException
	 * @throws SemanticException
	 */
	public void test_regression_161179_2() throws DesignFileException, SemanticException {
		openDesign(REPORT1);
		libraryHandle = designHandle.getLibrary("LIB"); //$NON-NLS-1$
		assertNotNull(libraryHandle);
		TableHandle table = (TableHandle) libraryHandle.findElement("table"); //$NON-NLS-1$
		assertNotNull(table);
		TableHandle newtable = (TableHandle) designHandle.getElementFactory().newElementFrom(table, "newTable"); //$NON-NLS-1$
		designHandle.getBody().add(newtable);

		// set/get border to table row in library
		RowHandle row = (RowHandle) newtable.getHeader().get(0);

		assertEquals(DesignChoiceConstants.LINE_STYLE_DASHED, row.getStringProperty(Style.BORDER_TOP_STYLE_PROP));
		assertEquals(DesignChoiceConstants.LINE_WIDTH_THIN, row.getStringProperty(Style.BORDER_TOP_WIDTH_PROP));
		assertEquals("blue", row //$NON-NLS-1$
				.getStringProperty(Style.BORDER_TOP_COLOR_PROP));

		// set border to style in library
		TableHandle table1 = designHandle.getElementFactory().newTableItem("table1", 3, 1, 1, 1); //$NON-NLS-1$
		RowHandle detailrow = (RowHandle) table1.getDetail().get(0);
		designHandle.setThemeName("LIB.theme1"); //$NON-NLS-1$
		detailrow.setStyleName("style"); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.LINE_STYLE_DOUBLE,
				detailrow.getStringProperty(Style.BORDER_LEFT_STYLE_PROP));
		assertEquals(DesignChoiceConstants.LINE_WIDTH_THICK, detailrow.getStringProperty(Style.BORDER_LEFT_WIDTH_PROP));
		assertEquals("green", detailrow //$NON-NLS-1$
				.getStringProperty(Style.BORDER_LEFT_COLOR_PROP));

	}

}
