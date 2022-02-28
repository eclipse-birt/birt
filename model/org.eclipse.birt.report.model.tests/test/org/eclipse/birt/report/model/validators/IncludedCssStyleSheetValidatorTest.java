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

import java.util.List;

import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>IncludedCssStyleSheetValidatorTest</code>.
 */

public class IncludedCssStyleSheetValidatorTest extends ValidatorTestCase {

	/**
	 * Validates the input file name of the included Css Style Sheet.
	 *
	 * @throws Exception
	 */
	public void testIncludedCssStyleSheetValidator() throws Exception {
		MetaDataDictionary.getInstance().setUseValidationTrigger(false);
		openDesign("IncludedCssStyleSheetValidatorTest.xml"); //$NON-NLS-1$

		List<ErrorDetail> list = design.getErrorList();

		assertTrue(hasException(list, CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND));
	}

	private boolean hasException(List<ErrorDetail> errors, String errorCode) {
		for (ErrorDetail error : errors) {
			if (error.getErrorCode().equals(errorCode)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Validates the input file name of the included Css Style Sheet which located
	 * in theme.
	 *
	 * @throws Exception
	 */
	public void testIncludedCssStyleSheetValidatorInTheme() throws Exception {
		MetaDataDictionary.getInstance().setUseValidationTrigger(false);
		openLibrary("IncludedCssStyleSheetValidatorInThemeTest.xml"); //$NON-NLS-1$

		List<ErrorDetail> list = libraryHandle.getModule().getErrorList();

		assertTrue(hasException(list, CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND));
	}
}
