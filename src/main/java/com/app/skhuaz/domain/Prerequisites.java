package com.app.skhuaz.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name="Prerequisites")
@AllArgsConstructor(access = PROTECTED)
@Builder
public class Prerequisites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preId; // 선수과목 id

    private Long subjectId;

    private Long prerequisiteId;

    public void updateContent(Long subjectId, Long prerequisiteId){
        this.subjectId = subjectId;
        this.prerequisiteId = prerequisiteId;
    }
}
