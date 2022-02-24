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

import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.validators.MasterPageMultiColumnValidator;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>MasterPageMultiColumnValidator</code>.
 */

public class MasterPageMultiColumnValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>MasterPageMultiColumnValidator</code>.
	 * 
	 * @throws Exception if any exception
	 */

	public void testTriggers() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		GraphicMasterPageHandle pageHandle = designHandle.getElementFactory().newGraphicMasterPage("masterPage1"); //$NON-NLS-1$
		designHandle.addValidationListener(listener);

		designHandle.getMasterPages().add(pageHandle);
		assertFalse(listener.hasError(pageHandle, MasterPageMultiColumnValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INVALID_MULTI_COLUMN));

		// Change page size to custom, and height = 15in, width = 10in

		pageHandle.setPageType(DesignChoiceConstants.PAGE_SIZE_CUSTOM);

		DimensionHandle height = pageHandle.getHeight();
		height.setStringValue("15in"); //$NON-NLS-1$

		DimensionHandle width = pageHandle.getWidth();
		width.setStringValue("10in"); //$NON-NLS-1$

		// Set column count and spacing.

		pageHandle.setColumnCount(3);
		assertFalse(listener.hasError(pageHandle, MasterPageMultiColumnValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INVALID_MULTI_COLUMN));

		DimensionHandle columnSpacing = pageHandle.getColumnSpacing();
		columnSpacing.setStringValue("6in"); //$NON-NLS-1$
		assertTrue(listener.hasError(pageHandle, MasterPageMultiColumnValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INVALID_MULTI_COLUMN));

		columnSpacing.setStringValue("1in"); //$NON-NLS-1$
		assertFalse(listener.hasError(pageHandle, MasterPageMultiColumnValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_INVALID_MULTI_COLUMN));
	}

}
