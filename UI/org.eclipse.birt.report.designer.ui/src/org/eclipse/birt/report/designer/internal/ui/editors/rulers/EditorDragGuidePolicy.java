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

package org.eclipse.birt.report.designer.internal.ui.editors.rulers;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

/**
 * The class is used for the EditorGuideEditPart EditPolicy.PRIMARY_DRAG_ROLE
 * policy
 *
 */
public class EditorDragGuidePolicy extends GraphicalEditPolicy {
	private static final String PIXELS_LABEL = Messages.getString("EditorDragGuidePolicy.pixels.label"); //$NON-NLS-1$
	private static final DecimalFormat FORMAT = new DecimalFormat("#0.000"); //$NON-NLS-1$
	private static final int DEFAULT_VALUE = 10;
	private static final int DISTANCE = 40;
	private static final Insets INSETS = new Insets(2, 4, 2, 4);
	private List attachedEditParts = null;
	private IFigure dummyGuideFigure, dummyLineFigure;
	private Label infoLabel;
	// private static final String PREFIX_LABEL = "Margin";
	private IChoiceSet choiceSet = ChoiceSetFactory.getElementChoiceSet(ReportDesignConstants.REPORT_DESIGN_ELEMENT,
			ReportDesignHandle.UNITS_PROP);
	private int maxWidth = -1;
	// private boolean dragInProgress = false;

	/**
	 * Creates the Line Figure, when drag the margin guide.
	 *
	 * @return
	 */
	protected IFigure createDummyLineFigure() {
		return new Figure() {

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
			 */
			@Override
			protected void paintFigure(Graphics graphics) {
				graphics.setLineStyle(Graphics.LINE_DOT);
				graphics.setXORMode(true);
				graphics.setForegroundColor(ColorConstants.darkGray);
				if (bounds.width > bounds.height) {
					graphics.drawLine(bounds.x, bounds.y, bounds.right(), bounds.y);
					graphics.drawLine(bounds.x + 2, bounds.y, bounds.right(), bounds.y);
				} else {
					graphics.drawLine(bounds.x, bounds.y, bounds.x, bounds.bottom());
					graphics.drawLine(bounds.x, bounds.y + 2, bounds.x, bounds.bottom());
				}
			}
		};
	}

	protected Label createInfoLabel() {
		Label labelFigure = new Label();

		labelFigure.setBorder(new MarginBorder(new Insets(0, 3, 0, 0)) {
			@Override
			public void paint(IFigure figure, Graphics graphics, Insets insets) {
				tempRect.setBounds(getPaintRectangle(figure, insets));
				if (getWidth() % 2 != 0) {
					tempRect.width--;
					tempRect.height--;
				}
				tempRect.shrink(getWidth() / 2, getWidth() / 2);
				graphics.setLineWidth(getWidth());

				graphics.drawRectangle(tempRect);
			}

			private int getWidth() {
				return 1;
			}
		});
		labelFigure.setLabelAlignment(PositionConstants.LEFT);
		labelFigure.setOpaque(true);
		labelFigure.setBackgroundColor(ReportColorConstants.TableGuideFillColor);
		return labelFigure;
	}

	/**
	 * Creates the Guide Figure, when drag the margin guide.
	 *
	 * @return
	 */
	protected EditorGuideFigure createDummyGuideFigure() {
		return new EditorGuidePlaceHolder(getGuideEditPart().isHorizontal());
	}

	/*
	 * If you undo guide creation while dragging that guide, it was leaving behind
	 * drag feedback. This was because by the time eraseSourceFeedback() was being
	 * called, the guide edit part had been deactivated (and hence
	 * eraseSourceFeedback is never called on this policy). So we make sure that
	 * this policy cleans up when it is deactivated.
	 */
	@Override
	public void deactivate() {
		removeFeedback();
		super.deactivate();
	}

	/**
	 * When drag the margin guide, the attache editparts move with the guide. Now do
	 * nothing
	 *
	 * @param request
	 */
	private void eraseAttachedPartsFeedback(Request request) {
		if (attachedEditParts != null) {
			ChangeBoundsRequest req = new ChangeBoundsRequest(request.getType());
			req.setEditParts(attachedEditParts);

			Iterator i = attachedEditParts.iterator();

			while (i.hasNext()) {
				((EditPart) i.next()).eraseSourceFeedback(req);
			}
			attachedEditParts = null;
		}
	}

	/*
	 * Erases the draw source feedback (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(org.eclipse.gef.Request)
	 */
	@Override
	public void eraseSourceFeedback(Request request) {
		getGuideEditPart().updateLocationOfFigures(getGuideEditPart().getZoomedPosition());
		getHostFigure().setVisible(true);
		getGuideEditPart().getGuideLineFigure().setVisible(true);
		removeFeedback();
		getGuideEditPart().setCurrentCursor(null);
		// dragInProgress = false;

		eraseAttachedPartsFeedback(request);

		maxWidth = -1;
	}

	private List getAttachedEditParts() {
		if (attachedEditParts == null) {
			attachedEditParts = getGuideEditPart().getRulerProvider().getAttachedEditParts(getHost().getModel(),
					((EditorRulerEditPart) getHost().getParent()).getDiagramViewer());
		}
		return attachedEditParts;
	}

	/*
	 * Gets the commande with specific request (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPolicy#getCommand(org.eclipse.gef.Request)
	 */
	@Override
	public Command getCommand(Request request) {
		Command cmd;
		final ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		if (isDeleteRequest(req)) {
			cmd = getGuideEditPart().getRulerProvider().getDeleteGuideCommand(getHost().getModel());
		} else {
			int pDelta;
			if (getGuideEditPart().isHorizontal()) {
				pDelta = req.getMoveDelta().y;
			} else {
				pDelta = req.getMoveDelta().x;
			}
			if (isMoveValid(getGuideEditPart().getZoomedPosition() + pDelta)) {
				ZoomManager zoomManager = getGuideEditPart().getZoomManager();
				if (zoomManager != null) {
					pDelta = (int) Math.round(pDelta / zoomManager.getZoom());
				}
				cmd = getGuideEditPart().getRulerProvider().getMoveGuideCommand(getHost().getModel(), pDelta);
			} else {
				cmd = UnexecutableCommand.INSTANCE;
			}
		}
		return cmd;
	}

	/**
	 * Creates the Guide Figure, when drag the margin guide.
	 *
	 * @return
	 */
	protected IFigure getDummyGuideFigure() {
		if (dummyGuideFigure == null) {
			dummyGuideFigure = createDummyGuideFigure();
		}
		return dummyGuideFigure;
	}

	/**
	 * Gets the line figure when drag the margin guide
	 *
	 * @return
	 */
	protected IFigure getDummyLineFigure() {
		if (dummyLineFigure == null) {
			dummyLineFigure = createDummyLineFigure();
		}
		return dummyLineFigure;
	}

	protected IFigure getInfoLabel() {
		if (infoLabel == null) {
			infoLabel = createInfoLabel();
		}
		return infoLabel;
	}

	/**
	 * Gets the GuideEditPart
	 *
	 * @return
	 */
	protected EditorGuideEditPart getGuideEditPart() {
		return (EditorGuideEditPart) getHost();
	}

	/**
	 * Now return false
	 *
	 * @param req
	 * @return if the darg is delete the margin guide
	 */
	protected boolean isDeleteRequest(ChangeBoundsRequest req) {
		return false;
	}

	/**
	 * Now return true
	 *
	 * @param zoomedPosition
	 * @return return true if the drag is valid.
	 */
	protected boolean isMoveValid(int zoomedPosition) {
		return true;
	}

	private void removeFeedback() {
		if (getDummyGuideFigure().getParent() != null) {
			getDummyGuideFigure().getParent().remove(getDummyGuideFigure());
		}
		if (getDummyLineFigure().getParent() != null) {
			getDummyLineFigure().getParent().remove(getDummyLineFigure());
		}
		if (getInfoLabel().getParent() != null) {
			getInfoLabel().getParent().remove(getInfoLabel());
		}
	}

	private void showAttachedPartsFeedback(ChangeBoundsRequest request) {
		ChangeBoundsRequest req = new ChangeBoundsRequest(request.getType());
		req.setEditParts(getAttachedEditParts());

		if (getGuideEditPart().isHorizontal()) {
			req.setMoveDelta(new Point(0, request.getMoveDelta().y));
		} else {
			req.setMoveDelta(new Point(request.getMoveDelta().x, 0));
		}

		Iterator i = getAttachedEditParts().iterator();

		while (i.hasNext()) {
			((EditPart) i.next()).showSourceFeedback(req);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(org.eclipse.gef.Request)
	 */
	@Override
	public void showSourceFeedback(Request request) {
		ChangeBoundsRequest req = (ChangeBoundsRequest) request;

		// add the placeholder guide figure to the ruler
		getHostFigure().getParent().add(getDummyGuideFigure(), 0);
		((GraphicalEditPart) getHost().getParent()).setLayoutConstraint(getHost(), getDummyGuideFigure(),
				Integer.valueOf(getGuideEditPart().getZoomedPosition()));
		getDummyGuideFigure().setBounds(getHostFigure().getBounds());
		// add the invisible placeholder line figure to the primary viewer
		getGuideEditPart().getGuideLayer().add(getDummyLineFigure(), 0);
		getGuideEditPart().getGuideLayer().setConstraint(getDummyLineFigure(),
				Boolean.valueOf(getGuideEditPart().isHorizontal()));
		// getDummyLineFigure( ).setBounds(
		// getGuideEditPart( ).getGuideLineFigure( ).getBounds( ) );
		getDummyLineFigure().setBounds(getDummyLineFigureBounds(req));
		// add the info label
		getGuideEditPart().getGuideLayer().add(getInfoLabel(), 0);
//		getGuideEditPart( ).getGuideLayer( ).setConstraint(
//				getInfoLabel( ),
//				Boolean.valueOf( getGuideEditPart( ).isHorizontal( ) ) );

		updateInfomation(getShowLable(req));

		// move the guide being dragged to the last index so that it's drawn
		// on
		// top of other guides
		List children = getHostFigure().getParent().getChildren();
		children.remove(getHostFigure());
		children.add(getHostFigure());

		if (isDeleteRequest(req)) {
			getHostFigure().setVisible(false);
			getGuideEditPart().getGuideLineFigure().setVisible(false);
			getGuideEditPart().setCurrentCursor(SharedCursors.ARROW);
			eraseAttachedPartsFeedback(request);
		} else {
			int newPosition;
			if (getGuideEditPart().isHorizontal()) {
				newPosition = getGuideEditPart().getZoomedPosition() + req.getMoveDelta().y;
			} else {
				newPosition = getGuideEditPart().getZoomedPosition() + req.getMoveDelta().x;
			}
			getHostFigure().setVisible(true);
			getGuideEditPart().getGuideLineFigure().setVisible(true);
			if (isMoveValid(newPosition)) {
				getGuideEditPart().setCurrentCursor(null);
				getGuideEditPart().updateLocationOfFigures(newPosition);
				showAttachedPartsFeedback(req);
			} else {
				getGuideEditPart().setCurrentCursor(SharedCursors.NO);
				getGuideEditPart().updateLocationOfFigures(getGuideEditPart().getZoomedPosition());
				eraseAttachedPartsFeedback(request);
			}
		}
	}

	private EditorRulerEditPart getRulerEditPart() {
		return (EditorRulerEditPart) getHost().getParent();
	}

	private Rectangle getDummyLineFigureBounds(ChangeBoundsRequest request) {
		Rectangle bounds = new Rectangle();
		EditorRulerEditPart source = getRulerEditPart();
		if (source.isHorizontal()) {
			bounds.x = getCurrentPositionZoomed(request);
			bounds.y = source.getGuideLayer().getBounds().y;
			bounds.width = 1;
			bounds.height = source.getGuideLayer().getBounds().height;
		} else {
			bounds.x = source.getGuideLayer().getBounds().x;
			bounds.y = getCurrentPositionZoomed(request);
			bounds.width = source.getGuideLayer().getBounds().width;
			bounds.height = 1;
		}
		return bounds;
	}

	private int getCurrentPositionZoomed(ChangeBoundsRequest request) {

		int newPosition;
		if (getGuideEditPart().isHorizontal()) {
			newPosition = getGuideEditPart().getZoomedPosition() + request.getMoveDelta().y;
		} else {
			newPosition = getGuideEditPart().getZoomedPosition() + request.getMoveDelta().x;
		}
		return newPosition;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPolicy#understandsRequest(org.eclipse.gef.Request)
	 */
	@Override
	public boolean understandsRequest(Request req) {
		return req.getType().equals(REQ_MOVE);
	}

	private String getShowLable(ChangeBoundsRequest req) {
		int pDelta;
		if (getGuideEditPart().isHorizontal()) {
			pDelta = req.getMoveDelta().y;
		} else {
			pDelta = req.getMoveDelta().x;
		}

		ZoomManager zoomManager = getGuideEditPart().getZoomManager();
		if (zoomManager != null) {
			pDelta = (int) Math.round(pDelta / zoomManager.getZoom());
		}

		int marginValue = ((EditorRulerProvider) getGuideEditPart().getRulerProvider())
				.getMarginValue(getHost().getModel(), pDelta);

		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();
//		MasterPageHandle page = SessionHandleAdapter.getInstance( )
//			.getFirstMasterPageHandle( handle );
		String unit = handle.getDefaultUnits();

		if (unit == null) {
			unit = DesignChoiceConstants.UNITS_IN;
		}
		double value = MetricUtility.pixelToPixelInch(marginValue);
		if (value < 0.0) {
			value = 0.0;
		}
		DimensionValue dim = DimensionUtil.convertTo(value, DesignChoiceConstants.UNITS_IN, unit);
		double showValue = dim.getMeasure();
		String prefix = ((EditorRulerProvider) getGuideEditPart().getRulerProvider())
				.getPrefixLabel(getHost().getModel());
		return prefix + " " + getShowValue(showValue) + " " + getUnitDisplayName(unit) + " (" + marginValue + " " //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				+ PIXELS_LABEL + ")"; //$NON-NLS-1$ ;
	}

	private String getShowValue(double value) {
		return FORMAT.format(value);
	}

	protected String getUnitDisplayName(String unit) {
		IChoice choice = choiceSet.findChoice(unit);
		return choice.getDisplayName();
	}

	private Dimension getDistance() {
//		Point p = getStartLocation( );
//
//		Control canvas = getGuideEditPart( ).getViewer( ).getControl( );
//		org.eclipse.swt.graphics.Rectangle rect = canvas.getBounds( );
//
//		Dimension retValue = new Dimension(rect.width - p.x, p.y);
//
//		return retValue;

		Point p = getStartLocation();

		FigureCanvas canvas = ((DeferredGraphicalViewer) getGuideEditPart().getViewer()
				.getProperty(GraphicalViewer.class.toString())).getFigureCanvas();
		org.eclipse.swt.graphics.Rectangle rect = canvas.getBounds();

		Dimension retValue = new Dimension(rect.width - p.x, p.y);
		if (canvas.getVerticalBar().isVisible()) {
			retValue.width = retValue.width - canvas.getVerticalBar().getSize().x;
		}
		return retValue;
	}

	private void adjustLocation() {
		if (infoLabel == null) {
			return;
		}
		Rectangle rect = infoLabel.getBounds().getCopy();
		Dimension dim = getDistance();
		Point p = ((Figure) infoLabel).getLocation().getCopy();
		if (dim.width < rect.width) {
			p.x = p.x - (rect.width - dim.width);
		}

		if (dim.height < rect.height + DISTANCE) {
			p.y = p.y + (rect.height + DISTANCE - dim.height);
		}

		infoLabel.setLocation(p);
	}

	private void setLabelLocation() {
		if (infoLabel == null) {
			return;
		}
		Point p = getStartLocation();
		Point location = p.getCopy();
		infoLabel.translateToRelative(p);
		if (getGuideEditPart().isHorizontal()) {
			p.y = location.y;
		} else {
			p.x = location.x;
		}
		infoLabel.setLocation(new Point(p.x, p.y - DISTANCE));
	}

	private Point getStartLocation() {
		if (getGuideEditPart().isHorizontal()) {
			return new Point(DISTANCE - DEFAULT_VALUE, getGuideEditPart().getZoomedPosition() + DISTANCE);
		} else {
			return new Point(getGuideEditPart().getZoomedPosition(), DEFAULT_VALUE);
		}
	}

	protected void updateInfomation(String label) {
		if (infoLabel == null) {
			return;
		}
		infoLabel.setText(label);
		Dimension size = FigureUtilities.getTextExtents(label, infoLabel.getFont());
		// Insets insets = getInfomationLabel( ).getInsets( );
		Insets insets = INSETS;
		Dimension newSize = size.getCopy().expand(insets.getWidth(), insets.getHeight());
		if (size.width > maxWidth) {
			maxWidth = size.width;
		} else {
			newSize = new Dimension(maxWidth, size.height).expand(insets.getWidth(), insets.getHeight());
		}
		infoLabel.setSize(newSize);
		setLabelLocation();
		adjustLocation();
	}
}
