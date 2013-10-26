package epsi.talkative.resource;

import javax.ws.rs.Path;

public class EditorResource {

	@Path("articles")
	public ArticlesResource getArticlesResource() {
		return new ArticlesResource();
	}

}
