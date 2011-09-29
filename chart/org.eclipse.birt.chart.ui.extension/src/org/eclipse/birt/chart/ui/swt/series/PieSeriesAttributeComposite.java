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

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.composites.FillChooserComposite;
import org.eclipse.birt.chart.ui.swt.composites.LineAttributesComposite;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.chart.util.NameSet;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
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
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.Spinner;

/**
 * UI composite for Pie series attributes
 * 
 */
public class PieSeriesAttributeComposite extends Composite implements
		Listener,
		SelectionListener
{

	private Group grpLeaderLine = null;

	private FillChooserComposite fccSliceOutline = null;

	private Combo cmbLeaderLine = null;

	private Spinner iscLeaderLength = null;

	private LineAttributesComposite liacLeaderLine = null;

	private PieSeries series = null;

	private static final int MAX_LEADER_LENGTH = 200;

	private ChartWizardContext context;

	private TextEditorComposite txtExplode;
	private Button btnBuilder;

	private Label lblExpSliWhen;
	private Label lblExpDistance;
	private Label lblRatio;
	private Label lblRotation;
	private Spinner iscExplosion;

	private Slider sRatio;
	private Slider sRotation;

	private Button btnLeaderLengthAuto;

	private Button btnRatioAuto;

	private Button btnRotationAuto;

	private Combo cmbDirection;

	private Button btnExplosionAuto;
	
	private final static String TOOLTIP_EXPLODE_SLICE_WHEN = Messages.getString( "PieBottomAreaComponent.Label.TheExplosionCondition" ); //$NON-NLS-1$
	private final static String TOOLTIP_EXPLOSION_DISTANCE = Messages.getString( "PieBottomAreaComponent.Label.TheAmplitudeOfTheExplosion" ); //$NON-NLS-1$
	private final static String TOOLTIP_RATIO = Messages.getString( "PieBottomAreaComponent.Label.TheRatioOfTheChart" ); //$NON-NLS-1$
	private final static String TOOLTIP_ROTATION = Messages.getString("PiesBottomAreaComponent.Label.TheRotationOfTheChart"); //$NON-NLS-1$

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui.extension/swt.series" ); //$NON-NLS-1$

	/**
	 * @param parent
	 * @param style
	 */
	public PieSeriesAttributeComposite( Composite parent, int style,
			Series series, ChartWizardContext context )
	{
		super( parent, style );
		this.context = context;
		if ( !( series instanceof PieSeries ) )
		{
			try
			{
				throw new ChartException( ChartUIExtensionPlugin.ID,
						ChartException.VALIDATION,
						"PieSeriesAttributeComposite.Exception.IllegalArgument", new Object[]{series.getClass( ).getName( )}, Messages.getResourceBundle( ) ); //$NON-NLS-1$
			}
			catch ( ChartException e )
			{
				logger.log( e );
				e.printStackTrace( );
			}
		}
		this.series = (PieSeries) series;
		init( );
		placeComponents( );
		ChartUIUtil.bindHelp( parent, ChartHelpContextIds.SUBTASK_YSERIES_PIE );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		{
			// Layout for content composite
			GridLayout glContent = new GridLayout( );
			glContent.numColumns = 2;
			glContent.marginHeight = 2;
			glContent.marginWidth = 4;
			// Main content composite
			this.setLayout( glContent );
		}

		// LeaderLine group
		grpLeaderLine = new Group( this, SWT.NONE );
		{
			GridData gdGRPLeaderLine = new GridData( GridData.FILL_BOTH );
			grpLeaderLine.setLayoutData( gdGRPLeaderLine );
			// Layout for content composite
			GridLayout glLeaderLine = new GridLayout( );
			glLeaderLine.numColumns = 2;
			glLeaderLine.marginHeight = 0;
			glLeaderLine.marginWidth = 2;
			glLeaderLine.verticalSpacing = 0;
			grpLeaderLine.setLayout( glLeaderLine );
			grpLeaderLine.setText( Messages.getString( "PieSeriesAttributeComposite.Lbl.LeaderLine" ) ); //$NON-NLS-1$
		}

		// LeaderLine Attributes composite
		liacLeaderLine = new LineAttributesComposite( grpLeaderLine,
				SWT.NONE,
				context,
				series.getLeaderLineAttributes( ),
				true,
				true,
				true,
				true,
				true );
		GridData gdLIACLeaderLine = new GridData( GridData.FILL_HORIZONTAL );
		gdLIACLeaderLine.horizontalSpan = 2;
		liacLeaderLine.setLayoutData( gdLIACLeaderLine );
		liacLeaderLine.addListener( this );

		Composite cmpStyle = new Composite( grpLeaderLine, SWT.NONE );
		{
			GridLayout gl = new GridLayout( 2, false );
			gl.marginHeight = 0;
			gl.marginBottom = 0;
			cmpStyle.setLayout( gl );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			cmpStyle.setLayoutData( gd );
		}
		
		// Leader Line Style composite
		Label lblLeaderStyle = new Label( cmpStyle, SWT.NONE );
		GridData gdLBLLeaderStyle = new GridData( );
		lblLeaderStyle.setLayoutData( gdLBLLeaderStyle );
		lblLeaderStyle.setText( Messages.getString( "PieSeriesAttributeComposite.Lbl.LeaderLineStyle" ) ); //$NON-NLS-1$

		cmbLeaderLine = new Combo( cmpStyle, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBLeaderLine = new GridData( GridData.FILL_HORIZONTAL );
		cmbLeaderLine.setLayoutData( gdCMBLeaderLine );
		cmbLeaderLine.addSelectionListener( this );

		// Leader Line Size composite
		Label lblLeaderSize = new Label( cmpStyle, SWT.NONE );
		GridData gdLBLLeaderSize = new GridData( );
		lblLeaderSize.setLayoutData( gdLBLLeaderSize );
		lblLeaderSize.setText( Messages.getString( "PieSeriesAttributeComposite.Lbl.LeaderLineLength" ) ); //$NON-NLS-1$

		Composite comp = new Composite( cmpStyle, SWT.NONE );
		GridLayout gl = new GridLayout( 2, false );
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		comp.setLayout( gl );
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		comp.setLayoutData( gd );
		
		iscLeaderLength = new Spinner( comp, SWT.BORDER );
		GridData gdISCLeaderLength = new GridData( GridData.FILL_HORIZONTAL );
		iscLeaderLength.setLayoutData( gdISCLeaderLength );
		iscLeaderLength.setMinimum( 0 );
		iscLeaderLength.setMaximum( MAX_LEADER_LENGTH );
		iscLeaderLength.setSelection( (int) series.getLeaderLineLength( ) );
		iscLeaderLength.addSelectionListener( this );

		btnLeaderLengthAuto = new Button( comp, SWT.CHECK );
		btnLeaderLengthAuto.setText( ChartUIExtensionUtil.getAutoMessage( ) );
		btnLeaderLengthAuto.setSelection( !series.isSetLeaderLineLength( ) );
		iscLeaderLength.setEnabled( !btnLeaderLengthAuto.getSelection( ) );
		btnLeaderLengthAuto.addSelectionListener( this );
		
		Composite cmpRight = new Composite( this, SWT.NONE );
		{
			cmpRight.setLayout( new GridLayout( 3, false ) );
			cmpRight.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		}

		createSeriesDetail( cmpRight );

		populateLists( );
	}

	private void createSeriesDetail( Composite cmpRight )
	{
		lblRatio = new Label( cmpRight, SWT.NONE );
		{
			lblRatio.setText( Messages.getString( "PieBottomAreaComponent.Label.Ratio" ) ); //$NON-NLS-1$
			lblRatio.setToolTipText( TOOLTIP_RATIO );
		}
		
		sRatio = new Slider( cmpRight, SWT.HORIZONTAL );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 1;
			sRatio.setLayoutData( gridData );
			sRatio.setValues( (int) ( series.getRatio( ) * 10 ),
					1,
					101,
					1,
					1,
					10 );
			sRatio.setToolTipText( String.valueOf( series.getRatio( ) ) );
			sRatio.setEnabled( true );
			sRatio.addSelectionListener( this );
			sRatio.addListener( SWT.FocusOut, this );
			sRatio.addListener( SWT.KeyDown, this );
			sRatio.addListener( SWT.Traverse, this );
		}
		
		btnRatioAuto = new Button(cmpRight, SWT.CHECK );
		btnRatioAuto.setText( ChartUIExtensionUtil.getAutoMessage( ) );
		btnRatioAuto.setSelection( !series.isSetRatio( ) );
		sRatio.setEnabled( !btnRatioAuto.getSelection( ) );
		btnRatioAuto.addSelectionListener( this );
		
		lblRotation = new Label( cmpRight, SWT.NONE );
		{
			lblRotation.setText( Messages.getString("PieBottomAreaComponent.Label.Rotation") ); //$NON-NLS-1$
			lblRotation.setToolTipText( TOOLTIP_ROTATION );
		}
		
		sRotation = new Slider( cmpRight, SWT.HORIZONTAL );
		{
			GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 1;
			sRotation.setLayoutData( gridData );
			sRotation.setValues( (int) ( series.getRotation( ) ),
					0,
					360,
					1,
					1,
					10 );
			sRotation.setToolTipText( String.valueOf( series.getRotation( ) ) );
			sRotation.setEnabled( true );
			sRotation.addSelectionListener( this );
			sRotation.addListener( SWT.FocusOut, this );
			sRotation.addListener( SWT.KeyDown, this );
			sRotation.addListener( SWT.Traverse, this );
		}
		
		btnRotationAuto = new Button(cmpRight, SWT.CHECK );
		btnRotationAuto.setText( ChartUIExtensionUtil.getAutoMessage( ) );
		btnRotationAuto.setSelection( !series.isSetRotation( ) );
		sRotation.setEnabled( !btnRotationAuto.getSelection( ) );
		btnRotationAuto.addSelectionListener( this );
		
		Label lbl = new Label(cmpRight, SWT.NONE);
		lbl.setText(  Messages.getString("PieSeriesAttributeComposite.Button.Direction") ); //$NON-NLS-1$
		
		cmbDirection = ChartUIExtensionUtil.createCombo( cmpRight,  new String[]{
				ChartUIExtensionUtil.getAutoMessage( ),
				Messages.getString("PieSeriesAttributeComposite.ItemLabel.Clockwise"), //$NON-NLS-1$
				Messages.getString("PieSeriesAttributeComposite.ItemLabel.AntiClockwise") //$NON-NLS-1$
		});
		{
			cmbDirection.setToolTipText( Messages.getString("PieSeriesAttributeComposite.Button.Direction.ToolTipText") ); //$NON-NLS-1$
			cmbDirection.select( series.isSetClockwise( ) ? ( series.isClockwise( ) ? 1
					: 2 )
					: 0 );
			cmbDirection.addListener( SWT.Selection, this );
		}

		Group grpSlice = new Group( cmpRight, SWT.NONE );
		{
			GridLayout gridLayout = new GridLayout( 3, false );
			gridLayout.marginWidth = 0;
			GridData gd = new GridData( GridData.FILL_BOTH );
			gd.horizontalSpan = 3;
			grpSlice.setLayoutData( gd );
			grpSlice.setLayout( gridLayout );
			grpSlice.setText( Messages.getString("PieSeriesAttributeComposite.Grp.Slice") );//$NON-NLS-1$
		}

		lblExpSliWhen = new Label( grpSlice, SWT.NONE );
		{
			lblExpSliWhen.setText( Messages.getString( "PieBottomAreaComponent.Label.ExplodeSliceWhen" ) ); //$NON-NLS-1$
			lblExpSliWhen.setToolTipText( TOOLTIP_EXPLODE_SLICE_WHEN );
		}

		txtExplode = new TextEditorComposite( grpSlice, SWT.BORDER
				| SWT.SINGLE );
		{
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			txtExplode.setLayoutData( gd );
			if ( series.getExplosionExpression( ) != null )
			{
				txtExplode.setText( series.getExplosionExpression( ) );
			}
			txtExplode.setToolTipText( Messages.getString( "PieBaseSeriesComponent.Tooltip.EnterBooleanExpression" ) ); //$NON-NLS-1$
			txtExplode.addListener( this );
		}

		btnBuilder = new Button( grpSlice, SWT.PUSH );
		{
			GridData gdBTNBuilder = new GridData( );
			gdBTNBuilder.heightHint = 20;
			gdBTNBuilder.widthHint = 20;
			btnBuilder.setLayoutData( gdBTNBuilder );
			btnBuilder.setImage( UIHelper.getImage( "icons/obj16/expressionbuilder.gif" ) ); //$NON-NLS-1$
			btnBuilder.addSelectionListener( this );
			btnBuilder.setToolTipText( Messages.getString( "DataDefinitionComposite.Tooltip.InvokeExpressionBuilder" ) ); //$NON-NLS-1$
			btnBuilder.getImage( ).setBackground( btnBuilder.getBackground( ) );
			if ( context.getUIServiceProvider( ) == null )
			{
				btnBuilder.setEnabled( false );
			}
		}

		lblExpDistance = new Label( grpSlice, SWT.NONE );
		{
			lblExpDistance.setText( Messages.getString( "PieBottomAreaComponent.Label.ByDistance" ) ); //$NON-NLS-1$
			lblExpDistance.setToolTipText( TOOLTIP_EXPLOSION_DISTANCE );
		}

		iscExplosion = new Spinner( grpSlice, SWT.BORDER );
		{
			GridData gdISCExplosion = new GridData( GridData.FILL_HORIZONTAL );
			iscExplosion.setLayoutData( gdISCExplosion );
			iscExplosion.setMinimum( 0 );
			iscExplosion.setMaximum( 100 );
			iscExplosion.setSelection( series.getExplosion( ) );
			iscExplosion.addSelectionListener( this );
		}

		btnExplosionAuto = new Button(grpSlice, SWT.CHECK );
		btnExplosionAuto.setText( ChartUIExtensionUtil.getAutoMessage() );
		btnExplosionAuto.setSelection( !series.isSetExplosion( ) );
		iscExplosion.setEnabled( !btnExplosionAuto.getSelection( ) );
		btnExplosionAuto.addSelectionListener( this );
		
		// Slice outline color composite
		Label lblSliceOutline = new Label( grpSlice, SWT.NONE );
		GridData gdLBLSliceOutline = new GridData( );
		lblSliceOutline.setLayoutData( gdLBLSliceOutline );
		lblSliceOutline.setText( Messages.getString( "PieSeriesAttributeComposite.Lbl.SliceOutline" ) ); //$NON-NLS-1$

		int fillStyles = FillChooserComposite.ENABLE_TRANSPARENT
				| FillChooserComposite.ENABLE_TRANSPARENT_SLIDER
				| FillChooserComposite.ENABLE_AUTO
				| FillChooserComposite.DISABLE_PATTERN_FILL;
		fccSliceOutline = new FillChooserComposite( grpSlice,
				SWT.NONE,
				fillStyles,
				context,
				series.getSliceOutline( ) );
		GridData gdFCCSliceOutline = new GridData( GridData.FILL_HORIZONTAL );
		gdFCCSliceOutline.horizontalSpan = 2;
		fccSliceOutline.setLayoutData( gdFCCSliceOutline );
		fccSliceOutline.addListener( this );

	}

	private void populateLists( )
	{
		NameSet ns = LiteralHelper.leaderLineStyleSet;
		cmbLeaderLine.setItems( ChartUIExtensionUtil.getItemsWithAuto( ns.getDisplayNames( ) ) );
		cmbLeaderLine.select( series.isSetLeaderLineStyle( ) ? ( ns.getSafeNameIndex( series.getLeaderLineStyle( )
				.getName( ) ) + 1 )
				: 0 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent( Event event )
	{
		boolean isUnset = ( event.detail == ChartUIExtensionUtil.PROPERTY_UNSET );
		if ( event.widget.equals( fccSliceOutline ) )
		{
			series.setSliceOutline( (ColorDefinition) event.data );
		}
		else if ( event.widget.equals( liacLeaderLine ) )
		{
			switch ( event.type )
			{
				case LineAttributesComposite.VISIBILITY_CHANGED_EVENT :
					ChartElementUtil.setEObjectAttribute( series.getLeaderLineAttributes( ),
							"visible", //$NON-NLS-1$
							( (Boolean) event.data ).booleanValue( ),
							isUnset );
					break;
				case LineAttributesComposite.STYLE_CHANGED_EVENT :
					ChartElementUtil.setEObjectAttribute( series.getLeaderLineAttributes( ),
							"style", //$NON-NLS-1$
							(LineStyle) event.data,
							isUnset );
					break;
				case LineAttributesComposite.WIDTH_CHANGED_EVENT :
					ChartElementUtil.setEObjectAttribute( series.getLeaderLineAttributes( ),
							"thickness", //$NON-NLS-1$
							( (Integer) event.data ).intValue( ),
							isUnset );
					break;
				case LineAttributesComposite.COLOR_CHANGED_EVENT :
					series.getLeaderLineAttributes( )
							.setColor( (ColorDefinition) event.data );
					break;
			}
		}
		else if ( event.widget.equals( txtExplode ) )
		{
			series.setExplosionExpression( txtExplode.getText( ) );
		}
		else if ( event.widget == cmbDirection )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"clockwise", //$NON-NLS-1$
					cmbDirection.getSelectionIndex( ) == 1,
					cmbDirection.getSelectionIndex( ) == 0 );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( iscExplosion ) )
		{
			series.setExplosion( iscExplosion.getSelection( ) );
		}
		else if ( e.widget == btnExplosionAuto )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"explosion", //$NON-NLS-1$
					iscExplosion.getSelection( ),
					btnExplosionAuto.getSelection( ) );
			iscExplosion.setEnabled( !btnExplosionAuto.getSelection( ) );
		}
		else if ( e.getSource( ).equals( iscLeaderLength ) )
		{
			series.setLeaderLineLength( iscLeaderLength.getSelection( ) );
		}
		else if ( e.widget == btnLeaderLengthAuto )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"leaderLineLength", //$NON-NLS-1$
					iscLeaderLength.getSelection( ),
					btnLeaderLengthAuto.getSelection( ) );
			iscLeaderLength.setEnabled( !btnLeaderLengthAuto.getSelection( ) );
		}
		else if ( e.getSource( ).equals( cmbLeaderLine ) )
		{
			ChartElementUtil.setEObjectAttribute( series,
					"leaderLineStyle", //$NON-NLS-1$
					LeaderLineStyle.getByName( LiteralHelper.leaderLineStyleSet.getNameByDisplayName( cmbLeaderLine.getText( ) ) ),
					cmbLeaderLine.getSelectionIndex( ) == 0 );
		}
		else if ( e.widget.equals( btnBuilder ) )
		{
			try
			{
				String sExpr = context.getUIServiceProvider( )
						.invoke( IUIServiceProvider.COMMAND_EXPRESSION_CHART_DATAPOINTS,
								txtExplode.getText( ),
								context.getExtendedItem( ),
								Messages.getString( "PieBaseSeriesComponent.Text.SpecifyExplodeSlice" ) //$NON-NLS-1$
						);
				txtExplode.setText( sExpr );
				txtExplode.setToolTipText( sExpr );
				series.setExplosionExpression( sExpr );
			}
			catch ( ChartException e1 )
			{
				WizardBase.displayException( e1 );
			}
		}
		else if ( e.widget.equals( sRatio ) )
		{
			series.setRatio( ( (double) sRatio.getSelection( ) ) / 10 );
			sRatio.setToolTipText( String.valueOf( series.getRatio( ) ) );
		}
		else if( e.widget.equals( sRotation ))
		{
			series.setRotation(  sRotation.getSelection( )  );
			sRotation.setToolTipText( String.valueOf( series.getRotation( ) ) );
		}
		else if ( e.widget == btnRotationAuto )
		{
			if ( btnRotationAuto.getSelection( ) )
			{
				series.unsetRotation();
				sRotation.setToolTipText( ChartUIExtensionUtil.getAutoMessage( ) );
			}
			else
			{
				series.setRotation(  sRotation.getSelection( )  );
				sRotation.setToolTipText( String.valueOf( series.getRotation( ) ) );
			}
			sRotation.setEnabled( !btnRotationAuto.getSelection( ) );
		}
		else if ( e.widget == btnRatioAuto )
		{
			if ( btnRatioAuto.getSelection( ) )
			{
				series.unsetRatio( );
				sRatio.setToolTipText( ChartUIExtensionUtil.getAutoMessage( ) );
			}
			else
			{
				series.setRatio( ( (double) sRatio.getSelection( ) ) / 10 );
				sRatio.setToolTipText( String.valueOf( series.getRatio( ) ) );
			}
			sRatio.setEnabled( !btnRatioAuto.getSelection( ) );
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
}