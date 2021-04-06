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

import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.validators.GroupNameValidator;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>GroupNameValidator</code>.
 */

public class GroupNameValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>GroupNameValidator</code>.
	 * 
	 * @throws Exception if any exception
	 */

	public void testGroupNameValidator() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		TableHandle tableHandle = designHandle.getElementFactory().newTableItem("table1"); //$NON-NLS-1$
		designHandle.addValidationListener(listener);
		designHandle.getBody().add(tableHandle);

		GroupHandle groupHandle1 = tableHandle.getElementFactory().newTableGroup();
		GroupHandle groupHandle2 = tableHandle.getElementFactory().newTableGroup();

		groupHandle1.setName("group1"); //$NON-NLS-1$
		assertFalse(listener.hasError(tableHandle, GroupNameValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME));

		groupHandle2.setName("group1"); //$NON-NLS-1$
		assertFalse(listener.hasError(tableHandle, GroupNameValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME));

		tableHandle.getGroups().add(groupHandle1);
		assertFalse(listener.hasError(tableHandle, GroupNameValidator.getInstance().getName(),
				SemanticError.DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME));

		// Note: Currently, the group with duplicate group name can not be
		// added.
		//
		// tableHandle.getGroups( ).add( groupHandle2 );
		// assertTrue( listener.hasError( tableHandle, GroupNameValidator
		// .getInstance( ).getName( ),
		// SemanticError.DESIGN_EXCEPTION_DUPLICATE_GROUP_NAME ) );

	}
}