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

package org.eclipse.birt.report.designer.core.model.views.outline;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.ScriptedElementVisitor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;

/**
 * Represents the scripts node of a report design or a report element
 */
public class ScriptElementNode implements IScriptTreeNode, IMenuListener {

	private DesignElementHandle parent;

	public ScriptElementNode(DesignElementHandle parent) {
		this.parent = parent;
	}

	@Override
	public Object[] getChildren() {
		if (this.parent != null) {
			ScriptedElementVisitor visitor = new ScriptedElementVisitor();
			return visitor.getScriptNodes(parent).toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent() {
		return this.parent;
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		manager.add(new GotoReportElementAction(getParent()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object arg0) {
		if (arg0 == this) {
			return true;
		}
		if (arg0 instanceof ScriptElementNode) {
			return ((ScriptElementNode) arg0).parent == parent;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hashCode = 13;
		if (parent != null) {
			hashCode += parent.hashCode() * 7;
		}
		return hashCode;
	}

}

class GotoReportElementAction extends Action {

	private static final String ACTION_TEXT = Messages.getString("ScriptElementNode.Action.Text"); //$NON-NLS-1$
	private Object source;

	public GotoReportElementAction(Object seletedElement) {
		super(ACTION_TEXT);
		this.source = seletedElement;
	}

	@Override
	public void run() {
		fireSelectionChanged();
	}

	/**
	 * Fires a selection changed event.
	 *
	 * @param selection the new selection
	 */
	protected void fireSelectionChanged() {
		ReportRequest request = new ReportRequest(source);
		List list = new ArrayList();
		list.add(source);

		request.setSelectionObject(list);
		request.setType(ReportRequest.SELECTION);

		SessionHandleAdapter.getInstance().getMediator().notifyRequest(request);
	}
}
