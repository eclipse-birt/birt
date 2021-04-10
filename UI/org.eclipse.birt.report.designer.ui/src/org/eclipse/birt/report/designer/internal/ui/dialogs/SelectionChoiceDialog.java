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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 * Dialog to add and edit selection choice for parameter
 */

public class SelectionChoiceDialog extends BaseDialog {

	public static interface ISelectionChoiceValidator {

		String validate(String displayLabelKey, String displayLabel, String value);
	}

	private Text labelEditor;

	private Text valueEditor;

	private SelectionChoice selectionChoice;

	private CLabel messageLine;

	private ISelectionChoiceValidator validator;

	private Text resourceText;

	private Button removeBtn;

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	public static final String SELECTON_CHOICE_HELPER_KEY = "Selection Choice Dialog Helper";//$NON-NLS-1$

	public static final String VALUE = "Value"; //$NON-NLS-1$

	public static final String CAN_EMPTY = "Can Use Empty"; //$NON-NLS-1$

	public static final String CAN_NULL = "Can Use NULL"; //$NON-NLS-1$

	private boolean canUseEmptyValue = false;

	private boolean canUseNullValue = false;

	private IDialogHelper helper;

	public SelectionChoiceDialog(String title) {
		this(UIUtil.getDefaultShell(), title);
	}

	public SelectionChoiceDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	public SelectionChoiceDialog(String title, boolean canUseNullValue, boolean canUseEmptyValue) {
		this(title);
		this.canUseEmptyValue = canUseEmptyValue;
		this.canUseNullValue = canUseNullValue;
	}

	protected boolean initDialog() {
		Assert.isNotNull(selectionChoice);
		labelEditor.setText(UIUtil.convertToGUIString(selectionChoice.getLabel()));
		resourceText.setText(UIUtil.convertToGUIString(selectionChoice.getLabelResourceKey()));
		if (validator != null) {
			updateStatus();
		}
		return true;
	}

	protected Control createDialogArea(Composite parent) {
		String[] labels = new String[] { Messages.getString("ParameterDialog.SelectionDialog.Label.DisplayTextKey"), //$NON-NLS-1$
				Messages.getString("ParameterDialog.SelectionDialog.Label.DisplayText"), //$NON-NLS-1$
				Messages.getString("ParameterDialog.SelectionDialog.Label.Value") //$NON-NLS-1$
		};
		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(4, false);
		layout.marginWidth = 15;
		layout.marginHeight = 15;
		composite.setLayout(layout);
		new Label(composite, SWT.NONE).setText(labels[0]);
		resourceText = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 200;
		resourceText.setLayoutData(gd);
		resourceText.setEditable(false);
		Button resourceBtn = new Button(composite, SWT.PUSH);
		resourceBtn.setText(Messages.getString("ParameterDialog.SelectionDialog.Button.Resource")); //$NON-NLS-1$
		resourceBtn.setToolTipText(Messages.getString("ParameterDialog.SelectionDialog.Button.Resource.Tooltip")); //$NON-NLS-1$
		resourceBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleBrowserSelectedEvent();
			}
		});
		resourceBtn.setEnabled(enableResourceKey());

		removeBtn = new Button(composite, SWT.NONE);
		removeBtn.setImage(ReportPlatformUIImages.getImage(ISharedImages.IMG_TOOL_DELETE));
		removeBtn.setToolTipText(Messages.getString("ParameterDialog.SelectionDialog.Button.Remove.Tooltip")); //$NON-NLS-1$
		removeBtn.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				resourceText.setText(EMPTY_STRING);
				labelEditor.setText(EMPTY_STRING);
				updateRemoveBtnState();
			}
		});

		new Label(composite, SWT.NONE).setText(labels[1]);
		labelEditor = new Text(composite, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 200;
		gd.horizontalSpan = 3;
		labelEditor.setLayoutData(gd);

		new Label(composite, SWT.NONE).setText(labels[2]);
		createValuePart(composite);

		final Composite noteContainer = new Composite(composite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		gd.widthHint = UIUtil.getMaxStringWidth(labels, composite) + 200 + layout.horizontalSpacing * 2
				+ resourceBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		noteContainer.setLayoutData(gd);

		layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		noteContainer.setLayout(layout);

		Label note = new Label(noteContainer, SWT.WRAP);
		note.setText(Messages.getString("ParameterDialog.SelectionDialog.Label.Note")); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = UIUtil.getMaxStringWidth(labels, composite) + 200 + layout.horizontalSpacing * 2
				+ resourceBtn.computeSize(SWT.DEFAULT, SWT.DEFAULT).x;
		note.setLayoutData(gd);

		messageLine = new CLabel(composite, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		messageLine.setLayoutData(gd);
		if (validator != null) {
			ModifyListener listener = new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					updateStatus();
				}

			};
			labelEditor.addModifyListener(listener);
			if (getValueControl() instanceof Text)
				((Text) getValueControl()).addModifyListener(listener);
			if (getValueControl() instanceof Combo)
				((Combo) getValueControl()).addModifyListener(listener);
			if (getValueControl() instanceof CCombo)
				((CCombo) getValueControl()).addModifyListener(listener);

		}

		UIUtil.bindHelp(composite, IHelpContextIds.SELECTION_CHOICE_DIALOG);

		return composite;
	}

	protected void okPressed() {
		selectionChoice.setLabel(UIUtil.convertToModelString(labelEditor.getText(), false));
		selectionChoice.setValue(getValueValue());
		selectionChoice.setLabelResourceKey(UIUtil.convertToModelString(resourceText.getText(), false));
		setResult(selectionChoice);
		super.okPressed();
	}

	private void updateStatus() {
		if (helper != null)
			helper.update(false);
		String erroeMessage = validator.validate(UIUtil.convertToModelString(resourceText.getText(), false),
				UIUtil.convertToModelString(labelEditor.getText(), false), getValueValue());
		if (erroeMessage != null) {
			messageLine.setText(erroeMessage);
			messageLine.setImage(ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
			getOkButton().setEnabled(false);
		} else {
			messageLine.setText(""); //$NON-NLS-1$
			messageLine.setImage(null);
			getOkButton().setEnabled(true);
		}
		updateRemoveBtnState();
	}

	public void setInput(SelectionChoice selectionChoice) {
		this.selectionChoice = selectionChoice;
	}

	public void setValidator(ISelectionChoiceValidator validator) {
		this.validator = validator;
	}

	private String[] getBaseNames() {
		List<String> resources = SessionHandleAdapter.getInstance().getReportDesignHandle().getIncludeResources();
		if (resources == null)
			return null;
		else
			return resources.toArray(new String[0]);
	}

	private URL[] getResourceURLs() {
		String[] baseNames = getBaseNames();
		if (baseNames == null)
			return null;
		else {
			URL[] urls = new URL[baseNames.length];
			for (int i = 0; i < baseNames.length; i++) {
				urls[i] = SessionHandleAdapter.getInstance().getReportDesignHandle().findResource(baseNames[i],
						IResourceLocator.MESSAGE_FILE);
			}
			return urls;
		}
	}

	private boolean enableResourceKey() {
		URL[] resources = getResourceURLs();
		String[] path = null;
		try {
			if (resources != null && resources.length > 0) {
				path = new String[resources.length];
				for (int i = 0; i < path.length; i++) {
					path[i] = DEUtil.getFilePathFormURL(resources[i]);
				}
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		if (resources == null || path == null || path.length == 0) {
			return false;
		} else {
			boolean flag = false;
			for (int i = 0; i < path.length; i++) {
				if (path[i] != null && new File(path[i]).exists()) {
					flag = true;
					break;
				}
			}
			return flag;
		}
	}

	protected void handleBrowserSelectedEvent() {
		ResourceEditDialog dlg = new ResourceEditDialog(getShell(),
				Messages.getString("ResourceKeyDescriptor.title.SelectKey")); //$NON-NLS-1$

		dlg.setResourceURLs(getResourceURLs());

		if (dlg.open() == Window.OK) {
			handleSelectedEvent((String[]) dlg.getDetailResult());
		}
	}

	private void handleSelectedEvent(String[] values) {
		if (values.length == 2) {
			if (values[0] != null)
				resourceText.setText(values[0]);
			if (values[1] != null)
				labelEditor.setText(values[1]);
			updateRemoveBtnState();
		}
	}

	private void updateRemoveBtnState() {
		removeBtn.setEnabled(resourceText.getText().equals(EMPTY_STRING) ? false : true);
	}

	private void createValuePart(Composite parent) {
		Object[] helperProviders = ElementAdapterManager.getAdapters(selectionChoice, IDialogHelperProvider.class);
		if (helperProviders != null) {
			for (int i = 0; i < helperProviders.length; i++) {
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if (helperProvider != null && helper == null) {
					helper = helperProvider.createHelper(this, SELECTON_CHOICE_HELPER_KEY);
					if (helper != null) {
						helper.setProperty(VALUE, selectionChoice.getValue());
						helper.setProperty(CAN_EMPTY, canUseEmptyValue);
						helper.setProperty(CAN_NULL, canUseNullValue);
						helper.createContent(parent);
						helper.addListener(SWT.Modify, new Listener() {

							public void handleEvent(Event event) {
								helper.update(false);
							}
						});
						helper.update(true);
					}
				}
			}
		}
		if (helper == null) {
			valueEditor = new Text(parent, SWT.BORDER);
			valueEditor.setText(UIUtil.convertToGUIString(selectionChoice.getValue()));
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 3;
			valueEditor.setLayoutData(gd);
			valueEditor.setFocus();
		} else {
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalSpan = 3;
			helper.getControl().setLayoutData(gd);
			helper.getControl().setFocus();
		}
	}

	private Control getValueControl() {
		if (helper == null) {
			return valueEditor;
		} else {

			return helper.getControl();
		}
	}

	private String getValueValue() {
		if (helper == null) {
			return valueEditor.getText();
		} else {
			return (String) helper.getProperty(VALUE);
		}
	}
}
