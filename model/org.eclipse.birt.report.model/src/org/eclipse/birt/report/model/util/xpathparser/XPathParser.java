/**
 * 
 */

package org.eclipse.birt.report.model.util.xpathparser;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.util.XPathUtil;
import org.eclipse.birt.report.model.core.CachedMemberRef;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Structure;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.util.xpathparser.XDepthParser.DepthInfo;

/**
 * To parse xpath string.
 * 
 */

public class XPathParser
{

	// ******** the type of current element *****

	private static final int INVALID = XDepthParser.INVALID;
	private static final int ELEMENT = XDepthParser.ELEMENT;
	private static final int SLOT = XDepthParser.SLOT;
	private static final int PROPERTY = XDepthParser.PROPERTY;

	// ******** keep the property *****

	private CachedMemberRef ref = null;

	/**
	 * The current parsed element.
	 */

	private DesignElementHandle currentElement = null;

	/**
	 * The root node.
	 */

	private ModuleHandle module = null;

	/**
	 * The current parsed tag type.
	 */

	private int valueType = INVALID;

	// ******** the slot id if the current element should be slot *****

	private int slotID = DesignElement.NO_SLOT;

	private String propertyName = null;

	/**
	 * Default constrcutor.
	 * 
	 * @param module
	 *            the report/library
	 */

	public XPathParser( ModuleHandle module )
	{
		this.module = module;
	}

	/**
	 * Returns the instance that matches input xpath in string.
	 * 
	 * @param input
	 *            the xpath in string
	 * 
	 * @return the corresponding instance. Can be
	 *         <code>DesignElementHandle</code>/<code>SlotHandle</code>/<code>StructureHandle</code>.
	 */

	public Object getObject( String input )
	{

		XDepthParser parser_1 = new XDepthParser( input );

		try
		{
			parser_1.parse( );
		}
		catch ( ParseException e )
		{
			return null;
		}

		List depthInfo = parser_1.getDepthInfo( );

		// ******** the index in the XPath tags *****

		int index = -1;

		currentElement = module;

		int lastValueType = INVALID;

		for ( int i = 0; i < depthInfo.size( ); i++ )
		{
			DepthInfo oneDepth = (DepthInfo) depthInfo.get( i );

			lastValueType = valueType;
			valueType = getNextValueType( valueType );

			String tagName = oneDepth.getTagName( );
			index = oneDepth.getIndex( );

			// ******** the property name and value in the XPath tags *****

			String attrName = oneDepth.getPropertyName( );
			String attrValue = oneDepth.getPropertyValue( );;

			ElementDefn elementDefn = (ElementDefn) currentElement.getDefn( );

			if ( i == 0 && tagName.equalsIgnoreCase( elementDefn.getXmlName( ) )
					&& module == currentElement )
			{
				valueType = ELEMENT;
				continue;
			}

			// try slot first.

			if ( ( valueType & SLOT ) != 0 )
			{
				slotID = getSlotId( elementDefn, tagName, true );
				if ( slotID != DesignElement.NO_SLOT )
				{
					valueType = SLOT;
					continue;
				}
			}

			if ( ( valueType & ELEMENT ) != 0 )
			{
				if ( lastValueType != SLOT )
				{
					slotID = getSlotId( elementDefn, tagName );
					if ( slotID != DesignElement.NO_SLOT )
						valueType = ELEMENT;
				}

				// try contents then.

				if ( valueType == ELEMENT )
				{
					if ( attrName == null
							|| DesignSchemaConstants.ID_ATTRIB
									.equalsIgnoreCase( attrName )
							|| XPathUtil.SLOT_NAME_PROPERTY
									.equalsIgnoreCase( attrName ) )
					{
						currentElement = findElement( tagName, attrName,
								attrValue );

						if ( XPathUtil.SLOT_NAME_PROPERTY
								.equalsIgnoreCase( attrName ) )
						{
							slotID = getSlotId( currentElement.getDefn( ),
									attrValue, false );
							if ( slotID != DesignElement.NO_SLOT )
								valueType = SLOT;
						}
						continue;
					}
				}
			}

			if ( ( ( valueType & ELEMENT ) != 0 || ( valueType & PROPERTY ) != 0 )
					&& isPropertyTag( tagName ) )
			{
				ref = parsePropertyValue( tagName, attrValue, index );
				valueType = PROPERTY;
			}

			if ( valueType == INVALID )
				break;

		}

		switch ( valueType )
		{
			case SLOT :
				return currentElement.getSlot( slotID );
			case ELEMENT :
				return currentElement;
			case PROPERTY :
				return getRespectivePropertyHandle( index );
			default :
				return null;
		}
	}

	/**
	 * Returns the index of the slot that can contain elements of which the
	 * report tag is <code>tagName</code>.
	 * 
	 * @param defn
	 *            the element definition
	 * @param tagName
	 *            the tag in the report design/library
	 * @return the index of matched slot
	 */

	private static int getSlotId( IElementDefn defn, String tagName )
	{
		for ( int j = 0; j < defn.getSlotCount( ); j++ )
		{
			SlotDefn slot = (SlotDefn) defn.getSlot( j );
			if ( isContentType( slot, tagName ) )
				return j;
		}
		return DesignElement.NO_SLOT;
	}

	/**
	 * Returns the index of the slot that matches the slot name/xmlName.
	 * 
	 * @param defn
	 *            the element definition
	 * @param slotName
	 *            the slot name/xmlName
	 * @return the index of matched slot
	 */

	private int getSlotId( IElementDefn defn, String slotName, boolean isXmlName )
	{
		for ( int j = 0; j < defn.getSlotCount( ); j++ )
		{
			SlotDefn slot = (SlotDefn) defn.getSlot( j );
			String name = null;
			if ( isXmlName )
				name = slot.getXmlName( );
			else
				name = slot.getName( );
			if ( slotName.equalsIgnoreCase( name ) )
			{
				return j;
			}
		}

		return DesignElement.NO_SLOT;
	}

	private static int getNextValueType( int tmpValueType )
	{

		int retValueType = INVALID;

		switch ( tmpValueType )
		{
			case SLOT :
				retValueType = ELEMENT;
				break;
			case ELEMENT :
				retValueType = SLOT | PROPERTY | ELEMENT;
				break;
			case PROPERTY :
				retValueType = PROPERTY;
				break;
			default :
				retValueType = ELEMENT | SLOT | PROPERTY;
				break;
		}
		return retValueType;

	}

	private Object getRespectivePropertyHandle( int index )
	{
		if ( ref == null )
		{
			if ( propertyName == null )
				return null;

			return new PropertyHandle( currentElement, propertyName );
		}

		switch ( ref.refType )
		{
			case CachedMemberRef.PROPERTY :
				return new PropertyHandle( currentElement, ref.getPropDefn( ) );
			case CachedMemberRef.PROPERTY_LISTn :
				PropertyHandle propHandle = new PropertyHandle( currentElement,
						ref.getPropDefn( ) );
				if ( index < 0 || index >= propHandle.getListValue( ).size( ) )
					return null;
				return propHandle.getAt( index < 0 ? 0 : index );
			case CachedMemberRef.PROPERTY_MEMBER_LISTn :
				propHandle = new PropertyHandle( currentElement, ref
						.getPropDefn( ) );
				StructureHandle structHandle = ( (Structure) propHandle
						.getValue( ) ).getHandle( propHandle );
				MemberHandle memberHandle = structHandle.getMember( ref
						.getMemberDefn( ).getName( ) );
				if ( index < 0 || index >= memberHandle.getListValue( ).size( ) )
					return null;
				return memberHandle.getAt( index < 0 ? 0 : index );
			default :
				return null;
		}
	}

	private CachedMemberRef parsePropertyValue( String tagName,
			String propName, int index )
	{
		valueType = PROPERTY;

		if ( propertyName == null )
		{
			ElementDefn elementDefn = (ElementDefn) currentElement.getDefn( );
			IPropertyDefn propDefn = elementDefn.getProperty( propName );
			if ( propDefn == null )
				return null;

			propertyName = propName;

			if ( propDefn.getTypeCode( ) == IPropertyType.STRUCT_TYPE )
				ref = new CachedMemberRef( (ElementPropertyDefn) propDefn );

			return ref;
		}

		if ( ref == null && propertyName != null
				&& DesignSchemaConstants.KEY_ATTRIB.equalsIgnoreCase( propName ) )
		{
			String newPropName = propertyName + XPathUtil.RESOURCE_KEY_SUFFIX;
			ElementDefn elementDefn = (ElementDefn) currentElement.getDefn( );
			IPropertyDefn propDefn = elementDefn.getProperty( newPropName );
			if ( propDefn != null )
				propertyName = newPropName;
			else
				propertyName = null;

			return null;
		}

		assert ref != null;

		if ( ref.refType == CachedMemberRef.PROPERTY
				&& DesignSchemaConstants.STRUCTURE_TAG
						.equalsIgnoreCase( tagName ) )
		{
			ElementDefn elementDefn = (ElementDefn) currentElement.getDefn( );
			IPropertyDefn propDefn = elementDefn.getProperty( ref.getPropDefn( )
					.getName( ) );

			ref = new CachedMemberRef( (ElementPropertyDefn) propDefn,
					index < 0 ? 0 : index );
			return ref;
		}

		if ( ref.refType == CachedMemberRef.PROPERTY
				&& DesignSchemaConstants.LIST_PROPERTY_TAG
						.equalsIgnoreCase( tagName ) )
		{
			ref = new CachedMemberRef( ref, propName );
			return ref;
		}

		if ( ref.refType == CachedMemberRef.PROPERTY_MEMBER
				&& DesignSchemaConstants.STRUCTURE_TAG
						.equalsIgnoreCase( tagName ) )
		{
			ref = new CachedMemberRef( ref, index < 0 ? 0 : index );
			return ref;
		}

		return null;
	}

	/**
	 * Checks whether the element can be contained in the given slot.
	 * 
	 * @param slot
	 *            the slot definition
	 * @param elementDefnName
	 *            the name of the element definition
	 * @return <code>true</code> if the element can be contained in the given
	 *         slot. Otherwise <code>false</code>.
	 */

	private static boolean isContentType( SlotDefn slot, String elementDefnName )
	{

		List tmpElementDefns = slot.getContentElements( );
		for ( int i = 0; i < tmpElementDefns.size( ); i++ )
		{
			ElementDefn tmpContentDefn = (ElementDefn) tmpElementDefns.get( i );
			if ( elementDefnName
					.equalsIgnoreCase( tmpContentDefn.getXmlName( ) ) )
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the element that is contained by the current element.
	 * 
	 * @param contentDefnName
	 *            the element xml name
	 * @param attrName
	 *            the element property name
	 * @param attrValue
	 *            the element property value
	 * @return the matched element
	 */

	private DesignElementHandle findElement( String contentDefnName,
			String attrName, String attrValue )
	{
		long id = DesignElement.NO_ID;
		if ( DesignSchemaConstants.ID_ATTRIB.equalsIgnoreCase( attrName ) )
		{
			try
			{
				id = Long.parseLong( attrValue );
			}
			catch ( NumberFormatException e )
			{
				return null;
			}

		}

		SlotHandle slot = currentElement.getSlot( slotID );
		for ( int i = 0; i < slot.getCount( ); i++ )
		{
			DesignElementHandle tmpElement = slot.get( i );
			ElementDefn tmpDefn = (ElementDefn) tmpElement.getDefn( );

			if ( ( DesignElement.NO_ID == id || tmpElement.getID( ) == id )
					&& tmpDefn.getXmlName( ).equalsIgnoreCase( contentDefnName ) )
				return tmpElement;
		}

		return null;
	}

	/**
	 * Checks whehter the input tag is the property tag.
	 * 
	 * @param tagName
	 *            the tag
	 * @return <code>true</code> if it is property tag. Otherwise
	 *         <code>false</code>.
	 */

	private static boolean isPropertyTag( String tagName )
	{
		if ( tagName == null )
			return true;

		if ( DesignSchemaConstants.PROPERTY_TAG.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.LIST_PROPERTY_TAG
				.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.METHOD_TAG.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.EXPRESSION_TAG
				.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG
				.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.TEXT_PROPERTY_TAG
				.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.HTML_PROPERTY_TAG
				.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.XML_PROPERTY_TAG
				.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG
				.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.EX_PROPERTY_TAG
				.equalsIgnoreCase( tagName ) )
			return true;
		else if ( DesignSchemaConstants.STRUCTURE_TAG
				.equalsIgnoreCase( tagName ) )
			return true;

		return false;
	}
}