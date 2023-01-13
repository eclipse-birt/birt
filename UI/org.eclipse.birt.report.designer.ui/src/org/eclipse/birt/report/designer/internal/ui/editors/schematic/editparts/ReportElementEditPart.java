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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.core.model.ReportDesignHandleAdapter;
import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.BaseBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportElementResizablePolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.IReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.AbstractGuideHandle;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.handles.IGuideFeedBackHost;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.ReportElementDragTracker;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * <p>
 * Abstract super class for all report element editPart
 * </p>
 */
public abstract class ReportElementEditPart extends AbstractGraphicalEditPart
		implements IModelAdapterHelper, IGuideFeedBackHost {

	private static final int DELAY_TIME = 1600;
	protected DesignElementHandleAdapter peer;
	private AbstractGuideHandle guideHandle = null;
	private boolean isEdited = false;
	protected Logger logger = Logger.getLogger(ReportElementEditPart.class.getName());

	// private static boolean canDeleteGuide = true;

	/**
	 * Constructor
	 *
	 * @param model
	 */
	public ReportElementEditPart(Object model) {
		super();
		if (Policy.TRACING_EDITPART_CREATE) {
			String[] result = this.getClass().getName().split("\\."); //$NON-NLS-1$
			System.out.println(result[result.length - 1] + " >> Created for " //$NON-NLS-1$
					+ model);
		}
		setModel(model);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#setModel(java.lang.Object)
	 */
	@Override
	public void setModel(Object model) {
		super.setModel(model);
		peer = creatDesignElementHandleAdapter();
	}

	/**
	 * Create the design handle adapter
	 *
	 * @return Return the design handle adapter
	 */
	public DesignElementHandleAdapter creatDesignElementHandleAdapter() {
		HandleAdapterFactory.getInstance().remove(getModel());
		return HandleAdapterFactory.getInstance().getDesignElementHandleAdapter(getModel(), this);
	}

	/**
	 * perform edit directly when the request is the corresponding type.
	 */
	@Override
	public void performRequest(Request request) {
		if (request.getExtendedData().get(DesignerConstants.NEWOBJECT_FROM_LIBRARY) != null) {
			return;
		}
		if (RequestConstants.REQ_OPEN.equals(request.getType())
				|| ReportRequestConstants.CREATE_ELEMENT.equals(request.getType())) {
			if (isEdited()) {
				return;
			}
			setEdited(true);
			try {
				performDirectEdit();
			} catch (RuntimeException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
			setEdited(false);
		}
	}

	/**
	 * Perform the direct edit (currently nothing will be done)
	 */
	public void performDirectEdit() {
		// do nothing
	}

	protected boolean isEdited() {
		return isEdited;
	}

	protected void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	/**
	 * Creates the guide handle, default get from parent.
	 *
	 * @return Return the guide handle
	 */
	protected AbstractGuideHandle createGuideHandle() {
		EditPart part = getParent();
		if (part instanceof ReportElementEditPart) {
			return ((ReportElementEditPart) part).getGuideHandle();
		}
		return null;
	}

	protected AbstractGuideHandle getGuideHandle() {
		if (guideHandle == null) {
			guideHandle = interCreateGuideHandle();
		}
		return guideHandle;
	}

	private AbstractGuideHandle interCreateGuideHandle() {
		if (getParent() instanceof MultipleEditPart) {
			return ((MultipleEditPart) getParent()).createGuideHandle();
		}
		return createGuideHandle();
	}

	/**
	 * Adds the guide handle to the handle layer.
	 *
	 */
	@Override
	public void addGuideFeedBack() {
		if (guideHandle == null) {
			guideHandle = interCreateGuideHandle();
		}

		if (guideHandle != null && guideHandle != findHandle()) {
			clearGuideHandle();
			getHandleLayer().add(guideHandle);
			guideHandle.invalidate();
			guideHandle.setCanDeleteGuide(true);
		} else if (guideHandle != null && guideHandle == findHandle()) {
			guideHandle.setCanDeleteGuide(false);
		} else if (guideHandle != null) {
			guideHandle.setCanDeleteGuide(true);
		}
	}

	private AbstractGuideHandle findHandle() {
		IFigure layer = getHandleLayer();
		List<?> list = layer.getChildren();
		int size = list.size();

		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (obj instanceof AbstractGuideHandle) {
				return (AbstractGuideHandle) obj;
			}
		}

		return null;
	}

	protected void clearGuideHandle() {
		IFigure layer = getHandleLayer();
		List<?> list = layer.getChildren();
		List<IFigure> temp = new ArrayList<IFigure>();
		int size = list.size();

		for (int i = 0; i < size; i++) {
			Object obj = list.get(i);
			if (obj instanceof AbstractGuideHandle) {
				temp.add((IFigure) obj);
			}
		}

		size = temp.size();
		for (int i = 0; i < size; i++) {
			IFigure figure = temp.get(i);
			layer.remove(figure);
		}
	}

	/**
	 * Removes the guide handle.
	 */
	protected void removeGuideFeedBack() {
		if (guideHandle != null && guideHandle.getParent() == getHandleLayer()) {
			getHandleLayer().remove(guideHandle);
		} else if (getParent() instanceof ReportElementEditPart) {
			((ReportElementEditPart) getParent()).removeGuideFeedBack();
		}
		guideHandle = null;
	}

	/**
	 * Removes the guide handle after the specified number of milliseconds.
	 */
	@Override
	public void delayRemoveGuideFeedBack() {
		if (guideHandle != null) {
			guideHandle.setCanDeleteGuide(true);
		}
		Display.getCurrent().timerExec(DELAY_TIME, new Runnable() {

			@Override
			public void run() {
				if (guideHandle != null && guideHandle.isCanDeleteGuide()) {
					removeGuideFeedBack();
				}
			}

		});
	}

	private IFigure getHandleLayer() {
		super.getLayer(LayerConstants.HANDLE_LAYER);
		LayerManager manager = (LayerManager) getViewer().getEditPartRegistry().get(LayerManager.ID);
		return manager.getLayer(LayerConstants.HANDLE_LAYER);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#activate()
	 */
	@Override
	public void activate() {
		if (isActive()) {
			return;
		}

		super.activate();

		refreshPageClip();

		getFigure().addMouseMotionListener(new MouseMotionListener.Stub() {

			@Override
			public void mouseEntered(MouseEvent me) {
				addGuideFeedBack();

			}

			@Override
			public void mouseExited(MouseEvent me) {
				delayRemoveGuideFeedBack();
			}

			@Override
			public void mouseHover(MouseEvent me) {
				addGuideFeedBack();
			}

			@Override
			public void mouseMoved(MouseEvent me) {
				addGuideFeedBack();
			}

		});

		getFigure().setFocusTraversable(true);

		updateLayoutPreference();

		// FIX BUG 298738
		Display.getCurrent().asyncExec(new Runnable() {
			@Override
			public void run() {
				if (!(getModel() instanceof DesignElementHandle) || isDelete()) {
					return;
				}

				EditPart parent = getParent();
				while (parent != null) {
					if (parent instanceof RootEditPart) {
						break;
					}
					parent = parent.getParent();
				}
				if (parent == null) {
					return;
				}
				if (((DeferredGraphicalViewer) getViewer()).getFigureCanvas() == null) {
					return;
				}
				DesignElementHandle handle = (DesignElementHandle) getModel();
				EditPart part = ReportElementEditPart.this;
				while (part != null && !(part instanceof RootEditPart)) {
					part = part.getParent();
				}

				if (getModelAdapter() == null || part == null) {
					return;
				}
				Object[] backGroundPosition = getBackgroundPosition(handle);
				Object xPosition = backGroundPosition[0];
				Object yPosition = backGroundPosition[1];
				boolean needRefresh = false;

				if (xPosition instanceof DimensionValue) {
					needRefresh = true;
				}

				if (yPosition instanceof DimensionValue) {
					needRefresh = true;
				}
//				if (isPercentageValue( handle.getProperty( StyleHandle.MARGIN_TOP_PROP ) ))
//				{
//					needRefresh = true;
//				}
//				if (isPercentageValue( handle.getProperty( StyleHandle.MARGIN_BOTTOM_PROP ) ))
//				{
//					needRefresh = true;
//				}
				if (isPercentageValue(handle.getProperty(IStyleModel.MARGIN_LEFT_PROP))) {
					needRefresh = true;
				}
				if (isPercentageValue(handle.getProperty(IStyleModel.MARGIN_RIGHT_PROP))) {
					needRefresh = true;
				}

				if (needRefresh) {
					refreshVisuals();
				}
			}
		});

	}

	private boolean isPercentageValue(Object object) {
		if (object instanceof DimensionValue) {
			DimensionValue dimension = (DimensionValue) object;
			String units = dimension.getUnits();
			if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(units)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		if (!isActive()) {
			return;
		}
		removeGuideFeedBack();

		super.deactivate();

		HandleAdapterFactory.getInstance().remove(getModel(), this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected abstract void createEditPolicies();

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	@Override
	public DragTracker getDragTracker(Request req) {
		DragEditPartsTracker track = new ReportElementDragTracker(this);
		return track;
	}

	/**
	 * @return bounds
	 */
	public Rectangle getBounds() {
		return getReportElementHandleAdapt().getbounds();
	}

	/**
	 * Sets bounds
	 *
	 * @param r
	 */
	public void setBounds(Rectangle r) {
		try {
			getReportElementHandleAdapt().setBounds(r);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Gets location
	 *
	 * @return Return the location point
	 */
	public Point getLocation() {
		return getReportElementHandleAdapt().getLocation();
	}

	/**
	 * Sets location
	 *
	 * @param p
	 */
	public void setLocation(Point p) {
		try {
			getReportElementHandleAdapt().setLocation(p);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * @return size
	 */
	public Dimension getSize() {
		return getReportElementHandleAdapt().getSize();
	}

	/**
	 * Sets size
	 *
	 * @param d
	 */
	public void setSize(Dimension d) {
		try {
			getReportElementHandleAdapt().setSize(d);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
	}

	/**
	 * Get the current font family.
	 *
	 * @return The current font family
	 */
	protected Font getFont(ReportItemHandle handle) {
		return UIUtil.getFont(handle);
	}

	protected Font getFont() {
		return getFont((ReportItemHandle) getModel());
	}

	/**
	 * @return display label
	 */
	public String getDisplayLabel() {
		return null;
	}

	private boolean isDirty = true;

	@Override
	public final void refreshVisuals() {
		super.refreshVisuals();
		refreshFigure();
		refreshReportChildren(this);
		// added for must repaint
		getFigure().repaint();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshChildren()
	 */
	@Override
	public void refreshChildren() {
		super.refreshChildren();
	}

	/**
	 * Refresh the report children
	 *
	 * @param parent parent like starting point of refresh
	 */
	public void refreshReportChildren(ReportElementEditPart parent) {
		List<?> list = parent.getChildren();
		for (int i = 0; i < list.size(); i++) {
			Object part = list.get(i);
			if (part instanceof ReportElementEditPart) {
				if (((ReportElementEditPart) part).isDelete()) {
					continue;
				}
				((ReportElementEditPart) part).refreshFigure();
				refreshReportChildren((ReportElementEditPart) part);
			}
		}
	}

	/**
	 *
	 */
	public abstract void refreshFigure();

	/**
	 * Refresh Margin property for this element.
	 */
	protected void refreshMargin() {
		if (getFigure() instanceof IReportElementFigure) {
			if (isFixLayout() && getFigure().getParent() != null) {
				((IReportElementFigure) getFigure()).setMargin(
						getModelAdapter().getMargin(null, getFigure().getParent().getClientArea().getSize()));
			} else {
				((IReportElementFigure) getFigure()).setMargin(getModelAdapter().getMargin(null));
			}
		}
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 *
	 */
	protected void refreshBackground(DesignElementHandle handle) {
		refreshBackgroundColor(handle);
		refreshBackgroundImage(handle);
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 *
	 */
	protected void refreshBackgroundColor(DesignElementHandle handle) {
		Object obj = handle.getProperty(IStyleModel.BACKGROUND_COLOR_PROP);

		if (handle instanceof MasterPageHandle) {
			getFigure().setOpaque(true);
		} else {
			getFigure().setOpaque(false);
		}

		if (obj != null) {
			int color = 0xFFFFFF;
			if (obj instanceof String) {
				color = ColorUtil.parseColor((String) obj);
			} else {
				color = ((Integer) obj).intValue();
			}
			getFigure().setBackgroundColor(ColorManager.getColor(color));
			getFigure().setOpaque(true);
		}
	}

	protected Image getBackImage(DesignElementHandle handle) {
		String backGroundImage = getBackgroundImage(handle);

		if (backGroundImage == null) {
			return null;
		}
		Object obj = handle.getProperty(IStyleModel.BACKGROUND_IMAGE_TYPE_PROP);
		String imageSourceType = CSSValueConstants.URL_VALUE.getCssText();
		if (obj instanceof String) {
			imageSourceType = obj.toString();
		}

		Image image = null;

		// URL image
		if (imageSourceType.equalsIgnoreCase(CSSValueConstants.URL_VALUE.getCssText())) {
			try {
				image = ImageManager.getInstance().getImage(getModelAdapter().getModuleHandle(), backGroundImage);
			} catch (SWTException e) {
				// Should not be ExceptionHandler.handle(e), see SCR#73730
				image = null;
			}
		}

		// embedded image
		if (imageSourceType.equalsIgnoreCase(CSSValueConstants.EMBED_VALUE.getCssText())) {
			try {
				image = ImageManager.getInstance().getEmbeddedImage(getModelAdapter().getModuleHandle(),
						backGroundImage);
			} catch (SWTException e) {
				// Should not be ExceptionHandler.handle(e), see SCR#73730
				image = null;
			}
		}
		return image;
	}

	/*
	 * Refresh Background: Color, Image, Repeat, PositionX, PositionY.
	 *
	 */
	protected void refreshBackgroundImage(DesignElementHandle handle) {
		IReportElementFigure figure = (IReportElementFigure) getFigure();

		String backGroundImage = getBackgroundImage(handle);

		if (backGroundImage == null) {
			figure.setImage(null);
		}

		Image image = null;
		// URL image
		try {
			image = ImageManager.getInstance().getImage(getModelAdapter().getModuleHandle(), backGroundImage);
		} catch (SWTException e) {
			// Should not be ExceptionHandler.handle(e), see SCR#73730
			image = null;
		}

		// embedded image
		if (image == null) {
			try {
				image = ImageManager.getInstance().getEmbeddedImage(getModelAdapter().getModuleHandle(),
						backGroundImage);
			} catch (SWTException e) {
				// Should not be ExceptionHandler.handle(e), see SCR#73730
				image = null;
			}
		}

		if (image == null) {
			figure.setImage(null);
			return;
		}
		int dpi = getImageDPI(backGroundImage);

		if (figure instanceof ReportElementFigure) {
			((ReportElementFigure) figure).setBackgroundImageDPI(dpi);
		}
		figure.setImage(image);

		Object[] backGroundPosition = getBackgroundPosition(handle);
		int backGroundRepeat = getBackgroundRepeat(handle);

		figure.setRepeat(backGroundRepeat);

		Object xPosition = backGroundPosition[0];
		Object yPosition = backGroundPosition[1];
		Rectangle area = getFigure().getClientArea();
		org.eclipse.swt.graphics.Rectangle imageArea = image.getBounds();
		Point position = new Point(-1, -1);
		int alignment = 0;

		if (xPosition instanceof Integer) {
			position.x = ((Integer) xPosition).intValue();
		} else if (xPosition instanceof DimensionValue) {
			int percentX = (int) ((DimensionValue) xPosition).getMeasure();
			position.x = (area.width - imageArea.width) * percentX / 100;
		} else if (xPosition instanceof String) {
			alignment |= DesignElementHandleAdapter.getPosition((String) xPosition);
		}

		if (yPosition instanceof Integer) {
			position.y = ((Integer) yPosition).intValue();
		} else if (yPosition instanceof DimensionValue) {
			int percentY = (int) ((DimensionValue) yPosition).getMeasure();
			position.y = (area.width - imageArea.width) * percentY / 100;
		} else if (yPosition instanceof String) {
			alignment |= DesignElementHandleAdapter.getPosition((String) yPosition);
		}

		figure.setAlignment(alignment);
		figure.setPosition(position);
	}

	private int getImageDPI(String backGroundImage) {
		if (!(getModel() instanceof DesignElementHandle)) {
			return 0;
		}
		DesignElementHandle model = (DesignElementHandle) getModel();
		InputStream in = null;
		URL temp = null;
		try {
			if (URIUtil.isValidResourcePath(backGroundImage)) {
				temp = ImageManager.getInstance().generateURL(model.getModuleHandle(),
						URIUtil.getLocalPath(backGroundImage));

			} else {
				temp = ImageManager.getInstance().generateURL(model.getModuleHandle(), backGroundImage);
			}
			if (temp != null) {
				in = temp.openStream();
			}

		} catch (IOException e) {
			in = null;
		}

		int dpi = UIUtil.getImageResolution(in)[0];
		if (in != null) {
			try {
				in.close();
			} catch (IOException e) {
				ExceptionHandler.handle(e);
			}
		}
		return dpi;
	}

	/**
	 * Marks edit part dirty
	 *
	 * @param bool
	 */
	@Override
	public void markDirty(boolean bool) {
		this.isDirty = bool;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.core.model.IModelAdaptHelper#isDirty()
	 */
	@Override
	public boolean isDirty() {
		return isDirty;
	}

	protected DesignElementHandleAdapter getModelAdapter() {
		if (peer == null) {
			peer = HandleAdapterFactory.getInstance().getDesignElementHandleAdapter(getModel(), this);
		}
		return peer;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.core.model.IModelAdaptHelper#
	 * getPreferredSize()
	 */
	@Override
	public Dimension getPreferredSize() {
		Dimension size = getFigure().getSize().getCopy();
		return size;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.core.model.IModelAdaptHelper#getInsets()
	 */
	@Override
	public Insets getInsets() {
		return new Insets(getFigure().getInsets());
	}

	protected ReportItemtHandleAdapter getReportElementHandleAdapt() {
		return (ReportItemtHandleAdapter) getModelAdapter();
	}

	protected void refreshPageClip() {
		if (getFigure() instanceof ReportElementFigure) {
			Object obj = getViewer().getProperty(DeferredGraphicalViewer.LAYOUT_SIZE);

			if (obj instanceof Rectangle) {
				((ReportElementFigure) getFigure()).setPageClip((Rectangle) obj);
			}
		}
	}

	protected void updateBaseBorder(DesignElementHandle handle, BaseBorder border) {
		updateBottomBorder(handle, border);
		updateTopBorder(handle, border);
		updateLeftBorder(handle, border);
		updateRightBorder(handle, border);
	}

	protected void updateBottomBorder(DesignElementHandle handle, BaseBorder border) {
		border.bottomColor = handle.getPropertyHandle(IStyleModel.BORDER_BOTTOM_COLOR_PROP).getIntValue();
		border.bottomStyle = handle.getPropertyHandle(IStyleModel.BORDER_BOTTOM_STYLE_PROP).getStringValue();
		border.bottomWidth = handle.getPropertyHandle(IStyleModel.BORDER_BOTTOM_WIDTH_PROP).getStringValue();
	}

	protected void updateTopBorder(DesignElementHandle handle, BaseBorder border) {
		border.topColor = handle.getPropertyHandle(IStyleModel.BORDER_TOP_COLOR_PROP).getIntValue();
		border.topStyle = handle.getPropertyHandle(IStyleModel.BORDER_TOP_STYLE_PROP).getStringValue();
		border.topWidth = handle.getPropertyHandle(IStyleModel.BORDER_TOP_WIDTH_PROP).getStringValue();
	}

	protected void updateLeftBorder(DesignElementHandle handle, BaseBorder border) {
		border.leftColor = handle.getPropertyHandle(IStyleModel.BORDER_LEFT_COLOR_PROP).getIntValue();
		border.leftStyle = handle.getPropertyHandle(IStyleModel.BORDER_LEFT_STYLE_PROP).getStringValue();
		border.leftWidth = handle.getPropertyHandle(IStyleModel.BORDER_LEFT_WIDTH_PROP).getStringValue();

	}

	protected void updateRightBorder(DesignElementHandle handle, BaseBorder border) {
		border.rightColor = handle.getPropertyHandle(IStyleModel.BORDER_RIGHT_COLOR_PROP).getIntValue();
		border.rightStyle = handle.getPropertyHandle(IStyleModel.BORDER_RIGHT_STYLE_PROP).getStringValue();
		border.rightWidth = handle.getPropertyHandle(IStyleModel.BORDER_RIGHT_WIDTH_PROP).getStringValue();
	}

	protected void refreshBorder(DesignElementHandle handle, BaseBorder border) {
		updateBaseBorder(handle, border);

		getFigure().setBorder(border);

		refreshPageClip();
	}

	protected Insets getMasterPageInsets(DesignElementHandle handle) {
		return ((ReportDesignHandleAdapter) getModelAdapter()).getMasterPageInsets(handle);
	}

	protected Dimension getMasterPageSize(DesignElementHandle handle) {
		return ((ReportDesignHandleAdapter) getModelAdapter()).getMasterPageSize(handle);
	}

	protected int getForegroundColor(DesignElementHandle handle) {
		return getModelAdapter().getForegroundColor(handle);
	}

	protected int getBackgroundColor(DesignElementHandle handle) {
		return getModelAdapter().getBackgroundColor(handle);
	}

	protected String getBackgroundImage(DesignElementHandle handle) {
		return getModelAdapter().getBackgroundImage(handle);
	}

	protected Object[] getBackgroundPosition(DesignElementHandle handle) {
		return getModelAdapter().getBackgroundPosition(handle);
	}

	protected int getBackgroundRepeat(DesignElementHandle handle) {
		return getModelAdapter().getBackgroundRepeat(handle);
	}

	protected boolean isFigureLeft(Request request) {
		if (!(request instanceof SelectionRequest)) {
			return true;
		}
		SelectionRequest selctionRequest = (SelectionRequest) request;
		Point p = selctionRequest.getLocation();
		// getFigure().translateToAbsolute(p);
		getFigure().translateToRelative(p);
		Point center = getFigure().getBounds().getCenter();
		return center.x >= p.x;
	}

	/**
	 * Verify if the element is deleted
	 *
	 * @return Return if the element is deleted
	 *
	 * @see org.eclipse.gef.EditPart#isActive()
	 */
	public boolean isDelete() {
		boolean bool = false;
		if (getModel() instanceof DesignElementHandle) {
			if (!(getModel() instanceof ModuleHandle)) {
				bool = ((DesignElementHandle) getModel()).getContainer() == null
						|| ((DesignElementHandle) getModel()).getRoot() == null;
			}
		}
		return bool;
	}

	/**
	 * Notify the model change
	 */
	public void notifyModelChange() {
		if (getParent() != null && getParent() instanceof ReportElementEditPart) {
			((ReportElementEditPart) getParent()).notifyModelChange();
		}
	}

	/**
	 * Refresh after content change
	 *
	 * @param info Map of changed content elements
	 */
	protected void contentChange(Map<?, ?> info) {
		markDirty(true);
		refresh();
	}

	/**
	 * Refresh after property change
	 *
	 * @param info Map of changed element properties
	 */
	protected void propertyChange(Map<?, ?> info) {
		refreshVisuals();
	}

	/**
	 * Compare of the current model with the requested model
	 *
	 * @param model Module to validate
	 * @return Return the compare result
	 */
	public boolean isinterest(Object model) {
		return getModel().equals(model);
	}

	/**
	 * @param object
	 * @return false
	 */
	public boolean isinterestSelection(Object object) {
		return false;
	}

	/**
	 * Get the resize policy object
	 *
	 * @param parentPolice
	 * @return Return the resize edit policy
	 */
	public EditPolicy getResizePolice(EditPolicy parentPolice) {
		ReportElementResizablePolicy policy = new ReportElementResizablePolicy();
		policy.setResizeDirections(PositionConstants.SOUTH | PositionConstants.EAST | PositionConstants.SOUTH_EAST);
		return policy;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#removeChild(org.eclipse.gef.
	 * EditPart)
	 */
	@Override
	public void removeChild(EditPart child) {
		super.removeChild(child);
	}

	/**
	 * Get guide label
	 *
	 * @return Return empty guide label
	 */
	public String getGuideLabel() {
		return ""; //$NON-NLS-1$
	}

	/**
	 * Get the direction of the text
	 *
	 * @return Return the text direction
	 */
	protected String getTextDirection() {
		DesignElementHandle handle = (DesignElementHandle) getModel();
		return getTextDirection(handle);
	}

	/**
	 * Get the direction of the text
	 *
	 * @param handle design handle to be used
	 * @return Return the text direction
	 */
	protected String getTextDirection(DesignElementHandle handle) {
		return handle.isDirectionRTL() ? DesignChoiceConstants.BIDI_DIRECTION_RTL
				: DesignChoiceConstants.BIDI_DIRECTION_LTR;
	}

	/**
	 *
	 */
	protected void updateLayoutPreference() {
		if (!(getModel() instanceof DesignElementHandle)) {
			return;
		}
		ModuleHandle handle = ((DesignElementHandle) getModel()).getModuleHandle();
		if (!(handle instanceof ReportDesignHandle)) {
			return;
		}
		if (getContentPane().getLayoutManager() instanceof ReportFlowLayout) {
			((ReportFlowLayout) getContentPane().getLayoutManager())
					.setLayoutPreference(((ReportDesignHandle) handle).getLayoutPreference());
		}
	}

	/**
	 * Verify if the layout is fixed
	 *
	 * @return Return the information of fixed layout
	 */
	public boolean isFixLayout() {
		return DEUtil.isFixLayout(getModel());
	}
}
