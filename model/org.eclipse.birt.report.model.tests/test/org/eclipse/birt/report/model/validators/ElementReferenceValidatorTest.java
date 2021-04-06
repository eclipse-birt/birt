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

import java.util.List;

import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Cases for ElementReferenceValidator.
 */

public class ElementReferenceValidatorTest extends ValidatorTestCase {

	/**
	 * If the element is in template parameter definition slot, should not check the
	 * validation of element references.
	 * 
	 * @throws Exception
	 */

	public void testElementReferencesInTemplateDefinition() throws Exception {
		MetaDataDictionary.getInstance().setUseValidationTrigger(false);

		openDesign("ElementReferenceValidatorTest.xml"); //$NON-NLS-1$

		List list = designHandle.getErrorList();

		assertEquals(0, list.size());
	}
}
