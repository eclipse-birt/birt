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

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.validators.CascadingParameterTypeValidator;
import org.eclipse.birt.report.model.api.validators.DataSetNameRequiredValidator;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;

/**
 * This class represents a scalar (single-value) parameter. Scalar parameters
 * can have selection lists. If the user enters no value for a parameter, then
 * the default value is used. If there is no default value, then BIRT checks if
 * nulls are allowed. If so, the value of the parameter is null. If nulls are
 * not allowed, then the user must enter a value.
 *
 *
 */

public class ScalarParameter extends AbstractScalarParameter implements IScalarParameterModel {

	/**
	 * The default constructor.
	 */

	public ScalarParameter() {
	}

	/**
	 * Constructs the scalar parameter with a required and unique name.
	 *
	 * @param theName the required name
	 */

	public ScalarParameter(String theName) {
		super(theName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#apply(org.eclipse.birt
	 * .report.model.elements.ElementVisitor)
	 */

	@Override
	public void apply(ElementVisitor visitor) {
		visitor.visitScalarParameter(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getElementName()
	 */

	@Override
	public String getElementName() {
		return ReportDesignConstants.SCALAR_PARAMETER_ELEMENT;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#getHandle(org.eclipse
	 * .birt.report.model.elements.ReportDesign)
	 */

	@Override
	public DesignElementHandle getHandle(Module module) {
		return handle(module);
	}

	/**
	 * Returns an API handle for this element.
	 *
	 * @param module the report design
	 * @return an API handle for this element
	 */

	public ScalarParameterHandle handle(Module module) {
		if (handle == null) {
			handle = new ScalarParameterHandle(module, this);
		}
		return (ScalarParameterHandle) handle;
	}

	/**
	 * Performs semantic check for the scalar parameter. That is, if the dynamic
	 * list tag exists and this tag has attributes for value or label column, it
	 * must have a property for the data set name.
	 *
	 * @see org.eclipse.birt.report.model.core.DesignElement#validate(module)
	 */

	@Override
	public List validate(Module module) {
		List list = super.validate(module);

		list.addAll(DataSetNameRequiredValidator.getInstance().validate(module, this));
		list.addAll(CascadingParameterTypeValidator.getInstance().validate(module, this));

		return list;
	}

}
