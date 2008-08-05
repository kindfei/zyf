package test.cluster.core;


public interface Event {

	/**
	 * @return  Returns the content.
	 * @uml.property  name="content"
	 */
	public Object getContent();

	/**
	 * Setter of the property <tt>content</tt>
	 * @param content  The content to set.
	 * @uml.property  name="content"
	 */
	public void setContent(Object content);

}
