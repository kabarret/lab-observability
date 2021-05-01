package br.com.demobff;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bff")
@Log
public class BFFController {

    @Autowired
    private BFFService bffService;

    @GetMapping("/buy")
    public ResponseEntity buy(){
        return new ResponseEntity(bffService.buy(), HttpStatus.OK);
    }

}
