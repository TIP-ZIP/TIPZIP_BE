package mutsa.TIPZIP_BE.dto;

import mutsa.TIPZIP_BE.entity.MemberEntity;
import mutsa.TIPZIP_BE.entity.Scrap;

public record MemberResponseDTO(Long userId, String username, Boolean badge) {

    public MemberResponseDTO(MemberEntity member) {
        this(member.getUserId(), member.getUsername(), member.getBadge());
    }
}
