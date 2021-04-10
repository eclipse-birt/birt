/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.viewer.ViewerPlugin;
import org.eclipse.birt.report.viewer.utilities.AppServerWrapper;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.jface.preference.IPreferencePage;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage </samp>,
 * we can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class PreviewServerPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	protected Text textServerAddr;

	protected Text textServerPort;

	protected Control createContents(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.PREFERENCE_BIRT_PREVIEW_SERVER_ID);
		Font font = parent.getFont();

		// TODO: Need set context sensitive help
		// WorkbenchHelp.setHelp(parent, IHelpUIConstants.PREF_PAGE_APPSERVER);

		Composite mainComposite = new Composite(parent, SWT.NULL);

		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

		GridLayout layout = new GridLayout();

		layout.marginHeight = 0;

		layout.marginWidth = 0;

		layout.numColumns = 2;

		mainComposite.setLayout(layout);

		// Description
		Label label = new Label(mainComposite, SWT.NONE);

		label.setText(Messages.getString("designer.preview.preference.server.description")); //$NON-NLS-1$

		GridData data = new GridData();

		data.horizontalSpan = 2;

		label.setLayoutData(data);

		label.setFont(font);

		// Spacer
		label = new Label(mainComposite, SWT.NONE);

		data = new GridData();

		data.horizontalSpan = 2;

		label.setLayoutData(data);

		label.setFont(font);

		// Host description
		label = new Label(mainComposite, SWT.NONE);

		label.setFont(font);

		label.setText(Messages.getString("designer.preview.preference.server.hostDescription")); //$NON-NLS-1$

		data = new GridData();

		data.horizontalSpan = 2;

		label.setLayoutData(data);

		// Host input, TODO: need validator
		textServerAddr = new Text(mainComposite, SWT.SINGLE | SWT.BORDER);

		data = new GridData();

		data.horizontalAlignment = GridData.FILL;

		data.grabExcessHorizontalSpace = true;

		data.horizontalSpan = 2;

		textServerAddr.setLayoutData(data);

		textServerAddr.setFont(font);

		// Spacer
		label = new Label(mainComposite, SWT.NONE);

		data = new GridData();

		data.horizontalSpan = 2;

		label.setLayoutData(data);

		label.setFont(font);

		// Port description
		label = new Label(mainComposite, SWT.NONE);

		label.setFont(font);

		label.setText(Messages.getString("designer.preview.preference.server.portDescription")); //$NON-NLS-1$

		data = new GridData();

		data.horizontalSpan = 2;

		label.setLayoutData(data);

		label.setFont(font);

		// Port input, TODO: need invalidator
		textServerPort = new Text(mainComposite, SWT.SINGLE | SWT.BORDER);

		textServerPort.setTextLimit(5);

		data = new GridData();

		data.widthHint = convertWidthInCharsToPixels(8);

		data.horizontalAlignment = GridData.FILL;

		data.grabExcessHorizontalSpace = true;

		data.horizontalSpan = 2;

		textServerPort.setLayoutData(data);

		textServerPort.setFont(font);

		// Validation of port field
		// textServerPort.addModifyListener(new ModifyListener() {
		// public void modifyText(ModifyEvent e) {
		// try {
		// int num = Integer.valueOf(textServerPort.getText())
		// .intValue();
		// if (0 <= num && num <= 0xFFFF) {
		// // port is valid
		// AppserverPreferencePage.this.setValid(true);
		// setErrorMessage(null);
		// return;
		// }
		//
		// // port is invalid
		// } catch (NumberFormatException nfe) {
		// }
		// AppserverPreferencePage.this.setValid(false);
		// setErrorMessage(HelpUIResources
		// .getString("AppserverPreferencePage.invalidPort")); //$NON-NLS-1$
		// }
		// });

		// Spacer
		label = new Label(mainComposite, SWT.NONE);

		data = new GridData();

		data.horizontalSpan = 2;

		label.setLayoutData(data);

		label.setFont(font);

		// Note
		label = new Label(mainComposite, SWT.NONE);

		label.setText(Messages.getString("designer.preview.preference.server.note")); //$NON-NLS-1$

		FontData[] fontData = font.getFontData();

		for (int i = 0; i < fontData.length; i++) {
			fontData[i].setStyle(fontData[i].getStyle() | SWT.BOLD);
		}

		final Font boldFont = new Font(label.getDisplay(), fontData);

		label.setFont(boldFont);

		label.addDisposeListener(new DisposeListener() {

			public void widgetDisposed(DisposeEvent e) {
				boldFont.dispose();
			}
		});

		data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);

		label.setLayoutData(data);

		// Require restart
		label = new Label(mainComposite, SWT.NONE);

		label.setText(Messages.getString("designer.preview.preference.server.requireRestart")); //$NON-NLS-1$

		data = new GridData();

		label.setLayoutData(data);

		label.setFont(font);

		// Initialize preference
		Preferences pref = ViewerPlugin.getDefault().getPluginPreferences();

		textServerAddr.setText(pref.getString(AppServerWrapper.HOST_KEY));

		textServerPort.setText(pref.getString(AppServerWrapper.PORT_KEY));

		return mainComposite;
	}

	public void init(IWorkbench workbench) {
		;
	}

	/**
	 * Performs special processing when this page's Defaults button has been
	 * pressed.
	 * <p>
	 * This is a framework hook method for sublcasses to do special things when the
	 * Defaults button has been pressed. Subclasses may override, but should call
	 * <code>super.performDefaults</code>.
	 * </p>
	 */
	protected void performDefaults() {
		Preferences pref = ViewerPlugin.getDefault().getPluginPreferences();

		textServerAddr.setText(pref.getDefaultString(AppServerWrapper.HOST_KEY));

		textServerPort.setText(pref.getDefaultString(AppServerWrapper.PORT_KEY));

		super.performDefaults();
	}

	/**
	 * @see IPreferencePage
	 */
	public boolean performOk() {
		Preferences pref = ViewerPlugin.getDefault().getPluginPreferences();

		pref.setValue(AppServerWrapper.HOST_KEY, textServerAddr.getText());

		pref.setValue(AppServerWrapper.PORT_KEY, textServerPort.getText());

		ViewerPlugin.getDefault().savePluginPreferences();

		return true;
	}

}