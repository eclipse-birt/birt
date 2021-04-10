
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.DescriptorToolkit;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.SimpleComboPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class SimpleComboSection extends Section {

	protected SimpleComboPropertyDescriptor simpleCombo;

	public SimpleComboSection(String labelText, Composite parent, boolean isFormStyle) {
		super(labelText, parent, isFormStyle);
		// TODO Auto-generated constructor stub
	}

	public void createSection() {
		getLabelControl(parent);
		getSimpleComboControl(parent);
		getGridPlaceholder(parent);
	}

	public void layout() {
		GridData gd = (GridData) simpleCombo.getControl().getLayoutData();
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

	public SimpleComboPropertyDescriptor getSimpleComboControl() {
		return simpleCombo;
	}

	protected SimpleComboPropertyDescriptor getSimpleComboControl(Composite parent) {
		if (simpleCombo == null) {
			simpleCombo = DescriptorToolkit.createSimpleComboPropertyDescriptor(true);
			if (getProvider() != null)
				simpleCombo.setDescriptorProvider(getProvider());
			simpleCombo.createControl(parent);
			simpleCombo.getControl().setLayoutData(new GridData());
			simpleCombo.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					simpleCombo = null;
				}
			});
		} else {
			checkParent(simpleCombo.getControl(), parent);
		}
		return simpleCombo;
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (simpleCombo != null)
			simpleCombo.setDescriptorProvider(provider);
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
		simpleCombo.setInput(input);
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
		if (simpleCombo != null) {
			if (value == null) {
				value = "";//$NON-NLS-1$
			}
			oldValue = simpleCombo.getStringValue();
			if (!oldValue.equals(value)) {
				simpleCombo.setStringValue(value);
			}
		}
	}

	public void setFocus() {
		if (simpleCombo != null) {
			simpleCombo.getControl().setFocus();
		}
	}

	public String getStringValue() {
		if (simpleCombo != null) {
			return simpleCombo.getStringValue();
		}

		return null;
	}

	public void load() {
		if (simpleCombo != null && !simpleCombo.getControl().isDisposed())
			simpleCombo.load();
	}

	public void reset() {
		if (simpleCombo != null && !simpleCombo.getControl().isDisposed()) {
			simpleCombo.reset();
		}
	}

	public void setHidden(boolean isHidden) {
		if (displayLabel != null)
			WidgetUtil.setExcludeGridData(displayLabel, isHidden);
		if (simpleCombo != null)
			simpleCombo.setHidden(isHidden);
		if (placeholderLabel != null)
			WidgetUtil.setExcludeGridData(placeholderLabel, isHidden);
	}

	public void setVisible(boolean isVisible) {
		if (displayLabel != null)
			displayLabel.setVisible(isVisible);
		if (simpleCombo != null)
			simpleCombo.setVisible(isVisible);
		if (placeholderLabel != null)
			placeholderLabel.setVisible(isVisible);
	}
}
