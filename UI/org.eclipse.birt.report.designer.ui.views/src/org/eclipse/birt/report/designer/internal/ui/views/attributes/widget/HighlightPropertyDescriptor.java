
package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.util.ColorManager;

public class HighlightPropertyDescriptor extends PreviewPropertyDescriptor {

	public HighlightPropertyDescriptor(boolean formStyle) {
		super(formStyle);
	}

	protected HighlightDescriptorProvider highlightProvider;

	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);
		if (provider instanceof HighlightDescriptorProvider)
			this.highlightProvider = (HighlightDescriptorProvider) provider;
	}

	protected void updatePreview(Object handle) {
		if (handle != null && highlightProvider != null) {
			String familyValue = highlightProvider.getFontFamily(handle);
			int sizeValue = highlightProvider.getFontSize(handle);
			previewLabel.setFontFamily(familyValue);
			previewLabel.setFontSize(sizeValue);
			previewLabel.setBold(highlightProvider.isBold(handle));
			previewLabel.setItalic(highlightProvider.isItalic(handle));
			previewLabel.setForeground(highlightProvider.getColor(handle));
			previewLabel.setBackground(highlightProvider.getBackgroundColor(handle));
			previewLabel.setUnderline(highlightProvider.isUnderline(handle));
			previewLabel.setLinethrough(highlightProvider.isLinethrough(handle));
			previewLabel.setOverline(highlightProvider.isOverline(handle));
			previewLabel.updateView();

			if (highlightProvider.getBackgroundColor(handle) == null && isFormStyle()) {
				FormWidgetFactory.getInstance().paintFormStyle(previewLabel);
				FormWidgetFactory.getInstance().adapt(previewLabel);
			}
		} else {
			previewLabel.restoreDefaultState();

			previewLabel.setForeground(ColorManager.getColor(-1));
			previewLabel.setBackground(ColorManager.getColor(-1));

			previewLabel.updateView();

			if (isFormStyle()) {
				FormWidgetFactory.getInstance().paintFormStyle(previewLabel);
				FormWidgetFactory.getInstance().adapt(previewLabel);
			}
		}
	}

}
