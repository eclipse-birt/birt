/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands;

import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Add the measure handle to the cross tab through the virtual editpart.
 */
public class CreateMeasureViewCommand extends AbstractCrosstabCommand {

	private CrosstabHandleAdapter handleAdpter;
	private MeasureHandle measureHandle;

	/**
	 * Trans name
	 */
	// private static final String NAME = "Create MeasureViewHandle";
	private static final String NAME = Messages.getString("CreateMeasureViewCommand.TransName");//$NON-NLS-1$

	/**
	 * Constructor
	 *
	 * @param handleAdpter
	 * @param measureHandle
	 */
	public CreateMeasureViewCommand(CrosstabHandleAdapter handleAdpter, MeasureHandle measureHandle) {
		super(measureHandle);
		this.handleAdpter = handleAdpter;
		this.measureHandle = measureHandle;

		setLabel(NAME);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		transStart(NAME);
		CrosstabReportItemHandle reportHandle = (CrosstabReportItemHandle) handleAdpter.getCrosstabItemHandle();

		try {
			if (reportHandle.getCube() == null) {
				reportHandle.setCube(CrosstabAdaptUtil.getCubeHandle(measureHandle));
			}
			CrosstabAdaptUtil.addMeasureHandle(reportHandle, measureHandle, 0);
//			MeasureViewHandle measureViewHandle = reportHandle.insertMeasure( measureHandle,
//					0 );
//			measureViewHandle.addHeader( );
//
//			ComputedColumn bindingColumn = CrosstabAdaptUtil.createComputedColumn( (ExtendedItemHandle)reportHandle.getModelHandle( ), measureHandle );
//			ComputedColumnHandle bindingHandle =((ExtendedItemHandle)reportHandle.getModelHandle( )).addColumnBinding( bindingColumn, false );
//
//			CrosstabCellHandle cellHandle = measureViewHandle.getCell( );
//
//			DataItemHandle dataHandle = DesignElementFactory.getInstance( )
//					.newDataItem( measureHandle.getName( ) );
//			dataHandle.setResultSetColumn( bindingHandle.getName( ) );
//
//			cellHandle.addContent( dataHandle );
//
//			LabelHandle labelHandle = DesignElementFactory.getInstance( ).newLabel(  null );
//			labelHandle.setText( measureHandle.getName( ) );
//
//			measureViewHandle.getHeader( ).addContent( labelHandle );
		} catch (SemanticException e) {
			rollBack();
			ExceptionUtil.handle(e);
			return;
		}
		transEnd();
	}
}
