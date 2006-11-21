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

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;

/**
 * Represents the extension loader for encryption helper.
 */

public class EncryptionHelperExtensionLoader extends ExtensionLoader
{

	/**
	 * The name of extension point
	 */

	public static final String EXTENSION_POINT = "org.eclipse.birt.report.model.encryptionHelper"; //$NON-NLS-1$

	private static final String ENCRYPTION_HELPER_TAG = "encryptionHelper"; //$NON-NLS-1$

	/**
	 * Default constructor
	 */

	public EncryptionHelperExtensionLoader( )
	{
		super( EXTENSION_POINT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ExtensionLoader#loadExtension(org.eclipse.birt.core.framework.IExtension)
	 */

	protected void loadExtension( IExtension extension )
	{
		IConfigurationElement[] configElements = extension
				.getConfigurationElements( );

		EncryptionHelperElementLoader loader = new EncryptionHelperElementLoader( );

		for ( int i = 0; i < configElements.length; i++ )
		{
			IConfigurationElement currentTag = configElements[i];
			if ( ENCRYPTION_HELPER_TAG.equals( currentTag.getName( ) ) )
			{
				loader.loadElement( currentTag );
			}
		}
	}

	class EncryptionHelperElementLoader extends ExtensionElementLoader
	{
		/**
		 * Loads the extension.
		 * 
		 * @param elementTag
		 *            the element tag
		 */

		public void loadElement( IConfigurationElement elementTag )
		{
			String extensionName = elementTag
					.getAttribute( EXTENSION_NAME_ATTRIB );
			String className = elementTag.getAttribute( CLASS_ATTRIB );

			if ( !checkRequiredAttribute( EXTENSION_NAME_ATTRIB, extensionName )
					|| !checkRequiredAttribute( CLASS_ATTRIB, className ) )
				return;

			try
			{
				IEncryptionHelper helper = (IEncryptionHelper) elementTag
						.createExecutableExtension( CLASS_ATTRIB );

				IEncryptionHelper registeredHelper = MetaDataDictionary
						.getInstance( ).getEncryptionHelper( );
				if ( registeredHelper != SimpleEncryptionHelper.getInstance( ) )
				{
					handleError( new ExtensionException(
							new String[]{className},
							ExtensionException.DESIGN_EXCEPTION_ENCYRPTION_EXTENSION_EXISTS ) );
					return;
				}

				MetaDataDictionary.getInstance( ).setEncryptionHelper( helper );
			}
			catch ( FrameworkException e )
			{
				handleError( new ExtensionException(
						new String[]{className},
						ExtensionException.DESIGN_EXCEPTION_FAILED_TO_CREATE_INSTANCE ) );
				return;
			}
		}

	}
}
