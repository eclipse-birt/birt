/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.VirtualField;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tree viewer content provider adapter for resource browser.
 * 
 */

public class DataContentProvider implements ITreeContentProvider
{
	public Object[] getChildren( Object parentElement )
	{
		if ( parentElement instanceof Object[] )
		{
			return (Object[]) parentElement;
		}
		if ( parentElement instanceof DataSetHandle )
		{
			return OlapUtil.getDataFields( (DataSetHandle) parentElement );
		}
		if ( parentElement instanceof TabularCubeHandle )
		{
			DataSetHandle primary = ( (TabularCubeHandle) parentElement ).getDataSet( );
			if ( OlapUtil.getAvailableDatasets( ).length > 1 )
			{
				VirtualField other = new VirtualField( VirtualField.TYPE_OTHER_DATASETS );
				other.setModel( parentElement );
				return new Object[]{
						primary, other
				};
			}
			else
				return new Object[]{
					primary
				};
		}
		if ( parentElement instanceof VirtualField
				&& ( (VirtualField) parentElement ).getType( )
						.equals( VirtualField.TYPE_OTHER_DATASETS ) )
		{
			ArrayList datasets = new ArrayList( );
			datasets.addAll( Arrays.asList( OlapUtil.getAvailableDatasets( ) ) );
			datasets.remove( ( (TabularCubeHandle) ( (VirtualField) parentElement ).getModel( ) ).getDataSet( ) );
			return datasets.toArray( );
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
		if ( element instanceof DataSetHandle )
		{
			return OlapUtil.getDataFields( (DataSetHandle) element ).length > 0;
		}
		if ( element instanceof TabularCubeHandle )
		{
			if ( ( (TabularCubeHandle) element ).getDataSet( ) != null )
				return true;
		}
		if ( element instanceof VirtualField
				&& ( (VirtualField) element ).getType( )
						.equals( VirtualField.TYPE_OTHER_DATASETS )
				&& OlapUtil.getAvailableDatasets( ).length > 1 )
			return true;
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
