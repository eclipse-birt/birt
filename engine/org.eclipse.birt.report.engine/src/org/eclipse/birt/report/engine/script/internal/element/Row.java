/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.engine.api.script.element.IHighlightRule;
import org.eclipse.birt.report.engine.api.script.element.IRow;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

public class Row extends DesignElement implements IRow {

	public Row(RowHandle handle) {
		super(handle);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getHeight()
	 */

	@Override
	public String getHeight() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl).getHeight();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getBookmark()
	 */

	@Override
	public String getBookmark() {
		return ((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl).getBookmark();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.engine.api.script.element.IRow#setBookmark(java.lang.
	 * String)
	 */

	@Override
	public void setBookmark(String value) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl).setBookmark(value);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#
	 * addHighlightRule(org.eclipse.birt.report.engine.api.script.element.
	 * IHighlightRule)
	 */

	@Override
	public void addHighlightRule(IHighlightRule rule) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl).addHighlightRule(
					SimpleElementFactory.getInstance().createHighlightRule((HighlightRule) rule.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#
	 * getHighlightRules()
	 */

	@Override
	public IHighlightRule[] getHighlightRules() {
		org.eclipse.birt.report.model.api.simpleapi.IHighlightRule[] values = ((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl)
				.getHighlightRules();
		IHighlightRule[] highlightRules = new IHighlightRule[values.length];

		for (int i = 0; i < values.length; i++) {
			highlightRules[i] = new HighlightRuleImpl(values[i]);
		}
		return highlightRules;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#
	 * removeHighlightRules()
	 */

	@Override
	public void removeHighlightRules() throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl).removeHighlightRules();
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#
	 * removeHighlightRule(org.eclipse.birt.report.engine.api.script.element.
	 * IHighlightRule)
	 */

	@Override
	public void removeHighlightRule(IHighlightRule rule) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl).removeHighlightRule(
					SimpleElementFactory.getInstance().createHighlightRule((HighlightRule) rule.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#
	 * addHideRule(org.eclipse.birt.report.engine.api.script.element.IHideRule)
	 */
	@Override
	public void addHideRule(IHideRule rule) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl)
					.addHideRule(SimpleElementFactory.getInstance().createHideRule((HideRule) rule.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#
	 * getHideRules()
	 */

	@Override
	public IHideRule[] getHideRules() {
		org.eclipse.birt.report.model.api.simpleapi.IHideRule[] values = ((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl)
				.getHideRules();
		IHideRule[] hideRules = new IHideRule[values.length];

		for (int i = 0; i < values.length; i++) {
			hideRules[i] = new HideRuleImpl(values[i]);
		}
		return hideRules;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#
	 * removeHideRule(org.eclipse.birt.report.engine.api.script.element.IHideRule)
	 */

	@Override
	public void removeHideRule(IHideRule rule) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl)
					.removeHideRule(SimpleElementFactory.getInstance().createHideRule((HideRule) rule.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#
	 * removeHideRules()
	 */

	@Override
	public void removeHideRules() throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IRow) designElementImpl).removeHideRules();
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}
}
