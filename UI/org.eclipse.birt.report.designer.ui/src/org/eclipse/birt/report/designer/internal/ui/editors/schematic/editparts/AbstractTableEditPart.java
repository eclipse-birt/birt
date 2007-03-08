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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.layer.TableBorderLayer;
import org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner;
import org.eclipse.birt.report.designer.internal.ui.layout.TableLayout;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayeredPane;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayeredPane;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;

/**
 *Abstract class for the table editpart. 
 */
public abstract class AbstractTableEditPart extends ReportElementEditPart implements
		LayerConstants,
		ITableLayoutOwner
{

	public static final String BORDER_LAYER = "Table Border layer"; //$NON-NLS-1$
	protected FreeformLayeredPane innerLayers;
	protected LayeredPane printableLayers;

	/**Constractor
	 * @param model
	 */
	public AbstractTableEditPart( Object model )
	{
		super( model );
	}

	/**
	 * Returns the layer indicated by the key. Searches all layered panes.
	 * 
	 * @see LayerManager#getLayer(Object)
	 */
	public IFigure getLayer( Object key )
	{
		if ( innerLayers == null )
			return null;
		IFigure layer = innerLayers.getLayer( key );
		if ( layer != null )
			return layer;
		if ( printableLayers == null )
			return null;
		return printableLayers.getLayer( key );
	}

	/**
	 * this layer may be a un-useful layer.
	 * 
	 * @return the layered pane containing all printable content
	 */
	protected LayeredPane getPrintableLayers( )
	{
		if ( printableLayers == null )
			printableLayers = createPrintableLayers( );
		return printableLayers;
	}

	/**
	 * Creates a layered pane and the layers that should be printed.
	 * 
	 * @see org.eclipse.gef.print.PrintGraphicalViewerOperation
	 * @return a new LayeredPane containing the printable layers
	 */
	protected LayeredPane createPrintableLayers( )
	{
		FreeformLayeredPane layeredPane = new FreeformLayeredPane( );
		FreeformLayer layer = new FreeformLayer( );

		layer.setLayoutManager( new TableLayout( this ) );
		layeredPane.add( layer, PRIMARY_LAYER );
		layeredPane.add( new TableBorderLayer( this ), BORDER_LAYER );
		return layeredPane;
	}

	/**
	 * The contents' Figure will be added to the PRIMARY_LAYER.
	 * 
	 * @see org.eclipse.gef.GraphicalEditPart#getContentPane()
	 */
	public IFigure getContentPane( )
	{
		return getLayer( PRIMARY_LAYER );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.layout.ITableLayoutOwner#reLayout()
	 */
	public void reLayout( )
	{
		notifyModelChange( );
		getFigure( ).invalidateTree( );
		// getFigure( ).getUpdateManager( ).addInvalidFigure( getFigure( ) );
		getFigure( ).revalidate( );
	}

	/**
	 * Get the cell on give position.
	 * 
	 * @param rowNumber
	 * @param columnNumber
	 */
	public abstract AbstractCellEditPart getCell( int rowNumber,
			int columnNumber );
}
