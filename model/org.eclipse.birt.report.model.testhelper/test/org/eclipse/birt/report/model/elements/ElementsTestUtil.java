
package org.eclipse.birt.report.model.elements;

import org.eclipse.birt.report.model.extension.PeerExtensibilityProvider;

/**
 * Class that provides util method for elements.
 */
public class ElementsTestUtil {

	/**
	 * 
	 * @param element
	 * @return
	 */
	public static PeerExtensibilityProvider getProvider(ExtendedItem element) {
		return element == null ? null : element.provider;
	}

}
