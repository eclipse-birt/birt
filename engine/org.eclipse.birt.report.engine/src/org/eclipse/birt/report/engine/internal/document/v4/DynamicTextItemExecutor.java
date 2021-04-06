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
