package com.gabia.gyebalja.service;

import com.gabia.gyebalja.common.exception.NotExistDataException;
import com.gabia.gyebalja.domain.Department;
import com.gabia.gyebalja.domain.User;
import com.gabia.gyebalja.dto.user.UserRequestDto;
import com.gabia.gyebalja.dto.user.UserResponseDto;
import com.gabia.gyebalja.exception.NotExistUserException;
import com.gabia.gyebalja.repository.DepartmentRepository;
import com.gabia.gyebalja.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Author : 정태균
 * Part : All
 */

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    /** 조회 - 사용자 한 건 */
    public UserResponseDto getOneUser(Long id) {
        User findUser = userRepository.findById(id).orElseThrow(() -> new NotExistUserException("존재하지 않는 회원입니다."));

        return new UserResponseDto(findUser);
    }

    /** 저장 - 사용자 한 건 */
    @Transactional
    public Long postOneUser(UserRequestDto userRequestDto) {
        Department findDept = departmentRepository.findById(userRequestDto.getDeptId()).orElseThrow(() -> new NotExistDataException("존재하지 않는 부서입니다."));

        User savedUser = userRepository.save(User.builder()
                .gabiaUserNo(userRequestDto.getGabiaUserNo())
                .email(userRequestDto.getEmail())
                .name(userRequestDto.getName())
                .engName(userRequestDto.getEngName())
                .gender(userRequestDto.getGender())
                .phone(userRequestDto.getPhone())
                .tel(userRequestDto.getTel())
                .positionId(userRequestDto.getPositionId())
                .positionName(userRequestDto.getPositionName())
                .profileImg(userRequestDto.getProfileImg())
                .department(findDept)
                .build());
        return savedUser.getId();
    }

    /** 수정 - 사용자 한 건 */
    @Transactional
    public Long putOneUser(Long id, UserRequestDto userRequestDto ) {
        User findUser = userRepository.findById(id).orElseThrow(() -> new NotExistUserException("존재하지 않는 사용자입니다."));
        Department findDept = departmentRepository.findById(userRequestDto.getDeptId()).orElseThrow(() -> new NotExistDataException("존재하지 않는 부서입니다."));

        // 하이웍스 Api에서 제공받는 값이 달라질수도있기때문에 password 제외 하고 모두 세팅
        findUser.changeUser(userRequestDto.getGabiaUserNo(),
                userRequestDto.getEmail(),
                userRequestDto.getName(),
                userRequestDto.getEngName(),
                userRequestDto.getGender(),
                userRequestDto.getPhone(),
                userRequestDto.getTel(),
                userRequestDto.getPositionId(),
                userRequestDto.getPositionName(),
                userRequestDto.getProfileImg(),
                findDept);

        return id;
    }
}
