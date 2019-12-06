package org.chl.common.model;

import java.io.Serializable;

public class PairModel<A, B> implements Serializable {

	private static final long serialVersionUID = 1L;
	public final A v1;
	public final B v2;

	public PairModel(A v1, B v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
}
