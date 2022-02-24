/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.core.format;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;

/**
 * 
 */

public class LocaleNeutralFormatter implements IFormatter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.format.IFormatter#formatValue(java.lang.Object)
	 */
	public String formatValue(Object value) throws BirtException {
		return DataTypeUtil.toLocaleNeutralString(value);
	}

}
