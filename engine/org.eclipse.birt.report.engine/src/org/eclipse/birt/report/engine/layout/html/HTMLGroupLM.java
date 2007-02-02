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
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;

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

	public void initialize( HTMLAbstractLM parent, IContent content,
			IReportItemExecutor executor, IContentEmitter emitter )
	{
		super.initialize( parent, content, executor, emitter );
		isFirstLayout = true;
	}

	protected void repeatHeader( )
	{
		if ( !isFirstLayout )
		{
			IGroupContent group = (IGroupContent) content;
			IBandContent header = group.getHeader( );
			if ( group.isHeaderRepeat( ) && header != null )
			{
				boolean pageBreak = context.allowPageBreak( );
				boolean skipPageHint = context.getSkipPageHint( );
				context.setAllowPageBreak( false );
				context.setSkipPageHint( true );
				engine.layout( this, header, emitter );
				context.setAllowPageBreak( pageBreak );
				context.setSkipPageHint( skipPageHint );
			}
		}
		isFirstLayout = false;
	}

	protected boolean layoutChildren( )
	{
		repeatHeader( );
		boolean hasNext = super.layoutChildren( );
		return hasNext;
	}

}
