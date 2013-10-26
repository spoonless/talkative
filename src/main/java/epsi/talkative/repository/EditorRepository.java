package epsi.talkative.repository;


public class EditorRepository {

	public boolean contains(String editorId) {
		return !"unknown".equals(editorId);
	}

}
