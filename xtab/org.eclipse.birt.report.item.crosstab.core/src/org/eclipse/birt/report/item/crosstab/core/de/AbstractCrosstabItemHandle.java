/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.logging.Logger;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.extension.CompatibilityStatus;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.extension.ReportItem;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.util.CubeUtil;

/**
 * Abstract report item class for all crosstab items.
 */

public class AbstractCrosstabItemHandle extends ReportItem implements ICrosstabConstants {

	/**
	 * The cached design element handle.
	 */
	protected ExtendedItemHandle handle;

	/**
	 * The module handle of the extended item.
	 */
	protected ModuleHandle moduleHandle;

	/**
	 * Log instance.
	 */
	protected static final Logger logger = Logger.getLogger(AbstractCrosstabItemHandle.class.getName());

	protected static final CompatibilityStatus COMP_OK_STATUS = new CompatibilityStatus();

	/**
	 * 
	 * @param handle
	 */
	protected AbstractCrosstabItemHandle(DesignElementHandle element) {
		if (!(element instanceof ExtendedItemHandle))
			throw new IllegalArgumentException("the element is not valid ExtendedItemHandle!"); //$NON-NLS-1$
		this.handle = (ExtendedItemHandle) element;
		this.moduleHandle = element.getModuleHandle();
	}

	/**
	 * Returns the model handle for this report item
	 * 
	 * @return the design element handle of this view
	 */
	public DesignElementHandle getModelHandle() {
		return handle;
	}

	/**
	 * Finds the extended item handle. The returned item must have the same name as
	 * the given one and its extension name is the same as the given one too.
	 * 
	 * @param name          the item name to search
	 * @param extensionName
	 * @return the extended item handle if found, otherwise null
	 */
	protected IReportItem findExtendedItem(String name, String extensionName) {
		if (extensionName == null)
			throw new IllegalArgumentException("extension name can not be null"); //$NON-NLS-1$
		DesignElementHandle element = moduleHandle.findElement(name);
		return CrosstabUtil.getReportItem(element, extensionName);
	}

	/**
	 * Gets the commands stack.
	 * 
	 * @return command stack
	 */
	public CommandStack getCommandStack() {
		return moduleHandle.getCommandStack();
	}

	/**
	 * Gets the nearest crosstab container handle.
	 * 
	 * @return the nearest crosstab container if found, otherwise null
	 */
	public DesignElementHandle getCrosstabHandle() {
		DesignElementHandle e = handle;
		while (e != null) {
			if (ICrosstabConstants.CROSSTAB_EXTENSION_NAME
					.equals(e.getStringProperty(ExtendedItemHandle.EXTENSION_NAME_PROP)))
				return e;
			e = e.getContainer();
		}
		return null;
	}

	/**
	 * Gets the crosstab report item.
	 * 
	 * @return
	 */
	public CrosstabReportItemHandle getCrosstab() {
		return (CrosstabReportItemHandle) CrosstabUtil.getReportItem(getCrosstabHandle());
	}

	/**
	 * Find level handle by full name.
	 * 
	 * @param fullLevelName
	 * @return
	 */
	public LevelHandle findLevelHandle(String fullLevelName) {
		if (fullLevelName == null) {
			return null;
		}

		LevelHandle level = null;
		CrosstabReportItemHandle crosstab = this.getCrosstab();
		if (crosstab != null && crosstab.getCube() != null) {
			String[] slices = CubeUtil.splitLevelName(fullLevelName);
			if (slices[0] != null && slices[1] != null) {
				DimensionHandle cubeDimension = crosstab.getCube().getDimension(slices[0]);
				if (cubeDimension != null && cubeDimension.getDefaultHierarchy() != null) {
					level = cubeDimension.getDefaultHierarchy().getLevel(slices[1]);
				}

				if (level == null && cubeDimension != null && cubeDimension.isTimeType()) {
					cubeDimension = crosstab.getCube().getDimension(slices[0], false);
					if (cubeDimension != null && cubeDimension.getDefaultHierarchy() != null) {
						level = cubeDimension.getDefaultHierarchy().getLevel(slices[1]);
					}
				}
			}
		}

		return level;
	}

	/**
	 * Returns the container for current handle.
	 * 
	 * @return the container as AbstractCrosstabItemHandle
	 */
	public AbstractCrosstabItemHandle getContainer() {
		if (handle != null) {
			DesignElementHandle deh = handle.getContainer();

			if (!(deh instanceof ExtendedItemHandle)) {
				return null;
			}

			String exName = deh.getStringProperty(ExtendedItemHandle.EXTENSION_NAME_PROP);

			if (CROSSTAB_EXTENSION_NAME.equals(exName) || CROSSTAB_VIEW_EXTENSION_NAME.equals(exName)
					|| DIMENSION_VIEW_EXTENSION_NAME.equals(exName) || LEVEL_VIEW_EXTENSION_NAME.equals(exName)
					|| MEASURE_VIEW_EXTENSION_NAME.equals(exName)
					|| COMPUTED_MEASURE_VIEW_EXTENSION_NAME.equals(exName)) {
				try {
					return (AbstractCrosstabItemHandle) ((ExtendedItemHandle) deh).getReportItem();
				} catch (ExtendedElementException e) {
					return null;
				}
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.extension.ReportItem#copy()
	 */
	public IReportItem copy() {
		// TODO return a copy
		return null;
	}

	/**
	 * Gets the logger instance for this class.
	 * 
	 * @return
	 */
	public Logger getLogger() {
		return logger;
	}

	/**
	 * Gets the module handle for this report item.
	 * 
	 * @return
	 */
	public ModuleHandle getModuleHandle() {
		return this.moduleHandle;
	}

	/**
	 * Checks the given version
	 * 
	 * @param version the version to be checked
	 * @return returns negative value (<0) if the given version is older than
	 *         current version, 0 if equals current version, positive value (>0) if
	 *         newer than current version.
	 */
	static protected int checkVersion(String version) {
		if (version == null || version.trim().length() == 0) {
			return -1;
		}

		if (!CROSSTAB_CURRENT_VERSION.equals(version)) {
			String[] ver = version.trim().split("\\."); //$NON-NLS-1$
			int major = Integer.parseInt(ver[0]);
			int minor = Integer.parseInt(ver[1]);
			int update = Integer.parseInt(ver[2]);

			if (major != CURRENT_CROSSTAB_MAJOR_VERSION) {
				return major - CURRENT_CROSSTAB_MAJOR_VERSION;
			} else {
				if (minor != CURRENT_CROSSTAB_MINOR_VERSION) {
					return minor - CURRENT_CROSSTAB_MINOR_VERSION;
				} else {
					return update - CURRENT_CROSSTAB_UPDATE_VERSION;
				}
			}
		}

		return 0;
	}
}
