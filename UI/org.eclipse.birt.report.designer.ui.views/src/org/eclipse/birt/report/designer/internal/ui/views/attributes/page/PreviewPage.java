
package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.util.SortMap;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PreviewPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.PreviewSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PreviewPropertyDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

public class PreviewPage extends AttributePage {

	private PreviewPropertyDescriptorProvider provider;
	private boolean isTabbed = false;

	public PreviewPage(boolean isTabbed) {
		this.isTabbed = isTabbed;
	}

	public void buildUI(Composite parent) {
		container = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		((ScrolledComposite) container).setExpandHorizontal(true);
		((ScrolledComposite) container).setExpandVertical(true);
		container.addControlListener(new ControlAdapter() {

			public void controlResized(ControlEvent e) {
				computeSize();
			}
		});

		composite = new Composite(container, SWT.NONE);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));

		if (sections == null)
			sections = new SortMap();
		composite.setLayout(WidgetUtil.createGridLayout(1));

		previewSection = new PreviewSection(provider.getDisplayName(), composite, true, isTabbed);
		previewSection.setPreview(preview);
		previewSection.setProvider(provider);
		previewSection.setHeight(160);
		previewSection.setFillPreview(true);
		addSection(PageSectionId.PREVIEW_PREVIEW, previewSection);

		createSections();
		layoutSections();

		((ScrolledComposite) container).setContent(composite);
	}

	private void computeSize() {
		Point size = composite.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		((ScrolledComposite) container).setMinSize(size.x, size.y + 10);
		container.layout();

	}

	public void setProvider(PreviewPropertyDescriptorProvider provider) {
		this.provider = provider;
	}

	PreviewPropertyDescriptor preview;
	private PreviewSection previewSection;
	private Composite composite;

	public void setPreview(PreviewPropertyDescriptor preview) {
		this.preview = preview;
	}

	private boolean checkControl(PreviewSection preview) {
		return preview != null && preview.getPreviewControl() != null
				&& !preview.getPreviewControl().getControl().isDisposed();
	}

	public void postElementEvent() {
		if (checkControl(previewSection))
			previewSection.getPreviewControl().postElementEvent();
	}
}
