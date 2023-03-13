/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;

public class PDFTableGroupLM extends PDFGroupLM implements IBlockStackingLayoutManager {

	protected PDFTableLM tableLM = null;
	protected boolean needRepeat = false;

	public PDFTableGroupLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		tableLM = getTableLayoutManager();
		tableLM.startGroup((IGroupContent) content);
	}

	@Override
	protected boolean traverseChildren() throws BirtException {

		boolean childBreak = super.traverseChildren();
		if (!childBreak) {
			int heightAdjustment = tableLM.endGroup((IGroupContent) content);
			currentBP += heightAdjustment;
		}

		return childBreak;
	}

	@Override
	protected void createRoot() {
		if (root == null) {
			root = (ContainerArea) AreaFactory.createBlockContainer(content);
		}
	}

	@Override
	protected void initialize() throws BirtException {
		if (root == null && keepWithCache.isEmpty() && !isFirst) {
			repeatCount = 0;
			needRepeat = true;
		}
		super.initialize();

	}

	private void repeat() throws BirtException {
		if (isFirst || tableLM.isFirst) {
			isFirst = false;
			return;
		}
		if (!needRepeat || !isCurrentDetailBand()) {
			return;
		}
		ITableBandContent header = (ITableBandContent) groupContent.getHeader();
		if (!isRepeatHeader() || header == null || header.getChildren().isEmpty()) {
			return;
		}
		if (child != null) {
			IContent content = child.getContent();
			if (content instanceof ITableBandContent) {
				if (((ITableBandContent) content).getBandType() == IBandContent.BAND_GROUP_HEADER) {
					return;
				}

			}
		}
		IReportItemExecutor headerExecutor = new DOMReportItemExecutor(header);
		headerExecutor.execute();
		PDFTableRegionLM regionLM = tableLM.getTableRegionLayout();
		regionLM.initialize(header);
		regionLM.setGroupLevel(getGroupLevel());
		regionLM.layout();
		TableArea tableRegion = (TableArea) tableLM.getContent().getExtension(IContent.LAYOUT_EXTENSION);
		if (tableRegion != null && tableRegion.getHeight() < getCurrentMaxContentHeight()) {
			// add to root
			Iterator iter = tableRegion.getChildren();
			RowArea row = null;
			int count = 0;
			while (iter.hasNext()) {
				row = (RowArea) iter.next();
				// FIXME should add to the first line of this group
				addArea(row, false, pageBreakAvoid);
				tableLM.addRow(row, true, false);
				count++;
			}

			repeatCount += count;
		}
		tableLM.getContent().setExtension(IContent.LAYOUT_EXTENSION, null);
		needRepeat = false;
	}

	protected int getGroupLevel() {
		if (content instanceof IGroupContent) {
			return ((IGroupContent) content).getGroupLevel();
		}
		return 0;
	}

	@Override
	protected void repeatHeader() throws BirtException {
		repeat();
		skipCachedRow();
	}

	@Override
	protected IReportItemExecutor createExecutor() {
		return executor;
	}

	protected boolean isCurrentDetailBand() {
		if (child != null) {
			IContent c = child.getContent();
			if (c != null) {
				if (c instanceof IGroupContent) {
					return true;
				} else if (c instanceof IBandContent) {
					IBandContent band = (IBandContent) c;
					if (band.getBandType() == IBandContent.BAND_DETAIL) {
						return true;
					}
				}
			}
		} else {
			return true;
		}
		return false;
	}

	protected void skipCachedRow() {
		if (keepWithCache.isEmpty()) {
			return;
		}
		Iterator iter = keepWithCache.getChildren();
		while (iter.hasNext()) {
			ContainerArea container = (ContainerArea) iter.next();
			skip(container);
		}
	}

	protected void skip(ContainerArea area) {
		if (area instanceof RowArea) {
			tableLM.skipRow((RowArea) area);
		} else {
			Iterator iter = area.getChildren();
			while (iter.hasNext()) {
				ContainerArea container = (ContainerArea) iter.next();
				skip(container);
			}
		}
	}

	public void updateHeight(int height) {
		currentBP += height;
	}
}
