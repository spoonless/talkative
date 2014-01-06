package epsi.talkative.resource;

import javax.ejb.EJB;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import epsi.talkative.repository.Editor;
import epsi.talkative.repository.EditorRepository;

@Path("editors")
public class EditorsResource {

	@EJB
	private EditorRepository editorRepository;

	@Path("{editor}")
	public EditorResource getEditor(@PathParam("editor") String editorId) {
		Editor editor = editorRepository.getEditor(editorId);
		if (editor == null) {
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		return new EditorResource(editorRepository, editor);
	}

}
