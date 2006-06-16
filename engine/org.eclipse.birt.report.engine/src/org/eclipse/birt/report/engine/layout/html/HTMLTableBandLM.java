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

package org.eclipse.birt.report.engine.layout.html;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class HTMLTableBandLM extends HTMLListingBandLM
{

	protected boolean dropDetailResolved;
	protected HTMLTableLM tbl;
	protected int groupLevel = 0;
	protected int bandType = IBandContent.BAND_DETAIL;

	public HTMLTableBandLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_TABLE_BAND;
	}

	public void initialize( HTMLAbstractLM parent, IContent content,
			IReportItemExecutor executor, IContentEmitter emitter )
	{
		// TODO Auto-generated method stub
		super.initialize( parent, content, executor, emitter );
		tbl = getTableLayoutManager( );
		ITableBandContent tableBand = (ITableBandContent) content;
		bandType = tableBand.getBandType( );
		IElement pContent = tableBand.getParent( );
		if ( pContent instanceof IGroupContent )
		{
			IGroupContent group = (IGroupContent) pContent;
			groupLevel = group.getGroupLevel( );
		}
		dropDetailResolved = false;
	}

	protected boolean layoutChildren( )
	{

		if ( bandType == IBandContent.BAND_GROUP_FOOTER )
		{
			if ( !dropDetailResolved )
			{
				tbl.updateDropCells( groupLevel, false );
				dropDetailResolved = false;
			}
		}
		boolean hasNext = super.layoutChildren( );
		if ( !hasNext )
		{
			if ( bandType == IBandContent.BAND_GROUP_FOOTER )
			{
				tbl.updateDropCells( groupLevel, true );
			}
		}
		return hasNext;
	}
}
