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

package org.eclipse.birt.report.engine.ir;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

public class EngineIRReader implements IOConstants
{

	protected Report reportDesign;

	public EngineIRReader( )
	{
	}

	public Report read( InputStream in ) throws IOException
	{
		DataInputStream dis = new DataInputStream( in );

		// read the version
		long version = IOUtil.readLong( dis );
		if ( version != 0L )
		{
			throw new IOException( "unsupported version:" + version );
		}

		String designVersion = IOUtil.readString( dis );
		if ( !DesignSchemaConstants.REPORT_VERSION.equals( designVersion ) )
		{
			throw new IOException( "un-compatable design version"
					+ designVersion );
		}

		reportDesign = new Report( );

		// read the int
		// design's base path
		String basePath = IOUtil.readString( dis );
		reportDesign.setBasePath( basePath );

		// design's unit
		String unit = IOUtil.readString( dis );
		reportDesign.setUnit( unit );

		// style informations
		int styleCount = IOUtil.readInt( dis );
		for ( int i = 0; i < styleCount; i++ )
		{
			String styleName = IOUtil.readString( dis );
			IStyle style = readStyle( dis );
			reportDesign.addStyle( styleName, style );
		}
		String rootStyleName = IOUtil.readString( dis );
		reportDesign.setRootStyleName( rootStyleName );

		// named expression
		Map namedExpressions = IOUtil.readMap( dis );
		if ( namedExpressions != null )
		{
			reportDesign.getNamedExpressions( ).putAll( namedExpressions );
		}

		// page setup
		PageSetupDesign pageSetup = reportDesign.getPageSetup( );
		int masterPageCount = IOUtil.readInt( dis );
		for ( int i = 0; i < masterPageCount; i++ )
		{
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) readDesign( dis );
			pageSetup.addMasterPage( masterPage );
		}

		// write the body conent
		int count = IOUtil.readInt( dis );
		for ( int i = 0; i < count; i++ )
		{
			ReportItemDesign item = (ReportItemDesign) readDesign( dis );
			reportDesign.addContent( item );
		}

		return reportDesign;
	}

	public void link( Report report, ReportDesignHandle handle )
	{
		new ReportItemVisitor( handle, report ).link( );
	}

	protected Object readDesign( DataInputStream in ) throws IOException
	{
		// read the type
		short designType = IOUtil.readShort( in );
		byte[] buffer = IOUtil.readBytes( in );
		DataInputStream dis = new DataInputStream( new ByteArrayInputStream(
				buffer ) );
		switch ( designType )
		{
			case SIMPLE_MASTER_PAGE_DESIGN :
			{
				SimpleMasterPageDesign masterPage = new SimpleMasterPageDesign( );
				readSimpleMasterPage( dis, masterPage );
				// read it's content
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					ReportItemDesign item = (ReportItemDesign) readDesign( in );
					masterPage.addHeader( item );
				}
				count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					ReportItemDesign item = (ReportItemDesign) readDesign( in );
					masterPage.addFooter( item );
				}
				return masterPage;
			}

			case GRID_DESIGN :
			{
				GridItemDesign grid = new GridItemDesign( );
				readGrid( dis, grid );
				// read it's content
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					RowDesign row = (RowDesign) readDesign( in );
					grid.addRow( row );
				}
				return grid;
			}
			case FREE_FORM_DESIGN :
			{
				FreeFormItemDesign freeForm = new FreeFormItemDesign( );
				readFreeForm( dis, freeForm );
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					ReportItemDesign item = (ReportItemDesign) readDesign( in );
					freeForm.addItem( item );
				}
				return freeForm;
			}
			case ROW_DESIGN :
			{
				RowDesign row = new RowDesign( );
				readRow( dis, row );
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					CellDesign cell = (CellDesign) readDesign( in );
					row.addCell( cell );
				}
				return row;
			}

			case CELL_DESIGN :
			{
				CellDesign cell = new CellDesign( );
				readCell( dis, cell );
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					ReportItemDesign item = (ReportItemDesign) readDesign( in );
					cell.addContent( item );
				}
				return cell;
			}

			case LIST_DESIGN :
			{
				ListItemDesign list = new ListItemDesign( );
				readList( dis, list );
				// read header
				if ( IOUtil.readBool( in ) )
				{
					ListBandDesign header = (ListBandDesign) readDesign( in );
					list.setHeader( header );
				}
				// read groups
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					ListGroupDesign group = (ListGroupDesign) readDesign( in );
					list.addGroup( group );
				}
				// read detail
				if ( IOUtil.readBool( in ) )
				{
					ListBandDesign detail = (ListBandDesign) readDesign( in );
					list.setDetail( detail );
				}
				// read footer
				if ( IOUtil.readBool( in ) )
				{
					ListBandDesign footer = (ListBandDesign) readDesign( in );
					list.setFooter( footer );
				}
				return list;
			}

			case LIST_GROUP_DESIGN :
			{
				ListGroupDesign listGroup = new ListGroupDesign( );
				readListGroup( dis, listGroup );
				// read header
				if ( IOUtil.readBool( in ) )
				{
					ListBandDesign groupHeader = (ListBandDesign) readDesign( in );
					listGroup.setHeader( groupHeader );
				}
				// read footer
				if ( IOUtil.readBool( in ) )
				{
					ListBandDesign footer = (ListBandDesign) readDesign( in );
					listGroup.setFooter( footer );
				}
				return listGroup;
			}

			case LIST_BAND_DESIGN :
			{
				ListBandDesign listBand = new ListBandDesign( );
				readListBand( dis, listBand );
				// read childern
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					ReportItemDesign item = (ReportItemDesign) readDesign( in );
					listBand.addContent( item );
				}
				return listBand;
			}

			case TABLE_DESIGN :
			{
				TableItemDesign table = new TableItemDesign( );
				readTable( dis, table );
				// read header
				if ( IOUtil.readBool( in ) )
				{
					TableBandDesign header = (TableBandDesign) readDesign( in );
					table.setHeader( header );
				}
				// read groups
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					TableGroupDesign group = (TableGroupDesign) readDesign( in );
					table.addGroup( group );
				}
				// read detail
				if ( IOUtil.readBool( in ) )
				{
					TableBandDesign detail = (TableBandDesign) readDesign( in );
					table.setDetail( detail );
				}
				// read footer
				if ( IOUtil.readBool( in ) )
				{
					TableBandDesign footer = (TableBandDesign) readDesign( in );
					table.setFooter( footer );
				}
				return table;
			}

			case TABLE_GROUP_DESIGN :
			{
				TableGroupDesign tableGroup = new TableGroupDesign( );
				readTableGroup( dis, tableGroup );
				// read header
				if ( IOUtil.readBool( in ) )
				{
					TableBandDesign groupHeader = (TableBandDesign) readDesign( in );
					tableGroup.setHeader( groupHeader );
				}
				// read footer
				if ( IOUtil.readBool( in ) )
				{
					TableBandDesign footer = (TableBandDesign) readDesign( in );
					tableGroup.setFooter( footer );
				}
				return tableGroup;
			}

			case TABLE_BAND_DESIGN :
			{
				TableBandDesign band = new TableBandDesign( );
				readTableBand( dis, band );
				// read childern
				int count = IOUtil.readInt( in );
				for ( int i = 0; i < count; i++ )
				{
					RowDesign row = (RowDesign) readDesign( in );
					band.addContent( row );
				}
				return band;
			}

			case LABEL_DESIGN :
			{
				LabelItemDesign design = new LabelItemDesign( );
				readLabel( dis, design );
				return design;
			}

			case TEXT_DESIGN :
			{
				TextItemDesign design = new TextItemDesign( );
				readText( dis, design );
				return design;
			}
			case DATA_DESIGN :
			{
				DataItemDesign design = new DataItemDesign( );
				readData( dis, design );
				return design;
			}
			case MULTI_LINE_DESIGN :
			{
				MultiLineItemDesign design = new MultiLineItemDesign( );
				readMultiline( dis, design );
				return design;
			}
			case IMAGE_DESIGN :
			{
				ImageItemDesign design = new ImageItemDesign( );
				readImage( dis, design );
				return design;
			}
			case TEMPLATE_DESIGN :
			{
				TemplateDesign design = new TemplateDesign( );
				readTemplate( dis, design );
				return design;
			}
			case EXTENDED_DESIGN :
			{
				ExtendedItemDesign design = new ExtendedItemDesign( );
				readExtended( dis, design );
				return design;
			}
			case AUTO_TEXT_DESIGN :
			{
				AutoTextItemDesign design = new AutoTextItemDesign( );
				readAutoText( dis, design );
				return design;
			}
			default :
				throw new IOException( "unknow design type:" + designType );
		}

	}

	protected void readReportElementField( DataInputStream in,
			ReportElementDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_ID :
				long id = IOUtil.readLong( in );
				design.setID( id );
				break;

			case FIELD_NAME :
				String name = IOUtil.readString( in );
				design.setName( name );
				break;
			case FIELD_EXTENDS :
				String ext = IOUtil.readString( in );
				design.setExtends( ext );
				break;
			case FIELD_JAVA_CLASS :
				String javaClass = IOUtil.readString( in );
				design.setJavaClass( javaClass );
				break;
			case FIELD_NAMED_EXPRESSIONS :
				Map namedExpression = IOUtil.readMap( in );
				if ( namedExpression != null )
				{
					design.getNamedExpressions( ).putAll( namedExpression );
				}
				break;
			case FIELD_CUSTOM_PROPERTIES :
				Map customProperties = IOUtil.readMap( in );
				if ( customProperties != null )
				{
					design.getCustomProperties( ).putAll( customProperties );
				}
				break;
			default :
				throw new IOException( "unknow field type " + fieldType );
		}
	}

	protected void readStyledElementField( DataInputStream in,
			StyledElementDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_STYLE_NAME :
				String styleName = IOUtil.readString( in );
				design.setStyleName( styleName );
				break;
			case FIELD_MAP :
				MapDesign map = readMap( in );
				design.setMap( map );
				break;
			case FIELD_HIGHLIGHT :
				HighlightDesign highlight = readHighlight( in );
				design.setHighlight( highlight );
				break;
			default :
				readReportElementField( in, design, fieldType );
		}
	}

	protected void readReportItemField( DataInputStream in,
			ReportItemDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_X :
				DimensionType x = readDimension( in );
				design.setX( x );
				break;

			case FIELD_Y :
				DimensionType y = readDimension( in );
				design.setY( y );

				break;
			case FIELD_HEIGHT :
				DimensionType height = readDimension( in );
				design.setHeight( height );
				break;
			case FIELD_WIDTH :
				DimensionType width = readDimension( in );
				design.setWidth( width );
				break;
			case FIELD_BOOKMARK :
				String bookmark = IOUtil.readString( in );
				design.setBookmark( bookmark );
				break;
			case FIELD_TOC :

				String toc = IOUtil.readString( in );
				design.setTOC( toc );
				break;
			case FIELD_ON_CREATE :
				String onCreate = IOUtil.readString( in );
				design.setOnCreate( onCreate );
				break;
			case FIELD_ON_RENDER :
				String onRender = IOUtil.readString( in );
				design.setOnRender( onRender );
				break;
			case FIELD_ON_PAGE_BREAK :
				String onPageBreak = IOUtil.readString( in );
				design.setOnPageBreak( onPageBreak );
				break;

			case FIELD_VISIBILITY :
				VisibilityDesign visibility = readVisibility( in );
				design.setVisibility( visibility );
				break;

			case FIELD_ACTION :
				ActionDesign action = readAction( in );
				design.setAction( action );
				break;

			default :
				readStyledElementField( in, design, fieldType );
		}

	}

	protected void readMasterPageField( DataInputStream in,
			MasterPageDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_PAGE_TYPE :
				String pageType = IOUtil.readString( in );
				design.setPageType( pageType );
				break;

			case FIELD_PAGE_SIZE :
				DimensionType width = readDimension( in );
				DimensionType height = readDimension( in );
				design.setPageSize( width, height );
				break;
			case FIELD_MARGIN :
				DimensionType top = readDimension( in );
				DimensionType left = readDimension( in );
				DimensionType bottom = readDimension( in );
				DimensionType right = readDimension( in );
				design.setMargin( top, left, bottom, right );
				break;

			case FIELD_ORIENTATION :
				String orientation = IOUtil.readString( in );
				design.setOrientation( orientation );
				break;

			case FIELD_BODY_STYLE :
				String bodyStyleName = IOUtil.readString( in );
				design.setBodyStyleName( bodyStyleName );
				break;
			default :
				readStyledElementField( in, design, fieldType );

		}

	}

	protected void readSimpleMasterPage( DataInputStream in,
			SimpleMasterPageDesign design ) throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readSimpleMasterPageField( in, design, fieldType );
		}
	}

	protected void readSimpleMasterPageField( DataInputStream in,
			SimpleMasterPageDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_SHOW_HEADER_ON_FIRST :
				boolean showHeaderOnFirst = IOUtil.readBool( in );
				design.setShowHeaderOnFirst( showHeaderOnFirst );
				break;
			case FIELD_SHOW_FOOTER_ON_LAST :
				boolean showFooterOnLast = IOUtil.readBool( in );
				design.setShowFooterOnLast( showFooterOnLast );
				break;
			case FIELD_FLOATING_FOOTER :
				boolean floatingFooter = IOUtil.readBool( in );
				design.setFloatingFooter( floatingFooter );
				break;
			case FEILD_HEADER_HEIGHT :
				DimensionType headerHeight = readDimension( in );
				design.setHeaderHeight( headerHeight );
				break;
			case FEILD_FOOTER_HEIGHT :
				DimensionType footerHeight = readDimension( in );
				design.setFooterHeight( footerHeight );
				break;
			default :
				readMasterPageField( in, design, fieldType );
		}

	}

	protected void readGraphicMasterPageField( DataInputStream in,
			GraphicMasterPageDesign design, short fieldType )
			throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_COLUMNS :
				int columns = IOUtil.readInt( in );
				design.setColumns( columns );
				break;
			case FIELD_COLUMN_SPACING :
				DimensionType columnSpacing = readDimension( in );
				design.setColumnSpacing( columnSpacing );
				break;
			default :
				readMasterPageField( in, design, fieldType );
		}
	}

	protected void readListingField( DataInputStream in, ListingDesign listing,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{

			case FIELD_REPEAT_HEADER :
				boolean repeatHeader = IOUtil.readBool( in );
				listing.setRepeatHeader( repeatHeader );
				break;
			case FIELD_PAGE_BREAK_INTERVAL :
				int pageBreakInterval = IOUtil.readInt( in );
				listing.setPageBreakInterval( pageBreakInterval );
				break;
			default :
				readReportItemField( in, listing, fieldType );
		}
	}

	protected void readGroupField( DataInputStream in, GroupDesign group,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_GROUP_LEVEL :
				int groupLevel = IOUtil.readInt( in );
				group.setGroupLevel( groupLevel );
				break;
			case FIELD_PAGE_BREAK_BEFORE :
				String pageBreakBefore = IOUtil.readString( in );
				group.setPageBreakBefore( pageBreakBefore );
				break;
			case FIELD_PAGE_BREAK_AFTER :
				String pageBreakAfter = IOUtil.readString( in );
				group.setPageBreakAfter( pageBreakAfter );
				break;
			case FIELD_HIDE_DETAIL :
				boolean hideDetail = IOUtil.readBool( in );
				group.setHideDetail( hideDetail );
				break;
			case FIELD_HEADER_REPEAT :
				boolean headerRepeat = IOUtil.readBool( in );
				group.setHeaderRepeat( headerRepeat );
				break;
			default :
				readReportItemField( in, group, fieldType );
		}

	}

	protected void readBandField( DataInputStream in, BandDesign band,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_BAND_TYPE :
				int bandType = IOUtil.readInt( in );
				band.setBandType( bandType );
				break;
			default :
				readReportItemField( in, band, fieldType );
		}
	}

	protected void readList( DataInputStream in, ListItemDesign list )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readListField( in, list, fieldType );
		}
	}

	protected void readListField( DataInputStream in, ListItemDesign list,
			short fieldType ) throws IOException
	{
		readListingField( in, list, fieldType );

	}

	protected void readListGroup( DataInputStream in, ListGroupDesign listGroup )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readListGroupField( in, listGroup, fieldType );
		}
	}

	protected void readListGroupField( DataInputStream in,
			ListGroupDesign group, short fieldType ) throws IOException
	{
		readGroupField( in, group, fieldType );
	}

	protected void readListBand( DataInputStream in, ListBandDesign listBand )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readListBandField( in, listBand, fieldType );
		}
	}

	protected void readListBandField( DataInputStream in,
			ListBandDesign listBand, short fieldType ) throws IOException
	{
		readBandField( in, listBand, fieldType );
	}

	protected void readTable( DataInputStream in, TableItemDesign table )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readTableField( in, table, fieldType );
		}
	}

	protected void readTableField( DataInputStream in, TableItemDesign table,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_CAPTION :
				String captionKey = IOUtil.readString( in );
				String caption = IOUtil.readString( in );
				table.setCaption( captionKey, caption );
				break;
			case FIELD_COLUMNS :
				int columnCount = IOUtil.readInt( in );
				for ( int i = 0; i < columnCount; i++ )
				{
					byte[] bytes = IOUtil.readBytes( in );
					DataInputStream buffer = new DataInputStream(
							new ByteArrayInputStream( bytes ) );
					ColumnDesign column = new ColumnDesign( );
					readColumn( buffer, column );
					table.addColumn( column );
				}
				break;
			default :
				readListingField( in, table, fieldType );
		}

	}

	protected void readTableGroup( DataInputStream in,
			TableGroupDesign tableGroup ) throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readTableGroupField( in, tableGroup, fieldType );
		}
	}

	protected void readTableGroupField( DataInputStream in,
			TableGroupDesign group, short fieldType ) throws IOException
	{
		readGroupField( in, group, fieldType );
	}

	protected void readTableBand( DataInputStream in, TableBandDesign tableBand )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readTableBandField( in, tableBand, fieldType );
		}
	}

	protected void readTableBandField( DataInputStream in,
			TableBandDesign tableBand, short fieldType ) throws IOException
	{
		readBandField( in, tableBand, fieldType );
	}

	protected void readGrid( DataInputStream in, GridItemDesign grid )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readGridField( in, grid, fieldType );
		}
	}

	protected void readGridField( DataInputStream in, GridItemDesign grid,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_COLUMNS :
				int columnCount = IOUtil.readInt( in );
				for ( int i = 0; i < columnCount; i++ )
				{
					byte[] bytes = IOUtil.readBytes( in );
					ColumnDesign column = new ColumnDesign( );
					readColumn( new DataInputStream( new ByteArrayInputStream(
							bytes ) ), column );
					grid.addColumn( column );
				}
				break;
			default :
				readReportItemField( in, grid, fieldType );
		}
	}

	protected void readColumn( DataInputStream in, ColumnDesign column )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readColumnField( in, column, fieldType );
		}
	}

	protected void readColumnField( DataInputStream in, ColumnDesign column,
			short fieldType ) throws IOException
	{

		switch ( fieldType )
		{
			case FIELD_WIDTH :
				DimensionType width = readDimension( in );
				column.setWidth( width );
				break;
			case FIELD_SUPPRESS_DUPLICATE :
				boolean suppressDuplicate = IOUtil.readBool( in );
				column.setSuppressDuplicate( suppressDuplicate );
				break;
			case FIELD_VISIBILITY :
				VisibilityDesign visibility = readVisibility( in );
				column.setVisibility( visibility );
				break;
			case FIELD_HAS_DATA_ITEMS_IN_DETAIL :
				boolean hasDataItemsInDetail = IOUtil.readBool( in );
				column.setHasDataItemsInDetail( hasDataItemsInDetail );
				break;

			default :
				readStyledElementField( in, column, fieldType );
		}

	}

	protected void readRow( DataInputStream in, RowDesign row )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readRowField( in, row, fieldType );
		}
	}

	protected void readRowField( DataInputStream in, RowDesign row,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_IS_START_OF_GROUP :
				boolean isStartOfGroup = IOUtil.readBool( in );
				row.setStartOfGroup( isStartOfGroup );
				break;
			default :
				readReportItemField( in, row, fieldType );
		}
	}

	protected void readCell( DataInputStream in, CellDesign cell )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readCellField( in, cell, fieldType );
		}
	}

	protected void readCellField( DataInputStream in, CellDesign cell,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_COLUMN :
				int column = IOUtil.readInt( in );
				cell.setColumn( column );
				break;
			case FIELD_COL_SPAN :
				int colSpan = IOUtil.readInt( in );
				cell.setColSpan( colSpan );
				break;
			case FIELD_ROW_SPAN :
				int rowSpan = IOUtil.readInt( in );
				cell.setRowSpan( rowSpan );
				break;
			case FIELD_DROP :
				String drop = IOUtil.readString( in );
				cell.setDrop( drop );
				break;
			case FIELD_DISPLAY_GROUP_ICON :
				boolean displayGroupIcon = IOUtil.readBool( in );
				cell.setDisplayGroupIcon( displayGroupIcon );
				break;
			default :
				readReportItemField( in, cell, fieldType );
		}
	}

	protected void readFreeForm( DataInputStream in, FreeFormItemDesign freeForm )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readFreeFormField( in, freeForm, fieldType );
		}
	}

	protected void readFreeFormField( DataInputStream in,
			FreeFormItemDesign freeForm, short fieldType ) throws IOException
	{
		readReportItemField( in, freeForm, fieldType );
	}

	protected void readLabel( DataInputStream in, LabelItemDesign label )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readLabelField( in, label, fieldType );
		}
	}

	protected void readLabelField( DataInputStream in, LabelItemDesign label,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_TEXT :
				String textKey = IOUtil.readString( in );
				String text = IOUtil.readString( in );
				label.setText( textKey, text );
				break;
			case FIELD_HELP_TEXT :
				String helpTextKey = IOUtil.readString( in );
				String helpText = IOUtil.readString( in );
				label.setHelpText( helpTextKey, helpText );
				break;
			default :
				readReportItemField( in, label, fieldType );
		}
	}

	protected void readData( DataInputStream in, DataItemDesign data )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readDataField( in, data, fieldType );
		}
	}

	/**
	 * FIELD_VALUE:
	 * 	this is an old format IR, the field saves the data expressions, like row['field name']. If this 
	 * 	field exits, that also means the FIELD_BINDING_COLUMN is not exits. so we need:
	 * 		1. parser the value expression to get the column name, that's the 'field name' in the expr.
	 * 		2. set the binding column.
	 * FIELD_BOUNDING_COLUMN:
	 * 	This is new format IR, the field saves the field name. We needn't set the value field here, as the
	 * 	setBindingColumn will do it automatically.
	 * @param in
	 * @param data
	 * @param fieldType
	 * @throws IOException
	 */
	protected void readDataField( DataInputStream in, DataItemDesign data,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_VALUE :
				String value = IOUtil.readString( in );
				try
				{
					String columnName = ExpressionUtil
							.getColumnBindingName( value );
					if ( columnName != null )
					{
						data.setBindingColumn( columnName );
					}
				}
				catch ( BirtException ex )
				{
					//just skip this exception
				}
				break;
			case FIELD_BINDING_COLUMN:
				String bindingColumn = IOUtil.readString( in );
				data.setBindingColumn( bindingColumn );
				break;
			case FIELD_SUPPRESS_DUPLICATE :
				boolean suppressDuplicate = IOUtil.readBool( in );
				data.setSuppressDuplicate( suppressDuplicate );
				break;
			case FIELD_HELP_TEXT :
				String helpTextKey = IOUtil.readString( in );
				String helpText = IOUtil.readString( in );
				data.setHelpText( helpTextKey, helpText );
				break;
			default :
				readReportItemField( in, data, fieldType );

		}

	}

	protected void readText( DataInputStream in, TextItemDesign design )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readTextField( in, design, fieldType );
		}
	}

	protected void readTextField( DataInputStream in, TextItemDesign design,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_TEXT_TYPE :
				String textType = IOUtil.readString( in );
				design.setTextType( textType );
				break;
			case FIELD_TEXT :
				String textKey = IOUtil.readString( in );
				String text = IOUtil.readString( in );
				design.setText( textKey, text );
				break;
			default :
				readReportItemField( in, design, fieldType );
		}
	}

	protected void readMultiline( DataInputStream in, MultiLineItemDesign design )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readMultilineField( in, design, fieldType );
		}
	}

	protected void readMultilineField( DataInputStream in,
			MultiLineItemDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_CONTENT_TYPE :
				String contentType = IOUtil.readString( in );
				design.setContentType( contentType );
				break;
			case FIELD_CONTENT :
				String content = IOUtil.readString( in );
				design.setContent( content );
				break;
			default :
				readReportItemField( in, design, fieldType );
		}
	}

	protected void readImage( DataInputStream in, ImageItemDesign image )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readImageField( in, image, fieldType );
		}
	}

	protected void readImageField( DataInputStream in, ImageItemDesign image,
			short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_IMAGE_SOURCE :
				int imageSource = IOUtil.readInt( in );
				switch ( imageSource )
				{
					case ImageItemDesign.IMAGE_NAME :
						String imageName = IOUtil.readString( in );
						image.setImageName( imageName );
						break;
					case ImageItemDesign.IMAGE_FILE :
						String imageFile = IOUtil.readString( in );
						image.setImageFile( imageFile );
						break;
					case ImageItemDesign.IMAGE_URI :
						String imageUri = IOUtil.readString( in );
						image.setImageUri( imageUri );
						break;
					case ImageItemDesign.IMAGE_EXPRESSION :
						String imageExpr = IOUtil.readString( in );
						String imageFormat = IOUtil.readString( in );
						image.setImageExpression( imageExpr, imageFormat );
						break;
					default :
						throw new IOException( "invalid image source: "
								+ imageSource );
				}
				break;
			case FIELD_ALT_TEXT :
				String altTextKey = IOUtil.readString( in );
				String altText = IOUtil.readString( in );
				image.setAltText( altTextKey, altText );
				break;
			case FIELD_HELP_TEXT :
				String helpTextKey = IOUtil.readString( in );
				String helpText = IOUtil.readString( in );
				image.setHelpText( helpTextKey, helpText );
				break;
			default :
				readReportItemField( in, image, fieldType );
		}
	}

	protected void readExtended( DataInputStream in, ExtendedItemDesign extended )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readExtendedField( in, extended, fieldType );
		}
	}

	protected void readExtendedField( DataInputStream in,
			ExtendedItemDesign extended, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_ALT_TEXT :
				String altTextKey = IOUtil.readString( in );
				String altText = IOUtil.readString( in );
				extended.setAltText( altTextKey, altText );
				break;
			default :
				readReportItemField( in, extended, fieldType );
		}

	}

	protected void readAutoText( DataInputStream in, AutoTextItemDesign autoText )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readAutoTextField( in, autoText, fieldType );
		}
	}

	protected void readAutoTextField( DataInputStream in,
			AutoTextItemDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_TYPE :
				String type = IOUtil.readString( in );
				design.setType( type );
				break;
			case FIELD_TEXT :
				String textKey = IOUtil.readString( in );
				String text = IOUtil.readString( in );
				design.setText( textKey, text );
				break;
			default :
				readReportItemField( in, design, fieldType );
		}
	}

	protected void readTemplate( DataInputStream in, TemplateDesign design )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readTemplateField( in, design, fieldType );
		}
	}

	protected void readTemplateField( DataInputStream in,
			TemplateDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{

			case FIELD_ALLOWED_TYPE :
				String allowedType = IOUtil.readString( in );
				design.setAllowedType( allowedType );
				break;

			case FIELD_PROMPT_TEXT :
				String promptTextKey = IOUtil.readString( in );
				String promptText = IOUtil.readString( in );
				design.setPromptText( promptText );
				design.setPromptTextKey( promptTextKey );
				break;
			default :
				readReportItemField( in, design, fieldType );

		}
	}

	protected DimensionType readDimension( DataInputStream in )
			throws IOException
	{
		boolean notNull = IOUtil.readBool( in );
		if ( notNull )
		{
			DimensionType dimension = new DimensionType( );
			dimension.readObject( in );
			return dimension;
		}
		return null;
	}

	protected VisibilityDesign readVisibility( DataInputStream in )
			throws IOException
	{
		VisibilityDesign visibility = new VisibilityDesign( );
		int ruleCount = IOUtil.readInt( in );
		for ( int i = 0; i < ruleCount; i++ )
		{
			VisibilityRuleDesign rule = new VisibilityRuleDesign( );
			String format = IOUtil.readString( in );
			String expr = IOUtil.readString( in );
			rule.setFormat( format );
			rule.setExpression( expr );
			visibility.addRule( rule );
		}
		return visibility;
	}

	protected MapDesign readMap( DataInputStream in ) throws IOException
	{
		MapDesign map = new MapDesign( );
		int ruleCount = IOUtil.readInt( in );
		for ( int i = 0; i < ruleCount; i++ )
		{
			MapRuleDesign rule = new MapRuleDesign( );;
			String testExpr = IOUtil.readString( in );
			String oper = IOUtil.readString( in );
			String value1 = IOUtil.readString( in );
			String value2 = IOUtil.readString( in );
			String displayText = IOUtil.readString( in );
			String displayKey = IOUtil.readString( in );
			rule.setTestExpression( testExpr );
			rule.setExpression( oper, value1, value2 );
			rule.setDisplayText( displayKey, displayText );
			map.addRule( rule );
		}
		return map;
	}

	protected HighlightDesign readHighlight( DataInputStream in )
			throws IOException
	{
		HighlightDesign highlight = new HighlightDesign( );
		int ruleCount = IOUtil.readInt( in );
		for ( int i = 0; i < ruleCount; i++ )
		{
			HighlightRuleDesign rule = new HighlightRuleDesign( );
			String testExpr = IOUtil.readString( in );
			String oper = IOUtil.readString( in );
			String value1 = IOUtil.readString( in );
			String value2 = IOUtil.readString( in );
			IStyle style = readStyle( in );
			rule.setTestExpression( testExpr );
			rule.setExpression( oper, value1, value2 );
			rule.setStyle( style );
			highlight.addRule( rule );
		}
		return highlight;
	}

	// /FIXME: we need a more fast method
	protected IStyle readStyle( DataInputStream in ) throws IOException
	{
		String cssText = IOUtil.readString( in );
		IStyle style = (IStyle) reportDesign.getCSSEngine( )
				.parseStyleDeclaration( cssText );
		return style;
	}

	protected ActionDesign readAction( DataInputStream in ) throws IOException
	{
		ActionDesign action = new ActionDesign( );
		int actionType = IOUtil.readInt( in );
		switch ( actionType )
		{
			case ActionDesign.ACTION_BOOKMARK :
				String bookmark = IOUtil.readString( in );
				action.setBookmark( bookmark );
				break;
			case ActionDesign.ACTION_DRILLTHROUGH :
				DrillThroughActionDesign drillThrough = readDrillThrough( in );
				action.setDrillThrough( drillThrough );
				break;
			case ActionDesign.ACTION_HYPERLINK :
				String hyperlink = IOUtil.readString( in );
				action.setHyperlink( hyperlink );
				break;
			default :
				throw new IOException( "invalid action type:" + actionType );
		}
		boolean isBookmark = IOUtil.readBool( in );
		String targetWindow = IOUtil.readString( in );

		action.setBookmarkType( isBookmark );
		action.setTargetWindow( targetWindow );
		return action;
	}

	protected DrillThroughActionDesign readDrillThrough( DataInputStream in )
			throws IOException
	{
		DrillThroughActionDesign drillThrough = new DrillThroughActionDesign( );
		String reportName = IOUtil.readString( in );
		Map parameters = IOUtil.readMap( in );
		Map search = IOUtil.readMap( in );
		String format = IOUtil.readString( in );
		boolean isBookmark = IOUtil.readBool( in );
		String bookmark = IOUtil.readString( in );

		drillThrough.setReportName( reportName );
		drillThrough.setParameters( parameters );
		drillThrough.setSearch( search );
		drillThrough.setFormat( format );
		drillThrough.setBookmarkType( isBookmark );
		drillThrough.setBookmark( bookmark );

		return drillThrough;
	}

	protected class ReportItemVisitor extends DefaultReportItemVisitorImpl
	{

		ReportDesignHandle handle;
		Report report;

		ReportItemVisitor( ReportDesignHandle handle, Report report )
		{
			this.handle = handle;
			this.report = report;
		}

		public void link( )
		{
			report.setReportDesign( handle );
			// link the master pages
			PageSetupDesign pageSetup = report.getPageSetup( );
			int masterPageCount = pageSetup.getMasterPageCount( );
			for ( int i = 0; i < masterPageCount; i++ )
			{
				SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) pageSetup
						.getMasterPage( i );
				linkReportElement( masterPage );
				int count = masterPage.getHeaderCount( );
				for ( int j = 0; j < count; j++ )
				{
					ReportItemDesign item = masterPage.getHeader( j );
					item.accept( this, null );
				}
				count = masterPage.getFooterCount( );
				for ( int j = 0; j < count; j++ )
				{
					ReportItemDesign item = masterPage.getFooter( j );
					item.accept( this, null );
				}
			}
			// link the body contents
			int count = report.getContentCount( );
			for ( int i = 0; i < count; i++ )
			{
				ReportItemDesign item = report.getContent( i );
				item.accept( this, null );
			}
		}

		protected void linkReportElement( ReportElementDesign element )
		{
			long id = element.getID( );
			DesignElementHandle elementHandle = handle.getElementByID( id );
			element.setHandle( elementHandle );
			report.setReportItemInstanceID( id, element );
		}

		public Object visitBand( BandDesign band, Object value )
		{
			linkReportElement( band );
			int count = band.getContentCount( );
			for ( int i = 0; i < count; i++ )
			{
				ReportItemDesign item = band.getContent( i );
				item.accept( this, value );
			}
			return value;
		}

		public Object visitCell( CellDesign cell, Object value )
		{
			linkReportElement( cell );
			int count = cell.getContentCount( );
			for ( int i = 0; i < count; i++ )
			{
				ReportItemDesign item = cell.getContent( i );
				item.accept( this, value );
			}
			return value;
		}

		public Object visitFreeFormItem( FreeFormItemDesign container,
				Object value )
		{
			linkReportElement( container );
			int count = container.getItemCount( );
			for ( int i = 0; i < count; i++ )
			{
				ReportItemDesign item = container.getItem( i );
				item.accept( this, value );
			}

			return value;
		}

		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			linkReportElement( grid );

			int count = grid.getColumnCount( );
			for ( int i = 0; i < count; i++ )
			{
				ColumnDesign column = grid.getColumn( i );
				linkReportElement( column );
			}

			count = grid.getRowCount( );
			for ( int i = 0; i < count; i++ )
			{
				ReportItemDesign item = grid.getRow( i );
				item.accept( this, value );
			}

			return value;
		}

		public Object visitGroup( GroupDesign group, Object value )
		{
			linkReportElement( group );
			BandDesign header = group.getHeader( );
			if ( header != null )
			{
				header.accept( this, value );
			}
			BandDesign footer = group.getFooter( );
			if ( footer != null )
			{
				footer.accept( this, value );
			}
			return value;
		}

		public Object visitListing( ListingDesign listing, Object value )
		{
			linkReportElement( listing );
			BandDesign header = listing.getHeader( );
			if ( header != null )
			{
				header.accept( this, value );
			}
			int count = listing.getGroupCount( );
			for ( int i = 0; i < count; i++ )
			{
				GroupDesign group = listing.getGroup( i );
				group.accept( this, value );
			}
			BandDesign detail = listing.getDetail( );
			if ( detail != null )
			{
				detail.accept( this, null );
			}
			BandDesign footer = listing.getFooter( );
			if ( footer != null )
			{
				footer.accept( this, value );
			}
			return value;

		}

		public Object visitTableItem( TableItemDesign table, Object value )
		{
			visitListing( table, value );

			int count = table.getColumnCount( );
			for ( int i = 0; i < count; i++ )
			{
				ColumnDesign column = table.getColumn( i );
				linkReportElement( column );
			}
			return value;
		}

		public Object visitReportItem( ReportItemDesign item, Object value )
		{
			linkReportElement( item );
			return value;
		}

		public Object visitRow( RowDesign row, Object value )
		{
			linkReportElement( row );
			int count = row.getCellCount( );
			for ( int i = 0; i < count; i++ )
			{
				CellDesign cell = row.getCell( i );
				cell.accept( this, value );
			}
			return value;
		}
	}
}
