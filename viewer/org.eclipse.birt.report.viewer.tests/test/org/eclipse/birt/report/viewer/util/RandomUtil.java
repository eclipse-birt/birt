/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.viewer.util;

import java.security.SecureRandom;
import java.util.Date;

/**
 * This class is used to generate defined length random String. User can set the
 * defined dictionary.
 * 
 */
public class RandomUtil {

	/**
	 * Dictionary for generating random string.
	 */
	private String dictionary = "0123456789ABCDEF"; //$NON-NLS-1$

	/**
	 * Generate Random String
	 * 
	 * @param length
	 * @return
	 */
	public String get(int length) {
		StringBuffer buffer = new StringBuffer();
		SecureRandom sr = new SecureRandom();
		for (int i = 0; i < length; i++) {
			sr.setSeed((new Date().toString()).getBytes());
			int rnd = sr.nextInt(dictionary.length());
			buffer.append(dictionary.charAt(rnd));
		}

		return buffer.toString();
	}

	/**
	 * @return the dictionary
	 */
	public String getDictionary() {
		return dictionary;
	}

	/**
	 * @param dictionary the dictionary to set
	 */
	public void setDictionary(String dictionary) {
		this.dictionary = dictionary;
	}
}
