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

public class DataSetNameRequiredValidator extends DataSetNameRequiredValidatorImpl {

	private final static DataSetNameRequiredValidator instance = new DataSetNameRequiredValidator();

	/**
	 * Returns the singleton validator instance.
	 *
	 * @return the validator instance
	 */

	public static DataSetNameRequiredValidator getInstance() {
		return instance;
	}

}
