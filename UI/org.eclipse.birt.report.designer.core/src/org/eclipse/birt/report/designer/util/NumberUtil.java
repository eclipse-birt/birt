/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.util;

import java.text.ParseException;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class NumberUtil {
	private static final NumberFormat format = NumberFormat.getInstance(ULocale.getDefault());

	public static String double2LocaleNum(double number) {
		return format.format(number);
	}

	public Number localeNum2Num(String localNum) {
		try {
			return format.parse(localNum);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return Double.NaN;
		}
	}
}
