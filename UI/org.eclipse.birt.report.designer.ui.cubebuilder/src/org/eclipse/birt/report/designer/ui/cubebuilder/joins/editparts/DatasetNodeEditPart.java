/*******************************************************************************
 * Copyright (c) 24 Actuate Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1. which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v1.html
 * 
 * Contributors: Actuate Corporation - Initial implementation.
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.TableNodeFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.TablePaneFigure;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstancts;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.OlapUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.DragEditPartsTracker;

/**
 * Edit Part corresponding to a Table object.
 * 
 */
public class DatasetNodeEditPart extends NodeEditPartHelper implements Listener
{

	public TablePaneFigure scrollPane;
	public TableNodeFigure tableNode;

	private TabularCubeHandle cube;
	private DataSetHandle dataset;

	/**
	 * @param impl
	 */
	public DatasetNodeEditPart( EditPart parent, DataSetHandle dataset )
	{
		setModel( dataset );
		setParent( parent );
		dataset.getModuleHandle( ).addListener( this );
		this.cube = (TabularCubeHandle) parent.getModel( );
		this.dataset = dataset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		String name = ( (DataSetHandle) dataset ).getName( )
				+ "(Cube Dataset)";
		tableNode = new TableNodeFigure( name );
		scrollPane = new TablePaneFigure( name );
		scrollPane.setContents( tableNode );
		return scrollPane;
	}

	/***************************************************************************
	 * Returns the Children for this Edit Part. It returns a List of
	 * ColumnEditParts
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{

		List childList = new ArrayList( );

		ResultSetColumnHandle[] columns = OlapUtil.getDataFields( dataset );
		if ( columns != null )
		{
			for ( int i = 0; i < columns.length; i++ )
			{
				childList.add( columns[i] );
			}
		}
		// childrenColumnNumber = childList.size( );
		return childList;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals( )
	{
		Rectangle r;
		r = new Rectangle( getPosX( ), getPosY( ), getWidth( ), getHeight( ) );
		getFigure( ).setBounds( r );
		( (GraphicalEditPart) getParent( ) ).setLayoutConstraint( this,
				getFigure( ),
				r );

	}

	public int getWidth( )
	{
		int width = UIHelper.getIntProperty( ( (ReportElementHandle) getModel( ) ).getModuleHandle( ),
				UIHelper.getId( dataset, cube ),
				BuilderConstancts.SIZE_WIDTH );
		return width == 0 ? 150 : width;
	}

	public int getHeight( )
	{
		int height = UIHelper.getIntProperty( ( (ReportElementHandle) getModel( ) ).getModuleHandle( ),
				UIHelper.getId( dataset, cube ),
				BuilderConstancts.SIZE_HEIGHT );
		return height == 0 ? 200 : height;
	}

	public int getPosX( )
	{
		int x = UIHelper.getIntProperty( ( (ReportElementHandle) getModel( ) ).getModuleHandle( ),
				UIHelper.getId( dataset, cube ),
				BuilderConstancts.POSITION_X );
		return x;
	}

	public int getPosY( )
	{
		int y = UIHelper.getIntProperty( ( (ReportElementHandle) getModel( ) ).getModuleHandle( ),
				UIHelper.getId( dataset, cube ),
				BuilderConstancts.POSITION_Y );
		return y;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.SELECTION_FEEDBACK_ROLE,
				new TableSelectionEditPolicy( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.model.activity.Listener#elementChanged(org.eclipse.birt.report.data.oda.jdbc.ui.model.DesignElement,
	 *      org.eclipse.birt.report.data.oda.jdbc.ui.model.activity.NotificationEvent)
	 */
	// public void elementChanged( BaseDataSourceElement focus,
	// NotificationEvent ev )
	// {
	// if ( ev instanceof JoinCreationEvent )
	// {
	// // Object addedObject = ((JoinCreationEvent)ev).getTarget();
	// ArrayList joins = ( (JoinCreationEvent) ev ).getJoins( );
	// if ( joins != null )
	// {
	// Iterator itor = joins.iterator( );
	// while ( itor.hasNext( ) )
	// {
	// Object addedObject = itor.next( );
	// if ( addedObject instanceof JoinImpl )
	// {
	// listenToJoin( (JoinImpl) addedObject );
	// }
	// else if ( addedObject instanceof JoinCondition )
	// {
	// listenToJoinCondition( (JoinCondition) addedObject );
	// }
	// }
	// }
	//
	// }
	//
	// refreshVisuals( );
	//
	// }
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.editparts.NodeEditPartHelper#connectToThisNode(java.lang.String,
	 *      int, boolean)
	 */
	public boolean connectToThisNode( String newColumnName,
			String newTableName, int joinConditionType, boolean isTarget )
	{
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.editparts.NodeEditPartHelper#getSourceRef()
	 */
	public DataSetHandle getSourceRef( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.editparts.NodeEditPartHelper#getColumnName()
	 */
	public String getColumnName( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.editparts.NodeEditPartHelper#getChopFigure()
	 */
	public IFigure getChopFigure( )
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane( )
	{
		return tableNode;
	}

	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( getRoot( ).getViewer( ).getControl( ) == null
				|| getRoot( ).getViewer( ).getControl( ).isDisposed( ) )
		{
			( (ReportElementHandle) getModel( ) ).getModuleHandle( )
					.removeListener( this );
		}
		else
			refreshVisuals( );
	}

	public DragTracker getDragTracker( Request req )
	{
		DragEditPartsTracker track = new DragEditPartsTracker( this );
		return track;
	}

	
	public TabularCubeHandle getCube( )
	{
		return cube;
	}

}