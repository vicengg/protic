package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.CompanyRecord;

@Mapper
public interface CompanyRecordMapper {

  CompanyRecord selectById(CompanyRecord companyRecord);

  CompanyRecord selectByNameValue(CompanyRecord companyRecord);

  int insert(CompanyRecord companyRecord);
}
