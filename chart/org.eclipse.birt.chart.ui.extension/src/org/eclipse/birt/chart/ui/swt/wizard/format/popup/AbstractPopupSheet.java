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
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.widgets.Composite;

/**
 * 
 */

public abstract class AbstractPopupSheet implements ITaskPopupSheet
{

	private transient ChartWizardContext context;

	protected transient Composite cmpTop = null;

	private boolean needRefresh = false;

	private String strTitle;

	public AbstractPopupSheet( String title, ChartWizardContext context,
			boolean needRefresh )
	{
		super( );
		this.strTitle = title;
		this.context = context;
		this.needRefresh = needRefresh;
	}

	abstract protected Composite getComponent( Composite parent );

	public Composite getUI( Composite parent )
	{
		// Cache the top composite for refresh later
		cmpTop = getComponent( parent );
		return cmpTop;
	}

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

	protected Chart getChart( )
	{
		return getContext( ).getModel( );
	}

	protected void setTitle( String title )
	{
		this.strTitle = title;
	}

	public String getTitle( )
	{
		return strTitle;
	}
}
