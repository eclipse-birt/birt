
package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.MapPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.util.ColorManager;

public class MapPropertyDescriptor extends PreviewPropertyDescriptor
{

	public MapPropertyDescriptor( boolean formStyle )
	{
		super( formStyle );
	}

	protected MapPropertyDescriptorProvider provider;

	public void setDescriptorProvider( IDescriptorProvider provider )
	{
		super.setDescriptorProvider( provider );
		if ( provider instanceof MapPropertyDescriptorProvider )
			this.provider = (MapPropertyDescriptorProvider) provider;
	}

	protected void updatePreview( Object handle )
	{

		if ( handle != null )
		{
			previewLabel.setText( provider.getDisplayText( handle ) );
			previewLabel.updateView( );
		}
		else
		{
			previewLabel.restoreDefaultState( );

			previewLabel.setForeground( ColorManager.getColor( -1 ) );
			previewLabel.setBackground( ColorManager.getColor( -1 ) );

			previewLabel.setText( "" ); //$NON-NLS-1$
			previewLabel.updateView( );

			if ( isFormStyle( ) )
			{
				FormWidgetFactory.getInstance( ).paintFormStyle( previewLabel );
				FormWidgetFactory.getInstance( ).adapt( previewLabel );
			}
		}
	}

}
