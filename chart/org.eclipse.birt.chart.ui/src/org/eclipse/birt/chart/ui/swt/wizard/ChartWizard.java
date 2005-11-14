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

package org.eclipse.birt.chart.ui.swt.wizard;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.TasksManager;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.swt.events.DisposeEvent;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartWizard extends WizardBase
{

	public static final String WIZARD_ID = "org.eclipse.birt.chart.ui.ChartWizard"; //$NON-NLS-1$

	private static final int DEFAULT_WIZARD_WIDTH = 750;

	private static final int DEFAULT_WIZARD_HEIGHT = 700;

	private static final double DEFAULT_CHART_HEIGHT = 250;

	private static final double DEFAULT_CHART_WIDTH = 400;

	private static final double DEFAULT_CHART_WITHOUT_AXIS_HEIGHT = 250;

	private static final double DEFAULT_CHART_WITHOUT_AXIS_WIDTH = 280;

	private ChartAdapter adapter = null;

	public ChartWizard( )
	{
		super( WIZARD_ID,
				DEFAULT_WIZARD_WIDTH,
				DEFAULT_WIZARD_HEIGHT,
				Messages.getString( "ChartWizard.ChartBuilder" ), //$NON-NLS-1$
				UIHelper.getImage( "icons/obj16/chartselector.gif" ) ); //$NON-NLS-1$
		adapter = new ChartAdapter( this );
	}

	public void addTask( String sTaskID )
	{
		super.addTask( sTaskID );
		ITask task = TasksManager.instance( ).getTask( sTaskID );
		if ( task instanceof ITaskChangeListener )
		{
			adapter.addListener( (ITaskChangeListener) task );
		}
	}

	private void resizeChart( Chart chartModelCurrent )
	{
		if ( chartModelCurrent.getBlock( ).getBounds( ) == null
				|| chartModelCurrent.getBlock( ).getBounds( ).getWidth( ) == 0
				|| chartModelCurrent.getBlock( ).getBounds( ).getHeight( ) == 0 )
		{
			if ( chartModelCurrent instanceof ChartWithoutAxesImpl )
			{
				chartModelCurrent.getBlock( ).setBounds( BoundsImpl.create( 0,
						0,
						DEFAULT_CHART_WITHOUT_AXIS_WIDTH,
						DEFAULT_CHART_WITHOUT_AXIS_HEIGHT ) );
			}
			else
			{
				chartModelCurrent.getBlock( ).setBounds( BoundsImpl.create( 0,
						0,
						DEFAULT_CHART_WIDTH,
						DEFAULT_CHART_HEIGHT ) );
			}
		}
	}

	private void removeAllAdapters( Chart chart )
	{
		chart.eAdapters( ).remove( adapter );
		TreeIterator iterator = chart.eAllContents( );
		while ( iterator.hasNext( ) )
		{
			Object oModel = iterator.next( );
			if ( oModel instanceof EObject )
			{
				( (EObject) oModel ).eAdapters( ).remove( adapter );
			}
		}
	}

	public void widgetDisposed( DisposeEvent e )
	{
		super.widgetDisposed( e );
		if ( context != null )
		{
			Chart chart = ( (ChartWizardContext) context ).getModel( );
			if ( chart != null )
			{
				// Set size if size is zero
				resizeChart( chart );

				// Remove all adapters
				removeAllAdapters( chart );
			}
		}
	}

	public EContentAdapter getAdapter( )
	{
		return adapter;
	}

	// protected void checkBeforeSaving( )
	// {
	// super.checkBeforeSaving( );
	// Chart chart = ( (ChartWizardContext) context ).getModel( );
	// List listError = new ArrayList( );
	// List listFixs = new ArrayList( );
	//
	// if ( !ChartUIUtil.checkDataBinding( chart ) )
	// {
	// listError.add( "Data binding is null!" );
	// listFixs.add( "Please check every series data binding existent!" );
	// }
	// displayError( list2StringArray( listError ),
	// list2StringArray( listFixs ),
	// null,
	// context,
	// null );
	// }
	//
	// private String[] list2StringArray( List list )
	// {
	// return (String[]) list.toArray( new String[list.size( )] );
	// }
}
