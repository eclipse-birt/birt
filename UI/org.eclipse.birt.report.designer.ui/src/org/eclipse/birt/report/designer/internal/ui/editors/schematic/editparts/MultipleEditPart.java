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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.designer.core.commands.SetConstraintCommand;
import org.eclipse.birt.report.designer.core.model.IMultipleAdapterHelper;
import org.eclipse.birt.report.designer.core.model.schematic.MultipleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportElementResizePolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.MultipleFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.AbstractGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.MultipleGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.layout.MultipleLayout;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.command.ViewsContentEvent;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;


/**
 * EditPar to support multiple views.
 */

public class MultipleEditPart extends ReportElementEditPart implements IMultipleAdapterHelper
{

	private String guideLabel;
	/**Constructor
	 * @param model
	 */
	public MultipleEditPart( Object model )
	{
		super( model );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE,
				new ReportComponentEditPolicy( ) );
		installEditPolicy( EditPolicy.LAYOUT_ROLE,
				new ReportFlowLayoutEditPolicy( ) );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		//do noting
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		return getModelAdapter( ).getChildren( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		Figure layer = new MultipleFigure();
		return layer;
	}
	
	/**Gets the MultipleAdapter.
	 * @return
	 */
	public MultipleAdapter getMultipleAdapter()
	{
		return (MultipleAdapter)getModelAdapter( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.core.model.IMultipleAdapterHelper#isMultiple()
	 */
	public boolean isMultiple( )
	{
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createGuideHandle()
	 */
	protected AbstractGuideHandle createGuideHandle( )
	{
		MultipleGuideHandle handle = new MultipleGuideHandle( this );
		
		handle.addChildren( getMultipleAdapter( ).getViews( ) );
		handle.setSelected( getMultipleAdapter( ).getCurrentViewNumber( ) );
		
		return handle;
	}
	
	/**Set the current view.
	 * @param number
	 */
	public void setCurrentView(int number)
	{
		getMultipleAdapter( ).setCurrentView( number );
		
		((MultipleGuideHandle)getGuideHandle( )).setSelected( number );
		
		//UIUtil.resetViewSelection(getViewer(), false);
	}
	
	/**Remove the view.
	 * @param number
	 */
	public void removeView(int number)
	{
		getMultipleAdapter( ).removeView( number );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#propertyChange(java.util.Map)
	 */
	protected void propertyChange( Map info )
	{
		Set set = info.keySet( );
		if (set.contains( IReportItemModel.MULTI_VIEWS_PROP ) && getMultipleAdapter( ).getViews( ).size( ) > 0)
		{
			refresh( );
			UIUtil.resetViewSelection(getViewer(), true);
			return;
		}
		super.propertyChange( info );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#contentChange(java.util.Map)
	 */
	protected void contentChange( Map info )
	{
		Object action = info.get(GraphicsViewModelEventProcessor.CONTENT_EVENTTYPE );
		if (action instanceof Integer)
		{
			int intValue = ((Integer)action).intValue( );
			if (intValue == ViewsContentEvent.ADD
					|| intValue == ViewsContentEvent.SHIFT
					|| intValue == ViewsContentEvent.REMOVE)
			{
				markDirty( true );
				removeGuideFeedBack( );
				if (getMultipleAdapter( ).getViews( ).size( ) ==0)
				{
					markDirty( true );
					EditPart part = getParent( );
					((ReportElementEditPart)getParent( )).removeChild( this );
					part.refresh( );
					
					return;
				}
			}
		}
		super.contentChange( info );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#getResizePolice(org.eclipse.gef.EditPolicy)
	 */
	public EditPolicy getResizePolice( EditPolicy parentPolice )
	{
		ReportElementResizePolicy policy = new MultipleResizePolicy( );
		policy.setResizeDirections( PositionConstants.SOUTH
				| PositionConstants.EAST
				| PositionConstants.SOUTH_EAST );
		return policy;
		//return super.getResizePolice( parentPolice );
	}
	
	/**
	 * MultipleResizePolicy
	 */
	private static class MultipleResizePolicy extends ReportElementResizePolicy
	{

		protected Command getResizeCommand( ChangeBoundsRequest request )
		{
			Command command = super.getResizeCommand( request );
			if (command instanceof SetConstraintCommand)
			{
				((SetConstraintCommand)command).setModel( (ReportItemHandle)getTrueHost( ).getModel( ) );
			}
			return command;
		}
		
		private GraphicalEditPart getTrueHost()
		{
			GraphicalEditPart parent = (GraphicalEditPart)getHost( );
			List list = parent.getChildren( );
			if (list.size( ) == 0)
			{
				return parent;
			}
			return (GraphicalEditPart)list.get( 0 );
		}
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#isinterestSelection(java.lang.Object)
	 */
	public boolean isinterestSelection( Object object )
	{
		return getMultipleAdapter( ).getViews( ).contains( object );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#notifyModelChange()
	 */
	public void notifyModelChange( )
	{
		super.notifyModelChange( );
		((MultipleLayout)getFigure( ).getLayoutManager( )).markDirty( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#getGuideLabel()
	 */
	public String getGuideLabel( )
	{
		if (guideLabel == null)
		{
			guideLabel = ((ReportElementEditPart)getChildren( ).get( 0 )).getGuideLabel( );
		}
		return guideLabel;
	}
}
