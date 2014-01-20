package epsi.talkative.repository;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ArticleRepository {

	@PersistenceContext(unitName = "talkative-persistence")
	private EntityManager entityManager;

	public Article getArticle(Editor editor, String articleUrl) throws NotFoundException {
		TypedQuery<Article> query = entityManager.createQuery("select a from Article a where a.editor = :editor and a.url = :url", Article.class);
		query.setParameter("editor", editor);
		query.setParameter("url", articleUrl);
		List<Article> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			throw new NotFoundException();
		}
		return resultList.get(0);
	}

	public List<Comment> getComments(Editor editor, String articleUrl) throws NotFoundException {
		return new ArrayList<Comment>(getArticle(editor, articleUrl).getComments());
	}
}
