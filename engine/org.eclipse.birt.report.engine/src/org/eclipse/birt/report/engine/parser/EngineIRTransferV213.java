/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.parser;

import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.FreeFormHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;

public class EngineIRTransferV213 extends DesignVisitor {

	/**
	 * logger used to log the error.
	 */
	protected static Logger logger = Logger.getLogger(EngineIRTransferV213.class.getName());

	/**
	 * Factory IR created by this visitor
	 */
	protected Report report;

	/**
	 * report design handle
	 */
	protected ReportDesignHandle handle;

	/**
	 * Used to fix half-baked handle, such as: fix the new added empty cell created
	 * in format irregular table or grid. fix default master page.
	 */
	long newCellId = -1;

	/**
	 * constructor
	 * 
	 * @param handle the entry point to the DE report design IR
	 * 
	 */
	public EngineIRTransferV213(ReportDesignHandle handle, Report report) {
		super();
		this.handle = handle;
		this.report = report;
	}

	/**
	 * translate the DE's IR to FPE's IR.
	 * 
	 * @return FPE's IR.
	 */
	public void transfer() {
		apply(handle);
		report.setVersion(ReportDocumentConstants.BIRT_ENGINE_VERSION_2_1_3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.DesignVisitor#visitReportDesign(org.eclipse
	 * .birt.report.model.api.ReportDesignHandle)
	 */
	public void visitReportDesign(ReportDesignHandle handle) {
		// Handle Master Page
		SlotHandle pageSlot = handle.getMasterPages();
		for (int i = 0; i < pageSlot.getCount(); i++) {
			apply(pageSlot.get(i));
		}

		// If there is no master page, set a default one.
		if (pageSlot.getCount() < 1) {
			PageSetupDesign pageSetup = report.getPageSetup();
			if (pageSetup.getMasterPageCount() > 0) {
				MasterPageDesign masterPage = pageSetup.getMasterPage(0);
				masterPage.setID(generateUniqueID());
			}
		}

		// Handle Report Body
		SlotHandle bodySlot = handle.getBody();
		for (int i = 0; i < bodySlot.getCount(); i++) {
			apply(bodySlot.get(i));
		}
	}

	public void visitList(ListHandle handle) {
		// get ListItem
		ListItemDesign listItem = (ListItemDesign) report.getReportItemByID(handle.getID());

		// Header
		SlotHandle headerSlot = handle.getHeader();
		if (headerSlot.getCount() > 0) {
			BandDesign header = listItem.getHeader();
			handleBand(header, headerSlot);
		}

		// Multiple groups
		SlotHandle groupsSlot = handle.getGroups();
		for (int i = 0; i < groupsSlot.getCount(); i++) {
			apply(groupsSlot.get(i));
		}

		// List detail
		SlotHandle detailSlot = handle.getDetail();
		if (detailSlot.getCount() > 0) {
			BandDesign detail = listItem.getDetail();
			handleBand(detail, detailSlot);
		}

		// List Footer
		SlotHandle footerSlot = handle.getFooter();
		if (footerSlot.getCount() > 0) {
			BandDesign footer = listItem.getFooter();
			handleBand(footer, footerSlot);
		}
	}

	public void visitFreeForm(FreeFormHandle handle) {
		// Set up each individual item in a free form container
		SlotHandle slot = handle.getReportItems();
		for (int i = 0; i < slot.getCount(); i++) {
			apply(slot.get(i));
		}
	}

	public void visitGrid(GridHandle handle) {
		// Create Grid Item
		GridItemDesign grid = new GridItemDesign();

		// Handle Rows
		SlotHandle rowSlot = handle.getRows();
		for (int i = 0; i < rowSlot.getCount(); i++) {
			apply(rowSlot.get(i));
		}

		fixGridDummyCell(grid);
	}

	public void visitTable(TableHandle handle) {
		// get Table Item
		TableItemDesign table = (TableItemDesign) report.getReportItemByID(handle.getID());

		// Handle Table Header
		SlotHandle headerSlot = handle.getHeader();
		if (headerSlot.getCount() > 0) {
			BandDesign header = table.getHeader();
			handleBand(header, headerSlot);
		}

		// Handle grouping in table
		SlotHandle groupSlot = handle.getGroups();
		for (int i = 0; i < groupSlot.getCount(); i++) {
			apply(groupSlot.get(i));
		}

		// Handle detail section
		SlotHandle detailSlot = handle.getDetail();
		if (detailSlot.getCount() > 0) {
			BandDesign detail = table.getDetail();
			handleBand(detail, detailSlot);
		}

		// Handle table footer
		SlotHandle footerSlot = handle.getFooter();
		if (footerSlot.getCount() > 0) {
			BandDesign footer = table.getFooter();
			handleBand(footer, footerSlot);
		}

		fixTableDummyCell(table);
	}

	public void visitRow(RowHandle handle) {
		// Cells in a row
		SlotHandle cellSlot = handle.getCells();
		for (int i = 0; i < cellSlot.getCount(); i++) {
			apply(cellSlot.get(i));
		}
	}

	public void visitCell(CellHandle handle) {
		// Cell contents
		SlotHandle contentSlot = handle.getContent();
		for (int i = 0; i < contentSlot.getCount(); i++) {
			apply(contentSlot.get(i));
		}
	}

	/**
	 * create a list band using the items in slot.
	 * 
	 * @param elements items in DE's IR
	 * @return ListBand.
	 */
	private BandDesign handleBand(BandDesign band, SlotHandle elements) {
		band.setID(generateUniqueID());
		setupElementIDMap(band);
		for (int i = 0; i < elements.getCount(); i++) {
			apply(elements.get(i));
		}
		return band;
	}

	/**
	 * create a list group using the DE's ListGroup.
	 * 
	 * @param handle De's list group
	 * @return engine's list group
	 */
	public void visitListGroup(ListGroupHandle handle) {
		ListGroupDesign listGroup = (ListGroupDesign) report.getReportItemByID(handle.getID());

		SlotHandle headerSlot = handle.getHeader();
		if (headerSlot.getCount() > 0) {
			BandDesign header = listGroup.getHeader();
			handleBand(header, headerSlot);
		}

		SlotHandle footerSlot = handle.getFooter();
		if (footerSlot.getCount() > 0) {
			BandDesign footer = listGroup.getFooter();
			handleBand(footer, headerSlot);
		}
	}

	/**
	 * create a table group using the DE's TableGroup.
	 * 
	 * @param handle De's table group
	 * @return engine's table group
	 */
	public void visitTableGroup(TableGroupHandle handle) {
		TableGroupDesign tableGroup = (TableGroupDesign) report.getReportItemByID(handle.getID());

		SlotHandle headerSlot = handle.getHeader();
		if (headerSlot.getCount() > 0) {
			BandDesign header = tableGroup.getHeader();
			handleBand(header, headerSlot);
		}

		SlotHandle footerSlot = handle.getFooter();
		if (footerSlot.getCount() > 0) {
			BandDesign footer = tableGroup.getFooter();
			handleBand(footer, footerSlot);
		}
	}

	private void setupElementIDMap(ReportElementDesign rptElement) {
		report.setReportItemInstanceID(rptElement.getID(), rptElement);
	}

	protected long generateUniqueID() {
		return newCellId--;
	}

	private void fixGridDummyCell(GridItemDesign grid) {
		for (int i = 0; i < grid.getRowCount(); i++) {
			RowDesign row = grid.getRow(i);
			for (int j = 0; j < row.getCellCount(); j++) {
				CellDesign cell = row.getCell(j);
				if (cell.getHandle() == null) {
					cell.setID(-1L);
				}
			}
		}
	}

	private void fixTableDummyCell(TableItemDesign table) {
		// table header
		fixTableBandDummyCell((TableBandDesign) table.getHeader());

		// group header
		for (int i = 0; i < table.getGroupCount(); i++) {
			GroupDesign group = table.getGroup(i);
			TableBandDesign header = (TableBandDesign) group.getHeader();
			fixTableBandDummyCell(header);
		}

		// detail
		fixTableBandDummyCell((TableBandDesign) table.getDetail());

		// group footer
		for (int i = table.getGroupCount() - 1; i >= 0; i--) {
			GroupDesign group = table.getGroup(i);
			TableBandDesign footer = (TableBandDesign) group.getFooter();
			fixTableBandDummyCell(footer);
		}

		// table footer
		fixTableBandDummyCell((TableBandDesign) table.getFooter());
	}

	public void fixTableBandDummyCell(TableBandDesign band) {
		if (band != null) {
			for (int i = 0; i < band.getRowCount(); i++) {
				RowDesign row = band.getRow(i);
				for (int j = 0; j < row.getCellCount(); j++) {
					CellDesign cell = row.getCell(j);
					if (cell.getHandle() == null) {
						cell.setID(-1L);
					}
				}
			}
		}
	}
}
