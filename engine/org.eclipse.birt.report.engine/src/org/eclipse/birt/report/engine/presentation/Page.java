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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;

public class Page {

	IPageContent pageContent;
	IContentEmitter emitter;
	PageRegion rootRegion;

	public Page(IContentEmitter emitter, IPageContent pageContent) {
		this.emitter = emitter;
		this.pageContent = pageContent;
		this.rootRegion = new PageRegion(this);
	}

	public PageRegion getRootRegion() {
		return rootRegion;
	}

	public IContentEmitter getEmitter() {
		return this.emitter;
	}

	public void open() throws BirtException {
		emitter.startPage(pageContent);
	}

	public void close() throws BirtException {
		emitter.endPage(pageContent);
	}

	public PageRegion createRegion() {
		return new PageRegion(this);
	}
}
