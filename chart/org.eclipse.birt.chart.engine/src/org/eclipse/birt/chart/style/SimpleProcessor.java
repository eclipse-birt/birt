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

package org.eclipse.birt.chart.style;

import java.util.Iterator;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * A default implementation for IStyleProcessor.
 */
public final class SimpleProcessor implements IStyleProcessor
{

	private static final SimpleStyle defaultStyle;

	private static SimpleProcessor instance;

	static
	{
		TextAlignment ta = TextAlignmentImpl.create( );
		FontDefinition font = FontDefinitionImpl.create( "SansSerif", //$NON-NLS-1$
				12,
				false,
				false,
				false,
				false,
				false,
				0,
				ta );

		defaultStyle = new SimpleStyle( font,
				ColorDefinitionImpl.BLACK( ),
				null,
				null,
				null );
	}

	/**
	 * The access entry point.
	 */
	synchronized public static SimpleProcessor instance( )
	{
		if ( instance == null )
		{
			instance = new SimpleProcessor( );
		}

		return instance;
	}

	/**
	 * The constructor.
	 */
	private SimpleProcessor( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyleProcessor#getStyle(org.eclipse.birt.chart.model.attribute.StyledComponent)
	 */
	public IStyle getStyle( Chart model, StyledComponent name )
	{
		if ( model != null && model.getStyles( ).size( ) > 0 )
		{
			for ( Iterator itr = model.getStyles( ).iterator( ); itr.hasNext( ); )
			{
				StyleMap sm = (StyleMap) itr.next( );

				if ( sm.getComponentName( ).equals( name ) )
				{
					Style ss = sm.getStyle( );

					SimpleStyle rt = new SimpleStyle( defaultStyle );

					if ( ss.getFont( ) != null )
					{
						rt.setFont( FontDefinitionImpl.copyInstance( ss.getFont( ) ) );
					}
					if ( ss.getColor( ) != null )
					{
						rt.setColor( ColorDefinitionImpl.copyInstance( ss.getColor( ) ) );
					}
					if ( ss.getBackgroundColor( ) != null )
					{
						rt.setBackgroundColor( ColorDefinitionImpl.copyInstance( ss.getBackgroundColor( ) ) );
					}
					if ( ss.getBackgroundImage( ) != null )
					{
						rt.setBackgroundImage( (Image) EcoreUtil.copy( ss.getBackgroundImage( ) ) );
					}
					if ( ss.getPadding( ) != null )
					{
						rt.setPadding( InsetsImpl.copyInstance( ss.getPadding( ) ) );
					}

					return rt;
				}
			}
		}

		// Always return the default value.
		return defaultStyle.copy( );
	}
}
