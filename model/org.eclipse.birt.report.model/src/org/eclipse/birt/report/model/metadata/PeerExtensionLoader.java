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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
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
		private static final String STYLE_TAG = "style"; //$NON-NLS-1$
		private static final String SLOT_TAG = "slot"; //$NON-NLS-1$
		private static final String ELEMENT_TYPE_TAG = "elementType"; //$NON-NLS-1$

		private static final String TOOL_TIP_ID_ATTRIB = "toolTipID"; //$NON-NLS-1$
		private static final String RETURN_TYPE_ATTRIB = "returnType"; //$NON-NLS-1$
		private static final String TAG_ID_ATTRIB = "tagID"; //$NON-NLS-1$		
		private static final String IS_STATIC_ATTRIB = "isStatic"; //$NON-NLS-1$
		private static final String DEFAULT_STYLE_ATTRIB = "defaultStyle"; //$NON-NLS-1$
		private static final String IS_NAME_REQUIRED_ATTRIB = "isNameRequired"; //$NON-NLS-1$
		private static final String CLASS_ATTRIB = "class"; //$NON-NLS-1$
		private static final String EXTENDS_FROM_ATTRIB = "extendsFrom"; //$NON-NLS-1$
		private static final String XML_TAG_NAME_ATTRIB = "xmlTagName"; //$NON-NLS-1$
		private static final String MULTIPLE_CARDINALITY_ATTRIB = "multipleCardinality"; //$NON-NLS-1$
		private static final String DETAIL_TYPE_ATTRIB = "detailType"; //$NON-NLS-1$
		private static final String SUB_TYPE_ATTRIB = "subType"; //$NON-NLS-1$
		private static final String IS_LIST_ATTRIB = "isList"; //$NON-NLS-1$

		/**
		 * List of the property types that are allowed for the extensions.
		 */

		List allowedPropertyTypes = null;

		/**
		 * List of the property types that are allowed for sub-type for the
		 * extensions.
		 */

		List allowedSubPropertyTypes = null;

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
			// load required parts
			String extensionName = elementTag
					.getAttribute( EXTENSION_NAME_ATTRIB );
			String className = elementTag.getAttribute( CLASS_ATTRIB );
			checkRequiredAttribute( EXTENSION_NAME_ATTRIB, extensionName );
			checkRequiredAttribute( CLASS_ATTRIB, className );

			// load optional parts
			String displayNameID = elementTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );

			String defaultStyle = elementTag
					.getAttribute( DEFAULT_STYLE_ATTRIB );
			String isNameRequired = elementTag
					.getAttribute( IS_NAME_REQUIRED_ATTRIB );
			String extendsFrom = elementTag.getAttribute( EXTENDS_FROM_ATTRIB );
			if ( StringUtil.isBlank( extendsFrom ) )
				extendsFrom = ReportDesignConstants.EXTENDED_ITEM;

			IReportItemFactory factory = null;
			PeerExtensionElementDefn elementDefn = null;
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
				elementDefn.setExtends( extendsFrom );
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
					else if ( SLOT_TAG
							.equalsIgnoreCase( elements[i].getName( ) ) )
					{
						SlotDefn slotDefn = loadSlot( elementTag, elements[i],
								elementDefn );
						elementDefn.addSlot( slotDefn );
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
			// load required parts
			String name = propTag.getAttribute( NAME_ATTRIB );
			String type = propTag.getAttribute( TYPE_ATTRIB );
			checkRequiredAttribute( NAME_ATTRIB, name );
			checkRequiredAttribute( TYPE_ATTRIB, type );

			// load optional parts
			String displayNameID = propTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );
			String canInherit = propTag.getAttribute( CAN_INHERIT_ATTRIB );
			String defaultValue = propTag.getAttribute( DEFAULT_VALUE_ATTRIB );
			String isEncrypted = propTag.getAttribute( IS_ENCRYPTABLE_ATTRIB );
			String defaultDisplayName = propTag
					.getAttribute( DEFAULT_DISPLAY_NAME_ATTRIB );
			String subType = propTag.getAttribute( SUB_TYPE_ATTRIB );
			// by default set it to 'string' type
			if ( StringUtil.isBlank( subType ) )
				subType = IPropertyType.STRING_TYPE_NAME;
			MetaDataDictionary dd = MetaDataDictionary.getInstance( );
			PropertyType propType = dd.getPropertyType( type );

			// not well-recognized or not supported by extension, fire error
			if ( propType == null
					|| !getAllowedPropertyTypes( ).contains( propType ) )
				throw new ExtensionException(
						new String[]{type},
						MetaDataException.DESIGN_EXCEPTION_INVALID_PROPERTY_TYPE );
			PropertyType subPropType = null;
			if ( propType.getTypeCode( ) == IPropertyType.LIST_TYPE )
			{
				subPropType = MetaDataDictionary.getInstance( )
						.getPropertyType( subType );
				if ( subPropType == null
						|| !getAllowedSubPropertyTypes( )
								.contains( subPropType ) )
					throw new ExtensionException(
							new String[]{name, subType},
							MetaDataException.DESIGN_EXCEPTION_UNSUPPORTED_SUB_TYPE );
			}

			ExtensionPropertyDefn extPropDefn = new ExtensionPropertyDefn(
					( (PeerExtensionElementDefn) elementDefn )
							.getReportItemFactory( ).getMessages( ) );

			extPropDefn.setName( name );
			extPropDefn.setDisplayNameID( displayNameID );
			extPropDefn.setType( propType );
			extPropDefn.setSubType( subPropType );
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

			// do some checks about the detail type
			String detailType = propTag.getAttribute( DETAIL_TYPE_ATTRIB );
			switch ( propType.getTypeCode( ) )
			{
				case IPropertyType.CHOICE_TYPE :
					// can not define detail-type and own choice list
					// synchronously, neither can be empty synchronously
					if ( ( !StringUtil.isBlank( detailType ) && choiceList
							.size( ) > 0 )
							|| ( StringUtil.isBlank( detailType ) && choiceList
									.size( ) <= 0 ) )
						throw new ExtensionException(
								new String[]{detailType},
								ExtensionException.DESIGN_EXCEPTION_INVALID_CHOICE_PROPERTY );
					if ( choiceList.size( ) > 0 )
					{
						Choice[] choices = new Choice[choiceList.size( )];
						choiceList.toArray( choices );
						ChoiceSet choiceSet = new ChoiceSet( );
						choiceSet.setChoices( choices );
						extPropDefn.setDetails( choiceSet );
					}
					else if ( !StringUtil.isBlank( detailType ) )
					{
						extPropDefn.setDetails( dd.getChoiceSet( detailType ) );
					}
					break;
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

			// after loading the choices, then validates the default value
			if ( !StringUtil.isBlank( defaultValue ) )
			{
				try
				{
					Object value = extPropDefn.validateXml( null, defaultValue );
					extPropDefn.setDefault( value );
				}
				catch ( PropertyValueException e )
				{
					throw new ExtensionException(
							new String[]{name, elementDefn.getName( )},
							MetaDataException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE );
				}
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
						MetaDataException.DESIGN_EXCEPTION_MISSING_METHOD_NAME );
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
			// by default 'isStatic' is false
			if ( !StringUtil.isBlank( isStatic )
					&& Boolean.valueOf( isStatic ).booleanValue( ) )
				methodInfo.setStatic( true );

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
								MetaDataException.DESIGN_EXCEPTION_DUPLICATE_ARGUMENT_NAME );
					}

				}
			}

			methodInfo.addArgumentList( argumentList );
			return addDefnTo( elementDefn, methodInfo );
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
		 * @return the property definition
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		PredefinedStyle loadStyle( IConfigurationElement elementTag,
				IConfigurationElement propTag, ElementDefn elementDefn )
				throws ExtensionException
		{
			// when add the style to the meta-data, checks will be done, such as
			// the unique and non-empty of the name, so do nothing here

			String name = propTag.getAttribute( NAME_ATTRIB );
			String displayNameID = propTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );

			PredefinedStyle style = new PredefinedStyle( );
			style.setName( name );
			style.setDisplayNameKey( displayNameID );
			return style;
		}

		/**
		 * Loads one slot definition of the given element.
		 * 
		 * @param elementTag
		 *            the element tag
		 * @param propTag
		 *            the property tag
		 * @param elementDefn
		 *            element definition
		 * @return the slot definition
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		SlotDefn loadSlot( IConfigurationElement elementTag,
				IConfigurationElement propTag, ExtensionElementDefn elementDefn )
				throws ExtensionException, MetaDataException
		{
			// read required parts
			String name = propTag.getAttribute( NAME_ATTRIB );
			String xmlTagName = propTag.getAttribute( XML_TAG_NAME_ATTRIB );
			checkRequiredAttribute( NAME_ATTRIB, name );
			checkRequiredAttribute( XML_TAG_NAME_ATTRIB, xmlTagName );

			// optional parts
			String displayNameID = propTag
					.getAttribute( DISPLAY_NAME_ID_ATTRIB );
			String defaultDisplayName = propTag
					.getAttribute( DEFAULT_DISPLAY_NAME_ATTRIB );
			String multipleCardinality = propTag
					.getAttribute( MULTIPLE_CARDINALITY_ATTRIB );
			String selector = propTag.getAttribute( DEFAULT_STYLE_ATTRIB );

			ExtensionSlotDefn slot = new ExtensionSlotDefn(
					( (PeerExtensionElementDefn) elementDefn )
							.getReportItemFactory( ).getMessages( ) );
			slot.setName( name );
			slot.setXmlName( xmlTagName );
			slot.setDisplayNameID( StringUtil.trimString( displayNameID ) );
			slot.setDefaultDisplayName( StringUtil
					.trimString( defaultDisplayName ) );
			// by default 'multipleCardinality' is true
			if ( !StringUtil.isBlank( multipleCardinality )
					&& !Boolean.valueOf( multipleCardinality ).booleanValue( ) )
				slot.setMultipleCardinality( false );
			slot.setSelector( StringUtil.trimString( selector ) );

			// load the element types
			IConfigurationElement[] elements = propTag.getChildren( );
			for ( int i = 0; i < elements.length; i++ )
			{
				if ( ELEMENT_TYPE_TAG.equalsIgnoreCase( elements[i].getName( ) ) )
				{
					String elementType = loadSlotElementType( elements[i] );
					slot.addType( elementType );
				}
			}

			return slot;
		}

		/**
		 * Loads one element type name of the given element.
		 * 
		 * @param elementTag
		 *            the element tag
		 * @param elementTypeTag
		 *            the element type tag
		 * @param elementDefn
		 *            element definition
		 * @return name of the element type
		 * @throws ExtensionException
		 *             if the class some attribute specifies can not be
		 *             instanced.
		 */

		String loadSlotElementType( IConfigurationElement elementTypeTag )
				throws ExtensionException
		{
			// read required parts
			String name = elementTypeTag.getAttribute( NAME_ATTRIB );
			checkRequiredAttribute( NAME_ATTRIB, name );

			return name;
		}

		/**
		 * Determines whether the element type is invalid or not. Now only
		 * support(ReportItem, Column, Row, Cell, ListingGroup).
		 * 
		 * @param type
		 *            the type
		 * @return true if the type is valid, otherwise false
		 */

		boolean isValidElementType( String type )
		{
			if ( ReportDesignConstants.EXTENDED_ITEM.equalsIgnoreCase( type )
					|| ReportDesignConstants.COLUMN_ELEMENT
							.equalsIgnoreCase( type )
					|| ReportDesignConstants.ROW_ELEMENT
							.equalsIgnoreCase( type )
					|| ReportDesignConstants.CELL_ELEMENT
							.equalsIgnoreCase( type )
					|| ReportDesignConstants.GROUP_ELEMENT
							.equalsIgnoreCase( type ) )
				return true;
			return false;
		}

		/**
		 * Gets the allowed property types for the extensions.
		 * 
		 * @return the allowed property types for the extensions
		 */

		List getAllowedPropertyTypes( )
		{
			if ( allowedPropertyTypes != null )
				return allowedPropertyTypes;

			allowedPropertyTypes = new ArrayList( );
			Iterator iter = MetaDataDictionary.getInstance( )
					.getPropertyTypes( ).iterator( );
			while ( iter.hasNext( ) )
			{
				PropertyType propType = (PropertyType) iter.next( );
				int type = propType.getTypeCode( );
				switch ( type )
				{
					case IPropertyType.STRING_TYPE :
					case IPropertyType.NUMBER_TYPE :
					case IPropertyType.INTEGER_TYPE :
					case IPropertyType.DIMENSION_TYPE :
					case IPropertyType.COLOR_TYPE :
					case IPropertyType.CHOICE_TYPE :
					case IPropertyType.BOOLEAN_TYPE :
					case IPropertyType.EXPRESSION_TYPE :
					case IPropertyType.HTML_TYPE :
					case IPropertyType.RESOURCE_KEY_TYPE :
					case IPropertyType.URI_TYPE :
					case IPropertyType.DATE_TIME_TYPE :
					case IPropertyType.XML_TYPE :
					case IPropertyType.NAME_TYPE :
					case IPropertyType.FLOAT_TYPE :
					case IPropertyType.LITERAL_STRING_TYPE :
					case IPropertyType.LIST_TYPE :
					case IPropertyType.STRUCT_TYPE :
						allowedPropertyTypes.add( propType );
						break;
					default :
						break;
				}
			}

			return allowedPropertyTypes;
		}

		/**
		 * Gets the allowed property types for the extensions.
		 * 
		 * @return the allowed property types for the extensions
		 */

		List getAllowedSubPropertyTypes( )
		{
			if ( allowedSubPropertyTypes != null
					&& !allowedSubPropertyTypes.isEmpty( ) )
				return allowedSubPropertyTypes;

			allowedSubPropertyTypes = new ArrayList( );
			Iterator iter = MetaDataDictionary.getInstance( )
					.getPropertyTypes( ).iterator( );
			while ( iter.hasNext( ) )
			{
				PropertyType propType = (PropertyType) iter.next( );
				int type = propType.getTypeCode( );
				switch ( type )
				{
					case IPropertyType.STRING_TYPE :
					case IPropertyType.BOOLEAN_TYPE :
					case IPropertyType.DATE_TIME_TYPE :
					case IPropertyType.FLOAT_TYPE :
					case IPropertyType.INTEGER_TYPE :
					case IPropertyType.EXPRESSION_TYPE :
						allowedSubPropertyTypes.add( propType );
						break;
					default :
						break;
				}
			}

			return allowedSubPropertyTypes;
		}
	}

	/**
	 * Generates a property with the given method info.
	 * 
	 * @param elementDefn
	 *            the element definition to handler
	 * @param methodInfo
	 *            the method info to add
	 * @return the generated property definition
	 * @throws ExtensionException
	 */

	private ExtensionPropertyDefn addDefnTo( ExtensionElementDefn elementDefn,
			MethodInfo methodInfo ) throws ExtensionException
	{
		ExtensionPropertyDefn extPropDefn = new ExtensionPropertyDefn(
				( (PeerExtensionElementDefn) elementDefn )
						.getReportItemFactory( ).getMessages( ) );

		PropertyType typeDefn = MetaDataDictionary.getInstance( )
				.getPropertyType( IPropertyType.SCRIPT_TYPE );

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

}
