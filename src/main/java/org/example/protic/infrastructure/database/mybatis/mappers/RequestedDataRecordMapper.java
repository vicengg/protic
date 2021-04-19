package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.RequestedDataRecord;

@Mapper
public interface RequestedDataRecordMapper {

  RequestedDataRecord selectById(RequestedDataRecord record);

  int insert(RequestedDataRecord record);

  int update(RequestedDataRecord record);
}
