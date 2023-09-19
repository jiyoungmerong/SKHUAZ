package com.app.skhuaz.controller;

import com.app.skhuaz.domain.Route;
import com.app.skhuaz.request.RouteSaveRequest;
import com.app.skhuaz.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
public class RouteController {
    private final RouteService routeService;

    @PostMapping("/create")
    public ResponseEntity<String> createRoute(@RequestBody RouteSaveRequest request, Principal principal) {
        // 현재 로그인한 사용자의 이메일 주소를 가져옴
        String userEmail = principal.getName();

        // 서비스 메서드를 호출하여 루트 글을 생성
        routeService.createRouteWithPreLecture(request, userEmail);

        return ResponseEntity.ok("Route created successfully.");
    }

    @GetMapping("/all")
    public ResponseEntity<List<Route>> getAllRoutes() {
        List<Route> routes = routeService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }
}
