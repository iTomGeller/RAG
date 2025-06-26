package com.cyberlanting.qwen_rag.mapper;

import com.cyberlanting.qwen_rag.pojo.entity.File;
import com.cyberlanting.qwen_rag.pojo.vo.FileVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FileMapper {
    void insert(File file);

    Integer getTotal(Long userId, Long knowledgeBaseId);

    List<FileVO> getFileList(Long knowledgeBaseId, Integer offset, Integer pageSize);

    File getFileById(Long id);

    void delete(Long id);
}
