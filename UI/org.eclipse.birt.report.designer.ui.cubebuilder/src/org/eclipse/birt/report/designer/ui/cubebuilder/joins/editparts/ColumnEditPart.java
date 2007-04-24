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
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
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
		if ( getParent( ) instanceof DatasetNodeEditPart )
		{
			( (DatasetNodeEditPart) getParent( ) ).getCube( )
					.addListener( this );
			this.cube = ( (DatasetNodeEditPart) getParent( ) ).getCube( );
		}
		else if ( getParent( ) instanceof HierarchyNodeEditPart )
		{
			( (HierarchyNodeEditPart) getParent( ) ).getCube( ).addListener( this );
			this.cube = ( (HierarchyNodeEditPart) getParent( ) ).getCube( );
		}
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
		ColumnFigure columnFigure = new ColumnFigure( );
		FlowLayout layout = new FlowLayout( );
		layout.setMinorSpacing( 2 );
		columnFigure.setLayoutManager( layout );
		columnFigure.setOpaque( true );
		String name = getColumn( ).getColumnName( );
		label = new Label( name );
		columnFigure.add( label );

		// add ColumnEditPart state change listener
		addEditPartListener( new org.eclipse.gef.EditPartListener.Stub( ) {

			public void selectedStateChanged( org.eclipse.gef.EditPart editpart )
			{
				// get self parent MapEditpart
				NodeEditPartHelper tableEditPart = (NodeEditPartHelper) editpart.getParent( );
				if ( editpart.hasFocus( ) )
				{
				}
				else
				{
					// set the MapEditPart selected state based on the
					// ColumnEditPart selected state
					if ( editpart.getSelected( ) == org.eclipse.gef.EditPart.SELECTED_NONE )
					{
						tableEditPart.setSelected( org.eclipse.gef.EditPart.SELECTED_NONE );
					}
					else if ( editpart.getSelected( ) == org.eclipse.gef.EditPart.SELECTED_PRIMARY )
					{
						tableEditPart.setSelected( org.eclipse.gef.EditPart.SELECTED_PRIMARY );
					}
				}
			}
		} );

	

		return columnFigure;
	}

	/**
	 * @return Gets the name of the Column resprested by the Edit Part
	 */
	public String getColumnName( )
	{
		ResultSetColumnHandle col = getColumn( );
		if ( col != null )
		{
			return col.getColumnName( );
		}

		return null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request request )
	{
		if ( getParent( ) instanceof HierarchyNodeEditPart )
		{
			ConnectionCreation connection = new ConnectionCreation( this );
			return connection;
		}
		return super.getDragTracker( request );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.editparts.NodeEditPartHelper#getChopFigure()
	 */
	public IFigure getChopFigure( )
	{
		return ( (AbstractGraphicalEditPart) this.getParent( ) ).getFigure( );
	}

	protected List getModelSourceConnections( )
	{
		List sourcejoins = new ArrayList( );
		if ( getParent( ) instanceof HierarchyNodeEditPart )
		{
			HierarchyNodeEditPart hierarchyEditpart = (HierarchyNodeEditPart) getParent( );
			Iterator iter = hierarchyEditpart.getCube( )
					.joinConditionsIterator( );
			while ( iter.hasNext( ) )
			{
				DimensionConditionHandle condition = (DimensionConditionHandle) iter.next( );
				HierarchyHandle conditionHierarchy = (HierarchyHandle) condition.getHierarchy( );
				if ( conditionHierarchy == hierarchyEditpart.getModel( ) )
				{
					Iterator conditionIter = condition.getJoinConditions( )
							.iterator( );
					while ( conditionIter.hasNext( ) )
					{
						DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIter.next( );
						if ( joinCondition.getHierarchyKey( )
								.equalsIgnoreCase( ( (ResultSetColumnHandle) getModel( ) ).getColumnName( ) ) ){
							sourcejoins.add( joinCondition );
							System.out.println(sourcejoins.size( ));
						}
					}
				}
			}
		}
		return sourcejoins;
	}

	protected List getModelTargetConnections( )
	{
		List targetjoins = new ArrayList( );
		if ( getParent( ) instanceof DatasetNodeEditPart )
		{
			DatasetNodeEditPart datasetEditpart = (DatasetNodeEditPart) getParent( );
			TabularCubeHandle cube = datasetEditpart.getCube( );
			Iterator iter = cube.joinConditionsIterator( );
			while ( iter.hasNext( ) )
			{
				DimensionConditionHandle condition = (DimensionConditionHandle) iter.next( );
				Iterator conditionIter = condition.getJoinConditions( )
						.iterator( );
				while ( conditionIter.hasNext( ) )
				{
					DimensionJoinConditionHandle joinCondition = (DimensionJoinConditionHandle) conditionIter.next( );
					if ( joinCondition.getCubeKey( )
							.equalsIgnoreCase( ( (ResultSetColumnHandle) getModel( ) ).getColumnName( ) ) )
						targetjoins.add( joinCondition );
				}
			}
		}
		return targetjoins;
	}

	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( getRoot( ).getViewer( ).getControl( ) == null
				|| getRoot( ).getViewer( ).getControl( ).isDisposed( ) )
		{
			if ( getParent( ) instanceof DatasetNodeEditPart )
			{
				( (DatasetNodeEditPart) getParent( ) ).getCube( )
						.removeListener( this );
			}
			else if ( getParent( ) instanceof HierarchyNodeEditPart )
			{
				( (HierarchyNodeEditPart) getParent( ) ).getCube( )
						.removeListener( this );
			}
		}
		else
		{
			refreshSourceConnections( );
			refreshTargetConnections( );
		}
	}
}