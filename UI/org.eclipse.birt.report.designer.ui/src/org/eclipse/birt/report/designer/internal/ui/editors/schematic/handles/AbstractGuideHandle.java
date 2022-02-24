/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles;

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportElementDragTracker;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;

/**
 * The class is the all ReportelemenEditPart base class.
 * 
 */
public abstract class AbstractGuideHandle extends AbstractHandle implements MouseMotionListener {

	private boolean isInGuideHandle = false;
	private boolean canDeleteGuide = true;

	public AbstractGuideHandle(GraphicalEditPart owner, Locator loc) {
		super(owner, loc);
		addMouseMotionListener(this);
		getLocator().relocate(this);

	}

	public void mouseEntered(MouseEvent me) {
		// System.out.println( "handle enter" );
		isInGuideHandle = true;
		getGuideFeedBackHost().addGuideFeedBack();
	}

	public void mouseExited(MouseEvent me) {
		// System.out.println( "handle exit" );
		isInGuideHandle = false;
		getGuideFeedBackHost().delayRemoveGuideFeedBack();
	}

	public void mouseHover(MouseEvent me) {
		// System.out.println( "handle hover" );
		isInGuideHandle = true;
		getGuideFeedBackHost().addGuideFeedBack();
	}

	public void mouseMoved(MouseEvent me) {
		// System.out.println( "handle move" );
		isInGuideHandle = true;

		// addGuideFeedBack();
	}

	public void mouseDragged(MouseEvent me) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.AbstractHandle#createDragTracker()
	 */
	protected DragTracker createDragTracker() {
		return new ReportElementDragTracker(getOwner());
	}

	protected IGuideFeedBackHost getGuideFeedBackHost() {
		return (IGuideFeedBackHost) getOwner();
	}

	public boolean isInGuideHandle() {
		return isInGuideHandle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#findFigureAt(int, int,
	 * org.eclipse.draw2d.TreeSearch)
	 */
	public IFigure findFigureAt(int x, int y, TreeSearch search) {
		return super.findFigureAt(x, y, search);
	}

	/**
	 * @return Returns the canDeleteGuide.
	 */
	public boolean isCanDeleteGuide() {
		return canDeleteGuide;
	}

	/**
	 * @param canDeleteGuide The canDeleteGuide to set.
	 */
	public void setCanDeleteGuide(boolean canDeleteGuide) {
		this.canDeleteGuide = canDeleteGuide;
	}
}
