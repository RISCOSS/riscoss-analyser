package eu.riscoss.fbk.lp;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class JsExtension {
	private ScriptEngineManager			mgr			= new ScriptEngineManager();
	private ScriptEngine			engine			= mgr.getEngineByName("JavaScript");
	
	private static final JsExtension instance = new JsExtension();
	
	public static JsExtension get() {
		return instance;
	}

	public Object eval( String code ) throws ScriptException {
		return engine.eval( code );
	}

	public void put( String key, Object object ) {
		engine.put( key, object );
	}

	public void set( String name, Object value ) {
		engine.put( name, value );
	}
}
