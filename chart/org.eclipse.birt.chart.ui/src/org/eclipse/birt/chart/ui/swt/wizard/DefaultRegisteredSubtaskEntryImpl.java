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

import org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSubtaskEntry;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;

/**
 * @author Actuate Corporation
 * 
 */
public class DefaultRegisteredSubtaskEntryImpl
		implements
			IRegisteredSubtaskEntry
{

	private transient String sNodeIndex = ""; //$NON-NLS-1$

	private transient String sNodePath = ""; //$NON-NLS-1$

	private transient String sDisplayName = null;

	private transient ISubtaskSheet sheetImpl = null;

	public DefaultRegisteredSubtaskEntryImpl( String sNodeIndex,
			String sNodePath, String sDisplayName, ISubtaskSheet sheet )
	{
		try
		{
			this.sNodeIndex = Integer.valueOf( sNodeIndex ).toString( );
		}
		catch ( NumberFormatException e )
		{
			sNodeIndex = "100"; //$NON-NLS-1$
		}
		this.sNodePath = sNodePath;
		this.sDisplayName = sDisplayName;
		this.sheetImpl = sheet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSheetEntry#getRegisteredNodePath()
	 */
	public int getNodeIndex( )
	{
		return Integer.valueOf( sNodeIndex ).intValue( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSheetEntry#getRegisteredNodePath()
	 */
	public String getNodePath( )
	{
		return sNodePath;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSheetEntry#getSheetClass()
	 */
	public ISubtaskSheet getSheet( )
	{
		return sheetImpl;
	}

	public String getDisplayName( )
	{
		return sDisplayName;
	}

}