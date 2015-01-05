/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * Read the transfered engine IR designs wrote in EngineIRWriter.
 * The reading sequence of report root:
 *    1. Version. Version id stored in IR, to remember the document changes.
 *    2. Report Version.
 *    *  in version 0 and 1, read:
 *          base path
 *          unit  
 *    4. Report
 * readDesign:
 *    1. Read design type
 *    2. Read report item design according design type.
 *    3. Read the current design's fields.
 *    4. Read the current design's children.
 *    
 * Version 1: remove read isBookmark of ActionDesign.
 * Version 2: remove read base path and unit of report.
 * Version 3: add extended item's children.
 * Version 4: change the way of writing and reading the style.
 */
public class EngineIRReaderImpl implements IOConstants
{

	protected Report reportDesign;
	protected String scriptLanguage = Expression.SCRIPT_JAVASCRIPT;
	
	/**
	 * The version wrote in EngineIRWriter.
	 */
	protected long version;
	
	protected boolean checkDesignVersion = false;

	protected DataInputStream dis;

	protected EngineIRReaderImpl( DataInputStream dis,
			boolean checkDesignVersion )
	{
		this.checkDesignVersion = checkDesignVersion;
		this.dis = dis;
	}
	
	public Report read( ) throws IOException
	{
		String designVersion = IOUtil.readString( dis );
		if ( checkDesignVersion && !DesignSchemaConstants.REPORT_VERSION.equals( designVersion ) )
		{
			throw new IOException( "un-compatable design version" //$NON-NLS-1$
					+ designVersion );
		}

		reportDesign = new Report( );

		if ( version == ENGINE_IR_VERSION_0 || version == ENGINE_IR_VERSION_1 )
		{
			// design's base path, removed from version 2
			IOUtil.readString( dis );
		
			// design's unit, removed from version 2
			IOUtil.readString( dis );
		}

		if ( version <= ENGINE_IR_VERSION_3 )
		{
			readReportV1_3( dis );
		}
		else
		{
			readReport( dis );
		}

		return reportDesign;
	}
	
	/*
	 * read the report of ENGINE_IR_VERSION_0, ENGINE_IR_VERSION_1,
	 * ENGINE_IR_VERSION_2 and ENGINE_IR_VERSION_3
	 */
	private void readReportV1_3( DataInputStream dis ) throws IOException
	{
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
		Map<String, Expression> namedExpressions = readExprMap( dis );
		if ( namedExpressions != null && !namedExpressions.isEmpty( ) )
		{
			reportDesign.setUserProperties( namedExpressions );
		}

		// page setup
		PageSetupDesign pageSetup = reportDesign.getPageSetup( );
		int masterPageCount = IOUtil.readInt( dis );
		for ( int i = 0; i < masterPageCount; i++ )
		{
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) readDesign( dis );
			pageSetup.addMasterPage( masterPage );
		}

		// read the body content
		int count = IOUtil.readInt( dis );
		for ( int i = 0; i < count; i++ )
		{
			ReportItemDesign item = (ReportItemDesign) readDesign( dis );
			reportDesign.addContent( item );
		}
	}
	
	private void readReport( DataInputStream dis ) throws IOException
	{
		// read how many segments in the report
		short num = IOUtil.readShort( dis );
		for ( short i = 0; i < num; i++ )
		{
			short reportSegmentType = IOUtil.readShort( dis );
			switch ( reportSegmentType )
			{
				case FIELD_REPORT_STYLES :
					readReportSytles( dis );
					break;
				case FIELD_REPORT_NAMED_EXPRESSIONS :
				case FIELD_REPORT_USER_PROPERTIES :
					readReportUserProperties( dis );
					break;
				case FIELD_REPORT_MASTER_PAGES :
					readReportPageSetup( dis );
					break;
				case FIELD_REPORT_BODY :
					readReportBodyContent( dis );
					break;
				case FIELD_REPORT_VARIABLE :
					readReportVariable( dis );
					break;
				case FIELD_ON_PAGE_START :
					Expression onPageStart = readExpression( dis );
					reportDesign.setOnPageStart( onPageStart );
					break;

				case FIELD_ON_PAGE_END :
					Expression onPageEnd = readExpression( dis );
					reportDesign.setOnPageStart( onPageEnd );
					break;
				case FIELD_REPORT_VERSION :
					String version = IOUtil.readString( dis );
					reportDesign.setVersion( version );
					break;
				case FIELD_REPORT_SCRIPT_LANGUAGE :
					scriptLanguage = IOUtil.readString( dis );
					break;
				case FIELD_REPORT_LOCALE :
					String locale = IOUtil.readString( dis );
					reportDesign.setLocale( locale );
					break;
				default :
					throw new IOException( "unknow report segment type:" + reportSegmentType ); //$NON-NLS-1$
			}
		}
	}
	
	private void readReportSytles( DataInputStream dis ) throws IOException
	{
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
	}

	private void readReportUserProperties( DataInputStream dis )
			throws IOException
	{
		// user properties
		Map<String, Expression> userProperties = readExprMap( dis );
		if ( userProperties != null && !userProperties.isEmpty( ) )
		{
			reportDesign.setUserProperties( userProperties );
		}
	}

	private void readReportPageSetup( DataInputStream dis ) throws IOException
	{
		// page setup
		PageSetupDesign pageSetup = reportDesign.getPageSetup( );
		int masterPageCount = IOUtil.readInt( dis );
		for ( int i = 0; i < masterPageCount; i++ )
		{
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) readDesign( dis );
			pageSetup.addMasterPage( masterPage );
		}
	}

	private void readReportBodyContent( DataInputStream dis )
			throws IOException
	{
		// read the body content
		int count = IOUtil.readInt( dis );
		for ( int i = 0; i < count; i++ )
		{
			ReportItemDesign item = (ReportItemDesign) readDesign( dis );
			reportDesign.addContent( item );
		}
	}

	private void readReportVariable( DataInputStream dis ) throws IOException
	{
		// style informations
		Collection<PageVariableDesign> vars = reportDesign.getPageVariables( );
		int varCount = IOUtil.readInt( dis );
		for ( int i = 0; i < varCount; i++ )
		{
			String varName = IOUtil.readString( dis );
			String varScope = IOUtil.readString( dis );
			Expression expr = readExpression( dis );
			PageVariableDesign varDesign = new PageVariableDesign( varName,
					varScope );
			varDesign.setDefaultValue( expr );
			vars.add( varDesign );
		}
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
				// read children
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
				// read children
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
				DynamicTextItemDesign design = new DynamicTextItemDesign( );
				readDynamicText( dis, design );
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
				if ( version >= ENGINE_IR_VERSION_3 )
				{
					// read children
					int count = IOUtil.readInt( in );
					for ( int i = 0; i < count; i++ )
					{
						design.getChildren( ).add( readDesign( in ) );
					}
				}
				return design;
			}
			case AUTO_TEXT_DESIGN :
			{
				AutoTextItemDesign design = new AutoTextItemDesign( );
				readAutoText( dis, design );
				return design;
			}
			default :
				throw new IOException( "unknow design type:" + designType ); //$NON-NLS-1$
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
			case FIELD_USER_PROPERTIES :
				//the named expression has been replaced by the user properties
				Map<String, Expression> userProperties = readExprMap( in );
				if ( userProperties != null && !userProperties.isEmpty( ) )
				{
					design.setUserProperties( userProperties );
				}
				break;
			case FIELD_CUSTOM_PROPERTIES :
				// skip the custom properties
				IOUtil.readMap( in );
				break;
			default :
				throw new IOException( "unknow field type " + fieldType ); //$NON-NLS-1$
		}
	}

	protected void readStyledElementField( DataInputStream in,
			StyledElementDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_STYLE_NAME :
				String styleClass = IOUtil.readString( in );
				design.setStyleName( styleClass );
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
				design.setX( readDimension( in ) );
				break;
			case FIELD_Y :
				design.setY( readDimension( in ) );
				break;
			case FIELD_HEIGHT :
				design.setHeight( readDimension( in ) );
				break;
			case FIELD_WIDTH :
				design.setWidth( readDimension( in ) );
				break;
			case FIELD_BOOKMARK :
				Expression bookmark = readExpression( in );
				design.setBookmark( bookmark );
				break;
			case FIELD_TOC :
				Expression toc = readExpression( in );
				design.setTOC( toc );
				break;
			case FIELD_ON_CREATE :
				Expression onCreate = readExpression( in );
				design.setOnCreate( onCreate );
				break;
			case FIELD_ON_RENDER :
				Expression onRender = readExpression( in );
				design.setOnRender( onRender );
				break;
			case FIELD_ON_PAGE_BREAK :
				Expression onPageBreak = readExpression( in );
				design.setOnPageBreak( onPageBreak );
				break;
				
			case FIELD_ALT_TEXT :
				String altTextKey = IOUtil.readString( in );
				Expression altTextExpr;
				if( version < ENGINE_IR_VERSION_9)
				{
					String altText = IOUtil.readString( in );
					altTextExpr = Expression.newConstant( altText );
				}
				else
				{
					altTextExpr = readExpression(in);
				}
				design.setAltText( altTextExpr );
				design.setAltTextKey( altTextKey );
				break;

			case FIELD_VISIBILITY :
				VisibilityDesign visibility = readVisibility( in );
				design.setVisibility( visibility );
				break;

			case FIELD_ACTION :
				ActionDesign action = readAction( in );
				design.setAction( action );
				break;
				
			case FIELD_ACTION_V1 :
				ActionDesign action1 = readActionV1( in );
				design.setAction( action1 );
				break;
				
			case FIELD_USE_CACHED_RESULT :
				boolean useCachedResult = IOUtil.readBool( in );
				design.setUseCachedResult( useCachedResult );
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
				listing.setRepeatHeader( IOUtil.readBool( in ) );
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
			case FIELD_PAGE_BREAK_INSIDE :
				String pageBreakInside = IOUtil.readString( in );
				group.setPageBreakInside( pageBreakInside );
				break;
			case FIELD_HIDE_DETAIL :
				boolean hideDetail = IOUtil.readBool( in );
				group.setHideDetail( hideDetail );
				break;
			case FIELD_HEADER_REPEAT :
				Boolean headerRepeat = IOUtil.readBool( in );
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
			case FIELD_SUMMARY :
				String summary = IOUtil.readString( in );
				table.setSummary( summary );
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
			case FIELD_CAPTION :
				String captionKey = IOUtil.readString( in );
				String caption = IOUtil.readString( in );
				grid.setCaption( captionKey, caption );
				break;
			case FIELD_SUMMARY:
				String summary = IOUtil.readString( in );
				grid.setSummary( summary );
				break;
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
			case FIELD_IS_COLUMN_HEADER :
				boolean isColumnHeader = IOUtil.readBool( in );
				column.setColumnHeaderState( isColumnHeader );
				break;
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
			case FIELD_IS_REPEATABLE :
				boolean isRepeatable = IOUtil.readBool( in );
				row.setRepeatable( isRepeatable );
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
			case FIELD_DIAGONAL_NUMBER :
				int diagonalNumber = IOUtil.readInt( in );
				cell.setDiagonalNumber( diagonalNumber );
				break;
			case FIELD_DIAGONAL_STYLE :
				String diagonalStyle = IOUtil.readString( in );
				cell.setDiagonalStyle( diagonalStyle );
				break;
			case FIELD_DIAGONAL_WIDTH :
				DimensionType diagonalWidth = readDimension( in );
				cell.setDiagonalWidth( diagonalWidth );
				break;
			case FIELD_DIAGONAL_COLOR :
				String diagonalColor = IOUtil.readString( in );
				cell.setDiagonalColor( diagonalColor );
				break;
			case FIELD_ANTIDIAGONAL_NUMBER :
				int antidiagonalNumber = IOUtil.readInt( in );
				cell.setAntidiagonalNumber( antidiagonalNumber );
				break;
			case FIELD_ANTIDIAGONAL_STYLE :
				String antidiagonalStyle = IOUtil.readString( in );
				cell.setAntidiagonalStyle( antidiagonalStyle );
				break;
			case FIELD_ANTIDIAGONAL_WIDTH :
				DimensionType antidiagonalWidth = readDimension( in );
				cell.setAntidiagonalWidth( antidiagonalWidth );
				break;
			case FIELD_ANTIDIAGONAL_COLOR :
				String antidiagonalColor = IOUtil.readString( in );
				cell.setAntidiagonalColor( antidiagonalColor );
				break;
			case FIELD_HEADERS:
				Expression headers = readExpression( in );
				cell.setHeaders( headers );
				break;
			case FIELD_SCOPE:
				String scope = IOUtil.readString( in );
				cell.setScope( scope );
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
			case FIELD_NEED_REFRESH_MAPPING:
				IOUtil.readBool( in );
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
			case FIELD_TEXT_HAS_EXPRESSION :
				boolean hasExpr = IOUtil.readBool( in );
				design.setHasExpression( hasExpr );
				break;
			default :
				readReportItemField( in, design, fieldType );
		}
	}

	protected void readDynamicText( DataInputStream in, DynamicTextItemDesign design )
			throws IOException
	{
		while ( in.available( ) > 0 )
		{
			short fieldType = IOUtil.readShort( in );
			readDynamicTextField( in, design, fieldType );
		}
	}

	protected void readDynamicTextField( DataInputStream in,
			DynamicTextItemDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_CONTENT_TYPE :
				String contentType = IOUtil.readString( in );
				design.setContentType( contentType );
				break;
			case FIELD_CONTENT :
				Expression content = readExpression( in );
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
						Expression imageName = readExpression( in );
						image.setImageName( imageName );
						break;
					case ImageItemDesign.IMAGE_FILE :
						Expression imageFile = readExpression( in );
						image.setImageFile( imageFile );
						break;
					case ImageItemDesign.IMAGE_URI :
						Expression imageUri = readExpression( in );
						image.setImageUri( imageUri );
						break;
					case ImageItemDesign.IMAGE_EXPRESSION :
						Expression imageExpr = readExpression( in );
						Expression imageFormat = readExpression( in );
						image.setImageExpression( imageExpr, imageFormat );
						break;
					default :
						throw new IOException( "invalid image source: " //$NON-NLS-1$
								+ imageSource );
				}
				break;
			case FIELD_HELP_TEXT :
				String helpTextKey = IOUtil.readString( in );
				String helpText = IOUtil.readString( in );
				image.setHelpText( helpTextKey, helpText );
				break;
			case FIELD_FIT_TO_CONTAINER:
				Boolean isFitToContainer = IOUtil.readBool( in );
				image.setFitToContainer( isFitToContainer );
				break;
			case FIELD_PROPORTIONAL_SCALE :
				image.setProportionalScale( IOUtil.readBool( in ) );
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
		readReportItemField( in, extended, fieldType );
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
			Expression expr = readExpression( in );
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
			readRuleDesign( in, rule );
			String displayText = IOUtil.readString( in );
			String displayKey = IOUtil.readString( in );
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
			readRuleDesign( in, rule );
			IStyle style = readStyle( in );
			rule.setStyle( style );
			highlight.addRule( rule );
		}
		return highlight;
	}

	protected void readRuleDesign( DataInputStream in, RuleDesign rule )
			throws IOException
	{
		if ( version < ENGINE_IR_VERSION_7 )
		{
			readRuleDesignV6( in, rule );
		}
		else
		{
			readRuleDesignV7( in, rule );
		}
	}

	protected void readRuleDesignV6( DataInputStream in, RuleDesign rule )
			throws IOException
	{
		String testExpr = IOUtil.readString( in );
		String oper = IOUtil.readString( in );
		Object object1 = IOUtil.readObject( in );
		Object object2 = IOUtil.readObject( in );
		rule.setTestExpression( Expression.newScript( testExpr ) );
		if ( object1 instanceof List<?> )
		{
			List<?> list = (List<?>) object1;
			List<Expression> exprs = new ArrayList<Expression>( list.size( ) );
			for ( Object expr : list )
			{
				if ( expr instanceof String )
				{
					exprs.add( Expression.newScript( (String) expr ) );
				}
				else
				{
					exprs.add( null );
				}
			}
			rule.setExpression( oper, exprs );
			rule.setValueIsList( true );
		}
		else
		{
			Expression expr1 = object1 == null ? null : Expression
					.newScript( (String) object1 );
			Expression expr2 = object2 == null ? null : Expression
					.newScript( (String) object2 );
			rule.setExpression( oper, expr1, expr2 );
			rule.setValueIsList( false );
		}
	}

	protected void readRuleDesignV7( DataInputStream in, RuleDesign rule )
			throws IOException
	{
		Expression testExpr = readExpression( in );
		String oper = IOUtil.readString( in );
		boolean isList = IOUtil.readBool( in );
		if ( isList )
		{
			int size = IOUtil.readInt( in );
			ArrayList<Expression> exprs = new ArrayList<Expression>( size );
			for ( int i = 0; i < size; i++ )
			{
				Expression expr = readExpression( in );
				exprs.add( expr );
			}
			rule.setExpression( oper, exprs );
		}
		else
		{
			Expression expr1 = readExpression( in );
			Expression expr2 = readExpression( in );
			rule.setExpression( oper, expr1, expr2 );
		}
		rule.setValueIsList( isList );
		rule.setTestExpression( testExpr );
	}

	protected IStyle readStyle( DataInputStream in ) throws IOException
	{
		if ( version <= ENGINE_IR_VERSION_3 )
		{
			String cssText = IOUtil.readString( in );
			IStyle style = (IStyle) reportDesign.getCSSEngine( )
					.parseStyleDeclaration( cssText );
			return style;
		}

		IStyle style = new StyleDeclaration( reportDesign.getCSSEngine( ) );
		if ( null != style )
		{
			style.read( in );
		}
		return style;
	}

	protected ActionDesign readAction( DataInputStream in ) throws IOException
	{
		ActionDesign action = new ActionDesign( );
		int actionType = IOUtil.readInt( in );
		switch ( actionType )
		{
			case ActionDesign.ACTION_BOOKMARK :
				Expression bookmark = readExpression( in );
				action.setBookmark( bookmark );
				break;
			case ActionDesign.ACTION_DRILLTHROUGH :
				DrillThroughActionDesign drillThrough = readDrillThrough( in );
				action.setDrillThrough( drillThrough );
				break;
			case ActionDesign.ACTION_HYPERLINK :
				Expression hyperlink = readExpression( in );
				action.setHyperlink( hyperlink );
				break;
			default :
				throw new IOException( "invalid action type:" + actionType ); //$NON-NLS-1$
		}
		if ( version == ENGINE_IR_VERSION_0 )
		{
			// We remove isBookmark of ActionDesign from version 1.
			IOUtil.readBool( in );
		}
		String targetWindow = IOUtil.readString( in );
		action.setTargetWindow( targetWindow );
		return action;
	}

	protected ActionDesign readActionV1( DataInputStream in )
			throws IOException
	{
		ActionDesign action = readAction( in );
		String tooltip = IOUtil.readString( in );
		action.setTooltip( tooltip );
		return action;
	}

	protected DrillThroughActionDesign readDrillThrough( DataInputStream in )
			throws IOException
	{
		DrillThroughActionDesign drillThrough = new DrillThroughActionDesign( );
		Expression reportName = readExpression( in );
		String fileType = null;
		if ( version >= ENGINE_IR_VERSION_7 )
		{
			fileType = IOUtil.readString( in );
		}
		Map<String, List<Expression>> parameters = readDrillThroughExprMap( in );
		Map search = IOUtil.readMap( in );
		String format = IOUtil.readString( in );
		boolean bookmarkType = IOUtil.readBool( in );
		drillThrough.setBookmarkType( bookmarkType );
		Expression bookmark = readExpression( in );

		drillThrough.setReportName( reportName );
		drillThrough.setTargetFileType( fileType );
		drillThrough.setParameters( parameters );
		drillThrough.setSearch( search );
		drillThrough.setFormat( format );
		drillThrough.setBookmark( bookmark );

		return drillThrough;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Expression> readExprMap( DataInputStream dis )
			throws IOException
	{
		HashMap<String, Expression> exprs = new HashMap<String, Expression>( );
		if ( version < ENGINE_IR_VERSION_7 )
		{
			Map<String, String> map = IOUtil.readMap( dis );
			for ( Map.Entry<String, String> entry : map.entrySet( ) )
			{
				String name = entry.getKey( );
				Expression expr = Expression.newScript( entry.getValue( ) );
				exprs.put( name, expr );
			}
		}
		else
		{
			int size = IOUtil.readInt( dis );
			for ( int i = 0; i < size; i++ )
			{
				String name = IOUtil.readString( dis );
				Expression expr = readExpression( dis );
				exprs.put( name, expr );
			}
		}
		return exprs;
	}
	
	private Map<String, List<Expression>> readDrillThroughExprMap( DataInputStream dis )
			throws IOException
	{
		HashMap<String, List<Expression>> exprs = new HashMap<String, List<Expression>>( );
		if ( version < ENGINE_IR_VERSION_7 )
		{
			Map<String, String> map = IOUtil.readMap( dis );
			for ( Map.Entry<String, String> entry : map.entrySet( ) )
			{
				String name = entry.getKey( );
				Expression expr = Expression.newScript( entry.getValue( ) );
				ArrayList<Expression> exprList = new ArrayList<Expression>( 1 );
				exprList.add( expr );
				exprs.put( name, exprList );
			}
		}
		else if ( version < ENGINE_IR_VERSION_8 )
		{
			int size = IOUtil.readInt( dis );
			for ( int i = 0; i < size; i++ )
			{
				String name = IOUtil.readString( dis );
				Expression expr = readExpression( dis );
				ArrayList<Expression> exprList = new ArrayList<Expression>( 1 );
				exprList.add( expr );
				exprs.put( name, exprList );
			}
		}
		else
		{
			int size = IOUtil.readInt( dis );
			for ( int i = 0; i < size; i++ )
			{
				String name = IOUtil.readString( dis );
				int exprSize = IOUtil.readInt( dis );
				ArrayList<Expression> exprList = new ArrayList<Expression>( exprSize );
				for ( int j = 0; j< exprSize; j++ )
				{
					Expression expr = readExpression( dis );
					exprList.add( expr );
				}
				exprs.put( name, exprList );
			}
		}
		return exprs;
	}

	protected Expression readExpression( DataInputStream in )
			throws IOException
	{
		if ( version < ENGINE_IR_VERSION_7 )
		{
			String scriptText = IOUtil.readString( in );
			if ( scriptText != null )
			{
				return Expression.newScript( scriptText );
			}
			return null;
		}
		boolean isNull = IOUtil.readBool( in );
		if ( !isNull )
		{
			int exprType = IOUtil.readInt( in );
			switch ( exprType )
			{
				case Expression.CONSTANT :
					int valueType = IOUtil.readInt( in );
					String scriptText = IOUtil.readString( in );
					return Expression.newConstant( valueType, scriptText );
				case Expression.SCRIPT :
					int scriptType = IOUtil.readShort( in );
					if ( scriptType == FIELD_EXPRESSION_WITH_LANGUAGE )
					{
						String language = IOUtil.readString( in );
						String expression = IOUtil.readString( in );
						return Expression.newScript( language, expression );
					}
					else if ( scriptType == FIELD_EXPRESSION_WITHOUT_LANGUAGE )
					{
						String expression = IOUtil.readString( in );
						return Expression.newScript( expression );
					}
			}
		}
		return null;
	}
}
