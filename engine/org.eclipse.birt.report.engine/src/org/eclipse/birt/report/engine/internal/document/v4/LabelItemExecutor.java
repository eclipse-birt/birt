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

	@Override
	protected IContent doCreateContent() {
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
