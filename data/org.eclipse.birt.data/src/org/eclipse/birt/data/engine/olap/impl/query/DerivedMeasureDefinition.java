package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;

public class DerivedMeasureDefinition extends MeasureDefinition implements IDerivedMeasureDefinition {
	//
	private IBaseExpression expr;

	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param type
	 * @param expr
	 */
	public DerivedMeasureDefinition(String name, int type, IBaseExpression expr) {
		super(name);
		super.setDataType(type);
		this.expr = expr;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition#
	 * getExpression()
	 */
	public IBaseExpression getExpression() {
		return this.expr;
	}
}
