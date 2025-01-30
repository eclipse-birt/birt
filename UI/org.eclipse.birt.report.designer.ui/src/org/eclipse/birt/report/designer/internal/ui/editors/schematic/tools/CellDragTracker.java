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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.birt.report.designer.internal.ui.editors.parts.TableCellSelectionHelper;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.AbstractTableEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.IDelaySelectionDragTracker;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableUtil;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * Table Cell drag track
 * </p>
 *
 *
 */
public class CellDragTracker extends DragEditPartsTracker implements IDelaySelectionDragTracker {

	/**
	 * Creates a new CellTracker, with the CROSS cursor
	 *
	 * @param sourceEditPart
	 */
	public CellDragTracker(EditPart sourceEditPart) {
		super(sourceEditPart);
		setDefaultCursor(SharedCursors.CROSS);
		setUnloadWhenFinished(false);
	}

	/*
	 * Overrides the method, do nothing (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.AbstractTool#handleFinished()
	 */
	@Override
	protected void handleFinished() {
	}

	static final int TOGGLE_MODE = 1;

	static final int APPEND_MODE = 2;

	private int mode;

	private Figure marqueeRectangleFigure;

	// private List allChildren = new ArrayList( );
	private List selectedEditParts;

	private ChangeBoundsRequest targetRequest;

	public static final ChangeBoundsRequest MARQUEE_REQUEST = new ChangeBoundsRequest(RequestConstants.REQ_SELECTION);

	private List calculateNewSelection() {

		List newSelections = new ArrayList();

		TableUtil.calculateNewSelection(getMarqueeSelectionRectangle(), newSelections, getAllChildren());
		int size = newSelections.size();
		TableUtil.calculateNewSelection(TableUtil.getUnionBounds(newSelections), newSelections, getAllChildren());
		while (size != newSelections.size()) {
			size = newSelections.size();
			TableUtil.calculateNewSelection(TableUtil.getUnionBounds(newSelections), newSelections, getAllChildren());

		}
		return newSelections;
	}

	@Override
	protected ChangeBoundsRequest createTargetRequest() {
		return MARQUEE_REQUEST;
	}

	/**
	 * Erases feedback if necessary and puts the tool into the terminal state.
	 */
	@Override
	public void deactivate() {
		if (isInState(STATE_DRAG_IN_PROGRESS)) {
			eraseMarqueeFeedback();
			eraseTargetFeedback();
		}
		super.deactivate();
		// allChildren = new ArrayList( );
		setState(STATE_TERMINAL);
	}

	private void eraseMarqueeFeedback() {
		if (marqueeRectangleFigure != null) {
			removeFeedback(marqueeRectangleFigure);
			marqueeRectangleFigure = null;
		}
	}

	@Override
	protected void eraseTargetFeedback() {
		if (selectedEditParts == null) {
			return;
		}
		ListIterator oldEditParts = selectedEditParts.listIterator();
		while (oldEditParts.hasNext()) {
			EditPart editPart = (EditPart) oldEditParts.next();
			editPart.eraseTargetFeedback(getTargetRequest());
		}
	}

	/**
	 * Return a List including all of the children of the Table editPart
	 */
	private List getAllChildren() {
		return getSourceEditPart().getParent().getChildren();
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	@Override
	protected String getCommandName() {
		return REQ_SELECTION;
	}

	private IFigure getMarqueeFeedbackFigure() {
		if (marqueeRectangleFigure == null) {
			marqueeRectangleFigure = new MarqueeRectangleFigure();
			addFeedback(marqueeRectangleFigure);
		}
		return marqueeRectangleFigure;
	}

	private Rectangle getMarqueeSelectionRectangle() {
		return new Rectangle(getStartLocation(), getLocation());
	}

	private int getSelectionMode() {
		return mode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.TargetingTool#getTargetRequest()
	 */
	@Override
	protected ChangeBoundsRequest getTargetRequest() {
		if (targetRequest == null) {
			targetRequest = createTargetRequest();
		}
		return targetRequest;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonDown(int)
	 */
	@Override
	protected boolean handleButtonDown(int button) {
		boolean rlt = super.handleButtonDown(button);

		if (button == 1 && getCurrentInput().isShiftKeyDown()) {
			performShiftSelect();
		} else if (button == 1 && getCurrentInput().isControlKeyDown()) {
			performCtrlSelect();
		}

		return rlt;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
	 */
	@Override
	protected boolean handleButtonUp(int button) {
		if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
			eraseTargetFeedback();
			eraseMarqueeFeedback();
			performMarqueeSelect();
			return true;
		}

		boolean bool = super.handleButtonUp(button);
		handleFinished();

		return bool;
	}

	private void performCtrlSelect() {
		/**
		 * Does nothing now.
		 */
	}

	protected void performShiftSelect() {
		AbstractTableEditPart parent = (AbstractTableEditPart) getSourceEditPart().getParent();

		/**
		 * Checks viewer consistency.
		 */
		if (parent.getViewer() != getCurrentViewer()) {
			return;
		}

		ArrayList nlst;

		List slst = getCurrentViewer().getSelectedEditParts();

		if (slst != null && slst.contains(getSourceEditPart())) {
			nlst = new ArrayList();

			nlst.add(slst.get(0));
		} else if (slst != null) {
			nlst = new ArrayList(slst);
		} else {
			nlst = new ArrayList();
		}

		Rectangle constraint = TableCellSelectionHelper
				.getSelectionRectangle((AbstractCellEditPart) getSourceEditPart(), nlst);

		boolean refined = TableCellSelectionHelper.increaseSelectionRectangle(constraint, parent);

		while (refined) {
			refined = TableCellSelectionHelper.increaseSelectionRectangle(constraint, parent);
		}

		List lst = TableCellSelectionHelper.getRectangleSelection(constraint, parent);

		if (lst == null || lst.size() == 0) {
			return;
		}

		getCurrentViewer().setSelection(new StructuredSelection(lst));

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.SelectEditPartTracker#performSelection()
	 */
	@Override
	protected void performSelection() {
		if (hasSelectionOccurred()) {
			return;
		}

		/**
		 * Hacks the old selection algorithm, checks the consistency of parents of
		 * selected objects.
		 */
		if (getCurrentInput().isControlKeyDown() || getCurrentInput().isShiftKeyDown()) {
			setFlag(FLAG_SELECTION_PERFORMED, true);
			EditPartViewer viewer = getCurrentViewer();
			List selectedObjects = viewer.getSelectedEditParts();

			boolean consist = true;

			EditPart sourceParent = getSourceEditPart().getParent();

			for (Iterator itr = selectedObjects.iterator(); itr.hasNext();) {
				EditPart part = (EditPart) itr.next();

				if (part.getParent() != sourceParent) {
					consist = false;
					break;
				}
			}

			if (consist) {
				if (getCurrentInput().isControlKeyDown()) {
					/**
					 * Does nothing, leaves it to performCtrlSelect().
					 */
					return;
				} else if (getCurrentInput().isShiftKeyDown()) {
					/**
					 * Does nothing, leaves it to performShiftSelect().
					 */
					return;
				}
			}

			viewer.select(getSourceEditPart());

			return;
		}

		super.performSelection();
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
	 */
	@Override
	public boolean handleDragInProgress() {
		if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
			showMarqueeFeedback();
			eraseTargetFeedback();
			selectedEditParts = calculateNewSelection();
			showTargetFeedback();
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleFocusLost()
	 */
	@Override
	protected boolean handleFocusLost() {
		if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
			handleFinished();
			return true;
		}
		return false;
	}

	/**
	 * This method is called when mouse or keyboard input is invalid and erases the
	 * feedback.
	 *
	 * @return <code>true</code>
	 */
	@Override
	protected boolean handleInvalidInput() {
		eraseTargetFeedback();
		eraseMarqueeFeedback();
		return true;
	}

	/**
	 * Handles high-level processing of a key down event. KeyEvents are forwarded to
	 * the current viewer's {@link KeyHandler}, via
	 * {@link KeyHandler#keyPressed(KeyEvent)}.
	 *
	 * @see AbstractTool#handleKeyDown(KeyEvent)
	 */
	@Override
	protected boolean handleKeyDown(KeyEvent e) {
		if (super.handleKeyDown(e) || (getCurrentViewer().getKeyHandler() != null && getCurrentViewer().getKeyHandler().keyPressed(e))) {
			return true;
		}
		return false;
	}

	private void performMarqueeSelect() {
		EditPartViewer viewer = getCurrentViewer();

		List newSelections = calculateNewSelection();

		// If in multiple select mode, add the new selections to the already
		// selected group; otherwise, clear the selection and select the new
		// group
		// System.out.println(getSelectionMode());
		if (getSelectionMode() == APPEND_MODE) {
			for (int i = 0; i < newSelections.size(); i++) {
				EditPart editPart = (EditPart) newSelections.get(i);
				viewer.appendSelection(editPart);
			}
		} else if (getSelectionMode() == TOGGLE_MODE) {
			List selected = new ArrayList(viewer.getSelectedEditParts());
			for (int i = 0; i < newSelections.size(); i++) {
				EditPart editPart = (EditPart) newSelections.get(i);
				if (editPart.getSelected() != EditPart.SELECTED_NONE) {
					selected.remove(editPart);
				} else {
					selected.add(editPart);
				}
			}
			viewer.setSelection(new StructuredSelection(selected));
		} else {
			viewer.setSelection(new StructuredSelection(newSelections));
		}
	}

	/**
	 * @see org.eclipse.gef.Tool#setViewer(org.eclipse.gef.EditPartViewer)
	 */
	@Override
	public void setViewer(EditPartViewer viewer) {
		if (viewer == getCurrentViewer()) {
			return;
		}
		super.setViewer(viewer);
		if (viewer instanceof GraphicalViewer) {
			setDefaultCursor(SharedCursors.CROSS);
		} else {
			setDefaultCursor(SharedCursors.NO);
		}
	}

	private void showMarqueeFeedback() {
		Rectangle rect = getMarqueeSelectionRectangle().getCopy();
		getMarqueeFeedbackFigure().translateToRelative(rect);
		getMarqueeFeedbackFigure().setBounds(rect);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.tools.TargetingTool#showTargetFeedback()
	 */
	@Override
	protected void showTargetFeedback() {
		for (int i = 0; i < selectedEditParts.size(); i++) {
			EditPart editPart = (EditPart) selectedEditParts.get(i);
			editPart.showTargetFeedback(getTargetRequest());
		}
	}

	private static class MarqueeRectangleFigure extends Figure {

		private int offset = 0;

		private boolean schedulePaint = true;

		private static final int DELAY = 110; // animation delay in millisecond

		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		@Override
		protected void paintFigure(Graphics graphics) {
			Rectangle bounds = getBounds().getCopy();
			graphics.translate(getLocation());

			graphics.setXORMode(true);
			graphics.setForegroundColor(ColorConstants.white);
			graphics.setBackgroundColor(ColorConstants.black);

			graphics.setLineStyle(Graphics.LINE_DOT);

			int[] points = new int[6];

			points[0] = 0 + offset;
			points[1] = 0;
			points[2] = bounds.width - 1;
			points[3] = 0;
			points[4] = bounds.width - 1;
			points[5] = bounds.height - 1;

			graphics.drawPolyline(points);

			points[0] = 0;
			points[1] = 0 + offset;
			points[2] = 0;
			points[3] = bounds.height - 1;
			points[4] = bounds.width - 1;
			points[5] = bounds.height - 1;

			graphics.drawPolyline(points);

			graphics.translate(getLocation().getNegated());

			if (schedulePaint) {
				Display.getCurrent().timerExec(DELAY, new Runnable() {

					@Override
					public void run() {
						offset++;
						if (offset > 5) {
							offset = 0;
						}

						schedulePaint = true;
						repaint();
					}
				});
			}

			schedulePaint = false;
		}

	}

	@Override
	public void setStartLocation(Point p) {
		super.setStartLocation(p);
	}

	@Override
	public void setState(int state) {
		super.setState(state);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * IDelaySelectionDragTracker#setLocation(org.eclipse.draw2d.geometry.Point)
	 */
	@Override
	public void setLocation(Point p) {
		getCurrentInput().setMouseLocation(p.x, p.y);
	}

	@Override
	public EditPart getSourceEditPart() {
		return super.getSourceEditPart();
	}
}
