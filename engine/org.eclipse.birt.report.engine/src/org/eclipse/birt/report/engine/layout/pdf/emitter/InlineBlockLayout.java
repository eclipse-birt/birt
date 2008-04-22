/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;


public class InlineBlockLayout extends ContainerLayout
{

	public InlineBlockLayout( LayoutEngineContext context,ContainerLayout parentContext,
			IContent content )
	{
		super( context, parentContext, content );
	}

	public boolean addArea( AbstractArea area )
	{
		// TODO Auto-generated method stub
		return false;
	}

	protected void closeLayout( )
	{
		// TODO Auto-generated method stub
		
	}

	protected void createRoot( )
	{
		// TODO Auto-generated method stub
		
	}

	protected void initialize( )
	{
		// TODO Auto-generated method stub
		
	}

}
