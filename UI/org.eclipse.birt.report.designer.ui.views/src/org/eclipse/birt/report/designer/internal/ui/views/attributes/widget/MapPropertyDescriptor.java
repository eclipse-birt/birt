
package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapDescriptorProvider;
import org.eclipse.birt.report.designer.util.ColorManager;

public class MapPropertyDescriptor extends PreviewPropertyDescriptor {

	public MapPropertyDescriptor(boolean formStyle) {
		super(formStyle);
	}

	protected MapDescriptorProvider mapProvider;

	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);
		if (provider instanceof MapDescriptorProvider)
			this.mapProvider = (MapDescriptorProvider) provider;
	}

	protected void updatePreview(Object handle) {

		if (handle != null && mapProvider != null) {
			previewLabel.setText(mapProvider.getDisplayText(handle));
			previewLabel.updateView();
		} else {
			previewLabel.restoreDefaultState();

			previewLabel.setForeground(ColorManager.getColor(-1));
			previewLabel.setBackground(ColorManager.getColor(-1));

			previewLabel.setText(""); //$NON-NLS-1$
			previewLabel.updateView();

			if (isFormStyle()) {
				FormWidgetFactory.getInstance().paintFormStyle(previewLabel);
				FormWidgetFactory.getInstance().adapt(previewLabel);
			}
		}
	}

}
