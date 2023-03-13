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
package org.eclipse.birt.report.service;

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;

public class BirtViewerReportDesignHandle implements IViewerReportDesignHandle {
	private String contentType;

	private String fileName;

	private IReportRunnable runnable; // Report design name not always exists.

	public BirtViewerReportDesignHandle(String contentType, String fileName) {
		this.contentType = contentType;
		this.fileName = fileName;
	}

	public BirtViewerReportDesignHandle(String contentType, IReportRunnable runnable) {
		this.contentType = contentType;
		this.runnable = runnable;
	}

	@Override
	public String getContentType() {
		return contentType;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public void setFileName(String name) {
		this.fileName = name;
	}

	@Override
	public Object getDesignObject() {
		return runnable;
	}

	@Override
	public void setDesignObject(Object obj) {
		runnable = (IReportRunnable) obj;
	}

	@Override
	public ByteArrayOutputStream getObjectStream() {
		// TODO What to do here??
		return null;
	}

	@Override
	public String getDocumentName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDocumentName(String documentName) {
		// TODO Auto-generated method stub

	}

}
