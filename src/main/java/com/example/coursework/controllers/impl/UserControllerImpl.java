package com.example.coursework.controllers.impl;


import com.example.coursework.models.User;
import com.example.coursework.controllers.UserController;
import com.example.coursework.services.impl.UserServiceImpl;
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
@RequestMapping("/users")
public class UserControllerImpl implements UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserControllerImpl(final UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAll() {
        return userServiceImpl.getAll();
    }

    @Override
    @GetMapping(path = "/{Id}")
    public ResponseEntity<User> getById(final @PathVariable("Id") Integer id) {
        if (userServiceImpl.getById(id) != null) {
            return ResponseEntity.ok(userServiceImpl.getById(id));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    @PostMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public User create(final @RequestBody User entity) {
        return userServiceImpl.create(entity);
    }

    @Override
    @PutMapping("/{Id}")
    public ResponseEntity<User> update(final @PathVariable("Id")Integer id,
                                       final @RequestBody User entity) {
        if (userServiceImpl.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userServiceImpl.update(id, entity);
        return ResponseEntity.ok(entity);
    }

    @Override
    @DeleteMapping(path = "/{Id}")
    public ResponseEntity<User> deleteById(final @PathVariable("Id") Integer id) {
        if (userServiceImpl.getById(id) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userServiceImpl.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
