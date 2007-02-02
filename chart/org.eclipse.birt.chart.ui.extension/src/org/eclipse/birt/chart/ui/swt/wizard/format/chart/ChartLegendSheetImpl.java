/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite; // import
																					// org.eclipse.birt.chart.ui.swt.composites.TooltipDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendLayoutSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendTextSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendTitleSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil; // import
													// org.eclipse.birt.chart.ui.util.UIHelper;
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
import org.eclipse.birt.chart.util.LiteralHelper;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartLegendSheetImpl extends SubtaskSheetImpl implements
		Listener,
		SelectionListener
{

	private Button btnVisible;

	private ExternalizedTextEditorComposite txtTitle;

	private Button btnTitleVisible;

	private Button btnShowValue;

	private Label lblTitle;

	private Label lblShowValue;

	private Button btnLegendTitle;

	private transient Label lblLegendBehavior;

	private transient Combo cmbLegendBehavior;

	private transient Button btnInteractivity;

	// private transient Button btnTooltip;

	public void createControl( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_LEGEND );

		init( );

		cmpContent = new Composite( parent, SWT.NONE );
		{
			GridLayout glContent = new GridLayout( 2, false );
			cmpContent.setLayout( glContent );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Group cmpBasic = new Group( cmpContent, SWT.NONE );
		{
			GridLayout layout = new GridLayout( 3, false );
			layout.marginWidth = 10;
			layout.marginHeight = 10;
			cmpBasic.setLayout( layout );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmpBasic.setLayoutData( gd );
			cmpBasic.setText( Messages.getString( "ChartLegendSheetImpl.Label.Legend" ) ); //$NON-NLS-1$
		}

		btnVisible = new Button( cmpBasic, SWT.CHECK );
		{
			GridData gdBTNVisible = new GridData( );
			gdBTNVisible.horizontalSpan = 3;
			btnVisible.setLayoutData( gdBTNVisible );
			btnVisible.setText( Messages.getString( "Shared.mne.Visibile_v" ) );//$NON-NLS-1$
			btnVisible.setSelection( getChart( ).getLegend( ).isVisible( ) );
			btnVisible.addSelectionListener( this );
		}

		lblTitle = new Label( cmpBasic, SWT.NONE );
		lblTitle.setText( Messages.getString( "ChartLegendSheetImpl.Label.Title" ) ); //$NON-NLS-1$

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
			gd.widthHint = 180;
			txtTitle.setLayoutData( gd );
			txtTitle.addListener( this );
		}

		btnTitleVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnTitleVisible.setText( Messages.getString( "Shared.mne.Visibile_s" ) ); //$NON-NLS-1$
			btnTitleVisible.addSelectionListener( this );
			btnTitleVisible.setSelection( getChart( ).getLegend( )
					.getTitle( )
					.isVisible( ) );
		}

		lblLegendBehavior = new Label( cmpBasic, SWT.NONE );
		{
			lblLegendBehavior.setText( Messages.getString( "ChartLegendSheetImpl.Label.LegendBehaviorType" ) ); //$NON-NLS-1$
		}

		cmbLegendBehavior = new Combo( cmpBasic, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gridData = new GridData( );
			gridData.widthHint = 150;
			cmbLegendBehavior.setLayoutData( gridData );
			cmbLegendBehavior.addSelectionListener( this );
		}

		new Label( cmpBasic, SWT.NONE );

		if ( isShowValueEnabled( ) )
		{
			lblShowValue = new Label( cmpBasic, SWT.NONE );
			lblShowValue.setText( Messages.getString( "ChartLegendSheetImpl.Label.Value" ) ); //$NON-NLS-1$

			btnShowValue = new Button( cmpBasic, SWT.CHECK );
			{
				GridData gdShowValue = new GridData( );
				gdShowValue.horizontalSpan = 2;
				btnShowValue.setLayoutData( gdShowValue );
				btnShowValue.setText( Messages.getString( "ChartLegendSheetImpl.Label.ShowValue" ) ); //$NON-NLS-1$
				btnShowValue.setToolTipText( Messages.getString( "ChartLegendSheetImpl.Tooltip.ShowDataPointValue" ) ); //$NON-NLS-1$
				btnShowValue.addSelectionListener( this );
				btnShowValue.setSelection( getChart( ).getLegend( )
						.isShowValue( ) );
			}
		}

		// Label lblTooltip = new Label( cmpBasic, SWT.NONE );
		// {
		// lblTooltip.setText( Messages.getString(
		// "ChartTitleSheetImpl.Label.Tooltip" ) ); //$NON-NLS-1$
		// }
		//		
		// btnTooltip = new Button( cmpBasic, SWT.PUSH );
		// {
		// btnTooltip.setImage( UIHelper.getImage( "icons/obj16/tooltip.gif" )
		// ); //$NON-NLS-1$
		// btnTooltip.addSelectionListener( this );
		// }

		populateLists( );
		createButtonGroup( cmpContent );
		setState( getChart( ).getLegend( ).isVisible( ) );
	}

	private void populateLists( )
	{
		NameSet nameSet = LiteralHelper.legendBehaviorTypeSet;
		cmbLegendBehavior.setItems( nameSet.getDisplayNames( ) );
		cmbLegendBehavior.select( nameSet.getSafeNameIndex( getChart( ).getInteractivity( )
				.getLegendBehavior( )
				.getName( ) ) );
	}

	private void setState( boolean enabled )
	{
		lblTitle.setEnabled( enabled );
		txtTitle.setEnabled( enabled && btnTitleVisible.getSelection( ) );
		btnTitleVisible.setEnabled( enabled );
		if ( isShowValueEnabled( ) )
		{
			lblShowValue.setEnabled( enabled );
			btnShowValue.setEnabled( enabled );
		}

		// Adjust the button selection according to visibility
		Iterator buttons = getToggleButtons( ).iterator( );
		while ( buttons.hasNext( ) )
		{
			( (Button) buttons.next( ) ).setEnabled( enabled );
		}
		btnLegendTitle.setEnabled( btnTitleVisible.getSelection( ) && enabled );
		btnInteractivity.setEnabled( getChart( ).getInteractivity( ).isEnable( )
				&& enabled );
	}

	private boolean isShowValueEnabled( )
	{
		return getChart( ).getLegend( ).getItemType( ) == LegendItemType.SERIES_LITERAL;
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
			cmp.setLayout( new GridLayout( 4, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		// Title
		ITaskPopupSheet popup = new LegendTitleSheet( Messages.getString( "ChartLegendSheetImpl.Title.FormatLegendTitle" ), getContext( ) ); //$NON-NLS-1$
		btnLegendTitle = createToggleButton( cmp,
				Messages.getString( "ChartLegendSheetImpl.Label.LegendTitle" ), popup ); //$NON-NLS-1$
		btnLegendTitle.addSelectionListener( this );
		btnLegendTitle.setEnabled( btnTitleVisible.getSelection( ) );

		// Text Area
		popup = new LegendTextSheet( Messages.getString( "ChartLegendSheetImpl.Title.FormatLegendText" ), getContext( ) ); //$NON-NLS-1$
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
		btnInteractivity = createToggleButton( cmp,
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

		if ( e.widget.equals( btnVisible ) )
		{
			getChart( ).getLegend( ).setVisible( btnVisible.getSelection( ) );
			boolean enabled = btnVisible.getSelection( );

			// If legend is invisible, close popup
			if ( !enabled && isButtonSelected( ) )
			{
				detachPopup( );
			}
			// Adjust the UI according to visibility
			setState( enabled );
		}
		else if ( e.widget.equals( btnTitleVisible ) )
		{
			btnLegendTitle.setEnabled( btnTitleVisible.getSelection( ) );
			getChart( ).getLegend( )
					.getTitle( )
					.setVisible( ( (Button) e.widget ).getSelection( ) );
			txtTitle.setEnabled( getChart( ).getLegend( )
					.getTitle( )
					.isVisible( ) );
			if ( !btnTitleVisible.getSelection( )
					&& btnLegendTitle.getSelection( ) )
			{
				btnLegendTitle.setSelection( false );
				detachPopup( );
			}
			else
			{
				refreshPopupSheet( );
			}
		}
		else if ( e.widget.equals( cmbLegendBehavior ) )
		{
			getChart( ).getInteractivity( )
					.setLegendBehavior( LegendBehaviorType.getByName( LiteralHelper.legendBehaviorTypeSet.getNameByDisplayName( cmbLegendBehavior.getText( ) ) ) );
		}
		else if ( e.widget.equals( btnShowValue ) )
		{
			getChart( ).getLegend( )
					.setShowValue( ( (Button) e.widget ).getSelection( ) );
		}
		// else if ( e.widget.equals( btnTooltip ) )
		// {
		// new TooltipDialog( cmpContent.getShell( ),
		// getChart( ).getLegend( ).getTriggers( ),
		// getContext( ),
		// Messages.getString( "ChartLegendSheetImpl.Title.Tooltip" ), false,
		// true ).open( ); //$NON-NLS-1$
		// }
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub
	}

}