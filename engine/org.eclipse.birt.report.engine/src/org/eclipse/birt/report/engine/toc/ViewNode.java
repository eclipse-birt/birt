/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.toc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

public class ViewNode extends TOCNode
{

	static final List<ViewNode> EMPTY_CHILDREN = Collections
			.unmodifiableList( new ArrayList<ViewNode>( 0 ) );

	TOCView view;
	ITreeNode node;
	int level;

	ViewNode( TOCView view, ViewNode parent, ITreeNode node )
	{
		this.view = view;

		this.node = node;
		this.nodeId = node.getNodeId( );
		this.bookmark = node.getBookmark( );

		this.parent = parent;
		//setup the fields if the node is not the root.
		if ( parent != null )
		{
			this.level = parent.level + 1;
			this.tocStyle = view.getTOCStyle( level - 1, node.getElementId( ) );
			Object value = node.getTOCValue( );
			if ( value != null )
			{
				this.displayString = view.localizeValue( value, tocStyle );
			}
		}
	}

	public int getLevel( )
	{
		return level;
	}

	public void setLevel( int level )
	{
		this.level = level;
	}

	public List getChildren( )
	{
		if ( children == null )
		{
			children = createViewChildren( );
		}
		return children;
	}

	public Object getTOCValue( )
	{
		return node.getTOCValue( );
	}

	private ArrayList<ViewNode> createViewChildren( )
	{
		// create the children for this node
		ArrayList<ViewNode> children = new ArrayList<ViewNode>( );
		for ( ITreeNode treeNode : node.getChildren( ) )
		{
			addViewNode( this, children, treeNode );
		}
		return children;
	}

	private void addViewChildren( ViewNode node, ArrayList<ViewNode> children,
			ITreeNode tree )
	{
		for ( ITreeNode treeNode : tree.getChildren( ) )
		{
			addViewNode( node, children, treeNode );
		}
	}

	private void addViewNode( ViewNode node, ArrayList<ViewNode> children,
			ITreeNode treeNode )
	{
		if ( isHidden( treeNode ) )
		{
			return;
		}

		if ( !isVisible( treeNode ) )
		{
			addViewChildren( node, children, treeNode );
			return;
		}

		if ( treeNode.isGroup( ) )
		{
			ViewNode group = createGroupNode( node, treeNode );
			if ( group != null )
			{
				children.add( group );
			}
			return;
		}

		if ( treeNode.getTOCValue( ) != null )
		{
			ViewNode child = new ViewNode( view, node, treeNode );
			children.add( child );
			return;
		}

		addViewChildren( node, children, treeNode );
	}

	protected ViewNode createGroupNode( ViewNode parent, ITreeNode treeNode )
	{
		ITreeNode labelNode = getFirstNoneNode( treeNode );
		if ( labelNode != null )
		{
			ViewNode groupNode = new ViewNode( view, parent, treeNode );
			Object groupValue = labelNode.getTOCValue( );
			IScriptStyle groupStyle = groupNode.getTOCStyle( );
			String groupLabel = view.localizeValue( groupValue, groupStyle );
			groupNode.displayString = groupLabel;
			return groupNode;
		}
		return null;
	}

	private ITreeNode getFirstNoneNode( ITreeNode treeNode )
	{
		if ( isHidden( treeNode ) )
		{
			return null;
		}
		if ( isVisible( treeNode ) )
		{
			if ( treeNode.getTOCValue( ) != null )
			{
				return treeNode;
			}
		}
		for ( ITreeNode childNode : treeNode.getChildren( ) )
		{
			ITreeNode firstNode = getFirstNoneNode( childNode );
			if ( firstNode != null )
			{
				return firstNode;
			}
		}
		return null;
	}

	protected boolean isHidden( ITreeNode node )
	{
		return view.isHidden( node );
	}

	protected boolean isVisible( ITreeNode node )
	{
		return view.isVisible( node );
	}
}
