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

package org.eclipse.birt.report.model.api.util;

import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.MemberRef;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.xpathparser.XPathParser;

/**
 * The XPath string helps user locate this element in desing file. It follows a
 * subset of XPath syntax. Each node name indicates the name of the element
 * definition and the 1-based element position in the slot. The position
 * information is only available when the element is in the multicardinality
 * slot.
 * 
 * <p>
 * For example, <table border="1" width="80%">
 * <tr>
 * <td width="25%">type</td>
 * <td width="50%">examples</td>
 * </tr>
 * <tr>
 * <td>element</td>
 * <td> /report/body/table[3] <br>
 * /library/components/grid/row[1]/cell[2]/label </td>
 * <tr>
 * <td>slot</td>
 * <td> /report/body <br>
 * /report/body/* <br>
 * /report/body/table/detail/row[slotName="cells"] <br>
 * </tr>
 * <tr>
 * <td> property </td>
 * <td> /report/parameters/scalar-parameter[@id="2"]/@name<br>
 * /report/list-property[@name="images"] <br>
 * /report/body/text[@id="19"]/text-property[@name="content"]<br>
 * /report/body/text[@id="19"]/text-property[@name="content"]/@key</td>
 * <tr>
 * <tr>
 * <td> structure (EmbeddedImageHandle/IncludedLibraryHandle/ResultSetColumnHandle) </td>
 * <td> /report/list-property[@name="images"]/structure[2]<br>
 * /report/list-property[@name="images"]/structure </td>
 * <tr> </table>
 * 
 * <p>
 * It is strongly recommended not to call
 * {@link #getInstance(ModuleHandle, String)} with manually created XPath
 * string. Much better to use XPath string returned by {@link #getXPath(Object)}.
 */

public class XPathUtil
{

	/**
	 * The attribute to indicate a slot name.
	 */

	public final static String SLOT_NAME_PROPERTY = "slotName"; //$NON-NLS-1$

	/**
	 * The suffix for the resource key.
	 */

	public final static String RESOURCE_KEY_SUFFIX = "ID"; //$NON-NLS-1$

	private final static String EMPTY_STRING = ""; //$NON-NLS-1$
	private final static String SEPARATOR = "/"; //$NON-NLS-1$

	/**
	 * Returns the XPath of the given instance. For the structure, only
	 * EmbeddedImageHandle is supported.
	 * 
	 * @param instance
	 *            can be <code>DesignElementHandle</code>,
	 *            <code>SlotHandle</code>, <code>StructureHandle</code>.
	 * @return the XPath in string
	 */

	public static String getXPath( Object instance )
	{
		if ( instance == null )
			return EMPTY_STRING;

		if ( instance instanceof DesignElementHandle )
			return serializeToXPath( (DesignElementHandle) instance );

		if ( instance instanceof SlotHandle )
		{
			SlotHandle slot = (SlotHandle) instance;
			return serializeToXPath( slot );
		}

		if ( instance instanceof StructureHandle )
		{
			return serializeToXPath( (StructureHandle) instance );
		}

		if ( instance instanceof PropertyHandle )
		{
			return serializeToXPath( (PropertyHandle) instance );
		}
		return null;
	}

	/**
	 * Returns the instance by the given XPath of
	 * <code>DesignElementHandle</code>, <code>SlotHandle</code>,
	 * <code>StructureHandle</code>.
	 * 
	 * @param module
	 *            the report/library
	 * @param xpath
	 *            the XPath in string
	 * @return <code>DesignElementHandle</code>/<code>SlotHandle</code>/
	 *         <code>StructureHandle</code>.
	 */

	public static Object getInstance( ModuleHandle module, String xpath )
	{
		return new XPathParser( module ).getObject( xpath );
	}

	/**
	 * Returns the XPath of the given slot.
	 * 
	 * @param slot
	 *            the slot instance
	 * @return the XPath in string
	 */

	private static String serializeToXPath( SlotHandle slot )
	{
		StringBuffer sb = new StringBuffer( );

		DesignElementHandle tmpElement = slot.getElementHandle( );
		sb.append( serializeToXPath( tmpElement ) );

		String slotInfo = slot.getDefn( ).getXmlName( );

		if ( StringUtil.isBlank( slotInfo ) )
			slotInfo = formatXPathProperty( SLOT_NAME_PROPERTY, slot.getDefn( )
					.getName( ) );
		else
			slotInfo = SEPARATOR + slotInfo;

		sb.append( slotInfo );

		return sb.toString( );
	}

	/**
	 * Returns the XPath of the given property.
	 * 
	 * @param slot
	 *            the property instance
	 * @return the XPath in string
	 */

	private static String serializeToXPath( PropertyHandle prop )
	{
		StringBuffer sb = new StringBuffer( );

		DesignElementHandle tmpElement = prop.getElementHandle( );
		sb.append( serializeToXPath( tmpElement ) );

		sb.append( SEPARATOR );
		PropertyDefn tmpPropDefn = (PropertyDefn) prop.getDefn( );
		if ( tmpPropDefn.isList( ) )
		{
			sb.append( DesignSchemaConstants.LIST_PROPERTY_TAG );
			sb.append( formatXPathProperty( DesignSchemaConstants.NAME_ATTRIB,
					tmpPropDefn.getName( ) ) );

			return sb.toString( );
		}

		boolean isResourceKeyDefn = false;
		IPropertyDefn resourcePropDefn = getResourceKeyDefn( (ElementPropertyDefn) tmpPropDefn );
		if ( resourcePropDefn != null )
			isResourceKeyDefn = true;

		if ( !isResourceKeyDefn && !isEnclosedAttr( tmpPropDefn.getName( ) ) )
		{
			sb
					.append( getTagByPropertyType( (ElementPropertyDefn) tmpPropDefn ) );
			sb.append( formatXPathProperty( DesignSchemaConstants.NAME_ATTRIB,
					tmpPropDefn.getName( ) ) );

			return sb.toString( );
		}

		if ( !isResourceKeyDefn )
		{
			sb.append( "@" + tmpPropDefn.getName( ) ); //$NON-NLS-1$
			return sb.toString( );
		}

		assert resourcePropDefn != null;

		sb
				.append( getTagByPropertyType( (ElementPropertyDefn) resourcePropDefn ) );
		sb.append( formatXPathProperty( DesignSchemaConstants.NAME_ATTRIB,
				resourcePropDefn.getName( ) ) );

		if ( resourcePropDefn != tmpPropDefn )
			sb.append( SEPARATOR + "@key" ); //$NON-NLS-1$

		return sb.toString( );
	}

	/**
	 * Returns the XPath of the given structure. Currently, only
	 * <code>EmbeddedImageHandle</code> is supported.
	 * 
	 * @param slot
	 *            the structure instance
	 * @return the XPath in string
	 */

	private static String serializeToXPath( StructureHandle prop )
	{
		StringBuffer sb = new StringBuffer( );

		DesignElementHandle tmpElement = prop.getElementHandle( );
		sb.append( serializeToXPath( tmpElement ) );

		MemberRef memberRef = prop.getReference( );
		PropertyDefn tmpPropDefn = memberRef.getPropDefn( );

		// first levle is the structure.

		if ( !tmpPropDefn.isList( ) && tmpPropDefn.getStructDefn( ) != null )
		{
			sb.append( SEPARATOR
					+ DesignSchemaConstants.STRUCTURE_TAG
					+ formatXPathProperty( DesignSchemaConstants.NAME_ATTRIB,
							tmpPropDefn.getName( ) ) );

			tmpPropDefn = memberRef.getMemberDefn( );
		}

		if ( tmpPropDefn.isList( ) )
		{
			sb.append( SEPARATOR
					+ DesignSchemaConstants.LIST_PROPERTY_TAG
					+ formatXPathProperty( DesignSchemaConstants.NAME_ATTRIB,
							tmpPropDefn.getName( ) ) );
		}

		sb.append( SEPARATOR + DesignSchemaConstants.STRUCTURE_TAG );

		if ( !tmpPropDefn.isList( ) )
		{
			sb.append( formatXPathProperty( DesignSchemaConstants.NAME_ATTRIB,
					prop.getDefn( ).getName( ) ) );
		}
		else
		{
			int index = memberRef.getIndex( ) + 1;
			sb.append( "[" + index + "]" ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		return sb.toString( );
	}

	/**
	 * Returns return the path corresponding to the current position of the
	 * element in the tree.
	 * 
	 * This path string helps user locate this element in user interface. It
	 * follows XPath syntax. Each node name indicates the name of the element
	 * definition and the 1-based element position in the slot. The position
	 * information is only available when the element is in the multicardinality
	 * slot.
	 * 
	 * <p>
	 * For example,
	 * <ul>
	 * <li>/report/Body[1]/Label[3] - The third label element in body slot
	 * <li>/report/Styles[1]/Style[1] - The first style in the styles slot
	 * <li>/report/page-setup[1]/Graphic Master Page - The master page in the
	 * page setup slot.
	 * </ul>
	 * <p>
	 * Note: the localized name is used for element type and slot name.
	 * 
	 * @return the path of this element
	 */

	private static String serializeToXPath( DesignElementHandle element )
	{

		DesignElementHandle tmpElement = element;
		StringBuffer sb = new StringBuffer( );

		do
		{
			ElementDefn elementDefn = (ElementDefn) tmpElement.getDefn( );
			DesignElementHandle container = tmpElement.getContainer( );

			String slotInfo = null;
			String idInfo = EMPTY_STRING;

			if ( container != null )
			{
				SlotHandle slot = tmpElement.getContainerSlotHandle( );
				String slotTagName = slot.getDefn( ).getXmlName( );

				if ( !StringUtil.isBlank( slotTagName ) )
					slotInfo = SEPARATOR + slotTagName;

				if ( slot.getCount( ) > 1 )
				{
					idInfo = formatXPathProperty( "id", Long //$NON-NLS-1$
							.toString( tmpElement.getID( ) ) );
				}
			}

			String elementPath = EMPTY_STRING;

			if ( !StringUtil.isBlank( slotInfo ) )
				elementPath = slotInfo;

			elementPath = elementPath + SEPARATOR + elementDefn.getXmlName( );
			elementPath = elementPath + idInfo;

			sb.insert( 0, elementPath );
			tmpElement = container;

		} while ( tmpElement != null );

		return sb.toString( );
	}

	/**
	 * Returns the formatted property that is applicable in XPath node.
	 * 
	 * @param propName
	 *            the property name
	 * @param propValue
	 *            the value
	 * @return the formatted property
	 */

	private static String formatXPathProperty( String propName, String propValue )
	{
		return "[@" + propName + "=\"" + propValue + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Checks whether the property is an enclosed XML element attribute in the
	 * design file.
	 * 
	 * @param propName
	 *            the property name
	 * @return <code>true</code> if it is. Otherwise <code>false</code>.
	 */

	private static boolean isEnclosedAttr( String propName )
	{
		if ( DesignElement.NAME_PROP.equalsIgnoreCase( propName )
				|| DesignSchemaConstants.EXTENDS_ATTRIB
						.equalsIgnoreCase( propName )
				|| IExtendedItemModel.EXTENSION_NAME_PROP
						.equalsIgnoreCase( propName )
				|| IOdaExtendableElementModel.EXTENSION_ID_PROP
						.equalsIgnoreCase( propName ) )
			return true;

		return false;
	}

	private static String getTagByPropertyType( ElementPropertyDefn propDefn )
	{
		String tagName = ModelUtil.getTagByPropertyType( propDefn );
		if ( !DesignSchemaConstants.PROPERTY_TAG.equalsIgnoreCase( tagName ) )
			return tagName;

		IPropertyDefn tmpPropDefn = getResourceKeyDefn( propDefn );
		if ( tmpPropDefn == null )
			return tagName;

		if ( tmpPropDefn.getTypeCode( ) == IPropertyType.HTML_TYPE )
			return DesignSchemaConstants.HTML_PROPERTY_TAG;

		return DesignSchemaConstants.TEXT_PROPERTY_TAG;
	}

	/**
	 * Returns the property definition that has a resource key bound with. For
	 * example, if <code>propDefn=TextItem.content</code>, the return value
	 * is property definition to the <code>TextItem.content</code>. If
	 * <code>propDefn=TextItem.contentID</code>, the return value is also the
	 * property definition to the <code>TextItem.content</code>.
	 * 
	 * @param propDefn
	 *            the input property definition
	 * @return the property definition that has a resource key bound with
	 */

	private static IPropertyDefn getResourceKeyDefn(
			ElementPropertyDefn propDefn )
	{
		String propName = propDefn.getName( );
		String otherPropName = EMPTY_STRING;

		ElementPropertyDefn tmpPropDefn = propDefn;
		boolean isKeyDefn = false;

		if ( propName.endsWith( RESOURCE_KEY_SUFFIX ) )
		{
			int index = propName.lastIndexOf( RESOURCE_KEY_SUFFIX );
			otherPropName = propName.substring( 0, index );

			if ( propDefn.getTypeCode( ) != IPropertyType.RESOURCE_KEY_TYPE )
				return null;

			isKeyDefn = true;
		}
		else
		{
			otherPropName = propName + RESOURCE_KEY_SUFFIX;
		}

		isKeyDefn = !isKeyDefn;
		tmpPropDefn = (ElementPropertyDefn) ( (ElementDefn) propDefn
				.definedBy( ) ).getProperty( otherPropName );
		if ( tmpPropDefn == null )
			return null;

		if ( isKeyDefn
				&& tmpPropDefn.getTypeCode( ) != IPropertyType.RESOURCE_KEY_TYPE )
			return null;

		return ( isKeyDefn ? propDefn : tmpPropDefn );
	}
}
