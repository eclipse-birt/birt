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
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.AbstractGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyEvent;
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

	private static final String GUIDEHANDLE_TEXT = Messages.getString( "ListEditPart.GUIDEHANDLE_TEXT" ); //$NON-NLS-1$

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
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createGuideHandle()
	 */
	protected AbstractGuideHandle createGuideHandle( )
	{
		TableGuideHandle handle = new TableGuideHandle( this );
		handle.setIndicatorLabel( GUIDEHANDLE_TEXT );
		handle.setIndicatorIcon( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_LIST ) );
		return handle;
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

		( (SectionBorder) ( getFigure( ).getBorder( ) ) ).setPaddingInsets( getListHandleAdapt( ).getPadding( getFigure( ).getInsets( ) ) );
		
		refreshMargin();

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
	public boolean insertGroup( )
	{
		return UIUtil.createGroup( getListHandleAdapt( ).getHandle( ) );
	}
	
	/**
	 * Inserts group in list.
	 * 
	 * @param part
	 *            the current part to specify the position of new group. Null to
	 *            call <code>insertGroup( )</code>
	 */
	public boolean insertGroup( Object part )
	{
		if ( part != null && part instanceof ListBandProxy )
		{
			return UIUtil.createListGroup( (ListBandProxy) part );
		}
		return insertGroup( );
	}

	/**
	 * Remove group
	 * 
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
	 * 
	 * @param bool
	 * @param id
	 */
	public void includeSlotHandle( boolean bool, int id )
	{
		Object model = getListHandleAdapt( ).getChild( id );
		ListBandEditPart part = (ListBandEditPart) getViewer( ).getEditPartRegistry( )
				.get( model );
		if ( part == null )
		{
			return;
		}
		part.setRenderVisile( bool );
	}

	/**
	 * Check if inlucde header/footer
	 * 
	 * @param id
	 * @return
	 */
	public boolean isIncludeSlotHandle( int id )
	{
		Object model = getListHandleAdapt( ).getChild( id );
		ListBandEditPart part = (ListBandEditPart) getViewer( ).getEditPartRegistry( )
				.get( model );
		if ( part == null )
		{
			return false;
		}
		return part.isRenderVisile( );
	}

	public void showTargetFeedback( Request request )
	{
		if ( this.getSelected( ) == 0
				&& isActive( )
				&& request.getType( ) == RequestConstants.REQ_SELECTION )
		{
			if ( isFigureLeft( request ) )
			{
				this.getViewer( ).setCursor( ReportPlugin.getDefault( )
						.getLeftCellCursor( ) );
			}
			else
			{
				this.getViewer( ).setCursor( ReportPlugin.getDefault( )
						.getRightCellCursor( ) );
			}
		}
		super.showTargetFeedback( request );
	}

	public void eraseTargetFeedback( Request request )
	{
		if ( isActive( ) )
		{
			this.getViewer( ).setCursor( null );
		}
		super.eraseTargetFeedback( request );
	}

	protected void addChildVisual( EditPart part, int index )
	{
		// make sure we don't keep a select cell cursor after new contents
		// are added
		this.getViewer( ).setCursor( null );
		super.addChildVisual( part, index );
	}
}