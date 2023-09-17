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

import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ImageHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ImageFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.ImageBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * Image edit part
 * </p>
 *
 */
public class ImageEditPart extends ReportElementEditPart implements IResourceEditPart {

	private static final String IMG_TRANS_MSG = Messages.getString("ImageEditPart.trans.editImage"); //$NON-NLS-1$

	/**
	 * Constructor
	 *
	 * @param model
	 */
	public ImageEditPart(Object model) {
		super(model);
	}

	/**
	 * @return Returns the handle.
	 */
	public ImageHandleAdapter getImageAdapter() {
		return (ImageHandleAdapter) getModelAdapter();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	@Override
	protected IFigure createFigure() {
		return new ImageFigure();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ReportComponentEditPolicy() {

			@Override
			public boolean understandsRequest(Request request) {
				if (RequestConstants.REQ_DIRECT_EDIT.equals(request.getType())
						|| RequestConstants.REQ_OPEN.equals(request.getType())
						|| ReportRequestConstants.CREATE_ELEMENT.equals(request.getType())) {
					return true;
				}
				return super.understandsRequest(request);
			}
		});
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
		refreshBorder((DesignElementHandle) getModel(), new LineBorder());

		Insets pist = getImageAdapter().getPadding(getFigure().getInsets());

		((LineBorder) (getFigure().getBorder())).setPaddingInsets(pist);

		Image image = null;
		try {
			image = getImageAdapter().getImage();
		} catch (SWTException e) {
			// Do nothing
		}

		((ImageFigure) this.getFigure()).setStretched(image != null);
		if (image == null) {
			image = ReportPlatformUIImages.getImage(IReportGraphicConstants.ICON_MISSING_IMG);
		}

		((ImageFigure) this.getFigure()).setImage(image);
		if (((ImageFigure) this.getFigure()).getImage() != null) {
			this.getImageAdapter().setImageFigureDimension(((ImageFigure) this.getFigure()).getImage().getBounds());
		}

		if (getImageAdapter().getSize() != null) {
			this.getFigure().setSize(getImageAdapter().getSize());
		} else if (image != null) {
			Dimension rawSize = getImageAdapter().getRawSize();

			if (rawSize.height == 0 && rawSize.width == 0) {
				this.getFigure().setSize(new Dimension(image.getBounds().width, image.getBounds().height));
			} else if (rawSize.height == 0) {
				this.getFigure().setSize(new Dimension(rawSize.width,
						(int) (image.getBounds().height * ((double) rawSize.width / image.getBounds().width))));
			} else {
				this.getFigure()
						.setSize(new Dimension(
								(int) (image.getBounds().width * ((double) rawSize.height / image.getBounds().height)),
								rawSize.height));
			}

		}

		refreshBackgroundColor((DesignElementHandle) getModel());

		refreshMargin();

		((AbstractGraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), getConstraint());
	}

	/**
	 * @return The constraint
	 */
	protected Object getConstraint() {
		ReportItemHandle handle = (ReportItemHandle) getModel();
		ReportItemConstraint constraint = new ReportItemConstraint();
		StyleHandle style = handle.getPrivateStyle();
		constraint.setDisplay(style.getDisplay());
		DimensionHandle value = handle.getWidth();
		constraint.setMeasure(value.getMeasure());
		constraint.setUnits(value.getUnits());

		String vAlign = style.getVerticalAlign();
		if (DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE.equals(vAlign)) {
			constraint.setAlign(ReportFlowLayout.ALIGN_CENTER);
		} else if (DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM.equals(vAlign)) {
			constraint.setAlign(ReportFlowLayout.ALIGN_RIGHTBOTTOM);
		} else if (DesignChoiceConstants.VERTICAL_ALIGN_TOP.equals(vAlign)) {
			constraint.setAlign(ReportFlowLayout.ALIGN_LEFTTOP);
		}
		return constraint;
	}

	/**
	 *
	 */
	@Override
	public void performDirectEdit() {
//		List dataSetList = DEUtil.getDataSetList( (DesignElementHandle) getModel( ) );
		List<?> dataSetList = DEUtil.getDataSetListExcludeSelf((DesignElementHandle) getModel());
		ImageBuilder dialog = new ImageBuilder(PlatformUI.getWorkbench().getDisplay().getActiveShell(),
				ImageBuilder.DLG_TITLE_EDIT, dataSetList);
		dialog.setInput(getModel());
		dialog.setEditModal(true);
		CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
		stack.startTrans(IMG_TRANS_MSG);
		if (dialog.open() == Window.OK) {
			stack.commit();
		} else {
			stack.rollback();
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * IResourceEditPart#refreshResource()
	 */
	@Override
	public void refreshResource() {
		String imageSource = ((ImageHandle) getImageAdapter().getHandle()).getSource();
		if (DesignChoiceConstants.IMAGE_REF_TYPE_FILE.equalsIgnoreCase(imageSource)
				|| DesignChoiceConstants.IMAGE_REF_TYPE_URL.equalsIgnoreCase(imageSource)) {
			refreshFigure();
		}
	}
}
