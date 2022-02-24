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
