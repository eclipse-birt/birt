/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.content.IContent;

/**
 * DataItemExecutor
 * 
 */
public class TextItemExecutor extends ReportItemExecutor {

	public TextItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.TEXTITEM);
	}

	protected IContent doCreateContent() {
		// the text item may generate a foreign content or a label content.
		// for foreign content, it must be saved into the report document.
		// for label content, if the text is not empty, it will be saved into
		// the document.
		// so, here is a empty label content.
		return report.createLabelContent();
	}

	protected void doExecute() throws Exception {
		executeQuery();
	}

	public void close() {
		closeQuery();
		super.close();
	}
}
