/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.figures.ColumnConnection;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionJoinConditionHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

public class JoinConditionEditPart extends AbstractConnectionEditPart implements
		Listener
{

	final static String REMOVEJOINT = "Remove Join";

	/**
	 * @param join
	 */
	public JoinConditionEditPart( DimensionJoinConditionHandle join )
	{
		setModel( join );
	}

	protected IFigure createFigure( )
	{
		return new ColumnConnection( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals( )
	{
		ColumnConnection connection = (ColumnConnection) this.getFigure( );
		connection.revalidate( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{

		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new JoinConnectionEditPolicy( ) );
		installEditPolicy( "Selection Policy", new JoinSelectionEditPolicy( ) );
	}

	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		// TODO Auto-generated method stub
		
	}

	/**
	 * Returns a Action for Deleting a Join.
	 * 
	 * @return
	 */
//	public DeleteJoinAction getRemoveAction( )
//	{
//		Query queryElement = (Query) ( GraphicalViewerUtil.getQueryEditPart( this ) ).getModel( );
//		DeleteJoinAction removeAction = new DeleteJoinAction( queryElement,
//				this,
//				getModel( ) );
////		Object selection = this.getModel( );
//		return removeAction;
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.ui.model.activity.Listener#elementChanged(org.eclipse.birt.report.data.oda.jdbc.ui.model.DesignElement,
	 *      org.eclipse.birt.report.data.oda.jdbc.ui.editors.graphical.command.NotificationEvent)
	 */
//	public void elementChanged( BaseDataSourceElement focus,
//			NotificationEvent ev )
//	{
//		if ( ev instanceof JoinCreationEvent )
//		{
//			refreshVisuals( );
//			refresh( );
//		}
//
//	}
}

