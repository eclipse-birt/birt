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

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tree viewer content provider adapter for resource browser.
 * 
 */

public class DataContentProvider implements ITreeContentProvider
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren( Object parentElement )
	{
		if ( parentElement instanceof Object[] )
		{
			return (Object[]) parentElement;
		}
		// if ( parentElement instanceof DataMock )
		// {
		// return new Object[]{
		// ( (DataMock) parentElement ).getSource( )
		// };
		// }
		// if ( parentElement instanceof DataSource )
		// {
		// return ( (DataSource) parentElement ).getDatasets( );
		// }
		if ( parentElement instanceof DataSetHandle )
		{
			return OlapUtil.getDataFields( (DataSetHandle) parentElement );
		}
		return new Object[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
	 */
	public Object getParent( Object element )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren( Object element )
	{
		if ( element instanceof Object[] )
		{
			return ( (Object[]) element ).length > 0;
		}
		// if ( element instanceof DataMock )
		// {
		// return ( (DataMock) element ).getSource( )!=null;
		// }
		// if ( element instanceof DataSource )
		// {
		// return ( (DataSource) element ).getDatasets( ).length > 0;
		// }
		if ( element instanceof DataSetHandle )
		{
			return OlapUtil.getDataFields( (DataSetHandle) element ).length > 0;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	public Object[] getElements( Object inputElement )
	{
		return getChildren( inputElement );
	}

	public void dispose( )
	{

	}

	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{

	}

}
