
package org.eclipse.birt.report.model.plugin;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.extension.oda.IODAProviderFactory;
import org.eclipse.birt.report.model.extension.oda.ODAProvider;

/**
 * Factory used to create an ODA provider.
 * 
 */
public class ODABaseProviderFactory implements IODAProviderFactory
{

	public ODAProvider createODAProvider( DesignElement element,
			String extensionID )
	{
		return new OdaExtensibilityProvider( element, extensionID );
	}
}
