package com.cj.theguardian.utils.time;

public final class TimeUnit {

	private TimeType type;
	private int value;
	
	public TimeUnit(TimeType type, int value) {
		this.type=type;
		this.value = value;
		if(value > type.getMaxValue() || value < type.getMinValue()) {
			throw new RuntimeException("Invalid TimeUnit "+value+"for " + type);
		}
	}
	
	public TimeType getType() {
		return type;
	}
	public int getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + value;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimeUnit other = (TimeUnit) obj;
		if (type != other.type)
			return false;
		if (value != other.value)
			return false;
		return true;
	}
	
}
