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

package org.eclipse.birt.report.designer.internal.ui.extension;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportElementNonResizablePolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.extensions.GuiExtensionManager;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.extensions.IExtensionConstants;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemBuilderUI;
import org.eclipse.birt.report.designer.ui.extensions.IReportItemFigureProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.jface.window.Window;

/**
 * ExtendedEditPart
 */
public class ExtendedEditPart extends ReportElementEditPart {

	private IReportItemFigureProvider elementUI;

	/**
	 * @param model
	 */
	public ExtendedEditPart(ExtendedItemHandle model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart
	 * #elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 * org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged(DesignElementHandle arg0, NotificationEvent arg1) {
		markDirty(true);
		refresh();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ReportComponentEditPolicy() {

			public boolean understandsRequest(Request request) {
				if (RequestConstants.REQ_DIRECT_EDIT.equals(request.getType())
						|| RequestConstants.REQ_OPEN.equals(request.getType()))
					// !creation request already processed in createion
					// tool
					// || ReportRequest.CREATE_ELEMENT.equals(
					// request.getType( ) ) )
					return true;
				return super.understandsRequest(request);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure() {
		refreshBorder((DesignElementHandle) getModel(), new LineBorder());
		getExtendedElementUI().updateFigure(getExtendedItemHandle(), getFigure());

		((AbstractGraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), getConstraint());
	}

	/**
	 * @return The constraint
	 */
	protected Object getConstraint() {
		ExtendedItemHandle handle = getExtendedItemHandle();
		ReportItemConstraint constraint = new ReportItemConstraint();

		String type = handle.getPrivateStyle().getDisplay();
		if (type == null || DesignChoiceConstants.DISPLAY_NONE.equals(type)) {
			type = DesignChoiceConstants.DISPLAY_BLOCK;
		}
		constraint.setDisplay(type);
		constraint.setMargin(getModelAdapter().getMargin(null));

		DimensionHandle value = handle.getWidth();
		constraint.setMeasure(value.getMeasure());
		constraint.setUnits(value.getUnits());

		String vAlign = handle.getPrivateStyle().getVerticalAlign();
		if (DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE.equals(vAlign)) {
			constraint.setAlign(ReportFlowLayout.ALIGN_CENTER);
		} else if (DesignChoiceConstants.VERTICAL_ALIGN_BOTTOM.equals(vAlign)) {
			constraint.setAlign(ReportFlowLayout.ALIGN_RIGHTBOTTOM);
		} else if (DesignChoiceConstants.VERTICAL_ALIGN_TOP.equals(vAlign)) {
			constraint.setAlign(ReportFlowLayout.ALIGN_LEFTTOP);
		}
		return constraint;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		return getExtendedElementUI().createFigure(getExtendedItemHandle());
	}

	public void performDirectEdit() {
		ExtendedElementUIPoint point = ExtensionPointManager.getInstance()
				.getExtendedElementPoint(((ExtendedItemHandle) getModel()).getExtensionName());

		IReportItemBuilderUI builder = point.getReportItemBuilderUI();

		if (builder != null) {
			String displayLabel = (String) point.getAttribute(IExtensionConstants.ELEMENT_REPORT_ITEM_LABEL_UI);

			// Start a transaction before opening builder

			CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
			final String transName = Messages.getFormattedString("ExtendedEditPart.edit", //$NON-NLS-1$
					new Object[] { displayLabel == null
							? DEUtil.getMetaDataDictionary().getExtension(point.getExtensionName()).getDisplayName()
							: displayLabel });

			stack.startTrans(transName);
			int result = Window.CANCEL;
			try {
				result = builder.open(getExtendedItemHandle());
			} catch (RuntimeException e) {
				ExceptionHandler.handle(e);
				stack.rollback();
				return;
			}

			if (result == Window.OK) {
				stack.commit();
				refreshVisuals();
			} else {
				stack.rollback();
			}

		}
	}

	public IReportItemFigureProvider getExtendedElementUI() {
		return elementUI;
	}

	public void setExtendedElementUI(IReportItemFigureProvider elementUI) {
		this.elementUI = elementUI;
	}

	public ExtendedItemHandle getExtendedItemHandle() {
		return (ExtendedItemHandle) getModel();
	}

	public boolean canResize() {
		String id = GuiExtensionManager.getExtendedElementID(getExtendedItemHandle());
		Boolean bool = (Boolean) ExtensionPointManager.getInstance().getExtendedElementPoint(id)
				.getAttribute(IExtensionConstants.ATTRIBUTE_EDITOR_CAN_RESIZE);

		return bool.booleanValue();
	}

	public void deactivate() {
		elementUI.disposeFigure(getExtendedItemHandle(), getFigure());
		super.deactivate();
	}

	@Override
	public EditPolicy getResizePolice(EditPolicy parentPolice) {
		if (canResize()) {
			return super.getResizePolice(parentPolice);
		} else {
			return new ReportElementNonResizablePolicy();
		}
	}

	@Override
	protected void updateLayoutPreference() {
		super.updateLayoutPreference();
		if (getFigure() instanceof LabelFigure) {
			((LabelFigure) getFigure()).setFixLayout(isFixLayout());
		}
	}
}
