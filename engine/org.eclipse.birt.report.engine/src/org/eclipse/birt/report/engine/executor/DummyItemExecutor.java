/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.report.engine.content.IContent;

public class DummyItemExecutor extends ReportItemExecutor {

	protected DummyItemExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.DUMMYITEM);
	}

	@Override
	public IContent execute() {
		generateUniqueID();
		return null;
	}
}
