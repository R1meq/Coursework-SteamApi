package com.example.coursework.controllers.impl;

import com.example.coursework.controllers.PublisherController;
import com.example.coursework.models.Publisher;
import com.example.coursework.services.impl.PublisherServiceImpl;
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
@RequestMapping("/publishers")
public class PublisherControllerImpl implements PublisherController {

    private final PublisherServiceImpl publisherServiceImpl;

    @Autowired
    public PublisherControllerImpl(final PublisherServiceImpl publisherServiceImpl) {
        this.publisherServiceImpl = publisherServiceImpl;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Publisher> getAll() {
        return publisherServiceImpl.getAll();
    }

    @Override
    @GetMapping(path = "/{Id}")
    public ResponseEntity<Publisher> getById(final @PathVariable("Id") Integer id) {
        if (publisherServiceImpl.getById(id) != null) {
            return ResponseEntity.ok(publisherServiceImpl.getById(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public Publisher create(final @RequestBody Publisher entity) {
        return publisherServiceImpl.create(entity);
    }

    @Override
    @PutMapping("/{Id}")
    public ResponseEntity<Publisher> update(final @PathVariable("Id") Integer id,
                                            final @RequestBody Publisher entity) {
        if (publisherServiceImpl.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        publisherServiceImpl.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @Override
    @DeleteMapping(path = "/{Id}")
    public ResponseEntity<Publisher> deleteById(final @PathVariable("Id") Integer id) {
        if (publisherServiceImpl.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        publisherServiceImpl.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
