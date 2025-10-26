package iuh.fit.MusicStreamAppBackEnd.mapper;

import iuh.fit.MusicStreamAppBackEnd.dto.*;
import iuh.fit.MusicStreamAppBackEnd.entity.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ModelMapper {

    // == USER ==
    public static UserDTO toUserDTO(User user) {
        if (user == null) return null;
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setVerified(user.isVerified());
        return dto;
    }

    public static UserProfileDTO toUserProfileDTO(User user, int trackCount) {
        if (user == null) return null;
        UserProfileDTO dto = new UserProfileDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail()); // Cần kiểm tra quyền trước khi gán
        dto.setProfilePictureUrl(user.getProfilePictureUrl());
        dto.setBio(user.getBio());
        dto.setVerified(user.isVerified());
        dto.setCreatedAt(user.getCreatedAt());
        // Lấy số lượng từ các set
        dto.setFollowersCount(user.getFollowers() != null ? user.getFollowers().size() : 0);
        dto.setFollowingCount(user.getFollowing() != null ? user.getFollowing().size() : 0);
        dto.setLikedTracksCount(user.getLikedTracks() != null ? user.getLikedTracks().size() : 0);
        dto.setTrackCount(trackCount); // Truyền từ service vào
        return dto;
    }

    // == TRACK & ALBUM ==
    public static AlbumDTO toAlbumDTO(Album album) {
        if (album == null) return null;
        AlbumDTO dto = new AlbumDTO();
        dto.setAlbumId(album.getAlbumId());
        dto.setTitle(album.getTitle());
        dto.setCoverArtUrl(album.getCoverArtUrl());
        dto.setReleaseDate(album.getReleaseDate());
        dto.setCreator(toUserDTO(album.getCreator()));
        // Tránh vòng lặp vô hạn, chỉ set tracks nếu cần
        dto.setTracks(Collections.emptySet()); // Mặc định không set
        return dto;
    }

    public static TrackDTO toTrackDTO(Track track) {
        if (track == null) return null;
        TrackDTO dto = new TrackDTO();
        dto.setTrackId(track.getTrackId());
        dto.setTitle(track.getTitle());
        dto.setDuration(track.getDuration());
        dto.setPlayCount(track.getPlayCount());
        dto.setAudioFileUrl(track.getAudioFileUrl());
        dto.setReleaseDate(track.getReleaseDate());
        dto.setCreator(toUserDTO(track.getCreator()));
        // Tránh vòng lặp: Chỉ map album cơ bản, không map ngược lại track
        if (track.getAlbum() != null) {
            AlbumDTO albumDTO = new AlbumDTO();
            albumDTO.setAlbumId(track.getAlbum().getAlbumId());
            albumDTO.setTitle(track.getAlbum().getTitle());
            albumDTO.setCoverArtUrl(track.getAlbum().getCoverArtUrl());
            dto.setAlbum(albumDTO);
        }
        return dto;
    }

    // == PLAYLIST ==
    public static PlaylistDTO toPlaylistDTO(Playlist playlist) {
        if (playlist == null) return null;
        PlaylistDTO dto = new PlaylistDTO();
        dto.setPlaylistId(playlist.getPlaylistId());
        dto.setName(playlist.getName());
        dto.setUser(toUserDTO(playlist.getUser()));
        // Map các bài hát trong playlist theo đúng thứ tự
        if (playlist.getTracks() != null) {
            List<TrackDTO> trackDTOs = playlist.getTracks().stream()
                    .map(PlaylistTrack::getTrack) // Lấy Track từ PlaylistTrack
                    .map(ModelMapper::toTrackDTO) // Chuyển Track sang TrackDTO
                    .collect(Collectors.toList());
            dto.setTracks(trackDTOs);
        } else {
            dto.setTracks(Collections.emptyList());
        }
        return dto;
    }

    // == POST & COMMENT ==
    public static CommentDTO toCommentDTO(Comment comment) {
        if (comment == null) return null;
        CommentDTO dto = new CommentDTO();
        dto.setCommentId(comment.getCommentId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setUser(toUserDTO(comment.getUser()));
        if(comment.getParentComment() != null) {
            dto.setParentCommentId(comment.getParentComment().getCommentId());
        }
        // Replies sẽ được tải riêng để tránh query N+1
        dto.setReplies(Collections.emptyList());
        return dto;
    }

    public static PostDTO toPostDTO(Post post, User currentUser) {
        if (post == null) return null;
        PostDTO dto = new PostDTO();
        dto.setPostId(post.getPostId());
        dto.setPostType(post.getPostType());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUser(toUserDTO(post.getUser()));

        // Nội dung post
        if (post.getTrack() != null) {
            dto.setTrack(toTrackDTO(post.getTrack()));
        }
        if (post.getOriginalPost() != null) {
            // Tránh vòng lặp, chỉ map original post 1 cấp
            dto.setOriginalPost(toPostDTO(post.getOriginalPost(), currentUser));
        }

        // Thống kê (tạm thời, nên tối ưu sau)
        // Đây là ví dụ, tốt nhất nên dùng projection query
        dto.setLikesCount(post.getLikedByUsers() != null ? post.getLikedByUsers().size() : 0);
        dto.setCommentsCount(post.getComments() != null ? post.getComments().size() : 0);
        dto.setRepostsCount(post.getReposts() != null ? post.getReposts().size() : 0);

        // Kiểm tra xem user hiện tại đã like post này chưa
        dto.setLikedByCurrentUser(
                currentUser != null &&
                        post.getLikedByUsers() != null &&
                        post.getLikedByUsers().stream().anyMatch(u -> u.getUserId().equals(currentUser.getUserId()))
        );

        return dto;
    }

    public static SubscriptionPlanDTO toSubscriptionPlanDTO(SubscriptionPlan plan) {
        if (plan == null) return null;
        SubscriptionPlanDTO dto = new SubscriptionPlanDTO();
        dto.setPlanId(plan.getPlanId());
        dto.setName(plan.getName());
        dto.setPrice(plan.getPrice());

        // Chuyển đổi Enum billingCycle sang String
        if (plan.getBillingCycle() != null) {
            dto.setBillingCycle(plan.getBillingCycle().name());
        }
        return dto;
    }

    public static SubscriptionDTO toSubscriptionDTO(Subscription subscription) {
        if (subscription == null) return null;
        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setSubscriptionId(subscription.getSubscriptionId());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setStatus(subscription.getStatus());

        // Gắn thông tin gói cước (plan) vào DTO
        dto.setPlan(toSubscriptionPlanDTO(subscription.getPlan()));

        return dto;
    }
}