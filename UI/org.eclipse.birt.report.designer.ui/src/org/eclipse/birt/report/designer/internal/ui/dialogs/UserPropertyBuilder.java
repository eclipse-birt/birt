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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 * 
 */

public class UserPropertyBuilder extends BaseDialog {

	public static final int USER_PROPERTY = 0;

	public static final int NAMED_EXPRESSION = 1;

	private static final String PROPERTY_TITLE = Messages.getString("UserPropertyBuilder.0"); //$NON-NLS-1$
	private static final String EXPRESSION_TITLE = Messages.getString("UserPropertyBuilder.Title.NamedExpression"); //$NON-NLS-1$

	private static final String ERROR_MSG_NAME_IS_REQUIRED = Messages
			.getString("UserPropertyBuilder.ErrorMessage.NoName"); //$NON-NLS-1$
	private static final String ERROR_MSG_NAME_DUPLICATED = Messages
			.getString("UserPropertyBuilder.ErrorMessage.DuplicatedName"); //$NON-NLS-1$

	private static final String LABEL_PROPERTY_NAME = Messages.getString("UserPropertyBuilder.Label.PropertyName"); //$NON-NLS-1$

	private static final String LABEL_EXPRESSION_NAME = Messages.getString("UserPropertyBuilder.Label.ExpressionName"); //$NON-NLS-1$

	private static final String LABEL_TYPE = Messages.getString("UserPropertyBuilder.Label.PropertyType"); //$NON-NLS-1$

	private static final String LABEL_DEFAULT_VALUE = Messages.getString("UserPropertyBuilder.Label.DefaultValue"); //$NON-NLS-1$

	private static final Image ERROR_ICON = ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK);

	private static PropertyType[] PROPERTY_TYPES;

	private static PropertyType EXPRESSION_TYPE;

	static {
		List typeList = new ArrayList(UserPropertyDefn.getAllowedTypes());
		EXPRESSION_TYPE = DEUtil.getMetaDataDictionary().getPropertyType(PropertyType.EXPRESSION_TYPE);
		typeList.remove(EXPRESSION_TYPE);
		PROPERTY_TYPES = (PropertyType[]) typeList.toArray(new PropertyType[0]);
	}

	private DesignElementHandle input;

	private int style;

	private Text nameEditor, defaultValueEditor;
	private Combo typeChooser;
	private CLabel messageLine;

	public UserPropertyBuilder(int style) {
		super(UIUtil.getDefaultShell(), ""); //$NON-NLS-1$
		switch (this.style = style) {
		case USER_PROPERTY:
			setTitle(PROPERTY_TITLE);
			break;
		case NAMED_EXPRESSION:
			setTitle(EXPRESSION_TITLE);
			break;
		}
	}

	protected boolean initDialog() {
		switch (style) {
		case USER_PROPERTY:
			typeChooser.setText(new UserPropertyDefn().getType().getDisplayName());
			break;
		case NAMED_EXPRESSION:
		}
		checkName();
		return super.initDialog();
	}

	protected Control createDialogArea(Composite parent) {

		switch (style) {
		case USER_PROPERTY:
			UIUtil.bindHelp(parent, IHelpContextIds.ADD_EDIT_USER_PROPERTIES_DIALOG_ID);
			break;
		case NAMED_EXPRESSION:
			UIUtil.bindHelp(parent, IHelpContextIds.ADD_EDIT_NAMED_EXPRESSION_DIALOG_ID);
			break;
		}

		Composite composite = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = layout.marginWidth = 10;
		composite.setLayout(layout);

		messageLine = new CLabel(composite, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		messageLine.setLayoutData(gd);

		switch (style) {
		case USER_PROPERTY:
			new Label(composite, SWT.NONE).setText(LABEL_PROPERTY_NAME);
			break;
		case NAMED_EXPRESSION:
			new Label(composite, SWT.NONE).setText(LABEL_EXPRESSION_NAME);
			break;
		}
		nameEditor = new Text(composite, SWT.BORDER | SWT.SINGLE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 200;
		nameEditor.setLayoutData(gd);
		nameEditor.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				checkName();
			}
		});

		switch (style) {
		case USER_PROPERTY:
			new Label(composite, SWT.NONE).setText(LABEL_TYPE);
			typeChooser = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
			typeChooser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			typeChooser.setVisibleItemCount(30);
			for (int i = 0; i < PROPERTY_TYPES.length; i++) {
				typeChooser.add(PROPERTY_TYPES[i].getDisplayName(), i);
			}
			break;
		case NAMED_EXPRESSION:
			new Label(composite, SWT.NONE).setText(LABEL_DEFAULT_VALUE);
			Composite subComposite = new Composite(composite, SWT.NONE);
			subComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			subComposite.setLayout(UIUtil.createGridLayoutWithoutMargin(2, false));

			defaultValueEditor = new Text(subComposite, SWT.BORDER | SWT.MULTI);
			gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.heightHint = defaultValueEditor.computeSize(SWT.DEFAULT, SWT.DEFAULT).y
					- defaultValueEditor.getBorderWidth() * 2;
			defaultValueEditor.setLayoutData(gd);

			createComplexExpressionButton(subComposite, defaultValueEditor);

		}
		return composite;
	}

	private void createComplexExpressionButton(Composite parent, final Text text) {
		ExpressionButtonUtil.createExpressionButton(parent, text, new ExpressionProvider(input), input);
	}

	protected void okPressed() {
		UserPropertyDefn def = new UserPropertyDefn();
		def.setName(nameEditor.getText().trim());
		switch (style) {
		case USER_PROPERTY:
			def.setType(PROPERTY_TYPES[typeChooser.getSelectionIndex()]);
			break;
		case NAMED_EXPRESSION:
			def.setType(EXPRESSION_TYPE);
			def.setDefault(ExpressionButtonUtil.getExpression(defaultValueEditor));
			break;
		}
		setResult(def);
		super.okPressed();
	}

	public void setInput(DesignElementHandle handle) {
		input = handle;
	}

	private void checkName() {
		String errorMessage = null;
		String name = nameEditor.getText().trim();
		if (name.length() == 0) {
			errorMessage = ERROR_MSG_NAME_IS_REQUIRED;
			messageLine.setImage(null);

		} else if (input.getPropertyHandle(name) != null) {
			errorMessage = ERROR_MSG_NAME_DUPLICATED;
			messageLine.setImage(ERROR_ICON);
		}

		if (errorMessage != null) {
			messageLine.setText(errorMessage);
		} else {
			messageLine.setText(""); //$NON-NLS-1$
			messageLine.setImage(null);
		}
		getOkButton().setEnabled(errorMessage == null);
	}

}
