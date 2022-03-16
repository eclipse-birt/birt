/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.device.util;

import java.awt.font.FontRenderContext;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;

/**
 * Factory of TextLayout
 */

public interface ITextLayoutFactory {

	ChartTextLayout createTextLayout(String value, Map<? extends Attribute, ?> fontAttributes, FontRenderContext frc);

}
