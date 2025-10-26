package iuh.fit.MusicStreamAppBackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "postId")
@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_type")
    private PostType postType;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "track_id")
    private Track track;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_post_id")
    private Post originalPost;


    /**
     * Ánh xạ tới trường 'post' trong Entity Comment.
     */
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comment> comments = new HashSet<>();

    /**
     * Ánh xạ tới trường 'likedPosts' trong Entity User.
     */
    @ManyToMany(mappedBy = "likedPosts")
    private Set<User> likedByUsers = new HashSet<>();

    /**
     * Ánh xạ tới trường 'originalPost' trong chính Entity Post này.
     */
    @OneToMany(mappedBy = "originalPost", cascade = CascadeType.ALL)
    private Set<Post> reposts = new HashSet<>();

    public enum PostType {
        NEW_TRACK, REPOST
    }
}