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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.core.RootElement;
import org.eclipse.birt.report.model.extension.IMessages;
import org.eclipse.birt.report.model.extension.IReportItemFactory;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Loads the extended element from plug-ins.
 */

public class ExtensionLoader
{

	/**
	 * The name of extension point
	 */

	public static final String EXTENSION_POINT = "org.eclipse.birt.report.model.reportitem"; //$NON-NLS-1$

	private static final String ELEMENT_TAG = "Element"; //$NON-NLS-1$
	private static final String PROPERTY_TAG = "Property"; //$NON-NLS-1$
	private static final String CHOICE_TAG = "Choice"; //$NON-NLS-1$
	private static final String STYLE_PROPERTY_TAG = "StyleProperty"; //$NON-NLS-1$
	private static final String PROPERTY_GROUP_TAG = "PropertyGroup"; //$NON-NLS-1$
	private static final String METHOD_TAG = "Method"; //$NON-NLS-1$

	private static final String NAME_ATTRIB = "name"; //$NON-NLS-1$
	private static final String DISPLAY_NAME_ID_ATTRIB = "displayNameID"; //$NON-NLS-1$
	private static final String HAS_STYLE_ATTRIB = "hasStyle"; //$NON-NLS-1$
	private static final String DEFAULT_STYLE_ATTRIB = "defaultStyle"; //$NON-NLS-1$
	private static final String IS_NAME_REQUIRED_ATTRIB = "isNameRequired"; //$NON-NLS-1$
	private static final String CLASS_ATTRIB = "class"; //$NON-NLS-1$
	private static final String TYPE_ATTRIB = "type"; //$NON-NLS-1$
	private static final String CAN_INHERIT_ATTRIB = "canInherit"; //$NON-NLS-1$
	private static final String DETAIL_TYPE_ATTRIB = "detailType"; //$NON-NLS-1$
	private static final String DEFAULT_VALUE_ATTRIB = "defaultValue"; //$NON-NLS-1$
	private static final String IS_STYLE_PROPERTY_ATTRIB = "isStyleProperty"; //$NON-NLS-1$
	private static final String VALUE_ATTRIB = "value"; //$NON-NLS-1$
	private static final String IS_VISIBLE_ATTRIB = "isVisible"; //$NON-NLS-1$
	
	/**
	 * Loads the extended elements in plug-ins, and add them into metadata
	 * dictionary.
	 * 
	 * @throws MetaDataReaderException
	 *             if error is found when loading extension.
	 */

	public static void init( ) throws MetaDataReaderException
	{
		try
		{
			load( );
		}
		catch ( ExtensionException e )
		{
			MetaLogManager.log( "Extension loading error", e ); //$NON-NLS-1$
			throw new MetaDataReaderException( e,
					MetaDataReaderException.EXTENSION_ERROR );
		}
		catch ( MetaDataException e )
		{
			MetaLogManager.log( "Extension loading error", e ); //$NON-NLS-1$
			throw new MetaDataReaderException( e,
					MetaDataReaderException.EXTENSION_ERROR );
		}
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

	static void load( ) throws ExtensionException, MetaDataException
	{
		IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry( );
		if ( pluginRegistry == null )
		{
			throw new ExtensionException( new String[]{EXTENSION_POINT},
					ExtensionException.EXTENSION_POINT_NOT_FOUND );
		}

		IExtensionPoint extensionPoint = pluginRegistry
				.getExtensionPoint( EXTENSION_POINT );
		if ( extensionPoint == null )
			throw new ExtensionException( new String[]{EXTENSION_POINT},
					ExtensionException.EXTENSION_POINT_NOT_FOUND );

		IExtension[] extensions = extensionPoint.getExtensions( );
		if ( extensions != null )
		{
			for ( int i = 0; i < extensions.length; i++ )
				loadExtension( extensions[i] );
		}
	}

	/**
	 * Load one extension which can be
	 * 
	 * @param extension
	 *            one extension which extends the model extension point.
	 * @throws ExtensionException
	 *             if error is found when loading extension
	 * @throws MetaDataException
	 *             if error encountered when adding the element to metadata
	 *             dictionary.
	 */

	private static void loadExtension( IExtension extension )
			throws ExtensionException, MetaDataException
	{
		IConfigurationElement[] configElements = extension
				.getConfigurationElements( );

		for ( int i = 0; i < configElements.length; i++ )
		{
			IConfigurationElement currentTag = configElements[i];
			if ( ELEMENT_TAG.equals( currentTag.getName( ) ) )
			{
				loadElement( currentTag );
			}
		}
	}

	/**
	 * Loads the extended element and its properties.
	 * 
	 * @param elementTag
	 *            the element tag
	 * @throws MetaDataException
	 *             if error encountered when adding the element to metadata
	 *             dictionary.
	 * @throws ExtensionException
	 *             if the class some attribute specifies can not be instanced.
	 */

	private static void loadElement( IConfigurationElement elementTag )
			throws MetaDataException, ExtensionException
	{
		IReportItemFactory romExtension = null;
		ExtensionElementDefn elementDefn = null;

		String name = elementTag.getAttribute( NAME_ATTRIB );
		String displayNameID = elementTag.getAttribute( DISPLAY_NAME_ID_ATTRIB );
		String hasStyle = elementTag.getAttribute( HAS_STYLE_ATTRIB );
		String defaultStyle = elementTag.getAttribute( DEFAULT_STYLE_ATTRIB );
		String isNameRequired = elementTag
				.getAttribute( IS_NAME_REQUIRED_ATTRIB );
		String className = elementTag.getAttribute( CLASS_ATTRIB );

		checkRequiredAttribute( NAME_ATTRIB, name );
		checkRequiredAttribute( CLASS_ATTRIB, className );

		try
		{
			romExtension = (IReportItemFactory) elementTag
					.createExecutableExtension( CLASS_ATTRIB );

			elementDefn = new ExtensionElementDefn( name, romExtension );
			elementDefn.setAbstract( false );
			elementDefn.setAllowsUserProperties( false );
			elementDefn.setCanExtend( true );
			elementDefn.setDisplayNameKey( displayNameID );
			elementDefn.setExtends( null );
			elementDefn.setJavaClass( null );
			elementDefn.setNameSpaceID( RootElement.ELEMENT_NAME_SPACE );
			elementDefn.setSelector( defaultStyle );

			if ( "true".equalsIgnoreCase( isNameRequired ) ) //$NON-NLS-1$
				elementDefn.setNameOption( MetaDataConstants.REQUIRED_NAME );
			else
				elementDefn.setNameOption( MetaDataConstants.OPTIONAL_NAME );

			if ( !StringUtil.isBlank( hasStyle ) )
				elementDefn.setHasStyle( Boolean.valueOf( hasStyle )
						.booleanValue( ) );

			List propList = new ArrayList( );

			IConfigurationElement[] elements = elementTag.getChildren( );
			for ( int i = 0; i < elements.length; i++ )
			{
				if ( PROPERTY_TAG.equalsIgnoreCase( elements[i].getName( ) ) )
				{
					SystemPropertyDefn extPropDefn = loadProperty( elementTag,
							elements[i], elementDefn );
					// Unique check is performed in addProperty()
					elementDefn.addProperty( extPropDefn );
				}
				else if ( PROPERTY_GROUP_TAG.equalsIgnoreCase( elements[i]
						.getName( ) ) )
				{
					loadPropertyGroup( elementTag, elements[i], elementDefn,
							propList );
				}
				else if ( STYLE_PROPERTY_TAG.equalsIgnoreCase( elements[i]
						.getName( ) ) )
				{
					// StyleProperty
				}
				else if ( METHOD_TAG.equalsIgnoreCase( elements[i].getName( ) ) )
				{
					// Method
				}
			}
		}
		catch ( FrameworkException e )
		{
			throw new ExtensionException( new String[]{className},
					ExtensionException.FAILED_TO_CREATE_INSTANCE );
		}

		MetaDataDictionary.getInstance( ).addExtension( elementDefn );
	}

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
	 *             if the class some attribute specifies can not be instanced.
	 */

	private static SystemPropertyDefn loadProperty(
			IConfigurationElement elementTag, IConfigurationElement propTag,
			ExtensionElementDefn elementDefn ) throws ExtensionException
	{
		String name = propTag.getAttribute( NAME_ATTRIB );
		String displayNameID = propTag.getAttribute( DISPLAY_NAME_ID_ATTRIB );
		String type = propTag.getAttribute( TYPE_ATTRIB );
		String canInherit = propTag.getAttribute( CAN_INHERIT_ATTRIB );
		String isStyleProperty = propTag
				.getAttribute( IS_STYLE_PROPERTY_ATTRIB );
		String detailType = propTag.getAttribute( DETAIL_TYPE_ATTRIB );
		String defaultValue = propTag.getAttribute( DEFAULT_VALUE_ATTRIB );
		String isVisible = propTag.getAttribute( IS_VISIBLE_ATTRIB );
		
		checkRequiredAttribute( NAME_ATTRIB, name );
		checkRequiredAttribute( DISPLAY_NAME_ID_ATTRIB, displayNameID );
		checkRequiredAttribute( TYPE_ATTRIB, type );

		PropertyType propType = MetaDataDictionary.getInstance( )
				.getPropertyType( type );
		if ( propType == null )
			throw new ExtensionException( new String[]{type},
					ExtensionException.INVALID_PROPERTY_TYPE );

		SystemPropertyDefn extPropDefn = null;

		extPropDefn = new ExtensionPropertyDefn( elementDefn
				.getElementFactory( ).getMessages( ) );

		extPropDefn.setExtended( true );
		extPropDefn.setName( name );
		extPropDefn.setDisplayNameID( displayNameID );
		extPropDefn.setDefault( defaultValue );
		extPropDefn.setType( propType );
		extPropDefn.setIntrinsic( false );
		extPropDefn.setStyleProperty( false );

		// TODO: DetailType can be set

		if ( !StringUtil.isBlank( isStyleProperty ) )
			extPropDefn.setStyleProperty( Boolean.valueOf( isStyleProperty )
					.booleanValue( ) );

		if ( !StringUtil.isBlank( canInherit ) )
			extPropDefn.setCanInherit( Boolean.valueOf( canInherit )
					.booleanValue( ) );

		if ( !StringUtil.isBlank( isVisible ) )
			extPropDefn.setVisible( Boolean.valueOf( isVisible )
					.booleanValue( ) );
		
		List choiceList = new ArrayList( );

		IConfigurationElement[] elements = propTag.getChildren( );
		for ( int k = 0; k < elements.length; k++ )
		{
			if ( CHOICE_TAG.equalsIgnoreCase( elements[k].getName( ) ) )
			{
				ExtensionChoice choiceDefn = loadChoice( elementTag, propTag,
						elements[k], elementDefn.getElementFactory( )
								.getMessages( ) );
				choiceList.add( choiceDefn );
			}
		}

		if ( choiceList.size( ) > 0 )
		{
			Choice[] choices = new Choice[choiceList.size( )];
			choiceList.toArray( choices );
			ChoiceSet choiceSet = new ChoiceSet( );
			choiceSet.setChoices( choices );
			extPropDefn.setDetails( choiceSet );
		}

		return extPropDefn;
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

	private static void loadPropertyGroup( IConfigurationElement elementTag,
			IConfigurationElement propGroupTag,
			ExtensionElementDefn elementDefn, List propList )
			throws MetaDataException
	{
		String displayNameID = propGroupTag
				.getAttribute( DISPLAY_NAME_ID_ATTRIB );

		checkRequiredAttribute( DISPLAY_NAME_ID_ATTRIB, displayNameID );

		IConfigurationElement[] elements = propGroupTag.getChildren( );
		for ( int i = 0; i < elements.length; i++ )
		{
			if ( PROPERTY_TAG.equalsIgnoreCase( elements[i].getName( ) ) )
			{
				SystemPropertyDefn extPropDefn = loadProperty( elementTag,
						elements[i], elementDefn );
				extPropDefn.setGroupNameKey( displayNameID );
				elementDefn.addProperty( extPropDefn );
			}
		}
	}

	/**
	 * Loads one choice.
	 * 
	 * @param elementTag
	 *            the element tag
	 * @param propTag
	 *            the property tag
	 * @param choiceTag
	 *            the choice tag
	 * @param messages
	 *            messages providing localized messages
	 * @return one choice
	 * @throws ExtensionException
	 *             if the class some attribute specifies can not be instanced.
	 */

	private static ExtensionChoice loadChoice(
			IConfigurationElement elementTag, IConfigurationElement propTag,
			IConfigurationElement choiceTag, IMessages messages )
			throws ExtensionException
	{
		ExtensionChoice choice = new ExtensionChoice( messages );

		String name = choiceTag.getAttribute( NAME_ATTRIB );
		String value = choiceTag.getAttribute( VALUE_ATTRIB );
		String displayNameID = choiceTag.getAttribute( DISPLAY_NAME_ID_ATTRIB );

		checkRequiredAttribute( NAME_ATTRIB, name );
		checkRequiredAttribute( DISPLAY_NAME_ID_ATTRIB, displayNameID );

		choice.setName( name );
		choice.setValue( value );
		choice.setDisplayNameKey( displayNameID );

		return choice;
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

	private static void checkRequiredAttribute( String name, String value )
			throws ExtensionException
	{
		if ( StringUtil.isBlank( value ) )
			throw new ExtensionException( new String[]{name},
					ExtensionException.VALUE_REQUIRED );
	}

}
