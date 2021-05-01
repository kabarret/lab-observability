package br.com.demobff;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Component
@Log
public class BFFService {

    @Retry(name = "backendretry")
    @CircuitBreaker(name = "backend", fallbackMethod = "fallback")
    public Object buy(){
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl
                = "http://localhost:8080";
        log.info("Trying access demo/buy");
        ResponseEntity<Object> response
                = restTemplate.getForEntity(fooResourceUrl + "/buy", Object.class);
        return response.getBody();
    }

    public Object fallback(Exception e){
        log.info("Circuit Open");
        return "Falha ao obter resultados";
    }

}
