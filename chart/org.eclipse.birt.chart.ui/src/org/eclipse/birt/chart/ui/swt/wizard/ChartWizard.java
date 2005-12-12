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
import org.eclipse.birt.chart.model.ScriptHandler;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskChangeListener;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.birt.core.ui.frameworks.taskwizard.TasksManager;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.IWizardContext;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartWizard extends WizardBase
{

	public static final String WIZARD_ID = "org.eclipse.birt.chart.ui.ChartWizard"; //$NON-NLS-1$

	private static final double DEFAULT_CHART_HEIGHT = 130;// 250;

	private static final double DEFAULT_CHART_WIDTH = 212;// 400;

	private static final double DEFAULT_CHART_WITHOUT_AXIS_HEIGHT = 130;// 250;

	private static final double DEFAULT_CHART_WITHOUT_AXIS_WIDTH = 212;// 280;

	private ChartAdapter adapter = null;

	public ChartWizard( )
	{
		super( WIZARD_ID,
				SWT.DEFAULT,
				SWT.DEFAULT,
				Messages.getString( "ChartWizard.Title.ChartBuilder" ), //$NON-NLS-1$
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

	private void updateDefaultScript( Chart chart )
	{
		if ( chart.getScript( ) == null || chart.getScript( ).length( ) == 0 )
		{
			chart.setScript( ScriptHandler.DEFAULT_JAVASCRIPT );
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
			Chart chart = getContext( ).getModel( );
			if ( chart != null )
			{
				// Set size if size is zero
				resizeChart( chart );

				// Update default script content.
				updateDefaultScript( chart );

				// Remove all adapters
				removeAllAdapters( chart );
			}
		}
	}

	public EContentAdapter getAdapter( )
	{
		return adapter;
	}

	protected ChartWizardContext getContext( )
	{
		return (ChartWizardContext) context;
	}

	public String[] validate( )
	{
		return getContext( ).getUIServiceProvider( )
				.validate( getContext( ).getModel( ),
						getContext( ).getExtendedItem( ) );
	}

	public IWizardContext open( String[] sTasks, String topTaskId,
			IWizardContext initialContext )
	{
		Chart chart = ( (ChartWizardContext) initialContext ).getModel( );
		if ( chart == null )
		{
			setTitle( Messages.getString( "ChartWizard.Title.NewChart" ) ); //$NON-NLS-1$
		}
		else
		{
			setTitle( Messages.getString( "ChartWizard.Title.EditChart" ) ); //$NON-NLS-1$
			// Add adapters to chart model
			chart.eAdapters( ).add( adapter );
		}
		return super.open( sTasks, topTaskId, initialContext );
	}
}
