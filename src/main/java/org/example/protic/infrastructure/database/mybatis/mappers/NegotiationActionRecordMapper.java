package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.NegotiationActionRecord;

import java.util.List;

@Mapper
public interface NegotiationActionRecordMapper {

  List<NegotiationActionRecord> selectByNegotiationId(
      NegotiationActionRecord negotiationActionRecord);

  int insert(NegotiationActionRecord negotiationActionRecord);
}
