/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

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
