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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

public class EngineIRWriter implements IOConstants
{

	public void write( OutputStream out, Report design ) throws IOException
	{
		DataOutputStream dos = new DataOutputStream( out );

		// stream version number
		IOUtil.writeLong( dos, 0L );

		// design version number
		IOUtil.writeString( dos, DesignSchemaConstants.REPORT_VERSION );

		// design's base path
		String basePath = design.getBasePath( );
		IOUtil.writeString( dos, basePath );

		// design's unit
		String unit = design.getUnit( );
		IOUtil.writeString( dos, unit );

		// style informations
		int styleCount = design.getStyleCount( );
		IOUtil.writeInt( dos, styleCount );
		for ( int i = 0; i < styleCount; i++ )
		{
			IStyle style = design.getStyle( i );
			IOUtil.writeString( dos, Report.PREFIX_STYLE_NAME + i );
			writeStyle( dos, style );
		}
		String rootStyleName = design.getRootStyleName( );
		IOUtil.writeString( dos, rootStyleName );

		// named expression
		Map namedExpressions = design.getNamedExpressions( );
		IOUtil.writeMap( dos, namedExpressions );

		ReportItemWriter writer = new ReportItemWriter( dos );
		// page setup
		PageSetupDesign pageSetup = design.getPageSetup( );
		int masterPageCount = pageSetup.getMasterPageCount( );
		IOUtil.writeInt( dos, masterPageCount );
		for ( int i = 0; i < masterPageCount; i++ )
		{
			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) pageSetup
					.getMasterPage( i );
			writer.writeMasterPage( masterPage );
		}

		// write the body conent
		int count = design.getContentCount( );
		IOUtil.writeInt( dos, count );
		for ( int i = 0; i < count; i++ )
		{
			ReportItemDesign item = design.getContent( i );
			writer.write( item );
		}
		dos.flush( );
	}

	private class ReportItemWriter extends DefaultReportItemVisitorImpl
	{

		DataOutputStream dos;
		ByteArrayOutputStream bout;
		DataOutputStream bdos;

		ReportItemWriter( DataOutputStream dos )
		{
			this.dos = dos;
			this.bout = new ByteArrayOutputStream( );
			this.bdos = new DataOutputStream( bout );
		}

		public void write( ReportItemDesign design ) throws IOException
		{

			Object exception = design.accept( this, null );
			if ( exception != null )
			{
				throw (IOException) exception;
			}

		}

		public void writeMasterPage( SimpleMasterPageDesign masterPage )
				throws IOException
		{
			bout.reset( );
			writeSimpleMasterPage( bdos, masterPage );
			bdos.flush( );

			IOUtil.writeShort( dos, SIMPLE_MASTER_PAGE_DESIGN );
			IOUtil.writeBytes( dos, bout.toByteArray( ) );

			int count = masterPage.getHeaderCount( );
			IOUtil.writeInt( dos, count );
			for ( int i = 0; i < count; i++ )
			{
				ReportItemDesign item = masterPage.getHeader( i );
				Object exception = item.accept( this, null );
				if ( exception != null )
				{
					throw (IOException) exception;
				}
			}
			count = masterPage.getFooterCount( );
			IOUtil.writeInt( dos, count );
			for ( int i = 0; i < count; i++ )
			{
				ReportItemDesign item = masterPage.getFooter( i );
				Object exception = item.accept( this, null );
				if ( exception != null )
				{
					throw (IOException) exception;
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitAutoTextItem(org.eclipse.birt.report.engine.ir.AutoTextItemDesign,
		 *      java.lang.Object)
		 */
		public Object visitAutoTextItem( AutoTextItemDesign autoText,
				Object value )
		{
			try
			{
				bout.reset( );
				writeAutoText( bdos, autoText );
				bdos.flush( );

				IOUtil.writeShort( dos, AUTO_TEXT_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitCell( CellDesign cell, Object value )
		{
			try
			{
				bout.reset( );
				writeCell( bdos, cell );
				bdos.flush( );
				IOUtil.writeShort( dos, CELL_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				int contentCount = cell.getContentCount( );
				IOUtil.writeInt( dos, contentCount );
				for ( int i = 0; i < contentCount; i++ )
				{
					ReportItemDesign content = cell.getContent( i );
					value = content.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitDataItem( DataItemDesign data, Object value )
		{
			try
			{
				bout.reset( );
				writeData( bdos, data );
				bdos.flush( );
				IOUtil.writeShort( dos, DATA_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitExtendedItem( ExtendedItemDesign item, Object value )
		{
			try
			{
				bout.reset( );
				writeExtended( bdos, item );
				bdos.flush( );
				IOUtil.writeShort( dos, EXTENDED_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitFreeFormItem(org.eclipse.birt.report.engine.ir.FreeFormItemDesign,
		 *      java.lang.Object)
		 */
		public Object visitFreeFormItem( FreeFormItemDesign container,
				Object value )
		{
			try
			{
				bout.reset( );
				writeFreeForm( bdos, container );
				bdos.flush( );
				IOUtil.writeShort( dos, FREE_FORM_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
				int contentCount = container.getItemCount( );
				IOUtil.writeInt( dos, contentCount );
				for ( int i = 0; i < contentCount; i++ )
				{
					ReportItemDesign content = container.getItem( i );
					value = content.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitGridItem(org.eclipse.birt.report.engine.ir.GridItemDesign,
		 *      java.lang.Object)
		 */
		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			try
			{
				bout.reset( );
				writeGrid( bdos, grid );
				bdos.flush( );
				IOUtil.writeShort( dos, GRID_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				int rowCount = grid.getRowCount( );
				IOUtil.writeInt( dos, rowCount );
				for ( int i = 0; i < rowCount; i++ )
				{
					ReportItemDesign row = grid.getRow( i );
					value = row.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitImageItem( ImageItemDesign image, Object value )
		{
			try
			{
				bout.reset( );
				writeImage( bdos, image );
				bdos.flush( );
				IOUtil.writeShort( dos, IMAGE_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitLabelItem( LabelItemDesign label, Object value )
		{
			try
			{
				bout.reset( );
				writeLabel( bdos, label );
				bdos.flush( );
				IOUtil.writeShort( dos, LABEL_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitListBand(org.eclipse.birt.report.engine.ir.ListBandDesign,
		 *      java.lang.Object)
		 */
		public Object visitListBand( ListBandDesign band, Object value )
		{
			try
			{
				bout.reset( );
				writeListBand( bdos, band );
				bdos.flush( );
				IOUtil.writeShort( dos, LIST_BAND_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				int count = band.getContentCount( );
				IOUtil.writeInt( dos, count );
				for ( int i = 0; i < count; i++ )
				{
					ReportItemDesign content = band.getContent( i );
					value = content.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitListGroup( ListGroupDesign group, Object value )
		{
			try
			{
				bout.reset( );
				writeListGroup( bdos, group );
				bdos.flush( );
				IOUtil.writeShort( dos, LIST_GROUP_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				BandDesign header = group.getHeader( );

				IOUtil.writeBool( dos, header != null );
				if ( header != null )
				{
					value = header.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}

				BandDesign footer = group.getFooter( );
				IOUtil.writeBool( dos, footer != null );
				if ( footer != null )
				{
					value = footer.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitListItem( ListItemDesign list, Object value )
		{
			try
			{
				bout.reset( );
				writeList( bdos, list );
				bdos.flush( );
				IOUtil.writeShort( dos, LIST_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				BandDesign header = list.getHeader( );
				IOUtil.writeBool( dos, header != null );
				if ( header != null )
				{
					value = header.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
				int count = list.getGroupCount( );
				IOUtil.writeInt( dos, count );
				for ( int i = 0; i < count; i++ )
				{
					GroupDesign group = list.getGroup( i );
					value = group.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
				BandDesign detail = list.getDetail( );
				IOUtil.writeBool( dos, detail != null );
				if ( detail != null )
				{
					value = detail.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}

				BandDesign footer = list.getFooter( );
				IOUtil.writeBool( dos, footer != null );
				if ( footer != null )
				{
					value = footer.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitMultiLineItem( MultiLineItemDesign multiLine,
				Object value )
		{
			try
			{
				bout.reset( );
				writeMultiline( bdos, multiLine );
				bdos.flush( );
				IOUtil.writeShort( dos, MULTI_LINE_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		public Object visitRow( RowDesign row, Object value )
		{
			try
			{
				bout.reset( );
				writeRow( bdos, row );
				bdos.flush( );
				IOUtil.writeShort( dos, ROW_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				int count = row.getCellCount( );
				IOUtil.writeInt( dos, count );
				for ( int i = 0; i < count; i++ )
				{
					CellDesign cell = row.getCell( i );
					value = cell.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTableBand(org.eclipse.birt.report.engine.ir.TableBandDesign,
		 *      java.lang.Object)
		 */
		public Object visitTableBand( TableBandDesign band, Object value )
		{
			try
			{
				bout.reset( );
				writeTableBand( bdos, band );
				bdos.flush( );
				IOUtil.writeShort( dos, TABLE_BAND_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				int rowCount = band.getRowCount( );
				IOUtil.writeInt( dos, rowCount );
				for ( int i = 0; i < rowCount; i++ )
				{
					ReportItemDesign row = band.getRow( i );
					value = row.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTableGroup(org.eclipse.birt.report.engine.ir.TableGroupDesign,
		 *      java.lang.Object)
		 */
		public Object visitTableGroup( TableGroupDesign group, Object value )
		{
			try
			{
				bout.reset( );
				writeTableGroup( bdos, group );
				bdos.flush( );
				IOUtil.writeShort( dos, TABLE_GROUP_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				BandDesign header = group.getHeader( );
				IOUtil.writeBool( dos, header != null );
				if ( header != null )
				{
					value = header.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}

				BandDesign footer = group.getFooter( );
				IOUtil.writeBool( dos, footer != null );
				if ( footer != null )
				{
					value = footer.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTableItem(org.eclipse.birt.report.engine.ir.TableItemDesign,
		 *      java.lang.Object)
		 */
		public Object visitTableItem( TableItemDesign table, Object value )
		{
			try
			{
				bout.reset( );
				writeTable( bdos, table );
				bdos.flush( );
				IOUtil.writeShort( dos, TABLE_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );

				BandDesign header = table.getHeader( );
				IOUtil.writeBool( dos, header != null );
				if ( header != null )
				{
					value = header.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}

				int count = table.getGroupCount( );
				IOUtil.writeInt( dos, count );
				for ( int i = 0; i < count; i++ )
				{
					GroupDesign group = table.getGroup( i );
					value = group.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
				BandDesign detail = table.getDetail( );
				IOUtil.writeBool( dos, detail != null );
				if ( detail != null )
				{
					value = detail.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}
				BandDesign footer = table.getFooter( );
				IOUtil.writeBool( dos, footer != null );
				if ( footer != null )
				{
					value = footer.accept( this, value );
					if ( value != null )
					{
						return value;
					}
				}

			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTemplate(org.eclipse.birt.report.engine.ir.TemplateDesign,
		 *      java.lang.Object)
		 */
		public Object visitTemplate( TemplateDesign template, Object value )
		{
			try
			{
				bout.reset( );
				writeTemplate( bdos, template );
				bdos.flush( );
				IOUtil.writeShort( dos, TEMPLATE_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl#visitTextItem(org.eclipse.birt.report.engine.ir.TextItemDesign,
		 *      java.lang.Object)
		 */
		public Object visitTextItem( TextItemDesign text, Object value )
		{
			try
			{
				bout.reset( );
				writeText( bdos, text );
				bdos.flush( );
				IOUtil.writeShort( dos, TEXT_DESIGN );
				IOUtil.writeBytes( dos, bout.toByteArray( ) );
			}
			catch ( IOException ex )
			{
				return ex;
			}
			return null;
		}

	}

	protected void writeReportElement( DataOutputStream out,
			ReportElementDesign design ) throws IOException
	{
		long id = design.getID( );
		if ( id != -1 )
		{
			IOUtil.writeShort( out, FIELD_ID );
			IOUtil.writeLong( out, id );
		}

		String name = design.getName( );
		if ( name != null )
		{
			IOUtil.writeShort( out, FIELD_NAME );
			IOUtil.writeString( out, name );
		}

		String ext = design.getExtends( );
		if ( ext != null )
		{
			IOUtil.writeShort( out, FIELD_EXTENDS );
			IOUtil.writeString( out, ext );
		}

		String javaClass = design.getJavaClass( );
		if ( javaClass != null )
		{
			IOUtil.writeShort( out, FIELD_JAVA_CLASS );
			IOUtil.writeString( out, javaClass );
		}

		Map namedExpression = design.getNamedExpressions( );
		if ( namedExpression != null && !namedExpression.isEmpty( ) )
		{
			IOUtil.writeShort( out, FIELD_NAMED_EXPRESSIONS );
			IOUtil.writeMap( out, namedExpression );
		}

		Map customProperties = design.getCustomProperties( );
		if ( customProperties != null && !customProperties.isEmpty( ) )
		{
			IOUtil.writeShort( out, FIELD_CUSTOM_PROPERTIES );
			IOUtil.writeMap( out, customProperties );
		}
	}

	protected void writeStyledElement( DataOutputStream out,
			StyledElementDesign design ) throws IOException
	{
		writeReportElement( out, design );

		String styleName = design.getStyleName( );
		if ( styleName != null )
		{
			IOUtil.writeShort( out, FIELD_STYLE_NAME );
			IOUtil.writeString( out, styleName );
		}

		MapDesign map = design.getMap( );
		if ( map != null )
		{
			IOUtil.writeShort( out, FIELD_MAP );
			writeMap( out, map );
		}

		HighlightDesign highlight = design.getHighlight( );;
		if ( highlight != null )
		{
			IOUtil.writeShort( out, FIELD_HIGHLIGHT );
			writeHighlight( out, highlight );
		}
	}

	protected void writeReportItem( DataOutputStream out,
			ReportItemDesign design ) throws IOException
	{
		writeStyledElement( out, design );

		DimensionType x = design.getX( );
		if ( x != null )
		{
			IOUtil.writeShort( out, FIELD_X );
			writeDimension( out, x );
		}

		DimensionType y = design.getY( );
		if ( y != null )
		{
			IOUtil.writeShort( out, FIELD_Y );
			writeDimension( out, y );
		}
		DimensionType height = design.getHeight( );
		if ( height != null )
		{
			IOUtil.writeShort( out, FIELD_HEIGHT );
			writeDimension( out, height );
		}
		DimensionType width = design.getWidth( );
		if ( width != null )
		{
			IOUtil.writeShort( out, FIELD_WIDTH );
			writeDimension( out, width );
		}

		String bookmark = design.getBookmark( );
		if ( bookmark != null )
		{
			IOUtil.writeShort( out, FIELD_BOOKMARK );
			IOUtil.writeString( out, bookmark );
		}
		String toc = design.getTOC( );
		if ( toc != null )
		{
			IOUtil.writeShort( out, FIELD_TOC );
			IOUtil.writeString( out, toc );
		}

		String onCreate = design.getOnCreate( );
		if ( onCreate != null )
		{
			IOUtil.writeShort( out, FIELD_ON_CREATE );
			IOUtil.writeString( out, onCreate );
		}

		String onRender = design.getOnRender( );
		if ( onRender != null )
		{
			IOUtil.writeShort( out, FIELD_ON_RENDER );
			IOUtil.writeString( out, onRender );
		}
		String onPageBreak = design.getOnPageBreak( );
		if ( onPageBreak != null )
		{
			IOUtil.writeShort( out, FIELD_ON_PAGE_BREAK );
			IOUtil.writeString( out, onPageBreak );
		}

		VisibilityDesign visibility = design.getVisibility( );
		if ( visibility != null )
		{
			IOUtil.writeShort( out, FIELD_VISIBILITY );
			writeVisibility( out, visibility );
		}
		ActionDesign action = design.getAction( );
		if ( action != null )
		{
			IOUtil.writeShort( out, FIELD_ACTION );
			writeAction( out, action );
		}
	}

	protected void writeMasterPage( DataOutputStream out,
			MasterPageDesign design ) throws IOException
	{
		writeStyledElement( out, design );

		String pageType = design.getPageType( );
		if ( pageType != null )
		{
			IOUtil.writeShort( out, FIELD_PAGE_TYPE );
			IOUtil.writeString( out, pageType );
		}

		DimensionType width = design.getPageWidth( );
		DimensionType height = design.getPageHeight( );
		IOUtil.writeShort( out, FIELD_PAGE_SIZE );
		writeDimension( out, width );
		writeDimension( out, height );

		DimensionType top = design.getTopMargin( );
		DimensionType left = design.getLeftMargin( );
		DimensionType bottom = design.getBottomMargin( );
		DimensionType right = design.getRightMargin( );

		IOUtil.writeShort( out, FIELD_MARGIN );
		writeDimension( out, top );
		writeDimension( out, left );
		writeDimension( out, bottom );
		writeDimension( out, right );

		String orientation = design.getOrientation( );
		if ( orientation != null )
		{
			IOUtil.writeShort( out, FIELD_ORIENTATION );
			IOUtil.writeString( out, orientation );
		}
		String bodyStyleName = design.getBodyStyleName( );
		if ( bodyStyleName != null )
		{
			IOUtil.writeShort( out, FIELD_BODY_STYLE );
			IOUtil.writeString( out, bodyStyleName );
		}
	}

	protected void writeSimpleMasterPage( DataOutputStream out,
			SimpleMasterPageDesign design ) throws IOException
	{
		writeMasterPage( out, design );

		boolean showHeaderOnFirst = design.isShowHeaderOnFirst( );
		if ( !showHeaderOnFirst )
		{
			IOUtil.writeShort( out, FIELD_SHOW_HEADER_ON_FIRST );
			IOUtil.writeBool( out, showHeaderOnFirst );
		}
		boolean showFooterOnLast = design.isShowFooterOnLast( );
		if ( !showFooterOnLast )
		{
			IOUtil.writeShort( out, FIELD_SHOW_FOOTER_ON_LAST );
			IOUtil.writeBool( out, showFooterOnLast );
		}
		boolean floatingFooter = design.isFloatingFooter( );
		if ( floatingFooter )
		{
			IOUtil.writeShort( out, FIELD_FLOATING_FOOTER );
			IOUtil.writeBool( out, floatingFooter );
		}
		DimensionType headerHeight = design.getHeaderHeight( );
		if ( headerHeight != null )
		{
			IOUtil.writeShort( out, FEILD_HEADER_HEIGHT );
			writeDimension( out, headerHeight );
		}
		DimensionType footerHeigh = design.getFooterHeight( );
		if ( footerHeigh != null )
		{
			IOUtil.writeShort( out, FEILD_FOOTER_HEIGHT );
			writeDimension( out, footerHeigh );
		}

	}

	protected void writeGraphicMasterPage( DataOutputStream out,
			GraphicMasterPageDesign design ) throws IOException
	{
		writeMasterPage( out, design );

		int columns = design.getColumns( );
		if ( columns != -1 )
		{
			IOUtil.writeShort( out, FIELD_COLUMNS );
			IOUtil.writeInt( out, columns );
		}

		DimensionType columnSpacing = design.getColumnSpacing( );
		if ( columnSpacing != null )
		{
			IOUtil.writeShort( out, FIELD_COLUMN_SPACING );
			writeDimension( out, columnSpacing );
		}
	}

	protected void writeListing( DataOutputStream out, ListingDesign listing )
			throws IOException
	{
		writeReportItem( out, listing );

		boolean repeatHeader = listing.isRepeatHeader( );
		int pageBreakInterval = listing.getPageBreakInterval( );
		if ( repeatHeader )
		{
			IOUtil.writeShort( out, FIELD_REPEAT_HEADER );
			IOUtil.writeBool( out, repeatHeader );
		}
		if ( pageBreakInterval != -1 )
		{
			IOUtil.writeShort( out, FIELD_PAGE_BREAK_INTERVAL );
			IOUtil.writeInt( out, pageBreakInterval );
		}
	}

	protected void writeGroup( DataOutputStream out, GroupDesign group )
			throws IOException
	{
		writeReportItem( out, group );
		int groupLevel = group.getGroupLevel( );
		String pageBreakBefore = group.getPageBreakBefore( );
		String pageBreakAfter = group.getPageBreakAfter( );
		boolean hideDetail = group.getHideDetail( );
		boolean headerRepeat = group.isHeaderRepeat( );

		if ( groupLevel != -1 )
		{
			IOUtil.writeShort( out, FIELD_GROUP_LEVEL );
			IOUtil.writeInt( out, groupLevel );
		}
		if ( pageBreakBefore != null )
		{
			IOUtil.writeShort( out, FIELD_PAGE_BREAK_BEFORE );
			IOUtil.writeString( out, pageBreakBefore );
		}
		if ( pageBreakAfter != null )
		{
			IOUtil.writeShort( out, FIELD_PAGE_BREAK_AFTER );
			IOUtil.writeString( out, pageBreakAfter );
		}

		if ( headerRepeat )
		{
			IOUtil.writeShort( out, FIELD_HEADER_REPEAT );
			IOUtil.writeBool( out, headerRepeat );
		}
		if ( hideDetail )
		{
			IOUtil.writeShort( out, FIELD_HIDE_DETAIL );
			IOUtil.writeBool( out, hideDetail );
		}

	}

	protected void writeBand( DataOutputStream out, BandDesign band )
			throws IOException
	{
		writeReportItem( out, band );
		int bandType = band.getBandType( );
		if ( bandType != -1 )
		{
			IOUtil.writeShort( out, FIELD_BAND_TYPE );
			IOUtil.writeInt( out, bandType );
		}
	}

	protected void writeList( DataOutputStream out, ListItemDesign list )
			throws IOException
	{
		writeListing( out, list );

	}

	protected void writeListGroup( DataOutputStream out, ListGroupDesign group )
			throws IOException
	{
		writeGroup( out, group );
	}

	protected void writeListBand( DataOutputStream out, ListBandDesign listBand )
			throws IOException
	{
		writeBand( out, listBand );
	}

	protected void writeTable( DataOutputStream out, TableItemDesign table )
			throws IOException
	{
		writeListing( out, table );

		String captionKey = table.getCaptionKey( );
		String caption = table.getCaption( );
		if ( caption != null || captionKey != null )
		{
			IOUtil.writeShort( out, FIELD_CAPTION );
			IOUtil.writeString( out, captionKey );
			IOUtil.writeString( out, caption );
		}

		// write columns
		int columnCount = table.getColumnCount( );
		IOUtil.writeShort( out, FIELD_COLUMNS );
		IOUtil.writeInt( out, columnCount );
		ByteArrayOutputStream bo = new ByteArrayOutputStream( );
		DataOutputStream buffer = new DataOutputStream( bo );
		for ( int i = 0; i < columnCount; i++ )
		{
			ColumnDesign column = table.getColumn( i );
			bo.reset( );
			writeColumn( buffer, column );
			buffer.flush( );
			IOUtil.writeBytes( out, bo.toByteArray( ) );
		}
	}

	protected void writeTableGroup( DataOutputStream out, TableGroupDesign group )
			throws IOException
	{
		writeGroup( out, group );
	}

	protected void writeTableBand( DataOutputStream out,
			TableBandDesign listBand ) throws IOException
	{
		writeBand( out, listBand );
	}

	protected void writeGrid( DataOutputStream out, GridItemDesign grid )
			throws IOException
	{
		writeReportItem( out, grid );
		// write columns
		int columnCount = grid.getColumnCount( );
		IOUtil.writeShort( out, FIELD_COLUMNS );
		IOUtil.writeInt( out, columnCount );
		ByteArrayOutputStream bo = new ByteArrayOutputStream( );
		DataOutputStream buffer = new DataOutputStream( bo );
		for ( int i = 0; i < columnCount; i++ )
		{
			ColumnDesign column = grid.getColumn( i );
			bo.reset( );
			writeColumn( buffer, column );
			buffer.flush( );
			IOUtil.writeBytes( out, bo.toByteArray( ) );
		}
	}

	protected void writeColumn( DataOutputStream out, ColumnDesign column )
			throws IOException
	{
		writeStyledElement( out, column );
		DimensionType width = column.getWidth( );
		if ( width != null )
		{
			IOUtil.writeShort( out, FIELD_WIDTH );
			writeDimension( out, width );
		}
		boolean suppressDuplicate = column.getSuppressDuplicate( );
		if ( suppressDuplicate )
		{
			IOUtil.writeShort( out, FIELD_SUPPRESS_DUPLICATE );
			IOUtil.writeBool( out, suppressDuplicate );
		}
		VisibilityDesign visibility = column.getVisibility( );
		if ( visibility != null )
		{
			IOUtil.writeShort( out, FIELD_VISIBILITY );
			writeVisibility( out, visibility );
		}
		boolean hasDataItemsInDetail = column.hasDataItemsInDetail( );
		if ( hasDataItemsInDetail )
		{
			IOUtil.writeShort( out, FIELD_HAS_DATA_ITEMS_IN_DETAIL );
			IOUtil.writeBool( out, hasDataItemsInDetail );
		}
	}

	protected void writeRow( DataOutputStream out, RowDesign row )
			throws IOException
	{
		writeReportItem( out, row );
		boolean isStartOfGroup = row.isStartOfGroup( );
		if ( isStartOfGroup )
		{
			IOUtil.writeShort( out, FIELD_IS_START_OF_GROUP );
			IOUtil.writeBool( out, isStartOfGroup );
		}
	}

	protected void writeCell( DataOutputStream out, CellDesign cell )
			throws IOException
	{
		writeReportItem( out, cell );
		int column = cell.getColumn( );
		if ( column != -1 )
		{
			IOUtil.writeShort( out, FIELD_COLUMN );
			IOUtil.writeInt( out, column );
		}
		int colSpan = cell.getColSpan( );
		if ( colSpan != 1 )
		{
			IOUtil.writeShort( out, FIELD_COL_SPAN );
			IOUtil.writeInt( out, colSpan );
		}
		int rowSpan = cell.getRowSpan( );
		if ( rowSpan != 1 )
		{
			IOUtil.writeShort( out, FIELD_ROW_SPAN );
			IOUtil.writeInt( out, rowSpan );
		}
		String drop = cell.getDrop( );
		if ( drop != null )
		{
			IOUtil.writeShort( out, FIELD_DROP );
			IOUtil.writeString( out, drop );
		}
		boolean displayGroupIcon = cell.getDisplayGroupIcon( );
		if (displayGroupIcon)
		{
			IOUtil.writeShort( out, FIELD_DISPLAY_GROUP_ICON );
			IOUtil.writeBool( out, displayGroupIcon );
		}
	}

	protected void writeFreeForm( DataOutputStream out,
			FreeFormItemDesign freeForm ) throws IOException
	{
		writeReportItem( out, freeForm );
	}

	protected void writeLabel( DataOutputStream out, LabelItemDesign label )
			throws IOException
	{
		writeReportItem( out, label );
		String text = label.getText( );
		String textKey = label.getTextKey( );
		String helpText = label.getHelpText( );
		String helpTextKey = label.getHelpTextKey( );

		if ( text != null || textKey != null )
		{
			IOUtil.writeShort( out, FIELD_TEXT );
			IOUtil.writeString( out, textKey );
			IOUtil.writeString( out, text );
		}
		if ( helpText != null || helpTextKey != null )
		{
			IOUtil.writeShort( out, FIELD_HELP_TEXT );
			IOUtil.writeString( out, helpTextKey );
			IOUtil.writeString( out, helpText );
		}
	}

	protected void writeData( DataOutputStream out, DataItemDesign data )
			throws IOException
	{
		writeReportItem( out, data );

		//String value = data.getValue( );
		String bindingColumn = data.getBindingColumn( );
		String helpText = data.getHelpText( );
		String helpTextKey = data.getHelpTextKey( );
		boolean suppressDuplicate = data.getSuppressDuplicate( );
		if ( bindingColumn != null )
		{
			IOUtil.writeShort( out, FIELD_BINDING_COLUMN);
			IOUtil.writeString( out, bindingColumn );
		}
		if ( helpText != null || helpTextKey != null )
		{
			IOUtil.writeShort( out, FIELD_HELP_TEXT );
			IOUtil.writeString( out, helpTextKey );
			IOUtil.writeString( out, helpText );
		}
		if ( suppressDuplicate )
		{
			IOUtil.writeShort( out, FIELD_SUPPRESS_DUPLICATE );
			IOUtil.writeBool( out, suppressDuplicate );
		}
	}

	protected void writeText( DataOutputStream out, TextItemDesign design )
			throws IOException
	{
		writeReportItem( out, design );
		String textType = design.getTextType( );
		String textKey = design.getTextKey( );
		String text = design.getText( );
		if ( textType != null )
		{
			IOUtil.writeShort( out, FIELD_TEXT_TYPE );
			IOUtil.writeString( out, textType );
		}
		if ( text != null || textKey != null )
		{
			IOUtil.writeShort( out, FIELD_TEXT );
			IOUtil.writeString( out, textKey );
			IOUtil.writeString( out, text );
		}
	}

	protected void writeMultiline( DataOutputStream out,
			MultiLineItemDesign design ) throws IOException
	{
		writeReportItem( out, design );
		String contentType = design.getContentType( );
		String content = design.getContent( );
		if ( contentType != null )
		{
			IOUtil.writeShort( out, FIELD_CONTENT_TYPE );
			IOUtil.writeString( out, contentType );
		}
		if ( content != null )
		{
			IOUtil.writeShort( out, FIELD_CONTENT );
			IOUtil.writeString( out, content );
		}
	}

	protected void writeImage( DataOutputStream out, ImageItemDesign image )
			throws IOException
	{
		writeReportItem( out, image );
		IOUtil.writeShort( out, FIELD_IMAGE_SOURCE );
		int imageSource = image.getImageSource( );
		IOUtil.writeInt( out, imageSource );
		switch ( imageSource )
		{
			case ImageItemDesign.IMAGE_NAME :
				{
					String imageName = image.getImageName( );
					DesignElementHandle handle = image.getHandle( );
					if ( handle instanceof ImageHandle )
					{
						String designImageName = image.getImageName( );
						if ( imageName != null && imageName.equals( designImageName ) )
						{
							imageName = null;
						}
					}
					IOUtil.writeString( out, imageName );
				}
				break;
			case ImageItemDesign.IMAGE_FILE :
				IOUtil.writeString( out, image.getImageUri( ) );
				break;
			case ImageItemDesign.IMAGE_URI :
				IOUtil.writeString( out, image.getImageUri( ) );
				break;
			case ImageItemDesign.IMAGE_EXPRESSION :
				IOUtil.writeString( out, image.getImageExpression( ) );
				IOUtil.writeString( out, image.getImageFormat( ) );
				break;
		}

		String altText = image.getAltText( );
		String altTextKey = image.getAltTextKey( );
		String helpText = image.getHelpText( );
		String helpTextKey = image.getHelpTextKey( );

		if ( altText != null || altTextKey != null )
		{
			IOUtil.writeShort( out, FIELD_ALT_TEXT );
			IOUtil.writeString( out, altTextKey );
			IOUtil.writeString( out, altText );
		}
		if ( helpText != null || helpText != null )
		{
			IOUtil.writeShort( out, FIELD_HELP_TEXT );
			IOUtil.writeString( out, helpTextKey );
			IOUtil.writeString( out, helpText );
		}
	}

	protected void writeExtended( DataOutputStream out,
			ExtendedItemDesign extended ) throws IOException
	{
		writeReportItem( out, extended );
		String altText = extended.getAltText( );
		String altTextKey = extended.getAltTextKey( );
		if ( altText != null || altTextKey != null )
		{
			IOUtil.writeShort( out, FIELD_ALT_TEXT );
			IOUtil.writeString( out, altTextKey );
			IOUtil.writeString( out, altText );
		}

	}

	protected void writeAutoText( DataOutputStream out,
			AutoTextItemDesign design ) throws IOException
	{
		writeReportItem( out, design );

		String type = design.getType( );
		if ( type != null )
		{
			IOUtil.writeShort( out, FIELD_TYPE );
			IOUtil.writeString( out, type );
		}

		String text = design.getText( );
		String textKey = design.getTextKey( );
		if ( text != null || textKey != null )
		{
			IOUtil.writeShort( out, FIELD_TEXT );
			IOUtil.writeString( out, textKey );
			IOUtil.writeString( out, text );
		}
	}

	protected void writeTemplate( DataOutputStream out, TemplateDesign design )
			throws IOException
	{
		writeReportItem( out, design );

		String allowedType = design.getAllowedType( );
		if ( allowedType != null )
		{
			IOUtil.writeShort( out, FIELD_ALLOWED_TYPE );
			IOUtil.writeString( out, allowedType );
		}

		String promptText = design.getPromptText( );
		String promptTextKey = design.getPromptTextKey( );
		if ( promptText != null || promptTextKey != null )
		{
			IOUtil.writeShort( out, FIELD_PROMPT_TEXT );
			IOUtil.writeString( out, promptTextKey );
			IOUtil.writeString( out, promptText );
		}
	}

	protected void writeDimension( DataOutputStream out, DimensionType dimension )
			throws IOException
	{
		IOUtil.writeBool( out, dimension != null );
		dimension.writeObject( out );
	}

	protected void writeVisibility( DataOutputStream out,
			VisibilityDesign visibility ) throws IOException
	{
		int ruleCount = visibility.count( );
		IOUtil.writeInt( out, ruleCount );
		for ( int i = 0; i < ruleCount; i++ )
		{
			VisibilityRuleDesign rule = visibility.getRule( i );
			IOUtil.writeString( out, rule.getFormat( ) );
			IOUtil.writeString( out, rule.getExpression( ) );
		}
	}

	protected void writeMap( DataOutputStream out, MapDesign map )
			throws IOException
	{
		int ruleCount = map.getRuleCount( );
		IOUtil.writeInt( out, ruleCount );
		for ( int i = 0; i < ruleCount; i++ )
		{
			MapRuleDesign rule = map.getRule( i );
			IOUtil.writeString( out, rule.getTestExpression( ) );
			IOUtil.writeString( out, rule.getOperator( ) );
			IOUtil.writeString( out, rule.getValue1( ) );
			IOUtil.writeString( out, rule.getValue2( ) );
			IOUtil.writeString( out, rule.getDisplayText( ) );
			IOUtil.writeString( out, rule.getDisplayKey( ) );
		}
	}

	protected void writeHighlight( DataOutputStream out,
			HighlightDesign highlight ) throws IOException
	{
		int ruleCount = highlight.getRuleCount( );
		IOUtil.writeInt( out, ruleCount );
		for ( int i = 0; i < ruleCount; i++ )
		{
			HighlightRuleDesign rule = highlight.getRule( i );
			IOUtil.writeString( out, rule.getTestExpression( ) );
			IOUtil.writeString( out, rule.getOperator( ) );
			IOUtil.writeString( out, rule.getValue1( ) );
			IOUtil.writeString( out, rule.getValue2( ) );
			writeStyle( out, rule.getStyle( ) );
		}
	}

	// /FIXME: we need a more fast method
	protected void writeStyle( DataOutputStream out, IStyle style )
			throws IOException
	{
		IOUtil.writeString( out, style.getCssText( ) );
	}

	protected void writeAction( DataOutputStream out, ActionDesign action )
			throws IOException
	{
		int actionType = action.getActionType( );
		IOUtil.writeInt( out, actionType );
		switch ( actionType )
		{
			case ActionDesign.ACTION_BOOKMARK :
				String bookmark = action.getBookmark( );
				IOUtil.writeString( out, bookmark );
				break;
			case ActionDesign.ACTION_DRILLTHROUGH :
				DrillThroughActionDesign drillThrough = action
						.getDrillThrough( );
				writeDrillThrough( out, drillThrough );
				break;
			case ActionDesign.ACTION_HYPERLINK :
				String hyperlink = action.getHyperlink( );
				IOUtil.writeString( out, hyperlink );
				break;
		}
		boolean isBookmark = action.isBookmark( );
		IOUtil.writeBool( out, isBookmark );
		String targetWindow = action.getTargetWindow( );
		IOUtil.writeString( out, targetWindow );
	}

	protected void writeDrillThrough( DataOutputStream out,
			DrillThroughActionDesign drillThrough ) throws IOException
	{
		String reportName = drillThrough.getReportName( );
		Map parameters = drillThrough.getParameters( );
		Map search = drillThrough.getSearch( );
		String format = drillThrough.getFormat( );
		boolean isBookmark = drillThrough.isBookmark( );
		String bookmark = drillThrough.getBookmark( );

		IOUtil.writeString( out, reportName );
		IOUtil.writeMap( out, parameters );
		IOUtil.writeMap( out, search );
		IOUtil.writeString( out, format );
		IOUtil.writeBool( out, isBookmark );
		IOUtil.writeString( out, bookmark );
	}
}
