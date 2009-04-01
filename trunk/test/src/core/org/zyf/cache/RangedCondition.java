package org.zyf.cache;

public class RangedCondition extends Condition {
	private Object fromKey;
	private Object toKey;
	
	public RangedCondition(Object key) {
		super(key);
	}
	
	public RangedCondition(Object fromKey, Object toKey) {
		super(null);
		this.fromKey = fromKey;
		this.toKey = toKey;
	}

	public Object getFromKey() {
		return fromKey;
	}

	public Object getToKey() {
		return toKey;
	}
}
