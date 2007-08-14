/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.html.buffer;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;


public class LeafBufferNode extends AbstractNode implements INode
{

	LeafBufferNode( IContent content, IContentEmitter emitter,
			PageHintGenerator generator )
	{
		super( content, emitter, generator );
	}

	protected void flushChildren( )
	{
		
	}

	public void flush( )
	{
		if ( !isStarted )
		{
			start( );
		}
		end( );
	}

}
