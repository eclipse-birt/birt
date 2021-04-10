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
