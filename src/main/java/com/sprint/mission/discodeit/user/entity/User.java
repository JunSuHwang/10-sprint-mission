package com.sprint.mission.discodeit.user.entity;

import com.sprint.mission.discodeit.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.binarycontent.entity.BinaryContent;
import com.sprint.mission.discodeit.userstatus.entity.UserStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User extends BaseUpdatableEntity {

  @Column(unique = true, nullable = false, length = 50)
  private String username;

  @Column(nullable = false, length = 60)
  private String password;

  @Column(unique = true, nullable = false, length = 100)
  private String email;

  @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
  @JoinColumn(name = "profile_id", unique = true)
  private BinaryContent profile;

  @Setter
  @OneToOne(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private UserStatus userStatus;

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public void updateUserName(String username) {
    this.username = username;
    this.updatedAt = Instant.now();
  }

  public void updatePassword(String password) {
    this.password = password;
    this.updatedAt = Instant.now();
  }

  public void updateEmail(String email) {
    this.email = email;
    this.updatedAt = Instant.now();
  }

  public boolean isProfileImageUploaded() {
    return profile != null;
  }
}
