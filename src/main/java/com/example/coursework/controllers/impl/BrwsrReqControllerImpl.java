package com.example.coursework.controllers.impl;

import com.example.coursework.controllers.BrwsrReqController;
import com.example.coursework.models.BrwsrReq;
import com.example.coursework.services.impl.BrwsrReqServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/brwsrReqs")
public class BrwsrReqControllerImpl implements BrwsrReqController {

    private final BrwsrReqServiceImpl brwsrReqService;

    @Autowired
    public BrwsrReqControllerImpl(BrwsrReqServiceImpl brwsrReqService) {
        this.brwsrReqService = brwsrReqService;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BrwsrReq> getAll() {
        return brwsrReqService.getAll();
    }

    @Override
    @GetMapping(path = "/{Id}")
    public ResponseEntity<BrwsrReq> getById(final @PathVariable("Id") Integer id) {
        if (brwsrReqService.getById(id) != null) {
            return ResponseEntity.ok(brwsrReqService.getById(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public BrwsrReq create(final @RequestBody BrwsrReq entity) {
        return brwsrReqService.create(entity);
    }

    @Override
    @PutMapping("/{Id}")
    public ResponseEntity<BrwsrReq> update(final @PathVariable("Id")Integer id,
                                           final @RequestBody BrwsrReq entity) {
        if (brwsrReqService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        brwsrReqService.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @Override
    @DeleteMapping(path = "/{Id}")
    public ResponseEntity<BrwsrReq> deleteById(final @PathVariable("Id")Integer id) {
        if (brwsrReqService.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        brwsrReqService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
