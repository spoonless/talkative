package epsi.talkative.resource;

import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.jee.EjbJar;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.jee.jpa.unit.PersistenceUnit;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.Configuration;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.Module;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import epsi.talkative.repository.Editor;
import epsi.talkative.repository.EditorRepository;

@RunWith(ApplicationComposer.class)
@EnableServices("jaxrs")
public class ArticlesResourceTest {

	@Resource
	private UserTransaction userTransaction;

	@PersistenceContext(unitName = "talkative-persistence")
	private EntityManager entityManager;

	@Module
	@Classes(TalkativeApplication.class)
	public WebApp webapp() {
		return new WebApp().contextRoot("talkative");
	}

	@Module
	@Classes(EditorRepository.class)
	public EjbJar ejb() {
		return new EjbJar();
	}

	@Module
	public PersistenceUnit persistenceUnit() {
		return new PersistenceUnit("talkative-persistence");
	}

	@Configuration
	public Properties properties() throws Exception {
		Properties properties = new Properties();
		properties.put("javax.persistence.jdbc.driver", "org.hsqldb.jdbcDriver");
		properties.put("javax.persistence.jdbc.url", "jdbc:hsqldb:mem");
		properties.put("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");
		properties.put("openjpa.Log", "DefaultLevel=WARN, Runtime=INFO, Tool=INFO, SQL=TRACE");
		return properties;
	}

	@Before
	public void clearDatabase() throws Exception {
		userTransaction.begin();
		entityManager.createNativeQuery("truncate schema public and commit").executeUpdate();
		userTransaction.commit();
	}

	@Test
	public void canRetrieveNoCommentForNewArticle() throws Exception {
		userTransaction.begin();
		Editor editor = createEditor();
		userTransaction.commit();

		WebClient client = createWebClient();

		String message = client.path("editors").path(editor.getId()).path("articles/www.epsi.fr/i4/mon%20article.html/comments").get(String.class);

		Assert.assertEquals(204, client.getResponse().getStatus());
		Assert.assertEquals("http://www.epsi.fr/i4/mon%20article.html; rel=\"article\"", client.getResponse().getMetadata().getFirst("Link"));
		Assert.assertNull(message);
	}

	@Test
	public void cannotRetrieveCommentWhenEditorIsNotKnown() {
		WebClient client = createWebClient();

		client.path("editors").path("unknown").path("articles/www.epsi.fr/i4/myarticle.html/comments").get();

		Assert.assertEquals(403, client.getResponse().getStatus());
	}

	private WebClient createWebClient() {
		WebClient client = WebClient.create("http://localhost:4204/talkative/api");
		ClientConfiguration config = WebClient.getConfig(client);
		config.getInInterceptors().add(new LoggingInInterceptor());
		config.getOutInterceptors().add(new LoggingOutInterceptor());
		return client;
	}

	private Editor createEditor() {
		Editor editor = new Editor();
		editor.setId("myEditor");
		entityManager.persist(editor);
		return editor;
	}
}
