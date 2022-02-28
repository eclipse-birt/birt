/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.commands.PasteCommand;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.ReportDesignMarginBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.AbstractPageFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.MasterPageLayout;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.ContentEvent;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.TreeSearch;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractEditPart;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.swt.widgets.Display;

/**
 * Master Page editor
 */
public class MasterPageEditPart extends AbstractReportEditPart {

	private static final Point PRIVATE_POINT = new Point();

	private static final Insets DEFAULT_CROP = new Insets(-3, -3, -2, -2);

	private List children = new ArrayList();

	/**
	 * Constructor
	 *
	 * @param model
	 */
	public MasterPageEditPart(Object model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#elementChanged(org.eclipse.birt.model.core.
	 * DesignElement, org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged(DesignElementHandle element, NotificationEvent ev) {
		switch (ev.getEventType()) {
		case NotificationEvent.CONTENT_EVENT:
		case NotificationEvent.ELEMENT_DELETE_EVENT:
		case NotificationEvent.PROPERTY_EVENT:
		case NotificationEvent.STYLE_EVENT:
		case NotificationEvent.THEME_EVENT:
		case NotificationEvent.TEMPLATE_TRANSFORM_EVENT: {
			markDirty(true);
			refresh();
			// The children of master page edit part keep
			// virtual model
			// Those edit part will not get notification
			// refresh them explicit
			for (Iterator it = getChildren().iterator(); it.hasNext();) {
				((AbstractEditPart) it.next()).refresh();
			}
		}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		getFigure().setFocusTraversable(false);
		if (getModel() instanceof ReportDesignHandle) {
			/*
			 * if BiDi support is enabled, set valid value for BiDi orientation property
			 */
			getViewer().setProperty(IReportGraphicConstants.REPORT_BIDIORIENTATION_PROPERTY,
					((ReportDesignHandle) getModel()).getBidiOrientation());

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new MasterPageEditPolicy());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		Figure figure = new ReportElementFigure() {

			@Override
			protected void paintFigure(Graphics graphics) {
				graphics.fillRectangle(getBounds());
				super.paintFigure(graphics);
//				graphics.setForegroundColor( ReportColorConstants.MarginBorderColor );
//				graphics.drawRectangle( getBounds( ).getCopy( )
//						.crop( getBorder( ).getInsets( this ) )
//						.crop( DEFAULT_CROP ) );

				graphics.setForegroundColor(ReportColorConstants.ReportForeground);
				graphics.drawRectangle(getBounds().getCopy().crop(new Insets(0, 0, 1, 1)));
			}

			@Override
			protected void paintChildren(Graphics graphics) {
				IFigure child;

				for (int i = 0; i < this.getChildren().size(); i++) {
					child = (IFigure) this.getChildren().get(i);
					if (child.isVisible()) {
						graphics.setClip(getBounds().getCopy().intersect(child.getBounds()));
						child.paint(graphics);
						graphics.restoreState();
					}
				}
			}

			/*
			 * (non-Javadoc)
			 *
			 * @see org.eclipse.draw2d.Figure#findDescendantAtExcluding(int, int,
			 * org.eclipse.draw2d.TreeSearch)
			 */
			@Override
			protected IFigure findDescendantAtExcluding(int x, int y, TreeSearch search) {
				PRIVATE_POINT.setLocation(x, y);
				translateFromParent(PRIVATE_POINT);
				if (!getBounds().contains(PRIVATE_POINT)) {
					return null;
				}

				IFigure fig;
				for (int i = getChildren().size(); i > 0;) {
					i--;
					fig = (IFigure) getChildren().get(i);
					if (fig.isVisible()) {
						fig = fig.findFigureAt(PRIVATE_POINT.x, PRIVATE_POINT.y, search);
						if (fig != null) {
							return fig;
						}
					}
				}
				// No descendants were found
				return null;
			}

		};

		figure.setOpaque(true);

		// figure.setBounds( new Rectangle( 0,
		// 0,
		// getMasterPageSize( (MasterPageHandle) getModel( ) ).width - 1,
		// getMasterPageSize( (MasterPageHandle) getModel( ) ).height - 1 ) );

		MasterPageLayout layout = new MasterPageLayout(this);

		// SlotHandle slotHandle = ( (ReportDesignHandle) getModel( )
		// ).getMasterPages( );
		// Iterator iter = slotHandle.iterator( );
		MasterPageHandle masterPageHandle = (MasterPageHandle) getModel();

		Dimension size = getMasterPageSize(masterPageHandle);

		Rectangle bounds = new Rectangle(0, 0, size.width - 1, size.height - 1);
		layout.setInitSize(bounds);

		figure.setLayoutManager(layout);

		figure.setBorder(new ReportDesignMarginBorder(getMasterPageInsets((MasterPageHandle) getModel())));
		figure.setBounds(bounds.getCopy());

		return figure;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	@Override
	public void refreshChildren() {
		super.refreshChildren();
		List list = getChildren();
		int size = list.size();
		for (int i = 0; i < size; i++) {
			((ReportElementEditPart) list.get(i)).refreshChildren();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#refreshFigure()
	 */
	@Override
	public void refreshFigure() {
		int color = getBackgroundColor((MasterPageHandle) getModel());
		getFigure().setBackgroundColor(getBackGroundColor(color));

		Dimension size = getMasterPageSize((MasterPageHandle) getModel());
		// getFigure( ).setBounds( new Rectangle( 0,
		// 0,
		// dim.width - 1,
		// dim.height - 1 ) );

		Rectangle bounds = new Rectangle(0, 0, size.width - 1, size.height - 1);

		((AbstractPageFlowLayout) getFigure().getLayoutManager()).setInitSize(bounds);

		ReportDesignMarginBorder reportDesignMarginBorder = new ReportDesignMarginBorder(
				getMasterPageInsets((MasterPageHandle) getModel()));
		// reportDesignMarginBorder.setBackgroundColor( ( (MasterPageHandle)
		// getModel( ) ).getProperty( StyleHandle.BACKGROUND_COLOR_PROP ) );
		reportDesignMarginBorder.setBackgroundColor(
				((MasterPageHandle) getModel()).getPropertyHandle(StyleHandle.BACKGROUND_COLOR_PROP).getIntValue());
		// getFigure( ).setBorder( reportDesignMarginBorder );

		refreshMarginBorder(reportDesignMarginBorder);

		refreshBackground((MasterPageHandle) getModel());

		((ReportElementFigure) getFigure()).setBackGroundImageSize(
				getModelAdapter().getBackgroundImageWidth((MasterPageHandle) getModel(), size,
						getBackImage((MasterPageHandle) getModel())),
				getModelAdapter().getBackgroundImageHeight((MasterPageHandle) getModel(), size,
						getBackImage((MasterPageHandle) getModel())));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * AbstractReportEditPart#refreshMarginBorder(org.eclipse.birt.report.designer.
	 * internal.ui.editors.schematic.border.ReportDesignMarginBorder)
	 */
	@Override
	public void refreshMarginBorder(ReportDesignMarginBorder border) {
		refreshBorder((MasterPageHandle) getModel(), border);
		Insets pist = getPadding(null);
		((LineBorder) (getFigure().getBorder())).setPaddingInsets(pist);
	}

	private Insets getPadding(Insets retValue) {
		return DEUtil.getPadding((MasterPageHandle) getModel(), retValue);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#getModelChildren()
	 */
	@Override
	protected List getModelChildren() {

		SlotHandle model = ((SimpleMasterPageHandle) getModel()).getPageHeader();

		if (!children.contains(model)) {
			children.add(model);
		}

		model = ((SimpleMasterPageHandle) getModel()).getPageFooter();

		if (!children.contains(model)) {
			children.add(model);
		}

		return children;
	}

	@Override
	protected void contentChange(Object focus, Map info) {
		if (getViewer() == null) {
			return;
		}
		Object action = info.get(GraphicsViewModelEventProcessor.CONTENT_EVENTTYPE);
		if (action instanceof Integer) {
			if (((Integer) action).intValue() == ContentEvent.REMOVE) {
				List list = (List) info.get(GraphicsViewModelEventProcessor.EVENT_CONTENTS);
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i) == getModel()) {
						SlotHandle slotHandle = (((SimpleMasterPageHandle) getModel()).getModuleHandle())
								.getMasterPages();
						Iterator iter = slotHandle.iterator();
						SimpleMasterPageHandle masterPageHandle = (SimpleMasterPageHandle) iter.next();

						final List temp = new ArrayList();
						temp.add(masterPageHandle);

						Display.getCurrent().asyncExec(new Runnable() {

							@Override
							public void run() {
								ReportRequest r = new ReportRequest();
								r.setType(ReportRequest.LOAD_MASTERPAGE);

								r.setSelectionObject(temp);
								SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);
							}
						});
						return;
					}
				}
			}
		}
		super.contentChange(focus, info);
	}

	@Override
	public boolean isinterest(Object model) {
		return super.isinterest(model) || model instanceof ModuleHandle;
	}

	@Override
	protected void propertyChange(Map info) {
		boolean invalidate = false;
		if (info.get(ReportDesignHandle.BIDI_ORIENTATION_PROP) instanceof ReportDesignHandle) {
			String newOrientation = ((ReportDesignHandle) info.get(ReportDesignHandle.BIDI_ORIENTATION_PROP))
					.getBidiOrientation();

			UIUtil.processOrientationChange(newOrientation, getViewer());

			invalidate = true;
		}

		super.propertyChange(info);
		if (info.get(MasterPageHandle.WIDTH_PROP) instanceof MasterPageHandle
				|| info.get(MasterPageHandle.HEIGHT_PROP) instanceof MasterPageHandle) {
			// fix bug 265256
			Display.getCurrent().asyncExec(new Runnable() {

				@Override
				public void run() {
					List list = getChildren();
					int size = list.size();
					for (int i = 0; i < size; i++) {
						((ReportElementEditPart) list.get(i)).refreshVisuals();
					}
				}
			});
		}

		if (invalidate) {
			getFigure().invalidateTree();
			// getFigure( ).getUpdateManager( ).addInvalidFigure( getFigure( ) );
			getFigure().revalidate();
		}
	}
}

/**
 * Provide getTargetEditPart for GEF framework. When the user click on margin
 * area of master page, the GEF framework need to iterator on installed edit
 * policies of target editpart to get the target edit part. This simple class is
 * written for this target and can avoid NULL pointer exception.
 */

class MasterPageEditPolicy extends GraphicalEditPolicy {

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPolicy#getTargetEditPart(org.eclipse.gef.Request)
	 */
	@Override
	public EditPart getTargetEditPart(Request request) {
		if (REQ_ADD.equals(request.getType()) || REQ_MOVE.equals(request.getType())
				|| REQ_CREATE.equals(request.getType()) || REQ_CLONE.equals(request.getType())) {
			return getHost();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.gef.editpolicies.AbstractEditPolicy#getCommand(org.eclipse.gef.
	 * Request)
	 */
	@Override
	public Command getCommand(Request request) {
		if (REQ_ADD.equals(request.getType())) {
			return getAddCommand((ChangeBoundsRequest) request);
		}

		return super.getCommand(request);
	}

	/**
	 * @param request
	 */
	protected Command getAddCommand(ChangeBoundsRequest request) {
		// Returns a invalid command to disable the whole request
		return new PasteCommand(null, null, null);
	}

}
