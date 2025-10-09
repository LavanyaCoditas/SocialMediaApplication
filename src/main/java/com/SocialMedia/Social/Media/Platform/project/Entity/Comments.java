package com.SocialMedia.Social.Media.Platform.project.Entity;


import com.SocialMedia.Social.Media.Platform.project.Constants.CommentStatus;
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
    @JoinColumn(name = "post_id")
    private Posts post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private CommentStatus status = CommentStatus.PENDING; // Default  status when  new comment is created is set to PENDING

    @ElementCollection
    private List<String> approvedBy = new ArrayList<>(); // Moderator's ID who approved- to get the list for the moderator about what comment he has approved

    @ElementCollection
    private List<String> disapprovedBy = new ArrayList<>(); // Moderator ID who disapproved- used in moderator controller
}
