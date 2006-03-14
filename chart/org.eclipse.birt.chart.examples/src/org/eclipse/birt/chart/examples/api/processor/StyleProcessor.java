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

package org.eclipse.birt.chart.examples.api.processor;

import java.util.Iterator;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.style.IStyle;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.style.SimpleStyle;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * SimpleProcessor
 */
public final class StyleProcessor implements IStyleProcessor
{

	private static final SimpleStyle sstyle;

	private static StyleProcessor instance;

	static
	{
		TextAlignment ta = TextAlignmentImpl.create( );
		ta.setHorizontalAlignment( HorizontalAlignment.RIGHT_LITERAL );
		ta.setVerticalAlignment( VerticalAlignment.BOTTOM_LITERAL );
		FontDefinition font = FontDefinitionImpl.create( "BookAntique", //$NON-NLS-1$
				14, true, true, true, true, true, 2.0, ta );

		sstyle = new SimpleStyle( font,
				ColorDefinitionImpl.PINK( ),
				ColorDefinitionImpl.CREAM( ),
				null,
				InsetsImpl.create( 1.0, 1.0, 1.0, 1.0 ) );
	}

	/**
	 * @return
	 */
	synchronized public static StyleProcessor instance( )
	{
		if ( instance == null )
		{
			instance = new StyleProcessor( );
		}

		return instance;
	}

	/**
	 * The constructor.
	 */
	private StyleProcessor( )
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
					Style style = sm.getStyle( );

					SimpleStyle rt = new SimpleStyle( sstyle );

					if ( style.getFont( ) != null )
					{
						rt.setFont( (FontDefinition) EcoreUtil.copy( style.getFont( ) ) );
					}
					if ( style.getColor( ) != null )
					{
						rt.setColor( (ColorDefinition) EcoreUtil.copy( style.getColor( ) ) );
					}
					if ( style.getBackgroundColor( ) != null )
					{
						rt.setBackgroundColor( (ColorDefinition) EcoreUtil.copy( style.getBackgroundColor( ) ) );
					}
					if ( style.getBackgroundImage( ) != null )
					{
						rt.setBackgroundImage( (Image) EcoreUtil.copy( style.getBackgroundImage( ) ) );
					}
					if ( style.getPadding( ) != null )
					{
						rt.setPadding( (Insets) EcoreUtil.copy( style.getPadding( ) ) );
					}

					return rt;
				}
			}
		}

		// Always return the default value.
		return sstyle.copy( );
	}
}