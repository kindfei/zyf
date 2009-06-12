package samples.jmx.mxbean.com.example.mxbeans;


public interface QueueSamplerMXBean {
    public QueueSample getQueueSample();
    public void clearQueue();
}
