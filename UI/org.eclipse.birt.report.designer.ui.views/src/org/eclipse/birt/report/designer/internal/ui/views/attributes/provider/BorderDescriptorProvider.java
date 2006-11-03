
package org.eclipse.birt.report.designer.internal.ui.views.attributes.provider;

import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeView;
import org.eclipse.birt.report.designer.ui.views.attributes.AttributeViewPage;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.GroupElementHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.ui.IViewPart;

public abstract class BorderDescriptorProvider implements IDescriptorProvider
{

	protected Object input;

	protected HashMap styleMap = new HashMap( );

	public BorderDescriptorProvider( )
	{
		styleMap.put( StyleHandle.BORDER_LEFT_STYLE_PROP, new Boolean( false ) );
		styleMap.put( StyleHandle.BORDER_RIGHT_STYLE_PROP, new Boolean( false ) );
		styleMap.put( StyleHandle.BORDER_TOP_STYLE_PROP, new Boolean( false ) );
		styleMap.put( StyleHandle.BORDER_BOTTOM_STYLE_PROP, new Boolean( false ) );
	}

	public void setStyleProperty( String style, Boolean value )
	{
		styleMap.put( style, value );
	}

	public void setInput( Object input )
	{
		this.input = input;
	}

	protected String getLocalStringValue( String property )
	{
		GroupElementHandle handle = null;
		if ( input instanceof List )
			handle = DEUtil.getGroupElementHandle( (List) input );
		if ( handle == null )
			return "";
		String value = handle.getLocalStringProperty( property );
		if ( value == null )
		// && multiSelectionHandle.shareSameValue( property ) )
		{
			value = ""; //$NON-NLS-1$
		}
		return value;
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

	protected void save( String property, Object value )
			throws SemanticException
	{
		if ( input instanceof GroupElementHandle )
		{
			( (GroupElementHandle) input ).setProperty( property, value );
		}
		else if ( input instanceof List )
		{
			DEUtil.getGroupElementHandle( (List) input ).setProperty( property,
					value );
		}
		refreshRestoreProperty( );
	}

	abstract void handleModifyEvent( );
}
