package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.WorkExperienceFilterRecord;
import org.example.protic.infrastructure.database.mybatis.records.WorkExperienceRecord;

import java.util.List;

@Mapper
public interface WorkExperienceRecordMapper {

  WorkExperienceRecord selectById(WorkExperienceRecord workExperienceRecord);

  int insert(WorkExperienceRecord workExperienceRecord);

  List<WorkExperienceRecord> select(WorkExperienceFilterRecord workExperienceFilterRecord);
}
