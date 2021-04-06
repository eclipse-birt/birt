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

package org.eclipse.birt.report.designer.ui.views.attributes.providers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AdvancePropertyPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AlterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.AutoTextPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BookMarkExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.BordersPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CascadingParameterGroupI18nPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CascadingParameterGroupPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPaddingPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CellPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ColumnPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ColumnSectionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.CommentsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.DataPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.DataSetPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.DataSourcePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.DescriptionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormatDateTimeAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormatNumberAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.FormatStringAttributePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.GridPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.HeaderFooterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.HyperLinkPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ImagePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ItemMarginPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.LabelI18nPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.LabelPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.LibraryPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ListPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ListingSectionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.MarginsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.MasterPageGeneralPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.NamedExpressionsPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ParameterGroupI18nPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ParameterGroupPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ReferencePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ReportPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ResourcesPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.RowPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ScalarParameterI18nPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.ScalarParameterPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.SectionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TOCExpressionPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TablePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TemplateReportItemI18Page;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TemplateReportItemPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TextI18nPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.TextPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.UserPropertiesPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.VariablePage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.VisibilityPage;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.CategoryProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.TemplateElementHandle;
import org.eclipse.birt.report.model.api.VariableElementHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;

/**
 * The default implement of ICategoryProviderFactory
 */

public class CategoryProviderFactory implements ICategoryProviderFactory {

	private static ICategoryProviderFactory instance = new CategoryProviderFactory();

	protected CategoryProviderFactory() {
	}

	/**
	 * 
	 * @return The unique CategoryProviderFactory instance
	 */
	public static ICategoryProviderFactory getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.ui.views.attributes.providers.
	 * ICategoryProviderFactory#getCategoryProvider(java.lang.Object)
	 */
	public ICategoryProvider getCategoryProvider(Object model) {
		if (model instanceof DesignElementHandle) {
			return getCategoryProvider((DesignElementHandle) model);
		}
		if (model instanceof String) {
			return getCategoryProvider((String) model);
		}
		if (model instanceof List) {
			List list = (List) model;
			if (!list.isEmpty()) {
				return getCategoryProvider(list.get(0));
			}
		}
		return null;
	}

	public final static String CATEGORY_KEY_GENERAL = "General"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_PADDING = "Padding"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_FONT = "Font"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_BORDERS = "Borders"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_USERPROPERTIES = "UserProperties"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_NAMEDEXPRESSIONS = "NamedExpressions"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_VISIBILITY = "Visibility"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_FORMATNUMBER = "formatNumber"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_FORMATDATETIME = "formatDateTime"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_FORMATSTRING = "formatString"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_MARGIN = "Margin"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_MASTER_COLUMNS = "MasterColumns"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_HYPERLINK = "HyperLink"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_SECTION = "Section"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_TOC = "TOC"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_BOOKMARK = "Bookmark"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_REFERENCE = "Reference"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_ALTTEXT = "AltText"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_I18N = "I18n"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_DESCRIPTION = "Description"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_COMMENTS = "Comments"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_RESOURCES = "Resources"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_HEADER_FOOTER = "Header&Footer"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_EXPRESSION = "Expression"; //$NON-NLS-1$
	public final static String CATEGORY_KEY_ADVANCEPROPERTY = "AdvanceProperty"; //$NON-NLS-1$

	/**
	 * CategoryHolder
	 */
	protected static class CategoryHolder {

		String[] keys;
		String[] labels;
		Class<?>[] pageClasses;

		public CategoryHolder(String[] key, String[] label, Class<?>[] pageClass) {
			this.keys = key;
			this.labels = label;
			this.pageClasses = pageClass;
		}

		public String[] getKeys() {
			return keys;
		}

		public String[] getLabels() {
			return labels;
		}

		public Class[] getClasses() {
			return pageClasses;
		}

		/**
		 * Replaces an existing entry of given key with new label and new page class.
		 * 
		 * @param targetKey
		 * @param label
		 * @param pageClass
		 */
		public void replace(String targetKey, String label, Class<?> pageClass) {
			if (targetKey == null || (label == null && pageClass == null) || keys == null) {
				return;
			}

			int idx = -1;

			for (int i = 0; i < keys.length; i++) {
				if (targetKey.equals(keys[i])) {
					idx = i;
					break;
				}
			}

			if (idx == -1) {
				return;
			}

			if (label != null && labels != null && idx < labels.length) {
				labels[idx] = label;
			}

			if (pageClass != null && pageClasses != null && idx < pageClasses.length) {
				pageClasses[idx] = pageClass;
			}
		}

		/**
		 * Inserts a new entry before an existing entry by given key.
		 * 
		 * @param beforeKey
		 * @param key
		 * @param label
		 * @param pageClass
		 */
		public void insertBefore(String beforeKey, String key, String label, Class<?> pageClass) {
			// TODO optimize performance

			List<String> lkeys = null;
			List<String> llabels = null;
			List<Class<?>> lclasses = null;

			if (keys == null) {
				lkeys = new ArrayList<String>();
			} else {
				lkeys = new ArrayList<String>(Arrays.asList(keys));
			}

			if (labels == null) {
				llabels = new ArrayList<String>();
			} else {
				llabels = new ArrayList<String>(Arrays.asList(labels));
			}

			if (pageClasses == null) {
				lclasses = new ArrayList<Class<?>>();
			} else {
				lclasses = new ArrayList<Class<?>>(Arrays.asList(pageClasses));
			}

			if (beforeKey != null) {
				int i = 0;
				for (; i < lkeys.size(); i++) {
					if (beforeKey.equals(lkeys.get(i))) {
						lkeys.add(i, key);
						llabels.add(i, label);
						lclasses.add(i, pageClass);

						this.keys = lkeys.toArray(new String[lkeys.size()]);
						this.labels = llabels.toArray(new String[llabels.size()]);
						this.pageClasses = lclasses.toArray(new Class[lclasses.size()]);

						return;
					}
				}
			}

			// append to end.
			lkeys.add(key);
			llabels.add(label);
			lclasses.add(pageClass);

			this.keys = lkeys.toArray(new String[lkeys.size()]);
			this.labels = llabels.toArray(new String[llabels.size()]);
			this.pageClasses = lclasses.toArray(new Class[lclasses.size()]);
		}
	}

	protected CategoryHolder getCategories(String elementName) {
		if (ReportDesignConstants.CELL_ELEMENT.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_PADDING, CATEGORY_KEY_BORDERS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("CellPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("CellPageGenerator.List.CellPadding"), //$NON-NLS-1$
							Messages.getString("CellPageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { CellPage.class, CellPaddingPage.class, BordersPage.class, UserPropertiesPage.class,
							NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.COLUMN_ELEMENT.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_SECTION, CATEGORY_KEY_VISIBILITY,
							CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("ColumnPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("ColumnPageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { ColumnPage.class, ColumnSectionPage.class, VisibilityPage.class,
							AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.DATA_ITEM.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_PADDING, CATEGORY_KEY_BORDERS,
							CATEGORY_KEY_MARGIN, CATEGORY_KEY_FORMATNUMBER, CATEGORY_KEY_FORMATDATETIME,
							CATEGORY_KEY_FORMATSTRING, CATEGORY_KEY_HYPERLINK, CATEGORY_KEY_SECTION,
							CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("DataPageGenerator.List.General"), //$NON-NLS-1$
							// Messages.getString( "DataPageGenerator.List.Expression"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.Padding"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.Margin"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.formatNumber"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.formatDateTime"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.formatString"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.HyperLink"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.TOC"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { DataPage.class,
							// ExpressionPage.class,
							CellPaddingPage.class, BordersPage.class, ItemMarginPage.class,
							FormatNumberAttributePage.class, FormatDateTimeAttributePage.class,
							FormatStringAttributePage.class, HyperLinkPage.class, SectionPage.class,
							VisibilityPage.class, TOCExpressionPage.class, BookMarkExpressionPage.class,
							CommentsPage.class, UserPropertiesPage.class, NamedExpressionsPage.class,
							AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.GRID_ITEM.equals(elementName)) {
			return new CategoryHolder(new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_BORDERS, CATEGORY_KEY_MARGIN,
					CATEGORY_KEY_SECTION, CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK,
					CATEGORY_KEY_COMMENTS, CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS,
					CATEGORY_KEY_ADVANCEPROPERTY,

			}, new String[] { Messages.getString("GridPageGenerator.List.General"), //$NON-NLS-1$
					Messages.getString("GridPageGenerator.List.Borders"), //$NON-NLS-1$
					Messages.getString("GridPageGenerator.List.Margin"), //$NON-NLS-1$
					Messages.getString("GridPageGenerator.List.Section"), //$NON-NLS-1$
					Messages.getString("GridPageGenerator.List.Visibility"), //$NON-NLS-1$
					Messages.getString("GridPageGenerator.List.TOC"), //$NON-NLS-1$
					Messages.getString("GridPageGenerator.List.Bookmark"), //$NON-NLS-1$
					Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
					Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
					Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
					Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
			}, new Class[] { GridPage.class, BordersPage.class, ItemMarginPage.class, SectionPage.class,
					VisibilityPage.class, TOCExpressionPage.class, BookMarkExpressionPage.class, CommentsPage.class,
					UserPropertiesPage.class, NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.IMAGE_ITEM.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_REFERENCE, CATEGORY_KEY_HYPERLINK,
							CATEGORY_KEY_ALTTEXT, CATEGORY_KEY_BORDERS, CATEGORY_KEY_MARGIN, CATEGORY_KEY_SECTION,
							CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("ImagePageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ImagePageGenerator.List.Reference"), //$NON-NLS-1$
							Messages.getString("ImagePageGenerator.List.HyperLink"), //$NON-NLS-1$
							Messages.getString("ImagePageGenerator.List.AltText"), //$NON-NLS-1$
							Messages.getString("ImagePageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("GridPageGenerator.List.Margin"), //$NON-NLS-1$
							Messages.getString("ImagePageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("ImagePageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("ImagePageGenerator.List.TOC"), //$NON-NLS-1$
							Messages.getString("ImagePageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { ImagePage.class, ReferencePage.class, HyperLinkPage.class, AlterPage.class,
							BordersPage.class, ItemMarginPage.class, SectionPage.class, VisibilityPage.class,
							TOCExpressionPage.class, BookMarkExpressionPage.class, CommentsPage.class,
							UserPropertiesPage.class, NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.LABEL_ITEM.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_PADDING, CATEGORY_KEY_BORDERS,
							CATEGORY_KEY_MARGIN, CATEGORY_KEY_HYPERLINK, CATEGORY_KEY_SECTION, CATEGORY_KEY_VISIBILITY,
							CATEGORY_KEY_I18N, CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("LabelPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.Padding"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.Margin"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.HyperLink"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.I18n"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.TOC"), //$NON-NLS-1$
							Messages.getString("LabelPageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { LabelPage.class, CellPaddingPage.class, BordersPage.class, ItemMarginPage.class,
							HyperLinkPage.class, SectionPage.class, VisibilityPage.class, LabelI18nPage.class,
							TOCExpressionPage.class, BookMarkExpressionPage.class, CommentsPage.class,
							UserPropertiesPage.class, NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.LIBRARY_ELEMENT.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_DESCRIPTION, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_RESOURCES,
							CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("ReportPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Description"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Resources"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { LibraryPage.class, DescriptionPage.class, CommentsPage.class,
							UserPropertiesPage.class, NamedExpressionsPage.class, ResourcesPage.class,
							AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.LIST_ITEM.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_BORDERS, CATEGORY_KEY_SECTION,
							CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("ListPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ListPageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("ListPageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("ListPageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("ListPageGenerator.List.TOC"), //$NON-NLS-1$
							Messages.getString("ListPageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { ListPage.class, BordersPage.class, ListingSectionPage.class, VisibilityPage.class,
							TOCExpressionPage.class, BookMarkExpressionPage.class, CommentsPage.class,
							UserPropertiesPage.class, NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.REPORT_DESIGN_ELEMENT.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_DESCRIPTION, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_RESOURCES,
							CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("ReportPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Description"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Resources"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { ReportPage.class, DescriptionPage.class, CommentsPage.class, UserPropertiesPage.class,
							NamedExpressionsPage.class, ResourcesPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.ROW_ELEMENT.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_BORDERS, CATEGORY_KEY_SECTION,
							CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_USERPROPERTIES,
							CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("RowPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("RowPageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("RowPageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("RowPageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { RowPage.class, BordersPage.class, SectionPage.class, VisibilityPage.class,
							BookMarkExpressionPage.class, UserPropertiesPage.class, NamedExpressionsPage.class,
							AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.SCALAR_PARAMETER_ELEMENT.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_I18N, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("ScalarParameterPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ScalarParameterPageGenerator.List.I18n"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { ScalarParameterPage.class, ScalarParameterI18nPage.class, CommentsPage.class,
							UserPropertiesPage.class, NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.PARAMETER_GROUP_ELEMENT.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_I18N, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("TextPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ScalarParameterPageGenerator.List.I18n"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { ParameterGroupPage.class, ParameterGroupI18nPage.class, CommentsPage.class,
							UserPropertiesPage.class, NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}

		if (ReportDesignConstants.CASCADING_PARAMETER_GROUP_ELEMENT.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_I18N, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("TextPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ScalarParameterPageGenerator.List.I18n"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { CascadingParameterGroupPage.class, CascadingParameterGroupI18nPage.class,
							CommentsPage.class, UserPropertiesPage.class, NamedExpressionsPage.class,
							AdvancePropertyPage.class, });
		}

		if (ReportDesignConstants.TABLE_ITEM.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_BORDERS, CATEGORY_KEY_MARGIN,
							CATEGORY_KEY_SECTION, CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK,
							CATEGORY_KEY_COMMENTS, CATEGORY_KEY_USERPROPERTIES, CATEGORY_KEY_NAMEDEXPRESSIONS,
							CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("TablePageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("TablePageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("TablePageGenerator.List.Marign"), //$NON-NLS-1$
							Messages.getString("TablePageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("TablePageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("TablePageGenerator.List.TOC"), //$NON-NLS-1$
							Messages.getString("TablePageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { TablePage.class, BordersPage.class, ItemMarginPage.class, ListingSectionPage.class,
							VisibilityPage.class, TOCExpressionPage.class, BookMarkExpressionPage.class,
							CommentsPage.class, UserPropertiesPage.class, NamedExpressionsPage.class,
							AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.TEXT_DATA_ITEM.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_EXPRESSION, CATEGORY_KEY_PADDING,
							CATEGORY_KEY_BORDERS, CATEGORY_KEY_MARGIN, CATEGORY_KEY_SECTION, CATEGORY_KEY_VISIBILITY,
							CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS, CATEGORY_KEY_USERPROPERTIES,
							CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("TextPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("DataPageGenerator.List.Expression"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Padding"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Margin"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.TOC"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { TextPage.class, ExpressionPage.class, CellPaddingPage.class, BordersPage.class,
							ItemMarginPage.class, SectionPage.class, VisibilityPage.class, TOCExpressionPage.class,
							BookMarkExpressionPage.class, CommentsPage.class, UserPropertiesPage.class,
							NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.TEXT_ITEM.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_PADDING, CATEGORY_KEY_BORDERS,
							CATEGORY_KEY_MARGIN, CATEGORY_KEY_SECTION, CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_I18N,
							CATEGORY_KEY_TOC, CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS, CATEGORY_KEY_USERPROPERTIES,
							CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("TextPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Padding"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Margin"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.I18n"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.TOC"), //$NON-NLS-1$
							Messages.getString("TextPageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { TextPage.class, CellPaddingPage.class, BordersPage.class, ItemMarginPage.class,
							SectionPage.class, VisibilityPage.class, TextI18nPage.class, TOCExpressionPage.class,
							BookMarkExpressionPage.class, CommentsPage.class, UserPropertiesPage.class,
							NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		if (ReportDesignConstants.AUTOTEXT_ITEM.equals(elementName)) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_PADDING, CATEGORY_KEY_BORDERS,
							CATEGORY_KEY_MARGIN, CATEGORY_KEY_SECTION, CATEGORY_KEY_VISIBILITY, CATEGORY_KEY_TOC,
							CATEGORY_KEY_BOOKMARK, CATEGORY_KEY_COMMENTS, CATEGORY_KEY_USERPROPERTIES,
							CATEGORY_KEY_NAMEDEXPRESSIONS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("AutoTextPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("AutoTextPageGenerator.List.Padding"), //$NON-NLS-1$
							Messages.getString("AutoTextPageGenerator.List.Borders"), //$NON-NLS-1$
							Messages.getString("AutoTextPageGenerator.List.Margin"), //$NON-NLS-1$
							Messages.getString("AutoTextPageGenerator.List.Section"), //$NON-NLS-1$
							Messages.getString("AutoTextPageGenerator.List.Visibility"), //$NON-NLS-1$
							Messages.getString("AutoTextPageGenerator.List.TOC"), //$NON-NLS-1$
							Messages.getString("AutoTextPageGenerator.List.Bookmark"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.UserProperties"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.NamedExpressions"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					},
					new Class[] { AutoTextPage.class, CellPaddingPage.class, BordersPage.class, ItemMarginPage.class,
							SectionPage.class, VisibilityPage.class, TOCExpressionPage.class,
							BookMarkExpressionPage.class, CommentsPage.class, UserPropertiesPage.class,
							NamedExpressionsPage.class, AdvancePropertyPage.class, });
		}
		return null;
	}

	protected CategoryHolder getCategories(DesignElementHandle handle) {
		if (handle instanceof MasterPageHandle) {
			return new CategoryHolder(new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_BORDERS, CATEGORY_KEY_MARGIN,
					// CATEGORY_KEY_MASTER_COLUMNS,
					CATEGORY_KEY_HEADER_FOOTER, CATEGORY_KEY_COMMENTS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("MasterPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("MasterPageGenerator.List.Borders"),
							Messages.getString("MasterPageGenerator.List.Margins"), //$NON-NLS-1$
							// Messages.getString( "MasterPageGenerator.List.Columns" ), //$NON-NLS-1$
							Messages.getString("MasterPageGenerator.List.Header&Footer"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { MasterPageGeneralPage.class, BordersPage.class, MarginsPage.class,
							// MasterColumnsPage.class,
							HeaderFooterPage.class, CommentsPage.class, AdvancePropertyPage.class, });
		}
		if (handle instanceof DataSetHandle) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_COMMENTS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("DataSetPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { DataSetPage.class, CommentsPage.class, AdvancePropertyPage.class, });
		}
		if (handle instanceof DataSourceHandle) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_COMMENTS, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("DataSourcePageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { DataSourcePage.class, CommentsPage.class, AdvancePropertyPage.class, });
		}
		if (handle instanceof TemplateElementHandle) {
			return new CategoryHolder(
					new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_I18N, CATEGORY_KEY_COMMENTS,
							CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("TemplateReportItemPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("TemplateReportItemPageGenerator.List.I18n"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.Comments"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { TemplateReportItemPage.class, TemplateReportItemI18Page.class, CommentsPage.class,
							AdvancePropertyPage.class, });
		}
		if (handle instanceof VariableElementHandle) {
			return new CategoryHolder(new String[] { CATEGORY_KEY_GENERAL, CATEGORY_KEY_ADVANCEPROPERTY, },
					new String[] { Messages.getString("ReportPageGenerator.List.General"), //$NON-NLS-1$
							Messages.getString("ReportPageGenerator.List.AdvancedProperty"), //$NON-NLS-1$
					}, new Class[] { VariablePage.class, AdvancePropertyPage.class, });
		}
		return getCategories(handle.getDefn().getName());
	}

	/**
	 * Get CategoryProvider according to input element name
	 */
	protected ICategoryProvider getCategoryProvider(String elementName) {
		CategoryHolder holder = getCategories(elementName);

		if (holder != null) {
			return new CategoryProvider(holder.keys, holder.labels, holder.pageClasses);
		}

		return null;
	}

	/**
	 * Get the CategoryProvider according to input handle
	 * 
	 * @param handle
	 * @return
	 */
	protected ICategoryProvider getCategoryProvider(DesignElementHandle handle) {
		CategoryHolder holder = getCategories(handle);

		if (holder != null) {
			return new CategoryProvider(holder.keys, holder.labels, holder.pageClasses);
		}

		return null;
	}
}