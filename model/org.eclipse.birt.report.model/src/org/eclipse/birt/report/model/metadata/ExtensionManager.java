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

package org.eclipse.birt.report.model.metadata;

/**
 * Represents the extension manager which is responsible to load all extensions
 * that Model supports. This class can not be instantiated and derived.
 */

public final class ExtensionManager
{

	/**
	 * Don't allow to instantiate.
	 */

	private ExtensionManager( )
	{
	}

	/**
	 * Initializes all extensions that Model supports.
	 */

	public static void initialize( )
	{
		// ensure the ROM is initialized first
		assert !MetaDataDictionary.getInstance( ).isEmpty( );

		// load extensions in all the extension points
		new PeerExtensionLoader( ).load( );
		new ScriptableObjectExtensionLoader( ).load( );
		new EncryptionHelperExtensionLoader( ).load( );		
	}
}
