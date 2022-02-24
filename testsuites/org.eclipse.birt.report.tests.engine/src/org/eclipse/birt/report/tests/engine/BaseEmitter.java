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

package org.eclipse.birt.report.tests.engine;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
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
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;

public abstract class BaseEmitter extends EngineCase implements IContentEmitter {

	private String inPath = this.genInputFolder() + "/";
	private String outPath = this.genOutputFolder() + "/";

	protected final static String EMITTER_HTML = "html";
	protected final static String EMITTER_PDF = "emitter_pdf";

	protected abstract String getReportName();

	/**
	 * @param format     render format
	 * @param pagination For html output only, decide whether generate report with
	 *                   page break or not.
	 * @throws EngineException
	 */
	protected ArrayList runandrender_emitter(String format, boolean pagination) throws EngineException {
		IReportRunnable reportRunnable = engine.openReportDesign(inPath + getReportName());
		IRunAndRenderTask task = engine.createRunAndRenderTask(reportRunnable);
		RenderOption options = new HTMLRenderOption();
		options.setOutputFormat(format);
		if (format.equals(EMITTER_HTML)) {
			((HTMLRenderOption) options).setHtmlPagination(pagination);
		}
		HashMap appContext = new HashMap();
		appContext.put("emitter_class", this);
		task.setAppContext(appContext);
		task.setRenderOption(options);
		task.run();
		ArrayList errors = (ArrayList) task.getErrors();
		task.close();
		return errors;
	}

	protected ArrayList runandthenrender_emitter(String format) throws EngineException {
		ArrayList errors = new ArrayList();
		this.run(getReportName() + ".rptdesign", getReportName() + ".rptdocument");
		IReportDocument document = engine.openReportDocument(outPath + getReportName() + ".rptdocument");
		IRenderTask task = engine.createRenderTask(document);
		RenderOption options = new HTMLRenderOption();
		options.setOutputFormat(format);
		HashMap appContext = new HashMap();
		appContext.put("emitter_class", this);
		task.setAppContext(appContext);
		task.setRenderOption(options);
		task.render();
		errors = (ArrayList) task.getErrors();
		task.close();
		return errors;

	}

	@Override
	public void end(IReportContent report) {
	}

	@Override
	public void endCell(ICellContent cell) {
	}

	@Override
	public void endContainer(IContainerContent container) {
	}

	@Override
	public void endContent(IContent content) {
	}

	@Override
	public void endGroup(IGroupContent group) {
	}

	@Override
	public void endList(IListContent list) {
	}

	@Override
	public void endListBand(IListBandContent listBand) {
	}

	@Override
	public void endListGroup(IListGroupContent group) {
	}

	@Override
	public void endPage(IPageContent page) {
	}

	@Override
	public void endRow(IRowContent row) {
	}

	@Override
	public void endTable(ITableContent table) {
	}

	@Override
	public void endTableBand(ITableBandContent band) {
	}

	@Override
	public void endTableGroup(ITableGroupContent group) {
	}

	@Override
	public String getOutputFormat() {
		return null;
	}

	@Override
	public void initialize(IEmitterServices service) {
	}

	@Override
	public void start(IReportContent report) {
	}

	@Override
	public void startAutoText(IAutoTextContent autoText) {
	}

	@Override
	public void startCell(ICellContent cell) {
	}

	@Override
	public void startContainer(IContainerContent container) {
	}

	@Override
	public void startContent(IContent content) {
	}

	@Override
	public void startData(IDataContent data) {
	}

	@Override
	public void startForeign(IForeignContent foreign) {
	}

	@Override
	public void startGroup(IGroupContent group) {
	}

	@Override
	public void startImage(IImageContent image) {
	}

	@Override
	public void startLabel(ILabelContent label) {
	}

	@Override
	public void startList(IListContent list) {
	}

	@Override
	public void startListBand(IListBandContent listBand) {
	}

	@Override
	public void startListGroup(IListGroupContent group) {
	}

	@Override
	public void startPage(IPageContent page) {
	}

	@Override
	public void startRow(IRowContent row) {
	}

	@Override
	public void startTable(ITableContent table) {
	}

	@Override
	public void startTableBand(ITableBandContent band) {
	}

	@Override
	public void startTableGroup(ITableGroupContent group) {
	}

	@Override
	public void startText(ITextContent text) {
	}

	// protected String genOutputFile( String output )
	// {
	// String outputFile = this.genOutputFile( output );
	// return outputFile;
	// }

	// protected String getFullQualifiedClassName( )
	// {
	// String className = this.getClass( ).getName( );
	// int lastDotIndex = className.lastIndexOf( "." ); //$NON-NLS-1$
	// className = className.substring( 0, lastDotIndex );
	//
	// return className;
	// }

}
