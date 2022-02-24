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

package org.eclipse.birt.report.model.api.validators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the data set name of scalar parameter is required.
 * 
 * <h3>Rule</h3> The rule is that DATASET_NAME_PROP is required when
 * LABEL_EXPR_PROP or VALUE_EXPR_PROP is provided.
 * 
 * <h3>Applicability</h3> This validator is only applied to
 * <code>ScalarParameter</code>.
 * 
 */

public class DataSetNameRequiredValidatorImpl extends AbstractElementValidator {

	private final static DataSetNameRequiredValidatorImpl instance = new DataSetNameRequiredValidatorImpl();

	/**
	 * Returns the singleton validator instance.
	 * 
	 * @return the validator instance
	 */

	public static DataSetNameRequiredValidatorImpl getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.validators.core.AbstractElementValidator
	 * #validate(org.eclipse.birt.report.model.elements.ReportDesign,
	 * org.eclipse.birt.report.model.core.DesignElement)
	 */

	public List validate(Module module, DesignElement element) {
		if (!(element instanceof ScalarParameter))
			return Collections.EMPTY_LIST;

		return doValidate(module, (ScalarParameter) element);
	}

	private List doValidate(Module module, ScalarParameter toValidate) {
		List list = new ArrayList();

		String labelExpr = toValidate.getStringProperty(module, IAbstractScalarParameterModel.LABEL_EXPR_PROP);
		String valueExpr = toValidate.getStringProperty(module, IAbstractScalarParameterModel.VALUE_EXPR_PROP);

		if (toValidate.getContainer() instanceof CascadingParameterGroup)
			return list;

		if (!StringUtil.isBlank(labelExpr) || !StringUtil.isBlank(valueExpr)) {
			String dataSetName = toValidate.getStringProperty(module, IAbstractScalarParameterModel.DATASET_NAME_PROP);

			if (StringUtil.isBlank(dataSetName))
				list.add(new PropertyValueException(toValidate, IAbstractScalarParameterModel.DATASET_NAME_PROP, null,
						PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED));
		}

		return list;
	}

}
