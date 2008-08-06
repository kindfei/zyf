package test.cluster.core;

public interface Service {

	/**
	 */
	public abstract String startup();

	/**
	 */
	public abstract String shutdown();

	/**
	 * @uml.property  name="MODE_M_H" readOnly="true"
	 */
	public static final int mode_m_h = 0;

	/**
	 * @uml.property  name="MODE_M_L"
	 */
	public static final int mode_m_l = 1;

	/**
	 * @uml.property  name="MODE_M_H_W_Q"
	 */
	public static final int mode_m_h_w_q = 2;

	/**
	 * @uml.property  name="MODE_M_H_W_T"
	 */
	public static final int mode_m_h_w_t = 3;

	/**
	 * @uml.property  name="MODE_M_L_W_Q" readOnly="true"
	 */
	public static final int mode_m_l_w_q = 4;

	/**
	 * @uml.property  name="MODE_M_L_W_T" readOnly="true"
	 */
	public static final int mode_m_l_w_t = 5;

}
