/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
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

package org.eclipse.birt.report.designer.core.commands;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.util.MetricUtility;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.gef.commands.Command;

/**
 * Move Guide Command
 *
 */
public class MoveGuideCommand extends Command {

	protected static final Logger logger = Logger.getLogger(MoveGuideCommand.class.getName());
	private int pDelta;
	private String propertyName;

	/**
	 * Constructor
	 *
	 * @param delta
	 * @param propertyName
	 */
	public MoveGuideCommand(int delta, String propertyName) {
		super();
		pDelta = delta;
		this.propertyName = propertyName;
	}

	/**
	 * Ececute this command
	 */
	@Override
	public void execute() {
		ModuleHandle handle = SessionHandleAdapter.getInstance().getReportDesignHandle();
		MasterPageHandle page = SessionHandleAdapter.getInstance().getFirstMasterPageHandle(handle);
		String unit = handle.getDefaultUnits();

		// This is only a patch, maybe the old report design file the default
		// unin of the
		// LibraryHandle is null,
		if (unit == null) {
			unit = DesignChoiceConstants.UNITS_IN;
		}
		double value = MetricUtility.pixelToPixelInch(pDelta);
		if (value < 0.0) {
			value = 0.0;
		}
		DimensionValue dim = DimensionUtil.convertTo(value, DesignChoiceConstants.UNITS_IN, unit);

		if (DesignerConstants.TRACING_COMMANDS) {
			System.out.println("MoveGuideCommand >>  Starts. Target: " //$NON-NLS-1$
					+ page.getDisplayLabel() + ",Property: " //$NON-NLS-1$
					+ propertyName + ",Value: " //$NON-NLS-1$
					+ dim.toDisplayString());
		}
		try {
			page.setProperty(propertyName, dim);
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("MoveGuideCommand >> Finished."); //$NON-NLS-1$
			}
		} catch (SemanticException e) {
			if (DesignerConstants.TRACING_COMMANDS) {
				System.out.println("MoveGuideCommand >> Failed."); //$NON-NLS-1$
			}
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

	}
}
