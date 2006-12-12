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
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.api.core.IModuleModel;
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
import org.eclipse.birt.report.model.api.elements.structures.StyleRule;
import org.eclipse.birt.report.model.api.extension.IEncryptionHelper;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.UserChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.ReferencableStructure;
import org.eclipse.birt.report.model.core.Structure;
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
import org.eclipse.birt.report.model.elements.interfaces.IAutoTextModel;
import org.eclipse.birt.report.model.elements.interfaces.ICascadingParameterGroupModel;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.IDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IDataSourceModel;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IFreeFormModel;
import org.eclipse.birt.report.model.elements.interfaces.IGraphicMaterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.IGridItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IJointDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;
import org.eclipse.birt.report.model.elements.interfaces.ILineItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMasterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSourceModel;
import org.eclipse.birt.report.model.elements.interfaces.IOdaExtendableElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IParameterGroupModel;
import org.eclipse.birt.report.model.elements.interfaces.IParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.birt.report.model.elements.interfaces.IScriptDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.IScriptDataSourceModel;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleDataSetModel;
import org.eclipse.birt.report.model.elements.interfaces.ISimpleMasterPageModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableColumnModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.elements.interfaces.ITemplateParameterDefinitionModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextDataItemModel;
import org.eclipse.birt.report.model.elements.interfaces.ITextItemModel;
import org.eclipse.birt.report.model.extension.oda.ODAProvider;
import org.eclipse.birt.report.model.extension.oda.OdaDummyProvider;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.SlotDefn;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.parser.treebuild.ContentNode;
import org.eclipse.birt.report.model.parser.treebuild.ContentTree;
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
	 * Control flag indicating whether need mark line number.
	 */

	protected boolean markLineNumber = true;

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
		// initialize control flag
		ModuleOption options = getModule( ).getOptions( );
		if ( options != null )
			markLineNumber = options.markLineNumber( );

		writer = new IndentableXMLWriter( outputFile, getModule( )
				.getUTFSignature( ), markLineNumber );
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
		// initialize control flag
		ModuleOption options = getModule( ).getOptions( );
		if ( options != null )
			markLineNumber = options.markLineNumber( );

		writer = new IndentableXMLWriter( os, getModule( ).getUTFSignature( ),
				markLineNumber );
		writeFile( );
	}

	/**
	 * Implementation method to write the file header and contents.
	 */

	private void writeFile( )
	{
		boundColumnsMgr = new BoundColumnsWriterMgr( getModule( )
				.getVersionManager( ).getVersion( ) );

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
		assert prop.getTypeCode( ) != IPropertyType.STRUCT_TYPE;

		switch ( prop.getTypeCode( ) )
		{
			case IPropertyType.EXPRESSION_TYPE :
				return DesignSchemaConstants.EXPRESSION_TAG;

			case IPropertyType.XML_TYPE :
				return DesignSchemaConstants.XML_PROPERTY_TAG;

			case IPropertyType.SCRIPT_TYPE :
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

		if ( nameProp.getTypeCode( ) == IPropertyType.HTML_TYPE )
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

		StructPropertyDefn keyProp = (StructPropertyDefn) structDefn
				.getMember( resourceKey );
		assert keyProp != null;

		Object value = null;
		Object keyValue = null;

		if ( struct instanceof Structure )
		{
			value = ( (Structure) struct ).getLocalProperty( getModule( ),
					nameProp );
			keyValue = ( (Structure) struct ).getLocalProperty( getModule( ),
					keyProp );
		}
		else
		{
			assert struct instanceof UserPropertyDefn;

			value = ( (UserPropertyDefn) struct ).getLocalProperty(
					getModule( ), nameProp );
			keyValue = ( (UserPropertyDefn) struct ).getLocalProperty(
					getModule( ), keyProp );
		}
		String xml = nameProp.getXmlValue( getModule( ), value );
		String xmlKey = keyProp.getXmlValue( getModule( ), keyValue );

		if ( xmlKey == null && xml == null )
			return;

		if ( nameProp.getTypeCode( ) == IPropertyType.HTML_TYPE )
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

		if ( propDefn.getTypeCode( ) == IPropertyType.SCRIPT_TYPE )
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

		Object value = null;
		if ( struct instanceof Structure )
			value = ( (Structure) struct ).getLocalProperty( getModule( ),
					propDefn );
		else
		{
			assert struct instanceof UserPropertyDefn;
			value = ( (UserPropertyDefn) struct ).getLocalProperty(
					getModule( ), propDefn );
		}
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

		if ( propDefn.getTypeCode( ) == IPropertyType.SCRIPT_TYPE )
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
		assert prop.getTypeCode( ) == IPropertyType.STRUCT_TYPE
				&& prop.isList( );

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

			// for example: TOC structure contains StringFormat,DateTimeFormat
			// structure.

			writeMember( struct, strcutPropDefn );
		}

		writer.endElement( );
	}

	/**
	 * Write property or structure according to definition.
	 * 
	 * @param struct
	 *            parent structure.
	 * @param propDefn
	 *            property definition.
	 */

	private void writeMember( IStructure struct, PropertyDefn propDefn )
	{
		// the member of the structure list may be the
		// structure/structure list again.

		if ( propDefn.getTypeCode( ) == IPropertyType.STRUCT_TYPE )
		{
			if ( propDefn.isList( ) )
				writeStructureList( struct, propDefn.getName( ) );
			else
				writeStructure( struct, propDefn.getName( ) );
		}
		else
			property( struct, propDefn.getName( ) );
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
		if ( prop == null || prop.getTypeCode( ) != IPropertyType.LIST_TYPE )
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

	private void writeStructure( IStructure struct, String memberName )
	{
		IStructureDefn structDefn = struct.getDefn( ).getMember( memberName )
				.getStructDefn( );

		assert struct instanceof Structure;

		IStructure memberStruct = (IStructure) ( (Structure) struct )
				.getLocalProperty( null, (PropertyDefn) struct.getDefn( )
						.getMember( memberName ) );

		if ( memberStruct == null )
			return;

		writer.conditionalStartElement( DesignSchemaConstants.STRUCTURE_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, memberName );

		Iterator iter = structDefn.propertiesIterator( );
		while ( iter.hasNext( ) )
		{
			StructPropertyDefn strcutPropDefn = (StructPropertyDefn) iter
					.next( );

			// for example: TOC structure contains StringFormat,DateTimeFormat
			// structure.

			writeMember( memberStruct, strcutPropDefn );
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
		assert prop.getTypeCode( ) == IPropertyType.STRUCT_TYPE
				&& prop.isList( );

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

				// for example: highlightrule structure contains
				// StringFormat,DateTimeFormat
				// structure.

				writeMember( struct, memberDefn );
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

	private void writeStructureList( IStructure obj, String memberName )
	{
		PropertyDefn prop = (PropertyDefn) obj.getDefn( )
				.getMember( memberName );
		assert prop != null;
		assert prop.getTypeCode( ) == IPropertyType.STRUCT_TYPE
				&& prop.isList( );

		assert obj instanceof Structure;
		List list = (List) ( (Structure) obj ).getLocalProperty( getModule( ),
				prop );
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

				// for example: highlightrule structure contains
				// StringFormat,DateTimeFormat
				// structure.

				writeMember( struct, memberDefn );
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
				IDesignElementModel.USER_PROPERTIES_PROP );

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
							Choice.NAME_PROP, choice.getName( ), false );

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

		if ( markLineNumber )
			obj.initLineNoMap( );

		writer.attribute( DesignSchemaConstants.XMLNS_ATTRIB,
				DEFAULT_NAME_SPACE );
		writer.attribute( DesignSchemaConstants.VERSION_ATTRIB,
				DesignSchemaConstants.REPORT_VERSION );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		property( obj, IModuleModel.AUTHOR_PROP );
		property( obj, IModuleModel.HELP_GUIDE_PROP );
		property( obj, IModuleModel.CREATED_BY_PROP );
		property( obj, IModuleModel.UNITS_PROP );
		property( obj, IModuleModel.BASE_PROP );
		property( obj, IModuleModel.INCLUDE_RESOURCE_PROP );

		resourceKey( obj, IModuleModel.TITLE_ID_PROP, IModuleModel.TITLE_PROP );
		property( obj, IDesignElementModel.COMMENTS_PROP );

		resourceKey( obj, IModuleModel.DESCRIPTION_ID_PROP,
				IModuleModel.DESCRIPTION_PROP );

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		// write property bindings

		writeStructureList( obj, IModuleModel.PROPERTY_BINDINGS_PROP );

		// write script libs

		writeStructureList( obj, IModuleModel.SCRIPTLIBS_PROP );
	}

	/**
	 * Visits the embedded images of the module.
	 * 
	 * @param obj
	 *            the module to traverse
	 */

	protected void writeEmbeddedImages( Module obj )
	{
		List list = (List) obj.getLocalProperty( obj, IModuleModel.IMAGES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					IModuleModel.IMAGES_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				EmbeddedImage image = (EmbeddedImage) list.get( i );
				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

				property( image, EmbeddedImage.NAME_MEMBER );
				property( image, EmbeddedImage.TYPE_MEMBER );
				property( image, ReferencableStructure.LIB_REFERENCE_MEMBER );

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
		List list = (List) obj.getLocalProperty( obj,
				IModuleModel.COLOR_PALETTE_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					IModuleModel.COLOR_PALETTE_PROP );

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

		property( obj, IScriptDataSourceModel.OPEN_METHOD );
		property( obj, IScriptDataSourceModel.CLOSE_METHOD );

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
				IOdaExtendableElementModel.EXTENSION_ID_PROP );

		super.visitOdaDataSource( obj );

		writeOdaDesignerState( obj, IOdaDataSourceModel.DESIGNER_STATE_PROP );

		List properties = (List) obj.getLocalProperty( getModule( ),
				IOdaDataSourceModel.PRIVATE_DRIVER_PROPERTIES_PROP );
		writeExtendedProperties( properties,
				IOdaDataSourceModel.PRIVATE_DRIVER_PROPERTIES_PROP );

		writeOdaExtensionProperties( obj,
				IOdaExtendableElementModel.EXTENSION_ID_PROP );

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
		{
			ODAProvider provider = null;

			if ( obj instanceof OdaDataSource )
				provider = ( (OdaDataSource) obj ).getProvider( );
			if ( obj instanceof OdaDataSet )
				provider = ( (OdaDataSet) obj ).getProvider( );

			if ( provider instanceof OdaDummyProvider )
				writeOdaDummyProperties( obj, provider );

			return;

		}

		List list = extDefn.getLocalProperties( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			PropertyDefn prop = (PropertyDefn) list.get( i );
			if ( extensionIDProp.equals( prop.getName( ) ) )
				continue;

			Object value = obj.getLocalProperty( getModule( ), prop.getName( ) );
			if ( value != null )
			{
				boolean cdata = false;

				if ( prop.getTypeCode( ) == IPropertyType.XML_TYPE
						|| prop.getTypeCode( ) == IPropertyType.SCRIPT_TYPE )
					cdata = true;
				writeProperty( obj, getTagByPropertyType( prop ), prop
						.getName( ), cdata );
			}
		}
	}

	/**
	 * Writes ODA exension properties.
	 * 
	 * @param obj
	 *            the ODA element to write
	 * @param provider
	 *            the extension provider
	 */

	private void writeOdaDummyProperties( DesignElement obj,
			ODAProvider provider )
	{

		assert provider instanceof OdaDummyProvider;

		OdaDummyProvider dummyProvider = (OdaDummyProvider) provider;

		// write other un-organized strings

		ContentTree tree = dummyProvider.getContentTree( );
		writeContentTree( tree );
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

		writeStructureList( obj, IDataSetModel.PARAMETERS_PROP );
		property( obj, IScriptDataSetModel.OPEN_METHOD );
		property( obj, IScriptDataSetModel.DESCRIBE_METHOD );
		property( obj, IScriptDataSetModel.FETCH_METHOD );
		property( obj, IScriptDataSetModel.CLOSE_METHOD );

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

		writeContents( obj, IFreeFormModel.REPORT_ITEMS_SLOT,
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

		property( obj, IDataItemModel.RESULT_SET_COLUMN_PROP );

		resourceKey( obj, IDataItemModel.HELP_TEXT_KEY_PROP,
				IDataItemModel.HELP_TEXT_PROP );

		writeAction( obj, IDataItemModel.ACTION_PROP );

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

		property( obj, ITextDataItemModel.VALUE_EXPR_PROP );
		property( obj, ITextDataItemModel.CONTENT_TYPE_PROP );

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

		ExtensionElementDefn extDefn = obj.getExtDefn( );
		if ( extDefn == null )
		{
			attribute( obj, DesignSchemaConstants.EXTENSION_NAME_ATTRIB,
					IExtendedItemModel.EXTENSION_NAME_PROP );

			super.visitExtendedItem( obj );

			resourceKey( obj, IExtendedItemModel.ALT_TEXT_KEY_PROP,
					IExtendedItemModel.ALT_TEXT_PROP );

			// write filter properties for the extended item

			writeStructureList( obj, IExtendedItemModel.FILTER_PROP );

			// write other un-organized strings
			ContentTree tree = obj.getContentTree( );
			writeContentTree( tree );
		}
		else
		{
			// write some attributes
			attribute( obj, DesignSchemaConstants.EXTENSION_NAME_ATTRIB,
					IExtendedItemModel.EXTENSION_NAME_PROP );
			String name = (String) obj.getLocalProperty( getModule( ),
					IDesignElementModel.NAME_PROP );
			if ( !StringUtil.isBlank( name ) )
				writer.attribute( DesignSchemaConstants.NAME_ATTRIB, name );
			String extendsFrom = obj.getExtendsName( );
			if ( !StringUtil.isBlank( extendsFrom ) )
				writer.attribute( DesignSchemaConstants.EXTENDS_ATTRIB,
						extendsFrom );
			writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
					.getID( ) ).toString( ) );

			// write all other properties
			List props = extDefn.getProperties( );
			for ( int i = 0; i < props.size( ); i++ )
			{
				PropertyDefn prop = (PropertyDefn) props.get( i );
				String propName = prop.getName( );
				if ( IDesignElementModel.NAME_PROP.equals( propName )
						|| IExtendedItemModel.EXTENSION_NAME_PROP
								.equals( propName )
						|| IDesignElementModel.EXTENDS_PROP.equals( propName ) )
					continue;

				// TODO: support extending those xml properties.
				// Now, each time a child is initialized, its xml-properties are
				// serialized on the IReportItem itself, never minding whether
				// the xml-property values are set locally or extended from
				// parent.
				switch ( prop.getTypeCode( ) )
				{
					case IPropertyType.LIST_TYPE :
						writeSimplePropertyList( obj, propName );
						break;
					case IPropertyType.XML_TYPE :
						writeProperty( obj, getTagByPropertyType( prop ),
								propName, true );
						break;
					case IPropertyType.STRUCT_TYPE :
						if ( prop.isList( ) )
							writeStructureList( obj, propName );
						else
							writeStructure( obj, propName );
						break;
					default :
						writeProperty( obj, getTagByPropertyType( prop ), prop
								.getName( ), false );
						break;
				}
			}

			// write the slot content
			if ( extDefn.isContainer( ) )
			{
				for ( int i = 0; i < extDefn.getSlotCount( ); i++ )
				{
					SlotDefn slotDefn = (SlotDefn) extDefn.getSlot( i );
					writeContents( obj, i, slotDefn.getXmlName( ) );
				}
			}

		}

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

		property( obj, ITextItemModel.CONTENT_TYPE_PROP );
		resourceKeyCDATA( obj, ITextItemModel.CONTENT_RESOURCE_KEY_PROP,
				ITextItemModel.CONTENT_PROP );

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

		resourceKey( obj, ILabelModel.TEXT_ID_PROP, ILabelModel.TEXT_PROP );
		resourceKey( obj, ILabelModel.HELP_TEXT_ID_PROP,
				ILabelModel.HELP_TEXT_PROP );

		writeAction( obj, ILabelModel.ACTION_PROP );

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

		property( obj, IAutoTextModel.AUTOTEXT_TYPE_PROP );

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

		writeContents( obj, IListingElementModel.HEADER_SLOT,
				DesignSchemaConstants.HEADER_TAG );

		// There is no groups tag for this slot. All groups are written under
		// list tag.

		writeContents( obj, IListingElementModel.GROUP_SLOT, null );
		writeContents( obj, IListingElementModel.DETAIL_SLOT,
				DesignSchemaConstants.DETAIL_TAG );
		writeContents( obj, IListingElementModel.FOOTER_SLOT,
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

		writeContents( obj, IGroupElementModel.HEADER_SLOT,
				DesignSchemaConstants.HEADER_TAG );
		writeContents( obj, IGroupElementModel.FOOTER_SLOT,
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

		resourceKey( obj, ITableItemModel.CAPTION_KEY_PROP,
				ITableItemModel.CAPTION_PROP );

		// There is no columns tag for this slot. All columns are written under
		// table tag.

		writeColumns( obj, ITableItemModel.COLUMN_SLOT );

		writeContents( obj, IListingElementModel.HEADER_SLOT,
				DesignSchemaConstants.HEADER_TAG );

		// There is no groups tag for this slot. All groups are written under
		// table tag.

		writeContents( obj, IListingElementModel.GROUP_SLOT, null );

		writeContents( obj, IListingElementModel.DETAIL_SLOT,
				DesignSchemaConstants.DETAIL_TAG );
		writeContents( obj, IListingElementModel.FOOTER_SLOT,
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

		writeContents( obj, IGroupElementModel.HEADER_SLOT,
				DesignSchemaConstants.HEADER_TAG );
		writeContents( obj, IGroupElementModel.FOOTER_SLOT,
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
				IDesignElementModel.VIEW_ACTION_PROP );

		super.visitColumn( obj );

		property( obj, ITableColumnModel.WIDTH_PROP );
		property( obj, ITableColumnModel.REPEAT_PROP );
		property( obj, ITableColumnModel.SUPPRESS_DUPLICATES_PROP );
		writeStructureList( obj, ITableColumnModel.VISIBILITY_PROP );

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
				IDesignElementModel.VIEW_ACTION_PROP );

		super.visitRow( obj );

		property( obj, ITableRowModel.HEIGHT_PROP );
		property( obj, ITableRowModel.BOOKMARK_PROP );
		property( obj, ITableRowModel.SUPPRESS_DUPLICATES_PROP );

		property( obj, IDesignElementModel.EVENT_HANDLER_CLASS_PROP );
		property( obj, ITableRowModel.ON_PREPARE_METHOD );
		property( obj, ITableRowModel.ON_CREATE_METHOD );
		property( obj, ITableRowModel.ON_RENDER_METHOD );

		// write user property definitions and values

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		writeStyle( obj );
		writeStructureList( obj, ITableRowModel.VISIBILITY_PROP );

		writeContents( obj, ITableRowModel.CONTENT_SLOT, null );

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
				IDesignElementModel.VIEW_ACTION_PROP );

		super.visitCell( obj );

		property( obj, ICellModel.COLUMN_PROP );
		property( obj, ICellModel.COL_SPAN_PROP );
		property( obj, ICellModel.ROW_SPAN_PROP );
		property( obj, ICellModel.DROP_PROP );
		property( obj, ICellModel.HEIGHT_PROP );
		property( obj, ICellModel.WIDTH_PROP );
		property( obj, IDesignElementModel.EVENT_HANDLER_CLASS_PROP );
		property( obj, ICellModel.ON_PREPARE_METHOD );
		property( obj, ICellModel.ON_CREATE_METHOD );
		property( obj, ICellModel.ON_RENDER_METHOD );

		writeStyle( obj );

		writeContents( obj, ICellModel.CONTENT_SLOT, null );

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

		writeColumns( obj, IGridItemModel.COLUMN_SLOT );
		writeContents( obj, IGridItemModel.ROW_SLOT, null );

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

		property( obj, ILineItemModel.ORIENTATION_PROP );

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

		property( obj, IGraphicMaterPageModel.COLUMNS_PROP );
		property( obj, IGraphicMaterPageModel.COLUMN_SPACING_PROP );

		writeContents( obj, IGraphicMaterPageModel.CONTENT_SLOT,
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

		property( obj, ISimpleMasterPageModel.SHOW_HEADER_ON_FIRST_PROP );
		property( obj, ISimpleMasterPageModel.SHOW_FOOTER_ON_LAST_PROP );
		property( obj, ISimpleMasterPageModel.FLOATING_FOOTER );
		property( obj, ISimpleMasterPageModel.HEADER_HEIGHT_PROP );
		property( obj, ISimpleMasterPageModel.FOOTER_HEIGHT_PROP );

		writeContents( obj, ISimpleMasterPageModel.PAGE_HEADER_SLOT,
				DesignSchemaConstants.PAGE_HEADER_TAG );
		writeContents( obj, ISimpleMasterPageModel.PAGE_FOOTER_SLOT,
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

		property( obj, IParameterGroupModel.START_EXPANDED_PROP );

		resourceKey( obj, IParameterGroupModel.HELP_TEXT_KEY_PROP,
				IParameterGroupModel.HELP_TEXT_PROP );

		writeContents( obj, IParameterGroupModel.PARAMETERS_SLOT,
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

		property( obj, IParameterGroupModel.START_EXPANDED_PROP );
		resourceKey( obj, IParameterGroupModel.HELP_TEXT_KEY_PROP,
				IParameterGroupModel.HELP_TEXT_PROP );
		property( obj, ICascadingParameterGroupModel.DATA_SET_PROP );
		property( obj, ICascadingParameterGroupModel.PROMPT_TEXT_PROP );
		property( obj, ICascadingParameterGroupModel.DATA_SET_MODE_PROP );

		writeContents( obj, IParameterGroupModel.PARAMETERS_SLOT,
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

		property( obj, IScalarParameterModel.VALUE_TYPE_PROP );
		property( obj, IScalarParameterModel.DATA_TYPE_PROP );
		resourceKey( obj, IScalarParameterModel.PROMPT_TEXT_ID_PROP,
				IScalarParameterModel.PROMPT_TEXT_PROP );
		property( obj, IScalarParameterModel.LIST_LIMIT_PROP );
		property( obj, IScalarParameterModel.CONCEAL_VALUE_PROP );
		property( obj, IScalarParameterModel.ALLOW_BLANK_PROP );
		property( obj, IScalarParameterModel.ALLOW_NULL_PROP );
		property( obj, IScalarParameterModel.CONTROL_TYPE_PROP );
		property( obj, IScalarParameterModel.ALIGNMENT_PROP );
		property( obj, IScalarParameterModel.DATASET_NAME_PROP );
		property( obj, IScalarParameterModel.VALUE_EXPR_PROP );
		property( obj, IScalarParameterModel.LABEL_EXPR_PROP );
		property( obj, IScalarParameterModel.MUCH_MATCH_PROP );
		property( obj, IScalarParameterModel.FIXED_ORDER_PROP );
		property( obj, IScalarParameterModel.DEFAULT_VALUE_PROP );

		writeStructure( obj, IScalarParameterModel.FORMAT_PROP );
		writeStructureList( obj, IScalarParameterModel.SELECTION_LIST_PROP );
		writeStructureList( obj, IReportItemModel.BOUND_DATA_COLUMNS_PROP );

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
				IDesignElementModel.NAME_PROP );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		resourceKey( obj, IDesignElementModel.DISPLAY_NAME_ID_PROP,
				IDesignElementModel.DISPLAY_NAME_PROP );

		property( obj, IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP );
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
				IDesignElementModel.NAME_PROP );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );

		property( obj, ITemplateParameterDefinitionModel.ALLOWED_TYPE_PROP );
		resourceKey( obj,
				ITemplateParameterDefinitionModel.DESCRIPTION_ID_PROP,
				ITemplateParameterDefinitionModel.DESCRIPTION_PROP );

		writeContents( obj, ITemplateParameterDefinitionModel.DEFAULT_SLOT,
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

		property( obj, IStyleModel.BACKGROUND_ATTACHMENT_PROP );
		property( obj, IStyleModel.BACKGROUND_COLOR_PROP );
		property( obj, IStyleModel.BACKGROUND_IMAGE_PROP );
		property( obj, IStyleModel.BACKGROUND_POSITION_X_PROP );
		property( obj, IStyleModel.BACKGROUND_POSITION_Y_PROP );
		property( obj, IStyleModel.BACKGROUND_REPEAT_PROP );

		// Font

		property( obj, IStyleModel.FONT_FAMILY_PROP );
		property( obj, IStyleModel.FONT_SIZE_PROP );
		property( obj, IStyleModel.FONT_WEIGHT_PROP );
		property( obj, IStyleModel.FONT_STYLE_PROP );
		property( obj, IStyleModel.FONT_VARIANT_PROP );
		property( obj, IStyleModel.COLOR_PROP );
		property( obj, IStyleModel.TEXT_LINE_THROUGH_PROP );
		property( obj, IStyleModel.TEXT_OVERLINE_PROP );
		property( obj, IStyleModel.TEXT_UNDERLINE_PROP );

		// Border

		property( obj, IStyleModel.BORDER_BOTTOM_COLOR_PROP );
		property( obj, IStyleModel.BORDER_BOTTOM_STYLE_PROP );
		property( obj, IStyleModel.BORDER_BOTTOM_WIDTH_PROP );
		property( obj, IStyleModel.BORDER_LEFT_COLOR_PROP );
		property( obj, IStyleModel.BORDER_LEFT_STYLE_PROP );
		property( obj, IStyleModel.BORDER_LEFT_WIDTH_PROP );
		property( obj, IStyleModel.BORDER_RIGHT_COLOR_PROP );
		property( obj, IStyleModel.BORDER_RIGHT_STYLE_PROP );
		property( obj, IStyleModel.BORDER_RIGHT_WIDTH_PROP );
		property( obj, IStyleModel.BORDER_TOP_COLOR_PROP );
		property( obj, IStyleModel.BORDER_TOP_STYLE_PROP );
		property( obj, IStyleModel.BORDER_TOP_WIDTH_PROP );

		// Margin

		property( obj, IStyleModel.MARGIN_TOP_PROP );
		property( obj, IStyleModel.MARGIN_LEFT_PROP );
		property( obj, IStyleModel.MARGIN_BOTTOM_PROP );
		property( obj, IStyleModel.MARGIN_RIGHT_PROP );

		// Padding

		property( obj, IStyleModel.PADDING_TOP_PROP );
		property( obj, IStyleModel.PADDING_LEFT_PROP );
		property( obj, IStyleModel.PADDING_BOTTOM_PROP );
		property( obj, IStyleModel.PADDING_RIGHT_PROP );

		// Formats

		property( obj, IStyleModel.NUMBER_ALIGN_PROP );

		writeStructure( obj, IStyleModel.DATE_TIME_FORMAT_PROP );
		writeStructure( obj, IStyleModel.NUMBER_FORMAT_PROP );
		writeStructure( obj, IStyleModel.STRING_FORMAT_PROP );

		// Text format

		property( obj, IStyleModel.TEXT_ALIGN_PROP );
		property( obj, IStyleModel.TEXT_INDENT_PROP );
		property( obj, IStyleModel.LETTER_SPACING_PROP );
		property( obj, IStyleModel.LINE_HEIGHT_PROP );
		property( obj, IStyleModel.ORPHANS_PROP );
		property( obj, IStyleModel.TEXT_TRANSFORM_PROP );
		property( obj, IStyleModel.VERTICAL_ALIGN_PROP );
		property( obj, IStyleModel.WHITE_SPACE_PROP );
		property( obj, IStyleModel.WIDOWS_PROP );
		property( obj, IStyleModel.WORD_SPACING_PROP );

		// Section Options

		property( obj, IStyleModel.DISPLAY_PROP );
		property( obj, IStyleModel.MASTER_PAGE_PROP );
		property( obj, IStyleModel.PAGE_BREAK_AFTER_PROP );
		property( obj, IStyleModel.PAGE_BREAK_BEFORE_PROP );
		property( obj, IStyleModel.PAGE_BREAK_INSIDE_PROP );
		property( obj, IStyleModel.SHOW_IF_BLANK_PROP );
		property( obj, IStyleModel.CAN_SHRINK_PROP );

		// Highlight

		List list = (ArrayList) obj.getLocalProperty( getModule( ),
				IStyleModel.HIGHLIGHT_RULES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					IStyleModel.HIGHLIGHT_RULES_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				HighlightRule rule = (HighlightRule) list.get( i );
				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

				property( rule, StyleRule.OPERATOR_MEMBER );

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

				property( rule, StyleRule.TEST_EXPR_MEMBER );
				property( rule, StyleRule.VALUE1_MEMBER );
				property( rule, StyleRule.VALUE2_MEMBER );

				writer.endElement( );
			}
			writer.endElement( );
		}

		// Map

		list = (ArrayList) obj.getLocalProperty( getModule( ),
				IStyleModel.MAP_RULES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					IStyleModel.MAP_RULES_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				MapRule rule = (MapRule) list.get( i );
				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

				property( rule, StyleRule.TEST_EXPR_MEMBER );
				property( rule, StyleRule.OPERATOR_MEMBER );
				property( rule, StyleRule.VALUE1_MEMBER );
				property( rule, StyleRule.VALUE2_MEMBER );

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

		property( obj, IImageItemModel.SIZE_PROP );
		property( obj, IImageItemModel.SCALE_PROP );
		property( obj, IImageItemModel.SOURCE_PROP );

		String source = (String) obj.getLocalProperty( getModule( ),
				IImageItemModel.SOURCE_PROP );

		if ( DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase( source )
				|| DesignChoiceConstants.IMAGE_REF_TYPE_FILE
						.equalsIgnoreCase( source ) )
		{
			property( obj, IImageItemModel.URI_PROP );
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_EMBED
				.equalsIgnoreCase( source ) )
		{
			property( obj, IImageItemModel.IMAGE_NAME_PROP );
		}
		else if ( DesignChoiceConstants.IMAGE_REF_TYPE_EXPR
				.equalsIgnoreCase( source ) )
		{
			property( obj, IImageItemModel.TYPE_EXPR_PROP );
			property( obj, IImageItemModel.VALUE_EXPR_PROP );
		}

		resourceKey( obj, IImageItemModel.ALT_TEXT_KEY_PROP,
				IImageItemModel.ALT_TEXT_PROP );
		resourceKey( obj, IImageItemModel.HELP_TEXT_ID_PROP,
				IImageItemModel.HELP_TEXT_PROP );

		writeAction( obj, IImageItemModel.ACTION_PROP );

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
		writer.attribute( IDesignElementModel.NAME_PROP, propName );

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

		if ( markLineNumber )
		{
			Module module = getModule( );
			if ( module != null )
				module
						.addElementLineNo( obj.getID( ), writer
								.getLineCounter( ) );
		}

		// The element name, id and extends should be written in the tag.

		attribute( obj, DesignSchemaConstants.NAME_ATTRIB,
				IDesignElementModel.NAME_PROP );
		writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long( obj
				.getID( ) ).toString( ) );
		attribute( obj, DesignSchemaConstants.EXTENDS_ATTRIB,
				IDesignElementModel.EXTENDS_PROP );
		attribute( obj, DesignSchemaConstants.VIEW_ACTION_ATTRIB,
				IDesignElementModel.VIEW_ACTION_PROP );

		property( obj, IDesignElementModel.COMMENTS_PROP );
		propertyCDATA( obj, IDesignElementModel.CUSTOM_XML_PROP );

		resourceKey( obj, IDesignElementModel.DISPLAY_NAME_ID_PROP,
				IDesignElementModel.DISPLAY_NAME_PROP );

		property( obj, IDesignElementModel.EVENT_HANDLER_CLASS_PROP );

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		writeStructureList( obj, IDesignElementModel.PROPERTY_MASKS_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitSimpleDataSet(org.eclipse.birt.report.model.elements.SimpleDataSet)
	 */

	public void visitDataSet( DataSet obj )
	{
		super.visitDataSet( obj );

		writeStructureList( obj, IDataSetModel.RESULT_SET_HINTS_PROP );
		writeStructureList( obj, IDataSetModel.COMPUTED_COLUMNS_PROP );
		writeStructureList( obj, IDataSetModel.COLUMN_HINTS_PROP );
		writeStructureList( obj, IDataSetModel.FILTER_PROP );

		CachedMetaData metadata = (CachedMetaData) obj.getLocalProperty(
				getModule( ), IDataSetModel.CACHED_METADATA_PROP );
		if ( metadata != null )
		{
			// Writing cached data set meta-data information.

			writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );
			writer.attribute( IDesignElementModel.NAME_PROP,
					IDataSetModel.CACHED_METADATA_PROP );

			writeStructureList( metadata, CachedMetaData.PARAMETERS_MEMBER );
			writeStructureList( metadata, CachedMetaData.RESULT_SET_MEMBER );

			writer.endElement( );

			// end of writing meta-data information.
		}

		property( obj, IDataSetModel.ROW_FETCH_LIMIT_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitSimpleDataSet(org.eclipse.birt.report.model.elements.SimpleDataSet)
	 */

	public void visitSimpleDataSet( SimpleDataSet obj )
	{
		super.visitSimpleDataSet( obj );

		property( obj, ISimpleDataSetModel.DATA_SOURCE_PROP );

		property( obj, ISimpleDataSetModel.BEFORE_OPEN_METHOD );
		property( obj, ISimpleDataSetModel.BEFORE_CLOSE_METHOD );
		property( obj, ISimpleDataSetModel.ON_FETCH_METHOD );
		property( obj, ISimpleDataSetModel.AFTER_OPEN_METHOD );
		property( obj, ISimpleDataSetModel.AFTER_CLOSE_METHOD );
		property( obj, IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP );
		property( obj, ISimpleDataSetModel.CACHED_ROW_COUNT_PROP );

		writeStructureList( obj, ISimpleDataSetModel.PARAM_BINDINGS_PROP );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDataSource(org.eclipse.birt.report.model.elements.DataSource)
	 */

	public void visitDataSource( DataSource obj )
	{
		super.visitDataSource( obj );

		property( obj, IDataSourceModel.BEFORE_OPEN_METHOD );
		property( obj, IDataSourceModel.BEFORE_CLOSE_METHOD );
		property( obj, IDataSourceModel.AFTER_OPEN_METHOD );
		property( obj, IDataSourceModel.AFTER_CLOSE_METHOD );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitParameter(org.eclipse.birt.report.model.elements.Parameter)
	 */

	public void visitParameter( Parameter obj )
	{
		super.visitParameter( obj );

		property( obj, IParameterModel.HIDDEN_PROP );

		resourceKey( obj, IParameterModel.HELP_TEXT_KEY_PROP,
				IParameterModel.HELP_TEXT_PROP );
		property( obj, IParameterModel.VALIDATE_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitReportItem(org.eclipse.birt.report.model.elements.ReportItem)
	 */

	public void visitReportItem( ReportItem obj )
	{
		super.visitReportItem( obj );

		property( obj, IReportItemModel.X_PROP );
		property( obj, IReportItemModel.Y_PROP );
		property( obj, IReportItemModel.Z_INDEX_PROP );
		property( obj, IReportItemModel.HEIGHT_PROP );
		property( obj, IReportItemModel.WIDTH_PROP );
		property( obj, IReportItemModel.DATA_SET_PROP );
		property( obj, IDesignElementModel.REF_TEMPLATE_PARAMETER_PROP );

		writeStructureList( obj, IReportItemModel.VISIBILITY_PROP );
		writeStructureList( obj, IReportItemModel.PARAM_BINDINGS_PROP );
		writeStructureList( obj, IReportItemModel.BOUND_DATA_COLUMNS_PROP );

		property( obj, IReportItemModel.BOOKMARK_PROP );
		writeStructure( obj, IReportItemModel.TOC_PROP );

		property( obj, IReportItemModel.ON_PREPARE_METHOD );
		property( obj, IReportItemModel.ON_CREATE_METHOD );
		property( obj, IReportItemModel.ON_RENDER_METHOD );
		property( obj, IReportItemModel.ON_PAGE_BREAK_METHOD );

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

		property( obj, IMasterPageModel.TYPE_PROP );

		// Only when type is custom, height and width can be output.

		String type = (String) obj.getLocalProperty( getModule( ),
				IMasterPageModel.TYPE_PROP );
		if ( DesignChoiceConstants.PAGE_SIZE_CUSTOM.equalsIgnoreCase( type ) )
		{
			property( obj, IMasterPageModel.HEIGHT_PROP );
			property( obj, IMasterPageModel.WIDTH_PROP );
		}

		property( obj, IMasterPageModel.ORIENTATION_PROP );
		property( obj, IMasterPageModel.TOP_MARGIN_PROP );
		property( obj, IMasterPageModel.LEFT_MARGIN_PROP );
		property( obj, IMasterPageModel.BOTTOM_MARGIN_PROP );
		property( obj, IMasterPageModel.RIGHT_MARGIN_PROP );

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
		property( obj, IStyledElementModel.STYLE_PROP );
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

		property( obj, IListingElementModel.REPEAT_HEADER_PROP );
		property( obj, IListingElementModel.PAGE_BREAK_INTERVAL_PROP );

		writeStructureList( obj, IListingElementModel.SORT_PROP );
		writeStructureList( obj, IListingElementModel.FILTER_PROP );
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
				IDesignElementModel.VIEW_ACTION_PROP );

		super.visitGroup( obj );

		property( obj, IGroupElementModel.GROUP_NAME_PROP );
		property( obj, IGroupElementModel.INTERVAL_BASE_PROP );
		property( obj, IGroupElementModel.INTERVAL_PROP );
		property( obj, IGroupElementModel.INTERVAL_RANGE_PROP );
		property( obj, IGroupElementModel.SORT_DIRECTION_PROP );
		property( obj, IGroupElementModel.SORT_TYPE_PROP );
		property( obj, IGroupElementModel.KEY_EXPR_PROP );

		writeStructure( obj, IGroupElementModel.TOC_PROP );

		property( obj, IDesignElementModel.EVENT_HANDLER_CLASS_PROP );
		property( obj, IGroupElementModel.ON_PREPARE_METHOD );
		property( obj, IGroupElementModel.ON_PAGE_BREAK_METHOD );
		property( obj, IGroupElementModel.REPEAT_HEADER_PROP );
		property( obj, IGroupElementModel.HIDE_DETAIL_PROP );

		property( obj, IStyleModel.PAGE_BREAK_AFTER_PROP );
		property( obj, IStyleModel.PAGE_BREAK_BEFORE_PROP );
		property( obj, IStyleModel.PAGE_BREAK_INSIDE_PROP );

		// write user property definitions and values

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		writeStructureList( obj, IGroupElementModel.SORT_PROP );
		writeStructureList( obj, IGroupElementModel.FILTER_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitExtendedDataSet(org.eclipse.birt.report.model.elements.ExtendedDataSet)
	 */

	public void visitOdaDataSet( OdaDataSet obj )
	{
		writer.startElement( DesignSchemaConstants.ODA_DATA_SET_TAG );
		attribute( obj, IOdaExtendableElementModel.EXTENSION_ID_PROP,
				IOdaExtendableElementModel.EXTENSION_ID_PROP );

		super.visitOdaDataSet( obj );

		writeStructureList( obj, IDataSetModel.PARAMETERS_PROP );
		writeStructureList( obj, IDataSetModel.RESULT_SET_PROP );

		if ( (String) obj.getLocalProperty( getModule( ),
				IOdaDataSetModel.QUERY_TEXT_PROP ) != null )
		{
			property( obj, IOdaDataSetModel.QUERY_TEXT_PROP );
		}

		property( obj, IOdaDataSetModel.RESULT_SET_NAME_PROP );
		writeOdaDesignerState( obj, IOdaDataSetModel.DESIGNER_STATE_PROP );
		propertyCDATA( obj, IOdaDataSetModel.DESIGNER_VALUES_PROP );

		List properties = (List) obj.getLocalProperty( getModule( ),
				IOdaDataSetModel.PRIVATE_DRIVER_PROPERTIES_PROP );
		writeExtendedProperties( properties,
				IOdaDataSetModel.PRIVATE_DRIVER_PROPERTIES_PROP );

		writeOdaExtensionProperties( obj,
				IOdaExtendableElementModel.EXTENSION_ID_PROP );

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
		assert slot == IGridItemModel.COLUMN_SLOT
				|| slot == ITableItemModel.COLUMN_SLOT;

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

			writer
					.conditionalStartElement( DesignSchemaConstants.REF_ENTRY_TAG );

			long baseId = virtualElement.getBaseId( );
			writer.attribute( DesignSchemaConstants.BASE_ID_ATTRIB, new Long(
					baseId ).toString( ) );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB, virtualElement
					.getName( ) );
			writer.attribute( DesignSchemaConstants.ID_ATTRIB, new Long(
					virtualElement.getID( ) ).toString( ) );
			if ( !virtualElement.hasLocalPropertyValues( )
					&& virtualElement.getStyle( ) == null )
			{
				writer.endElement( );
				continue;
			}

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
				if ( IDesignElementModel.NAME_PROP.equalsIgnoreCase( propDefn
						.getName( ) ) )
					continue;

				if ( virtualElement instanceof ExtendedItem
						&& IExtendedItemModel.EXTENSION_NAME_PROP
								.equalsIgnoreCase( propDefn.getName( ) ) )
					continue;

				boolean cdata = false;
				if ( propDefn.getTypeCode( ) == IPropertyType.SCRIPT_TYPE
						|| propDefn.getTypeCode( ) == IPropertyType.XML_TYPE )
					cdata = true;

				if ( propDefn.getTypeCode( ) == IPropertyType.STRUCT_TYPE )
				{
					if ( propDefn.isList( ) )
						writeStructureList( virtualElement, propDefn.getName( ) );
					else
						writeStructure( virtualElement, propDefn.getName( ) );
				}
				else if ( propDefn.getTypeCode( ) == IPropertyType.LIST_TYPE )
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

		writeStructureList( obj, IDataSetModel.PARAMETERS_PROP );
		writeStructureList( obj, IDataSetModel.RESULT_SET_PROP );
		writeSimplePropertyList( obj, IJointDataSetModel.DATA_SETS_PROP );

		writeStructureList( obj, IJointDataSetModel.JOIN_CONDITONS_PROP );
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
		writer.attribute( IDesignElementModel.NAME_PROP,
				IOdaDataSetModel.DESIGNER_STATE_PROP );

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

	/**
	 * Writes the content tree out to the file.
	 * 
	 * @param tree
	 *            the tree to write out
	 */

	protected void writeContentTree( ContentTree tree )
	{
		if ( tree == null || tree.isEmpty( ) )
			return;
		List children = tree.getChildren( );
		for ( int i = 0; i < children.size( ); i++ )
		{
			ContentNode node = (ContentNode) children.get( i );
			writeContentNode( node );
		}
	}

	/**
	 * Writers the content node out to the file.
	 * 
	 * @param node
	 *            the content node to write out
	 */

	private void writeContentNode( ContentNode node )
	{
		String tagName = node.getName( );
		boolean isCdata = node.isCDATASection( );

		writer.startElement( tagName );

		// attributes
		Map attributes = node.getAttributes( );
		Iterator keys = attributes.keySet( ).iterator( );
		while ( keys.hasNext( ) )
		{
			String key = (String) keys.next( );
			String attr = (String) attributes.get( key );
			writer.attribute( key, attr );
		}

		// write the value or the children
		String value = node.getValue( );
		List children = node.getChildren( );
		assert StringUtil.isBlank( value ) || children.isEmpty( );
		if ( !StringUtil.isBlank( value ) )
		{
			if ( isCdata )
				writer.textCDATA( value );
			else
				writer.text( value );
		}
		else
		{
			for ( int i = 0; i < children.size( ); i++ )
			{
				ContentNode child = (ContentNode) children.get( i );
				writeContentNode( child );
			}
		}

		writer.endElement( );
	}
}