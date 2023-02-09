package com.formease.api.controller;

import com.formease.api.domain.user.*;
import com.formease.api.domain.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping( "/users" )
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    @Transactional
    public ResponseEntity add( @RequestBody @Valid AddUserData data, UriComponentsBuilder uriBuilder )
    {
        User user = userService.createNewUser( data );

        URI uri = uriBuilder.path( "users/{id}" ).buildAndExpand( user.getId() ).toUri();

        return ResponseEntity.created( uri ).body( data );
    }

    @GetMapping
    public ResponseEntity<Page<UserDetailsData>> getUsers( Pageable page )
    {
        Page<UserDetailsData> users = userRepository.findAllByState( page, User.States.ACTIVE )
                                                    .map( UserDetailsData::new );

        return ResponseEntity.ok( users );
    }

    @GetMapping( "/{id}" )
    public ResponseEntity getUser( @PathVariable Long id )
    {
        User user = userRepository.getReferenceById( id );

        return ResponseEntity.ok( new UserDetailsData( user ) );
    }

    @PutMapping
    @Transactional
    public ResponseEntity update( @RequestBody @Valid UpdateUserData data )
    {
        User user = userRepository.getReferenceById(data.id() );

        user.updateInfo( data );

        return ResponseEntity.ok( new UserDetailsData( user ) );
    }

    @DeleteMapping( "/{id}" )
    @Transactional
    public ResponseEntity delete( @PathVariable Long id )
    {
        User user = userRepository.getReferenceById( id );

        user.delete();

        return ResponseEntity.noContent().build();
    }
}
