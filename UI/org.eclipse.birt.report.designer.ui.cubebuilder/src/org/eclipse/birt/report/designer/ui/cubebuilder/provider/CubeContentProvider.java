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

import java.util.List;

import org.eclipse.birt.report.designer.data.ui.util.CubeModel;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.VirtualField;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Tree viewer content provider adapter for resource browser.
 * 
 */

public class CubeContentProvider implements ITreeContentProvider
{

	private CubeModel dimension;
	private CubeModel measures;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
	 */

	private VirtualField virtualMeasure = new VirtualField( VirtualField.TYPE_MEASURE );
	private VirtualField virtualLevel = new VirtualField( VirtualField.TYPE_LEVEL );

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
			else
			{
				virtualLevel.setModel( parentElement );
				return new Object[]{
					virtualLevel
				};
			}
		}
		if ( parentElement instanceof CubeHandle )
		{
			CubeHandle handle = (CubeHandle) parentElement;
			if ( dimension == null )
				dimension = new CubeModel( handle, CubeModel.TYPE_DIMENSION );
			else if ( dimension.getModel( ) != handle )
				dimension.setModel( handle );
			if ( measures == null )
				measures = new CubeModel( handle, CubeModel.TYPE_MEASURES );
			else if ( measures.getModel( ) != handle )
				measures.setModel( handle );
			return new Object[]{
					dimension, measures
			};
		}
		if ( parentElement instanceof CubeModel )
		{
			CubeModel model = (CubeModel) parentElement;
			if ( model.getType( ) == CubeModel.TYPE_DIMENSION )
			{
				return model.getModel( )
						.getContents( CubeHandle.DIMENSIONS_PROP )
						.toArray( );
			}
			if ( model.getType( ) == CubeModel.TYPE_MEASURES )
			{
				return model.getModel( )
						.getContents( CubeHandle.MEASURE_GROUPS_PROP )
						.toArray( );
			}
		}
		if ( parentElement instanceof LevelHandle )
		{
			HierarchyHandle hierarchy = (HierarchyHandle) ( (LevelHandle) parentElement ).getContainer( );
			int pos = ( (LevelHandle) parentElement ).getIndex( );
			return new Object[]{
				hierarchy.getLevel( pos + 1 )
			};
		}
		if ( parentElement instanceof MeasureGroupHandle )
		{
			Object[] measures = ( (MeasureGroupHandle) parentElement ).getContents( MeasureGroupHandle.MEASURES_PROP )
					.toArray( );
			if ( measures == null || measures.length == 0 )
			{
				virtualMeasure.setModel( parentElement );
				return new Object[]{
					virtualMeasure
				};
			}
			else
				return measures;
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
			return true;
		}
		if ( element instanceof LevelHandle )
		{
			HierarchyHandle hierarchy = (HierarchyHandle) ( (LevelHandle) element ).getContainer( );
			int pos = ( (LevelHandle) element ).getIndex( );
			return hierarchy.getLevel( pos + 1 ) != null;
		}
		if ( element instanceof MeasureGroupHandle )
		{
			return true;
		}
		if ( element instanceof CubeHandle )
		{
			return true;
		}
		if ( element instanceof CubeModel )
		{
			CubeModel model = (CubeModel) element;
			if ( model.getType( ) == CubeModel.TYPE_DIMENSION )
			{
				List dimensionList = model.getModel( )
						.getContents( CubeHandle.DIMENSIONS_PROP );
				if ( dimensionList == null || dimensionList.size( ) == 0 )
				{
					TabularDimensionHandle dimension = DesignElementFactory.getInstance( )
							.newTabularDimension( "Group" );
					try
					{
						model.getModel( ).add( CubeHandle.DIMENSIONS_PROP,
								dimension );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
				}
				return dimensionList != null && dimensionList.size( ) > 0;
			}
			else if ( model.getType( ) == CubeModel.TYPE_MEASURES )
			{
				List measureList = model.getModel( )
						.getContents( CubeHandle.MEASURE_GROUPS_PROP );
				if ( measureList == null || measureList.size( ) == 0 )
				{
					TabularMeasureGroupHandle measureGroup = DesignElementFactory.getInstance( )
							.newTabularMeasureGroup( "Summary Field" );
					try
					{
						model.getModel( ).add( CubeHandle.MEASURE_GROUPS_PROP,
								measureGroup );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
				}
				return measureList != null && measureList.size( ) > 0;
			}
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
