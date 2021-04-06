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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.TableGroupHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.action.Action;

/**
 * Show group row action
 * 
 * 
 */
public abstract class IncludeTableGroupAction extends Action {

	protected Object selection;

	/**
	 * Constructor
	 * 
	 * @param selection
	 */
	public IncludeTableGroupAction(Object selection) {
		super("", AS_CHECK_BOX); //$NON-NLS-1$
		this.selection = selection;
		showAvailable();
	}

	private SlotHandle getRowContainer() {
		if (selection instanceof RowHandle) {
			return ((RowHandle) selection).getContainerSlotHandle();
		}
		return null;
	}

	protected boolean isTableGroup() {
		return getRowContainer() != null && getRowContainer().getElementHandle() instanceof TableGroupHandle;
	}

	protected TableGroupHandle getTableGroup() {
		if (isTableGroup()) {
			return (TableGroupHandle) getRowContainer().getElementHandle();
		}
		return null;
	}

	private TableGroupHandleAdapter getTableGroupAdapter() {
		return HandleAdapterFactory.getInstance().getTableGroupHandleAdapter(getTableGroup());
	}

	private void includeSlotHandle(boolean bool, int id) {
		try {
			if (bool) {
				getTableGroupAdapter().insertRowInSlotHandle(id);
			} else {
				getTableGroupAdapter().deleteRowInSlotHandle(id);
			}
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled() {
		return true;
	}

	/**
	 * Runs action.
	 */
	public void run() {
		if (Policy.TRACING_ACTIONS) {
			System.out.println("Include table group action >> Run ..."); //$NON-NLS-1$
		}
		includeSlotHandle(isChecked(), getGroupSlotId());
	}

	private void showAvailable() {
		if (getRowContainer() != null) {
			setChecked(getTableGroupAdapter().hasSlotHandleRow(getGroupSlotId()));
		}
	}

	abstract protected int getGroupSlotId();

	public static class IncludeTableGroupHeaderAction extends IncludeTableGroupAction {

		private static final String ACTION_MSG_INCLUDE_HEADER = Messages
				.getString("IncludeTableGroupHeaderAction.actionMsg.includeHeader"); //$NON-NLS-1$

		/**
		 * @param selection
		 */
		public IncludeTableGroupHeaderAction(Object selection) {
			super(selection);
			setText(ACTION_MSG_INCLUDE_HEADER);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.
		 * IncludeTableGroupAction#getGroupSlotId()
		 */
		protected int getGroupSlotId() {
			return GroupHandle.HEADER_SLOT;
		}
	}

	public static class IncludeTableGroupFooterAction extends IncludeTableGroupAction {

		private static final String ACTION_MSG_INCLUDE_HEADER = Messages
				.getString("IncludeTableGroupFooterAction.actionMsg.includeFooter"); //$NON-NLS-1$

		/**
		 * @param selection
		 */
		public IncludeTableGroupFooterAction(Object selection) {
			super(selection);
			setText(ACTION_MSG_INCLUDE_HEADER);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.
		 * IncludeTableGroupAction#getGroupSlotId()
		 */
		protected int getGroupSlotId() {
			return GroupHandle.FOOTER_SLOT;
		}
	}
}