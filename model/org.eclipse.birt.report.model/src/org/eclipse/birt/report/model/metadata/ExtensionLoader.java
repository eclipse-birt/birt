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

import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * The abstract extension loader which provides the common functionality for
 * extension loaders. This loader will read the extension definition, generate
 * extension element definition and add them into the extension element list of
 * <code>MetaDataDicationary</code>.
 */

public abstract class ExtensionLoader
{

	/**
	 * Logger instance.
	 */

	private static Logger logger = Logger.getLogger( ExtensionLoader.class
			.getName( ) );

	private String extensionPointer = null;

	/**
	 * Constructor with the id of extension pointer which the extension this
	 * extension loader loads implements.
	 * 
	 * @param extensionPointer
	 *            the id of extension pointer
	 */

	ExtensionLoader( String extensionPointer )
	{
		this.extensionPointer = extensionPointer;
	}

	abstract void loadExtension( IExtension extension )
			throws ExtensionException, MetaDataException;

	/**
	 * Loads the extensions in plug-ins, and add them into metadata dictionary.
	 * 
	 * @throws MetaDataParserException
	 *             if error is found when loading extension.
	 */

	public void load( ) throws MetaDataParserException
	{
		try
		{
			doLoad( );

			// buidl all the extension definitions
			List extensions = MetaDataDictionary.getInstance( ).getExtensions( );
			if ( extensions == null || extensions.isEmpty( ) )
				return;

			for ( int i = 0; i < extensions.size( ); i++ )
			{
				ElementDefn defn = (ElementDefn) extensions.get( i );
				defn.build( );
			}
		}
		catch ( ExtensionException e )
		{
			logExtenstionException( e );
			throw new MetaDataParserException( e,
					MetaDataParserException.DESIGN_EXCEPTION_EXTENSION_ERROR );
		}
		catch ( MetaDataException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ) );
			MetaLogManager.log( "Extension loading error", e ); //$NON-NLS-1$
			throw new MetaDataParserException( e,
					MetaDataParserException.DESIGN_EXCEPTION_EXTENSION_ERROR );
		}
	}

	/**
	 * Logs the exceptions when extension pointers can't be found.
	 * 
	 * @param e
	 *            the extension exception.
	 */

	protected void logExtenstionException( ExtensionException e )
	{
		logger.log( Level.SEVERE, e.getMessage( ) );
		MetaLogManager.log( "Extension loading error", e ); //$NON-NLS-1$
	}

	/**
	 * Loads the extended elements in plug-ins, and add them into metadata
	 * dictionary.
	 * 
	 * @throws ExtensionException
	 *             if error is found when loading extension.
	 * @throws MetaDataException
	 *             if error encountered when adding the element to metadata
	 *             dictionary.
	 */

	void doLoad( ) throws ExtensionException, MetaDataException
	{
		IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry( );
		if ( pluginRegistry == null )
		{
			throw new ExtensionException(
					new String[]{extensionPointer},
					ExtensionException.DESIGN_EXCEPTION_EXTENSION_POINT_NOT_FOUND );
		}

		IExtensionPoint extensionPoint = pluginRegistry
				.getExtensionPoint( extensionPointer );
		if ( extensionPoint == null )
			throw new ExtensionException(
					new String[]{extensionPointer},
					ExtensionException.DESIGN_EXCEPTION_EXTENSION_POINT_NOT_FOUND );

		IExtension[] extensions = extensionPoint.getExtensions( );
		if ( extensions != null )
		{
			for ( int i = 0; i < extensions.length; i++ )
			{
				loadExtension( extensions[i] );
			}
		}
	}

	/**
	 * Represents the loader which loads the top level XML element in extension
	 * definition file. The common constants are defined for parsing.
	 */

	abstract class ExtensionElementLoader
	{

		static final String PROPERTY_TAG = "property"; //$NON-NLS-1$
		static final String CHOICE_TAG = "choice"; //$NON-NLS-1$
		static final String PROPERTY_GROUP_TAG = "propertyGroup"; //$NON-NLS-1$
		static final String PROPERTY_VISIBILITY_TAG = "propertyVisibility"; //$NON-NLS-1$

		static final String EXTENSION_NAME_ATTRIB = "extensionName"; //$NON-NLS-1$
		static final String NAME_ATTRIB = "name"; //$NON-NLS-1$
		static final String DISPLAY_NAME_ID_ATTRIB = "displayNameID"; //$NON-NLS-1$
		static final String TYPE_ATTRIB = "type"; //$NON-NLS-1$
		static final String CAN_INHERIT_ATTRIB = "canInherit"; //$NON-NLS-1$
		static final String DEFAULT_VALUE_ATTRIB = "defaultValue"; //$NON-NLS-1$
		static final String VALUE_ATTRIB = "value"; //$NON-NLS-1$
		static final String VISIBILITY_ATTRIB = "visibility"; //$NON-NLS-1$
		static final String DEFAULT_DISPLAY_NAME_ATTRIB = "defaultDisplayName"; //$NON-NLS-1$
		static final String IS_ENCRYPTABLE_ATTRIB = "isEncryptable"; //$NON-NLS-1$

		IExtension extension;

		ExtensionElementLoader( IExtension extension )
		{
			this.extension = extension;
		}

		/**
		 * Loads the extension element definition and its properties.
		 * 
		 * @param elementTag
		 *            the element tag
		 * @throws MetaDataException
		 *             if error encountered when adding the element to metadata
		 *             dictionary.
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		abstract void loadElement( IConfigurationElement elementTag )
				throws MetaDataException, ExtensionException;

		/**
		 * Loads one property definition of the given element.
		 * 
		 * @param elementTag
		 *            the element tag
		 * @param propTag
		 *            the property tag
		 * @param elementDefn
		 *            element definition
		 * 
		 * @return the property definition
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		abstract ExtensionPropertyDefn loadProperty(
				IConfigurationElement elementTag,
				IConfigurationElement propTag, ExtensionElementDefn elementDefn )
				throws ExtensionException;

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

		/**
		 * Loads one choice.
		 * 
		 * @param choiceTag
		 *            the choice tag
		 * @param choice
		 *            the extension choice set load
		 * @param propDefn
		 *            the property definition in which the choices are inserted
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		void loadChoice( IConfigurationElement choiceTag,
				ExtensionChoice choice, PropertyDefn propDefn )
				throws ExtensionException
		{
			// read required first
			String name = choiceTag.getAttribute( NAME_ATTRIB );
			checkRequiredAttribute( NAME_ATTRIB, name );

			// read optional
			String value = choiceTag.getAttribute( VALUE_ATTRIB );
			String displayNameID = choiceTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );
			String defaultDisplayName = choiceTag
					.getAttribute( DEFAULT_DISPLAY_NAME_ATTRIB );

			choice.setName( name );
			if ( propDefn.getTypeCode( ) != PropertyType.CHOICE_TYPE )
			{
				try
				{
					Object validateValue = propDefn.validateXml( null, value );
					choice.setValue( validateValue );
				}
				catch ( PropertyValueException e )
				{
					throw new ExtensionException(
							new String[]{value, propDefn.getName( )},
							ExtensionException.DESIGN_EXCEPTION_INVALID_CHOICE_VALUE );
				}
			}
			else
				choice.setValue( value );

			choice.setDisplayNameKey( displayNameID );
			choice.setDefaultDisplayName( defaultDisplayName );
		}

		/**
		 * Loads one visibility rule of a system property definition.
		 * 
		 * @param propTag
		 *            the property tag
		 * @param elementDefn
		 *            element definition
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		void loadPropertyVisibility( IConfigurationElement propTag,
				ExtensionElementDefn elementDefn ) throws ExtensionException
		{
			// load required parts first
			String name = propTag.getAttribute( NAME_ATTRIB );
			checkRequiredAttribute( NAME_ATTRIB, name );

			// load optional parts
			String visible = propTag.getAttribute( VISIBILITY_ATTRIB );
			elementDefn.addPropertyVisibility( name, visible );
		}

		/**
		 * Loads the properties of one group.
		 * 
		 * @param elementTag
		 *            the element tag
		 * @param propGroupTag
		 *            the property group tag
		 * @param elementDefn
		 *            element definition
		 * @param propList
		 *            the property list into which the new property is added.
		 * 
		 * @throws MetaDataException
		 */

		void loadPropertyGroup( IConfigurationElement elementTag,
				IConfigurationElement propGroupTag,
				ExtensionElementDefn elementDefn, List propList )
				throws MetaDataException
		{
			// read required parts first
			String groupName = propGroupTag.getAttribute( NAME_ATTRIB );
			checkRequiredAttribute( NAME_ATTRIB, groupName );

			// read optinal parts
			String displayNameID = propGroupTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );
			String defaultDisplayName = propGroupTag
					.getAttribute( DEFAULT_DISPLAY_NAME_ATTRIB );

			IConfigurationElement[] elements = propGroupTag.getChildren( );
			for ( int i = 0; i < elements.length; i++ )
			{
				if ( PROPERTY_TAG.equalsIgnoreCase( elements[i].getName( ) ) )
				{
					ExtensionPropertyDefn extPropDefn = loadProperty(
							elementTag, elements[i], elementDefn );
					extPropDefn.setGroupName( groupName );
					extPropDefn.setGroupNameKey( displayNameID );
					extPropDefn.setGroupDefauleDisplayName( defaultDisplayName );
					elementDefn.addProperty( extPropDefn );
				}
			}
		}
	}
}