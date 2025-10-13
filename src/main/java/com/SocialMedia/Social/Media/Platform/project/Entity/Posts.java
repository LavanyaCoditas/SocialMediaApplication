package com.SocialMedia.Social.Media.Platform.project.Entity;

import com.SocialMedia.Social.Media.Platform.project.Constants.PostStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="posts")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Column(name = "title")
    private String title;

    @NotBlank
    @Column(name = "content")
    private String content;

    @Column(name = "createdAt")
    private LocalDateTime dateTime;

    @ManyToOne
    @JoinColumn(name = "user")
    @JsonManagedReference
    User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    List<Comments> commentsList;

    @Enumerated(EnumType.STRING)
    private PostStatus status = PostStatus.PENDING;

    @ElementCollection
    @JsonManagedReference
    private List<String> approvedBy = new ArrayList<>();

    @ElementCollection
    @JsonManagedReference
    private List<String> disapprovedBy = new ArrayList<>();

}
