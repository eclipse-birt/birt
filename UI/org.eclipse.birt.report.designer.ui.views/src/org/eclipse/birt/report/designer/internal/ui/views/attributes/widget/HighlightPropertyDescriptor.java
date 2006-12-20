
package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HighlightDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.util.ColorManager;

public class HighlightPropertyDescriptor extends PreviewPropertyDescriptor
{

	public HighlightPropertyDescriptor( boolean formStyle )
	{
		super( formStyle );
	}

	protected HighlightDescriptorProvider provider;

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof HighlightDescriptorProvider )
			this.provider = (HighlightDescriptorProvider) provider;
	}

	protected void updatePreview( Object handle )
	{
		if ( handle != null )
		{
			String familyValue = provider.getFontFamily( handle );
			int sizeValue = provider.getFontSize( handle );
			previewLabel.setFontFamily( familyValue );
			previewLabel.setFontSize( sizeValue );
			previewLabel.setBold( provider.isBold( handle ) );
			previewLabel.setItalic( provider.isItalic( handle ) );
			previewLabel.setForeground( provider.getColor( handle ) );
			previewLabel.setBackground( provider.getBackgroundColor( handle ) );
			previewLabel.setUnderline( provider.isUnderline( handle ) );
			previewLabel.setLinethrough( provider.isLinethrough( handle ) );
			previewLabel.setOverline( provider.isOverline( handle ) );
			previewLabel.updateView( );

			if ( provider.getBackgroundColor( handle ) == null && isFormStyle( ) )
			{
				FormWidgetFactory.getInstance( ).paintFormStyle( previewLabel );
				FormWidgetFactory.getInstance( ).adapt( previewLabel );
			}
		}
		else
		{
			previewLabel.restoreDefaultState( );

			previewLabel.setForeground( ColorManager.getColor( -1 ) );
			previewLabel.setBackground( ColorManager.getColor( -1 ) );

			previewLabel.updateView( );

			if ( isFormStyle( ) )
			{
				FormWidgetFactory.getInstance( ).paintFormStyle( previewLabel );
				FormWidgetFactory.getInstance( ).adapt( previewLabel );
			}
		}
	}

}
