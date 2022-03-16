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

package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;

/**
 * Abstract class for the various kinds of scalar parameters.
 *
 */
public abstract class AbstractScalarParameterImpl extends Parameter implements IAbstractScalarParameterModel {

	/**
	 * Default constructor.
	 */

	protected AbstractScalarParameterImpl() {
	}

	/**
	 * Constructs the abstract scalar parameter element with a required and unique
	 * name.
	 *
	 * @param theName the required name
	 */

	protected AbstractScalarParameterImpl(String theName) {
		super(theName);
	}

	/**
	 * Returns the data set element, if any, for this element.
	 *
	 * @param module the report design of the report item
	 *
	 * @return the data set element defined on this cascading parameter group.
	 */

	public DataSet getDataSetElement(Module module) {
		ElementRefValue dataSetRef = (ElementRefValue) getProperty(module, DATASET_NAME_PROP);
		if (dataSetRef == null) {
			return null;
		}
		return (DataSet) dataSetRef.getElement();
	}

}
