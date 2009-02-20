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

package org.eclipse.birt.report.engine.emitter.excel;

import java.util.Hashtable;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.excel.layout.ContainerSizeInfo;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelLayoutEngine;
import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;

/**
 * This class is used to caculate styles for Excel.
 * 
 * 
 */
public class StyleEngine
{
	public static final int DEFAULT_DATE_STYLE = 1;
	
	public static final int RESERVE_STYLE_ID = 20;

	private int styleID = RESERVE_STYLE_ID;	
	private Hashtable<StyleEntry,Integer> style2id = new Hashtable<StyleEntry,Integer>( );
	private ExcelLayoutEngine engine;
	private Stack<StyleEntry> containerStyles = new Stack<StyleEntry>( );
	/**
	 * 
	 * @param dataMap
	 *            layout data
	 * @return a StyleEngine instance
	 */
	public StyleEngine( ExcelLayoutEngine engine )
	{
		this.engine = engine;

		style2id.put( getDefaultEntry( DEFAULT_DATE_STYLE ), new Integer(
				DEFAULT_DATE_STYLE ) );
	}
	
	public StyleEntry getDefaultEntry( int id )
	{
		StyleEntry entry = new StyleEntry( );
		if ( id == DEFAULT_DATE_STYLE )
		{
			entry.setProperty( StyleConstant.DATE_FORMAT_PROP,
					"yyyy-M-d HH:mm:ss AM/PM" );
			entry.setProperty( StyleConstant.DATA_TYPE_PROP, Integer
					.toString( SheetData.DATE ) );
		}
		return entry;
	}

	public StyleEntry createEntry(ContainerSizeInfo sizeInfo, IStyle style)
	{
		if ( style == null )
		{
			return StyleBuilder.createEmptyStyleEntry( );
		}	
		
		StyleEntry entry = initStyle( style, sizeInfo );
		entry.setStart( true );
		return entry;
	}

	public StyleEntry createHorizontalStyle( ContainerSizeInfo rule )
	{
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry( );
		
		if(engine.getContainers( ).size( ) > 0)
		{
			XlsContainer container = engine.getCurrentContainer( );
			ContainerSizeInfo crule = container.getSizeInfo( );
			StyleEntry cEntry = container.getStyle( );

			StyleBuilder.mergeInheritableProp( cEntry, entry );

			if ( rule.getStartCoordinate( ) == crule.getStartCoordinate( ) )
			{
				StyleBuilder.applyLeftBorder( cEntry, entry );
			}

			if ( rule.getEndCoordinate( ) == crule.getEndCoordinate( ) )
			{
				StyleBuilder.applyRightBorder( cEntry, entry );
			}
		}

		return entry;
	}

	public StyleEntry getStyle( IStyle style, ContainerSizeInfo rule )
	{
		// This style associated element is not in any container.
		return initStyle( style, rule );
	}

	public int getStyleID( StyleEntry entry )
	{
		if ( style2id.get( entry ) != null )
		{
			return style2id.get( entry ).intValue( );
		}
		else
		{
			int styleId = styleID;
			style2id.put( entry, new Integer( styleId ) );
			styleID++;
			return styleId;
		}
	}	

	public Map<StyleEntry,Integer> getStyleIDMap( )
	{
		return style2id;
	}

	private void applyHBorders( StyleEntry centry, StyleEntry entry,
			ContainerSizeInfo crule, ContainerSizeInfo rule )
	{
		if ( crule == null || rule == null )
		{
			return;
		}
		if ( crule.getStartCoordinate( ) == rule.getStartCoordinate( ) )
		{
			StyleBuilder.applyLeftBorder( centry, entry );
		}

		if ( crule.getEndCoordinate( ) == rule.getEndCoordinate( ) )
		{
			StyleBuilder.applyRightBorder( centry, entry );
		}
	}

	private StyleEntry initStyle( IStyle style, ContainerSizeInfo rule )
	{
		StyleEntry entry = StyleBuilder.createStyleEntry( style );;
		if ( !containerStyles.isEmpty( ) )
		{
			StyleEntry centry = containerStyles.peek( );
			StyleBuilder.mergeInheritableProp( centry, entry );
		}
		if ( engine.getContainers( ).size( ) > 0 )
		{
			XlsContainer container = engine.getCurrentContainer( );
			StyleEntry cEntry = container.getStyle( );
			StyleBuilder.mergeInheritableProp( cEntry, entry );
			applyHBorders( cEntry, entry, container.getSizeInfo( ), rule );
		}

		return entry;
	}

	public void addContainderStyle( IStyle computedStyle )
	{
		StyleEntry entry = StyleBuilder.createStyleEntry( computedStyle );
		if ( !containerStyles.isEmpty( ) )
		{
			StyleEntry centry = containerStyles.peek( );
			StyleBuilder.mergeInheritableProp( centry, entry );
		}
		containerStyles.add( entry );
	}

	public void removeForeignContainerStyle( )
	{
		if ( !containerStyles.isEmpty( ) )
			containerStyles.pop( );
	}

	/**
	 * 
	 */
	public void applyContainerBottomStyle( )
	{
		applyContainerBottomStyle( engine.getCurrentContainer( ) );
	}

	public void applyContainerBottomStyle( XlsContainer container )
	{
		ContainerSizeInfo rule = container.getSizeInfo( );
		StyleEntry entry = container.getStyle( );
		int start = rule.getStartCoordinate( );
		int col = engine.getAxis( ).getColumnIndexByCoordinate( start );
		int span = engine.getAxis( ).getColumnIndexByCoordinate(
				rule.getEndCoordinate( ) );
		for ( int i = col; i < span; i++ )
		{
			SheetData data = engine.getColumnLastData( i );

			if ( data == null )
			{
				continue;
			}

			StyleBuilder.applyBottomBorder( entry, data.style );
		}
	}
}
