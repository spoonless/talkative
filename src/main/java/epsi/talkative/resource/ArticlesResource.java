package epsi.talkative.resource;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import epsi.talkative.repository.Article;
import epsi.talkative.repository.ArticleRepository;
import epsi.talkative.repository.Editor;
import epsi.talkative.repository.NotFoundException;

public class ArticlesResource {

	private final Editor editor;
	private final ArticleRepository articleRepository;

	public ArticlesResource(Editor editor, ArticleRepository articleRepository) {
		this.editor = editor;
		this.articleRepository = articleRepository;
	}

	@GET
	@Path("{article: .+}/comments")
	public Response getComments(@PathParam("article") @Encoded String articleUrl) {
		try {
			Article article = articleRepository.getArticle(editor, "http://" + articleUrl);
			return Response.ok().header("Link", article.getUrl() + "; rel=\"article\"").build();
		} catch (NotFoundException e) {
			return Response.noContent().header("Link", "http://" + articleUrl + "; rel=\"article\"").build();
		}
	}
}
