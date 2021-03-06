package com.zamrad.repositories;

import com.zamrad.domain.posts.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
@Transactional
public interface PostRepository extends JpaRepository<Post, UUID> {
}
