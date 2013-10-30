package epsi.talkative.resource;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.openejb.jee.EjbJar;
import org.apache.openejb.jee.SingletonBean;
import org.apache.openejb.jee.WebApp;
import org.apache.openejb.junit.ApplicationComposer;
import org.apache.openejb.testing.Classes;
import org.apache.openejb.testing.EnableServices;
import org.apache.openejb.testing.Module;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import epsi.talkative.repository.MockEditorRepository;

@RunWith(ApplicationComposer.class)
@EnableServices("jaxrs")
public class ArticlesResourceTest {

	@Module
	@Classes(TalkativeApplication.class)
	public WebApp webapp() {
		return new WebApp().contextRoot("talkative");
	}

	@Module
	public EjbJar ejb() {
		return new EjbJar().enterpriseBean(new SingletonBean(MockEditorRepository.class));
	}

	@Test
	public void canRetrieveNoCommentForNewArticle() {
		WebClient client = createWebClient();

		String message = client.path("editors/davidg/articles/www.epsi.fr/i4/mon%20article.html/comments").get(String.class);

		Assert.assertEquals(204, client.getResponse().getStatus());
		Assert.assertEquals("http://www.epsi.fr/i4/mon%20article.html; rel=\"article\"", client.getResponse().getMetadata().getFirst("Link"));
		Assert.assertNull(message);
	}

	@Test
	public void cannotRetrieveCommentWhenEditorIsNotKnown() {
		WebClient client = createWebClient();

		client.path("editors").path(MockEditorRepository.UNKNOWN_EDITOR).path("articles/www.epsi.fr/i4/myarticle.html/comments").get();

		Assert.assertEquals(403, client.getResponse().getStatus());
	}

	private WebClient createWebClient() {
		WebClient client = WebClient.create("http://localhost:4204/talkative/api");
		ClientConfiguration config = WebClient.getConfig(client);
		config.getInInterceptors().add(new LoggingInInterceptor());
		config.getOutInterceptors().add(new LoggingOutInterceptor());
		return client;
	}
}
