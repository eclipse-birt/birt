/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.presentation.aggregation.parameter;

import org.eclipse.birt.report.service.api.ParameterDefinition;

/**
 * Fragment help rendering scalar parameter.
 * <p>
 *
 * @see org.eclipse.birt.report.presentation.aggregation.BaseFragment
 */
public class CheckboxParameterFragment extends ScalarParameterFragment {
	/**
	 * Protected constructor.
	 *
	 * @param parameter parameter definition reference.
	 */
	public CheckboxParameterFragment(ParameterDefinition parameter) {
		super(parameter);
	}
}
