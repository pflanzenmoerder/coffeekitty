package de.caffeine.kitty.web.common;

import org.apache.wicket.Component;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

public class InputFieldFeedbackContainer extends WebMarkupContainer {
	private static final long serialVersionUID = 1L;
	
	private static final AttributeAppender ERROR_CLASS_APPENDER = new ErrorCssClassAppender();

	private static class ErrorCssClassAppender extends AttributeAppender {
		private static final long serialVersionUID = 1L;

		public ErrorCssClassAppender() {
			super("class", Model.of("error"), " ");
		}
		
		@Override
		public boolean isTemporary(Component component) {
			return true;
		}
	}
	
	
	public InputFieldFeedbackContainer(String id) {
		super(id);
	}

	protected void onBeforeRender() {
		visitChildren(new IVisitor<Component, Void>() {
			@Override
			public void component(Component component, IVisit<Void> visit) {
				if (component.hasErrorMessage()) {
					InputFieldFeedbackContainer.this.add(ERROR_CLASS_APPENDER);
					visit.dontGoDeeper();
				}
			}
		});
		
		super.onBeforeRender();
	}
	
}
