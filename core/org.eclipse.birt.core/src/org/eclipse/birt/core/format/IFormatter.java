/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	String formatValue(Object value) throws BirtException;

	/**
	 *
	 */
	static class DefaultFormatter implements IFormatter {

		private ULocale locale;

		public DefaultFormatter(ULocale locale) {
			this.locale = locale;
		}

		@Override
		public String formatValue(Object value) throws BirtException {
			return DataTypeUtil.toString(value, locale);
		}
	}
}
