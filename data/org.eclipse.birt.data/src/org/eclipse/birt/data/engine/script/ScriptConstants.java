
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.script;

/**
 * Default key words of global JavaScript objects
 */
public final class ScriptConstants {

	/** constant for object: dataSetRow */
	public static final String DATA_SET_ROW_SCRIPTABLE = "dataSetRow";

	/** constant for object: row */
	public static final String DATA_SET_BINDING_SCRIPTABLE = "row";

	/** constant for object: data */
	public static final String DATA_BINDING_SCRIPTABLE = "data";

	/** constant for object: measure */
	public static final String MEASURE_SCRIPTABLE = "measure";

	/** constant for object: dimension */
	public static final String DIMENSION_SCRIPTABLE = "dimension";

	/** constant for object: _outer */
	public static final String OUTER_RESULT_KEYWORD = "_outer";

	/** constant for object: __rownum */
	public static final String ROW_NUM_KEYWORD = "__rownum";
}
