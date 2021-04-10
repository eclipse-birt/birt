/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.editors;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.content.IContentDescriber;
import org.eclipse.core.runtime.content.IContentDescription;

/**
 * ReportDocumentDescriber
 */
public class ReportDocumentDescriber implements IContentDescriber {

	public ReportDocumentDescriber() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.content.IContentDescriber#describe(java.io.
	 * InputStream, org.eclipse.core.runtime.content.IContentDescription)
	 */
	public int describe(InputStream contents, IContentDescription description) throws IOException {
		return VALID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.content.IContentDescriber#getSupportedOptions()
	 */
	public QualifiedName[] getSupportedOptions() {
		return IContentDescription.ALL;
	}

}
