/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - Implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;

/**
 * This class is used to caculate styles for Excel.
 * 
 * 
 */
public class StyleEngine implements IListVisitor
{

	private static StyleEngine engine = new StyleEngine( );

	private static int ID = 0;

	private Stack styles = new Stack( );
	private Stack spans = new Stack( );

	private Stack pos = new Stack( );
	private Hashtable style2id = new Hashtable( );

	// Save styles which are not able to calculate now.
	private ListBuffer dataMap = null;

	/**
	 * 
	 * @param dataMap
	 *            layout data
	 * @return a StyleEngine instance
	 */
	public static StyleEngine createStyleEngine( ListBuffer dataMap )
	{
		engine.release( );
		engine.setDataMap( dataMap );
		return engine;
	}

	private StyleEngine( )
	{
	}

	public void addContainerStyle( IStyle style, Span span, int start )
	{
		StyleEntry entry = initStyle( style, span );
		entry.setStart( true );
		spans.push( span );
		styles.push( entry );
		pos.push( new Integer( start ) );
	}

	public void calculateTopStyles( )
	{
		if ( styles.size( ) > 0 )
		{
			StyleEntry style = (StyleEntry) styles.peek( );
			boolean first = style.isStart( );

			if ( first )
			{
				Span span = (Span) spans.peek( );
				int start = ( (Integer) pos.peek( ) ).intValue( );
				applyContainerTopBorder( span, start );
				style.setStart(false);
			}
		}
	}

	public StyleEntry createHorizionStyle( int pos )
	{
		Span span = (Span) spans.peek( );
		StyleEntry cEntry = (StyleEntry) styles.peek( );
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry( );

		StyleBuilder.mergeInheritableProp( cEntry, entry );

		if ( pos == span.getCol( ) )
		{
			StyleBuilder.applyLeftBorder( cEntry, entry );
		}

		if ( pos == span.getCol( ) + span.getColSpan( ) )
		{
			StyleBuilder.applyRightBorder( cEntry, entry );
		}

		return entry;
	}

	public void calculateBottomStyles( )
	{
		Span span = (Span) spans.peek( );
		StyleEntry entry = (StyleEntry) styles.peek( );
		
		if(entry.isStart( ))
		{
			calculateTopStyles();
		}	

		int col = span.getCol( );
		int cp = dataMap.getListSize( col );

		cp = cp > 0 ? cp - 1 : 0;

		for ( int i = 0; i < span.getColSpan( ) + 1; i++ )
		{
			StyleBuilder.applyBottomBorder( entry, ( (Data) dataMap.get( i
					+ col, cp ) ).style );
		}
		
		styles.pop( );
		spans.pop( );
		pos.pop();
	}

	public Span getContainerSpan( )
	{
		return (Span) spans.peek( );
	}

	public StyleEntry getStyle( IStyle style, Span span )
	{
		// This style associated element is not in any container.
		return initStyle( style, span );
	}

	public int getStyleID( StyleEntry entry )
	{
		if ( style2id.get( entry ) != null )
		{
			return ( (Integer) style2id.get( entry ) ).intValue( );
		}
		else
		{
			int styleId = ID;
			style2id.put( entry, new Integer( styleId ) );
			ID++;
			return styleId;
		}
	}

	public void visit( Object o )
	{
		Data d = (Data) o;
		d.styleId = engine.getStyleID( d.style );
	}

	public Map getStyleIDMap( )
	{
		return style2id;
	}

	private void applyContainerTopBorder( Span span, int pos )
	{
		StyleEntry entry = (StyleEntry) styles.peek( );
		int col = span.getCol( );
		
		for ( int i = col; i < span.getColSpan( ) + col + 1; i++ )
		{
			StyleBuilder.applyTopBorder( entry,
					( (Data) dataMap.get( i, pos ) ).style );
		}
	}

	private void applyHBorders( StyleEntry cEntry, StyleEntry entry,
			Span cSpan, Span span )
	{
		if ( cSpan == null || span == null )
		{
			return;
		}
		if ( cSpan.getCol( ) == span.getCol( ) )
		{
			StyleBuilder.applyLeftBorder( cEntry, entry );
		}

		if ( ( cSpan.getCol( ) + cSpan.getColSpan( ) ) == ( span.getCol( ) + span
				.getColSpan( ) ) )
		{
			StyleBuilder.applyRightBorder( cEntry, entry );
		}
	}

	private StyleEntry initStyle( IStyle style, Span span )
	{
		StyleEntry entry = StyleBuilder.createStyleEntry( style );

		if ( styles.size( ) > 0 )
		{
			StyleEntry cEntry = (StyleEntry) styles.peek( );
			StyleBuilder.mergeInheritableProp( cEntry, entry );
			applyHBorders( cEntry, entry, (Span) ( spans.peek( ) ), span );
		}

		return entry;
	}

	private void release( )
	{
		ID = 0;
		styles.clear( );
		pos.clear( );
		spans.clear( );
		style2id.clear( );
	}

	private void setDataMap( ListBuffer dataMap )
	{
		this.dataMap = dataMap;
	}
}
