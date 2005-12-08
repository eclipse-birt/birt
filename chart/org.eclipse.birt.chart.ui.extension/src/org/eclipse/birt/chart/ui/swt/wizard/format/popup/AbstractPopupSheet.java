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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.ui.swt.interfaces.ITaskPopupSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public abstract class AbstractPopupSheet implements ITaskPopupSheet
{

	private transient ChartWizardContext context;

	protected transient IUIServiceProvider serviceprovider;

	protected transient Chart chart = null;

	protected transient Composite cmpTop = null;

	private boolean needRefresh = false;

	public AbstractPopupSheet( Composite parent, ChartWizardContext context,
			boolean needRefresh )
	{
		super( );
		this.context = context;
		this.chart = context.getModel( );
		serviceprovider = context.getUIServiceProvider( );
		this.needRefresh = needRefresh;
	}

	public AbstractPopupSheet( )
	{
		super( );
	}

	abstract protected Composite getComponent( Composite parent );

	public void refreshComponent( Composite parent )
	{
		if ( needRefresh )
		{
			if ( cmpTop != null && !cmpTop.isDisposed( ) )
			{
				cmpTop.dispose( );
			}
			cmpTop = getComponent( parent );
			parent.layout( );
		}
	}

	protected void setIgnoreNotifications( boolean bIgnoreNotifications )
	{
		ChartAdapter.ignoreNotifications( bIgnoreNotifications );
	}

	protected ChartWizardContext getContext( )
	{
		return context;
	}
}
