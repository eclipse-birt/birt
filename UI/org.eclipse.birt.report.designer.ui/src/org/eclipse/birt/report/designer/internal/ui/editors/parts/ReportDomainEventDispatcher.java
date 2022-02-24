/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation .
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import java.util.List;

import org.eclipse.draw2d.EventDispatcher;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.ui.parts.DomainEventDispatcher;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.TraverseEvent;

/**
 * add comment here
 * 
 */
public class ReportDomainEventDispatcher extends DomainEventDispatcher {

	private ReportFocusTraverseManager focusManager = new ReportFocusTraverseManager();

	/**
	 * @param d
	 * @param v
	 */
	public ReportDomainEventDispatcher(EditDomain d, EditPartViewer v) {
		super(d, v);
	}

	/**
	 * @see EventDispatcher#dispatchKeyTraversed(TraverseEvent)
	 */
	public void dispatchKeyTraversed(TraverseEvent e) {
		IFigure focusOwner = null;
		// List list = getViewer().getSelectedEditParts();
		List list = ((IStructuredSelection) getViewer().getSelection()).toList();
		int size = list.size();
		IFigure nextFigure = null;

		if (e.detail == SWT.TRAVERSE_TAB_NEXT) {
			AbstractGraphicalEditPart part = size == 0 ? null : (AbstractGraphicalEditPart) list.get(list.size() - 1);
			IFigure figure = part == null ? null : part.getFigure();
			focusOwner = getFocusOwner(figure);
			if ((e.stateMask & SWT.CTRL) > 0) {
				nextFigure = focusManager.getNextFocusableFigure(getRoot(), focusOwner);
			} else {
				nextFigure = focusManager.getNextFocusableFigureInSameOrder(getRoot(), focusOwner);
			}
		} else if (e.detail == SWT.TRAVERSE_TAB_PREVIOUS) {
			AbstractGraphicalEditPart part = size == 0 ? null : (AbstractGraphicalEditPart) list.get(0);
			IFigure figure = part == null ? null : part.getFigure();
			focusOwner = getFocusOwner(figure);

			if ((e.stateMask & SWT.CTRL) > 0) {
				nextFigure = focusManager.getPreviousFocusableFigure(getRoot(), focusOwner);
			} else {
				nextFigure = focusManager.getPreviousFocusableFigureInSameOrder(getRoot(), focusOwner);
			}

		}

		if (nextFigure != null) {
			e.doit = false;
			setFocus(nextFigure);

			Object obj = getViewer().getVisualPartMap().get(nextFigure);
			if (obj != null) {
				getViewer().setSelection(new StructuredSelection(obj));
			}
			if (obj instanceof EditPart) {
				getViewer().reveal((EditPart) obj);
			}
		}
	}

	private boolean isFocusEligible(IFigure fig) {
		if (fig == null || !fig.isFocusTraversable() || !fig.isShowing())
			return false;
		return true;
	}

	private IFigure getFocusOwner(IFigure fig) {
		if (isFocusEligible(fig)) {
			return fig;
		} else {
			return null;
		}
	}

}
