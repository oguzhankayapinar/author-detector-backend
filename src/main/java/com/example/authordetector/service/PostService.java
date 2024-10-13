package com.example.authordetector.service;

import com.example.authordetector.model.Post;
import com.example.authordetector.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }



    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
