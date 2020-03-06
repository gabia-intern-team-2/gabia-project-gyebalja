package com.gabia.gyebalja.controller;

import com.gabia.gyebalja.common.CommonJsonFormat;
import com.gabia.gyebalja.common.StatusCode;
import com.gabia.gyebalja.dto.board.BoardAllResponseDto;
import com.gabia.gyebalja.dto.board.BoardRequestDto;
import com.gabia.gyebalja.dto.board.BoardDetailResponseDto;
import com.gabia.gyebalja.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BoardApiController {

    private final BoardService boardService;

    /** 등록 - board 한 건 (게시글 등록) */
    @PostMapping("/api/v1/boards")
    public CommonJsonFormat postOneBoard(@RequestBody BoardRequestDto boardRequestDto){
        Long response = boardService.postOneBoard(boardRequestDto);

        return new CommonJsonFormat(StatusCode.OK.getCode(), StatusCode.OK.getMessage(), response);
    }

    /** 조회 - board 한 건 (상세페이지) */
    @GetMapping("/api/v1/boards/{boardId}")
    public CommonJsonFormat getOneBoard(@PathVariable("boardId") Long boardId) {
        BoardDetailResponseDto response = boardService.getOneBoard(boardId);

        return new CommonJsonFormat(StatusCode.OK.getCode(), StatusCode.OK.getMessage(), response);
    }

    /** 수정 - board 한 건 (상세페이지에서) */
    @PutMapping("/api/v1/boards/{boardId}")
    public CommonJsonFormat putOneBoard(@PathVariable("boardId") Long boardId, @RequestBody BoardRequestDto boardRequestDto){
        Long response = boardService.putOneBoard(boardId, boardRequestDto);

        return new CommonJsonFormat(StatusCode.OK.getCode(), StatusCode.OK.getMessage(), response);
    }

    /** 삭제 - board 한 건 (상세페이지에서) */
    @DeleteMapping("/api/v1/boards/{boardId}")
    public CommonJsonFormat deleteOneBoard(@PathVariable("boardId") Long boardId){
        Long response = boardService.deleteOneBoard(boardId);

        return new CommonJsonFormat(StatusCode.OK.getCode(), StatusCode.OK.getMessage(), response);
    }

    /** 조회 - board 전체 (페이징) */
    @GetMapping("/api/v1/boards")
    public CommonJsonFormat getAllBoard(@PageableDefault(size=100, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        // Example - http://localhost:8080/api/v1/boards?page=0&size=4&sort=id,desc
        Page<BoardAllResponseDto> response = boardService.getAllBoard(pageable);

        return new CommonJsonFormat(StatusCode.OK.getCode(), StatusCode.OK.getMessage(), response);
    }
}
