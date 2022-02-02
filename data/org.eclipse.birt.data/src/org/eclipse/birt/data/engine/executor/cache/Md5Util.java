/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.cache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {

	private static final String MD5 = "MD5";
	private static char[] code = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };

	/**
	 * 
	 * @param rawString
	 * @return
	 */
	public static String getMD5(String rawString) {
		if (rawString == null)
			return null;
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(MD5);
			byte[] bytes = md.digest(rawString.getBytes());
			char[] md5 = new char[32];
			byte index = 0;
			for (byte i = 0; i < bytes.length; i++) {
				int n = bytes[i] + 128;
				md5[index++] = code[(n & 0x000000f0) >> 4];
				md5[index++] = code[n & 0x0000000f];
			}
			return new String(md5);
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}
}
