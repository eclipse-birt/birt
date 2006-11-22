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

package org.eclipse.birt.report.designer.internal.lib.editparts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.lib.commands.SetCurrentEditModelCommand;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportRootFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.AbstractPageFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportDesignLayout;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.swt.widgets.Display;

/**
 * This is the content edit part for Library. All other library elements puts on
 * to it
 */
public class LibraryReportDesignEditPart extends ReportDesignEditPart implements
		PropertyChangeListener
{

	private static final Insets INSETS = new Insets( 30, 30, 30, 30 );
	private static final Dimension DEFAULTSIZE = new Dimension( 800, 1000 );

	/**
	 * @param obj
	 */
	public LibraryReportDesignEditPart( Object obj )
	{
		super( obj );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		ReportRootFigure figure = new ReportRootFigure( );

		figure.setOpaque( true );
		figure.setShowMargin( showMargin );

		ReportDesignLayout layout = new ReportDesignLayout( this );

		Dimension size = DEFAULTSIZE;

		Rectangle bounds = new Rectangle( 0, 0, size.width - 1, size.height - 1 );

		layout.setInitSize( bounds );

		figure.setLayoutManager( layout );

		figure.setBorder( new ReportDesignMarginBorder( INSETS ) );

		figure.setBounds( bounds.getCopy( ) );

		return figure;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.ui.editor.edit.ReportElementEditPart#getModelChildren()
	 */
	protected List getModelChildren( )
	{

		return HandleAdapterFactory.getInstance( )
				.getLibraryHandleAdapter( getModel( ) )
				.getChildren( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.designer.internal.ui.editors.schematic.editparts.AbstractReportEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{

		ReportRootFigure figure = (ReportRootFigure) getFigure( );
		figure.setShowMargin( showMargin );
		Dimension size = DEFAULTSIZE;

		Rectangle bounds = new Rectangle( 0, 0, size.width - 1, size.height - 1 );

		( (AbstractPageFlowLayout) getFigure( ).getLayoutManager( ) ).setInitSize( bounds );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart#activate()
	 */
	public void activate( )
	{
		HandleAdapterFactory.getInstance( )
				.getLibraryHandleAdapter( getModel( ) )
				.addPropertyChangeListener( this );
		super.activate( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart#deactivate()
	 */
	public void deactivate( )
	{
		HandleAdapterFactory.getInstance( )
				.getLibraryHandleAdapter( getModel( ) )
				.removePropertyChangeListener( this );
		super.deactivate( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange( final PropertyChangeEvent evt )
	{
		if ( LibraryHandleAdapter.CURRENTMODEL.equals(evt.getPropertyName( )  ) 
				|| LibraryHandleAdapter.CREATE_ELEMENT.equals(evt.getPropertyName( )))
		{

			refresh( );
			Display.getCurrent( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					Object model = evt.getNewValue( );
					Object editpart = getViewer( ).getEditPartRegistry( )
							.get( model );
					if ( editpart instanceof EditPart )
					{
						getViewer( ).flush( );
						if ( !( editpart instanceof EmptyEditPart ) )
						{
							getViewer( ).select( (EditPart) editpart );
						}
					}
					if ( editpart != null )
					{
						getViewer( ).reveal( (EditPart) editpart );
						
						if (LibraryHandleAdapter.CREATE_ELEMENT.equals(evt.getPropertyName( )))
						{
							Request request = new Request(ReportRequest.CREATE_ELEMENT);
							if ( ( (EditPart) editpart ).understandsRequest( request ) )
							{
								( (EditPart) editpart ).performRequest( request );
							}
						}
					}
					
				}
			} );
		}

	}


	private boolean isModelInModuleHandle( )
	{
		List list = getModelChildren( );
		int size = list.size( );
		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );
			if ( obj instanceof DesignElementHandle )
			{
				DesignElementHandle handle = (DesignElementHandle) obj;
				if ( handle.getRoot( ) == null )
				{
					return false;
				}
			}
		}
		return true;
	}

	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.LAYOUT_ROLE,
				new ReportFlowLayoutEditPolicy( ) {

					protected org.eclipse.gef.commands.Command getCreateCommand(
							CreateRequest request )
					{
						List list = getHost( ).getChildren( );
						Boolean direct = (Boolean) request.getExtendedData( ).get( DesignerConstants.DIRECT_CREATEITEM );
						if ( list.size( ) != 0
								&& !( list.get( 0 ) instanceof EmptyEditPart ) 
								&&  (direct == null || 
									 !direct.booleanValue( )))
						{
							return UnexecutableCommand.INSTANCE;
						}
						// EditPart after = getInsertionReference( request );
//						final DesignElementHandle newObject = (DesignElementHandle) request.getExtendedData( )
//								.get( DesignerConstants.KEY_NEWOBJECT );
						
						CreateCommand command = new CreateCommand( request.getExtendedData( ) ) 
						{

							public void execute( )
							{
								super.execute( );
								Display.getCurrent( )
										.asyncExec( new Runnable( ) {

											public void run( )
											{
												SetCurrentEditModelCommand c = new SetCurrentEditModelCommand( getNewObject(),LibraryHandleAdapter.CREATE_ELEMENT );
												c.execute( );
											}
										} );

							}
						};

						Object model = this.getHost( ).getModel( );
						if ( model instanceof SlotHandle )
						{
							command.setParent( model );
						}
						else if ( model instanceof ListBandProxy )
						{
							command.setParent( ( (ListBandProxy) model ).getSlotHandle( ) );
						}
						else
						{
							command.setParent( model );
						}
						// No previous edit part
						// if ( after != null )
						// {
						// command.setAfter( after.getModel( ) );
						// }

						return command;
					}
				} );

		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new ReportContainerEditPolicy( ) );
	}
	
	protected void notifyModelChange(Object focus )
	{
		super.notifyModelChange( focus );
		if ( !isModelInModuleHandle( ) )
		{
			SetCurrentEditModelCommand command = new SetCurrentEditModelCommand( null );
			command.execute( );
		}
	}
}