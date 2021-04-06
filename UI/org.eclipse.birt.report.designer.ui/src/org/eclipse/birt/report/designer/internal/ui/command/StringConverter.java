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

package org.eclipse.birt.report.designer.internal.ui.command;

import org.eclipse.core.commands.AbstractParameterValueConverter;
import org.eclipse.core.commands.ParameterValueConversionException;

/**
 * 
 */

public class StringConverter extends AbstractParameterValueConverter {

	public StringConverter() {
	}

	public Object convertToObject(String parameterValue) throws ParameterValueConversionException {
		return parameterValue;
	}

	public String convertToString(Object parameterValue) throws ParameterValueConversionException {
		return parameterValue != null ? parameterValue.toString() : ""; //$NON-NLS-1$
	}

}
