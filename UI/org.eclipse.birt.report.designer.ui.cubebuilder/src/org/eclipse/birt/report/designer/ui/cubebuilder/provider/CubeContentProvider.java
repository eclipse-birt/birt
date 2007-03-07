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

import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tree viewer content provider adapter for resource browser.
 * 
 */

public class CubeContentProvider implements ITreeContentProvider
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
		if ( parentElement instanceof DimensionHandle )
		{
			HierarchyHandle hierarchy = (HierarchyHandle) ( (DimensionHandle) parentElement ).getContent( DimensionHandle.HIERARCHIES_PROP,
					0 );
			if ( hierarchy.getLevelCount( ) > 0 )
				return new Object[]{
					hierarchy.getLevel( 0 )
				};
		}
		if ( parentElement instanceof CubeHandle )
		{
			return ( (CubeHandle) parentElement ).getContents( CubeHandle.DIMENSIONS_PROP )
					.toArray( );
		}
		if ( parentElement instanceof LevelHandle )
		{
			HierarchyHandle hierarchy = (HierarchyHandle) ( (LevelHandle) parentElement ).getContainer( );
			int pos = ( (LevelHandle) parentElement ).getIndex( );
			return new Object[]{
				hierarchy.getLevel( pos + 1 )
			};
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
		if ( element instanceof DimensionHandle )
		{
			HierarchyHandle hierarchy = (HierarchyHandle) ( (DimensionHandle) element ).getContent( DimensionHandle.HIERARCHIES_PROP,
					0 );
			return hierarchy != null && hierarchy.getLevelCount( ) > 0;
		}
		if ( element instanceof LevelHandle )
		{
			HierarchyHandle hierarchy = (HierarchyHandle) ( (LevelHandle) element ).getContainer( );
			int pos = ( (LevelHandle) element ).getIndex( );
			return hierarchy.getLevel( pos + 1 ) != null;
		}
		if ( element instanceof CubeHandle )
		{
			return ( (CubeHandle) element ).getContentCount( CubeHandle.DIMENSIONS_PROP ) > 0;
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
