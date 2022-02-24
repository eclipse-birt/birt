/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.instance;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.instance.IPageInstance;
import org.eclipse.birt.report.engine.api.script.instance.IReportItemInstance;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.script.internal.ElementUtil;

public class PageInstance implements IPageInstance {

	ExecutionContext context;
	PageContent pageContent;
	Collection<IContent> contents;

	public PageInstance(ExecutionContext context, PageContent pageContent, Collection<IContent> contents) {
		this.context = context;
		this.pageContent = pageContent;
		this.contents = contents;
	}

	public IReportItemInstance[] getInstancesByElementId(int elementId) throws ScriptException {
		ArrayList<IReportItemInstance> instances = new ArrayList<IReportItemInstance>();
		for (IContent content : contents) {
			Object generateBy = content.getGenerateBy();
			if (generateBy instanceof ReportElementDesign) {
				ReportElementDesign design = (ReportElementDesign) generateBy;
				if (design.getID() == elementId) {
					try {
						ReportItemInstance instance = (ReportItemInstance) ElementUtil.getInstance(content, context,
								RunningState.PAGEBREAK);
						instances.add(instance);
					} catch (BirtException ex) {
						throw new ScriptException(ex);
					}
				}
			}
		}
		if (!instances.isEmpty()) {
			return instances.toArray(new IReportItemInstance[instances.size()]);
		}
		return null;
	}

	public IReportItemInstance[] getInstancesByElementName(String elementName) throws ScriptException {
		ArrayList<IReportItemInstance> instances = new ArrayList<IReportItemInstance>();
		for (IContent content : contents) {
			Object generateBy = content.getGenerateBy();
			if (generateBy instanceof ReportElementDesign) {
				ReportElementDesign design = (ReportElementDesign) generateBy;
				if (elementName.equals(design.getName())) {
					try {
						ReportItemInstance instance = (ReportItemInstance) ElementUtil.getInstance(content, context,
								RunningState.PAGEBREAK);
						instances.add(instance);
					} catch (BirtException ex) {
						throw new ScriptException(ex);
					}
				}
			}
		}
		if (!instances.isEmpty()) {
			return instances.toArray(new IReportItemInstance[instances.size()]);
		}
		return null;
	}
}
