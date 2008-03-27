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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IInlineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;

public abstract class PDFInlineStackingLM extends PDFStackingLM
		implements
			IInlineStackingLayoutManager
{

	protected List children = new ArrayList( );

	public List getChildren( )
	{
		return children;
	}

	public PDFInlineStackingLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content,  executor );
	}

	protected void addChild( PDFAbstractLM child )
	{
		children.add( child );
		this.child = child;
	}

	protected void cancelChildren( )
	{
		for ( int i = 0; i < this.children.size( ); i++ )
		{
			ILayoutManager child = (ILayoutManager) children.get( i );
			child.cancel( );
		}
	}
	
	protected boolean hasNextChild()
	{
		return true;
	}
	
	public void autoPageBreak()
	{	
		for(int i=0; i<children.size( ); i++)
		{
			PDFAbstractLM childLM = (PDFAbstractLM)children.get( i );
			childLM.autoPageBreak( );
		}
		closeLayout();
		submitRoot( );
	}
	
	protected boolean clearCache()
	{
		return true;
	}
	
	protected void verticalAlign()
	{
		Iterator iter = root.getChildren( );
		while ( iter.hasNext( ) )
		{
			AbstractArea child = (AbstractArea) iter.next( );
			IStyle childStyle = child.getStyle( );
			String vAlign = childStyle.getVerticalAlign( );
			if ( childStyle == null )
			{
				continue;
			}
			if ( CSSConstants.CSS_BASELINE_VALUE.equalsIgnoreCase( vAlign ) )
			{
				child.setPosition( child.getX( ), child.getY( )
						+ getMaxBaseLine( ) - child.getBaseLine( ) );
			}
			else
			{
				int spacing = root.getHeight( ) - child.getAllocatedHeight( );
				if ( spacing < 0 )
				{
					spacing = 0;
				}
				if ( CSSConstants.CSS_BOTTOM_VALUE.equalsIgnoreCase( vAlign ) )
				{
					child.setPosition( child.getX( ), spacing + child.getY( ) );
				}
				else if ( CSSConstants.CSS_MIDDLE_VALUE
						.equalsIgnoreCase( vAlign ) )
				{
					child.setPosition( child.getX( ), spacing / 2
							+ child.getY( ) );
				}
				else
				{
					int lineHeight = ( (PDFBlockStackingLM) parent )
							.getLineHeight( );
					if ( lineHeight > 0 )
					{
						// align to middle, fix issue 164072
						child.setPosition( child.getX( ), spacing / 2
								+ child.getY( ) );
					}
				}
			}
		}
	}
	
	private int maxBaseLine = 0;

	private int getMaxBaseLine( )
	{
		if ( maxBaseLine == 0 )
		{
			Iterator iter = root.getChildren( );
			while ( iter.hasNext( ) )
			{
				AbstractArea child = (AbstractArea) iter.next( );
				maxBaseLine = Math.max( maxBaseLine, child.getBaseLine( ) );
			}
		}
		return maxBaseLine;
	}

}
