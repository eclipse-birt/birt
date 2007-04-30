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
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

public class HTMLTableLM extends HTMLBlockStackingLM
{

	/**
	 * emitter used to layout the table
	 */
	protected HTMLTableLayoutNoNestEmitter tableEmitter;

	public HTMLTableLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_TABLE;
	}

	boolean isFirstLayout = true;

	public void initialize( HTMLAbstractLM parent, IContent content,
			IReportItemExecutor executor, IContentEmitter emitter )
	{
		tableEmitter = new HTMLTableLayoutNoNestEmitter( emitter );
		super.initialize( parent, content, executor, tableEmitter );
		isFirstLayout = true;
	}

	protected void repeatHeader( )
	{
		if ( !isFirstLayout )
		{
			ITableContent table = (ITableContent) content;
			if ( table.isHeaderRepeat( ) )
			{
				IBandContent header = table.getHeader( );
				if ( header != null )
				{
					boolean pageBreak = context.allowPageBreak( );
					//boolean skipPageHint = context.getSkipPageHint( );
					context.setAllowPageBreak( pageBreak );
					//context.setSkipPageHint( true );
					engine.layout( header, emitter );
					context.setAllowPageBreak( pageBreak );
					//context.setSkipPageHint( skipPageHint );
					context.endContentWithPageBreak( null );

				}
			}
		}
		isFirstLayout = false;
	}

	protected boolean layoutChildren( )
	{
		repeatHeader( );
		boolean hasNext = super.layoutChildren( );
		tableEmitter.resolveAll( );
		tableEmitter.flush( );
		return hasNext;
	}

	public void updateDropCells( int groupLevel, boolean dropAll )
	{
		tableEmitter.resolveCellsOfDrop( groupLevel, dropAll );
	}
}
