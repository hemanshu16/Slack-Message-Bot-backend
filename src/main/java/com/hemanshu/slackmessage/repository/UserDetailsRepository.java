package com.hemanshu.slackmessage.repository;

import com.hemanshu.slackmessage.models.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserDetailsRepository extends JpaRepository<UserDetails, UUID> {
    Optional<UserDetails> findByPhoneNumberAndSuccessfullySentIsTrue(String phoneNumber);
}
