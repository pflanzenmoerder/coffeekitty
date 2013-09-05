package de.caffeine.kitty.web.tools;

import org.apache.wicket.model.IModel;

@SuppressWarnings("serial")
public class TestModel<T> implements IModel<T> {

	private T object;
	
	
	public TestModel(T object) {
		super();
		this.object = object;
	}

	@Override
	public void detach() {	
	}

	@Override
	public T getObject() {
		return object;
	}

	@Override
	public void setObject(T object) {
		this.object=object;
	}
	
}
