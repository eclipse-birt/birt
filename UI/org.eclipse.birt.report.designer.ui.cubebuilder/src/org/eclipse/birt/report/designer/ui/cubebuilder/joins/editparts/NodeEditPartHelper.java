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

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnConnectionAnchor;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DimensionConditionHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

/**
 * Utility base class containing methods most commonly used by other edit Parts
 * Some of the other edit parts which inherit from this is ColumnEditPart,
 * TableNodeEditPart
 * 
 */
public abstract class NodeEditPartHelper extends AbstractGraphicalEditPart implements
		NodeEditPart,
		Listener
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected abstract IFigure createFigure( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected abstract void createEditPolicies( );

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelSourceConnections()
	 */
	protected List getModelSourceConnections( )
	{
		List sourcejoins = new ArrayList( );
		return sourcejoins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getModelTargetConnections()
	 */
	protected List getModelTargetConnections( )
	{
		List targetjoins = new ArrayList( );
		return targetjoins;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection )
	{
		return new ColumnConnectionAnchor( this.getFigure( ), getChopFigure( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.ConnectionEditPart)
	 */
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection )
	{
		return new ColumnConnectionAnchor( this.getFigure( ), getChopFigure( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.NodeEditPart#getSourceConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getSourceConnectionAnchor( Request request )
	{
		return new ColumnConnectionAnchor( this.getFigure( ), getChopFigure( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.NodeEditPart#getTargetConnectionAnchor(org.eclipse.gef.Request)
	 */
	public ConnectionAnchor getTargetConnectionAnchor( Request request )
	{
		return new ColumnConnectionAnchor( this.getFigure( ), getChopFigure( ) );
	}

	// /**
	// * @param joinCondition
	// */
	// protected void stopListenToJoinCondition( JoinCondition joinCondition )
	// {
	// joinCondition.removeListener( this );
	// //((Notifier)joinCondition).eAdapters().remove(this);
	// }
	//
	// /**
	// * @param join
	// */
	// protected void stopListenToJoin( JoinImpl join )
	// {
	// // Object map = getSourceRef( );
	// // if ( ( (JoinImpl) join ).getLeft( ).equals( map )
	// // || ( (JoinImpl) join ).getRight( ).equals( map ) )
	// // {
	// // JoinCondition joinCondition = ( (JoinImpl) join ).getCondition( );
	// // stopListenToJoinCondition( joinCondition );
	// //
	// // }
	// }

	// /**
	// * @param joinCondition
	// */
	// protected void listenToJoinCondition( JoinCondition joinCondition )
	// {
	// // joinCondition.addListener( this );
	// }
	//
	// /**
	// * @param join
	// */
	// protected void listenToJoin( JoinImpl join )
	// {
	// JoinCondition joinCondition = ( (JoinImpl) join ).getCondition( );
	// listenToJoinCondition( joinCondition );
	// }


	public abstract IFigure getChopFigure( );

	protected void removeTargetConnection( ConnectionEditPart connection )
	{
		if ( connection.isActive( ) )
			connection.deactivate( );
		super.removeTargetConnection( connection );
	}

	protected void removeSourceConnection( ConnectionEditPart connection )
	{
		if ( connection.isActive( ) )
			connection.deactivate( );
		super.removeSourceConnection( connection );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#addTargetConnection(org.eclipse.gef.ConnectionEditPart,
	 *      int)
	 */
	protected void addTargetConnection( ConnectionEditPart connection, int index )
	{
		super.addTargetConnection( connection, index );
		if ( isActive( ) )
			connection.activate( );
	}
}