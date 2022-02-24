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

package org.eclipse.birt.report.designer.internal.ui.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.IMixedHandle;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.PasteAction;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.util.CopyUtil;
import org.eclipse.birt.report.model.api.util.IElementCopy;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.ui.actions.Clipboard;

/**
 * 
 */

public class PasteHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub

		super.execute(event);
		Object selection = getFirstSelectVariable();

		if (Policy.TRACING_ACTIONS) {
			System.out.println("Paste action >> Paste " + getClipBoardContents()); //$NON-NLS-1$
		}
		if (selection instanceof IMixedHandle) {
			Object cbContents = sortClipboardContents(getClipBoardContents(), selection);

			if (cbContents instanceof Object[]) {
				Object[] contents = (Object[]) cbContents;

				if (PasteAction.validateCanPaste(((IMixedHandle) selection).getSlotHandle(), contents[0], null)) {
					DNDUtil.copyHandles(contents[0], ((IMixedHandle) selection).getSlotHandle());
				}
				if (PasteAction.validateCanPaste(((IMixedHandle) selection).getPropertyHandle(), contents[1], null)) {
					DNDUtil.copyHandles(contents[1], ((IMixedHandle) selection).getPropertyHandle());
				}
			}
		} else {
			DNDUtil.copyHandles(getClipBoardContents(), selection);
		}
		return null;
	}

	protected Object getClipBoardContents() {
		return Clipboard.getDefault().getContents();
	}

	/**
	 * Sorts the contents in the Clipboard into SlotHandle and PropertyHandle, for
	 * MixedHandle can contain both SlotHandle and PropertyHandle.
	 * 
	 * @param transferData
	 * @param targetObj
	 * @return Sorted array. The first element is the SlotHandle, and the second
	 *         element is the PropertyHandle.
	 */
	protected Object sortClipboardContents(Object transferData, Object targetObj) {
		if (transferData instanceof Object[] && targetObj instanceof IMixedHandle) {
			Object[] array = (Object[]) transferData;

			List sHandle = new ArrayList();
			List pHandle = new ArrayList();

			for (int i = 0; i < array.length; i++) {
				if (array[i] instanceof IElementCopy) {
					if (CopyUtil.canPaste((IElementCopy) array[i],
							((IMixedHandle) targetObj).getSlotHandle().getElementHandle(),
							((IMixedHandle) targetObj).getSlotHandle().getSlotID()).canPaste()) {
						sHandle.add(array[i]);
					} else if (CopyUtil
							.canPaste((IElementCopy) array[i],
									((IMixedHandle) targetObj).getPropertyHandle().getElementHandle(),
									((IMixedHandle) targetObj).getPropertyHandle().getPropertyDefn().getName())
							.canPaste()) {
						pHandle.add(array[i]);
					}
				}
			}
			return new Object[] { sHandle.toArray(), pHandle.toArray() };
		}
		return transferData;
	}
}
