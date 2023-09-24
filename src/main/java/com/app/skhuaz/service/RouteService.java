package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.Route;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.PreLectureRepository;
import com.app.skhuaz.repository.RouteRepository;
import com.app.skhuaz.request.RouteSaveRequest;
import com.app.skhuaz.response.AllRoutesResponse;
import com.app.skhuaz.response.JoinResponse;
import com.app.skhuaz.response.RouteDetailResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.*;

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
        try{
            routeRepository.save(route);
        }catch (Exception e) { // 서버 오류
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR);
        }

        return new RspsTemplate<>(HttpStatus.OK, "루트 저장 성공!!", preLectures);
    }

    public RspsTemplate<RouteDetailResponse> getRouteDetails(Long routeId) {
        Route route = routeRepository.findById(routeId).orElse(null);

        if (route == null) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_ROUTE);
        }

        return new RspsTemplate<>(HttpStatus.OK, routeId + "번 글에 대한 상세보기를 성공적으로 불러왔습니다.",
                RouteDetailResponse.of(route.getTitle(), route.getRecommendation(), route.getPreLectures()));
    }

    @Transactional
    public List<AllRoutesResponse> getAllRoutesWithPreLecturesInReverseOrder() {
        List<Route> routes = routeRepository.findAll();
        List<AllRoutesResponse> routeResponses = new ArrayList<>();

        for (Route route : routes) {
            List<PreLecture> preLectures = preLectureService.getPreLecturesByEmail(route.getEmail());
            Hibernate.initialize(preLectures); // lecNames 필드 초기화

            AllRoutesResponse routeResponse = AllRoutesResponse.of(
                    route.getTitle(),
                    route.getRecommendation(),
                    route.getCreateAt(),
                    route.getEmail(),
                    preLectures
            );

            routeResponses.add(routeResponse);
        }

        // 리스트를 역순으로 정렬
        Collections.reverse(routeResponses);

        return routeResponses;
    }

    @Transactional
    public void deleteRouteById(Long routeId) {
        Optional<Route> optionalRoute = routeRepository.findById(routeId);
        if (optionalRoute.isPresent()) {
            Route route = optionalRoute.get();
            routeRepository.delete(route);
        } else {
            // 해당 ID의 루트평을 찾을 수 없는 경우 예외 처리 또는 에러 처리를 수행할 수 있습니다.
            throw new BusinessException(ErrorCode.NOT_EXISTS_ROUTE);
        }
    }
}