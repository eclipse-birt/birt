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

package org.eclipse.birt.sample.reportitem.rotatedtext;

import java.util.List;

import org.eclipse.birt.report.designer.ui.extensions.IMenuBuilder;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;

/**
 * RotatedTextMenuBuilder
 */
public class RotatedTextMenuBuilder implements IMenuBuilder {

	public void buildMenu(IMenuManager menu, List selectedList) {
		if (selectedList != null && selectedList.size() == 1 && selectedList.get(0) instanceof ExtendedItemHandle) {
			ExtendedItemHandle handle = (ExtendedItemHandle) selectedList.get(0);

			if (!RotatedTextItem.EXTENSION_NAME.equals(handle.getExtensionName())) {
				return;
			}

			RotatedTextItem item = null;
			try {
				item = (RotatedTextItem) handle.getReportItem();
			} catch (ExtendedElementException e) {
				e.printStackTrace();
			}

			if (item == null) {
				return;
			}

			Separator separator = new Separator("group.rotatedtext"); //$NON-NLS-1$
			if (menu.getItems().length > 0) {
				menu.insertBefore(menu.getItems()[0].getId(), separator);
			} else {
				menu.add(separator);
			}

			menu.appendToGroup(separator.getId(), new RotateAction(item, -90));
			menu.appendToGroup(separator.getId(), new RotateAction(item, 90));
			menu.appendToGroup(separator.getId(), new RotateAction(item, 0));
			menu.appendToGroup(separator.getId(), new RotateAction(item, 180));
		}
	}

	/**
	 * RotateAtction
	 */
	static class RotateAction extends Action {

		private RotatedTextItem item;
		private int angle;

		RotateAction(RotatedTextItem item, int angle) {
			this.item = item;
			this.angle = angle;

			setText("Rotate as " + angle + "\u00BA"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		@Override
		public void run() {
			try {
				item.setRotationAngle(angle);
			} catch (SemanticException e) {
				e.printStackTrace();
			}
		}
	}
}
