package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.WorkExperienceTechnologyRecord;

import java.util.Set;

@Mapper
public interface WorkExperienceTechnologyRecordMapper {

  Set<WorkExperienceTechnologyRecord> selectByWorkExperienceId(
      WorkExperienceTechnologyRecord workExperienceTechnologyRecord);

  int insert(WorkExperienceTechnologyRecord workExperienceTechnologyRecord);

  int deleteByWorkExperienceId(WorkExperienceTechnologyRecord workExperienceTechnologyRecord);
}
