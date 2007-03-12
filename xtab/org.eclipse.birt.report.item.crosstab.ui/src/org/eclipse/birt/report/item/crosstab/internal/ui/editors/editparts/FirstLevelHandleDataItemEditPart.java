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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts;

import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.ReportFigureUtilities;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.figures.FirstLevelHandleDataItemFigure;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Menu;

/**
 * The first level handle dataitem editpart.
 */
public class FirstLevelHandleDataItemEditPart extends DataEditPart
{
	/**Constructor
	 * @param model
	 */
	public FirstLevelHandleDataItemEditPart( Object model )
	{
		super( model );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart#createFigure()
	 */
	protected IFigure createFigure( )
	{
		FirstLevelHandleDataItemFigure label = new FirstLevelHandleDataItemFigure( );
		label.setLayoutManager( new StackLayout( ) );
		return label;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#refreshBackgroundColor(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */
	protected void refreshBackgroundColor( DesignElementHandle handle )
	{
		super.refreshBackgroundColor( handle );
		Object obj = handle.getProperty( StyleHandle.BACKGROUND_COLOR_PROP );

		if ( obj == null )
		{
			getFigure( ).setBackgroundColor( ReportColorConstants.TableGuideFillColor );
			getFigure( ).setOpaque( true );
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	public DragTracker getDragTracker( Request req )
	{
		DragEditPartsTracker track = new DragEditPartsTracker( this ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.gef.tools.SelectEditPartTracker#handleButtonDown(int)
			 */
			protected boolean handleButtonDown( int button )
			{
				if ( getCurrentViewer( ) instanceof DeferredGraphicalViewer )
				{
					( (DeferredGraphicalViewer) getCurrentViewer( ) ).initStepDat( );
				}
				boolean bool = super.handleButtonDown( button );

				if ( ( button == 3 || button == 1 ) )
				// && isInState(STATE_INITIAL))
				{
					if ( getSourceEditPart( ) instanceof FirstLevelHandleDataItemEditPart )
					{
						FirstLevelHandleDataItemEditPart first = (FirstLevelHandleDataItemEditPart) getSourceEditPart( );
						if ( first.contains( getLocation( ) ) )
						{
							MenuManager manager = new CrosstabPopMenuProvider( getViewer( ) );
							manager.createContextMenu( getViewer( ).getControl( ) );
							Menu menu = manager.getMenu( );
							
							menu.setVisible( true );
							return true;
						}
					}
				}
				return bool;
			}
		};
		return track;
	}

	/**The point if in the triangle.
	 * @param pt
	 * @return
	 */
	public boolean contains( Point pt )
	{
		FirstLevelHandleDataItemFigure figure = (FirstLevelHandleDataItemFigure) getFigure( );
		Rectangle bounds = figure.getClientArea( );
		Point center = figure.getCenterPoint( bounds );

		figure.translateToAbsolute( center );
		return ReportFigureUtilities.isInTriangle( center, bounds.height, pt );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DataEditPart#getText()
	 */
	protected String getText( )
	{
		String text = ( (DataItemHandle) getModel( ) ).getDisplayLabel( );
		return text;
	}
}
