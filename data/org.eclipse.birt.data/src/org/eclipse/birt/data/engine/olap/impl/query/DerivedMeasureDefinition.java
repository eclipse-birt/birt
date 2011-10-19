package org.eclipse.birt.data.engine.olap.impl.query;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.olap.api.query.IDerivedMeasureDefinition;


public class DerivedMeasureDefinition extends MeasureDefinition implements IDerivedMeasureDefinition
{
	//
	private IBaseExpression expr;
	private int type;
	
	/**
	 * Constructor.
	 * 
	 * @param name
	 * @param type
	 * @param expr
	 */
	public DerivedMeasureDefinition( String name, int type, IBaseExpression expr )
	{
		super( name );
		this.expr = expr;
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition#getExpression()
	 */
	public IBaseExpression getExpression( )
	{
		return this.expr;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.api.query.IComputedMeasureDefinition#getType()
	 */
	public int getType()
	{
		return this.type;
	}
}
