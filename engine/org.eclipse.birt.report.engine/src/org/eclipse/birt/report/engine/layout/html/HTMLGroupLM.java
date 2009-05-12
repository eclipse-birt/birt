/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.layout.html.buffer.IPageBuffer;

public class HTMLGroupLM extends HTMLBlockStackingLM
{

	public HTMLGroupLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_GROUP;
	}

	boolean isFirstLayout = true;
	boolean isHeaderRefined = false;

	public void initialize( HTMLAbstractLM parent, IContent content,
			IReportItemExecutor executor, IContentEmitter emitter )
			throws BirtException
	{
		super.initialize( parent, content, executor, emitter );
		isFirstLayout = true;
		isHeaderRefined = false;
	}
	
	protected boolean isHeaderBand()
	{
		if(childLayout!=null)
		{
			IContent band = ((HTMLAbstractLM)childLayout).getContent();
			if(band instanceof IBandContent)
			{
				return ((IBandContent)band).getBandType( )== IBandContent.BAND_GROUP_HEADER;
			}
		}
		return false;
	}

	protected void repeatHeader( ) throws BirtException
	{
		if ( !isFirstLayout )
		{
			IGroupContent group = (IGroupContent) content;
			IBandContent header = group.getHeader( );
			if ( group.isHeaderRepeat( ) && header != null && !isHeaderBand())
			{
				if( header instanceof ITableBandContent )
					refineBandContent( (ITableBandContent) header );
				boolean pageBreak = context.allowPageBreak( );
				context.setAllowPageBreak( false );
				IPageBuffer buffer =  context.getPageBufferManager( );
				boolean isRepeated = buffer.isRepeated();
				buffer.setRepeated( true );
				engine.layout(this, header, emitter );
				buffer.setRepeated( isRepeated );
				context.setAllowPageBreak( pageBreak );
			}
		}
		isFirstLayout = false;
	}
	
	private void refineBandContent( ITableBandContent content )
	{
		if ( isHeaderRefined )
			return;

		Collection children = content.getChildren( );
		ArrayList removed = new ArrayList( );
		if ( children != null )
		{
			Iterator itr = children.iterator( );
			while ( itr.hasNext( ) )
			{
				IRowContent rowContent = (IRowContent) itr.next( );
				RowDesign rowDesign = (RowDesign) rowContent.getGenerateBy( );
				if ( !rowDesign.getRepeatable( ) )
				{
					removed.add( rowContent );
				}
			}
			children.removeAll( removed );
		}
		isHeaderRefined = true;
	}

	protected boolean layoutChildren( ) throws BirtException
	{
		repeatHeader( );
		boolean hasNext = super.layoutChildren( );
		return hasNext;
	}

}
