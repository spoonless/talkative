package epsi.talkative.repository;

import javax.annotation.ManagedBean;

@ManagedBean
public class MockEditorRepository extends EditorRepository {

	public static final String UNKNOWN_EDITOR = "unknown";

	@Override
	public boolean contains(String editorId) {
		return !UNKNOWN_EDITOR.equals(editorId);
	}

}
