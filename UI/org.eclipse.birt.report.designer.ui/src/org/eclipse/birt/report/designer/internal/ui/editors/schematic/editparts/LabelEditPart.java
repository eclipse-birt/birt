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

import org.eclipse.birt.report.designer.core.model.schematic.LabelHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.ReportColorConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.border.LineBorder;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.LabelDirectEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportComponentEditPolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportElementNonResizablePolicy;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.LabelFigure;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.LabelCellEditorLocator;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.tools.LabelEditManager;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportFlowLayout;
import org.eclipse.birt.report.designer.internal.ui.layout.ReportItemConstraint;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * Provides support for label edit parts.
 * 
 */
public class LabelEditPart extends ReportElementEditPart {

	protected static final int TRUNCATE_LENGTH = 18;

	protected static final String ELLIPSIS = "..."; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.core.facade.IModelAdaptHelper#markDirty(
	 * boolean)
	 */
	private static final String ELEMENT_DEFAULT_TEXT = Messages.getString("LabelEditPart.Figure.Default");//$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param model
	 */
	public LabelEditPart(Object model) {
		super(model);
	}

	private DirectEditManager manager;

	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ReportComponentEditPolicy());

		installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE, new LabelDirectEditPolicy());
	}

	protected IFigure createFigure() {
		LabelFigure label = new LabelFigure();
		return label;
	}

	/**
	 * Perform director edit on label
	 */
	public void performDirectEdit() {
		if (manager == null)
			manager = new LabelEditManager(this, TextCellEditor.class,
					new LabelCellEditorLocator((Figure) getFigure()));
		manager.show();
	}

	/**
	 * @return
	 */
	protected LabelHandleAdapter getLabelAdapter() {
		return (LabelHandleAdapter) getModelAdapter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#refreshFigure()
	 */
	public void refreshFigure() {
		StyleHandle style = ((DesignElementHandle) getModel()).getPrivateStyle();
		// ( (LabelFigure) getFigure( ) ).setDirection( style.getTextDirection( ) ); //
		// bidi_hcg
		((LabelFigure) getFigure()).setDirection(getTextDirection()); // bidi_hcg
		((LabelFigure) getFigure()).setText(getText());
		((LabelFigure) getFigure()).setFont(getFont());

		((LabelFigure) getFigure()).setTextUnderline(style.getTextUnderline());
		((LabelFigure) getFigure()).setTextLineThrough(style.getTextLineThrough());
		((LabelFigure) getFigure()).setTextOverline(style.getTextOverline());
		((LabelFigure) getFigure()).setTextAlign(style.getTextAlign());
		((LabelFigure) getFigure()).setVerticalAlign(style.getVerticalAlign());

		((LabelFigure) getFigure()).setDisplay(style.getDisplay());

		((LabelFigure) getFigure()).setRecommendSize(getLabelAdapter().getSize());

		((LabelFigure) getFigure()).setFixLayout(isFixLayout());
		((AbstractGraphicalEditPart) getParent()).setLayoutConstraint(this, getFigure(), getConstraint());

		((LabelFigure) getFigure())
				.setForegroundColor(ColorManager.getColor(getForegroundColor((ReportItemHandle) getModel())));

		refreshBorder((DesignElementHandle) getModel(), new LineBorder());

		Insets pist = getLabelAdapter().getPadding(getFigure().getInsets());

		((LineBorder) (getFigure().getBorder())).setPaddingInsets(pist);

		if (!hasText())
			((LabelFigure) getFigure()).setForegroundColor(ReportColorConstants.ShadowLineColor);
		else
			((LabelFigure) getFigure())
					.setForegroundColor(ColorManager.getColor(getForegroundColor((ReportItemHandle) getModel())));

		refreshBackground((DesignElementHandle) getModel());

		refreshMargin();
	}

	/**
	 * Returns if the model element has explicit text set.
	 * 
	 * @return
	 */
	protected boolean hasText() {
		if (StringUtil.isBlank(((LabelHandle) getModel()).getDisplayText())) {
			return false;
		}

		return true;
	}

	/**
	 * Get the text shown on label.
	 * 
	 * @return The text shown on label
	 */
	protected String getText() {
		String text = ((LabelHandle) getModel()).getDisplayText();
		if (text == null) {
			text = ELEMENT_DEFAULT_TEXT;
		}
		return text;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.
	 * ReportElementEditPart#getResizePolice(org.eclipse.gef.EditPolicy)
	 */
	public EditPolicy getResizePolice(EditPolicy parentPolice) {
		return new ReportElementNonResizablePolicy();
	}

	@Override
	protected void updateLayoutPreference() {
		super.updateLayoutPreference();
		((LabelFigure) getFigure()).setFixLayout(isFixLayout());
	}

}
