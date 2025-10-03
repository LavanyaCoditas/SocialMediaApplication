package com.SocialMedia.Social.Media.Platform.project.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank

    private String title;

    @NotBlank
    private String content;

    @ManyToOne
    @JoinColumn(name = "user")
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comments> commentsList;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.PENDING;

    @ElementCollection
    private List<String> approvedBy = new ArrayList<>();

    @ElementCollection
    private List<String> disapprovedBy = new ArrayList<>();

}
