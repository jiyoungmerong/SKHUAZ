package com.app.skhuaz.repository;

import com.app.skhuaz.domain.Route;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route, Long> {

    @EntityGraph(attributePaths = "preLectures")
    List<Route> findAll();

}
