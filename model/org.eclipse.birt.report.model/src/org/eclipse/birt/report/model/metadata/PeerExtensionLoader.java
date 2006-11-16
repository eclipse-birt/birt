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
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the extension loader for peer extension.
 */

public class PeerExtensionLoader extends ExtensionLoader
{

	/**
	 * The name of extension point.
	 */

	public static final String EXTENSION_POINT = "org.eclipse.birt.report.model.reportItemModel"; //$NON-NLS-1$

	private static final String ELEMENT_TAG = "reportItem"; //$NON-NLS-1$

	/**
	 * Constructs the extension loader for peer extension.
	 */

	public PeerExtensionLoader( )
	{
		super( EXTENSION_POINT );
	}

	/**
	 * Load one extension.
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
		private static final String ARGUMENT_TAG = "argument"; //$NON-NLS-1$
		private static final String TOOL_TIP_ID_ATTRIB = "toolTipID"; //$NON-NLS-1$
		private static final String RETURN_TYPE_ATTRIB = "returnType"; //$NON-NLS-1$
		private static final String TAG_ID_ATTRIB = "tagID"; //$NON-NLS-1$		
		private static final String IS_STATIC_ATTRIB = "isStatic"; //$NON-NLS-1$
		private static final String STYLE_TAG = "style"; //$NON-NLS-1$

		private static final String DEFAULT_STYLE_ATTRIB = "defaultStyle"; //$NON-NLS-1$
		private static final String IS_NAME_REQUIRED_ATTRIB = "isNameRequired"; //$NON-NLS-1$
		private static final String CLASS_ATTRIB = "class"; //$NON-NLS-1$
		private static final String IS_LIST_ATTRIB = "isList"; //$NON-NLS-1$

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
						ExtensionPropertyDefn extPropDefn = loadMethod(
								elementTag, elements[i], elementDefn );

						// Unique check is performed in addProperty()

						elementDefn.addProperty( extPropDefn );
					}
					else if ( STYLE_TAG
							.equalsIgnoreCase( elements[i].getName( ) ) )
					{
						PredefinedStyle style = loadStyle( elementTag,
								elements[i], elementDefn );

						MetaDataDictionary.getInstance( ).addPredefinedStyle(
								style );
					}
				}
			}
			catch ( FrameworkException e )
			{
				throw new ExtensionException(
						new String[]{className},
						ExtensionException.DESIGN_EXCEPTION_FAILED_TO_CREATE_INSTANCE );
			}
			elementDefn.extensionPoint = EXTENSION_POINT;
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

			String defaultValue = propTag.getAttribute( DEFAULT_VALUE_ATTRIB );
			String isEncrypted = propTag.getAttribute( IS_ENCRYPTABLE_ATTRIB );
			String defaultDisplayName = propTag
					.getAttribute( DEFAULT_DISPLAY_NAME_ATTRIB );

			checkRequiredAttribute( NAME_ATTRIB, name );
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

			extPropDefn.setName( name );
			extPropDefn.setDisplayNameID( displayNameID );
			extPropDefn.setType( propType );
			try
			{
				Object value = extPropDefn.validateXml( null, defaultValue );
				extPropDefn.setDefault( value );
			}
			catch ( PropertyValueException e )
			{
				throw new ExtensionException(
						new String[]{defaultValue},
						ExtensionException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE );
			}
			extPropDefn.setIntrinsic( false );
			extPropDefn.setStyleProperty( false );
			extPropDefn.setDefaultDisplayName( defaultDisplayName );

			if ( !StringUtil.isBlank( canInherit ) )
				extPropDefn.setCanInherit( Boolean.valueOf( canInherit )
						.booleanValue( ) );

			if ( !StringUtil.isBlank( isEncrypted ) )
				extPropDefn.setIsEncryptable( Boolean.valueOf( isEncrypted )
						.booleanValue( ) );

			List choiceList = new ArrayList( );

			IConfigurationElement[] elements = propTag.getChildren( );
			for ( int k = 0; k < elements.length; k++ )
			{
				if ( CHOICE_TAG.equalsIgnoreCase( elements[k].getName( ) ) )
				{
					ExtensionChoice choiceDefn = new ExtensionChoice(
							( (PeerExtensionElementDefn) elementDefn )
									.getReportItemFactory( ).getMessages( ) );
					loadChoice( elements[k], choiceDefn, extPropDefn );
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
			
			// do some checks about the detail type
			String detailType = propTag.getAttribute( DETAIL_TYPE_ATTRIB );
			switch ( propType.getTypeCode( ) )
			{
				case IPropertyType.STRUCT_TYPE :
					String isList = propTag.getAttribute( IS_LIST_ATTRIB );
					isList = StringUtil.trimString( isList );
					// by default it is 'false'
					if ( "true".equalsIgnoreCase( isList ) ) //$NON-NLS-1$
						extPropDefn.setIsList( true );
					else
						extPropDefn.setIsList( false );
					extPropDefn.setDetails( detailType );
					break;
			}


			return extPropDefn;
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

		ExtensionPropertyDefn loadMethod( IConfigurationElement elementTag,
				IConfigurationElement propTag, ExtensionElementDefn elementDefn )
				throws ExtensionException
		{
			String name = propTag.getAttribute( NAME_ATTRIB );
			String displayNameID = propTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );
			String toolTipID = propTag.getAttribute( TOOL_TIP_ID_ATTRIB );
			String returnType = propTag.getAttribute( RETURN_TYPE_ATTRIB );
			String isStatic = propTag.getAttribute( IS_STATIC_ATTRIB );

			if ( name == null )
			{
				throw new ExtensionException( new String[]{},
						ExtensionException.DESIGN_EXCEPTION_MISSING_METHOD_NAME );
			}
			if ( displayNameID == null )
			{
				throw new ExtensionException( new String[]{},
						ExtensionException.DESIGN_EXCEPTION_VALUE_REQUIRED );
			}

			// Note that here ROM supports overloadding, while JavaScript not.
			// finds the method info if it has been parsed.

			MethodInfo methodInfo = new MethodInfo( false );

			methodInfo.setName( name );
			methodInfo.setDisplayNameKey( displayNameID );
			methodInfo.setReturnType( returnType );
			methodInfo.setToolTipKey( toolTipID );
			methodInfo.setStatic( Boolean.getBoolean( isStatic ) );

			IConfigurationElement[] elements = propTag.getChildren( );
			ArgumentInfoList argumentList = null;

			for ( int i = 0; i < elements.length; i++ )
			{
				if ( ARGUMENT_TAG.equalsIgnoreCase( elements[i].getName( ) ) )
				{
					ArgumentInfo argument = loadArgument( elementTag,
							elements[i], elementDefn );

					if ( argumentList == null )
						argumentList = new ArgumentInfoList( );

					try
					{
						argumentList.addArgument( argument );
					}
					catch ( MetaDataException e )
					{
						throw new ExtensionException(
								new String[]{},
								ExtensionException.DESIGN_EXCEPTION_DUPLICATE_ARGUMENT_NAME );
					}

				}
			}

			methodInfo.addArgumentList( argumentList );
			return addDefnTo( elementDefn, methodInfo );
		}

		private ExtensionPropertyDefn addDefnTo(
				ExtensionElementDefn elementDefn, MethodInfo methodInfo )
				throws ExtensionException
		{
			ExtensionPropertyDefn extPropDefn = new ExtensionPropertyDefn(
					( (PeerExtensionElementDefn) elementDefn )
							.getReportItemFactory( ).getMessages( ) );

			PropertyType typeDefn = MetaDataDictionary.getInstance( )
					.getPropertyType( PropertyType.SCRIPT_TYPE );

			String name = methodInfo.getName( );
			String displayNameID = methodInfo.getDisplayNameKey( );

			extPropDefn.setName( name );
			extPropDefn.setDisplayNameID( displayNameID );
			extPropDefn.setType( typeDefn );
			extPropDefn.setGroupNameKey( null );
			extPropDefn.setCanInherit( true );
			extPropDefn.setIntrinsic( false );
			extPropDefn.setStyleProperty( false );
			extPropDefn.setDetails( methodInfo );

			return extPropDefn;
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

		ArgumentInfo loadArgument( IConfigurationElement elementTag,
				IConfigurationElement propTag, ExtensionElementDefn elementDefn )
				throws ExtensionException
		{
			String name = propTag.getAttribute( NAME_ATTRIB );
			String tagID = propTag.getAttribute( TAG_ID_ATTRIB );
			String type = propTag.getAttribute( TYPE_ATTRIB );

			if ( name == null )
				return null;

			ArgumentInfo argument = new ArgumentInfo( );
			argument.setName( name );
			argument.setType( type );
			argument.setDisplayNameKey( tagID );

			return argument;
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

		PredefinedStyle loadStyle( IConfigurationElement elementTag,
				IConfigurationElement propTag, ExtensionElementDefn elementDefn )
				throws ExtensionException
		{
			String name = propTag.getAttribute( NAME_ATTRIB );
			String displayNameID = propTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );

			PredefinedStyle style = new PredefinedStyle( );
			style.setName( name );
			style.setDisplayNameKey( displayNameID );
			return style;
		}
	}
}
