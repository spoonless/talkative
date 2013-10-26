package epsi.talkative.resource;

import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import epsi.talkative.repository.EditorRepository;

@Path("editors")
public class EditorsResource {

	@Inject
	private EditorRepository editorRepository;

	@Path("{editor}")
	public EditorResource getEditor(@PathParam("editor") String editorId) {
		if (!editorRepository.contains(editorId)) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		return new EditorResource();
	}

}
