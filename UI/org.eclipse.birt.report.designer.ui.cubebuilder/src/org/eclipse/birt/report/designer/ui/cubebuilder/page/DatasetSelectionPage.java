/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.page;

import org.eclipse.birt.report.designer.data.ui.property.AbstractDescriptionPropertyPage;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.FilterListDialog;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DatasetSelectionPage extends AbstractDescriptionPropertyPage
{

	private CubeHandle input;
	private Combo dataSetCombo;
	private Text nameText;
	private CubeBuilder builder;
	private Button filterButton;

	public DatasetSelectionPage( CubeBuilder builder, CubeHandle model )
	{
		input = model;
		this.builder = builder;
	}

	public Control createContents( Composite parent )
	{
		UIUtil.bindHelp( parent,
				IHelpContextIds.CUBE_BUILDER_DATASET_SELECTION_PAGE );

		Composite container = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		layout.marginRight = 20;
		container.setLayout( layout );

		Label nameLabel = new Label( container, SWT.NONE );
		nameLabel.setText( Messages.getString( "DatasetPage.Label.Name" ) ); //$NON-NLS-1$
		nameText = new Text( container, SWT.BORDER );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				try
				{
					input.setName( nameText.getText( ) );
					builder.setErrorMessage( null );
					builder.setTitleMessage( Messages.getString( "DatasetPage.Title.Message" ) ); //$NON-NLS-1$
				}
				catch ( NameException e1 )
				{
					builder.setErrorMessage( e1.getMessage( ) );
				}

			}

		} );

		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 2;
		nameText.setLayoutData( data );

		Label dateSetLabel = new Label( container, SWT.NONE );
		dateSetLabel.setText( Messages.getString( "DatasetPage.Label.PrimaryDataset" ) ); //$NON-NLS-1$
		dataSetCombo = new Combo( container, SWT.BORDER | SWT.READ_ONLY );
		dataSetCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		dataSetCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( dataSetCombo.getItemCount( ) == 0 )
					return;
				String datasetName = dataSetCombo.getItem( dataSetCombo.getSelectionIndex( ) );
				try
				{
					( (TabularCubeHandle) input ).setDataSet( OlapUtil.getDataset( datasetName ) );
				}
				catch ( SemanticException e1 )
				{
					ExceptionHandler.handle( e1 );
				}
				if ( dataSetCombo.getSelectionIndex( ) == -1 )
				{
					builder.setOKEnable( false );
					filterButton.setEnabled( false );
				}
				else
				{
					builder.setOKEnable( true );
					filterButton.setEnabled( true );
				}
			}

		} );

		filterButton = new Button( container, SWT.PUSH );
		filterButton.setText( Messages.getString( "DatasetPage.Button.Filter" ) ); //$NON-NLS-1$
		data = new GridData( );
		data.widthHint = Math.max( 60, filterButton.computeSize( SWT.DEFAULT,
				SWT.DEFAULT ).x );
		filterButton.setLayoutData( data );
		filterButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				FilterListDialog dialog = new FilterListDialog( );
				dialog.setInput( input );
				dialog.open( );
			}

		} );

		filterButton.setEnabled( false );
		return container;
	}

	public void pageActivated( )
	{
		getContainer( ).setMessage( Messages.getString( "DatasetPage.Container.Title.Message" ),//$NON-NLS-1$
				IMessageProvider.NONE );
		builder.setTitleTitle( Messages.getString( "DatasetPage.Title.Title" ) ); //$NON-NLS-1$
		builder.setErrorMessage( null );
		builder.setTitleMessage( Messages.getString( "DatasetPage.Title.Message" ) ); //$NON-NLS-1$
		load( );
	}

	private void refresh( )
	{
		// dataSetCombo.setItems( input.getAvailableDatasetsName( ) );
		// dataSetCombo.select( input.getIndexOfPrimaryDataset( ) );
		if ( dataSetCombo != null && !dataSetCombo.isDisposed( ) )
		{
			dataSetCombo.setItems( OlapUtil.getAvailableDatasetNames( ) );
			if ( ( (TabularCubeHandle) input ).getDataSet( ) != null )
			{
				String datasetName = ( (TabularCubeHandle) input ).getDataSet( )
						.getQualifiedName( );
				if ( dataSetCombo.indexOf( datasetName ) == -1 )
				{
					dataSetCombo.add( datasetName, 0 );
				}
				dataSetCombo.setText( datasetName );
			}
			if ( dataSetCombo.getSelectionIndex( ) == -1 )
			{
				builder.setOKEnable( false );
				filterButton.setEnabled( false );
			}
			else
			{
				builder.setOKEnable( true );
				filterButton.setEnabled( true );
			}
		}
	}

	private void load( )
	{
		if ( input != null )
		{
			if ( input.getName( ) != null )
				nameText.setText( input.getName( ) );
			refresh( );
		}
	}

}
