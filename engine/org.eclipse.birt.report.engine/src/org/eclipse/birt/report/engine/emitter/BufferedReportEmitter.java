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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;

/**
 * a buffered report emitter that allows content objects from the engine to be
 * buffered before output to a specific format. Buffering is needed sometimes,
 * for handling drop, table, etc.
 * 
 * @version $Revision: 1.1 $ $Date: 2005/11/11 06:26:42 $
 */
public class BufferedReportEmitter extends ContentEmitterAdapter
{

	/**
	 * refers to the non-buffered emitter
	 */
	protected IContentEmitter emitter;

	/**
	 * conent stack
	 */
	protected Stack stack = new Stack( );

	protected ArrayList contents = new ArrayList( );

	/**
	 * constructor
	 * 
	 * @param emitter
	 */
	public BufferedReportEmitter( IContentEmitter emitter )
	{
		this.emitter = emitter;
	}

	public void initialize( IEmitterServices services )
	{
	}

	/**
	 * output content in one node
	 * 
	 * @param node
	 *            the buffered node
	 * @param start
	 *            the start visitor
	 * @param end
	 *            the end visitor
	 */
	protected void emitNode( BufferedNode node, IContentEmitter emitter )
	{
		IContent content = node.getContent( );
		ContentEmitterUtil.startContent( content, emitter );
		ArrayList children = node.getChildren( );
		if ( children != null )
		{
			for ( int i = 0; i < children.size( ); i++ )
			{
				emitNode( (BufferedNode) children.get( i ), emitter );
			}
		}
		ContentEmitterUtil.endContent( content, emitter );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getOutputFormat()
	 */
	public String getOutputFormat( )
	{
		return emitter.getOutputFormat( );
	}

	public boolean isEmpty( )
	{
		return stack.isEmpty( );
	}

	public void flush( )
	{
		Iterator nodeIter = contents.iterator( );
		while ( nodeIter.hasNext( ) )
		{
			BufferedNode node = (BufferedNode) nodeIter.next( );
			emitNode( node, emitter);
		}
	}

	/**
	 * buffer a node. The node is added as a child to the top node on stack; it
	 * is then added to the stack so that it becomes the top of the stack
	 * 
	 * @param obj
	 *            The buffered object
	 */
	protected void push( IContent obj )
	{

		BufferedNode content = new BufferedNode( obj );
		if ( !stack.isEmpty( ) )
		{
			( (BufferedNode) stack.peek( ) ).addChild( content );
		}
		else
		{
			contents.add(content);
		}
		stack.push( content );
	}

	/**
	 * remove a buffered node. The popped node mays till be referenced by its
	 * parent
	 */
	protected void pop( )
	{
		assert !stack.empty( );
		stack.pop( );
	}

	public void start( IReportContent report )
	{
	}

	public void end( IReportContent report )
	{
	}

	public void startContent( IContent content )
	{
		push( content );
	}

	public void endContent( IContent content )
	{
		pop( );
	}

	public class BufferedNode
	{

		/**
		 * The content object stored in this node
		 */
		protected IContent content;

		/**
		 * the children of this node
		 */
		protected ArrayList children;

		/**
		 * @param item
		 *            the content object
		 */
		BufferedNode( IContent content )
		{
			this.content = content;
		}

		/**
		 * @param child
		 *            another object of the type <code>BufferedNode</code>
		 */
		public void addChild( BufferedNode child )
		{
			assert ( child != null );
			if ( children == null )
			{
				children = new ArrayList( );
			}
			children.add( child );
		}

		/**
		 * @return Returns the content object stored in this node
		 */
		public IContent getContent( )
		{
			return content;
		}

		public ArrayList getChildren( )
		{
			return children;
		}

	}
}