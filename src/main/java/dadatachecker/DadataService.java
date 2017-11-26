package dadatachecker;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import dadatachecker.entity.Address;
import dadatachecker.entity.DadataResponseEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class DadataService {
    private static final String DADATA_URI = "https://suggestions.dadata.ru/suggestions/api/4_1/rs/suggest/address";
    private static final String TOKEN_FILE = "token.txt";

    private final ObjectMapper objectMapper;
    private final JsonFactory factory;

    public DadataService() {
        this.objectMapper = new ObjectMapper();
        this.factory = new JsonFactory();
    }


    public List<Address> getAddressSuggestions(String address) {
        try {
            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            DefaultHttpClient defaultHttpClient = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
            socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
            registry.register(new Scheme("https", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(defaultHttpClient.getParams(), registry);
            DefaultHttpClient client = new DefaultHttpClient(mgr, defaultHttpClient.getParams());

            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

            HttpPost post = new HttpPost(DADATA_URI);

            post.setHeader("Content-Type", "application/json");
            post.setHeader("Accept", "application/json");
            String token = Files.readAllLines(
                    Paths.get(
                            ClassLoader.getSystemResource(TOKEN_FILE).toURI()))
                    .stream()
                    .findFirst()
                    .get();
            post.setHeader("Authorization", token);

            HttpEntity entity = new StringEntity(String.format("{ \"query\": \"%s\", \"count\": 1 }", address), "UTF-8");

            post.setEntity(entity);

            HttpResponse response = client.execute(post);

            DadataResponseEntity dadataResponseEntity = objectMapper.readValue(response.getEntity().getContent(), DadataResponseEntity.class);
            return dadataResponseEntity.getSuggestions();
        } catch (IOException e) {
            System.err.println("Error during post request to dadata");
            e.printStackTrace();

        } catch (URISyntaxException e) {
            System.err.println("Error during token file reading");
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}

