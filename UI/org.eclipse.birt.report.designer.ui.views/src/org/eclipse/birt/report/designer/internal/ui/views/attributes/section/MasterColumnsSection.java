
package org.eclipse.birt.report.designer.internal.ui.views.attributes.section;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.MasterColumnsPropertyDescriptor;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class MasterColumnsSection extends Section {

	public MasterColumnsSection(Composite parent, boolean isFormStyle) {
		super(" ", parent, isFormStyle); //$NON-NLS-1$
	}

	protected MasterColumnsPropertyDescriptor columns;

	public void createSection() {
		getcolumnsControl(parent);
		getGridPlaceholder(parent);
	}

	public MasterColumnsPropertyDescriptor getcolumnsControl() {
		return columns;
	}

	protected MasterColumnsPropertyDescriptor getcolumnsControl(Composite parent) {
		if (columns == null) {
			columns = new MasterColumnsPropertyDescriptor(isFormStyle);
			columns.setDescriptorProvider(provider);
			columns.createControl(parent);
			columns.getControl().setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			columns.getControl().addDisposeListener(new DisposeListener() {

				public void widgetDisposed(DisposeEvent event) {
					columns = null;
				}
			});
		} else {
			checkParent(columns.getControl(), parent);
		}
		return columns;
	}

	public void layout() {
		GridData gd = (GridData) columns.getControl().getLayoutData();
		if (getLayoutNum() > 0)
			gd.horizontalSpan = getLayoutNum() - placeholder;
		else
			gd.horizontalSpan = ((GridLayout) parent.getLayout()).numColumns - placeholder;
	}

	public void load() {
		if (columns != null && !columns.getControl().isDisposed())
			columns.load();
	}

	IDescriptorProvider provider;

	public IDescriptorProvider getProvider() {
		return provider;
	}

	public void setProvider(IDescriptorProvider provider) {
		this.provider = provider;
		if (columns != null)
			columns.setDescriptorProvider(provider);
	}

	public void setInput(Object input) {
		assert (input != null);
		columns.setInput(input);
	}

	public void setHidden(boolean isHidden) {
		if (columns != null)
			WidgetUtil.setExcludeGridData(columns.getControl(), isHidden);

	}

	public void setVisible(boolean isVisable) {
		if (columns != null)
			columns.getControl().setVisible(isVisable);

	}

}
