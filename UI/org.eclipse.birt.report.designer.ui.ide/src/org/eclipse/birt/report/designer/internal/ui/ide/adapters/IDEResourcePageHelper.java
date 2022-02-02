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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.AbstractDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.ide.dialog.StringVariableSelectionDialog;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

/**
 * 
 */

public class IDEResourcePageHelper extends AbstractDialogHelper {
	public static final String WOKSPACE_BUTTON = Messages.getString("IDEResourcePageHelper.WorkSpace.Button"); //$NON-NLS-1$
	public static final String FILESYSTEM_BUTTON = Messages.getString("IDEResourcePageHelper.File.Button"); //$NON-NLS-1$
	public static final String VARIABLES_BUTTON = Messages.getString("IDEResourcePageHelper.Varible.Button"); //$NON-NLS-1$
	private static final String DirectoryDialog_Text = Messages.getString("IDEResourcePageHelper.Dialog.Title"); //$NON-NLS-1$
	private static final String DirectoryDialog_Message = Messages.getString("IDEResourcePageHelper.Dialog.Prompt"); //$NON-NLS-1$
	private static final String ContainerSelectionDialog_Message = Messages
			.getString("IDEResourcePageHelper.Dialog.Text"); //$NON-NLS-1$
	private String location = ""; //$NON-NLS-1$
	private Button fVariablesButton;
	private Button fFileSystemButton;
	private Button fWorkspaceButton;
	private Control control;
	private int butonAlignment = SWT.END;
	private ButtonListener fListener = new ButtonListener();
	private String[] buttonLabels = new String[] { WOKSPACE_BUTTON, FILESYSTEM_BUTTON, VARIABLES_BUTTON };

	public void setButonAlignment(int butonAlignment) {
		this.butonAlignment = butonAlignment;
	}

	public void setButtonLabels(String[] buttonLabels) {
		this.buttonLabels = buttonLabels;
	}

	class ButtonListener extends SelectionAdapter {

		public void widgetSelected(SelectionEvent e) {
			Object source = e.getSource();
			if (source == fFileSystemButton) {
				handleBrowseFileSystem();
			} else if (source == fWorkspaceButton) {
				handleBrowseWorkspace();
			} else if (source == fVariablesButton) {
				handleInsertVariable();
			}
		}
	}

	@Override
	public void createContent(Composite parent) {
		Composite buttons = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(buttonLabels.length, false);
		layout.marginHeight = layout.marginWidth = 0;
		buttons.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		buttons.setLayoutData(gd);

		this.control = buttons;
		createButtons(buttons, buttonLabels);
	}

	@Override
	public Control getControl() {
		return control;
	}

	protected void createButtons(Composite parent, String[] buttonLabels) {
		Button button = createButton(parent, buttonLabels[0]);
		populateButton(button, buttonLabels[0]);
		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = butonAlignment;
		button.setLayoutData(gd);

		for (int i = 1; i < buttonLabels.length; i++) {
			populateButton(createButton(parent, buttonLabels[i]), buttonLabels[i]);
		}
	}

	private void populateButton(Button button, String label) {
		if (WOKSPACE_BUTTON.equals(label)) {
			fWorkspaceButton = button;
		} else if (FILESYSTEM_BUTTON.equals(label)) {
			fFileSystemButton = button;
		} else if (VARIABLES_BUTTON.equals(label)) {
			fVariablesButton = button;
		}
	}

	protected Button createButton(Composite parent, String text) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(text);
		button.setLayoutData(new GridData());
		button.addSelectionListener(fListener);
		StringVariableSelectionDialog.setButtonDimensionHint(button);
		return button;
	}

	protected void handleBrowseFileSystem() {
		DirectoryDialog dialog = new DirectoryDialog(getControl().getShell());
		dialog.setFilterPath(getLocation());
		dialog.setText(DirectoryDialog_Text);
		dialog.setMessage(DirectoryDialog_Message);
		String result = dialog.open();
		if (result != null) {
			// fLocationText.setText(result);
			location = result;
			result = replaceString(result);
			notifyTextChange(result);
		}
	}

	private String replaceString(String str) {
		String retValue = str.replace('\\', '/'); // $NON-NLS-1$ //$NON-NLS-2$
		if (!retValue.endsWith("/")) //$NON-NLS-1$
		{
			retValue = retValue + "/"; //$NON-NLS-1$
		}
		return retValue;
	}

	protected void handleBrowseWorkspace() {
		ContainerSelectionDialog dialog = new ContainerSelectionDialog(getControl().getShell(),
				ResourcesPlugin.getWorkspace().getRoot(), true, ContainerSelectionDialog_Message);
		if (dialog.open() == Window.OK) {
			Object[] result = dialog.getResult();
			if (result.length == 0)
				return;
			IPath path = (IPath) result[0];
			// fLocationText.setText("${workspace_loc:" + path.makeRelative().toString() +
			// "}"); //$NON-NLS-1$ //$NON-NLS-2$
			notifyTextChange("${workspace_loc:" //$NON-NLS-1$
					+ path.makeRelative().toString() + "}"); //$NON-NLS-1$
		}
	}

	private void handleInsertVariable() {
		StringVariableSelectionDialog dialog = new StringVariableSelectionDialog(getControl().getShell());
		if (dialog.open() == Window.OK)
			notifyTextChange(dialog.getVariableExpression());
	}

	private String getLocation() {
		return location;
	}

	private void notifyTextChange(String text) {
		Event event = new Event();
		event.text = text;
		List<Listener> list = listeners.get(SWT.Selection);
		for (int i = 0; i < list.size(); i++) {
			Listener listener = list.get(i);
			listener.handleEvent(event);
		}
	}
}
