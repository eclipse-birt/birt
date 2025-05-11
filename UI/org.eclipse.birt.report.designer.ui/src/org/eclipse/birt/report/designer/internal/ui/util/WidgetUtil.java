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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.widget.WidgetConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * WidgetUtil defines constant values to custom control size and provides common
 * layout mechanism.
 */
public class WidgetUtil implements WidgetConstants {

	/**
	 * Creates a default GridLayout layout Manager.
	 *
	 * @param columns The number of columns in the grid
	 * @return A GridLayout instance.
	 */
	public static GridLayout createGridLayout(int columns) {
		GridLayout layout = new GridLayout(columns, false);
		layout.marginHeight = MARGIN_HEIGHT;
		layout.marginWidth = MARGIN_WIDTH;
		layout.horizontalSpacing = SPACING;
		layout.verticalSpacing = SPACING;
		return layout;
	}

	public static GridLayout createGridLayout(int columns, int marginWidth) {
		GridLayout layout = new GridLayout(columns, false);
		layout.marginHeight = WidgetConstants.MARGIN_HEIGHT;

		layout.marginWidth = marginWidth;
		layout.horizontalSpacing = WidgetConstants.SPACING;
		layout.verticalSpacing = WidgetConstants.SPACING;
		return layout;
	}

	public static GridLayout createGridLayout(int columns, int marginWidth, int marginHeight) {
		GridLayout layout = new GridLayout(columns, false);
		layout.marginHeight = WidgetConstants.MARGIN_HEIGHT;

		layout.marginWidth = marginWidth;
		layout.marginHeight = marginHeight;
		layout.horizontalSpacing = WidgetConstants.SPACING;
		layout.verticalSpacing = WidgetConstants.SPACING;
		return layout;
	}

	public static void setGridData(Control control, int hSpan, boolean grabSpace) {
		GridData data = new GridData();
		data.horizontalSpan = hSpan;
		data.grabExcessHorizontalSpace = grabSpace;
		if (control instanceof Text || control instanceof Combo) {
			data.widthHint = MIN_TEXT_WIDTH;
		}
		data.horizontalAlignment = GridData.FILL;
		control.setLayoutData(data);
	}

	public static void setGridData(Control control, int hSpan, int width) {
		GridData data = new GridData();
		data.horizontalSpan = hSpan;
		data.widthHint = width;
		control.setLayoutData(data);
	}

	/**
	 * Creates a GridLayout layout Manager that specified spaces of each border.
	 *
	 * @param columns The number of columns in the grid
	 * @param space   The space.
	 * @return A GridLayout instance.
	 */
	public static GridLayout createSpaceGridLayout(int columns, int space) {
		GridLayout layout = new GridLayout(columns, false);
		layout.marginHeight = space;
		layout.marginWidth = space;
		layout.horizontalSpacing = space;
		layout.verticalSpacing = space;
		return layout;
	}

	public static GridLayout createSpaceGridLayout(int columns, int space, boolean isFormStyle) {
		if (isFormStyle) {
			space += 2;
		}
		GridLayout layout = new GridLayout(columns, false);
		layout.marginHeight = space;
		layout.marginWidth = space;
		layout.horizontalSpacing = space;
		layout.verticalSpacing = space;
		return layout;
	}

	/**
	 * Creates a default FormLayout layout Manager.
	 *
	 * @return A FormLayout instance.
	 */
	public static FormLayout createFormLayout() {
		FormLayout layout = new FormLayout();
		layout.marginHeight = MARGIN_HEIGHT;
		layout.marginWidth = MARGIN_WIDTH;
		layout.spacing = SPACING;
		return layout;
	}

	/**
	 * Creates a default FormLayout layout Manager.
	 *
	 * @param space The space.
	 * @return A FormLayout instance.
	 */
	public static FormLayout createSpaceFormLayout(int space) {
		FormLayout layout = new FormLayout();
		layout.marginHeight = space;
		layout.marginWidth = space;
		layout.spacing = space;
		return layout;
	}

	public static Composite buildGridComposite(Composite parent, int hSpan, boolean grabSpace) {

		Composite composite = new Composite(parent, SWT.NONE);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = hSpan;
		data.grabExcessHorizontalSpace = grabSpace;
		composite.setLayoutData(data);

		return composite;
	}

	/**
	 * Creates a place-holder Label for using in GridLayout.
	 *
	 * @param parent    A widget which will be the parent of the new instance
	 *                  (cannot be null)
	 * @param hSpan     The number of column cells that operable-control will take
	 *                  up.
	 * @param grabSpace grabSpace specifies whether the cell will be made wide
	 *                  enough to fit the remaining horizontal space.
	 * @return The place-holder Label control.
	 */
	public static Label createGridPlaceholder(Composite parent, int hSpan, boolean grabSpace) {
		return createGridPlaceholder(parent, hSpan, grabSpace, false);
	}

	public static Label createGridPlaceholder(Composite parent, int hSpan, boolean grabSpace, boolean isFormStyle) {
		Label label = isFormStyle ? FormWidgetFactory.getInstance().createLabel(parent, true)
				: new Label(parent, SWT.NONE);
		GridData data = new GridData();
		data.horizontalSpan = hSpan;
		data.grabExcessHorizontalSpace = grabSpace;
		label.setLayoutData(data);
		return label;
	}

	/**
	 * Creates a horizontal seperator line.
	 *
	 * @param parent A widget which will be the parent of the new instance (cannot
	 *               be null)
	 * @param hSpan  The number of column cells that operable-control will take up.
	 * @return The Label control.
	 */
	public static Label createHorizontalLine(Composite parent, int hSpan, boolean isFormStyle) {
		Label label = isFormStyle ? FormWidgetFactory.getInstance().createLabel(parent, "", //$NON-NLS-1$
				SWT.SEPARATOR | SWT.HORIZONTAL) : new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData data = new GridData();
		data.horizontalSpan = hSpan;
		data.horizontalAlignment = GridData.FILL;
		label.setLayoutData(data);
		return label;
	}

	/**
	 * Error processor, shows the Error message.
	 *
	 * @param shell the parent window.
	 * @param e     Exception object.
	 *
	 * @deprecated use {@link #processError(Exception)} instead
	 */
	@Deprecated
	public static void processError(Shell shell, Exception e) {
		processError(e);
	}

	/**
	 * Error processor, shows the Error message.
	 *
	 * @param e     Exception object.
	 *
	 * @deprecated use {@link ExceptionUtil#handle(Throwable)} instead.
	 */
	@Deprecated
	public static void processError(Exception e) {
		ExceptionHandler.handle(e);
	}

	public static void setExcludeGridData(Control control, boolean exclude) {
		Object obj = control.getLayoutData();
		if (obj == null) {
			control.setLayoutData(new GridData());
		} else if (!(obj instanceof GridData)) {
			return;
		}
		GridData data = (GridData) control.getLayoutData();
		data.exclude = exclude;
		control.setLayoutData(data);
		control.setVisible(!exclude);
	}

	/**
	 * Sets the span of a control. Assumes that GridData is used.
	 */
	public static void setHorizontalSpan(Control control, int span) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).horizontalSpan = span;
		} else if (span != 1) {
			GridData gd = new GridData();
			gd.horizontalSpan = span;
			control.setLayoutData(gd);
		}
	}

	/**
	 * Sets the width hint of a control. Assumes that GridData is used.
	 */
	public static void setWidthHint(Control control, int widthHint) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).widthHint = widthHint;
		}
	}

	/**
	 * Sets the heightHint hint of a control. Assumes that GridData is used.
	 */
	public static void setHeightHint(Control control, int heightHint) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).heightHint = heightHint;
		}
	}

	/**
	 * Sets the horizontal indent of a control. Assumes that GridData is used.
	 */
	public static void setHorizontalIndent(Control control, int horizontalIndent) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).horizontalIndent = horizontalIndent;
		}
	}

	/**
	 * Sets the horizontal grabbing of a control to true. Assumes that GridData is
	 * used.
	 */
	public static void setHorizontalGrabbing(Control control) {
		Object ld = control.getLayoutData();
		if (ld instanceof GridData) {
			((GridData) ld).grabExcessHorizontalSpace = true;
		}
	}

}
