package epsi.talkative.resource;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import epsi.talkative.repository.Comment;

@XmlRootElement(name = "comments")
public class CommentsRepresentation {

	private List<Comment> comment;

	public CommentsRepresentation() {
	}

	public CommentsRepresentation(List<Comment> comment) {
		this.comment = comment;
	}

	public List<Comment> getComment() {
		return comment;
	}

	public void setComment(List<Comment> comment) {
		this.comment = comment;
	}

}
