
package org.eclipse.birt.report.engine.emitter.excel.layout;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.excel.Data;
import org.eclipse.birt.report.engine.emitter.excel.DataCache;
import org.eclipse.birt.report.engine.emitter.excel.ExcelUtil;
import org.eclipse.birt.report.engine.emitter.excel.HyperlinkDef;
import org.eclipse.birt.report.engine.emitter.excel.Span;
import org.eclipse.birt.report.engine.emitter.excel.StyleBuilder;
import org.eclipse.birt.report.engine.emitter.excel.StyleConstant;
import org.eclipse.birt.report.engine.emitter.excel.StyleEngine;
import org.eclipse.birt.report.engine.emitter.excel.StyleEntry;

public class ExcelLayoutEngine
{

	public final static String EMPTY = "";

	public final static int MAX_ROW = 65525;

	public final static int MAX_CLOUMN = 255;

	public final static Object waste = new Object( );

	private DataCache cache;

	private AxisProcessor axis;

	private StyleEngine engine;

	private int detal;

	private int left;

	private Stack containers = new Stack( );

	private Stack tables = new Stack( );

	private Hashtable links = new Hashtable( );

	public ExcelLayoutEngine( PageDef page )
	{
		axis = new AxisProcessor( );
		int[] init = new int[3];
		init[0] = page.leftmargin;
		init[1] = page.contentwidth + init[0];
		init[2] = page.rightmargin + init[1];
		axis.addCoordinates( init );
		Rule rule = new Rule( init[0], init[1] - init[0] );
		cache = new DataCache( 1 );
		engine = new StyleEngine( this );
		detal = init[0] == 0 ? 0 : 1;
		left = init[0];
		containers.push( createContainer( rule, page.style ) );
	}

	public XlsContainer getCurrentContainer( )
	{
		return (XlsContainer) containers.peek( );
	}

	public Stack getContainers( )
	{
		return containers;
	}

	public void addTable( TableInfo table, IStyle style )
	{
		Rule rule = getCurrentContainer( ).getRule( );

		int start = rule.getStart( );
		int[] npos = new int[table.getColumnCount( )];
		npos[0] = start;

		for ( int i = 1; i < table.getColumnCount( ); i++ )
		{
			npos[i] = npos[i - 1] + table.getColumnWidth( i - 1 );
		}

		int[] scale = axis.getRange( start, rule.getEnd( ) );

		for ( int i = 0; i < scale.length - 1; i++ )
		{
			int sp = scale[i];
			int se = scale[i + 1];

			int[] range = inRange( sp, se, npos );

			if ( range.length > 0 )
			{
				int pos = axis.getCoordinate( sp ) - detal;
				cache.insertColumns( pos, range.length );

				for ( int j = 0; j < range.length; j++ )
				{
					axis.addCoordinate( range[j] );
				}
			}

		}

		XlsContainer container = createContainer( rule, style );
		XlsTable tcontainer = new XlsTable( table, container );
		addContainer( tcontainer );
		tables.push( tcontainer );
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

	public void addCell( int col, int span, IStyle style )
	{
		XlsTable table = (XlsTable) tables.peek( );
		Rule rule = table.getColumnRule( col, span );
		addContainer( createContainer( rule, style ) );
	}

	public void endCell( )
	{
		endContainer( );
	}

	public void addRow( IStyle style )
	{
		XlsTable table = (XlsTable) containers.peek( );
		XlsContainer container = createContainer( table.getRule( ), style );
		container.setEmpty( false );
		addContainer( container );
	}

	public void endRow( )
	{
		synchronous( );
		endContainer( );
	}

	private void synchronous( )
	{
		Rule rule = getCurrentContainer( ).getRule( );
		int start = rule.getStart( );
		int end = rule.getEnd( );
		int startcol = axis.getCoordinate( start );
		int endcol = axis.getCoordinate( end );

		int max = 0;
		int len[] = new int[endcol - startcol];

		for ( int i = startcol; i < endcol; i++ )
		{
			int columnsize = cache.getColumnSize( i - detal );
			len[i - startcol] = columnsize;
			max = max > columnsize ? max : columnsize;
		}

		for ( int i = startcol; i < endcol; i++ )
		{
			int rowspan = max - len[i - startcol];
			int last = len[i - startcol] - 1;

			if(rowspan > 0)
			{				
				Object data = null;				
				Object upstair =  cache.getData( i - detal, last );
				
				if ( upstair != null && upstair != waste )
				{
					Data predata = (Data) upstair;
					int rs = predata.getRowSpan( ) + rowspan;
					predata.setRowSpan( rs );
					data = predata;
				}
				else
				{
					data = waste;
					
				}
				
				for(int p =0 ; p < rowspan; p++) {
					cache.addData( i - detal, data );
				}	
			}
		}
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
		Rule rule = getCurrentContainer( ).getRule( );
		StyleEntry entry = engine.createEntry( rule, style );
		addContainer( new XlsContainer( entry, rule ) );
	}

	public void addContainer( XlsContainer container )
	{
		getCurrentContainer( ).setEmpty( false );
		int col = axis.getCoordinate( container.getRule( ).getStart( ) );
		int pos = cache.getColumnSize( col - detal );
		container.setStart( pos );
		containers.push( container );
	}

	public void endContainer( )
	{
		XlsContainer container = getCurrentContainer( );
		// Make sure all containers contain data except table.
		if ( container.isEmpty( ) )
		{
			Data data = new Data( EMPTY, container.getStyle( ), Data.STRING );
			data.setRule( container.getRule( ) );
			addData( data );
		}

		engine.removeContainerStyle( );
		containers.pop( );
	}

	public void addData( Object txt, IStyle style, HyperlinkDef link )
	{
		Rule rule = getCurrentContainer( ).getRule( );
		StyleEntry entry = engine.getStyle( style, rule );
		Data data = createData(txt, entry);
		data.setHyperlinkDef( link );
		data.setRule( rule );

		addData( data );
	}
	
	public void addCaption(String text) {
		Rule rule = getCurrentContainer( ).getRule( );
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry( );
		entry.setProperty( StyleEntry.H_ALIGN_PROP, "Center" );
		Data data = createData(text, entry);		
		data.setRule( rule );

		addData( data );		
	}	
	
	public Data createData( Object txt, StyleEntry entry )
	{

		if ( ExcelUtil.getType( txt ).equals( Data.NUMBER ) )
		{
			String format = ExcelUtil.getPattern( txt, entry
					.getProperty( StyleConstant.NUMBER_FORMAT_PROP ) );
			entry.setProperty( StyleConstant.NUMBER_FORMAT_PROP, format );
			entry.setProperty( StyleConstant.DATA_TYPE_PROP, Data.NUMBER );
			return new Data( txt, entry, Data.NUMBER );

		}
		else if ( ExcelUtil.getType( txt ).equals( Data.DATE ) )
		{
			String format = ExcelUtil.getPattern( txt, entry
					.getProperty( StyleConstant.DATE_FORMAT_PROP ) );
			entry.setProperty( StyleConstant.DATE_FORMAT_PROP, format );
			entry.setProperty( StyleConstant.DATA_TYPE_PROP, Data.DATE );
			return new Data( txt, entry, Data.DATE );

		}
		entry.setProperty( StyleConstant.DATA_TYPE_PROP, Data.STRING );
		return new Data( txt, entry, Data.STRING );

	}

	private void addData( Data data )
	{
		getCurrentContainer( ).setEmpty( false );
		int col = axis.getCoordinate( data.getRule( ).getStart( ) );
		int span = axis.getCoordinate( data.getRule( ).getEnd( ) ) - col;
		addDatatoCache( col, data );

		for ( int i = col + 1; i < col + span; i++ )
		{
			addDatatoCache( i, waste );
		}
	}

	public XlsContainer createContainer( Rule rule, IStyle style )
	{
		return new XlsContainer( engine.createEntry( rule, style ), rule );
	}

	public Map getStyleMap( )
	{
		return engine.getStyleIDMap( );
	}

	public int[] getCoordinates( )
	{
		return axis.getCoordinates( );
	}

	public int getRowCount( )
	{
		int realcount = cache.getRowCount( );
		return Math.min( realcount, MAX_ROW - 1 );
	}

	public AxisProcessor getAxis( )
	{
		return axis;
	}

	public int getColumnSize( int column )
	{
		return cache.getColumnSize( column - detal );
	}

	public Data getData( int col, int row )
	{
		Object object = cache.getData( col - detal, row );
		return object == waste ? null : (Data) object;
	}

	public Data[] getRow( int rownum )
	{
		Object[] row = cache.getRowData( rownum );
		List data = new ArrayList( );
		int width = Math.min( row.length, MAX_CLOUMN - 1 );
		// margin, we can ignore them now
		// if(detal == 1)
		// {
		// Data margin = createMarginData();
		// rowdata[0] = margin;
		// }

		for ( int i = 0; i < width; i++ )
		{
			if ( waste == row[i] )
			{
				continue;
			}

			Data d = (Data) row[i];
			
			if(d.isProcessed()) {
				continue;
			}
			
			HyperlinkDef def = d.getHyperlinkDef( );

			if ( def != null
					&& def.getType( ) == IHyperlinkAction.ACTION_BOOKMARK )
			{
				def.setUrl( (String) links.get( def.getUrl( ) ) );
			}
			
			d.setProcessed( true );
			data.add( row[i] );
		}		
		
		return (Data[]) data.toArray( new Data[0] );
	}

	private void addDatatoCache( int col, Object value )
	{
		cache.addData( col - detal, value );
	}

	// private Data createMarginData( )
	// {
	// Data data = new Data( EMPTY, null );
	// data.setRule( new Rule( 0, left ) );
	// return data;
	// }

	public void complete( )
	{
		int rowcount = cache.getRowCount( );

		for ( int i = 0; i < rowcount; i++ )
		{
			Object[] row = cache.getRowData( i );

			for ( int j = 0; j < row.length; j++ )
			{
				if ( row[j] == waste )
				{
					continue;
				}

				Data d = (Data) row[j];
				int styleid = engine.getStyleID( d.getStyleEntry( ) );
				d.setStyleId( styleid );
				Rule rule = d.getRule( );
				int start = axis.getCoordinate( rule.getStart( ) );
				int end = axis.getCoordinate( rule.getEnd( ) );
				Span span = new Span( start + 1, end - start - 1 );

				HyperlinkDef link = d.getHyperlinkDef( );

				if ( link != null && link.getBookmark( ) != null )
				{
					// Since Excel cell start is 1, 1
					links.put( link.getBookmark( ), getCellName( i + 1,
							start + 1 ) );
				}

				d.setSpan( span );
			}
		}
	}

	private String getCellName( int row, int col )
	{
		char base = (char) ( col + 64 );
		Character chr = new Character( base );

		return chr.toString( ) + row;
	}
}