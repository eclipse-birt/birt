/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Direction;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.InsetsComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
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
 * Legend - Layout
 */

public class LegendLayoutSheet extends AbstractPopupSheet
		implements
			Listener,
			SelectionListener
{

	private transient Combo cmbAnchor;

	private transient Combo cmbStretch;

	private transient LineAttributesComposite outlineLegend;

	private transient InsetsComposite icLegend;

	// private transient IntegerSpinControl iscVSpacing;
	//
	// private transient IntegerSpinControl iscHSpacing;

	private transient Combo cmbOrientation;

	private transient Combo cmbPosition;

	private transient FillChooserComposite fccBackground;

	private transient Combo cmbDirection;

	private transient Button btnVisible;

	private transient TextEditorComposite txtWrapping = null;

	// private transient Label lblHorizontalSpacing;
	//
	// private transient Label lblVerticalSpacing;

	private transient Label lblDirection;

	private transient Label lblBackground;

	private transient Label lblStretch;

	private transient Label lblAnchor;

	private transient Label lblPosition;

	private transient Label lblOrientation;

	private transient Label lblWrapping;

	private boolean bEnableUI = true;

	public LegendLayoutSheet( String title, ChartWizardContext context )
	{
		super( title, context, true );
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_LEGEND_LAYOUT );
		
		bEnableUI = getBlockForProcessing( ).isVisible( );

		Composite cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( new GridLayout( ) );

		Group grpLegendArea = new Group( cmpContent, SWT.NONE );
		{
			grpLegendArea.setLayout( new GridLayout( 2, false ) );
			grpLegendArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			grpLegendArea.setText( Messages.getString( "MoreOptionsChartLegendSheet.Label.LegendArea" ) ); //$NON-NLS-1$
		}

		Composite cmpLegLeft = new Composite( grpLegendArea, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 2, false );
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			cmpLegLeft.setLayout( gl );
			cmpLegLeft.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
					| GridData.VERTICAL_ALIGN_BEGINNING ) );

			getComponentLegendLeftArea( cmpLegLeft );
		}

		Composite cmpLegRight = new Composite( grpLegendArea, SWT.NONE );
		{
			GridLayout gl = new GridLayout( );
			gl.marginHeight = 0;
			gl.marginWidth = 0;
			gl.verticalSpacing = 10;
			cmpLegRight.setLayout( gl );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.verticalAlignment = SWT.BEGINNING;
			cmpLegRight.setLayoutData( gd );

			getComponentLegendRightArea( cmpLegRight );
		}

		populateLists( );

		return cmpContent;
	}

	private void getComponentLegendLeftArea( Composite cmpLegLeft )
	{
		btnVisible = new Button( cmpLegLeft, SWT.CHECK );
		GridData gdBTNVisible = new GridData( );
		gdBTNVisible.horizontalSpan = 2;
		btnVisible.setLayoutData( gdBTNVisible );
		btnVisible.setText( Messages.getString( "MoreOptionsChartLegendSheet.Label.Visible" ) );//$NON-NLS-1$
		btnVisible.setSelection( bEnableUI );
		btnVisible.addSelectionListener( this );

		lblOrientation = new Label( cmpLegLeft, SWT.NONE );
		lblOrientation.setText( Messages.getString( "BlockAttributeComposite.Lbl.Orientation" ) ); //$NON-NLS-1$

		cmbOrientation = new Combo( cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBOrientation = new GridData( GridData.FILL_HORIZONTAL );
		cmbOrientation.setLayoutData( gdCMBOrientation );
		cmbOrientation.addSelectionListener( this );
		cmbOrientation.setEnabled( bEnableUI );

		lblPosition = new Label( cmpLegLeft, SWT.NONE );
		lblPosition.setText( Messages.getString( "BlockAttributeComposite.Lbl.Position" ) ); //$NON-NLS-1$
		lblPosition.setEnabled( bEnableUI );

		cmbPosition = new Combo( cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBPosition = new GridData( GridData.FILL_HORIZONTAL );
		cmbPosition.setLayoutData( gdCMBPosition );
		cmbPosition.addSelectionListener( this );
		cmbPosition.setEnabled( bEnableUI );

		lblAnchor = new Label( cmpLegLeft, SWT.NONE );
		lblAnchor.setText( Messages.getString( "BlockAttributeComposite.Lbl.Anchor" ) ); //$NON-NLS-1$
		lblAnchor.setEnabled( bEnableUI );

		cmbAnchor = new Combo( cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCBAnchor = new GridData( GridData.FILL_HORIZONTAL );
		cmbAnchor.setLayoutData( gdCBAnchor );
		cmbAnchor.addSelectionListener( this );
		cmbAnchor.setEnabled( bEnableUI );

		lblStretch = new Label( cmpLegLeft, SWT.NONE );
		lblStretch.setText( Messages.getString( "BlockAttributeComposite.Lbl.Stretch" ) ); //$NON-NLS-1$
		lblStretch.setEnabled( bEnableUI );

		cmbStretch = new Combo( cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCBStretch = new GridData( GridData.FILL_HORIZONTAL );
		cmbStretch.setLayoutData( gdCBStretch );
		cmbStretch.addSelectionListener( this );
		cmbStretch.setEnabled( bEnableUI );

		lblBackground = new Label( cmpLegLeft, SWT.NONE );
		lblBackground.setText( Messages.getString( "BlockAttributeComposite.Lbl.Background" ) ); //$NON-NLS-1$
		lblBackground.setEnabled( bEnableUI );

		fccBackground = new FillChooserComposite( cmpLegLeft,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getBackground( ),
				true,
				true );
		GridData gdFCCBackground = new GridData( GridData.FILL_HORIZONTAL );
		fccBackground.setLayoutData( gdFCCBackground );
		fccBackground.addListener( this );
		fccBackground.setEnabled( bEnableUI );

		lblDirection = new Label( cmpLegLeft, SWT.NONE );
		lblDirection.setText( Messages.getString( "BlockAttributeComposite.Lbl.Direction" ) ); //$NON-NLS-1$
		lblDirection.setEnabled( bEnableUI );

		cmbDirection = new Combo( cmpLegLeft, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBDirection = new GridData( GridData.FILL_HORIZONTAL );
		cmbDirection.setLayoutData( gdCMBDirection );
		cmbDirection.addSelectionListener( this );
		cmbDirection.setEnabled( bEnableUI );

		lblWrapping = new Label( cmpLegLeft, SWT.NONE );
		lblWrapping.setText( Messages.getString( "LegendLayoutSheet.Label.WrappingWidth" ) ); //$NON-NLS-1$
		lblWrapping.setEnabled( bEnableUI );

		txtWrapping = new TextEditorComposite( cmpLegLeft, SWT.BORDER
				| SWT.SINGLE, true );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			txtWrapping.setLayoutData( gd );
			txtWrapping.setText( String.valueOf( getBlockForProcessing( ).getWrappingSize( ) ) );
			txtWrapping.addListener( this );
		}

		// lblVerticalSpacing = new Label( cmpLegLeft, SWT.NONE );
		// lblVerticalSpacing.setText( Messages.getString(
		// "BlockAttributeComposite.Lbl.VerticalSpacing" ) ); //$NON-NLS-1$
		//
		// iscVSpacing = new IntegerSpinControl( cmpLegLeft,
		// SWT.NONE,
		// getBlockForProcessing( ).getVerticalSpacing( ) );
		// GridData gdISCVSpacing = new GridData( GridData.FILL_HORIZONTAL );
		// iscVSpacing.setLayoutData( gdISCVSpacing );
		// iscVSpacing.addListener( this );
		//
		// lblHorizontalSpacing = new Label( cmpLegLeft, SWT.NONE );
		// lblHorizontalSpacing.setText( Messages.getString(
		// "BlockAttributeComposite.Lbl.HorizontalSpacing" ) ); //$NON-NLS-1$
		//
		// iscHSpacing = new IntegerSpinControl( cmpLegLeft,
		// SWT.NONE,
		// getBlockForProcessing( ).getHorizontalSpacing( ) );
		// GridData gdISCHSpacing = new GridData( GridData.FILL_HORIZONTAL );
		// iscHSpacing.setLayoutData( gdISCHSpacing );
		// iscHSpacing.addListener( this );
	}

	private void getComponentLegendRightArea( Composite cmpLegRight )
	{
		Group grpOutline = new Group( cmpLegRight, SWT.NONE );
		{
			GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
			grpOutline.setLayoutData( gdGRPOutline );
			grpOutline.setLayout( new FillLayout( ) );
			grpOutline.setText( Messages.getString( "MoreOptionsChartLegendSheet.Label.Outline" ) ); //$NON-NLS-1$
		}

		outlineLegend = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getOutline( ),
				true,
				true,
				true );
		{
			outlineLegend.addListener( this );
			outlineLegend.setEnabled( bEnableUI );
		}

		icLegend = new InsetsComposite( cmpLegRight,
				SWT.NONE,
				getBlockForProcessing( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		GridData gdICBlock = new GridData( GridData.FILL_HORIZONTAL );
		icLegend.setLayoutData( gdICBlock );
		icLegend.addListener( this );
	}

	private void populateLists( )
	{
		// Set block Anchor property
		NameSet ns = LiteralHelper.anchorSet;
		cmbAnchor.setItems( ns.getDisplayNames( ) );
		cmbAnchor.select( ns.getSafeNameIndex( getBlockForProcessing( ).getAnchor( )
				.getName( ) ) );

		// Set the block Stretch property
		ns = LiteralHelper.stretchSet;
		cmbStretch.setItems( ns.getDisplayNames( ) );
		cmbStretch.select( ns.getSafeNameIndex( getBlockForProcessing( ).getStretch( )
				.getName( ) ) );

		// Set Legend Orientation property
		ns = LiteralHelper.orientationSet;
		cmbOrientation.setItems( ns.getDisplayNames( ) );
		cmbOrientation.select( ns.getSafeNameIndex( getBlockForProcessing( ).getOrientation( )
				.getName( ) ) );

		// Set Legend Direction property
		ns = LiteralHelper.directionSet;
		cmbDirection.setItems( ns.getDisplayNames( ) );
		cmbDirection.select( ns.getSafeNameIndex( getBlockForProcessing( ).getDirection( )
				.getName( ) ) );

		// Set Legend Position property
		ns = LiteralHelper.fullPositionSet;
		cmbPosition.setItems( ns.getDisplayNames( ) );
		cmbPosition.select( ns.getSafeNameIndex( getBlockForProcessing( ).getPosition( )
				.getName( ) ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( fccBackground ) )
		{
			getBlockForProcessing( ).setBackground( (Fill) event.data );
		}
		else if ( event.widget.equals( outlineLegend ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getBlockForProcessing( ).getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if ( event.widget.equals( icLegend ) )
		{
			getBlockForProcessing( ).setInsets( (Insets) event.data );
		}
		else if ( event.widget.equals( txtWrapping ) )
		{
			String text = (String) event.data;
			double wrappingSize = 0;
			try
			{
				if ( text.trim( ).length( ) > 0 )
				{
					wrappingSize = Double.parseDouble( text );
				}
			}
			catch ( NumberFormatException ex )
			{
				// Do nothing
			}
			getBlockForProcessing( ).setWrappingSize( wrappingSize );
		}
		// else if ( event.widget.equals( iscHSpacing ) )
		// {
		// getBlockForProcessing( ).setHorizontalSpacing( ( (Integer) event.data
		// ).intValue( ) );
		// }
		// else if ( event.widget.equals( iscVSpacing ) )
		// {
		// getBlockForProcessing( ).setVerticalSpacing( ( (Integer) event.data
		// ).intValue( ) );
		// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Object oSource = e.getSource( );
		if ( oSource.equals( cmbAnchor ) )
		{
			getBlockForProcessing( ).setAnchor( Anchor.getByName( LiteralHelper.anchorSet.getNameByDisplayName( cmbAnchor.getText( ) ) ) );
		}
		else if ( oSource.equals( cmbStretch ) )
		{
			getBlockForProcessing( ).setStretch( Stretch.getByName( LiteralHelper.stretchSet.getNameByDisplayName( cmbStretch.getText( ) ) ) );
		}
		else if ( oSource.equals( btnVisible ) )
		{
			getBlockForProcessing( ).setVisible( btnVisible.getSelection( ) );
			boolean bEnableUI = btnVisible.getSelection( );
			lblAnchor.setEnabled( bEnableUI );
			cmbAnchor.setEnabled( bEnableUI );
			lblStretch.setEnabled( bEnableUI );
			cmbStretch.setEnabled( bEnableUI );
			lblBackground.setEnabled( bEnableUI );
			fccBackground.setEnabled( bEnableUI );
			icLegend.setEnabled( bEnableUI );

			// lblHorizontalSpacing.setEnabled( bEnableUI );
			// iscHSpacing.setEnabled( bEnableUI );
			// lblVerticalSpacing.setEnabled( bEnableUI );
			// iscVSpacing.setEnabled( bEnableUI );

			lblOrientation.setEnabled( bEnableUI );
			cmbOrientation.setEnabled( bEnableUI );
			lblDirection.setEnabled( bEnableUI );
			cmbDirection.setEnabled( bEnableUI );
			lblPosition.setEnabled( bEnableUI );
			cmbPosition.setEnabled( bEnableUI );
			outlineLegend.setEnabled( bEnableUI );
		}
		else if ( oSource.equals( cmbOrientation ) )
		{
			getBlockForProcessing( ).setOrientation( Orientation.getByName( LiteralHelper.orientationSet.getNameByDisplayName( cmbOrientation.getText( ) ) ) );
		}
		else if ( oSource.equals( cmbDirection ) )
		{
			getBlockForProcessing( ).setDirection( Direction.getByName( LiteralHelper.directionSet.getNameByDisplayName( cmbDirection.getText( ) ) ) );
		}
		else if ( oSource.equals( cmbPosition ) )
		{
			getBlockForProcessing( ).setPosition( Position.getByName( LiteralHelper.fullPositionSet.getNameByDisplayName( cmbPosition.getText( ) ) ) );
		}
	}

	private Legend getBlockForProcessing( )
	{
		return getChart( ).getLegend( );
	}
}