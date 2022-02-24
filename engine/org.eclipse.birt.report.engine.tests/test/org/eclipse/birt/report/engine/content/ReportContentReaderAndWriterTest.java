/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.content;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.util.IOUtil;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.CellContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.content.impl.ForeignContent;
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.RowContent;
import org.eclipse.birt.report.engine.content.impl.TableBandContent;
import org.eclipse.birt.report.engine.content.impl.TableContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.ir.DimensionType;

/**
 * 
 */

public class ReportContentReaderAndWriterTest extends EngineCase {

	protected final int VERSION_1 = 1;

	protected ReportContent reportContent = new ReportContent();

	protected CellContent createCellContent() {
		CellContent content = (CellContent) reportContent.createCellContent();
		content.setName("cellTest");
		content.setWidth(new DimensionType(100, "100"));
		content.setHeight(new DimensionType("100"));
		ActionContent hyperlink = new ActionContent();
		hyperlink.setHyperlink("link", "target");
		Map attr = new HashMap();
		attr.put("test", "1");
		hyperlink.setDrillThrough("bookmartTest", true, "reportTest", attr, attr, "target", "html", null);
		content.setHyperlinkAction(hyperlink);
		/*
		 * StyleDeclaration inlineStyle = new StyleDeclaration();
		 * inlineStyle.setCssText( "inlineStyleTest" ); content.setInlineStyle(
		 * inlineStyle );
		 */
		content.setRowSpan(10);
		content.setColSpan(11);
		content.setColumn(12);
		return content;
	}

	protected ContainerContent createContainerContent() {
		ContainerContent content = (ContainerContent) reportContent.createContainerContent();
		content.setName("ContainerContent");
		content.setHeight(new DimensionType(4, "400"));
		ActionContent hyperlink = new ActionContent();
		hyperlink.setHyperlink("link", "target");
		Map attr = new HashMap();
		attr.put("test", "4");
		hyperlink.setDrillThrough("bookmartTest", true, "reportTest", attr, attr, "target", "html", null);
		content.setHyperlinkAction(hyperlink);
		return content;
	}

	protected DataContent createDataContent() {
		DataContent content = (DataContent) reportContent.createDataContent();
		content.setValue(null);
		content.setLabelText("setLabelText");
		content.setLabelKey("setLabelKey");
		content.getHelpKey();
		return content;
	}

	protected ForeignContent createForeignContent() {
		ForeignContent content = (ForeignContent) reportContent.createForeignContent();
		content.setRawType("rawType");
		content.setRawValue(null);
		return content;
	}

	protected ImageContent createImageContent() {
		ImageContent content = (ImageContent) reportContent.createImageContent();
		content.setAltText("altText");
		content.setAltTextKey("altTextKey");
		content.setImageSource(1);
		return content;
	}

	protected LabelContent createLabelContent() {
		LabelContent content = (LabelContent) reportContent.createLabelContent();
		content.setLabelText("labelText");
		content.setLabelKey("labelTextKey");
		return content;
	}

	protected PageContent createPageContent() {
		PageContent content = (PageContent) reportContent.createPageContent();
		content.setPageType("pageType");
		content.setPageHeight(new DimensionType(3, "pageHeight"));
		content.setPageNumber(100);
		return content;
	}

	protected RowContent createRowContent() {
		RowContent content = (RowContent) reportContent.createRowContent();
		content.setRowID(100);
		return content;
	}

	protected TableBandContent createTableBandContent() {
		TableBandContent content = (TableBandContent) reportContent.createTableBandContent();
		content.setBandType(100);
		return content;
	}

	protected TableContent createTableContent() {
		TableContent content = (TableContent) reportContent.createTableContent();
		content.setCaption("captionTest");
		content.setCaptionKey("captionKeyTest");
		content.setHeaderRepeat(true);
		Column column1 = new Column(reportContent);
		column1.setWidth(new DimensionType(1, "100"));
		content.addColumn(column1);
		Column column2 = new Column(reportContent);
		column2.setWidth(new DimensionType(2, "200"));
		content.addColumn(column2);
		return content;
	}

	protected TextContent createTextContent() {
		TextContent content = (TextContent) reportContent.createTextContent();
		content.setName("TextTest");
		content.setHeight(new DimensionType(10, "100"));
		ActionContent hyperlink = new ActionContent();
		hyperlink.setHyperlink("link", "target");
		Map attr = new HashMap();
		attr.put("test", "1");
		hyperlink.setDrillThrough("bookmartTest", true, "reportTest", attr, attr, "target", "html", null);
		content.setHyperlinkAction(hyperlink);
		return content;
	}

	protected void checkActionContent(ActionContent in, ActionContent out) {
		assertEquals(" Bookmark ", in.getBookmark(), out.getBookmark());
		assertEquals(" reportName ", in.getReportName(), out.getReportName());
		assertEquals(" getHyperlink ", in.getHyperlink(), out.getHyperlink());
		assertEquals(" target ", in.getTargetWindow(), out.getTargetWindow());
		assertEquals(" ParameterBindings ", in.getParameterBindings().get("test").toString(),
				out.getParameterBindings().get("test").toString());
	}

	protected void checkDimensionType(DimensionType in, DimensionType out) {
		assertEquals(" type ", in.getValueType(), out.getValueType());
		if (in.getValueType() == DimensionType.TYPE_DIMENSION) {
			assertEquals(" unitType ", in.getUnits(), out.getUnits());
			assertEquals(" measure ", in.getMeasure(), out.getMeasure(), 0);
		} else {
			assertEquals(" choice ", in.getChoice(), out.getChoice());
		}
	}

	protected void checkCellContent(CellContent in, CellContent out) {
		assertEquals(" Name ", in.getName(), out.getName());
		checkDimensionType(in.getHeight(), out.getHeight());
		checkDimensionType(in.getWidth(), out.getWidth());
		// assertEquals( " InlineStyle ",
		// in.getInlineStyle().getCssText(), out.getInlineStyle().getCssText()
		// );
		assertEquals(" RowSpan ", in.getRowSpan(), out.getRowSpan());
		assertEquals(" ColSpan ", in.getColSpan(), out.getColSpan());
		assertEquals(" Column ", in.getColumn(), out.getColumn());
		checkActionContent((ActionContent) in.getHyperlinkAction(), (ActionContent) out.getHyperlinkAction());
	}

	protected void checkContainerContent(ContainerContent in, ContainerContent out) {
		assertEquals(" Name ", in.getName(), out.getName());
		checkDimensionType(in.getHeight(), out.getHeight());
		checkActionContent((ActionContent) in.getHyperlinkAction(), (ActionContent) out.getHyperlinkAction());
	}

	protected void checkDataContent(DataContent in, DataContent out) {
		assertEquals(" getLabelText ", in.getLabelText(), out.getLabelText());
		assertEquals(" getLabelKey ", in.getLabelKey(), out.getLabelKey());
		assertEquals(" getHelpText ", in.getHelpText(), out.getHelpText());
	}

	protected void checkForeignContent(ForeignContent in, ForeignContent out) {
		assertEquals(" RawType ", in.getRawType(), out.getRawType());
	}

	protected void checkImageContent(ImageContent in, ImageContent out) {
		assertEquals(" AltText ", in.getAltText(), out.getAltText());
		assertEquals(" AltTextKey ", in.getAltTextKey(), out.getAltTextKey());
		assertEquals(" SourceType ", in.getImageSource(), out.getImageSource());
	}

	protected void checkLabelContent(LabelContent in, LabelContent out) {
		assertEquals(" LabelText ", in.getLabelText(), out.getLabelText());
		assertEquals(" LabelTextKey ", in.getLabelKey(), out.getLabelKey());
	}

	protected void checkPageContent(PageContent in, PageContent out) {
		assertEquals(" PageType ", in.getPageType(), out.getPageType());
		checkDimensionType(in.getPageHeight(), out.getPageHeight());
		assertEquals(" PageNumber ", in.getPageNumber(), out.getPageNumber());
	}

	protected void checkRowContent(RowContent in, RowContent out) {
		assertEquals(" RowID ", in.getRowID(), out.getRowID());
	}

	protected void checkTableBandContent(TableBandContent in, TableBandContent out) {
		assertEquals(" Type ", in.getBandType(), out.getBandType());
	}

	protected void checkTableContent(TableContent in, TableContent out) {
		assertEquals(" Caption ", in.getCaption(), out.getCaption());
		assertEquals(" CaptionKey ", in.getCaptionKey(), out.getCaptionKey());
		if (in.getColumnCount() == out.getColumnCount()) {
			Column inColumn, outColumn;
			for (int i = 0; i < in.getColumnCount(); i++) {
				inColumn = (Column) in.getColumn(i);
				outColumn = (Column) out.getColumn(i);
				checkDimensionType(inColumn.getWidth(), outColumn.getWidth());
			}
		} else {
			fail();
		}
	}

	protected void checkTextContent(TextContent in, TextContent out) {
		assertEquals(" Name ", in.getName(), out.getName());
		checkDimensionType(in.getHeight(), out.getHeight());
		checkActionContent((ActionContent) in.getHyperlinkAction(), (ActionContent) out.getHyperlinkAction());
	}

	public void testReadWriter() {
		try {
			byte[] buffer;
			IContent src, tgt;

			src = createCellContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkCellContent((CellContent) tgt, (CellContent) src);

			src = createContainerContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkContainerContent((ContainerContent) tgt, (ContainerContent) src);

			src = createDataContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkDataContent((DataContent) tgt, (DataContent) src);

			src = createForeignContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkForeignContent((ForeignContent) tgt, (ForeignContent) src);

			src = createImageContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkImageContent((ImageContent) tgt, (ImageContent) src);

			src = createLabelContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkLabelContent((LabelContent) tgt, (LabelContent) src);

			src = createPageContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkPageContent((PageContent) tgt, (PageContent) src);

			src = createRowContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkRowContent((RowContent) tgt, (RowContent) src);

			src = createTableBandContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkTableBandContent((TableBandContent) tgt, (TableBandContent) src);

			src = createTableContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkTableContent((TableContent) tgt, (TableContent) src);

			src = createTextContent();
			buffer = doWrite(src);
			tgt = (IContent) doRead(buffer);
			checkTextContent((TextContent) tgt, (TextContent) src);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	protected byte[] doWrite(IContent content) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataOutputStream oo = new DataOutputStream(out);
		IOUtil.writeInt(oo, content.getContentType());
		content.writeContent(oo);
		oo.flush();
		return out.toByteArray();
	}

	protected Object doRead(byte[] buffer) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(buffer);
		DataInputStream oi = new DataInputStream(in);
		Object object = new Object();
		int contentType = IOUtil.readInt(oi);
		int version = VERSION_1;
		switch (contentType) {
		case IContent.CELL_CONTENT:
			CellContent cellContent = (CellContent) reportContent.createCellContent();
			cellContent.setVersion(VERSION_1);
			cellContent.readContent(oi, null);
			object = cellContent;
			break;
		case IContent.CONTAINER_CONTENT:
			ContainerContent containerContent = (ContainerContent) reportContent.createContainerContent();
			containerContent.setVersion(version);
			containerContent.readContent(oi, null);
			object = containerContent;
			break;
		case IContent.DATA_CONTENT:
			DataContent dataContent = (DataContent) reportContent.createDataContent();
			dataContent.setVersion(version);
			dataContent.readContent(oi, null);
			object = dataContent;
			break;
		case IContent.FOREIGN_CONTENT:
			ForeignContent foreignContent = (ForeignContent) reportContent.createForeignContent();
			foreignContent.setVersion(version);
			foreignContent.readContent(oi, null);
			object = foreignContent;
			break;
		case IContent.IMAGE_CONTENT:
			ImageContent imageContent = (ImageContent) reportContent.createImageContent();
			imageContent.setVersion(version);
			imageContent.readContent(oi, null);
			object = imageContent;
			break;
		case IContent.LABEL_CONTENT:
			LabelContent labelContent = (LabelContent) reportContent.createLabelContent();
			labelContent.setVersion(version);
			labelContent.readContent(oi, null);
			object = labelContent;
			break;
		case IContent.PAGE_CONTENT:
			PageContent pageContent = (PageContent) reportContent.createPageContent();
			pageContent.setVersion(version);
			pageContent.readContent(oi, null);
			object = pageContent;
			break;
		case IContent.ROW_CONTENT:
			RowContent rowContent = (RowContent) reportContent.createRowContent();
			rowContent.setVersion(version);
			rowContent.readContent(oi, null);
			object = rowContent;
			break;
		case IContent.TABLE_BAND_CONTENT:
			TableBandContent tableBandContent = (TableBandContent) reportContent.createTableBandContent();
			tableBandContent.setVersion(version);
			tableBandContent.readContent(oi, null);
			object = tableBandContent;
			break;
		case IContent.TABLE_CONTENT:
			TableContent tableContent = (TableContent) reportContent.createTableContent();
			tableContent.setVersion(version);
			tableContent.readContent(oi, null);
			object = tableContent;
			break;
		case IContent.TEXT_CONTENT:
			TextContent textContent = (TextContent) reportContent.createTextContent();
			textContent.setVersion(version);
			textContent.readContent(oi, null);
			object = textContent;
			break;
		default:
			throw new ClassNotFoundException("No class type: " + contentType);
		}
		return object;
	}
}
