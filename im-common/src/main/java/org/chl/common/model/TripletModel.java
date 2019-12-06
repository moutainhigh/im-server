package org.chl.common.model;

import java.io.Serializable;

public class TripletModel<A, B, C> extends PairModel<A, B> implements Serializable {

	private static final long serialVersionUID = 1L;

	public final C v3;

	public TripletModel(A v1, B v2, C v3) {
		super(v1, v2);
		this.v3 = v3;
	}
}
