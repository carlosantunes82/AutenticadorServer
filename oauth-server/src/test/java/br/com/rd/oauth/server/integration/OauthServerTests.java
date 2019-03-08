package br.com.rd.oauth.server.integration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OauthServerTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Value("${url.base.resource.cadastro.pbm}")
    private String baseUrl;

    @Test
    public void autenticar_comCredenciaisCorretas_retornarOk(){

        // Headers
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, "Basic cmQ6YzJWamNtVjBYM0poYVdGZlpISnZaMkZ6YVd4Zk1qQXhPUT09");

        // Request
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", "RaiaDrogasil");
        map.add("password", "UmFpYWRyb2dhc2lsMjAxOQ==");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity response = restTemplate.exchange(baseUrl + "/oauth/token" , HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void autenticar_comUserIncorreto_retornarHttpErro400(){

        // Headers
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, "Basic cmQ6YzJWamNtVjBYM0poYVdGZlpISnZaMkZ6YVd4Zk1qQXhPUT09");

        // Request
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", "RaiaDrogasil_USER_INCORRETO");
        map.add("password", "UmFpYWRyb2dhc2lsMjAxOQ==");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);


        ResponseEntity response = null;
        try {
            response = restTemplate.exchange(baseUrl + "/oauth/token" , HttpMethod.POST, request, String.class);
        } catch (final HttpClientErrorException httpClientErrorException) {
            assertThat(httpClientErrorException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    public void autenticar_comSenhaIncorreta_retornarHttpErro400(){

        // Headers
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, "Basic cmQ6YzJWamNtVjBYM0poYVdGZlpISnZaMkZ6YVd4Zk1qQXhPUT09");

        // Request
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", "RaiaDrogasil");
        map.add("password", "UmFpYWRyb2dhc2lsMjAxOQ==PASSWORD_INCORRETO");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity response = null;
        try {
            response = restTemplate.exchange(baseUrl + "/oauth/token" , HttpMethod.POST, request, String.class);
        } catch (final HttpClientErrorException httpClientErrorException) {
            assertThat(httpClientErrorException.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Test
    public void autenticar_comHashUserAndPasswordAplicationIncorretos_retornarHttpErro400(){

        // Headers
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(new MediaType[] { MediaType.APPLICATION_JSON }));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(HttpHeaders.AUTHORIZATION, "Basic cmQ6YzJWamNtVjBYM0poYVdGZlpISnZaMkZ6YVd4Zk1qQXhPUT09_HASH_INCORRETO");

        // Request
        MultiValueMap<String, String> map= new LinkedMultiValueMap<String, String>();
        map.add("grant_type", "password");
        map.add("username", "RaiaDrogasil");
        map.add("password", "UmFpYWRyb2dhc2lsMjAxOQ==");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        ResponseEntity response = restTemplate.exchange(baseUrl + "/oauth/token" , HttpMethod.POST, request, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FOUND);
    }
}


