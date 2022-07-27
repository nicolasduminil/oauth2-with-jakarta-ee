package fr.simplex_software.security.oauth2.tests;

import com.gargoylesoftware.htmlunit.*;
import fr.simplex_software.security.oauth2.*;
import org.jboss.arquillian.container.test.api.*;
import org.jboss.arquillian.junit5.*;
import org.jboss.arquillian.test.api.*;
import org.jboss.shrinkwrap.api.*;
import org.jboss.shrinkwrap.api.asset.*;
import org.jboss.shrinkwrap.api.spec.*;
import org.jboss.shrinkwrap.resolver.api.maven.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;

import java.io.*;
import java.net.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ArquillianExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ITAuthService
{
  private WebClient webClient;
  private static WebClient anotherWebClient;
  @ArquillianResource
  private URL base;

  @BeforeEach
  public void init()
  {
    webClient = new WebClient();
  }

  @AfterEach
  public void afterEach()
  {
    webClient.close();
    webClient = null;
  }

  @Deployment
  public static WebArchive createDeployment()
  {
    return ShrinkWrap.create(WebArchive.class, "oauth.war")
      .addClass(AuthService.class)
      .addClass(Callback.class)
      .addClass(SecuredPage.class)
      .addClass(UnsecuredPage.class)
      .addAsLibraries(Maven.resolver().resolve("org.apache.commons:commons-lang3:3.12.0").withTransitivity().asFile())
      .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
  }

  @Test
  @Order(10)
  @RunAsClient
  public void testGetUnsecuredPageShouldSucceed() throws IOException
  {
    String result = ((TextPage) webClient.getPage(base + "unsecured")).getContent();
    assertThat(result).isEqualTo("This is an unsecured web page");
  }

  @Test
  @Order(20)
  @RunAsClient
  public void testGetSecuredPageShouldSucceed() throws IOException
  {
    TextPage page = webClient.getPage(base + "secured");
    assertThat(page.getUrl().getPath()).isEqualTo("/oauth/callback");
    assertThat(page.getContent()).isNotEqualTo("null");
    anotherWebClient = webClient;
  }

  @Test
  @Order(30)
  @RunAsClient
  public void testGetSecuredPageShouldFail() throws IOException
  {
    assertThrows(FailingHttpStatusCodeException.class, ()-> anotherWebClient.getPage(base + "secured"));
  }
}
