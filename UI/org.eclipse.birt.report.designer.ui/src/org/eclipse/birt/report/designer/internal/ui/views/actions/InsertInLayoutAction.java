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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.dnd.InsertInLayoutUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * Action to insert object to layout.
 * <p>
 * Can use run() method of instance or use static method
 * <code>insertSingleInsert()</code> to new a object
 * <p>
 */

public class InsertInLayoutAction extends AbstractViewAction {

	public static final String DISPLAY_TEXT = Messages.getString("InsertInLayoutAction.action.text"); //$NON-NLS-1$

	private EditPart targetPart;

	/**
	 * Constructor. Uses DISPLAY_TEXT as default text.
	 * 
	 * @param selectedObject
	 */
	public InsertInLayoutAction(Object selectedObject) {
		this(selectedObject, DISPLAY_TEXT);
	}

	/**
	 *  
	 */
	public InsertInLayoutAction(Object selectedObject, String text) {
		super(selectedObject, text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled() {
		return isTypeAvailable() && getTargetEditPart() != null
				&& InsertInLayoutUtil.handleValidateInsertToLayout(getSelection(), getTargetEditPart());
	}

	/**
	 * Returns if the selection is the type which can be inserted to layout
	 */
	public boolean isTypeAvailable() {
		return InsertInLayoutUtil.handleValidateInsert(getSelection());
	}

	protected EditPart getTargetEditPart() {
		if (targetPart == null) {
			EditPartViewer viewer = UIUtil.getLayoutEditPartViewer();
			if (viewer == null) {
				return null;
			}
			IStructuredSelection targets = (IStructuredSelection) viewer.getSelection();
			if (targets.isEmpty() && targets.size() > 1)
				return null;
			targetPart = (EditPart) targets.getFirstElement();
		}
		return targetPart;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run() {
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(DISPLAY_TEXT);
		try {
			if (Policy.TRACING_ACTIONS) {
				System.out.println("Insert layout action >> Runs ..."); //$NON-NLS-1$
			}
			Object newElement = InsertInLayoutUtil.performInsert(getSelection(), getTargetEditPart());
			if (newElement != null)
				runCreate(newElement, targetPart.getModel());
			stack.commit();
			fireCreateRequest(newElement, getSelection());
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
			stack.rollback();
		}
	}

	private void fireCreateRequest(Object newElement, Object source) {
		List list = new ArrayList();
		list.add(newElement);
		ReportRequest r = new ReportRequest(source);
		r.setType(ReportRequest.CREATE_ELEMENT);

		r.setSelectionObject(list);
		SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);
	}

	private void runCreate(Object insertedObj, Object container) {
		if (container instanceof ListBandProxy) {
			container = ((ListBandProxy) container).getSlotHandle();
		}
		HashMap map = new HashMap();
		map.put(DesignerConstants.KEY_NEWOBJECT, insertedObj);
		CreateCommand command = new CreateCommand(map);
		command.setParent(container);
		command.execute();
	}
}