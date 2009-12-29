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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.BIRTActionEvaluator;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartReportStyleProcessor;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartExpressionProvider;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IExpressionButton;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ApplyButtonHandler;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.preview.ChartLivePreviewThread;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.IExpressionHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil.ExpressionHelper;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.HyperlinkBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.text.NumberFormat;

/**
 * ChartReportItemBuilderImpl
 */
public class ChartReportItemBuilderImpl extends ReportItemBuilderUI implements
		IUIServiceProvider
{
	private static boolean isChartWizardOpen = false;

	protected static int iInstanceCount = 0;

	protected ExtendedItemHandle extendedHandle = null;
	
	protected ChartWizardContext wizardContext = null;

	protected final String taskId;

	protected static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public ChartReportItemBuilderImpl( )
	{
		this( null );
	}

	/**
	 * Open the chart with specified task
	 * 
	 * @param taskId
	 *            specified task to open
	 */
	public ChartReportItemBuilderImpl( String taskId )
	{
		super( );
		this.taskId = taskId;
	}
	
	public static boolean isChartWizardOpen( )
	{
		return isChartWizardOpen;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI#open(org.eclipse.birt.report.model.api.ExtendedItemHandle)
	 */
	public int open( final ExtendedItemHandle eih )
	{
		if ( iInstanceCount > 0 ) // LIMIT TO ONE INSTANCE
		{
			return Window.CANCEL;
		}
		iInstanceCount++;

		if ( ChartCubeUtil.isAxisChart( eih ) )
		{
			// If this handle hosts another chart, use the host chart directly
			DesignElementHandle hostChart = eih.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART );
			this.extendedHandle = (ExtendedItemHandle) hostChart;
		}
		else
		{
			// Set the ExtendedItemHandle instance (for use by the Chart Builder
			// UI
			this.extendedHandle = eih;
		}
		ReportDataServiceProvider dataProvider = null;
		ChartLivePreviewThread livePreviewThread = null; 
		try
		{
			IReportItem item = null;
			try
			{
				item = extendedHandle.getReportItem( );
				if ( item == null )
				{
					extendedHandle.loadExtendedElement( );
					item = extendedHandle.getReportItem( );
				}
			}
			catch ( ExtendedElementException exception )
			{
				logger.log( exception );
			}
			if ( item == null )
			{
				logger.log( ILogger.ERROR,
						Messages.getString( "ChartReportItemBuilderImpl.log.UnableToLocate" ) ); //$NON-NLS-1$
				return Window.CANCEL;
			}

			final CommandStack commandStack = extendedHandle.getRoot( )
					.getCommandStack( );
			final String TRANS_NAME = org.eclipse.birt.chart.reportitem.i18n.Messages.getString( "ChartElementCommandImpl.editChart" ); //$NON-NLS-1$
			commandStack.startTrans( TRANS_NAME );

			final ChartReportItemImpl crii = ( (ChartReportItemImpl) item );
			final Chart cm = (Chart) crii.getProperty( ChartReportItemConstants.PROPERTY_CHART );
			final Chart cmClone = ( cm == null ) ? null : cm.copyInstance( );
			if ( cmClone != null )
			{
				maintainCompatibility( cmClone );
			}
			
			// This array is for storing the latest chart data before pressing
			// apply button
			final Object[] applyData = new Object[3];

			// Use workbench shell to open the dialog
			Shell parentShell = null;
			if ( PlatformUI.isWorkbenchRunning( ) )
			{
				parentShell = PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( );
			}
			final ChartWizard chartBuilder = new ChartWizard( parentShell );
			dataProvider = new ReportDataServiceProvider( extendedHandle );
			ChartReportItemUIFactory uiFactory = ChartReportItemUIFactory.instance( );
			IChartDataSheet dataSheet = uiFactory.createDataSheet( extendedHandle,
					dataProvider );
			final ChartWizardContext context = uiFactory.createWizardContext( cmClone,
					this,
					dataProvider,
					dataSheet );
			this.wizardContext = context;
			livePreviewThread = new ChartLivePreviewThread( dataProvider );
			livePreviewThread.start( );
			context.setLivePreviewThread( livePreviewThread );
			
			dataProvider.setWizardContext( context );
			if ( dataProvider.checkState( IDataServiceProvider.PART_CHART ) )
			{
				// Disable some UI sections for xtab case
				context.setEnabled( ChartUIConstants.SUBTASK_AXIS, false );
				context.setEnabled( ChartUIConstants.SUBTASK_AXIS_X, false );
				context.setEnabled( ChartUIConstants.SUBTASK_AXIS_Y, false );
				context.setEnabled( ChartUIConstants.SUBTASK_AXIS_Z, false );
				context.setEnabled( ChartUIConstants.SUBTASK_LEGEND, false );
				context.setEnabled( ChartUIConstants.SUBTASK_TITLE, false );

				// Disable some chart types
				context.setEnabled( ChartUIConstants.TYPE_PIE, false );
				context.setEnabled( ChartUIConstants.TYPE_METER, false );
				context.setEnabled( ChartUIConstants.TYPE_STOCK, false );
				context.setEnabled( ChartUIConstants.TYPE_BUBBLE, false );
				context.setEnabled( ChartUIConstants.TYPE_DIFFERENCE, false );
				context.setEnabled( ChartUIConstants.TYPE_GANTT, false );
			}
			chartBuilder.addCustomButton( new ApplyButtonHandler( chartBuilder ) {

				public void run( )
				{
					super.run( );
					// Save the data when applying
					applyData[0] = context.getModel( ).copyInstance( );
					applyData[1] = context.getOutputFormat( );
					applyData[2] = context.isInheritColumnsOnly( );

					commandStack.commit( );
					commandStack.startTrans( TRANS_NAME );
				}
			} );

			// Set direction from model to chart
			context.setRtL( crii.isLayoutDirectionRTL( ) );
			context.setTextRtL( extendedHandle.isDirectionRTL( ) );
			context.setResourceFinder( crii );
			context.setExternalizer( crii );
			
			Object of = extendedHandle.getProperty( ChartReportItemConstants.PROPERTY_OUTPUT );
			if ( of instanceof String )
			{
				// GIF is deprecated in favor of PNG. Automatically update
				// model
				if ( of.equals( "GIF" ) ) //$NON-NLS-1$
				{
					context.setOutputFormat( "PNG" ); //$NON-NLS-1$
				}
				else
				{
					context.setOutputFormat( (String) of );
				}
			}
			context.setInheritColumnsOnly( extendedHandle.getBooleanProperty( ChartReportItemConstants.PROPERTY_INHERIT_COLUMNS ) );
			context.setExtendedItem( extendedHandle );
			context.setProcessor( new ChartReportStyleProcessor( extendedHandle,
					false ) );
			
			// #269935
			// If it is sharing chart case, copy expressions settings from
			// referred chart model into current.
			ChartAdapter.beginIgnoreNotifications( );
			if ( dataProvider.checkState( IDataServiceProvider.SHARE_CHART_QUERY ) )
			{
				dataProvider.update( ChartUIConstants.COPY_SERIES_DEFINITION, null );
			}
			
			// clear all old exceptions
			ChartWizard.clearExceptions( );

			// Add instance of BIRTActionEValuator into chart wizard context for the validation of expressions in chart UI.
			context.setActionEvaluator( new BIRTActionEvaluator( ) );
			
			ChartAdapter.endIgnoreNotifications( );
			
			isChartWizardOpen = true;
			ChartWizardContext contextResult = (ChartWizardContext) chartBuilder.open( null,
					taskId,
					context );
			
			isChartWizardOpen = false;
			
			if ( contextResult != null && contextResult.getModel( ) != null )
			{

				// Pressing Finish
				updateModel( extendedHandle,
						chartBuilder,
						crii,
						cm,
						contextResult.getModel( ),
						contextResult.getOutputFormat( ),
						contextResult.isInheritColumnsOnly( ) );
				if ( dataProvider.isPartChart( ) )
				{
					ChartXTabUIUtil.updateXTabForAxis( ChartCubeUtil.getXtabContainerCell( extendedHandle ),
							extendedHandle,
							ChartXTabUIUtil.isTransposedChartWithAxes( cm ),
							(ChartWithAxes) contextResult.getModel( ) );
				}
				commandStack.commit( );
				return Window.OK;
			}
			else if ( applyData[0] != null )
			{
				// Pressing Cancel but Apply was pressed before, so revert to
				// the point pressing Apply
				commandStack.rollback( );
				updateModel( extendedHandle,
						chartBuilder,
						crii,
						cm,
						(Chart) applyData[0],
						(String) applyData[1],
						(Boolean) applyData[2] );
				if ( dataProvider.isPartChart( ) )
				{
					commandStack.startTrans( TRANS_NAME );
					ChartXTabUIUtil.updateXTabForAxis( ChartCubeUtil.getXtabContainerCell( extendedHandle ),
							extendedHandle,
							ChartXTabUIUtil.isTransposedChartWithAxes( cm ),
							(ChartWithAxes) applyData[0] );
					commandStack.commit( );
				}
				return Window.OK;
			}
			commandStack.rollback( );
			return Window.CANCEL;
		}
		catch ( Exception e )
		{
			throw new RuntimeException( e );
		}
		finally
		{
			iInstanceCount--;
			// Reset the ExtendedItemHandle instance since it is no
			// longer needed
			this.extendedHandle = null;
			isChartWizardOpen = false;
			if ( livePreviewThread != null )
			{
				livePreviewThread.end( );
			}
//			
//			if ( dataProvider != null )
//			{
//				dataProvider.dispose();
//			}
		}
	}
	
	private void maintainCompatibility( Chart cm )
	{
		// Revise chart version to current.
		ChartUtil.reviseVersion( cm );
		
		// Make it compatible with old model
		if ( cm.getInteractivity( ) == null )
		{
			Interactivity interactivity = InteractivityImpl.create( );
			interactivity.eAdapters( ).addAll( cm.eAdapters( ) );
			cm.setInteractivity( interactivity );
		}
		if ( cm.getLegend( ).getSeparator( ) == null )
		{
			LineAttributes separator = LineAttributesImpl.create( ColorDefinitionImpl.BLACK( ),
					LineStyle.SOLID_LITERAL,
					1 );
			separator.setVisible( true );
			separator.eAdapters( ).addAll( cm.eAdapters( ) );
			cm.getLegend( ).setSeparator( separator );
		}
		if ( cm.getExtendedProperties( ).isEmpty( ) )
		{
			ChartModelHelper.instance( )
					.updateExtendedProperties( cm.getExtendedProperties( ) );
		}
	}

	private void updateModel( ExtendedItemHandle eih, ChartWizard chartBuilder,
			ChartReportItemImpl crii, Chart cmOld, Chart cmNew,
			String outputFormat, boolean bInheritColumnsOnly )
	{		
		try
		{
			// update the output format property information.
			eih.setProperty( ChartReportItemConstants.PROPERTY_OUTPUT, outputFormat );
			eih.setProperty( ChartReportItemConstants.PROPERTY_INHERIT_COLUMNS,
					bInheritColumnsOnly );

			// TODO: Added till the model team sorts out pass-through
			// for setProperty
			crii.executeSetModelCommand( eih, cmOld, cmNew );

			// Resizes chart with a default value when the size is zero or null
			if ( cmNew.getBlock( ).getBounds( ) == null
					|| cmNew.getBlock( ).getBounds( ).getWidth( ) == 0
					|| cmNew.getBlock( ).getBounds( ).getHeight( ) == 0 )
			{
				cmNew.getBlock( )
						.setBounds( ChartItemUtil.createDefaultChartBounds( eih,
								cmNew ) );
			}

			final Bounds bo = cmNew.getBlock( ).getBounds( );

			// Modified to fix Bugzilla #99331
			NumberFormat nf = ChartUIUtil.getDefaultNumberFormatInstance( );

			if ( eih.getWidth( ).getStringValue( ) == null )
			{
				eih.setWidth( nf.format( bo.getWidth( ) ) + "pt" ); //$NON-NLS-1$
			}
			if ( eih.getHeight( ).getStringValue( ) == null )
			{
				eih.setHeight( nf.format( bo.getHeight( ) ) + "pt" ); //$NON-NLS-1$
			}
		}
		catch ( SemanticException smx )
		{
			logger.log( smx );
		}

		if ( crii.getDesignerRepresentation( ) != null )
		{
			( (DesignerRepresentation) crii.getDesignerRepresentation( ) ).setDirty( true );
		}
	}

	/**
	 * @deprecated
	 */
	public String invoke( String sExpression, Object oContext, String sTitle )
	{
		final ExpressionBuilder eb = new ExpressionBuilder( sExpression );
		eb.setExpressionProvider( new ExpressionProvider( (ExtendedItemHandle) oContext ) );
		if ( sTitle != null )
		{
			eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
		}
		if ( eb.open( ) == Window.OK )
		{
			sExpression = eb.getResult( );
		}
		return sExpression;
	}

	/**
	 * @deprecated
	 */
	public String invoke( String sExpression, Object oContext, String sTitle,
			boolean isChartProvider )
	{
		final ExpressionBuilder eb = new ExpressionBuilder( sExpression );
		eb.setExpressionProvider( new ChartExpressionProvider( ) );
		if ( sTitle != null )
		{
			eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
		}
		if ( eb.open( ) == Window.OK )
		{
			sExpression = eb.getResult( );
		}
		return sExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#validate(org.eclipse.birt.chart.model.Chart,
	 *      java.lang.Object)
	 */
	public String[] validate( Chart cm, Object oContext )
	{
		final ArrayList<String> alProblems = new ArrayList<String>( 4 );

		// CHECK FOR UNBOUND DATASET
		final ExtendedItemHandle eih = (ExtendedItemHandle) oContext;
		if ( DEUtil.getDataSetList( eih ).size( ) == 0
				&& ChartCubeUtil.getBindingCube( eih ) == null )
		{
			alProblems.add( Messages.getString( "ChartReportItemBuilderImpl.problem.hasNotBeenFound" ) ); //$NON-NLS-1$
		}

		// CHECK FOR UNDEFINED SERIES QUERIES (DO NOT NEED THE RUNTIME CONTEXT)
		final QueryUIHelper.SeriesQueries[] qsqa = new QueryUIHelper( ).getSeriesQueryDefinitions( cm );
		Collection<String> co;
		for ( int i = 0; i < qsqa.length; i++ )
		{
			co = qsqa[i].validate( );
			if ( co != null )
			{
				alProblems.addAll( co );
			}
		}

		return alProblems.toArray( new String[alProblems.size( )] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getRegisteredKeys()
	 */
	@SuppressWarnings("unchecked")
	public final List getRegisteredKeys( )
	{
		return extendedHandle.getModuleHandle( ).getMessageKeys( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getValue(java.lang.String)
	 */
	public final String getValue( String sKey )
	{
		String value = extendedHandle.getModuleHandle( ).getMessage( sKey );
		if ( value == null || value.equals( "" ) ) //$NON-NLS-1$
		{
			return sKey;
		}
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getConvertedValue(double,
	 *      java.lang.String, java.lang.String)
	 */
	public final double getConvertedValue( double dOriginalValue,
			String sFromUnits, String sToUnits )
	{
		if ( sFromUnits == null || sToUnits == null )
		{
			return dOriginalValue;
		}
		double dResult = -1d;

		// CONVERT FROM PIXELS
		final IDisplayServer ids = ChartUIUtil.getDisplayServer( );
		if ( sFromUnits.equalsIgnoreCase( "pixels" ) ) //$NON-NLS-1$
		{
			dOriginalValue = ( dOriginalValue * 72d ) / ids.getDpiResolution( );
		}

		// Convert to target units - Will convert to Points if target is Points,
		// Pixels or Unknown
		dResult = ( DimensionUtil.convertTo( dOriginalValue,
				getBIRTUnitsFor( sFromUnits ),
				getBIRTUnitsFor( sToUnits ) ) ).getMeasure( );

		// Special handling to convert TO Pixels
		if ( sToUnits.equalsIgnoreCase( "pixels" ) ) //$NON-NLS-1$
		{
			dResult = ( ids.getDpiResolution( ) * dResult ) / 72d;
		}
		return dResult;
	}

	/**
	 * @param sUnits
	 */
	private static String getBIRTUnitsFor( String sUnits )
	{
		if ( sUnits.equalsIgnoreCase( "inches" ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.UNITS_IN;
		}
		else if ( sUnits.equalsIgnoreCase( "centimeters" ) ) //$NON-NLS-1$
		{
			return DesignChoiceConstants.UNITS_CM;
		}
		else
		{
			return DesignChoiceConstants.UNITS_PT;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#invoke(int,
	 *      java.lang.String, java.lang.Object, java.lang.String)
	 */
	public String invoke( int command, String value, final Object context,
			String sTitle ) throws ChartException
	{
		final ExpressionProvider ep = new ChartExpressionProvider( (ExtendedItemHandle) context,
				wizardContext,
				getExpressionBuilderStyle( command ) );
		Shell shell = null;

		switch ( command )
		{
			case COMMAND_HYPERLINK :
			case COMMAND_HYPERLINK_DATAPOINTS :
			case COMMAND_HYPERLINK_LEGEND :
				shell = new Shell( Display.getDefault( ), SWT.DIALOG_TRIM
						| SWT.RESIZE
						| SWT.APPLICATION_MODAL );
				ChartUIUtil.bindHelp( shell,
						ChartHelpContextIds.DIALOG_EDIT_URL );
				HyperlinkBuilder hb = new HyperlinkBuilder( shell, true ) {

					@Override
					protected ExpressionProvider getExpressionProvider( )
					{
						return ep;
					}

				};
				try
				{
					hb.setInputString( value, extendedHandle );
					if ( sTitle != null )
					{
						hb.setTitle( hb.getTitle( ) + " - " + sTitle ); //$NON-NLS-1$
					}
					if ( hb.open( ) == Window.OK )
					{
						value = hb.getResultString( );
					}
				}
				catch ( Exception e )
				{
					throw new ChartException( ChartReportItemUIActivator.ID,
							ChartException.UNDEFINED_VALUE,
							e );
				}
				break;

			case COMMAND_EXPRESSION_CHART_DATAPOINTS :
			case COMMAND_EXPRESSION_DATA_BINDINGS :
			case COMMAND_EXPRESSION_TRIGGERS_SIMPLE :
			case COMMAND_EXPRESSION_SCRIPT_DATAPOINTS :
			case COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS :
				shell = new Shell( Display.getDefault( ), SWT.DIALOG_TRIM
						| SWT.RESIZE
						| SWT.APPLICATION_MODAL );
				ChartUIUtil.bindHelp( shell,
						ChartHelpContextIds.DIALOG_EXPRESSION_BUILDER );
				ExpressionBuilder eb = new ExpressionBuilder( shell, value );
				eb.setExpressionProvider( ep );
				if ( sTitle != null )
				{
					eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
				}
				if ( eb.open( ) == Window.OK )
				{
					value = eb.getResult( );
				}
		}

		return value;
	}

	public boolean isInvokingSupported( )
	{
		return true;
	}

	public boolean isEclipseModeSupported( )
	{
		return true;
	}

	/**
	 * Returns the categories list in BIRT chart expression builder
	 * 
	 * @param builderCommand
	 * @return category style
	 */
	private int getExpressionBuilderStyle( int builderCommand )
	{
		if ( builderCommand == COMMAND_EXPRESSION_DATA_BINDINGS )
		{
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS;
		}
		else if ( builderCommand == COMMAND_EXPRESSION_CHART_DATAPOINTS )
		{
			return ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		}
		else if ( builderCommand == COMMAND_EXPRESSION_SCRIPT_DATAPOINTS )
		{
			// Script doesn't support column binding expression.
			return ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT;
		}
		else if ( builderCommand == COMMAND_EXPRESSION_TRIGGERS_SIMPLE )
		{
			// Bugzilla#202386: Tooltips never support chart
			// variables. Use COMMAND_EXPRESSION_TRIGGERS_SIMPLE for un-dp
			return ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT;
		}
		else if ( builderCommand == COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS )
		{
			// Bugzilla#202386: Tooltips never support chart
			// variables. Use COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS for dp
			return ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		}
		else if ( builderCommand == COMMAND_HYPERLINK )
		{
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS;
		}
		else if ( builderCommand == COMMAND_HYPERLINK_DATAPOINTS )
		{
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		}
		else if ( builderCommand == COMMAND_HYPERLINK_LEGEND )
		{
			// Add Legend item variables and remove column bindings
			return ChartExpressionProvider.CATEGORY_WITH_LEGEND_ITEMS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT
					| ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES;
		}
		return ChartExpressionProvider.CATEGORY_BASE;
	}

	/**
	 * ChartExpressionHelper
	 */
	private static class ChartExpressionHelper extends ExpressionHelper
	{
		@Override
		public void notifyExpressionChangeEvent( String oldExpression,
				String newExpression )
		{
			if ( listener != null )
			{
				Event event = new Event( );
				event.widget = button.getControl( );
				event.data = new String[]{
						oldExpression, newExpression
				};
				event.detail = SWT.Modify;
				listener.handleEvent( event );
			}
		}

		@Override
		public String getExpression( )
		{
			if ( control.isDisposed( ) )
			{
				return ""; //$NON-NLS-1$
			}
			return ChartUIUtil.getText( control );
		}

		@Override
		public void setExpression( String expression )
		{
			if ( control.isDisposed( ) )
			{
				return;
			}
			ChartUIUtil.setText( control, DEUtil.resolveNull( expression ) );
		}

	}

	protected static class ChartExpressionButton implements IExpressionButton
	{

		private final ExpressionCodec codec = ChartModelHelper.instance( )
				.createExpressionCodec( );

		protected final ExpressionButton eb;

		protected final IExpressionHelper eHelper;

		public ChartExpressionButton( Composite parent, Control control,
				ExtendedItemHandle eih, IExpressionProvider ep,
				Listener listener )
		{
			this.eHelper = new ChartExpressionHelper( );
			eb = ExpressionButtonUtil.createExpressionButton( parent,
					control,
					ep,
					eih,
					listener,
					false,
					SWT.PUSH,
					(ChartExpressionHelper) eHelper );
			ExpressionButtonUtil.initExpressionButtonControl( control,
					(Expression) null );
		}

		public ChartExpressionButton( ExpressionButton eb )
		{
			this.eb = eb;
			this.eHelper = eb.getExpressionHelper( );
		}

		public String getExpression( )
		{
			codec.setExpression( eHelper.getExpression( ) );
			codec.setType( eHelper.getExpressionType( ) );
			return codec.encode( );
		}
	
		public void setExpression( String expr )
		{
			if ( expr != null && expr.length( ) > 0 )
			{
				codec.decode( expr );
				eHelper.setExpression( codec.getExpression( ) );
				eHelper.setExpressionType( codec.getType( ) );
			}
			else
			{
				eHelper.setExpression( codec.getExpression( ) );
			}
			eb.refresh( );
		}

		public String getExpressionString( )
		{
			return eHelper.getExpression( );
		}

		public boolean isEnabled( )
		{
			return eb.isEnabled( );
		}

		public void setEnabled( boolean bEnabled )
		{
			eb.setEnabled( bEnabled );
		}

	}

	public Object invoke( Command command, Object... inData )
			throws ChartException
	{
		Object outData = null;
		switch ( command )
		{
			case EXPRESS_BUTTON_CREATE :
				if ( inData.length == 5 )
				{
					Composite parent = (Composite) inData[0];
					Control control = (Control) inData[1];
					ExtendedItemHandle eih = (ExtendedItemHandle) inData[2];
					int iCode = (Integer) inData[3];
					Listener listener = (Listener) inData[4];

					IExpressionProvider ep = new ChartExpressionProvider( eih,
							wizardContext,
							getExpressionBuilderStyle( iCode ) );

					ChartExpressionButton ceb = new ChartExpressionButton( parent,
							control,
							eih,
							ep,
							listener );

					outData = ceb;
				}
				break;
		}
		return outData;
	}
}