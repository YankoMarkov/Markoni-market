package org.yanmark.markoni.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.yanmark.markoni.domain.entities.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {

    List<Comment> findAllByOrderByTimeDesc();
}
