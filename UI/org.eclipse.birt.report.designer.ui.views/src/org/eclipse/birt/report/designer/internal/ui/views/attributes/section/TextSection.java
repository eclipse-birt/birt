
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.TextPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class TextSection extends Section {

	/**
	 * Old text value.
	 */
	private String oldValue;

	/**
	 * The text field, or <code>null</code> if none.
	 */
	protected TextPropertyDescriptor textField;

	private int style = -1;

	public TextSection(String labelText, Composite parent, boolean formStyle) {
		super(labelText, parent, formStyle);
	}

	private boolean fillText = false;

	private int width = -1;

	public void createSection() {
		getLabelControl(parent);
		getTextControl(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) textField.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillText;

		if (height > -1) {
			gd.heightHint = height;
			gd.grabExcessVerticalSpace = false;
			if (displayLabel != null) {
				gd = (GridData) displayLabel.getLayoutData();
				gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
			}
		} else {
			gd.grabExcessVerticalSpace = fillText;
			if (fillText) {
				gd.verticalAlignment = GridData.FILL;
				if (displayLabel != null) {
					gd = (GridData) displayLabel.getLayoutData();
					gd.verticalAlignment = GridData.VERTICAL_ALIGN_FILL;
				}
			}

		}
		if (fillText) {
			gd = (GridData) textField.getControl().getLayoutData();
			gd.grabExcessVerticalSpace = true;
			gd.verticalAlignment = GridData.FILL;
		}

	}

	public String getStringValue() {
		if (textField != null) {
			return textField.getText();
		}

		return null;
	}

	public TextPropertyDescriptor getTextControl() {
		return textField;
	}

	protected TextPropertyDescriptor getTextControl(Composite parent) {
		if (textField == null) {
			textField = DescriptorToolkit.createTextPropertyDescriptor(isFormStyle);
			if (getProvider() != null)
				textField.setDescriptorProvider(getProvider());
			if (style != -1)
				textField.setStyle(style);
			textField.createControl(parent);
			textField.getControl().setLayoutData(new GridData());
			textField.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					textField = null;
				}
			});
		} else {
			checkParent(textField.getControl(), parent);
		}
		return textField;
	}

	public void setFocus() {
		if (textField != null) {
			textField.getControl().setFocus();
		}
	}

	public void setStringValue(String value) {
		if (textField != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			oldValue = textField.getText();
			if (!oldValue.equals(value)) {
				textField.setText(value);
			}
		}
	}

	public void setTextLimit(int limit) {
		if (textField != null) {
			textField.setTextLimit(limit);
		}
	}

	public void load() {
		if (textField != null && !textField.getControl().isDisposed())
			textField.load();
	}

	public void reset() {
		if (textField != null && !textField.getControl().isDisposed()) {
			textField.reset();
		}
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (textField != null)
			textField.setDescriptorProvider(provider);
	}

	public void setInput(Object input) {
		assert (input != null);
		textField.setInput(input);
	}

	private int height = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean isFillText() {
		return fillText;
	}

	public void setFillText(boolean fillText) {
		this.fillText = fillText;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (textField != null)
			textField.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (textField != null)
			textField.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}

	public int getStyle() {
		return style;
	}

	public void setStyle(int style) {
		this.style = style;
	}

}
