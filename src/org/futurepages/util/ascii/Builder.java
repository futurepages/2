package org.futurepages.util.ascii;

public abstract class Builder<B extends IBuilder>{
	B tb;

	public Builder(B tb){
		this.tb = tb;
	}

	public B end(){
		return tb;
	}

}