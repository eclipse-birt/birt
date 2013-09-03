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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeContentProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeLabelProvider;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.IHierarchyModel;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Show or hide the LevelViewHnadle for the special DimensionViewHandle
 * LevelViewDialog
 */
public class LevelViewDialog extends BaseDialog
{
	private boolean isEdit = false;
	private Button regularBtn;
	private Button dateBtn;
	private boolean isRegular = false;

	public LevelViewDialog( boolean isEdit )
	{
		this( UIUtil.getDefaultShell( ) );
		this.isEdit = isEdit;
	}
	
	public LevelViewDialog( Shell shell )
	{
		super( Messages.getString( "LevelViewDialog.Title" ) ); //$NON-NLS-1$
	}

	private DimensionHandle dimension;
	private List showLevels;
	private CheckboxTreeViewer levelViewer;

	public void setInput( DimensionHandle dimension, List showLevels )
	{
		this.dimension = dimension;
		this.showLevels = new LinkedList( );
		this.showLevels.addAll( showLevels );
		this.isRegular = !dimension.isTimeType( );
	}

	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.XTAB_LEVEL_VIEW_DIALOG );

		Composite dialogArea = (Composite) super.createDialogArea( parent );

		Label infoLabel = new Label( dialogArea, SWT.WRAP );
		infoLabel.setText( Messages.getString( "LevelViewDialog.Label.Info" ) ); //$NON-NLS-1$
		
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = 400;
		infoLabel.setLayoutData( gd );
		
		if( !isEdit && dimension.isTimeType( ) )
		{
			createButtonArea( dialogArea );
		}
		
		createLevelViewer( dialogArea );

		init( );

		return dialogArea;
	}
	
	private void createButtonArea(Composite parent)
	{
		regularBtn = new Button(parent, SWT.RADIO);
		regularBtn.setText( Messages.getString( "LevelViewDialog.Button.Regular.Text" ) );
		regularBtn.addSelectionListener( new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				isRegular = true;
				levelViewer.getTree( ).setVisible( false );
				checkOKButtonStatus( );
			}
		} );
		
		dateBtn = new Button( parent, SWT.RADIO );
		dateBtn.setText( Messages.getString( "LevelViewDialog.Button.Date.Text" ) );
		dateBtn.addSelectionListener( new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e)
			{
				isRegular = false;
				levelViewer.getTree( ).setVisible( true );
				checkOKButtonStatus( );
			}
		} );
		
		if(dimension.isTimeType( ))
		{
			dateBtn.setSelection( true );
		}
		else
		{
			regularBtn.setSelection( true );
		}
	}
	
	@Override
	protected boolean initDialog( )
	{
		checkOKButtonStatus( );
		
		return super.initDialog( );
	}

	private void init( )
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

	private void createLevelViewer( Composite parent )
	{
		levelViewer = new CheckboxTreeViewer( parent, SWT.SINGLE | SWT.BORDER );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 340;
		gd.heightHint = 250;
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

				checkOKButtonStatus( );
			}

		} );
	}

	public Object getResult( )
	{
		if( !isEdit && isRegular )
		{
			return new ArrayList();
		}
		return showLevels;
	}

	private void checkOKButtonStatus( )
	{
		if ( ( isEdit || !isRegular ) && (showLevels == null || showLevels.size( ) == 0) )
		{
			if ( getOkButton( ) != null )
				getOkButton( ).setEnabled( false );
		}
		else
		{
			if ( getOkButton( ) != null )
				getOkButton( ).setEnabled( true );
		}
	}
}
