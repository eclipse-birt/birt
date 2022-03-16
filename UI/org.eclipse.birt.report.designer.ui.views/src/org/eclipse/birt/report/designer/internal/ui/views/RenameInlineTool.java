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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * The tool used to rename element inline
 */

public class RenameInlineTool {

	/**
	 * The tree item to rename
	 */
	private TreeItem selectedItem;

	private Tree tree;

	private TreeEditor treeEditor;

	private Composite textEditorParent;

	private Text textEditor = null;

	private String originalName;

	private static RenameInlineTool activeInstance = null;

	private static final String ERROR_TITLE = Messages.getString("RenameInlineTool.DialogTitle.RenameFailed"); //$NON-NLS-1$

	private static final String TRANS_LABEL = Messages.getString("RenameInlineTool.TransLabel.Rename"); //$NON-NLS-1$

	private boolean isDispatching = false;

	/**
	 * Constructor. Creates a new tool to renames element on the tree.
	 *
	 * @param item The tree item to rename on
	 */
	public RenameInlineTool(TreeItem item) {
		selectedItem = item;
		tree = item.getParent();
	}

	public void doRename() {
		if ((activeInstance != null && activeInstance != this) || (textEditor == null && !tree.isFocusControl())) {// the focus has lost
			return;
		}

		if (selectedItem.getData() instanceof DesignElementHandle
				|| selectedItem.getData() instanceof EmbeddedImageHandle) {
			if (textEditor == null) {
				initOriginalName();
				createTextEditor();
				activeInstance = this;
			}

			textEditor.setText(originalName);
			computeTextSize();

			// Open text editor with initial size.
			textEditorParent.setVisible(true);

			textEditor.selectAll();
			textEditor.setFocus();
		}
	}

	private void initOriginalName() {
		if (selectedItem.getData() instanceof DesignElementHandle) {
			originalName = ((DesignElementHandle) selectedItem.getData()).getQualifiedName();
		}
		if (selectedItem.getData() instanceof EmbeddedImageHandle) {
			originalName = ((EmbeddedImageHandle) selectedItem.getData()).getQualifiedName();
		}

		if (originalName == null) {
			originalName = ""; //$NON-NLS-1$
		}
	}

	private void createTextEditor() {
		// Create tree editor
		treeEditor = new TreeEditor(tree);
		treeEditor.horizontalAlignment = SWT.LEFT;
		treeEditor.grabHorizontal = true;
		treeEditor.minimumWidth = 40;

		// Create text editor parent. This draws a nice bounding.
		textEditorParent = new Composite(tree, SWT.NONE);
		textEditorParent.setVisible(false);
		treeEditor.setEditor(textEditorParent, selectedItem);
		final int inset = getCellEditorInset(textEditorParent);
		if (inset > 0) { // only register for paint events if we have a
			// border
			textEditorParent.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					Point textSize = textEditor.getSize();
					Point parentSize = textEditorParent.getSize();
					e.gc.drawRectangle(0, 0, Math.min(textSize.x + 4, parentSize.x - 1), parentSize.y - 1);
				}
			});
		}
		// Create inner text editor.
		textEditor = new Text(textEditorParent, SWT.NONE);
		textEditor.setFont(selectedItem.getFont());
		textEditorParent.setBackground(textEditor.getBackground());
		textEditor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				computeTextSize();
			}
		});
		textEditor.addTraverseListener(new TraverseListener() {

			@Override
			public void keyTraversed(TraverseEvent e) {
				switch (e.detail) {
				case SWT.TRAVERSE_ESCAPE:
					// Do nothing in this case
					close();
					e.doit = true;
					e.detail = SWT.TRAVERSE_NONE;
					if (tree != null) {
						tree.setFocus();
					}
					break;
				case SWT.TRAVERSE_RETURN:
					saveChangesAndClose();
					e.doit = true;
					e.detail = SWT.TRAVERSE_NONE;
					if (tree != null) {
						tree.setFocus();
					}
					break;
				}

			}
		});

		textEditor.addFocusListener(new FocusAdapter() {

			@Override
			public void focusLost(FocusEvent fe) {
				saveChangesAndClose();
			}
		});

	}

	/**
	 * Close the text widget and reset the editorText field.
	 */
	public void close() {
		if (textEditorParent != null) {
			textEditorParent.dispose();
			textEditorParent = null;
			textEditor = null;
			treeEditor.setEditor(null, null);
			activeInstance = null;
		}
	}

	public static void cancelActiveInstance() {
		if (activeInstance != null) {
			activeInstance.close();
		}
	}

	private void saveChangesAndClose() {
		if (textEditor == null || isDispatching) {
			// disposed
			return;
		}

		isDispatching = true;

		String newName = textEditor.getText().trim();

		if (!newName.equals(originalName)) {
			if (!rename(selectedItem.getData(), newName)) {
				// failed to rename, do again
				doRename();
				isDispatching = false;
				return;
			}
		}
		close();
		isDispatching = false;
	}

	/**
	 * Perform renaming
	 *
	 * @param handle  the handle of the element to rename
	 * @param newName the newName to set
	 * @return Returns true if perform successfully,or false if failed
	 */
	private boolean rename(Object handle, String newName) {
		if (newName.length() == 0) {
			newName = null;
		}
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(TRANS_LABEL + " " + DEUtil.getDisplayLabel(handle)); //$NON-NLS-1$
		try {
			if (handle instanceof DesignElementHandle) {
				((DesignElementHandle) handle).setName(newName);
			}

			if (handle instanceof EmbeddedImageHandle) {
				((EmbeddedImageHandle) handle).setName(newName);
			}
			stack.commit();
		} catch (NameException e) {
			ExceptionUtil.handle(e, ERROR_TITLE, e.getLocalizedMessage());
			stack.rollback();
			return false;
		} catch (SemanticException e) {
			ExceptionUtil.handle(e, ERROR_TITLE, e.getLocalizedMessage());
			stack.rollback();
			// If set EmbeddedImage name error, then use former name;
			return true;
		}
		return true;
	}

	/**
	 * On MAC the text widget already provides a border when it has focus, so there
	 * is no need to draw another one. The value of returned by this method is used
	 * to control the inset we apply to the text field bound's in order to get space
	 * for drawing a border. A value of 1 means a one-pixel wide border around the
	 * text field. A negative value supresses the border. However, in M9 the system
	 * property "org.eclipse.swt.internal.carbon.noFocusRing" has been introduced as
	 * a temporary workaround for bug #28842. The existence of the property turns
	 * the native focus ring off if the widget is contained in a main window (not
	 * dialog). The check for the property should be removed after a final fix for
	 * #28842 has been provided.
	 */
	private int getCellEditorInset(Control c) {
		if ("carbon".equals(SWT.getPlatform())) //$NON-NLS-1$
		{ // special case for MacOS X
			if (System.getProperty("org.eclipse.swt.internal.carbon.noFocusRing") == null //$NON-NLS-1$
					|| c.getShell().getParent() != null) {
				return -2; // native border
			}
		}
		return 1; // one pixel wide black border
	}

	private void computeTextSize() {
		int inset = getCellEditorInset(textEditorParent);
		Point textSize = textEditor.computeSize(SWT.DEFAULT, SWT.DEFAULT);

		textSize.x += textSize.y; // Add extra space for new characters.
		if (textSize.x < 20 || textEditor.getCharCount() == 0) {// minimal width
			textSize.x = 40;
		} else {
			textSize.x += 20;
		}

		Point parentSize = textEditorParent.getSize();
		textEditor.setBounds(2, inset, Math.min(textSize.x, parentSize.x - 4), parentSize.y - 2 * inset);
		textEditorParent.redraw();
	}

}
