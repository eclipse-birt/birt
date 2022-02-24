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

package org.eclipse.birt.report.item.crosstab.ui.views.dialogs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class CrosstabPageBreakDialog extends BaseDialog {

	protected LevelViewHandle levelHandle;
	protected CrosstabReportItemHandle reportItemHandle;

	public final static String TITLE = Messages.getString("CrosstabPageBreakDialog.Title"); //$NON-NLS-1$

	protected Combo levelCombo, pageBreakBeforeCombo, pageBreakAfterCombo, pageBreakInsideCombo;

	protected Text intervalText;

	final private static IChoice[] pagebreakBeforeChoicesAll = DEUtil.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_PAGE_BREAK_BEFORE).getChoices();
	final private static IChoice[] pagebreakAfterChoicesAll = DEUtil.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_PAGE_BREAK_AFTER).getChoices();
	final private static IChoice[] pagebreakInsideChoicesAll = DEUtil.getMetaDataDictionary()
			.getChoiceSet(DesignChoiceConstants.CHOICE_PAGE_BREAK_INSIDE).getChoices();

	final private static int PAGE_BREAK_BEFORE = 0;
	final private static int PAGE_BREAK_AFTER = 1;
	final private static int PAGE_BREAK_INSIDE = 2;

	private int axis;

	public void setAxis(int axis) {
		this.axis = axis;
	}

	protected CrosstabPageBreakDialog(String title) {
		this(UIUtil.getDefaultShell(), title);
		// TODO Auto-generated constructor stub
	}

	protected CrosstabPageBreakDialog(Shell parentShell, String title) {
		super(parentShell, title);
	}

	public void setLevelViewHandle(LevelViewHandle levelHandle) {
		this.levelHandle = levelHandle;
	}

	public CrosstabPageBreakDialog(CrosstabReportItemHandle reportItem) {
		this(TITLE);
		this.reportItemHandle = reportItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createContents(Composite parent) {
		UIUtil.bindHelp(parent, IHelpContextIds.INSERT_EDIT_PAGE_BREAK_DIALOG_ID);

		GridData gdata;
		GridLayout glayout;
		Composite contents = new Composite(parent, SWT.NONE);
		contents.setLayout(new GridLayout());
		contents.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTitleArea(contents);

		Composite composite = new Composite(contents, SWT.NONE);
		glayout = new GridLayout();
		glayout.marginHeight = 0;
		glayout.marginWidth = 0;
		glayout.verticalSpacing = 0;
		composite.setLayout(glayout);
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		applyDialogFont(composite);
		initializeDialogUnits(composite);

		Composite innerParent = (Composite) createDialogArea(composite);
		createButtonBar(composite);

		createPageBreakContent(innerParent);

		Composite space = new Composite(innerParent, SWT.NONE);
		gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.minimumWidth = 250;
		gdata.heightHint = 10;
		space.setLayoutData(gdata);

		Label lb = new Label(innerParent, SWT.SEPARATOR | SWT.HORIZONTAL);
		lb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		iniValue();
		// updateButtons( );

		return composite;
	}

	protected void iniValue() {
		if (levelHandle != null) {
			levelCombo.add(levelHandle.getCubeLevelName());
			levelCombo.setEnabled(false);
			levelCombo.select(0);
			if (levelHandle.getPageBreakBefore() != null) {
				pageBreakBeforeCombo.select(getPageBreakIndex(levelHandle.getPageBreakBefore(), PAGE_BREAK_BEFORE));
			}
			if (levelHandle.getPageBreakAfter() != null) {
				pageBreakAfterCombo.select(getPageBreakIndex(levelHandle.getPageBreakAfter(), PAGE_BREAK_AFTER));
			}
			if (axis == ICrosstabConstants.ROW_AXIS_TYPE && levelHandle.getPageBreakInside() != null) {
				pageBreakInsideCombo.select(getPageBreakIndex(levelHandle.getPageBreakInside(), PAGE_BREAK_INSIDE));
			}
			if (levelHandle.getModelHandle().getProperty(ILevelViewConstants.PAGE_BREAK_INTERVAL_PROP) != null)
				intervalText.setText(Integer.toString(levelHandle.getPageBreakInterval()));
			else
				intervalText.setText(""); //$NON-NLS-1$
		} else {
			levelCombo.setItems(getLevelNames(reportItemHandle, axis));
			levelCombo.setEnabled(true);
			levelCombo.select(0);
			pageBreakBeforeCombo.select(0);
			pageBreakAfterCombo.select(0);
			if (axis == ICrosstabConstants.ROW_AXIS_TYPE) {
				pageBreakInsideCombo.select(0);
			}
			intervalText.setText(""); //$NON-NLS-1$
		}

		updateButtons();

	}

	private CommandStack getActionStack() {
		return SessionHandleAdapter.getInstance().getCommandStack();
	}

	protected void okPressed() {

		CommandStack stack = getActionStack();
		stack.startTrans(Messages.getString("FormPage.Menu.ModifyProperty")); //$NON-NLS-1$

		LevelViewHandle level = getLevelFromName(reportItemHandle, levelCombo.getText(), axis);
		try {
			level.setPageBreakBefore(getPageBreak(pageBreakBeforeCombo.getSelectionIndex(), PAGE_BREAK_BEFORE));
			level.setPageBreakAfter(getPageBreak(pageBreakAfterCombo.getSelectionIndex(), PAGE_BREAK_AFTER));
			if (axis == ICrosstabConstants.ROW_AXIS_TYPE) {
				level.setPageBreakInside(getPageBreak(pageBreakInsideCombo.getSelectionIndex(), PAGE_BREAK_INSIDE));
			}
			if (intervalText.getText().trim().length() == 0) {
				level.getModelHandle().setProperty(ILevelViewConstants.PAGE_BREAK_INTERVAL_PROP, null);
			} else
				level.setPageBreakInterval(Integer.parseInt(intervalText.getText().trim()));
			stack.commit();
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			logger.log(Level.SEVERE, e.getMessage(), e);
			stack.rollback();
			super.okPressed();
			return;
		}

		super.okPressed();
	}

	protected void createPageBreakContent(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout glayout = new GridLayout(2, false);
		container.setLayout(glayout);

		Label lb = new Label(container, SWT.NONE);
		lb.setText(Messages.getString("CrosstabPageBreakDialog.Text.GroupLevel")); //$NON-NLS-1$

		levelCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		GridData gdata = new GridData(GridData.FILL_HORIZONTAL);
		gdata.minimumWidth = 190;
		levelCombo.setLayoutData(gdata);
		levelCombo.setVisibleItemCount(30);
		levelCombo.addListener(SWT.Selection, updateButtonListener);

		lb = new Label(container, SWT.NONE);
		lb.setText(Messages.getString("CrosstabPageBreakDialog.Text.PageBreakBefore")); //$NON-NLS-1$

		pageBreakBeforeCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		pageBreakBeforeCombo.setLayoutData(gdata);
		pageBreakBeforeCombo.setVisibleItemCount(30);
		pageBreakBeforeCombo.setItems(getPageBreakDisplayNames(PAGE_BREAK_BEFORE));
		pageBreakBeforeCombo.addListener(SWT.Selection, updateButtonListener);

		lb = new Label(container, SWT.NONE);
		lb.setText(Messages.getString("CrosstabPageBreakDialog.Text.PageBreakAfter")); //$NON-NLS-1$

		pageBreakAfterCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
		pageBreakAfterCombo.setLayoutData(gdata);
		pageBreakAfterCombo.setVisibleItemCount(30);
		pageBreakAfterCombo.setItems(getPageBreakDisplayNames(PAGE_BREAK_AFTER));
		pageBreakAfterCombo.addListener(SWT.Selection, updateButtonListener);

		if (axis == ICrosstabConstants.ROW_AXIS_TYPE) {
			lb = new Label(container, SWT.NONE);
			lb.setText(Messages.getString("CrosstabPageBreakDialog.Text.PageBreakInside")); //$NON-NLS-1$

			pageBreakInsideCombo = new Combo(container, SWT.BORDER | SWT.READ_ONLY);
			pageBreakInsideCombo.setLayoutData(gdata);
			pageBreakInsideCombo.setVisibleItemCount(30);
			pageBreakInsideCombo.setItems(getPageBreakDisplayNames(PAGE_BREAK_INSIDE));
			pageBreakInsideCombo.addListener(SWT.Selection, updateButtonListener);
		}

		lb = new Label(container, SWT.NONE);
		lb.setText(Messages.getString("CrosstabPageBreakDialog.Text.PageBreakInterval")); //$NON-NLS-1$

		intervalText = new Text(container, SWT.BORDER);
		intervalText.setLayoutData(gdata);
		intervalText.addListener(SWT.Modify, updateButtonListener);
		intervalText.addListener(SWT.Verify, numberVerifyListener);

	}

	private Composite createTitleArea(Composite parent) {
		int heightMargins = 3;
		int widthMargins = 8;
		final Composite titleArea = new Composite(parent, SWT.NONE);
		FormLayout layout = new FormLayout();
		layout.marginHeight = heightMargins;
		layout.marginWidth = widthMargins;
		titleArea.setLayout(layout);

		Display display = parent.getDisplay();
		Color background = JFaceColors.getBannerBackground(display);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.heightHint = 20 + (heightMargins * 2);
		titleArea.setLayoutData(layoutData);
		titleArea.setBackground(background);

		titleArea.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				e.gc.setForeground(titleArea.getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				Rectangle bounds = titleArea.getClientArea();
				bounds.height = bounds.height - 2;
				bounds.width = bounds.width - 1;
				e.gc.drawRectangle(bounds);
			}
		});

		Label label = new Label(titleArea, SWT.NONE);
		label.setBackground(background);
		label.setFont(FontManager.getFont(label.getFont().toString(), 10, SWT.BOLD));
		label.setText(getTitle());

		return titleArea;
	}

	private String[] getPageBreakDisplayNames(int type) {
		IChoice[][] pageBreakChoices = new IChoice[3][];
		pageBreakChoices[0] = pagebreakBeforeChoicesAll;
		pageBreakChoices[1] = pagebreakAfterChoicesAll;
		pageBreakChoices[2] = pagebreakInsideChoicesAll;

		if (type > 3 || type < 0) {
			type = PAGE_BREAK_BEFORE;
		}

		String retArray[] = new String[pageBreakChoices[type].length];
		for (int i = 0; i < pageBreakChoices[type].length; i++) {
			retArray[i] = pageBreakChoices[type][i].getDisplayName();
		}

		return retArray;

	}

	private String getPageBreakDisplayName(String name, int type) {
		IChoice[][] pageBreakChoices = new IChoice[3][];
		pageBreakChoices[0] = pagebreakBeforeChoicesAll;
		pageBreakChoices[1] = pagebreakAfterChoicesAll;
		pageBreakChoices[2] = pagebreakInsideChoicesAll;

		if (type > 3 || type < 0) {
			type = PAGE_BREAK_BEFORE;
		}

		for (int i = 0; i < pageBreakChoices[type].length; i++) {
			if (pageBreakChoices[type][i].getName().equals(name)) {
				return pageBreakChoices[type][i].getDisplayName();
			}

		}

		return ""; //$NON-NLS-1$

	}

	private String getPageBreak(int index, int type) {
		IChoice[][] pageBreakChoices = new IChoice[3][];
		pageBreakChoices[0] = pagebreakBeforeChoicesAll;
		pageBreakChoices[1] = pagebreakAfterChoicesAll;
		pageBreakChoices[2] = pagebreakInsideChoicesAll;

		if (type > 3 || type < 0) {
			type = PAGE_BREAK_BEFORE;
		}

		if (index < 0 || index >= pageBreakChoices[type].length) {
			return null;
		}

		return pageBreakChoices[type][index].getName();

	}

	private int getPageBreakIndex(String name, int type) {
		IChoice[][] pageBreakChoices = new IChoice[3][];
		pageBreakChoices[0] = pagebreakBeforeChoicesAll;
		pageBreakChoices[1] = pagebreakAfterChoicesAll;
		pageBreakChoices[2] = pagebreakInsideChoicesAll;

		if (type > 3 || type < 0) {
			type = PAGE_BREAK_BEFORE;
		}

		for (int i = 0; i < pageBreakChoices[type].length; i++) {
			if (pageBreakChoices[type][i].getName().equals(name)) {
				return i;
			}

		}

		return -1;

	}

	private String[] getLevelNames(CrosstabReportItemHandle crosstabHandle, int axis) {
		List list = new ArrayList();
		if (crosstabHandle.getCrosstabView(axis) == null) {
			return new String[0];
		}

		CrosstabViewHandle crosstabView = crosstabHandle.getCrosstabView(axis);
		if (crosstabView == null) {
			return new String[0];
		}
		int dimensionCount = crosstabView.getDimensionCount();

		for (int i = 0; i < dimensionCount; i++) {
			DimensionViewHandle dimension = crosstabView.getDimension(i);
			int levelCount = dimension.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				if (!isInLevelList(dimension.getLevel(j)))
					list.add(dimension.getLevel(j).getCubeLevelName());
			}
		}

		return (String[]) list.toArray(new String[list.size()]);

	}

	private LevelViewHandle getLevelFromName(CrosstabReportItemHandle crosstabHandle, String name, int axis) {
		if (crosstabHandle.getCrosstabView(axis) == null) {
			return null;
		}

		CrosstabViewHandle crosstabView = crosstabHandle.getCrosstabView(axis);
		if (crosstabView == null) {
			return null;
		}
		int dimensionCount = crosstabView.getDimensionCount();

		for (int i = 0; i < dimensionCount; i++) {
			DimensionViewHandle dimension = crosstabView.getDimension(i);
			int levelCount = dimension.getLevelCount();
			for (int j = 0; j < levelCount; j++) {
				if (dimension.getLevel(j).getCubeLevelName().equals(name)) {
					return dimension.getLevel(j);
				}
			}
		}

		return null;

	}

	private boolean isConditionOK() {
		if (levelCombo.getSelectionIndex() == -1) {
			return false;
		}

		if (pageBreakBeforeCombo.getSelectionIndex() == -1 && pageBreakAfterCombo.getSelectionIndex() == -1) {
			return false;
		}

		return true;
	}

	private boolean isInLevelList(LevelViewHandle level) {
		List list = new ArrayList();
		if (reportItemHandle.getCrosstabView(axis) != null) {
			CrosstabViewHandle crosstabView = reportItemHandle.getCrosstabView(axis);
			list = getLevel(crosstabView);
		}
		if (list.indexOf(level) != -1) {
			return true;
		}

		return false;

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

	/**
	 * Refreshes the OK button state.
	 * 
	 */
	protected void updateButtons() {
		getOkButton().setEnabled(isConditionOK());
	}

	protected Listener updateButtonListener = new Listener() {

		public void handleEvent(Event event) {
			updateButtons();

		}

	};

	protected Listener numberVerifyListener = new Listener() {

		public void handleEvent(Event e) {
			// TODO Auto-generated method stub
			Pattern pattern = Pattern.compile("[0-9]\\d*"); //$NON-NLS-1$
			Matcher matcher = pattern.matcher(e.text);
			if (matcher.matches()) // number
			{
				e.doit = true;
			} else if (e.text.length() > 0) // characters including tab, space,
			// Chinese character, etc.
			{
				e.doit = false;
			} else
			// control keys
			{
				e.doit = true;
			}

			try {
				if (e.doit = true && Integer.parseInt(((Text) e.widget).getText() + e.text) >= 0) {
					e.doit = true;
				}
			} catch (NumberFormatException e1) {
				e.doit = false;
			}
		}

	};
}
