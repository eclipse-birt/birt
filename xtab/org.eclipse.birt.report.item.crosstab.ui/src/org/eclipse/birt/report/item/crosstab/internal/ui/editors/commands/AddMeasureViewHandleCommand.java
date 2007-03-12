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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands;

import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Add the measure handle to the cross tab in the measure area.
 */
//TODO binding the data
//TODO position
public class AddMeasureViewHandleCommand extends AbstractCrosstabCommand
{

	private CrosstabHandleAdapter handleAdpter;
	private MeasureHandle measureHandle;

	/**
	 * trans name
	 */
	private static final String NAME = "Add MeasureView";

	/**
	 * Constructor
	 * 
	 * @param handleAdpter
	 * @param measureHandle
	 */
	public AddMeasureViewHandleCommand( CrosstabHandleAdapter handleAdpter,
			MeasureHandle measureHandle )
	{
		super( measureHandle );
		this.handleAdpter = handleAdpter;
		this.measureHandle = measureHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute( )
	{
		transStart( NAME );
		CrosstabReportItemHandle reportHandle = (CrosstabReportItemHandle) handleAdpter.getCrosstabItemHandle( );

		try
		{
			// TODO the same measure handle can drop to the measure area?
			int position = reportHandle.getMeasureCount( );
			MeasureViewHandle measureViewHandle = reportHandle.insertMeasure( measureHandle,
					position );
			measureViewHandle.addHeader( );
			
			ComputedColumn bindingColumn = CrosstabAdaptUtil.createComputedColumn( (ExtendedItemHandle)reportHandle.getModelHandle( ), measureHandle );
			((ExtendedItemHandle)reportHandle.getModelHandle( )).addColumnBinding( bindingColumn, false );
			
			CrosstabCellHandle cellHandle = measureViewHandle.getCell( );
			
			
			DataItemHandle dataHandle = DesignElementFactory.getInstance( )
					.newDataItem( measureHandle.getName( ) );

			dataHandle.setResultSetColumn( bindingColumn.getName( ) );
			
			cellHandle.addContent( dataHandle );

			LabelHandle labelHandle = DesignElementFactory.getInstance( ).newLabel(  null );
			labelHandle.setDisplayName( measureHandle.getName( ) );

			measureViewHandle.getHeader( ).addContent( labelHandle );
		}
		catch ( SemanticException e )
		{
			rollBack( );
			e.printStackTrace( );
		}
		transEnd( );
	}
}
