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

package org.eclipse.birt.report.model.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.OdaDesignerState;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.UserChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.AutoText;
import org.eclipse.birt.report.model.elements.CascadingParameterGroup;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.JointDataSet;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.LineItem;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.OdaDataSet;
import org.eclipse.birt.report.model.elements.OdaDataSource;
import org.eclipse.birt.report.model.elements.Parameter;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.RectangleItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.elements.ScriptDataSource;
import org.eclipse.birt.report.model.elements.SimpleDataSet;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TemplateDataSet;
import org.eclipse.birt.report.model.elements.TemplateElement;
import org.eclipse.birt.report.model.elements.TemplateParameterDefinition;
import org.eclipse.birt.report.model.elements.TemplateReportItem;
import org.eclipse.birt.report.model.elements.TextDataItem;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.util.ContentIterator;

/**
 * Represents the module writer which writes an XML file following the BIRT
 * design schema. Uses a visitor pattern to traverse each element. BIRT elements
 * support inheritance in several forms. Because of this, the module writer
 * writes only those properties "local" to the element being written -- it does
 * not write inherited properties.
 * <p>
 * Because the XML schema was designed for to be understood by humans, the
 * schema is not a literal representation of the model. Instead, properties are
 * named and grouped in a way that is easiest to explain and understand. This
 * means that the writer has to do a bit more work to write the design, the the
 * extra work here is well worth the savings to the many customers who will read
 * the design format.
 * 
 */

public abstract class ModuleWriter extends ElementVisitor
{

	/**
	 * The low-level writer that emits XML syntax.
	 */
	/**
	 * The constant for default model name space.
	 */

	final protected static String DEFAULT_NAME_SPACE = "http://www.eclipse.org/birt/2005/design"; //$NON-NLS-1$

	/**
	 * The XML writer.
	 */

	protected IndentableXMLWriter writer = null;

	/**
	 * The base 64 codec for embedded images.
	 */

	protected static Base64 base = new Base64( );

	/**
	 * The compatibility to create bound columns.
	 */

	protected BoundColumnsWriterMgr boundColumnsMgr = null;

	/**
	 * Returns the module to write.
	 * 
	 * @return the module to write
	 */

	abstract protected Module getModule( );

	/**
	 * Writes the report design to a file.
	 * 
	 * @param outputFile
	 *            output file into which to write the design
	 * 
	 * @throws IOException
	 *             if a write error occurs
	 */

	public void write( File outputFile ) throws IOException
	{
		writer = new IndentableXMLWriter( outputFile, getModule( )
				.getUTFSignature( ) );
		writeFile( );
		writer.close( );
	}

	/**
	 * Writes the report design to the output stream.
	 * 
	 * @param os
	 *            the output stream to which the design is written.
	 * @throws IOException
	 *             if a write error occurs.
	 */

	public void write( OutputStream os ) throws IOException
	{
		writer = new IndentableXMLWriter( os, getModule( ).getUTFSignature( ) );
		writeFile( );
	}

	/**
	 * Implementation method to write the file header and contents.
	 */

	private void writeFile( )
	{
		boundColumnsMgr = new BoundColumnsWriterMgr( getModule( )
				.getVersionManager( ).getVersion( ) );

		writer.literal( "<!-- Written by Eclipse BIRT 2.0 -->\r\n" ); //$NON-NLS-1$
		getModule( ).apply( this );

		getModule( ).getVersionManager( ).setVersion(
				DesignSchemaConstants.REPORT_VERSION );
	}

	/**
	 * Write an XML attribute from an element property.
	 * 
	 * @param obj
	 *            the element that has the property
	 * @param attr
	 *            the XML attribute name
	 * @param propName
	 *            the name of the property to write
	 */

	protected void attribute( DesignElement obj, String attr, String propName )
	{
		ElementPropertyDefn prop = obj.getPropertyDefn( propName );
		assert prop != null;

		Object value = obj.getLocalProperty( getModule( ), prop );
		if ( value == null )
			return;

		String xml = prop.getXmlValue( getModule( ), value );
		if ( xml == null )
			return;

		writer.attribute( attr, xml );
	}

	/**
	 * Returns the tag according to the simple property type. If the property
	 * type is structure or structure list, this method can not be used.
	 * 
	 * <ul>
	 * <li>EXPRESSION_TAG, if the property is expression;
	 * <li>XML_PROPERTY_TAG, if the property is xml;
	 * <li>METHOD_TAG, if the property is method;
	 * <li>PROPERTY_TAG, if the property is string, number, and so on.
	 * </ul>
	 * 
	 * @param prop
	 *            the property definition
	 * @return the tag of this property
	 */

	protected String getTagByPropertyType( PropertyDefn prop )
	{
		assert prop != null;
		assert prop.getTypeCode( ) != PropertyType.STRUCT_TYPE;

		switch ( prop.getTypeCode( ) )
		{
			case PropertyType.EXPRESSION_TYPE :
				return DesignSchemaConstants.EXPRESSION_TAG;

			case PropertyType.XML_TYPE :
				return DesignSchemaConstants.XML_PROPERTY_TAG;

			case PropertyType.SCRIPT_TYPE :
				return DesignSchemaConstants.METHOD_TAG;

			default :
				if ( prop.isEncryptable( ) )
					return DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG;

				return DesignSchemaConstants.PROPERTY_TAG;
		}
	}

	/**
	 * Writes one property entry of an element.
	 * 
	 * @param obj
	 *            the design element
	 * @param propName
	 *            the property name
	 */

	protected void property( DesignElement obj, String propName )
	{
		writeProperty( obj, null, propName, false );
	}

	/**
	 * Writes one property entry of an structure.
	 * 
	 * @param structure
	 *            the structure
	 * @param memberName
	 *            the member name
	 */

	protected void property( IStructure structure, String memberName )
	{
		writeProperty( structure, null, memberName, false, false );
	}

	/**
	 * Writes one property entry of an element as CDATA.
	 * 
	 * @param obj
	 *            the design element
	 * @param propName
	 *            the property name
	 */

	protected void propertyCDATA( DesignElement obj, String propName )
	{
		writeProperty( obj, null, propName, true );
	}

	/**
	 * Writes one property entry of an structure as CDATA.
	 * 
	 * @param structure
	 *            the structure
	 * @param memberName
	 *            the member name
	 */

	protected void propertyCDATA( IStructure structure, String memberName )
	{
		writeProperty( structure, null, memberName, true, false );
	}

	/**
	 * Writes one property entry of an structure without member name.
	 * 
	 * @param structure
	 *            the structure
	 * @param memberName
	 *            the member name
	 */

	protected void propertyWithoutName( IStructure structure, String memberName )
	{
		writeProperty( structure, null, memberName, false, true );
	}

	/**
	 * Writes the resource key.
	 * 
	 * @param obj
	 *            the design element
	 * @param resourceKey
	 *            the resource key value
	 * @param resourceValue
	 *            the user-visible resource value
	 */

	protected void resourceKey( DesignElement obj, String resourceKey,
			String resourceValue )
	{
		resourceKey( obj, resourceKey, resourceValue, false );
	}

	/**
	 * Writes the resource key.
	 * 
	 * @param obj
	 *            the design element
	 * @param resourceKey
	 *            the property name of resource key
	 * @param resourceName
	 *            the property name of user-visible resource
	 * @param cdata
	 *            whether the values is written as CDATA
	 */

	protected void resourceKey( DesignElement obj, String resourceKey,
			String resourceName, boolean cdata )
	{
		PropertyDefn nameProp = (ElementPropertyDefn) obj.getDefn( )
				.getProperty( resourceName );
		assert nameProp != null;

		Object value = obj.getLocalProperty( getModule( ), nameProp.getName( ) );
		String xml = nameProp.getXmlValue( getModule( ), value );

		PropertyDefn keyProp = (ElementPropertyDefn) obj.getDefn( )
				.getProperty( resourceKey );
		assert keyProp != null;

		value = obj.getLocalProperty( getModule( ), keyProp.getName( ) );
		String xmlKey = keyProp.getXmlValue( getModule( ), value );
		if ( xmlKey == null && xml == null )
			return;

		if ( nameProp.getTypeCode( ) == PropertyType.HTML_TYPE )
		{
			writeResouceKey( DesignSchemaConstants.HTML_PROPERTY_TAG,
					resourceName, xmlKey, xml, cdata );
		}
		else
		{
			writeResouceKey( DesignSchemaConstants.TEXT_PROPERTY_TAG,
					resourceName, xmlKey, xml, cdata );
		}
	}

	/**
	 * Writes the resource key of structure.
	 * 
	 * @param struct
	 *            the structure
	 * @param resourceKey
	 *            the property name of resource key
	 * @param resourceName
	 *            the property name of user-visible resource
	 */

	protected void resourceKey( IStructure struct, String resourceKey,
			String resourceName )
	{
		StructureDefn structDefn = (StructureDefn) struct.getDefn( );
		assert structDefn != null;

		StructPropertyDefn nameProp = (StructPropertyDefn) structDefn
				.getMember( resourceName );
		assert nameProp != null;

		Object value = struct.getLocalProperty( getModule( ), nameProp );
		String xml = nameProp.getXmlValue( getModule( ), value );

		StructPropertyDefn keyProp = (StructPropertyDefn) structDefn
				.getMember( resourceKey );
		assert keyProp != null;

		value = struct.getLocalProperty( getModule( ), keyProp );
		String xmlKey = keyProp.getXmlValue( getModule( ), value );
		if ( xmlKey == null && xml == null )
			return;

		if ( nameProp.getTypeCode( ) == PropertyType.HTML_TYPE )
		{
			writeResouceKey( DesignSchemaConstants.HTML_PROPERTY_TAG,
					resourceName, xmlKey, xml, false );
		}
		else
		{
			writeResouceKey( DesignSchemaConstants.TEXT_PROPERTY_TAG,
					resourceName, xmlKey, xml, false );
		}
	}

	/**
	 * Writes the resource key as CDATA.
	 * 
	 * @param obj
	 *            the design element
	 * @param resourceKey
	 *            the resource key value
	 * @param resourceValue
	 *            the user-visible resource value
	 */

	protected void resourceKeyCDATA( DesignElement obj, String resourceKey,
			String resourceValue )
	{
		resourceKey( obj, resourceKey, resourceValue, true );
	}

	/**
	 * Writes the entry.
	 * 
	 * @param tag
	 *            the tag to write
	 * @param name
	 *            the property name or member name
	 * @param value
	 *            the xml value
	 * @param cdata
	 *            whether the values should be written as CDATA.
	 */

	protected void writeEntry( String tag, String name, String value,
			boolean cdata )
	{
		writer.startElement( tag );

		if ( name != null )
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB, name );

		if ( cdata )
			writer.textCDATA( value );
		else
			writer.text( value );

		writer.endElement( );

	}

	/**
	 * Writes a list of extended property structure.
	 * 
	 * @param properties
	 *            the list of Extended property structure to write.
	 * @param propName
	 *            the tag name for Extended property list.
	 */

	protected void writeExtendedProperties( List properties, String propName )
	{
		if ( properties != null && properties.size( ) != 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB, propName );

			Iterator iter = properties.iterator( );
			while ( iter.hasNext( ) )
			{
				ExtendedProperty property = (ExtendedProperty) iter.next( );

				writer.startElement( DesignSchemaConstants.EX_PROPERTY_TAG );

				if ( property.getName( ) != null )
				{
					writer.startElement( DesignSchemaConstants.NAME_ATTRIB );
					writer.text( property.getName( ) );
					writer.endElement( );
				}

				if ( property.getValue( ) != null )
				{
					writer.startElement( DesignSchemaConstants.VALUE_TAG );
					writer.text( property.getValue( ) );
					writer.endElement( );
				}

				writer.endElement( );
			}
			writer.endElement( );
		}
	}

	/**
	 * Writes out a long text the length of which exceeds 80.
	 * 
	 * @param tag
	 *            the element tag
	 * @param name
	 *            the name attribute
	 * @param value
	 *            the text value
	 */

	protected void writeLongIndentText( String tag, String name, String value )
	{
		writer.startElement( tag );
		if ( name != null )
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB, name );

		writer.indentLongText( value );
		writer.endElement( );
	}

	/**
	 * Writes the property of the given design element.
	 * 
	 * @param obj
	 *            the design element to write
	 * @param tag
	 *            the tag of the write
	 * @param propName
	 *            the property name
	 * @param cdata
	 *            whether the value should be written as CDATA.
	 */

	protected void writeProperty( DesignElement obj, String tag,
			String propName, boolean cdata )
	{
		PropertyDefn propDefn = obj.getPropertyDefn( propName );

		// The style property is not available for all elements.

		if ( propDefn == null )
			return;

		Object value = obj.getLocalProperty( getModule( ), propName );
		if ( value == null )
			return;

		String xml = propDefn.getXmlValue( getModule( ), value );
		if ( xml == null )
			return;

		if ( propDefn.isEncryptable( ) )
		{
			IEncryptionHelper helper = MetaDataDictionary.getInstance( )
					.getEncryptionHelper( );
			xml = helper.encrypt( xml );
		}

		if ( tag == null )
			tag = getTagByPropertyType( propDefn );

		if ( propDefn.getTypeCode( ) == PropertyType.SCRIPT_TYPE )
			cdata = true;

		writeEntry( tag, propDefn.getName( ), xml, cdata );
	}

	/**
	 * Writes the structure memeber.
	 * 
	 * @param struct
	 *            the structure
	 * @param tag
	 *            the tag to write
	 * @param memberName
	 *            the member name
	 * @param cdata
	 *            whether the value should be written as CDATA
	 * @param withoutName
	 *            whether the property name should be written
	 */

	protected void writeProperty( IStructure struct, String tag,
			String memberName, boolean cdata, boolean withoutName )
	{
		StructureDefn structDefn = (StructureDefn) struct.getDefn( );
		assert structDefn != null;

		StructPropertyDefn propDefn = (StructPropertyDefn) structDefn
				.getMember( memberName );
		assert propDefn != null;

		Object value = struct.getLocalProperty( getModule( ), propDefn );
		if ( value == null )
			return;

		String xml = propDefn.getXmlValue( getModule( ), value );
		if ( xml == null )
			return;

		if ( propDefn.isEncryptable( ) )
		{
			IEncryptionHelper helper = MetaDataDictionary.getInstance( )
					.getEncryptionHelper( );
			xml = helper.encrypt( xml );
		}

		if ( tag == null )
			tag = getTagByPropertyType( propDefn );

		if ( propDefn.getTypeCode( ) == PropertyType.SCRIPT_TYPE )
			cdata = true;

		if ( withoutName )
			writeEntry( tag, null, xml, cdata );
		else
			writeEntry( tag, memberName, xml, cdata );
	}

	/**
	 * Writes the resource key entry.
	 * 
	 * @param tagName
	 *            the tag name to write
	 * @param name
	 *            the property name of resource
	 * @param key
	 *            the resource key value
	 * @param xml
	 *            the resource value
	 * @param cdata
	 *            whether the resource value should be written as CDATA.
	 */

	protected void writeResouceKey( String tagName, String name, String key,
			String xml, boolean cdata )
	{
		// No value should be output.

		if ( key == null && xml == null )
			return;

		writer.startElement( tagName );

		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, name );
		writer.attribute( DesignSchemaConstants.KEY_ATTRIB, key );
		if ( cdata )
			writer.textCDATA( xml );
		else
			writer.text( xml );

		writer.endElement( );
	}

	/**
	 * Writes the structure list, each of which has only one member.
	 * 
	 * @param obj
	 *            the design element
	 * @param propName
	 *            the name of the list property
	 * @param memberName
	 *            the name of the member in structure
	 */

	protected void writeSimpleStructureList( DesignElement obj,
			String propName, String memberName )
	{
		PropertyDefn prop = (ElementPropertyDefn) obj.getDefn( ).getProperty(
				propName );
		assert prop != null;
		assert prop.getTypeCode( ) == PropertyType.STRUCT_TYPE && prop.isList( );

		List list = (List) obj.getLocalProperty( getModule( ), propName );
		if ( list == null || list.size( ) == 0 )
			return;

		writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, propName );

		PropertyDefn propDef = (ElementPropertyDefn) obj.getDefn( )
				.getProperty( propName );
		PropertyDefn memberDefn = (PropertyDefn) propDef.getStructDefn( )
				.getMember( memberName );

		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			IStructure struct = (IStructure) iter.next( );

			propertyWithoutName( struct, memberDefn.getName( ) );
		}

		writer.endElement( );
	}

	/**
	 * Writes the structure list, each of which has only one member.
	 * 
	 * @param obj
	 *            the design element
	 * @param propName
	 *            the name of the list property
	 */

	protected void writeStructure( DesignElement obj, String propName )
	{
		PropertyDefn prop = (ElementPropertyDefn) obj.getDefn( ).getProperty(
				propName );
		if ( prop == null )
			return;

		IStructure struct = (IStructure) obj.getLocalProperty( getModule( ),
				propName );
		if ( struct == null )
			return;

		writer.conditionalStartElement( DesignSchemaConstants.STRUCTURE_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, propName );

		IStructureDefn structDefn = prop.getStructDefn( );

		Iterator iter = structDefn.propertiesIterator( );
		while ( iter.hasNext( ) )
		{
			StructPropertyDefn strcutPropDefn = (StructPropertyDefn) iter
					.next( );
			property( struct, strcutPropDefn.getName( ) );
		}

		writer.endElement( );
	}

	/**
	 * Writes the structure list, each of which has only one member.
	 * 
	 * @param obj
	 *            the design element
	 * @param propName
	 *            the name of the list property
	 */

	protected void writeSimplePropertyList( DesignElement obj, String propName )
	{
		PropertyDefn prop = (ElementPropertyDefn) obj.getDefn( ).getProperty(
				propName );
		if ( prop == null || prop.getTypeCode( ) != PropertyType.LIST_TYPE )
			return;

		List values = (List) obj.getLocalProperty( getModule( ), propName );
		if ( values == null || values.isEmpty( ) )
			return;

		writer
				.conditionalStartElement( DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, propName );

		for ( int i = 0; i < values.size( ); i++ )
		{
			PropertyType type = prop.getSubType( );
			String xmlValue = type.toXml( getModule( ), prop, values.get( i ) );
			if ( xmlValue != null )
			{
				writer.startElement( DesignSchemaConstants.VALUE_TAG );
				writer.text( xmlValue );
				writer.endElement( );
			}
		}

		writer.endElement( );
	}

	/**
	 * Writes the structure list, each of which has only one member.
	 * 
	 * @param struct
	 *            the partent structure that contains this structure
	 * @param memberName
	 *            the name of the list property
	 */

	protected void writeStructure( IStructure struct, String memberName )
	{
		IStructureDefn structDefn = struct.getDefn( ).getMember( memberName )
				.getStructDefn( );

		IStructure memberStruct = (IStructure) struct.getLocalProperty( null,
				(PropertyDefn) struct.getDefn( ).getMember( memberName ) );

		if ( memberStruct == null )
			return;

		writer.conditionalStartElement( DesignSchemaConstants.STRUCTURE_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, memberName );

		Iterator iter = structDefn.propertiesIterator( );
		while ( iter.hasNext( ) )
		{
			StructPropertyDefn strcutPropDefn = (StructPropertyDefn) iter
					.next( );

			property( memberStruct, strcutPropDefn.getName( ) );
		}

		writer.endElement( );
	}

	/**
	 * Writes the structure list of the give property definition for given
	 * design element.
	 * 
	 * @param obj
	 *            the design element to write
	 * @param propName
	 *            the name of the structure list property to write
	 */

	protected void writeStructureList( DesignElement obj, String propName )
	{
		PropertyDefn prop = (ElementPropertyDefn) obj.getDefn( ).getProperty(
				propName );
		assert prop != null;
		assert prop.getTypeCode( ) == PropertyType.STRUCT_TYPE && prop.isList( );

		List list = (List) obj.getLocalProperty( getModule( ), propName );
		if ( list == null || list.size( ) == 0 )
			return;

		writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, propName );

		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			IStructure struct = (IStructure) iter.next( );

			writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

			Iterator memberIter = prop.getStructDefn( ).propertiesIterator( );
			while ( memberIter.hasNext( ) )
			{
				PropertyDefn memberDefn = (PropertyDefn) memberIter.next( );
				property( struct, memberDefn.getName( ) );
			}
			writer.endElement( );
		}

		writer.endElement( );
	}

	/**
	 * Writes the structure list of the give property definition for given
	 * structure.
	 * 
	 * @param obj
	 *            the structure to write
	 * @param memberName
	 *            the name of the structure list property to write
	 */

	protected void writeStructureList( IStructure obj, String memberName )
	{
		PropertyDefn prop = (PropertyDefn) obj.getDefn( )
				.getMember( memberName );
		assert prop != null;
		assert prop.getTypeCode( ) == PropertyType.STRUCT_TYPE && prop.isList( );

		List list = (List) obj.getLocalProperty( getModule( ), prop );
		if ( list == null || list.size( ) == 0 )
			return;

		writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, memberName );

		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			IStructure struct = (IStructure) iter.next( );

			writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

			Iterator memberIter = prop.getStructDefn( ).propertiesIterator( );
			while ( memberIter.hasNext( ) )
			{
				PropertyDefn memberDefn = (PropertyDefn) memberIter.next( );
				property( struct, memberDefn.getName( ) );
			}
			writer.endElement( );
		}

		writer.endElement( );
	}

	/**
	 * Writes user property definitions.
	 * 
	 * @param obj
	 *            the element that contains user properties
	 */

	protected void writeUserPropertyDefns( DesignElement obj )
	{
		List props = obj.getLocalUserProperties( );
		if ( props == null || props.size( ) == 0 )
			return;

		writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
				DesignElement.USER_PROPERTIES_PROP );

		Iterator iter = props.iterator( );
		while ( iter.hasNext( ) )
		{
			UserPropertyDefn propDefn = (UserPropertyDefn) iter.next( );
			writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

			property( propDefn, UserPropertyDefn.NAME_MEMBER );
			property( propDefn, UserPropertyDefn.TYPE_MEMBER );

			resourceKey( propDefn, UserPropertyDefn.DISPLAY_NAME_ID_MEMBER,
					UserPropertyDefn.DISPLAY_NAME_MEMBER );

			// write default value

			if ( propDefn.getDefault( ) != null )
				writeEntry( DesignSchemaConstants.PROPERTY_TAG,
						UserPropertyDefn.DEFAULT_MEMBER, propDefn.getXmlValue(
								null, propDefn.getDefault( ) ), false );

			IChoiceSet choiceSet = propDefn.getChoices( );

			if ( choiceSet != null && choiceSet.getChoices( ) != null
					&& choiceSet.getChoices( ).length > 0 )
			{
				writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
				writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
						UserPropertyDefn.CHOICES_MEMBER );

				IChoice[] choices = choiceSet.getChoices( );

				for ( int i = 0; i < choices.length; i++ )
				{
					UserChoice choice = (UserChoice) choices[i];
					writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

					writeEntry( DesignSchemaConstants.PROPERTY_TAG,
							UserChoice.NAME_PROP, choice.getName( ), false );

					if ( choice.getValue( ) != null )
					{
						writeEntry( DesignSchemaConstants.PROPERTY_TAG,
								UserChoice.VALUE_PROP, choice.getValue( )
										.toString( ), false );
					}

					writeResouceKey( DesignSchemaConstants.TEXT_PROPERTY_TAG,
							UserChoice.DISPLAY_NAME_PROP, choice
									.getDisplayNameKey( ), choice
									.getDisplayName( ), false );

					writer.endElement( );
				}
				writer.endElement( );
			}
			writer.endElement( );
		}
		writer.endElement( );
	}

	/**
	 * Writes the values for user properties.
	 * 
	 * @param obj
	 *            the element that has user properties.
	 */

	protected void writeUserPropertyValues( DesignElement obj )
	{
		List userProps = obj.getUserProperties( );
		if ( userProps == null || userProps.size( ) == 0 )
			return;

		Iterator iter = userProps.iterator( );
		while ( iter.hasNext( ) )
		{
			UserPropertyDefn propDefn = (UserPropertyDefn) iter.next( );

			property( obj, propDefn.getName( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitModule(org.eclipse.birt.report.model.core.Module)
	 */

	public void visitModule( Module obj )
	{
		writer.attribute( DesignSchemaConstants.XMLNS_ATTRIB,
				DEFAULT_NAME_SPACE );
		writer.attribute( DesignSchemaConstants.VERSION_ATTRIB,
				DesignSchemaConstants.REPORT_VERSION );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		property( obj, Module.AUTHOR_PROP );
		property( obj, Module.HELP_GUIDE_PROP );
		property( obj, Module.CREATED_BY_PROP );
		property( obj, Module.UNITS_PROP );
		property( obj, Module.BASE_PROP );
		property( obj, Module.INCLUDE_RESOURCE_PROP );

		resourceKey( obj, Module.TITLE_ID_PROP, Module.TITLE_PROP );
		property( obj, Module.COMMENTS_PROP );

		resourceKey( obj, Module.DESCRIPTION_ID_PROP, Module.DESCRIPTION_PROP );

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		// write property bindings

		writeStructureList( obj, Module.PROPERTY_BINDINGS_PROP );
	}

	/**
	 * Visits the embedded images of the module.
	 * 
	 * @param obj
	 *            the module to traverse
	 */

	protected void writeEmbeddedImages( Module obj )
	{
		List list = (List) obj.getLocalProperty( obj, Module.IMAGES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					ReportDesign.IMAGES_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				EmbeddedImage image = (EmbeddedImage) list.get( i );
				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

				property( image, EmbeddedImage.NAME_MEMBER );
				property( image, EmbeddedImage.TYPE_MEMBER );
				property( image, EmbeddedImage.LIB_REFERENCE_MEMBER );

				try
				{
					if ( image.getLocalProperty( getModule( ),
							(PropertyDefn) image.getDefn( ).getMember(
									EmbeddedImage.DATA_MEMBER ) ) != null )
					{
						byte[] data = base
								.encode( image.getData( getModule( ) ) );
						String value = new String( data, EmbeddedImage.CHARSET );

						if ( value.length( ) < IndentableXMLWriter.MAX_CHARS_PER_LINE )
							writeEntry( DesignSchemaConstants.PROPERTY_TAG,
									EmbeddedImage.DATA_MEMBER, value, false );
						else
							writeLongIndentText(
									DesignSchemaConstants.PROPERTY_TAG,
									EmbeddedImage.DATA_MEMBER, value );
					}
				}
				catch ( UnsupportedEncodingException e )
				{
					assert false;
				}
				writer.endElement( );
			}
			writer.endElement( );
		}
	}

	/**
	 * Visits the translations of the module.
	 * 
	 * @param obj
	 *            the module to traverse
	 */

	protected void writeTranslations( Module obj )
	{
		String[] resourceKeys = obj.getTranslationResourceKeys( );
		if ( resourceKeys != null && resourceKeys.length > 0 )
		{
			writer.startElement( DesignSchemaConstants.TRANSLATIONS_TAG );

			for ( int i = 0; i < resourceKeys.length; i++ )
			{
				writer.startElement( DesignSchemaConstants.RESOURCE_TAG );
				writer.attribute( DesignSchemaConstants.KEY_ATTRIB,
						resourceKeys[i] );

				List translations = obj.getTranslations( resourceKeys[i] );
				for ( int j = 0; j < translations.size( ); j++ )
				{
					writer.startElement( DesignSchemaConstants.TRANSLATION_TAG );

					Translation translation = (Translation) translations
							.get( j );

					writer.attribute( DesignSchemaConstants.LOCALE_ATTRIB,
							translation.getLocale( ) );
					writer.text( translation.getText( ) );
					writer.endElement( );
				}
				writer.endElement( );
			}
			writer.endElement( );
		}
	}

	/**
	 * Visits the custom colors of the module.
	 * 
	 * @param obj
	 *            the module to traverse
	 */

	protected void writeCustomColors( Module obj )
	{
		List list = (List) obj
				.getLocalProperty( obj, Module.COLOR_PALETTE_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					ReportDesign.COLOR_PALETTE_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				CustomColor color = (CustomColor) list.get( i );

				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );
				property( color, CustomColor.NAME_MEMBER );
				property( color, CustomColor.COLOR_MEMBER );
				resourceKey( color, CustomColor.DISPLAY_NAME_ID_MEMBER,
						CustomColor.DISPLAY_NAME_MEMBER );
				writer.endElement( );
			}
			writer.endElement( );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitScriptDataSource(org.eclipse.birt.report.model.elements.ScriptDataSource)
	 */

	public void visitScriptDataSource( ScriptDataSource obj )
	{
		writer.startElement( DesignSchemaConstants.SCRIPT_DATA_SOURCE_TAG );

		super.visitScriptDataSource( obj );

		property( obj, ScriptDataSource.OPEN_METHOD );
		property( obj, ScriptDataSource.CLOSE_METHOD );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitExtendedDataSource(org.eclipse.birt.report.model.elements.ExtendedDataSource)
	 */
	public void visitOdaDataSource( OdaDataSource obj )
	{
		writer.startElement( DesignSchemaConstants.ODA_DATA_SOURCE_TAG );
		attribute( obj, DesignSchemaConstants.EXTENSION_ID_ATTRIB,
				OdaDataSource.EXTENSION_ID_PROP );

		super.visitOdaDataSource( obj );

		writeOdaDesignerState( obj, OdaDataSource.DESIGNER_STATE_PROP );

		List properties = (List) obj.getLocalProperty( getModule( ),
				OdaDataSource.PRIVATE_DRIVER_PROPERTIES_PROP );
		writeExtendedProperties( properties,
				OdaDataSource.PRIVATE_DRIVER_PROPERTIES_PROP );

		writeOdaExtensionProperties( obj, OdaDataSource.EXTENSION_ID_PROP );

		writer.endElement( );
	}

	/**
	 * Writes ODA exension properties.
	 * 
	 * @param obj
	 *            the ODA element to write
	 * @param extensionIDProp
	 *            the extension ID for ODA properties
	 */

	private void writeOdaExtensionProperties( DesignElement obj,
			String extensionIDProp )
	{
		IElementDefn extDefn = null;
		if ( obj instanceof OdaDataSource )
			extDefn = ( (OdaDataSource) obj ).getExtDefn( );
		else if ( obj instanceof OdaDataSet )
			extDefn = ( (OdaDataSet) obj ).getExtDefn( );

		if ( extDefn == null )
			return;

		List list = extDefn.getLocalProperties( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			PropertyDefn prop = (PropertyDefn) list.get( i );
			if ( extensionIDProp.equals( prop.getName( ) ) )
				continue;

			Object value = obj.getLocalProperty( getModule( ), prop.getName( ) );
			if ( value != null )
			{
				if ( prop.getTypeCode( ) != PropertyType.XML_TYPE )
					writeProperty( obj, getTagByPropertyType( prop ), prop
							.getName( ), false );
				else
					writeProperty( obj, getTagByPropertyType( prop ), prop
							.getName( ), true );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitScriptDataSet(org.eclipse.birt.report.model.elements.ScriptDataSet)
	 */
	public void visitScriptDataSet( ScriptDataSet obj )
	{
		writer.startElement( DesignSchemaConstants.SCRIPT_DATA_SET_TAG );

		super.visitScriptDataSet( obj );

		property( obj, ScriptDataSet.OPEN_METHOD );
		property( obj, ScriptDataSet.DESCRIBE_METHOD );
		property( obj, ScriptDataSet.FETCH_METHOD );
		property( obj, ScriptDataSet.CLOSE_METHOD );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitFreeForm(org.eclipse.birt.report.model.design.elements.Container)
	 */

	public void visitFreeForm( FreeForm obj )
	{
		writer.startElement( DesignSchemaConstants.FREE_FORM_TAG );

		super.visitFreeForm( obj );

		writeContents( obj, FreeForm.REPORT_ITEMS_SLOT,
				DesignSchemaConstants.REPORT_ITEMS_TAG );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitDataItem(org.eclipse.birt.report.model.design.elements.DataItem)
	 */

	public void visitDataItem( DataItem obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealData( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.DATA_TAG );

		super.visitDataItem( obj );

		property( obj, DataItem.RESULT_SET_COLUMN_PROP );

		resourceKey( obj, DataItem.HELP_TEXT_KEY_PROP, DataItem.HELP_TEXT_PROP );

		writeAction( obj, DataItem.ACTION_PROP );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitMultiLineDataItem(org.eclipse.birt.report.model.elements.TextDataItem)
	 */

	public void visitTextDataItem( TextDataItem obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealTextData( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.TEXT_DATA_TAG );

		super.visitTextDataItem( obj );

		property( obj, TextDataItem.VALUE_EXPR_PROP );
		property( obj, TextDataItem.CONTENT_TYPE_PROP );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitExtendedItem(org.eclipse.birt.report.model.elements.ExtendedItem)
	 */

	public void visitExtendedItem( ExtendedItem obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealExtendedItem( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.EXTENDED_ITEM_TAG );
		attribute( obj, DesignSchemaConstants.EXTENSION_NAME_ATTRIB,
				ExtendedItem.EXTENSION_NAME_PROP );
	
		super.visitExtendedItem( obj );
		
		resourceKey( obj, ExtendedItem.ALT_TEXT_KEY_PROP,
				ExtendedItem.ALT_TEXT_PROP );
		
		// write the extension item local properties
		ExtensionElementDefn extDefn = obj.getExtDefn( );
		if ( extDefn != null )
		{
			// TODO: write the style properties

			List list = extDefn.getLocalProperties( );
			for ( int i = 0; i < list.size( ); i++ )
			{
				PropertyDefn prop = (PropertyDefn) list.get( i );
				if ( ExtendedItem.EXTENSION_NAME_PROP.equals( prop.getName( ) ) )
					continue;

				// TODO: support extending those xml properties.
				// Now, each time a child is initialized, its xml-properties are
				// serialized on the IReportItem itself, never minding whether
				// the xml-property values are set locally or extended from
				// parent.

				if ( prop.getTypeCode( ) != PropertyType.XML_TYPE )
					writeProperty( obj, getTagByPropertyType( prop ), prop
							.getName( ), false );
				else
					writeProperty( obj, getTagByPropertyType( prop ), prop
							.getName( ), true );

			}
		}

		// write filter properties for the extended item

		writeStructureList( obj, ExtendedItem.FILTER_PROP );
		
		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitTextItem(org.eclipse.birt.report.model.design.elements.TextItem)
	 */

	public void visitTextItem( TextItem obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealText( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.TEXT_TAG );

		super.visitTextItem( obj );

		property( obj, TextItem.CONTENT_TYPE_PROP );
		resourceKeyCDATA( obj, TextItem.CONTENT_RESOURCE_KEY_PROP,
				TextItem.CONTENT_PROP );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitLabel(org.eclipse.birt.report.model.design.elements.Label)
	 */

	public void visitLabel( Label obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealLabel( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.LABEL_TAG );

		super.visitLabel( obj );

		resourceKey( obj, Label.TEXT_ID_PROP, Label.TEXT_PROP );
		resourceKey( obj, Label.HELP_TEXT_ID_PROP, Label.HELP_TEXT_PROP );

		writeAction( obj, Label.ACTION_PROP );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitLabel(org.eclipse.birt.report.model.design.elements.Label)
	 */

	public void visitAutoText( AutoText obj )
	{
		writer.startElement( DesignSchemaConstants.AUTO_TEXT_TAG );

		super.visitAutoText( obj );

		property( obj, AutoText.AUTOTEXT_TYPE_PROP );

		writer.endElement( );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitList(org.eclipse.birt.report.model.design.elements.ListItem)
	 */

	public void visitList( ListItem obj )
	{
		// provide bound column compatibility for list

		boundColumnsMgr.dealList( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.LIST_TAG );

		super.visitList( obj );

		writeContents( obj, ListItem.HEADER_SLOT,
				DesignSchemaConstants.HEADER_TAG );

		// There is no groups tag for this slot. All groups are written under
		// list tag.

		writeContents( obj, ListItem.GROUP_SLOT, null );
		writeContents( obj, ListItem.DETAIL_SLOT,
				DesignSchemaConstants.DETAIL_TAG );
		writeContents( obj, ListItem.FOOTER_SLOT,
				DesignSchemaConstants.FOOTER_TAG );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitListGroup(org.eclipse.birt.report.model.design.elements.ListGroup)
	 */

	public void visitListGroup( ListGroup obj )
	{
		writer.startElement( DesignSchemaConstants.GROUP_TAG );

		super.visitListGroup( obj );

		writeContents( obj, ListGroup.HEADER_SLOT,
				DesignSchemaConstants.HEADER_TAG );
		writeContents( obj, ListGroup.FOOTER_SLOT,
				DesignSchemaConstants.FOOTER_TAG );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitTable(org.eclipse.birt.report.model.design.elements.TableItem)
	 */

	public void visitTable( TableItem obj )
	{
		// provide bound column compatibility for table

		boundColumnsMgr.dealTable( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.TABLE_TAG );

		super.visitTable( obj );

		resourceKey( obj, TableItem.CAPTION_KEY_PROP, TableItem.CAPTION_PROP );

		// There is no columns tag for this slot. All columns are written under
		// table tag.

		writeColumns( obj, TableItem.COLUMN_SLOT );

		writeContents( obj, TableItem.HEADER_SLOT,
				DesignSchemaConstants.HEADER_TAG );

		// There is no groups tag for this slot. All groups are written under
		// table tag.

		writeContents( obj, TableItem.GROUP_SLOT, null );

		writeContents( obj, TableItem.DETAIL_SLOT,
				DesignSchemaConstants.DETAIL_TAG );
		writeContents( obj, TableItem.FOOTER_SLOT,
				DesignSchemaConstants.FOOTER_TAG );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitTableGroup(org.eclipse.birt.report.model.design.elements.TableGroup)
	 */

	public void visitTableGroup( TableGroup obj )
	{
		writer.startElement( DesignSchemaConstants.GROUP_TAG );

		super.visitTableGroup( obj );

		writeContents( obj, TableGroup.HEADER_SLOT,
				DesignSchemaConstants.HEADER_TAG );
		writeContents( obj, TableGroup.FOOTER_SLOT,
				DesignSchemaConstants.FOOTER_TAG );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitColumn(org.eclipse.birt.report.model.design.elements.TableColumn)
	 */

	public void visitColumn( TableColumn obj )
	{
		// If there is no property defined for column, nothing should be
		// written.

		writer.startElement( DesignSchemaConstants.COLUMN_TAG );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		attribute( obj, DesignSchemaConstants.VIEW_ACTION_ATTRIB,
				DesignElement.VIEW_ACTION_PROP );

		super.visitColumn( obj );

		property( obj, TableColumn.WIDTH_PROP );
		property( obj, TableColumn.REPEAT_PROP );
		property( obj, TableColumn.SUPPRESS_DUPLICATES_PROP );
		writeStructureList( obj, TableColumn.VISIBILITY_PROP );

		writeStyle( obj );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitColumn(org.eclipse.birt.report.model.design.elements.TableColumn)
	 */

	public void visitRow( TableRow obj )
	{
		writer.startElement( DesignSchemaConstants.ROW_TAG );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		attribute( obj, DesignSchemaConstants.VIEW_ACTION_ATTRIB,
				DesignElement.VIEW_ACTION_PROP );

		super.visitRow( obj );

		property( obj, TableRow.HEIGHT_PROP );
		property( obj, TableRow.BOOKMARK_PROP );
		property( obj, TableRow.SUPPRESS_DUPLICATES_PROP );

		property( obj, TableRow.EVENT_HANDLER_CLASS_PROP );
		property( obj, TableRow.ON_PREPARE_METHOD );
		property( obj, TableRow.ON_CREATE_METHOD );
		property( obj, TableRow.ON_RENDER_METHOD );

		// write user property definitions and values

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		writeStyle( obj );
		writeStructureList( obj, TableRow.VISIBILITY_PROP );

		writeContents( obj, TableRow.CONTENT_SLOT, null );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitCell(org.eclipse.birt.report.model.design.elements.Cell)
	 */

	public void visitCell( Cell obj )
	{
		writer.startElement( DesignSchemaConstants.CELL_TAG );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		attribute( obj, DesignSchemaConstants.VIEW_ACTION_ATTRIB,
				DesignElement.VIEW_ACTION_PROP );

		super.visitCell( obj );

		property( obj, Cell.COLUMN_PROP );
		property( obj, Cell.COL_SPAN_PROP );
		property( obj, Cell.ROW_SPAN_PROP );
		property( obj, Cell.DROP_PROP );
		property( obj, Cell.HEIGHT_PROP );
		property( obj, Cell.WIDTH_PROP );
		property( obj, Cell.EVENT_HANDLER_CLASS_PROP );
		property( obj, Cell.ON_PREPARE_METHOD );
		property( obj, Cell.ON_CREATE_METHOD );
		property( obj, Cell.ON_RENDER_METHOD );

		writeStyle( obj );

		writeContents( obj, Cell.CONTENT_SLOT, null );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitGrid(org.eclipse.birt.report.model.design.elements.GridItem)
	 */

	public void visitGrid( GridItem obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealGrid( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.GRID_TAG );

		super.visitGrid( obj );

		writeColumns( obj, GridItem.COLUMN_SLOT );
		writeContents( obj, GridItem.ROW_SLOT, null );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitLine(org.eclipse.birt.report.model.design.elements.LineItem)
	 */

	public void visitLine( LineItem obj )
	{
		writer.startElement( DesignSchemaConstants.LINE_TAG );

		super.visitLine( obj );

		property( obj, LineItem.ORIENTATION_PROP );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitGraphicMasterPage(org.eclipse.birt.report.model.design.elements.GraphicMasterPage)
	 */

	public void visitGraphicMasterPage( GraphicMasterPage obj )
	{
		writer.startElement( DesignSchemaConstants.GRAPHIC_MASTER_PAGE_TAG );

		super.visitGraphicMasterPage( obj );

		property( obj, GraphicMasterPage.COLUMNS_PROP );
		property( obj, GraphicMasterPage.COLUMN_SPACING_PROP );

		writeContents( obj, GraphicMasterPage.CONTENT_SLOT,
				DesignSchemaConstants.CONTENTS_TAG );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitSimpleMasterPage(org.eclipse.birt.report.model.design.elements.SimpleMasterPage)
	 */

	public void visitSimpleMasterPage( SimpleMasterPage obj )
	{
		writer.startElement( DesignSchemaConstants.SIMPLE_MASTER_PAGE_TAG );

		super.visitSimpleMasterPage( obj );

		property( obj, SimpleMasterPage.SHOW_HEADER_ON_FIRST_PROP );
		property( obj, SimpleMasterPage.SHOW_FOOTER_ON_LAST_PROP );
		property( obj, SimpleMasterPage.FLOATING_FOOTER );
		property( obj, SimpleMasterPage.HEADER_HEIGHT_PROP );
		property( obj, SimpleMasterPage.FOOTER_HEIGHT_PROP );

		writeContents( obj, SimpleMasterPage.PAGE_HEADER_SLOT,
				DesignSchemaConstants.PAGE_HEADER_TAG );
		writeContents( obj, SimpleMasterPage.PAGE_FOOTER_SLOT,
				DesignSchemaConstants.PAGE_FOOTER_TAG );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitParameterGroup(org.eclipse.birt.report.model.design.elements.ParameterGroup)
	 */

	public void visitParameterGroup( ParameterGroup obj )
	{
		writer.startElement( DesignSchemaConstants.PARAMETER_GROUP_TAG );

		super.visitParameterGroup( obj );

		property( obj, ParameterGroup.START_EXPANDED_PROP );

		resourceKey( obj, ParameterGroup.HELP_TEXT_KEY_PROP,
				ParameterGroup.HELP_TEXT_PROP );

		writeContents( obj, ParameterGroup.PARAMETERS_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitCascadingParameterGroup(org.eclipse.birt.report.model.elements.CascadingParameterGroup)
	 */

	public void visitCascadingParameterGroup( CascadingParameterGroup obj )
	{
		writer
				.startElement( DesignSchemaConstants.CASCADING_PARAMETER_GROUP_TAG );

		super.visitParameterGroup( obj );

		property( obj, CascadingParameterGroup.START_EXPANDED_PROP );
		resourceKey( obj, CascadingParameterGroup.HELP_TEXT_KEY_PROP,
				CascadingParameterGroup.HELP_TEXT_PROP );
		property( obj, CascadingParameterGroup.DATA_SET_PROP );
		property( obj, CascadingParameterGroup.PROMPT_TEXT_PROP );
		property( obj, CascadingParameterGroup.DATA_SET_MODE_PROP );

		writeContents( obj, CascadingParameterGroup.PARAMETERS_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );
		writeOverridenPropertyValues( obj );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitScalarParameter(org.eclipse.birt.report.model.elements.ScalarParameter)
	 */

	public void visitScalarParameter( ScalarParameter obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealScalarParameter( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.SCALAR_PARAMETER_TAG );

		super.visitScalarParameter( obj );

		property( obj, ScalarParameter.VALUE_TYPE_PROP );
		property( obj, ScalarParameter.DATA_TYPE_PROP );
		resourceKey( obj, ScalarParameter.PROMPT_TEXT_ID_PROP,
				ScalarParameter.PROMPT_TEXT_PROP );
		property( obj, ScalarParameter.LIST_LIMIT_PROP );
		property( obj, ScalarParameter.CONCEAL_VALUE_PROP );
		property( obj, ScalarParameter.ALLOW_BLANK_PROP );
		property( obj, ScalarParameter.ALLOW_NULL_PROP );
		property( obj, ScalarParameter.CONTROL_TYPE_PROP );
		property( obj, ScalarParameter.ALIGNMENT_PROP );
		property( obj, ScalarParameter.DATASET_NAME_PROP );
		property( obj, ScalarParameter.VALUE_EXPR_PROP );
		property( obj, ScalarParameter.LABEL_EXPR_PROP );
		property( obj, ScalarParameter.MUCH_MATCH_PROP );
		property( obj, ScalarParameter.FIXED_ORDER_PROP );
		property( obj, ScalarParameter.DEFAULT_VALUE_PROP );

		writeStructure( obj, ScalarParameter.FORMAT_PROP );
		writeStructureList( obj, ScalarParameter.SELECTION_LIST_PROP );
		writeStructureList( obj, ReportItem.BOUND_DATA_COLUMNS_PROP );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTemplateDataSet(org.eclipse.birt.report.model.elements.TemplateDataSet)
	 */

	public void visitTemplateDataSet( TemplateDataSet obj )
	{
		writer.startElement( DesignSchemaConstants.TEMPLATE_DATA_SET_TAG );
		super.visitTemplateDataSet( obj );
		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTemplateElement(org.eclipse.birt.report.model.elements.TemplateElement)
	 */

	public void visitTemplateElement( TemplateElement obj )
	{
		attribute( obj, DesignSchemaConstants.NAME_ATTRIB,
				DesignElement.NAME_PROP );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );

		property( obj, TemplateElement.REF_TEMPLATE_PARAMETER_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTemplateParameterDefinition(org.eclipse.birt.report.model.elements.TemplateParameterDefinition)
	 */
	public void visitTemplateParameterDefinition(
			TemplateParameterDefinition obj )
	{
		writer
				.startElement( DesignSchemaConstants.TEMPLATE_PARAMETER_DEFINITION_TAG );
		attribute( obj, DesignSchemaConstants.NAME_ATTRIB,
				DesignElement.NAME_PROP );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );

		property( obj, TemplateParameterDefinition.ALLOWED_TYPE_PROP );
		resourceKey( obj, TemplateParameterDefinition.DESCRIPTION_ID_PROP,
				TemplateParameterDefinition.DESCRIPTION_PROP );

		writeContents( obj, TemplateParameterDefinition.DEFAULT_SLOT,
				DesignSchemaConstants.DEFAULT_TAG );
		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitTemplateReportItem(org.eclipse.birt.report.model.elements.TemplateReportItem)
	 */

	public void visitTemplateReportItem( TemplateReportItem obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealTemplateReportItem( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.TEMPLATE_REPORT_ITEM_TAG );
		super.visitTemplateReportItem( obj );

		writeStructureList( obj, TemplateReportItem.VISIBILITY_PROP );

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitStyle(org.eclipse.birt.report.model.elements.Style)
	 */

	public void visitStyle( Style obj )
	{
		writer.startElement( DesignSchemaConstants.STYLE_TAG );

		super.visitStyle( obj );

		writeStyleProps( obj );

		writer.endElement( );
	}

	/**
	 * Write the style properties for either a shared style, or the "private
	 * style" for a styled element.
	 * 
	 * @param obj
	 *            the styled element
	 */

	private void writeStyleProps( DesignElement obj )
	{
		// Background

		property( obj, Style.BACKGROUND_ATTACHMENT_PROP );
		property( obj, Style.BACKGROUND_COLOR_PROP );
		property( obj, Style.BACKGROUND_IMAGE_PROP );
		property( obj, Style.BACKGROUND_POSITION_X_PROP );
		property( obj, Style.BACKGROUND_POSITION_Y_PROP );
		property( obj, Style.BACKGROUND_REPEAT_PROP );

		// Font

		property( obj, Style.FONT_FAMILY_PROP );
		property( obj, Style.FONT_SIZE_PROP );
		property( obj, Style.FONT_WEIGHT_PROP );
		property( obj, Style.FONT_STYLE_PROP );
		property( obj, Style.FONT_VARIANT_PROP );
		property( obj, Style.COLOR_PROP );
		property( obj, Style.TEXT_LINE_THROUGH_PROP );
		property( obj, Style.TEXT_OVERLINE_PROP );
		property( obj, Style.TEXT_UNDERLINE_PROP );

		// Border

		property( obj, Style.BORDER_BOTTOM_COLOR_PROP );
		property( obj, Style.BORDER_BOTTOM_STYLE_PROP );
		property( obj, Style.BORDER_BOTTOM_WIDTH_PROP );
		property( obj, Style.BORDER_LEFT_COLOR_PROP );
		property( obj, Style.BORDER_LEFT_STYLE_PROP );
		property( obj, Style.BORDER_LEFT_WIDTH_PROP );
		property( obj, Style.BORDER_RIGHT_COLOR_PROP );
		property( obj, Style.BORDER_RIGHT_STYLE_PROP );
		property( obj, Style.BORDER_RIGHT_WIDTH_PROP );
		property( obj, Style.BORDER_TOP_COLOR_PROP );
		property( obj, Style.BORDER_TOP_STYLE_PROP );
		property( obj, Style.BORDER_TOP_WIDTH_PROP );

		// Margin

		property( obj, Style.MARGIN_TOP_PROP );
		property( obj, Style.MARGIN_LEFT_PROP );
		property( obj, Style.MARGIN_BOTTOM_PROP );
		property( obj, Style.MARGIN_RIGHT_PROP );

		// Padding

		property( obj, Style.PADDING_TOP_PROP );
		property( obj, Style.PADDING_LEFT_PROP );
		property( obj, Style.PADDING_BOTTOM_PROP );
		property( obj, Style.PADDING_RIGHT_PROP );

		// Formats

		property( obj, Style.NUMBER_ALIGN_PROP );

		writeStructure( obj, Style.DATE_TIME_FORMAT_PROP );
		writeStructure( obj, Style.NUMBER_FORMAT_PROP );
		writeStructure( obj, Style.STRING_FORMAT_PROP );

		// Text format

		property( obj, Style.TEXT_ALIGN_PROP );
		property( obj, Style.TEXT_INDENT_PROP );
		property( obj, Style.LETTER_SPACING_PROP );
		property( obj, Style.LINE_HEIGHT_PROP );
		property( obj, Style.ORPHANS_PROP );
		property( obj, Style.TEXT_TRANSFORM_PROP );
		property( obj, Style.VERTICAL_ALIGN_PROP );
		property( obj, Style.WHITE_SPACE_PROP );
		property( obj, Style.WIDOWS_PROP );
		property( obj, Style.WORD_SPACING_PROP );

		// Section Options

		property( obj, Style.DISPLAY_PROP );
		property( obj, Style.MASTER_PAGE_PROP );
		property( obj, Style.PAGE_BREAK_AFTER_PROP );
		property( obj, Style.PAGE_BREAK_BEFORE_PROP );
		property( obj, Style.PAGE_BREAK_INSIDE_PROP );
		property( obj, Style.SHOW_IF_BLANK_PROP );
		property( obj, Style.CAN_SHRINK_PROP );

		// Highlight

		List list = (ArrayList) obj.getLocalProperty( getModule( ),
				Style.HIGHLIGHT_RULES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					Style.HIGHLIGHT_RULES_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				HighlightRule rule = (HighlightRule) list.get( i );
				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

				property( rule, HighlightRule.OPERATOR_MEMBER );

				property( rule, HighlightRule.BACKGROUND_COLOR_MEMBER );

				// Border
				property( rule, HighlightRule.BORDER_TOP_STYLE_MEMBER );
				property( rule, HighlightRule.BORDER_TOP_WIDTH_MEMBER );
				property( rule, HighlightRule.BORDER_TOP_COLOR_MEMBER );

				property( rule, HighlightRule.BORDER_LEFT_STYLE_MEMBER );
				property( rule, HighlightRule.BORDER_LEFT_WIDTH_MEMBER );
				property( rule, HighlightRule.BORDER_LEFT_COLOR_MEMBER );

				property( rule, HighlightRule.BORDER_BOTTOM_STYLE_MEMBER );
				property( rule, HighlightRule.BORDER_BOTTOM_WIDTH_MEMBER );
				property( rule, HighlightRule.BORDER_BOTTOM_COLOR_MEMBER );

				property( rule, HighlightRule.BORDER_RIGHT_STYLE_MEMBER );
				property( rule, HighlightRule.BORDER_RIGHT_WIDTH_MEMBER );
				property( rule, HighlightRule.BORDER_RIGHT_COLOR_MEMBER );

				// Font
				property( rule, HighlightRule.FONT_FAMILY_MEMBER );
				property( rule, HighlightRule.FONT_SIZE_MEMBER );
				property( rule, HighlightRule.FONT_STYLE_MEMBER );
				property( rule, HighlightRule.FONT_WEIGHT_MEMBER );
				property( rule, HighlightRule.FONT_VARIANT_MEMBER );
				property( rule, HighlightRule.COLOR_MEMBER );
				property( rule, HighlightRule.TEXT_UNDERLINE_MEMBER );
				property( rule, HighlightRule.TEXT_OVERLINE_MEMBER );
				property( rule, HighlightRule.TEXT_LINE_THROUGH_MEMBER );
				property( rule, HighlightRule.TEXT_ALIGN_MEMBER );
				property( rule, HighlightRule.TEXT_TRANSFORM_MEMBER );
				property( rule, HighlightRule.TEXT_INDENT_MEMBER );

				// Format
				property( rule, HighlightRule.NUMBER_ALIGN_MEMBER );

				writeStructure( rule, HighlightRule.DATE_TIME_FORMAT_MEMBER );
				writeStructure( rule, HighlightRule.NUMBER_FORMAT_MEMBER );
				writeStructure( rule, HighlightRule.STRING_FORMAT_MEMBER );

				property( rule, HighlightRule.TEST_EXPR_MEMBER );
				property( rule, HighlightRule.VALUE1_MEMBER );
				property( rule, HighlightRule.VALUE2_MEMBER );

				writer.endElement( );
			}
			writer.endElement( );
		}

		// Map

		list = (ArrayList) obj.getLocalProperty( getModule( ),
				Style.MAP_RULES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					Style.MAP_RULES_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				MapRule rule = (MapRule) list.get( i );
				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

				property( rule, MapRule.TEST_EXPR_MEMBER );
				property( rule, MapRule.OPERATOR_MEMBER );
				property( rule, MapRule.VALUE1_MEMBER );
				property( rule, MapRule.VALUE2_MEMBER );

				resourceKey( rule, MapRule.DISPLAY_ID_MEMBER,
						MapRule.DISPLAY_MEMBER );
				writer.endElement( );
			}
			writer.endElement( );
		}
	}

	/**
	 * Writes the contents of a slot. The contents are enclosed in an optional
	 * list tag.
	 * 
	 * @param obj
	 *            the container element
	 * @param slot
	 *            the slot to write
	 * @param tag
	 *            the optional list tag that encloses the list of contents
	 */

	protected void writeContents( DesignElement obj, int slot, String tag )
	{
		List list = obj.getSlot( slot ).getContents( );
		if ( list.isEmpty( ) )
			return;

		// if there is "extends" element, do not write out the conent.

		if ( obj.getExtendsElement( ) != null )
			return;

		if ( tag != null )
			writer.conditionalStartElement( tag );

		// Iterate over the contents using this visitor to write each one.
		// Note that this may result in a recursive call back into this
		// method as we do a depth-first traversal of the design tree.

		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			( (DesignElement) iter.next( ) ).apply( this );
		}
		if ( tag != null )
			writer.endElement( );
	}

	/**
	 * Writes the contents of a slot. The order is not the order in the slot
	 * while we first write the ancestor and then the derived ones. The contents
	 * are enclosed in an optional list tag.
	 * 
	 * @param obj
	 *            the container element
	 * @param slot
	 *            the slot to write
	 * @param tag
	 *            the optional list tag that encloses the list of contents
	 */

	protected void writeArrangedContents( DesignElement obj, int slot,
			String tag )
	{
		List list = obj.getSlot( slot ).getContents( );
		if ( list.isEmpty( ) )
			return;
		LinkedList newList = new LinkedList( );
		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			DesignElement element = (DesignElement) iter.next( );
			DesignElement parent = element.getExtendsElement( );
			if ( !newList.contains( element ) )
			{
				newList.add( element );
			}
			if ( parent != null && list.contains( parent ) )
			{
				if ( !newList.contains( parent ) )
				{
					int index = newList.indexOf( element );
					newList.add( index, parent );
				}
				else if ( newList.indexOf( element ) < newList.indexOf( parent ) )
				{
					newList.remove( parent );
					int index = newList.indexOf( element );
					newList.add( index, parent );
				}
			}
		}
		if ( tag != null )
			writer.conditionalStartElement( tag );

		// Iterate over the contents using this visitor to write each one.
		// Note that this may result in a recursive call back into this
		// method as we do a depth-first traversal of the design tree.

		iter = newList.iterator( );
		while ( iter.hasNext( ) )
		{
			( (DesignElement) iter.next( ) ).apply( this );
		}
		if ( tag != null )
			writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitRectangle(org.eclipse.birt.report.model.design.elements.Rectangle)
	 */

	public void visitRectangle( RectangleItem obj )
	{
		writer.startElement( DesignSchemaConstants.RECTANGLE_TAG );

		super.visitRectangle( obj );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.elements.ElementVisitor#visitImage(org.eclipse.birt.report.model.design.elements.ImageItem)
	 */

	public void visitImage( ImageItem obj )
	{
		// provide bound column compatibility

		boundColumnsMgr.dealImage( obj, getModule( ) );

		writer.startElement( DesignSchemaConstants.IMAGE_TAG );

		super.visitImage( obj );

		property( obj, ImageItem.SIZE_PROP );
		property( obj, ImageItem.SCALE_PROP );

		String source = (String) obj.getLocalProperty( getModule( ),
				ImageItem.SOURCE_PROP );

		if ( DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase( source )
				|| DesignChoiceConstants.IMAGE_REF_TYPE_FILE
						.equalsIgnoreCase( source ) )
		{
			property( obj, ImageItem.URI_PROP );
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_EMBED
				.equalsIgnoreCase( source ) )
		{
			property( obj, ImageItem.IMAGE_NAME_PROP );
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_EXPR
				.equalsIgnoreCase( source ) )
		{
			property( obj, ImageItem.TYPE_EXPR_PROP );
			property( obj, ImageItem.VALUE_EXPR_PROP );
		}

		resourceKey( obj, ImageItem.ALT_TEXT_KEY_PROP, ImageItem.ALT_TEXT_PROP );
		resourceKey( obj, ImageItem.HELP_TEXT_ID_PROP, ImageItem.HELP_TEXT_PROP );

		writeAction( obj, ImageItem.ACTION_PROP );

		writer.endElement( );
	}

	/**
	 * Writes the action structure.
	 * 
	 * @param obj
	 *            the element containing action structure
	 * @param propName
	 *            the property name of action structure
	 */

	protected void writeAction( DesignElement obj, String propName )
	{
		Action action = (Action) obj.getLocalProperty( getModule( ), propName );
		if ( action == null )
			return;
		writeAction( action, propName );
	}

	/**
	 * Write the action structure.
	 * 
	 * @param action
	 *            action structure instance.
	 * @param propName
	 *            the property name of action structure on the element.
	 */

	protected void writeAction( Action action, String propName )
	{
		String linkType = (String) action.getProperty( getModule( ),
				Action.LINK_TYPE_MEMBER );

		writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );
		writer.attribute( DesignElement.NAME_PROP, propName );

		property( action, Action.FORMAT_TYPE_MEMBER );
		property( action, Action.LINK_TYPE_MEMBER );

		if ( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK
				.equalsIgnoreCase( linkType ) )
		{
			property( action, Action.URI_MEMBER );
			property( action, Action.TARGET_WINDOW_MEMBER );
			property( action, Action.TARGET_FILE_TYPE_MEMBER );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK
				.equalsIgnoreCase( linkType ) )
		{
			property( action, Action.TARGET_BOOKMARK_MEMBER );
			property( action, Action.TARGET_BOOKMARK_TYPE_MEMBER );
		}
		else if ( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH
				.equalsIgnoreCase( linkType ) )
		{
			property( action, Action.REPORT_NAME_MEMBER );
			property( action, Action.TARGET_BOOKMARK_MEMBER );
			property( action, Action.TARGET_BOOKMARK_TYPE_MEMBER );
			property( action, Action.TARGET_WINDOW_MEMBER );
			property( action, Action.TARGET_FILE_TYPE_MEMBER );
			writeStructureList( action, Action.PARAM_BINDINGS_MEMBER );
			writeStructureList( action, Action.SEARCH_MEMBER );
		}
		else
		{
			assert false;
		}

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDesignElement(org.eclipse.birt.report.model.core.DesignElement)
	 */

	public void visitDesignElement( DesignElement obj )
	{
		super.visitDesignElement( obj );

		// The element name, id and extends should be written in the tag.

		attribute( obj, DesignSchemaConstants.NAME_ATTRIB,
				DesignElement.NAME_PROP );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		attribute( obj, DesignSchemaConstants.EXTENDS_ATTRIB,
				DesignElement.EXTENDS_PROP );
		attribute( obj, DesignSchemaConstants.VIEW_ACTION_ATTRIB,
				DesignElement.VIEW_ACTION_PROP );

		property( obj, DesignElement.COMMENTS_PROP );
		propertyCDATA( obj, DesignElement.CUSTOM_XML_PROP );

		resourceKey( obj, DesignElement.DISPLAY_NAME_ID_PROP,
				DesignElement.DISPLAY_NAME_PROP );

		property( obj, DesignElement.EVENT_HANDLER_CLASS_PROP );

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		writeStructureList( obj, DesignElement.PROPERTY_MASKS_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitSimpleDataSet(org.eclipse.birt.report.model.elements.SimpleDataSet)
	 */

	public void visitDataSet( DataSet obj )
	{
		super.visitDataSet( obj );

		writeStructureList( obj, DataSet.PARAMETERS_PROP );
		writeStructureList( obj, DataSet.RESULT_SET_PROP );
		writeStructureList( obj, DataSet.RESULT_SET_HINTS_PROP );
		writeStructureList( obj, DataSet.COMPUTED_COLUMNS_PROP );
		writeStructureList( obj, DataSet.COLUMN_HINTS_PROP );
		writeStructureList( obj, DataSet.FILTER_PROP );

		CachedMetaData metadata = (CachedMetaData) obj.getLocalProperty(
				getModule( ), DataSet.CACHED_METADATA_PROP );
		if ( metadata != null )
		{
			// Writing cached data set meta-data information.

			writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );
			writer.attribute( DesignElement.NAME_PROP,
					SimpleDataSet.CACHED_METADATA_PROP );

			writeStructureList( metadata, CachedMetaData.PARAMETERS_MEMBER );
			writeStructureList( metadata, CachedMetaData.RESULT_SET_MEMBER );

			writer.endElement( );

			// end of writing meta-data information.
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitSimpleDataSet(org.eclipse.birt.report.model.elements.SimpleDataSet)
	 */

	public void visitSimpleDataSet( SimpleDataSet obj )
	{
		super.visitSimpleDataSet( obj );

		property( obj, SimpleDataSet.DATA_SOURCE_PROP );

		property( obj, SimpleDataSet.BEFORE_OPEN_METHOD );
		property( obj, SimpleDataSet.BEFORE_CLOSE_METHOD );
		property( obj, SimpleDataSet.ON_FETCH_METHOD );
		property( obj, SimpleDataSet.AFTER_OPEN_METHOD );
		property( obj, SimpleDataSet.AFTER_CLOSE_METHOD );
		property( obj, SimpleDataSet.REF_TEMPLATE_PARAMETER_PROP );
		property( obj, SimpleDataSet.CACHED_ROW_COUNT_PROP );

		writeStructureList( obj, SimpleDataSet.PARAM_BINDINGS_PROP );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDataSource(org.eclipse.birt.report.model.elements.DataSource)
	 */

	public void visitDataSource( DataSource obj )
	{
		super.visitDataSource( obj );

		property( obj, DataSource.BEFORE_OPEN_METHOD );
		property( obj, DataSource.BEFORE_CLOSE_METHOD );
		property( obj, DataSource.AFTER_OPEN_METHOD );
		property( obj, DataSource.AFTER_CLOSE_METHOD );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitParameter(org.eclipse.birt.report.model.elements.Parameter)
	 */

	public void visitParameter( Parameter obj )
	{
		super.visitParameter( obj );

		property( obj, Parameter.HIDDEN_PROP );

		resourceKey( obj, Parameter.HELP_TEXT_KEY_PROP,
				Parameter.HELP_TEXT_PROP );
		property( obj, Parameter.VALIDATE_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitReportItem(org.eclipse.birt.report.model.elements.ReportItem)
	 */

	public void visitReportItem( ReportItem obj )
	{
		super.visitReportItem( obj );

		property( obj, ReportItem.X_PROP );
		property( obj, ReportItem.Y_PROP );
		property( obj, ReportItem.HEIGHT_PROP );
		property( obj, ReportItem.WIDTH_PROP );
		property( obj, ReportItem.DATA_SET_PROP );
		property( obj, ReportItem.REF_TEMPLATE_PARAMETER_PROP );

		writeStructureList( obj, ReportItem.VISIBILITY_PROP );
		writeStructureList( obj, ReportItem.PARAM_BINDINGS_PROP );
		writeStructureList( obj, ReportItem.BOUND_DATA_COLUMNS_PROP );

		property( obj, ReportItem.BOOKMARK_PROP );
		property( obj, ReportItem.TOC_PROP );

		property( obj, ReportItem.ON_PREPARE_METHOD );
		property( obj, ReportItem.ON_CREATE_METHOD );
		property( obj, ReportItem.ON_RENDER_METHOD );
		property( obj, ReportItem.ON_PAGE_BREAK_METHOD );

		writeOverridenPropertyValues( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitMasterPage(org.eclipse.birt.report.model.elements.MasterPage)
	 */

	public void visitMasterPage( MasterPage obj )
	{
		super.visitMasterPage( obj );

		property( obj, MasterPage.TYPE_PROP );

		// Only when type is custom, height and width can be output.

		String type = (String) obj.getLocalProperty( getModule( ),
				MasterPage.TYPE_PROP );
		if ( DesignChoiceConstants.PAGE_SIZE_CUSTOM.equalsIgnoreCase( type ) )
		{
			property( obj, MasterPage.HEIGHT_PROP );
			property( obj, MasterPage.WIDTH_PROP );
		}

		property( obj, MasterPage.ORIENTATION_PROP );
		property( obj, MasterPage.TOP_MARGIN_PROP );
		property( obj, MasterPage.LEFT_MARGIN_PROP );
		property( obj, MasterPage.BOTTOM_MARGIN_PROP );
		property( obj, MasterPage.RIGHT_MARGIN_PROP );

		writeStyle( obj );
		writeOverridenPropertyValues( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitStyledElement(org.eclipse.birt.report.model.core.StyledElement)
	 */

	public void visitStyledElement( StyledElement obj )
	{
		super.visitStyledElement( obj );

		writeStyle( obj );
	}

	/**
	 * Writes the style and style properties for styled element.
	 * 
	 * @param obj
	 *            the styled element
	 */

	private void writeStyle( StyledElement obj )
	{
		property( obj, ReportItem.STYLE_PROP );
		writeStyleProps( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitListing(org.eclipse.birt.report.model.elements.ListingElement)
	 */

	public void visitListing( ListingElement obj )
	{
		super.visitListing( obj );

		property( obj, ListingElement.REPEAT_HEADER_PROP );

		writeStructureList( obj, ListingElement.SORT_PROP );
		writeStructureList( obj, ListingElement.FILTER_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitGroup(org.eclipse.birt.report.model.elements.GroupElement)
	 */
	public void visitGroup( GroupElement obj )
	{
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		attribute( obj, DesignSchemaConstants.VIEW_ACTION_ATTRIB,
				DesignElement.VIEW_ACTION_PROP );

		super.visitGroup( obj );

		property( obj, GroupElement.GROUP_NAME_PROP );
		property( obj, GroupElement.INTERVAL_BASE_PROP );
		property( obj, GroupElement.INTERVAL_PROP );
		property( obj, GroupElement.INTERVAL_RANGE_PROP );
		property( obj, GroupElement.SORT_DIRECTION_PROP );
		property( obj, GroupElement.SORT_TYPE_PROP );
		property( obj, GroupElement.KEY_EXPR_PROP );
		property( obj, GroupElement.TOC_PROP );

		property( obj, GroupElement.EVENT_HANDLER_CLASS_PROP );
		property( obj, GroupElement.ON_PREPARE_METHOD );
		property( obj, GroupElement.ON_PAGE_BREAK_METHOD );
		property( obj, GroupElement.REPEAT_HEADER_PROP );
		property( obj, GroupElement.HIDE_DETAIL_PROP );

		property( obj, Style.PAGE_BREAK_AFTER_PROP );
		property( obj, Style.PAGE_BREAK_BEFORE_PROP );

		// write user property definitions and values

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		writeStructureList( obj, GroupElement.SORT_PROP );
		writeStructureList( obj, GroupElement.FILTER_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitExtendedDataSet(org.eclipse.birt.report.model.elements.ExtendedDataSet)
	 */

	public void visitOdaDataSet( OdaDataSet obj )
	{
		writer.startElement( DesignSchemaConstants.ODA_DATA_SET_TAG );
		attribute( obj, OdaDataSet.EXTENSION_ID_PROP,
				OdaDataSet.EXTENSION_ID_PROP );

		super.visitOdaDataSet( obj );

		if ( (String) obj.getLocalProperty( getModule( ),
				OdaDataSet.QUERY_TEXT_PROP ) != null )
		{
			property( obj, OdaDataSet.QUERY_TEXT_PROP );
		}

		property( obj, OdaDataSet.RESULT_SET_NAME_PROP );
		writeOdaDesignerState( obj, OdaDataSet.DESIGNER_STATE_PROP );

		List properties = (List) obj.getLocalProperty( getModule( ),
				OdaDataSet.PRIVATE_DRIVER_PROPERTIES_PROP );
		writeExtendedProperties( properties,
				OdaDataSet.PRIVATE_DRIVER_PROPERTIES_PROP );

		writeOdaExtensionProperties( obj, OdaDataSet.EXTENSION_ID_PROP );

		writer.endElement( );
	}

	/**
	 * Writes the columns slot of <code>GridItem</code> and
	 * <code>TableItem</code>.
	 * 
	 * @param obj
	 *            the grid item or table item
	 * @param slot
	 *            the columns slot
	 */

	private void writeColumns( DesignElement obj, int slot )
	{
		assert obj instanceof GridItem || obj instanceof TableItem;
		assert slot == GridItem.COLUMN_SLOT || slot == TableItem.COLUMN_SLOT;

		// TODO: UI requires the column to keep the table layout information, so
		// the unnecessary columns can not be remove this moment. The related
		// SCR is SRC#74095.

		boolean revert = true;
		if ( revert )
		{
			writeContents( obj, slot, null );
			return;
		}

		List list = obj.getSlot( slot ).getContents( );
		if ( list.isEmpty( ) )
			return;

		// If there is no column with any value, columns will not be written.

		boolean needWrite = false;

		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) && !needWrite )
		{
			DesignElement column = (DesignElement) iter.next( );
			List propDefns = column.getPropertyDefns( );

			Iterator iterDefn = propDefns.iterator( );
			while ( iterDefn.hasNext( ) && !needWrite )
			{
				PropertyDefn propDefn = (PropertyDefn) iterDefn.next( );
				if ( column
						.getLocalProperty( getModule( ), propDefn.getName( ) ) != null )
				{
					needWrite = true;
				}
			}
		}

		if ( needWrite )
		{
			// Iterate over the contents using this visitor to write each one.
			// Note that this may result in a recursive call back into this
			// method as we do a depth-first traversal of the design tree.

			iter = list.iterator( );
			while ( iter.hasNext( ) )
			{
				( (DesignElement) iter.next( ) ).apply( this );
			}
		}
	}

	/**
	 * Writes the values for user properties.
	 * 
	 * @param obj
	 *            the element that has user properties.
	 */

	protected void writeOverridenPropertyValues( DesignElement obj )
	{
		// if no extends do not write this part.

		if ( obj.getExtendsElement( ) == null )
			return;

		writer
				.conditionalStartElement( DesignSchemaConstants.OVERRIDDEN_VALUES_TAG );

		Iterator iter = new ContentIterator( obj );
		while ( iter.hasNext( ) ) // for each virtual element in the child
		{
			DesignElement virtualElement = (DesignElement) iter.next( );

			if ( !virtualElement.hasLocalPropertyValues( )
					&& virtualElement.getStyle( ) == null
					&& StringUtil.isBlank( virtualElement.getName( ) ) )
				continue;

			writer
					.conditionalStartElement( DesignSchemaConstants.REF_ENTRY_TAG );

			long baseId = virtualElement.getBaseId( );
			writer.attribute( DesignSchemaConstants.BASE_ID_ATTRIB, new Long(
					baseId ).toString( ) );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB, virtualElement
					.getName( ) );

			assert virtualElement.getExtendsElement( ) == null;

			List propDefns = null;
			if ( virtualElement instanceof ExtendedItem )
			{
				propDefns = ( (ExtendedItem) virtualElement ).getExtDefn( )
						.getProperties( );
			}
			else
			{
				propDefns = virtualElement.getPropertyDefns( );
			}

			for ( int i = 0; i < propDefns.size( ); i++ )
			{
				PropertyDefn propDefn = (PropertyDefn) propDefns.get( i );
				if ( DesignElement.NAME_PROP.equalsIgnoreCase( propDefn
						.getName( ) ) )
					continue;

				if ( virtualElement instanceof ExtendedItem
						&& ExtendedItem.EXTENSION_NAME_PROP
								.equalsIgnoreCase( propDefn.getName( ) ) )
					continue;

				boolean cdata = false;
				if ( propDefn.getTypeCode( ) == PropertyType.SCRIPT_TYPE
						|| propDefn.getTypeCode( ) == PropertyType.XML_TYPE )
					cdata = true;

				if ( propDefn.getTypeCode( ) == PropertyType.STRUCT_TYPE )
				{
					if ( propDefn.isList( ) )
						writeStructureList( virtualElement, propDefn.getName( ) );
					else
						writeStructure( virtualElement, propDefn.getName( ) );
				}
				else if ( propDefn.getTypeCode( ) == PropertyType.LIST_TYPE )
					writeSimplePropertyList( virtualElement, propDefn.getName( ) );
				else
					writeProperty( virtualElement,
							getTagByPropertyType( propDefn ), propDefn
									.getName( ), cdata );
			}

			writer.endElement( ); // end ��ref-entry��

		}

		writer.endElement( ); // end ��Overridden-values��

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitJointDataSet(org.eclipse.birt.report.model.elements.JointDataSet)
	 */

	public void visitJointDataSet( JointDataSet obj )
	{

		writer.startElement( DesignSchemaConstants.JOINT_DATA_SET_TAG );
		super.visitJointDataSet( obj );

		writeSimplePropertyList( obj, JointDataSet.DATA_SETS_PROP );

		writeStructureList( obj, JointDataSet.JOIN_CONDITONS_PROP );
		writer.endElement( );
	}

	/**
	 * Visits the designer state of the oda data set.
	 * 
	 * @param obj
	 *            the oda data set to traverse
	 */

	private void writeOdaDesignerState( DesignElement obj, String propName )
	{
		OdaDesignerState designerState = (OdaDesignerState) obj
				.getLocalProperty( getModule( ), propName );

		if ( designerState == null )
			return;

		writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );
		writer.attribute( DesignElement.NAME_PROP,
				OdaDataSet.DESIGNER_STATE_PROP );

		property( designerState, OdaDesignerState.VERSION_MEMBER );
		property( designerState, OdaDesignerState.CONTENT_AS_STRING_MEMBER );

		try
		{
			if ( designerState.getContentAsBlob( ) != null )
			{
				byte[] data = base.encode( designerState.getContentAsBlob( ) );
				String value = new String( data, OdaDesignerState.CHARSET );

				if ( value.length( ) < IndentableXMLWriter.MAX_CHARS_PER_LINE )
					writeEntry( DesignSchemaConstants.PROPERTY_TAG,
							OdaDesignerState.CONTENT_AS_BLOB_MEMBER, value,
							false );
				else
					writeLongIndentText( DesignSchemaConstants.PROPERTY_TAG,
							OdaDesignerState.CONTENT_AS_BLOB_MEMBER, value );
			}
		}
		catch ( UnsupportedEncodingException e )
		{
			assert false;
		}
		writer.endElement( );
	}
}