/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/
package org.eclipse.birt.report.designer.ui.widget;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

public class PopupSelectionList {

	protected Shell shell;
	protected List list;
	// private static int PADDING = 50;
	private String result = null;
	private int selectionIndex = -1;

	public PopupSelectionList(Shell parent) {
		shell = new Shell(parent, SWT.NONE);
		shell.setLayout(new FillLayout());
		shell.addShellListener(new ShellAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.swt.events.ShellAdapter#shellDeactivated(org.eclipse.swt.events.
			 * ShellEvent)
			 */
			public void shellDeactivated(ShellEvent e) {
				closeShell();
			}

		});
		list = new List(shell, SWT.SINGLE | SWT.V_SCROLL);
		list.addMouseListener(new MouseAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.MouseAdapter#mouseUp(org.eclipse.swt.events.
			 * MouseEvent)
			 */
			public void mouseUp(MouseEvent e) {
				closeShell();
			}

		});

		list.addKeyListener(new KeyAdapter() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.
			 * KeyEvent)
			 */
			public void keyPressed(KeyEvent e) {
				if (e.character == SWT.CR) {
					closeShell();
				}
			}

		});
	}

	public String open(Rectangle rect) {
		int maxWidth = getMaxStringWidth();
		if (rect.width > maxWidth) {
			maxWidth = rect.width;
		}

		Point listSize = list.computeSize(maxWidth, SWT.DEFAULT, false);
		Rectangle screenSize = shell.getDisplay().getBounds();

		// Position the dialog so that it does not run off the screen and the
		// largest number of items are visible
		int spaceBelow = screenSize.height - (rect.y + rect.height) - 30;
		int spaceAbove = rect.y - 30;

		int y = 0;
		if (spaceAbove > spaceBelow && listSize.y > spaceBelow) {
			// place popup list above table cell
			if (listSize.y > spaceAbove) {
				listSize.y = spaceAbove;
			} else {
				listSize.y += 2;
			}
			y = rect.y - listSize.y;

		} else {
			// place popup list below table cell
			if (listSize.y > spaceBelow) {
				listSize.y = spaceBelow;
			} else {
				listSize.y += 2;
			}
			y = rect.y + rect.height;
		}

		// Make dialog as wide as the cell
		listSize.x = maxWidth;

		// Align right side of dialog with right side of cell
		int x = rect.x + maxWidth - listSize.x;

		shell.setBounds(x, y, listSize.x, listSize.y);

		shell.open();
		list.setFocus();

		Display display = shell.getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		return result;
	}

	private int getMaxStringWidth() {
		GC gc = new GC(shell);
		String[] items = list.getItems();
		ArrayList separatorIndexes = new ArrayList(5);
		int maxChars = 0;
		int maxWidth = 0;
		Point pt = null;
		for (int n = 0; n < items.length; n++) {
			pt = gc.textExtent(items[n]);
			if (pt.x > maxWidth) {
				maxWidth = pt.x;
			}
			int len = items[n].length();
			if (len > maxChars) {
				maxChars = len;
			}
			if (items[n].equals("-")) //$NON-NLS-1$
			{
				separatorIndexes.add(Integer.valueOf(n));
			}

		}
		gc.dispose();
		String separator = getSeparator(maxChars);
		Iterator iter = separatorIndexes.iterator();
		while (iter.hasNext()) {
			list.setItem(((Integer) iter.next()).intValue(), separator);
		}

		return maxWidth + 5;
	}

	private void closeShell() {
		if (!shell.isDisposed()) {
			String[] strings = list.getSelection();
			if (strings.length != 0) {
				result = strings[0];
				selectionIndex = list.getSelectionIndex();
			}
			shell.dispose();
		}
	}

	private String getSeparator(int maxChars) {
		StringBuffer buf = new StringBuffer();
		for (int n = 0; n < maxChars; n++) {
			buf.append("_"); //$NON-NLS-1$
		}

		return buf.toString();
	}

	public void setItems(String[] items) {
		list.setItems(items);
	}

	public int getSelectionIndex() {
		return selectionIndex;
	}
}
