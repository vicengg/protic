package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.NegotiationRecord;

@Mapper
public interface NegotiationRecordMapper {

  NegotiationRecord selectById(NegotiationRecord negotiationRecord);

  int insert(NegotiationRecord negotiationRecord);

  int update(NegotiationRecord negotiationRecord);
}
