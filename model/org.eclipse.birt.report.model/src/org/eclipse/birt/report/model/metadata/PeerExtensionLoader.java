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
import org.eclipse.birt.report.model.api.extension.IReportItemFactory;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * TODO: Document required
 */
public class PeerExtensionLoader extends ExtensionLoader
{

	/**
	 * The name of extension point.
	 */

	public static final String EXTENSION_POINT = "org.eclipse.birt.report.model.reportItemModel"; //$NON-NLS-1$

	private static final String ELEMENT_TAG = "reportItem"; //$NON-NLS-1$

	/**
	 */
	public PeerExtensionLoader( )
	{
		super( EXTENSION_POINT );
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

	void loadExtension( IExtension extension ) throws ExtensionException,
			MetaDataException
	{
		IConfigurationElement[] configElements = extension
				.getConfigurationElements( );

		PeerExtensionElementLoader loader = new PeerExtensionElementLoader(
				extension );
		for ( int i = 0; i < configElements.length; i++ )
		{
			IConfigurationElement currentTag = configElements[i];
			if ( ELEMENT_TAG.equals( currentTag.getName( ) ) )
			{
				loader.loadElement( currentTag );
			}
		}
	}

	class PeerExtensionElementLoader extends ExtensionElementLoader
	{

		private static final String STYLE_PROPERTY_TAG = "styleProperty"; //$NON-NLS-1$
		private static final String METHOD_TAG = "method"; //$NON-NLS-1$

		private static final String HAS_STYLE_ATTRIB = "hasStyle"; //$NON-NLS-1$
		private static final String DEFAULT_STYLE_ATTRIB = "defaultStyle"; //$NON-NLS-1$
		private static final String IS_NAME_REQUIRED_ATTRIB = "isNameRequired"; //$NON-NLS-1$
		private static final String CLASS_ATTRIB = "class"; //$NON-NLS-1$
		private static final String IS_STYLE_PROPERTY_ATTRIB = "isStyleProperty"; //$NON-NLS-1$

		PeerExtensionElementLoader( IExtension extension )
		{
			super( extension );
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
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		void loadElement( IConfigurationElement elementTag )
				throws MetaDataException, ExtensionException
		{
			IReportItemFactory factory = null;
			PeerExtensionElementDefn elementDefn = null;

			String extensionName = elementTag
					.getAttribute( EXTENSION_NAME_ATTRIB );
			String displayNameID = elementTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );
			String hasStyle = elementTag.getAttribute( HAS_STYLE_ATTRIB );
			String defaultStyle = elementTag
					.getAttribute( DEFAULT_STYLE_ATTRIB );
			String isNameRequired = elementTag
					.getAttribute( IS_NAME_REQUIRED_ATTRIB );
			String className = elementTag.getAttribute( CLASS_ATTRIB );

			checkRequiredAttribute( EXTENSION_NAME_ATTRIB, extensionName );
			checkRequiredAttribute( CLASS_ATTRIB, className );

			try
			{
				factory = (IReportItemFactory) elementTag
						.createExecutableExtension( CLASS_ATTRIB );

				elementDefn = new PeerExtensionElementDefn( extensionName,
						factory );
				elementDefn.setAbstract( false );
				elementDefn.setAllowsUserProperties( false );
				elementDefn.setCanExtend( true );
				elementDefn.setDisplayNameKey( displayNameID );
				elementDefn.setExtends( null );
				elementDefn.setJavaClass( null );
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
						SystemPropertyDefn extPropDefn = loadProperty(
								elementTag, elements[i], elementDefn );
						// Unique check is performed in addProperty()
						elementDefn.addProperty( extPropDefn );
					}
					else if ( PROPERTY_VISIBILITY_TAG
							.equalsIgnoreCase( elements[i].getName( ) ) )
					{
						loadPropertyVisibility( elements[i], elementDefn );
					}
					else if ( PROPERTY_GROUP_TAG.equalsIgnoreCase( elements[i]
							.getName( ) ) )
					{
						loadPropertyGroup( elementTag, elements[i],
								elementDefn, propList );
					}
					else if ( STYLE_PROPERTY_TAG.equalsIgnoreCase( elements[i]
							.getName( ) ) )
					{
						// StyleProperty
					}
					else if ( METHOD_TAG.equalsIgnoreCase( elements[i]
							.getName( ) ) )
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
		 * @return the property definition
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		ExtensionPropertyDefn loadProperty( IConfigurationElement elementTag,
				IConfigurationElement propTag, ExtensionElementDefn elementDefn )
				throws ExtensionException
		{
			String name = propTag.getAttribute( NAME_ATTRIB );
			String displayNameID = propTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );
			String type = propTag.getAttribute( TYPE_ATTRIB );
			String canInherit = propTag.getAttribute( CAN_INHERIT_ATTRIB );
			String isStyleProperty = propTag
					.getAttribute( IS_STYLE_PROPERTY_ATTRIB );
			String defaultValue = propTag.getAttribute( DEFAULT_VALUE_ATTRIB );
			String visibility = propTag
					.getAttribute( PROPERTY_VISIBILITY_ATTRIB );

			checkRequiredAttribute( NAME_ATTRIB, name );
			checkRequiredAttribute( DISPLAY_NAME_ID_ATTRIB, displayNameID );
			checkRequiredAttribute( TYPE_ATTRIB, type );

			PropertyType propType = MetaDataDictionary.getInstance( )
					.getPropertyType( type );
			if ( propType == null )
				throw new ExtensionException(
						new String[]{type},
						ExtensionException.DESIGN_EXCEPTION_INVALID_PROPERTY_TYPE );

			ExtensionPropertyDefn extPropDefn = new ExtensionPropertyDefn(
					( (PeerExtensionElementDefn) elementDefn )
							.getReportItemFactory( ).getMessages( ) );

			extPropDefn.setExtended( true );
			extPropDefn.setName( elementDefn.getName( ) + "::" + name ); //$NON-NLS-1$
			extPropDefn.setDisplayNameID( displayNameID );
			extPropDefn.setDefault( defaultValue );
			extPropDefn.setType( propType );
			extPropDefn.setIntrinsic( false );
			extPropDefn.setStyleProperty( false );

			if ( !StringUtil.isBlank( isStyleProperty ) )
				extPropDefn.setStyleProperty( Boolean.valueOf( isStyleProperty )
						.booleanValue( ) );

			if ( !StringUtil.isBlank( canInherit ) )
				extPropDefn.setCanInherit( Boolean.valueOf( canInherit )
						.booleanValue( ) );

			if ( !StringUtil.isBlank( visibility ) )
				extPropDefn.setVisibility( visibility );
			else if ( propType.getTypeCode( ) == PropertyType.STRUCT_TYPE )
				extPropDefn
						.setVisibility( ElementPropertyDefn.HIDDEN_IN_PROPERTY_SHEET );

			List choiceList = new ArrayList( );

			IConfigurationElement[] elements = propTag.getChildren( );
			for ( int k = 0; k < elements.length; k++ )
			{
				if ( CHOICE_TAG.equalsIgnoreCase( elements[k].getName( ) ) )
				{
					ExtensionChoice choiceDefn = new ExtensionChoice(
							( (PeerExtensionElementDefn) elementDefn )
									.getReportItemFactory( ).getMessages( ) );
					loadChoice( elements[k], choiceDefn );
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
	}
}
