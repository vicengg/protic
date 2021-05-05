package org.example.protic.infrastructure.database.mybatis.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.example.protic.infrastructure.database.mybatis.records.NegotiationRecord;

import java.util.List;

@Mapper
public interface NegotiationRecordMapper {

  NegotiationRecord selectById(NegotiationRecord negotiationRecord);

  int insert(NegotiationRecord negotiationRecord);

  int update(NegotiationRecord negotiationRecord);

  List<NegotiationRecord> selectByCreator(NegotiationRecord negotiationRecord);

  List<NegotiationRecord> selectByReceiver(NegotiationRecord negotiationRecord);

  List<NegotiationRecord> selectByOfferedWorkExperience(NegotiationRecord negotiationRecord);

  List<NegotiationRecord> selectByDemandedWorkExperience(NegotiationRecord negotiationRecord);
}
