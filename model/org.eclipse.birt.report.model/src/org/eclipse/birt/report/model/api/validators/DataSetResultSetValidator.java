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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.validators.AbstractElementValidator;

/**
 * Validates the result set of the given data set has at least one column.
 *
 * <h3>Rule</h3> The rule is that the result set of the given data set has at
 * least one column.
 *
 * <h3>Applicability</h3> This validator is only applied to <code>DataSet</code>
 * .
 *
 * @deprecated since birt 2.2
 */

@Deprecated
public class DataSetResultSetValidator extends AbstractElementValidator {

	private final static DataSetResultSetValidator instance = new DataSetResultSetValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static DataSetResultSetValidator getInstance() {
		return instance;
	}

	/**
	 * Validates whether the result set of the given data set has no column defined.
	 *
	 * @param module  the module
	 * @param element the data set to validate
	 * @return error list, each of which is the instance of
	 *         <code>SemanticException</code>.
	 */

	@Override
	public List<SemanticException> validate(Module module, DesignElement element) {
		if (!(element instanceof SimpleDataSet)) {
			return Collections.emptyList();
		}

		return doValidate(module, (SimpleDataSet) element);
	}

	private List<SemanticException> doValidate(Module module, SimpleDataSet toValidate) {

		List<SemanticException> list = new ArrayList<>();

		List<Object> columns = (List) toValidate.getProperty(module, IDataSetModel.RESULT_SET_PROP);
		if (columns != null && columns.size() == 0) {
			list.add(new SemanticError(toValidate, SemanticError.DESIGN_EXCEPTION_AT_LEAST_ONE_COLUMN));
		}
		return list;
	}
}
