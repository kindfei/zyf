package test.ejb.hello;

import javax.ejb.Stateful;


@Stateful(name="HelloSFSB", mappedName="hf")
public class HelloSFSB extends HelloImpl implements HelloSFSBRemote, HelloSFSBLocal {

}
