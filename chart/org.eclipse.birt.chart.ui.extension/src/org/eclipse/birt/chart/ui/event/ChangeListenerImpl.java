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

package org.eclipse.birt.chart.ui.event;

import java.util.Iterator;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.LabelBlock;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.ui.swt.interfaces.IChangeListener;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIManager;

/**
 * @author Actuate Corporation
 *
 */
public class ChangeListenerImpl implements IChangeListener {
	private transient int iBaseSeriesCount = 0;

	private transient int iOrthogonalSeriesCount = 0;

	private transient int iBaseAxisCount = 0;

	private transient int iOrthogonalAxisCount = 0;

	private transient int iLabelBlockCount = 0;

	private static final String BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES = "BaseSeriesSheetsCWA"; //$NON-NLS-1$

	private static final String ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES = "OrthogonalSeriesSheetsCWA"; //$NON-NLS-1$

	private static final String BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES = "BaseSeriesSheetsCWOA"; //$NON-NLS-1$

	private static final String ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES = "OrthogonalSeriesSheetsCWOA"; //$NON-NLS-1$

	private static final String BASE_AXIS_SHEET_COLLECTION = "BaseAxisSheets"; //$NON-NLS-1$

	private static final String ORTHOGONAL_AXIS_SHEET_COLLECTION = "OrthogonalAxisSheets"; //$NON-NLS-1$

	private static final String LABEL_BLOCK_SHEET_COLLECTION = "LabelBlockSeriesSheets"; //$NON-NLS-1$

	private static final String[] BASE_SERIES_SHEETS_FOR_CHARTS_WITH_AXES = { "Data.X Series", //$NON-NLS-1$
			"Attributes.X Series" //$NON-NLS-1$
	};

	private static final String[] ORTHOGONAL_SERIES_SHEETS_FOR_CHARTS_WITH_AXES = { "Data.Y Series", //$NON-NLS-1$
			"Attributes.Y Series", "Attributes.Y Series.Labels" //$NON-NLS-1$ //$NON-NLS-2$
	};

	private static final String[] BASE_SERIES_SHEETS_FOR_CHARTS_WITHOUT_AXES = { "Data.Base Series", //$NON-NLS-1$
			"Attributes.Base Series" //$NON-NLS-1$
	};

	private static final String[] ORTHOGONAL_SERIES_SHEETS_FOR_CHARTS_WITHOUT_AXES = { "Data.Orthogonal Series", //$NON-NLS-1$
			"Attributes.Orthogonal Series", "Attributes.Orthogonal Series.Labels" //$NON-NLS-1$ //$NON-NLS-2$
	};

	private static final String[] BASE_AXIS_SHEETS = { "Data.X Axis", "Attributes.X Axis", //$NON-NLS-1$ //$NON-NLS-2$
			"Attributes.X Axis.Labels", "Attributes.X Axis.Markers" //$NON-NLS-1$ //$NON-NLS-2$
	};

	private static final String[] ORTHOGONAL_AXIS_SHEETS = { "Data.Y Axis", "Attributes.Y Axis", //$NON-NLS-1$ //$NON-NLS-2$
			"Attributes.Y Axis.Labels", "Attributes.Y Axis.Markers" //$NON-NLS-1$ //$NON-NLS-2$
	};

	private static final String[] LABEL_BLOCK_SHEETS = { "Layout.Label Block" //$NON-NLS-1$
	};

	@Override
	public void initialize(Chart cModel, IUIManager uiManager) {
		// Register sheet collections
		uiManager.registerSheetCollection(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES,
				BASE_SERIES_SHEETS_FOR_CHARTS_WITH_AXES);
		uiManager.registerSheetCollection(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES,
				ORTHOGONAL_SERIES_SHEETS_FOR_CHARTS_WITH_AXES);
		uiManager.registerSheetCollection(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES,
				BASE_SERIES_SHEETS_FOR_CHARTS_WITHOUT_AXES);
		uiManager.registerSheetCollection(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES,
				ORTHOGONAL_SERIES_SHEETS_FOR_CHARTS_WITHOUT_AXES);
		uiManager.registerSheetCollection(BASE_AXIS_SHEET_COLLECTION, BASE_AXIS_SHEETS);
		uiManager.registerSheetCollection(ORTHOGONAL_AXIS_SHEET_COLLECTION, ORTHOGONAL_AXIS_SHEETS);
		uiManager.registerSheetCollection(LABEL_BLOCK_SHEET_COLLECTION, LABEL_BLOCK_SHEETS);

		iLabelBlockCount = 0;
		if (cModel instanceof ChartWithAxes) {
			iBaseAxisCount = ((ChartWithAxes) cModel).getAxes().size();
			iOrthogonalAxisCount = 0;
			iBaseSeriesCount = 0;
			iOrthogonalSeriesCount = 0;
			for (int i = 0; i < iBaseAxisCount; i++) {
				iBaseSeriesCount += ((Axis) ((ChartWithAxes) cModel).getAxes().get(i)).getSeriesDefinitions().size();
				iOrthogonalAxisCount += ((Axis) ((ChartWithAxes) cModel).getAxes().get(i)).getAssociatedAxes().size();
				for (int iS = 0; iS < iOrthogonalAxisCount; iS++) {
					iOrthogonalSeriesCount += ((Axis) ((Axis) ((ChartWithAxes) cModel).getAxes().get(i))
							.getAssociatedAxes().get(iS)).getSeriesDefinitions().size();
				}
			}
			// Start from 1 because there will always be at least 1 entry for each
			// registered sheet when this method is
			// called
			for (int iBA = 1; iBA < iBaseAxisCount; iBA++) {
				uiManager.addCollectionInstance(BASE_AXIS_SHEET_COLLECTION);
			}
			for (int iOA = 1; iOA < iOrthogonalAxisCount; iOA++) {
				uiManager.addCollectionInstance(ORTHOGONAL_AXIS_SHEET_COLLECTION);
			}
			// Remove series sheets (for charts with axes) since they are not needed for
			// Charts Without Axes
			uiManager.removeCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
			uiManager.removeCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
			// Start from 1 because there will always be at least 1 entry for each
			// registered sheet when this method is
			// called
			for (int iBS = 1; iBS < iBaseSeriesCount; iBS++) {
				uiManager.addCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
			}
			for (int iOS = 1; iOS < iOrthogonalSeriesCount; iOS++) {
				uiManager.addCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
			}
		} else {
			iBaseAxisCount = 0;
			iOrthogonalAxisCount = 0;
			iBaseSeriesCount = ((ChartWithoutAxes) cModel).getSeriesDefinitions().size();
			iOrthogonalSeriesCount = 0;
			for (int iS = 0; iS < iBaseSeriesCount; iS++) {
				iOrthogonalSeriesCount += ((SeriesDefinition) ((ChartWithoutAxes) cModel).getSeriesDefinitions()
						.get(iS)).getSeriesDefinitions().size();
			}

			// Remove axis sheets since they are not needed for Charts Without Axes
			uiManager.removeCollectionInstance(ORTHOGONAL_AXIS_SHEET_COLLECTION);
			uiManager.removeCollectionInstance(BASE_AXIS_SHEET_COLLECTION);
			// Remove series sheets (for charts with axes) since they are not needed for
			// Charts Without Axes
			uiManager.removeCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
			uiManager.removeCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
			// Start from 1 because there will always be at least 1 entry for each
			// registered sheet when this method is
			// called
			for (int iBS = 1; iBS < iBaseSeriesCount; iBS++) {
				uiManager.addCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
			}
			for (int iOS = 1; iOS < iOrthogonalSeriesCount; iOS++) {
				uiManager.addCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
			}
		}
		Iterator iter = cModel.getBlock().getChildren().iterator();
		while (iter.hasNext()) {
			Block block = (Block) iter.next();
			if (block instanceof LabelBlock && !(block instanceof TitleBlock)) {
				iLabelBlockCount++;
			}
		}

		if (iLabelBlockCount == 0) {
			uiManager.removeCollectionInstance(LABEL_BLOCK_SHEET_COLLECTION);
		} else {
			for (int iLB = 1; iLB < iLabelBlockCount; iLB++) {
				uiManager.addCollectionInstance(LABEL_BLOCK_SHEET_COLLECTION);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.ui.swt.IChangeListener#chartModified(org.eclipse.emf.
	 * common.notify.Notification, org.eclipse.birt.chart.ui.swt.IUIManager)
	 */
	@Override
	public void chartModified(Chart cModel, IUIManager uiManager) {
		int iNewBaseAxisCount = 0;
		int iNewOrthogonalAxisCount = 0;
		int iNewBaseSeriesCount = 0;
		int iNewOrthogonalSeriesCount = 0;
		int iNewLabelBlockCount = 0;

		if (cModel instanceof ChartWithAxes) {
			iNewBaseAxisCount = ((ChartWithAxes) cModel).getAxes().size();
			iNewOrthogonalAxisCount = 0;
			iNewBaseSeriesCount = 0;
			iNewOrthogonalSeriesCount = 0;
			for (int i = 0; i < iNewBaseAxisCount; i++) {
				iNewBaseSeriesCount += ((Axis) ((ChartWithAxes) cModel).getAxes().get(i)).getSeriesDefinitions().size();
				iNewOrthogonalAxisCount += ((Axis) ((ChartWithAxes) cModel).getAxes().get(i)).getAssociatedAxes()
						.size();
				for (int iS = 0; iS < iNewOrthogonalAxisCount; iS++) {
					iNewOrthogonalSeriesCount += ((Axis) ((Axis) ((ChartWithAxes) cModel).getAxes().get(i))
							.getAssociatedAxes().get(iS)).getSeriesDefinitions().size();
				}
			}
			// Start from 1 because there will always be at least 1 entry for each
			// registered sheet when this method is
			// called
			if (iNewBaseAxisCount >= iBaseAxisCount) {
				for (int iBA = iBaseAxisCount; iBA < iNewBaseAxisCount; iBA++) {
					uiManager.addCollectionInstance(BASE_AXIS_SHEET_COLLECTION);
				}
			} else {
				for (int iBA = iBaseAxisCount; iBA > iNewBaseAxisCount; iBA--) {
					uiManager.removeCollectionInstance(BASE_AXIS_SHEET_COLLECTION);
				}
			}

			if (iNewOrthogonalAxisCount >= iOrthogonalAxisCount) {
				for (int iOA = iOrthogonalAxisCount; iOA < iNewOrthogonalAxisCount; iOA++) {
					uiManager.addCollectionInstance(ORTHOGONAL_AXIS_SHEET_COLLECTION);
				}
			} else {
				for (int iOA = iOrthogonalAxisCount; iOA > iNewOrthogonalAxisCount; iOA--) {
					uiManager.removeCollectionInstance(ORTHOGONAL_AXIS_SHEET_COLLECTION);
				}
			}
		} else {
			iNewBaseAxisCount = 0;
			iNewOrthogonalAxisCount = 0;
			iNewBaseSeriesCount = ((ChartWithoutAxes) cModel).getSeriesDefinitions().size();
			iNewOrthogonalSeriesCount = 0;
			for (int iS = 0; iS < iNewBaseSeriesCount; iS++) {
				iNewOrthogonalSeriesCount += ((SeriesDefinition) ((ChartWithoutAxes) cModel).getSeriesDefinitions()
						.get(iS)).getSeriesDefinitions().size();
			}
		}
		Iterator iter = cModel.getBlock().getChildren().iterator();
		while (iter.hasNext()) {
			Block block = (Block) iter.next();
			if (block instanceof LabelBlock && !(block instanceof TitleBlock)) {
				iNewLabelBlockCount++;
			}
		}
		if (cModel instanceof ChartWithAxes) {
			// Start from 1 because there will always be at least 1 entry for each
			// registered sheet when this method is
			// called
			if (iNewBaseSeriesCount >= iBaseSeriesCount) {
				for (int iBS = iBaseSeriesCount; iBS < iNewBaseSeriesCount; iBS++) {
					uiManager.addCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
				}
			} else {
				for (int iBS = iBaseSeriesCount; iBS > iNewBaseSeriesCount; iBS--) {
					uiManager.removeCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
				}
			}

			if (iNewOrthogonalSeriesCount >= iOrthogonalSeriesCount) {
				for (int iOS = iOrthogonalSeriesCount; iOS < iNewOrthogonalSeriesCount; iOS++) {
					uiManager.addCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
				}
			} else {
				for (int iOS = iOrthogonalSeriesCount; iOS > iNewOrthogonalSeriesCount; iOS--) {
					uiManager.removeCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITH_AXES);
				}
			}
		} else {
			// Start from 1 because there will always be at least 1 entry for each
			// registered sheet when this method is
			// called
			if (iNewBaseSeriesCount >= iBaseSeriesCount) {
				for (int iBS = iBaseSeriesCount; iBS < iNewBaseSeriesCount; iBS++) {
					uiManager.addCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
				}
			} else {
				for (int iBS = iBaseSeriesCount; iBS > iNewBaseSeriesCount; iBS--) {
					uiManager.removeCollectionInstance(BASE_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
				}
			}

			if (iNewOrthogonalSeriesCount >= iOrthogonalSeriesCount) {
				for (int iOS = iOrthogonalSeriesCount; iOS < iNewOrthogonalSeriesCount; iOS++) {
					uiManager.addCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
				}
			} else {
				for (int iOS = iOrthogonalSeriesCount; iOS > iNewOrthogonalSeriesCount; iOS--) {
					uiManager.removeCollectionInstance(ORTHOGONAL_SERIES_SHEET_COLLECTION_FOR_CHARTS_WITHOUT_AXES);
				}
			}
		}

		// Update the stored counts
		iBaseAxisCount = iNewBaseAxisCount;
		iOrthogonalAxisCount = iNewOrthogonalAxisCount;
		iBaseSeriesCount = iNewBaseSeriesCount;
		iOrthogonalSeriesCount = iNewOrthogonalSeriesCount;
	}
}
