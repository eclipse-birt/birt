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

package org.eclipse.birt.report.designer.ui.preferences;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.util.PixelConverter;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 */
public class CommentTemplatesConfigurationBlock extends OptionsConfigurationBlock {

	private static final String ENABLE_BUTTON = Messages
			.getString("org.eclipse.birt.report.designer.ui.preference.commenttemplates.enablecomment"); //$NON-NLS-1$
	private final Key PREF_ENABLE_COMMENT = getReportKey(ReportPlugin.ENABLE_COMMENT_PREFERENCE);
	private final Key PREF_COMMENT_TEMPLATES = getReportKey(ReportPlugin.COMMENT_PREFERENCE);
	private PixelConverter fPixelConverter;

	public CommentTemplatesConfigurationBlock(IStatusChangeListener context, IProject project) {
		super(context, ReportPlugin.getDefault(), project);
		setKeys(getKeys());
	}

	private Key[] getKeys() {
		Key[] keys = new Key[] { PREF_ENABLE_COMMENT, PREF_COMMENT_TEMPLATES };
		return keys;
	}

	/*
	 * @see org.eclipse.jface.preference.PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		fPixelConverter = new PixelConverter(parent);
		setShell(parent.getShell());

		Composite mainComp = new Composite(parent, SWT.NONE);
		mainComp.setFont(parent.getFont());
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainComp.setLayout(layout);

		Composite othersComposite = createBuildPathTabContent(mainComp);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true, true);
		gridData.heightHint = fPixelConverter.convertHeightInCharsToPixels(20);
		othersComposite.setLayoutData(gridData);

		validateSettings(null, null, null);

		return mainComp;
	}

	private static final String ENABLED = "true"; //$NON-NLS-1$
	private static final String DISABLED = "false"; //$NON-NLS-1$
	private Button enableButton;
	private Text commentText;

	private Composite createBuildPathTabContent(Composite parent) {
		String[] enableDisableValues = new String[] { ENABLED, DISABLED };

		Composite pageContent = new Composite(parent, SWT.NONE);

		GridData data = new GridData(
				GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL | GridData.VERTICAL_ALIGN_BEGINNING);
		data.grabExcessHorizontalSpace = true;
		pageContent.setLayoutData(data);

		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.numColumns = 3;
		pageContent.setLayout(layout);

		enableButton = addCheckBox(pageContent, ENABLE_BUTTON, PREF_ENABLE_COMMENT, enableDisableValues, 0);
		commentText = addTextField(pageContent, null, PREF_COMMENT_TEMPLATES, 0, 0,
				SWT.BORDER | SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
		data = new GridData(GridData.FILL_VERTICAL);
		data.horizontalSpan = 3;
		data.widthHint = 400;
		commentText.setLayoutData(data);

		enableButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleChangeCommentText();
			}
		});

		handleChangeCommentText();
		return pageContent;
	}

	private void handleChangeCommentText() {
		if (enableButton == null || commentText == null)
			return;
		if (enableButton.getSelection() && enableButton.isEnabled()) {
			commentText.setEditable(true);
			commentText.setEnabled(true);
		} else {
			commentText.setEnabled(false);
			commentText.setEditable(false);
		}
	}

	/*
	 * (non-javadoc) Update fields and validate. @param changedKey Key that changed,
	 * or null, if all changed.
	 */

	public void performDefaults() {
		super.performDefaults();
		handleChangeCommentText();
	}

	public void useProjectSpecificSettings(boolean enable) {
		super.useProjectSpecificSettings(enable);
		handleChangeCommentText();
	}
}
