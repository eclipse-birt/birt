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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstancts;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.designer.ui.dialogs.BindingExpressionProvider;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class CrosstabBindingExpressionProvider extends
		BindingExpressionProvider
{

	protected void addFilterToProvider()
	{
		this.addFilter( new ExpressionFilter( ) {

			public boolean select( Object parentElement, Object element )
			{
				// bug 220714
				if(parentElement instanceof String )
				{
					String parent = (String)parentElement;
					if(ExpressionFilter.CATEGORY.equals( parent ))
					{
						if(element instanceof String)
						{
							String elementString = (String)element;
							if(COLUMN_BINDINGS.equals( elementString ))
							{
								return false;
							}
						}
					}
					
					if(CURRENT_CUBE.equals( parent ))
					{
						if(element instanceof PropertyHandle)
						{
							PropertyHandle handle = (PropertyHandle) element;
							if ( handle.getPropertyDefn( )
									.getName( )
									.equals( ICubeModel.MEASURE_GROUPS_PROP ) )
							{
								return true;
							}
							return false;
						}
					}
				}
//				if ( parentElement instanceof PropertyHandle )
//				{
//					PropertyHandle handle = (PropertyHandle) parentElement;
//					if ( handle.getPropertyDefn( )
//							.getName( )
//							.equals( ICubeModel.DIMENSIONS_PROP ) )
//					{
//						try
//						{
//							CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle( );
//							if ( xtabHandle.getDimension( ( (TabularDimensionHandle) element ).getName( ) ) == null )
//								return false;
//							return true;
//						}
//						catch ( ExtendedElementException e )
//						{
//							return false;
//						}
//					}
//					//Bug 211024
//					//					else if ( handle.getPropertyDefn( )
//					//							.getName( )
//					//							.equals( ICubeModel.MEASURE_GROUPS_PROP ) )
//					//					{
//					//
//					//						try
//					//						{
//					//							CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle( );
//					//							MeasureGroupHandle mgHandle = (MeasureGroupHandle) element;
//					//							for ( int i = 0; i < xtabHandle.getMeasureCount( ); i++ )
//					//							{
//					//								if ( xtabHandle.getMeasure( i )
//					//										.getCubeMeasure( )
//					//										.getContainer( )
//					//										.equals( mgHandle ) )
//					//									return true;
//					//							}
//					//							return false;
//					//						}
//					//						catch ( ExtendedElementException e )
//					//						{
//					//							return false;
//					//						}
//					//					}
//				}
				//Bug 211024
				//				if ( element instanceof MeasureHandle )
				//				{
				//					try
				//					{
				//						CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle( );
				//						for ( int i = 0; i < xtabHandle.getMeasureCount( ); i++ )
				//						{
				//							if ( xtabHandle.getMeasure( i )
				//									.getCubeMeasure( )
				//									.equals( element ) )
				//								return true;
				//						}
				//						return false;
				//					}
				//					catch ( ExtendedElementException e )
				//					{
				//						return false;
				//					}
				//				}
				return true;
			}
		}  );
	}
	
	public CrosstabBindingExpressionProvider( DesignElementHandle handle )
	{
		super( handle );
		addFilterToProvider( );
	}

	protected List getChildrenList( Object parent )
	{
		if ( parent instanceof TabularDimensionHandle )
		{
			List children = new ArrayList( );
			try
			{
				TabularDimensionHandle handle = (TabularDimensionHandle) parent;
				CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle( );
				for ( int i = 0; i < xtabHandle.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dimensionHandle = xtabHandle.getDimension( ICrosstabConstants.ROW_AXIS_TYPE,
							i );
					if ( dimensionHandle.getCubeDimension( ).equals( handle ) )
						children.add( dimensionHandle.getLevel( 0 )
								.getCubeLevel( ) );
				}
				for ( int i = 0; i < xtabHandle.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dimensionHandle = xtabHandle.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE,
							i );
					if ( dimensionHandle.getCubeDimension( ).equals( handle ) )
						children.add( dimensionHandle.getLevel( 0 )
								.getCubeLevel( ) );
				}
			}
			catch ( ExtendedElementException e )
			{
			}
			return children;
		}
		else if ( parent instanceof LevelHandle )
		{
			List children = new ArrayList( );
			LevelHandle levelHandle = (LevelHandle) parent;
			try
			{
				CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle( );
				for ( int i = 0; i < xtabHandle.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dimensionHandle = xtabHandle.getDimension( ICrosstabConstants.ROW_AXIS_TYPE,
							i );
					LevelViewHandle levelViewHandle = dimensionHandle.getLevel( levelHandle.getQualifiedName( ) );
					if ( levelViewHandle != null )
						if ( dimensionHandle.getLevelCount( ) > levelViewHandle.getIndex( ) + 1 )
							children.add( dimensionHandle.getLevel( levelViewHandle.getIndex( ) + 1 )
									.getCubeLevel( ) );
				}
				for ( int i = 0; i < xtabHandle.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE ); i++ )
				{
					DimensionViewHandle dimensionHandle = xtabHandle.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE,
							i );
					LevelViewHandle levelViewHandle = dimensionHandle.getLevel( levelHandle.getQualifiedName( ) );
					if ( levelViewHandle != null )
						if ( dimensionHandle.getLevelCount( ) > levelViewHandle.getIndex( ) + 1 )
							children.add( dimensionHandle.getLevel( levelViewHandle.getIndex( ) + 1 )
									.getCubeLevel( ) );
				}
			}
			catch ( ExtendedElementException e )
			{
			}
			return children;
		}
		return super.getChildrenList( parent );
	}

	protected CrosstabReportItemHandle getCrosstabReportItemHandle( )
			throws ExtendedElementException
	{
		return (CrosstabReportItemHandle) ( (ExtendedItemHandle) elementHandle ).getReportItem( );
	}

	public String getDisplayText( Object element )
	{
		if ( element instanceof LevelViewHandle )
			return ( (LevelViewHandle) element ).getCubeLevel( ).getName( );
		return super.getDisplayText( element );
	}

	public Image getImage( Object element )
	{
		if ( element instanceof LevelViewHandle )
			return UIHelper.getImage( BuilderConstancts.IMAGE_LEVEL );
		return super.getImage( element );
	}

	public boolean hasChildren( Object element )
	{
		if ( element instanceof LevelHandle )
		{
			return getChildrenList( element ).size( ) > 0;
		}
		return super.hasChildren( element );
	}

}
