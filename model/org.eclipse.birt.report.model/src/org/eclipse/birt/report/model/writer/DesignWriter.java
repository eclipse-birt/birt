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
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.core.UserPropertyDefn;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.DataSet;
import org.eclipse.birt.report.model.elements.DataSource;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ElementVisitor;
import org.eclipse.birt.report.model.elements.ExtendedDataSet;
import org.eclipse.birt.report.model.elements.ExtendedDataSource;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.FreeForm;
import org.eclipse.birt.report.model.elements.GraphicMasterPage;
import org.eclipse.birt.report.model.elements.GridItem;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ImageItem;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.LineItem;
import org.eclipse.birt.report.model.elements.ListGroup;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.ListingElement;
import org.eclipse.birt.report.model.elements.MasterPage;
import org.eclipse.birt.report.model.elements.MultiLineDataItem;
import org.eclipse.birt.report.model.elements.Parameter;
import org.eclipse.birt.report.model.elements.ParameterGroup;
import org.eclipse.birt.report.model.elements.RectangleItem;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.ScriptDataSet;
import org.eclipse.birt.report.model.elements.ScriptDataSource;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableGroup;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.TextItem;
import org.eclipse.birt.report.model.elements.Translation;
import org.eclipse.birt.report.model.elements.structures.Action;
import org.eclipse.birt.report.model.elements.structures.CachedMetaData;
import org.eclipse.birt.report.model.elements.structures.CustomColor;
import org.eclipse.birt.report.model.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.elements.structures.IncludeLibrary;
import org.eclipse.birt.report.model.elements.structures.IncludeScript;
import org.eclipse.birt.report.model.elements.structures.MapRule;
import org.eclipse.birt.report.model.metadata.Choice;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.metadata.UserChoice;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Writes the design to an XML design file that follows the BIRT design schema.
 * Uses a visitor pattern to traverse each element. BIRT elements support
 * inheritance in several forms. Because of this, the design writer writes only
 * those properties "local" to the element being written -- it does not write
 * inherited properties.
 * <p>
 * Because the XML schema was designed for to be understood by humans, the
 * schema is not a literal representation of the model. Instead, properties are
 * named and grouped in a way that is easiest to explain and understand. This
 * means that the writer has to do a bit more work to write the design, the the
 * extra work here is well worth the savings to the many customers who will read
 * the design format.
 *  
 */

public class DesignWriter extends ElementVisitor
{

	/**
	 * The low-level writer that emits XML syntax.
	 */

	protected IndentableXMLWriter writer = null;

	/**
	 * The design context used to convert units.
	 */

	private ReportDesign design;

	/**
	 * The base 64 codec for embedded images.
	 */

	private static Base64 base = new Base64( );

	/**
	 * Constructs a writer with the specified design.
	 * 
	 * @param design
	 *            the internal representation of the design
	 */

	public DesignWriter( ReportDesign design )
	{
		this.design = design;
	}

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
		writer = new IndentableXMLWriter( outputFile );
		writeFile( );
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
		writer = new IndentableXMLWriter( os );
		writeFile( );
	}

	/**
	 * Implementation method to write the file header and contents.
	 */

	private void writeFile( )
	{
		writer
				.literal( "<!-- Written by Eclipse BIRT 0.0 (http://www.eclipse.com) -->\r\n" ); //$NON-NLS-1$
		design.apply( this );
	}

	/**
	 * Write the top-level Report tag, and the properties and contents of the
	 * report itself.
	 * 
	 * @param obj
	 *            the object to write
	 */

	public void visitReportDesign( ReportDesign obj )
	{
		writer.startElement( DesignSchemaConstants.REPORT_TAG );
		writer.attribute( "xmlns", //$NON-NLS-1$ 
				"http://www.eclipse.com/schemas/BIRT_schema.xsd" ); //$NON-NLS-1$ 
		property( obj, ReportDesign.AUTHOR_PROP );
		property( obj, ReportDesign.HELP_GUIDE_PROP );
		property( obj, ReportDesign.CREATED_BY_PROP );
		property( obj, ReportDesign.UNITS_PROP );
		property( obj, ReportDesign.REFRESH_RATE_PROP );
		property( obj, ReportDesign.BASE_PROP );
		property( obj, ReportDesign.MSG_BASE_NAME_PROP );
        
		resourceKey( obj, ReportDesign.TITLE_ID_PROP, ReportDesign.TITLE_PROP );
		property( obj, ReportDesign.COMMENTS_PROP );

		resourceKey( obj, ReportDesign.DESCRIPTION_ID_PROP,
				ReportDesign.DESCRIPTION_PROP );

		property( obj, ReportDesign.INITIALIZE_METHOD );
		property( obj, ReportDesign.BEFORE_FACTORY_METHOD );
		property( obj, ReportDesign.AFTER_FACTORY_METHOD );
		property( obj, ReportDesign.BEFORE_OPEN_DOC_METHOD );
		property( obj, ReportDesign.AFTER_OPEN_DOC_METHOD );
		property( obj, ReportDesign.BEFORE_CLOSE_DOC_METHOD );
		property( obj, ReportDesign.AFTER_CLOSE_DOC_METHOD );
		property( obj, ReportDesign.BEFORE_RENDER_METHOD );
		property( obj, ReportDesign.AFTER_RENDER_METHOD );
       
		// include libraries and scripts

		writeSimpleStructureList( obj, ReportDesign.INCLUDE_LIBRARIES,
				IncludeLibrary.FILE_NAME_MEMBER );
		writeSimpleStructureList( obj, ReportDesign.INCLUDE_SCRIPTS,
				IncludeScript.FILE_NAME_MEMBER );

		// config variables

		writeStructureList( obj, ReportDesign.CONFIG_VARS_PROP );

		writeArrangedContents( obj, ReportDesign.PARAMETER_SLOT,
				DesignSchemaConstants.PARAMETERS_TAG );
		writeArrangedContents( obj, ReportDesign.DATA_SOURCE_SLOT,
				DesignSchemaConstants.DATA_SOURCES_TAG );
		writeArrangedContents( obj, ReportDesign.DATA_SET_SLOT,
				DesignSchemaConstants.DATA_SETS_TAG );

		// ColorPalette tag

		List list = (List) obj.getLocalProperty( design,
				ReportDesign.COLOR_PALETTE_PROP );
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

		// Translations. ( Custom-defined messages )

		String[] resourceKeys = design.getTranslationResourceKeys( );
		if ( resourceKeys != null && resourceKeys.length > 0 )
		{
			writer.startElement( DesignSchemaConstants.TRANSLATIONS_TAG );

			for ( int i = 0; i < resourceKeys.length; i++ )
			{
				writer.startElement( DesignSchemaConstants.RESOURCE_TAG );
				writer.attribute( DesignSchemaConstants.KEY_ATTRIB,
						resourceKeys[i] );

				List translations = design.getTranslations( resourceKeys[i] );
				for ( int j = 0; j < translations.size( ); j++ )
				{
					writer.startElement( DesignSchemaConstants.TRANSLATION_TAG );
					Translation translation = (Translation) translations
							.get( j );
					String locale = translation.getLocale( );
					if ( !StringUtil.isBlank( locale ) )
					{
						writer.attribute( DesignSchemaConstants.LOCALE_ATTRIB,
								locale );
					}

					writer.text( translation.getText( ) );
					writer.endElement( );
				}
				writer.endElement( );
			}
			writer.endElement( );
		}

		writeContents( obj, ReportDesign.STYLE_SLOT,
				DesignSchemaConstants.STYLES_TAG );
		writeArrangedContents( obj, ReportDesign.COMPONENT_SLOT,
				DesignSchemaConstants.COMPONENTS_TAG );
		writeArrangedContents( obj, ReportDesign.PAGE_SLOT,
				DesignSchemaConstants.PAGE_SETUP_TAG );
		writeContents( obj, ReportDesign.BODY_SLOT,
				DesignSchemaConstants.BODY_TAG );
		writeContents( obj, ReportDesign.SCRATCH_PAD_SLOT,
				DesignSchemaConstants.SCRATCH_PAD_TAG );

		// Embedded images

		list = (List) obj.getLocalProperty( design, ReportDesign.IMAGES_PROP );
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

				try
				{
					if ( image.getData( ) != null )
					{
						byte[] data = base.encode( image.getData( ) );
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
		writer.endElement( );
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
	public void visitExtendedDataSource( ExtendedDataSource obj )
	{
		writer.startElement( DesignSchemaConstants.EXTENDED_DATA_SOURCE_TAG );

		super.visitExtendedDataSource( obj );

		property( obj, ExtendedDataSource.DRIVER_NAME_PROP );

		List properties = (List) obj.getLocalProperty( design,
				ExtendedDataSource.PUBLIC_DRIVER_PROPERTIES_PROP );
		writeExtendedProperties( properties,
				ExtendedDataSource.PUBLIC_DRIVER_PROPERTIES_PROP );

		properties = (List) obj.getLocalProperty( design,
				ExtendedDataSource.PRIVATE_DRIVER_PROPERTIES_PROP );
		writeExtendedProperties( properties,
				ExtendedDataSource.PRIVATE_DRIVER_PROPERTIES_PROP );

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

	private void writeExtendedProperties( List properties, String propName )
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
				if ( !StringUtil.isBlank( property.getName( ) ) )
				{
					writer.startElement( DesignSchemaConstants.NAME_ATTRIB );
					writer.text( property.getName( ) );
					writer.endElement( );
				}
				if ( !StringUtil.isBlank( property.getValue( ) ) )
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
		writer.startElement( DesignSchemaConstants.DATA_TAG );

		super.visitDataItem( obj );

		property( obj, DataItem.DISTINCT_PROP );
		property( obj, DataItem.DISTINCT_RESET_PROP );
		property( obj, DataItem.VALUE_EXPR_PROP );

		resourceKey( obj, DataItem.HELP_TEXT_KEY_PROP, DataItem.HELP_TEXT_PROP );

		writeAction( obj, DataItem.ACTION_PROP );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitMultiLineDataItem(org.eclipse.birt.report.model.elements.MultiLineDataItem)
	 */

	public void visitMultiLineDataItem( MultiLineDataItem obj )
	{
		writer.startElement( DesignSchemaConstants.MULTI_LINE_DATA_TAG );

		super.visitMultiLineDataItem( obj );

		property( obj, MultiLineDataItem.VALUE_EXPR_PROP );
		property( obj, MultiLineDataItem.CONTENT_TYPE_EXPR_PROP );

		writer.endElement( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitExtendedItem(org.eclipse.birt.report.model.elements.ExtendedItem)
	 */

	public void visitExtendedItem( ExtendedItem obj )
	{
		writer.startElement( DesignSchemaConstants.EXTENDED_ITEM_TAG );
		attribute( obj, DesignSchemaConstants.EXTENSION_ATTRIB,
				ExtendedItem.EXTENSION_PROP );
		super.visitExtendedItem( obj );
		ExtensionElementDefn extDefn = obj.getExtDefn( );
		if ( extDefn != null )
		{
			// TODO: write the style properties

			List list = extDefn.getProperties( );
			for ( int i = 0; i < list.size( ); i++ )
			{
				PropertyDefn prop = (PropertyDefn) list.get( i );
				Object value = obj.getLocalProperty( design, prop.getName( ) );
				if ( value != null )
				{
					if ( prop.getTypeCode( ) != PropertyType.XML_TYPE )
						writeEntry( getTagByPropertyType( prop ), prop
								.getName( ), prop.getXmlValue( design, value ),
								false );
					else
					{
						writeEntry( getTagByPropertyType( prop ), prop
								.getName( ), prop.getXmlValue( design, value ),
								true );
					}
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
	 * @see org.eclipse.birt.report.model.design.elements.DesignVisitor#visitList(org.eclipse.birt.report.model.design.elements.ListItem)
	 */

	public void visitList( ListItem obj )
	{
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
		writer.startElement( DesignSchemaConstants.TABLE_TAG );

		super.visitTable( obj );

		property( obj, TableItem.REPEAT_HEADER_PROP );

		resourceKey( obj, TableItem.CAPTION_KEY_PROP, TableItem.CAPTION_PROP );

		// There is no columns tag for this slot. All columns are written under
		// table tag.

		writeContents( obj, TableItem.COLUMN_SLOT, null );

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
		writer.startElement( DesignSchemaConstants.COLUMN_TAG );

		super.visitColumn( obj );

		property( obj, TableColumn.WIDTH_PROP );
		property( obj, TableColumn.REPEAT_PROP );

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

		super.visitRow( obj );

		property( obj, TableRow.HEIGHT_PROP );
		property( obj, TableRow.BOOKMARK_PROP );

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

		super.visitCell( obj );

		property( obj, Cell.COLUMN_PROP );
		property( obj, Cell.COL_SPAN_PROP );
		property( obj, Cell.ROW_SPAN_PROP );
		property( obj, Cell.DROP_PROP );
		property( obj, Cell.HEIGHT_PROP );
		property( obj, Cell.WIDTH_PROP );

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
		writer.startElement( DesignSchemaConstants.GRID_TAG );

		super.visitGrid( obj );

		writeContents( obj, GridItem.COLUMN_SLOT, null );
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
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitScalarParameter(org.eclipse.birt.report.model.elements.ScalarParameter)
	 */

	public void visitScalarParameter( ScalarParameter obj )
	{
		writer.startElement( DesignSchemaConstants.SCALAR_PARAMETER_TAG );

		super.visitScalarParameter( obj );

		property( obj, ScalarParameter.DATA_TYPE_PROP );
		property( obj, ScalarParameter.CONCEAL_VALUE_PROP );
		property( obj, ScalarParameter.ALLOW_BLANK_PROP );
		property( obj, ScalarParameter.ALLOW_NULL_PROP );
		property( obj, ScalarParameter.FORMAT_PROP );
		property( obj, ScalarParameter.CONTROL_TYPE_PROP );
		property( obj, ScalarParameter.ALIGNMENT_PROP );
		property( obj, ScalarParameter.DATASET_NAME_PROP );
		property( obj, ScalarParameter.VALUE_EXPR_PROP );
		property( obj, ScalarParameter.LABEL_EXPR_PROP );
		property( obj, ScalarParameter.MUCH_MATCH_PROP );
		property( obj, ScalarParameter.FIXED_ORDER_PROP );
		property( obj, ScalarParameter.DEFAULT_VALUE_PROP );

		writeStructureList( obj, ScalarParameter.SELECTION_LIST_PROP );

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

		property( obj, Style.DATE_TIME_FORMAT_PROP );
		property( obj, Style.NUMBER_FORMAT_PROP );
		property( obj, Style.NUMBER_ALIGN_PROP );
		property( obj, Style.STRING_FORMAT_PROP );

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
		property( obj, Style.HIGHLIGHT_TEST_EXPR_PROP );

		List list = (ArrayList) obj.getLocalProperty( design,
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

				//Border
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

				//Font
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
				//Format
				property( rule, HighlightRule.DATE_TIME_FORMAT_MEMBER );
				property( rule, HighlightRule.NUMBER_FORMAT_MEMBER );
				property( rule, HighlightRule.NUMBER_ALIGN_MEMBER );
				property( rule, HighlightRule.STRING_FORMAT_MEMBER );

				property( rule, HighlightRule.VALUE1_MEMBER );
				property( rule, HighlightRule.VALUE2_MEMBER );

				writer.endElement( );
			}
			writer.endElement( );
		}

		// Map
		property( obj, Style.MAP_TEST_EXPR_PROP );

		list = (ArrayList) obj.getLocalProperty( design, Style.MAP_RULES_PROP );
		if ( list != null && list.size( ) > 0 )
		{
			writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
					Style.MAP_RULES_PROP );

			for ( int i = 0; i < list.size( ); i++ )
			{
				MapRule rule = (MapRule) list.get( i );
				writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

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

	private void writeContents( DesignElement obj, int slot, String tag )
	{
		List list = obj.getSlot( slot ).getContents( );
		if ( list.isEmpty( ) )
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

	private void writeArrangedContents( DesignElement obj, int slot, String tag )
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

	/**
	 * Writes user property definitions.
	 * 
	 * @param obj
	 *            the element that contains user properties
	 */

	private void writeUserPropertyDefns( DesignElement obj )
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

			ChoiceSet choiceSet = propDefn.getChoices( );

			if ( choiceSet != null && choiceSet.getChoices( ) != null
					&& choiceSet.getChoices( ).length > 0 )
			{
				writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
				writer.attribute( DesignSchemaConstants.NAME_ATTRIB,
						UserPropertyDefn.CHOICES_MEMBER );

				Choice[] choices = choiceSet.getChoices( );

				for ( int i = 0; i < choices.length; i++ )
				{
					UserChoice choice = (UserChoice) choices[i];
					writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );

					if ( !StringUtil.isBlank( choice.getName( ) ) )
					{
						writeEntry( DesignSchemaConstants.PROPERTY_TAG,
								UserChoice.NAME_PROP, choice.getName( ), false );
					}
					if ( choice.getValue( ) != null )
					{
						writeEntry( DesignSchemaConstants.PROPERTY_TAG,
								UserChoice.VALUE_PROP, choice.getValue( )
										.toString( ), false );
					}
					if ( !StringUtil.isBlank( choice.getDisplayNameKey( ) )
							|| !StringUtil.isBlank( choice.getDisplayName( ) ) )
					{
						writeResouceKey(
								DesignSchemaConstants.TEXT_PROPERTY_TAG,
								UserChoice.DISPLAY_NAME_PROP, choice
										.getDisplayNameKey( ), choice
										.getDisplayName( ), false );
					}

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

	private void writeUserPropertyValues( DesignElement obj )
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
		writer.startElement( DesignSchemaConstants.IMAGE_TAG );

		super.visitImage( obj );

		property( obj, ImageItem.SIZE_PROP );
		property( obj, ImageItem.SCALE_PROP );

		String source = (String) obj.getLocalProperty( design,
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

	private void writeAction( DesignElement obj, String propName )
	{
		Action action = (Action) obj.getLocalProperty( design, propName );
		if ( action == null )
			return;

		String linkType = (String) action.getProperty( design,
				Action.LINK_TYPE_MEMBER );
		String targetWindow = (String) action.getProperty( design,
				Action.TARGET_WINDOW_MEMBER );

		// Empty action

		if ( StringUtil.isBlank( targetWindow )
				&& linkType
						.equalsIgnoreCase( DesignChoiceConstants.ACTION_LINK_TYPE_NONE ) )
			return;

		writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );
		writer.attribute( DesignElement.NAME_PROP, propName );

		property( action, Action.TARGET_WINDOW_MEMBER );

		if ( linkType
				.equalsIgnoreCase( DesignChoiceConstants.ACTION_LINK_TYPE_HYPERLINK ) )
		{
			property( action, Action.HYPERLINK_MEMBER );
		}
		else if ( linkType
				.equalsIgnoreCase( DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK ) )
		{
			property( action, Action.BOOKMARK_LINK_MEMBER );
		}
		else if ( linkType
				.equalsIgnoreCase( DesignChoiceConstants.ACTION_LINK_TYPE_DRILL_THROUGH ) )
		{
			property( action, Action.DRILLTHROUGH_REPORT_NAME_MEMBER );

			writeStructureList( action,
					Action.DRILLTHROUGH_PARAM_BINDINGS_MEMBER );

			String drillThroughType = (String) action.getProperty( design,
					Action.DRILLTHROUGH_TYPE_MEMBER );

			if ( DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_BOOKMARK_LINK
					.equalsIgnoreCase( drillThroughType ) )
			{
				property( action, Action.DRILLTHROUGH_BOOKMARK_LINK_MEMBER );
			}
			else if ( DesignChoiceConstants.DRILL_THROUGH_LINK_TYPE_SEARCH
					.equalsIgnoreCase( drillThroughType ) )
			{
				writeStructureList( action, Action.DRILLTHROUGH_SEARCH_MEMBER );
			}
		}
		else
		{
			assert false;
		}

		writer.endElement( );
	}

	/**
	 * Writes one property entry of an element.
	 * 
	 * @param obj
	 *            the design element
	 * @param propName
	 *            the property name
	 */

	private void property( DesignElement obj, String propName )
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

	private void property( IStructure structure, String memberName )
	{
		writeProperty( structure, null, memberName, false, false );
	}

	/**
	 * Writes one property entry of an structure without member name.
	 * 
	 * @param structure
	 *            the structure
	 * @param memberName
	 *            the member name
	 */

	private void propertyWithoutName( IStructure structure, String memberName )
	{
		writeProperty( structure, null, memberName, false, true );
	}

	/**
	 * Writes one property entry of an element as CDATA.
	 * 
	 * @param obj
	 *            the design element
	 * @param propName
	 *            the property name
	 */

	private void propertyCDATA( DesignElement obj, String propName )
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

	private void propertyCDATA( IStructure structure, String memberName )
	{
		writeProperty( structure, null, memberName, true, false );
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

	private void writeProperty( DesignElement obj, String tag, String propName,
			boolean cdata )
	{
		PropertyDefn propDefn = obj.getPropertyDefn( propName );

		// The style property is not available for all elements.

		if ( propDefn == null )
			return;

		Object value = obj.getLocalProperty( design, propName );
		if ( value == null )
			return;

		String xml = propDefn.getXmlValue( design, value );
		if ( xml == null )
			return;

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

	private void writeProperty( IStructure struct, String tag,
			String memberName, boolean cdata, boolean withoutName )
	{
		StructureDefn structDefn = struct.getDefn( );
		assert structDefn != null;

		StructPropertyDefn propDefn = structDefn.getMember( memberName );
		assert propDefn != null;

		Object value = struct.getProperty( design, propDefn );
		if ( value == null )
			return;

		String xml = propDefn.getXmlValue( design, value );
		if ( xml == null )
			return;

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

	private void writeEntry( String tag, String name, String value,
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
	 * Writes out a long text the length of which exceeds 80.
	 * 
	 * @param tag
	 *            the element tag
	 * @param name
	 *            the name attribute
	 * @param value
	 *            the text value
	 */

	private void writeLongIndentText( String tag, String name, String value )
	{
		writer.startElement( tag );
		if ( name != null )
			writer.attribute( DesignSchemaConstants.NAME_ATTRIB, name );

		writer.indentLongText( value );
		writer.endElement( );
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

	private void resourceKeyCDATA( DesignElement obj, String resourceKey,
			String resourceValue )
	{
		resourceKey( obj, resourceKey, resourceValue, true );
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

	private void resourceKey( DesignElement obj, String resourceKey,
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

	private void resourceKey( DesignElement obj, String resourceKey,
			String resourceName, boolean cdata )
	{
		PropertyDefn nameProp = obj.getDefn( ).getProperty( resourceName );
		assert nameProp != null;

		Object value = obj.getLocalProperty( design, nameProp.getName( ) );
		String xml = nameProp.getXmlValue( design, value );

		PropertyDefn keyProp = obj.getDefn( ).getProperty( resourceKey );
		assert keyProp != null;

		value = obj.getLocalProperty( design, keyProp.getName( ) );
		String xmlKey = keyProp.getXmlValue( design, value );
		if ( StringUtil.isBlank( xmlKey ) && StringUtil.isBlank( xml ) )
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

	private void resourceKey( IStructure struct, String resourceKey,
			String resourceName )
	{
		StructureDefn structDefn = struct.getDefn( );
		assert structDefn != null;

		StructPropertyDefn nameProp = structDefn.getMember( resourceName );
		assert nameProp != null;

		Object value = struct.getProperty( design, nameProp );
		String xml = nameProp.getXmlValue( design, value );

		StructPropertyDefn keyProp = structDefn.getMember( resourceKey );
		assert keyProp != null;

		value = struct.getProperty( design, keyProp );
		String xmlKey = keyProp.getXmlValue( design, value );
		if ( StringUtil.isBlank( xmlKey ) && StringUtil.isBlank( xml ) )
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

	private void writeResouceKey( String tagName, String name, String key,
			String xml, boolean cdata )
	{
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

	private String getTagByPropertyType( PropertyDefn prop )
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
				return DesignSchemaConstants.PROPERTY_TAG;
		}
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

	private void writeStructureList( DesignElement obj, String propName )
	{
		PropertyDefn prop = obj.getDefn( ).getProperty( propName );
		assert prop != null;
		assert prop.getTypeCode( ) == PropertyType.STRUCT_TYPE && prop.isList( );

		List list = (List) obj.getLocalProperty( design, propName );
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

	private void writeStructureList( IStructure obj, String memberName )
	{
		PropertyDefn prop = obj.getDefn( ).getMember( memberName );
		assert prop != null;
		assert prop.getTypeCode( ) == PropertyType.STRUCT_TYPE && prop.isList( );

		List list = (List) obj.getProperty( design, prop );
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
	 * Writes the structure list, each of which has only one member.
	 * 
	 * @param obj
	 *            the design element
	 * @param propName
	 *            the name of the list property
	 * @param memberName
	 *            the name of the member in structure
	 */

	private void writeSimpleStructureList( DesignElement obj, String propName,
			String memberName )
	{
		PropertyDefn prop = obj.getDefn( ).getProperty( propName );
		assert prop != null;
		assert prop.getTypeCode( ) == PropertyType.STRUCT_TYPE && prop.isList( );

		List list = (List) obj.getLocalProperty( design, propName );
		if ( list == null || list.size( ) == 0 )
			return;

		writer.startElement( DesignSchemaConstants.LIST_PROPERTY_TAG );
		writer.attribute( DesignSchemaConstants.NAME_ATTRIB, propName );

		PropertyDefn propDef = obj.getDefn( ).getProperty( propName );
		PropertyDefn memberDefn = propDef.getStructDefn( ).getMember(
				memberName );

		Iterator iter = list.iterator( );
		while ( iter.hasNext( ) )
		{
			IStructure struct = (IStructure) iter.next( );

			propertyWithoutName( struct, memberDefn.getName( ) );
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

		// The element name and extends should be written in the tag.

		attribute( obj, DesignSchemaConstants.NAME_ATTRIB,
				DesignElement.NAME_PROP );
		attribute( obj, DesignSchemaConstants.EXTENDS_ATTRIB,
				DesignElement.EXTENDS_PROP );

		property( obj, DesignElement.COMMENTS_PROP );
		propertyCDATA( obj, DesignElement.CUSTOM_XML_PROP );

		resourceKey( obj, DesignElement.DISPLAY_NAME_ID_PROP,
				DesignElement.DISPLAY_NAME_PROP );

		writeUserPropertyDefns( obj );
		writeUserPropertyValues( obj );

		writeStructureList( obj, DesignElement.PROPERTY_MASKS_PROP );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitDataSet(org.eclipse.birt.report.model.elements.DataSet)
	 */

	public void visitDataSet( DataSet obj )
	{
		super.visitDataSet( obj );

		property( obj, DataSet.DATA_SOURCE_PROP );

		property( obj, DataSet.BEFORE_OPEN_METHOD );
		property( obj, DataSet.BEFORE_CLOSE_METHOD );
		property( obj, DataSet.ON_FETCH_METHOD );
		property( obj, DataSet.AFTER_OPEN_METHOD );
		property( obj, DataSet.AFTER_CLOSE_METHOD );

		writeStructureList( obj, DataSet.INPUT_PARAMETERS_PROP );
		writeStructureList( obj, DataSet.OUTPUT_PARAMETERS_PROP );
		writeStructureList( obj, DataSet.PARAM_BINDINGS_PROP );
		writeStructureList( obj, DataSet.RESULT_SET_PROP );
		writeStructureList( obj, DataSet.COMPUTED_COLUMNS_PROP );
		writeStructureList( obj, DataSet.COLUMN_HINTS_PROP );
		writeStructureList( obj, DataSet.FILTER_PROP );
        
        CachedMetaData metadata = (CachedMetaData)obj.getLocalProperty( design, DataSet.CACHED_METADATA_PROP );
        if( metadata != null )
        {
            // Writing cached data set meta-data information.
        	
            writer.startElement( DesignSchemaConstants.STRUCTURE_TAG );
            writer.attribute( DesignElement.NAME_PROP, DataSet.CACHED_METADATA_PROP );
            
            writeStructureList( metadata, CachedMetaData.INPUT_PARAMETERS_MEMBER );
            writeStructureList( metadata, CachedMetaData.OUTPUT_PARAMETERS_MEMBER );
            writeStructureList( metadata, CachedMetaData.RESULT_SET_MEMBER );
        
            writer.endElement();

            // end of writing meta-data information.        
        }
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

		writeStructureList( obj, ReportItem.VISIBILITY_PROP );
		writeStructureList( obj, ReportItem.PARAM_BINDINGS_PROP );

		property( obj, ReportItem.BOOKMARK_PROP );
		property( obj, ReportItem.TOC_PROP );

		property( obj, ReportItem.ON_CREATE_METHOD );
		property( obj, ReportItem.ON_RENDER_METHOD );
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

		String type = (String) obj.getLocalProperty( design,
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

		property( obj, ListingElement.ON_START_METHOD );
		property( obj, ListingElement.ON_ROW_METHOD );
		property( obj, ListingElement.ON_FINISH_METHOD );

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
		super.visitGroup( obj );

		property( obj, GroupElement.GROUP_NAME_PROP );
		property( obj, GroupElement.INTERVAL_PROP );
		property( obj, GroupElement.INTERVAL_RANGE_PROP );
		property( obj, GroupElement.SORT_DIRECTION_PROP );
		property( obj, GroupElement.KEY_EXPR_PROP );
		property( obj, GroupElement.TOC_PROP );

		property( obj, GroupElement.ON_START_METHOD );
		property( obj, GroupElement.ON_ROW_METHOD );
		property( obj, GroupElement.ON_FINISH_METHOD );

		writeStructureList( obj, GroupElement.SORT_PROP );
		writeStructureList( obj, GroupElement.FILTER_PROP );
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

	private void attribute( DesignElement obj, String attr, String propName )
	{
		ElementPropertyDefn prop = obj.getPropertyDefn( propName );
		assert prop != null;

		Object value = obj.getLocalProperty( design, prop );
		if ( value == null )
			return;

		String xml = prop.getXmlValue( design, value );
		if ( xml == null )
			return;

		writer.attribute( attr, xml );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.elements.ElementVisitor#visitExtendedDataSet(org.eclipse.birt.report.model.elements.ExtendedDataSet)
	 */

	public void visitExtendedDataSet( ExtendedDataSet obj )
	{
		writer.startElement( DesignSchemaConstants.EXTENDED_DATA_SET_TAG );

		super.visitExtendedDataSet( obj );

		String queryFrom = (String) obj.getLocalProperty( design,
				ExtendedDataSet.QUERY_CHOICE_TYPE_PROP );

		if ( DesignChoiceConstants.QUERY_CHOICE_TYPE_SCRIPT
				.equalsIgnoreCase( queryFrom ) )
		{
			property( obj, ExtendedDataSet.QUERY_SCRIPT_METHOD );
		}
		else if ( DesignChoiceConstants.QUERY_CHOICE_TYPE_TEXT
				.equalsIgnoreCase( queryFrom ) )
		{
			property( obj, ExtendedDataSet.QUERY_TEXT_PROP );
		}

		property( obj, ExtendedDataSet.TYPE_PROP );
		property( obj, ExtendedDataSet.RESULT_SET_NAME_PROP );

		propertyCDATA( obj, ExtendedDataSet.PRIVATE_DRIVER_DESIGN_STATE_PROP );

		List properties = (List) obj.getLocalProperty( design,
				ExtendedDataSet.PUBLIC_DRIVER_PROPERTIES_PROP );
		writeExtendedProperties( properties,
				ExtendedDataSet.PUBLIC_DRIVER_PROPERTIES_PROP );

		properties = (List) obj.getLocalProperty( design,
				ExtendedDataSet.PRIVATE_DRIVER_PROPERTIES_PROP );
		writeExtendedProperties( properties,
				ExtendedDataSet.PRIVATE_DRIVER_PROPERTIES_PROP );

		writer.endElement( );
	}
}
