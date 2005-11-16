
package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.internal.ui.dialogs.ImageBuilderDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.TextEditDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.ui.PlatformUI;

public class ElementBuilderFactory
{

	static private ElementBuilderFactory instance;

	private ElementBuilderFactory( )
	{
	}

	/**
	 * @return instance of factory.
	 */
	static public ElementBuilderFactory getInstance( )
	{
		if ( instance == null )
		{
			instance = new ElementBuilderFactory( );
		}
		return instance;
	}

	/**
	 * Creates builder for given element
	 * @param handle
	 * @return
	 */
	public Object createBuilder( DesignElementHandle handle )
	{
		if ( handle instanceof TextItemHandle )
		{
			return new TextEditDialog( handle.getName( ),
					(TextItemHandle) handle );
		}
		if ( handle instanceof TextDataHandle )
		{
			ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI
					.getWorkbench( ).getDisplay( ).getActiveShell( ),
					( (TextDataHandle) handle ).getValueExpr( ) );

			dialog.setExpressionProvier( ( new ExpressionProvider( handle
					.getModuleHandle( ), DEUtil.getDataSetList( handle ) ) ) );

			return ( dialog );
		}
		if ( handle instanceof DataItemHandle )
		{
			ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI
					.getWorkbench( ).getDisplay( ).getActiveShell( ),
					( (DataItemHandle) handle ).getValueExpr( ) );

			dialog.setExpressionProvier( ( new ExpressionProvider( handle
					.getModuleHandle( ), DEUtil.getDataSetList( handle ) ) ) );

			return ( dialog );
		}
		if(handle instanceof ImageHandle)
		{
			ImageBuilderDialog dialog = new ImageBuilderDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ) );
			dialog.setInput( handle );
			return dialog;
		}

		return null;
	}

}
