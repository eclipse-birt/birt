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
package org.eclipse.birt.report.data.oda.jdbc.ui.model;

import org.eclipse.birt.report.data.oda.jdbc.ui.JdbcPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;


public class DBNodeUtil
{
	private DBNodeUtil( )
	{ 	
	}
	
	public static void createRootTip( Tree tree, RootNode node )
	{
		tree.removeAll( );
		TreeItem root = new TreeItem( tree, SWT.NONE );
		root.setText( node.getDisplayName( ) );
		root.setImage( node.getImage( ) );
		root.setData( node );
	}
	
	public static void createTreeRoot( Tree tree, RootNode node, FilterConfig fc )
	{
		tree.removeAll( );
		TreeItem dummyItem = new TreeItem( tree, SWT.NONE );
		dummyItem.setText( JdbcPlugin.getResourceString( "tablepage.refreshing" ) );
		
		if ( ! node.isChildrenPrepared( ) )
		{
			node.prepareChildren( fc );
		}
		tree.removeAll( );
		TreeItem root = new TreeItem( tree, SWT.NONE );
		root.setText( node.getDisplayName( ) );
		root.setImage( node.getImage( ) );
		root.setData( node );
		IDBNode[] children = node.getChildren( );
		if ( children != null )
		{
			for ( IDBNode child : children )
			{
				createTreeItem( root, child );
			}
		}
		root.setExpanded( true );
	}
	
	public static TreeItem createTreeItem( TreeItem parent, IDBNode node )
	{
		TreeItem item = new TreeItem( parent, SWT.NONE );
		item.setText(  node.getDisplayName( ) );
		item.setImage( node.getImage( ) );
		item.setData( node );
		item.setExpanded( false );
		if ( node instanceof ChildrenAllowedNode )
		{
			if ( ( (ChildrenAllowedNode) node ).isChildrenPrepared( ) )
			{
				//add all prepared children
				IDBNode[] children = ( (ChildrenAllowedNode) node ).getChildren( );
				if ( children != null )
				{
					for ( IDBNode child : children )
					{
						createTreeItem( item, child );
					}
				}
			}
			else
			{
				//create a dummy child to flag that this tree node may have children waiting to be explored
				new TreeItem(item, SWT.NONE);
			}
		}
		return item;
	}
}
