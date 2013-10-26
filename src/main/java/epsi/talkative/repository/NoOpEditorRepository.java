package epsi.talkative.repository;

import javax.ejb.Singleton;

@Singleton
public class NoOpEditorRepository implements EditorRepository {

	@Override
	public boolean contains(String editorId) {
		return true;
	}

}
