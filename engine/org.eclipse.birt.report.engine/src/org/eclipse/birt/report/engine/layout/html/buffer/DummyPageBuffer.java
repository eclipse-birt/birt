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
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class DummyPageBuffer implements IPageBuffer
{

	public void endContainer( IContent content, boolean finished,
			IContentEmitter emitter )
	{
		if ( emitter != null )
		{
			ContentEmitterUtil.endContent( content, emitter );
		}

	}

	public void startContainer( IContent content, boolean isFirst,
			IContentEmitter emitter )
	{
		if ( emitter != null )
		{
			ContentEmitterUtil.startContent( content, emitter );
		}

	}

	public void startContent( IContent content, IContentEmitter emitter )
	{
		if ( emitter != null )
		{
			ContentEmitterUtil.startContent( content, emitter );
			ContentEmitterUtil.endContent( content, emitter );
		}

	}

	public boolean isRepeated( )
	{
		// TODO Auto-generated method stub
		return false;
	}

	public void setRepeated( boolean isRepeated )
	{
		// TODO Auto-generated method stub
		
	}

}
