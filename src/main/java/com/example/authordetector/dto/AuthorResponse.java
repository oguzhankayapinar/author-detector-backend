package com.example.authordetector.dto;


import java.util.List;

public class AuthorResponse {
    private String name;
    private List<String> posts;

    public AuthorResponse(String name, List<String> posts) {
        this.name = name;
        this.posts = posts;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }
}

