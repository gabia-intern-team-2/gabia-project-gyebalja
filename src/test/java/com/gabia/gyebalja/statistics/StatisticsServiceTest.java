package com.gabia.gyebalja.statistics;

import com.gabia.gyebalja.domain.Category;
import com.gabia.gyebalja.domain.Department;
import com.gabia.gyebalja.domain.EduTag;
import com.gabia.gyebalja.domain.Education;
import com.gabia.gyebalja.domain.EducationType;
import com.gabia.gyebalja.domain.GenderType;
import com.gabia.gyebalja.domain.Tag;
import com.gabia.gyebalja.domain.User;
import com.gabia.gyebalja.dto.statistics.StatisticsMainMonthResponseDto;
import com.gabia.gyebalja.dto.statistics.StatisticsMainCategoryResponseDto;
import com.gabia.gyebalja.dto.statistics.StatisticsMainTagResponseDto;
import com.gabia.gyebalja.dto.statistics.StatisticsMainYearResponseDto;
import com.gabia.gyebalja.repository.CategoryRepository;
import com.gabia.gyebalja.repository.DepartmentRepository;
import com.gabia.gyebalja.repository.EduTagRepository;
import com.gabia.gyebalja.repository.EducationRepository;
import com.gabia.gyebalja.repository.TagRepository;
import com.gabia.gyebalja.repository.UserRepository;
import com.gabia.gyebalja.service.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class StatisticsServiceTest {

    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private EducationRepository educationRepository;
    @Autowired private EduTagRepository eduTagRepository;
    @Autowired private TagRepository tagRepository;


    @Autowired
    private StatisticsService statisticsService;

    private Department department;
    private User user;
    private Category category;
    private Tag tag;

    @BeforeEach
    public void setUp(){
        departmentRepository.save(this.department);
        userRepository.save(this.user);
        categoryRepository.save(this.category);
        tagRepository.save(this.tag);
    }

    @Autowired
    public StatisticsServiceTest() {
        // Department
        this.department = Department.builder()
                .name("테스트팀")
                .depth(0)
                .parentDepartment(null)
                .build();

        // User
        this.user = User.builder()
                .email("gabiaUser@gabia.com")
                .password("1234")
                .name("가비아")
                .gender(GenderType.MALE)
                .phone("010-2345-5678")
                .tel("02-2345-5678")
                .positionId(5L)
                .positionName("직원")
                .department(this.department)
                .profileImg(null)
                .build();

        // Category
        this.category = Category.builder().name("개발").build();

        // Tag
        this.tag = Tag.builder().name("HTML").build();
    }

    @Test
    @DisplayName("통계(메인) - 연간 건수, 시간 테스트")
    public void getMainStatisticsWithYear(){
        // given
        LocalDate currentDate = LocalDate.now();
        int yearRange = 5;
        int currentYear = currentDate.getYear();
        int baseYear = currentYear - yearRange;
        int hours = 10;
        int totalNumberOfData = 5;
        for(int i = 0; i < totalNumberOfData; i++){
            this.educationRepository.save(Education.builder()
                    .title("테스트 - Mysql 초급 강좌 제목")
                    .content("테스트 - Mysql 초급 강좌 본문")
                    .startDate(currentDate)
                    .endDate(currentDate.plusDays(1))
                    .totalHours(hours)
                    .type(EducationType.ONLINE)
                    .place("테스트 - 인프런 온라인 교육 사이트")
                    .user(this.user)
                    .category(this.category)
                    .build());
        }

        // when
        StatisticsMainYearResponseDto statisticsMainYearResponseDto = statisticsService.getMainStatisticsWithYear();

        // then
        int targetIndex = currentYear - baseYear - 1;
        assertThat(statisticsMainYearResponseDto.getYears()[targetIndex]).isEqualTo(Integer.toString(currentYear));
        assertThat(statisticsMainYearResponseDto.getTotalEducationCount()[targetIndex]).isEqualTo(totalNumberOfData);
        assertThat(statisticsMainYearResponseDto.getTotalEducationTime()[targetIndex]).isEqualTo(hours * totalNumberOfData);
    }

    @Test
    @DisplayName("통계(메인) - 월간 건수, 시간 테스트")
    public void getMainStatisticsWithMonth(){
        // given
        LocalDate currentDate = LocalDate.now();
        int currentMonth = currentDate.getMonthValue();
        int hours = 10;
        int totalNumberOfData = 5;
        for (int i = 0; i < totalNumberOfData; i++) {
            this.educationRepository.save(Education.builder()
                    .title("테스트 - Mysql 초급 강좌 제목")
                    .content("테스트 - Mysql 초급 강좌 본문")
                    .startDate(currentDate)
                    .endDate(currentDate.plusDays(1))
                    .totalHours(hours)
                    .type(EducationType.ONLINE)
                    .place("테스트 - 인프런 온라인 교육 사이트")
                    .user(this.user)
                    .category(this.category)
                    .build());
        }

        // when
        StatisticsMainMonthResponseDto statisticsMainMonthResponseDto = statisticsService.getMainStatisticsWithMonth();

        // then
        int targetIndex = currentMonth - 1;
        assertThat(statisticsMainMonthResponseDto.getMonths()[targetIndex]).isEqualTo(String.format("%02d", currentMonth));
        assertThat(statisticsMainMonthResponseDto.getTotalEducationCount()[targetIndex]).isEqualTo(totalNumberOfData);
        assertThat(statisticsMainMonthResponseDto.getTotalEducationTime()[targetIndex]).isEqualTo(hours * totalNumberOfData);
    }

    @Test
    @DisplayName("통계(메인) - 카테고리 Top n 테스트")
    public void getMainStatisticsWithCategory(){
        // given
        LocalDate currentDate = LocalDate.now();
        int hours = 10;
        int totalNumberOfData = 5;
        for (int i = 0; i < totalNumberOfData; i++) {
            this.educationRepository.save(Education.builder()
                    .title("테스트 - Mysql 초급 강좌 제목")
                    .content("테스트 - Mysql 초급 강좌 본문")
                    .startDate(currentDate)
                    .endDate(currentDate.plusDays(1))
                    .totalHours(hours)
                    .type(EducationType.ONLINE)
                    .place("테스트 - 인프런 온라인 교육 사이트")
                    .user(this.user)
                    .category(this.category)
                    .build());
        }

        // when
        StatisticsMainCategoryResponseDto statisticsMainCategoryResponseDto = statisticsService.getMainStatisticsWithCategory();
        System.out.println(statisticsMainCategoryResponseDto);

        // then
        int targetIndex = statisticsMainCategoryResponseDto.getCategories().indexOf(this.category.getName());
        assertThat(statisticsMainCategoryResponseDto.getCategories().get(targetIndex)).isEqualTo(this.category.getName());
        assertThat(statisticsMainCategoryResponseDto.getTotalCategoryCount().get(targetIndex)).isEqualTo(totalNumberOfData);
    }

    @Test
    @DisplayName("통계(메인) - 태그 Top n 테스트")
    public void getMainStatisticsWithTag(){
        // given
        LocalDate currentDate = LocalDate.now();
        int hours = 10;
        int totalNumberOfData = 5;
        Education education;
        for (int i = 0; i < totalNumberOfData; i++) {
            education = Education.builder()
                    .title("테스트 - Mysql 초급 강좌 제목")
                    .content("테스트 - Mysql 초급 강좌 본문")
                    .startDate(currentDate)
                    .endDate(currentDate.plusDays(1))
                    .totalHours(hours)
                    .type(EducationType.ONLINE)
                    .place("테스트 - 인프런 온라인 교육 사이트")
                    .user(this.user)
                    .category(this.category)
                    .build();
            this.educationRepository.save(education);
            this.eduTagRepository.save(EduTag.builder().education(education).tag(this.tag).build());
        }

        // when
        StatisticsMainTagResponseDto statisticsMainTagResponseDto = statisticsService.getMainStatisticsWithTag();
        System.out.println(statisticsMainTagResponseDto);

        // then
        int targetIndex = statisticsMainTagResponseDto.getNames().indexOf(this.tag.getName());
        assertThat(statisticsMainTagResponseDto.getNames().get(targetIndex)).isEqualTo(this.tag.getName());
        assertThat(statisticsMainTagResponseDto.getTotalTagCount().get(targetIndex)).isEqualTo(totalNumberOfData);
    }
}
