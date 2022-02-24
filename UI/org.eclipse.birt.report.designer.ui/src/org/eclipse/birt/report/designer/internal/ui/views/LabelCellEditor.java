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

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;

/**
 * A cell editor that manages a text entry field. The cell editor's value is the
 * text string itself.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 */
public class LabelCellEditor extends TextCellEditor {

	/**
	 * Creates a new text string cell editor with no control The cell editor value
	 * is the string itself, which is initially the empty string. Initially, the
	 * cell editor has no cell validator.
	 * 
	 */
	public LabelCellEditor() {
		super();
	}

	/**
	 * Creates a new text string cell editor parented under the given control. The
	 * cell editor value is the string itself, which is initially the empty string.
	 * Initially, the cell editor has no cell validator.
	 * 
	 * @param parent the parent control
	 */
	public LabelCellEditor(Composite parent) {
		super(parent);
	}

	/**
	 * Creates a new text string cell editor parented under the given control. The
	 * cell editor value is the string itself, which is initially the empty string.
	 * Initially, the cell editor has no cell validator.
	 * 
	 * @param parent the parent control
	 * @param style  the style bits
	 */
	public LabelCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	/**
	 * Processes a key release event that occurred in this cell editor.
	 * <p>
	 * The <code>TextCellEditor</code> implementation of this framework method
	 * ignores when the RETURN key is pressed since this is handled in
	 * <code>handleDefaultSelection</code>. An exception is made for Ctrl+Enter for
	 * multi-line texts, since a default selection event is not sent in this case.
	 * </p>
	 * 
	 * @param keyEvent the key event
	 */
	protected void keyReleaseOccured(KeyEvent keyEvent) {
		if (keyEvent.character == '\r') { // Return key
											// Enter is handled in handleDefaultSelection.
											// Do not apply the editor value in response to an Enter key event
											// since this can be received from the IME when the intent is -not-
											// to apply the value.
											// See bug 39074 [CellEditors] [DBCS] canna input mode fires bogus
											// event from Text Control
											//
											// An exception is made for Ctrl+Enter for multi-line texts, since
											// a default selection event is not sent in this case.
			if (text != null && !text.isDisposed() && (text.getStyle() & SWT.MULTI) != 0) {
				if ((keyEvent.stateMask & SWT.CTRL) == 0) {
					leaveKeyRelease(keyEvent);
				}
			}
			return;
		}
		leaveKeyRelease(keyEvent);
	}

	private void leaveKeyRelease(KeyEvent keyEvent) {
		if (keyEvent.character == '\u001b') { // Escape character
			fireCancelEditor();
		} else if (keyEvent.character == '\r') { // Return key
			fireApplyEditorValue();
			deactivate();
		}

	}
}
