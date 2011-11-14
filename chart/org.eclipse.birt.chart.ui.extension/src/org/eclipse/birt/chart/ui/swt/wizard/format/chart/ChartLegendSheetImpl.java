/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.chart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.TriggerDataComposite;
import org.eclipse.birt.chart.ui.swt.composites.TristateCheckbox;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendLayoutSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendTextSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.LegendTitleSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
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
 * Legend subtask
 * 
 */
public class ChartLegendSheetImpl extends SubtaskSheetImpl
		implements
			Listener,
			SelectionListener
{

	private TristateCheckbox btnVisible;
	
	private ExternalizedTextEditorComposite txtTitle;
	
	private TristateCheckbox btnTitleVisible;
	
	private TristateCheckbox btnShowValue;

	private Label lblTitle;

	private Label lblShowValue;

	private Label lblLegendBehavior;

	protected Combo cmbLegendBehavior;

	// private Button btnTooltip;

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

		btnVisible = new TristateCheckbox( cmpBasic, SWT.NONE );
		{
			GridData gdBTNVisible = new GridData( );
			gdBTNVisible.horizontalSpan = 3;
			btnVisible.setLayoutData( gdBTNVisible );
			btnVisible.setText( Messages.getString( "Shared.mne.Visibile_v" ) );//$NON-NLS-1$
		}

		lblTitle = new Label( cmpBasic, SWT.NONE );
		lblTitle.setText( Messages.getString( "ChartLegendSheetImpl.Label.Title" ) ); //$NON-NLS-1$

		List<String> keys = null;
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

		btnTitleVisible = new TristateCheckbox( cmpBasic, SWT.NONE );
		btnTitleVisible.setText( Messages.getString( "Shared.mne.Visibile_s" ) ); //$NON-NLS-1$
		
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
			cmbLegendBehavior.setEnabled( getChart( ).getInteractivity( ).isEnable( ) );
		}

		new Label( cmpBasic, SWT.NONE );

		if ( isShowValueEnabled( ) )
		{
			lblShowValue = new Label( cmpBasic, SWT.NONE );
			lblShowValue.setText( Messages.getString( "ChartLegendSheetImpl.Label.Value" ) ); //$NON-NLS-1$

			btnShowValue = new TristateCheckbox( cmpBasic, SWT.NONE );
			{
				GridData gdBTNVisible = new GridData( );
				gdBTNVisible.horizontalSpan = 2;
				btnShowValue.setLayoutData( gdBTNVisible );
				btnShowValue.setText( Messages.getString( "ChartLegendSheetImpl.Label.ShowValue" ) ); //$NON-NLS-1$
				btnShowValue.setToolTipText( Messages.getString( "ChartLegendSheetImpl.Tooltip.ShowDataPointValue" ) ); //$NON-NLS-1$
			}
		}

		populateLists( );
		initDataNListeners( );
		createButtonGroup( cmpContent );
		setState( getChart( ).getLegend( ).isSetVisible( )
				&& getChart( ).getLegend( ).isVisible( ) );
	}

	protected void initDataNListeners( )
	{
		Legend l = getChart( ).getLegend( );
		int state = l.isSetVisible( ) ? ( l.isVisible( ) ? TristateCheckbox.STATE_SELECTED
				: TristateCheckbox.STATE_UNSELECTED )
				: TristateCheckbox.STATE_GRAYED;
		btnVisible.setSelectionState( state );
		btnVisible.addSelectionListener( this );

		state = l.getTitle( ).isSetVisible( ) ? ( l.getTitle( ).isVisible( ) ? TristateCheckbox.STATE_SELECTED
				: TristateCheckbox.STATE_UNSELECTED )
				: TristateCheckbox.STATE_GRAYED;
		btnTitleVisible.setSelectionState( state );
		btnTitleVisible.addSelectionListener( this );

		if ( isShowValueEnabled( ) )
		{
			state = l.isSetShowValue( ) ? ( l.isShowValue( ) ? TristateCheckbox.STATE_SELECTED
					: TristateCheckbox.STATE_UNSELECTED )
					: TristateCheckbox.STATE_GRAYED;
			btnShowValue.addSelectionListener( this );
			btnShowValue.setSelectionState( state );
		}
	}

	protected void populateLists( )
	{
		NameSet nameSet = LiteralHelper.legendBehaviorTypeSet;
		List<String> names = new ArrayList<String>( Arrays.asList( nameSet.getDisplayNames( ) ) );
		names.add( 0, ChartUIExtensionUtil.getAutoMessage( ) );
		if ( isBehaviorSupported( ) ) 
		{
			cmbLegendBehavior.setItems( names.toArray( new String[]{} ) );
			if ( !getChart( ).getInteractivity( ).isSetLegendBehavior( ) )
			{
				cmbLegendBehavior.select( 0 );
			}
			else
			{
				cmbLegendBehavior.select( nameSet.getSafeNameIndex( getChart( ).getInteractivity( )
						.getLegendBehavior( )
						.getName( ) ) + 1 );
			}
		}
		else
		{
			cmbLegendBehavior.setItems( new String[]{
				nameSet.getDisplayNames( )[0]
			} );
			cmbLegendBehavior.select( 0 );
		}
	}

	protected boolean isBehaviorSupported( )
	{
		return "SVG".equalsIgnoreCase( getContext( ).getOutputFormat( ) ); //$NON-NLS-1$
	}

	private void setState( boolean enabled )
	{
		lblTitle.setEnabled( enabled );
		txtTitle.setEnabled( enabled &&  getTitleVisibleSelection( ) );
		btnTitleVisible.setEnabled( enabled );
		lblLegendBehavior.setEnabled( enabled );
		cmbLegendBehavior.setEnabled( enabled );
		if ( isShowValueEnabled( ) )
		{
			lblShowValue.setEnabled( enabled );
			btnShowValue.setEnabled( enabled );
		}

		// Adjust the button selection according to visibility
		Iterator<Button> buttons = getToggleButtons( ).iterator( );
		while ( buttons.hasNext( ) )
		{
			Button toggle = buttons.next( );
			toggle.setEnabled( enabled
					&& getContext( ).isEnabled( SUBTASK_LEGEND
							+ toggle.getData( ) ) );
		}
		setToggleButtonEnabled( BUTTON_TITLE, getTitleVisibleSelection( )
				&& enabled );
		setToggleButtonEnabled( BUTTON_INTERACTIVITY,
				getChart( ).getInteractivity( ).isEnable( ) && enabled );
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

	protected void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 5, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 2;
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		// Title
		ITaskPopupSheet popup = new LegendTitleSheet( Messages.getString( "ChartLegendSheetImpl.Title.LegendTitle" ),//$NON-NLS-1$
				getContext( ) );
		Button btnLegendTitle = createToggleButton( cmp,
				BUTTON_TITLE,
				Messages.getString( "ChartLegendSheetImpl.Label.LegendTitle&" ),//$NON-NLS-1$
				popup,
				getTitleVisibleSelection( ) );
		btnLegendTitle.addSelectionListener( this );

		// Layout
		popup = new LegendLayoutSheet( Messages.getString( "ChartLegendSheetImpl.Title.LegendLayout" ), getContext( ) ); //$NON-NLS-1$
		Button btnAreaProp = createToggleButton( cmp,
				BUTTON_LAYOUT,
				Messages.getString( "ChartLegendSheetImpl.Label.Layout" ), popup ); //$NON-NLS-1$
		btnAreaProp.addSelectionListener( this );

		// Entries
		createLegendEntriesUI( cmp );

		// Interactivity
		if ( getContext( ).isInteractivityEnabled( ) )
		{
			popup = new InteractivitySheet( Messages.getString( "ChartLegendSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
					getContext( ),
					getChart( ).getLegend( ).getTriggers( ),
					getChart( ).getLegend( ),
					TriggerSupportMatrix.TYPE_LEGEND,
					TriggerDataComposite.ENABLE_URL_PARAMETERS
							| TriggerDataComposite.DISABLE_CATEGORY_SERIES
							| TriggerDataComposite.DISABLE_VALUE_SERIES
							| TriggerDataComposite.ENABLE_SHOW_TOOLTIP_VALUE );
			Button btnInteractivity = createToggleButton( cmp,
					BUTTON_INTERACTIVITY,
					Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
					popup,
					getChart( ).getInteractivity( ).isEnable( ) );
			btnInteractivity.addSelectionListener( this );
		}
	}

	protected void createLegendEntriesUI( Composite cmp )
	{
		ITaskPopupSheet popup;
		popup = new LegendTextSheet( Messages.getString( "ChartLegendSheetImpl.Title.LegendEntries" ), getContext( ) ); //$NON-NLS-1$
		Button btnLegendText = createToggleButton( cmp,
				BUTTON_ENTRIES,
				Messages.getString( "ChartLegendSheetImpl.Label.Entries" ), popup ); //$NON-NLS-1$
		btnLegendText.addSelectionListener( this );
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
			attachPopup( ( (Button) e.widget ).getData( ).toString( ) );
		}

		if ( e.widget.equals( btnVisible ) )
		{
			boolean enabled = false;
			int state = btnVisible.getSelectionState( );
			switch ( state )
			{
				case TristateCheckbox.STATE_GRAYED :
					getChart( ).getLegend( ).unsetVisible( );
					enabled = false;
					break;
				case TristateCheckbox.STATE_SELECTED :
					getChart( ).getLegend( ).setVisible( true );
					enabled = true;
					break;
				case TristateCheckbox.STATE_UNSELECTED :
					getChart( ).getLegend( ).setVisible( false );
					enabled = false;
					break;
			}

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

			setToggleButtonEnabled( BUTTON_TITLE,
					getTitleVisibleSelection( ) );
			int state = btnTitleVisible.getSelectionState( );
			boolean enabled = false;
			switch ( state )
			{
				case TristateCheckbox.STATE_GRAYED :
					getChart( ).getLegend( ).getTitle( ).unsetVisible( );
					enabled = false;
					break;
				case TristateCheckbox.STATE_SELECTED :
					getChart( ).getLegend( ).getTitle( ).setVisible( true );
					enabled = true;
					break;
				case TristateCheckbox.STATE_UNSELECTED :
					getChart( ).getLegend( ).getTitle( ).setVisible( false );
					enabled = false;
					break;
			}
			txtTitle.setEnabled( enabled );
			Button btnLegendTitle = getToggleButton( BUTTON_TITLE );
			if ( !getTitleVisibleSelection( )
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
			if ( cmbLegendBehavior.getSelectionIndex( ) == 0 )
			{
				getChart( ).getInteractivity( ).unsetLegendBehavior( );
			}
			else
			{
				getChart( ).getInteractivity( )
						.setLegendBehavior( LegendBehaviorType.getByName( LiteralHelper.legendBehaviorTypeSet.getNameByDisplayName( cmbLegendBehavior.getText( ) ) ) );
			}
		}
		else if ( e.widget.equals( btnShowValue ) )
		{
			int state = btnShowValue.getSelectionState( );
			switch ( state )
			{
				case TristateCheckbox.STATE_GRAYED :
					getChart( ).getLegend( ).unsetShowValue( );
					break;
				case TristateCheckbox.STATE_SELECTED :
					getChart( ).getLegend( ).setShowValue( true );
					break;
				case TristateCheckbox.STATE_UNSELECTED :
					getChart( ).getLegend( ).setShowValue( false );
			}
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
		// Do nothing.
	}

	private boolean getTitleVisibleSelection()
	{
		return btnTitleVisible.getSelectionState() == TristateCheckbox.STATE_SELECTED;
	}
}