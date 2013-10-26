package epsi.talkative.repository;

import javax.ejb.Singleton;

@Singleton
public class MockEditorRepository implements EditorRepository {

	public static final String UNKNOWN_EDITOR = "unknown";

	@Override
	public boolean contains(String editorId) {
		return !UNKNOWN_EDITOR.equals(editorId);
	}

}
