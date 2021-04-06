/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.parts;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractCellEditPart;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.action.IAction;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

/**
 * Provides key events for report viewer.
 * <p>
 * COPY, PASTE, UNDO, REDO, DELETE functions are provided currently.
 * <p>
 */

public class ReportViewerKeyHandler extends GraphicalViewerKeyHandler {

	private ActionRegistry actionRegistry;

	private TableCellKeyDelegate tableDelgate;

	final static public int NO_MASK = 0;

	/**
	 * Constructor of KeyHandler
	 * 
	 * @param viewer
	 * @param actionRegistry
	 */
	public ReportViewerKeyHandler(GraphicalViewer viewer, ActionRegistry actionRegistry) {
		super(viewer);
		this.actionRegistry = actionRegistry;

		put(KeyStroke.getPressed(SWT.F2, 0), actionRegistry.getAction(GEFActionConstants.DIRECT_EDIT));
		tableDelgate = new TableCellKeyDelegate(viewer, actionRegistry);
	}

	/**
	 * Bounds actions with key events
	 * 
	 * @param character
	 * @param keyCode
	 * @param stateMask
	 * @param actionID
	 */
	public void put(char character, int keyCode, int stateMask, String actionID) {
		IAction action = actionRegistry.getAction(actionID);
		if (action != null) {
			put(KeyStroke.getReleased(character, keyCode, stateMask), action);
		}
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler#keyPressed(org.eclipse.swt.events.KeyEvent)
	 */
	public boolean keyPressed(KeyEvent event) {
		GraphicalEditPart part = getFocusEditPart();

		switch (event.keyCode) {
		case SWT.ARROW_LEFT:

		case SWT.ARROW_RIGHT:

		case SWT.ARROW_UP:

		case SWT.ARROW_DOWN:
			if (scrollIncrement(part, event)) {
				return true;
			}
			break;

		case SWT.PAGE_DOWN:

		case SWT.PAGE_UP:
			if (scrollPageIncrement(part, event.keyCode)) {
				return true;
			}
			break;
		default:
			break;
		}
		/**
		 * Hacks Table Cell key behaviors.
		 */
		if (part instanceof AbstractCellEditPart) {
			return tableCellKeyPressed(event);
		}

		return super.keyPressed(event);
	}

	private boolean scrollIncrement(GraphicalEditPart part, KeyEvent event) {
		if ((event.stateMask & SWT.CONTROL) == 0) {
			return false;
		}
		if (!(part.getViewer() instanceof DeferredGraphicalViewer)) {
			return false;
		}
		DeferredGraphicalViewer viewer = (DeferredGraphicalViewer) part.getViewer();
		FigureCanvas canvas = viewer.getFigureCanvas();
		int code = event.keyCode;
		int increment = 0;
		if (code == SWT.ARROW_RIGHT) {
			increment = canvas.getHorizontalBar().getSelection() + canvas.getHorizontalBar().getIncrement();
		} else if (code == SWT.ARROW_LEFT) {
			increment = canvas.getHorizontalBar().getSelection() - canvas.getHorizontalBar().getIncrement();
		} else if (code == SWT.ARROW_DOWN) {
			increment = canvas.getVerticalBar().getSelection() + canvas.getVerticalBar().getIncrement();
		} else if (code == SWT.ARROW_UP) {
			increment = canvas.getVerticalBar().getSelection() - canvas.getVerticalBar().getIncrement();
		}

		if (code == SWT.ARROW_RIGHT || code == SWT.ARROW_LEFT) {
			canvas.scrollToX(increment);
		}
		if (code == SWT.ARROW_UP || code == SWT.ARROW_DOWN) {
			canvas.scrollToY(increment);
		}
		return true;

	}

	private boolean scrollPageIncrement(GraphicalEditPart part, int code) {
		if (!(part.getViewer() instanceof DeferredGraphicalViewer)) {
			return false;
		}
		DeferredGraphicalViewer viewer = (DeferredGraphicalViewer) part.getViewer();
		FigureCanvas canvas = viewer.getFigureCanvas();

		int increment = 0;
		if (code == SWT.PAGE_DOWN) {
			increment = canvas.getVerticalBar().getSelection() + canvas.getVerticalBar().getPageIncrement();
		} else if (code == SWT.PAGE_UP) {
			increment = canvas.getVerticalBar().getSelection() - canvas.getVerticalBar().getPageIncrement();
		}
		canvas.scrollToY(increment);
		return true;
	}

	protected boolean tableCellKeyPressed(KeyEvent event) {
		return tableDelgate.keyPressed(event);
	}
}