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

package org.eclipse.birt.report.utility;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlUtility {
	public static final String QUERY_CHAR = "?"; //$NON-NLS-1$
	public static final String ANCHOR_CHAR = "#"; //$NON-NLS-1$
	public static final String PARAM_SEPARATOR = "&"; //$NON-NLS-1$
	public static final String PARAM_EQUALS = "="; //$NON-NLS-1$

	/**
	 * UTF-8 encode constants.
	 */
	public static final String UTF_8_ENCODING = "UTF-8"; //$NON-NLS-1$

	/**
	 * Makes an URI string based on the given parameters.
	 * 
	 * @param parameters map of parameters to values (must be String)
	 * @return URI string
	 */
	public static String makeUriString(Map parameters) {
		StringBuffer output = new StringBuffer();

		for (Iterator i = parameters.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			Object value = entry.getValue();

			output.append(urlParamValueEncode(entry.getKey()));

			if (value != null) {
				output.append(PARAM_EQUALS);
				output.append(urlParamValueEncode(value));
			}
			if (i.hasNext()) {
				output.append(PARAM_SEPARATOR);
			}
		}

		return output.toString();
	}

	/**
	 * Extracts the URI parameters from the given URI string into a Map.
	 * 
	 * @param uriString URI string to decode
	 * @return map of URI parameters
	 */
	public static Map extractUriParameters(String uriString) {
		Map params = new HashMap();
		if (uriString != null) {
			int anchorPosition = uriString.indexOf(ANCHOR_CHAR);
			if (anchorPosition >= 0) {
				uriString = uriString.substring(0, anchorPosition);
			}

			int queryPosition = uriString.indexOf(QUERY_CHAR);
			if (queryPosition >= 0) {
				uriString = uriString.substring(queryPosition + 1);
			}

			String[] uriParts = uriString.split("\\&"); //$NON-NLS-1$
			for (int i = 0; i < uriParts.length; i++) {
				String param = uriParts[i];
				String[] paramParts = param.split(PARAM_EQUALS);
				if (paramParts.length > 0) {
					String paramName = urlParamValueDecode(paramParts[0]);
					String paramValue = null;
					if (paramParts.length > 1) {
						paramValue = urlParamValueDecode(paramParts[1]);
					}

					if (paramName != null && paramName.length() > 0) {
						params.put(paramName, paramValue);
					}
				}
			}
		}
		return params;
	}

	/**
	 * Decodes the url parameter value
	 * 
	 * @param s string to decode
	 * @return
	 */
	public static String urlParamValueDecode(String s) {
		if (s != null) {
			try {
				return URLDecoder.decode(s, UTF_8_ENCODING);
			} catch (UnsupportedEncodingException e) {
			}
		}
		return s;
	}

	/**
	 * Encode the url parameter value
	 * 
	 * @param s string to encode
	 * @return
	 */
	public static String urlParamValueEncode(String s) {
		if (s != null) {
			try {
				return URLEncoder.encode(s, UTF_8_ENCODING);
			} catch (UnsupportedEncodingException e) {
			}
		}

		return s;
	}

	/**
	 * Encode the url parameter value
	 * 
	 * @param plain
	 * @return
	 */
	public static String urlParamValueEncode(Object value) {
		if (!(value instanceof String)) {
			value = value.toString();
		}
		return urlParamValueEncode((String) value);
	}

	/**
	 * Extracts the base URL from the given URL by stripping the query and anchor
	 * part.
	 * 
	 * @param url URL
	 * @return URL without the query part
	 */
	public static String extractBaseUrl(String url) {
		if (url != null) {
			int queryPosition = url.indexOf(QUERY_CHAR);
			if (queryPosition <= 0) {
				queryPosition = url.indexOf(ANCHOR_CHAR);
			}

			if (queryPosition >= 0) {
				url = url.substring(0, queryPosition);
			}
		}
		return url;
	}

	/**
	 * Returns the anchor value of the given URL.
	 * 
	 * @param url URL
	 * @return anchor value, or null if none was defined
	 */
	public static String getAnchor(String url) {
		if (url != null) {
			int anchorPosition = url.indexOf(ANCHOR_CHAR);
			if (anchorPosition >= 0) {
				return url.substring(anchorPosition + 1);
			}
		}
		return null;
	}

	/**
	 * Builds an URL.
	 * 
	 * @param baseUrl     base URL
	 * @param queryParams query parameters
	 * @param anchor      anchor
	 * @return URL
	 */
	public static String buildUrl(String baseUrl, Map queryParams, String anchor) {
		// Save the URL String
		StringBuffer url = new StringBuffer();
		url.append(baseUrl);
		if (queryParams != null && !queryParams.isEmpty()) {
			url.append(QUERY_CHAR);
			url.append(makeUriString(queryParams));
		}
		if (anchor != null) {
			url.append(ANCHOR_CHAR);
			url.append(anchor);
		}

		return url.toString();
	}

}
