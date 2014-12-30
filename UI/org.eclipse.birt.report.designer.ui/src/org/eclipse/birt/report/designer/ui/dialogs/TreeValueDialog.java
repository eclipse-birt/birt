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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseElementTreeSelectionDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 * TreeValueDialog
 */
public class TreeValueDialog extends BaseElementTreeSelectionDialog
{

	List<ListenerClass> listeners = new ArrayList<ListenerClass>( );

	private static class ListenerClass
	{

		int type;
		Listener listener;

		public ListenerClass( int type, Listener listener )
		{
			this.type = type;
			this.listener = listener;
		}
	}

	/**
	 * @param parent
	 * @param labelProvider
	 * @param contentProvider
	 */
	public TreeValueDialog( Shell parent, ILabelProvider labelProvider,
			ITreeContentProvider contentProvider )
	{
		super( parent, labelProvider, contentProvider );

		setAllowMultiple( false );
	}

	/**
	 * Creates and initializes the tree viewer.
	 * 
	 * @param parent
	 *            the parent composite
	 * @return the tree viewer
	 * @see #doCreateTreeViewer(Composite, int)
	 */
	protected TreeViewer createTreeViewer( Composite parent )
	{
		TreeViewer treeViewer = super.createTreeViewer( parent );
		Tree tree = treeViewer.getTree( );
		assert ( tree != null );
		for ( int i = 0; i < listeners.size( ); i++ )
		{
			int type = listeners.get( i ).type;
			Listener listener = listeners.get( i ).listener;
			tree.addListener( type, listener );
		}
		return treeViewer;
	}

	public void addListener( int type, Listener listner )
	{
		listeners.add( new ListenerClass( type, listner ) );
	}

	public boolean removeListener( int index )
	{
		if ( index >= 0 && index < listeners.size( ) )
		{
			listeners.remove( index );
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean removeAllListeners( )
	{
		listeners.clear( );
		return true;
	}
}
