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

import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

/**
 * Tests <code>StructureListValidator</code>.
 */

public class StructureListValidatorTest extends ValidatorTestCase {

	MyListener listener = new MyListener();

	/**
	 * Tests <code>StructureListValidator</code>.
	 * 
	 * @throws Exception if any exception
	 */

	public void testStructureListValidator() throws Exception {
		createDesign();
		MetaDataDictionary.getInstance().setUseValidationTrigger(true);

		OdaDataSetHandle dataSetHandle = designHandle.getElementFactory().newOdaDataSet("dataSet1"); //$NON-NLS-1$
		designHandle.getDataSets().add(dataSetHandle);
		designHandle.addValidationListener(listener);

		ParamBinding p1 = StructureFactory.createParamBinding();
		ParamBinding p2 = StructureFactory.createParamBinding();
		p1.setParamName("p1"); //$NON-NLS-1$
		p2.setParamName("p1"); //$NON-NLS-1$

		// Note: the structure with such error can not be added into structure
		// list.
		// PropertyHandle bindingHandle = dataSetHandle
		// .getPropertyHandle( OdaDataSet.PARAM_BINDINGS_PROP );

		// bindingHandle.addItem( p1 );
		// assertNull( listener.exception );
		// bindingHandle.addItem( p2 );
		// assertNotNull( listener.exception );
	}

}
