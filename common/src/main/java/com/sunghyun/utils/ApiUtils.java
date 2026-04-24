package com.sunghyun.utils;


import com.sunghyun.annotation.UpdateAble;
import com.sunghyun.web.exception.MergeException;
import com.sunghyun.web.ErrorCode;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ApiUtils {
    public static String getUUID(){
        return UUID.randomUUID().toString();
    }

    /**
     * IllegalAccessException을 RuntimeException인 MergeException으로 변환하여 던지는 래핑 메서드
     * 서비스 레이어에서 try-catch 없이 깔끔하게 사용할 수 있도록 지원한다.
     */
    public static <T> boolean merge(T source, T target) {
        try {
            return safeMerge(source, target);
        } catch (IllegalAccessException e) {
            // 이전에 정의하신 MergeException과 ErrorCode를 활용
            // ErrorCode.G01은 공통 시스템/리플렉션 에러로 가정
            throw new MergeException(ErrorCode.G01);
        }
    }

    /****
     * 자체 Util 클래스 안에 머지 메소드 구현
     * 두개의 머지가능한 오브젝트에서 @Merge 어노테이션을 활용해
     * 해당 필드들이 머지 가능한지 체크해서 source 의 머지가능한 값을 target 로 넣어준다.
     * @param source
     * @param target
     * @return
     * @throws IllegalAccessException
     */
    private static <T> boolean safeMerge(T source, T target) throws IllegalAccessException {
        //사전 검증 진행
        //두 클래스가 다를 경우 에러리턴
        if(!source.getClass().equals(target.getClass())){
            throw new MergeException(ErrorCode.G02);
        }

        //머지 여부 플래그
        boolean doMerge = false;

        //각 필드 선회
        for(Field field: source.getClass().getDeclaredFields()){
            //각 필드 값 가져올 수 있도록 세팅
            field.setAccessible(true);
            
            //각 source,target 필드에 해당하는 value 가져오기
            Object sourceValue = field.get(source);
            Object targetValue = field.get(target);

            boolean doUpdate = false;

            //UpdateAble 어노테이션
            Annotation annotation = field.getAnnotation(UpdateAble.class);

            //업데이트 대상이 아닌 경우 패스
            if(annotation==null) continue;

            //필드가 null 무시하는 경우
            if(((UpdateAble)annotation).ignoreNull()){
                if(canUpdate(sourceValue,targetValue)){
                    doUpdate = true;
                }

            }
            //필드가 null 무시하지 않는 경우
            else{
                if(canUpdateNull(sourceValue,targetValue)){
                    doUpdate = true;
                }
            }

            if(doUpdate){
                field.set(target,sourceValue);
                doMerge =true;
            }
        }

        return doMerge;
    }


    private static boolean canUpdate(Object sourceValue, Object targetValue){
        //요청값이 null이거나 기존값과 같은 경우 update하지 않는다.
        if(sourceValue == null || sourceValue.equals(targetValue)){
            return false;
        }

        return true;
    }

    private static boolean canUpdateNull(Object sourceValue, Object targetValue){
        // 요청값이 null인 경우
        if(sourceValue == null){
            //기존값이 null이 아닌 경우
            if(targetValue != null){
                //업데이트
                return true;
            }

            //null인 경우(==같은 경우)
            //업데이트 하지 않는다.
            return false;
        }

        //null이 아니면서 두 값이 다른 경우 업데이트
        return !sourceValue.equals(targetValue);
    }

    /**
     * ISO 8601 날짜 문자열에서 날짜(yyyyMMdd) 추출
     * 예: "2026-03-30T20:00:00+09:00" -> "20260330"
     */
    public static String parseDate(String schedule) {
        if (schedule == null || schedule.isBlank()) return "";
        return OffsetDateTime.parse(schedule)
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * ISO 8601 날짜 문자열에서 시간(HHmm) 추출
     * 예: "2026-03-30T20:00:00+09:00" -> "2000"
     */
    public static String parseTime(String schedule) {
        if (schedule == null || schedule.isBlank()) return "";
        return OffsetDateTime.parse(schedule)
                .format(DateTimeFormatter.ofPattern("HHmmss"));
    }

    /**
     * 현재 날짜를 yyyyMMdd 형식으로 반환
     * 예: 20260423
     */
    public static String getCurrentDt() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    /**
     * 현재 시간을 HHmmss 형식으로 반환
     * 예: 200151
     */
    public static String getCurrentTm() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
    }

    /**
     * ISO 8601 날짜 문자열이 현재 시간보다 이전인지 확인
     * @param schedule ISO 8601 형식의 문자열 (예: 2026-03-30T20:00:00+09:00)
     * @return 현재보다 이전이면 true, 미래면 false
     */
    public static boolean isPastSchedule(String schedule) {
        if (schedule == null || schedule.isBlank()) return true;

        OffsetDateTime matchTime = OffsetDateTime.parse(schedule);
        // 서버 시간대와 관계없이 정확한 비교를 위해 OffsetDateTime.now() 사용
        return matchTime.isBefore(OffsetDateTime.now());
    }
}
