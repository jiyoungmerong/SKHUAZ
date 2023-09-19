package com.app.skhuaz.service;

import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.Route;
import com.app.skhuaz.repository.PreLectureRepository;
import com.app.skhuaz.repository.RouteRepository;
import com.app.skhuaz.request.RouteSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RouteService {

    private final PreLectureRepository preLectureRepository;

    private final RouteRepository routeRepository;

    @Transactional
    public void createRouteWithPreLecture(RouteSaveRequest request, String userEmail) {
        // 사용자의 이메일로 해당 선수 과목을 조회
        List<PreLecture> userPreLecture = preLectureRepository.findByEmail(userEmail);

        Route route = Route.builder()
                .title(request.getTitle())
                .recommendation(request.getRecommendation())
                .createAt(LocalDateTime.now())
                .email(userEmail)
                .preLectures(userPreLecture)
                .build();

        routeRepository.save(route);
    }

//    @Transactional
//    public void createRouteWithPreLecture(RouteSaveRequest request, String userEmail) {
//        // 사용자의 이메일로 해당 선수 과목을 조회
//        List<Long> prelectureIds = new ArrayList<>();
//        for (PreLecture prelecture : userPrelectures) {
//            prelectureIds.add(prelecture.getPreLectureId());
//        }
//
//        Route route = Route.builder()
//                .title(request.getTitle())
//                .recommendation(request.getRecommendation())
//                .createAt(LocalDateTime.now())
//                .email(userEmail)
//                .prelectureIds(prelectureIds) // 수정: PreLecture 아이디 리스트 전달
//                .build();
//
//        routeRepository.save(route);
//    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }

}
