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

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * <b>Regression description:</b>
 * </p>
 * NPE throwed when I copy an empty table(no layout)
 * </p>
 * <b>Test description:</b>
 * <p>
 * <b>Steps:</b>
 * <ol>
 * <li>TableHandle table = factory.newTableItem( "table1" );
 * <li>libHandle.getComponents( ).add( table );
 * <li>TableItem copiedTable = (TableItem)libHandle.findElement( "table1"
 * ).copy( );
 * </ol>
 * NPE throwed when executing line 3
 * <p>
 * <b>Test description:</b>
 * <p>
 * Follow the steps, ensure no exception throwed.
 */
public class Regression_152704 extends BaseTestCase {

	/**
	 * @throws NameException
	 * @throws ContentException
	 */
	public void test_regression_112910() throws NameException, ContentException {
		SessionHandle sessionHandle = new DesignEngine(new DesignConfig()).newSessionHandle(ULocale.ENGLISH);
		LibraryHandle libHandle = sessionHandle.createLibrary();

		ElementFactory factory = libHandle.getElementFactory();
		TableHandle table = factory.newTableItem("table1"); //$NON-NLS-1$
		libHandle.getComponents().add(table);
		libHandle.findElement("table1") //$NON-NLS-1$
				.copy();

	}
}
