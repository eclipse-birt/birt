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

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.ILineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class PDFLineAreaLM extends PDFInlineStackingLM
		implements
			ILineStackingLayoutManager
{

	/**
	 * the base-level of the line created by this layout mangaer. each LineArea
	 * has a fixed base-level.
	 */
	//private int baseLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;

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

	protected boolean submitRoot(  )
	{
		boolean submit = super.submitRoot( );
		if ( !submit )
		{
			//if ( childBreak )
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
			if(parent.addArea( last, false, false ))
			{
				last = null;
			}
			else
			{
				context.setAutoPageBreak( true );
				//FIXME implement autoPageBreak
				return false;
			}
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
				if ( lineFinished )
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

	protected void initialize( )
	{
		createRoot( );
		maxAvaWidth =  parent.getCurrentMaxContentWidth( ) ;
		maxAvaHeight = parent.getCurrentMaxContentHeight( )  ;
		root.setWidth( parent.getCurrentMaxContentWidth( ) );
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
		align( );
		boolean ret = true;
		if(root.getChildrenCount( )>0)
		{
			ret = parent.addArea( root, false, false );
		}
		lineFinished = true;
		if ( ret )
		{
			initialize( );
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
		align( );
	}

		 

	public boolean addArea( IArea area, boolean keepWithPrevious, boolean keepWithNext )
	{
		submit((AbstractArea)area);
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

	protected void align( )
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
				if ( CSSConstants.CSS_BOTTOM_VALUE.equalsIgnoreCase( vAlign ))
				{
					child.setPosition( child.getX( ), spacing + child.getY( ) );
				}
				else if ( CSSConstants.CSS_MIDDLE_VALUE
						.equalsIgnoreCase( vAlign ) )
				{
					child.setPosition( child.getX( ), spacing/2 + child.getY( ) );
				}
				else
				{
					int lineHeight = ( (PDFBlockStackingLM) parent ).getLineHeight( );
					if(lineHeight>0)
					{
						//align to middle, fix issue 164072
						child.setPosition( child.getX( ), spacing/2 + child.getY( ) );
					}
				}
			}

		}
	}

	public boolean isEmptyLine( )
	{
		return isRootEmpty( );
	}
	
	private boolean needLineBreak(IContent content)
	{
		int specWidth = 0;
		int avaWidth = getCurrentMaxContentWidth( );
		int calWidth = getDimensionValue( content.getWidth( ) );
		if ( calWidth > 0 && calWidth < this.maxAvaWidth )
		{
			specWidth = calWidth;
		}
		if(specWidth<=avaWidth && specWidth>0)
		{
			expectedIP = currentIP + specWidth;
		}
		return specWidth>avaWidth;
	}


	public void submit( AbstractArea area )
	{
		root.addChild( area );
		area.setAllocatedPosition( getCurrentIP( ), getCurrentBP( ) );
		setCurrentIP( getCurrentIP( ) + area.getAllocatedWidth( ) );
		lineFinished = false;
		
	}
}
