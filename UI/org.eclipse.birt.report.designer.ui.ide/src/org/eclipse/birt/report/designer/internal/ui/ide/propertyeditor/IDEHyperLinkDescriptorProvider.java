
package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.GroupHyperLinkDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.HyperlinkBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

public class IDEHyperLinkDescriptorProvider extends GroupHyperLinkDescriptorProvider
{

	public boolean hyperLinkSelected( )
	{
		boolean flag = true;
		HyperlinkBuilder dialog = new HyperlinkBuilder( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ) ,true );
		getActionStack( ).startTrans( Messages.getString( "HyperLinkPage.Menu.Save" ) ); //$NON-NLS-1$
		ActionHandle handle = getActionHandle( );
		if ( handle == null )
		{
			try
			{
				handle = DEUtil.setAction( (ReportItemHandle) DEUtil.getInputFirstElement( input ),
						StructureFactory.createAction( ) );
			}
			catch ( SemanticException e1 )
			{
				getActionStack( ).rollback( );
				ExceptionHandler.handle( e1 );
				return false;
			}
		}
		dialog.setInput( handle );
		needRefresh = false;
		boolean isOK = dialog.open( ) == Dialog.OK;
		needRefresh = true;
		if ( isOK )
		{
			getActionStack( ).commit( );
			flag = true;
		}
		else
		{
			getActionStack( ).rollback( );
			flag = false;
		}
		return flag;
	}
}
