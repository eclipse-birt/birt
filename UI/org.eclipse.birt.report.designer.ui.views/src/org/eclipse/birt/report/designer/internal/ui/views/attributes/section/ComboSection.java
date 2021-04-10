
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ComboPropertyDescriptor;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class ComboSection extends Section {

	protected ComboPropertyDescriptor combo;

	public ComboSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	public void createSection() {
		getLabelControl(parent);
		getComboControl(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) combo.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - 1 - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - 1 - placeholder;
		if (width > -1) {
			gd.widthHint = width;
			gd.grabExcessHorizontalSpace = false;
		} else
			gd.grabExcessHorizontalSpace = fillCombo;
	}

	protected ComboPropertyDescriptor getComboControl(Composite parent) {
		if (combo == null) {
			combo = DescriptorToolkit.createComboPropertyDescriptor(isFormStyle);
			if (getProvider() != null)
				combo.setDescriptorProvider(getProvider());
			combo.createControl(parent);
			combo.getControl().setLayoutData(new GridData());
			combo.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					combo = null;
				}
			});
		} else {
			checkParent(combo.getControl(), parent);
		}
		return combo;
	}

	public ComboPropertyDescriptor getComboControl() {
		return combo;
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (combo != null)
			combo.setDescriptorProvider(provider);
	}

	private int width = -1;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setInput(Object input) {
		assert (input != null);
		combo.setInput(input);
	}

	private boolean fillCombo = false;

	public boolean isFillCombo() {
		return fillCombo;
	}

	public void setFillCombo(boolean fillCombo) {
		this.fillCombo = fillCombo;
	}

	private String oldValue;

	public void setStringValue(String value) {
		if (combo != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			oldValue = combo.getStringValue();
			if (!oldValue.equals(value)) {
				combo.setStringValue(value);
			}
		}
	}

	public void setFocus() {
		if (combo != null) {
			combo.getControl().setFocus();
		}
	}

	public String getStringValue() {
		if (combo != null) {
			return combo.getStringValue();
		}

		return null;
	}

	public void load() {
		if (combo != null && !combo.getControl().isDisposed())
			combo.load();
	}

	public void reset() {
		if (combo != null && !combo.getControl().isDisposed()) {
			combo.reset();
		}
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (combo != null)
			combo.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (combo != null)
			combo.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}
}
