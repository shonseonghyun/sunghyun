package com.sunghyun.plab.subscription.domain.enums;

import com.sunghyun.message.Message;
import com.sunghyun.plab.subscription.domain.model.PlabMatchData;

public enum PlabNotiMessage implements Message<PlabMatchData> {
    PLAYER_COUNT{
        public String getSubject(final PlabMatchData item) {
            final String subject = String.format(
                    "[알림] %s 매치 인원 %s 충족",
                    item.getStadiumName(),
                    item.getPlayerCnt().getDesc()
            );

            return subject;
        }

        public String getContent(final PlabMatchData item) {
            String matchUrl = "https://www.plabfootball.com/match/" + item.getPlabMatchNo() + "/";
            String msg = "";
            msg += "<div style='margin:20px; font-family: sans-serif;'>";
            msg += "    <div style='background-color: #f8f9fa; padding: 20px; border-radius: 10px; border: 1px solid #dee2e6;'>";
            msg += "       <h2 style='color: #2c3e50; margin-top: 0;'>[인원 충족 알림]</h2>";
            msg += "        <p>설정하신 매치의 인원 조건이 충족되었습니다.</p>";
            msg += "        <hr style='border: 0; border-top: 1px solid #eee;'>";
            msg += "        <p><b>📍 경기장:</b> " + item.getStadiumName() + "</p>";
            msg += "        <p><b>👥 현재 인원:</b> <span style='color: #e74c3c; font-size: 1.1em;'>" + item.getPlayerCnt().getDesc() + "</span></p>";
            msg += "        <p><b>⏰ 경기 일시:</b> " + item.getMatchDt() + " " + item.getMatchTm() + "</p>";
            msg += "        <br>";
            // 하이퍼링크 버튼 추가
            msg += "        <div style='text-align: left;'>";
            msg += "            <a href='" + matchUrl + "' style='background-color: #3498db; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;'>매치 확인하러 가기</a>";
            msg += "        </div>";
            msg += "    </div>";
            msg += "</div>";

            return msg;
        }
    },
    FREE_SUB{
        public String getSubject(final PlabMatchData item) {
            String subject = String.format("[상태변경] %s 서브 타입: %s",
                    item.getStadiumName(), item.getSubType().getDesc());

            return subject;
        }

        public String getContent(final PlabMatchData item) {
            String matchUrl = "https://www.plabfootball.com/match/" + item.getPlabMatchNo() + "/";
            String msg = "";
            msg += "<div style='margin:20px; font-family: sans-serif;'>";
            msg += "    <div style='background-color: #f0f7ff; padding: 20px; border-radius: 10px; border: 1px solid #d1e9ff;'>";
            msg += "        <h2 style='color: #2c3e50; margin-top: 0;'>[서브 활성화 알림]</h2>";
            msg += "        <p>매치의 서브 타입이 변경되었습니다.</p>";
            msg += "        <hr style='border: 0; border-top: 1px solid #e1f0ff;'>";
            msg += "        <p><b>📍 경기장:</b> " + item.getStadiumName() + "</p>";
            msg += "        <p><b>✨ 변경된 타입:</b> <span style='color: #007bff; font-size: 1.1em; font-weight: bold;'>" + item.getSubType().getDesc() + "</span></p>";
            msg += "        <p><b>⏰ 경기 일시:</b> " + item.getMatchDt() + " " + item.getMatchTm() + "</p>";
            msg += "        <br>";
            // 하이퍼링크 버튼 추가
            msg += "        <div align='center'>";
            msg += "            <a href='" + matchUrl + "' style='background-color: #007bff; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;'>혜택 확인하러 가기</a>";
            msg += "        </div>";
            msg += "    </div>";
            msg += "</div>";

            return msg;
        }
    }
}
