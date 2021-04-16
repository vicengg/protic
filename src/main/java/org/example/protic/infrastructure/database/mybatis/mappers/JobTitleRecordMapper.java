package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.JobTitleRecord;
import org.example.protic.infrastructure.database.mybatis.records.TextRecord;

import java.util.List;

@Mapper
public interface JobTitleRecordMapper {

  JobTitleRecord selectById(JobTitleRecord jobTitleRecord);

  JobTitleRecord selectByNameValue(JobTitleRecord jobTitleRecord);

  List<JobTitleRecord> selectByNameValueContaining(TextRecord textRecord);

  int insert(JobTitleRecord jobTitleRecord);
}
