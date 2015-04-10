/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;

/**
 * Page break handler which notifies the context to fire a page break event;
 *
 */
public class ContextPageBreakHandler implements ILayoutPageHandler
{
	ExecutionContext context;
	
	public ContextPageBreakHandler( ExecutionContext context )
	{
		this.context = context;
	}

	public void onPage( long page, Object pageContext )
	{
		if ( pageContext instanceof HTMLLayoutContext )
		{
			context.firePageBreakEvent( ( (HTMLLayoutContext) pageContext )
					.isHorizontalPageBreak( ), false );
		}
		else
		{
			context.firePageBreakEvent( false, true );
		}
	}

}
