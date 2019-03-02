package org.eclipse.birt.data.engine.impl.index;

import java.io.Serializable;
import java.util.Comparator;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompareHints;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;



public class CompareHintsComparator<K> implements Comparator<K>, Serializable
{
	
	private static final long serialVersionUID = 1L;
	private CompareHints compareHints;
	
	
	public CompareHintsComparator( CompareHints compareHints )
	{
		this.compareHints = compareHints;
	}

	public int compare( K o1, K o2 )
	{
		try
		{
			return ScriptEvalUtil.compare( o1, o2, compareHints );
		}
		catch ( DataException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0 ;
	}
	
}
