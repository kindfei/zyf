package test.ejb.hello;

import javax.ejb.Stateless;


@Stateless(name="HelloSLSB", mappedName="hl")
public class HelloSLSB extends HelloImpl implements HelloSLSBRemote, HelloSLSBLocal {

}
