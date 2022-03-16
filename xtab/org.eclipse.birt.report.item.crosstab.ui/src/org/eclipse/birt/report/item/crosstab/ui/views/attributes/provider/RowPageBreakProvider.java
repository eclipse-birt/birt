/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.ui.views.attributes.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.AbstractFormHandleProvider;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.dialogs.CrosstabPageBreakDialog;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Table;

/**
 *
 */

public class RowPageBreakProvider extends AbstractFormHandleProvider {

	private CellEditor[] editors;
	private String[] columnNames = { Messages.getString("CrosstabPageBreakProvider.Column.GroupLevel"), //$NON-NLS-1$
			Messages.getString("CrosstabPageBreakProvider.Column.Before"), //$NON-NLS-1$
			Messages.getString("CrosstabPageBreakProvider.Column.After"), //$NON-NLS-1$
			Messages.getString("CrosstabPageBreakProvider.Column.Inside"), //$NON-NLS-1$
			Messages.getString("CrosstabPageBreakProvider.Column.Interval"), //$NON-NLS-1$
	};

	final private static IChoice[] pagebreakBeforeChoicesAll = DEUtil.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_PAGE_BREAK_BEFORE).getChoices();
	final private static IChoice[] pagebreakAfterChoicesAll = DEUtil.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_PAGE_BREAK_AFTER).getChoices();
	final private static IChoice[] pagebreakInsideChoicesAll = DEUtil.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_PAGE_BREAK_INSIDE).getChoices();

	final private static int PAGE_BREAK_BEFORE = 0;
	final private static int PAGE_BREAK_AFTER = 1;
	final private static int PAGE_BREAK_INSIDE = 2;

	private int[] columnWidths = { 160, 140, 140, 140, 80 };

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = {};

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#canModify(java.lang.Object, java.lang.String)
	 */
	@Override
	public boolean canModify(Object element, String property) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#doAddItem(int)
	 */
	@Override
	public boolean doAddItem(int pos) throws Exception {
		// TODO Auto-generated method stub
		CrosstabReportItemHandle reportHandle = null;
		try {
			reportHandle = (CrosstabReportItemHandle) ((ExtendedItemHandle) (((List) input)).get(0)).getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		CrosstabPageBreakDialog pageBreakDialog = new CrosstabPageBreakDialog(reportHandle);
		pageBreakDialog.setAxis(ICrosstabConstants.ROW_AXIS_TYPE);
		if (pageBreakDialog.open() == Dialog.CANCEL) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#doDeleteItem(int)
	 */
	@Override
	public boolean doDeleteItem(int pos) throws Exception {
		// TODO Auto-generated method stub
		CrosstabReportItemHandle reportHandle = null;
		try {
			reportHandle = (CrosstabReportItemHandle) ((ExtendedItemHandle) (((List) input)).get(0)).getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		if (reportHandle.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE) != null) {
			LevelViewHandle levelViewHandle;
			CrosstabViewHandle crosstabView = reportHandle.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE);
			levelViewHandle = (LevelViewHandle) getLevel(crosstabView).get(pos);
			if (levelViewHandle != null) {
				CommandStack stack = SessionHandleAdapter.getInstance().getCommandStack();
				stack.startTrans("Remove PageBreak"); //$NON-NLS-1$
				try {
					levelViewHandle.setPageBreakAfter(null);
					levelViewHandle.setPageBreakBefore(null);
					levelViewHandle.getModelHandle().setProperty(ILevelViewConstants.PAGE_BREAK_INTERVAL_PROP, null);
				} catch (SemanticException e) {
					stack.rollback();
					return false;
				}
				stack.commit();
			}

		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#doEditItem(int)
	 */
	@Override
	public boolean doEditItem(int pos) {
		// TODO Auto-generated method stub
		CrosstabReportItemHandle reportHandle = null;
		try {
			reportHandle = (CrosstabReportItemHandle) ((ExtendedItemHandle) (((List) input)).get(0)).getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}

		List list = new ArrayList();
		if (reportHandle.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE) != null) {
			CrosstabViewHandle crosstabView = reportHandle.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE);
			list = getLevel(crosstabView);
		}
		CrosstabPageBreakDialog pageBreakDialog = new CrosstabPageBreakDialog(reportHandle);
		pageBreakDialog.setLevelViewHandle((LevelViewHandle) list.get(pos));
		pageBreakDialog.setAxis(ICrosstabConstants.ROW_AXIS_TYPE);
		if (pageBreakDialog.open() == Dialog.CANCEL) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#doMoveItem(int, int)
	 */
	@Override
	public boolean doMoveItem(int oldPos, int newPos) throws Exception {
		// TODO Auto-generated method stub

		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getColumnNames()
	 */
	@Override
	public String[] getColumnNames() {
		// TODO Auto-generated method stub
		return columnNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getColumnText(java.lang.Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		if (!(element instanceof LevelViewHandle)) {
			return ""; //$NON-NLS-1$
		}
		LevelViewHandle levelViewHandle = (LevelViewHandle) element;
		switch (columnIndex) {
		case 0:
			return levelViewHandle.getCubeLevelName();
		case 1:
			return getPageBreakDisplayName(levelViewHandle.getPageBreakBefore(), PAGE_BREAK_BEFORE);
		case 2:
			return getPageBreakDisplayName(levelViewHandle.getPageBreakAfter(), PAGE_BREAK_AFTER);
		case 3:
			return getPageBreakDisplayName(levelViewHandle.getPageBreakInside(), PAGE_BREAK_INSIDE);
		case 4:
			if (levelViewHandle.getModelHandle().getProperty(ILevelViewConstants.PAGE_BREAK_INTERVAL_PROP) == null) {
				return ""; //$NON-NLS-1$
			} else {
				return "" + levelViewHandle.getPageBreakInterval(); //$NON-NLS-1$
			}

		default:
			break;
		}
		return ""; //$NON-NLS-1$
	}

	private String getPageBreakDisplayName(String value, int type) {
		IChoice[][] pageBreakChoices = new IChoice[3][];
		pageBreakChoices[0] = pagebreakBeforeChoicesAll;
		pageBreakChoices[1] = pagebreakAfterChoicesAll;
		pageBreakChoices[2] = pagebreakInsideChoicesAll;

		if (type > 3 || type < 0) {
			type = PAGE_BREAK_BEFORE;
		}

		for (int i = 0; i < pageBreakChoices[type].length; i++) {
			if (pageBreakChoices[type][i].getName().equals(value)) {
				return pageBreakChoices[type][i].getDisplayName();
			}
		}

		return ""; //$NON-NLS-1$

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getColumnWidths()
	 */
	@Override
	public int[] getColumnWidths() {
		// TODO Auto-generated method stub
		return columnWidths;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getEditors(org.eclipse.swt.widgets.Table)
	 */
	@Override
	public CellEditor[] getEditors(Table table) {
		// TODO Auto-generated method stub
		if (editors == null) {
			editors = new CellEditor[columnNames.length];
			for (int i = 0; i < columnNames.length; i++) {
				editors[i] = new TextCellEditor();
			}
		}
		return editors;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		input = inputElement;
		Object obj = null;
		if (inputElement instanceof List) {
			obj = ((List) inputElement).get(0);
		} else {
			obj = inputElement;
		}

		List list = new ArrayList();
		if (!(obj instanceof ExtendedItemHandle)) {
			return EMPTY;
		}
		ExtendedItemHandle element = (ExtendedItemHandle) obj;
		CrosstabReportItemHandle crossTab = null;
		try {
			crossTab = (CrosstabReportItemHandle) element.getReportItem();
		} catch (ExtendedElementException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
		if (crossTab == null) {
			return list.toArray();
		}
		// if ( crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE )
		// != null )
		// {
		// DesignElementHandle elementHandle = crossTab.getCrosstabView(
		// ICrosstabConstants.ROW_AXIS_TYPE )
		// .getModelHandle( );
		// list.addAll( getLevel( (ExtendedItemHandle) elementHandle ) );
		// }

		if (crossTab.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE) != null) {
			CrosstabViewHandle crosstabView = crossTab.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE);
			list.addAll(getLevel(crosstabView));
		}

		return list.toArray();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getImagePath(java.lang.Object, int)
	 */
	@Override
	public Image getImage(Object element, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#getValue(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object getValue(Object element, String property) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider#modify(java.lang.Object, java.lang.String, java.lang.Object)
	 */
	@Override
	public boolean modify(Object data, String property, Object value) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IFormProvider
	 * #needRefreshed(org.eclipse.birt.report.model.api.activity.NotificationEvent )
	 */
	@Override
	public boolean needRefreshed(NotificationEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.designer.internal.ui.views.attributes.provider
	 * .IDescriptorProvider#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return Messages.getString("CrosstabPageGenerator.List.PageBreak"); //$NON-NLS-1$
	}

	private List getLevel(CrosstabViewHandle crosstabViewHandle) {
		List list = new ArrayList();
		if (crosstabViewHandle == null) {
			return list;
		}
		int dimensionCount = crosstabViewHandle.getDimensionCount();

		for (int i = 0; i < dimensionCount; i++) {
			DimensionViewHandle dimension = crosstabViewHandle.getDimension(i);
			int levelCount = dimension.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				LevelViewHandle levelHandle = dimension.getLevel(j);
				ExtendedItemHandle ext = (ExtendedItemHandle) levelHandle.getModelHandle();
				PropertyHandle before = ext.getPropertyHandle(ILevelViewConstants.PAGE_BREAK_BEFORE_PROP);
				PropertyHandle after = ext.getPropertyHandle(ILevelViewConstants.PAGE_BREAK_AFTER_PROP);
				if ((before != null && before.isLocal()) || (after != null && after.isLocal())) {
					list.add(levelHandle);
				}
			}
		}
		return list;
	}

	public boolean isAddEnable() {
		ExtendedItemHandle extend = (ExtendedItemHandle) DEUtil.getInputFirstElement(this.input);
		CrosstabReportItemHandle crossTab = null;
		try {
			crossTab = (CrosstabReportItemHandle) extend.getReportItem();
		} catch (ExtendedElementException e) {
			ExceptionUtil.handle(e);
			return false;
		}
		if (crossTab == null) {
			return false;
		}
		if (getLevelNames(crossTab).length == 0) {
			return false;
		} else {
			return true;
		}
	}

	private String[] getLevelNames(CrosstabReportItemHandle crosstabHandle) {
		List list = new ArrayList();
		if (crosstabHandle.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE) == null) {
			return new String[0];
		}

		CrosstabViewHandle crosstabView = crosstabHandle.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE);
		if (crosstabView == null) {
			return new String[0];
		}
		int dimensionCount = crosstabView.getDimensionCount();

		for (int i = 0; i < dimensionCount; i++) {
			DimensionViewHandle dimension = crosstabView.getDimension(i);
			int levelCount = dimension.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				if (!isInLevelList(crosstabHandle, dimension.getLevel(j))) {
					list.add(dimension.getLevel(j).getCubeLevelName());
				}
			}
		}

		return (String[]) list.toArray(new String[list.size()]);

	}

	private boolean isInLevelList(CrosstabReportItemHandle crosstabHandle, LevelViewHandle level) {
		List list = new ArrayList();
		if (crosstabHandle.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE) != null) {
			CrosstabViewHandle crosstabView = crosstabHandle.getCrosstabView(ICrosstabConstants.ROW_AXIS_TYPE);
			list = getLevel(crosstabView);
		}
		if (list.indexOf(level) != -1) {
			return true;
		}

		return false;

	}
}
