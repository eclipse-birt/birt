
package org.eclipse.birt.report.engine.emitter.excel;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateTimeUtil
{

	static Map<Locale, String> locale2Code = new HashMap<Locale, String>( );

	static
	{
		locale2Code.put( new Locale( "sq_", "" ), "[$-41C]" );
		locale2Code.put( new Locale( "sq", "AL" ), "[$-41C]" );

		locale2Code.put( new Locale( "ar_", "" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "DZ" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "BH" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "EG" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "IQ" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "JO" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "KW" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "LB" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "LY" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "MA" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "OM" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "QA" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "SA" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "SD" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "SY" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "TN" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "AE" ), "[$-2010401]" );
		locale2Code.put( new Locale( "ar", "YE" ), "[$-2010401]" );

		locale2Code.put( new Locale( "be_", "" ), "[$-423]" );
		locale2Code.put( new Locale( "be", "BY" ), "[$-423]" );

		locale2Code.put( new Locale( "bg_", "" ), "[$-402]" );
		locale2Code.put( new Locale( "bg", "BG" ), "[$-402]" );

		locale2Code.put( new Locale( "ca_", "" ), "[$-403]" );
		locale2Code.put( new Locale( "ca", "ES" ), "[$-403]" );

		locale2Code.put( new Locale( "zh_", "" ), "[$-804]" );
		locale2Code.put( new Locale( "zh", "CN" ), "[$-804]" );
		locale2Code.put( new Locale( "zh", "HK" ), "[$-804]" );
		locale2Code.put( new Locale( "zh", "TW" ), "[$-804]" );

		locale2Code.put( new Locale( "hr_", "" ), "[$-41A]" );
		locale2Code.put( new Locale( "hr", "HR" ), "[$-41A]" );

		locale2Code.put( new Locale( "cs_", "" ), "[$-405]" );
		locale2Code.put( new Locale( "cs", "CZ" ), "[$-405]" );

		locale2Code.put( new Locale( "da_", "" ), "[$-406]" );
		locale2Code.put( new Locale( "da", "DK" ), "[$-406]" );

		locale2Code.put( new Locale( "nl_", "" ), "[$-413]" );
		locale2Code.put( new Locale( "nl", "BE" ), "[$-413]" );
		locale2Code.put( new Locale( "nl", "NL" ), "[$-413]" );

		locale2Code.put( new Locale( "en_", "" ), "[$-409]" );
		locale2Code.put( new Locale( "en", "AU" ), "[$-C09]" );
		locale2Code.put( new Locale( "en", "CA" ), "[$-1009]" );
		locale2Code.put( new Locale( "en", "IN" ), "[$-409]" );
		locale2Code.put( new Locale( "en", "IE" ), "[$-409]" );
		locale2Code.put( new Locale( "en", "NZ" ), "[$-409]" );
		locale2Code.put( new Locale( "en", "ZA" ), "[$-409]" );
		locale2Code.put( new Locale( "en", "GB" ), "[$-809]" );
		locale2Code.put( new Locale( "en", "US" ), "[$-409]" );

		locale2Code.put( new Locale( "et_", "" ), "[$-425]" );
		locale2Code.put( new Locale( "et", "EE" ), "[$-425]" );

		locale2Code.put( new Locale( "fi_", "" ), "[$-40B]" );
		locale2Code.put( new Locale( "fi", "FI" ), "[$-40B]" );

		locale2Code.put( new Locale( "fr_", "" ), "[$-40C]" );
		locale2Code.put( new Locale( "fr", "BE" ), "[$-40C]" );
		locale2Code.put( new Locale( "fr", "CA" ), "[$-C0C]" );
		locale2Code.put( new Locale( "fr", "FR" ), "[$-40C]" );
		locale2Code.put( new Locale( "fr", "LU" ), "[$-40C]" );
		locale2Code.put( new Locale( "fr", "CH" ), "[$-40C]" );

		locale2Code.put( new Locale( "de_", "" ), "[$-407]" );
		locale2Code.put( new Locale( "de", "AT" ), "[$-C07]" );
		locale2Code.put( new Locale( "de", "DE" ), "[$-407]" );
		locale2Code.put( new Locale( "de", "LU" ), "[$-407]" );
		locale2Code.put( new Locale( "de", "CH" ), "[$-807]" );

		locale2Code.put( new Locale( "el_", "" ), "[$-408]" );
		locale2Code.put( new Locale( "el", "GR" ), "[$-408]" );

		locale2Code.put( new Locale( "iw_", "" ), "[$-40D]" );
		locale2Code.put( new Locale( "iw", "IL" ), "[$-40D]" );

		locale2Code.put( new Locale( "hi", "IN" ), "[$-3010439]" );

		locale2Code.put( new Locale( "hu_", "" ), "[$-40E]" );
		locale2Code.put( new Locale( "hu", "HU" ), "[$-40E]" );

		locale2Code.put( new Locale( "is_", "" ), "[$-40F]" );
		locale2Code.put( new Locale( "is", "IS" ), "[$-40F]" );

		locale2Code.put( new Locale( "it_", "" ), "[$-410]" );
		locale2Code.put( new Locale( "it", "IT" ), "[$-410]" );
		locale2Code.put( new Locale( "it", "CH" ), "[$-410]" );

		locale2Code.put( new Locale( "ja_", "" ), "[$-411]" );
		locale2Code.put( new Locale( "ja", "JP" ), "[$-411]" );

		locale2Code.put( new Locale( "ko_", "" ), "[$-412]" );
		locale2Code.put( new Locale( "ko", "KR" ), "[$-412]" );

		locale2Code.put( new Locale( "lv_", "" ), "[$-426]" );
		locale2Code.put( new Locale( "lv", "LV" ), "[$-426]" );

		locale2Code.put( new Locale( "lt_", "" ), "[$-427]" );
		locale2Code.put( new Locale( "lt", "LT" ), "[$-427]" );

		locale2Code.put( new Locale( "mk_", "" ), "[$-42F]" );
		locale2Code.put( new Locale( "mk", "MK" ), "[$-42F]" );

		locale2Code.put( new Locale( "no_", "" ), "[$-414]" );
		locale2Code.put( new Locale( "no", "NO" ), "[$-414]" );

		locale2Code.put( new Locale( "pl_", "" ), "[$-415]" );
		locale2Code.put( new Locale( "pl", "PL" ), "[$-415]" );

		locale2Code.put( new Locale( "pt_", "" ), "[$-816]" );
		locale2Code.put( new Locale( "pt", "BR" ), "[$-416]" );
		locale2Code.put( new Locale( "pt", "PT" ), "[$-816]" );

		locale2Code.put( new Locale( "ro_", "" ), "[$-418]" );
		locale2Code.put( new Locale( "ro", "RO" ), "[$-418]" );

		locale2Code.put( new Locale( "ru_", "" ), "[$-419]" );
		locale2Code.put( new Locale( "ru", "RU" ), "[$-419]" );

		locale2Code.put( new Locale( "sk_", "" ), "[$-41B]" );
		locale2Code.put( new Locale( "sk", "SK" ), "[$-41B]" );

		locale2Code.put( new Locale( "sl_", "" ), "[$-424]" );
		locale2Code.put( new Locale( "sl", "SI" ), "[$-424]" );

		locale2Code.put( new Locale( "es_", "" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "AR" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "BO" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "CL" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "CO" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "CR" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "DO" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "EC" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "SV" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "GT" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "HN" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "MX" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "NI" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "PA" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "PY" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "PE" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "PR" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "ES" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "UY" ), "[$-C0A]" );
		locale2Code.put( new Locale( "es", "VE" ), "[$-C0A]" );

		locale2Code.put( new Locale( "sv_", "" ), "[$-41D]" );
		locale2Code.put( new Locale( "sv", "SE" ), "[$-41D]" );

		locale2Code.put( new Locale( "th_", "" ), "[$-41E]" );
		locale2Code.put( new Locale( "th", "TH" ), "[$-41E]" );

		locale2Code.put( new Locale( "tr_", "" ), "[$-41F]" );
		locale2Code.put( new Locale( "tr", "TR" ), "[$-41F]" );

		locale2Code.put( new Locale( "uk_", "" ), "[$-422]" );
		locale2Code.put( new Locale( "uk", "UA" ), "[$-422]" );

		locale2Code.put( new Locale( "vi_", "" ), "[$-042A]" );
		locale2Code.put( new Locale( "vi", "VN" ), "[$-042A]" );
	}

	public static String formatDateTime( String format, Locale locale )
	{
		return locale2Code.get( locale ) + format;
	}
}
