/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IPreparationContext;
import org.eclipse.birt.report.engine.script.internal.ExtendedItemScriptExecutor;
import org.eclipse.birt.report.engine.script.internal.ReportContextImpl;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignVisitor;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

public class PreparationContext extends ReportContextImpl implements IPreparationContext {

	DesignVisitor visitor = null;

	public PreparationContext(ExecutionContext context, DesignVisitor visitor) {
		super(context);
		this.visitor = visitor;
	}

	public void prepare(DesignElementHandle handle) throws BirtException {
		visitor.apply(handle);
	}

	public void triggerEvent(DesignElementHandle handle) throws BirtException {
		ExtendedItemScriptExecutor.handleOnPrepare((ExtendedItemHandle) handle, context);
	}
}
