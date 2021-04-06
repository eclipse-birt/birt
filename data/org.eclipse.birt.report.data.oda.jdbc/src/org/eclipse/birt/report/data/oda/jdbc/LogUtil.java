/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

/**
 * LogUtil to encrypt password/psw value in log file.
 * 
 */
class LogUtil {

	static String encryptURL(String url) {
		if (url != null) {
			StringBuffer msg = new StringBuffer();
			String[] urlStrs = url.split(";");
			msg.append(urlStrs[0]);
			for (int i = 1; i < urlStrs.length; i++) {
				String[] props = urlStrs[i].split("=");
				if (props.length == 2) {
					if (props[0].toLowerCase().indexOf("password") >= 0 || props[0].toLowerCase().indexOf("pwd") >= 0) {
						msg.append(";" + props[0]);
						msg.append("=");
						msg.append("***");
					} else {
						msg.append(";" + props[0]);
						msg.append("=");
						msg.append(props[1]);
					}
				} else {
					msg.append(";" + urlStrs[i]);
				}
			}
			return msg.toString();
		}
		return url;
	}
}
