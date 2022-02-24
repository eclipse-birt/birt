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

package org.eclipse.birt.report.designer.ui.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.ui.preview.parameter.AbstractParamGroup;
import org.eclipse.birt.report.designer.ui.preview.parameter.CascadingGroup;
import org.eclipse.birt.report.designer.ui.preview.parameter.ListingParam;
import org.eclipse.birt.report.designer.ui.preview.parameter.RadioParam;
import org.eclipse.birt.report.designer.ui.preview.parameter.ScalarParam;
import org.eclipse.birt.report.designer.ui.preview.parameter.StaticTextParam;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * The dialog for inputting report parameter values when previewing report in
 * new preview prototype
 */
public class InputParameterDialog extends Dialog {

	private Composite container;
	private List params;

	private Map paramValues = new HashMap();
	private ScrolledComposite scroller;
	private List isRequiredParameters = new ArrayList();
	private List dataTypeCheckList = new ArrayList();
	private Shell parentShell;

	private static IParameterSelectionChoice nullValueChoice = new IParameterSelectionChoice() {

		public String getLabel() {
			return "Null Value"; //$NON-NLS-1$
		}

		public Object getValue() {
			return null;
		}

	};

	private static IParameterSelectionChoice blankValueChoice = new IParameterSelectionChoice() {

		public String getLabel() {
			return ""; //$NON-NLS-1$
		}

		public Object getValue() {
			return ""; //$NON-NLS-1$
		}
	};

	public InputParameterDialog(Shell parentShell, List params, Map paramValues) {
		super(parentShell);
		this.parentShell = parentShell;
		this.params = params;
		if (paramValues != null)
			this.paramValues.putAll(paramValues);
	}

	protected void buttonPressed(int buttonId) {
		// TODO Auto-generated method stub
		if (buttonId == Window.OK) {
			Iterator ite = isRequiredParameters.iterator();
			while (ite.hasNext()) {
				String paramName = (String) ite.next();
				Object paramValue = paramValues.get(paramName);
				if (paramValue == null || (paramValue instanceof String && ((String) paramValue).equals(""))) //$NON-NLS-1$
				{
					MessageDialog.openError(parentShell, "Error", paramName //$NON-NLS-1$
							+ " cannot be NULL or blank"); //$NON-NLS-1$
					return;

				}
			}

			ite = dataTypeCheckList.iterator();
			while (ite.hasNext()) {
				ScalarParam scalarParam = (ScalarParam) ite.next();
				String paramValue = (String) paramValues.get(scalarParam.getHandle().getName());
				try {
					paramValues.put(scalarParam.getHandle().getName(), scalarParam.converToDataType(paramValue));

				} catch (BirtException e) {
					// TODO: handle exception
					MessageDialog.openError(parentShell, "Invalid value type", //$NON-NLS-1$
							"The value \"" //$NON-NLS-1$
									+ paramValue + "\" is invalid with type " //$NON-NLS-1$
									+ scalarParam.getHandle().getDataType());
					return;
				}
			}
		}
		super.buttonPressed(buttonId);
	}

	protected Control createDialogArea(Composite parent) {
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = gridLayout.marginHeight = 5;
		parent.setLayout(gridLayout);

		this.scroller = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		this.scroller.setExpandHorizontal(true);
		this.scroller.setExpandVertical(true);

		scroller.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL));

		createParameters();

		return super.createDialogArea(parent);
	}

	private void createParameters() {
		if (this.container != null && !this.container.isDisposed())
			this.container.dispose();

		this.container = new Composite(this.scroller, SWT.NONE);
		this.scroller.setContent(this.container);

		this.container.setLayoutData(new GridData(GridData.FILL_BOTH));

		this.container.setLayout(new GridLayout());

		createParametersSection(params, this.container);

		this.container.setSize(this.container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		this.scroller.setMinSize(this.container.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	}

	private void createParametersSection(List children, Composite parent) {
		Iterator iterator = children.iterator();
		while (iterator.hasNext()) {
			Object obj = iterator.next();
			if (obj instanceof ScalarParam && !((ScalarParam) obj).getHandle().isHidden()) {
				ScalarParam param = (ScalarParam) obj;
				createParamSection(param, parent);
			} else if (obj instanceof AbstractParamGroup) {
				AbstractParamGroup group = (AbstractParamGroup) obj;
				createParametersSection(group.getChildren(), createParamGroupSection(group, parent));
			}
		}
	}

	private Composite createParamGroupSection(AbstractParamGroup paramGroup, Composite parent) {
		Group group = new Group(parent, SWT.NONE);
		group.setText(paramGroup.getHandle().getDisplayLabel());
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
		group.setLayout(new GridLayout());
		return group;
	}

	private Composite createParamSection(ScalarParam param, Composite parent) {
		boolean isRequired = param.getHandle().isRequired();
		boolean isStringType = param.getHandle().getDataType().equals(DesignChoiceConstants.PARAM_TYPE_STRING);
		if (isRequired) {
			isRequiredParameters.add(param.getHandle().getName());
		}

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		new Label(container, SWT.NONE).setText(param.getHandle().getDisplayLabel() + ":"); //$NON-NLS-1$

		if (param instanceof StaticTextParam) {
			final StaticTextParam textParam = (StaticTextParam) param;
			String value = textParam.getDefaultValue();
			dataTypeCheckList.add(textParam);
			Text input = new Text(container, SWT.BORDER);
			input.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			input.addModifyListener(new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					Text input = (Text) e.getSource();
					paramValues.put(textParam.getHandle().getName(), input.getText());
				}
			});

			if (paramValues.containsKey(textParam.getHandle().getName())) {
				value = paramValues.get(textParam.getHandle().getName()).toString();
			}
			if (value != null) {
				input.setText(value);
			}
		} else if (param instanceof RadioParam) {
			final RadioParam radioParam = (RadioParam) param;
			Object value = null;
			try {
				value = radioParam.converToDataType(radioParam.getDefaultValue());
			} catch (BirtException e) {

			}
			if (paramValues.containsKey(radioParam.getHandle().getName())) {
				value = paramValues.get(radioParam.getHandle().getName());
			}

			List list = radioParam.getValueList();
			if (!isRequired) {
				list.add(InputParameterDialog.nullValueChoice);
			}

			int i = 0;
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				if (i > 0) {
					new Label(container, SWT.NONE);
				}

				IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next();
				Button button = new Button(container, SWT.RADIO);
				button.setText(choice.getLabel());
				button.setData(choice.getValue());
				if (choice.getValue() != null && choice.getValue().equals(value)) {
					button.setSelection(true);
				} else if (value == null && choice.getLabel().equals("Null Value")) //$NON-NLS-1$
				{
					button.setSelection(true);
				}
				button.addSelectionListener(new SelectionListener() {

					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub

					}

					public void widgetSelected(SelectionEvent e) {
						Button button = (Button) e.getSource();
						paramValues.put(radioParam.getHandle().getName(), button.getData());
					}
				});
				i++;
			}
		} else if (param instanceof ListingParam) {
			final ListingParam listParam = (ListingParam) param;
			Object value = null;
			try {
				value = listParam.converToDataType(listParam.getDefaultValue());
			} catch (BirtException e) {

			}
			if (paramValues.containsKey(listParam.getHandle().getName())) {
				value = paramValues.get(listParam.getHandle().getName());
				if (value != null)
					listParam.setSelectionValue(value.toString());
			}

			Combo combo = new Combo(container, SWT.BORDER);
			combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			combo.setVisibleItemCount(30);
			List list = new ArrayList();
			if (isStringType && !isRequired) {
				list.add(blankValueChoice);
				list.addAll(listParam.getValueList());
			} else {
				list = listParam.getValueList();
			}
			if (!isRequired) {
				list.add(InputParameterDialog.nullValueChoice);
			}
			for (Iterator iterator = list.iterator(); iterator.hasNext();) {
				IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next();
				String label = (String) (choice.getLabel() == null ? choice.getValue() : choice.getLabel());
				if (label != null) {
					combo.add(label);
					combo.setData(label, choice.getValue());
				}
			}
			if (value == null && !isRequired) {
				combo.select(combo.getItemCount() - 1);
			} else {
				for (int i = 0; i < combo.getItemCount(); i++) {
					if (combo.getData(combo.getItem(i)).equals(value)) {
						combo.select(i);
						break;
					}
				}
			}

			combo.addSelectionListener(new SelectionListener() {

				public void widgetDefaultSelected(SelectionEvent e) {
					// TODO Auto-generated method stub

				}

				public void widgetSelected(SelectionEvent e) {
					Combo combo = (Combo) e.getSource();
					paramValues.put(listParam.getHandle().getName(),
							combo.getData(combo.getItem(combo.getSelectionIndex())));
					if (listParam.getParentGroup() instanceof CascadingGroup) {
						CascadingGroup group = (CascadingGroup) listParam.getParentGroup();
						if (group.getPostParameter(listParam) != null)
							try {
								createParameters();
							} catch (RuntimeException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
					}
				}
			});

		}
		return container;
	}

	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Input parameters"); //$NON-NLS-1$
		newShell.setSize(400, 400);
	}

	public Map getParameters() {
		return this.paramValues;
	}
}
