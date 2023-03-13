/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

/**
 * Settings for rendering a report to an output format. Currently supported
 * options include format of the request, whether to render HTML with style
 * sheet, etc. Potential future options includes image formats in PDF (vector or
 * bitmap), font embedding options, etc.
 * <p>
 * The predefined options strings are what BIRT supports. Other options are
 * available depending on custom extensions.
 *
 * @deprecated use RenderOption instead
 */
@Deprecated
public class RenderOptionBase extends RenderOption {
}
