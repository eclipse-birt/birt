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

	protected ArrayList<IContentEmitter> emitters = new ArrayList<IContentEmitter>();

	protected String format = "html";

	/**
	 * Constructor
	 */
	public CompositeContentEmitter() {
	}

	/**
	 * Composite the content emitter
	 *
	 * @param format format
	 */
	public CompositeContentEmitter(String format) {
		this.format = format;
	}

	/**
	 * Add an emitter
	 *
	 * @param emitter content emitter
	 */
	public void addEmitter(IContentEmitter emitter) {
		emitters.add(emitter);
	}

	@Override
	public void end(IReportContent report) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).end(report);
		}
	}

	@Override
	public void endGroup(IGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endGroup(group);
		}
	}

	@Override
	public void endList(IListContent list) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endList(list);
		}
	}

	@Override
	public void endListGroup(IListGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endListGroup(group);
		}
	}

	@Override
	public void endTableGroup(ITableGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endTableGroup(group);
		}
	}

	@Override
	public void startAutoText(IAutoTextContent autoText) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startAutoText(autoText);
		}
	}

	@Override
	public void startGroup(IGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startGroup(group);
		}
	}

	@Override
	public void startListGroup(IListGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startListGroup(group);
		}
	}

	@Override
	public void startTableGroup(ITableGroupContent group) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startTableGroup(group);
		}
	}

	@Override
	public void endCell(ICellContent cell) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endCell(cell);
		}
	}

	@Override
	public void endContainer(IContainerContent container) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endContainer(container);
		}
	}

	@Override
	public void endContent(IContent content) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endContent(content);
		}
	}

	@Override
	public void endPage(IPageContent page) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endPage(page);
		}
	}

	@Override
	public void endRow(IRowContent row) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endRow(row);
		}
	}

	@Override
	public void startTableBand(ITableBandContent band) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startTableBand(band);
		}
	}

	@Override
	public void endTableBand(ITableBandContent band) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endTableBand(band);
		}
	}

	@Override
	public void endTable(ITableContent table) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endTable(table);
		}
	}

	@Override
	public String getOutputFormat() {
		return format;
	}

	@Override
	public void initialize(IEmitterServices service) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).initialize(service);
		}
	}

	@Override
	public void start(IReportContent report) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).start(report);
		}
	}

	@Override
	public void startCell(ICellContent cell) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startCell(cell);
		}
	}

	@Override
	public void startContainer(IContainerContent container) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startContainer(container);
		}
	}

	@Override
	public void startContent(IContent content) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startContent(content);
		}
	}

	@Override
	public void startData(IDataContent data) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startData(data);
		}
	}

	@Override
	public void startForeign(IForeignContent foreign) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startForeign(foreign);
		}
	}

	@Override
	public void startImage(IImageContent image) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startImage(image);
		}
	}

	@Override
	public void startLabel(ILabelContent label) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startLabel(label);
		}
	}

	@Override
	public void startPage(IPageContent page) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startPage(page);
		}
	}

	@Override
	public void startRow(IRowContent row) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startRow(row);
		}
	}

	@Override
	public void startTable(ITableContent table) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startTable(table);
		}
	}

	@Override
	public void startListBand(IListBandContent band) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startListBand(band);
		}
	}

	@Override
	public void endListBand(IListBandContent band) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).endListBand(band);
		}
	}

	@Override
	public void startList(IListContent list) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startList(list);
		}
	}

	@Override
	public void startText(ITextContent text) throws BirtException {
		for (int i = 0; i < emitters.size(); i++) {
			emitters.get(i).startText(text);
		}
	}

}
