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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.ir.Expression.JSExpression;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
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

		// read the body conent
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
				case FIELD_ROOT_STYLE :
					reportDesign.setRootStyle( readStyle( dis ) );
					break;
				case FIELD_REPORT_NAMED_EXPRESSIONS :
					readReportNamedExpressions( dis );
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
					String scriptText = IOUtil.readString( dis );
					if ( scriptText != null )
					{
						ScriptExpression script = new ScriptExpression(
								scriptText );
						reportDesign.setOnPageStart( script );
					}
					break;

				case FIELD_ON_PAGE_END:
					scriptText = IOUtil.readString( dis );
					if ( scriptText != null )
					{
						ScriptExpression script = new ScriptExpression(
								scriptText );
						reportDesign.setOnPageEnd( script );
					}
					break;

				default :
					throw new IOException( "unknow report segment type:" + reportSegmentType ); //$NON-NLS-1$
			}
		}
		// If document is generated by a design not supporting root style,
		// then get the root style by root style name.
		String rootStyleName = reportDesign.getRootStyleName( );
		if ( rootStyleName != null && reportDesign.getRootStyle( ) == null )
		{
			reportDesign.setRootStyle( reportDesign.findStyle( rootStyleName ) );
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
	
	private void readReportNamedExpressions( DataInputStream dis )
			throws IOException
	{
		// named expression
		Map namedExpressions = IOUtil.readMap( dis );
		if ( namedExpressions != null )
		{
			reportDesign.getNamedExpressions( ).putAll( namedExpressions );
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
		// read the body conent
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
			vars.add( new PageVariableDesign( varName, varScope ) );
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
				processClassStyleCompatibility( masterPage );
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
				processClassStyleCompatibility( grid );
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
				processClassStyleCompatibility( freeForm );
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
				processClassStyleCompatibility( row );
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
				processClassStyleCompatibility( cell );
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
				processClassStyleCompatibility( list );
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
				processClassStyleCompatibility( listGroup );
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
				processClassStyleCompatibility( listBand );
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
				processClassStyleCompatibility( table );
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
				processClassStyleCompatibility( tableGroup );
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
				processClassStyleCompatibility( band );
				return band;
			}

			case LABEL_DESIGN :
			{
				LabelItemDesign design = new LabelItemDesign( );
				readLabel( dis, design );
				processClassStyleCompatibility( design );
				return design;
			}

			case TEXT_DESIGN :
			{
				TextItemDesign design = new TextItemDesign( );
				readText( dis, design );
				processClassStyleCompatibility( design );
				return design;
			}
			case DATA_DESIGN :
			{
				DataItemDesign design = new DataItemDesign( );
				readData( dis, design );
				processClassStyleCompatibility( design );
				return design;
			}
			case MULTI_LINE_DESIGN :
			{
				DynamicTextItemDesign design = new DynamicTextItemDesign( );
				readDynamicText( dis, design );
				processClassStyleCompatibility( design );
				return design;
			}
			case IMAGE_DESIGN :
			{
				ImageItemDesign design = new ImageItemDesign( );
				readImage( dis, design );
				processClassStyleCompatibility( design );
				return design;
			}
			case TEMPLATE_DESIGN :
			{
				TemplateDesign design = new TemplateDesign( );
				readTemplate( dis, design );
				processClassStyleCompatibility( design );
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
				processClassStyleCompatibility( design );
				return design;
			}
			case AUTO_TEXT_DESIGN :
			{
				AutoTextItemDesign design = new AutoTextItemDesign( );
				readAutoText( dis, design );
				processClassStyleCompatibility( design );
				return design;
			}
			default :
				throw new IOException( "unknow design type:" + designType ); //$NON-NLS-1$
		}

	}

	private void processClassStyleCompatibility( StyledElementDesign design )
	{
		String styleClass = design.getStyleClass( );
		if ( styleClass != null && design.getStyle( ) == null )
		{
			design.setStyle( reportDesign.findStyle( styleClass ) );
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
				throw new IOException( "unknow field type " + fieldType ); //$NON-NLS-1$
		}
	}

	protected void readStyledElementField( DataInputStream in,
			StyledElementDesign design, short fieldType ) throws IOException
	{
		switch ( fieldType )
		{
			case FIELD_STYLE_CLASS :
				String styleClass = IOUtil.readString( in );
				design.setStyleClass( styleClass );
				break;
			case FIELD_STYLE :
				design.setStyle( readStyle( in ) );
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
				design.setX( readDimensionExpression( in ) );
				break;
			case FIELD_Y :
				design.setY( readDimensionExpression( in ) );
				break;
			case FIELD_HEIGHT :
				design.setHeight( readDimensionExpression( in ) );
				break;
			case FIELD_WIDTH :
				design.setWidth( readDimensionExpression( in ) );
				break;
			case FIELD_BOOKMARK :
				Expression<String> bookmark = readStringExpression( in );
				design.setBookmark( bookmark );
				break;
			case FIELD_TOC :

				Expression<Object> toc = readObjectExpression( in );
				design.setTOC( toc );
				break;
			case FIELD_ON_CREATE :
				String onCreatScriptText = IOUtil.readString( in );
				ScriptExpression onCreatScriptExpr = new ScriptExpression( onCreatScriptText );
				design.setOnCreate( onCreatScriptExpr );
				break;
			case FIELD_ON_RENDER :
				String OnRenderScriptText = IOUtil.readString( in );
				ScriptExpression OnRenderScriptExpr = new ScriptExpression( OnRenderScriptText );
				design.setOnRender( OnRenderScriptExpr );
				break;
			case FIELD_ON_PAGE_BREAK :
				String OnPageBreakScriptText = IOUtil.readString( in );
				ScriptExpression OnPageBreakScriptExpr = new ScriptExpression( OnPageBreakScriptText );
				design.setOnPageBreak( OnPageBreakScriptExpr );
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

	protected Expression<DimensionType> readDimensionExpression( DataInputStream in )
			throws IOException
	{
		return readConstant( in, DimensionType.class );
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
				listing.setRepeatHeader( readBoolExpression(in) );
				break;
			case FIELD_PAGE_BREAK_INTERVAL :
				Expression<Integer> pageBreakInterval = readIntConstant( in );
				listing.setPageBreakInterval( pageBreakInterval );
				break;
			default :
				readReportItemField( in, listing, fieldType );
		}
	}

	protected Expression<Boolean> readBoolExpression( DataInputStream in )
			throws IOException
	{
		return readConstant(in, boolean.class);
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
				Expression<String> pageBreakBefore = readStringConstant( in );
				group.setPageBreakBefore( pageBreakBefore );
				break;
			case FIELD_PAGE_BREAK_AFTER :
				Expression<String> pageBreakAfter = readStringConstant( in );
				group.setPageBreakAfter( pageBreakAfter );
				break;
			case FIELD_PAGE_BREAK_INSIDE :
				Expression<String> pageBreakInside = readStringConstant( in );
				group.setPageBreakInside( pageBreakInside );
				break;
			case FIELD_HIDE_DETAIL :
				Expression<Boolean> hideDetail = readBoolExpression( in );
				group.setHideDetail( hideDetail );
				break;
			case FIELD_HEADER_REPEAT :
				Expression<Boolean> headerRepeat = readBoolExpression( in );
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
				Expression<String> captionKey = readStringConstant( in );
				Expression<String> caption = readStringConstant( in );
				table.setCaption( captionKey, caption );
				break;
			case FIELD_SUMMARY:
				Expression<String> summary = readStringConstant( in );
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
				Expression<String> captionKey = readStringConstant( in );
				Expression<String> caption = readStringConstant( in );
				grid.setCaption( captionKey, caption );
				break;
			case FIELD_SUMMARY:
				Expression<String> summary = readStringConstant( in );
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
				Expression<Boolean> isColumnHeader = readBoolExpression( in );
				column.setColumnHeaderState( isColumnHeader );
				break;
			case FIELD_WIDTH :
				Expression<DimensionType> width = readDimensionExpression( in );
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
				Expression<String> headers = readStringExpression(in);
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
				Expression<String> textKey = readStringConstant( in );
				Expression<String> text = readStringConstant( in );
				label.setText( textKey, text );
				break;
			case FIELD_HELP_TEXT :
				Expression<String> helpTextKey = readStringConstant( in );
				Expression<String> helpText = readStringConstant( in );
				label.setHelpText( helpTextKey, helpText );
				break;
			default :
				readReportItemField( in, label, fieldType );
		}
	}

	protected Expression<String> readStringExpression( DataInputStream in )
			throws IOException
	{
		return readExpression( in, String.class );
	}

	protected Expression<String> readStringConstant( DataInputStream in )
			throws IOException
	{
		return readConstant( in, String.class );
	}

	protected Expression<Boolean> readBooleanConstant( DataInputStream in )
			throws IOException
	{
		return readConstant( in, Boolean.class );
	}

	protected Expression<Object> readObjectConstant( DataInputStream in )
			throws IOException
	{
		return readConstant( in, Object.class );
	}

	protected Expression<Integer> readIntConstant( DataInputStream in )
			throws IOException
	{
		return readConstant( in, int.class );
	}
	
	protected <T> Expression<T> readConstant( DataInputStream in, Class<T> clazz )
			throws IOException
	{
		T value = null;
		if ( clazz == DimensionType.class )
		{
			Object dimensionType = readDimension( in );
			value = (T)dimensionType;
		}
		else
		{
			value = IOUtil.read( in, clazz );
		}
		if ( value == null )
		{
			return null;
		}
		return Expression.newConstant( value );
	}
	
	protected Expression<Object> readObjectExpression( DataInputStream in )
			throws IOException
	{
		return readExpression( in, Object.class );
	}

	protected <T> Expression<T> readExpression( DataInputStream in, Class<T> clazz )
			throws IOException
	{
		String expression = IOUtil.readString( in );
		return Expression.newExpression( expression, clazz );
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
				Expression<String> helpTextKey = readStringConstant( in );
				Expression<String> helpText = readStringConstant( in );
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
				Expression<String> textType = readStringConstant( in );
				design.setTextType( textType );
				break;
			case FIELD_TEXT :
				Expression<String> textKey = readStringConstant( in );
				Expression<String> text = readStringConstant( in );
				design.setText( textKey, text );
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
				Expression<String> contentType = readStringExpression( in );
				design.setContentType( contentType );
				break;
			case FIELD_CONTENT :
				Expression<Object> content = readObjectExpression( in );
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
						throw new IOException( "invalid image source: " //$NON-NLS-1$
								+ imageSource );
				}
				break;
			case FIELD_ALT_TEXT :
				Expression<String> altTextKey = readStringConstant( in );
				Expression<String> altText = readStringConstant( in );
				image.setAltText( altTextKey, altText );
				break;
			case FIELD_HELP_TEXT :
				Expression<String> helpTextKey = readStringConstant( in );
				Expression<String> helpText = readStringConstant( in );
				image.setHelpText( helpTextKey, helpText );
				break;
			case FIELD_FIT_TO_CONTAINER:
				Boolean isFitToContainer = IOUtil.readBool( in );
				image.setFitToContainer( isFitToContainer );
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
				Expression<String> altTextKey = readStringConstant( in );
				Expression<String> altText = readStringConstant( in );
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
				Expression<String> promptTextKey = readStringConstant( in );
				Expression<String> promptText = readStringConstant( in );
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
			Expression<Boolean> expr = readBooleanConstant( in );
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
			Expression<String> displayText = readStringConstant( in );
			Expression<String> displayKey = readStringConstant( in );
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
		Expression<String> testExpr = readStringConstant( in );
		rule.setTestExpression( testExpr );
		String oper = IOUtil.readString( in );
		Object object1 = IOUtil.readObject( in );
		Object object2 = IOUtil.readObject( in );
		if ( object1 instanceof List )
		{
			rule.setExpression( oper, Expression.newConstant((List)object1) );
			rule.setValueIsList( true );
		}
		else
		{
			Expression<String> value1 = createExpression( (String) object1 );
			Expression<String> value2 = createExpression( (String) object2 );
			rule.setExpression( oper, value1, value2 );
			rule.setValueIsList( false );
		}
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
				Expression<String> bookmark = readStringExpression( in );
				action.setBookmark( bookmark );
				break;
			case ActionDesign.ACTION_DRILLTHROUGH :
				DrillThroughActionDesign drillThrough = readDrillThrough( in );
				action.setDrillThrough( drillThrough );
				break;
			case ActionDesign.ACTION_HYPERLINK :
				Expression<String> hyperlink = readStringExpression( in );
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
		Expression<String> targetWindow = readStringConstant( in );
		action.setTargetWindow( targetWindow );
		return action;
	}

	protected ActionDesign readActionV1( DataInputStream in )
			throws IOException
	{
		ActionDesign action = readAction( in );
		Expression<String> tooltip = readStringConstant( in );
		action.setTooltip( tooltip );
		return action;
	}

	protected DrillThroughActionDesign readDrillThrough( DataInputStream in )
			throws IOException
	{
		DrillThroughActionDesign drillThrough = new DrillThroughActionDesign( );
		Expression<String> reportName = readStringConstant( in );
		Map<String, Expression<Object>> parameters = readParameterBindings( in );
		Map search = IOUtil.readMap( in );
		Expression<String> format = readStringConstant( in );
		Expression<String> bookmarkType = readBookmarkType( in );
		drillThrough.setBookmarkType( bookmarkType );
		Expression<String> bookmark = readStringExpression( in );

		drillThrough.setReportName( reportName );
		drillThrough.setParameters( parameters );
		drillThrough.setSearch( search );
		drillThrough.setFormat( format );
		drillThrough.setBookmark( bookmark );

		return drillThrough;
	}

	protected Map<String, Expression<Object>> readParameterBindings(
			DataInputStream in ) throws IOException
	{
		Map<String, String> tempParameters = IOUtil.readMap( in );
		Set<Entry<String, String>> entrySet = tempParameters.entrySet( );
		Map<String, Expression<Object>> parameters = new HashMap<String, Expression<Object>>( );
		for ( Entry<String, String> entry : entrySet )
		{
			parameters.put( entry.getKey( ), createObjectExpression( entry
					.getValue( ) ) );
		}
		return parameters;
	}

	protected Expression<String> readBookmarkType( DataInputStream in )
			throws IOException
	{
		boolean isBookmark = IOUtil.readBool( in );
		Expression<String> bookmarkType = null;
		if ( !isBookmark )
		{
			bookmarkType = Expression
					.newConstant( DesignChoiceConstants.ACTION_BOOKMARK_TYPE_TOC );
		}
		return bookmarkType;
	}

	private JSExpression<String> createExpression( String expression )
	{
		return createExpression( expression, String.class );
	}

	private JSExpression<Boolean> createBooleanExpression( String expression )
	{
		return createExpression( expression, Boolean.class );
	}

	private JSExpression<Object> createObjectExpression( String expression )
	{
		return createExpression( expression, Object.class );
	}

	private <T> JSExpression<T> createExpression( String expression, Class<T> type )
	{
		if ( expression == null )
		{
			return null;
		}
		return Expression.newExpression( expression, type );
	}
}
