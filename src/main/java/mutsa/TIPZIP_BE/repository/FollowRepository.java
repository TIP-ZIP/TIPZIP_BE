package mutsa.TIPZIP_BE.repository;

import mutsa.TIPZIP_BE.entity.Follow;
import mutsa.TIPZIP_BE.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFollowerAndFollowing(MemberEntity follower, MemberEntity following);
    void deleteByFollowerAndFollowing(MemberEntity follower, MemberEntity following);
    /*
    @Query("SELECT f.following FROM Follow f WHERE f.follower = :follower")
    List<MemberEntity> findFollowing(@Param("follower") MemberEntity follower);

    @Query("SELECT f.follower FROM Follow f WHERE f.following = :following")
    List<MemberEntity> findFollowers(@Param("following") MemberEntity following);
    */
    int countByFollower(MemberEntity follower);
    int countByFollowing(MemberEntity following);
}


