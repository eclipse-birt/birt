/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.de.internal;

import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.core.util.ICrosstabUpdateListener;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * MeasureViewTask
 */
public class MeasureViewTask extends AbstractCrosstabModelTask {

	protected MeasureViewHandle focus = null;

	/**
	 *
	 * @param theCrosstab
	 * @param levelView
	 */
	public MeasureViewTask(MeasureViewHandle levelView) {
		super(levelView);
		this.focus = levelView;
	}

	/**
	 * Removes header cell for current measure.
	 *
	 * @throws SemanticException
	 */
	public void removeHeader() throws SemanticException {
		PropertyHandle propHandle = focus.getHeaderProperty();

		List contents = propHandle.getContents();

		CommandStack stack = focus.getCommandStack();
		stack.startTrans(Messages.getString("MeasureViewTask.msg.remove.header")); //$NON-NLS-1$

		try {
			for (int i = 0; i < contents.size(); i++) {
				((DesignElementHandle) contents.get(i)).drop();
			}

		} catch (SemanticException e) {
			focus.getLogger().log(Level.WARNING, e.getMessage(), e);
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	/**
	 * Adds header cell for current measure. If header cell already exists, this
	 * method just does nothing.
	 *
	 * @throws SemanticException
	 */
	public void addHeader() throws SemanticException {
		PropertyHandle propHandle = focus.getHeaderProperty();

		int expectHeaders = CrosstabModelUtil.computeAllMeasureHeaderCount(crosstab, focus);
		int availableHeaders = propHandle.getContentCount();

		if (availableHeaders >= expectHeaders) {
			focus.getLogger().log(Level.INFO, "Measure header already present, need not add another"); //$NON-NLS-1$
			return;
		}

		CommandStack stack = focus.getCommandStack();
		stack.startTrans(Messages.getString("MeasureViewTask.msg.add.header")); //$NON-NLS-1$

		try {
			for (int i = 0; i < expectHeaders - availableHeaders; i++) {
				ExtendedItemHandle headerCell = CrosstabExtendedItemFactory.createCrosstabCell(focus.getModuleHandle());
				propHandle.add(headerCell);

				CrosstabModelUtil.notifyCreation(ICrosstabUpdateListener.MEASURE_HEADER,
						CrosstabUtil.getReportItem(headerCell), null);
			}
		} catch (SemanticException e) {
			focus.getLogger().log(Level.WARNING, e.getMessage(), e);
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

}
