package me.zoey.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    /**
     * 사용자 이름 변경
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override //권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override //사용자의 id를 반환(고유한 값)
    public String getUsername() {
        return email;
    }

    @Override //사용자의 패스워드 반환
    public String getPassword() {
        return password; //비밀번호는 암호화하여 저장
    }

    @Override //계정 만료 여부 반환
    public boolean isAccountNonExpired() {
        //TODO: 만료되었는지 확인하는 로직
        return true; //true: 만료되지 않았음
    }

    @Override //계정 잠금 여부 반환
    public boolean isAccountNonLocked() {
        //TODO: 계정 잠금되었는지 확인하는 로직
        return true; //true: 잠금되지 않았음
    }

    @Override //패스워드의 만료 여부 반환
    public boolean isCredentialsNonExpired() {
        //TODO: 패스워드가 만료되었는지 확인하는 로직
        return true; //true: 만료되지 않았음
    }

    @Override //계정 사용 가능 여부 반환
    public boolean isEnabled() {
        //TODO: 계정이 사용 가능한지 확인하는 로직
        return true; //true: 사용 가능
    }

}
