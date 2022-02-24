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
 * MultiLine Item Executor
 * 
 */
public class DynamicTextItemExecutor extends ReportItemExecutor {

	DynamicTextItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.DYNAMICTEXTITEM);
	}

	protected void doExecute() throws Exception {
		executeQuery();
	}

	public void close() {
		closeQuery();
		super.close();
	}

	protected IContent doCreateContent() {
		throw new IllegalStateException("can not re-generate content for MultiLineItem");
	}
}
