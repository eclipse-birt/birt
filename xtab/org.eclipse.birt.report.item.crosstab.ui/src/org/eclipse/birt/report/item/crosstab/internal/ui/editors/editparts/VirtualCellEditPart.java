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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.CellBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.CellFigure;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editpolicies.VirtualCrosstabCellFlowLayoutEditPolicy;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;

/**
 * When create a empty cross tab,It need four cell editpart, but the cross tab don't have the cell handle.
 * So create the virtual editpart.The model is VirtualCrosstabCellAdapter.
 */
public class VirtualCellEditPart extends CrosstabCellEditPart
{

	/**Constructor
	 * @param model
	 */
	public VirtualCellEditPart( Object model )
	{
		super( model );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart#refreshFigure()
	 */
	public void refreshFigure( )
	{
		CellBorder cborder = new CellBorder( );

		if ( getFigure( ).getBorder( ) instanceof CellBorder )
		{
			cborder.setBorderInsets( ( (CellBorder) getFigure( ).getBorder( ) ).getBorderInsets( ) );
		}
		initEmptyBorder( cborder );
		getFigure( ).setBorder( cborder );
		updateBlankString( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart#createEditPolicies()
	 */
	protected void createEditPolicies( )
	{
		installEditPolicy( EditPolicy.COMPONENT_ROLE,
				new ReportComponentEditPolicy( ) );
		installEditPolicy( EditPolicy.LAYOUT_ROLE,
				new VirtualCrosstabCellFlowLayoutEditPolicy( ) );
		installEditPolicy( EditPolicy.CONTAINER_ROLE,
				new ReportContainerEditPolicy( ) );
	}

	/**
	 * Draws the string when the cell is empty
	 */
	public void updateBlankString( )
	{
		int type = ( (VirtualCrosstabCellAdapter) getCrosstabCellAdapter( ) ).getType( );
		switch (type)
		{
			case VirtualCrosstabCellAdapter.COLUMN_TYPE:
				( (CellFigure) getFigure( ) ).setBlankString( "column" );
				break;
			case VirtualCrosstabCellAdapter.ROW_TYPE:
				( (CellFigure) getFigure( ) ).setBlankString( "rows" );
				break;
			case VirtualCrosstabCellAdapter.MEASURE_TYPE:
				( (CellFigure) getFigure( ) ).setBlankString( "measure" );
				break;
			default:
				( (CellFigure) getFigure( ) ).setBlankString( null );	
		}

	}

	private void initEmptyBorder( CellBorder cborder )
	{
		cborder.bottomStyle = CellBorder.STYLE_NONO;
		cborder.topStyle = CellBorder.STYLE_NONO;
		cborder.leftStyle = CellBorder.STYLE_NONO;
		cborder.rightStyle = CellBorder.STYLE_NONO;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart#getHandleList()
	 */
	protected List getHandleList( )
	{
		//TODO the virtual editpart alllow to drag the cell to adjust the column and row?
		return super.getHandleList( );
	}
}
