package org.eclipse.birt.data.engine.api.script;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * API abstract class for NEvaluator
 *
 * @since 4.8
 */
public abstract class BaseNEvaluator {

	public abstract boolean evaluate(ScriptContext cx, Scriptable scope) throws DataException;

}
