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