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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeContentProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeLabelProvider;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * LevelViewDialog
 */
public class LevelViewDialog extends TrayDialog
{

	/**Constructor
	 * @param shell
	 */
	public LevelViewDialog( Shell shell )
	{
		super( shell );
	}

	private DimensionHandle dimension;
	private List showLevels;
	private CheckboxTreeViewer levelViewer;

	/**
	 * @param dimension
	 * @param showLevels
	 */
	public void setInput( DimensionHandle dimension, List showLevels )
	{
		this.dimension = dimension;
		this.showLevels = new LinkedList( );
		this.showLevels.addAll( showLevels );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		getShell( ).setText( "Show/Hide Group Levels" );
		Composite dialogArea = (Composite) super.createDialogArea( parent );

		Label infoLabel = new Label( dialogArea, SWT.NONE );
		infoLabel.setText( "Select levels to show on Crosstab:" );

		createLevelViewer( dialogArea );

		initDialog( );

		return dialogArea;
	}

	/**
	 * 
	 */
	private void initDialog( )
	{
		if ( dimension != null )
		{
			levelViewer.setInput( dimension );
			levelViewer.expandToLevel( dimension.getDefaultHierarchy( )
					.getContentCount( IHierarchyModel.LEVELS_PROP ) );
		}
		if ( showLevels == null || showLevels.size( ) == 0 )
			return;
		TreeItem item = levelViewer.getTree( ).getItem( 0 );
		while ( item != null )
		{
			LevelHandle level = (LevelHandle) item.getData( );
			if ( showLevels.contains( level ) )
			{
				item.setChecked( true );
			}
			if ( item.getItemCount( ) > 0 )
				item = item.getItem( 0 );
			else
				item = null;
		}
	}

	/**
	 * @param parent
	 */
	private void createLevelViewer( Composite parent )
	{
		levelViewer = new CheckboxTreeViewer( parent, SWT.SINGLE | SWT.BORDER );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 200;
		gd.heightHint = 150;
		levelViewer.getTree( ).setLayoutData( gd );

		levelViewer.setContentProvider( new CubeContentProvider( ) );
		levelViewer.setLabelProvider( new CubeLabelProvider( ) );
		levelViewer.addCheckStateListener( new ICheckStateListener( ) {

			public void checkStateChanged( CheckStateChangedEvent event )
			{

				LevelHandle item = (LevelHandle) event.getElement( );
				if ( event.getChecked( ) )
				{
					if ( !showLevels.contains( item ) )
						showLevels.add( item );
				}
				else
				{
					if ( showLevels.contains( item ) )
						showLevels.remove( item );
				}
			}

		} );
	}

	/**
	 * @return
	 */
	public List getResult( )
	{
		return showLevels;
	}
}
