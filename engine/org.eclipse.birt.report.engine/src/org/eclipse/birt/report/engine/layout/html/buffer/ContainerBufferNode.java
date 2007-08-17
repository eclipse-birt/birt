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

import java.util.LinkedList;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class ContainerBufferNode extends AbstractNode implements IContainerNode
{

	protected LinkedList children = new LinkedList( );

	public ContainerBufferNode( IContent content, IContentEmitter emitter,
			PageHintGenerator generator )
	{
		super( content, emitter, generator );
	}
	
	

	protected void flushChildren( )
	{
		if ( children.size( )>0 )
		{
			for ( int i = 0; i < children.size( ); i++ )
			{
				INode child = (INode) children.get( i );
				child.flush( );
			}
		}
	}
	
	public void flush( )
	{
		if ( !isStarted )
		{
			start( );
		}
		flushChildren( );
		end( );

	}
	
	protected void flushUnStartedChildren( )
	{
		if ( children.size( )>0 )
		{
			while(children.size( )>1)
			{
				INode child = (INode) children.removeFirst( );
				child.flush( );
			}
		}
	}

	public void start( )
	{
		if(isStarted)
		{
			return;
		}
		if ( parent != null && !parent.isStarted( ) )
		{
			parent.start( );
		}
		ContentEmitterUtil.startContent( content, emitter );
		generator.start( content, isFirst );
		isStarted = true;
		flushUnStartedChildren( );
	}

	public void addChild( INode node )
	{
		children.addLast( node );
	}

	public void removeChildren( )
	{
		children.clear( );
	}

}
