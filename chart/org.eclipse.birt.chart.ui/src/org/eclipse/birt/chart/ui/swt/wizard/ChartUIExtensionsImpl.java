/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.ui.swt.interfaces.IChangeListener;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IRegisteredSubtaskEntry;
import org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider;
import org.eclipse.birt.core.framework.FrameworkException;
import org.eclipse.birt.core.framework.IConfigurationElement;
import org.eclipse.birt.core.framework.IExtension;
import org.eclipse.birt.core.framework.IExtensionPoint;
import org.eclipse.birt.core.framework.IExtensionRegistry;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ISubtaskSheet;
import org.eclipse.birt.core.ui.utils.UIHelper;

import com.ibm.icu.util.StringTokenizer;

/**
 * @author Actuate Corporation
 * 
 */
public class ChartUIExtensionsImpl {

	private Map<String, Collection<IRegisteredSubtaskEntry>> mSheets = null;

	private Map<String, Collection<IChartType>> mChartTypes = null;

	private Collection<IChangeListener> cListeners = null;

	private Map<String, Collection<DefaultRegisteredEntry<ISeriesUIProvider>>> mSeriesUIs = null;

	private static final String[] saSheets = new String[] {
			"20/Chart/Chart Area/org.eclipse.birt.chart.ui.swt.wizard.format.chart.ChartSheetImpl", //$NON-NLS-1$
			"21/Chart.Axis/ /org.eclipse.birt.chart.ui.swt.wizard.format.axis.AxisSheetImpl", //$NON-NLS-1$
			"22/Chart.Axis.X Axis/ /org.eclipse.birt.chart.ui.swt.wizard.format.axis.AxisXSheetImpl", //$NON-NLS-1$
			"23/Chart.Axis.Y Axis/ /org.eclipse.birt.chart.ui.swt.wizard.format.axis.AxisYSheetImpl", //$NON-NLS-1$
			"24/Chart.Axis.Z Axis/ /org.eclipse.birt.chart.ui.swt.wizard.format.axis.AxisZSheetImpl", //$NON-NLS-1$
			"25/Chart.Title/ /org.eclipse.birt.chart.ui.swt.wizard.format.chart.ChartTitleSheetImpl", //$NON-NLS-1$
			"26/Chart.Plot/ /org.eclipse.birt.chart.ui.swt.wizard.format.chart.ChartPlotSheetImpl", //$NON-NLS-1$
			"27/Chart.Legend/ /org.eclipse.birt.chart.ui.swt.wizard.format.chart.ChartLegendSheetImpl", //$NON-NLS-1$
			"30/Series/ /org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesSheetImpl", //$NON-NLS-1$
			"31/Series.Y Series/Value (Y) Series/org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesYSheetImpl", //$NON-NLS-1$
			"32/Series.Category Series/ /org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesXSheetImpl", //$NON-NLS-1$
			"33/Series.Value Series/ /org.eclipse.birt.chart.ui.swt.wizard.format.series.SeriesYSheetImpl", //$NON-NLS-1$
			"34/Series.Value Series.Needle/ /org.eclipse.birt.chart.ui.swt.wizard.format.series.NeedleSheetImpl", //$NON-NLS-1$
	};

	private static String[] saTypes = new String[] { "org.eclipse.birt.chart.ui.swt.type.BarChart", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.type.LineChart", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.type.AreaChart", "org.eclipse.birt.chart.ui.swt.type.PieChart", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.birt.chart.ui.swt.type.MeterChart", "org.eclipse.birt.chart.ui.swt.type.ScatterChart", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.birt.chart.ui.swt.type.StockChart", "org.eclipse.birt.chart.ui.swt.type.GanttChart", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.birt.chart.ui.swt.type.BubbleChart", "org.eclipse.birt.chart.ui.swt.type.DifferenceChart", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.birt.chart.ui.swt.type.TubeChart", "org.eclipse.birt.chart.ui.swt.type.ConeChart", //$NON-NLS-1$ //$NON-NLS-2$
			"org.eclipse.birt.chart.ui.swt.type.PyramidChart"//$NON-NLS-1$
	};

	private static final String[] saListeners = new String[] { "org.eclipse.birt.chart.ui.event.ChangeListenerImpl" //$NON-NLS-1$
	};

	private static String[] saSeriesUI = new String[] { "org.eclipse.birt.chart.ui.swt.series.SeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.AreaSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.BarSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.LineSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.MeterSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.PieSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.ScatterSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.StockSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.GanttSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.BubbleSeriesUIProvider", //$NON-NLS-1$
			"org.eclipse.birt.chart.ui.swt.series.DifferenceSeriesUIProvider" //$NON-NLS-1$
	};

	private static ChartUIExtensionsImpl uiExtensions = null;

	private static final ILogger logger = Logger.getLogger("org.eclipse.birt.chart.ui/swt.wizard"); //$NON-NLS-1$

	private static final String NS_NATIVE_IMPL = "org.eclipse.birt.chart.ui.extension";//$NON-NLS-1$

	/**
	 * 
	 */
	private ChartUIExtensionsImpl() {
		super();
	}

	public static synchronized ChartUIExtensionsImpl instance() {
		if (uiExtensions == null) {
			uiExtensions = new ChartUIExtensionsImpl();
		}
		return uiExtensions;
	}

	private void initUISheetExtensions(String defaultExtensionId) {
		mSheets = new LinkedHashMap<String, Collection<IRegisteredSubtaskEntry>>();
		if (UIHelper.isEclipseMode()) {
			IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint("org.eclipse.birt.chart.ui", //$NON-NLS-1$
					"uisheets"); //$NON-NLS-1$
			IExtension[] extensions = extensionPoint.getExtensions();

			for (int iC = 0; iC < extensions.length; iC++) {
				IExtension extension = extensions[iC];
				IConfigurationElement[] configElements = extension.getConfigurationElements();
				String id = extension.getSimpleIdentifier();
				if (id == null) {
					id = defaultExtensionId;
				}
				Set<IRegisteredSubtaskEntry> cSheets = new LinkedHashSet<IRegisteredSubtaskEntry>();
				for (int i = 0; i < configElements.length; i++) {
					IConfigurationElement currentTag = configElements[i];
					if (currentTag.getName().equals("propertySheet")) //$NON-NLS-1$
					{
						try {
							cSheets.add(new DefaultRegisteredSubtaskEntryImpl(currentTag.getAttribute("nodeIndex"), //$NON-NLS-1$
									currentTag.getAttribute("nodePath"), currentTag.getAttribute("displayName"), //$NON-NLS-1$ //$NON-NLS-2$
									(ISubtaskSheet) currentTag.createExecutableExtension("classDefinition"))); //$NON-NLS-1$
						} catch (FrameworkException e) {
							logger.log(e);
						}
					}
				}
				if (!cSheets.isEmpty()) {
					// Combine the entries of the same id extension
					if (mSheets.containsKey(id)) {
						Collection<IRegisteredSubtaskEntry> oldSheets = mSheets.get(id);
						Map<Integer, IRegisteredSubtaskEntry> oldSheetsMap = new HashMap<Integer, IRegisteredSubtaskEntry>();
						for (IRegisteredSubtaskEntry entry : oldSheets) {
							oldSheetsMap.put(entry.getNodeIndex(), entry);
						}
						for (IRegisteredSubtaskEntry entry : cSheets) {
							// If current sheet is new or has higher priority
							IRegisteredSubtaskEntry oldEntry = oldSheetsMap.get(entry.getNodeIndex());
							if (oldEntry == null || ((DefaultRegisteredSubtaskEntryImpl) entry)
									.getPriority() > ((DefaultRegisteredSubtaskEntryImpl) oldEntry).getPriority()) {
								// Add or replace the entry according to the
								// node index
								oldSheets.add(entry);
							}
						}
					} else {
						mSheets.put(id, cSheets);
					}
				}
			}
		} else {
			List<IRegisteredSubtaskEntry> cSheets = new ArrayList<IRegisteredSubtaskEntry>();
			for (int iC = 0; iC < saSheets.length; iC++) {
				try {
					StringTokenizer tokens = new StringTokenizer(saSheets[iC], "/"); //$NON-NLS-1$
					String sNodeIndex = tokens.nextToken();
					String sNodePath = tokens.nextToken();
					String sDisplayName = tokens.nextToken();
					String sSheetClass = tokens.nextToken();
					DefaultRegisteredSubtaskEntryImpl entry = new DefaultRegisteredSubtaskEntryImpl(sNodeIndex,
							sNodePath, sDisplayName, (ISubtaskSheet) Class.forName(sSheetClass).newInstance());
					cSheets.add(entry);
				} catch (InstantiationException e) {
					logger.log(e);
				} catch (IllegalAccessException e) {
					logger.log(e);
				} catch (ClassNotFoundException e) {
					logger.log(e);
				}
			}
			// Default id is TaskFormatChart, which is registered in ui
			// extension
			mSheets.put("TaskFormatChart", cSheets); //$NON-NLS-1$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IUIExtensions#getUISheetExtensions
	 * ()
	 */
	public Collection<IRegisteredSubtaskEntry> getUISheetExtensions(String extensionId) {
		if (mSheets == null) {
			initUISheetExtensions(extensionId);
		}
		Collection<IRegisteredSubtaskEntry> cSheets = mSheets.get(extensionId);
		if (cSheets != null) {
			return cSheets;
		}
		return Collections.emptyList();
	}

	private void initUIChartTypeExtensions(String defaultExtensionId) {
		mChartTypes = new HashMap<String, Collection<IChartType>>();
		if (UIHelper.isEclipseMode()) {
			IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint("org.eclipse.birt.chart.ui", "types"); //$NON-NLS-1$ //$NON-NLS-2$
			IExtension[] extensions = extensionPoint.getExtensions();

			for (int iC = 0; iC < extensions.length; iC++) {
				IExtension extension = extensions[iC];
				IConfigurationElement[] configElements = extension.getConfigurationElements();
				String id = extension.getSimpleIdentifier();
				if (id == null) {
					id = defaultExtensionId;
				}
				Vector<IChartType> cChartTypes = new Vector<IChartType>();
				for (int i = 0; i < configElements.length; i++) {
					IConfigurationElement currentTag = configElements[i];
					if (currentTag.getName().equals("chartType")) //$NON-NLS-1$
					{
						try {
							cChartTypes.add((IChartType) currentTag.createExecutableExtension("classDefinition")); //$NON-NLS-1$
						} catch (FrameworkException e) {
							logger.log(e);
						}
					}
				}
				if (!cChartTypes.isEmpty()) {
					// Combine the entries of the same id extension
					if (mChartTypes.containsKey(id)) {
						if (extension.getNamespace().equals(NS_NATIVE_IMPL)) {
							// Always let native charts be first
							cChartTypes.addAll(mChartTypes.get(id));
							mChartTypes.put(id, cChartTypes);
						} else {
							mChartTypes.get(id).addAll(cChartTypes);
						}
					} else {
						mChartTypes.put(id, cChartTypes);
					}
				}
			}
		} else {
			Vector<IChartType> cChartTypes = new Vector<IChartType>();
			for (int iC = 0; iC < saTypes.length; iC++) {
				try {
					cChartTypes.add((IChartType) Class.forName(saTypes[iC]).newInstance());
				} catch (InstantiationException e) {
					logger.log(e);
				} catch (IllegalAccessException e) {
					logger.log(e);
				} catch (ClassNotFoundException e) {
					logger.log(e);
				}
			}
			mChartTypes.put(defaultExtensionId, cChartTypes);
		}
	}

	public Collection<IChartType> getUIChartTypeExtensions(String extensionId) {
		if (mChartTypes == null) {
			initUIChartTypeExtensions(extensionId);
		}
		Collection<IChartType> cTypes = mChartTypes.get(extensionId);
		if (cTypes != null) {
			return cTypes;
		}
		return Collections.emptyList();
	}

	public Collection<IChangeListener> getUIListeners() {
		if (cListeners == null) {
			cListeners = new Vector<IChangeListener>();
			if (UIHelper.isEclipseMode()) {
				IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
				IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint("org.eclipse.birt.chart.ui", //$NON-NLS-1$
						"changelisteners"); //$NON-NLS-1$
				IExtension[] extensions = extensionPoint.getExtensions();
				for (int iC = 0; iC < extensions.length; iC++) {
					IExtension extension = extensions[iC];
					IConfigurationElement[] configElements = extension.getConfigurationElements();
					for (int i = 0; i < configElements.length; i++) {
						IConfigurationElement currentTag = configElements[i];
						if (currentTag.getName().equals("changeListener")) //$NON-NLS-1$
						{
							try {
								cListeners.add((IChangeListener) currentTag
										.createExecutableExtension("listenerClassDefinition")); //$NON-NLS-1$
							} catch (FrameworkException e) {
								logger.log(e);
							}
						}
					}
				}
			} else {
				for (int iC = 0; iC < saListeners.length; iC++) {
					try {
						cListeners.add((IChangeListener) Class.forName(saListeners[iC]).newInstance());
					} catch (InstantiationException e) {
						logger.log(e);
					} catch (IllegalAccessException e) {
						logger.log(e);
					} catch (ClassNotFoundException e) {
						logger.log(e);
					}
				}
			}
		}
		return cListeners;
	}

	public Collection<ISeriesUIProvider> getSeriesUIComponents(String extensionId) {
		if (mSeriesUIs == null) {
			initSeriesUIComponents(extensionId);
		}
		Collection<DefaultRegisteredEntry<ISeriesUIProvider>> cSeriesUI = mSeriesUIs.get(extensionId);
		if (cSeriesUI != null) {
			return DefaultRegisteredEntry.convert(cSeriesUI);
		}
		return Collections.emptyList();
	}

	private void initSeriesUIComponents(String defaultExtensionId) {
		mSeriesUIs = new HashMap<String, Collection<DefaultRegisteredEntry<ISeriesUIProvider>>>();
		if (UIHelper.isEclipseMode()) {
			IExtensionRegistry pluginRegistry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = pluginRegistry.getExtensionPoint("org.eclipse.birt.chart.ui", //$NON-NLS-1$
					"seriescomposites"); //$NON-NLS-1$
			IExtension[] extensions = extensionPoint.getExtensions();
			for (int iC = 0; iC < extensions.length; iC++) {
				IExtension extension = extensions[iC];
				IConfigurationElement[] configElements = extension.getConfigurationElements();
				String id = extension.getSimpleIdentifier();
				if (id == null) {
					id = defaultExtensionId;
				}
				Vector<DefaultRegisteredEntry<ISeriesUIProvider>> cSeriesUI = new Vector<DefaultRegisteredEntry<ISeriesUIProvider>>();
				for (int i = 0; i < configElements.length; i++) {
					IConfigurationElement currentTag = configElements[i];
					if (currentTag.getName().equals("seriescomposite")) //$NON-NLS-1$
					{
						try {
							cSeriesUI.add(new DefaultRegisteredEntry<ISeriesUIProvider>(
									(ISeriesUIProvider) currentTag.createExecutableExtension("seriesUIProvider"), //$NON-NLS-1$
									currentTag.getAttribute("seriesType"), //$NON-NLS-1$
									currentTag.getAttribute("priority"))); //$NON-NLS-1$
						} catch (FrameworkException e) {
							logger.log(e);
						}
					}
				}
				if (!cSeriesUI.isEmpty()) {
					// Combine the entries of the same id extension
					if (mSeriesUIs.containsKey(id)) {
						Collection<DefaultRegisteredEntry<ISeriesUIProvider>> oldSheets = mSeriesUIs.get(id);
						Map<String, DefaultRegisteredEntry<ISeriesUIProvider>> oldSheetsMap = new HashMap<String, DefaultRegisteredEntry<ISeriesUIProvider>>();
						for (DefaultRegisteredEntry<ISeriesUIProvider> entry : oldSheets) {
							oldSheetsMap.put(entry.getName(), entry);
						}
						for (DefaultRegisteredEntry<ISeriesUIProvider> entry : cSeriesUI) {
							// If current sheet is new or has higher priority
							DefaultRegisteredEntry<ISeriesUIProvider> oldEntry = oldSheetsMap.get(entry.getName());
							if (oldEntry == null || entry.getPriority() > oldEntry.getPriority()) {
								// Add or replace the entry according to the
								// priority
								oldSheets.add(entry);
							}
						}
					} else {
						mSeriesUIs.put(id, cSeriesUI);
					}
				}
			}
		} else {
			Vector<DefaultRegisteredEntry<ISeriesUIProvider>> cSeriesUI = new Vector<DefaultRegisteredEntry<ISeriesUIProvider>>();
			for (int iC = 0; iC < saSeriesUI.length; iC++) {
				try {
					cSeriesUI.add(new DefaultRegisteredEntry<ISeriesUIProvider>(
							(ISeriesUIProvider) Class.forName(saSeriesUI[iC]).newInstance(), null, null));
				} catch (InstantiationException e) {
					logger.log(e);
				} catch (IllegalAccessException e) {
					logger.log(e);
				} catch (ClassNotFoundException e) {
					logger.log(e);
				}
			}
			mSeriesUIs.put(defaultExtensionId, cSeriesUI);
		}
	}
}