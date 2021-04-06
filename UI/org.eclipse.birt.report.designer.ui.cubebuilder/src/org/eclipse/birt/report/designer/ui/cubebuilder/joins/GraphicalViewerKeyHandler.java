/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.joins;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.ui.cubebuilder.joins.action.DeleteJoinAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.joins.editparts.JoinConditionEditPart;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

/**
 * 
 * The handler for Key events on the Join Page. Whenever the join page has the
 * focus, any key event will be sent to this class. The events supported include
 * Deleting a Join, Deleting a Table.
 *
 */
public class GraphicalViewerKeyHandler extends KeyHandler {

	private EditPart selctedEditPart;
	private ScrollingGraphicalViewer viewer;

	/**
	 * @param viewer
	 */
	public GraphicalViewerKeyHandler(ScrollingGraphicalViewer viewer) {
		super();
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.KeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public boolean keyPressed(KeyEvent event) {
		List selectedParts = viewer.getSelectedEditParts();
		if (selectedParts != null && selectedParts.size() > 0) {
			Iterator editPartsIterator = selectedParts.iterator();
			EditPart selectedEditPart = (EditPart) editPartsIterator.next();
			if (selectedEditPart instanceof JoinConditionEditPart) {
				if (event.keyCode == SWT.DEL) {
					DeleteJoinCondition((JoinConditionEditPart) selectedEditPart);
				}
			}
		}
		return super.keyPressed(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.KeyHandler#keyReleased(org.eclipse.swt.events.KeyEvent)
	 */
	public boolean keyReleased(KeyEvent event) {
		return super.keyReleased(event);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.KeyHandler#put(org.eclipse.gef.KeyStroke,
	 * org.eclipse.jface.action.IAction)
	 */
	public void put(KeyStroke keystroke, IAction action) {
		super.put(keystroke, action);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.KeyHandler#remove(org.eclipse.gef.KeyStroke)
	 */
	public void remove(KeyStroke keystroke) {
		super.remove(keystroke);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.KeyHandler#setParent(org.eclipse.gef.KeyHandler)
	 */
	public KeyHandler setParent(KeyHandler parent) {
		return super.setParent(parent);
	}

	/**
	 * @return Returns the selctedEditPart.
	 */
	public EditPart getSelctedEditPart() {
		return selctedEditPart;
	}

	/**
	 * @param selctedEditPart The selctedEditPart to set.
	 */
	public void setSelctedEditPart(EditPart selctedEditPart) {
		this.selctedEditPart = selctedEditPart;
	}

	/**
	 * @param selectedEditPart
	 */
	private void DeleteJoinCondition(JoinConditionEditPart selectedEditPart) {
		DeleteJoinAction deleteAction = selectedEditPart.getRemoveAction();
		deleteAction.run();
	}

}