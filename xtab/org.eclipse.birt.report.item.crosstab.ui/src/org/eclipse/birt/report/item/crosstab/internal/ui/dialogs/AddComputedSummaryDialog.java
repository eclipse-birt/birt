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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 *
 */

public class AddComputedSummaryDialog extends BaseDialog {

	private CrosstabReportItemHandle crosstab = null;
	private final static String TITLE = Messages.getString("AddComputedSummaryDialog.Title");

	private Text nameText, expressionText;
	private Combo dataTypeCmb;
	private CLabel errorLabel;

	private String name;
	private Expression expression;
	private String dataType;

	protected static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary()
			.getStructure(ComputedColumn.COMPUTED_COLUMN_STRUCT).getMember(ComputedColumn.DATA_TYPE_MEMBER)
			.getAllowedChoices();
	protected String[] dataTypes = ChoiceSetFactory.getDisplayNamefromChoiceSet(DATA_TYPE_CHOICE_SET);
	protected static final IChoice[] DATA_TYPE_CHOICES = DATA_TYPE_CHOICE_SET.getChoices(null);

	public AddComputedSummaryDialog(Shell parentShell, CrosstabReportItemHandle crosstab) {
		super(parentShell, TITLE);
		this.crosstab = crosstab;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent)

	{
		Composite parentComposite = (Composite) super.createDialogArea(parent);

		Composite composite = new Composite(parentComposite, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		composite.setLayoutData(gridData);
		composite.setLayout(layout);

		Label nameLabel = new Label(composite, SWT.NONE);
		nameLabel.setText(Messages.getString("AddComputedSummaryDialog.Label.Name"));

		nameText = new Text(composite, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 200;
		gridData.horizontalSpan = 2;
		nameText.setLayoutData(gridData);
		nameText.addModifyListener(modifyListener);

		Label dataTypeLb = new Label(composite, SWT.NONE);
		dataTypeLb.setText(Messages.getString("AddComputedSummaryDialog.Label.DataType"));

		dataTypeCmb = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.horizontalSpan = 2;
		dataTypeCmb.setLayoutData(gridData);
		dataTypeCmb.setVisibleItemCount(30);
		Label expressionLabel = new Label(composite, SWT.NONE);
		expressionLabel.setText(Messages.getString("AddComputedSummaryDialog.Label.Expression"));

		expressionText = new Text(composite, SWT.BORDER | SWT.MULTI);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.heightHint = expressionText.computeSize(SWT.DEFAULT, SWT.DEFAULT).y
				- expressionText.getBorderWidth() * 2;
		expressionText.setLayoutData(gridData);
		expressionText.addModifyListener(modifyListener);

		ExpressionButtonUtil.createExpressionButton(composite, expressionText,
				new CrosstabComputedMeasureExpressionProvider(crosstab.getModelHandle()), crosstab.getModelHandle());

		Label seperator = new Label(parentComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		seperator.setLayoutData(gridData);

		errorLabel = new CLabel(parentComposite, SWT.NONE);
		errorLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.bindHelp(parent, IHelpContextIds.ADD_COMPUTED_SUMMARY_DIALOG_ID);
		return parentComposite;
	}

	@Override
	protected Control createContents(Composite parent) {
		Control contents = super.createContents(parent);
		initialize();
		validate();
		return contents;
	}

	protected void initialize() {
		dataTypeCmb.setItems(dataTypes);
		if (dataTypeCmb.getItemCount() > 0) {
			for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
				if (DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals(DATA_TYPE_CHOICES[i].getValue())) {
					dataTypeCmb.select(i);
					return;
				}
			}
			dataTypeCmb.select(0);
		}
	}

	private ModifyListener modifyListener = new ModifyListener() {

		@Override
		public void modifyText(ModifyEvent e) {
			// TODO Auto-generated method stub
			validate();
		}
	};

	protected void validate() {
		boolean ok = true;
		final String EMPTY_STRING = "";
		String errorMessage = EMPTY_STRING;
		String name = nameText.getText().trim();
		String expression = expressionText.getText().trim();
		if (name.length() == 0) {
			errorMessage = Messages.getString("AddComputedSummaryDialog.ErrMsg.Msg1");
		} else if (crosstab.getMeasure(name) != null) {
			errorMessage = Messages.getString("AddComputedSummaryDialog.ErrMsg.Msg2");
		} else if (expression.length() == 0) {
			errorMessage = Messages.getString("AddComputedSummaryDialog.ErrMsg.Msg3");
		}

		if (!errorMessage.equals(EMPTY_STRING)) {
			ok = false;
		}

		getOkButton().setEnabled(ok);

		if ((errorLabel != null) && (!errorLabel.isDisposed())) {
			errorLabel.setText(errorMessage);
			if (ok) {
				errorLabel.setImage(null);
			} else {
				errorLabel.setImage(ReportPlatformUIImages.getImage(ISharedImages.IMG_OBJS_ERROR_TSK));
			}
		}

	}

	public String getName() {
		return name;
	}

	public Expression getExpression() {
		return expression;
	}

	public String getDataType() {
		return dataType;
	}

	@Override
	protected void okPressed() {
		name = nameText.getText().trim();
		expression = new Expression(expressionText.getText(),
				(String) expressionText.getData(ExpressionButtonUtil.EXPR_TYPE));
		dataType = getType();
		super.okPressed();
	}

	private String getType() {
		for (int i = 0; i < DATA_TYPE_CHOICES.length; i++) {
			if (DATA_TYPE_CHOICES[i].getDisplayName().equals(dataTypeCmb.getText())) {
				return DATA_TYPE_CHOICES[i].getName();
			}
		}
		return ""; //$NON-NLS-1$
	}

}
