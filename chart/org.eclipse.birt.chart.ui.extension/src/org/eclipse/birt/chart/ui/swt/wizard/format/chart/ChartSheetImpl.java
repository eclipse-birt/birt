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
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.BlockPropertiesSheet;
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

	private transient Composite cmpContent = null;

	private transient ExternalizedTextEditorComposite txtTitle = null;

	private transient FillChooserComposite cmbBackground;

	private transient Button btnTitleProp;

	private transient Button btnBlockProp;

	private transient Button btnGeneralProp;
	
	private transient Button btnTVisible;

	private transient Button btnVisible;

	private transient Button btnEnable;

	private transient Combo cmbColorBy;

	private transient Combo cmbStyle;

	private transient Combo cmbInteractivity;

	private transient Button btnEnablePreview;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 3, true );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 3, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmpBasic.setLayoutData( gd );
		}

		Label lblTitle = new Label( cmpBasic, SWT.NONE );
		{
			lblTitle.setText( Messages.getString( "ChartSheetImpl.Label.ChartTitle" ) ); //$NON-NLS-1$
		}

		List keys = null;
		if ( serviceprovider != null )
		{
			keys = serviceprovider.getRegisteredKeys( );
		}
		txtTitle = new ExternalizedTextEditorComposite( cmpBasic,
				SWT.BORDER,
				-1,
				-1,
				keys,
				serviceprovider,
				getChart( ).getTitle( ).getLabel( ).getCaption( ).getValue( ) );
		{
			GridData gdTXTTitle = new GridData( GridData.FILL_HORIZONTAL );
			txtTitle.setLayoutData( gdTXTTitle );
			txtTitle.setEnabled( getChart( ).getTitle( ).isVisible( ) );
			txtTitle.addListener( this );
		}
		
		btnTVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnTVisible.setText( Messages.getString( "ChartSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnTVisible.setSelection( getChart( ).getTitle( )
					.isVisible( ) );
			btnTVisible.addSelectionListener( this );
		}

		Label lblBackground = new Label( cmpBasic, SWT.NONE );
		lblBackground.setText( Messages.getString( "ChartSheetImpl.Label.Background" ) ); //$NON-NLS-1$

		cmbBackground = new FillChooserComposite( cmpBasic,
				SWT.NONE,
				getChart( ).getBlock( ).getBackground( ),
				true,
				true );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			cmbBackground.setLayoutData( gridData );
			cmbBackground.addListener( this );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartSheetImpl.Label.Outline" ) ); //$NON-NLS-1$

		btnVisible = new Button( cmpBasic, SWT.CHECK );
		{
			GridData gridData = new GridData( );
			gridData.horizontalSpan = 2;
			btnVisible.setLayoutData( gridData );
			btnVisible.setText( Messages.getString( "ChartSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnVisible.setSelection( getChart( ).getBlock( )
					.getOutline( )
					.isVisible( ) );
			btnVisible.addSelectionListener( this );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartSheetImpl.Label.ColorBy" ) ); //$NON-NLS-1$

		cmbColorBy = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			cmbColorBy.setLayoutData( gridData );
			NameSet ns = LiteralHelper.legendItemTypeSet;
			cmbColorBy.setItems( ns.getDisplayNames( ) );
			cmbColorBy.select( ns.getSafeNameIndex( getChart( ).getLegend( )
					.getItemType( )
					.getName( ) ) );
			cmbColorBy.addSelectionListener( this );
		}

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

		Composite cmpInteractivity = new Composite( cmpBasic, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 4, false );
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			cmpInteractivity.setLayout( gl );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL
					| GridData.HORIZONTAL_ALIGN_BEGINNING );
			gd.horizontalSpan = 3;
			cmpInteractivity.setLayoutData( gd );
		}

		new Label( cmpInteractivity, SWT.NONE ).setText( Messages.getString( "ChartSheetImpl.Label.Interactivity" ) ); //$NON-NLS-1$

		btnEnable = new Button( cmpInteractivity, SWT.CHECK );
		{
			btnEnable.setText( Messages.getString( "ChartSheetImpl.Label.InteractivityEnable" ) ); //$NON-NLS-1$
			btnEnable.setSelection( getChart( ).getInteractivity( ).isEnable( ) );
			btnEnable.addSelectionListener( this );
		}

		Label lblType = new Label( cmpInteractivity, SWT.NONE );
		{
			GridData gridData = new GridData( );
			gridData.horizontalIndent = 10;
			lblType.setLayoutData( gridData );
			lblType.setText( Messages.getString( "ChartSheetImpl.Label.LegendBehaviorType" ) ); //$NON-NLS-1$
		}

		cmbInteractivity = new Combo( cmpInteractivity, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			cmbInteractivity.setLayoutData( gridData );
			cmbInteractivity.addSelectionListener( this );
			cmbInteractivity.setEnabled( btnEnable.getSelection( ) );
		}

		populateLists( );

		createButtonGroup( cmpContent );
	}

	private void populateLists( )
	{
		// POPULATE STYLE COMBO WITH AVAILABLE REPORT STYLES
		IDataServiceProvider idsp = ( (ChartWizardContext) super.getContext( ) ).getDataServiceProvider( );
		if ( idsp != null )
		{
			String[] styles = idsp.getAllStyles( );

			// Add None option to remove style
			String[] selection = new String[styles.length + 1];
			System.arraycopy( styles, 0, selection, 1, styles.length );
			selection[0] = Messages.getString( "ChartSheetImpl.Label.None" ); //$NON-NLS-1$
			cmbStyle.setItems( selection );

			String sStyle = idsp.getCurrentStyle( );
			cmbStyle.setText( ( sStyle == null ) ? selection[0] : sStyle );

			NameSet nameSet = LiteralHelper.legendBehaviorTypeSet;
			cmbInteractivity.setItems( nameSet.getDisplayNames( ) );
			cmbInteractivity.select( nameSet.getSafeNameIndex( getChart( ).getInteractivity( )
					.getLegendBehavior( )
					.getName( ) ) );
		}
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 5, true ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 3;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		btnTitleProp = new Button( cmp, SWT.TOGGLE );
		{
			btnTitleProp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			btnTitleProp.setText( Messages.getString( "ChartSheetImpl.Label.TitleProperties" ) ); //$NON-NLS-1$
			btnTitleProp.addSelectionListener( this );
			btnTitleProp.setEnabled( getChart( ).getTitle( ).isVisible( ) );
		}

		btnBlockProp = new Button( cmp, SWT.TOGGLE );
		{
			btnBlockProp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			btnBlockProp.setText( Messages.getString( "ChartSheetImpl.Label.BlockProperties" ) ); //$NON-NLS-1$
			btnBlockProp.addSelectionListener( this );
		}

		btnGeneralProp = new Button( cmp, SWT.TOGGLE );
		{
			btnGeneralProp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			btnGeneralProp.setText( Messages.getString( "ChartSheetImpl.Label.GeneralProperties" ) ); //$NON-NLS-1$
			btnGeneralProp.addSelectionListener( this );
		}
	}

	public Object onHide( )
	{
		detachPopup( );
		cmpContent.dispose( );
		return getContext( );
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
		// detach popup dialogue
		if ( detachPopup( e.widget ) )
		{
			return;
		}
		if ( e.widget instanceof Button
				&& ( ( (Button) e.widget ).getStyle( ) & SWT.TOGGLE ) == SWT.TOGGLE
				&& ( (Button) e.widget ).getSelection( ) )
		{
			selectAllButtons( false );
			( (Button) e.widget ).setSelection( true );
		}

		if ( e.widget.equals( btnTitleProp ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new TitlePropertiesSheet( popupShell, getChart( ) );
			getWizard( ).attachPopup( btnTitleProp.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnBlockProp ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new BlockPropertiesSheet( popupShell, getChart( ) );
			getWizard( ).attachPopup( btnBlockProp.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnGeneralProp ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new MoreOptionsChartSheet( popupShell, getChart( ) );
			getWizard( ).attachPopup( btnGeneralProp.getText( ), -1, -1 );
		}

		if ( e.widget.equals( btnVisible ) )
		{
			getChart( ).getBlock( )
					.getOutline( )
					.setVisible( btnVisible.getSelection( ) );
			refreshPopupSheet( );
		}
		else if ( e.widget.equals( btnTVisible ) )
		{
			getChart( ).getTitle( )
					.setVisible( btnTVisible.getSelection( ) );
			txtTitle.setEnabled( btnTVisible.getSelection( ) );
			btnTitleProp.setEnabled( btnTVisible.getSelection( ) );
			
			if( btnTitleProp.getSelection( ) )
			{
				detachPopup( );			
			}
		}
		else if ( e.widget.equals( cmbColorBy ) )
		{
			getChart( ).getLegend( )
					.setItemType( LegendItemType.get( LiteralHelper.legendItemTypeSet.getNameByDisplayName( cmbColorBy.getText( ) ) ) );
		}
		else if ( e.widget.equals( cmbStyle ) )
		{
			String sStyleName = cmbStyle.getText( );
			if ( cmbStyle.getSelectionIndex( ) == 0 )
			{
				sStyleName = null;
			}
			( (ChartWizardContext) super.getContext( ) ).getDataServiceProvider( )
					.setStyle( sStyleName );
			refreshPreview( );
		}
		else if ( e.widget.equals( btnEnablePreview ) )
		{
			ChartPreviewPainter.enableProcessor( btnEnablePreview.getSelection( ) );
			refreshPreview( );
		}
		else if ( e.widget.equals( btnEnable ) )
		{
			getChart( ).getInteractivity( ).setEnable( btnEnable.getSelection( ) );
			cmbInteractivity.setEnabled( btnEnable.getSelection( ) );
		}
		else if ( e.widget.equals( cmbInteractivity ) )
		{
			getChart( ).getInteractivity( )
					.setLegendBehavior( LegendBehaviorType.get( LiteralHelper.legendBehaviorTypeSet.getNameByDisplayName( cmbInteractivity.getText( ) ) )  );
		}

	}

	/**
	 * Refreshes the preview by model modification. Used by non-model change.
	 * 
	 */
	private void refreshPreview( )
	{
		boolean currentValue = btnVisible.getSelection( );
		getChart( ).getBlock( ).getOutline( ).setVisible( true );
		getChart( ).getBlock( ).getOutline( ).setVisible( currentValue );
	}

	protected void selectAllButtons( boolean isSelected )
	{
		btnTitleProp.setSelection( isSelected );
		btnBlockProp.setSelection( isSelected );
		btnGeneralProp.setSelection( isSelected );
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}
}