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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.text.FlowBox;
import org.eclipse.draw2d.text.FlowFigure;
import org.eclipse.draw2d.text.FlowPage;
import org.eclipse.draw2d.text.ParagraphTextLayout;
import org.eclipse.swt.graphics.Font;

/**
 * A Figure with an embedded TextFlow within a FlowPage that contains text.
 * 
 *  
 */
public class LabelFigure extends ReportElementFigure
{

	private static final Dimension ZERO_DIMENSION = new Dimension( );

	private TextFlow label;

	private FlowPage flowPage;

	private String display;

	/**
	 * Creates a new LabelFigure with a default MarginBorder size 3 and a
	 * FlowPage containing a TextFlow with the style WORD_WRAP_SOFT.
	 */
	public LabelFigure( )
	{
		this( 1 );
	}

	/**
	 * Creates a new LabelFigure with a MarginBorder that is the given size and
	 * a FlowPage containing a TextFlow with the style WORD_WRAP_HARD.
	 * 
	 * @param borderSize
	 *            the size of the MarginBorder
	 */
	public LabelFigure( int borderSize )
	{
		setForegroundColor( ReportColorConstants.ShadowLineColor );
		setBorder( new MarginBorder( borderSize ) );

		label = new TextFlow( ) {

			public void postValidate( )
			{
				if ( DesignChoiceConstants.DISPLAY_BLOCK.equals( display )
						|| DesignChoiceConstants.DISPLAY_INLINE.equals( display ) )
				{
					List list = getFragments( );
					FlowBox box;

					int left = Integer.MAX_VALUE, top = left;
					int bottom = Integer.MIN_VALUE;

					for ( int i = 0; i < list.size( ); i++ )
					{
						box = (FlowBox) list.get( i );
						left = Math.min( left, box.x );
						top = Math.min( top, box.y );
						bottom = Math.max( bottom, box.y + box.getHeight( ) );
					}

					setBounds( new Rectangle( left,
							top,
							LabelFigure.this.getClientArea( ).width,
							bottom - top ) );

					list = getChildren( );
					for ( int i = 0; i < list.size( ); i++ )
					{
						( (FlowFigure) list.get( i ) ).postValidate( );
					}
				}
				else
				{
					super.postValidate( );
				}
			}
		};

		label.setLayoutManager( new ParagraphTextLayout( label,
				ParagraphTextLayout.WORD_WRAP_SOFT ) );

		flowPage = new FlowPage( );

		flowPage.add( label );

		setLayoutManager( new StackLayout( ) );

		add( flowPage );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#getMinimumSize(int, int)
	 */
	public Dimension getMinimumSize( int wHint, int hHint )
	{
		if ( DesignChoiceConstants.DISPLAY_NONE.equals( display ) )
		{
			return ZERO_DIMENSION;
		}

		if ( wHint == -1 && hHint == -1 )
		{
			//return ZERO_DIMENSION;
			return new Dimension( getInsets( ).getWidth( )
					+ getMinimumFontSize( getFont( ) ),
					getInsets( ).getHeight( ) );
		}

		//return the true minimum size with minimum width;
		Dimension dim = super.getMinimumSize( -1, hHint );
		
		if (dim.width < wHint)
		{
			return dim; 
		}

		return super.getMinimumSize( wHint, hHint );
	}

	private static int getMinimumFontSize( Font ft )
	{
		if ( ft != null && ft.getFontData( ).length > 0 )
		{
			return ft.getFontData( )[0].height;
		}

		return 0;
	}

	/**
	 * Sets the display property of the Label.
	 * 
	 * @param display
	 *            the display property. this should be one of the following:
	 *            DesignChoiceConstants.DISPLAY_BLOCK |
	 *            DesignChoiceConstants.DISPLAY_INLINE |
	 *            DesignChoiceConstants.DISPLAY_NONE
	 */
	public void setDisplay( String display )
	{
		this.display = display;
	}

	/**
	 * Returns the text inside the TextFlow.
	 * 
	 * @return the text flow inside the text.
	 */
	public String getText( )
	{
		return label.getText( );
	}

	/**
	 * Sets the text of the TextFlow to the given value.
	 * 
	 * @param newText
	 *            the new text value.
	 */
	public void setText( String newText )
	{
		label.setText( newText );
	}

	/**
	 * Sets the over-line style of the text.
	 * 
	 * @param textOverline
	 *            The textOverline to set.
	 */
	public void setTextOverline( String textOverline )
	{
		label.setTextOverline( textOverline );
	}

	/**
	 * Sets the line-through style of the text.
	 * 
	 * @param textLineThrough
	 *            The textLineThrough to set.
	 */
	public void setTextLineThrough( String textLineThrough )
	{
		label.setTextLineThrough( textLineThrough );
	}

	/**
	 * Sets the underline style of the text.
	 * 
	 * @param textUnderline
	 *            The textUnderline to set.
	 */
	public void setTextUnderline( String textUnderline )
	{
		label.setTextUnderline( textUnderline );
	}

	/**
	 * Sets the horizontal text alignment style.
	 * 
	 * @param textAlign
	 *            The textAlign to set.
	 */
	public void setTextAlign( String textAlign )
	{
		label.setTextAlign( textAlign );
	}

	/**
	 * Sets the vertical text alignment style.
	 * 
	 * @param verticalAlign
	 *            The verticalAlign to set.
	 */
	public void setVerticalAlign( String verticalAlign )
	{
		label.setVerticalAlign( verticalAlign );
	}
}