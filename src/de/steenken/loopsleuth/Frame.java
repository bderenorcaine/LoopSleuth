package de.steenken.loopsleuth;

import java.util.Arrays;

public class Frame {
	byte[] data;

	long index;

	public Frame(byte[] data, long index, boolean deepCopy) {
		this.index = index;
		if (deepCopy) {
			this.data = new byte[data.length];
			for (int i = 0; i < data.length; ++i)
				this.data[i] = data[i];
		} else
			this.data = data;
	}

	public final byte[] data() {
		return data;
	}

	public long getIndex() {
		return index;
	}

	@Override
	public int hashCode() {
		int result = 0;
		for (int i = 0; i < data.length; ++i)
			result += data[i];
		result = (int) Math.floor(result / data.length);
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
		Frame other = (Frame) obj;
		if (!Arrays.equals(data, other.data))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Frame [data=" + Arrays.toString(data) + ", index=" + index
				+ "]";
	}

}
