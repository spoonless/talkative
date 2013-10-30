package epsi.talkative.repository;

public class MockEditorRepository implements EditorRepository {

	public static final String UNKNOWN_EDITOR = "unknown";

	@Override
	public boolean contains(String editorId) {
		return !UNKNOWN_EDITOR.equals(editorId);
	}

}
