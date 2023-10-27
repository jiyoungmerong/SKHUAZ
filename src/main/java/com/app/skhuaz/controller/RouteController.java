package com.app.skhuaz.controller;

import com.app.skhuaz.common.RspsTemplate;
import com.app.skhuaz.domain.PreLecture;
import com.app.skhuaz.domain.Route;
import com.app.skhuaz.request.RouteEditRequest;
import com.app.skhuaz.request.RouteSaveRequest;
import com.app.skhuaz.response.AllRoutesResponse;
import com.app.skhuaz.response.RouteDetailResponse;
import com.app.skhuaz.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RouteController {
    private final RouteService routeService;

    @PostMapping("/route/create") // 루트평 저장
    public RspsTemplate<List<PreLecture>> createRoute(@RequestBody RouteSaveRequest request, Principal principal) {
        return routeService.createRoute(request, principal.getName());
    }

    @GetMapping("/route/details/{routeId}") // 상세조회
    public RspsTemplate<RouteDetailResponse> getRouteDetails(@PathVariable Long routeId) {
        return routeService.getRouteDetails(routeId);
    }

    @GetMapping("/route/AllRoute")
    public RspsTemplate<List<AllRoutesResponse>> getAllRoutesWithPreLectures() {
        List<AllRoutesResponse> routesWithPreLectures = routeService.getAllRoutesWithPreLecturesInReverseOrder();
        return new RspsTemplate<>(HttpStatus.OK, "모든 루트평을 성공적으로 불러왔습니다.", routesWithPreLectures);
    }

    @GetMapping("/route/MyRoutes")
    public RspsTemplate<List<AllRoutesResponse>> getMyRoutesWithPreLectures(Principal principal) {
        List<AllRoutesResponse> myRoutesWithPreLectures = routeService.getRoutesByUserEmailWithPreLectures(principal.getName());
        return new RspsTemplate<>(HttpStatus.OK, "내가 작성한 루트평을 성공적으로 불러왔습니다.", myRoutesWithPreLectures);
    }

    @PutMapping("/route/edit/{routeId}")
    public RspsTemplate<Route> updateEvaluation(
            @PathVariable Long routeId,
            @RequestBody @Valid RouteEditRequest request, Principal principal) {

        return routeService.updateRoute(routeId, request, principal.getName());
    }


    @DeleteMapping("/route/delete/{routeId}")
    public RspsTemplate<Void> deleteRoute(@PathVariable Long routeId, Principal principal) {
        routeService.deleteRouteById(routeId, principal.getName());
        return new RspsTemplate<>(HttpStatus.OK, "루트평을 성공적으로 삭제하였습니다.");
    }
}