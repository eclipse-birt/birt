
package org.eclipse.birt.report.designer.internal.ui.views.attributes.page;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.PreviewPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.BorderSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.section.PreviewSection;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.PreviewPropertyDescriptor;
import org.eclipse.swt.widgets.Composite;

public class PreviewPage extends AttributePage
{

	private PreviewPropertyDescriptorProvider provider;
	private boolean isTabbed = false;



	public PreviewPage(  boolean isTabbed )
	{
		this.isTabbed = isTabbed;
	}

	public void buildUI( Composite parent  )
	{
		super.buildUI( parent );
		container.setLayout( WidgetUtil.createGridLayout( 1 ) );
		previewSection = new PreviewSection( provider.getDisplayName( ),
						container,
						true,
						isTabbed );
		previewSection.setPreview( preview );
		previewSection.setProvider( provider );
		previewSection.setFillPreview( true );
		addSection( PageSectionId.PREVIEW_PREVIEW, previewSection );

		createSections( );
		layoutSections( );
	}

	public void setProvider( PreviewPropertyDescriptorProvider provider )
	{
		this.provider = provider;
	}

	PreviewPropertyDescriptor preview;
	private PreviewSection previewSection;

	public void setPreview( PreviewPropertyDescriptor preview )
	{
		this.preview = preview;
	}

	private boolean checkControl( PreviewSection preview )
	{
		return preview != null
				&& preview.getPreviewControl( ) != null
				&& !preview.getPreviewControl( )
						.getControl( )
						.isDisposed( );
	}
	
	public void postElementEvent( )
	{
		if ( checkControl( previewSection ) )
		previewSection.getPreviewControl( ).postElementEvent( );
	}
}
