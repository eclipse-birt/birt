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

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 *
 */

public class DeleteHandler extends SelectionHandler {

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.
	 * ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		super.execute(event);

		List list = convertDeleteList(getElementHandles());
		List deletes = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof SlotHandle) {
				List objs = ((SlotHandle) obj).getContents();
				for (int j = 0; j < objs.size(); j++) {
					if (UIUtil.canDelete(objs.get(j))) {
						deletes.add(objs.get(j));
					}
				}
			} else if (UIUtil.canDelete(obj)) {
				deletes.add(obj);
			}
		}

		// boolean hasExecuted = UIUtil.canDelete( getElementHandles( ) );
		boolean hasExecuted = !deletes.isEmpty();
		if (hasExecuted) {
			createDeleteCommand(deletes.toArray()).execute();
		}

		return Boolean.valueOf(hasExecuted);
	}

	private List convertDeleteList(List list) {
		List retValue = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Object obj = list.get(i);
			if (obj instanceof IStructuredSelection) {
				List objs = ((IStructuredSelection) obj).toList();
				for (int j = 0; j < objs.size(); j++) {
					if (objs.get(j) instanceof SlotHandle) {
						List temsps = ((SlotHandle) objs.get(j)).getContents();
						for (int k = 0; k < temsps.size(); k++) {
							retValue.add(temsps.get(k));
						}
					} else if (objs.get(j) instanceof MeasureGroupHandle) {
						retValue.addAll(
								((MeasureGroupHandle) objs.get(j)).getContents(MeasureGroupHandle.MEASURES_PROP));
						retValue.add(objs.get(j));
					} else {
						retValue.add(objs.get(j));
					}
				}
			} else if (obj instanceof MeasureGroupHandle) {
				retValue.addAll(((MeasureGroupHandle) obj).getContents(MeasureGroupHandle.MEASURES_PROP));
				retValue.add(obj);
			} else {
				retValue.add(obj);
			}
		}
		return retValue;
	}

	protected Command createDeleteCommand(Object objects) {
		return new DeleteCommand(objects);
	}

}
