/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;

/**
 * Supports dragging elements in outline view.
 */

public abstract class DesignElementDragAdapter extends DragSourceAdapter {

	private StructuredViewer viewer;

	// added this member to fix bug 116180
	protected List selectionList = new ArrayList();

	/**
	 * Constructor
	 * 
	 * @param viewer
	 */
	public DesignElementDragAdapter(StructuredViewer viewer) {
		super();
		this.viewer = viewer;
	}

	/**
	 * @see DragSourceAdapter#dragFinished(DragSourceEvent)
	 */
	public void dragFinished(DragSourceEvent event) {
		if (event.doit) {
			TemplateTransfer.getInstance().setTemplate(null);
			if (Policy.TRACING_DND_DRAG) {
				System.out.println("DND >> Drag finished."); //$NON-NLS-1$
			}
		}
	}

	/**
	 * @see DragSourceAdapter#dragSetData(DragSourceEvent)
	 */
	public void dragSetData(DragSourceEvent event) {
//		IStructuredSelection selection = (IStructuredSelection) getViewer( ).getSelection( );
//		Object[] objects = selection.toList( ).toArray( );

		// fix bug 116180
		Object[] objects = selectionList.toArray();
		if (TemplateTransfer.getInstance().isSupportedType(event.dataType))
			event.data = objects;
	}

	/**
	 * @see DragSourceAdapter#dragStart(DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		boolean doit = !getViewer().getSelection().isEmpty();
		if (doit) {
			IStructuredSelection selection = (IStructuredSelection) getViewer().getSelection();
			selectionList = selection.toList();
			Object[] objects = selection.toList().toArray();
			if (validateType(objects)) {
				for (int i = 0; i < objects.length; i++)
					if (!validateTransfer(objects[i])) {
						doit = false;
						break;
					}
			} else
				doit = false;
			if (doit)
				TemplateTransfer.getInstance().setTemplate(objects);
		}
		event.doit = doit;
		if (Policy.TRACING_DND_DRAG && doit) {
			System.out.println("DND >> Drag starts."); //$NON-NLS-1$
		}
	}

	/**
	 * Validates every transfer element
	 * 
	 * @param transfer every transfer element
	 * @return if transfer element can be dragged
	 */
	protected abstract boolean validateTransfer(Object transfer);

	/**
	 * Validates types of transfer elements.
	 * <p>
	 * Default implementation is verify all types of transfer elements are same
	 * 
	 * @param transfer transfer elements
	 * @return type is same or not
	 */
	protected boolean validateType(Object transfer) {
		Object[] objects = (Object[]) transfer;
		if (objects.length <= 0) {
			return false;
		}

		// Theme can be draged when only one is selected. Fix bug #151953
		if (objects[0] instanceof ThemeHandle) {
			if (objects.length == 1) {
				return true;
			} else {
				return false;
			}
		}

		// Drag the elements if selected ones are the same type.
		Class type = null;
		for (int i = 0; i < objects.length; i++) {
			if (type == null)
				type = objects[i].getClass();
			else if (!type.equals(objects[i].getClass()))
				return false;
		}
		return true;
	}

	/**
	 * Returns viewer
	 * 
	 * @return viewer
	 */
	protected StructuredViewer getViewer() {
		return viewer;
	}
}