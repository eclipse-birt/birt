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

import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ScriptValue;
import org.eclipse.birt.chart.model.attribute.SeriesValue;
import org.eclipse.birt.chart.model.attribute.TooltipValue;
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.attribute.impl.TooltipValueImpl;
import org.eclipse.birt.chart.model.attribute.impl.URLValueImpl;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.data.impl.ActionImpl;
import org.eclipse.birt.chart.model.data.impl.TriggerImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 * 
 */
public class TriggerDataComposite extends Composite implements
		SelectionListener
{

	private transient Group grpValue = null;

	private transient Composite cmpURL = null;

	private transient Text txtBaseURL = null;

	private transient Text txtTarget = null;

	private transient Group grpParameters = null;

	private transient Text txtBaseParm = null;

	private transient Text txtValueParm = null;

	private transient Text txtSeriesParm = null;

	private transient Composite cmpDefault = null;

	private transient Composite cmpScript = null;

	private transient Text txtScript = null;

	private transient Composite cmpTooltip = null;

	private transient IntegerSpinControl iscDelay = null;

	// private transient Text txtTooltipText = null;

	private transient Composite cmpSeries = null;

	// private transient Text txtSeriesDefinition = null;

	private transient StackLayout slValues = null;

	private transient Combo cmbTriggerType = null;

	private transient Combo cmbActionType = null;

	/**
	 * @param parent
	 * @param style
	 */
	public TriggerDataComposite( Composite parent, int style, Trigger trigger )
	{
		super( parent, style );
		init( );
		placeComponents( );
		setTrigger( trigger );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );
	}

	private void placeComponents( )
	{
		// Layout for the content composite
		GridLayout glCMPTrigger = new GridLayout( );
		glCMPTrigger.numColumns = 2;
		glCMPTrigger.horizontalSpacing = 16;
		glCMPTrigger.verticalSpacing = 5;
		glCMPTrigger.marginHeight = 0;
		glCMPTrigger.marginWidth = 0;

		// Layout for the Action Details group
		slValues = new StackLayout( );

		// Layout for script value composite
		GridLayout glScript = new GridLayout( );
		glScript.marginWidth = 4;
		glScript.marginHeight = 6;

		// Layout for toggle visibility value composite
		GridLayout glVisibility = new GridLayout( );
		glVisibility.marginWidth = 4;
		glVisibility.marginHeight = 6;
		glVisibility.horizontalSpacing = 6;
		glVisibility.numColumns = 3;

		// Layout for tooltip value composite
		GridLayout glTooltip = new GridLayout( );
		glTooltip.marginWidth = 2;
		glTooltip.marginHeight = 6;
		glTooltip.horizontalSpacing = 6;
		glTooltip.numColumns = 3;

		// Layout for url value composite
		GridLayout glURL = new GridLayout( );
		glURL.marginWidth = 2;
		glURL.marginHeight = 6;
		glURL.horizontalSpacing = 6;
		glURL.numColumns = 3;

		// Layout for script value composite
		GridLayout glParameter = new GridLayout( );
		glParameter.marginWidth = 2;
		glParameter.marginHeight = 6;
		glParameter.horizontalSpacing = 6;
		glParameter.numColumns = 3;

		// Main content composite
		this.setLayout( glCMPTrigger );

		Label lblTriggerType = new Label( this, SWT.NONE );
		GridData gdLBLTriggerType = new GridData( );
		gdLBLTriggerType.horizontalIndent = 4;
		lblTriggerType.setLayoutData( gdLBLTriggerType );
		lblTriggerType.setText( Messages.getString( "TriggerDataComposite.Lbl.Type" ) ); //$NON-NLS-1$

		cmbTriggerType = new Combo( this, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBTriggerType = new GridData( GridData.FILL_HORIZONTAL );
		cmbTriggerType.setLayoutData( gdCMBTriggerType );
		cmbTriggerType.addSelectionListener( this );

		Label lblActionType = new Label( this, SWT.NONE );
		GridData gdLBLActionType = new GridData( );
		gdLBLActionType.horizontalIndent = 4;
		lblActionType.setLayoutData( gdLBLActionType );
		lblActionType.setText( Messages.getString( "TriggerDataComposite.Lbl.Action" ) ); //$NON-NLS-1$

		cmbActionType = new Combo( this, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBActionType = new GridData( GridData.FILL_HORIZONTAL );
		cmbActionType.setLayoutData( gdCMBActionType );
		cmbActionType.addSelectionListener( this );

		grpValue = new Group( this, SWT.NONE );
		GridData gdGRPValue = new GridData( GridData.FILL_BOTH );
		gdGRPValue.horizontalSpan = 2;
		grpValue.setLayoutData( gdGRPValue );
		grpValue.setText( Messages.getString( "TriggerDataComposite.Lbl.ActionDetails" ) ); //$NON-NLS-1$
		grpValue.setLayout( slValues );

		// Composite for defualt value
		cmpDefault = new Composite( grpValue, SWT.NONE );

		// Composite for script value
		cmpScript = new Composite( grpValue, SWT.NONE );
		cmpScript.setLayout( glScript );

		Label lblScript = new Label( cmpScript, SWT.NONE );
		GridData gdLBLScript = new GridData( );
		lblScript.setLayoutData( gdLBLScript );
		lblScript.setText( Messages.getString( "TriggerDataComposite.Lbl.Script" ) ); //$NON-NLS-1$

		txtScript = new Text( cmpScript, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL );
		GridData gdTXTScript = new GridData( GridData.FILL_BOTH );
		txtScript.setLayoutData( gdTXTScript );

		// Composite for series value
		cmpSeries = new Composite( grpValue, SWT.NONE );
		cmpSeries.setLayout( glVisibility );

		// Label lblSeries = new Label( cmpSeries, SWT.NONE );
		// GridData gdLBLSeries = new GridData( );
		// gdLBLSeries.horizontalIndent = 2;
		// lblSeries.setLayoutData( gdLBLSeries );
		// lblSeries.setText( Messages.getString(
		// "TriggerDataComposite.Lbl.SeriesDefinition" ) ); //$NON-NLS-1$
		//
		// txtSeriesDefinition = new Text( cmpSeries, SWT.BORDER );
		// GridData gdTXTSeriesDefinition = new GridData(
		// GridData.FILL_HORIZONTAL );
		// gdTXTSeriesDefinition.horizontalSpan = 2;
		// txtSeriesDefinition.setLayoutData( gdTXTSeriesDefinition );

		// Composite for tooltip value
		cmpTooltip = new Composite( grpValue, SWT.NONE );
		cmpTooltip.setLayout( glTooltip );

		Label lblDelay = new Label( cmpTooltip, SWT.NONE );
		GridData gdLBLDelay = new GridData( );
		gdLBLDelay.horizontalIndent = 2;
		lblDelay.setLayoutData( gdLBLDelay );
		lblDelay.setText( Messages.getString( "TriggerDataComposite.Lbl.TooltipDelay" ) ); //$NON-NLS-1$

		iscDelay = new IntegerSpinControl( cmpTooltip, SWT.NONE, 200 );
		GridData gdISCDelay = new GridData( );
		gdISCDelay.horizontalSpan = 2;
		gdISCDelay.widthHint = 50;
		iscDelay.setLayoutData( gdISCDelay );
		iscDelay.setMinimum( 100 );
		iscDelay.setMaximum( 5000 );
		iscDelay.setIncrement( 100 );

		// Label lblText = new Label( cmpTooltip, SWT.NONE );
		// GridData gdLBLText = new GridData( );
		// gdLBLText.horizontalIndent = 2;
		// gdLBLText.horizontalSpan = 3;
		// lblText.setLayoutData( gdLBLText );
		// lblText.setText( Messages.getString(
		// "TriggerDataComposite.Lbl.TooltipText" ) ); //$NON-NLS-1$
		//
		// txtTooltipText = new Text( cmpTooltip, SWT.BORDER
		// | SWT.MULTI
		// | SWT.V_SCROLL );
		// GridData gdTXTTooltipText = new GridData( GridData.FILL_BOTH );
		// gdTXTTooltipText.horizontalSpan = 3;
		// txtTooltipText.setLayoutData( gdTXTTooltipText );

		cmpURL = new Composite( grpValue, SWT.NONE );
		cmpURL.setLayout( glURL );

		Label lblBaseURL = new Label( cmpURL, SWT.NONE );
		GridData gdLBLBaseURL = new GridData( );
		gdLBLBaseURL.horizontalIndent = 2;
		lblBaseURL.setLayoutData( gdLBLBaseURL );
		lblBaseURL.setText( Messages.getString( "TriggerDataComposite.Lbl.BaseURL" ) ); //$NON-NLS-1$

		txtBaseURL = new Text( cmpURL, SWT.BORDER );
		GridData gdTXTBaseURL = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTBaseURL.horizontalSpan = 2;
		txtBaseURL.setLayoutData( gdTXTBaseURL );

		Label lblTarget = new Label( cmpURL, SWT.NONE );
		GridData gdLBLTarget = new GridData( );
		gdLBLTarget.horizontalIndent = 2;
		lblTarget.setLayoutData( gdLBLTarget );
		lblTarget.setText( Messages.getString( "TriggerDataComposite.Lbl.Target" ) ); //$NON-NLS-1$

		txtTarget = new Text( cmpURL, SWT.BORDER );
		GridData gdTXTTarget = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTTarget.horizontalSpan = 2;
		txtTarget.setLayoutData( gdTXTTarget );

		grpParameters = new Group( cmpURL, SWT.NONE );
		GridData gdGRPParameters = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPParameters.horizontalSpan = 3;
		grpParameters.setLayoutData( gdGRPParameters );
		grpParameters.setLayout( glParameter );
		grpParameters.setText( Messages.getString( "TriggerDataComposite.Lbl.ParameterNames" ) ); //$NON-NLS-1$

		Label lblBaseParm = new Label( grpParameters, SWT.NONE );
		GridData gdLBLBaseParm = new GridData( );
		gdLBLBaseParm.horizontalIndent = 2;
		lblBaseParm.setLayoutData( gdLBLBaseParm );
		lblBaseParm.setText( Messages.getString( "TriggerDataComposite.Lbl.BaseParameter" ) ); //$NON-NLS-1$

		txtBaseParm = new Text( grpParameters, SWT.BORDER );
		GridData gdTXTBaseParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTBaseParm.horizontalSpan = 2;
		txtBaseParm.setLayoutData( gdTXTBaseParm );

		Label lblValueParm = new Label( grpParameters, SWT.NONE );
		GridData gdLBLValueParm = new GridData( );
		gdLBLValueParm.horizontalIndent = 2;
		lblValueParm.setLayoutData( gdLBLValueParm );
		lblValueParm.setText( Messages.getString( "TriggerDataComposite.Lbl.ValueParameter" ) ); //$NON-NLS-1$

		txtValueParm = new Text( grpParameters, SWT.BORDER );
		GridData gdTXTValueParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTValueParm.horizontalSpan = 2;
		txtValueParm.setLayoutData( gdTXTValueParm );

		Label lblSeriesParm = new Label( grpParameters, SWT.NONE );
		GridData gdLBLSeriesParm = new GridData( );
		gdLBLSeriesParm.horizontalIndent = 2;
		lblSeriesParm.setLayoutData( gdLBLSeriesParm );
		lblSeriesParm.setText( Messages.getString( "TriggerDataComposite.Lbl.SeriesParameter" ) ); //$NON-NLS-1$

		txtSeriesParm = new Text( grpParameters, SWT.BORDER );
		GridData gdTXTSeriesParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTSeriesParm.horizontalSpan = 2;
		txtSeriesParm.setLayoutData( gdTXTSeriesParm );

		populateLists( );
		slValues.topControl = cmpURL;
	}

	private void populateLists( )
	{
		cmbTriggerType.setItems( LiteralHelper.triggerConditionSet.getDisplayNames( ) );
		cmbTriggerType.select( 0 );

		cmbActionType.setItems( LiteralHelper.actionTypeSet.getDisplayNames( ) );
		cmbActionType.select( 0 );
	}

	public void setTrigger( Trigger trigger )
	{
		updateUI( trigger );
	}

	private void updateUI( Trigger trigger )
	{
		if ( trigger == null )
		{
			clear( );
			return;
		}
		cmbTriggerType.setText( LiteralHelper.triggerConditionSet.getDisplayNameByName( trigger.getCondition( )
				.getName( ) ) );
		cmbActionType.setText( LiteralHelper.actionTypeSet.getDisplayNameByName( trigger.getAction( )
				.getType( )
				.getName( ) ) );
		switch ( cmbActionType.getSelectionIndex( ) )
		{
			case 0 :
				this.slValues.topControl = cmpURL;
				URLValue urlValue = (URLValue) trigger.getAction( ).getValue( );
				txtBaseURL.setText( ( urlValue.getBaseUrl( ).length( ) > 0 ) ? urlValue.getBaseUrl( )
						: "" ); //$NON-NLS-1$
				txtTarget.setText( ( urlValue.getTarget( ).length( ) > 0 ) ? urlValue.getTarget( )
						: "" ); //$NON-NLS-1$
				txtBaseParm.setText( ( urlValue.getBaseParameterName( )
						.length( ) > 0 ) ? urlValue.getBaseParameterName( )
						: "" ); //$NON-NLS-1$
				txtValueParm.setText( ( urlValue.getValueParameterName( )
						.length( ) > 0 ) ? urlValue.getValueParameterName( )
						: "" ); //$NON-NLS-1$
				txtSeriesParm.setText( ( urlValue.getSeriesParameterName( )
						.length( ) > 0 ) ? urlValue.getSeriesParameterName( )
						: "" ); //$NON-NLS-1$
				break;
			case 1 :
				this.slValues.topControl = cmpTooltip;
				TooltipValue tooltipValue = (TooltipValue) trigger.getAction( )
						.getValue( );
				iscDelay.setValue( tooltipValue.getDelay( ) );
				// txtTooltipText.setText( ( tooltipValue.getText( ).length( ) >
				// 0 ) ? tooltipValue.getText( )
				// : "" ); //$NON-NLS-1$
				break;
			case 2 :
				this.slValues.topControl = cmpSeries;
				// SeriesValue seriesValue = (SeriesValue) trigger.getAction( )
				// .getValue( );
				// txtSeriesDefinition.setText( ( seriesValue.getName( ).length(
				// ) > 0 ) ? seriesValue.getName( )
				// : "" ); //$NON-NLS-1$
				break;
			case 3 :
				this.slValues.topControl = cmpScript;
				ScriptValue scriptValue = (ScriptValue) trigger.getAction( )
						.getValue( );
				txtScript.setText( ( scriptValue.getScript( ).length( ) > 0 ) ? scriptValue.getScript( )
						: "" ); //$NON-NLS-1$
				break;
			case 4 :
				this.slValues.topControl = cmpSeries;
				// SeriesValue highlightSeriesValue = (SeriesValue)
				// trigger.getAction( )
				// .getValue( );
				// txtSeriesDefinition.setText( ( highlightSeriesValue.getName(
				// )
				// .length( ) > 0 ) ? highlightSeriesValue.getName( ) : "" );
				// //$NON-NLS-1$
				break;
			default :
				this.slValues.topControl = cmpDefault;
				break;
		}
		grpValue.layout( );
	}

	public Trigger getTrigger( )
	{
		ActionValue value = null;
		switch ( cmbActionType.getSelectionIndex( ) )
		{
			case 0 :
				value = URLValueImpl.create( txtBaseURL.getText( ),
						txtTarget.getText( ),
						txtBaseParm.getText( ),
						txtValueParm.getText( ),
						txtSeriesParm.getText( ) );
				break;
			case 1 :
				value = TooltipValueImpl.create( iscDelay.getValue( ), "" ); //$NON-NLS-1$
				break;
			case 2 :
				value = AttributeFactory.eINSTANCE.createSeriesValue( );
				( (SeriesValue) value ).setName( "" ); //$NON-NLS-1$
				break;
			case 3 :
				value = AttributeFactory.eINSTANCE.createScriptValue( );
				( (ScriptValue) value ).setScript( txtScript.getText( ) );
				break;
			case 4 :
				value = AttributeFactory.eINSTANCE.createSeriesValue( );
				( (SeriesValue) value ).setName( "" ); //$NON-NLS-1$
				break;
			default :
				break;
		}
		Action action = ActionImpl.create( ActionType.get( LiteralHelper.actionTypeSet.getNameByDisplayName( cmbActionType.getText( ) ) ),
				value );
		return TriggerImpl.create( TriggerCondition.get( LiteralHelper.triggerConditionSet.getNameByDisplayName( cmbTriggerType.getText( ) ) ),
				action );
	}

	public void clear( )
	{
		cmbTriggerType.select( 0 );
		cmbActionType.select( 0 );
		switch ( cmbActionType.getSelectionIndex( ) )
		{
			case 0 :
				this.slValues.topControl = cmpURL;
				break;
			case 1 :
				this.slValues.topControl = cmpTooltip;
				break;
			case 2 :
				this.slValues.topControl = cmpSeries;
				break;
			case 3 :
				this.slValues.topControl = cmpScript;
				break;
			case 4 :
				this.slValues.topControl = cmpSeries;
				break;
			default :
				this.slValues.topControl = cmpDefault;
				break;
		}
		grpValue.layout( );
	}

	public Point getPreferredSize( )
	{
		return new Point( 260, 260 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.events.SelectionEvent)
	 */
	public void widgetSelected( SelectionEvent e )
	{
		if ( e.getSource( ).equals( cmbActionType ) )
		{
			switch ( cmbActionType.getSelectionIndex( ) )
			{
				case 0 :
					this.slValues.topControl = cmpURL;
					grpValue.layout( );
					break;
				case 1 :
					this.slValues.topControl = cmpTooltip;
					grpValue.layout( );
					break;
				case 2 :
					this.slValues.topControl = cmpSeries;
					grpValue.layout( );
					break;
				case 3 :
					this.slValues.topControl = cmpScript;
					grpValue.layout( );
					break;
				case 4 :
					this.slValues.topControl = cmpSeries;
					grpValue.layout( );
					break;
				default :
					this.slValues.topControl = cmpDefault;
					grpValue.layout( );
					break;
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
}