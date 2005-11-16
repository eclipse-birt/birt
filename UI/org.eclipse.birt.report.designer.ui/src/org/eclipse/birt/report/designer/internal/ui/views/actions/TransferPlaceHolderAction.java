
package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.ui.views.ProviderFactory;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.gef.Request;

public class TransferPlaceHolderAction extends CreatePlaceHolderAction
{

	public TransferPlaceHolderAction( Object selectedObject )
	{
		super( selectedObject, "transfer place Holder" );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#doAction()
	 */
	protected boolean doAction( ) throws Exception
	{
		if ( getSelectedElement( ) == null )
		{
			return false;
		}

		return ProviderFactory
				.createProvider( getSelectedElement( ) )
				.performRequest(
						getSelectedElement( ),
						new Request(
								IRequestConstants.REQUEST_TRANSFER_PLACEHOLDER ) );
	}

	public boolean isEnabled( )
	{
		return getSelectedElement( ) instanceof TemplateElementHandle
				&& ( ( (TemplateElementHandle) getSelectedElement( ) )
						.getDefaultElement( ) != null );
	}

}
