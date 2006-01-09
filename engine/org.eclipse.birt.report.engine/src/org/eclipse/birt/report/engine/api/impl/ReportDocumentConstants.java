package org.eclipse.birt.report.engine.api.impl;

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

public interface ReportDocumentConstants
{
	static final String CORE_STREAM = "/core";
	static final String DESIGN_STREAM = "/design"; //$NON-NLS-1$
	static final String BOOKMARK_STREAM = "/bookmark"; //$NON-NLS-1$
	static final String PAGEHINT_STREAM = "/pages"; //$NON-NLS-1$
	static final String TOC_STREAM = "/toc"; //$NON-NLS-1$
	static final String CONTENT_FOLDER = "/content"; //$NON-NLS-1$
	
	static final String REPORT_DOCUMENT_TAG = "reportdocument";
	static final String REPORT_DOCUMENT_VERSION_1_0_0 = "1.0.0";
}
