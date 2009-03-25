/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.text.Bidi;

public class LineArea extends InlineStackingArea
{

	protected byte baseLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;

	public LineArea( ContainerArea parent, LayoutContext context )
	{
		super( parent, context, null );
		assert(parent!=null);
		isInInlineStacking = parent.isInInlineStacking;
	}

	public LineArea( LineArea area )
	{
		super( area );
		this.baseLevel = area.baseLevel;
	}

	public void setBaseLevel( byte baseLevel )
	{
		this.baseLevel = baseLevel;
	}

	public void addChild( IArea area )
	{
		// FIXME ?
		int childHorizontalSpan = area.getX( ) + area.getWidth( );
		int childVerticalSpan = area.getY( ) + area.getHeight( );

		if ( childHorizontalSpan > width )
		{
			setWidth( childHorizontalSpan );
		}

		if ( childVerticalSpan > height )
		{
			setHeight( childVerticalSpan );
		}
		children.add( area );
	}

	public void align( boolean lastLine, LayoutContext context )
	{
		assert ( parent instanceof BlockContainerArea );
		CSSValue align = ( (BlockContainerArea) parent ).getTextAlign( );

		// bidi_hcg: handle empty and justify align in RTL direction as right
		// alignment
		boolean isRightAligned = BidiAlignmentResolver.isRightAligned(
				parent.content, align, lastLine );

		// single line
		if ( ( isRightAligned || IStyle.CENTER_VALUE.equals( align ) ) )
		{
			int spacing = width - currentIP;
			Iterator iter = getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea area = (AbstractArea) iter.next( );

				if ( isRightAligned )
				{
					if ( parent.content.isDirectionRTL( ) )
					{
						area.setPosition( spacing + area.getX( ), area.getY( ) );
					}
					else
					{
						area.setPosition( spacing + area.getX( )
								+ ignoreRightMostWhiteSpace( ), area.getY( ) );
					}
				}
				else if ( IStyle.CENTER_VALUE.equals( align ) )
				{
					area.setPosition( spacing / 2 + area.getX( ), area.getY( ) );
				}

			}
		}
		else if ( IStyle.JUSTIFY_VALUE.equals( align ) && !lastLine )
		{
			justify( );
		}
		if ( context.getBidiProcessing( ) )
			reorderVisually( );
		verticalAlign( );
	}
	
	private int ignoreRightMostWhiteSpace( )
	{
		AbstractArea area = this;
		while ( area instanceof ContainerArea )
		{
			ArrayList children = ( (ContainerArea) area ).children;
			if ( children != null && children.size( ) > 0 )
			{
				area = (AbstractArea) children.get( children.size( ) - 1 );
			}
			else
			{
				return 0;
			}
			if ( area instanceof TextArea )
			{
				String text = ( (TextArea) area ).getText( );
				if ( null != text )
				{
					char[] charArray = text.toCharArray( );
					int len = charArray.length;
					while ( len > 0 && ( charArray[len - 1] <= ' ' ) )
					{
						len--;
					}
					if ( len != charArray.length )
					{
						return ( (TextArea) area ).getTextWidth( text
								.substring( len ) );
					}
				}
			}
		}
		return 0;
	}

	private int adjustWordSpacing( int wordSpacing, ContainerArea area )
	{
		if ( wordSpacing == 0 )
			return 0;
		Iterator iter = area.getChildren( );
		int delta = 0;
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			if ( child instanceof TextArea )
			{
				TextArea textArea = (TextArea) child;
				String text = textArea.getText( );
				int blankNumber = text.split( " " ).length - 1;
				if ( blankNumber > 0 )
				{
					TextStyle style = new TextStyle( textArea.getStyle( ) );
					int original = style.getWordSpacing( );
					style.setWordSpacing( original + wordSpacing );
					textArea.setStyle( style );
					int spacing = wordSpacing * blankNumber;
					child.setWidth( child.getWidth( ) + spacing );
					child.setPosition( child.getX( ) + delta, child.getY( ) );
					delta += spacing;
				}
			}
			else if ( child instanceof ContainerArea )
			{
				child.setPosition( child.getX( ) + delta, child.getY( ) );
				int spacing = adjustWordSpacing( wordSpacing,
						(ContainerArea) child );
				child.setWidth( child.getWidth( ) + spacing );
				delta += spacing;
			}
			else
			{
				child.setPosition( child.getX( ) + delta, child.getY( ) );
			}
		}
		return delta;
	}

	private int adjustLetterSpacing( int letterSpacing, ContainerArea area )
	{
		Iterator iter = area.getChildren( );
		int delta = 0;
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			if ( child instanceof TextArea )
			{
				TextArea textArea = (TextArea) child;
				String text = textArea.getText( );
				int letterNumber = ( text.length( ) > 1
						? ( text.length( ) - 1 )
						: 0 );
				TextStyle style = textArea.getStyle( );
				int original = style.getLetterSpacing( );
				style.setLetterSpacing( original + letterSpacing );
				int spacing = letterSpacing * letterNumber;
				child.setWidth( child.getWidth( ) + spacing );
				child.setPosition( child.getX( ) + delta, child.getY( ) );
				delta += spacing;

			}
			else if ( child instanceof ContainerArea )
			{
				child.setPosition( child.getX( ) + delta, child.getY( ) );
				int spacing = adjustLetterSpacing( letterSpacing,
						(ContainerArea) child );
				child.setWidth( child.getWidth( ) + spacing );
				delta += spacing;
			}
			else
			{
				child.setPosition( child.getX( ) + delta, child.getY( ) );
			}
		}
		return delta;
	}

	private int getBlankNumber( ContainerArea area )
	{
		int count = 0;
		Iterator iter = area.getChildren( );
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			if ( child instanceof TextArea )
			{
				String text = ( (TextArea) child ).getText( );
				count = count + text.split( " " ).length - 1;
			}
			else if ( child instanceof ContainerArea )
			{
				count += getBlankNumber( (ContainerArea) child );
			}
		}
		return count;
	}

	private int getLetterNumber( ContainerArea area )
	{
		int count = 0;
		Iterator iter = area.getChildren( );
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			if ( child instanceof TextArea )
			{
				String text = ( (TextArea) child ).getText( );
				count = ( text.length( ) > 1 ? ( text.length( ) - 1 ) : 0 ) - 1;
			}
			else if ( child instanceof ContainerArea )
			{
				count += getLetterNumber( (ContainerArea) child );
			}
		}
		return count;
	}

	protected void justify( )
	{
		int spacing = width - currentIP;
		int blankNumber = getBlankNumber( this );
		if ( blankNumber > 0 )
		{
			int wordSpacing = spacing / blankNumber;
			adjustWordSpacing( wordSpacing, this );
		}
		else
		{
			int letterNumber = getLetterNumber( this );
			if ( letterNumber > 0 )
			{
				int letterSpacing = spacing / letterNumber;
				adjustLetterSpacing( letterSpacing, this );
			}
		}

	}

	/**
	 * Puts container's child areas into the visual (display) order and
	 * repositions them following that order horizontally.
	 * 
	 * @author Lina Kemmel
	 */
	private void reorderVisually( )
	{
		int n = getChildrenCount( );
		if ( n < 2 )
			return;

		AbstractArea[] children = new AbstractArea[n];
		byte[] levels = new byte[n];
		Iterator iter = getChildren( );
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

	public void endLine( ) throws BirtException
	{
		close( false );
		//initialize( );
		currentIP = 0;
	}

	public int getMaxLineWidth( )
	{
		return maxAvaWidth;
	}

	public boolean isEmptyLine( )
	{
		return getChildrenCount( ) == 0;
	}

	public void update( AbstractArea area ) throws BirtException
	{
		int aWidth = area.getAllocatedWidth( );
		if(aWidth + currentIP > maxAvaWidth )
		{
			removeChild( area );
			endLine( );
			children.add( area );
		}
		area.setAllocatedPosition( currentIP, currentBP );
		currentIP += aWidth;
		int height = area.getAllocatedHeight( );
		if ( height > getHeight( ) )
		{
			this.height = height;
		}
	}
	
	protected void close( boolean isLastLine ) throws BirtException
	{
		if ( children.size( ) == 0 )
		{
			return;
		}
		int lineHeight = ( (BlockContainerArea) parent ).getLineHeight( );
		height = Math.max( height, lineHeight );
		width = Math.max( currentIP, maxAvaWidth );
		align( isLastLine, context );
		if ( isLastLine )
		{
			checkPageBreak( );
			parent.add( this );
			parent.update( this );
		}
		else
		{
			if ( !isInInlineStacking && context.isAutoPageBreak( ) )
			{
				int aHeight = getAllocatedHeight( );
				while ( aHeight + parent.getAbsoluteBP( ) >= context.getMaxBP( ) )
				{
					parent.autoPageBreak( );
					aHeight = getAllocatedHeight( );
				}
			}
			LineArea area = cloneArea( );
			area.children = children;
			area.setParent( parent );
			children = new ArrayList( );
			parent.add( area );
			parent.update( area );
			// setPosition(parent.currentIP + parent.getOffsetX( ),
			// parent.getOffsetY() + parent.currentBP);
			height = 0;
			this.baseLine = 0;
		}
	}

	public void close( ) throws BirtException
	{
		close( true );
		finished = true;
	}

	public void initialize( ) throws BirtException
	{
		hasStyle = false;
		boxStyle = BoxStyle.DEFAULT;
		localProperties = LocalProperties.DEFAULT;
		maxAvaWidth = parent.getCurrentMaxContentWidth( );

		// Derive the baseLevel from the parent content direction.
		if ( parent.content != null )
		{
			if ( CSSConstants.CSS_RTL_VALUE.equals( parent.content
					.getComputedStyle( ).getDirection( ) ) )
				baseLevel = Bidi.DIRECTION_RIGHT_TO_LEFT;
		}
		//parent.add( this );
	}
	
	public SplitResult split( int height, boolean force ) throws BirtException
	{
		assert(height<this.height);
		LineArea result = null;
		Iterator iter = children.iterator( );
		while ( iter.hasNext( ) )
		{
			ContainerArea child  = (ContainerArea) iter.next( );
			if ( iter.hasNext( ) )
			{
				if ( child.getX( ) < height )
				{
					continue;
				}
				else
				{
					if ( child.getMinYPosition( ) <= height )
					{
						iter.remove( );
						if ( result == null )
						{
							result = cloneArea( );
						}
						result.addChild( child );
						child.setParent( result );
					}
					else
					{
						SplitResult splitChild = child.split( height - child.getY(), force );
						ContainerArea splitChildArea = splitChild.getResult( );
						if(splitChildArea!=null)
						{
							if ( result == null )
							{
								result = cloneArea( );
							}
							result.addChild( splitChildArea );
							splitChildArea.setParent( result );
						}
					}
				}
			}
			else
			{
				break;
			}
		}

		if(result!=null)
		{
			int h = 0;
			iter = result.getChildren( );
			while(iter.hasNext( ))
			{
				ContainerArea child  = (ContainerArea) iter.next( );
				h = Math.max( h, child.getAllocatedHeight( ) );
			}
			result.setHeight( h );
		}
		
		if(children.size( )>0)
		{
			int h = 0;
			iter = getChildren();
			while(iter.hasNext( ))
			{
				ContainerArea child  = (ContainerArea) iter.next( );
				h = Math.max( h, child.getAllocatedHeight( ) );
			}
			setHeight( h );
		}
		if(result!=null)
		{
			return new SplitResult(result, SplitResult.SPLIT_SUCCEED_WITH_PART);
		}
		else
		{
			return SplitResult.SUCCEED_WITH_NULL;
		}
	}


	public LineArea cloneArea( )
	{
		return new LineArea( this );
	}

	public SplitResult splitLines( int lineCount ) throws BirtException
	{
		return SplitResult.SUCCEED_WITH_NULL;
	}
	
	public boolean isPageBreakAfterAvoid( )
	{
		return false;
	}

	public boolean isPageBreakBeforeAvoid( )
	{
		return false;
	}

	public boolean isPageBreakInsideAvoid( )
	{
		return false;
	}
}
