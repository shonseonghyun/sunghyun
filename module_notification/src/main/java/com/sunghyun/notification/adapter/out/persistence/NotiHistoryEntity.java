package com.sunghyun.notification.adapter.out.persistence;

import com.sunghyun.notification.domain.model.NotiHistory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Table(name = "noti_history")
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotiHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyNo;

    @Column
    private Long memberNo;

    @Column
    private String email;

    @Column
    private String subject;

    @Lob
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column
    private String sendDt;

    @Column
    private String sendTm;

    public static NotiHistoryEntity from(NotiHistory notiHistory){
        return NotiHistoryEntity.builder()
                .memberNo(notiHistory.getMemberNo())
                .email(notiHistory.getEmail())
                .subject(notiHistory.getSubject())
                .content(notiHistory.getContent())
                .sendDt(notiHistory.getSendDt())
                .sendTm(notiHistory.getSendTm())
                .build();
    }

    public NotiHistory toDomain() {
        return NotiHistory.builder()
                .historyNo(this.historyNo)
                .memberNo(this.memberNo)
                .email(this.email)
                .subject(this.subject)
                .content(this.content)
                .sendDt(this.sendDt)
                .sendTm(this.sendTm)
                .build();
    }
}
