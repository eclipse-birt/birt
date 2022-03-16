/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.presentation;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;

import org.eclipse.birt.core.framework.parser.AbstractParseState;
import org.eclipse.birt.core.framework.parser.XMLParserException;
import org.eclipse.birt.core.framework.parser.XMLParserHandler;
import org.eclipse.birt.core.util.CommonUtil;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XMLContentReader {

	public static IReportContent read(InputStream in) {
		try {
			IReportContent reportContent = ContentFactory.createReportContent(null);
			SAXParser parser = CommonUtil.createSAXParser();
			if (in != null) {
				parser.parse(in, new XMLContentParser(reportContent));
				in.close();
				return reportContent;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	static class XMLContentParser extends XMLParserHandler {

		IReportContent reportContent = null;

		public XMLContentParser(IReportContent content) {
			this.reportContent = content;
		}

		protected String getAttrValue(Attributes attr, String name) {
			String value = attr.getValue(name);
			if (value != null) {
				value = value.trim();
				if ("".equals(value)) {
					return null;
				}
			}
			return value;
		}

		/**
		 * Overrides the super method. This method first parses attributes of the
		 * current state, and then query whether to use a new state or the current one
		 * according to the attributes value.
		 *
		 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
		 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
		 */

		@Override
		public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
				throws SAXException {
			currentElement = qName;
			AbstractParseState newState = topState().startElement(qName);
			newState.parseAttrs(atts);
			AbstractParseState jumpToState = newState.jumpTo();
			if (jumpToState != null) {
				pushState(jumpToState);
				return;
			}

			newState.setElementName(currentElement);
			pushState(newState);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.model.util.XMLParserHandler#createStartState()
		 */

		@Override
		public AbstractParseState createStartState() {
			return new ReportContentState(reportContent);
		}

		@Override
		public void semanticError(XMLParserException ex) {
		}

		/**
		 * Recognizes the top-level tags: Report or Template.
		 */

		class ReportContentState extends InnerParseState {

			IReportContent reportContent;

			ReportContentState(IReportContent reportContent) {
				this.reportContent = reportContent;
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
			 * String)
			 */

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("text")) {
					return new TextContentState();
				}

				if (tagName.equalsIgnoreCase("label")) {
					return new LabelContentState();
				}

				if (tagName.equalsIgnoreCase("data")) {
					return new DataContentState();
				}

				if (tagName.equalsIgnoreCase("image")) {
					return new ImageContentState();
				}

				if (tagName.equalsIgnoreCase("foreign")) {
					return new ForeignContentState();
				}

				if (tagName.equalsIgnoreCase("container")) {
					return new ContainerContentState();
				}

				if (tagName.equalsIgnoreCase("table")) {
					return new TableContentState();
				}

				if (tagName.equalsIgnoreCase("page")) {
					return new PageContentState();
				}

				return super.startElement(tagName);
			}
		}

		class ContentParseState extends InnerParseState {

			IContent content;

			ContentParseState() {
			}

			public void setContent(IContent content) {
				this.content = content;
			}

			public IContent getContent() {
				return this.content;
			}

			@Override
			public void parseAttrs(Attributes attrs) throws XMLParserException {
				String style = attrs.getValue("style");
				if (style != null) {
					IStyle inlineStyle = reportContent.createStyle();
					inlineStyle.setCssText(style);
					content.setInlineStyle(inlineStyle);
				}
			}

			@Override
			public void endElement(AbstractParseState state) {
				ContentParseState contentState = (ContentParseState) state;
				IContent child = contentState.content;
				content.getChildren().add(child);
				child.setParent(content);
			}
		}

		class PageContentState extends ContentParseState {

			PageContentState() {
				setContent(reportContent.createPageContent());
			}

			@Override
			public AbstractParseState startElement(String tagName) {
				IPageContent pageContent = (IPageContent) content;
				if (tagName.equalsIgnoreCase("page-header")) {
					return new PageHeaderState(pageContent);
				}

				if (tagName.equalsIgnoreCase("page-footer")) {
					return new PageFooterState(pageContent);
				}

				if (tagName.equalsIgnoreCase("page-body")) {
					return new PageBodyState(pageContent);
				}

				return super.startElement(tagName);
			}

		}

		class PageHeaderState extends InnerParseState {

			IPageContent pageContent;

			PageHeaderState(IPageContent pageContent) {
				this.pageContent = pageContent;
			}

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("text")) {
					return new TextContentState();
				}

				if (tagName.equalsIgnoreCase("label")) {
					return new LabelContentState();
				}

				if (tagName.equalsIgnoreCase("data")) {
					return new DataContentState();
				}

				if (tagName.equalsIgnoreCase("image")) {
					return new ImageContentState();
				}

				if (tagName.equalsIgnoreCase("foreign")) {
					return new ForeignContentState();
				}

				if (tagName.equalsIgnoreCase("container")) {
					return new ContainerContentState();
				}

				if (tagName.equalsIgnoreCase("table")) {
					return new TableContentState();
				}

				return super.startElement(tagName);
			}

			@Override
			public void endElement(AbstractParseState state) {
				ContentParseState contentState = (ContentParseState) state;
				pageContent.getHeader().add(contentState.content);
			}

		}

		class PageFooterState extends InnerParseState {

			IPageContent pageContent;

			PageFooterState(IPageContent pageContent) {
				this.pageContent = pageContent;
			}

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("text")) {
					return new TextContentState();
				}

				if (tagName.equalsIgnoreCase("label")) {
					return new LabelContentState();
				}

				if (tagName.equalsIgnoreCase("data")) {
					return new DataContentState();
				}

				if (tagName.equalsIgnoreCase("image")) {
					return new ImageContentState();
				}

				if (tagName.equalsIgnoreCase("foreign")) {
					return new ForeignContentState();
				}

				if (tagName.equalsIgnoreCase("container")) {
					return new ContainerContentState();
				}

				if (tagName.equalsIgnoreCase("table")) {
					return new TableContentState();
				}

				return super.startElement(tagName);
			}

			@Override
			public void endElement(AbstractParseState state) {
				ContentParseState contentState = (ContentParseState) state;
				pageContent.getFooter().add(contentState.content);
			}

		}

		class PageBodyState extends InnerParseState {

			IPageContent pageContent;

			PageBodyState(IPageContent pageContent) {
				this.pageContent = pageContent;
			}

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("text")) {
					return new TextContentState();
				}

				if (tagName.equalsIgnoreCase("label")) {
					return new LabelContentState();
				}

				if (tagName.equalsIgnoreCase("data")) {
					return new DataContentState();
				}

				if (tagName.equalsIgnoreCase("image")) {
					return new ImageContentState();
				}

				if (tagName.equalsIgnoreCase("foreign")) {
					return new ForeignContentState();
				}

				if (tagName.equalsIgnoreCase("container")) {
					return new ContainerContentState();
				}

				if (tagName.equalsIgnoreCase("table")) {
					return new TableContentState();
				}

				return super.startElement(tagName);
			}

			@Override
			public void endElement(AbstractParseState state) {
				ContentParseState contentState = (ContentParseState) state;
				pageContent.getChildren().add(contentState.content);
			}

		}

		class TextContentState extends ContentParseState {

			TextContentState() {
				setContent(reportContent.createTextContent());
			}
		}

		class LabelContentState extends ContentParseState {

			LabelContentState() {
				setContent(reportContent.createLabelContent());
			}
		}

		class DataContentState extends ContentParseState {

			DataContentState() {
				setContent(reportContent.createDataContent());

			}
		}

		class ImageContentState extends ContentParseState {

			ImageContentState() {
				setContent(reportContent.createImageContent());
			}
		}

		class ForeignContentState extends ContentParseState {

			ForeignContentState() {
				setContent(reportContent.createForeignContent());

			}
		}

		class ContainerContentState extends ContentParseState {

			ContainerContentState() {
				setContent(reportContent.createContainerContent());
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
			 * String)
			 */

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("text")) {
					return new TextContentState();
				}

				if (tagName.equalsIgnoreCase("label")) {
					return new LabelContentState();
				}

				if (tagName.equalsIgnoreCase("data")) {
					return new DataContentState();
				}

				if (tagName.equalsIgnoreCase("image")) {
					return new ImageContentState();
				}

				if (tagName.equalsIgnoreCase("foreign")) {
					return new ForeignContentState();
				}

				if (tagName.equalsIgnoreCase("container")) {
					return new ContainerContentState();
				}

				if (tagName.equalsIgnoreCase("table")) {
					return new TableContentState();
				}
				return super.startElement(tagName);
			}
		}

		class TableContentState extends ContentParseState {

			TableContentState() {
				setContent(reportContent.createTableContent());
			}

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("table-header")) {
					return new TableBandContentState(ITableBandContent.BAND_HEADER);
				}
				if (tagName.equalsIgnoreCase("table-body")) {
					return new TableBandContentState(ITableBandContent.BAND_DETAIL);
				}
				if (tagName.equalsIgnoreCase("table-footer")) {
					return new TableBandContentState(ITableBandContent.BAND_FOOTER);
				}
				return super.startElement(tagName);
			}

			@Override
			public void parseAttrs(Attributes attrs) throws XMLParserException {
				ITableContent tableContent = (ITableContent) content;
				int columnCount = getIntAttribute(attrs, "column", 1);
				for (int i = 0; i < columnCount; i++) {
					tableContent.addColumn(new Column(reportContent));
				}

				// boolean headerRepeat = getBooleanAttribute("");
				// tableContent.setHeaderRepeat();

				super.parseAttrs(attrs);
			}
		}

		class TableBandContentState extends ContentParseState {

			ITableBandContent bandContent;

			TableBandContentState(int bandType) {
				bandContent = reportContent.createTableBandContent();
				bandContent.setBandType(bandType);
				setContent(bandContent);
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
			 * String)
			 */

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("table-row")) {
					return new TableRowContentState();
				}
				return super.startElement(tagName);
			}

		}

		class TableRowContentState extends ContentParseState {

			TableRowContentState() {
				setContent(reportContent.createRowContent());
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
			 * String)
			 */

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("cell")) {
					return new TableCellContentState();
				}
				return super.startElement(tagName);
			}

			@Override
			public void parseAttrs(Attributes attrs) throws XMLParserException {
				IRowContent rowContent = (IRowContent) content;
				rowContent.setRowID(getIntAttribute(attrs, "row", -1));
				super.parseAttrs(attrs);
			}
		}

		class TableCellContentState extends ContentParseState {

			TableCellContentState() {
				setContent(reportContent.createCellContent());
			} /*
				 * (non-Javadoc)
				 *
				 * @see
				 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.
				 * String)
				 */

			@Override
			public AbstractParseState startElement(String tagName) {
				if (tagName.equalsIgnoreCase("text")) {
					return new TextContentState();
				}

				if (tagName.equalsIgnoreCase("label")) {
					return new LabelContentState();
				}

				if (tagName.equalsIgnoreCase("data")) {
					return new DataContentState();
				}

				if (tagName.equalsIgnoreCase("image")) {
					return new ImageContentState();
				}

				if (tagName.equalsIgnoreCase("foreign")) {
					return new ForeignContentState();
				}

				if (tagName.equalsIgnoreCase("container")) {
					return new ContainerContentState();
				}

				if (tagName.equalsIgnoreCase("table")) {
					return new TableContentState();
				}
				return super.startElement(tagName);
			}

			@Override
			public void parseAttrs(Attributes attrs) throws XMLParserException {
				ICellContent cellContent = (ICellContent) content;

				cellContent.setColSpan(getIntAttribute(attrs, "col-span", 1));
				cellContent.setRowSpan(getIntAttribute(attrs, "row-span", 1));
				cellContent.setColumn(getIntAttribute(attrs, "cell", -1));
				super.parseAttrs(attrs);
			}
		}

		protected int getIntAttribute(Attributes attrs, String name, int defaultValue) {
			String value = attrs.getValue(name);
			if (value == null) {
				try {
					int intValue = Integer.parseInt(value);
					return intValue;
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
			return defaultValue;
		}

		protected boolean getBooleanAttribute(Attributes attrs, String name) {
			String value = attrs.getValue(name);
			return Boolean.parseBoolean(value);
		}
	}
}
