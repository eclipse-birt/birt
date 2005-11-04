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

package org.eclipse.birt.report.designer.ui.lib.explorer.dnd;

import org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDragAdapter;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.jface.viewers.StructuredViewer;

/**
 * Library tree viewer drag listener.
 */

public class LibraryDragListener extends DesignElementDragAdapter
{

	public LibraryDragListener( StructuredViewer viewer )
	{
		super( viewer );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dnd.DesignElementDragAdapter#validateTransfer(java.lang.Object)
	 */
	protected boolean validateTransfer( Object transfer )
	{
		if ( transfer instanceof ReportElementHandle )
		{
			if ( transfer instanceof DataSetHandle
					|| transfer instanceof DataSourceHandle
					|| transfer instanceof MasterPageHandle )
			{
				return false;
			}
			return true;
		}
		return false;
	}

}