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
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendTextSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.MoreOptionsChartLegendSheet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartLegendSheetImpl extends SubtaskSheetImpl
		implements
			Listener,
			SelectionListener
{

	private transient Composite cmpContent = null;

	private transient ExternalizedTextEditorComposite txtTitle;

	private transient Button btnOutlineVisible;

	private transient FillChooserComposite cmbBackgroundColor;

	private transient Button btnLegendText;

	private transient Button btnMoreOpt;

	private transient Button btnTitleVisible;

	private transient Button btnShowValue;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.ISheet#getComponent(org.eclipse.swt.widgets.Composite)
	 */
	public void getComponent( Composite parent )
	{
		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, true );
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

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartLegendSheetImpl.Label.Title" ) ); //$NON-NLS-1$

		btnTitleVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnTitleVisible.setText( Messages.getString( "ChartLegendSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnTitleVisible.addSelectionListener( this );
			btnTitleVisible.setSelection( getChart( ).getLegend( )
					.getTitle( )
					.isVisible( ) );
		}

		new Label( cmpBasic, SWT.NONE );

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
				getChart( ).getLegend( ).getTitle( ).getCaption( ).getValue( ) );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.widthHint = 200;
			txtTitle.setLayoutData( gd );
			txtTitle.addListener( this );
			txtTitle.setEnabled( getChart( ).getLegend( )
					.getTitle( )
					.isVisible( ) );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartLegendSheetImpl.Label.Background" ) ); //$NON-NLS-1$

		cmbBackgroundColor = new FillChooserComposite( cmpBasic, SWT.DROP_DOWN
				| SWT.READ_ONLY, getChart( ).getLegend( )
				.getClientArea( )
				.getBackground( ), true, true );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			cmbBackgroundColor.setLayoutData( gridData );
			cmbBackgroundColor.addListener( this );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartLegendSheetImpl.Label.Outline" ) ); //$NON-NLS-1$

		btnOutlineVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnOutlineVisible.setText( Messages.getString( "ChartLegendSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnOutlineVisible.addSelectionListener( this );
			btnOutlineVisible.setSelection( getChart( ).getLegend( )
					.getClientArea( )
					.getOutline( )
					.isVisible( ) );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartLegendSheetImpl.Label.Value" ) ); //$NON-NLS-1$

		btnShowValue = new Button( cmpBasic, SWT.CHECK );
		{
			btnShowValue.setText( Messages.getString( "ChartLegendSheetImpl.Label.ShowValue" ) ); //$NON-NLS-1$
			btnShowValue.addSelectionListener( this );
			btnShowValue.setSelection( getChart( ).getLegend( ).isShowValue( ) );
		}

		createButtonGroup( cmpContent );
	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 5, true ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		btnLegendText = new Button( cmp, SWT.TOGGLE );
		btnLegendText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		btnLegendText.setText( Messages.getString( "ChartLegendSheetImpl.Label.LegendText" ) ); //$NON-NLS-1$
		btnLegendText.addSelectionListener( this );

		btnMoreOpt = new Button( cmp, SWT.TOGGLE );
		btnMoreOpt.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		btnMoreOpt.setText( Messages.getString( "ChartLegendSheetImpl.Label.MoreOptions" ) ); //$NON-NLS-1$
		btnMoreOpt.addSelectionListener( this );
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
			getChart( ).getLegend( )
					.getTitle( )
					.getCaption( )
					.setValue( txtTitle.getText( ) );
		}
		else if ( event.widget.equals( cmbBackgroundColor ) )
		{
			if ( event.type == FillChooserComposite.FILL_CHANGED_EVENT )
			{
				getChart( ).getLegend( )
						.getClientArea( )
						.setBackground( (Fill) event.data );
			}
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

		if ( e.widget.equals( btnTitleVisible ) )
		{
			getChart( ).getLegend( )
					.getTitle( )
					.setVisible( ( (Button) e.widget ).getSelection( ) );
			txtTitle.setEnabled( getChart( ).getLegend( )
					.getTitle( )
					.isVisible( ) );
			refreshPopupSheet( );
		}
		else if ( e.widget.equals( btnOutlineVisible ) )
		{
			getChart( ).getLegend( )
					.getClientArea( )
					.getOutline( )
					.setVisible( ( (Button) e.widget ).getSelection( ) );
			refreshPopupSheet( );
		}
		else if ( e.widget.equals( btnShowValue ) )
		{
			getChart( ).getLegend( )
					.setShowValue( ( (Button) e.widget ).getSelection( ) );
		}
		else if ( e.widget.equals( btnMoreOpt ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new MoreOptionsChartLegendSheet( popupShell,
					getChart( ) );
			getWizard( ).attachPopup( btnMoreOpt.getText( ), -1, -1 );
		}
		else if ( e.widget.equals( btnLegendText ) )
		{
			popupShell = createPopupShell( );
			popupSheet = new LegendTextSheet( popupShell, getChart( ) );
			getWizard( ).attachPopup( btnLegendText.getText( ), -1, -1 );
		}

	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

	protected void selectAllButtons( boolean isSelected )
	{
		btnLegendText.setSelection( isSelected );
		btnMoreOpt.setSelection( isSelected );
	}

}