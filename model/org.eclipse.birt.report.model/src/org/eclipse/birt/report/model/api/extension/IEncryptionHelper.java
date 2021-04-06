/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public String encrypt(String string);

	/**
	 * Decrypts the given encrypted string, and returns the original string.
	 * 
	 * @param string the string to decrypt
	 * @return the original string
	 */

	public String decrypt(String string);
}
