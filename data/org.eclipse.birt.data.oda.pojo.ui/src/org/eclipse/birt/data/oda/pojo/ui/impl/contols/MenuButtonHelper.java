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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.oda.pojo.ui.i18n.Messages;
import org.eclipse.birt.data.oda.pojo.ui.impl.models.ClassPathElement;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;

public class MenuButtonHelper implements IMenuButtonHelper {

	private TableViewer viewer;
	private ClassSelectionButton button;
	private IMenuButtonProvider provider;

	private List<ClassPathElement> elements;

	public MenuButtonHelper(TableViewer viewer) {
		this.viewer = viewer;
		updateTableElementsList();
	}

	public void clearTableElementsList() {
		if (elements == null) {
			elements = new ArrayList<>();
		} else {
			elements.clear();
		}
	}

	public void updateTableElementsList() {
		elements = (List<ClassPathElement>) viewer.getInput();
		if (elements == null) {
			elements = new ArrayList<>();
		}
	}

	public int getElementCount() {
		return elements == null ? 0 : elements.size();
	}

	@Override
	public void addClassPathElements(ClassPathElement[] items, boolean current) {
		boolean containsDuplicated = false;
		for (int i = 0; i < items.length; i++) {
			boolean exists = false;
			for (int j = 0; j < elements.size(); j++) {
				if (elements.get(j).isRelativePath() == items[i].isRelativePath()
						&& elements.get(j).getFullPath() != null
						&& elements.get(j).getFullPath().equals(items[i].getFullPath())) {
					exists = true;
					break;
				}
			}
			if (!exists) {
				this.elements.add(items[i]);
			} else {
				containsDuplicated = true;
			}
		}
		viewer.setInput(elements);
		viewer.refresh();
		if (containsDuplicated && current) {
			ExceptionHandler.openMessageBox(Messages.getString("ExceptionDialog.title.erro"), //$NON-NLS-1$
					Messages.getString("ExceptionDialog.DataSource.AddDuplicatedJar"), SWT.ICON_ERROR); //$NON-NLS-1$
		}
	}

	@Override
	public void setProvider(IMenuButtonProvider provider) {
		this.provider = provider;
	}

	public IMenuButtonProvider getProvider() {
		return this.provider;
	}

	@Override
	public void setMenuButton(ClassSelectionButton button) {
		this.button = button;
	}

	@Override
	public Object getPropertyValue(String key) {
		return button.getControl().getData(key);
	}

	@Override
	public void setProperty(String key, Object value) {
		button.getControl().setData(key, value);
	}

	@Override
	public void setListener(Listener listener) {

	}

	@Override
	public void notifyExpressionChangeEvent(String oldExpression, String newExpression) {

	}

}
