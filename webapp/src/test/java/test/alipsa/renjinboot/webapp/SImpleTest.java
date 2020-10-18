package test.alipsa.renjinboot.webapp;

import org.junit.jupiter.api.Test;
import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.SEXP;

import javax.script.ScriptException;

public class SImpleTest {

  @Test
  public void testDataType() throws ScriptException {
    RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
    SEXP sexp = (SEXP)factory.getScriptEngine().eval("'foo'");
    System.out.println(sexp + ", class is " + sexp.getClass());
  }

}
