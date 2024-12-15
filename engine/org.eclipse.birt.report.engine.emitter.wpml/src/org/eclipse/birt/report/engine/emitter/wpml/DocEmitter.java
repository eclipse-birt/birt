/*******************************************************************************
 * Copyright (c) 2006, 2024 Inetsoft Technology Corp and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.engine.emitter.wpml;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
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
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.internal.content.wrap.TableContentWrapper;
import org.eclipse.birt.report.engine.presentation.ContentEmitterVisitor;

/**
 * Representation of standard doc emitter
 *
 * @since 3.3
 *
 */
public class DocEmitter extends ContentEmitterAdapter {

	/** property: DocxEmitter maximum column count */
	public final static int MAX_COLUMN = 63;

	/**
	 * property: Word emitter, use wrapped styling table to get margin and padding
	 * (standard up to BIRT 4.17)
	 *
	 * @since 4.18
	 */
	public static final String WORD_MARGIN_PADDING_WRAPPED_TABLE = "WordEmitter.WrappedTableForMarginPadding";

	/**
	 * property: Word emitter, use a standard grid like master structure for header
	 * and footer area independent of the content (standard up to BIRT 4.17)
	 *
	 * @since 4.18
	 */
	public static final String WORD_HEADER_FOOTER_WRAPPED_TABLE = "WordEmitter.WrappedTableHeaderFooter";

	/**
	 * property: Word emitter, use combined calculation of margin and padding
	 *
	 * @since 4.18
	 */
	public static final String WORD_MARGIN_PADDING_COMBINE = "WordEmitter.CombineMarginPadding";

	/**
	 * property: Word emitter, add empty paragraph for all cells independent of the
	 * cell content (standard up to BIRT 4.17)
	 *
	 * @since 4.18
	 */
	public static final String WORD_ADD_EMPTY_PARAGRAPH_FOR_TABLE_CELL = "WordEmitter.AddEmptyParagraphForTableCell";

	/**
	 * property: Word emitter, add empty paragraph for the list table cell
	 * independent of the cell content (standard up to BIRT 4.17)
	 *
	 * @since 4.18
	 */
	public static final String WORD_ADD_EMPTY_PARAGRAPH_FOR_LIST_CELL = "WordEmitter.AddEmptyParagraphForListCell";

	private static Logger logger = Logger.getLogger(DocEmitter.class.getName());

	protected AbstractEmitterImpl emitterImplement = null;

	protected ContentEmitterVisitor contentVisitor;

	private int omitCellLayer = 0;

	private boolean isClipped = false;

	/**
	 * Constructor
	 */
	public DocEmitter() {
		contentVisitor = new ContentEmitterVisitor(this);
		createEmitterImplement();
	}

	protected void createEmitterImplement() {
		emitterImplement = new DocEmitterImpl(contentVisitor);
	}

	@Override
	public void initialize(IEmitterServices service) throws EngineException {
		emitterImplement.initialize(service);
	}

	@Override
	public String getOutputFormat() {
		return emitterImplement.getOutputFormat();
	}

	@Override
	public void startPage(IPageContent page) throws BirtException {
		try {
			emitterImplement.startPage(page);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void end(IReportContent report) throws BirtException {
		try {
			emitterImplement.end(report);
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	@Override
	public void endCell(ICellContent cell) {
		if (omitCellLayer != 0) {
			omitCellLayer--;
			if (omitCellLayer == 0) {
				isClipped = false;
			}
			return;
		}
		emitterImplement.endCell(cell);
	}

	@Override
	public void endContainer(IContainerContent container) {
		if (isClipped) {
			return;
		}
		emitterImplement.endContainer(container);
	}

	@Override
	public void startContainer(IContainerContent container) {
		if (isClipped) {
			return;
		}
		emitterImplement.startContainer(container);
	}

	@Override
	public void endContent(IContent content) {
		if (isClipped) {
			return;
		}
		emitterImplement.endContent(content);
	}

	@Override
	public void endGroup(IGroupContent group) {
		if (isClipped) {
			return;
		}
		emitterImplement.endGroup(group);
	}

	/**
	 * Compute accounted page properties
	 *
	 * @param page page content
	 */
	public void accountPageProp(IPageContent page) {
		emitterImplement.computePageProperties(page);
	}

	@Override
	public void endList(IListContent list) {
		if (isClipped) {
			return;
		}
		emitterImplement.endList(list);
	}

	@Override
	public void endListBand(IListBandContent listBand) {
		if (isClipped) {
			return;
		}
		emitterImplement.endListBand(listBand);
	}

	@Override
	public void endListGroup(IListGroupContent group) {
		if (isClipped) {
			return;
		}
		emitterImplement.endListGroup(group);

	}

	@Override
	public void endPage(IPageContent page) {
		emitterImplement.endPage(page);
	}

	@Override
	public void endRow(IRowContent row) {
		if (isClipped) {
			return;
		}
		emitterImplement.endRow(row);
	}

	@Override
	public void endTable(ITableContent table) {
		if (isClipped) {
			return;
		}
		emitterImplement.endTable(table);
	}

	@Override
	public void endTableBand(ITableBandContent band) {
		if (isClipped) {
			return;
		}
		emitterImplement.endTableBand(band);
	}

	@Override
	public void endTableGroup(ITableGroupContent group) {
		if (isClipped) {
			return;
		}
		emitterImplement.endTableGroup(group);
	}

	@Override
	public void start(IReportContent report) {
		emitterImplement.start(report);
	}

	@Override
	public void startAutoText(IAutoTextContent autoText) {
		if (isClipped) {
			return;
		}
		emitterImplement.startAutoText(autoText);
	}

	@Override
	public void startCell(ICellContent cell) {
		if (isClipped) {
			omitCellLayer++;
			return;
		}
		int colCount = cell.getColumn();
		if (colCount >= MAX_COLUMN) {
			omitCellLayer++;
			isClipped = true;
			return;
		}
		emitterImplement.startCell(cell);
	}

	@Override
	public void startContent(IContent content) {
		if (isClipped) {
			return;
		}
		emitterImplement.startContent(content);
	}

	@Override
	public void startData(IDataContent data) {
		if (isClipped) {
			return;
		}
		emitterImplement.startData(data);
	}

	@Override
	public void startForeign(IForeignContent foreign) throws BirtException {
		if (isClipped) {
			return;
		}
		emitterImplement.startForeign(foreign);
	}

	@Override
	public void startGroup(IGroupContent group) {
		if (isClipped) {
			return;
		}
		emitterImplement.startGroup(group);
	}

	@Override
	public void startImage(IImageContent image) {
		if (isClipped) {
			return;
		}
		emitterImplement.startImage(image);
	}

	@Override
	public void startLabel(ILabelContent label) {
		if (isClipped) {
			return;
		}
		emitterImplement.startLabel(label);
	}

	@Override
	public void startList(IListContent list) {
		if (isClipped) {
			return;
		}
		emitterImplement.startList(list);
	}

	@Override
	public void startListBand(IListBandContent listBand) {
		if (isClipped) {
			return;
		}
		emitterImplement.startListBand(listBand);
	}

	@Override
	public void startListGroup(IListGroupContent group) {
		if (isClipped) {
			return;
		}
		emitterImplement.startListGroup(group);
	}

	@Override
	public void startRow(IRowContent row) {
		if (isClipped) {
			return;
		}
		emitterImplement.startRow(row);
	}

	@Override
	public void startTable(ITableContent table) {
		if (isClipped) {
			return;
		}
		int colCount = table.getColumnCount();
		if (colCount > MAX_COLUMN) {
			table = getPartTable(table);
			logger.log(Level.WARNING,
					"There are too many columns in the table , just output the first " + MAX_COLUMN + " columns");
		}
		emitterImplement.startTable(table);
	}

	private ITableContent getPartTable(ITableContent table) {
		List<?> columns = table.getColumns();
		columns = columns.subList(0, MAX_COLUMN);
		ITableContent content = new TableContentWrapper(table, columns);
		return content;
	}

	@Override
	public void startTableBand(ITableBandContent band) {
		if (isClipped) {
			return;
		}
		emitterImplement.startTableBand(band);
	}

	@Override
	public void startTableGroup(ITableGroupContent group) {
		if (isClipped) {
			return;
		}
		emitterImplement.startTableGroup(group);
	}

	@Override
	public void startText(ITextContent text) {
		if (isClipped) {
			return;
		}
		emitterImplement.startText(text);
	}
}
