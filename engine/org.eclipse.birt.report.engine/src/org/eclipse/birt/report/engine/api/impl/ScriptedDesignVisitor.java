/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.model.api.AutoTextHandle;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GraphicMasterPageHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;

abstract class ScriptedDesignVisitor extends DesignVisitor {

	/**
	 * report design handle
	 */
	protected ReportDesignHandle handle;

	/**
	 * constructor
	 * 
	 * @param handle           - the entry point to the DE report design IR
	 * @param executionContext - the execution context to execute the onPrepare
	 *                         script
	 */
	ScriptedDesignVisitor(ReportDesignHandle handle) {
		super();

		this.handle = handle;
	}

	abstract protected void handleOnPrepare(ReportItemHandle handle);

	abstract protected void handleOnPrepare(CellHandle handle);

	abstract protected void handleOnPrepare(GroupHandle handle);

	abstract protected void handleOnPrepare(RowHandle handle);

	abstract protected void handleOnPrepare(ReportDesignHandle handle);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.DesignVisitor#visitReportDesign(org.eclipse
	 * .birt.report.model.api.ReportDesignHandle)
	 */
	public void visitReportDesign(ReportDesignHandle handle) {
		handleOnPrepare(handle);

		// Handle Master Page
		SlotHandle pageSlot = handle.getMasterPages();
		for (int i = 0; i < pageSlot.getCount(); i++) {
			apply(pageSlot.get(i));
		}

		// FIXME: add page sequence support
		/*
		 * SlotHandle seqSlot = handle.getPageSequences( ); for ( int i = 0; i <
		 * seqSlot.getCount( ); i++ ) { apply( seqSlot.get( i ) ); }
		 */

		// Handle top-level components
		SlotHandle componentsSlot = handle.getComponents();
		for (int i = 0; i < componentsSlot.getCount(); i++) {
			apply(componentsSlot.get(i));
		}

		// Handle Report Body
		SlotHandle bodySlot = handle.getBody();
		for (int i = 0; i < bodySlot.getCount(); i++) {
			apply(bodySlot.get(i));
		}

		// Handler data source
		SlotHandle dataSourceSlot = handle.getDataSources();
		for (int i = 0; i < dataSourceSlot.getCount(); i++) {
			apply(dataSourceSlot.get(i));
		}

		// Handle data set
		SlotHandle dataSetSlot = handle.getDataSets();
		for (int i = 0; i < dataSetSlot.getCount(); i++) {
			apply(dataSetSlot.get(i));
		}

		// Handle parameters
		SlotHandle parameterSlot = handle.getParameters();
		for (int i = 0; i < parameterSlot.getCount(); i++) {
			apply(parameterSlot.get(i));
		}

		// Handle scratch pad
		SlotHandle scratchSlot = handle.getScratchPad();
		for (int i = 0; i < scratchSlot.getCount(); i++) {
			apply(scratchSlot.get(i));
		}

		// Handle data set
		SlotHandle styleSlot = handle.getStyles();
		for (int i = 0; i < styleSlot.getCount(); i++) {
			apply(styleSlot.get(i));
		}
	}

	/**
	 * setup the master page object from the base master page handle.
	 * 
	 * @param page   page object
	 * @param handle page handle
	 */

	public void visitGraphicMasterPage(GraphicMasterPageHandle handle) {
		// handleOnPrepare( handle );

		// Master page content
		SlotHandle contentSlot = handle.getContent();
		for (int i = 0; i < contentSlot.getCount(); i++) {
			apply(contentSlot.get(i));
		}
	}

	public void visitSimpleMasterPage(SimpleMasterPageHandle handle) {
		// handleOnPrepare( handle );

		SlotHandle headerSlot = handle.getPageHeader();
		for (int i = 0; i < headerSlot.getCount(); i++) {
			apply(headerSlot.get(i));
		}

		SlotHandle footerSlot = handle.getPageFooter();
		for (int i = 0; i < footerSlot.getCount(); i++) {
			apply(footerSlot.get(i));
		}
	}

	public void visitAutoText(AutoTextHandle handle) {
		handleOnPrepare(handle);
	}

	public void visitList(ListHandle handle) {
		handleOnPrepare(handle);

		// Header
		SlotHandle headerSlot = handle.getHeader();
		for (int i = 0; i < headerSlot.getCount(); i++) {
			apply(headerSlot.get(i));
		}

		// Detail
		SlotHandle detailSlot = handle.getDetail();
		for (int i = 0; i < detailSlot.getCount(); i++) {
			apply(detailSlot.get(i));
		}

		// Footer
		SlotHandle footerSlot = handle.getFooter();
		for (int i = 0; i < footerSlot.getCount(); i++) {
			apply(footerSlot.get(i));
		}

		// Multiple groups
		SlotHandle groupsSlot = handle.getGroups();
		for (int i = 0; i < groupsSlot.getCount(); i++) {
			apply(groupsSlot.get(i));
		}
	}

	public void visitFreeForm(FreeFormHandle handle) {
		handleOnPrepare(handle);

		// Set up each individual item in a free form container
		SlotHandle slot = handle.getReportItems();
		for (int i = 0; i < slot.getCount(); i++) {
			apply(slot.get(i));
		}
	}

	public void visitTextDataItem(TextDataHandle handle) {
		handleOnPrepare(handle);
	}

	public void visitParameterGroup(ParameterGroupHandle handle) {
		// handleOnPrepare( handle );

		SlotHandle parameters = handle.getParameters();
		int size = parameters.getCount();
		for (int n = 0; n < size; n++) {
			apply(parameters.get(n));
		}
	}

	public void visitCascadingParameterGroup(CascadingParameterGroupHandle handle) {
		// handleOnPrepare( handle );

		SlotHandle parameters = handle.getParameters();
		int size = parameters.getCount();
		for (int n = 0; n < size; n++) {
			apply(parameters.get(n));
		}
	}

	public void visitScalarParameter(ScalarParameterHandle handle) {
		// handleOnPrepare( handle );
	}

	public void visitLabel(LabelHandle handle) {
		handleOnPrepare(handle);
	}

	public void visitDataItem(DataItemHandle handle) {
		handleOnPrepare(handle);
	}

	public void visitGrid(GridHandle handle) {
		handleOnPrepare(handle);

		// Handle Columns
		SlotHandle columnSlot = handle.getColumns();
		for (int i = 0; i < columnSlot.getCount(); i++) {
			apply(columnSlot.get(i));
		}

		// Handle Rows
		SlotHandle rowSlot = handle.getRows();
		for (int i = 0; i < rowSlot.getCount(); i++) {
			apply(rowSlot.get(i));
		}
	}

	public void visitImage(ImageHandle handle) {
		handleOnPrepare(handle);
	}

	public void visitTable(TableHandle handle) {
		handleOnPrepare(handle);

		// Handle header design
		SlotHandle headerSlot = handle.getHeader();
		for (int i = 0; i < headerSlot.getCount(); i++) {
			apply(headerSlot.get(i));
		}

		// Handler detail design
		SlotHandle detailSlot = handle.getDetail();
		for (int i = 0; i < detailSlot.getCount(); i++) {
			apply(detailSlot.get(i));
		}

		// Handle footer design
		SlotHandle footerSlot = handle.getFooter();
		for (int i = 0; i < footerSlot.getCount(); i++) {
			apply(footerSlot.get(i));
		}

		// Handle table Columns
		SlotHandle columnSlot = handle.getColumns();
		for (int i = 0; i < columnSlot.getCount(); i++) {
			apply(columnSlot.get(i));
		}

		// Handle grouping in table
		SlotHandle groupSlot = handle.getGroups();
		for (int i = 0; i < groupSlot.getCount(); i++) {
			apply(groupSlot.get(i));
		}
	}

	public void visitColumn(ColumnHandle handle) {
		// handleOnPrepare( handle );
	}

	public void visitRow(RowHandle handle) {
		handleOnPrepare(handle);

		// Cells in a row
		SlotHandle cellSlot = handle.getCells();
		for (int i = 0; i < cellSlot.getCount(); i++) {
			apply(cellSlot.get(i));
		}
	}

	public void visitCell(CellHandle handle) {
		handleOnPrepare(handle);

		// Cell contents
		SlotHandle contentSlot = handle.getContent();
		for (int i = 0; i < contentSlot.getCount(); i++) {
			apply(contentSlot.get(i));
		}
	}

	/**
	 * create a list group using the DE's ListGroup.
	 * 
	 * @param handle De's list group
	 * @return engine's list group
	 */
	public void visitListGroup(ListGroupHandle handle) {
		handleOnPrepare(handle);

		// Handle header design
		SlotHandle headerSlot = handle.getHeader();
		for (int i = 0; i < headerSlot.getCount(); i++) {
			apply(headerSlot.get(i));
		}

		// Handle footer design
		SlotHandle footerSlot = handle.getFooter();
		for (int i = 0; i < footerSlot.getCount(); i++) {
			apply(footerSlot.get(i));
		}

	}

	/**
	 * create a table group using the DE's TableGroup.
	 * 
	 * @param handle De's table group
	 * @return engine's table group
	 */
	public void visitTableGroup(TableGroupHandle handle) {
		handleOnPrepare(handle);

		// Handle header design
		SlotHandle headerSlot = handle.getHeader();
		for (int i = 0; i < headerSlot.getCount(); i++) {
			apply(headerSlot.get(i));
		}

		// Handle footer design
		SlotHandle footerSlot = handle.getFooter();
		for (int i = 0; i < footerSlot.getCount(); i++) {
			apply(footerSlot.get(i));
		}
	}

	public void visitTextItem(TextItemHandle handle) {
		handleOnPrepare(handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.DesignVisitor#visitExtendedItem(org.eclipse
	 * .birt.report.model.api.ExtendedItemHandle)
	 */
	protected void visitExtendedItem(ExtendedItemHandle handle) {

	}

}
