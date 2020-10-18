package se.alipsa.renjinboot.webapp.service;

import org.renjin.eval.Context;
import org.renjin.script.RenjinScriptEngine;
import org.renjin.sexp.Environment;
import org.renjin.sexp.ListVector;
import org.renjin.sexp.SEXP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.script.ScriptException;
import java.util.Map;

@Service
public class AnalysisEngine {

  private final RenjinScriptEngine scriptEngine;

  @Autowired
  public AnalysisEngine(RenjinScriptEngine scriptEngine) {
    this.scriptEngine = scriptEngine;
  }

  public SEXP runScript(String script) throws ScriptException {
    return (SEXP)scriptEngine.eval(script);
  }

  public SEXP runScript(String script, Map<String, Object> params) throws ScriptException {
    params.forEach(scriptEngine::put);
    return (SEXP)scriptEngine.eval(script);
  }

  public SEXP getVariable(String varName) {
    Environment global = scriptEngine.getSession().getGlobalEnvironment();
    Context topContext = scriptEngine.getSession().getTopLevelContext();
    return global.getVariable(topContext, varName);
  }
}
