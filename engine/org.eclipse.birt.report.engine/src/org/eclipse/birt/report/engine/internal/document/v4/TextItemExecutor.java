/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

	@Override
	protected IContent doCreateContent() {
		// the text item may generate a foreign content or a label content.
		// for foreign content, it must be saved into the report document.
		// for label content, if the text is not empty, it will be saved into
		// the document.
		// so, here is a empty label content.
		return report.createLabelContent();
	}

	@Override
	protected void doExecute() throws Exception {
		executeQuery();
	}

	@Override
	public void close() {
		closeQuery();
		super.close();
	}
}
