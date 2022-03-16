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

package org.eclipse.birt.report.designer.internal.ui.views;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.birt.report.designer.internal.ui.views.actions.EditAction;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RenameAction;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The listeners for rename action
 */

public class RenameListener extends KeyAdapter implements MouseListener, SelectionListener, IDoubleClickListener {

	private TreeViewer sourceViewer;

	/**
	 * selection cached
	 */
	private TreeItem selectedItem = null;

	private Timer timer;

	private boolean readyToRename;

	public RenameListener(TreeViewer sourceViewer) {
		this.sourceViewer = sourceViewer;
	}

	public void apply() {
		sourceViewer.getTree().addSelectionListener(this);
		sourceViewer.getTree().addKeyListener(this);
		// sourceViewer.getTree( ).addMouseListener( this );
		sourceViewer.addDoubleClickListener(this);
	}

	public void remove() {
		sourceViewer.getTree().removeSelectionListener(this);
		sourceViewer.getTree().removeKeyListener(this);
		// sourceViewer.getTree( ).removeMouseListener( this );
		sourceViewer.removeDoubleClickListener(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.MouseListener#mouseDoubleClick(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseDoubleClick(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.MouseListener#mouseDown(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseDown(MouseEvent e) {// prevent from conflicts
		cancelTimer();
		if (e.button != 1) {
			cancelRenaming();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.
	 * MouseEvent)
	 */
	@Override
	public void mouseUp(MouseEvent e) {
		if (!readyToRename) {
			return;
		}
		readyToRename = false;

		// selection doesn't change

		timer = new Timer();
		final RenameAction renameAction = new RenameAction(sourceViewer);
		timer.schedule(new TimerTask() {

			@Override
			public void run() {// Do rename
				sourceViewer.getTree().getDisplay().asyncExec(new Runnable() {

					@Override
					public void run() {
						renameAction.run();
					}
				});
			}
			// wait for double time to check if it is a double click
		}, Display.getCurrent().getDoubleClickTime() + 100);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.KeyListener#keyReleased(org.eclipse.swt.events.
	 * KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		cancelTimer();
		if (e.keyCode == SWT.F2 && e.stateMask == 0) {
			if (selectedItem != null) {
				RenameAction action = new RenameAction(sourceViewer);
				if (action.isEnabled() && action.isHandled()) {
					action.run();
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(org.eclipse.swt.
	 * events.SelectionEvent)
	 */
	@Override
	public void widgetSelected(SelectionEvent e) {
		cancelTimer();
		TreeItem lastSelect = selectedItem;
		TreeItem[] selectedItems = ((Tree) e.getSource()).getSelection();
		if (selectedItems.length != 1) {// No selection or multiple selection
			readyToRename = false;
		} else {
			selectedItem = selectedItems[0];
			readyToRename = (selectedItem == lastSelect);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.
	 * swt.events.SelectionEvent)
	 */
	@Override
	public void widgetDefaultSelected(SelectionEvent e) {// Do nothing
	}

	/**
	 * Cancels the timer
	 */
	private void cancelTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	/**
	 * Cancels the inline rename action
	 */
	private void cancelRenaming() {
		RenameInlineTool.cancelActiveInstance();
		cancelTimer();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.
	 * viewers.DoubleClickEvent)
	 */
	@Override
	public void doubleClick(DoubleClickEvent event) {
		// perform edit
		cancelTimer();
		if (selectedItem != null && !selectedItem.isDisposed()) {// ignore multiple selection or invalid
																	// selection
			new EditAction(selectedItem.getData()).run();
		}
	}
}
