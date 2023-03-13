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

package org.eclipse.birt.report.engine.api.impl;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.RowHandle;

public class ScriptedDesignSearcher extends ScriptedDesignVisitor {

	protected boolean hasOnPrepareScript = false;

	public ScriptedDesignSearcher(ReportDesignHandle handle) {
		super(handle);
	}

	@Override
	public void apply(DesignElementHandle handle) {
		try {
			super.apply(handle);
		} catch (StopException e) {
			hasOnPrepareScript = true;
		}
	}

	@Override
	protected void handleOnPrepare(ReportDesignHandle handle) {
		if ((handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0)
				|| (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0)) {
			throw new StopException();
		}

	}

	@Override
	public void visitReportDesign(ReportDesignHandle handle) {
		if (handle.getInitialize() != null && handle.getInitialize().length() > 0
				|| handle.getBeforeFactory() != null && handle.getBeforeFactory().length() > 0
				|| handle.getEventHandlerClass() != null && handle.getEventHandlerClass().length() > 0) {
			throw new StopException();
		}
		super.visitReportDesign(handle);
	}

	public boolean hasOnPrepareScript() {
		return hasOnPrepareScript;
	}

	@Override
	protected void handleOnPrepare(ReportItemHandle handle) {
		if ((handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0)
				|| (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0)) {
			throw new StopException();
		}

	}

	@Override
	protected void handleOnPrepare(CellHandle handle) {
		if ((handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0)
				|| (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0)) {
			throw new StopException();
		}

	}

	@Override
	protected void handleOnPrepare(GroupHandle handle) {
		if ((handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0)
				|| (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0)) {
			throw new StopException();
		}

	}

	@Override
	protected void handleOnPrepare(RowHandle handle) {
		if ((handle.getOnPrepare() != null) && (handle.getOnPrepare().length() != 0)
				|| (handle.getEventHandlerClass() != null) && (handle.getEventHandlerClass().length() != 0)) {
			throw new StopException();
		}

	}

	@Override
	protected void visitExtendedItem(ExtendedItemHandle handle) {
		hasOnPrepareScript = true;
	}

	static class StopException extends RuntimeException {

		/**
		 *
		 */
		private static final long serialVersionUID = 1793414995245120248L;

	}

}
