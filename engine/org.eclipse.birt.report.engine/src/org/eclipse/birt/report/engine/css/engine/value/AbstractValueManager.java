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
 *  Actuate Corporation  - modification of Batik's AbstractValueManager.java to support BIRT's CSS rules
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value;

import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSStylableElement;
import org.eclipse.birt.report.engine.css.engine.ValueManager;

/**
 * This class provides an abstract implementation of the ValueManager interface.
 *
 */
public abstract class AbstractValueManager extends AbstractValueFactory implements ValueManager {
	/**
	 * Implements
	 * {@link ValueManager#computeValue(CSSStylableElement,CSSEngine,int,Value)}.
	 */
	@Override
	public Value computeValue(CSSStylableElement elt, CSSEngine engine, int idx, Value value) {
		return value;
	}

}
