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

import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapt;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportRootFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.AbstractPageFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportDesignLayout;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

/**
 * This is the content edit part for Library. All other library elements puts on
 * to it
 */
public class LibraryReportDesignEditPart extends ReportDesignEditPart
		implements
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
		// TODO Auto-generated constructor stub
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
		return HandleAdapterFactory.getInstance( ).getLibraryHandleAdapter( )
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

		( (AbstractPageFlowLayout) getFigure( ).getLayoutManager( ) )
				.setInitSize( bounds );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportDesignEditPart#activate()
	 */
	public void activate( )
	{
		HandleAdapterFactory.getInstance( ).getLibraryHandleAdapter((LibraryHandle)getModel() )
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
		HandleAdapterFactory.getInstance( ).getLibraryHandleAdapter((LibraryHandle)getModel() )
				.removePropertyChangeListener( this );
		super.deactivate( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange( PropertyChangeEvent evt )
	{
		if ( evt.getPropertyName( ).equals( LibraryHandleAdapt.CURRENTMODEL ) )
		{
			refresh( );
			Display.getCurrent( ).asyncExec( new Runnable( )
			{

				public void run( )
				{
					List mediatorSelection = SessionHandleAdapter.getInstance().getMediator().getCurrentState().getSelectionObject();
					if (mediatorSelection.size() == 1 && mediatorSelection.get(0) instanceof LibraryHandle)	
					{
						return ;
					}
					List list = getChildren();
					
					EditPartViewer viewer = getViewer();
					if ( viewer instanceof DeferredGraphicalViewer )
					{
						( (DeferredGraphicalViewer) viewer ).setSelection(
								new StructuredSelection( list ), false );
					}
					//getViewer().setSelection(new StructuredSelection(list));
				}
			} );
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.core.Listener#elementChanged(org.eclipse.birt.report.model.api.DesignElementHandle,
	 *      org.eclipse.birt.report.model.api.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		// TODO Auto-generated method stub

	}
}