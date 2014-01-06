package epsi.talkative.resource;

import java.util.Properties;

import javax.ejb.EJB;

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
import org.junit.Test;
import org.junit.runner.RunWith;

import epsi.talkative.repository.Editor;
import epsi.talkative.repository.EditorRepository;

@RunWith(ApplicationComposer.class)
@EnableServices("jaxrs")
public class ArticlesResourceTest {

	@EJB
	private EditorRepository editorRepository;

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
		PersistenceUnit persistenceUnit = new PersistenceUnit("talkative");
		persistenceUnit.addClass(Editor.class);
		return persistenceUnit;
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

	@Test
	public void canRetrieveNoCommentForNewArticle() {
		String editorId = "myEditor";
		createEditor(editorId);
		WebClient client = createWebClient();

		String message = client.path("editors").path(editorId).path("articles/www.epsi.fr/i4/mon%20article.html/comments").get(String.class);

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

	private Editor createEditor(String editorId) {
		Editor editor = new Editor();
		editor.setId(editorId);
		editorRepository.create(editor);
		return editor;
	}
}
