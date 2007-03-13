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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.exception.ChartException;
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
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.chart.util.LiteralHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * @author Actuate Corporation
 * 
 */
public class TriggerDataComposite extends Composite
		implements
			SelectionListener
{

	private transient Group grpValue = null;

	private transient Composite cmpURL = null;

	// private transient Text txtBaseURL = null;

	// private transient Text txtTarget = null;

	private transient Group grpParameters = null;

	private transient Text txtBaseParm = null;

	private transient Text txtValueParm = null;

	private transient Text txtSeriesParm = null;

	private transient Composite cmpCallback = null;

	private transient Composite cmpDefault = null;

	private transient Composite cmpScript = null;

	private transient Text txtScript = null;

	private transient Composite cmpTooltip = null;

	// private transient Spinner iscDelay = null;

	private transient Text txtTooltipText = null;

	private transient Button btnBuilder = null;

	private transient Composite cmpVisiblity = null;

	private transient Composite cmpHighlight = null;

	private transient Composite cmpDPVisibility = null;

	// private transient Text txtSeriesDefinition = null;

	private transient StackLayout slValues = null;

	private transient CTextCombo cmbTriggerType = null;

	private transient Combo cmbActionType = null;

	private transient ChartWizardContext wizardContext;

	private transient Button btnBaseURL;

	private transient Button btnAdvanced;

	private transient String sBaseURL = ""; //$NON-NLS-1$

	private transient boolean bEnableURLParameters;

	private transient boolean bEnableShowTooltipValue;

	private transient boolean bAdvanced = false;

	private EList triggersList;

	private Map triggersMap;

	private String lastTriggerType;

	// Indicates whether the trigger will be saved when UI is disposed
	private boolean needSaveWhenDisposing = false;

	private final static int INDEX_1_URL_REDIRECT = 1;
	private final static int INDEX_2_TOOLTIP = 2;
	private final static int INDEX_3_TOOGLE_VISABILITY = 3;
	private final static int INDEX_4_SCRIPT = 4;
	private final static int INDEX_5_HIGHLIGHT = 5;
	private final static int INDEX_6_CALLBACK = 6;
	private final static int INDEX_7_TOOGLE_DATAPOINT = 7;

	public TriggerDataComposite( Composite parent, int style, EList triggers,
			ChartWizardContext wizardContext, boolean bEnableURLParameters,
			boolean bEnableShowTooltipValue )
	{
		super( parent, style );
		this.wizardContext = wizardContext;
		this.bEnableURLParameters = bEnableURLParameters;
		this.bEnableShowTooltipValue = bEnableShowTooltipValue;
		this.triggersList = triggers;
		init( );
		placeComponents( );

		addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				if ( needSaveWhenDisposing )
				{
					// Only save when it's needed
					updateTrigger( cmbTriggerType.getText( ) );
				}
			}
		} );
	}

	private void init( )
	{
		this.setSize( getParent( ).getClientArea( ).width,
				getParent( ).getClientArea( ).height );

		triggersMap = new HashMap( );
		for ( int i = 0; i < triggersList.size( ); i++ )
		{
			Trigger trigger = (Trigger) triggersList.get( i );
			triggersMap.put( LiteralHelper.triggerConditionSet.getDisplayNameByName( trigger.getCondition( )
					.getName( ) ),
					trigger );
		}
	}

	private void placeComponents( )
	{
		// Layout for the content composite
		GridLayout glCMPTrigger = new GridLayout( );
		glCMPTrigger.numColumns = 2;
		glCMPTrigger.horizontalSpacing = 16;
		glCMPTrigger.verticalSpacing = 5;

		// Layout for the Action Details group
		slValues = new StackLayout( );

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

		Label lblTriggerEvent = new Label( this, SWT.NONE );
		GridData gdLBLTriggerEvent = new GridData( );
		gdLBLTriggerEvent.horizontalIndent = 4;
		lblTriggerEvent.setLayoutData( gdLBLTriggerEvent );
		lblTriggerEvent.setText( Messages.getString( "TriggerDataComposite.Lbl.Event" ) ); //$NON-NLS-1$

		cmbTriggerType = new CTextCombo( this, SWT.NONE );
		GridData gdCMBTriggerType = new GridData( GridData.FILL_HORIZONTAL );
		cmbTriggerType.setLayoutData( gdCMBTriggerType );
		cmbTriggerType.addListener( new Listener( ) {

			public void handleEvent( Event event )
			{
				updateTrigger( lastTriggerType );

				Trigger trigger = (Trigger) triggersMap.get( cmbTriggerType.getText( ) );
				if ( trigger != null )
				{
					cmbActionType.setText( getActionText( trigger ) );
				}
				else
				{
					cmbActionType.select( 0 );
				}
				updateUI( trigger );
				switchUI( );
				lastTriggerType = cmbTriggerType.getText( );
			}
		} );

		Label lblActionType = new Label( this, SWT.NONE );
		GridData gdLBLActionType = new GridData( );
		gdLBLActionType.horizontalIndent = 4;
		lblActionType.setLayoutData( gdLBLActionType );
		lblActionType.setText( Messages.getString( "TriggerDataComposite.Lbl.Action" ) ); //$NON-NLS-1$

		cmbActionType = new Combo( this, SWT.DROP_DOWN | SWT.READ_ONLY );
		GridData gdCMBActionType = new GridData( GridData.FILL_HORIZONTAL );
		cmbActionType.setLayoutData( gdCMBActionType );
		cmbActionType.addSelectionListener( this );
		cmbActionType.setVisibleItemCount( 10 );

		grpValue = new Group( this, SWT.NONE );
		GridData gdGRPValue = new GridData( GridData.FILL_BOTH );
		gdGRPValue.horizontalSpan = 2;
		grpValue.setLayoutData( gdGRPValue );
		grpValue.setText( Messages.getString( "TriggerDataComposite.Lbl.ActionDetails" ) ); //$NON-NLS-1$
		grpValue.setLayout( slValues );

		// Composite for default value
		cmpDefault = new Composite( grpValue, SWT.NONE );

		// Composite for callback value
		cmpCallback = new Composite( grpValue, SWT.NONE );
		cmpCallback.setLayout( new GridLayout( ) );

		addDescriptionLabel( cmpCallback,
				1,
				Messages.getString( "TriggerDataComposite.Label.CallbackDescription" ) ); //$NON-NLS-1$

		// Composite for highlight value
		cmpHighlight = new Composite( grpValue, SWT.NONE );
		cmpHighlight.setLayout( new GridLayout( ) );

		addDescriptionLabel( cmpHighlight,
				1,
				Messages.getString( "TriggerDataComposite.Label.HighlightDescription" ) ); //$NON-NLS-1$

		// Composite for Toogle Visibility value
		cmpVisiblity = new Composite( grpValue, SWT.NONE );
		cmpVisiblity.setLayout( new GridLayout( ) );

		addDescriptionLabel( cmpVisiblity,
				1,
				Messages.getString( "TriggerDataComposite.Label.VisiblityDescription" ) ); //$NON-NLS-1$

		// Composite for Toogle DataPoint Visibility value
		cmpDPVisibility = new Composite( grpValue, SWT.NONE );
		cmpDPVisibility.setLayout( new GridLayout( ) );

		addDescriptionLabel( cmpDPVisibility,
				1,
				Messages.getString( "TriggerDataComposite.Label.DPVisibilityDescription" ) ); //$NON-NLS-1$

		// Composite for script value
		cmpScript = new Composite( grpValue, SWT.NONE );
		cmpScript.setLayout( new GridLayout( 2, false ) );

		Label lblScript = new Label( cmpScript, SWT.NONE );
		GridData gdLBLScript = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
		lblScript.setLayoutData( gdLBLScript );
		lblScript.setText( Messages.getString( "TriggerDataComposite.Lbl.Script" ) ); //$NON-NLS-1$

		txtScript = new Text( cmpScript, SWT.MULTI | SWT.BORDER | SWT.V_SCROLL );
		txtScript.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		txtScript.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.InputScript" ) ); //$NON-NLS-1$

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
		cmpTooltip.setLayout( new GridLayout( 3, false ) );

		// Deprecate delay attribute: #132289
		// Label lblDelay = new Label( cmpTooltip, SWT.NONE );
		// GridData gdLBLDelay = new GridData( );
		// lblDelay.setLayoutData( gdLBLDelay );
		// lblDelay.setText( Messages.getString(
		// "TriggerDataComposite.Lbl.TooltipDelay" ) ); //$NON-NLS-1$
		//
		// iscDelay = new Spinner( cmpTooltip, SWT.BORDER );
		// GridData gdISCDelay = new GridData( GridData.FILL_HORIZONTAL );
		// gdISCDelay.horizontalSpan = 2;
		// iscDelay.setLayoutData( gdISCDelay );
		// iscDelay.setMaximum( 5000 );
		// iscDelay.setMinimum( 100 );
		// iscDelay.setIncrement( 100 );
		// iscDelay.setSelection( 200 );

		Label lblText = new Label( cmpTooltip, SWT.NONE );
		lblText.setText( Messages.getString( "TriggerDataComposite.Lbl.TooltipText" ) ); //$NON-NLS-1$

		if ( bEnableShowTooltipValue )
		{
			GridData lblGd = new GridData( );
			lblGd.horizontalSpan = 3;
			lblText.setLayoutData( lblGd );

			txtTooltipText = new Text( cmpTooltip, SWT.BORDER
					| SWT.MULTI | SWT.V_SCROLL );
			GridData gdTXTTooltipText = new GridData( GridData.FILL_BOTH );
			gdTXTTooltipText.horizontalSpan = 3;
			txtTooltipText.setLayoutData( gdTXTTooltipText );
		}
		else
		{
			txtTooltipText = new Text( cmpTooltip, SWT.BORDER | SWT.SINGLE );
			txtTooltipText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

			btnBuilder = new Button( cmpTooltip, SWT.PUSH );
			GridData gdBTNBuilder = new GridData( );
			gdBTNBuilder.heightHint = 20;
			gdBTNBuilder.widthHint = 20;
			btnBuilder.setLayoutData( gdBTNBuilder );
			btnBuilder.setImage( UIHelper.getImage( "icons/obj16/expressionbuilder.gif" ) ); //$NON-NLS-1$
			btnBuilder.addSelectionListener( this );
			btnBuilder.setToolTipText( Messages.getString( "DataDefinitionComposite.Tooltip.InvokeExpressionBuilder" ) ); //$NON-NLS-1$
			btnBuilder.getImage( ).setBackground( btnBuilder.getBackground( ) );

			addDescriptionLabel( cmpTooltip,
					3,
					Messages.getString( "TriggerDataComposite.Label.TooltipUsingDataLabelOfSeries" ) ); //$NON-NLS-1$
		}

		//Composite for url value
		cmpURL = new Composite( grpValue, SWT.NONE );
		cmpURL.setLayout( glURL );

		Label lblBaseURL = new Label( cmpURL, SWT.NONE );
		GridData gdLBLBaseURL = new GridData( );
		gdLBLBaseURL.horizontalIndent = 2;
		lblBaseURL.setLayoutData( gdLBLBaseURL );
		lblBaseURL.setText( Messages.getString( "TriggerDataComposite.Lbl.BaseURL" ) ); //$NON-NLS-1$

		btnBaseURL = new Button( cmpURL, SWT.NONE );
		{
			GridData gd = new GridData( );
			btnBaseURL.setLayoutData( gd );
			btnBaseURL.setText( Messages.getString( "TriggerDataComposite.Text.EditBaseURL" ) ); //$NON-NLS-1$
			btnBaseURL.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.InvokeURLBuilder" ) ); //$NON-NLS-1$
			btnBaseURL.addSelectionListener( this );
			btnBaseURL.setEnabled( wizardContext.getUIServiceProvider( )
					.isInvokingSupported( ) );
			btnBaseURL.setVisible( wizardContext.getUIServiceProvider( )
					.isEclipseModeSupported( ) );
		}

		Label lblDefine = new Label( cmpURL, SWT.WRAP );
		{
			GridData gd = new GridData( );
			gd.horizontalIndent = 2;
			gd.horizontalSpan = 3;
			gd.widthHint = 200;
			lblDefine.setLayoutData( gd );
			lblDefine.setText( Messages.getString( "TriggerDataComposite.Label.Description" ) ); //$NON-NLS-1$
		}

		btnAdvanced = new Button( cmpURL, SWT.NONE );
		{
			GridData gd = new GridData( );
			gd.horizontalSpan = 2;
			btnAdvanced.setLayoutData( gd );
			btnAdvanced.setText( getAdvancedButtonText( bAdvanced ) );
			btnAdvanced.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.Advanced" ) ); //$NON-NLS-1$
			btnAdvanced.addSelectionListener( this );
			btnAdvanced.setEnabled( bEnableURLParameters );
		}

		// Label lblTarget = new Label( cmpURL, SWT.NONE );
		// GridData gdLBLTarget = new GridData( );
		// gdLBLTarget.horizontalIndent = 2;
		// lblTarget.setLayoutData( gdLBLTarget );
		// lblTarget.setText( Messages.getString(
		// "TriggerDataComposite.Lbl.Target" ) ); //$NON-NLS-1$
		//
		// txtTarget = new Text( cmpURL, SWT.BORDER );
		// GridData gdTXTTarget = new GridData( GridData.FILL_HORIZONTAL );
		// gdTXTTarget.horizontalSpan = 2;
		// txtTarget.setLayoutData( gdTXTTarget );

		grpParameters = new Group( cmpURL, SWT.NONE );
		GridData gdGRPParameters = new GridData( GridData.FILL_HORIZONTAL );
		gdGRPParameters.horizontalSpan = 3;
		grpParameters.setLayoutData( gdGRPParameters );
		grpParameters.setLayout( glParameter );
		grpParameters.setText( Messages.getString( "TriggerDataComposite.Lbl.ParameterNames" ) ); //$NON-NLS-1$
		grpParameters.setVisible( bAdvanced );

		StyledText stParameters = new StyledText( grpParameters, SWT.WRAP );
		{
			GridData gd = new GridData( );
			gd.horizontalIndent = 2;
			gd.horizontalSpan = 3;
			gd.widthHint = 200;
			stParameters.setLayoutData( gd );
			stParameters.setText( Messages.getString( "TriggerDataComposite.Label.OptionalURLParameters" ) ); //$NON-NLS-1$
			StyleRange[] sr = {
					new StyleRange( 0,
							4,
							this.getForeground( ),
							this.getBackground( ),
							SWT.ITALIC ),
					new StyleRange( 4,
							stParameters.getText( ).length( ) - 4,
							this.getForeground( ),
							this.getBackground( ),
							SWT.NORMAL )
			};
			stParameters.setStyleRanges( sr );
			stParameters.setBackground( this.getBackground( ) );
		}

		Label lblBaseParm = new Label( grpParameters, SWT.NONE );
		{
			GridData gdLBLBaseParm = new GridData( );
			gdLBLBaseParm.horizontalIndent = 2;
			lblBaseParm.setLayoutData( gdLBLBaseParm );
			lblBaseParm.setText( Messages.getString( "TriggerDataComposite.Lbl.CategorySeries" ) ); //$NON-NLS-1$
			lblBaseParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterCategory" ) ); //$NON-NLS-1$
		}

		txtBaseParm = new Text( grpParameters, SWT.BORDER );
		GridData gdTXTBaseParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTBaseParm.horizontalSpan = 2;
		txtBaseParm.setLayoutData( gdTXTBaseParm );
		txtBaseParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterCategory" ) ); //$NON-NLS-1$

		Label lblValueParm = new Label( grpParameters, SWT.NONE );
		{
			GridData gdLBLValueParm = new GridData( );
			gdLBLValueParm.horizontalIndent = 2;
			lblValueParm.setLayoutData( gdLBLValueParm );
			lblValueParm.setText( Messages.getString( "TriggerDataComposite.Lbl.ValueSeries" ) ); //$NON-NLS-1$
			lblValueParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterValue" ) ); //$NON-NLS-1$
		}

		txtValueParm = new Text( grpParameters, SWT.BORDER );
		GridData gdTXTValueParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTValueParm.horizontalSpan = 2;
		txtValueParm.setLayoutData( gdTXTValueParm );
		txtValueParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterValue" ) ); //$NON-NLS-1$

		Label lblSeriesParm = new Label( grpParameters, SWT.NONE );
		{
			GridData gdLBLSeriesParm = new GridData( );
			gdLBLSeriesParm.horizontalIndent = 2;
			lblSeriesParm.setLayoutData( gdLBLSeriesParm );
			lblSeriesParm.setText( Messages.getString( "TriggerDataComposite.Lbl.ValueSeriesName" ) ); //$NON-NLS-1$
			lblSeriesParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterSeries" ) ); //$NON-NLS-1$
		}

		txtSeriesParm = new Text( grpParameters, SWT.BORDER );
		GridData gdTXTSeriesParm = new GridData( GridData.FILL_HORIZONTAL );
		gdTXTSeriesParm.horizontalSpan = 2;
		txtSeriesParm.setLayoutData( gdTXTSeriesParm );
		txtSeriesParm.setToolTipText( Messages.getString( "TriggerDataComposite.Tooltip.ParameterSeries" ) ); //$NON-NLS-1$

		populateLists( );
	}

	private void populateLists( )
	{
		String[] triggerTypes = LiteralHelper.triggerConditionSet.getDisplayNames( );
		cmbTriggerType.setItems( triggerTypes );
		cmbTriggerType.select( 0 );

		String firstTrigger = null;
		for ( int i = 0; i < triggerTypes.length; i++ )
		{
			if ( triggersMap.containsKey( triggerTypes[i] ) )
			{
				cmbTriggerType.markSelection( triggerTypes[i] );
				if ( firstTrigger == null )
				{
					firstTrigger = triggerTypes[i];
				}
			}
		}

		cmbActionType.setItems( LiteralHelper.actionTypeSet.getDisplayNames( ) );
		cmbActionType.add( Messages.getString( "TriggerDataComposite.Lbl.None" ), 0 ); //$NON-NLS-1$
		cmbActionType.select( 0 );

		if ( firstTrigger != null )
		{
			// Select first trigger
			setTrigger( (Trigger) triggersMap.get( firstTrigger ) );
		}
		else
		{
			slValues.topControl = cmpDefault;
		}

	}

	private Label addDescriptionLabel( Composite parent, int horizontalSpan,
			String description )
	{
		Label label = new Label( parent, SWT.WRAP );
		{
			GridData gd = new GridData( );
			gd.widthHint = 200;
			gd.horizontalSpan = horizontalSpan;
			label.setLayoutData( gd );
			label.setText( description );
		}
		return label;
	}

	/**
	 * Marks UI will save the trigger when closing
	 * 
	 */
	public void markSaveWhenClosing( )
	{
		this.needSaveWhenDisposing = true;
	}

	public void setTrigger( Trigger trigger )
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
		updateUI( trigger );
	}

	private void initUI( )
	{
		final String BLANK_STRING = ""; //$NON-NLS-1$
		// case INDEX_1_URL_REDIRECT :
		txtBaseParm.setText( BLANK_STRING );
		txtValueParm.setText( BLANK_STRING );
		txtSeriesParm.setText( BLANK_STRING );
		// case INDEX_2_TOOLTIP :
		txtTooltipText.setText( BLANK_STRING );
		// case INDEX_4_SCRIPT :
		txtScript.setText( BLANK_STRING );
	}

	private void updateUI( Trigger trigger )
	{
		if ( trigger == null )
		{
			initUI( );
			return;
		}
		switch ( cmbActionType.getSelectionIndex( ) )
		{
			case INDEX_1_URL_REDIRECT :
				this.slValues.topControl = cmpURL;
				URLValue urlValue = (URLValue) trigger.getAction( ).getValue( );
				sBaseURL = ( urlValue.getBaseUrl( ).length( ) > 0 )
						? urlValue.getBaseUrl( ) : ""; //$NON-NLS-1$
				// txtBaseURL.setText( sBaseURL );
				// txtTarget.setText( ( urlValue.getTarget( ).length( ) > 0 )
				// ? urlValue.getTarget( ) : "" ); //$NON-NLS-1$
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
			case INDEX_2_TOOLTIP :
				this.slValues.topControl = cmpTooltip;
				TooltipValue tooltipValue = (TooltipValue) trigger.getAction( )
						.getValue( );
				// iscDelay.setSelection( tooltipValue.getDelay( ) );
				txtTooltipText.setText( ( tooltipValue.getText( ) != null )
						? tooltipValue.getText( ) : "" ); //$NON-NLS-1$
				break;
			case INDEX_3_TOOGLE_VISABILITY :
				this.slValues.topControl = cmpVisiblity;
				// SeriesValue seriesValue = (SeriesValue) trigger.getAction( )
				// .getValue( );
				// txtSeriesDefinition.setText( ( seriesValue.getName( ).length(
				// ) > 0 ) ? seriesValue.getName( )
				// : "" ); //$NON-NLS-1$
				break;
			case INDEX_4_SCRIPT :
				this.slValues.topControl = cmpScript;
				ScriptValue scriptValue = (ScriptValue) trigger.getAction( )
						.getValue( );
				txtScript.setText( ( scriptValue.getScript( ).length( ) > 0 )
						? scriptValue.getScript( ) : "" ); //$NON-NLS-1$
				break;
			case INDEX_5_HIGHLIGHT :
				this.slValues.topControl = cmpHighlight;
				// SeriesValue highlightSeriesValue = (SeriesValue)
				// trigger.getAction( )
				// .getValue( );
				// txtSeriesDefinition.setText( ( highlightSeriesValue.getName(
				// )
				// .length( ) > 0 ) ? highlightSeriesValue.getName( ) : "" );
				// //$NON-NLS-1$
				break;
			case INDEX_6_CALLBACK :
				this.slValues.topControl = cmpCallback;
				break;
			case INDEX_7_TOOGLE_DATAPOINT :
				this.slValues.topControl = cmpDPVisibility;
				break;
			default :
				this.slValues.topControl = cmpDefault;
				break;
		}
		grpValue.layout( );
	}

	public Trigger getTrigger( )
	{
		if ( cmbActionType.getSelectionIndex( ) == 0 )
		{
			return null;
		}
		ActionValue value = null;
		switch ( cmbActionType.getSelectionIndex( ) )
		{
			case INDEX_1_URL_REDIRECT :
				value = URLValueImpl.create( sBaseURL, null,// txtTarget.getText(
						// ),
						txtBaseParm.getText( ),
						txtValueParm.getText( ),
						txtSeriesParm.getText( ) );
				break;
			case INDEX_2_TOOLTIP :
				// value = TooltipValueImpl.create( iscDelay.getSelection( ), ""
				// ); //$NON-NLS-1$
				value = TooltipValueImpl.create( 200, "" ); //$NON-NLS-1$
				( (TooltipValue) value ).setText( txtTooltipText.getText( ) );
				break;
			case INDEX_3_TOOGLE_VISABILITY :
				value = AttributeFactory.eINSTANCE.createSeriesValue( );
				( (SeriesValue) value ).setName( "" ); //$NON-NLS-1$
				break;
			case INDEX_4_SCRIPT :
				value = AttributeFactory.eINSTANCE.createScriptValue( );
				( (ScriptValue) value ).setScript( txtScript.getText( ) );
				break;
			case INDEX_5_HIGHLIGHT :
				value = AttributeFactory.eINSTANCE.createSeriesValue( );
				( (SeriesValue) value ).setName( "" ); //$NON-NLS-1$
				break;
			case INDEX_7_TOOGLE_DATAPOINT :
				value = AttributeFactory.eINSTANCE.createSeriesValue( );
				( (SeriesValue) value ).setName( "" ); //$NON-NLS-1$
			default :
				break;
		}
		Action action = ActionImpl.create( ActionType.getByName( LiteralHelper.actionTypeSet.getNameByDisplayName( cmbActionType.getText( ) ) ),
				value );
		return TriggerImpl.create( TriggerCondition.getByName( LiteralHelper.triggerConditionSet.getNameByDisplayName( lastTriggerType == null
				? cmbTriggerType.getText( ) : lastTriggerType ) ),
				action );
	}

	public void clear( )
	{
		cmbTriggerType.select( 0 );
		cmbActionType.select( 0 );
		switchUI( );
	}

	public Point getPreferredSize( )
	{
		return new Point( 260, 260 );
	}

	private void switchUI( )
	{
		switch ( cmbActionType.getSelectionIndex( ) )
		{
			case INDEX_1_URL_REDIRECT :
				this.slValues.topControl = cmpURL;
				break;
			case INDEX_2_TOOLTIP :
				this.slValues.topControl = cmpTooltip;
				break;
			case INDEX_3_TOOGLE_VISABILITY :
				this.slValues.topControl = cmpVisiblity;
				break;
			case INDEX_4_SCRIPT :
				this.slValues.topControl = cmpScript;
				break;
			case INDEX_5_HIGHLIGHT :
				this.slValues.topControl = cmpHighlight;
				break;
			case INDEX_6_CALLBACK :
				this.slValues.topControl = cmpCallback;
				break;
			case INDEX_7_TOOGLE_DATAPOINT :
				this.slValues.topControl = cmpDPVisibility;
				break;
			default :
				this.slValues.topControl = cmpDefault;
				break;
		}
		grpValue.layout( );
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
			String triggerType = cmbTriggerType.getText( );
			if ( cmbActionType.getSelectionIndex( ) == 0 )
			{
				cmbTriggerType.unmarkSelection( triggerType );
				Object trigger = triggersMap.get( triggerType );
				if ( trigger != null )
				{
					triggersMap.remove( triggerType );
					triggersList.remove( trigger );
				}
			}
			else
			{
				cmbTriggerType.markSelection( triggerType );
			}
			cmbTriggerType.setText( triggerType );
			switchUI( );
		}
		else if ( e.getSource( ).equals( btnBaseURL ) )
		{
			try
			{
				if ( wizardContext != null )
				{
					sBaseURL = wizardContext.getUIServiceProvider( )
							.invoke( IUIServiceProvider.COMMAND_HYPERLINK,
									sBaseURL,
									wizardContext.getExtendedItem( ),
									null );
				}
			}
			catch ( ChartException ex )
			{
				ChartWizard.displayException( ex );
			}
		}
		else if ( e.getSource( ).equals( btnAdvanced ) )
		{
			bAdvanced = !bAdvanced;
			btnAdvanced.setText( getAdvancedButtonText( bAdvanced ) );
			grpParameters.setVisible( bAdvanced );
			this.slValues.topControl = cmpURL;
			grpValue.layout( true, true );
		}
		else if ( e.getSource( ).equals( btnBuilder ) )
		{
			try
			{
				String sExpr = wizardContext.getUIServiceProvider( )
						.invoke( IUIServiceProvider.COMMAND_EXPRESSION,
								txtTooltipText.getText( ),
								wizardContext.getExtendedItem( ),
								null );
				txtTooltipText.setText( sExpr );
			}
			catch ( ChartException e1 )
			{
				WizardBase.displayException( e1 );
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

	private String getActionText( Trigger trigger )
	{
		return LiteralHelper.actionTypeSet.getDisplayNameByName( trigger.getAction( )
				.getType( )
				.getName( ) );
	}

	private void updateTrigger( String triggerType )
	{
		if ( triggerType == null )
		{
			return;
		}

		if ( cmbActionType.getSelectionIndex( ) == 0 )
		{
			cmbTriggerType.unmarkSelection( triggerType );
			return;
		}
		cmbTriggerType.markSelection( triggerType );

		Object oldTrigger = triggersMap.get( triggerType );
		Object newTrigger = getTrigger( );
		if ( oldTrigger != null )
		{
			int index = triggersList.indexOf( oldTrigger );
			if ( index >= 0 )
			{
				triggersList.set( index, newTrigger );
			}
		}
		else
		{
			triggersList.add( newTrigger );
		}
		triggersMap.put( triggerType, newTrigger );
	}

	private String getAdvancedButtonText( boolean bAdvanced )
	{
		if ( bAdvanced )
		{
			return Messages.getString( "TriggerDataComposite.Text.OpenAdvanced" ); //$NON-NLS-1$
		}
		return Messages.getString( "TriggerDataComposite.Text.Advanced" ); //$NON-NLS-1$

	}
}