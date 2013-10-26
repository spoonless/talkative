package epsi.talkative.repository;

import javax.annotation.ManagedBean;
import javax.enterprise.context.ApplicationScoped;

@ManagedBean
@ApplicationScoped
public class EditorRepository {

	public boolean contains(String editorId) {
		return true;
	}

}
