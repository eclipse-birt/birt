/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.notification.DeferredRefreshManager;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.RootDragTracker;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportDesignLayout;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.FreeformGraphicalRootEditPart;
import org.eclipse.gef.editparts.GuideLayer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;

/**
 * Root editPart
 *  
 */
public class ReportRootEditPart extends ScalableFreeformRootEditPart
{

	private DeferredRefreshManager refreshManager;

	/**
	 * Constructor
	 * 
	 * @param manager
	 */
	public ReportRootEditPart( DeferredRefreshManager manager )
	{
		this.refreshManager = manager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request req )
	{
		return new RootDragTracker( );
	}

	/**
	 * gets the DeferredRefreshManager, all editPart have only one.
	 * 
	 * @return
	 */
	public DeferredRefreshManager getRefreshManager( )
	{
		return refreshManager;
	}

	/**
	 * Creates a layered pane and the layers that should be printed.
	 * 
	 * @see org.eclipse.gef.print.PrintGraphicalViewerOperation
	 * @return a new LayeredPane containing the printable layers
	 */
	protected LayeredPane createPrintableLayers( )
	{
		FreeformLayeredPane layeredPane = new FreeformLayeredPane( ) {

			protected void paintFigure( Graphics graphics )
			{
				graphics.setBackgroundColor( ColorConstants.gray );
				graphics.fillRectangle( getBounds( ) );
			}
		};

		
		FreeformLayer layer = new FreeformLayer( )
		{
			
			
			/* (non-Javadoc)
			 * @see org.eclipse.draw2d.FreeformLayer#getFreeformExtent()
			 */
			public Rectangle getFreeformExtent( )
			{	
				Rectangle rect = super.getFreeformExtent();
				Rectangle retValue = rect.getCopy();
				Object obj = getViewer().getProperty( DeferredGraphicalViewer.REPORT_SIZE );
				if (obj instanceof Rectangle)
				{
					Rectangle temp = (Rectangle)obj;
					if (temp.width - rect.right() <= ReportDesignLayout.MINRIGHTSPACE)
					{
						retValue.width = retValue.width + ReportDesignLayout.MINRIGHTSPACE;
					}
					if (temp.height - rect.bottom() <= ReportDesignLayout.MINBOTTOMSPACE)
					{
						retValue.height = retValue.height + ReportDesignLayout.MINBOTTOMSPACE;
					}
					
				}
				return retValue;
			}
		};
		layeredPane.add( layer, PRIMARY_LAYER );

		layeredPane.add( new ConnectionLayer( ), CONNECTION_LAYER );
		return layeredPane;
	}

	/**
	 * @see FreeformGraphicalRootEditPart#createLayers(LayeredPane)
	 */
	protected void createLayers( LayeredPane layeredPane )
	{
		layeredPane.add( getScaledLayers( ), SCALABLE_LAYERS );

		layeredPane.add( new FreeformLayer( ), HANDLE_LAYER );
		layeredPane.add( new FeedbackLayer( ), FEEDBACK_LAYER );
		layeredPane.add( new GuideLayer( ), GUIDE_LAYER );
	}

	class FeedbackLayer extends FreeformLayer
	{

		FeedbackLayer( )
		{
			setEnabled( false );
		}
	}
}