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


public class HTMLRowLM extends HTMLInlineStackingLM
{
	public HTMLRowLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_ROW;
	}

	public boolean layout( )
	{
		boolean hasNext =  super.layout( );
		if ( !context.getSkipPageHint( ) && !hasNext )
		{
			context.setPageEmpty( false );
		}
		return hasNext;
	}
	
	protected boolean handleVisibility( )
	{
		boolean ret = super.handleVisibility( );
		// tbl.skipHiddenRow();
		return ret;
	}
}
