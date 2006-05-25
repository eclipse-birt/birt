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

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Stretch;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.InsetsComposite;
import org.eclipse.birt.chart.ui.swt.composites.IntegerSpinControl;
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
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

/**
 * 
 */

public class PlotClientAreaSheet extends AbstractPopupSheet
		implements
			Listener,
			SelectionListener
{

	private transient Composite cmpContent;

	private transient Combo cmbAnchor;

	private transient Combo cmbStretch;

	private transient LineAttributesComposite outlineIncluding;

	private transient LineAttributesComposite outlineWithin;

	private transient InsetsComposite icIncluding;

	private transient InsetsComposite icWithin;

	private transient IntegerSpinControl iscVSpacing;

	private transient IntegerSpinControl iscHSpacing;

	private transient TextEditorComposite txtHeight;

	private transient TextEditorComposite txtWidth;

	private transient FillChooserComposite fccShadow;

	public PlotClientAreaSheet( String title, ChartWizardContext context )
	{
		super( title, context, true );
	}

	protected Composite getComponent( Composite parent )
	{
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.POPUP_PLOT_AREA_FORMAT );
		
		cmpContent = new Composite( parent, SWT.NONE );
		cmpContent.setLayout( new GridLayout( ) );

		Group grpAreaIncluding = new Group( cmpContent, SWT.NONE );
		{
			grpAreaIncluding.setLayout( new GridLayout( 4, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			grpAreaIncluding.setLayoutData( gd );
			grpAreaIncluding.setText( getChart( ) instanceof ChartWithAxes
					? Messages.getString( "ChartPlotSheetImpl.Label.AreaIncludingAxes" ) : Messages.getString( "ChartPlotSheetImpl.Label.PlotArea" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Label lblAnchor = new Label( grpAreaIncluding, SWT.NONE );
		GridData gdLBLAnchor = new GridData( );
		lblAnchor.setLayoutData( gdLBLAnchor );
		lblAnchor.setText( Messages.getString( "MoreOptionsChartPlotSheet.Label.Anchor" ) ); //$NON-NLS-1$

		cmbAnchor = new Combo( grpAreaIncluding, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCBAnchor = new GridData( GridData.FILL_HORIZONTAL );
		cmbAnchor.setLayoutData( gdCBAnchor );
		cmbAnchor.addSelectionListener( this );

		Group grpOutline = new Group( grpAreaIncluding, SWT.NONE );
		GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPOutline.horizontalSpan = 2;
		gdGRPOutline.verticalSpan = 4;
		gdGRPOutline.widthHint = 150;
		grpOutline.setLayoutData( gdGRPOutline );
		grpOutline.setLayout( new FillLayout( ) );
		grpOutline.setText( Messages.getString( "MoreOptionsChartPlotSheet.Label.Outline" ) ); //$NON-NLS-1$

		outlineIncluding = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getOutline( ),
				true,
				true,
				false );
		outlineIncluding.addListener( this );

		Label lblStretch = new Label( grpAreaIncluding, SWT.NONE );
		{
			GridData gd = new GridData( );
			lblStretch.setLayoutData( gd );
			lblStretch.setText( Messages.getString( "MoreOptionsChartPlotSheet.Label.Stretch" ) ); //$NON-NLS-1$
		}

		cmbStretch = new Combo( grpAreaIncluding, SWT.DROP_DOWN | SWT.READ_ONLY );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			cmbStretch.setLayoutData( gd );
			cmbStretch.addSelectionListener( this );
		}

		Label lblVerticalSpacing = new Label( grpAreaIncluding, SWT.NONE );
		lblVerticalSpacing.setLayoutData( new GridData( ) );
		lblVerticalSpacing.setText( Messages.getString( "BlockAttributeComposite.Lbl.VerticalSpacing" ) ); //$NON-NLS-1$

		iscVSpacing = new IntegerSpinControl( grpAreaIncluding,
				SWT.NONE,
				getBlockForProcessing( ).getVerticalSpacing( ) );
		iscVSpacing.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		iscVSpacing.addListener( this );

		Label lblHorizontalSpacing = new Label( grpAreaIncluding, SWT.NONE );
		lblHorizontalSpacing.setLayoutData( new GridData( ) );
		lblHorizontalSpacing.setText( Messages.getString( "BlockAttributeComposite.Lbl.HorizontalSpacing" ) ); //$NON-NLS-1$

		iscHSpacing = new IntegerSpinControl( grpAreaIncluding,
				SWT.NONE,
				getBlockForProcessing( ).getHorizontalSpacing( ) );
		iscHSpacing.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		iscHSpacing.addListener( this );

		new Label( grpAreaIncluding, SWT.NONE ).setText( Messages.getString( "PlotClientAreaSheet.Label.HeightHint" ) ); //$NON-NLS-1$

		txtHeight = new TextEditorComposite( grpAreaIncluding, SWT.BORDER, true );
		{
			txtHeight.setDefaultValue( "-1" ); //$NON-NLS-1$
			txtHeight.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			txtHeight.setText( String.valueOf( getBlockForProcessing( ).getHeightHint( ) ) );
			txtHeight.addListener( this );
		}

		icIncluding = new InsetsComposite( grpAreaIncluding,
				SWT.NONE,
				getBlockForProcessing( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		GridData gdInsets = new GridData( GridData.FILL_HORIZONTAL );
		gdInsets.horizontalSpan = 2;
		gdInsets.verticalSpan = 3;
		icIncluding.setLayoutData( gdInsets );

		new Label( grpAreaIncluding, SWT.NONE ).setText( Messages.getString( "PlotClientAreaSheet.Label.WidthHint" ) ); //$NON-NLS-1$

		txtWidth = new TextEditorComposite( grpAreaIncluding, SWT.BORDER, true );
		{
			txtWidth.setDefaultValue( "-1" ); //$NON-NLS-1$
			txtWidth.setText( String.valueOf( getBlockForProcessing( ).getWidthHint( ) ) );
			txtWidth.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			txtWidth.addListener( this );
		}

		new Label( grpAreaIncluding, SWT.NONE );

		new Label( grpAreaIncluding, SWT.NONE );

		Group grpAreaWithin = new Group( cmpContent, SWT.NONE );
		{
			grpAreaWithin.setLayout( new GridLayout( 4, false ) );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			grpAreaWithin.setLayoutData( gd );
			grpAreaWithin.setText( getChart( ) instanceof ChartWithAxes
					? Messages.getString( "ChartPlotSheetImpl.Label.AreaWithinAxes" ) : Messages.getString( "ChartPlotSheetImpl.Label.ClientArea" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		createClientArea( grpAreaWithin );
		populateLists( );
		return cmpContent;
	}

	private void createClientArea( Group grpAreaWithin )
	{
		// WithinAxes area is not supported in 3D
		boolean isNot3D = getChart( ).getDimension( ).getValue( ) != ChartDimension.THREE_DIMENSIONAL;
		Label lblShadow = new Label( grpAreaWithin, SWT.NONE );
		{
			GridData gdLBLShadow = new GridData( );
			lblShadow.setLayoutData( gdLBLShadow );
			lblShadow.setText( Messages.getString( "ClientAreaAttributeComposite.Lbl.Shadow" ) ); //$NON-NLS-1$
			lblShadow.setEnabled( isNot3D );
		}

		fccShadow = new FillChooserComposite( grpAreaWithin,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getClientArea( ).getShadowColor( ),
				false,
				false );
		{
			GridData gdFCCShadow = new GridData( GridData.FILL_HORIZONTAL );
			fccShadow.setLayoutData( gdFCCShadow );
			fccShadow.addListener( this );
			fccShadow.setEnabled( isNot3D );
		}

		Group grpOutline = new Group( grpAreaWithin, SWT.NONE );
		{
			GridData gdGRPOutline = new GridData( GridData.FILL_HORIZONTAL );
			gdGRPOutline.horizontalSpan = 2;
			gdGRPOutline.verticalSpan = 2;
			grpOutline.setLayoutData( gdGRPOutline );
			grpOutline.setLayout( new FillLayout( ) );
			grpOutline.setText( Messages.getString( "MoreOptionsChartPlotSheet.Label.Outline" ) ); //$NON-NLS-1$
		}

		outlineWithin = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				getContext( ),
				getBlockForProcessing( ).getClientArea( ).getOutline( ),
				true,
				true,
				false );
		{
			outlineWithin.addListener( this );
		}

		icWithin = new InsetsComposite( grpAreaWithin,
				SWT.NONE,
				getBlockForProcessing( ).getClientArea( ).getInsets( ),
				getChart( ).getUnits( ),
				getContext( ).getUIServiceProvider( ) );
		{
			GridData gdInsets = new GridData( GridData.FILL_HORIZONTAL );
			gdInsets.horizontalSpan = 2;
			icWithin.setLayoutData( gdInsets );
			icWithin.setEnabled( isNot3D );
		}
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		if ( event.widget.equals( outlineIncluding ) )
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
		else if ( event.widget.equals( outlineWithin ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					getBlockForProcessing( ).getClientArea( )
							.getOutline( )
							.setStyle( (LineStyle) event.data );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					getBlockForProcessing( ).getClientArea( )
							.getOutline( )
							.setThickness( ( (Integer) event.data ).intValue( ) );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					getBlockForProcessing( ).getClientArea( )
							.getOutline( )
							.setColor( (ColorDefinition) event.data );
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					getBlockForProcessing( ).getClientArea( )
							.getOutline( )
							.setVisible( ( (Boolean) event.data ).booleanValue( ) );
					break;
			}
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			getBlockForProcessing( ).getClientArea( )
					.setShadowColor( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( iscHSpacing ) )
		{
			getBlockForProcessing( ).setHorizontalSpacing( ( (Integer) event.data ).intValue( ) );
		}
		else if ( event.widget.equals( iscVSpacing ) )
		{
			getBlockForProcessing( ).setVerticalSpacing( ( (Integer) event.data ).intValue( ) );
		}
		else if ( event.widget.equals( icIncluding ) )
		{
			getBlockForProcessing( ).setInsets( (Insets) event.data );
		}
		else if ( event.widget.equals( icWithin ) )
		{
			getBlockForProcessing( ).getClientArea( )
					.setInsets( (Insets) event.data );
		}
		else if ( event.widget.equals( txtHeight ) )
		{
			if ( event.type == TextEditorComposite.TEXT_MODIFIED )
			{
				getBlockForProcessing( ).setHeightHint( Double.parseDouble( (String) event.data ) );
			}
		}
		else if ( event.widget.equals( txtWidth ) )
		{
			if ( event.type == TextEditorComposite.TEXT_MODIFIED )
			{
				getBlockForProcessing( ).setWidthHint( Double.parseDouble( (String) event.data ) );
			}
		}
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
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.ISheet#getTitleText()
	 */
	public String getTitleText( )
	{
		return Messages.getString( "AttributeSheetImpl.Title.SheetTitle" ); //$NON-NLS-1$
	}

	private Plot getBlockForProcessing( )
	{
		return getChart( ).getPlot( );
	}
}