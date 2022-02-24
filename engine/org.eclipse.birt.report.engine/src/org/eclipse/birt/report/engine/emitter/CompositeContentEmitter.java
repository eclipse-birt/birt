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

package org.eclipse.birt.report.engine.emitter;

import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * Emitter the input to mutiple outputs.
 *
 */
public class CompositeContentEmitter extends ContentEmitterAdapter {
	protected ArrayList emitters = new ArrayList();

	protected String format = "html";

	public CompositeContentEmitter() {
	}

	public CompositeContentEmitter(String format) {
		this.format = format;
	}

	public void addEmitter(IContentEmitter emitter) {
		emitters.add(emitter);
	}

	public void end(IReportContent report) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).end(report);
		}
	}

	public void endGroup(IGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endGroup(group);
		}
	}

	public void endList(IListContent list) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endList(list);
		}
	}

	public void endListGroup(IListGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endListGroup(group);
		}
	}

	public void endTableGroup(ITableGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endTableGroup(group);
		}
	}

	public void startAutoText(IAutoTextContent autoText) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startAutoText(autoText);
		}
	}

	public void startGroup(IGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startGroup(group);
		}
	}

	public void startListGroup(IListGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startListGroup(group);
		}
	}

	public void startTableGroup(ITableGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startTableGroup(group);
		}
	}

	public void endCell(ICellContent cell) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endCell(cell);
		}
	}

	public void endContainer(IContainerContent container) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endContainer(container);
		}
	}

	public void endContent(IContent content) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endContent(content);
		}
	}

	public void endPage(IPageContent page) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endPage(page);
		}
	}

	public void endRow(IRowContent row) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endRow(row);
		}
	}

	public void startTableBand(ITableBandContent band) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startTableBand(band);
		}
	}

	public void endTableBand(ITableBandContent band) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endTableBand(band);
		}
	}

	public void endTable(ITableContent table) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endTable(table);
		}
	}

	public String getOutputFormat() {
		return format;
	}

	public void initialize(IEmitterServices service) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).initialize(service);
		}
	}

	public void start(IReportContent report) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).start(report);
		}
	}

	public void startCell(ICellContent cell) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startCell(cell);
		}
	}

	public void startContainer(IContainerContent container) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startContainer(container);
		}
	}

	public void startContent(IContent content) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startContent(content);
		}
	}

	public void startData(IDataContent data) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startData(data);
		}
	}

	public void startForeign(IForeignContent foreign) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startForeign(foreign);
		}
	}

	public void startImage(IImageContent image) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startImage(image);
		}
	}

	public void startLabel(ILabelContent label) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startLabel(label);
		}
	}

	public void startPage(IPageContent page) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startPage(page);
		}
	}

	public void startRow(IRowContent row) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startRow(row);
		}
	}

	public void startTable(ITableContent table) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startTable(table);
		}
	}

	public void startListBand(IListBandContent band) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startListBand(band);
		}
	}

	public void endListBand(IListBandContent band) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).endListBand(band);
		}
	}

	public void startList(IListContent list) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startList(list);
		}
	}

	public void startText(ITextContent text) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			((IContentEmitter) emitters.get(i)).startText(text);
		}
	}

}
