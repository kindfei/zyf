package test.cluster.core;

public class MessageDrivenService extends AbstractService {

	public MessageDrivenService(int mode, AbstractProcessor processor, String dest) {
		super(mode, processor);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
}
