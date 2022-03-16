/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.runtime.IAdaptable;

/**
 * Drag the item in the same cross cell.
 */
public class CrosstabFlowMoveChildCommand extends AbstractCrosstabCommand {

	private static final String TRANS_LABEL_MOVE_ELEMENT = Messages
			.getString("FlowMoveChildCommand.transLabel.moveElement"); //$NON-NLS-1$

	private Object child = null;

	private Object after = null;

	private Object container = null;

	/**
	 * Constructor
	 *
	 * @param container
	 * @param model
	 * @param model2
	 */
	public CrosstabFlowMoveChildCommand(Object child, Object after, Object container) {
		super((DesignElementHandle) child);
		this.child = child;
		this.after = after;
		if (container instanceof IAdaptable) {
			this.container = ((IAdaptable) container).getAdapter(DesignElementHandle.class);
		} else {
			this.container = container;
		}

		setLabel(TRANS_LABEL_MOVE_ELEMENT);
	}

	/**
	 * Executes the Command. This method should not be called if the Command is not
	 * executable.
	 */

	@Override
	public void execute() {
		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("FlowMoveChildCommand >> Starts ... "); //$NON-NLS-1$
		}
		try {

			DesignElementHandle containerHandle;

			int pos;

			// for real node that contains design element handle
			containerHandle = (DesignElementHandle) container;
			String contentProperty = DEUtil.getContentProperty(containerHandle, after);
			pos = CrosstabAdaptUtil.findInsertPosition(containerHandle, (DesignElementHandle) after);

			DesignElementHandle handle = (DesignElementHandle) child;

			transStart(TRANS_LABEL_MOVE_ELEMENT);

			handle.moveTo(containerHandle, contentProperty);

			// containerHandle.getSlot( slotID ).shift( handle, pos );
			containerHandle.shift(contentProperty, handle, pos);
			transEnd();
		} catch (SemanticException e) {
			rollBack();
			ExceptionUtil.handle(e);
		}
	}
}
