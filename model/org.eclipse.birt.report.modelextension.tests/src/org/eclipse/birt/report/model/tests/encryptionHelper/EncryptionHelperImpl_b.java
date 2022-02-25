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

package org.eclipse.birt.report.model.tests.encryptionHelper;

import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;

/**
 * TODO: Document required
 */
public class EncryptionHelperImpl_b implements IEncryptionHelper {

	private static final String PRE_FIX = "_b_"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IEncryptionHelper#encrypt
	 * (java.lang.String)
	 */
	@Override
	public String encrypt(String string) {
		return PRE_FIX + string;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.extension.IEncryptionHelper#decrypt
	 * (java.lang.String)
	 */
	@Override
	public String decrypt(String string) {
		if (string == null || !string.startsWith(PRE_FIX)) {
			return null;
		}
		return string.replaceFirst(PRE_FIX, ""); //$NON-NLS-1$
	}
}
