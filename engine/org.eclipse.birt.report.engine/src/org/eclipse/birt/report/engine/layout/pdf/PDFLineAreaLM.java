/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.text.Bidi;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.ILineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.InlineContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.TextArea;
import org.w3c.dom.css.CSSPrimitiveValue;

public class PDFLineAreaLM extends PDFInlineStackingLM
		implements
			ILineStackingLayoutManager
{

	/**
	 * the base-level of the line created by this layout mangaer. each LineArea
	 * has a fixed base-level.
	 */
	private int baseLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;

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
	
	public PDFLineAreaLM( PDFLayoutEngineContext context, PDFStackingLM parent,
			IReportItemExecutor executor )
	{
		super( context, parent, null, executor );
	}

	protected boolean submitRoot( boolean childBreak )
	{
		boolean submit = super.submitRoot( childBreak );
		if ( !submit )
		{
			if ( childBreak )
			{
				// FIXME special case: inline text with page-break-after at
				// first line of page
				// breakAfterRelayout = true;
			}
		}
		last = root;
		return submit;

	}

	protected boolean traverseChildren( )
	{
		// need relayout
		if ( last != null )
		{
			parent.addArea( last );
			last = null;
			if ( breakAfterRelayout )
			{
				breakAfterRelayout = false;
				return true;
			}
		}

		boolean childBreak = false;

		if ( children.size( ) > 0 )
		{
			Iterator iterLM = children.iterator( );
			while ( iterLM.hasNext( ) )
			{
				PDFAbstractLM childLM = (PDFAbstractLM) iterLM.next( );
				child = childLM;
				boolean currentBreak = childLM.layout( );
				if ( currentBreak )
				{
					childBreak = true;
				}
				else
				{
					iterLM.remove( );
				}
			}
			if ( childBreak )
			{
				return true;
			}
		}
		boolean childHasNext = false;
		while ( executor.hasNextChild( ) || unfinishedExecutor!=null )
		{
			IReportItemExecutor childExecutor = null;
			if(unfinishedExecutor!=null)
			{
				childExecutor = unfinishedExecutor;
				unfinishedExecutor = null;
			}
			else
			{
				childExecutor = executor.getNextChild( );
			}
			assert ( childExecutor != null );
			childHasNext = handleChild( childExecutor ) || childHasNext;
			if ( childHasNext )
			{
				if ( lineFinished  )
				{
					return true;
				}
			}
		}
		return childHasNext;
	}

	protected boolean handleChild( IReportItemExecutor childExecutor )
	{
		// child should be inline element
		boolean childBreak = false;
		IContent childContent = childExecutor.execute( );
		PDFAbstractLM childLM = getFactory( ).createLayoutManager( this,
				childContent, childExecutor );
		if(needLineBreak( childContent ))
		{
			unfinishedExecutor = childExecutor;
			return !endLine();
		}
		else
		{
			childBreak = childLM.layout( );
			if ( childBreak )
			{
				if ( !childLM.isFinished( ) )
				{
					addChild( childLM );
					if(currentIP<expectedIP)
					{
						currentIP = expectedIP;
					}
					if(!(childLM instanceof PDFStackingLM))
					{
						lineFinished = true;
					}
				}
			}
			return childBreak;
		}
	}

	protected void createRoot( )
	{
		root = AreaFactory.createLineArea( context.getReport( ));
		lineCount++;
	}

	protected void newContext( )
	{
		createRoot( );
		setMaxAvaWidth( parent.getMaxAvaWidth( ) );
		setMaxAvaHeight( parent.getMaxAvaHeight( ) - parent.getCurrentBP( ) );
		root.setWidth( getMaxAvaWidth( ) );
		setCurrentBP( 0 );
		if ( lineCount == 1 )
		{
			assert ( parent instanceof IBlockStackingLayoutManager );
			setCurrentIP( ( (PDFBlockStackingLM) parent ).getTextIndent( ) );
		}
		else
		{
			setCurrentIP( currentPosition );
		}
		setupMinHeight( );
	}

	/**
	 * submit current line to parent true if succeed
	 * 
	 * @return
	 */
	public boolean endLine( )
	{
		updateLine( );
		align( false );
		boolean ret = true;
		if(root.getChildrenCount( )>0)
		{
			ret = parent.addArea( root );
		}
		lineFinished = true;
		if ( ret )
		{
			newContext( );
			last = null;
			return true;
		}
		else
		{
			last = root;
			return false;
		}
	}

	protected void closeLayout( )
	{
		updateLine( );
		align( true );
	}

		 

	public boolean addArea( IArea area )
	{
		AbstractArea cArea = (AbstractArea) area;
		root.addChild( cArea );
		cArea.setAllocatedPosition( getCurrentIP( ), getCurrentBP( ) );
		setCurrentIP( getCurrentIP( ) + cArea.getAllocatedWidth( ) );
		lineFinished = false;
		return true;
	}

	protected void setupMinHeight( )
	{
		assert ( parent instanceof PDFBlockStackingLM );
		int lineHeight = ( (PDFBlockStackingLM) parent ).getLineHeight( );
		if ( lineHeight > 0 )
		{
			minHeight = lineHeight;
		}
	}

	protected void updateLine( )
	{
		if ( root == null )
		{
			return;
		}
		Iterator iter = root.getChildren( );
		int height = root.getHeight( );
		int lineHeight = ( (PDFBlockStackingLM) parent ).getLineHeight( );
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			int childHeight = child.getAllocatedHeight( );
			height = Math.max( height, childHeight );
		}
		height = Math.max( height, lineHeight );
		root.setContentHeight( height );
		// root.setWidth( getCurrentIP() );

	}

	protected void align( boolean lastLine)
	{
		if ( root == null )
		{
			return;
		}
		assert ( parent instanceof PDFBlockStackingLM );
		String align = ( (PDFBlockStackingLM) parent ).getTextAlign( );
		// single line
		if ( ( CSSConstants.CSS_RIGHT_VALUE.equalsIgnoreCase( align ) || CSSConstants.CSS_CENTER_VALUE
				.equalsIgnoreCase( align ) ) )
		{
			int spacing = root.getContentWidth( ) - getCurrentIP( );
			Iterator iter = root.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea area = (AbstractArea) iter.next( );
				if ( CSSConstants.CSS_RIGHT_VALUE.equalsIgnoreCase( align ) )
				{
					area.setAllocatedPosition( spacing + area.getAllocatedX( ),
							area.getAllocatedY( ) );
				}
				else if ( CSSConstants.CSS_CENTER_VALUE
						.equalsIgnoreCase( align ) )
				{
					area.setAllocatedPosition( spacing / 2
							+ area.getAllocatedX( ), area.getAllocatedY( ) );
				}
			}

		}
		else if( CSSConstants.CSS_JUSTIFY_VALUE.equalsIgnoreCase( align ) && !lastLine)
		{
			justify();
		}
		// FIXME to implement
		// implement vertical alignment, current only support top, bottom and
		// center
		// reslove used value of height
		Iterator iter = root.getChildren( );
		int height = root.getHeight( );
		// vertical alignment
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			IStyle childStyle = child.getStyle( );
			String vAlign = childStyle.getVerticalAlign( );
			if ( childStyle != null )
			{
				int spacing = height - child.getAllocatedHeight( );
				assert ( spacing >= 0 );
				if ( CSSConstants.CSS_BOTTOM_VALUE.equalsIgnoreCase( vAlign )
						|| CSSConstants.CSS_BASELINE_VALUE
								.equalsIgnoreCase( vAlign ) )
				{
					child.setPosition( child.getX( ), spacing + child.getY( ) );
				}
				else if ( CSSConstants.CSS_MIDDLE_VALUE
						.equalsIgnoreCase( vAlign ) )
				{
					child.setPosition( child.getX( ), spacing + child.getY( ) );
				}
				else
				{
					// do nothing
				}
			}

		}
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
	
	public void setBaseLevel( int baseLevel )
	{
		this.baseLevel = baseLevel;
	}

	public boolean isEmptyLine( )
	{
		return isRootEmpty( );
	}
	
	private boolean needLineBreak(IContent content)
	{
		int specWidth = 0;
		int avaWidth = getMaxAvaWidth( ) - this.getCurrentIP( );
		int calWidth = getDimensionValue( content.getWidth( ) );
		if ( calWidth > 0 && calWidth < getMaxAvaWidth() )
		{
			specWidth = calWidth;
		}
		if(specWidth<=avaWidth && specWidth>0)
		{
			expectedIP = currentIP + specWidth;
		}
		return specWidth>avaWidth;
	}
}
