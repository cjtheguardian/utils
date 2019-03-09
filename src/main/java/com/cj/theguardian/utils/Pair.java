package com.cj.theguardian.utils;

public class Pair<F,S> implements Comparable <Pair<F,S>>{

	private F first;
	private S second;
	
	public Pair(F first, S second) {
		this.first = first;
		this.second = second;
	}
	
	public F getFirst() {
		return first;
	}
	public void setFirst(F first) {
		this.first = first;
	}
	public S getSecond() {
		return second;
	}
	public void setSecond(S second) {
		this.second = second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
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
		Pair other = (Pair) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
	
	public String toString() {
		return "["+StringUtil.toString(first) +" - "+StringUtil.toString(second)+"]";
	}

	@Override
	public int compareTo(Pair<F, S> o) {
		if(this.equals(o)) {
			return 0;
		}
		if(o == null) {
			return -1;
		}
		int compareTo = 0;
		compareTo = compare(this.getFirst(), o.getFirst());
		if(compareTo == 0) {
			compare(this.getSecond(), o.getSecond());
		}
		return compareTo == 0 ? -1 : compareTo;
	}
	
	private int compare(Object thisF, Object otherF) {
		if(thisF != null && otherF != null && thisF.getClass() == otherF.getClass() && thisF instanceof Comparable) {
			return ((Comparable)thisF).compareTo(otherF);			
		}
		return 0;
	}
	
}
