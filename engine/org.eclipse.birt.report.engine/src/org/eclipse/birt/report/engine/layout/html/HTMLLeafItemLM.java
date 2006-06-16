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

import org.eclipse.birt.report.engine.layout.ILayoutManager;

public class HTMLLeafItemLM extends HTMLAbstractLM implements ILayoutManager
{

	public HTMLLeafItemLM( HTMLLayoutManagerFactory factory )
	{
		super( factory );
	}

	public int getType( )
	{
		return LAYOUT_MANAGER_LEAF;
	}

	public boolean layout( )
	{
		context.setPageEmpty( false );
		return super.layout( );
	}
	
	protected boolean layoutChildren( )
	{
		return false;
	}

	protected void processEndStatus( )
	{

	}

	protected void cancelChildren( )
	{
		return;
	}

}
