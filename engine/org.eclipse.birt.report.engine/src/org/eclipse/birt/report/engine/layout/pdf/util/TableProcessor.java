/*******************************************************************************
 * Copyright (c)2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.executor.buffermgr.Cell;
import org.eclipse.birt.report.engine.executor.buffermgr.Row;
import org.eclipse.birt.report.engine.executor.buffermgr.Table;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TableProcessor implements HTMLConstants {

	private static final String ATTRIBUTE_COLSPAN = "colspan";

	private static final String ATTRIBUTE_ROWSPAN = "rowspan";

	// FIXME code review: extract two method so that the logic will be more
	// clear.
	public static void processTable(Element ele, Map<Element, StyleProperties> cssStyles, IContent content,
			ActionContent action) {
		// FIXME code review: this block is used to parse table content. extract
		// a method parseTable().
		TableState tableState = new TableState(ele, cssStyles, content, action);
		tableState.processNodes();

		// FIXME code review: this block is used to layout the table. extract to
		// method layoutTable();
		Table table = new Table(tableState.getRowCount(), tableState.getColumnCount());
		TableContent tableContent = (TableContent) tableState.getContent();
		Iterator rows = tableContent.getChildren().iterator();
		while (rows.hasNext()) {
			RowContent row = (RowContent) rows.next();
			table.createRow(row);
			Iterator cells = row.getChildren().iterator();
			while (cells.hasNext()) {
				CellContent cell = (CellContent) cells.next();
				int rowSpan = cell.getRowSpan();
				int colSpan = cell.getColSpan();
				// Notice that the cell id is -1, that means the cell id will be
				// dynamic adjusted by <code>Table</code>.
				table.createCell(-1, rowSpan, colSpan, new InternalCellContent(cell));
			}
		}
		normalize(table, tableContent, tableState);
	}

	protected static void normalize(Table table, TableContent tableContent, TableState tableState) {
		ReportContent report = (ReportContent) tableContent.getReportContent();
		for (int i = 0; i < table.getRowCount(); i++) {
			Row row = table.getRow(i);
			RowContent rowContent = (RowContent) row.getContent();

			// the default page break inside for html table row is "auto"
			IStyle rowStyle = rowContent.getStyle();
			if (rowStyle != null) {
				if (rowStyle.getPageBreakInside() == null) {
					rowStyle.setProperty(IStyle.STYLE_PAGE_BREAK_INSIDE, IStyle.AUTO_VALUE);
				}
			}

			Collection children = rowContent.getChildren();
			children.clear();
			for (int j = 0; j < table.getColCount(); j++) {
				Cell cell = row.getCell(j);
				CellContent cellContent = null;
				int status = cell.getStatus();
				if (status == Cell.CELL_EMPTY) {
					cellContent = (CellContent) report.createCellContent();
					cellContent.setRowSpan(1);
					cellContent.setColSpan(1);
					cellContent.setColumn(j);
					children.add(cellContent);
					cellContent.setParent(rowContent);
				} else if (status == Cell.CELL_USED) {
					cellContent = ((InternalCellContent) cell.getContent()).cell;
					cellContent.setColSpan(cell.getColSpan());
					cellContent.setRowSpan(cell.getRowSpan());
					cellContent.setColumn(j);
					children.add(cellContent);
					cellContent.setParent(rowContent);
				}
			}
		}
	}

	private static class State {

		protected Element element;
		protected Map<Element, StyleProperties> cssStyles;
		protected IContent content;
		protected ActionContent action;

		public State(Element element, Map<Element, StyleProperties> cssStyles, ActionContent action) {
			this.element = element;
			this.cssStyles = cssStyles;
			this.action = action;
		}

		protected void setParent(IContent parent) {
			parent.getChildren().add(content);
			content.setParent(parent);
		}

		public IContent getContent() {
			return content;
		}

	}

	public static class TableState extends State {

		private int columnCount;
		private int rowCount;
		private TableContent table;

		public TableState(Element element, Map<Element, StyleProperties> cssStyles, IContent parent,
				ActionContent action) {
			super(element, cssStyles, action);
			content = (TableContent) parent.getReportContent().createTableContent();
			table = (TableContent) content;
			setParent(parent);
			content.setWidth(PropertyUtil.getDimensionAttribute(element, PROPERTY_WIDTH));
			HTML2Content.handleStyle(element, cssStyles, content);
			processCellStyle(element, cssStyles);
		}

		protected void processRow(Element element, Map<Element, StyleProperties> cssStyles, String border,
				String padding) {
			for (Node n = element.getFirstChild(); n != null; n = n.getNextSibling()) {
				Element c = (Element) n;
				if (TAG_TD.equals(c.getTagName()) || TAG_TH.equals(c.getTagName())) {
					StyleProperties sp = cssStyles.get(c);
					if (sp == null) {
						sp = new StyleProperties(new StyleDeclaration(content.getCSSEngine()));
						cssStyles.put(c, sp);
					}
					if (border != null && border.length() > 0) {
						PropertiesProcessor.process(PROPERTY_BORDER, border, sp);
					}
					if (padding != null && padding.length() > 0) {
						PropertiesProcessor.process(PROPERTY_CELLPADDING, padding, sp);
					}
				}
			}
		}

		private void processCellStyle(Element element, Map<Element, StyleProperties> cssStyles) {
			String border = element.getAttribute(PROPERTY_BORDER);
			String padding = element.getAttribute(PROPERTY_CELLPADDING);
			boolean hasBorder = border != null && border.length() > 0;
			boolean hasPadding = padding != null && padding.length() > 0;
			if (hasBorder || hasPadding) {
				for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
					Element r = (Element) node;
					if (TAG_TR.equals(r.getTagName())) {
						processRow(r, cssStyles, border, padding);
					} else if ("tbody".equals(r.getTagName()) || "thead".equals(r.getTagName())
							|| "tfoot".equals(r.getTagName())) {
						for (Node n = r.getFirstChild(); n != null; n = n.getNextSibling()) {
							Element c = (Element) n;
							if (TAG_TR.equals(c.getTagName())) {
								processRow(c, cssStyles, border, padding);
							}
						}
					}
				}
			}
		}

		public void processNodes() {
			Element ele = element;
			processNodes(ele);
		}

		void handleColumnStyle(Element ele, Map<Element, StyleProperties> cssStyles, IColumn column) {
			StyleProperties sp = cssStyles.get(ele);
			if (sp == null) {
				sp = new StyleProperties(new StyleDeclaration(content.getCSSEngine()));
				cssStyles.put(ele, sp);
			}
			String tagName = ele.getTagName();
			Tag2Style tag2Style = Tag2Style.getStyleProcess(tagName);
			if (tag2Style != null) {
				tag2Style.process(ele, sp);
			}
			column.setInlineStyle(sp.getStyle());
			Object w = sp.getProperty(StyleProperties.WIDTH);
			if (w != null && w instanceof DimensionType) {
				column.setWidth((DimensionType) w);
			}
		}

		private void addColumn(int count) {
			for (int i = 0; i < count; i++) {
				table.addColumn(new Column(table.getReportContent()));
			}
		}

		private Hashtable<Integer, Integer> records;
		private int index;
		{
			records = new Hashtable<Integer, Integer>();
			index = 0;
		}

		private void processNodes(Element ele) {
			for (Node node = ele.getFirstChild(); node != null; node = node.getNextSibling()) {
				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				Element element = (Element) node;
				String tagName = element.getTagName();
				if ("tr".equals(tagName)) {
					RowState rowState = new RowState(element, cssStyles, content, action, records, index);
					rowState.processNodes();
					columnCount = Math.max(columnCount, rowState.getColumnCount());
					if (columnCount > table.getColumnCount()) {
						addColumn(columnCount - table.getColumnCount());
					}
					++rowCount;
					++index;
				} else if ("col".equals(tagName)) {
					Column column = new Column(content.getReportContent());
					DimensionType cw = PropertyUtil.getDimensionAttribute(element, "width");
					if (cw != null) {
						column.setWidth(cw);
					}
					((TableContent) content).addColumn(column);
					handleColumnStyle(element, cssStyles, column);
				} else if ("tbody".equals(tagName) || "thead".equals(tagName) || "tfoot".equals(tagName)) {
					processNodes(element);
				}
			}
		}

		public int getColumnCount() {
			return columnCount;
		}

		public int getRowCount() {
			return rowCount;
		}

	}

	private static class RowState extends State {

		private Hashtable<Integer, Integer> records;
		private int columnCount;
		private int index;

		public RowState(Element element, Map<Element, StyleProperties> cssStyles, IContent parent, ActionContent action,
				Hashtable<Integer, Integer> records, int index) {
			super(element, cssStyles, action);
			content = (RowContent) parent.getReportContent().createRowContent();
			setParent(parent);
			content.setHeight(PropertyUtil.getDimensionAttribute(element, "height"));
			HTML2Content.handleStyle(element, cssStyles, content);
			this.records = records;
			this.index = index;
		}

		public void processNodes() {
			if (records.containsKey(index))
				columnCount += records.get(index);
			for (Node node = element.getFirstChild(); node != null; node = node.getNextSibling()) {
				int current = index;
				assert (node.getNodeType() == Node.ELEMENT_NODE);
				Element element = (Element) node;
				String tagName = element.getTagName();
				assert ("td".equals(tagName));
				CellState cellState = new CellState(element, cssStyles, content, action);
				cellState.processNodes();
				int rowSpan = cellState.getRowSpan();
				int colSpan = cellState.getColSpan();
				if (rowSpan > 1) {
					for (int i = 1; i < rowSpan; i++) {
						int offset = current + i;// offset means the row's index in table.
						if (records.containsKey(offset)) {
							records.put(offset, records.get(offset) + colSpan);
						} else {
							records.put(offset, colSpan);
						}
					}
				}
				columnCount += cellState.getColSpan();
			}
		}

		public int getColumnCount() {
			return columnCount;
		}
	}

	private static class CellState extends State {

		private CellContent cell;

		public CellState(Element element, Map<Element, StyleProperties> cssStyles, IContent parent,
				ActionContent action) {
			super(element, cssStyles, action);
			cell = (CellContent) parent.getReportContent().createCellContent();
			content = cell;
			setParent(parent);
			HTML2Content.handleStyle(element, cssStyles, content);
			cell.setRowSpan(PropertyUtil.getIntAttribute(element, ATTRIBUTE_ROWSPAN));
			cell.setColSpan(PropertyUtil.getIntAttribute(element, ATTRIBUTE_COLSPAN));
		}

		public void processNodes() {
			HTML2Content.processNodes(element, cssStyles, content, action, 0);
		}

		public int getColSpan() {
			return cell.getColSpan();
		}

		public int getRowSpan() {
			return cell.getRowSpan();
		}
	}

	private static class InternalCellContent implements Cell.Content {

		CellContent cell;

		InternalCellContent(CellContent cell) {
			this.cell = cell;
		}

		public boolean isEmpty() {
			return cell != null;
		}

		public void reset() {
		}
	}
}
