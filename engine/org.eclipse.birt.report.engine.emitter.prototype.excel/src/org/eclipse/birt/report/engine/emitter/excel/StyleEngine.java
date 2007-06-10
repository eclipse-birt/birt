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

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelLayoutEngine;
import org.eclipse.birt.report.engine.emitter.excel.layout.Rule;
import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;

/**
 * This class is used to caculate styles for Excel.
 * 
 * 
 */
public class StyleEngine implements IListVisitor
{

	private int styleID = 0;	
	private Hashtable style2id = new Hashtable( );
	private ExcelLayoutEngine engine;

	/**
	 * 
	 * @param dataMap
	 *            layout data
	 * @return a StyleEngine instance
	 */
	public StyleEngine( ExcelLayoutEngine engine )
	{
		this.engine = engine;
	}
	
	public StyleEntry createEntry(Rule rule, IStyle style)
	{
		if(style ==  null)
		{
			return StyleBuilder.createEmptyStyleEntry( );
		}	
		
		StyleEntry entry = initStyle( style, rule );
		entry.setStart( true );
		return entry;
	}

	public void calculateTopStyles( )
	{
		if ( engine.getContainers( ).size( ) > 0 )
		{
			XlsContainer container = engine.getCurrentContainer( );
			StyleEntry style = container.getStyle( );
			boolean first = style.isStart( );

			if ( first )
			{
				Rule rule = container.getRule( );
				int start = container.getStart( );
				applyContainerTopBorder( rule, start );
				style.setStart( false );
			}
		}
	}

	public StyleEntry createHorizionStyle( Rule rule )
	{
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry( );
		
		if(engine.getContainers( ).size( ) > 0)
		{
			XlsContainer container = engine.getCurrentContainer( );
			Rule crule = container.getRule( );
			StyleEntry cEntry = container.getStyle( );

			StyleBuilder.mergeInheritableProp( cEntry, entry );

			if ( rule.getStart( ) == crule.getStart( ) )
			{
				StyleBuilder.applyLeftBorder( cEntry, entry );
			}

			if ( rule.getEnd( ) == crule.getEnd( ) )
			{
				StyleBuilder.applyRightBorder( cEntry, entry );
			}
		}

		return entry;
	}

	public void removeContainerStyle( )
	{
		calculateBottomStyles( );
	}

	public void calculateBottomStyles( )
	{
		if(engine.getContainers( ).size() == 0)
		{
			return;
		}	
		
		XlsContainer container = engine.getCurrentContainer( );
		Rule rule = container.getRule( );
		StyleEntry entry = container.getStyle( );

		if ( entry.isStart( ) )
		{
			calculateTopStyles( );
		}

		int start = rule.getStart( );
		int col = engine.getAxis().getCoordinate( start );
		int span = engine.getAxis().getCoordinate( rule.getEnd( ) ) - col;
		int cp = engine.getColumnSize( col );

		cp = cp > 0 ? cp - 1 : 0;

		for ( int i = 0; i < span; i++ )
		{
			Data data = engine.getData( i + col, cp );
			
			if(data == null)
			{
				continue;
			}	
			
			StyleBuilder.applyBottomBorder( entry, data.style );
		}		
	}	

	public StyleEntry getStyle( IStyle style, Rule rule )
	{
		// This style associated element is not in any container.
		return initStyle( style, rule );
	}

	public int getStyleID( StyleEntry entry )
	{
		if ( style2id.get( entry ) != null )
		{
			return ( (Integer) style2id.get( entry ) ).intValue( );
		}
		else
		{
			int styleId = styleID;
			style2id.put( entry, new Integer( styleId ) );
			styleID++;
			return styleId;
		}
	}

	public void visit( Object o )
	{
		Data d = (Data) o;
		d.styleId = getStyleID( d.style );
	}

	public Map getStyleIDMap( )
	{
		return style2id;
	}

	private void applyContainerTopBorder( Rule rule, int pos )
	{
		if(engine.getContainers( ).size( ) == 0)
		{
			return;
		}	
		
		XlsContainer container = engine.getCurrentContainer( );
		StyleEntry entry = container.getStyle( );
		int col = engine.getAxis( ).getCoordinate( rule.getStart( ) );
		int span = engine.getAxis( ).getCoordinate( rule.getEnd( ) ) - col;

		for ( int i = col; i < span + col; i++ )
		{
			Data data = engine.getData( i, pos );			
			
			if(data == null || data == engine.waste)
			{
				continue;
			}	
			StyleBuilder.applyTopBorder( entry,	data.style );
		}
	}

	private void applyHBorders( StyleEntry centry, StyleEntry entry,
			Rule crule, Rule rule )
	{
		if ( crule == null || rule == null )
		{
			return;
		}
		if ( crule.getStart( ) == rule.getStart( ) )
		{
			StyleBuilder.applyLeftBorder( centry, entry );
		}

		if ( crule.getEnd( ) == rule.getEnd( ) )
		{
			StyleBuilder.applyRightBorder( centry, entry );
		}
	}

	private StyleEntry initStyle( IStyle style, Rule rule )
	{
		StyleEntry entry = StyleBuilder.createStyleEntry( style );
		
		if(engine.getContainers( ).size( ) > 0)		
		{
			XlsContainer container = engine.getCurrentContainer( );
			StyleEntry cEntry = container.getStyle( );
			StyleBuilder.mergeInheritableProp( cEntry, entry );
			applyHBorders( cEntry, entry, container.getRule( ), rule );
		}

		return entry;
	}
}
