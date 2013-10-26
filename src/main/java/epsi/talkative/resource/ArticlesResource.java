package epsi.talkative.resource;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

public class ArticlesResource {

	@GET
	@Path("{article: .+}/comments")
	public Response getComments(@PathParam("article") @Encoded String article) {
		return Response.noContent().header("Link", "http://" + article + "; rel=\"article\"").build();
	}
}
