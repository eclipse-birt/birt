
package org.eclipse.birt.report.model.extension;

import org.eclipse.birt.report.model.elements.ElementsTestUtil;
import org.eclipse.birt.report.model.elements.ExtendedItem;

/**
 * Class that provides util methods for extension.
 */
public class ExtensionTestUtil {

	/**
	 * Gets the extension value stored in the hash-map of extended-item.
	 * 
	 * @param element
	 * @param propName
	 * @return
	 */
	public static Object getLocalExtensionMapValue(ExtendedItem element, String propName) {
		PeerExtensibilityProvider provider = ElementsTestUtil.getProvider(element);
		return provider == null ? null : provider.extensionPropValues.get(propName);
	}
}
