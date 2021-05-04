package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.UserCacheRecord;

@Mapper
public interface UserCacheRecordMapper {

  UserCacheRecord selectById(UserCacheRecord userCacheRecord);

  int insert(UserCacheRecord userCacheRecord);
}
