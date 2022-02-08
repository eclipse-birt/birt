/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.log;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.util.SecurityUtil;

import com.ibm.icu.util.ULocale;

/**
 * A default implementation for the chart logging framework
 */
public final class DefaultLoggerImpl implements ILogger {

	/**
	 * A singleton instance maintained internally.
	 */
	private static DefaultLoggerImpl dli = null;

	/**
	 * The verbose level associated with logging messages.
	 */
	private int iVerboseLevel = ILogger.ERROR | ILogger.WARNING | ILogger.FATAL | ILogger.INFORMATION;

	/**
	 * Returns a singleton instance of the logger.
	 * 
	 * @return A singleton instance of the logger.
	 */
	public synchronized static final ILogger instance() {
		if (dli == null) {
			dli = new DefaultLoggerImpl();
		}
		return dli;
	}

	/**
	 * Cannot be instantiated externally
	 */
	private DefaultLoggerImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.log.ILogger#setVerboseLevel(int)
	 */
	public final void setVerboseLevel(int iVerboseLevel) {
		this.iVerboseLevel = iVerboseLevel;
	}

	/**
	 * @deprecated Logs messages originating from scripts associated with a chart
	 *             model.
	 * 
	 * @param sMessage The informational message to be logged.
	 */
	public final void logFromScript(String sMessage) {
		if ((iVerboseLevel & ILogger.INFORMATION) == ILogger.INFORMATION) {
			System.out.println(Messages.getString("info.log.script", new Object[] { sMessage }, ULocale.getDefault())); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.log.ILogger#log(int, java.lang.String)
	 */
	public void log(int iCode, String sMessage) {
		if (iCode == ILogger.INFORMATION && (iVerboseLevel & ILogger.INFORMATION) == ILogger.INFORMATION) {
			System.out.println(Messages.getString("info.log.info", new Object[] { sMessage }, ULocale.getDefault())); //$NON-NLS-1$
		} else if (iCode == ILogger.WARNING && (iVerboseLevel & ILogger.WARNING) == ILogger.WARNING) {
			System.out.println(Messages.getString("info.log.warn", new Object[] { sMessage }, ULocale.getDefault())); //$NON-NLS-1$
		} else if (iCode == ILogger.ERROR && (iVerboseLevel & ILogger.ERROR) == ILogger.ERROR) {
			System.err.println(Messages.getString("info.log.err", new Object[] { sMessage }, ULocale.getDefault())); //$NON-NLS-1$
		} else if (iCode == ILogger.FATAL && (iVerboseLevel & ILogger.FATAL) == ILogger.FATAL) {
			System.err.println(Messages.getString("info.log.fatal", new Object[] { sMessage }, ULocale.getDefault())); //$NON-NLS-1$
			SecurityUtil.sysExit(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.log.ILogger#log(int, java.lang.Exception)
	 */
	public void log(Exception ex) {
		System.err.println(Messages.getString("info.log.err", new Object[] { ex.toString() }, ULocale.getDefault())); //$NON-NLS-1$
		ex.printStackTrace();
	}

}
