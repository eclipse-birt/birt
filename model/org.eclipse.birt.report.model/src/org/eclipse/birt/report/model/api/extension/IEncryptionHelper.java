/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.model.api.extension;

/**
 * Defines the interface of encryption helper, which includes two methods for
 * encryption and decryption.
 */

public interface IEncryptionHelper {

	/**
	 * Encrypts the given string, and returns the encrypted string.
	 *
	 * @param string the string to encrypt
	 * @return the encrypted string
	 */

	String encrypt(String string);

	/**
	 * Decrypts the given encrypted string, and returns the original string.
	 *
	 * @param string the string to decrypt
	 * @return the original string
	 */

	String decrypt(String string);
}
