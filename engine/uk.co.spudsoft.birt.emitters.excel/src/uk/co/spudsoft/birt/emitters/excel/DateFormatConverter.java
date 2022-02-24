/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DateFormatConverter {

	public static class DateFormatTokenizer {
		String format;
		int pos;

		public DateFormatTokenizer(String format) {
			this.format = format;
		}

		public String getNextToken() {
			if (pos >= format.length()) {
				return null;
			}
			int subStart = pos;
			char curChar = format.charAt(pos);
			++pos;
			if (curChar == '\'') {
				while ((pos < format.length()) && ((curChar = format.charAt(pos)) != '\'')) {
					++pos;
				}
				if (pos < format.length()) {
					++pos;
				}
			} else {
				char activeChar = curChar;
				while ((pos < format.length()) && ((curChar = format.charAt(pos)) == activeChar)) {
					++pos;
				}
			}
			return format.substring(subStart, pos);
		}

		public static String[] tokenize(String format) {
			List<String> result = new ArrayList<String>();

			DateFormatTokenizer tokenizer = new DateFormatTokenizer(format);
			String token;
			while ((token = tokenizer.getNextToken()) != null) {
				result.add(token);
			}

			return result.toArray(new String[0]);
		}

		public String toString() {
			StringBuilder result = new StringBuilder();

			DateFormatTokenizer tokenizer = new DateFormatTokenizer(format);
			String token;
			while ((token = tokenizer.getNextToken()) != null) {
				if (result.length() > 0) {
					result.append(", ");
				}
				result.append("[").append(token).append("]");
			}

			return result.toString();
		}
	}

	private static Map<String, String> tokenConversions = prepareTokenConversions();
	private static Map<String, String> localePrefixes = prepareLocalePrefixes();

	private static Map<String, String> prepareTokenConversions() {
		Map<String, String> result = new HashMap<String, String>();

		result.put("EEEE", "dddd");
		result.put("EEE", "ddd");
		result.put("EE", "ddd");
		result.put("E", "d");
		result.put("Z", "");
		result.put("z", "");
		result.put("a", "am/pm");
		result.put("A", "AM/PM");
		result.put("K", "H");
		result.put("KK", "HH");
		result.put("k", "h");
		result.put("kk", "hh");
		result.put("S", "0");
		result.put("SS", "00");
		result.put("SSS", "000");

		return result;
	}

	private static Map<String, String> prepareLocalePrefixes() {
		Map<String, String> result = new HashMap<String, String>();

		result.put("af", "[$-1010436]");
		result.put("am", "[$-101045E]");
		result.put("ar_ae", "[$-1013801]");
		result.put("ar_bh", "[$-1013C01]");
		result.put("ar_dz", "[$-1011401]");
		result.put("ar_eg", "[$-1010C01]");
		result.put("ar_iq", "[$-1010801]");
		result.put("ar_jo", "[$-1012C01]");
		result.put("ar_kw", "[$-1013401]");
		result.put("ar_lb", "[$-1013001]");
		result.put("ar_ly", "[$-1011001]");
		result.put("ar_ma", "[$-1011801]");
		result.put("ar_om", "[$-1012001]");
		result.put("ar_qa", "[$-1014001]");
		result.put("ar_sa", "[$-1010401]");
		result.put("ar_sy", "[$-1012801]");
		result.put("ar_tn", "[$-1011C01]");
		result.put("ar_ye", "[$-1012401]");
		result.put("as", "[$-101044D]");
		result.put("az_az", "[$-101082C]");
		result.put("az_az", "[$-101042C]");
		result.put("be", "[$-1010423]");
		result.put("bg", "[$-1010402]");
		result.put("bn", "[$-1010845]");
		result.put("bn", "[$-1010445]");
		result.put("bo", "[$-1010451]");
		result.put("bs", "[$-101141A]");
		result.put("ca", "[$-1010403]");
		result.put("cs", "[$-1010405]");
		result.put("cy", "[$-1010452]");
		result.put("da", "[$-1010406]");
		result.put("de_at", "[$-1010C07]");
		result.put("de_ch", "[$-1010807]");
		result.put("de_de", "[$-1010407]");
		result.put("de_li", "[$-1011407]");
		result.put("de_lu", "[$-1011007]");
		result.put("dv", "[$-1010465]");
		result.put("el", "[$-1010408]");
		result.put("en_au", "[$-1010C09]");
		result.put("en_bz", "[$-1012809]");
		result.put("en_ca", "[$-1011009]");
		result.put("en_cb", "[$-1012409]");
		result.put("en_gb", "[$-1010809]");
		result.put("en_ie", "[$-1011809]");
		result.put("en_in", "[$-1014009]");
		result.put("en_jm", "[$-1012009]");
		result.put("en_nz", "[$-1011409]");
		result.put("en_ph", "[$-1013409]");
		result.put("en_tt", "[$-1012C09]");
		result.put("en_us", "[$-1010409]");
		result.put("en_za", "[$-1011C09]");
		result.put("es_ar", "[$-1012C0A]");
		result.put("es_bo", "[$-101400A]");
		result.put("es_cl", "[$-101340A]");
		result.put("es_co", "[$-101240A]");
		result.put("es_cr", "[$-101140A]");
		result.put("es_do", "[$-1011C0A]");
		result.put("es_ec", "[$-101300A]");
		result.put("es_es", "[$-101040A]");
		result.put("es_gt", "[$-101100A]");
		result.put("es_hn", "[$-101480A]");
		result.put("es_mx", "[$-101080A]");
		result.put("es_ni", "[$-1014C0A]");
		result.put("es_pa", "[$-101180A]");
		result.put("es_pe", "[$-101280A]");
		result.put("es_pr", "[$-101500A]");
		result.put("es_py", "[$-1013C0A]");
		result.put("es_sv", "[$-101440A]");
		result.put("es_uy", "[$-101380A]");
		result.put("es_ve", "[$-101200A]");
		result.put("et", "[$-1010425]");
		result.put("eu", "[$-101042D]");
		result.put("fa", "[$-1010429]");
		result.put("fi", "[$-101040B]");
		result.put("fo", "[$-1010438]");
		result.put("fr_be", "[$-101080C]");
		result.put("fr_ca", "[$-1010C0C]");
		result.put("fr_ch", "[$-101100C]");
		result.put("fr_fr", "[$-101040C]");
		result.put("fr_lu", "[$-101140C]");
		result.put("gd", "[$-101043C]");
		result.put("gd_ie", "[$-101083C]");
		result.put("gn", "[$-1010474]");
		result.put("gu", "[$-1010447]");
		result.put("he", "[$-101040D]");
		result.put("hi", "[$-1010439]");
		result.put("hr", "[$-101041A]");
		result.put("hu", "[$-101040E]");
		result.put("hy", "[$-101042B]");
		result.put("id", "[$-1010421]");
		result.put("is", "[$-101040F]");
		result.put("it_ch", "[$-1010810]");
		result.put("it_it", "[$-1010410]");
		result.put("ja", "[$-1010411]");
		result.put("kk", "[$-101043F]");
		result.put("km", "[$-1010453]");
		result.put("kn", "[$-101044B]");
		result.put("ko", "[$-1010412]");
		result.put("ks", "[$-1010460]");
		result.put("la", "[$-1010476]");
		result.put("lo", "[$-1010454]");
		result.put("lt", "[$-1010427]");
		result.put("lv", "[$-1010426]");
		result.put("mi", "[$-1010481]");
		result.put("mk", "[$-101042F]");
		result.put("ml", "[$-101044C]");
		result.put("mn", "[$-1010850]");
		result.put("mn", "[$-1010450]");
		result.put("mr", "[$-101044E]");
		result.put("ms_bn", "[$-101083E]");
		result.put("ms_my", "[$-101043E]");
		result.put("mt", "[$-101043A]");
		result.put("my", "[$-1010455]");
		result.put("ne", "[$-1010461]");
		result.put("nl_be", "[$-1010813]");
		result.put("nl_nl", "[$-1010413]");
		result.put("no_no", "[$-1010814]");
		result.put("or", "[$-1010448]");
		result.put("pa", "[$-1010446]");
		result.put("pl", "[$-1010415]");
		result.put("pt_br", "[$-1010416]");
		result.put("pt_pt", "[$-1010816]");
		result.put("rm", "[$-1010417]");
		result.put("ro", "[$-1010418]");
		result.put("ro_mo", "[$-1010818]");
		result.put("ru", "[$-1010419]");
		result.put("ru_mo", "[$-1010819]");
		result.put("sa", "[$-101044F]");
		result.put("sb", "[$-101042E]");
		result.put("sd", "[$-1010459]");
		result.put("si", "[$-101045B]");
		result.put("sk", "[$-101041B]");
		result.put("sl", "[$-1010424]");
		result.put("so", "[$-1010477]");
		result.put("sq", "[$-101041C]");
		result.put("sr_sp", "[$-1010C1A]");
		result.put("sr_sp", "[$-101081A]");
		result.put("sv_fi", "[$-101081D]");
		result.put("sv_se", "[$-101041D]");
		result.put("sw", "[$-1010441]");
		result.put("ta", "[$-1010449]");
		result.put("te", "[$-101044A]");
		result.put("tg", "[$-1010428]");
		result.put("th", "[$-101041E]");
		result.put("tk", "[$-1010442]");
		result.put("tn", "[$-1010432]");
		result.put("tr", "[$-101041F]");
		result.put("ts", "[$-1010431]");
		result.put("tt", "[$-1010444]");
		result.put("uk", "[$-1010422]");
		result.put("ur", "[$-1010420]");
		result.put("UTF_8", "[$-1010000]");
		result.put("uz_uz", "[$-1010843]");
		result.put("uz_uz", "[$-1010443]");
		result.put("vi", "[$-101042A]");
		result.put("xh", "[$-1010434]");
		result.put("yi", "[$-101043D]");
		result.put("zh_cn", "[$-1010804]");
		result.put("zh_hk", "[$-1010C04]");
		result.put("zh_mo", "[$-1011404]");
		result.put("zh_sg", "[$-1011004]");
		result.put("zh_tw", "[$-1010404]");
		result.put("zu", "[$-1010435]");

		result.put("ar", "[$-1010401]");
		result.put("bn", "[$-1010845]");
		result.put("de", "[$-1010407]");
		result.put("en", "[$-1010409]");
		result.put("es", "[$-101040A]");
		result.put("fr", "[$-101040C]");
		result.put("it", "[$-1010410]");
		result.put("ms", "[$-101043E]");
		result.put("nl", "[$-1010413]");
		result.put("nn", "[$-1010814]");
		result.put("no", "[$-1010414]");
		result.put("pt", "[$-1010816]");
		result.put("sr", "[$-1010C1A]");
		result.put("sv", "[$-101041D]");
		result.put("uz", "[$-1010843]");
		result.put("zh", "[$-1010804]");

		result.put("ga", "[$-101043C]");
		result.put("ga_ie", "[$-101083C]");
		result.put("in", "[$-1010421]");
		result.put("iw", "[$-101040D]");

		return result;
	}

	public static String getPrefixForLocale(Locale locale) {
		String localeString = locale.toString().toLowerCase();
		String result = localePrefixes.get(localeString);
		if (result == null) {
			result = localePrefixes.get(localeString.substring(0, 2));
			if (result == null) {
				// Locale parentLocale = new Locale(localeString.substring( 0, 2 ));
				// System.out.println( "Unable to find prefix for " + locale + "(" +
				// locale.getDisplayName() + ") or "
				// + localeString.substring( 0, 2 ) + "(" + parentLocale.getDisplayName() + ")"
				// );
				return "";
			}
		}
		return result;
	}

	public static String convert(Locale locale, String format) {
		StringBuilder result = new StringBuilder();

		result.append(getPrefixForLocale(locale));
		DateFormatTokenizer tokenizer = new DateFormatTokenizer(format);
		String token;
		while ((token = tokenizer.getNextToken()) != null) {
			if (token.startsWith("'")) {
				result.append(token.replaceAll("'", "\""));
			} else if (!Character.isLetter(token.charAt(0))) {
				result.append(token);
			} else {
				// It's a code, translate it if necessary
				String mappedToken = tokenConversions.get(token);
				result.append(mappedToken == null ? token : mappedToken);
			}
		}

		return result.toString().trim();
	}

	public static String getJavaDatePattern(int style, Locale locale) {
		DateFormat df = DateFormat.getDateInstance(style, locale);
		if (df instanceof SimpleDateFormat) {
			return ((SimpleDateFormat) df).toPattern();
		} else {
			switch (style) {
			case DateFormat.SHORT:
				return "d/MM/yy";
			case DateFormat.MEDIUM:
				return "MMM d, yyyy";
			case DateFormat.LONG:
				return "MMMM d, yyyy";
			case DateFormat.FULL:
				return "dddd, MMMM d, yyyy";
			default:
				return "MMM d, yyyy";
			}
		}
	}

	public static String getJavaTimePattern(int style, Locale locale) {
		DateFormat df = DateFormat.getTimeInstance(style, locale);
		if (df instanceof SimpleDateFormat) {
			return ((SimpleDateFormat) df).toPattern();
		} else {
			switch (style) {
			case DateFormat.SHORT:
				return "h:mm a";
			case DateFormat.MEDIUM:
				return "h:mm:ss a";
			case DateFormat.LONG:
				return "h:mm:ss a";
			case DateFormat.FULL:
				return "h:mm:ss a";
			default:
				return "h:mm:ss a";
			}
		}
	}

	public static String getJavaDateTimePattern(int style, Locale locale) {
		DateFormat df = DateFormat.getDateTimeInstance(style, style, locale);
		if (df instanceof SimpleDateFormat) {
			return ((SimpleDateFormat) df).toPattern();
		} else {
			switch (style) {
			case DateFormat.SHORT:
				return "M/d/yy h:mm a";
			case DateFormat.MEDIUM:
				return "MMM d, yyyy h:mm:ss a";
			case DateFormat.LONG:
				return "MMMM d, yyyy h:mm:ss a";
			case DateFormat.FULL:
				return "dddd, MMMM d, yyyy h:mm:ss a";
			default:
				return "MMM d, yyyy h:mm:ss a";
			}
		}
	}

}
