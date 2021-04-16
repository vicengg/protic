package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.JobTitleRecord;
import org.example.protic.infrastructure.database.mybatis.records.TechnologyRecord;
import org.example.protic.infrastructure.database.mybatis.records.TextRecord;

import java.util.List;

@Mapper
public interface TechnologyRecordMapper {

  TechnologyRecord selectById(TechnologyRecord technologyRecord);

  TechnologyRecord selectByNameValue(TechnologyRecord technologyRecord);

  List<TechnologyRecord> selectByNameValueContaining(TextRecord textRecord);

  int insert(TechnologyRecord technologyRecord);
}
