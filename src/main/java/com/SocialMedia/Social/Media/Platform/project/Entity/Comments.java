package com.SocialMedia.Social.Media.Platform.project.Entity;


import com.SocialMedia.Social.Media.Platform.project.Constants.CommentStatus;
import io.micrometer.core.annotation.Counted;
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
public class Comments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CommentStatus status = CommentStatus.PENDING; // Default  status when  new comment is created is set to PENDING

    @ElementCollection
    private List<String> approvedBy = new ArrayList<>(); // Moderator's ID who approved- to get the list for the moderator about what comment he has approved

    @ElementCollection
    private List<String> disapprovedBy = new ArrayList<>(); // Moderator ID who disapproved- used in moderator controller
}
