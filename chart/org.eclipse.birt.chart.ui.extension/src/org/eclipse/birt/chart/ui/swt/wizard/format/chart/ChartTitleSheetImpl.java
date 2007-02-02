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

import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.ExternalizedTextEditorComposite;
import org.eclipse.birt.chart.ui.swt.composites.FontDefinitionComposite;
//import org.eclipse.birt.chart.ui.swt.composites.TooltipDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.SubtaskSheetImpl;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.InteractivitySheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.TitleBlockSheet;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart.TitleTextSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
//import org.eclipse.birt.chart.ui.util.UIHelper;
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
public class ChartTitleSheetImpl extends SubtaskSheetImpl implements
		SelectionListener,
		Listener
{
	private transient ExternalizedTextEditorComposite txtTitle = null;
	
	private transient FontDefinitionComposite fdcFont;
	
	private transient Button btnTitleProp;
	
	private transient Button btnVisible;
//	
//	private transient Button btnTooltip;
	
	public static final int FONT_CHANGED_EVENT = 3;

	public void createControl( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_CHART );

		init( );

		cmpContent = new Composite( parent, SWT.NONE );
		{
			cmpContent.setLayout(  new GridLayout( ) );
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
			GridData gdTXTTitle = new GridData( );
			gdTXTTitle.widthHint = 200;
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
		
		Label lblFont = new Label( cmpBasic, SWT.NONE );
		lblFont.setText( Messages.getString( "LabelAttributesComposite.Lbl.Font" ) ); //$NON-NLS-1$

		fdcFont = new FontDefinitionComposite( cmpBasic,
				SWT.NONE,
				getContext( ),
				getChart( ).getTitle( ).getLabel( ).getCaption( ).getFont( ),
				getChart( ).getTitle( ).getLabel( ).getCaption( ).getColor( ),
				true );
		GridData gdFDCFont = new GridData( );
		gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
		gdFDCFont.widthHint = 200;
		gdFDCFont.grabExcessVerticalSpace = false;
		fdcFont.setLayoutData( gdFDCFont );
		fdcFont.addListener( this );
		
//		new Label( cmpBasic, SWT.NONE );
//		
//		Label lblTooltip = new Label( cmpBasic, SWT.NONE );
//		{
//			lblTooltip.setText( Messages.getString( "ChartTitleSheetImpl.Label.Tooltip" ) ); //$NON-NLS-1$
//		}
//		
//		btnTooltip = new Button( cmpBasic, SWT.PUSH );
//		{
//			btnTooltip.setImage( UIHelper.getImage( "icons/obj16/tooltip.gif" ) ); //$NON-NLS-1$
//			btnTooltip.addSelectionListener( this );
//		}

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
			cmp.setLayout( new GridLayout( 4, false ) );
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.grabExcessVerticalSpace = true;
			gridData.verticalAlignment = SWT.END;
			cmp.setLayoutData( gridData );
		}

		ITaskPopupSheet popup;
		// Title Text
		popup = new TitleTextSheet( Messages.getString( "ChartTitleSheetImpl.Text.TitleText" ), //$NON-NLS-1$
				getContext( ) );
		btnTitleProp = createToggleButton( cmp,
				Messages.getString( "ChartTitleSheetImpl.Text.TitleText&" ), //$NON-NLS-1$
				//$NON-NLS-1$
				popup );
		btnTitleProp.addSelectionListener( this );
		btnTitleProp.setEnabled( getChart( ).getTitle( ).isVisible( ) );
		
		// Title Block
		popup = new TitleBlockSheet( Messages.getString( "ChartTitleSheetImpl.Text.TitleBlock" ), //$NON-NLS-1$
				getContext( ) );
		Button btnBlockProp = createToggleButton( cmp,
				Messages.getString( "ChartTitleSheetImpl.Text.TitleBlock&" ), //$NON-NLS-1$
				//$NON-NLS-1$
				popup );
		btnBlockProp.addSelectionListener( this );
		btnBlockProp.setEnabled( getChart( ).getTitle( ).isVisible( ) );

		// Interactivity
		popup = new InteractivitySheet( Messages.getString( "SeriesYSheetImpl.Label.Interactivity" ), //$NON-NLS-1$
				getContext( ),
				getChart( ).getTitle( ).getTriggers( ),
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
			getChart( ).getTitle( )
					.getLabel( )
					.getCaption( )
					.setValue( txtTitle.getText( ) );
		}
		else if ( event.widget.equals( fdcFont ) )
		{
			event.type = FONT_CHANGED_EVENT;
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
//		else if ( e.widget.equals( btnTooltip ) )
//		{
//			new TooltipDialog( cmpContent.getShell( ),
//					getChart( ).getTitle( ).getTriggers( ),
//					getContext( ),
//					Messages.getString( "ChartTitleSheetImpl.Title.Tooltip" ), false, true ).open( ); //$NON-NLS-1$
//		}
	}

	public void widgetDefaultSelected( SelectionEvent e )
	{
		// TODO Auto-generated method stub

	}

}