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

package org.eclipse.birt.report.model.validators;

import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.validators.InconsistentColumnsValidator;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>InconsistentColumnValidator</code>.
 */

public class InconsistentColumnsValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>InconsistentColumnValidator</code>.
	 * 
	 * @throws Exception if any exception
	 */

	public void testTriggers() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		TableHandle tableHandle = designHandle.getElementFactory().newTableItem("table1"); //$NON-NLS-1$
		designHandle.addValidationListener(listener);
		designHandle.getBody().add(tableHandle);

		// Add three columns

		ColumnHandle columnHandle1 = tableHandle.getElementFactory().newTableColumn();
		ColumnHandle columnHandle2 = tableHandle.getElementFactory().newTableColumn();
		ColumnHandle columnHandle3 = tableHandle.getElementFactory().newTableColumn();
		ColumnHandle columnHandle4 = tableHandle.getElementFactory().newTableColumn();

		tableHandle.getColumns().add(columnHandle1);
		assertTrue(listener.hasError(tableHandle, InconsistentColumnsValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT));

		tableHandle.getColumns().add(columnHandle2);
		assertTrue(listener.hasError(tableHandle, InconsistentColumnsValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT));

		tableHandle.getColumns().add(columnHandle3);
		assertTrue(listener.hasError(tableHandle, InconsistentColumnsValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT));

		// Add three rows in header, detail and footer respectively.

		RowHandle rowHandle1 = tableHandle.getElementFactory().newTableRow(2);
		RowHandle rowHandle2 = tableHandle.getElementFactory().newTableRow(3);
		RowHandle rowHandle3 = tableHandle.getElementFactory().newTableRow(4);

		tableHandle.getHeader().add(rowHandle1);
		assertTrue(listener.hasError(tableHandle, InconsistentColumnsValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT));

		tableHandle.getDetail().add(rowHandle2);
		assertFalse(listener.hasError(tableHandle, InconsistentColumnsValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT));

		tableHandle.getFooter().add(rowHandle3);
		assertTrue(listener.hasError(tableHandle, InconsistentColumnsValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT));

		// Add additional column to table

		tableHandle.getColumns().add(columnHandle4);
		assertFalse(listener.hasError(tableHandle, InconsistentColumnsValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT));

		// Drop one column from table

		columnHandle2.dropAndClear();
		assertTrue(listener.hasError(tableHandle, InconsistentColumnsValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INCONSITENT_TABLE_COL_COUNT));
	}
}
