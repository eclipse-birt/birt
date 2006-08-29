/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.series;

import java.util.List;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.LocalizedNumberEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.series.SeriesPaletteSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class SeriesXSheetImpl extends SubtaskSheetImpl
		implements
			Listener,
			ModifyListener,
			SelectionListener
{

	private transient Label lblMinSlice;
	private transient Label lblBottomPercent;
	private transient Label lblLabel;
	private transient Combo cmbMinSlice;
	private transient LocalizedNumberEditorComposite txtMinSlice;
	private transient ExternalizedTextEditorComposite txtLabel = null;

	private final static String TOOLTIP_MINIMUM_SLICE = Messages.getString( "PieBottomAreaComponent.Label.AnySliceWithASize" ); //$NON-NLS-1$

	private final static String[] MINMUM_SLICE_ITEMS = new String[]{
			Messages.getString( "PieBottomAreaComponent.Label.Percentage" ), Messages.getString( "PieBottomAreaComponent.Label.Value" ) //$NON-NLS-1$ //$NON-NLS-2$
	};

	public void createControl( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_XSERIES );
		cmpContent = new Composite( parent, SWT.NONE );
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

		if ( getChart( ) instanceof ChartWithoutAxes
				&& !( getChart( ) instanceof DialChart ) )
		{
			createPieAxisArea( cmpBasic );
		}

		createButtonGroup( cmpContent );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		ITaskPopupSheet popup = new SeriesPaletteSheet( Messages.getString( "SeriesXSheetImpl.Label.SeriesPalette" ), //$NON-NLS-1$
				getContext( ),
				getSeriesDefinitionForProcessing( ) );
		Button btnSeriesPal = createToggleButton( cmp,
				Messages.getString( "SeriesXSheetImpl.Label.SeriesPalette&" ), //$NON-NLS-1$
				popup );
		btnSeriesPal.addSelectionListener( this );
	}

	private void createPieAxisArea( Composite parent )
	{
		lblMinSlice = new Label( parent, SWT.NONE );
		{
			lblMinSlice.setText( Messages.getString( "PieBottomAreaComponent.Label.MinimumSlice" ) ); //$NON-NLS-1$
			lblMinSlice.setToolTipText( TOOLTIP_MINIMUM_SLICE );
		}

		Composite cmpMinSlice = new Composite( parent, SWT.NONE );
		{
			GridLayout gridLayout = new GridLayout( 3, false );
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			cmpMinSlice.setLayout( gridLayout );
			cmpMinSlice.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		}

		cmbMinSlice = new Combo( cmpMinSlice, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			cmbMinSlice.setToolTipText( TOOLTIP_MINIMUM_SLICE );
			cmbMinSlice.setItems( MINMUM_SLICE_ITEMS );
			cmbMinSlice.setText( ( (ChartWithoutAxes) getChart( ) ).isMinSlicePercent( )
					? MINMUM_SLICE_ITEMS[0] : MINMUM_SLICE_ITEMS[1] );
			cmbMinSlice.addSelectionListener( this );
		}

		txtMinSlice = new LocalizedNumberEditorComposite( cmpMinSlice,
				SWT.BORDER );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			txtMinSlice.setLayoutData( gridData );
			txtMinSlice.setToolTipText( TOOLTIP_MINIMUM_SLICE );
			txtMinSlice.setValue( ( (ChartWithoutAxes) getChart( ) ).getMinSlice( ) );
			txtMinSlice.addModifyListener( this );
		}

		lblBottomPercent = new Label( cmpMinSlice, SWT.NONE );
		lblBottomPercent.setText( "%" ); //$NON-NLS-1$
		lblBottomPercent.setVisible( ( (ChartWithoutAxes) getChart( ) ).isMinSlicePercent( ) );

		lblLabel = new Label( parent, SWT.NONE );
		{
			lblLabel.setText( Messages.getString( "PieBottomAreaComponent.Label.MinSliceLabel" ) ); //$NON-NLS-1$
			lblLabel.setToolTipText( TOOLTIP_MINIMUM_SLICE );
		}

		List keys = null;
		if ( getContext( ).getUIServiceProvider( ) != null )
		{
			keys = getContext( ).getUIServiceProvider( ).getRegisteredKeys( );
		}
		txtLabel = new ExternalizedTextEditorComposite( parent,
				SWT.BORDER,
				-1,
				-1,
				keys,
				getContext( ).getUIServiceProvider( ),
				( (ChartWithoutAxes) getChart( ) ).getMinSliceLabel( ) != null
						? ( (ChartWithoutAxes) getChart( ) ).getMinSliceLabel( )
						: "" ); //$NON-NLS-1$
		{
			GridData gdTXTTitle = new GridData( GridData.FILL_HORIZONTAL );
			txtLabel.setLayoutData( gdTXTTitle );
			txtLabel.setEnabled( ( (ChartWithoutAxes) getChart( ) ).getMinSlice( ) != 0 );
			txtLabel.addListener( this );
		}
	}

	private SeriesDefinition getSeriesDefinitionForProcessing( )
	{
		SeriesDefinition sd = null;
		if ( getChart( ) instanceof ChartWithAxes )
		{
			sd = ( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) getChart( ) ).getAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).get( getIndex( ) ) );
		}
		else if ( getChart( ) instanceof ChartWithoutAxes )
		{
			sd = ( (SeriesDefinition) ( (ChartWithoutAxes) getChart( ) ).getSeriesDefinitions( )
					.get( getIndex( ) ) );
		}
		return sd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.ModifyListener#modifyText(org.eclipse.swt.events.ModifyEvent)
	 */
	public void modifyText( ModifyEvent e )
	{
		if ( e.getSource( ) == txtMinSlice )
		{
			( (ChartWithoutAxes) getChart( ) ).setMinSlice( txtMinSlice.getValue( ) );
			txtLabel.setEnabled( ( (ChartWithoutAxes) getChart( ) ).getMinSlice( ) != 0 );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( getChart( ) instanceof ChartWithoutAxes )
		{
			if ( event.widget.equals( txtLabel ) )
			{
				( (ChartWithoutAxes) getChart( ) ).setMinSliceLabel( txtLabel.getText( ) );
			}
		}
	}

	public void widgetSelected( SelectionEvent e )
	{
		// Detach popup dialog if there's selected popup button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getText( ) );
		}

		if ( getChart( ) instanceof ChartWithoutAxes )
		{
			if ( e.widget.equals( cmbMinSlice ) )
			{
				( (ChartWithoutAxes) getChart( ) ).setMinSlicePercent( cmbMinSlice.getSelectionIndex( ) == 0 );
				lblBottomPercent.setVisible( ( (ChartWithoutAxes) getChart( ) ).isMinSlicePercent( ) );
			}
		}

	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

}