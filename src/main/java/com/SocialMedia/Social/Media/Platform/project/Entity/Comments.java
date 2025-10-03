package com.SocialMedia.Social.Media.Platform.project.Entity;


import com.SocialMedia.Social.Media.Platform.project.Entity.CommentStatus;
import com.SocialMedia.Social.Media.Platform.project.Entity.Posts;
import com.SocialMedia.Social.Media.Platform.project.Entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments") // Changed table name for consistency
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Comments { // Renamed to singular "Comment"
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id") // Renamed for clarity
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "user_id") // Renamed for clarity
    private User user;

    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.PENDING; // Default to PENDING

    @ElementCollection
    private List<String> approvedBy = new ArrayList<>(); // Moderator user IDs who approved

    @ElementCollection
    private List<String> disapprovedBy = new ArrayList<>(); // Moderator user IDs who disapproved
}
