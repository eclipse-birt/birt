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

package org.eclipse.birt.report.designer.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * VariableDialog
 */
public class VariableDialog extends BaseTitleAreaDialog {

	private ReportDesignHandle designHandle;
	private VariableElementHandle variable;

	private Text nameTxt;
	private Text expressionTxt;
	private Button reportRadio;
	private Button pageRadio;

	public VariableDialog(String title, ReportDesignHandle designHandle, VariableElementHandle variable) {
		super(UIUtil.getDefaultShell());
		this.title = title;
		this.designHandle = designHandle;
		this.variable = variable;
	}

	protected Control createDialogArea(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.INSERT_EDIT_SORTKEY_DIALOG_ID);

		Composite area = (Composite) super.createDialogArea(parent);
		Composite contents = new Composite(area, SWT.NONE);
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));
		contents.setLayout(new GridLayout());

		this.setTitle(title);
		getShell().setText(title);

		applyDialogFont(contents);
		initializeDialogUnits(area);
		createInputContents(contents);

		Composite space = new Composite(contents, SWT.NONE);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.heightHint = 10;
		space.setLayoutData(gdata);

		Label lb = new Label(contents, SWT.SEPARATOR | SWT.HORIZONTAL);
		lb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return area;
	}

	protected Control createInputContents(Composite parent) {
		Composite content = new Composite(parent, SWT.NONE);
		content.setLayoutData(new GridData(GridData.FILL_BOTH));
		content.setLayout(GridLayoutFactory.swtDefaults().numColumns(3).create());

		new Label(content, SWT.NONE).setText(Messages.getString("VariableDialog.VariableType")); //$NON-NLS-1$

		Composite typeChoices = new Composite(content, SWT.NONE);
		typeChoices.setLayout(GridLayoutFactory.swtDefaults().numColumns(2).create());
		reportRadio = new Button(typeChoices, SWT.RADIO);
		reportRadio.setText(Messages.getString("VariableDialog.ReportVariable")); //$NON-NLS-1$
		pageRadio = new Button(typeChoices, SWT.RADIO);
		pageRadio.setText(Messages.getString("VariableDialog.PageVariable")); //$NON-NLS-1$
		new Label(content, SWT.NONE);

		new Label(content, SWT.NONE).setText(Messages.getString("VariableDialog.Name")); //$NON-NLS-1$

		nameTxt = new Text(content, SWT.BORDER);
		nameTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		nameTxt.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				validate();
			}
		});
		// dummy
		new Label(content, SWT.NONE);

		// new Label( content, SWT.NONE ).setText( Messages.getString(
		// "VariableDialog.DataType" ) ); //$NON-NLS-1$
		//
		// dataTypeCombo = new Combo( content, SWT.READ_ONLY );
		// dataTypeCombo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL )
		// );
		// new Label( content, SWT.NONE );

		new Label(content, SWT.NONE).setText(Messages.getString("VariableDialog.DefaultValue")); //$NON-NLS-1$
		expressionTxt = new Text(content, SWT.BORDER | SWT.MULTI);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = expressionTxt.computeSize(SWT.DEFAULT, SWT.DEFAULT).y - expressionTxt.getBorderWidth() * 2;
		expressionTxt.setLayoutData(gd);

		ExpressionButtonUtil.createExpressionButton(content, expressionTxt, this.getExpressionProvider(), variable);

		UIUtil.bindHelp(parent, IHelpContextIds.VARIABLE_DIALOG_ID);

		return content;
	}

	@Override
	protected boolean initDialog() {
		// IChoiceSet datatypes = DEUtil.getMetaDataDictionary( )
		// .getChoiceSet( DesignChoiceConstants.CHOICE_PARAM_TYPE );
		// for ( IChoice choice : datatypes.getChoices( ) )
		// {
		// dataTypeCombo.add( choice.getDisplayName( ) );
		// }
		if (this.variable != null) {
			this.nameTxt.setText(this.variable.getName());
			if (this.variable.getType() == null
					|| this.variable.getType().equals(DesignChoiceConstants.VARIABLE_TYPE_REPORT))
				this.reportRadio.setSelection(true);
			else
				this.pageRadio.setSelection(true);
			// if ( this.variable.getDataType( ) != null )
			// {
			// String displayName = getDisplayNameByDataType(
			// this.variable.getDataType( ),
			// datatypes );
			// for ( int i = 0; i < dataTypeCombo.getItemCount( ); i++ )
			// {
			// if ( dataTypeCombo.getItem( i ).equals( displayName ) )
			// {
			// dataTypeCombo.select( i );
			// break;
			// }
			// }
			// }

			ExpressionButtonUtil.initExpressionButtonControl(expressionTxt, variable, VariableElementHandle.VALUE_PROP);
		} else {
			this.reportRadio.setSelection(true);
			// this.dataTypeCombo.select( 0 );
		}
		validate();
		return true;
	}

	@Override
	protected void okPressed() {
		if (this.variable == null) {
			this.variable = DesignElementFactory.getInstance(this.designHandle)
					.newVariableElement(this.nameTxt.getText().trim());
			try {
				this.designHandle.add(IReportDesignModel.PAGE_VARIABLES_PROP, this.variable);
			} catch (SemanticException e) {
				ExceptionHandler.handle(e);
			}
		}
		try {
			this.variable.setVariableName(this.nameTxt.getText().trim());
			if (this.reportRadio.getSelection())
				this.variable.setType(DesignChoiceConstants.VARIABLE_TYPE_REPORT);
			else
				this.variable.setType(DesignChoiceConstants.VARIABLE_TYPE_PAGE);
			// this.variable.setDataType( getDataTypeByDisplayName(
			// this.dataTypeCombo.getText( ),
			// DEUtil.getMetaDataDictionary( )
			// .getChoiceSet( DesignChoiceConstants.CHOICE_PARAM_TYPE ) ) );
			ExpressionButtonUtil.saveExpressionButtonControl(expressionTxt, variable, VariableElementHandle.VALUE_PROP);
		} catch (SemanticException e) {
			ExceptionHandler.handle(e);
		}
		super.okPressed();
	}

	private void validate() {
		if (this.nameTxt.getText().trim().length() == 0) {
			getOkButton().setEnabled(false);
		} else if (isNameDuplicated(this.nameTxt.getText().trim())) {
			setErrorMessage(Messages.getFormattedString("VariableDialog.Error.NameDuplicate",
					new String[] { this.nameTxt.getText().trim() }));
			getOkButton().setEnabled(false);
		} else {
			setErrorMessage(null);
			getOkButton().setEnabled(true);
		}
	}

	private boolean isNameDuplicated(String text) {
		if (this.variable != null && this.variable.getName().equals(text))
			return false;
		for (VariableElementHandle veh : this.designHandle.getPageVariables()) {
			if (veh.getName().equals(text))
				return true;
		}
		return false;
	}

	private IExpressionProvider getExpressionProvider() {
		ExpressionProvider provider = new ExpressionProvider(VariableDialog.this.variable);
		provider.addFilter(new ExpressionFilter() {

			@Override
			public boolean select(Object parentElement, Object element) {
				return !element.equals(VariableDialog.this.variable);
			}

		});
		return provider;
	}

}
