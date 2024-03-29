/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.core.ui.swt.custom;

import org.eclipse.jface.viewers.AbstractListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * TextComboViewer
 */
public class TextComboViewer extends AbstractListViewer implements Listener {

	private TextCombo combo;

	public TextComboViewer(TextCombo list) {
		this.combo = list;
		hookControl(list);

		list.addListener(TextCombo.SELECTION_EVENT, this);
	}

	@Override
	public void handleEvent(Event event) {
		if (event.type == TextCombo.SELECTION_EVENT) {
			SelectionChangedEvent sce = new SelectionChangedEvent(this,
					new StructuredSelection(getSelectionFromWidget()));
			fireSelectionChanged(sce);
		}
	}

	@Override
	protected void listAdd(String string, int index) {
		// combo.add( string, index );
	}

	@Override
	protected void listSetItem(int index, String string) {
		// combo.setItem( index, string );
	}

	@Override
	protected int[] listGetSelectionIndices() {
		int idx = combo.getChoiceIndex();

		if (idx < 0) {
			return new int[0];
		}

		return new int[] { idx };
	}

	@Override
	protected int listGetItemCount() {
		return combo.getItemCount();
	}

	@Override
	protected void listSetItems(String[] labels) {
		combo.setItems(labels);
	}

	@Override
	protected void listRemoveAll() {
		combo.setItems(null);
	}

	@Override
	protected void listRemove(int index) {
		// combo.remove( index );
	}

	/*
	 * (non-Javadoc) Method declared on Viewer.
	 */
	@Override
	public Control getControl() {
		return combo;
	}

	/*
	 * Do nothing -- combos only display the selected element, so there is no way we
	 * can ensure that the given element is visible without changing the selection.
	 * Method defined on StructuredViewer.
	 */
	@Override
	public void reveal(Object element) {
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.AbstractListViewer#listSetSelection(int[])
	 */
	@Override
	protected void listSetSelection(int[] ixs) {
		if (ixs != null && ixs.length > 0) {
			combo.select(ixs[0]);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.AbstractListViewer#listDeselectAll()
	 */
	@Override
	protected void listDeselectAll() {
		// combo.setChoiceValue( null );
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.viewers.AbstractListViewer#listShowSelection()
	 */
	@Override
	protected void listShowSelection() {
	}
}
