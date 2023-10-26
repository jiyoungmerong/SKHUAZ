package com.app.skhuaz.service;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.Route;
import com.app.skhuaz.domain.User;
import com.app.skhuaz.exception.ErrorCode;
import com.app.skhuaz.exception.exceptions.BusinessException;
import com.app.skhuaz.repository.PreLectureRepository;
import com.app.skhuaz.repository.RouteRepository;
import com.app.skhuaz.repository.UserRepository;
import com.app.skhuaz.request.RouteSaveRequest;
import com.app.skhuaz.response.AllRoutesResponse;
import com.app.skhuaz.response.RouteDetailResponse;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RouteService {

    private final PreLectureRepository preLectureRepository;

    private final PreLectureService preLectureService;

    private final RouteRepository routeRepository;

    private final UserRepository userRepository;

    @Transactional
    public RspsTemplate<List<PreLecture>> createRoute(RouteSaveRequest request, String email) { // 루트 저장

        List<Long> preLectureIds = request.getPreLectureList();

        List<PreLecture> preLectures = new ArrayList<>();
        for (Long preLectureId : preLectureIds) {
            Optional<PreLecture> preLectureOptional = preLectureRepository.findById(preLectureId);
            preLectureOptional.ifPresent(preLectures::add);
        }

        Route route = Route.builder()
                .title(request.getTitle())
                .recommendation(request.getRecommendation())
                .createAt(LocalDateTime.now())
                .email(email)
                .preLectures(preLectures)  // preLectures 설정
                .build();

        routeRepository.save(route);

        return new RspsTemplate<>(HttpStatus.OK, "루트 저장 성공!!", preLectures);
    }

    public RspsTemplate<RouteDetailResponse> getRouteDetails(Long routeId) { // 상세보기
        Route route = routeRepository.findById(routeId).orElse(null);

        if (route == null) {
            throw new BusinessException(ErrorCode.NOT_EXISTS_ROUTE);
        }

        return new RspsTemplate<>(HttpStatus.OK, routeId + "번 글에 대한 상세보기를 성공적으로 불러왔습니다.",
                RouteDetailResponse.of(route.getTitle(), route.getRecommendation(), route.getPreLectures()));
    }

    @Transactional
    public List<AllRoutesResponse> getAllRoutesWithPreLecturesInReverseOrder() { // 모든 루트 불러오기
        List<Route> routes = routeRepository.findAll();
        List<AllRoutesResponse> routeResponses = new ArrayList<>();

        for (Route route : routes) {
            List<PreLecture> preLectures = preLectureService.getPreLecturesByEmail(route.getEmail());
            Hibernate.initialize(preLectures); // lecNames 필드 초기화

            AllRoutesResponse routeResponse = AllRoutesResponse.of(
                    (long) route.getRouteId(),
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
    public void deleteRouteById(Long routeId, String email) {
        Optional<Route> optionalRoute = routeRepository.findById(routeId);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_JOIN));

        optionalRoute.ifPresent(route -> {
            if (Objects.equals(user.getEmail(), route.getEmail())) {
                routeRepository.delete(route);
            } else {
                throw new BusinessException(ErrorCode.NOT_EXISTS_AUTHORITY);
            }
        });
    }
}
