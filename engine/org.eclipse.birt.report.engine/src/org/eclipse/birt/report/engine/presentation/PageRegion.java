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

package org.eclipse.birt.report.engine.presentation;

import java.util.ArrayList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.ContentEmitterUtil;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.emitter.IEmitterServices;

public class PageRegion extends ContentEmitterAdapter {
	// private DOMBuildingEmitter domBuilderEmitter;
	IContentEmitter emitter;

	public PageRegion(Page page) {
		emitter = page.getEmitter();
	}

	public void open(IContent content) throws BirtException {
		if (content != null) {
			ArrayList contents = getAncestors(content);
			int size = contents.size() - 1;
			for (int i = size; i >= 0; i--) {
				IContent parent = (IContent) contents.get(i);
				ContentEmitterUtil.startContent(parent, emitter);
			}
		}
	}

	public void close(IContent content) throws BirtException {
		if (content != null) {
			ArrayList contents = getAncestors(content);
			int size = contents.size();
			for (int i = 0; i < size; i++) {
				IContent parent = (IContent) contents.get(i);
				ContentEmitterUtil.endContent(parent, emitter);
			}
		}
	}

	private ArrayList getAncestors(IContent content) {
		ArrayList list = new ArrayList();
		// Top level content is a virtual element, not a real ancestor.
		while (content.getParent() != null) {
			list.add(content);
			content = (IContent) content.getParent();
		}
		return list;
	}

	@Override
	public void end(IReportContent report) throws BirtException {
		emitter.end(report);
	}

	@Override
	public String getOutputFormat() {
		return emitter.getOutputFormat();
	}

	@Override
	public void initialize(IEmitterServices service) throws BirtException {
		emitter.initialize(service);
	}

	@Override
	public void start(IReportContent report) throws BirtException {
		emitter.start(report);
	}

	@Override
	public void startContent(IContent content) throws BirtException {
		ContentEmitterUtil.startContent(content, emitter);
	}

	@Override
	public void endContent(IContent content) throws BirtException {
		ContentEmitterUtil.endContent(content, emitter);
	}
}
