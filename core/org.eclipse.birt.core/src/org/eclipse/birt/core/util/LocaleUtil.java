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
