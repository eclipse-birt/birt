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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.GridFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.TableFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.AbstractGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.TableGuideHandle;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;

/**
 * Grid EditPart,control the UI & model of grid
 */
public class GridEditPart extends TableEditPart
{

	private static final String GUIDEHANDLE_TEXT = Messages.getString( "GridEditPart.GUIDEHANDLE_TEXT" ); //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param obj
	 */
	public GridEditPart( Object obj )
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
		TableFigure viewport = new GridFigure( );
		viewport.setOpaque( false );
		innerLayers = new FreeformLayeredPane( );
		createLayers( innerLayers );
		viewport.setContents( innerLayers );
		return viewport;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart#createGuideHandle()
	 */
	protected AbstractGuideHandle createGuideHandle( )
	{
		TableGuideHandle handle = new TableGuideHandle( this );
		handle.setIndicatorLabel( getGuideLabel( ) );
		handle.setIndicatorIcon( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_GRID ) );
		return handle;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart#getGuideLabel()
	 */
	public String getGuideLabel( )
	{
		return GUIDEHANDLE_TEXT;
	}
	
}