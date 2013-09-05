package de.caffeine.kitty.web.page;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.service.AdminService;
import de.caffeine.kitty.web.header.HeaderPanel;


@AuthorizeInstantiation(Roles.ADMIN)
@Configurable
@SuppressWarnings("serial")
public class AdminPage extends BaseCafManPage {
	@Autowired
	private transient AdminService adminService;
	
	public AdminPage() {
		add(new HeaderPanel("header"));
		add(new AjaxLink<Void>("reset") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				adminService.reset();
			}
		});
	}

}
