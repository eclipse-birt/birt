/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.toc.TOCBuilder;

public class MasterPageExecutor extends ReportItemExecutor {

	MasterPageDesign masterPage;
	long pageNumber;
	TOCBuilder tocBuilder;
	int nextBand;
	IBaseResultSet[] rs;
	static final int HEADER_BAND = 0;
	static final int BODY_BAND = 1;
	static final int FOOTER_BAND = 2;

	public MasterPageExecutor(ExecutorManager manager, long pageNumber, MasterPageDesign masterPage) {
		super(manager, -1);
		this.pageNumber = pageNumber;
		this.masterPage = masterPage;
		this.nextBand = HEADER_BAND;
	}

	public IContent execute() {
		context.setPageNumber(pageNumber);

		// disable the tocBuilder
		tocBuilder = context.getTOCBuilder();
		context.setTOCBuilder(null);
		rs = context.getResultSets();
		context.setExecutingMasterPage(true);

		IPageContent pageContent = report.createPageContent();
		pageContent.setPageNumber(pageNumber);

		content = pageContent;
		content.setGenerateBy(masterPage);
		instanceId = new InstanceID(null, pageNumber, masterPage.getID(), null);
		content.setInstanceID(instanceId);
		return content;
	}

	public void close() throws BirtException {
		context.setExecutingMasterPage(false);
		// reenable the TOC
		context.setTOCBuilder(tocBuilder);
		context.setResultSets(rs);
		super.close();
	}

	public boolean hasNextChild() {
		return nextBand >= HEADER_BAND && nextBand <= FOOTER_BAND;
	}

	public IReportItemExecutor getNextChild() {
		if (hasNextChild()) {
			ArrayList band = null;
			switch (nextBand) {
			case HEADER_BAND:
				if (masterPage instanceof SimpleMasterPageDesign) {
					band = ((SimpleMasterPageDesign) masterPage).getHeaders();
				} else {
					band = new ArrayList();
				}
				break;
			case FOOTER_BAND:
				if (masterPage instanceof SimpleMasterPageDesign) {
					band = ((SimpleMasterPageDesign) masterPage).getFooters();

				} else {
					band = new ArrayList();
				}
				break;
			case BODY_BAND:
				band = new ArrayList();
				break;
			}
			nextBand++;
			PageBandExecutor bandExecutor = new PageBandExecutor(this, band);
			bandExecutor.setParent(this);
			return bandExecutor;
		}
		return null;
	}
}
