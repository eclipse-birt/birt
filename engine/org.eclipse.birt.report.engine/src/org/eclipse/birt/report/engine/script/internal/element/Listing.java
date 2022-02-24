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
import org.eclipse.birt.report.engine.api.script.element.IFilterCondition;
import org.eclipse.birt.report.engine.api.script.element.IListing;
import org.eclipse.birt.report.engine.api.script.element.ISortCondition;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of Listing
 */
public class Listing extends ReportItem implements IListing {

	/**
	 * Constructor
	 *
	 * @param listing
	 */
	public Listing(ListingHandle listing) {
		super(listing);
	}

	@Override
	public IFilterCondition[] getFilterConditions() {
		org.eclipse.birt.report.model.api.simpleapi.IFilterCondition[] values = ((org.eclipse.birt.report.model.api.simpleapi.IListing) designElementImpl)
				.getFilterConditions();
		IFilterCondition[] filterConditions = new IFilterCondition[values.length];

		for (int i = 0; i < values.length; i++) {
			filterConditions[i] = new FilterConditionImpl(
					(org.eclipse.birt.report.model.api.simpleapi.IFilterCondition) values[i]);
		}
		return filterConditions;
	}

	@Override
	public ISortCondition[] getSortConditions() {
		org.eclipse.birt.report.model.api.simpleapi.ISortCondition[] values = ((org.eclipse.birt.report.model.api.simpleapi.IListing) designElementImpl)
				.getSortConditions();
		ISortCondition[] sortConditions = new ISortCondition[values.length];

		for (int i = 0; i < values.length; i++) {
			sortConditions[i] = new SortConditionImpl(
					(org.eclipse.birt.report.model.api.simpleapi.ISortCondition) values[i]);
		}
		return sortConditions;
	}

	/**
	 * Add FilterCondition
	 *
	 * @param condition
	 * @throws ScriptException
	 */

	@Override
	public void addFilterCondition(IFilterCondition condition) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IListing) designElementImpl)
					.addFilterCondition(SimpleElementFactory.getInstance()
							.createFilterCondition((FilterCondition) condition.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	/**
	 * Add SortCondition
	 *
	 * @param condition
	 * @throws ScriptException
	 */

	@Override
	public void addSortCondition(ISortCondition condition) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IListing) designElementImpl).addSortCondition(
					SimpleElementFactory.getInstance().createSortCondition((SortKey) condition.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public void removeFilterCondition(IFilterCondition condition) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IListing) designElementImpl)
					.removeFilterCondition(SimpleElementFactory.getInstance()
							.createFilterCondition((FilterCondition) condition.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public void removeFilterConditions() throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IListing) designElementImpl).removeFilterConditions();
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public void removeSortCondition(ISortCondition condition) throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IListing) designElementImpl).removeSortCondition(
					SimpleElementFactory.getInstance().createSortCondition((SortKey) condition.getStructure()));
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}
	}

	@Override
	public void removeSortConditions() throws ScriptException {
		try {
			((org.eclipse.birt.report.model.api.simpleapi.IListing) designElementImpl).removeSortConditions();
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

}
