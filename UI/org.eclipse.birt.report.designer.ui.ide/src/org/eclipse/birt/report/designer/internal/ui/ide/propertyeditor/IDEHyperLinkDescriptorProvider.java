
package org.eclipse.birt.report.designer.internal.ui.ide.propertyeditor;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.HyperLinkDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.HyperlinkBuilder;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.PlatformUI;

public class IDEHyperLinkDescriptorProvider extends HyperLinkDescriptorProvider
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

		boolean isOK = dialog.open( ) == Dialog.OK;
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

	private CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}

	private ActionHandle getActionHandle( )
	{
		return DEUtil.getActionHandle( (ReportItemHandle) DEUtil.getInputFirstElement( input ) );
	}
	
}
