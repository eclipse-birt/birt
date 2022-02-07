/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author Actuate Corporation
 * 
 */
public class FormatSpecifierDialog extends TrayDialog {

	private transient FormatSpecifierComposite editor = null;

	private transient FormatSpecifier formatspecifier = null;

	private AxisType[] axisTypes = null;

	/**
	 * 
	 * @param shellParent     dialog shell
	 * @param formatspecifier format model
	 * @param sTitle          this argument is obsolete
	 */
	public FormatSpecifierDialog(Shell shellParent, FormatSpecifier formatspecifier, String sTitle) {
		super(shellParent);
		setHelpAvailable(false);
		if (formatspecifier != null) {
			this.formatspecifier = formatspecifier.copyInstance();
		}
	}

	/**
	 * 
	 * @param shellParent     dialog shell
	 * @param formatspecifier format model
	 * @param sTitle          this argument is obsolete
	 * @param axisType        Axis type to indicate supported data type.
	 * @since 2.2
	 */
	public FormatSpecifierDialog(Shell shellParent, FormatSpecifier formatspecifier, AxisType axisType, String sTitle) {
		this(shellParent, formatspecifier, new AxisType[] { axisType }, sTitle);
	}

	/**
	 * 
	 * @param shellParent     dialog shell
	 * @param formatspecifier format model
	 * @param sTitle          this argument is obsolete
	 * @param axisTypes       Axis types to indicate supported data types. Null
	 *                        means all types are supported.
	 * @since 2.2
	 */
	public FormatSpecifierDialog(Shell shellParent, FormatSpecifier formatspecifier, AxisType[] axisTypes,
			String sTitle) {
		this(shellParent, formatspecifier, sTitle);
		if (axisTypes != null && axisTypes.length == 1 && axisTypes[0] == null) {
			this.axisTypes = null;
		} else {
			this.axisTypes = axisTypes;
		}
	}

	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
	}

	protected Control createContents(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.DIALOG_EDIT_FORMAT);
		getShell().setText(Messages.getString("FormatSpecifierDialog.Title.EditFormat")); //$NON-NLS-1$
		UIHelper.centerOnScreen(getShell());
		return super.createContents(parent);
	}

	protected Control createDialogArea(Composite parent) {
		if (axisTypes == null) {
			editor = new FormatSpecifierComposite(parent, SWT.NONE, formatspecifier);
		} else {
			List<String> supportedList = new ArrayList<String>();
			for (int i = 0; i < axisTypes.length; i++) {
				// If NOT exist, append it into supported types.
				String type = getSupportedType(axisTypes[i]);
				if (!supportedList.contains(type)) {
					supportedList.add(type);
				}
			}
			editor = new FormatSpecifierComposite(parent, SWT.NONE, formatspecifier,
					supportedList.toArray(new String[supportedList.size()]));
		}
		GridData gdEditor = new GridData(GridData.FILL_BOTH);
		editor.setLayoutData(gdEditor);

		return editor;
	}

	public FormatSpecifier getFormatSpecifier() {
		return formatspecifier;
	}

	protected void okPressed() {
		formatspecifier = editor.getFormatSpecifier();
		super.okPressed();
	}

	String getSupportedType(AxisType axisType) {
		if (axisType == AxisType.DATE_TIME_LITERAL) {
			return FormatSpecifierComposite.DATA_TYPE_DATETIME;
		}
		if (axisType == AxisType.LINEAR_LITERAL || axisType == AxisType.LOGARITHMIC_LITERAL) {
			return FormatSpecifierComposite.DATA_TYPE_NUMBER;
		}
		if (axisType == AxisType.TEXT_LITERAL) {
			return FormatSpecifierComposite.DATA_TYPE_STRING;
		}
		return FormatSpecifierComposite.DATA_TYPE_NONE;
	}

}
