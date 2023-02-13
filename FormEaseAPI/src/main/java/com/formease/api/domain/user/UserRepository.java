package com.formease.api.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository
    extends
        JpaRepository<User, Long>
{
    UserDetails findById( String string );

    UserDetails findByEmail( String email );

    Page<User> findAllByState( Pageable page, User.States state );
}
