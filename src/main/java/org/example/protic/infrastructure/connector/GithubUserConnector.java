package org.example.protic.infrastructure.connector;

import org.example.protic.domain.user.User;
import org.example.protic.infrastructure.database.mybatis.mappers.UserCacheRecordMapper;
import org.example.protic.infrastructure.database.mybatis.records.UserCacheRecord;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.Objects;
import java.util.Optional;

public class GithubUserConnector implements UserConnector {

  private static final RestTemplate REST_TEMPLATE = new RestTemplate();
  private static final String URL = "https://api.github.com/user/{0}";
  private final UserCacheRecordMapper userCacheRecordMapper;

  public GithubUserConnector(UserCacheRecordMapper userCacheRecordMapper) {
    this.userCacheRecordMapper = userCacheRecordMapper;
  }

  @Override
  public User findUserById(String id) {
    return lookInCache(id).orElseGet(() -> persistInCache(getFromGithub(id)));
  }

  private User persistInCache(User user) {
    UserCacheRecord userCacheRecord = new UserCacheRecord();
    userCacheRecord.userId = user.getId();
    userCacheRecord.login = user.getLogin();
    userCacheRecord.avatarUrl = user.getAvatarUrl().orElse(null);
    checkOneModification(userCacheRecordMapper.insert(userCacheRecord));
    return user;
  }

  private static User getFromGithub(String id) {
    ResponseEntity<GithubUserDto> responseEntity =
        REST_TEMPLATE.getForEntity(MessageFormat.format(URL, id), GithubUserDto.class);
    checkStatusCode(responseEntity);
    return mapToUser(
        Objects.requireNonNull(responseEntity.getBody(), "Null Github response body."));
  }

  private Optional<User> lookInCache(String id) {
    UserCacheRecord userCacheRecord = new UserCacheRecord();
    userCacheRecord.userId = id;
    return Optional.ofNullable(userCacheRecordMapper.selectById(userCacheRecord))
        .map(GithubUserConnector::mapToUser);
  }

  private static User mapToUser(UserCacheRecord userCacheRecord) {
    return User.of(userCacheRecord.userId, userCacheRecord.login, userCacheRecord.avatarUrl);
  }

  private static User mapToUser(GithubUserDto githubUserDto) {
    return User.of(githubUserDto.id, githubUserDto.login, githubUserDto.avatarUrl);
  }

  private static void checkStatusCode(ResponseEntity<GithubUserDto> responseEntity) {
    if (!responseEntity.getStatusCode().is2xxSuccessful()) {
      // TODO: Change this.
      throw new RuntimeException("Error calling Github.");
    }
  }

  private static void checkOneModification(long insertedRecords) {
    if (insertedRecords != 1) {
      // TODO: Change this.
      throw new RuntimeException("Insertion failed.");
    }
  }
}
