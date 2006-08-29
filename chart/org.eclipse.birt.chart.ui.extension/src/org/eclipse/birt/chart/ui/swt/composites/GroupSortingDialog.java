/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 */

public class GroupSortingDialog extends TrayDialog implements Listener
{

	private static final String UNSORTED_OPTION = Messages.getString( "BaseSeriesDataSheetImpl.Choice.Unsorted" ); //$NON-NLS-1$

	private ChartWizardContext wizardContext;
	private SeriesDefinition sd;

	private Combo cmbSorting;

	public GroupSortingDialog( Shell shell, ChartWizardContext wizardContext,
			SeriesDefinition sd )
	{
		super( shell );
		this.wizardContext = wizardContext;
		this.sd = sd;
	}

	protected Control createDialogArea( Composite parent )
	{
		getShell( ).setText( Messages.getString( "GroupSortingDialog.Label.GroupAndSorting" ) ); //$NON-NLS-1$

		Composite cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, false );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 2, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpBasic.setLayoutData( gd );
		}

		Label lblSorting = new Label( cmpBasic, SWT.NONE );
		lblSorting.setText( Messages.getString( "BaseSeriesDataSheetImpl.Lbl.DataSorting" ) ); //$NON-NLS-1$

		cmbSorting = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBSorting = new GridData( GridData.FILL_HORIZONTAL );
		cmbSorting.setLayoutData( gdCMBSorting );
		cmbSorting.addListener( SWT.Selection, this );

		Composite cmpGrouping = new Composite( cmpBasic, SWT.NONE );
		GridData gdCMPGrouping = new GridData( GridData.FILL_HORIZONTAL );
		gdCMPGrouping.horizontalSpan = 2;
		cmpGrouping.setLayoutData( gdCMPGrouping );
		cmpGrouping.setLayout( new FillLayout( ) );

		new SeriesGroupingComposite( cmpGrouping,
				SWT.NONE,
				getSeriesDefinitionForProcessing( ),
				wizardContext.getModel( ) instanceof ChartWithoutAxes );

		populateLists( );

		return cmpContent;
	}

	private SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		return sd;
	}

	private void populateLists( )
	{
		// populate sorting combo
		cmbSorting.add( UNSORTED_OPTION );

		String[] nss = LiteralHelper.sortOptionSet.getDisplayNames( );
		for ( int i = 0; i < nss.length; i++ )
		{
			cmbSorting.add( nss[i] );
		}

		// Select value
		if ( !getSeriesDefinitionForProcessing( ).eIsSet( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) ) )
		{
			cmbSorting.select( 0 );
		}
		else
		{
			// plus one for the first is unsorted option.
			cmbSorting.select( LiteralHelper.sortOptionSet.getNameIndex( getSeriesDefinitionForProcessing( ).getSorting( )
					.getName( ) ) + 1 );
		}

	}

	public void handleEvent( Event event )
	{
		if ( event.widget == cmbSorting )
		{
			if ( cmbSorting.getText( ).equals( UNSORTED_OPTION ) )
			{
				getSeriesDefinitionForProcessing( ).eUnset( DataPackage.eINSTANCE.getSeriesDefinition_Sorting( ) );
			}
			else
			{
				getSeriesDefinitionForProcessing( ).setSorting( SortOption.getByName( LiteralHelper.sortOptionSet.getNameByDisplayName( cmbSorting.getText( ) ) ) );
			}
		}

	}

}
