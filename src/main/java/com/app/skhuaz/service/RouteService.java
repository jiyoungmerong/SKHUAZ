package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.Route;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.PreLectureRepository;
import com.app.skhuaz.repository.RouteRepository;
import com.app.skhuaz.request.RouteSaveRequest;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.response.RouteDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RouteService {

    private final PreLectureRepository preLectureRepository;

    private final PreLectureService preLectureService;

    private final RouteRepository routeRepository;

    @Transactional
    public RspsTemplate<List<PreLecture>> createRoute(RouteSaveRequest request, String email) {
        Route route = Route.builder()
                .title(request.getTitle())
                .recommendation(request.getRecommendation())
                .createAt(LocalDateTime.now())
                .email(email)
                .build();

        List<Long> preLectureIds = request.getPreLectureList();

        List<PreLecture> preLectures = new ArrayList<>();
        for (Long preLectureId : preLectureIds) {
            Optional<PreLecture> preLectureOptional = preLectureRepository.findById(preLectureId);
            preLectureOptional.ifPresent(preLecture -> preLectures.add(preLecture));
        }

        route.setPreLectures(preLectures);


        routeRepository.save(route);
        return new RspsTemplate<>(HttpStatus.OK, "루트 저장 성공!!", preLectures);
    }

    public RspsTemplate<RouteDetailResponse> getRouteDetails(Long routeId) {
        // 먼저 주어진 routeId로 루트 글을 조회합니다.
        Route route = routeRepository.findById(routeId).orElse(null);

        if (route == null) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_ROUTE);
        }

        return new RspsTemplate<>(HttpStatus.OK, routeId + "번 글에 대한 상세보기를 성공적으로 불러왔습니다.",
                RouteDetailResponse.of(route.getTitle(), route.getRecommendation(), route.getPreLectures()));
    }

    public List<Route> getAllRoutes() {
        return routeRepository.findAll();
    }
}
