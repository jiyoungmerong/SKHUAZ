package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.Route;
import com.app.skhuaz.request.RouteSaveRequest;
import com.app.skhuaz.response.PreLectureResponse;
import com.app.skhuaz.response.RouteDetailResponse;
import com.app.skhuaz.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
public class RouteController {
    private final RouteService routeService;

    @PostMapping("/create")
    public RspsTemplate<List<PreLecture>> createRoute(@RequestBody RouteSaveRequest request, Principal principal) {
        return routeService.createRoute(request, principal.getName());
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<RouteDetailResponse>> getAllRoutes() {
//        List<Route> routes = routeService.getAllRoutes();
//        List<RouteDetailResponse> routeDetailsList = new ArrayList<>();
//
//        for (Route route : routes) {
//            List<PreLecture> preLectures = route.getPreLectures();
//            List<PreLectureResponse> preLectureResponses = preLectures.stream()
//                    .map(PreLectureResponse::of)
//                    .collect(Collectors.toList());
//
//            RouteDetailResponse routeDetails = RouteDetailResponse(route, preLectureResponses);
//
//            routeDetailsList.add(routeDetails);
//        }
//        return ResponseEntity.ok(routeDetailsList);
//    }

    @GetMapping("/details/{routeId}") // 상세조회
    public RspsTemplate<RouteDetailResponse> getRouteDetails(@PathVariable Long routeId) {
        return routeService.getRouteDetails(routeId);
    }

//    @DeleteMapping("/delete/{routeId}")
//    public ResponseEntity<String> deleteRoute(@PathVariable Long routeId) {
//        // 서비스 메서드를 호출하여 루트 글 삭제
//        routeService.deleteRoute(routeId);
//
//        return ResponseEntity.ok("Route deleted successfully.");
//    }
}
