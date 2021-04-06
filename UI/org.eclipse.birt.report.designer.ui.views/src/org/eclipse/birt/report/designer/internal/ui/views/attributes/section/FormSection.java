
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TabbedPropertyTitle;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFormHandleProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.FormPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class FormSection extends Section {

	public FormSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	boolean isTabbed = false;

	public FormSection(String labelText, Composite parent, boolean isFormStyle, boolean isTabbed) {
		super(labelText, parent, isFormStyle);
		this.isTabbed = isTabbed;
	}

	private boolean showLabel = false;;

	public void showDisplayLabel(boolean show) {
		this.showLabel = show;
	}

	protected FormPropertyDescriptor form;

	public void createSection() {
		if (isTabbed)
			getTitleControl(parent);
		else if (showLabel)
			getLabelControl(parent);
		getFormControl(parent);
		getGridPlaceholder(parent);

	}

	protected TabbedPropertyTitle title;

	public TabbedPropertyTitle getTitleControl() {
		return title;
	}

	protected TabbedPropertyTitle getTitleControl(Composite parent) {
		if (title == null) {
			title = new TabbedPropertyTitle(parent, FormWidgetFactory.getInstance());
			title.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			title.setFont(parent.getFont());
			title.setLayoutData(new GridData());
			String text = getLabelText();
			if (text != null) {
				title.setTitle(text, null);
			}
			title.addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					title = null;
				}
			});
		} else {
			checkParent(title, parent);
		}
		return title;
	}

	private FormPropertyDescriptor customForm;

	public FormPropertyDescriptor getFormControl() {
		return form;
	}

	protected FormPropertyDescriptor getFormControl(Composite parent) {
		if (form == null) {
			if (customForm != null) {
				form = customForm;
			} else
				form = DescriptorToolkit.createFormPropertyDescriptor(true);
			if (style != -1)
				form.setStyle(style);
			if (getProvider() != null)
				form.setDescriptorProvider(getProvider());
			form.setButtonWithDialog(withDialog);
			form.createControl(parent);
			form.getControl().setLayoutData(new GridData());
			form.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					form = null;
				}
			});
		} else {
			checkParent(form.getControl(), parent);
		}
		return form;
	}

	int displayLabelStyle = SWT.VERTICAL;

	public void setDisplayLabelStyle(int style) {
		displayLabelStyle = style;
	}

	public void layout() {
		GridData gd = (GridData) form.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
		if (displayLabel != null && (displayLabelStyle & SWT.HORIZONTAL) != 0) {
			gd.horizontalSpan = gd.horizontalSpan - 1;
		}

		gd.horizontalAlignment = SWT.FILL;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillForm;

		if (height > -1) {
			if (height > form.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y)
				gd.heightHint = height;
			else
				gd.heightHint = form.getControl().computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
			gd.grabExcessVerticalSpace = false;
		} else
			gd.grabExcessVerticalSpace = fillForm;

		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = GridData.FILL;

		if (displayLabel != null) {
			if ((displayLabelStyle & SWT.VERTICAL) != 0) {
				gd = (GridData) displayLabel.getLayoutData();
				gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns;
				gd.grabExcessHorizontalSpace = true;
				gd.horizontalAlignment = SWT.FILL;
			} else {
				gd = (GridData) displayLabel.getLayoutData();
				gd.verticalAlignment = SWT.BEGINNING;
			}
		}

		if (title != null) {
			gd = (GridData) title.getLayoutData();
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns;
			gd.grabExcessHorizontalSpace = true;
			gd.horizontalAlignment = SWT.FILL;
		}

	}

	public void load() {
		if (form != null && !form.getControl().isDisposed()) {
			if (getProvider() instanceof AbstractFormHandleProvider) {
				((AbstractFormHandleProvider) getProvider()).setReadOnly(isReadOnly());
			}
			form.load();
			setLabelText(getProvider().getDisplayName());
		}

	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (form != null)
			form.setDescriptorProvider(provider);
	}

	private int height = -1;
	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setInput(Object input) {
		assert (input != null);
		form.setInput(input);
	}

	boolean fillForm = false;

	public boolean isFillForm() {
		return fillForm;
	}

	public void setFillForm(boolean fillForm) {
		this.fillForm = fillForm;
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (title != null)
			WidgetUtil.setExcludeGridData(title, isHidden);
		if (form != null)
			form.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (title != null)
			title.setVisible(isVisible);
		if (form != null)
			form.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}

	private int style = -1;

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
		if (form != null)
			form.setStyle(style);
	}

	boolean withDialog = false;

	public void setButtonWithDialog(boolean withDialog) {
		this.withDialog = withDialog;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setCustomForm(FormPropertyDescriptor customForm) {
		this.customForm = customForm;
	}

	/**
	 * Set the index of the current form created within one page.
	 * 
	 * @param index
	 */
	public void setButtonGroupIndex(int index) {
		this.customForm.setButtonGroupIndex(index);
	}
}
