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

import java.lang.reflect.Field;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.text.TextFragmentBox;
import org.eclipse.swt.graphics.Color;

/**
 * An enhanced TextFlow inherited from org.eclipse.draw2d.text.TextFlow. Adds
 * supports for horizontal-alignment, line-through, underline styles.
 * 
 */
public class TextFlow extends org.eclipse.draw2d.text.TextFlow
{

	private String specialPREFIX = "";
	/**
	 * The multiple of this is the actual drawing line width.
	 */
	static int LINE_FACTOR = 24;

	/**
	 * Since the <b>ELLIPSIS </b> field in org.eclipse.draw2d.text.TextFlow is
	 * not accessible outside its package, we use this to substitute it.
	 */
	static String ELLIPSIS = "..."; //$NON-NLS-1$

	/**
	 * Since the <b>truncated </b> field in
	 * org.eclipse.draw2d.text.TextFragmentBox is not accessible outside its
	 * package, we use this Field to reflect the value of relevant object.
	 */
	static Field TRUNCATED;

	/**
	 * Text underline style.
	 */
	private String textUnderline = DesignChoiceConstants.TEXT_UNDERLINE_NONE;

	/**
	 * Text line-through style.
	 */
	private String textLineThrough = DesignChoiceConstants.TEXT_LINE_THROUGH_NONE;

	/**
	 * Text over-line style.
	 */
	private String textOverline = DesignChoiceConstants.TEXT_OVERLINE_NONE;

	/**
	 * Text horizontal alignment style.
	 */
	private String textAlign = DesignChoiceConstants.TEXT_ALIGN_LEFT;

	/**
	 * Text vertical alignment style.
	 */
	private String verticalAlign = DesignChoiceConstants.VERTICAL_ALIGN_TOP;

	static
	{
		try
		{
			/**
			 * Here we try to retrieve the original value of <b>ELLIPSIS </b> in
			 * org.eclipse.draw2d.text.TextFlow, so we can adapt the future
			 * change of that class.
			 */

			Field ellipsis = org.eclipse.draw2d.text.TextFlow.class
					.getDeclaredField( "ELLIPSIS" ); //$NON-NLS-1$
			ellipsis.setAccessible( true );

			ELLIPSIS = (String) ellipsis
					.get( new org.eclipse.draw2d.text.TextFlow( ) );
		}
		catch ( SecurityException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( NoSuchFieldException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( IllegalArgumentException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( IllegalAccessException e )
		{
			ExceptionHandler.handle( e );
		}

		try
		{
			/**
			 * Creates the <b>TRUNCATED </b> field object in advance for
			 * efficiency consideration.
			 */
			TRUNCATED = TextFragmentBox.class.getDeclaredField( "truncated" ); //$NON-NLS-1$
			TRUNCATED.setAccessible( true );
		}
		catch ( SecurityException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( NoSuchFieldException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Paints self to specified graphics with given translating point.
	 * 
	 * @param g
	 * @param translationPoint
	 */
	public void paintTo( Graphics g, Point translationPoint )
	{
		paintTo( g, translationPoint.x, translationPoint.y );
	}

	/**
	 * Paints self to specified graphics with given X,Y offsets.
	 * 
	 * @param g
	 * @param xoff
	 * @param yoff
	 */
	public void paintTo( Graphics g, int xoff, int yoff )
	{

		TextFragmentBox frag;
		List fragments = this.getFragments( );
		assert this.getFont( ).getFontData( ).length > 0;

		/**
		 * Calculates the actual drawing line width according to the Font size.
		 */
		int lineWidth = this.getFont( ).getFontData( )[0].getHeight( )
				/ LINE_FACTOR + 1;
		/**
		 * Get the total fragments height first
		 */
		int totalHeight = 0;
		for ( int i = 0; i < fragments.size( ); i++ )
		{
//			FlowBoxWrapper wrapper = new FlowBoxWrapper( (FlowBox) fragments
//					.get( i ) );
			totalHeight += ((TextFragmentBox)fragments.get( i )).getAscent( ) + ((TextFragmentBox)fragments.get( i )).getDescent( );
		}

		for ( int i = 0; i < fragments.size( ); i++ )
		{
			frag = (TextFragmentBox) fragments.get( i );

			//FlowBoxWrapper wrapper = new FlowBoxWrapper( frag );
			String draw = null;

			try
			{
				/**
				 * Uses stored <b>TRUNCATED </b> Field object to reflect the
				 * relevant value.
				 */
				if ( TRUNCATED != null && TRUNCATED.getBoolean( frag ) )
				{
					draw = getText( ).substring( frag.offset,
							frag.offset + frag.length )
							+ ELLIPSIS;
				}
				else
				{
					draw = getText( ).substring( frag.offset,
							frag.offset + frag.length );
				}
			}
			catch ( IllegalArgumentException e )
			{
				ExceptionHandler.handle( e );
			}
			catch ( IllegalAccessException e )
			{
				ExceptionHandler.handle( e );
			}

			/**
			 * Calculates the adjusted left coordinate according to the
			 * horizontal alignment style.
			 */

			// Here we need re-calculate the line width of fragments,
			// since maybe the font style is changed
			// See bugzilla item
			int linew = FigureUtilities.getTextWidth( draw, g.getFont( ) );
			frag.setWidth( linew );
			

			int left = calculateLeft( this.getSize( ).width, frag.getWidth( ) );

			int top = calculateTop( this.getSize( ).height, totalHeight );

			int realX = frag.getX( ) + left + xoff;
			int realY = frag.getBaseline( )-frag.getAscent( ) + top + yoff;


			if ( !isEnabled( ) )
			{
//				g.setForegroundColor( ColorConstants.buttonLightest );
//				g.drawString( draw, realX + 1, realY + 1 );
				g.setForegroundColor( ColorConstants.buttonDarker );
				//g.drawString( draw, realX, realY );
				paintSpecial( g, draw, realX, realY, i==0 );
			}
			else
			{
				//g.drawString( draw, realX, realY );
				paintSpecial( g, draw, realX, realY, i==0 );
			}

			/**
			 * Only draws the line when width is greater than 1 to avoid
			 * unnecessary lines under/in blank text.
			 */
			if ( frag.getWidth( ) > 1 )
			{
				g.setLineWidth( lineWidth );

				/**
				 * Processes the underline style.
				 */
				if ( textUnderline
						.equals( DesignChoiceConstants.TEXT_UNDERLINE_UNDERLINE ) )
				{
					g.drawLine( realX, frag.getBaseline( )+ top + frag.getDescent( )-lineWidth , realX
							+ frag.getWidth( ),  frag.getBaseline( )+top+frag.getDescent( )-lineWidth );
				}

				/**
				 * Processes the line-through style.
				 */
				if ( textLineThrough
						.equals( DesignChoiceConstants.TEXT_LINE_THROUGH_LINE_THROUGH ) )
				{
					g.drawLine( realX,   frag.getBaseline( )+ top - frag.getAscent( )/2+ lineWidth, realX
							+ frag.getWidth( ), frag.getBaseline( )+ top - frag.getAscent( )/2 + lineWidth);
				}
				/**
				 * Processes the over-line style.
				 */
				if ( textOverline
						.equals( DesignChoiceConstants.TEXT_OVERLINE_OVERLINE ) )
				{
					g.drawLine( realX, realY + 1, realX + frag.getWidth( ),
							realY + 1 );
				}

			}

			g.restoreState( );
		}
	}
	
	private void paintSpecial(Graphics g, String text, int x, int y, boolean  firstBox)
	{
 		if (firstBox && specialPREFIX.length( ) != 0 && text.indexOf( specialPREFIX ) == 0)
		{
			int with = FigureUtilities.getTextWidth( specialPREFIX, g.getFont( ));
			Color c = g.getForegroundColor( );
			
			g.setForegroundColor(ReportColorConstants.greyFillColor );
			g.drawString( specialPREFIX, x,y );
			
			g.setForegroundColor( c );
			g.drawString( text.substring( specialPREFIX.length( )), x + with, y );
		}
		else
		{
			g.drawString( text, x, y );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.text.TextFlow#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	protected void paintFigure( Graphics g )
	{
		paintTo( g, 0, 0 );
	}

	/**
	 * Calculates the left coordinate by given container width, text width and
	 * horizontal alignment style.
	 * 
	 * @param compWidth
	 *            Container width.
	 * @param textWidth
	 *            Text width.
	 * @return
	 */
	protected int calculateLeft( int compWidth, int textWidth )
	{
		int rlt = 0;

		if ( textAlign.equals( DesignChoiceConstants.TEXT_ALIGN_LEFT )
				|| textAlign.equals( DesignChoiceConstants.TEXT_ALIGN_JUSTIFY ) )
		{
			rlt = 0;
		}
		else if ( textAlign.equals( DesignChoiceConstants.TEXT_ALIGN_CENTER ) )
		{
			rlt = ( compWidth - textWidth ) / 2;
		}
		else if ( textAlign.equals( DesignChoiceConstants.TEXT_ALIGN_RIGHT ) )
		{
			rlt = ( compWidth - textWidth );
		}

		return rlt;
	}

	/**
	 * Calculates the top coordinate by given container height, text height and
	 * vertical alignment style.
	 * 
	 * @param compHeight
	 *            Container height.
	 * @param textHeight
	 *            text height.
	 * @return
	 */
	protected int calculateTop( int compHeight, int textHeight )
	{
		int rlt = 0;

		if ( verticalAlign.equals( DesignChoiceConstants.VERTICAL_ALIGN_TOP )
				|| verticalAlign
						.equals( DesignChoiceConstants.VERTICAL_ALIGN_BASELINE ) )
		{
			rlt = 0;
		}
		else if ( verticalAlign
				.equals( DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE ) )
		{
			rlt = ( compHeight - textHeight ) / 2;
		}
		else if ( verticalAlign
				.equals( DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM ) )
		{
			rlt = ( compHeight - textHeight );
		}

		return rlt;
	}

	/**
	 * Sets the horizontal text alignment style.
	 * 
	 * @param textAlign
	 *            The textAlign to set.
	 */
	public void setTextAlign( String textAlign )
	{
		this.textAlign = textAlign;
	}

	/**
	 * Gets the horizontal text alignment style.
	 * 
	 * @return
	 */
	public String getTextAlign( )
	{
		return textAlign;
	}

	/**
	 * Sets the over-line style of the text.
	 * 
	 * @param textOverline
	 *            The textOverLine to set.
	 */
	public void setTextOverline( String textOverline )
	{
		this.textOverline = textOverline;
	}

	/**
	 * Sets the line-through style of the text.
	 * 
	 * @param textLineThrough
	 *            The textLineThrough to set.
	 */
	public void setTextLineThrough( String textLineThrough )
	{
		this.textLineThrough = textLineThrough;
	}

	/**
	 * Sets the underline style of the text.
	 * 
	 * @param textUnderline
	 *            The textUnderline to set.
	 */
	public void setTextUnderline( String textUnderline )
	{
		this.textUnderline = textUnderline;
	}

	/**
	 * Sets the vertical text alignment style.
	 * 
	 * @param verticalAlign
	 *            The verticalAlign to set.
	 */
	public void setVerticalAlign( String verticalAlign )
	{
		this.verticalAlign = verticalAlign;
	}

	
	
	/**
	 * @param specialPREFIX
	 */
	public void setSpecialPREFIX( String specialPREFIX )
	{
		if (specialPREFIX == null)
		{
			return;
		}
		this.specialPREFIX = specialPREFIX;
		repaint( );
	}

}