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

import java.util.List;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.TitleBlockSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.TitleTextSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.TriggerSupportMatrix;
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
 * Title subtask
 * 
 */
public class ChartTitleSheetImpl extends SubtaskSheetImpl
		implements
			SelectionListener,
			Listener
{

	private ExternalizedTextEditorComposite txtTitle = null;

	private FontDefinitionComposite fdcFont;

	private Button btnVisible;

	public void createControl( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_TITLE );

		init( );

		cmpContent = new Composite( parent, SWT.NONE );
		{
			cmpContent.setLayout( new GridLayout( ) );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpContent.setLayoutData( gd );
		}

		Composite cmpBasic = new Composite( cmpContent, SWT.NONE );
		{
			cmpBasic.setLayout( new GridLayout( 3, false ) );
			GridData gd = new GridData( GridData.FILL_BOTH );
			cmpBasic.setLayoutData( gd );
		}

		Label lblTitle = new Label( cmpBasic, SWT.NONE );
		{
			lblTitle.setText( Messages.getString( "ChartTitleSheetImpl.Label.ChartTitle" ) ); //$NON-NLS-1$
		}

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
				getChart( ).getTitle( ).getLabel( ).getCaption( ).getValue( ) );
		{
			GridData gdTXTTitle = new GridData( );
			gdTXTTitle.widthHint = 200;
			txtTitle.setLayoutData( gdTXTTitle );
			if ( !getChart( ).getTitle( ).isVisible( ) )
			{
				txtTitle.setEnabled( false );
			}
			txtTitle.addListener( this );
		}

		btnVisible = new Button( cmpBasic, SWT.CHECK );
		{
			btnVisible.setText( Messages.getString( "ChartSheetImpl.Label.Visible" ) ); //$NON-NLS-1$
			btnVisible.setSelection( getChart( ).getTitle( ).isVisible( ) );
			btnVisible.addSelectionListener( this );
		}

		Label lblFont = new Label( cmpBasic, SWT.NONE );
		lblFont.setText( Messages.getString( "LabelAttributesComposite.Lbl.Font" ) ); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite( cmpBasic,
				SWT.NONE,
				getContext( ),
				getChart( ).getTitle( ).getLabel( ).getCaption( ).getFont( ),
				getChart( ).getTitle( ).getLabel( ).getCaption( ).getColor( ),
				true );
		GridData gdFDCFont = new GridData( );
		// gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
		gdFDCFont.widthHint = 200;
		gdFDCFont.grabExcessVerticalSpace = false;
		fdcFont.setLayoutData( gdFDCFont );
		fdcFont.addListener( this );

		// new Label( cmpBasic, SWT.NONE );
		//		
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

	private void createButtonGroup( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		{
			cmp.setLayout( new GridLayout( 5, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		ITaskPopupSheet popup;
		// Title Text
		popup = new TitleTextSheet( Messages.getString( "ChartTitleSheetImpl.Text.TitleText" ), //$NON-NLS-1$
				getContext( ) );
		Button btnTitleProp = createToggleButton( cmp,
				BUTTON_TEXT,
				Messages.getString( "ChartTitleSheetImpl.Text.TitleText&" ), //$NON-NLS-1$
				popup,
				getChart( ).getTitle( ).isVisible( ) );
		btnTitleProp.addSelectionListener( this );

		// Title Layout
		popup = new TitleBlockSheet( Messages.getString( "ChartTitleSheetImpl.Text.TitleLayout" ), //$NON-NLS-1$
				getContext( ) );
		Button btnBlockProp = createToggleButton( cmp,
				BUTTON_LAYOUT,
				Messages.getString( "ChartTitleSheetImpl.Text.Layout&" ), //$NON-NLS-1$
				popup,
				getChart( ).getTitle( ).isVisible( ) );
		btnBlockProp.addSelectionListener( this );

		// Interactivity
		popup = new InteractivitySheet( Messages.getString( "ChartTitleSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
				getContext( ),
				getChart( ).getTitle( ).getTriggers( ),
				getChart( ).getTitle( ),
				TriggerSupportMatrix.TYPE_CHARTTITLE,
				false,
				true );
		Button btnInteractivity = createToggleButton( cmp,
				BUTTON_INTERACTIVITY,
				Messages.getString( "SeriesYSheetImpl.Label.Interactivity&" ), //$NON-NLS-1$
				popup,
				getChart( ).getInteractivity( ).isEnable( ) );
		btnInteractivity.addSelectionListener( this );
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
		else if ( event.widget.equals( fdcFont ) )
		{
			if ( event.type == FontDefinitionComposite.FONT_CHANTED_EVENT )
			{
				getChart( ).getTitle( )
						.getLabel( )
						.getCaption( )
						.setFont( (FontDefinition) ( (Object[]) event.data )[0] );
				getChart( ).getTitle( )
						.getLabel( )
						.getCaption( )
						.setColor( (ColorDefinition) ( (Object[]) event.data )[1] );
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
			attachPopup( ( (Button) e.widget ).getData( ).toString( ) );
		}

		if ( e.widget.equals( btnVisible ) )
		{
			getChart( ).getTitle( ).setVisible( btnVisible.getSelection( ) );
			txtTitle.setEnabled( btnVisible.getSelection( ) );
			setToggleButtonEnabled( BUTTON_TEXT, btnVisible.getSelection( ) );
			setToggleButtonEnabled( BUTTON_LAYOUT, btnVisible.getSelection( ) );

			if ( getToggleButton( BUTTON_TEXT ).getSelection( )
					|| getToggleButton( BUTTON_LAYOUT ).getSelection( ) )
			{
				detachPopup( );
			}
		}
		// else if ( e.widget.equals( btnTooltip ) )
		// {
		// new TooltipDialog( cmpContent.getShell( ),
		// getChart( ).getTitle( ).getTriggers( ),
		// getContext( ),
		// Messages.getString( "ChartTitleSheetImpl.Title.Tooltip" ), false,
		// true ).open( ); //$NON-NLS-1$
		// }
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}

}