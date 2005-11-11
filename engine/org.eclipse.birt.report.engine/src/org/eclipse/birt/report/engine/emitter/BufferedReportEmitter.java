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

import org.eclipse.birt.report.engine.content.ContentVisitorAdapter;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * a buffered report emitter that allows content objects from the engine to be
 * buffered before output to a specific format. Buffering is needed sometimes,
 * for handling drop, table, etc.
 * 
 * @version $Revision: 1.3 $ $Date: 2005/11/10 08:55:18 $
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
	protected void emitNode( BufferedNode node, IContentVisitor start,
			IContentVisitor end )
	{
		node.getContent( ).accept( start , null);
		ArrayList children = node.getChildren( );
		if ( children != null )
		{
			for ( int i = 0; i < children.size( ); i++ )
			{
				emitNode( (BufferedNode) children.get( i ), start, end );
			}
		}
		node.getContent( ).accept( end , null);
	}

	/**
	 * @version $Revision: 1.3 $ $Date: 2005/11/10 08:55:18 $
	 */
	protected class BufferedEndVisitor extends ContentVisitorAdapter
	{

		/**
		 * the composite report emitter
		 */
		private IContentEmitter emitter;

		/**
		 * constructor
		 * 
		 * @param emitter
		 */
		public BufferedEndVisitor( IContentEmitter emitter )
		{
			this.emitter = emitter;
		}

		public void visitPage( IPageContent page, Object value )
		{
			this.emitter.endPage( page );
		}

		public void visitContainer( IContainerContent container ,Object value)
		{
			this.emitter.endContainer( container );
		}

		public void visitTable( ITableContent table ,Object value)
		{
			this.emitter.endTable( table );
		}

		public void visitRow( IRowContent row ,Object value)
		{
			this.emitter.endRow( row );
		}

		public void visitCell( ICellContent cell,Object value )
		{
			this.emitter.endCell( cell );
		}

		public void visitTableBand( ITableBandContent tableBand ,Object value)
		{
			switch ( tableBand.getType( ) )
			{
				case ITableBandContent.BAND_HEADER :
					emitter.endTableHeader( tableBand );
					break;
				case ITableBandContent.BAND_FOOTER :
					emitter.endTableFooter( tableBand );
					break;
				case ITableBandContent.BAND_BODY :
					emitter.endTableBody( tableBand );
					break;
			}
		}
	}

	/**
	 * @version $Revision: 1.3 $ $Date: 2005/11/10 08:55:18 $
	 */
	protected class BufferedStartVisitor extends ContentVisitorAdapter
	{

		private IContentEmitter emitter;

		/**
		 * constructor
		 * 
		 * @param emitter
		 */
		public BufferedStartVisitor( IContentEmitter emitter )
		{
			this.emitter = emitter;
		}

		public void visitPage( IPageContent page ,Object value)
		{
			emitter.startPage( page );
		}

		public void visitContainer( IContainerContent container ,Object value)
		{
			emitter.startContainer( container );
		}

		public void visitTable( ITableContent table ,Object value)
		{
			emitter.startTable( table );
		}

		public void visitTableBand( ITableBandContent tableBand ,Object value)
		{
			switch ( tableBand.getType( ) )
			{
				case ITableBandContent.BAND_HEADER :
					emitter.startTableHeader( tableBand );
					break;
				case ITableBandContent.BAND_FOOTER :
					emitter.startTableFooter( tableBand );
					break;
				case ITableBandContent.BAND_BODY :
					emitter.startTableBody( tableBand );
					break;
			}
		}

		public void visitRow( IRowContent row ,Object value)
		{
			emitter.startRow( row );
		}

		public void visitCell( ICellContent cell ,Object value)
		{
			emitter.startCell( cell );
		}

		public void visitText( ITextContent text ,Object value)
		{
			emitter.startText( text );
		}

		public void visitImage( IImageContent image ,Object value)
		{
			emitter.startImage( image );
		}

		public void visitForeign( IForeignContent content ,Object value)
		{
			emitter.startForeign( content );
		}
		
		public void visitLabel(ILabelContent label, Object value)
		{
			emitter.startLabel(label);
		}
		
		public void visitData(IDataContent data, Object value)
		{
			emitter.startData(data);
		}
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
		IContentVisitor start = new BufferedStartVisitor( emitter );
		IContentVisitor end = new BufferedEndVisitor( emitter );

		Iterator nodeIter = contents.iterator( );
		while ( nodeIter.hasNext( ) )
		{
			BufferedNode node = (BufferedNode) nodeIter.next( );
			emitNode( node, start, end );
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