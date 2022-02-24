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

package org.eclipse.birt.report.designer.core.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.IMixedHandle;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementDetailHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.gef.commands.Command;

/**
 * This command moves a child inside a SlotHandle
 * 
 * 
 */

public class FlowMoveChildCommand extends Command {

	private static Logger logger = Logger.getLogger(FlowMoveChildCommand.class.getName());

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
	public FlowMoveChildCommand(Object child, Object after, Object container) {
		this.child = child;
		this.after = after;
		this.container = container;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is not
	 * executable.
	 */

	public void execute() {
		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("FlowMoveChildCommand >> Starts ... "); //$NON-NLS-1$
		}
		try {

			DesignElementHandle containerHandle = null;

			int slotID = -1, pos = -1;
			String contentString = null;

			// for virtual model that contains a slot handle
			if (container instanceof ListBandProxy) {
				containerHandle = ((ListBandProxy) container).getSlotHandle().getElementHandle();
				ElementDetailHandle slot = ((ListBandProxy) container).getSlotHandle();
				if (slot instanceof SlotHandle) {
					slotID = ((SlotHandle) slot).getSlotID();
				} else {
					slotID = -1;
				}
				pos = DEUtil.findInsertPosition(containerHandle, (DesignElementHandle) after, slotID);
				int cur = DEUtil.findInsertPosition(containerHandle, (DesignElementHandle) child, slotID);
				if (cur < pos) {
					pos--;
				}
			}

			// for real node that contains design element handle
			else if (container instanceof DesignElementHandle) {
				containerHandle = (DesignElementHandle) container;
				slotID = DEUtil.getSlotID(containerHandle, after);
				if (slotID == -1) {
					contentString = DEUtil.getDefaultContentName(containerHandle);
					pos = DEUtil.findInsertPosition(containerHandle, (DesignElementHandle) after, contentString);
				} else {
					pos = DEUtil.findInsertPosition(containerHandle, (DesignElementHandle) after);
				}
			} else
//			if ( container instanceof ReportElementModel )
//			{
//				containerHandle = ( (ReportElementModel) container ).getElementHandle( );
//				slotID = ( (ReportElementModel) container ).getSlotId( );
//				pos = DEUtil.findInsertPosition( containerHandle,
//						(DesignElementHandle) after,
//						slotID );
//			}else 
			if (container instanceof SlotHandle) {
				containerHandle = ((SlotHandle) container).getElementHandle();
				slotID = ((SlotHandle) container).getSlotID();
				pos = DEUtil.findInsertPosition(containerHandle, (DesignElementHandle) after, slotID);
			} else if (container instanceof IMixedHandle) {
				IMixedHandle mixHandleInstance = (IMixedHandle) container;
				if (child instanceof CubeHandle) {
					containerHandle = mixHandleInstance.getSlotHandle().getElementHandle();
					ElementDetailHandle slot = mixHandleInstance.getSlotHandle();
					if (slot instanceof SlotHandle) {
						slotID = ((SlotHandle) slot).getSlotID();
					}

					pos = DEUtil.findInsertPosition(containerHandle,
							(DesignElementHandle) adjustAfterObjectForSlotHandleInIMixedHandle(), slotID);
					int cur = DEUtil.findInsertPosition(containerHandle, (DesignElementHandle) child, slotID);
					if (cur < pos) {
						pos--;
					}
				} else {
					containerHandle = mixHandleInstance.getPropertyHandle().getElementHandle();
					contentString = DEUtil.getDefaultContentName(containerHandle);
					pos = computePosForPropertyHandleInIMixedHandle(containerHandle, contentString);
					int cur = DEUtil.findInsertPosition(containerHandle, (DesignElementHandle) child, contentString);
					if (cur < pos) {
						pos--;
					}
				}
			}

			DesignElementHandle handle = (DesignElementHandle) child;

			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();

			stack.startTrans(TRANS_LABEL_MOVE_ELEMENT);

			if (slotID == -1) {
				handle.moveTo(containerHandle, contentString);
				containerHandle.getPropertyHandle(contentString).shift(handle, pos);
			} else {
				handle.moveTo(containerHandle, slotID);
				containerHandle.getSlot(slotID).shift(handle, pos);
			}

			stack.commit();
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("FlowMoveChildCommand >> Finished. Moved " //$NON-NLS-1$
						+ DEUtil.getDisplayLabel(handle) + " to the slot " //$NON-NLS-1$
						+ slotID + " of " //$NON-NLS-1$
						+ DEUtil.getDisplayLabel(containerHandle) + ",Position: " //$NON-NLS-1$
						+ pos);
			}
		} catch (ContentException e) {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("FlowMoveChildCommand >> Failed"); //$NON-NLS-1$
			}
			logger.log(Level.SEVERE, e.getMessage(), e);
		} catch (SemanticException ee) {
			logger.log(Level.SEVERE, ee.getMessage(), ee);
		}
	}

	private int computePosForPropertyHandleInIMixedHandle(DesignElementHandle containerHandle, String contentString) {
		if (after != null && after instanceof CubeHandle) {
			return 0;
		} else {
			return DEUtil.findInsertPosition(containerHandle, (DesignElementHandle) after, contentString);
		}
	}

	private Object adjustAfterObjectForSlotHandleInIMixedHandle() {
		if (after != null && !(after instanceof CubeHandle)) {
			return null;
		}
		return after;
	}
}
