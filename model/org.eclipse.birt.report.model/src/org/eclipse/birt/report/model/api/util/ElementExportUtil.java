
package org.eclipse.birt.report.model.api.util;

/**
 * Represents the utility class to help export element and structure to library
 * file. The element or structure to export must comply with the following rule:
 * <ul>
 * <li>Element must have name defined.
 * <li>The name property of structure must have value.
 * <li>The element or structure must be contained in design file.
 * </ul>
 * 
 * Any violation will throw <code>IllegalArgumentException</code>.
 */

public class ElementExportUtil extends ElementExportUtilImpl {

}
