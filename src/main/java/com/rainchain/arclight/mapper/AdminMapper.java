package com.rainchain.arclight.mapper;

import com.rainchain.arclight.entity.FrequencyWarning;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface AdminMapper {
    void addFrequencyWarning(FrequencyWarning frequencyWarning);
}
