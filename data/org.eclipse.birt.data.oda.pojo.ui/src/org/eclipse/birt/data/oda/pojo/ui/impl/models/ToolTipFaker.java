
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
package org.eclipse.birt.data.oda.pojo.ui.impl.models;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.birt.data.oda.pojo.util.ClassParser;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Generate tool tip for each item in class structure tree
 */
public class ToolTipFaker {
	private static final String TREE_ITEM_FLAG = "_TREEITEM"; //$NON-NLS-1$
	private TreeViewer tv;

	public ToolTipFaker(TreeViewer tv) {
		this.tv = tv;
	}

	public void fakeToolTip() {
		tv.getTree().setToolTipText(""); //$NON-NLS-1$

		// Implement a "fake" tooltip
		final Listener labelListener = new Listener() {
			@Override
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TreeItem) label.getData(TREE_ITEM_FLAG);
					// Assuming tree is single select, set the selection as if
					// the mouse down event went through to the table
					tv.getTree().setSelection(new TreeItem[] { (TreeItem) e.item });
					tv.getTree().notifyListeners(SWT.Selection, e);

					shell.dispose();
					tv.getTree().setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};

		Listener treeListener = new Listener() {
			Shell tip = null;
			Label label = null;

			@Override
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null) {
						break;
					}
					tip.dispose();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					TreeItem item = tv.getTree().getItem(new Point(event.x, event.y));
					if (item != null) {
						if (tip != null && !tip.isDisposed()) {
							tip.dispose();
						}
						tip = new Shell(tv.getTree().getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
						tip.setBackground(
								tv.getTree().getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						FillLayout layout = new FillLayout();
						layout.marginWidth = 2;
						tip.setLayout(layout);
						label = new Label(tip, SWT.NONE);
						label.setForeground(
								tv.getTree().getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
						label.setBackground(
								tv.getTree().getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						label.setData(TREE_ITEM_FLAG, item);
						label.setText(getToolTip(item));
						label.addListener(SWT.MouseExit, labelListener);
						label.addListener(SWT.MouseDown, labelListener);
						Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
						Rectangle rect = item.getBounds(0);
						Point pt = tv.getTree().toDisplay(rect.x, rect.y);
						tip.setBounds(pt.x, pt.y, size.x, size.y);
						tip.setVisible(true);
					}
				}
				}
			}
		};

		tv.getTree().addListener(SWT.Dispose, treeListener);
		tv.getTree().addListener(SWT.KeyDown, treeListener);
		tv.getTree().addListener(SWT.MouseMove, treeListener);
		tv.getTree().addListener(SWT.MouseHover, treeListener);

	}

	private String getToolTip(TreeItem item) {
		Object obj = item.getData();
		if (obj instanceof TreeData) {
			Object data = ((TreeData) item.getData()).getWrappedObject();
			if (data instanceof Field) {
				return ((Field) data).getName() + " : " //$NON-NLS-1$
						+ ClassParser.getTypeLabel(((Field) data).getGenericType());
			}
			if (data instanceof Method) {
				return ((Method) data).getName() + "(" //$NON-NLS-1$
						+ ClassParser.getParametersLabel((Method) data) + ")" //$NON-NLS-1$
						+ " : " + ClassParser.getTypeLabel(((Method) data).getGenericReturnType()); //$NON-NLS-1$
			}
			return item.getText();
		}
		return null;
	}
}
