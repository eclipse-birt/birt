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

package org.eclipse.birt.report.item.crosstab.ui.extension;

import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;

/**
 * AggregationCellViewAdapter
 */
public abstract class AggregationCellViewAdapter implements
		IAggregationCellViewProvider
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider#matchView(org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle)
	 */
	public boolean matchView( AggregationCellHandle cell )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider#restoreView(org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle)
	 */
	public void restoreView( AggregationCellHandle cell )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider#switchView(org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle)
	 */
	public void switchView( AggregationCellHandle cell )
	{
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider#updateView(org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle)
	 */
	public void updateView( AggregationCellHandle cell )
	{
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.item.crosstab.ui.extension.IAggregationCellViewProvider#canSwitch(org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle)
	 */
	public boolean canSwitch( AggregationCellHandle cell )
	{
		return true;
	}
}
