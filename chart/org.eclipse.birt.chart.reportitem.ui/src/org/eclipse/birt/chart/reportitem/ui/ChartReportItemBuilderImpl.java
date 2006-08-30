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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartReportStyleProcessor;
import org.eclipse.birt.chart.reportitem.QueryHelper;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartExpressionProvider;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ApplyButtonHandler;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.HyperlinkBuilder;
import org.eclipse.birt.report.designer.ui.extensions.ReportItemBuilderUI;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.text.NumberFormat;

/**
 * ChartReportItemBuilderImpl
 */
public class ChartReportItemBuilderImpl extends ReportItemBuilderUI
		implements
			IUIServiceProvider
{

	private static int iInstanceCount = 0;

	private transient ExtendedItemHandle extendedHandle = null;

	private transient String taskId = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.reportitem/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public ChartReportItemBuilderImpl( )
	{
		super( );
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

		try
		{
			IReportItem item = null;
			try
			{
				item = eih.getReportItem( );
				if ( item == null )
				{
					eih.loadExtendedElement( );
					item = eih.getReportItem( );
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
			
			final CommandStack commandStack = eih.getRoot( ).getCommandStack( );
			final String TRANS_NAME = "chart builder internal transaction"; //$NON-NLS-1$
			commandStack.startTrans( TRANS_NAME );

			final ChartReportItemImpl crii = ( (ChartReportItemImpl) item );
			final Chart cm = (Chart) crii.getProperty( "chart.instance" ); //$NON-NLS-1$
			final Chart cmClone = ( cm == null ) ? null
					: (Chart) EcoreUtil.copy( cm );
			// This array is for storing the latest chart data before pressing
			// apply button
			final Object[] applyData = new Object[2];

			// Set the ExtendedItemHandle instance (for use by the Chart Builder
			// UI
			this.extendedHandle = eih;

			// Use workbench shell to open the dialog
			Shell parentShell = null;
			if ( PlatformUI.isWorkbenchRunning( ) )
			{
				parentShell = PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( );
			}
			final ChartWizard chartBuilder = new ChartWizard( parentShell );
			final ChartWizardContext context = new ChartWizardContext( cmClone );
			chartBuilder.addCustomButton( new ApplyButtonHandler( chartBuilder ) {

				public void run( )
				{
					super.run( );
					// Save the data when applying
					applyData[0] = EcoreUtil.copy( context.getModel( ) );
					applyData[1] = context.getOutputFormat( );

					commandStack.commit( );
					commandStack.startTrans( TRANS_NAME );
				}
			} );

			context.setUIServiceProvider( this );
			context.setDataServiceProvider( new ReportDataServiceProvider( eih ) );
			context.setRtL( ReportItemUIUtil.isRtl( ) );
			Object of = eih.getProperty( "outputFormat" ); //$NON-NLS-1$
			if ( of instanceof String )
			{
				// GIF is deprecated in favor of PNG. Automatically update
				// model
				if ( of.equals( "GIF" ) ) //$NON-NLS-1$
				{
					context.setOutputFormat( "PNG" ); //$NON-NLS-1$
				}
				else
					context.setOutputFormat( (String) of );
			}
			context.setExtendedItem( eih );
			context.setProcessor( new ChartReportStyleProcessor( eih, false ) );
			ChartWizardContext contextResult = (ChartWizardContext) chartBuilder.open( null,
					taskId,
					context );
			if ( contextResult != null && contextResult.getModel( ) != null )
			{
				// Pressing Finish
				commandStack.commit( );
				updateModel( eih,
						chartBuilder,
						crii,
						cm,
						contextResult.getModel( ),
						contextResult.getOutputFormat( ) );
				return Window.OK;
			}
			else if ( applyData[0] != null )
			{
				// Pressing Cancel but Apply was pressed before, so revert to
				// the point pressing Apply
				commandStack.rollback( );
				updateModel( eih,
						chartBuilder,
						crii,
						cm,
						(Chart) applyData[0],
						(String) applyData[1] );
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
		}
	}

	private void updateModel( ExtendedItemHandle eih, ChartWizard chartBuilder,
			ChartReportItemImpl crii, Chart cmOld, Chart cmNew,
			String outputFormat )
	{
		try
		{
			// update the output format property information.
			eih.setProperty( "outputFormat", outputFormat );//$NON-NLS-1$

			// TODO: Added till the model team sorts out pass-through
			// for setProperty
			crii.executeSetModelCommand( eih, cmOld, cmNew );

			// Resizes chart with a default value when the size is zero or null
			chartBuilder.resizeChart( cmNew );

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IExpressionBuilder#invoke(java.lang.String)
	 */
	public String invoke( String sExpression, Object oContext, String sTitle )
	{
		final ExpressionBuilder eb = new ExpressionBuilder( sExpression );
		eb.setExpressionProvier( new ExpressionProvider( (ExtendedItemHandle) oContext ) );
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

	public String invoke( String sExpression, Object oContext, String sTitle,
			boolean isChartProvider )
	{
		final ExpressionBuilder eb = new ExpressionBuilder( sExpression );
		eb.setExpressionProvier( new ChartExpressionProvider( ) );
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
	public final String[] validate( Chart cm, Object oContext )
	{
		final ArrayList alProblems = new ArrayList( 4 );

		// CHECK FOR UNBOUND DATASET
		final ExtendedItemHandle eih = (ExtendedItemHandle) oContext;
		if ( DEUtil.getDataSetList( eih ).size( ) == 0 )
		{
			alProblems.add( Messages.getString( "ChartReportItemBuilderImpl.problem.hasNotBeenFound" ) ); //$NON-NLS-1$
		}

		// CHECK FOR UNDEFINED SERIES QUERIES (DO NOT NEED THE RUNTIME CONTEXT)
		final QueryHelper.SeriesQueries[] qsqa = new QueryHelper( ).getSeriesQueryDefinitions( cm );
		Collection co;
		for ( int i = 0; i < qsqa.length; i++ )
		{
			co = qsqa[i].validate( );
			if ( co != null )
			{
				alProblems.addAll( co );
			}
		}

		return (String[]) alProblems.toArray( new String[alProblems.size( )] );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider#getRegisteredKeys()
	 */
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
		ExpressionBuilder eb = null;
		Shell shell = null;

		switch ( command )
		{
			case IUIServiceProvider.COMMAND_EXPRESSION :
				shell = new Shell( Display.getDefault( ), SWT.DIALOG_TRIM
						| SWT.RESIZE | SWT.APPLICATION_MODAL );
				ChartUIUtil.bindHelp( shell,
						ChartHelpContextIds.DIALOG_EXPRESSION_BUILDER );
				eb = new ExpressionBuilder( shell, value );
				ExpressionProvider ep = new ExpressionProvider( (ExtendedItemHandle) context );
				ep.addFilter( new ExpressionFilter( ) {

					public boolean select( Object parentElement, Object element )
					{
						// Remove unsupported expression. See bugzilla#132768
						return !( parentElement.equals( ExpressionProvider.BIRT_OBJECTS )
								&& element instanceof IClassInfo && ( (IClassInfo) element ).getName( )
								.equals( "Total" ) ); //$NON-NLS-1$
					}
				} );
				eb.setExpressionProvier( ep );
				if ( sTitle != null )
				{
					eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
				}
				if ( eb.open( ) == Window.OK )
				{
					value = eb.getResult( );
				}
				break;
			case IUIServiceProvider.COMMAND_CHART_EXPRESSION :
				eb = new ExpressionBuilder( value );
				eb.setExpressionProvier( new ChartExpressionProvider( ) );
				if ( sTitle != null )
				{
					eb.setDialogTitle( eb.getDialogTitle( ) + " - " + sTitle ); //$NON-NLS-1$
				}
				if ( eb.open( ) == Window.OK )
				{
					value = eb.getResult( );
				}
				break;
			case IUIServiceProvider.COMMAND_HYPERLINK :
				shell = new Shell( Display.getDefault( ), SWT.DIALOG_TRIM
						| SWT.RESIZE | SWT.APPLICATION_MODAL );
				ChartUIUtil.bindHelp( shell,
						ChartHelpContextIds.DIALOG_EDIT_URL );
				HyperlinkBuilder hb = new HyperlinkBuilder( shell ) {

					protected void configureExpressionBuilder(
							ExpressionBuilder builder )
					{
						builder.setExpressionProvier( new ExpressionProvider( (ExtendedItemHandle) context ) );
					}

				};
				try
				{
					hb.setInputString( value );
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
		}

		return value;
	}

	public boolean isInvokingSupported( )
	{
		return true;
	}
}