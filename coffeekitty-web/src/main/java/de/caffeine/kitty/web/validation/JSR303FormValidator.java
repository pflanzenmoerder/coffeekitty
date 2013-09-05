/* 
 * Copyright (c) 2010 Zenika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.caffeine.kitty.web.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.html.form.AbstractTextComponent;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.AbstractPropertyModel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Validates a form and its components.
 * 
 * <p>
 * When this validator is added to a form, a visit to all of its components is
 * performed by a
 * {@link org.apache.wicket.markup.html.form.FormComponent.IVisitor
 * FormComponent.IVisitor} and a {@link BeanPropertyValidator} is added to each
 * of them.
 * 
 * <p>
 * <strong>IMPORTANT</strong> : This Validator must be added on a Form with a
 * {@link org.apache.wicket.model.CompoundPropertyModel CompoundPropertyModel}.
 * 
 * <p>
 * Example usage :<br />
 * 
 * <pre>
 * Form&lt;BeanObject&gt; form = new Form&lt;BeanObject&gt;(&quot;form&quot;, new CompoundPropertyModel(new BeanObject()));
 * form.add(new JSR303FormValidator());
 * </pre>
 * 
 * @author ophelie salm (zenika)
 * 
 */
public class JSR303FormValidator extends Behavior implements IFormValidator {
	/** Logger for this class */
	private static final Logger LOGGER = LoggerFactory.getLogger(JSR303FormValidator.class);
	
    /**
     * Performs a visit to the form components and adds a BeanPropertyValidator
     * to all of them.
     * 
     * @author ophelie salm (zenika)
     * 
     */
    private class JSR303ValidatorFormComponentVisitor extends Behavior implements IVisitor<FormComponent<?>, Void> {

        /**
         * {@inheritDoc}
         */
        public void component(FormComponent<?> component, IVisit<Void> visit) {
            if (component instanceof AbstractTextComponent) {
                final IModel<?> model = component.getModel();

                if (model instanceof AbstractPropertyModel) {
                	final AbstractPropertyModel<?> propertyModel = (AbstractPropertyModel<?>) model;
                	
                	// We add a BeanPropertyValidator only if the model owns a
                	// chained model with a model object != null
                	if (propertyModel.getChainedModel() != null && propertyModel.getChainedModel().getObject() != null) {
                		Class<?> modelObjectClass = propertyModel.getChainedModel().getObject().getClass();
                		component.add(new BeanPropertyValidator(modelObjectClass, propertyModel.getPropertyExpression(), groups));
                		// This formComponent is added to the dependent form
                		// components list
                		formComponents.add(component);
                	}
					
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("ignoring component " + component + " because it has no PropertyModel");
					}
				}
            }
        }

    }

    private Form<?> form;

    private static final long serialVersionUID = 1L;

    private transient final Logger log = LoggerFactory.getLogger(JSR303FormValidator.class);

    private final Class<?>[] groups;

    private final List<FormComponent<?>> formComponents;

    /**
     * @param groups
     *            a group or a list of groups targeted for validation
     */
    public JSR303FormValidator(final Class<?>... groups) {
        this.groups = groups;
        formComponents = new ArrayList<FormComponent<?>>();
    }

    /**
     * {@inheritDoc}
     */
    public FormComponent<?>[] getDependentFormComponents() {
        return formComponents.toArray(new FormComponent[formComponents.size()]);
    }

    /**
     * {@inheritDoc}
     * 
     * @throws WicketRuntimeException
     *             if the component on which this validator is added isn't a
     *             Form
     * @throws WicketRuntimeException
     *             if the form model isn't a CompoundPropertyModel
     */
    @Override
    public void bind(Component component) {

        // If the component isn't a form we throw an exception
        if (!(component instanceof Form)) {
            throw new WicketRuntimeException("The JSR303FormValidator must be added to a Form");
        }

        form = (Form<?>) component;

        // If the form model isn't a compoundPropertyModel we throw an exception
        if (!(form.getModel() instanceof CompoundPropertyModel<?>))
            throw new WicketRuntimeException("The form model must be a valid CompoundPropertyModel");

        log.debug("The JSR303FormValidator is being added to the form with path : " + form.getPath());

        // We visit all of the form's components
        form.visitFormComponents(new JSR303ValidatorFormComponentVisitor());

    }

    /**
     * {@inheritDoc}
     */
    public void validate(final Form<?> form) {
        // empty
        // TODO class level validation
    }
}