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

package org.eclipse.birt.report.engine.internal.document.v4;

import org.eclipse.birt.report.engine.content.IContent;

/**
 * the labelItem excutor
 * 
 */
public class LabelItemExecutor extends ReportItemExecutor {

	public LabelItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.LABELITEM);
	}

	protected IContent doCreateContent() {
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
