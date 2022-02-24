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

package org.eclipse.birt.report.engine.executor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Locale;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportRunnable;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;
import org.eclipse.birt.report.engine.emitter.XMLWriter;

import com.ibm.icu.util.ULocale;

import junit.framework.TestCase;

/**
 *
 * abstract class
 *
 */
abstract public class ReportItemExecutorTestAbs extends TestCase {

	/**
	 * Now, we have two kinds of unit tests. For the report items involved in
	 * dynamic data, a design file is provided and is run as if it was a true
	 * reporting. And the whole standard files are provided in the form of the file.
	 * <p>
	 * But as for those report items not involved in the dynamic data, an IR for it
	 * is created directly. Actually we only run a snippet of codes. The standards
	 * are provided in the form of literal string and apparently these strings have
	 * the same XML file header, so here we provide this method to avoid to write
	 * many useless headers.
	 * <p>
	 * While the comparing, the result is converted to upper case and removed all
	 * the spaces including the line break. So here returns the result after
	 * conversion.
	 *
	 * @return the upper-case XML file header without spaces
	 */
	protected String getUpperXMLHeaderNoSpace() {
		return "<?XMLVERSION=\"1.0\"ENCODING=\"UTF-8\"?>";
	}

	protected class DumpEmitter extends ContentEmitterAdapter {

		protected IEmitterServices services;

		protected XMLWriter writer;

		public DumpEmitter(OutputStream out) {
			writer = new XMLWriter();
			writer.open(out);
		}

		@Override
		public void initialize(IEmitterServices services) {

			this.services = services;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.engine.emitter.IReportEmitter#getEmitterServices()
		 */
		public IEmitterServices getEmitterServices() {
			return services;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.report.engine.emitter.IReportEmitter#getOutputFormat()
		 */
		@Override
		public String getOutputFormat() {
			return "TEXT"; // $NON-NLS-1$
		}

		@Override
		public void start(IReportContent report) {
			writer.startWriter();
			writer.openTag("report");
		}

		@Override
		public void end(IReportContent report) {
			writer.closeTag("report");
			writer.endWriter();
			writer.close();
		}

		@Override
		public void startPage(IPageContent pageContent) {
			writer.openTag("page");
			writer.attribute("page-number", pageContent.getPageNumber());
		}

		@Override
		public void endPage(IPageContent pageConent) {
			writer.closeTag("page");
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.engine.emitter.IImageEmitter#startImage(org.eclipse.
		 * birt.report.engine.content.ImageItemContent)
		 */
		@Override
		public void startImage(IImageContent image) {
			writer.openTag("image");
			String source = "unknown";
			switch (image.getImageSource()) {
			case IImageContent.IMAGE_EXPRESSION:
				source = "expression";
				break;
			case IImageContent.IMAGE_FILE:
				source = "file";
				break;
			case IImageContent.IMAGE_NAME:
				source = "name";
				break;
			case IImageContent.IMAGE_URL:
				source = "uri";
				break;
			}
			writer.attribute("source", source);
			writer.closeTag("image");
		}

		@Override
		public void startTable(ITableContent tableObj) {
			writer.openTag("table");
			writer.attribute("caption", tableObj.getCaption());
			for (int i = 0; i < tableObj.getColumnCount(); i++) {
				writer.openTag("column");
				writer.closeTag("column");
			}
		}

		@Override
		public void endTable(ITableContent tableObj) {
			writer.closeTag("Table");
		}

		@Override
		public void startRow(IRowContent rowObj) {
			writer.openTag("row");
		}

		@Override
		public void endRow(IRowContent rowobj) {
			writer.closeTag("row");
		}

		@Override
		public void startCell(ICellContent cellObj) {
			writer.openTag("cell");
			writer.attribute("column", cellObj.getColumn());
			writer.attribute("row-span", cellObj.getRowSpan());
			writer.attribute("col-span", cellObj.getColSpan());
		}

		@Override
		public void endCell(ICellContent cell) {
			writer.closeTag("cell");
		}

		@Override
		public void startForeign(IForeignContent foreign) {
			writer.openTag("foreign");
			writer.attribute("raw-type", foreign.getRawType());
			writer.closeTag("foreign");
		}

		@Override
		public void startText(ITextContent textObj) {
			writer.openTag("text");
			writer.closeTag("text");
		}

		@Override
		public void startLabel(ILabelContent label) {
			writer.openTag("label");
			writer.closeTag("label");
		}

		@Override
		public void startAutoText(IAutoTextContent autoText) {
			writer.openTag("auto-text");
			writer.closeTag("auto-text");
		}

		@Override
		public void startData(IDataContent data) {
			writer.openTag("data");
			writer.closeTag("data");
		}

		@Override
		public void endList(IListContent list) {
			writer.closeTag("list");
		}

		@Override
		public void endListBand(IListBandContent listBand) {
			writer.closeTag("list-band");
		}

		@Override
		public void endTableBand(ITableBandContent band) {
			writer.closeTag("table-band");
		}

		@Override
		public void startList(IListContent list) {
			writer.openTag("list");
		}

		@Override
		public void startListBand(IListBandContent listBand) {
			writer.openTag("list-band");
		}

		@Override
		public void startTableBand(ITableBandContent band) {
			writer.openTag("table-band");
		}

		@Override
		public void endGroup(IGroupContent group) {
			writer.closeTag("group");
		}

		@Override
		public void startGroup(IGroupContent group) {
			writer.openTag("group");
		}
	}

	protected String execute(String reportName, Locale locale) throws Exception {
		ReportEngine engine = new ReportEngine(new EngineConfig());
		InputStream in = this.getClass().getResourceAsStream(reportName);
		assertTrue(in != null);
		ReportRunnable runnable = (ReportRunnable) engine.openReportDesign(in);
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
			}
		}

		ExecutionContext context = new ExecutionContext();
		setEngine(context, engine);

		context.setLocale(ULocale.forLocale(locale));
		context.setRunnable(runnable);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DumpEmitter emitter = new DumpEmitter(out);
		ReportExecutor executor = new ReportExecutor(context);
		context.setExecutor(executor);

		ReportExecutorUtil.execute(executor, emitter);

		return out.toString();
	}

	private void setEngine(ExecutionContext context, ReportEngine engine)
			throws NoSuchFieldException, IllegalAccessException {
		Class<?> engineClass = engine.getClass();
		Field field = engineClass.getDeclaredField("engine");
		field.setAccessible(true);
		IReportEngine reportEngine = (IReportEngine) field.get(engine);

		Class<?> contextClass = context.getClass();
		field = contextClass.getDeclaredField("engine");
		field.setAccessible(true);
		field.set(context, reportEngine);
	}

	protected String execute(String reportName) throws Exception {
		return execute(reportName, Locale.getDefault());
	}

	protected String loadResource(String resourceName) throws Exception {
		InputStream in = this.getClass().getResourceAsStream(resourceName);
		assertTrue(in != null);
		byte[] buffer = new byte[in.available()];
		in.read(buffer);
		return new String(buffer);
	}

	protected void compare(String designFile, String goldenFile) {
		try {
			String result = execute(designFile);
			String golden = loadResource(goldenFile);

			result = result.replaceAll("\\s", "").toUpperCase();
			golden = golden.replaceAll("\\s", "").toUpperCase();

			assertEquals(golden, result);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}

	}
}
