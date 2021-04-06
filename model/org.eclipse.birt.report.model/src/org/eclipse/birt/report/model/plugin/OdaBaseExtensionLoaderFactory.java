package org.eclipse.birt.report.model.plugin;

import org.eclipse.birt.report.model.extension.oda.IOdaExtensionLoader;
import org.eclipse.birt.report.model.extension.oda.IOdaExtensionLoaderFactory;

public class OdaBaseExtensionLoaderFactory implements IOdaExtensionLoaderFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.IOdaExtensionLoaderFactory#
	 * createOdaExtensionLoader()
	 */
	public IOdaExtensionLoader createOdaExtensionLoader() {

		return new OdaExtensibilityExtensionLoader();
	}

}
