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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;

/**
 * 
 */

public class ComputedColumnExpressionFilter extends ExpressionFilter
{

	protected TableViewer tableViewer;

	public ComputedColumnExpressionFilter( TableViewer tableViewer )
	{
		super( );
		this.tableViewer = tableViewer;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter#select(java.lang.Object,
	 *      java.lang.Object)
	 */
	public boolean select( Object parentElement, Object element )
	{
		if ( tableViewer != null )
		{
			IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection( );
			Object obj = selection.getFirstElement( );
			if ( obj instanceof ComputedColumnHandle
					&& element instanceof ComputedColumnHandle )
			{
				ComputedColumnHandle objHandle = (ComputedColumnHandle) obj;
				ComputedColumnHandle elementHandle = (ComputedColumnHandle) element;
				if ( objHandle.getStructure( ) == elementHandle.getStructure( ) )
				{
					return false;
				}
			}
			return true;
		}
		
		return true;
	}

}
