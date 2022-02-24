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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.core.commands.AbstractParameterValueConverter;
import org.eclipse.core.commands.ParameterValueConversionException;

/**
 *
 */

public class StringConverter extends AbstractParameterValueConverter {

	public StringConverter() {
	}

	@Override
	public Object convertToObject(String parameterValue) throws ParameterValueConversionException {
		return parameterValue;
	}

	@Override
	public String convertToString(Object parameterValue) throws ParameterValueConversionException {
		return parameterValue != null ? parameterValue.toString() : ""; //$NON-NLS-1$
	}

}
