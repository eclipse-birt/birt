
package org.eclipse.birt.report.engine.layout.pdf;

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
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.IPDFTableLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;

public class PDFTableBandLM extends PDFBlockStackingLM
		implements
			IBlockStackingLayoutManager
{

	protected IPDFTableLayoutManager tbl;
	protected int groupLevel;
	protected int type;
	protected boolean repeatHeader = false;

	public PDFTableBandLM( PDFLayoutEngineContext context,
			PDFStackingLM parent, IContent content, IReportItemExecutor executor )
	{
		super( context, parent, content, executor );
		tbl = getTableLayoutManager( );
		IElement pContent = content.getParent( );

		if ( pContent instanceof IGroupContent )
		{
			IGroupContent groupContent = (IGroupContent) pContent;
			groupLevel = groupContent.getGroupLevel( );
			repeatHeader = groupContent.isHeaderRepeat( );
			type = ( (IBandContent) content ).getBandType( );
			if ( type == IBandContent.BAND_GROUP_HEADER
					&& !( executor instanceof DOMReportItemExecutor )
					&& repeatHeader )
			{
				execute( executor, content );
				executor.close( );
				groupContent.getChildren( ).add( content );
				this.executor = new DOMReportItemExecutor( content );
				this.executor.execute( );
			}
		}
		else if ( pContent instanceof ITableContent )
		{
			ITableContent tableContent = (ITableContent) pContent;
			repeatHeader = tableContent.isHeaderRepeat( );
			type = ( (IBandContent) content ).getBandType( );
			if ( type == IBandContent.BAND_HEADER
					&& !( executor instanceof DOMReportItemExecutor )
					&& repeatHeader )
			{
				execute( executor, content );
				executor.close( );
				tableContent.getChildren( ).add( content );
				this.executor = new DOMReportItemExecutor( content );
				this.executor.execute( );
			}
		}

	}
	
	protected boolean checkAvailableSpace( )
	{
		boolean availableSpace = super.checkAvailableSpace( );
		if(availableSpace && tbl != null)
		{
			tbl.setTableCloseStateAsForced( );
		}
		return availableSpace;
	}

	protected boolean traverseChildren( )
	{
		if ( isFirst && groupLevel >= 0
				&& type == IBandContent.BAND_GROUP_FOOTER )
		{
			tbl.updateUnresolvedCell( groupLevel, false );
		}
		isFirst = false;
		boolean childBreak = super.traverseChildren( );
		if ( !childBreak && groupLevel >= 0
				&& type == IBandContent.BAND_GROUP_FOOTER )
		{
			tbl.updateUnresolvedCell( groupLevel, true );
		}
		return childBreak;
	}

	public int getCurrentBP( )
	{
		return parent.getCurrentBP( );
	}

	protected boolean submitRoot( boolean childBreak )
	{
		return true;
	}

	public int getCurrentIP( )
	{
		return parent.getCurrentIP( );
	}

	public int getMaxAvaHeight( )
	{
		return parent.getMaxAvaHeight( );
	}

	public int getMaxAvaWidth( )
	{
		return parent.getMaxAvaWidth( );
	}

	public int getOffsetX( )
	{
		return parent.getOffsetX( );
	}

	public int getOffsetY( )
	{
		return parent.getOffsetY( );
	}

	public void setCurrentBP( int bp )
	{
		parent.setCurrentBP( bp );
	}

	public void setCurrentIP( int ip )
	{
		parent.setCurrentIP( ip );
	}

	public void setMaxAvaHeight( int height )
	{
		parent.setMaxAvaHeight( height );
	}

	public void setMaxAvaWidth( int width )
	{
		parent.setMaxAvaWidth( width );
	}

	public void setOffsetX( int x )
	{
		parent.setOffsetX( x );
	}

	public void setOffsetY( int y )
	{
		parent.setOffsetY( y );
	}

	public boolean addArea( IArea area )
	{
		return parent.addArea( area );
	}

	protected void createRoot( )
	{
		// do nothing
	}

	protected void newContext( )
	{

	}

	protected IReportItemExecutor createExecutor( )
	{
		return executor;
	}

	protected boolean allowPageBreak( )
	{
		if( type == IBandContent.BAND_GROUP_HEADER || type== IBandContent.BAND_HEADER)
		{
			return !repeatHeader;
		}
		return true;
	}
	
	
}
