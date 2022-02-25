/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IAction;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

public class ActionImpl implements IAction {

	private org.eclipse.birt.report.model.api.simpleapi.IAction actionImpl;

	private void init(ActionHandle action, ReportItemHandle handle) {
		actionImpl = SimpleElementFactory.getInstance().createAction(action, handle);
	}

	public ActionImpl(ActionHandle action, LabelHandle handle) {
		init(action, handle);
	}

	public ActionImpl(ActionHandle action, ImageHandle handle) {
		init(action, handle);
	}

	public ActionImpl(ActionHandle action, DataItemHandle handle) {
		init(action, handle);
	}

	public ActionImpl(org.eclipse.birt.report.model.api.simpleapi.IAction action) {
		actionImpl = action;
	}

	public ActionImpl() {
		actionImpl = SimpleElementFactory.getInstance().createAction();
	}

	@Override
	public String getURI() {
		return actionImpl.getURI();
	}

	@Override
	public String getTargetWindow() {
		return actionImpl.getTargetWindow();
	}

	@Override
	public String getLinkType() {
		return actionImpl.getLinkType();
	}

	@Override
	public void setLinkType(String type) throws ScriptException {
		try {
			actionImpl.setLinkType(type);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public void setFormatType(String type) throws ScriptException {
		try {
			actionImpl.setFormatType(type);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public String getFormatType() {
		return actionImpl.getFormatType();
	}

	@Override
	public void setTargetWindow(String window) throws ScriptException {
		try {
			actionImpl.setTargetWindow(window);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public void setURI(String uri) throws ScriptException {
		try {
			actionImpl.setURI(uri);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getReportName() {
		return actionImpl.getReportName();
	}

	@Override
	public void setReportName(String reportName) throws ScriptException {
		try {
			actionImpl.setReportName(reportName);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public String getTargetBookmark() {
		return actionImpl.getTargetBookmark();
	}

	@Override
	public void setTargetBookmark(String bookmark) throws ScriptException {
		try {
			actionImpl.setTargetBookmark(bookmark);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public IStructure getStructure() {
		return actionImpl.getStructure();

	}

}
