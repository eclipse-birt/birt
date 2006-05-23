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
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendLayoutSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendTextSheet;
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

	private transient ExternalizedTextEditorComposite txtTitle;

	private transient FillChooserComposite cmbBackgroundColor;

	private transient Button btnTitleVisible;

	private transient Button btnShowValue;

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
			GridLayout glContent = new GridLayout( 2, false );
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

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartLegendSheetImpl.Label.Title" ) ); //$NON-NLS-1$

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
				getChart( ).getLegend( ).getTitle( ).getCaption( ).getValue( ) );
		{
			GridData gd = new GridData( );
			gd.widthHint = 200;
			txtTitle.setLayoutData( gd );
			txtTitle.addListener( this );
			txtTitle.setEnabled( getChart( ).getLegend( )
					.getTitle( )
					.isVisible( ) );
		}

		btnTitleVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnTitleVisible.setText( Messages.getString( "ChartLegendSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnTitleVisible.addSelectionListener( this );
			btnTitleVisible.setSelection( getChart( ).getLegend( )
					.getTitle( )
					.isVisible( ) );
		}

		new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartLegendSheetImpl.Label.Background" ) ); //$NON-NLS-1$

		cmbBackgroundColor = new FillChooserComposite( cmpBasic, SWT.DROP_DOWN
				| SWT.READ_ONLY, getContext( ), getChart( ).getLegend( )
				.getClientArea( )
				.getBackground( ), true, true );
		{
			GridData gridData = new GridData( );
			gridData.widthHint = 200;
			gridData.horizontalSpan = 2;
			cmbBackgroundColor.setLayoutData( gridData );
			cmbBackgroundColor.addListener( this );
		}

		if ( getChart( ).getLegend( ).getItemType( ) == LegendItemType.SERIES_LITERAL )
		{
			new Label( cmpBasic, SWT.NONE ).setText( Messages.getString( "ChartLegendSheetImpl.Label.Value" ) ); //$NON-NLS-1$

			btnShowValue = new Button( cmpBasic, SWT.CHECK );
			{
				GridData gridData = new GridData( );
				gridData.horizontalSpan = 2;
				btnShowValue.setLayoutData( gridData );
				btnShowValue.setText( Messages.getString( "ChartLegendSheetImpl.Label.ShowValue" ) ); //$NON-NLS-1$
				btnShowValue.setToolTipText( Messages.getString( "ChartLegendSheetImpl.Tooltip.ShowDataPointValue" ) ); //$NON-NLS-1$
				btnShowValue.addSelectionListener( this );
				btnShowValue.setSelection( getChart( ).getLegend( )
						.isShowValue( ) );
			}
		}

		createButtonGroup( cmpContent );
	}

	private void init( )
	{
		// Make it compatible with old model
		if ( getChart( ).getLegend( ).getTitle( ) == null )
		{
			org.eclipse.birt.chart.model.component.Label label = LabelImpl.create( );
			label.eAdapters( ).addAll( getChart( ).getLegend( ).eAdapters( ) );
			getChart( ).getLegend( ).setTitle( label );
		}

	}

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 3, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		// Text
		ITaskPopupSheet popup = new LegendTextSheet( Messages.getString( "ChartLegendSheetImpl.Title.FormatLegendText" ), getContext( ) ); //$NON-NLS-1$
		Button btnLegendText = createToggleButton( cmp,
				Messages.getString( "ChartLegendSheetImpl.Label.TextFormat" ), popup ); //$NON-NLS-1$
		btnLegendText.addSelectionListener( this );

		// Layout
		popup = new LegendLayoutSheet( Messages.getString( "ChartLegendSheetImpl.Title.LayoutLegend" ), getContext( ) ); //$NON-NLS-1$
		Button btnAreaProp = createToggleButton( cmp,
				Messages.getString( "ChartLegendSheetImpl.Label.Layout" ), popup ); //$NON-NLS-1$
		btnAreaProp.addSelectionListener( this );

		// Interactivity
		popup = new InteractivitySheet( Messages.getString( "SeriesYSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
				getContext( ),
				getChart( ).getLegend( ).getTriggers( ),
				false,
				true );
		Button btnInteractivity = createToggleButton( cmp,
				Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
				popup );
		btnInteractivity.addSelectionListener( this );
		btnInteractivity.setEnabled( getChart( ).getInteractivity( ).isEnable( ) );
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
		// Detach popup dialog if there's selected button.
		if ( detachPopup( e.widget ) )
		{
			return;
		}

		if ( isRegistered( e.widget ) )
		{
			attachPopup( ( (Button) e.widget ).getText( ) );
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
		else if ( e.widget.equals( btnShowValue ) )
		{
			getChart( ).getLegend( )
					.setShowValue( ( (Button) e.widget ).getSelection( ) );
		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

}