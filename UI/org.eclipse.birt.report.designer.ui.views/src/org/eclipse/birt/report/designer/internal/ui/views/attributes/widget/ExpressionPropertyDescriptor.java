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

package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.ExpressionPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * Property Descriptor for value expression property.
 */

public class ExpressionPropertyDescriptor extends PropertyDescriptor {

	protected Text text;

	protected Button button;

	private Composite containerPane;

	private String deValue;

	private String newValue;

	/**
	 * The constructor.
	 */
	public ExpressionPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}
	
	public Text getTextControl()
	{
		return text;
	}

	public void setInput(Object handle) {
		this.input = handle;
		getDescriptorProvider().setInput(input);
	}

	/**
	 * After selection changed, re-sets UI data.
	 */
	public void load() {
		deValue = getDescriptorProvider().load().toString();
		if (getDescriptorProvider() instanceof ExpressionPropertyDescriptorProvider) {
			boolean readOnly = ((ExpressionPropertyDescriptorProvider) getDescriptorProvider())
					.isReadOnly();
			button.setEnabled(!readOnly);
			text.setEnabled(!readOnly);

			boolean enable = ((ExpressionPropertyDescriptorProvider) getDescriptorProvider())
					.isEnable();
			button.setEnabled(enable);
			text.setEnabled(enable);
		}
		setExpressionButtonImage(button);
		if (deValue == null) {
			deValue = ""; //$NON-NLS-1$
		}
		if (!text.getText().equals(deValue)) {
			text.setText(deValue);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PropertyDescriptor#getControl()
	 */
	public Control getControl() {
		return containerPane;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.extensions.IPropertyDescriptor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createControl(Composite parent) {
		containerPane = new Composite(parent, SWT.NONE);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		containerPane.setLayout(layout);
		if (isFormStyle())
			text = FormWidgetFactory.getInstance().createText(containerPane,
					"", SWT.MULTI | SWT.WRAP | SWT.H_SCROLL | SWT.V_SCROLL);
		else
			text = new Text(containerPane, SWT.MULTI | SWT.WRAP | SWT.BORDER
					| SWT.H_SCROLL | SWT.V_SCROLL);
		text.setLayoutData(new GridData(GridData.FILL_BOTH));
		// text.addSelectionListener( new SelectionAdapter( ) {
		//
		// public void widgetDefaultSelected( SelectionEvent e )
		// {
		// handleSelectEvent( );
		// }
		// } );
		text.addFocusListener(new FocusListener() {

			public void focusGained(FocusEvent e) {

			}

			public void focusLost(FocusEvent e) {
				handleFocusLostEvent();
			}
		});
		if (isFormStyle())
			button = FormWidgetFactory.getInstance().createButton(
					containerPane, SWT.PUSH, true);
		else
			button = new Button(containerPane, SWT.PUSH);
		// if ( buttonText != null )
		// button.setText( buttonText );
		// else
		// button.setText( Messages.getString(
		// "ExpressionPropertyDescriptor.text.Edit" ) ); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				handleButtonSelectEvent();
			}
		});

		setExpressionButtonImage(button);
		return containerPane;
	}

	protected void setExpressionButtonImage(Button button) {
		String imageName;
		if (button.isEnabled()) {
			imageName = IReportGraphicConstants.ICON_ENABLE_EXPRESSION_BUILDERS;
		} else {
			imageName = IReportGraphicConstants.ICON_DISABLE_EXPRESSION_BUILDERS;
		}
		Image image = ReportPlatformUIImages.getImage(imageName);

		GridData gd = new GridData(GridData.VERTICAL_ALIGN_END);
		gd.widthHint = 20;
		gd.heightHint = 20;
		button.setLayoutData(gd);

		button.setImage(image);
		if (button.getImage() != null) {
			button.getImage().setBackground(button.getBackground());
		}

	}

	protected void handleSelectEvent() {
		newValue = text.getText();
		processAction();
	}

	protected void handleFocusLostEvent() {
		newValue = text.getText();
		processAction();
	}

	/**
	 * Processes the save action.
	 */
	private void processAction() {
		String value = newValue;
		if (value != null && value.length() == 0) {
			value = null;
		}

		if ((value == null && deValue != null)
				|| (value != null && !value.equals(deValue))) {
			try {
				text.setText(UIUtil.convertToGUIString(value));
				save(value);
			} catch (SemanticException e1) {
				text.setText(UIUtil.convertToGUIString(deValue));
				WidgetUtil.processError(text.getShell(), e1);

			}
		}
	}

	private String buttonText;

	public void setButtonText(String text) {
		// if ( button != null )
		// button.setText( text );
		// buttonText = text;
	}

	public void setText(String text) {
		this.text.setText(text);
	}

	public void save(Object obj) throws SemanticException {
		getDescriptorProvider().save(obj);

	}

	protected void handleButtonSelectEvent() {
		ExpressionBuilder builder = new ExpressionBuilder(button.getShell(),
				deValue);

		if (getDescriptorProvider() instanceof ExpressionPropertyDescriptorProvider) {
			ExpressionProvider provider = ((ExpressionPropertyDescriptorProvider) getDescriptorProvider())
					.getExpressionProvider();
			if (provider != null)
				builder.setExpressionProvier(provider);
		}
		if (builder.open() == Window.OK) {
			newValue = builder.getResult();
			processAction();
		}
	}

	public void setHidden(boolean isHidden) {
		WidgetUtil.setExcludeGridData(containerPane, isHidden);
	}

	public void setVisible(boolean isVisible) {
		containerPane.setVisible(isVisible);
	}
}