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

package org.eclipse.birt.report.engine.emitter;

import java.util.List;

import org.eclipse.birt.report.engine.content.IContent;

/**
 * receive the input and construct the DOM strcuture 
 * of the received contents.
 *
 * @version $Revision:$ $Date:$
 */
public class DOMBuilderEmitter extends ContentEmitterAdapter
{

	protected IContent root;
	protected IContent parent;

	/**
	 * the following contnet will be 
	 * add under the root content.
	 * @param root root content.
	 */
	public DOMBuilderEmitter( IContent root )
	{
		this.root = root;
		this.parent = null;
	}

	public void startContent( IContent content )
	{
		List children = root.getChildren( );
		if ( parent != null )
		{
			children = parent.getChildren( );
		}
		if ( children.contains( content ) )
		{
			children.add( content );
		}
		parent = content;
	}

	public void endContent( IContent content )
	{
		if ( parent != null )
		{
			parent = (IContent) parent.getParent( );
		}
	}
}
