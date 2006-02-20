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
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.util.StringUtil;

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

	void loadExtension( IExtension extension ) throws ExtensionException,
			MetaDataException
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

	/**
	 * Logs the exception if encryption helper extension pointer is not found on
	 * non-eclipse platform, the error messages won't be logged out.
	 * 
	 * 
	 * @see org.eclipse.birt.report.model.metadata.ExtensionLoader#logExtenstionException(
	 *      ExtensionException e )
	 */

	protected void logExtenstionException( ExtensionException e )
	{
		if ( Platform.runningEclipse( ) )
		{
			super.logExtenstionException( e );
		}
	}

	class EncryptionHelperElementLoader
	{

		private static final String EXTENSION_NAME_ATTRIB = "extensionName"; //$NON-NLS-1$
		private static final String CLASS_ATTRIB = "class"; //$NON-NLS-1$

		/**
		 * Loads the extension.
		 * 
		 * @param elementTag
		 *            the element tag
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		public void loadElement( IConfigurationElement elementTag )
				throws ExtensionException
		{
			String extensionName = elementTag
					.getAttribute( EXTENSION_NAME_ATTRIB );
			String className = elementTag.getAttribute( CLASS_ATTRIB );

			checkRequiredAttribute( EXTENSION_NAME_ATTRIB, extensionName );
			checkRequiredAttribute( CLASS_ATTRIB, className );

			try
			{
				IEncryptionHelper helper = (IEncryptionHelper) elementTag
						.createExecutableExtension( CLASS_ATTRIB );

				IEncryptionHelper registeredHelper = MetaDataDictionary
						.getInstance( ).getEncryptionHelper( );
				if ( registeredHelper != SimpleEncryptionHelper.getInstance( ) )
				{
					throw new ExtensionException(
							new String[]{className},
							ExtensionException.DESIGN_EXCEPTION_ENCYRPTION_EXTENSION_EXISTS );
				}

				MetaDataDictionary.getInstance( ).setEncryptionHelper( helper );
			}
			catch ( FrameworkException e )
			{
				throw new ExtensionException(
						new String[]{className},
						ExtensionException.DESIGN_EXCEPTION_FAILED_TO_CREATE_INSTANCE );
			}
		}

		/**
		 * Checks whether the required attribute is set.
		 * 
		 * @param name
		 *            the required attribute name
		 * @param value
		 *            the attribute value
		 * @throws ExtensionException
		 *             if the value is empty
		 */

		void checkRequiredAttribute( String name, String value )
				throws ExtensionException
		{
			if ( StringUtil.isBlank( value ) )
				throw new ExtensionException( new String[]{name},
						ExtensionException.DESIGN_EXCEPTION_VALUE_REQUIRED );
		}

	}
}
