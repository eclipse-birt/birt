/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.internal.document.v4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.internal.document.v3.CachedReportContentReaderV3;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.presentation.IPageHint;

/**
 * execute the master page.
 * 
 * the master page's children include page header and footer.
 */
public class MasterPageExecutor extends ContainerExecutor {

	private static final int HEADER_BAND = 0;
	private static final int BODY_BAND = 1;
	private static final int FOOTER_BAND = 2;

	private long pageNumber;
	private long pageOffset;
	private SimpleMasterPageDesign masterPage;

	private int nextBand;
	IBaseResultSet[] rs;

	protected MasterPageExecutor(ExecutorManager manager, long pageNumber, MasterPageDesign masterPage) {
		super(manager, -1);
		this.reader = manager.getPageReader();
		this.pageNumber = pageNumber;
		this.pageOffset = -1;
		this.nextBand = 0;
		this.masterPage = (SimpleMasterPageDesign) masterPage;
	}

	public void close() {
		context.setExecutingMasterPage(false);
		context.setResultSets(rs);
		pageNumber = 0;
		if (pageOffset != -1) {
			manager.getPageReader().unloadContent(pageOffset);
		}
		nextBand = 0;
		super.close();
	}

	public IContent execute() {
		if (executed) {
			return content;
		}
		context.setExecutingMasterPage(true);
		rs = context.getResultSets();
		context.setPageNumber(pageNumber);
		executed = true;
		try {
			long pageNo = pageNumber;
			PageHintReader hintReader = manager.getPageHintReader();
			long totalPage = hintReader.getTotalPage();
			if (pageNumber > totalPage) {
				pageNo = totalPage;
			}

			IPageHint pageHint = hintReader.getPageHint(pageNo);
			Collection<PageVariable> vars = pageHint.getPageVariables();
			if (vars != null) {
				context.addPageVariables(vars);
			}

			pageOffset = hintReader.getPageOffset(pageNo, masterPage.getName());

			CachedReportContentReaderV3 pageReader = manager.getPageReader();
			content = pageReader.loadContent(pageOffset);
			InstanceID iid = content.getInstanceID();
			long id = iid.getComponentID();
			masterPage = (SimpleMasterPageDesign) context.getReport().getReportItemByID(id);
			content.setGenerateBy(masterPage);

			IPageContent pageContent = (IPageContent) content;
			pageContent.setPageNumber(pageNumber);

			return content;
		} catch (IOException ex) {
			context.addException(this.getDesign(), new EngineException(ex.getLocalizedMessage(), ex));
		}
		return null;
	}

	protected ReportItemExecutor doCreateExecutor(long offset) throws Exception {
		if (nextBand >= HEADER_BAND && nextBand <= FOOTER_BAND) {
			ArrayList band = null;
			switch (nextBand) {
			case HEADER_BAND:
				band = masterPage.getHeaders();
				break;
			case FOOTER_BAND:
				band = masterPage.getFooters();
				break;
			case BODY_BAND:
				band = new ArrayList();
				break;
			}
			nextBand++;
			PageBandExecutor bandExecutor = new PageBandExecutor(this, band);
			bandExecutor.setParent(this);
			bandExecutor.setOffset(offset);
			return bandExecutor;
		}
		return null;

	}

	/**
	 * adjust the nextItem to the nextContent.
	 * 
	 * before call this method, both the nextContent and the nextFragment can't be
	 * NULL.
	 * 
	 * @return
	 */
	protected void doSkipToExecutor(InstanceID id, long offset) throws Exception {
		throw new IllegalStateException("master page never comes with page hints");
	}

}
