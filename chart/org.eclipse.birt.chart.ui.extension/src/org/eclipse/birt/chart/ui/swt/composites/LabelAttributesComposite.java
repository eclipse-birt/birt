/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.Vector;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
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
 * @author Actuate Corporation
 * 
 */
public class LabelAttributesComposite extends Composite implements
		SelectionListener,
		Listener
{

	private transient Composite cmpGeneral = null;

	private transient Composite grpAttributes = null;

	private transient Group grpOutline = null;

	private transient Button cbVisible = null;

	private transient Label lblLabel = null;

	private transient Label lblPosition = null;

	private transient Label lblFill = null;

	private transient Label lblShadow = null;

	private transient Label lblFont = null;

	private transient Combo cmbPosition = null;

	private transient FontDefinitionComposite fdcFont = null;

	private transient FillChooserComposite fccBackground = null;

	private transient FillChooserComposite fccShadow = null;

	private transient InsetsComposite icInsets = null;

	private transient String sGroupName = Messages.getString( "LabelAttributesComposite.Lbl.Label" ); //$NON-NLS-1$

	private transient Position lpCurrent = null;

	private transient Fill fBackground = null;

	private transient ColorDefinition cdShadow = null;

	private transient FontDefinition fdCurrent = null;

	private transient ColorDefinition cdFont = null;

	private transient LineAttributes laCurrent = null;

	private transient org.eclipse.birt.chart.model.component.Label lblCurrent = null;

	private transient Insets insets = null;

	private transient String sUnits = null;

	private transient LineAttributesComposite liacOutline = null;

	private transient ExternalizedTextEditorComposite txtLabel = null;

	private transient Vector vListeners = null;

	public static final int VISIBILITY_CHANGED_EVENT = 1;

	public static final int POSITION_CHANGED_EVENT = 2;

	public static final int FONT_CHANGED_EVENT = 3;

	public static final int BACKGROUND_CHANGED_EVENT = 4;

	public static final int SHADOW_CHANGED_EVENT = 5;

	public static final int OUTLINE_STYLE_CHANGED_EVENT = 6;

	public static final int OUTLINE_WIDTH_CHANGED_EVENT = 7;

	public static final int OUTLINE_COLOR_CHANGED_EVENT = 8;

	public static final int OUTLINE_VISIBILITY_CHANGED_EVENT = 9;

	public static final int INSETS_CHANGED_EVENT = 10;

	public static final int LABEL_CHANGED_EVENT = 11;

	public static final int ALLOW_ALL_POSITION = ChartUIConstants.ALLOW_ALL_POSITION;

	public static final int ALLOW_VERTICAL_POSITION = ChartUIConstants.ALLOW_VERTICAL_POSITION;

	public static final int ALLOW_HORIZONTAL_POSITION = ChartUIConstants.ALLOW_HORIZONTAL_POSITION;

	public static final int ALLOW_INOUT_POSITION = ChartUIConstants.ALLOW_INOUT_POSITION;

	private transient boolean bEnabled = true;

	private int positionScope = 0;

	private transient ChartWizardContext wizardContext;

	private transient LabelAttributesContext attributesContext;

	/**
	 * 
	 * UI context for LabelAttributesComposite. Note that
	 * {@link #isLabelEnabled} default value is false, others are all true.
	 * 
	 */
	public static class LabelAttributesContext
	{

		public boolean isLabelEnabled = false;
		public boolean isPositionEnabled = true;
		public boolean isVisibilityEnabled = true;
		public boolean isFontAlignmentEnabled = true;
		public boolean isFontEnabled = true;
		public boolean isInsetsEnabled = true;
		public boolean isShadowEnabled = true;
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param wizardContext
	 * @param attributesContext
	 * @param sGroupName
	 * @param lpCurrent
	 * @param lblCurrent
	 * @param sUnits
	 * @since 2.1.1
	 */
	public LabelAttributesComposite( Composite parent, int style,
			ChartWizardContext wizardContext,
			LabelAttributesContext attributesContext, String sGroupName,
			Position lpCurrent,
			org.eclipse.birt.chart.model.component.Label lblCurrent,
			String sUnits )
	{
		this( parent,
				style,
				wizardContext,
				attributesContext,
				sGroupName,
				lpCurrent,
				lblCurrent,
				sUnits,
				ALLOW_ALL_POSITION );
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param wizardContext
	 * @param attributesContext
	 * @param sGroupName
	 * @param lpCurrent
	 * @param lblCurrent
	 * @param sUnits
	 * @param positionScope
	 * @since 2.1.1
	 */
	public LabelAttributesComposite( Composite parent, int style,
			ChartWizardContext wizardContext,
			LabelAttributesContext attributesContext, String sGroupName,
			Position lpCurrent,
			org.eclipse.birt.chart.model.component.Label lblCurrent,
			String sUnits, int positionScope )
	{
		super( parent, style );
		this.wizardContext = wizardContext;
		this.attributesContext = attributesContext;
		this.sGroupName = sGroupName;
		this.lpCurrent = lpCurrent;
		this.lblCurrent = lblCurrent;
		this.sUnits = sUnits;
		this.fdCurrent = lblCurrent.getCaption( ).getFont( );
		this.cdFont = lblCurrent.getCaption( ).getColor( );
		this.fBackground = lblCurrent.getBackground( );
		this.cdShadow = lblCurrent.getShadowColor( );
		this.laCurrent = lblCurrent.getOutline( );
		this.insets = lblCurrent.getInsets( );
		this.positionScope = positionScope;

		init( );
		placeComponents( );
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param sGroupName
	 * @param lpCurrent
	 * @param lblCurrent
	 * @param sUnits
	 * @param bPositionEnabled
	 * @param bVisibilityEnabled
	 * @param serviceprovider
	 * @param isAlignmentEnabled
	 * @since 2.1
	 * @deprecated To use
	 *             {@link #LabelAttributesComposite(Composite, int, ChartWizardContext, org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext, String, Position, org.eclipse.birt.chart.model.component.Label, String)}
	 */
	public LabelAttributesComposite( Composite parent, int style,
			String sGroupName, Position lpCurrent,
			org.eclipse.birt.chart.model.component.Label lblCurrent,
			String sUnits, boolean bPositionEnabled,
			boolean bVisibilityEnabled, ChartWizardContext wizardContext,
			boolean isAlignmentEnabled )
	{
		this( parent,
				style,
				sGroupName,
				lpCurrent,
				lblCurrent,
				sUnits,
				bPositionEnabled,
				bVisibilityEnabled,
				wizardContext,
				ALLOW_ALL_POSITION,
				isAlignmentEnabled );
	}

	/**
	 * 
	 * @param parent
	 * @param style
	 * @param sGroupName
	 * @param lpCurrent
	 * @param lblCurrent
	 * @param sUnits
	 * @param bPositionEnabled
	 * @param bVisibilityEnabled
	 * @param wizardContext
	 * @param positionScope
	 * @param isAlignmentEnabled
	 * @deprecated To use
	 *             {@link #LabelAttributesComposite(Composite, int, ChartWizardContext, org.eclipse.birt.chart.ui.swt.composites.LabelAttributesComposite.LabelAttributesContext, String, Position, org.eclipse.birt.chart.model.component.Label, String, int)}
	 */
	public LabelAttributesComposite( Composite parent, int style,
			String sGroupName, Position lpCurrent,
			org.eclipse.birt.chart.model.component.Label lblCurrent,
			String sUnits, boolean bPositionEnabled,
			boolean bVisibilityEnabled, ChartWizardContext wizardContext,
			int positionScope, boolean isAlignmentEnabled )
	{
		super( parent, style );
		this.sGroupName = sGroupName;
		this.lpCurrent = lpCurrent;
		this.lblCurrent = lblCurrent;
		this.sUnits = sUnits;
		this.fdCurrent = lblCurrent.getCaption( ).getFont( );
		this.cdFont = lblCurrent.getCaption( ).getColor( );
		this.fBackground = lblCurrent.getBackground( );
		this.cdShadow = lblCurrent.getShadowColor( );
		this.laCurrent = lblCurrent.getOutline( );
		this.insets = lblCurrent.getInsets( );
		this.wizardContext = wizardContext;
		this.positionScope = positionScope;

		attributesContext = new LabelAttributesContext( );
		attributesContext.isPositionEnabled = bPositionEnabled;
		attributesContext.isVisibilityEnabled = bVisibilityEnabled;
		attributesContext.isFontAlignmentEnabled = isAlignmentEnabled;

		init( );
		placeComponents( );
	}

	/**
	 * 
	 */
	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
		vListeners = new Vector( );
	}

	/**
	 * 
	 */
	private void placeComponents( )
	{
		FillLayout flMain = new FillLayout( );
		flMain.marginHeight = 0;
		flMain.marginWidth = 0;

		GridLayout glAttributes = new GridLayout( );
		glAttributes.horizontalSpacing = 5;
		glAttributes.verticalSpacing = 5;
		glAttributes.marginHeight = 7;
		glAttributes.marginWidth = 7;

		GridLayout glGeneral = new GridLayout( );
		glGeneral.numColumns = 2;
		glGeneral.horizontalSpacing = 5;
		glGeneral.verticalSpacing = 5;

		FillLayout flOutline = new FillLayout( );

		this.setLayout( flMain );

		if ( sGroupName == null || sGroupName.length( ) == 0 )
		{
			grpAttributes = new Composite( this, SWT.NONE );
		}
		else
		{
			grpAttributes = new Group( this, SWT.NONE );
			( (Group) grpAttributes ).setText( sGroupName );
		}
		grpAttributes.setLayout( glAttributes );

		cmpGeneral = new Composite( grpAttributes, SWT.NONE );
		GridData gdCMPGeneral = new GridData( GridData.FILL_HORIZONTAL );
		cmpGeneral.setLayoutData( gdCMPGeneral );
		cmpGeneral.setLayout( glGeneral );

		boolean bEnableUI = bEnabled && this.lblCurrent.isVisible( );
		if ( attributesContext.isVisibilityEnabled )
		{
			cbVisible = new Button( cmpGeneral, SWT.CHECK );
			GridData gdCBVisible = new GridData( GridData.FILL_HORIZONTAL );
			gdCBVisible.horizontalSpan = 2;
			cbVisible.setLayoutData( gdCBVisible );
			cbVisible.setSelection( this.lblCurrent.isVisible( ) );
			cbVisible.setText( Messages.getString( "LabelAttributesComposite.Lbl.IsVisible" ) ); //$NON-NLS-1$
			cbVisible.addSelectionListener( this );
			if ( bEnabled )
			{
				bEnableUI = cbVisible.getSelection( );
			}
		}

		if ( attributesContext.isLabelEnabled )
		{
			lblLabel = new Label( cmpGeneral, SWT.NONE );
			{
				GridData gd = new GridData( );
				lblLabel.setLayoutData( gd );
				lblLabel.setText( Messages.getString( "AxisMarkersSheet.Label.Label" ) ); //$NON-NLS-1$
			}

			txtLabel = new ExternalizedTextEditorComposite( cmpGeneral,
					SWT.BORDER | SWT.SINGLE,
					-1,
					-1,
					wizardContext.getUIServiceProvider( ).getRegisteredKeys( ),
					wizardContext.getUIServiceProvider( ),
					"" ); //$NON-NLS-1$
			{
				GridData gd = new GridData( GridData.FILL_BOTH );
				txtLabel.setLayoutData( gd );
				txtLabel.addListener( this );
			}
		}

		if ( attributesContext.isPositionEnabled )
		{
			lblPosition = new Label( cmpGeneral, SWT.NONE );
			GridData gdLBLPosition = new GridData( );
			lblPosition.setLayoutData( gdLBLPosition );
			lblPosition.setText( Messages.getString( "LabelAttributesComposite.Lbl.Position" ) ); //$NON-NLS-1$
			lblPosition.setEnabled( bEnableUI );

			cmbPosition = new Combo( cmpGeneral, SWT.DROP_DOWN | SWT.READ_ONLY );
			GridData gdCMBPosition = new GridData( GridData.FILL_BOTH );
			cmbPosition.setLayoutData( gdCMBPosition );
			cmbPosition.addSelectionListener( this );
			cmbPosition.setEnabled( bEnableUI );
		}

		if ( attributesContext.isFontEnabled )
		{
			lblFont = new Label( cmpGeneral, SWT.NONE );
			GridData gdLFont = new GridData( );
			lblFont.setLayoutData( gdLFont );
			lblFont.setText( Messages.getString( "LabelAttributesComposite.Lbl.Font" ) ); //$NON-NLS-1$
			lblFont.setEnabled( bEnableUI );

			fdcFont = new FontDefinitionComposite( cmpGeneral,
					SWT.NONE,
					wizardContext,
					this.fdCurrent,
					this.cdFont,
					attributesContext.isFontAlignmentEnabled );
			GridData gdFDCFont = new GridData( GridData.FILL_BOTH );
			// gdFDCFont.heightHint = fdcFont.getPreferredSize( ).y;
			gdFDCFont.widthHint = fdcFont.getPreferredSize( ).x;
			gdFDCFont.grabExcessVerticalSpace = false;
			fdcFont.setLayoutData( gdFDCFont );
			fdcFont.addListener( this );
			fdcFont.setEnabled( bEnableUI );
		}

		lblFill = new Label( cmpGeneral, SWT.NONE );
		GridData gdLFill = new GridData( );
		lblFill.setLayoutData( gdLFill );
		lblFill.setText( Messages.getString( "LabelAttributesComposite.Lbl.Background" ) ); //$NON-NLS-1$
		lblFill.setEnabled( bEnableUI );

		fccBackground = new FillChooserComposite( cmpGeneral,
				SWT.NONE,
				wizardContext,
				fBackground,
				false,
				false );
		GridData gdFCCBackground = new GridData( GridData.FILL_BOTH );
		gdFCCBackground.heightHint = fccBackground.getPreferredSize( ).y;
		fccBackground.setLayoutData( gdFCCBackground );
		fccBackground.addListener( this );
		fccBackground.setEnabled( bEnableUI );

		if ( attributesContext.isShadowEnabled )
		{
			lblShadow = new Label( cmpGeneral, SWT.NONE );
			GridData gdLBLShadow = new GridData( );
			lblShadow.setLayoutData( gdLBLShadow );
			lblShadow.setText( Messages.getString( "LabelAttributesComposite.Lbl.Shadow" ) ); //$NON-NLS-1$
			lblShadow.setEnabled( bEnableUI );

			fccShadow = new FillChooserComposite( cmpGeneral,
					SWT.NONE,
					wizardContext,
					cdShadow,
					false,
					false );
			GridData gdFCCShadow = new GridData( GridData.FILL_BOTH );
			fccShadow.setLayoutData( gdFCCShadow );
			fccShadow.addListener( this );
			fccShadow.setEnabled( bEnableUI );
		}

		grpOutline = new Group( grpAttributes, SWT.NONE );
		GridData gdGOutline = new GridData( GridData.FILL_HORIZONTAL );
		gdGOutline.heightHint = 110;
		grpOutline.setLayoutData( gdGOutline );
		grpOutline.setText( Messages.getString( "LabelAttributesComposite.Lbl.Outline" ) ); //$NON-NLS-1$
		grpOutline.setLayout( flOutline );
		grpOutline.setEnabled( bEnableUI );

		liacOutline = new LineAttributesComposite( grpOutline,
				SWT.NONE,
				wizardContext,
				laCurrent,
				true,
				true,
				true );
		liacOutline.addListener( this );
		liacOutline.setAttributesEnabled( bEnableUI );

		if ( attributesContext.isInsetsEnabled )
		{
			icInsets = new InsetsComposite( grpAttributes,
					SWT.NONE,
					insets,
					sUnits,
					wizardContext.getUIServiceProvider( ) );
			GridData gdICInsets = new GridData( GridData.FILL_HORIZONTAL );
			gdICInsets.heightHint = icInsets.getPreferredSize( ).y;
			gdICInsets.grabExcessVerticalSpace = false;
			icInsets.addListener( this );
			icInsets.setLayoutData( gdICInsets );
			icInsets.setEnabled( bEnableUI );
		}

		populateLists( );
	}

	public void setEnabled( boolean bState )
	{
		boolean bEnableUI = true;
		if ( attributesContext.isVisibilityEnabled )
		{
			bEnableUI = cbVisible.getSelection( );
			cbVisible.setEnabled( bState );
		}

		setVisibleState( bState & bEnableUI );
		grpAttributes.setEnabled( bState );

		this.bEnabled = bState;
	}

	public boolean isEnabled( )
	{
		return this.bEnabled;
	}

	private void populateLists( )
	{
		if ( attributesContext.isPositionEnabled )
		{
			cmbPosition.setItems( ChartUIUtil.getPositionDisplayNames( positionScope,
					( isAxisAttribute( ) || isSeriesAttribute( ) )
							&& isFlippedAxes( ) ) );
			if ( lpCurrent != null )
			{
				String positionName = ChartUIUtil.getFlippedPosition( lpCurrent,
						( isAxisAttribute( ) || isSeriesAttribute( ) )
								&& isFlippedAxes( ) )
						.getName( );
				for ( int i = 0; i < cmbPosition.getItemCount( ); i++ )
				{
					if ( positionName.equals( LiteralHelper.fullPositionSet.getNameByDisplayName( cmbPosition.getItem( i ) ) ) )
					{
						cmbPosition.select( i );
					}
				}
			}
		}
	}

	public void setLabel( org.eclipse.birt.chart.model.component.Label lbl,
			String sUnits )
	{
		this.lblCurrent = lbl;
		this.sUnits = sUnits;
		this.fBackground = lblCurrent.getBackground( );
		this.laCurrent = lblCurrent.getOutline( );

		// update the UI
		if ( attributesContext.isVisibilityEnabled )
		{
			this.cbVisible.setSelection( lblCurrent.isVisible( ) );
			setVisibleState( cbVisible.getSelection( ) && cbVisible.isEnabled( ) );
		}

		if ( attributesContext.isLabelEnabled )
		{
			this.txtLabel.setText( lbl.getCaption( ).getValue( ) );
		}

		if ( attributesContext.isInsetsEnabled )
		{
			this.insets = lblCurrent.getInsets( );
			this.icInsets.setInsets( insets, this.sUnits );
		}

		if ( attributesContext.isShadowEnabled )
		{
			this.cdShadow = lblCurrent.getShadowColor( );
			this.fccShadow.setFill( cdShadow );
		}

		if ( attributesContext.isFontEnabled )
		{
			this.fdCurrent = lblCurrent.getCaption( ).getFont( );
			this.cdFont = lblCurrent.getCaption( ).getColor( );
			this.fdcFont.setFontDefinition( fdCurrent );
			this.fdcFont.setFontColor( cdFont );
		}

		this.fccBackground.setFill( fBackground );
		this.liacOutline.setLineAttributes( laCurrent );

		redraw( );

	}

	public void setLabelPosition( Position pos )
	{
		this.lpCurrent = pos;
		if ( attributesContext.isPositionEnabled )
		{
			if ( isAxisAttribute( ) || isSeriesAttribute( ) )
			{
				this.cmbPosition.setText( LiteralHelper.fullPositionSet.getDisplayNameByName( ChartUIUtil.getFlippedPosition( lpCurrent,
						isFlippedAxes( ) )
						.getName( ) ) );
			}
			else
			{
				this.cmbPosition.setText( LiteralHelper.fullPositionSet.getDisplayNameByName( lpCurrent.getName( ) ) );
			}
		}
	}

	public void addListener( Listener listener )
	{
		vListeners.add( listener );
	}

	private void fireEvent( Event e )
	{
		for ( int iL = 0; iL < vListeners.size( ); iL++ )
		{
			( (Listener) vListeners.get( iL ) ).handleEvent( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		Event eLabel = new Event( );
		eLabel.widget = this;
		if ( e.getSource( ).equals( cmbPosition ) )
		{
			if ( isAxisAttribute( ) || isSeriesAttribute( ) )
			{
				eLabel.data = ChartUIUtil.getFlippedPosition( Position.getByName( LiteralHelper.fullPositionSet.getNameByDisplayName( cmbPosition.getText( ) ) ),
						isFlippedAxes( ) );
			}
			else
			{
				eLabel.data = Position.getByName( LiteralHelper.fullPositionSet.getNameByDisplayName( cmbPosition.getText( ) ) );
			}
			eLabel.type = POSITION_CHANGED_EVENT;
		}
		else if ( e.getSource( ).equals( cbVisible ) )
		{
			eLabel.data = Boolean.valueOf( cbVisible.getSelection( ) );
			eLabel.type = VISIBILITY_CHANGED_EVENT;

			setVisibleState( cbVisible.getSelection( ) );
		}
		fireEvent( eLabel );
	}

	private void setVisibleState( boolean isVisible )
	{
		if ( attributesContext.isLabelEnabled )
		{
			lblLabel.setEnabled( isVisible );
			txtLabel.setEnabled( isVisible );
		}

		if ( attributesContext.isPositionEnabled )
		{
			lblPosition.setEnabled( isVisible );
			cmbPosition.setEnabled( isVisible );
		}
		lblFill.setEnabled( isVisible );
		if ( attributesContext.isFontEnabled )
		{
			lblFont.setEnabled( isVisible );
			fdcFont.setEnabled( isVisible );
		}
		fccBackground.setEnabled( isVisible );

		if ( attributesContext.isShadowEnabled )
		{
			lblShadow.setEnabled( isVisible );
			fccShadow.setEnabled( isVisible );
		}
		if ( attributesContext.isInsetsEnabled )
		{
			icInsets.setEnabled( isVisible );
		}
		grpOutline.setEnabled( isVisible );
		liacOutline.setAttributesEnabled( isVisible );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetDefaultSelected( SelectionEvent e )
	{
	}

	public Point getPreferredSize( )
	{
		Point ptSize = new Point( 300, 130 );
		if ( attributesContext.isVisibilityEnabled )
		{
			ptSize.y += 30;
		}
		if ( attributesContext.isPositionEnabled )
		{
			ptSize.y += 30;
		}
		return ptSize;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		Event eLabel = new Event( );
		eLabel.widget = this;
		if ( event.widget.equals( fdcFont ) )
		{
			eLabel.type = FONT_CHANGED_EVENT;
		}
		else if ( event.widget.equals( liacOutline ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					eLabel.type = OUTLINE_STYLE_CHANGED_EVENT;
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					eLabel.type = OUTLINE_WIDTH_CHANGED_EVENT;
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					eLabel.type = OUTLINE_COLOR_CHANGED_EVENT;
					break;
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					eLabel.type = OUTLINE_VISIBILITY_CHANGED_EVENT;
					break;
			}
		}
		else if ( event.widget.equals( fccBackground ) )
		{
			eLabel.type = BACKGROUND_CHANGED_EVENT;
		}
		else if ( event.widget.equals( fccShadow ) )
		{
			eLabel.type = SHADOW_CHANGED_EVENT;
		}
		else if ( event.widget.equals( icInsets ) )
		{
			eLabel.type = INSETS_CHANGED_EVENT;
		}
		else if ( event.widget.equals( txtLabel ) )
		{
			eLabel.type = LABEL_CHANGED_EVENT;
		}
		eLabel.data = event.data;
		fireEvent( eLabel );
	}

	private boolean isFlippedAxes( )
	{
		return wizardContext.getModel( ) instanceof ChartWithAxes
				&& ( (ChartWithAxes) wizardContext.getModel( ) ).getOrientation( )
						.equals( Orientation.HORIZONTAL_LITERAL );
	}

	private boolean isAxisAttribute( )
	{
		return lblCurrent.eContainer( ) instanceof Axis;
	}

	private boolean isSeriesAttribute( )
	{
		return ( wizardContext.getModel( ) instanceof ChartWithAxes )
				&& ( lblCurrent.eContainer( ) instanceof Series );
	}
}