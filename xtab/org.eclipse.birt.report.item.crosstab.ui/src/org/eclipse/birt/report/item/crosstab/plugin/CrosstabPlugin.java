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

package org.eclipse.birt.report.item.crosstab.plugin;

import java.util.Map;

import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.core.util.AbstractCrosstabUpateListener;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.core.util.ICrosstabUpdateListener;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * CrosstabPlugin
 */
public class CrosstabPlugin extends AbstractUIPlugin {

	/** Plugin ID */
	public static final String ID = "org.eclipse.birt.report.item.crosstab.ui"; //$NON-NLS-1$

	/** Preference ID */
	public static final String PREFERENCE_FILTER_LIMIT = "Filter.Limit"; //$NON-NLS-1$

	public static final String PREFERENCE_AUTO_DEL_BINDINGS = "Auto.Del.Bindings"; //$NON-NLS-1$

	public static final String CUBE_BUILDER_WARNING_PREFERENCE = "org.eclipse.birt.report.designer.ui.cubebuilder.warning"; //$NON-NLS-1$

	public static final int FILTER_LIMIT_DEFAULT = 100;

	public static final String AUTO_DEL_BINDING_DEFAULT = MessageDialogWithToggle.PROMPT;

	// The shared instance.
	private static CrosstabPlugin plugin;

	/**
	 * The constructor.
	 */
	public CrosstabPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static CrosstabPlugin getDefault() {
		return plugin;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

		PreferenceFactory.getInstance().getPreferences(CrosstabPlugin.getDefault()).setDefault(PREFERENCE_FILTER_LIMIT,
				FILTER_LIMIT_DEFAULT);
		PreferenceFactory.getInstance().getPreferences(CrosstabPlugin.getDefault())
				.setDefault(PREFERENCE_AUTO_DEL_BINDINGS, AUTO_DEL_BINDING_DEFAULT);
		PreferenceFactory.getInstance().getPreferences(CrosstabPlugin.getDefault())
				.setDefault(CUBE_BUILDER_WARNING_PREFERENCE, MessageDialogWithToggle.PROMPT);

		// There add a listener, when create a measure head, add a lable to the
		// head cell
		CrosstabUtil.setCrosstabUpdateListener(new AbstractCrosstabUpateListener() {

			public void onCreated(int type, Object model, Map<String, Object> extras) {
				if (context != null) {
					try {
						// do not call this if want to perform custom handling
						context.performDefaultCreation(type, model, extras);
					} catch (SemanticException e) {
						ExceptionUtil.handle(e);
					}
				}

				if (type == ICrosstabUpdateListener.MEASURE_HEADER && model instanceof CrosstabCellHandle) {
					CrosstabCellHandle cellHandle = ((CrosstabCellHandle) model);
					if (cellHandle.getContents().size() > 0) {
						return;
					}
					LabelHandle labelHandle = DesignElementFactory.getInstance(cellHandle.getModuleHandle())
							.newLabel(null);
					try {
						MeasureViewHandle measureViewHandle = ((MeasureViewHandle) cellHandle.getContainer());
						MeasureHandle measure = measureViewHandle.getCubeMeasure();
						String labelName = measureViewHandle.getCubeMeasureName();
						// ComputedMeasureViews doesn't hold reference to measure , if they don't have
						// their own aggregation.
						if (measure != null && measure.getDisplayName() != null) {
							labelName = measure.getDisplayName();
						}
						labelHandle.setText(labelName);

						cellHandle.addContent(labelHandle);
					} catch (SemanticException e) {
						ExceptionUtil.handle(e);
					}
				}
			}

			public void onValidate(int type, Object model, Map<String, Object> extras) {
				if (context != null) {
					try {
						// do not call this if want to perform custom handling
						context.performDefaultValidation(type, model, extras);
					} catch (SemanticException e) {
						ExceptionUtil.handle(e);
					}
				}

				if (type == ICrosstabUpdateListener.MEASURE_DETAIL && model instanceof AggregationCellHandle) {
					AggregationCellHandle cellHandle = (AggregationCellHandle) model;

					if (cellHandle.getContents().size() == 1
							&& cellHandle.getContents().get(0) instanceof DataItemHandle) {
						MeasureViewHandle measureView = (MeasureViewHandle) cellHandle.getContainer();

						DataItemHandle dataItem = (DataItemHandle) cellHandle.getContents().get(0);

						CrosstabAdaptUtil.formatDataItem(measureView.getCubeMeasure(), dataItem);
						// update action to dataHandle
						if (measureView.getCubeMeasure() == null) {
							return;
						}
						ActionHandle actionHandle = measureView.getCubeMeasure().getActionHandle();

						if (actionHandle != null) {
							try {
								dataItem.setAction((Action) actionHandle.getStructure().copy());
							} catch (SemanticException e) {
								ExceptionUtil.handle(e);
							}
						}
					}
				}
			}

		});
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		CrosstabModelUtil.setCrosstabModelListener(null);
	}
}
