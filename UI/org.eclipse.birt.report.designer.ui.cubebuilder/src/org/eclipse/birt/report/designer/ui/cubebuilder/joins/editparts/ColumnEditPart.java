/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.ColumnSelectionEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editpolicies.ConnectionCreationEditPolicy;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * The Edit Part corresponding to the Column of a Table
 * 
 * @see
 * <p>
 * NodeDditPartHelper
 * <p>
 * for other methods defined here
 * 
 */
public class ColumnEditPart extends NodeEditPartHelper implements Listener

{

	protected Label label;

	/**
	 * @param context
	 * @param column
	 */
	public ColumnEditPart( EditPart parent, ResultSetColumnHandle column )
	{
		setParent( parent );
		setModel( column );
		this.cube = ( (DatasetNodeEditPart) getParent( ) ).getCube( );
	}

	private TabularCubeHandle cube;

	public TabularCubeHandle getCube( )
	{
		return cube;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		ColumnFigure columnFigure = null;
		columnFigure = new ColumnFigure( );
		FlowLayout layout = new FlowLayout( );
		layout.setMinorSpacing( 2 );
		columnFigure.setLayoutManager( layout );
		columnFigure.setOpaque( true );
		String name = getColumn( ).getColumnName( );
		label = new Label( name );
		columnFigure.add( label );
		return columnFigure;
	}

	/**
	 * @return Gets the Model object represented by this Edit Part
	 */
	private ResultSetColumnHandle getColumn( )
	{
		// TODO Auto-generated method stub
		return (ResultSetColumnHandle) getModel( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		// // TODO Auto-generated method stub
		ColumnSelectionEditPolicy colEditPol = new ColumnSelectionEditPolicy( );
		this.installEditPolicy( "Selection Policy", colEditPol ); //$NON-NLS-1$
		installEditPolicy( EditPolicy.GRAPHICAL_NODE_ROLE,
				new ConnectionCreationEditPolicy( ) );

	}

	public IFigure getChopFigure( )
	{
		return ( (AbstractGraphicalEditPart) this.getParent( ) ).getFigure( );
	}

	protected List getModelTargetConnections( )
	{
		List targetjoins = new ArrayList( );

		DatasetNodeEditPart datasetEditpart = (DatasetNodeEditPart) getParent( );
		TabularCubeHandle cube = datasetEditpart.getCube( );
		Iterator iter = cube.joinConditionsIterator( );
		while ( iter.hasNext( ) )
		{
			DimensionConditionHandle condition = (DimensionConditionHandle) iter.next( );
			Iterator conditionIter = condition.getJoinConditions( ).iterator( );
			while ( conditionIter.hasNext( ) )
			{
				DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIter.next( );
				if ( joinCondition.getCubeKey( )
						.equals( getColumn( ).getColumnName( ) ) )
				{
					TabularHierarchyHandle hierarchy = (TabularHierarchyHandle) condition.getHierarchy( );
					if ( hierarchy.getDataSet( ) == null )
						break;
					if ( joinCondition.getLevel( ) != null )
					{
						LevelHandle level = joinCondition.getLevel( );
						Iterator attrIter = level.attributesIterator( );
						while ( attrIter.hasNext( ) )
						{
							LevelAttributeHandle handle = (LevelAttributeHandle) attrIter.next( );
							ResultSetColumnHandle column = OlapUtil.getDataField( hierarchy.getDataSet( ),
									handle.getName( ) );
							if ( column != null
									&& column.getColumnName( )
											.equals( joinCondition.getHierarchyKey( ) ) )
							{
								targetjoins.add( joinCondition );
								break;
							}

						}
					}
					else
					{
						if ( OlapUtil.getDataField( hierarchy.getDataSet( ),
								joinCondition.getHierarchyKey( ) ) != null )
							for ( int i = 0; i < hierarchy.getLevelCount( ); i++ )
							{
								TabularLevelHandle level = (TabularLevelHandle) hierarchy.getLevel( i );
								if ( level.getColumnName( ) != null
										&& level.getColumnName( )
												.equals( joinCondition.getHierarchyKey( ) ) )
								{
									targetjoins.add( joinCondition );
									break;
								}
							}
					}
				}
			}
		}

		return targetjoins;
	}

	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( isActive( ) && !isDelete( ) )
		{
			refreshTargetConnections( );
		}
	}

	public void deactivate( )
	{
		super.deactivate( );
		( (DatasetNodeEditPart) getParent( ) ).getCube( ).removeListener( this );
	}

	public void activate( )
	{
		super.activate( );
		( (DatasetNodeEditPart) getParent( ) ).getCube( ).addListener( this );
	}

	public String getColumnName( )
	{
		return getColumn( ).getColumnName( );
	}
}