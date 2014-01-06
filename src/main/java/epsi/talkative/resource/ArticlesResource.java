package epsi.talkative.resource;

import java.util.List;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import epsi.talkative.repository.ArticleRepository;
import epsi.talkative.repository.Comment;
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
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getComments(@PathParam("article") @Encoded String articleUrl) {
		articleUrl = "http://" + articleUrl;
		try {
			List<Comment> comments = articleRepository.getComments(editor, articleUrl);
			return Response.ok()
					.header("Link", articleUrl + "; rel=\"article\"")
					.entity(new CommentsRepresentation(comments)).build();
		} catch (NotFoundException e) {
			return Response.noContent().header("Link", articleUrl + "; rel=\"article\"").build();
		}
	}
}
