/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.excel.BlankData;
import org.eclipse.birt.report.engine.emitter.excel.BookmarkDef;
import org.eclipse.birt.report.engine.emitter.excel.Data;
import org.eclipse.birt.report.engine.emitter.excel.DataCache;
import org.eclipse.birt.report.engine.emitter.excel.DateTimeUtil;
import org.eclipse.birt.report.engine.emitter.excel.ExcelEmitter;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.emitter.excel.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.excel.ImageData;
import org.eclipse.birt.report.engine.emitter.excel.RowData;
import org.eclipse.birt.report.engine.emitter.excel.SheetData;
import org.eclipse.birt.report.engine.emitter.excel.Span;
import org.eclipse.birt.report.engine.emitter.excel.StyleBuilder;
import org.eclipse.birt.report.engine.emitter.excel.StyleConstant;
import org.eclipse.birt.report.engine.emitter.excel.StyleEngine;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;
import org.eclipse.birt.report.engine.i18n.EngineResourceHandle;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.layout.emitter.EmitterUtil;
import org.eclipse.birt.report.engine.util.FlashFile;

import com.ibm.icu.util.ULocale;


public class ExcelLayoutEngine
{
	public final static String EMPTY = "";
	
	private static final double DEFAULT_ROW_HEIGHT = 15;
	// Excel 2007 can support 1048576 rows and 16384 columns.
	private int autoBookmarkIndex = 0;

	public static final String AUTO_GENERATED_BOOKMARK = "auto_generated_bookmark_";

	public final static int MAX_ROW_OFFICE2007 = 1048576;
	
	public final static int MAX_COL_OFFICE2007 = 16384;
	
	public final static int MAX_ROW_OFFICE2003 = 65535;
	
	public static int MAX_COLUMN_OFFICE2003 = 255;
	
	private int maxRow = MAX_ROW_OFFICE2003;

	private int maxCol = MAX_COLUMN_OFFICE2003;
	
	private HashMap<String, String> cachedBookmarks = new HashMap<String, String>( );

	private DataCache cache;

	private AxisProcessor axis;

	private StyleEngine engine;	
	
	private ExcelEmitter emitter;

	private Stack<XlsContainer> containers = new Stack<XlsContainer>( );

	private Stack<XlsTable> tables = new Stack<XlsTable>( );

	private ExcelContext context = null;

	private String messageFlashObjectNotSupported;
	
	private HashMap<String, BookmarkDef> bookmarkList = new HashMap<String, BookmarkDef>( );

	public ExcelLayoutEngine( PageDef page, ExcelContext context,
			ExcelEmitter emitter )
	{
		this.context = context;
		this.emitter = emitter;
		ULocale locale = context.getLocale( );
		if ( locale == null )
		{
			locale = ULocale.getDefault( );
		}
		EngineResourceHandle resourceHandle = new EngineResourceHandle( locale );
		messageFlashObjectNotSupported = resourceHandle
				.getMessage( MessageConstants.FLASH_OBJECT_NOT_SUPPORTED_PROMPT );
		initalize( page );
	}
	
	private void initalize(PageDef page)
	{
		axis = new AxisProcessor( );		
		axis.addCoordinate( page.contentwidth );

		setCacheSize();
		
		ContainerSizeInfo rule = new ContainerSizeInfo( 0, page.contentwidth );
		cache = new DataCache( maxCol, maxRow );
		engine = new StyleEngine( this );
		containers.push( createContainer( rule, page.style, null ) );
	}
	
	private void setCacheSize()
	{
		if ( context.getOfficeVersion( ).equals( "office2007" ) )
		{
			maxCol = MAX_COL_OFFICE2007;
		}
	}

	public XlsContainer getCurrentContainer( )
	{
		return (XlsContainer) containers.peek( );
	}

	public Stack<XlsContainer> getContainers( )
	{
		return containers;
	}

	public void setPageStyle( IStyle style )
	{
		XlsContainer topContainer = containers.peek( );
		topContainer.setStyle( StyleBuilder.createStyleEntry( style ) );
	}
	
	public void addTable( TableInfo table, IStyle style )
	{
		XlsContainer currentContainer = getCurrentContainer( );
		ContainerSizeInfo parentSizeInfo = currentContainer.getSizeInfo( );
		int startCoordinate = parentSizeInfo.getStartCoordinate( );
		int endCoordinate = parentSizeInfo.getEndCoordinate( );

		int[] columnStartCoordinates = calculateColumnCoordinates( table,
				startCoordinate, endCoordinate );

		splitColumns( startCoordinate, endCoordinate, columnStartCoordinates );

		createTable( table, style, currentContainer, columnStartCoordinates );
	}

	private void createTable( TableInfo tableInfo, IStyle style,
			XlsContainer currentContainer, int[] columnStartCoordinates )
	{
		int leftCordinate = columnStartCoordinates[0];
		int width = columnStartCoordinates[columnStartCoordinates.length - 1]
				- leftCordinate;
		ContainerSizeInfo sizeInfo = new ContainerSizeInfo( leftCordinate,
				width );
		StyleEntry styleEntry = engine.createEntry( sizeInfo, style );
		XlsTable table = new XlsTable( tableInfo, styleEntry,
				sizeInfo, currentContainer );
		tables.push( table );
		addContainer( table );
	}

	private void splitColumns( int startCoordinate, int endCoordinate,
			int[] columnStartCoordinates )
	{
		int[] scale = axis.getColumnCoordinatesInRange( startCoordinate,
				endCoordinate );

		for ( int i = 0; i < scale.length - 1; i++ )
		{
			int startPosition = scale[i];
			int endPostion = scale[i + 1];

			int[] range = inRange( startPosition, endPostion, columnStartCoordinates );

			if ( range.length > 0 )
			{				
				int pos = axis.getColumnIndexByCoordinate( startPosition );
				cache.insertColumns( pos, range.length );

				for ( int j = 0; j < range.length; j++ )
				{
					axis.addCoordinate( range[j] );
				}
			}
		}
	}

	private int[] calculateColumnCoordinates( TableInfo table,
			int startCoordinate,
			int endCoordinate )
	{
		int columnCount = table.getColumnCount( );
		int[] columnStartCoordinates = new int[ columnCount + 1 ];
		columnStartCoordinates[0] = startCoordinate;
		for ( int i = 1; i <= columnCount; i++ )
		{
			columnStartCoordinates[i] = columnStartCoordinates[i - 1]
				+ table.getColumnWidth( i - 1 );
		}
		return columnStartCoordinates;
	}

	private int[] inRange( int start, int end, int[] data )
	{
		int[] range = new int[data.length];
		int count = 0;

		for ( int i = 0; i < data.length; i++ )
		{
			if ( ( data[i] > start ) && ( data[i] < end ) )
			{
				count++;
				range[count] = data[i];
			}
		}

		int[] result = new int[count];

		int j = 0;
		for ( int i = 0; i < range.length; i++ )
		{
			if ( range[i] != 0 )
			{
				result[j] = range[i];
				j++;
			}
		}

		return result;
	}

	public void addCell( int col, int colSpan, int rowSpan, IStyle style )
	{
		XlsTable table = tables.peek( );
		ContainerSizeInfo cellSizeInfo = table.getColumnSizeInfo( col, colSpan );
		XlsCell cell = new XlsCell( engine.createEntry( cellSizeInfo, style ),
				cellSizeInfo, getCurrentContainer( ), rowSpan );
		addContainer( cell );
	}

	public void endCell( )
	{
		endNormalContainer( );
	}

	public void addRow( IStyle style )
	{
		XlsContainer parent = getCurrentContainer( );
		ContainerSizeInfo sizeInfo = parent.getSizeInfo( );
		XlsContainer container = createContainer( sizeInfo, style,
				parent );
		container.setEmpty( false );
		addContainer( container );
	}

	public void endRow( )
	{
		synchronize( );
		endContainer( );
	}

	private void synchronize( )
	{
		XlsContainer rowContainer = getCurrentContainer( );
		ContainerSizeInfo rowSizeInfo = rowContainer.getSizeInfo( );
		int startCoordinate = rowSizeInfo.getStartCoordinate( );
		int endCoordinate = rowSizeInfo.getEndCoordinate( );
		int startColumnIndex = axis.getColumnIndexByCoordinate( startCoordinate );
		int endColumnIndex = axis.getColumnIndexByCoordinate( endCoordinate );

		int maxRowIndex = 0;
		int rowIndexes[] = new int[endColumnIndex - startColumnIndex];

		for ( int currentColumnIndex = startColumnIndex; currentColumnIndex < endColumnIndex; currentColumnIndex++ )
		{			
			int rowIndex = cache.getMaxRowIndex( currentColumnIndex );
			rowIndexes[currentColumnIndex - startColumnIndex] = rowIndex;
			maxRowIndex = maxRowIndex > rowIndex ? maxRowIndex : rowIndex;
		}
		rowContainer.setRowIndex( maxRowIndex );
		for ( int currentColumnIndex = startColumnIndex; currentColumnIndex < endColumnIndex; currentColumnIndex++ )
		{
			int rowspan = maxRowIndex - rowIndexes[currentColumnIndex - startColumnIndex];
			if ( rowspan > 0 )
			{
				SheetData upstair = cache
						.getColumnLastData( currentColumnIndex );
				if ( upstair != null && canSpan( upstair, rowContainer ) )
				{
					SheetData predata = upstair;
					int rs = predata.getRowSpan( ) + rowspan;
					predata.setRowSpan( rs );
					SheetData realData = getRealData( predata );
					BlankData blankData = new BlankData( realData );
					if ( !isInContainer( predata, rowContainer ) )
					{
						blankData.decreasRowSpanInDesign( );
					}
					int rowIndex = predata.getRowIndex( );
					for ( int p = 1; p <= rowspan; p++ )
					{
						BlankData blank = new BlankData( predata );
						blank.setRowIndex( rowIndex + p );
						cache.addData( currentColumnIndex, blank );
					}
				}
			}
		}
	}

	private boolean canSpan( SheetData data, XlsContainer rowContainer )
	{
		SheetData realData = getRealData(data);
		if ( realData == null )
			return false;
		if ( isInContainer( realData, rowContainer ) )
		{
			return true;
		}
		return realData.getRowSpanInDesign( ) > 0;
	}

	private SheetData getRealData( SheetData data )
	{
		if ( data.isBlank( ) )
		{
			return ((BlankData)data).getData( );
		}
		return data;
	}

	private boolean isInContainer( SheetData data, XlsContainer rowContainer )
	{
		XlsContainer container = data.getContainer( );
		while( container != null )
		{
			if ( container == rowContainer )
			{
				return true;
			}
			container = container.getParent( );
		}
		return false;
	}
	
	public void endTable( )
	{
		if ( !tables.isEmpty( ) )
		{
			tables.pop( );
			endContainer( );
		}
	}

	public void addContainer( IStyle style, HyperlinkDef link )
	{
		XlsContainer parent = getCurrentContainer( );
		ContainerSizeInfo sizeInfo = parent.getSizeInfo( );
		StyleEntry entry = engine.createEntry( sizeInfo, style );
		addContainer( new XlsContainer( entry, sizeInfo, parent ) );
	}

	private void addContainer( XlsContainer child )
	{
		XlsContainer parent = child.getParent( );
		if ( parent != null )
		{
			parent.setEmpty( false );
		}
		containers.push( child);
	}

	public void endContainer( )
	{
		setParentContainerIndex( );
		endNormalContainer( );
	}

	private void setParentContainerIndex( )
	{
		XlsContainer container = getCurrentContainer( );
		XlsContainer parent = container.getParent( );
		if ( parent != null )
			parent.setRowIndex( container.getRowIndex( ) );
	}

	public void endNormalContainer( )
	{
		XlsContainer container = getCurrentContainer( );
		if ( container.isEmpty( ) )
		{
			Data data = new Data( EMPTY, container.getStyle( ), Data.STRING,
					container );
			data.setSizeInfo( container.getSizeInfo( ) );
			addData( data );
		}
		engine.applyContainerBottomStyle( );
		containers.pop( );
	}

	public void addData( Object txt, IStyle style, HyperlinkDef link, BookmarkDef bookmark )
	{
		ContainerSizeInfo rule = getCurrentContainer( ).getSizeInfo( );
		StyleEntry entry = engine.getStyle( style, rule );
		Data data = createData( txt, entry );
		data.setHyperlinkDef( link );
		data.setBookmark( bookmark );
		data.setSizeInfo( rule );
		addData( data );
	}

	public void addImageData( IImageContent image, IStyle style,
			HyperlinkDef link, BookmarkDef bookmark )
	{
		XlsContainer container=getCurrentContainer();
		ContainerSizeInfo rule = container.getSizeInfo( );
		StyleEntry entry = engine.getStyle( style, rule );
		SheetData data = createImageData( image, entry,container );
		data.setHyperlinkDef( link );
		data.setBookmark( bookmark );
		data.setSizeInfo( rule );
		addData( data );

	}

	private SheetData createImageData( IImageContent image, StyleEntry entry, XlsContainer container )
	{
		int type = SheetData.IMAGE;
		entry.setProperty( StyleConstant.DATA_TYPE_PROP, Integer
				.toString( type ) );
		String uri = image.getURI( );
		String mimeType = image.getMIMEType( );
		String extension = image.getExtension( );
		String altText = image.getAltText( );
		if ( FlashFile.isFlash( mimeType, uri, extension ) )
		{
			if ( null == altText )
			{
				altText = messageFlashObjectNotSupported; 
			}
			return createData( altText, entry);
		}

		org.eclipse.birt.report.engine.layout.emitter.Image imageInfo= EmitterUtil.parseImage( image, image.getImageSource( ), image.getURI( ),image.getMIMEType( ) ,
				image.getExtension( ) );
		byte[] data=imageInfo.getData( );
		if ( data != null )
		{
			return new ImageData( image, entry, type, imageInfo, container);
		}
		else
		{
			return createData( image.getAltText( ), entry );
		}

	}

	public void addDateTime(Object txt, IStyle style, HyperlinkDef link, BookmarkDef bookmark)
	{
		ContainerSizeInfo rule = getCurrentContainer( ).getSizeInfo( );
		StyleEntry entry = engine.getStyle( style, rule );
		Data data = null;
		
		IDataContent dataContent = (IDataContent)txt;
		Object value = dataContent.getValue( );
		Date date = ExcelUtil.getDate( value );
		
		//If date time is before 1900, it must be output as string, otherwise, excel can't format the date.
		if ( date != null
				&& ( ( date instanceof Time ) || date.getYear( ) >= 0 ) )
		{
			data = createDateData( value, entry, style.getDateTimeFormat( ) );
			data.setHyperlinkDef( link );
			data.setBookmark( bookmark );
			data.setSizeInfo( rule );
			addData( data );
		}
		else
		{
			addData( dataContent.getText( ), style, link, bookmark );
		}
	}

	public void addCaption( String text )
	{
		ContainerSizeInfo rule = getCurrentContainer( ).getSizeInfo( );
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry( );
		entry.setProperty( StyleEntry.H_ALIGN_PROP, "Center" );
		Data data = createData( text, entry );
		data.setSizeInfo( rule );

		addData( data );
	}

	public Data createData( Object txt, StyleEntry entry )
	{
		int type = SheetData.STRING;
		Locale locale = emitter.getLocale( );
		if ( SheetData.NUMBER==ExcelUtil.getType( txt )  )
		{
			String format = ExcelUtil.getPattern( txt, entry
					.getProperty( StyleConstant.NUMBER_FORMAT_PROP ) );
			format = ExcelUtil.formatNumberPattern( format, locale );
			entry.setProperty( StyleConstant.NUMBER_FORMAT_PROP, format );
			type = SheetData.NUMBER;

		}
		else if ( SheetData.DATE == ExcelUtil.getType( txt ) )
		{
			String format = ExcelUtil.getPattern( txt, entry
					.getProperty( StyleConstant.DATE_FORMAT_PROP ) );
			entry.setProperty( StyleConstant.DATE_FORMAT_PROP, format );
			type = Data.DATE;
		}

		entry.setProperty( StyleConstant.DATA_TYPE_PROP, Integer
				.toString( type ) );

		return new Data( txt, entry, type, getCurrentContainer( ) );
	}

	private Data createDateData( Object txt, StyleEntry entry, String timeFormat )
	{
		Locale locale = emitter.getLocale( );
		timeFormat = ExcelUtil.parse( timeFormat, locale );
		if ( timeFormat.equals( "" ) )
		{
			if ( txt instanceof java.sql.Date )
			{
				timeFormat = DateTimeUtil
						.formatDateTime( "MMM d, yyyy", locale );
			}
			else if ( txt instanceof java.sql.Time )
			{
				timeFormat = DateTimeUtil.formatDateTime( "H:mm:ss AM/PM",
						locale );
			}
			else
			{
				timeFormat = DateTimeUtil.formatDateTime(
						"MMM d, yyyy H:mm AM/PM", locale );
			}
		}
		else
		{
			timeFormat = DateTimeUtil.formatDateTime( timeFormat, locale );
		}
		entry.setProperty( StyleConstant.DATE_FORMAT_PROP, timeFormat );
		entry.setProperty( StyleConstant.DATA_TYPE_PROP, Integer
				.toString( SheetData.DATE ) );
		return new Data( txt, entry, SheetData.DATE, getCurrentContainer( ) );
	}

	private void addData( SheetData data )
	{
		XlsContainer container = getCurrentContainer( );
		container.setEmpty( false );
		int col = axis.getColumnIndexByCoordinate( data.getSizeInfo( ).getStartCoordinate( ) );
		int span = axis.getColumnIndexByCoordinate( data.getSizeInfo( ).getEndCoordinate( ) ) - col;
		applyTopBorderStyle( data );
		// FIXME: there is a bug when this data is in middle of a row.
		outputDataIfBufferIsFull( );
		updataRowIndex( data, container );
		addDatatoCache( col, data );
		SheetData newData = new Data( data );
		for ( int i = col + 1; i < col + span; i++ )
		{
			BlankData blankData = new BlankData( newData );
			addDatatoCache( i, blankData );
		}
		
		if ( container instanceof XlsCell )
		{
			XlsCell cell = (XlsCell)container;
			data.setRowSpanInDesign( cell.getRowSpan( ) - 1 );
		}
	}

	private void updataRowIndex( SheetData data, XlsContainer container )
	{
		int rowIndex = container.getRowIndex( ) + 1;
		data.setRowIndex( rowIndex );
		container.setRowIndex( rowIndex );
	}

	private void outputDataIfBufferIsFull( )
	{
		if ( getCurrentContainer( ).getRowIndex( ) >= maxRow )
		{
			emitter.outputSheet( );
			cache.clearCachedSheetData( );
			resetContainers( );
		}
	}

	/**
	 * @param data
	 */
	private void applyTopBorderStyle( SheetData data )
	{
		XlsContainer container = getCurrentContainer( );
		int rowIndex = container.getRowIndex( );
		XlsContainer parent = container;
		while ( parent != null && parent.getStartRowId( ) == rowIndex )
		{
			StyleBuilder.applyTopBorder( parent.getStyle( ), data
					.getStyle( ) );
			parent = parent.getParent( );
		}
	}

	public XlsContainer createContainer( ContainerSizeInfo sizeInfo,
			IStyle style, XlsContainer parent )
	{
		return new XlsContainer( engine.createEntry( sizeInfo, style ),
				sizeInfo, parent );
	}

	public XlsContainer createCellContainer( IStyle style, XlsContainer parent, int rowSpan )
	{
		ContainerSizeInfo sizeInfo = parent.getSizeInfo( );
		return new XlsCell( engine.createEntry( sizeInfo, style ), sizeInfo,
				parent, rowSpan );
	}

	public Map<StyleEntry,Integer> getStyleMap( )
	{
		return engine.getStyleIDMap( );
	}

	public int[] getCoordinates( )
	{
		int[] coord = axis.getColumnWidths( );

		if ( coord.length <= maxCol )
		{
			return coord;
		}
		else
		{
			int[] ncoord = new int[maxCol];
			System.arraycopy( coord, 0, ncoord, 0, maxCol );
			return ncoord;
		}
	}

	public int getRowCount( )
	{
		return cache.getMaxRow( );
	}

	public AxisProcessor getAxis( )
	{
		return axis;
	}

	public SheetData getColumnLastData( int column )
	{
		return cache.getColumnLastData( column );
	}

	private void addDatatoCache( int col, SheetData value )
	{
		cache.addData( col, value );
	}

	public void complete( )
	{
		endNormalContainer( );
		Iterator<SheetData[]> iterator = cache.getRowIterator( );
		while ( iterator.hasNext( ) )
		{
			SheetData[] rowData = iterator.next( );

			for ( int j = 0; j < rowData.length; j++ )
			{
				SheetData data = rowData[j];
				if ( data == null || data.isBlank( ) )
				{
					continue;
				}

				int styleid = engine.getStyleID( data.getStyle( ) );
				data.setStyleId( styleid );
				ContainerSizeInfo rule = data.getSizeInfo( );

				// Excel Cell Starts From 1
				int start = axis.getColumnIndexByCoordinate( rule
						.getStartCoordinate( ) ) + 1;
				int end = axis.getColumnIndexByCoordinate( rule
						.getEndCoordinate( ) ) + 1;

				end = Math.min( end, maxCol );
				int scount = Math.max( 0, end - start - 1 );
				// Excel Span Starts From 1
				Span span = new Span( start, scount );
				data.setSpan( span );
				HyperlinkDef hyperLink = data.getHyperlinkDef( );
				if ( hyperLink != null )
				{
					if ( hyperLink.getType( ) == IHyperlinkAction.ACTION_BOOKMARK )
					{
						setLinkedBookmark( data, hyperLink );
					}
				}
			}
		}
	}

	/**
	 * @param data
	 * @param hyperLink
	 */
	private void setLinkedBookmark( SheetData data, HyperlinkDef hyperLink )
	{
		String bookmarkName = hyperLink.getUrl( );
		BookmarkDef linkedBookmark = bookmarkList
				.get( bookmarkName );
		if ( linkedBookmark != null )
		{
			data.setLinkedBookmark( linkedBookmark );
		}
		else
		{
			BookmarkDef newBookmark;
			if ( ExcelUtil.isValidBookmarkName( bookmarkName ) )
				newBookmark = new BookmarkDef( bookmarkName );
			else
			{
				String generateBookmarkName = getGenerateBookmark( bookmarkName );
				newBookmark = new BookmarkDef(
						generateBookmarkName );
				cachedBookmarks.put( bookmarkName,
						generateBookmarkName );
			}
			data.setLinkedBookmark( newBookmark );
		}
	}

	public Stack<XlsTable> getTable( )
	{
		return tables;
	}

	public void addContainerStyle( IStyle computedStyle )
	{
		engine.addContainderStyle(computedStyle);	
	}

	public void removeContainerStyle( )
	{
		engine.removeForeignContainerStyle( );
	}

	public void resetContainers( )
	{
		for ( XlsContainer container : containers )
		{
			container.setRowIndex( 0 );
			container.setStartRowId( 0 );
		}
		for ( XlsTable table : tables )
		{
			table.setRowIndex( 0 );
		}
	}

	public ExcelLayoutEngineIterator getIterator( )
	{
		return new ExcelLayoutEngineIterator( );
	}

	private class ExcelLayoutEngineIterator implements Iterator<RowData>
	{

		Iterator<SheetData[]> rowIterator;

		public ExcelLayoutEngineIterator( )
		{
			rowIterator = cache.getRowIterator( );
		}

		public boolean hasNext( )
		{
			return rowIterator.hasNext( );
		}

		public RowData next( )
		{
			SheetData[] row = rowIterator.next( );
			List<SheetData> data = new ArrayList<SheetData>( );
			int width = Math.min( row.length, maxCol - 1 );
			double rowHeight = DEFAULT_ROW_HEIGHT;
			for ( int i = 0; i < width; i++ )
			{
				SheetData d = row[i];
				if ( d == null || d.isBlank( ) )
				{
					continue;
				}

				if ( d.isProcessed( ) )
				{
					continue;
				}

				d.setProcessed( true );
				data.add( row[i] );
				if ( d instanceof ImageData )
				{
					ImageData imagedata = (ImageData) d;
					double height = imagedata.getHeight( );
					if ( height > rowHeight )
						rowHeight = height;
				}
			}

			SheetData[] rowdata = new SheetData[data.size( )];
			data.toArray( rowdata );
			return new RowData( rowdata, rowHeight );
		}

		public void remove( )
		{
			throw new UnsupportedOperationException( );
		}
	}

	public void cacheBookmarks( int sheetIndex )
	{
		List<BookmarkDef> currentSheetBookmarks = cache.getBookmarks( );
		for ( BookmarkDef book : currentSheetBookmarks )
		{
			book.setSheetIndex( sheetIndex );
			bookmarkList.put( book.getName( ), book );
		}
	}

	public HashMap<String, BookmarkDef> getAllBookmarks( )
	{
		return this.bookmarkList;
	}

	/**
	 * @param bookmarkName
	 * @return generatedBookmarkName
	 */
	public String getGenerateBookmark( String bookmarkName )
	{
		String generatedName = cachedBookmarks.get( bookmarkName );
		return generatedName != null ? generatedName : AUTO_GENERATED_BOOKMARK
				+ autoBookmarkIndex++;
	}
}