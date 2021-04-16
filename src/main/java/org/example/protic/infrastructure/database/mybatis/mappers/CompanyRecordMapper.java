package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.CompanyRecord;
import org.example.protic.infrastructure.database.mybatis.records.TextRecord;

import java.util.List;

@Mapper
public interface CompanyRecordMapper {

  CompanyRecord selectById(CompanyRecord companyRecord);

  CompanyRecord selectByNameValue(CompanyRecord companyRecord);

  List<CompanyRecord> selectByNameValueContaining(TextRecord textRecord);

  int insert(CompanyRecord companyRecord);
}
