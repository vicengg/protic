package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.JobTitleRecord;

@Mapper
public interface JobTitleRecordMapper {

  JobTitleRecord selectById(JobTitleRecord jobTitleRecord);

  JobTitleRecord selectByNameValue(JobTitleRecord jobTitleRecord);

  int insert(JobTitleRecord jobTitleRecord);
}
