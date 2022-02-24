/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

package org.eclipse.birt.data.oda.pojo.ui.impl.contols;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;

public class MenuButtonUtil {

	public static ClassSelectionButton createClassSelectionButton(POJOClassTabFolderPage folderPage, Composite parent,
			final TableViewer viewer, final IMenuButtonProvider provider, final Listener listener, int style) {
		final ClassSelectionButton button = new ClassSelectionButton(parent, style, provider);
		button.setContainer(folderPage);
		IMenuButtonHelper helper = new MenuButtonHelper(viewer);
		helper.setProvider(provider);
		helper.setListener(listener);
		helper.setMenuButton(button);
		button.setMenuButtonHelper(helper);

		button.refresh();

		return button;
	}

}
