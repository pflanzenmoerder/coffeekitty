package de.caffeine.kitty.web.page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import de.caffeine.kitty.entities.Account;

@SuppressWarnings("serial") 
public class SortedAccountListModel extends LoadableDetachableModel<List<Account>> {
	private IModel<Set<Account>> accountSet;
	
	public SortedAccountListModel(IModel<Set<Account>> accountSet) {
		super();
		this.accountSet = accountSet;
	}

	@Override
	protected List<Account> load() {
		Map<String, Account> accountMap = new TreeMap<String, Account>();
		for(Account account : accountSet.getObject()) {
			accountMap.put(account.getKitty().getName(), account);
		}
		return new ArrayList<Account>(accountMap.values());
	}

	@Override
	protected void onDetach() {
		super.onDetach();
		accountSet.detach();
	}
}