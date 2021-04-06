
package org.eclipse.birt.report.designer.internal.ui.views.attributes.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.swt.custom.FormWidgetFactory;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.IDescriptorProvider;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.provider.OutputPropertyDescriptorProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.engine.api.EmitterInfo;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;

public class OutputPropertyDescriptor extends PropertyDescriptor {

	/**
	 * The display title for all-outputs selection
	 */
	private static final String All_Title = Messages.getString("VisibilityPage.Label.DetailAll"); //$NON-NLS-1$

	/**
	 * The display title for specific-outputs selection
	 */
	private static final String Spec_Title = Messages.getString("VisibilityPage.Label.DetailSpecific"); //$NON-NLS-1$

	private Button allRadio;

	private Button specRadio;

	private Group group;

	private Composite allContainer;

	private Composite specContainer;

	private ExpressionComposite allExpression;

	private SelectionAdapter listener;

	public OutputPropertyDescriptor(boolean formStyle) {
		setFormStyle(formStyle);
	}

	public Control createControl(Composite parent) {
		listener = new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				if (!specCheckButtons.containsValue(e.getSource())) {
					setOutputEnable(hideCheckbox.getSelection());
				}

				if (hideCheckbox.getSelection()) {
					saveHideInfo();
				} else {
					try {
						outputDescriptorProvider.clearProperty();
					} catch (Exception e1) {
						ExceptionHandler.handle(e1);
					}
				}
			}
		};

		container = new Composite(parent, SWT.NONE);
		container.setLayout(WidgetUtil.createGridLayout(4));

		hideCheckbox = FormWidgetFactory.getInstance().createButton(container, SWT.CHECK, isFormStyle());
		hideCheckbox.setText(Messages.getString("VisibilityPage.Check.HideElement")); //$NON-NLS-1$
		GridData data = new GridData();
		// data.verticalSpan = 4;
		data.verticalAlignment = GridData.BEGINNING;
		hideCheckbox.setLayoutData(data);
		hideCheckbox.addSelectionListener(listener);
		WidgetUtil.createGridPlaceholder(container, 3, true, true);
		WidgetUtil.createHorizontalLine(container, 4, true);

		allRadio = FormWidgetFactory.getInstance().createButton(container, SWT.RADIO, isFormStyle());
		allRadio.setText(Messages.getString("VisibilityPage.Radio.AllOutputs")); //$NON-NLS-1$
		allRadio.addSelectionListener(listener);
		WidgetUtil.createGridPlaceholder(container, 3, true, true);

		specRadio = FormWidgetFactory.getInstance().createButton(container, SWT.RADIO, isFormStyle());
		specRadio.setText(Messages.getString("VisibilityPage.Radio.SpecificOutputs")); //$NON-NLS-1$
		WidgetUtil.createGridPlaceholder(container, 3, true, true);

		if (isFormStyle())
			group = FormWidgetFactory.getInstance().createGroup(container, ""); //$NON-NLS-1$
		else
			group = new Group(container, SWT.NONE);
		group.setLayout(new GridLayout());
		data = new GridData();
		data.verticalSpan = 4;
		data.verticalAlignment = GridData.FILL;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		group.setLayoutData(data);

		allContainer = buildUIForAlloutput(group);
		specContainer = buildUIForSpecific(group);
		// sets default output type
		allRadio.setSelection(true);

		setOutputInfo();
		return container;
	}

	public Control getControl() {
		return container;
	}

	private Composite buildUIForAlloutput(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);
		Label label = FormWidgetFactory.getInstance().createLabel(container, isFormStyle());
		label.setText(Messages.getString("VisibilityPage.Label.Expression")); //$NON-NLS-1$
		allExpression = new ExpressionComposite(container, isFormStyle());
		allExpression.addListener(SWT.Modify, new Listener() {

			public void handleEvent(Event event) {
				saveHideInfo();
			}
		});
		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		allExpression.setLayoutData(data);
		return container;
	}

	private Composite buildUIForSpecific(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(3, false);
		container.setLayout(layout);

		ReportEngine engine = new ReportEngine(new EngineConfig());
		String[] typeInfo = null;

		EmitterInfo[] emitters = engine.getEmitterInfo();
		if (emitters == null || emitters.length == 0) {
			typeInfo = new String[] {};
		} else {
			List<String> temp = new ArrayList<String>();
			for (int i = 0; i < emitters.length; i++) {
				EmitterInfo info = emitters[i];
				if (!info.isHidden()) {
					temp.add(info.getFormat());
				}
			}
			Collections.sort(temp, new AlphabeticallyComparator());
			typeInfo = temp.toArray(new String[temp.size()]);
		}
		specCheckButtons = new HashMap();
		specExpressions = new HashMap();
		for (int i = 0; i < typeInfo.length; i++) {
			Button btn = FormWidgetFactory.getInstance().createButton(container, SWT.CHECK, isFormStyle());
			btn.setText(typeInfo[i]);
			btn.setSelection(false);

			Label label = FormWidgetFactory.getInstance().createLabel(container, isFormStyle());
			label.setText(Messages.getString("VisibilityPage.Label.Expression")); //$NON-NLS-1$

			ExpressionComposite expression = new ExpressionComposite(container, isFormStyle());
			GridData data = new GridData();
			data.grabExcessHorizontalSpace = true;
			data.horizontalAlignment = GridData.FILL;
			expression.setLayoutData(data);

			specCheckButtons.put(typeInfo[i], btn);
			specExpressions.put(typeInfo[i], expression);

			btn.addSelectionListener(listener);
			expression.addListener(SWT.Modify, new Listener() {

				public void handleEvent(Event event) {
					saveHideInfo();
				}
			});
		}

		return container;
	}

	protected void saveHideInfo() {
		// if the user selects specRadio, the UI must refresh to show the
		// specific outputs.
		setOutputInfo();

		if (allRadio.getSelection()) {
			try {
				outputDescriptorProvider.saveAllOutput(allExpression.getExpression());
			} catch (Exception e) {
				ExceptionHandler.handle(e);
			}
		} else {
			boolean[] selections = new boolean[outputDescriptorProvider.getTypeInfo().length];
			Expression[] expressions = new Expression[outputDescriptorProvider.getTypeInfo().length];

			for (int i = 0; i < outputDescriptorProvider.getTypeInfo().length; i++) {
				selections[i] = ((Button) specCheckButtons.get(outputDescriptorProvider.getTypeInfo()[i]))
						.getSelection();
				expressions[i] = ((ExpressionComposite) specExpressions.get(outputDescriptorProvider.getTypeInfo()[i]))
						.getExpression();

			}

			try {
				outputDescriptorProvider.saveSpecialOutput(selections, expressions);
			} catch (Exception e) {
				ExceptionHandler.handle(e);
			}
			hideCheckbox.setSelection(true);
			setOutputEnable(true);
		}
	}

	/**
	 * Sets the enable status of output type.
	 * 
	 * @param enable The enable status.
	 */
	private void setOutputEnable(boolean enable) {
		allRadio.setEnabled(enable);
		specRadio.setEnabled(enable);
		group.setEnabled(enable);
		Composite container = getCurrentContainer();
		Control[] children = container.getChildren();
		if (container == allContainer) {
			for (int i = 0; i < children.length; i++) {
				children[i].setEnabled(enable);
			}
		} else {
			for (int i = 0; i < children.length; i++) {
				if (children[i] instanceof ExpressionComposite)
					continue;
				children[i].setEnabled(enable);
			}

			for (int i = 0; i < outputDescriptorProvider.getTypeInfo().length; i++) {
				((ExpressionComposite) specExpressions.get(outputDescriptorProvider.getTypeInfo()[i]))
						.setEnabled(hideCheckbox.getSelection()
								&& ((Button) specCheckButtons.get(outputDescriptorProvider.getTypeInfo()[i]))
										.getSelection());
			}
		}

	}

	private Composite getCurrentContainer() {
		return ((GridData) allContainer.getLayoutData()).heightHint != 0 ? allContainer : specContainer;
	}

	private void setOutputInfo() {
		if (allRadio.getSelection() && (((GridData) allContainer.getLayoutData()).heightHint == 0
				|| ((GridData) specContainer.getLayoutData()).heightHint != 0)) {
			group.setText(All_Title);
			((GridData) specContainer.getLayoutData()).heightHint = 0;
			((GridData) allContainer.getLayoutData()).heightHint = SWT.DEFAULT;
			group.layout();
			group.getParent().layout();
		}
		if (specRadio.getSelection() && (((GridData) specContainer.getLayoutData()).heightHint == 0
				|| ((GridData) allContainer.getLayoutData()).heightHint != 0)) {
			group.setText(Spec_Title);
			((GridData) specContainer.getLayoutData()).heightHint = SWT.DEFAULT;
			((GridData) allContainer.getLayoutData()).heightHint = 0;
			group.layout();
			group.getParent().layout();
		}
		dealParentLayout(container);
	}

	private void dealParentLayout(Composite container) {
		if (container == null)
			return;
		if (!(container instanceof ScrolledComposite)) {
			dealParentLayout(container.getParent());
			return;
		}
		ScrolledComposite composite = (ScrolledComposite) container;
		Composite control = (Composite) composite.getContent();
		composite.setMinSize(control.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		control.layout();
	}

	public void save(Object obj) throws SemanticException {
		// TODO Auto-generated method stub

	}

	private boolean inputChanged = false;

	public void setInput(Object object) {
		super.setInput(object);
		getDescriptorProvider().setInput(object);
		inputChanged = true;
	}

	private void resetUI() {
		allRadio.setSelection(true);
		specRadio.setSelection(false);
		setOutputInfo();

		for (int i = 0; i < outputDescriptorProvider.getTypeInfo().length; i++) {
			((Button) specCheckButtons.get(outputDescriptorProvider.getTypeInfo()[i])).setSelection(false);
			((ExpressionComposite) specExpressions.get(outputDescriptorProvider.getTypeInfo()[i]))
					.setExpression(new Expression("true", UIUtil.getDefaultScriptType()));//$NON-NLS-1$
		}
		allExpression.setExpression(new Expression("true", UIUtil.getDefaultScriptType()));//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.report.designer.internal.ui.views.attributes.page.
	 * AttributePage#refreshValues(java.util.Set)
	 */

	private Object oldInput = null;

	private boolean needResetUI() {
		if (oldInput == null)
			oldInput = getInput();
		Iterator visibilities = outputDescriptorProvider.getVisibilityRulesIterator();
		if ((visibilities == null || !visibilities.hasNext()) && hideCheckbox.getSelection()) {
			if (!allRadio.getSelection()) {
				if (oldInput.equals(getInput()))
					return false;
			}
			oldInput = getInput();
			return true;
		} else {
			oldInput = getInput();
			while (visibilities.hasNext()) {
				Object obj = visibilities.next();
				String format = outputDescriptorProvider.getFormat(obj);
				if (outputDescriptorProvider.isFormatTypeAll(format)) {
					return true;
				}
			}
			if (getCurrentContainer() == specContainer) {
				if (!hideCheckbox.getSelection())
					return true;
				return false;
			} else
				return true;
		}
	}

	public void load() {
		if (needResetUI())
			inputChanged();
		doLoad();
	}

	private void inputChanged() {
		if (inputChanged) {
			resetUI();
			inputChanged = false;
		}
	}

	private void doLoad() {
		setExpressionProvider();

		if (!outputDescriptorProvider.isEnabled()) {
			hideCheckbox.setSelection(false);
			hideCheckbox.setEnabled(false);
			setOutputEnable(false);
			return;
		} else
			hideCheckbox.setEnabled(true);

		if (needResetUI()) {
			hideCheckbox.setEnabled(true);

			Iterator visibilities = outputDescriptorProvider.getVisibilityRulesIterator();

			hideCheckbox.setSelection((visibilities != null) && visibilities.hasNext());
			setOutputEnable(hideCheckbox.getSelection());

			if (visibilities == null)
				return;

			while (visibilities.hasNext()) {
				Object obj = visibilities.next();
				String format = outputDescriptorProvider.getFormat(obj);
				Expression expression = outputDescriptorProvider.getExpression(obj);
				if (expression == null)
					expression = new Expression(null, UIUtil.getDefaultScriptType()); // $NON-NLS-1$
				if (outputDescriptorProvider.isFormatTypeAll(format)) {
					allRadio.setSelection(true);
					allExpression.setExpression(expression);
					specRadio.setSelection(false);
					break;
				}
				allRadio.setSelection(false);
				specRadio.setSelection(true);

				if (specCheckButtons.containsKey(format))
					((Button) specCheckButtons.get(format)).setSelection(true);
				if (specExpressions.containsKey(format))
					((ExpressionComposite) specExpressions.get(format)).setExpression(expression);
			}

			setOutputInfo();
			setOutputEnable(hideCheckbox.getSelection());

			for (int i = 0; i < outputDescriptorProvider.getTypeInfo().length; i++) {
				ExpressionComposite expr = (ExpressionComposite) specExpressions
						.get(outputDescriptorProvider.getTypeInfo()[i]);
				Button check = (Button) specCheckButtons.get(outputDescriptorProvider.getTypeInfo()[i]);
				expr.setEnabled(check.getEnabled() && check.getSelection() && hideCheckbox.getSelection());
			}
		} else {
			for (int i = 0; i < outputDescriptorProvider.getTypeInfo().length; i++) {
				((Button) specCheckButtons.get(outputDescriptorProvider.getTypeInfo()[i])).setSelection(false);
				((ExpressionComposite) specExpressions.get(outputDescriptorProvider.getTypeInfo()[i]))
						.setExpression(new Expression("true", UIUtil.getDefaultScriptType())); //$NON-NLS-1$
			}

			Iterator visibilities = outputDescriptorProvider.getVisibilityRulesIterator();
			while (visibilities.hasNext()) {
				Object obj = visibilities.next();
				String format = outputDescriptorProvider.getFormat(obj);
				Expression expression = outputDescriptorProvider.getExpression(obj);
				if (expression == null)
					expression = new Expression(null, UIUtil.getDefaultScriptType());
				if (specCheckButtons.containsKey(format))
					((Button) specCheckButtons.get(format)).setSelection(true);
				if (specExpressions.containsKey(format))
					((ExpressionComposite) specExpressions.get(format)).setExpression(expression);
			}

			for (int i = 0; i < outputDescriptorProvider.getTypeInfo().length; i++) {
				ExpressionComposite expr = (ExpressionComposite) specExpressions
						.get(outputDescriptorProvider.getTypeInfo()[i]);
				Button check = (Button) specCheckButtons.get(outputDescriptorProvider.getTypeInfo()[i]);
				expr.setEnabled(check.getEnabled() && check.getSelection() && hideCheckbox.getSelection());
			}
		}
	}

	private void setExpressionProvider() {
		ExpressionProvider provider = outputDescriptorProvider.getExpressionProvider();
		allExpression.setExpressionProvider(provider);
		allExpression.setInput(this.getInput());
		for (int i = 0; i < outputDescriptorProvider.getTypeInfo().length; i++) {
			ExpressionComposite expressionComposite = ((ExpressionComposite) specExpressions
					.get(outputDescriptorProvider.getTypeInfo()[i]));
			expressionComposite.setExpressionProvider(provider);
			expressionComposite.setInput(this.getInput());
		}
	}

	private OutputPropertyDescriptorProvider outputDescriptorProvider;

	private HashMap specCheckButtons;

	private HashMap specExpressions;

	private Button hideCheckbox;

	private Composite container;

	public void setDescriptorProvider(IDescriptorProvider provider) {
		super.setDescriptorProvider(provider);
		if (provider instanceof OutputPropertyDescriptorProvider)
			outputDescriptorProvider = (OutputPropertyDescriptorProvider) provider;
	}

}
