/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.schematic.ListHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.SectionBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ListLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ListFigure;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.activity.NotificationEvent;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.command.ContentException;
import org.eclipse.birt.report.model.command.NameException;
import org.eclipse.birt.report.model.command.PropertyEvent;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;

/**
 * List element edit part.
 *  
 */
public class ListEditPart extends ReportElementEditPart
{

	/**
	 * Constructor.
	 * 
	 * @param obj
	 */
	public ListEditPart( Object obj )
	{
		super( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		switch ( ev.getEventType( ) )
		{
			case NotificationEvent.CONTENT_EVENT :
			{
				if ( focus instanceof ListHandle )
				{
					addListBandEditPart( );
				}
				markDirty( true );
				refreshChildren( );

				break;
			}
			case NotificationEvent.ELEMENT_DELETE_EVENT :
			{

				focus.removeListener( this );

				markDirty( true );
				getListHandleAdapt( ).remove( focus );
				refresh( );
				break;
			}
			case NotificationEvent.PROPERTY_EVENT :
			{
				markDirty( true );
				reLayout( );
				refresh( );

				PropertyEvent event = (PropertyEvent) ev;

				if ( event.getPropertyName( ).startsWith( "border" ) )//$NON-NLS-1$
				{
					refreshVisuals( );
				}
				if ( event.getPropertyName( ).equals( Style.PADDING_TOP_PROP )
						|| event.getPropertyName( )
								.equals( Style.PADDING_BOTTOM_PROP )
						|| event.getPropertyName( )
								.equals( Style.PADDING_LEFT_PROP )
						|| event.getPropertyName( )
								.equals( Style.PADDING_RIGHT_PROP ) )
				{
					getFigure( ).getParent( ).revalidate( );
				}

				break;
			}
			default :
			{
				markDirty( true );
				reLayout( );
				refresh( );
				break;
			}
		}
	}

	/**
	 *  
	 */
	private void addListBandEditPart( )
	{
		List list = getModelChildren( );
		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			ListBandProxy proxy = (ListBandProxy) list.get( i );
			proxy.getElemtHandle( ).addListener( this );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#activate()
	 */
	public void activate( )
	{

		super.activate( );
		addListBandEditPart( );
	}
	/**
	 * layouts the figure
	 */
	private void reLayout( )
	{
		getFigure( ).invalidateTree( );

		getFigure( ).getUpdateManager( ).addInvalidFigure( getFigure( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE,
				new ReportComponentEditPolicy( ) );
		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new ReportContainerEditPolicy( ) );
		installEditPolicy( EditPolicy.LAYOUT_ROLE, new ListLayoutEditPolicy( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		refreshBorder( getListHandleAdapt( ).getHandle( ),
				(BaseBorder) getFigure( ).getBorder( ) );

		( (SectionBorder) ( getFigure( ).getBorder( ) ) ).setInsets( getListHandleAdapt( ).getPadding( getFigure( ).getInsets( ) ) );

		refreshBackground( (DesignElementHandle) getModel( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	protected void refreshChildren( )
	{
		super.refreshChildren( );
		List list = getChildren( );
		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			( (ListBandEditPart) list.get( i ) ).refreshChildren( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		ListFigure figure = new ListFigure( );
		figure.setOpaque( false );

		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{
		return getListHandleAdapt( ).getChildren( );
	}

	private ListHandleAdapter getListHandleAdapt( )
	{
		return (ListHandleAdapter) getModelAdapter( );
	}

	/**
	 * Insert group in list element
	 */
	public ListGroupHandle insertGroup( )
	{
		ListGroupHandle groupHandle = null;
		try
		{
			groupHandle = getListHandleAdapt( ).insertGroup( );
		}
		catch ( ContentException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( NameException e )
		{
			ExceptionHandler.handle( e );
		}
		return groupHandle;
	}

	/**
	 * Remove group
	 * @param group
	 */
	public void removeGroup( Object group )
	{
		try
		{
			getListHandleAdapt( ).removeGroup( group );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#notifyChildrenDirty(boolean)
	 */
	protected void notifyChildrenDirty( boolean bool )
	{
		super.notifyChildrenDirty( bool );
		if ( bool )
		{
			reLayout( );
		}
	}

	/**
	 * Check if inlucde header/footer
	 * @param bool
	 * @param id
	 */
	public void includeSlotHandle( boolean bool, int id )
	{
		Object model = getListHandleAdapt( ).getChild( id );
		ListBandEditPart part = (ListBandEditPart) getViewer( ).getEditPartRegistry( )
				.get( model );
		if(part == null)
		{
			return;
		}
		part.setRenderVisile( bool );
	}

	/**
	 * Check if inlucde header/footer
	 * @param id
	 * @return
	 */
	public boolean isIncludeSlotHandle( int id )
	{
		Object model = getListHandleAdapt( ).getChild( id );
		ListBandEditPart part = (ListBandEditPart) getViewer( ).getEditPartRegistry( )
				.get( model );
		if(part == null)
		{
			return false;
		}
		return part.isRenderVisile( );
	}
	
	
	public void showTargetFeedback(Request request)
	{
	    if ( this.getSelected() == 0 &&
	    		isActive() && request.getType() == RequestConstants.REQ_SELECTION )
	    {
		    this.getViewer().setCursor( ReportPlugin.getDefault().getCellCursor() );
	    }
	    super.showTargetFeedback( request );
	}
	
	public void eraseTargetFeedback( Request request)
	{
		if (isActive())
		{
			this.getViewer().setCursor( null );
		}
	    super.eraseTargetFeedback( request );
	}
	
	protected void addChildVisual(EditPart part, int index)
	{
	    // make sure we don't keep a select cell cursor after new contents
	    // are added
	    this.getViewer().setCursor( null );
	    super.addChildVisual(part, index);
	}
}