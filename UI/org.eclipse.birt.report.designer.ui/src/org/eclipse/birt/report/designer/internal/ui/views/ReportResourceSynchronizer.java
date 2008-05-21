/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeEvent;
import org.eclipse.birt.report.designer.ui.views.IReportResourceChangeListener;
import org.eclipse.birt.report.designer.ui.views.IReportResourceSynchronizer;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;

/**
 * ReportResourceSynchronizer
 */
public class ReportResourceSynchronizer implements IReportResourceSynchronizer
{

	protected ListenerList listeners = new ListenerList( );

	protected boolean disabled = false;

	public ReportResourceSynchronizer( )
	{
	}

	public void addListener( IReportResourceChangeListener listener )
	{
		if ( disabled )
		{
			return;
		}

		listeners.add( listener );
	}

	public void removeListener( IReportResourceChangeListener listener )
	{
		if ( disabled )
		{
			return;
		}

		listeners.remove( listener );
	}

	protected void notifyListeners( final IReportResourceChangeEvent event )
	{
		System.out.println(event);
		
		Object[] list = listeners.getListeners( );

		for ( int i = 0; i < list.length; i++ )
		{
			final IReportResourceChangeListener rcl = (IReportResourceChangeListener) list[i];

			SafeRunner.run( new SafeRunnable( ) {

				public void run( ) throws Exception
				{
					rcl.resourceChanged( event );
				}
			} );
		}

	}

	public void notifyResourceChanged( IReportResourceChangeEvent event )
	{
		if ( disabled )
		{
			return;
		}

		notifyListeners( event );
	}

}
