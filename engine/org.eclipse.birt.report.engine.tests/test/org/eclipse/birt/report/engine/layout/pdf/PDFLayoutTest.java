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

package org.eclipse.birt.report.engine.layout.pdf;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.PDFRenderOption;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.RunAndRenderTask;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.impl.PageContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.InlineContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.LineArea;
import org.eclipse.birt.report.engine.nLayout.area.impl.TextArea;

public abstract class PDFLayoutTest extends EngineCase {

	protected boolean isEmpty(ContainerArea container) {
		int childrenCount = container.getChildrenCount();
		if (childrenCount == 0) {
			return true;
		} else {
			Iterator iter = container.getChildren();
			while (iter.hasNext()) {
				Object children = iter.next();
				if (children instanceof ContainerArea) {
					return isEmpty((ContainerArea) children);
				} else {
					return false;
				}
			}
			return true;
		}
	}

	protected IArea getChildren(ContainerArea container, int index) {
		int current = 0;
		Iterator children = container.getChildren();
		while (children.hasNext()) {
			Object child = children.next();
			if (current == index) {
				return (IArea) child;
			}
			++current;
		}
		return null;
	}

	protected String getText(LineArea line, int index) {
		InlineContainerArea inlineArea = (InlineContainerArea) getChildren(line, index);
		IArea area = getChildren(inlineArea, 0);
		if (!(area instanceof TextArea)) {
			fail("Child " + index + " of line doesn't contains text Area");
		}
		return ((TextArea) area).getText();
	}

	protected List getPageAreas(String designFile) throws EngineException {
		IReportRunnable report = openReportDesign(designFile);
		return getPageAreas(report);
	}

	protected IReportRunnable openReportDesign(String designFile) throws EngineException {
		useDesignFile(designFile);
		IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
		return report;
	}

	protected List getPageAreas(IReportRunnable runnable) throws EngineException {
		List pageAreas = new ArrayList();
		IEmitterMonitor monitor = new PageMonitor(pageAreas);
		IRunAndRenderTask runAndRenderTask = new TestRunAndRenderTask(engine, runnable, monitor);

		runAndRenderTask.setRenderOption(createRenderOption());
		runAndRenderTask.run();
		runAndRenderTask.close();
		return pageAreas;
	}

	protected List getpageAreas(String designFile) throws EngineException {
		IReportRunnable report = openReportDesign(designFile);
		List pageAreas = getPageAreas(report);
		return pageAreas;
	}

	protected PDFRenderOption createRenderOption() {
		PDFRenderOption options = new PDFRenderOption();
		options.setOutputFormat("pdf");
		options.setOutputStream(new ByteArrayOutputStream());
		return options;
	}

	protected static class PageMonitor implements IEmitterMonitor {
		List pageAreas;

		public PageMonitor(List pageAreas) {
			this.pageAreas = pageAreas;
		}

		public void onMethod(Method method, Object[] args) {
			if ("startPage".equals(method.getName())) {
				PageContent pageContent = (PageContent) args[0];
				pageAreas.add(pageContent.getExtension(IContent.LAYOUT_EXTENSION));
			}
		}
	}
}

interface IEmitterMonitor {
	void onMethod(Method method, Object[] args);
}

class TestRunAndRenderTask extends RunAndRenderTask {
	IEmitterMonitor monitor;

	public TestRunAndRenderTask(IReportEngine engine, IReportRunnable runnable, IEmitterMonitor monitor) {
		super((ReportEngine) engine, runnable);
		this.monitor = monitor;
	}

	protected IContentEmitter createContentEmitter() throws EngineException {
		final IContentEmitter emitter = super.createContentEmitter();
		return (IContentEmitter) Proxy.newProxyInstance(emitter.getClass().getClassLoader(),
				new Class[] { IContentEmitter.class }, new InvocationHandler() {

					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						monitor.onMethod(method, args);
						return method.invoke(emitter, args);
					}
				});
	}
}
