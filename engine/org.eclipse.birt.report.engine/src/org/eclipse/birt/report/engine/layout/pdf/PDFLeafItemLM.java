/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;

public abstract class PDFLeafItemLM extends PDFAbstractLM {

	public PDFLeafItemLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
	}

	@Override
	protected void cancelChildren() {

	}

	@Override
	public boolean allowPageBreak() {
		return false;
	}

	@Override
	protected boolean hasNextChild() {
		return true;
	}

	@Override
	public void autoPageBreak() {
	}

}
