package epsi.talkative.repository;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class EditorRepository {

	@PersistenceContext(unitName = "talkative-persistence")
	private EntityManager entityManager;

	@EJB
	private ArticleRepository articleRepository;

	public Editor getEditor(String editorId) {
		return entityManager.find(Editor.class, editorId);
	}

	public ArticleRepository getArticleRepository() {
		return articleRepository;
	}
}
