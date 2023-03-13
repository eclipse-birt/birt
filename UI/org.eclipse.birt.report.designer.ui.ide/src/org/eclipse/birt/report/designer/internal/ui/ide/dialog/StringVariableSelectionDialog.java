/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.ide.dialog;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.variables.IStringVariable;
import org.eclipse.core.variables.VariablesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;

/**
 * Dialog to select a variable
 */

public class StringVariableSelectionDialog extends ElementListSelectionDialog {
	private Text fDescriptionText;
	private static final String TITLE = Messages.getString("StringVariableSelectionDialog.Dialog.Title"); //$NON-NLS-1$
	private static final String MESSAGE = Messages.getString("StringVariableSelectionDialog.Dialog.Prompt"); //$NON-NLS-1$

	private static final String DESCRIPTION_LABEL = Messages.getString("StringVariableSelectionDialog.Dialog.Label"); //$NON-NLS-1$

	/**
	 * Constructs a new string substitution variable selection dialog.
	 *
	 * @param parent parent shell
	 */
	public StringVariableSelectionDialog(Shell parent) {
		super(parent, new StringVariableLabelProvider());
		setHelpAvailable(false);
		setShellStyle(getShellStyle() | SWT.RESIZE);
		setTitle(TITLE);
		setMessage(MESSAGE);
		setMultipleSelection(false);
		setElements(VariablesPlugin.getDefault().getStringVariableManager().getVariables());
	}

	/**
	 * Returns the variable expression the user generated from this dialog, or
	 * <code>null</code> if none.
	 *
	 * @return variable expression the user generated from this dialog, or
	 *         <code>null</code> if none
	 */
	public String getVariableExpression() {
		Object[] selected = getResult();
		if (selected != null && selected.length == 1) {
			IStringVariable variable = (IStringVariable) selected[0];
			StringBuilder buffer = new StringBuilder();
			buffer.append("${"); //$NON-NLS-1$
			buffer.append(variable.getName());

			buffer.append("}"); //$NON-NLS-1$
			return buffer.toString();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createContents(Composite parent) {
		Control ctrl = super.createContents(parent);

		return ctrl;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Control control = super.createDialogArea(parent);
		createArgumentArea((Composite) control);
		UIUtil.bindHelp(parent, IHelpContextIds.PREF_PAGE_RESOURCE_VARIABLES_DIALOG_ID);
		return control;
	}

	/**
	 * Creates an area to display a description of the selected variable and a
	 * button to configure the variable's argument.
	 *
	 * @param parent parent widget
	 */
	private void createArgumentArea(Composite parent) {
		Composite container = createComposite(parent, parent.getFont(), 2, 1, GridData.FILL_HORIZONTAL, 0, 0);
		createHorizontalSpacer(container, 1);

		createWrapLabel(container, DESCRIPTION_LABEL, 2);

		fDescriptionText = new Text(container, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		fDescriptionText.setFont(container.getFont());
		fDescriptionText.setEditable(false);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 50;
		fDescriptionText.setLayoutData(gd);
	}

	/**
	 * Update variable description and argument button enablement.
	 *
	 * @see org.eclipse.ui.dialogs.AbstractElementListSelectionDialog#handleSelectionChanged()
	 */
	@Override
	protected void handleSelectionChanged() {
		super.handleSelectionChanged();
		Object[] objects = getSelectedElements();
		String text = null;
		if (objects.length == 1) {
			IStringVariable variable = (IStringVariable) objects[0];
			text = variable.getDescription();
		}
		if (text == null) {
			text = ""; //$NON-NLS-1$
		}

		fDescriptionText.setText(text);
	}

	/**
	 * @param parent
	 * @param font
	 * @param columns
	 * @param hspan
	 * @param fill
	 * @param marginwidth
	 * @param marginheight
	 * @return
	 */
	public static Composite createComposite(Composite parent, Font font, int columns, int hspan, int fill,
			int marginwidth, int marginheight) {
		Composite g = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(columns, false);
		layout.marginWidth = marginwidth;
		layout.marginHeight = marginheight;
		g.setLayout(layout);
		g.setFont(font);
		GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
		g.setLayoutData(gd);
		return g;
	}

	public static void createHorizontalSpacer(Composite comp, int numlines) {
		Label lbl = new Label(comp, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = numlines;
		lbl.setLayoutData(gd);
	}

	/**
	 * @param parent
	 * @param label
	 * @param image
	 * @param fill
	 * @return
	 */
	public static Button createPushButton(Composite parent, String label, Image image, int fill) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		if (image != null) {
			button.setImage(image);
		}
		if (label != null) {
			button.setText(label);
		}
		GridData gd = new GridData(fill);
		button.setLayoutData(gd);
		setButtonDimensionHint(button);
		return button;
	}

	/**
	 * @param parent
	 * @param label
	 * @param image
	 * @return
	 */
	public static Button createPushButton(Composite parent, String label, Image image) {
		Button button = new Button(parent, SWT.PUSH);
		button.setFont(parent.getFont());
		if (image != null) {
			button.setImage(image);
		}
		if (label != null) {
			button.setText(label);
		}
		GridData gd = new GridData();
		button.setLayoutData(gd);
		setButtonDimensionHint(button);
		return button;
	}

	/**
	 * @param button
	 */
	public static void setButtonDimensionHint(Button button) {
		Assert.isNotNull(button);
		Object gd = button.getLayoutData();
		if (gd instanceof GridData) {
			((GridData) gd).widthHint = getButtonWidthHint(button);
			((GridData) gd).horizontalAlignment = GridData.FILL;
		}
	}

	/**
	 * @param button
	 * @return
	 */
	public static int getButtonWidthHint(Button button) {
		button.setFont(JFaceResources.getDialogFont());
		PixelConverter converter = new PixelConverter(button);
		int widthHint = converter.convertHorizontalDLUsToPixels(IDialogConstants.BUTTON_WIDTH);
		return Math.max(widthHint, button.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x);
	}

	/**
	 * @param parent
	 * @param text
	 * @param hspan
	 * @return
	 */
	public static Label createWrapLabel(Composite parent, String text, int hspan) {
		Label l = new Label(parent, SWT.NONE | SWT.WRAP);
		l.setFont(parent.getFont());
		l.setText(text);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = hspan;
		l.setLayoutData(gd);
		return l;
	}

	static class StringVariableLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof IStringVariable) {
				IStringVariable variable = (IStringVariable) element;
				return variable.getName();
			}
			return super.getText(element);
		}

	}

}
