package epsi.talkative.resource;

import javax.ws.rs.Path;

import epsi.talkative.repository.Editor;
import epsi.talkative.repository.EditorRepository;

public class EditorResource {

	private final EditorRepository editorRepository;
	private final Editor editor;

	public EditorResource(EditorRepository editorRepository, Editor editor) {
		this.editorRepository = editorRepository;
		this.editor = editor;
	}

	@Path("articles")
	public ArticlesResource getArticlesResource() {
		return new ArticlesResource(editor, editorRepository.getArticleRepository());
	}

}
