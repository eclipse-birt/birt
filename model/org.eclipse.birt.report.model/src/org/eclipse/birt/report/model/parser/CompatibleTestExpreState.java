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

package org.eclipse.birt.report.model.parser;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * This state handle the compatible issue for old design file of highlight rule.
 * 
 * old design file:
 * 
 * <pre>
 *        
 *                    &lt;expression name=&quot;highlightTestExpr&quot;&gt;[this]&lt;/expression&gt;
 *                    &lt;list-property name=&quot;highlightRules&quot;&gt;
 *                    &lt;structure&gt;
 *                    &lt;property name=&quot;operator&quot;&gt;is-null&lt;/property&gt;
 *                    &lt;structure name=&quot;dateTimeFormat&quot;&gt;
 *                    &lt;property name=&quot;category&quot;&gt;Custom&lt;/property&gt;
 *                    &lt;property name=&quot;pattern&quot;&gt;yyyy/mm/dd&lt;/property&gt;
 *                    &lt;/structure&gt;
 *                    &lt;structure name=&quot;numberFormat&quot;&gt;
 *                    &lt;property name=&quot;category&quot;&gt;Custom&lt;/property&gt;
 *                    &lt;/structure&gt;
 *                    &lt;structure name=&quot;stringFormat&quot;&gt;
 *                    &lt;property name=&quot;category&quot;&gt;noformat&lt;/property&gt;
 *                    &lt;/structure&gt;
 *                    &lt;expression name=&quot;value1&quot;&gt;&quot;10&quot;&lt;/expression&gt;
 *                    &lt;expression name=&quot;value2&quot;&gt;&quot;20&quot;&lt;/expression&gt;         
 *                    &lt;/structure&gt;
 *                    &lt;structure&gt;
 *                    &lt;property name=&quot;operator&quot;&gt;is-null&lt;/property&gt;         
 *                    &lt;/structure&gt;
 *                    &lt;structure&gt;
 *                    &lt;property name=&quot;operator&quot;&gt;is-not-null&lt;/property&gt;                   
 *                    &lt;/structure&gt;
 *                    &lt;structure&gt;
 *                    &lt;property name=&quot;operator&quot;&gt;is-true&lt;/property&gt;                  
 *                    &lt;/structure&gt;
 *                    &lt;structure&gt;
 *                    &lt;property name=&quot;operator&quot;&gt;is-false&lt;/property&gt;                
 *                    &lt;/structure&gt;
 *                    &lt;/list-property&gt;
 * </pre>
 * 
 * new design file:
 * 
 * <pre>
 *                    &lt;list-property name=&quot;highlightRules&quot;&gt;
 *                   &lt;structure&gt;
 *                   &lt;property name=&quot;operator&quot;&gt;is-null&lt;/property&gt;
 *                   &lt;structure name=&quot;dateTimeFormat&quot;&gt;
 *                   &lt;property name=&quot;category&quot;&gt;Custom&lt;/property&gt;
 *                   &lt;property name=&quot;pattern&quot;&gt;yyyy/mm/dd&lt;/property&gt;
 *                   &lt;/structure&gt;
 *                   &lt;structure name=&quot;numberFormat&quot;&gt;
 *                   &lt;property name=&quot;category&quot;&gt;Custom&lt;/property&gt;
 *                   &lt;/structure&gt;
 *                   &lt;structure name=&quot;stringFormat&quot;&gt;
 *                   &lt;property name=&quot;category&quot;&gt;noformat&lt;/property&gt;
 *                   &lt;/structure&gt;
 *                   &lt;expression name=&quot;testExpr&quot;&gt;[this]&lt;/expression&gt;
 *                   &lt;expression name=&quot;value1&quot;&gt;&quot;10&quot;&lt;/expression&gt;
 *                   &lt;expression name=&quot;value2&quot;&gt;&quot;20&quot;&lt;/expression&gt;
 *                   &lt;/structure&gt;
 *                   &lt;structure&gt;
 *                   &lt;property name=&quot;operator&quot;&gt;is-null&lt;/property&gt;
 *                   &lt;expression name=&quot;testExpr&quot;&gt;[this]&lt;/expression&gt;
 *                   &lt;/structure&gt;
 *                   &lt;structure&gt;
 *                   &lt;property name=&quot;operator&quot;&gt;is-not-null&lt;/property&gt;
 *                   &lt;expression name=&quot;testExpr&quot;&gt;[this]&lt;/expression&gt;
 *                   &lt;/structure&gt;
 *                   &lt;structure&gt;
 *                   &lt;property name=&quot;operator&quot;&gt;is-true&lt;/property&gt;
 *                   &lt;expression name=&quot;testExpr&quot;&gt;[this]&lt;/expression&gt;
 *                   &lt;/structure&gt;
 *                   &lt;structure&gt;
 *                   &lt;property name=&quot;operator&quot;&gt;is-false&lt;/property&gt;
 *                   &lt;expression name=&quot;testExpr&quot;&gt;[this]&lt;/expression&gt;
 *                   &lt;/structure&gt;
 *                   &lt;/list-property&gt;
 * </pre>
 * 
 */
public class CompatibleTestExpreState extends CompatibleMiscExpressionState {

	private String tempVeluekey = null;

	/**
	 * Constructs <code>CompatibleTestExpreState</code>.
	 * 
	 * @param theHandler the parser handler
	 * @param element    the current element
	 * @param key
	 */

	public CompatibleTestExpreState(ModuleParserHandler theHandler, DesignElement element, String key) {
		super(theHandler, element);
		tempVeluekey = key;
	}

	public void end() throws SAXException {
		String value = text.toString();

		if (handler.versionNumber >= VersionUtil.VERSION_3_2_0) {
			handler.tempValue.put(tempVeluekey, value);
			return;
		}
		DesignElement target = BoundDataColumnUtil.findTargetOfBoundColumns(element, handler.module);

		setupBoundDataColumns(target, value, true);
		handler.tempValue.put(tempVeluekey, value);
	}
}
