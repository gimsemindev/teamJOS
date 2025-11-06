package com.sp.model;

public class MemberDTO {
	private String memberId;

    /** 사용자 이름 */
    private String memberName;

    /** 사용자 권한 (admin, employee 등) */
    private String role;

    // 기본 생성자
    public MemberDTO() {}

    // 모든 필드를 받는 생성자
    public MemberDTO(String memberId, String memberName, String role) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.role = role;
    }

    // Getter / Setter
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // 로그인 정보 요약 출력용
    @Override
    public String toString() {
        return String.format("[ID: %s, 이름: %s, 권한: %s]", memberId, memberName, role);
    }
}

