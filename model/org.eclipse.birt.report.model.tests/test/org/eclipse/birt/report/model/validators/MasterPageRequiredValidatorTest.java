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

import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.validators.MasterPageRequiredValidator;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>MasterPageRequiredValidator</code>.
 */

public class MasterPageRequiredValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>MasterPageRequiredValidator</code>.
	 *
	 * @throws Exception if any exception
	 */

	public void testTriggers() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		designHandle.addValidationListener(listener);

		SimpleMasterPageHandle pageHandle = designHandle.getElementFactory().newSimpleMasterPage("masterPage1"); //$NON-NLS-1$
		designHandle.getMasterPages().add(pageHandle);
		assertFalse(listener.hasError(designHandle, MasterPageRequiredValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE));

		pageHandle.dropAndClear();
		assertTrue(listener.hasError(designHandle, MasterPageRequiredValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE));

		designHandle.getMasterPages().add(pageHandle);
		assertFalse(listener.hasError(designHandle, MasterPageRequiredValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE));
	}

}
