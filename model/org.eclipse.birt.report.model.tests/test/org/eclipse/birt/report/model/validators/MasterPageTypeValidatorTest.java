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
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.validators.MasterPageTypeValidator;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>MasterPageTypeValidator</code>.
 */

public class MasterPageTypeValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>MasterPageTypeValidator</code>.
	 * 
	 * @throws Exception if any exception
	 */

	public void testTriggers() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		SimpleMasterPageHandle pageHandle = designHandle.getElementFactory().newSimpleMasterPage("masterPage1"); //$NON-NLS-1$
		designHandle.getMasterPages().add(pageHandle);

		designHandle.addValidationListener(listener);

		assertEquals(DesignChoiceConstants.PAGE_SIZE_US_LETTER, pageHandle.getPageType());

		// Width can't be set.

		DimensionHandle width = pageHandle.getWidth();
		assertDimensionUnsettable(width, "8in"); //$NON-NLS-1$

		// Height can't be set.

		DimensionHandle height = pageHandle.getHeight();
		assertDimensionUnsettable(height, "10in"); //$NON-NLS-1$

		// To set type to custom clears error

		pageHandle.setPageType(DesignChoiceConstants.PAGE_SIZE_CUSTOM);
		assertFalse(listener.hasError(pageHandle, MasterPageTypeValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE));
	}

	/**
	 * Checks if set a value by a dimension handle will get exception.
	 * 
	 * @param handle the dimension handle
	 * @throws SemanticException if the string value is not valid for the property
	 *                           or member.
	 * 
	 */
	private void assertDimensionUnsettable(DimensionHandle handle, String value) throws SemanticException {
		try {
			handle.setStringValue(value);
			fail();
		} catch (SemanticError expected) {
			assertEquals(expected.getErrorCode(), SemanticError.DESIGN_EXCEPTION_CANNOT_SPECIFY_PAGE_SIZE);
		}
	}

}
