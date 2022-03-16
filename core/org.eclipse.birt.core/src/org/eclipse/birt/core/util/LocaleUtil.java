/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.core.util;

import java.util.HashMap;
import java.util.Locale;

import com.ibm.icu.util.ULocale;

public class LocaleUtil {

	private static HashMap<Locale, ULocale> localeMap = new HashMap();

	public synchronized static ULocale forLocale(Locale locale) {
		if (localeMap.containsKey(locale)) {
			return localeMap.get(locale);
		} else {
			ULocale ulocale = ULocale.forLocale(locale);
			localeMap.put(locale, ulocale);
			return ulocale;
		}
	}
}
