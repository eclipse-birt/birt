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
package org.eclipse.birt.report.engine.emitter.ods;

import java.util.HashMap;
import java.util.Map;

import com.ibm.icu.util.ULocale;

public class DateTimeUtil
{

	static Map<ULocale, String> locale2Code = new HashMap<ULocale, String>( );

	static
	{
		locale2Code.put( new ULocale( "sq", "" ), "[$-41C]" );
		locale2Code.put( new ULocale( "sq", "AL" ), "[$-41C]" );

		locale2Code.put( new ULocale( "ar", "" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "DZ" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "BH" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "EG" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "IQ" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "JO" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "KW" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "LB" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "LY" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "MA" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "OM" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "QA" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "SA" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "SD" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "SY" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "TN" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "AE" ), "[$-2010401]" );
		locale2Code.put( new ULocale( "ar", "YE" ), "[$-2010401]" );

		locale2Code.put( new ULocale( "be", "" ), "[$-423]" );
		locale2Code.put( new ULocale( "be", "BY" ), "[$-423]" );

		locale2Code.put( new ULocale( "bg", "" ), "[$-402]" );
		locale2Code.put( new ULocale( "bg", "BG" ), "[$-402]" );

		locale2Code.put( new ULocale( "ca", "" ), "[$-403]" );
		locale2Code.put( new ULocale( "ca", "ES" ), "[$-403]" );

		locale2Code.put( new ULocale( "zh", "" ), "[$-804]" );
		locale2Code.put( new ULocale( "zh", "CN" ), "[$-804]" );
		locale2Code.put( new ULocale( "zh", "HK" ), "[$-804]" );
		locale2Code.put( new ULocale( "zh", "TW" ), "[$-804]" );

		locale2Code.put( new ULocale( "hr", "" ), "[$-41A]" );
		locale2Code.put( new ULocale( "hr", "HR" ), "[$-41A]" );

		locale2Code.put( new ULocale( "cs", "" ), "[$-405]" );
		locale2Code.put( new ULocale( "cs", "CZ" ), "[$-405]" );

		locale2Code.put( new ULocale( "da", "" ), "[$-406]" );
		locale2Code.put( new ULocale( "da", "DK" ), "[$-406]" );

		locale2Code.put( new ULocale( "nl", "" ), "[$-413]" );
		locale2Code.put( new ULocale( "nl", "BE" ), "[$-413]" );
		locale2Code.put( new ULocale( "nl", "NL" ), "[$-413]" );

		locale2Code.put( new ULocale( "en", "" ), "[$-409]" );
		locale2Code.put( new ULocale( "en", "AU" ), "[$-C09]" );
		locale2Code.put( new ULocale( "en", "CA" ), "[$-1009]" );
		locale2Code.put( new ULocale( "en", "IN" ), "[$-409]" );
		locale2Code.put( new ULocale( "en", "IE" ), "[$-409]" );
		locale2Code.put( new ULocale( "en", "NZ" ), "[$-409]" );
		locale2Code.put( new ULocale( "en", "ZA" ), "[$-409]" );
		locale2Code.put( new ULocale( "en", "GB" ), "[$-809]" );
		locale2Code.put( new ULocale( "en", "US" ), "[$-409]" );

		locale2Code.put( new ULocale( "et", "" ), "[$-425]" );
		locale2Code.put( new ULocale( "et", "EE" ), "[$-425]" );

		locale2Code.put( new ULocale( "fi", "" ), "[$-40B]" );
		locale2Code.put( new ULocale( "fi", "FI" ), "[$-40B]" );

		locale2Code.put( new ULocale( "fr", "", "" ), "[$-40C]" );
		locale2Code.put( new ULocale( "fr", "BE" ), "[$-40C]" );
		locale2Code.put( new ULocale( "fr", "CA" ), "[$-C0C]" );
		locale2Code.put( new ULocale( "fr", "FR" ), "[$-40C]" );
		locale2Code.put( new ULocale( "fr", "LU" ), "[$-40C]" );
		locale2Code.put( new ULocale( "fr", "CH" ), "[$-40C]" );

		locale2Code.put( new ULocale( "de", "" ), "[$-407]" );
		locale2Code.put( new ULocale( "de", "AT" ), "[$-C07]" );
		locale2Code.put( new ULocale( "de", "DE" ), "[$-407]" );
		locale2Code.put( new ULocale( "de", "LU" ), "[$-407]" );
		locale2Code.put( new ULocale( "de", "CH" ), "[$-807]" );

		locale2Code.put( new ULocale( "el", "" ), "[$-408]" );
		locale2Code.put( new ULocale( "el", "GR" ), "[$-408]" );

		locale2Code.put( new ULocale( "iw", "" ), "[$-40D]" );
		locale2Code.put( new ULocale( "iw", "IL" ), "[$-40D]" );

		locale2Code.put( new ULocale( "hi", "IN" ), "[$-3010439]" );

		locale2Code.put( new ULocale( "hu", "" ), "[$-40E]" );
		locale2Code.put( new ULocale( "hu", "HU" ), "[$-40E]" );

		locale2Code.put( new ULocale( "is", "" ), "[$-40F]" );
		locale2Code.put( new ULocale( "is", "IS" ), "[$-40F]" );

		locale2Code.put( new ULocale( "it", "" ), "[$-410]" );
		locale2Code.put( new ULocale( "it", "IT" ), "[$-410]" );
		locale2Code.put( new ULocale( "it", "CH" ), "[$-410]" );

		locale2Code.put( new ULocale( "ja", "" ), "[$-411]" );
		locale2Code.put( new ULocale( "ja", "JP" ), "[$-411]" );

		locale2Code.put( new ULocale( "ko", "" ), "[$-412]" );
		locale2Code.put( new ULocale( "ko", "KR" ), "[$-412]" );

		locale2Code.put( new ULocale( "lv", "" ), "[$-426]" );
		locale2Code.put( new ULocale( "lv", "LV" ), "[$-426]" );

		locale2Code.put( new ULocale( "lt", "" ), "[$-427]" );
		locale2Code.put( new ULocale( "lt", "LT" ), "[$-427]" );

		locale2Code.put( new ULocale( "mk", "" ), "[$-42F]" );
		locale2Code.put( new ULocale( "mk", "MK" ), "[$-42F]" );

		locale2Code.put( new ULocale( "no", "" ), "[$-414]" );
		locale2Code.put( new ULocale( "no", "NO" ), "[$-414]" );

		locale2Code.put( new ULocale( "pl", "" ), "[$-415]" );
		locale2Code.put( new ULocale( "pl", "PL" ), "[$-415]" );

		locale2Code.put( new ULocale( "pt", "" ), "[$-816]" );
		locale2Code.put( new ULocale( "pt", "BR" ), "[$-416]" );
		locale2Code.put( new ULocale( "pt", "PT" ), "[$-816]" );

		locale2Code.put( new ULocale( "ro", "" ), "[$-418]" );
		locale2Code.put( new ULocale( "ro", "RO" ), "[$-418]" );

		locale2Code.put( new ULocale( "ru", "" ), "[$-419]" );
		locale2Code.put( new ULocale( "ru", "RU" ), "[$-419]" );

		locale2Code.put( new ULocale( "sk", "" ), "[$-41B]" );
		locale2Code.put( new ULocale( "sk", "SK" ), "[$-41B]" );

		locale2Code.put( new ULocale( "sl", "" ), "[$-424]" );
		locale2Code.put( new ULocale( "sl", "SI" ), "[$-424]" );

		locale2Code.put( new ULocale( "es", "" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "AR" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "BO" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "CL" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "CO" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "CR" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "DO" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "EC" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "SV" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "GT" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "HN" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "MX" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "NI" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "PA" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "PY" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "PE" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "PR" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "ES" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "UY" ), "[$-C0A]" );
		locale2Code.put( new ULocale( "es", "VE" ), "[$-C0A]" );

		locale2Code.put( new ULocale( "sv", "" ), "[$-41D]" );
		locale2Code.put( new ULocale( "sv", "SE" ), "[$-41D]" );

		locale2Code.put( new ULocale( "th", "" ), "[$-41E]" );
		locale2Code.put( new ULocale( "th", "TH" ), "[$-41E]" );

		locale2Code.put( new ULocale( "tr", "" ), "[$-41F]" );
		locale2Code.put( new ULocale( "tr", "TR" ), "[$-41F]" );

		locale2Code.put( new ULocale( "uk", "" ), "[$-422]" );
		locale2Code.put( new ULocale( "uk", "UA" ), "[$-422]" );

		locale2Code.put( new ULocale( "vi", "" ), "[$-042A]" );
		locale2Code.put( new ULocale( "vi", "VN" ), "[$-042A]" );
	}
	
	public static String formatDateTime( String format, ULocale locale )
	{
		String code = locale2Code.get( locale );
		if ( code == null )
		{
			String language = locale.getLanguage( );
			code = locale2Code.get( new ULocale( language, "" ) );
		}
		if ( code == null )
		{
			return format;
		}
		return code + format;
	}
}
