package com.gabia.gyebalja.service;

import com.gabia.gyebalja.domain.Comment;
import com.gabia.gyebalja.dto.CommentDto;
import com.gabia.gyebalja.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    /** 등록 - comment 한 건 (댓글 등록) */
    @Transactional
    public Long save(CommentDto commentDto){
        System.out.println("SERVICE START");
        Long commentId = commentRepository.save(commentDto.toEntity()).getId();
        System.out.println("SERVICE END");
        return commentId;
    }

    /** 조회 - comment 한 건 (어디서 사용할 지 모르지만) */
    @Transactional
    public CommentDto findById(Long id){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));

        return new CommentDto(comment);
    }

    /** 수정 - comment 한 건 */
    @Transactional
    public Long update(Long id, CommentDto commentDto){
        Comment comment = commentRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
        // 더티 체킹
        comment.changeContent(commentDto.getContent());

        return id;
    }

    /** 삭제 - comment 한 건 */
    @Transactional
    public Long delete(Long id){
        commentRepository.deleteById(id);

        return id;
    }
}
