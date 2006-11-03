
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeView;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.ui.IViewPart;

public class RepeatHeaderDescriptorProvider implements IDescriptorProvider
{

	public String getDisplayName( )
	{
		return Messages.getString( "ListingSectionPage.RepeatHeader" );
	}

	public Object load( )
	{
		if ( DEUtil.getInputSize( input ) == 1
				&& DEUtil.getInputFirstElement( input ) instanceof ListingHandle )
		{
			ListingHandle listingHandle = (ListingHandle) DEUtil.getInputFirstElement( input );

			return new Boolean( listingHandle.repeatHeader( ) ).toString( );

		}
		return "false";
	}

	public void save( Object value ) throws SemanticException
	{
		if ( DEUtil.getInputSize( input ) == 1
				&& DEUtil.getInputFirstElement( input ) instanceof ListingHandle )
		{
			ListingHandle listingHandle = (ListingHandle) DEUtil.getInputFirstElement( input );
			try
			{
				listingHandle.setRepeatHeader( Boolean.getBoolean( value.toString( ) ) );
			}
			catch ( SemanticException e )
			{
			}
			
			
			refreshRestoreProperty( );
		}
	}

	private Object input;

	public void setInput( Object input )
	{
		this.input = input;

	}
	
	protected void refreshRestoreProperty( )
	{
		IViewPart view = UIUtil.getView( "org.eclipse.birt.report.designer.ui.attributes.AttributeView" );
		if ( view != null
				&& view instanceof AttributeView
				&& ( (AttributeView) view ).getCurrentPage( ) instanceof AttributeViewPage )
		{

			( (AttributeViewPage) ( (AttributeView) view ).getCurrentPage( ) ).resetRestorePropertiesAction( DEUtil.getInputElements( input ) );

		}
	}

}
