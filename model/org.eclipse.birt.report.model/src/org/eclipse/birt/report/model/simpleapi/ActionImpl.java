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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.PropertyNameException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.simpleapi.IAction;
import org.eclipse.birt.report.model.elements.interfaces.ILabelModel;

public class ActionImpl extends Structure implements IAction {

	private DesignElementHandle handle;

	private Action action;

	public ActionImpl(ActionHandle acHandle, ReportItemHandle handle) {
		super(acHandle);

		if (acHandle != null) {
			this.handle = handle;
			action = (Action) acHandle.getStructure();
		} else
			action = new Action();

	}

	public ActionImpl() {
		super(null);
		action = new Action();
	}

	public String getURI() {
		return action.getStringProperty(null, Action.URI_MEMBER);
	}

	public String getTargetWindow() {
		return action.getStringProperty(null, Action.TARGET_WINDOW_MEMBER);
	}

	public String getLinkType() {
		return action.getStringProperty(null, action.LINK_TYPE_MEMBER);
	}

	public void setLinkType(String type) throws SemanticException {
		ActivityStack cmdStack = null;

		if (structureHandle != null) {
			cmdStack = structureHandle.getModule().getActivityStack();
		} else if (handle != null) {
			checkAction();
			cmdStack = handle.getModule().getActivityStack();
		}

		if (cmdStack != null) {
			cmdStack.startNonUndoableTrans(null);
			try {
				action.setProperty(action.LINK_TYPE_MEMBER, type);
			} catch (Exception e) {
				cmdStack.rollback();
				throw new SemanticException(handle.getElement(), "Failed to set action Link type  value");
			}

			cmdStack.commit();
		} else
			action.setProperty(action.LINK_TYPE_MEMBER, type);
	}

	public void setFormatType(String type) throws SemanticException {
		setActionProperty(Action.FORMAT_TYPE_MEMBER, type);
	}

	public String getFormatType() {
		return action.getStringProperty(null, Action.FORMAT_TYPE_MEMBER);
	}

	public void setTargetWindow(String window) throws SemanticException {
		setActionProperty(Action.TARGET_WINDOW_MEMBER, window);
	}

	public void setURI(String uri) throws SemanticException {
		setActionProperty(Action.URI_MEMBER, uri);
	}

	public String getReportName() {
		return action.getStringProperty(null, Action.REPORT_NAME_MEMBER);
	}

	public void setReportName(String reportName) throws SemanticException {
		setActionProperty(Action.REPORT_NAME_MEMBER, reportName);
	}

	public String getTargetBookmark() {
		return action.getStringProperty(null, Action.TARGET_BOOKMARK_MEMBER);
	}

	public void setTargetBookmark(String bookmark) throws SemanticException {
		setActionProperty(Action.TARGET_BOOKMARK_MEMBER, bookmark);
	}

	private void checkAction() throws SemanticException {
		if (handle != null) {
			if (handle instanceof LabelHandle) {
				((LabelHandle) handle).setAction(action);
				structureHandle = ((LabelHandle) handle).getActionHandle();
			} else if (handle instanceof ImageHandle) {
				((ImageHandle) handle).setAction(action);
				structureHandle = ((ImageHandle) handle).getActionHandle();
			} else if (handle instanceof DataItemHandle) {
				((DataItemHandle) handle).setAction(action);
				structureHandle = ((DataItemHandle) handle).getActionHandle();
			} else {
				throw new PropertyNameException(handle.getElement(), ILabelModel.ACTION_PROP);
			}
		}
	}

	private void setActionProperty(String propName, String value) throws SemanticException {

		if (structureHandle != null)
			setProperty(propName, value);

		else {
			checkAction();
			action.setProperty(propName, value);
		}
	}

	public IStructure getStructure() {
		// TODO Auto-generated method stub
		return action;
	}

}
