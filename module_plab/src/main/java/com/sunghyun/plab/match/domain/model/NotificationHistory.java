package com.sunghyun.plab.match.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Table(name="notification_history")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNo;

    @Column
    private Long subscriptionNo;
}
