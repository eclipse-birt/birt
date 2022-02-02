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

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public interface IFormatter {

	/**
	 * format the value object into a string.
	 * 
	 * @param value
	 * @return
	 * @throws BirtException
	 */
	public String formatValue(Object value) throws BirtException;

	/**
	 * 
	 */
	static class DefaultFormatter implements IFormatter {

		private ULocale locale;

		public DefaultFormatter(ULocale locale) {
			this.locale = locale;
		}

		public String formatValue(Object value) throws BirtException {
			return DataTypeUtil.toString(value, locale);
		}
	}
}
