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

package org.eclipse.birt.report.model.tests.encryptionHelper;

import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;

/**
 * TODO: Document required
 */
public class EncryptionHelperImpl implements IEncryptionHelper
{

	private static final String PRE_FIX = "_ab_"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IEncryptionHelper#encrypt
	 * (java.lang.String)
	 */
	public String encrypt( String string )
	{
		return PRE_FIX + string;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.IEncryptionHelper#decrypt
	 * (java.lang.String)
	 */
	public String decrypt( String string )
	{
		if ( string == null || !string.startsWith( PRE_FIX ) )
			return null;
		return string.replaceFirst( PRE_FIX, "" ); //$NON-NLS-1$
	}

}
