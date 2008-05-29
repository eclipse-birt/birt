/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 * IBM Corporation - Bidi reordering implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.InlineContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.TextArea;
import org.w3c.dom.css.CSSPrimitiveValue;

import com.ibm.icu.text.Bidi;

public class LineLayout extends InlineStackingLayout implements IInlineStackingLayout
{
	
	/**
	 * the base-level of the line created by this layout manager. each LineArea
	 * has a fixed base-level.
	 */
	private byte baseLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;

	/**
	 * line counter
	 */
	protected int lineCount = 0;

	/**
	 * current position in current line
	 */
	protected int currentPosition = 0;

	protected boolean breakAfterRelayout = false;

	protected boolean lineFinished = true;

	protected HashMap positionMap = new HashMap( );

	protected ContainerArea last = null;
	
	protected int expectedIP = 0;
	
	protected IReportItemExecutor unfinishedExecutor = null;
	
	protected boolean isEmpty = true;
	
	protected int lineHeight;
	
	
	public LineLayout(LayoutEngineContext context, ContainerLayout parentContext )
	{
		super(context, parentContext, null );
	}
	
	protected void createRoot( )
	{
		root = AreaFactory.createLineArea( context.getReport( ));
		lineCount++;
	}

	protected void initialize( )
	{
		createRoot( );
		maxAvaWidth = parent.getCurrentMaxContentWidth( );
		maxAvaHeight = parent.getCurrentMaxContentHeight( );
		root.setWidth( parent.getCurrentMaxContentWidth( ) );
		lineHeight = ( (BlockStackingLayout) parent ).getLineHeight( );

		 // Derive the baseLevel from the parent content direction.
		if ( parent.content != null )
		{
			if ( CSSConstants.CSS_RTL_VALUE.equals( parent.content
					.getComputedStyle( ).getDirection( ) ) )
				baseLevel = Bidi.DIRECTION_RIGHT_TO_LEFT;
		}
	}
	
	public void setTextIndent( ITextContent content )
	{
		if( isEmpty )
		{
			if ( content != null )
			{
				IStyle contentStyle = content.getComputedStyle( );
				currentIP =  getDimensionValue( contentStyle
						.getProperty( StyleConstants.STYLE_TEXT_INDENT ), maxAvaWidth ) ;
			}
		}
	}
	
	/**
	 * submit current line to parent true if succeed
	 * 
	 * @return
	 */
	public boolean endLine( )
	{
		root.setHeight( Math.max( root.getHeight( ), lineHeight ) );
		align( false );
		if(root.getChildrenCount( )>0)
		{
			parent.addArea( root );
		}
		lineFinished = true;
		initialize( );
		currentIP = 0;
		return true;
	}

	protected void closeLayout( )
	{
		if ( root.getChildrenCount( ) == 0 )
		{
			lineCount--;
			return;
		}
		root.setHeight( Math.max( root.getHeight( ), lineHeight ) );
		align( true );
		parent.addArea( root );
	}

		 

	public boolean addArea( AbstractArea area )
	{
		area.setAllocatedPosition( currentIP, currentBP );
		currentIP += area.getAllocatedWidth( );
		
		if ( currentIP > root.getWidth( ))
		{
			root.setWidth( currentIP );
		}
		int height = area.getAllocatedHeight( );
		if ( currentBP + height > root.getHeight( ))
		{
			root.setHeight( currentBP + height );
		}		
		root.addChild( area );
		isEmpty = false;
		lineFinished = false;
		return true;
	}


	protected void align( boolean lastLine)
	{
		assert ( parent instanceof BlockStackingLayout );
		String align = ( (BlockStackingLayout) parent ).getTextAlign( );
		// single line
		if ( ( CSSConstants.CSS_RIGHT_VALUE.equalsIgnoreCase( align ) || CSSConstants.CSS_CENTER_VALUE
				.equalsIgnoreCase( align ) ) )
		{
			int spacing = root.getWidth( ) - currentIP;
			Iterator iter = root.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea area = (AbstractArea) iter.next( );
				if ( spacing > 0 )
				{
					if ( CSSConstants.CSS_RIGHT_VALUE.equalsIgnoreCase( align ) )
					{
						area.setAllocatedPosition( spacing
								+ area.getAllocatedX( ), area.getAllocatedY( ) );
					}
					else if ( CSSConstants.CSS_CENTER_VALUE
							.equalsIgnoreCase( align ) )
					{
						area.setAllocatedPosition( spacing / 2
								+ area.getAllocatedX( ), area.getAllocatedY( ) );
					}
				}
			}

		}
		else if( CSSConstants.CSS_JUSTIFY_VALUE.equalsIgnoreCase( align ) && !lastLine)
		{
			justify();
		}
		if ( context.getBidiProcessing( ) )
			reorderVisually( );
		verticalAlign();
	}
	

	protected void justify()
	{
		int spacing = root.getContentWidth( ) - getCurrentIP( );
		int blankNumber = 0;
		int charNumber = 0;
		int[] blanks = new int[root.getChildrenCount( )];
		int[] chars = new int[root.getChildrenCount( )];
		Iterator iter = root.getChildren( );
		int index = 0;
		while ( iter.hasNext( ) )
		{
			AbstractArea area = (AbstractArea) iter.next( );
			if(area instanceof TextArea)
			{
				String text = ((TextArea)area).getText( );
				blanks[index] = text.split( " " ).length - 1;
				chars[index] = (text.length( )>1 ? (text.length( )-1): 0);
				blankNumber += blanks[index];
				charNumber += chars[index];
			}
			else if(area instanceof InlineContainerArea)
			{
				ContainerArea container = (InlineContainerArea)area;
				if(container.getChildrenCount( )==1)
				{
					Iterator it = container.getChildren( );
					AbstractArea child = (AbstractArea) it.next( );
					if(child instanceof TextArea)
					{
						String text = ((TextArea)child).getText( );
						blanks[index] = text.split( " " ).length - 1;
						chars[index] = (text.length( )>1 ? (text.length( )-1): 0);
						blankNumber += blanks[index];
						charNumber += chars[index];
					}
				}
			}
				
			index++;
		}
		if(blankNumber>0)
		{
			iter = root.getChildren( );
			int posDelta = 0;
			int wordSpacing = spacing / blankNumber;
			index = 0;
			while ( iter.hasNext( ) )
			{
				AbstractArea area = (AbstractArea) iter.next( );
				
				if(area instanceof TextArea)
				{
					IStyle style = area.getStyle( );
					int original = getDimensionValue( style.getProperty( StyleConstants.STYLE_WORD_SPACING ) );
					style.setProperty( StyleConstants.STYLE_WORD_SPACING, 
							new FloatValue(CSSPrimitiveValue.CSS_NUMBER, original + wordSpacing ));
					area.setWidth( area.getWidth( ) +  wordSpacing*blanks[index]);
				}
				else if(area instanceof InlineContainerArea)
				{
					ContainerArea container = (InlineContainerArea)area;
					if(container.getChildrenCount( )==1)
					{
						Iterator it = container.getChildren( );
						AbstractArea child = (AbstractArea) it.next( );
						if(child instanceof TextArea)
						{
							IStyle style = child.getStyle( );
							int original = getDimensionValue( style.getProperty( StyleConstants.STYLE_WORD_SPACING ) );
							style.setProperty( StyleConstants.STYLE_WORD_SPACING, 
									new FloatValue(CSSPrimitiveValue.CSS_NUMBER, original + wordSpacing ));
							child.setWidth( area.getWidth( ) +  wordSpacing*blanks[index]);
							area.setWidth( area.getWidth( ) +  wordSpacing*blanks[index]);
						}
					}
				}
				area.setPosition( area.getX( )+ posDelta, area.getY( ) );
				if(blanks[index]>0)
				{
					posDelta += wordSpacing*blanks[index];
				}
				index++;
			}
		}
		else if(charNumber>0)
		{
			iter = root.getChildren( );
			int posDelta = 0;
			int letterSpacing = spacing / charNumber;
			index = 0;
			while ( iter.hasNext( ) )
			{
				AbstractArea area = (AbstractArea) iter.next( );
				
				if(area instanceof TextArea)
				{
					IStyle style = area.getStyle( );
					int original = getDimensionValue( style.getProperty( StyleConstants.STYLE_LETTER_SPACING ) );
					style.setProperty( StyleConstants.STYLE_LETTER_SPACING, 
							new FloatValue(CSSPrimitiveValue.CSS_NUMBER, original + letterSpacing ));
					area.setWidth( area.getWidth( ) +  letterSpacing*chars[index]);
				}
				else if(area instanceof InlineContainerArea)
				{
					ContainerArea container = (InlineContainerArea)area;
					if(container.getChildrenCount( )==1)
					{
						Iterator it = container.getChildren( );
						AbstractArea child = (AbstractArea) it.next( );
						if(child instanceof TextArea)
						{
							IStyle style = child.getStyle( );
							int original = getDimensionValue( style.getProperty( StyleConstants.STYLE_LETTER_SPACING ) );
							style.setProperty( StyleConstants.STYLE_LETTER_SPACING, 
									new FloatValue(CSSPrimitiveValue.CSS_NUMBER, original + letterSpacing ));
							child.setWidth( area.getWidth( ) +  letterSpacing*chars[index]);
							area.setWidth( area.getWidth( ) +  letterSpacing*chars[index]);
						}
					}
				}
				area.setPosition( area.getX( ) + posDelta,  area.getY( ) );
				if(chars[index]>0)
				{
					posDelta += letterSpacing*chars[index];
				}
				index++;
			}
		}
		
	}

	public int getMaxLineWidth( )
	{
		return this.maxAvaWidth;
	}
	
	public boolean isEmptyLine( )
	{
		return isRootEmpty( );
	}
	
	/**
	 * Puts container's child areas into the visual (display) order and
	 * repositions them following that order horizontally.
	 * 
	 * @author Lina Kemmel
	 */
	private void reorderVisually( )
	{
		int n = root.getChildrenCount( );
		if ( n < 2 )
			return;

		AbstractArea[] children = new AbstractArea[n];
		byte[] levels = new byte[n];
		Iterator iter = root.getChildren( );
		int i = 0;

		for ( ; i < n && iter.hasNext( ); i++ )
		{
			children[i] = (AbstractArea) iter.next( );
			if ( children[i] instanceof TextArea )
				levels[i] = (byte) ( (TextArea) children[i] ).getRunLevel( );
			else
				levels[i] = baseLevel;
		}
		int x = children[0].getX( );

		Bidi.reorderVisually( levels, 0, children, 0, n );

		for ( i = 0; i < n; i++ )
		{
			children[i].setPosition( x, children[i].getY( ) );
			x += children[i].getAllocatedWidth( );
		}
	}

}
