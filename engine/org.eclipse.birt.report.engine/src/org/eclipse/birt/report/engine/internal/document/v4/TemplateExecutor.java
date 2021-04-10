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

public class TemplateExecutor extends ReportItemExecutor {

	/**
	 * constructor
	 * 
	 * @param context the excutor context
	 * @param visitor the report executor visitor
	 */
	public TemplateExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.TEMPLATEITEM);
	}

	protected IContent doCreateContent() {
		throw new IllegalStateException("can not re-generate content for template item");
	}

	protected void doExecute() throws Exception {

	}

}
