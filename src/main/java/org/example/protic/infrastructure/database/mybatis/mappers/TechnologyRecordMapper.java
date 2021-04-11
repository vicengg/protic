package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.TechnologyRecord;

@Mapper
public interface TechnologyRecordMapper {

  TechnologyRecord selectById(TechnologyRecord technologyRecord);

  TechnologyRecord selectByNameValue(TechnologyRecord technologyRecord);

  int insert(TechnologyRecord technologyRecord);
}
