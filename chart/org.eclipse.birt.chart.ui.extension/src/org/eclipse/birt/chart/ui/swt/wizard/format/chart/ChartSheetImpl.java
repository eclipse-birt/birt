/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import java.util.List;

import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.TriggerEditorDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.BlockPropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.CustomPropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.MoreOptionsChartSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.TitlePropertiesSheet;
import org.eclipse.birt.chart.ui.swt.wizard.internal.ChartPreviewPainter;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartSheetImpl extends SubtaskSheetImpl implements
		SelectionListener,
		Listener
{

	private transient ExternalizedTextEditorComposite txtTitle = null;

	private transient FillChooserComposite cmbBackground;

	private transient Button btnTitleProp;

	private transient Button btnVisible;

	private transient Button btnEnable;

	private transient Combo cmbStyle;

	private transient Combo cmbInteractivity;

	private transient Button btnEnablePreview;

	private transient Button btnTitleTriggers;

	private transient Button btnChartAreaTriggers;

	private transient Label lblLegendBehavior;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		init( );

		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 3, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpBasic.setLayoutData( gd );
		}

		Label lblTitle = new Label( cmpBasic, SWT.NONE );
		{
			lblTitle.setText( Messages.getString( "ChartSheetImpl.Label.ChartTitle" ) ); //$NON-NLS-1$
		}

		List keys = null;
		if ( getContext( ).getUIServiceProvider( ) != null )
		{
			keys = getContext( ).getUIServiceProvider( ).getRegisteredKeys( );
		}
		txtTitle = new ExternalizedTextEditorComposite( cmpBasic,
				SWT.BORDER,
				-1,
				-1,
				keys,
				getContext( ).getUIServiceProvider( ),
				getChart( ).getTitle( ).getLabel( ).getCaption( ).getValue( ) );
		{
			GridData gdTXTTitle = new GridData( GridData.FILL_HORIZONTAL );
			txtTitle.setLayoutData( gdTXTTitle );
			if ( !getChart( ).getTitle( ).isVisible( ) )
				txtTitle.setEnabled( false );
			txtTitle.addListener( this );
		}

		btnVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnVisible.setText( Messages.getString( "ChartSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnVisible.setSelection( getChart( ).getTitle( ).isVisible( ) );
			btnVisible.addSelectionListener( this );
		}

		Label lblBackground = new Label( cmpBasic, SWT.NONE );
		lblBackground.setText( Messages.getString( "ChartSheetImpl.Label.Background" ) ); //$NON-NLS-1$

		cmbBackground = new FillChooserComposite( cmpBasic,
				SWT.NONE,
				getContext( ),
				getChart( ).getBlock( ).getBackground( ),
				true,
				true );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			cmbBackground.setLayoutData( gridData );
			cmbBackground.addListener( this );
		}

		new Label( cmpBasic, SWT.NONE );

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartSheetImpl.Label.Style" ) ); //$NON-NLS-1$

		cmbStyle = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			cmbStyle.setLayoutData( gridData );
			cmbStyle.addSelectionListener( this );
		}

		btnEnablePreview = new Button( cmpBasic, SWT.CHECK );
		{
			btnEnablePreview.setText( Messages.getString( "ChartSheetImpl.Label.EnableInPreview" ) ); //$NON-NLS-1$
			btnEnablePreview.setSelection( ChartPreviewPainter.isProcessorEnabled( ) );
			btnEnablePreview.addSelectionListener( this );
		}

		Group cmpInteractivity = new Group( cmpBasic, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 2, false );
			cmpInteractivity.setLayout( gl );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmpInteractivity.setLayoutData( gd );
			cmpInteractivity.setText( Messages.getString( "ChartSheetImpl.Label.Interactivity" ) ); //$NON-NLS-1$
		}

		btnEnable = new Button( cmpInteractivity, SWT.CHECK );
		{
			GridData gridData = new GridData( );
			gridData.horizontalSpan = 2;
			btnEnable.setLayoutData( gridData );
			btnEnable.setText( Messages.getString( "ChartSheetImpl.Label.InteractivityEnable" ) ); //$NON-NLS-1$
			btnEnable.setSelection( getChart( ).getInteractivity( ).isEnable( ) );
			btnEnable.addSelectionListener( this );
		}

		lblLegendBehavior = new Label( cmpInteractivity, SWT.NONE );
		{
			lblLegendBehavior.setText( Messages.getString( "ChartSheetImpl.Label.LegendBehaviorType" ) ); //$NON-NLS-1$
		}

		cmbInteractivity = new Combo( cmpInteractivity, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			cmbInteractivity.setLayoutData( gridData );
			cmbInteractivity.addSelectionListener( this );
		}

		btnTitleTriggers = new Button( cmpInteractivity, SWT.PUSH );
		{
			btnTitleTriggers.setText( Messages.getString( "ChartSheetImpl.Text.TitleInteractivity" ) ); //$NON-NLS-1$
			btnTitleTriggers.addSelectionListener( this );
		}

		btnChartAreaTriggers = new Button( cmpInteractivity, SWT.PUSH );
		{
			btnChartAreaTriggers.setText( Messages.getString( "ChartSheetImpl.Text.ChartAreaInteractivity" ) ); //$NON-NLS-1$
			btnChartAreaTriggers.addSelectionListener( this );
		}

		enableInteractivity( btnEnable.getSelection( ) );

		populateLists( );

		createButtonGroup( cmpContent );
	}

	private void init( )
	{
		// Make it compatible with old model
		if ( getChart( ).getInteractivity( ) == null )
		{
			Interactivity interactivity = InteractivityImpl.create( );
			interactivity.eAdapters( ).addAll( getChart( ).eAdapters( ) );
			getChart( ).setInteractivity( interactivity );
		}

	}

	private void populateLists( )
	{
		// POPULATE STYLE COMBO WITH AVAILABLE REPORT STYLES
		IDataServiceProvider idsp = getContext( ).getDataServiceProvider( );
		if ( idsp != null )
		{
			String[] allStyleNames = idsp.getAllStyles( );
			String[] displayNames = idsp.getAllStyleDisplayNames( );

			// Add None option to remove style
			String[] selection = new String[displayNames.length + 1];
			System.arraycopy( displayNames,
					0,
					selection,
					1,
					displayNames.length );
			selection[0] = Messages.getString( "ChartSheetImpl.Label.None" ); //$NON-NLS-1$
			cmbStyle.setItems( selection );
			cmbStyle.setData( allStyleNames );

			String sStyle = idsp.getCurrentStyle( );
			int idx = getStyleIndex( sStyle );
			cmbStyle.select( idx + 1 );

			NameSet nameSet = LiteralHelper.legendBehaviorTypeSet;
			cmbInteractivity.setItems( nameSet.getDisplayNames( ) );
			cmbInteractivity.select( nameSet.getSafeNameIndex( getChart( ).getInteractivity( )
					.getLegendBehavior( )
					.getName( ) ) );
		}
	}

	private int getStyleIndex( String style )
	{
		String[] allStyleNames = (String[]) cmbStyle.getData( );

		if ( style != null && allStyleNames != null )
		{
			for ( int i = 0; i < allStyleNames.length; i++ )
			{
				if ( style.equals( allStyleNames[i] ) )
				{
					return i;
				}
			}
		}

		return -1;
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 4, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		ITaskPopupSheet popup;
		popup = new TitlePropertiesSheet( Messages.getString( "ChartSheetImpl.Text.TitleFormat" ), //$NON-NLS-1$
				getContext( ) );
		btnTitleProp = createToggleButton( cmp,
				Messages.getString( "ChartSheetImpl.Text.TitleFormat&" ), //$NON-NLS-1$
				popup );
		btnTitleProp.addSelectionListener( this );
		btnTitleProp.setEnabled( getChart( ).getTitle( ).isVisible( ) );

		popup = new BlockPropertiesSheet( Messages.getString( "ChartSheetImpl.Text.Outline" ), //$NON-NLS-1$
				getContext( ) );
		Button btnBlockProp = createToggleButton( cmp,
				Messages.getString( "ChartSheetImpl.Text.Outline&" ), //$NON-NLS-1$
				popup );
		btnBlockProp.addSelectionListener( this );

		popup = new MoreOptionsChartSheet( Messages.getString( "ChartSheetImpl.Text.GeneralProperties" ), //$NON-NLS-1$
				getContext( ) );
		Button btnGeneralProp = createToggleButton( cmp,
				Messages.getString( "ChartSheetImpl.Text.GeneralProperties&" ), //$NON-NLS-1$
				popup );
		btnGeneralProp.addSelectionListener( this );

		popup = new CustomPropertiesSheet( Messages.getString( "ChartSheetImpl.Text.CustomProperties" ), //$NON-NLS-1$
				getContext( ) );
		Button btnCustomProp = createToggleButton( cmp,
				Messages.getString( "ChartSheetImpl.Text.CustomProperties&" ), //$NON-NLS-1$
				popup );
		btnCustomProp.addSelectionListener( this );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( txtTitle ) )
		{
			getChart( ).getTitle( )
					.getLabel( )
					.getCaption( )
					.setValue( txtTitle.getText( ) );
		}
		else if ( event.widget.equals( cmbBackground ) )
		{
			getChart( ).getBlock( ).setBackground( (Fill) event.data );
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

		if ( e.widget.equals( btnVisible ) )
		{
			getChart( ).getTitle( ).setVisible( btnVisible.getSelection( ) );
			txtTitle.setEnabled( btnVisible.getSelection( ) );
			btnTitleProp.setEnabled( btnVisible.getSelection( ) );

			if ( btnTitleProp.getSelection( ) )
			{
				detachPopup( );
			}
		}
		else if ( e.widget.equals( cmbStyle ) )
		{
			String[] allStyleNames = (String[]) cmbStyle.getData( );
			String sStyle = null;
			int idx = cmbStyle.getSelectionIndex( );
			if ( idx > 0 )
			{
				sStyle = allStyleNames[idx - 1];
			}
			getContext( ).getDataServiceProvider( ).setStyle( sStyle );
			refreshPreview( );
		}
		else if ( e.widget.equals( btnEnablePreview ) )
		{
			ChartPreviewPainter.enableProcessor( btnEnablePreview.getSelection( ) );
			refreshPreview( );
		}
		else if ( e.widget.equals( btnEnable ) )
		{
			getChart( ).getInteractivity( )
					.setEnable( btnEnable.getSelection( ) );
			enableInteractivity( btnEnable.getSelection( ) );
		}
		else if ( e.widget.equals( cmbInteractivity ) )
		{
			getChart( ).getInteractivity( )
					.setLegendBehavior( LegendBehaviorType.getByName( LiteralHelper.legendBehaviorTypeSet.getNameByDisplayName( cmbInteractivity.getText( ) ) ) );
		}
		else if ( e.widget.equals( btnTitleTriggers ) )
		{
			new TriggerEditorDialog( cmpContent.getShell( ),
					getChart( ).getTitle( ).getTriggers( ),
					getContext( ),
					Messages.getString( "ChartSheetImpl.Title.ChartTitle" ), false, true ); //$NON-NLS-1$
		}
		else if ( e.widget.equals( btnChartAreaTriggers ) )
		{
			new TriggerEditorDialog( cmpContent.getShell( ),
					getChart( ).getBlock( ).getTriggers( ),
					getContext( ),
					Messages.getString( "ChartSheetImpl.Title.ChartArea" ), false, true ); //$NON-NLS-1$
		}

	}

	private void enableInteractivity( boolean isEnabled )
	{
		lblLegendBehavior.setEnabled( isEnabled );
		cmbInteractivity.setEnabled( isEnabled );
		btnChartAreaTriggers.setEnabled( isEnabled );
		btnTitleTriggers.setEnabled( isEnabled );
	}

	/**
	 * Refreshes the preview by model modification. Used by non-model change.
	 * 
	 */
	private void refreshPreview( )
	{
		// Populate a model changed event to refresh the preview canvas.
		boolean currentValue = btnVisible.getSelection( );
		ChartAdapter.ignoreNotifications( true );
		getChart( ).getTitle( ).setVisible( !currentValue );
		ChartAdapter.ignoreNotifications( false );
		getChart( ).getTitle( ).setVisible( currentValue );
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}
}