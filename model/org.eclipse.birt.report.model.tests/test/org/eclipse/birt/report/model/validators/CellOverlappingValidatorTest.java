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

package org.eclipse.birt.report.model.validators;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.validators.CellOverlappingValidator;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>CellOverlappingValidator</code>.
 */

public class CellOverlappingValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>CellOverlappingValidator</code>.
	 * 
	 * @throws Exception if any exception
	 */

	public void testCellOverlappingValidator() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		GridHandle gridHandle = designHandle.getElementFactory().newGridItem("grid1"); //$NON-NLS-1$
		designHandle.getBody().add(gridHandle);

		RowHandle rowHandle1 = gridHandle.getElementFactory().newTableRow(2);
		designHandle.addValidationListener(listener);

		// Add one row without error

		gridHandle.getRows().add(rowHandle1);
		assertFalse(listener.hasError(rowHandle1, CellOverlappingValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS));

		CellHandle cellHandle1 = (CellHandle) rowHandle1.getCells().get(0);
		CellHandle cellHandle2 = (CellHandle) rowHandle1.getCells().get(1);

		// Set column and columnSpan

		cellHandle1.setColumn(1);
		assertFalse(listener.hasError(rowHandle1, CellOverlappingValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS));

		cellHandle1.setColumnSpan(2);
		assertFalse(listener.hasError(rowHandle1, CellOverlappingValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS));

		cellHandle2.setColumn(2);
		assertTrue(listener.hasError(rowHandle1, CellOverlappingValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS));

		cellHandle2.setColumnSpan(2);
		assertTrue(listener.hasError(rowHandle1, CellOverlappingValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_OVERLAPPING_CELLS));
	}

}